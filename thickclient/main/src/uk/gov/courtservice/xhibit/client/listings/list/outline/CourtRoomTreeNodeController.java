package uk.gov.courtservice.xhibit.client.listings.list.outline;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.netbeans.swing.outline.Outline;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.services.listing.ListingsControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListComplexValue;
import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractDailyFirmListModel;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListModel;
import uk.gov.courtservice.xhibit.client.listings.list.sitting.SittingDialog;
import uk.gov.courtservice.xhibit.client.listings.list.sitting.SittingModel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * Controller for tree nodes that display court rooms.
 * 
 * @author uphillj
 * 
 * @amend groenmg - amended getChildModelType to be a SittingTreeNodeModel
 * @amend groenmg - add pop menu and dropChild functionality
 * @amend groenm -added validate methods. ctx-1186/7
 *
 */
public class CourtRoomTreeNodeController extends AbstractTreeNodeController<CourtRoomTreeNodeModel> {

	private static final long serialVersionUID = 1L;
	protected JPopupMenu courtRoomPopupMenu;
	
	protected XhbCourtRoomBasicValue courtRoomBeforeRefresh;
	protected List<SittingOnListComplexValue> listOfSittingsInRoomBeforeARefresh;
	private SittingOnListComplexValue sittingBeforeRefresh;
	private int noOfSittingsBeforeRefresh;
	private int noOfCasesBeforeRefresh;
	
	public CourtRoomTreeNodeController(TreeNodeFactory treeNodeFactory, XDialog listDialog, ListModel listModel, Outline listOutline, DefaultTreeModel listTreeModel) {
		super(treeNodeFactory, CourtRoomTreeNodeModel.class, listDialog, listModel, listOutline, listTreeModel);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Class<SittingTreeNodeModel> getChildModelType() {
		return SittingTreeNodeModel.class;
	}
	
	@Override
	public JPopupMenu getPopupMenu() {
		if (courtRoomPopupMenu == null) {
    		courtRoomPopupMenu = new JPopupMenu();
    		courtRoomPopupMenu.add(new CreateSittingAction());
    	}
    	return courtRoomPopupMenu;
	}
	
	@Override
	public void setOriginalValues(DefaultMutableTreeNode childNode) {
		// Get the original values for the court room we are moving to
		final AbstractDailyFirmListModel dailyFirmListModel = (AbstractDailyFirmListModel) this.getListModel();		
		courtRoomBeforeRefresh = this.getModel().getCourtRoom();
		listOfSittingsInRoomBeforeARefresh = dailyFirmListModel.getSittingsOnList(this.getModel().getListDate().getTime(), courtRoomBeforeRefresh.getCourtRoomId());
		// Get the original values for the sitting we are moving
		if (childNode != null) {
			final SittingTreeNodeController sittingController = (SittingTreeNodeController) OutlineUtils.getTreeNodeController(childNode);
			sittingBeforeRefresh = sittingController.getModel().getSittingOnList();
			noOfSittingsBeforeRefresh = getNumberOfNonObsoleteSittingsInList(listOfSittingsInRoomBeforeARefresh);
			noOfCasesBeforeRefresh = getNumberOfNonObsoleteCasesinList(sittingBeforeRefresh.getSittingOnListId());
		}
	}
	
	/**
	 * Validate that the court room we are moving to has not had any structural changes when refreshed before a drop.
	 */
	@Override
	public boolean validateStructure(DefaultMutableTreeNode childNode) {	
		boolean valid = false;

		final AbstractDailyFirmListModel dailyFirmListModel = (AbstractDailyFirmListModel) this.getListModel(); 
		final SittingTreeNodeController sittingController = (SittingTreeNodeController) OutlineUtils.getTreeNodeController(childNode); 
		final SittingOnListComplexValue sittingAfterRefresh = getSittingAfterRefresh(sittingBeforeRefresh.getTimeListed().getTime(), sittingBeforeRefresh);
				 
		// Validate Court Room Structure
		if (validateSittingOnListStructure(sittingBeforeRefresh, sittingAfterRefresh)) {
			
			// list of sittings on node dropping to - the courtRoom node
			List<SittingOnListComplexValue> listOfSittingOnCourtRoomDroppingToAfterRefresh = dailyFirmListModel.getSittingsOnList(this.getModel().getListDate().getTime(), this.getModel().getCourtRoom().getCourtRoomId());
			
			// ensure the room we are moving to has same number of sittings underneath it as it did before refresh
			if(noOfSittingsBeforeRefresh == getNumberOfNonObsoleteSittingsInList(listOfSittingOnCourtRoomDroppingToAfterRefresh)){
				// ensure the room we are moving to has same number of cases underneath it as it did before refresh
				if(noOfCasesBeforeRefresh == getNumberOfNonObsoleteCasesinList(sittingAfterRefresh.getSittingOnListId())){
					valid = true;
				}
			}
		}
		
		if(!valid){
			// if the structure has change in the part of the list the user is amending/dropping
			showInvalidStructureMsg();
			// Fire refresh of the tree from the list model in memory
			refreshOutline("validateStructure");
		} else {
			// NB Court room nodes are updated on entry to the screen
			
			// Update child with the latest version
			sittingController.getModel().setSittingOnList(sittingAfterRefresh);
		}
		return valid;
	}
	
	/**
	 * Method used to obtain the number of sittings in the list 
	 * that are not marked as obsolete.
	 * 
	 * @param sittingList
	 * @return number of sittings not that are not set to obs
	 */
	private int getNumberOfNonObsoleteSittingsInList(List<SittingOnListComplexValue> sittingList){
		int noOfSittings = 0;
		
		if(sittingList != null){
			for(SittingOnListComplexValue sitting : sittingList){
				if(!YES.equals(sitting.getObsInd())){
					noOfSittings++;
				}
			}
		}

		return noOfSittings;
	}
	
	@Override
	public String getControllerId() {
		if (this.getModel().getCourtRoom() != null &&
				this.getModel().getCourtRoom().getCourtRoomId() != null) {
			return getParentControllerId() + this.getModel().getCourtRoom().getCourtRoomId().toString();
		}
		return EMPTY_STRING;
	}

	@Override
	protected boolean dropChild(DefaultMutableTreeNode childNode, int index) {
		boolean dropChild = true;
		
		// Get the controller for the child sitting node being dropped
		final SittingTreeNodeController controller = (SittingTreeNodeController) OutlineUtils.getTreeNodeController(childNode);	
		SittingOnListComplexValue sittingOnList = controller.getModel().getSittingOnList();

		// Get the date of court room and the sittings's current date
		final Date courtRoomDate = this.getModel().getListDate().getTime();
		final Date sittingCurrentDate = sittingOnList.getTimeListedDate();

		// Remove the defendants from the child cases that are already listed if moving the sitting into
		// this date from another date on the list. Validation will have checked that some defendants can
		// be listed on all the child cases, but can only remove defendants from cases on successful drop
		if (!DateTimeUtilities.isDaySame(courtRoomDate, sittingCurrentDate)) {
			removeDefendantsForASitting(childNode);
		}

		// Move the sitting in the list model to this court room
		XhbCourtRoomBasicValue courtRoom = getModel().getCourtRoom();
		getListModel().moveSitting(sittingOnList, courtRoom, index + 1, courtRoomDate);

		return dropChild;
	}
	
	@Override
	protected boolean validateChild(DefaultMutableTreeNode childNode, int index) {
		boolean validChild = true;
		
		// Get the controller for the child case node being dropped
		final SittingTreeNodeController sittingController = (SittingTreeNodeController) OutlineUtils.getTreeNodeController(childNode);
		final SittingOnListComplexValue sittingOnList = sittingController.getModel().getSittingOnList();

		// Get the court site id and the sitting's current court site id
		final Integer courtSiteId = this.getModel().getCourtRoom().getCourtSiteId();
		final Integer sittingSiteId = sittingOnList.getCourtSiteId();

		// Get the court room id and the sitting's current court room id
		final Integer courtRoomId = this.getModel().getCourtRoom().getCourtRoomId();
		final Integer sittingRoomId = sittingOnList.getCourtRoomId();

		// Get the date of court room and the sittings's current date
		final Date courtRoomDate = this.getModel().getListDate().getTime();
		final Date sittingCurrentDate = sittingOnList.getTimeListedDate();

		// validation when changing court site of a sitting
		if (validChild && !courtSiteId.equals(sittingSiteId)) {
			// confirm that the change of court site is okay 
			validChild = displayMoveWarningDialog("validateChild-ChangeCourt",XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationChangeCourtSiteWithinListSittingMove"));
		}
		
		// validation when changing date of a sitting
		if (validChild && !DateTimeUtilities.isDaySame(courtRoomDate, sittingCurrentDate)) {
			if (childNode.getChildCount() > 0) {
				// need to validate child cases underneath the sitting			
				// validate fixture is moving to correct date
				if(validChild){
					validChild = validateFixtureDatesForASitting(childNode);						 		
				}

				// validate all cases not already listed underneath the sitting
				if (validChild) {
					validChild = validateCasesForASitting(childNode);	
				}

				// validate non available days for cases underneath the sitting
				if (validChild) {
					validChild = validateNonAvailableDaysForASitting(childNode);
				}
			}				
		}
		
		// validation when changing court room of a sitting
		if (validChild && !courtRoomId.equals(sittingRoomId)) {
			if (childNode.getChildCount() > 0) {
				// need to validate child cases underneath the sitting			
				// validate secure room requirement
				validChild = validateSecureCourtForASitting(childNode);	
				
				if(validChild){
					// validate video link room requirement
					validChild = validateVideoLinkForASitting(childNode);	
				}
			}				
		}
		
		return validChild;
	}	
	
	/**
	 * Validate all cases under sitting node for secure court room requirement.
	 * 
	 * @param childNode
	 * @return
	 */
	protected boolean validateSecureCourtForASitting(DefaultMutableTreeNode childNode) {
		boolean validChild = true;						
		
		// need to validate child cases underneath the sitting
		for(int i = 0; i < childNode.getChildCount(); i++){
			final DefaultMutableTreeNode caseNode = (DefaultMutableTreeNode)childNode.getChildAt(i);
			final CaseTreeNodeController caseController = (CaseTreeNodeController) OutlineUtils.getTreeNodeController(caseNode);
			final CaseOnListComplexValue caseOnList = caseController.getModel().getCaseOnList();
			final XhbCourtRoomBasicValue room = this.getModel().getCourtRoom();
			
			// validate secure room requirement
			String secureCourtRoom = caseOnList.getCase().getSecureCourt();
			if(YES.equals(secureCourtRoom)){
				// check if court room is secure
				String secureRoom = room.getSecurityInd();
				if(!YES.equals(secureRoom)){
					validChild = displayMoveWarningDialog("validateSecureCourtForASitting",XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationSecureRoomSittingMove"));		
					//shown message once, so stop
					break;
				}
			}	
		}
		
		return validChild;
	}	
	
	/**
	 * Validate all cases under sitting node for video link requirement.
	 * 
	 * @param childNode
	 * @return
	 */
	protected boolean validateVideoLinkForASitting(DefaultMutableTreeNode childNode) {
		boolean validChild = true;			 			
		
		// need to validate child cases underneath the sitting
		for(int i = 0; i < childNode.getChildCount(); i++){
			final DefaultMutableTreeNode caseNode = (DefaultMutableTreeNode)childNode.getChildAt(i);
			final CaseTreeNodeController caseController = (CaseTreeNodeController) OutlineUtils.getTreeNodeController(caseNode);
			final CaseOnListComplexValue caseOnList = caseController.getModel().getCaseOnList();
			final XhbCourtRoomBasicValue room = this.getModel().getCourtRoom();
			
			// validate video link room requirement
			String requireVideoLinkCourtRoom = caseOnList.getCase().getVideoLinkRequired();
			if(YES.equals(requireVideoLinkCourtRoom)){
				// check if court room has video link
				String videoRoom = room.getVideoInd();
				if(!YES.equals(videoRoom)){
					validChild = displayMoveWarningDialog("validateVideoLinkForASitting",XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationVideoLinkSittingMove"));		
					//shown message once, so stop
					break;
				}
			}	
		}
		
		return validChild;
	}	
	
	/**
	 * Validate all cases under sitting node to see whether cases can be listed.
	 * 
	 * @param childNode
	 * @return
	 */
	protected boolean validateCasesForASitting(DefaultMutableTreeNode childNode) {
		boolean validChild = true;			 			

		// date of court room, i.e. new date for the sitting
		final Date courtRoomDate = this.getModel().getListDate().getTime();
		
		// need to validate child cases underneath the sitting
		for(int i = 0; i < childNode.getChildCount(); i++){
			final DefaultMutableTreeNode caseNode = (DefaultMutableTreeNode)childNode.getChildAt(i);
			final CaseTreeNodeController caseController = (CaseTreeNodeController) OutlineUtils.getTreeNodeController(caseNode);
			final CaseOnListComplexValue caseOnList = caseController.getModel().getCaseOnList();

			// Create error message from case that will be displayed if validation fails
			String errorMsg = MessageFormat.format(XHIBITConstant.getResource(XhibitBundles.Listings, 
					"listValidationDefendantSittingMove"), new Object[] { getDisplayCaseNumber(caseOnList.getCase()) });

			// validate if case already listed
			if(isMainCaseType(caseOnList.getCase().getCaseType())){
				validChild = validateDefendants(caseOnList, courtRoomDate, errorMsg);
			}
			else{
				validChild = validateCaseListed(caseOnList, courtRoomDate, errorMsg);
			}
			
			// Break loop on first failure as cannot drop sitting
			if (!validChild) {
				break;
			}
		}
		
		return validChild;
	}

	/**
	 * Validate all cases under sitting node to see whether there are non-available days.
	 * 
	 * @param childNode
	 * @return
	 */
	protected boolean validateNonAvailableDaysForASitting(DefaultMutableTreeNode childNode) {
		boolean validChild = true;			 			

		// date of court room, i.e. new date for the sitting
		final Date courtRoomDate = this.getModel().getListDate().getTime();
		
		// need to validate child cases underneath the sitting
		for(int i = 0; i < childNode.getChildCount(); i++){
			final DefaultMutableTreeNode caseNode = (DefaultMutableTreeNode)childNode.getChildAt(i);
			final CaseTreeNodeController caseController = (CaseTreeNodeController) OutlineUtils.getTreeNodeController(caseNode);
			final CaseOnListComplexValue caseOnList = caseController.getModel().getCaseOnList();

			// if the child case has a non-available day for this date,
			// ask the user to confirm that they want to move sitting
			try {
				if (XhibitDelegateHelper.getListingsDelegate().isCaseNonAvailDay(caseOnList.getCaseId(), courtRoomDate)) {
					validChild = displayMoveWarningDialog("validateNonAvailableDaysForASitting",
							XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationNonAvailableSittingMove"));
					//shown message once, so stop
					break;
				}
			} catch (ListingsControllerException e) {
				// Re-throw as runtime to cancel drop as unable to check days
				throw new CSUnrecoverableException(e);
			}
		}

		return validChild;
	}

	/**
	 * Validate all cases under sitting node to see whether there are non-available days.
	 * 
	 * @param childNode
	 * @return
	 */
	protected boolean validateFixtureDatesForASitting(DefaultMutableTreeNode childNode) {
		boolean validChild = true;			 			

		// date of court room, i.e. new date for the sitting
		final Date courtRoomDate = this.getModel().getListDate().getTime();
		
		// need to validate child cases underneath the sitting
		for(int i = 0; i < childNode.getChildCount(); i++){
			final DefaultMutableTreeNode caseNode = (DefaultMutableTreeNode)childNode.getChildAt(i);
			final CaseTreeNodeController caseController = (CaseTreeNodeController) OutlineUtils.getTreeNodeController(caseNode);
			final CaseOnListComplexValue caseOnList = caseController.getModel().getCaseOnList();

			// if the child case was from a fixture for a different date,
			// ask the user to confirm that they want to move sitting
			try {
				if (caseOnList.getCaseDiaryFixtureId() != null) {
					CaseDiaryFixtureComplexValue fixture = XhibitDelegateHelper.getListingsDelegate().findCaseDiaryFixture(caseOnList.getCaseDiaryFixtureId());
					if (!DateTimeUtilities.isDaySame(fixture.getListingDate(), courtRoomDate)) {
						validChild = displayMoveWarningDialog("validateFixtureDatesForASitting",XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationFixtureDateSittingMove"));
						//shown message once, so stop
						break;
					}
				}
			} catch (ListingsControllerException e) {
				// Re-throw as runtime to cancel drop as unable to check days
				throw new CSUnrecoverableException(e);
			}
		}

		return validChild;
	}
	
	/**
	 * Remove defendants from all cases under sitting node that are already listed.
	 * 
	 * @param childNode
	 */
	protected void removeDefendantsForASitting(DefaultMutableTreeNode childNode) {
		// date of court room, i.e. new date for the sitting
		final Date courtRoomDate = this.getModel().getListDate().getTime();
		
		// need to check all child cases underneath the sitting
		for(int i = 0; i < childNode.getChildCount(); i++){
			final DefaultMutableTreeNode caseNode = (DefaultMutableTreeNode)childNode.getChildAt(i);
			final CaseTreeNodeController caseController = (CaseTreeNodeController) OutlineUtils.getTreeNodeController(caseNode);
			final CaseOnListComplexValue caseOnList = caseController.getModel().getCaseOnList();

			// remove the defendants from the case that are already listed
			for (DefOnCaseOnListBasicValue defOnCaseOnList : getDefendantsListed(caseOnList, courtRoomDate)) {
				getListModel().removeDefendant(caseOnList, defOnCaseOnList.getDefendantOnCaseId());
			}
		}
	}
	
	protected class CreateSittingAction extends ReloadCourtRoomAction {

        private static final long serialVersionUID = 1L;

        public CreateSittingAction() {
            super("ListingCreateSitting");
        }

        @Override
        protected void doAction() throws Exception {
        	// get the court room node and the next sitting number 
        	DefaultMutableTreeNode courtRoomNode = CourtRoomTreeNodeController.this.getTreeNode();
        	Integer sittingNumber = courtRoomNode.getChildCount() + 1;

        	// create the sitting on list basic value from the list model
        	SittingOnListComplexValue sitting = getListModel().createSitting(CourtRoomTreeNodeController.this.getModel().getCourtRoom(), 
																			sittingNumber, 
																			CourtRoomTreeNodeController.this.getModel().getListDate().getTime());
        	SittingModel sittingModel = createSittingModel(sitting);

        	// open the sitting dialog
        	SittingDialog sittingDialog = new SittingDialog(getListDialog(), sittingModel, getListModel());
			sittingDialog.setVisible(true);
			
			// now create the new sitting node if saved otherwise remove from list model
			if(sittingModel.isDirty()){
				// Save the sitting
				save("CreateSittingAction");
				// Open the first sitting
				if (getOutline().getSelectedRow() > -1) {
					courtRoomNode = OutlineUtils.getTreeNode(getOutline(), getOutline().getSelectedRow());
					if (courtRoomNode.getChildCount() > 0) {
						DefaultMutableTreeNode firstChild = (DefaultMutableTreeNode)courtRoomNode.getChildAt(0);
						getOutline().expandPath(new TreePath(firstChild.getPath()));
					}
				}
			} else {
				getListModel().deleteSitting(sitting);
			}
        }

        /**
         * Returns a SittingModel populated ready for use by the sitting dialog.
         * 
         * @param sitting SittingOnListComplexValue to be populated
         * @return SittingModel
         */
		private SittingModel createSittingModel(SittingOnListComplexValue sitting) {        	
			SittingModel sittingModel = new SittingModel();
			sittingModel.setModelType(SittingModel.ModelType.NEW);
        	sittingModel.setXac(getListModel().getXac());
        	sittingModel.setSitting(sitting);
			return sittingModel;
		}
    }

	private abstract class ReloadCourtRoomAction extends ReloadObjectAction {
		private static final long serialVersionUID = 1L;

		public ReloadCourtRoomAction(String actionName) {
			super(actionName, "ReloadCourtRoomAction");
		}

		@Override
		protected void getLatestValues() {
			setOriginalValues(null);
			refreshDBOnly("ReloadCourtRoomAction");
		}

		@Override
		protected boolean isValid() {
			XhbCourtRoomBasicValue courtRoomAfterRefresh = CourtRoomTreeNodeController.this.getModel().getCourtRoom();
			boolean valid = validateCourtRoomStructure(courtRoomBeforeRefresh, courtRoomAfterRefresh);
			return valid;
		}
	}
}
