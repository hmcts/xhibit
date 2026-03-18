package uk.gov.courtservice.xhibit.client.listings.list.outline;

import java.awt.datatransfer.Transferable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.netbeans.swing.outline.Outline;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeTicketBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListComplexValue;
import uk.gov.courtservice.xhibit.client.listings.CaseOnListType;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractDailyFirmListModel;
import uk.gov.courtservice.xhibit.client.listings.list.common.CourtListingTransferableSitting;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListModel;
import uk.gov.courtservice.xhibit.client.listings.list.sitting.SittingDialog;
import uk.gov.courtservice.xhibit.client.listings.list.sitting.SittingModel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * Controller for tree nodes that display sittings.
 * 
 * @author uphillj
 * @history groenmg - added pop menu and dropChild() functionality
 * @history groenmg - added validateChild() functionality as a proof
 * 					  and amended dropChild() accordingly
 * @amend groenmg -added validate methods. ctx-1186/7
 * @amend groenmg - ctx-1789 - added validation for in complete case
 * @amend groenmg - ctx-1182 - added functionality to check if hearing 
 * 					type on case when dragging - if not display additional details dialog.
 * 
 * @amend groenmg - ctx-1189 - added validation when a case is closed
 * @amend groenmg - ctx-1190 - added validation for bench warrants on a case
 * @amend groenmg - ctx-1188 - added validation for Court Site
 * 
 */
public class SittingTreeNodeController extends AbstractTreeNodeController<SittingTreeNodeModel> {

	private static final long serialVersionUID = 1L;
	
	protected final static String HIGH_COURT_JUDGE = "HJ";
	protected final static String T_TYPE_CASE = "T";	
	
	protected JPopupMenu sittingPopupMenu;	
	
	protected SittingOnListComplexValue sittingCVBeforeRefresh;
	protected List<CaseOnListComplexValue> listOfCasesOnSittingBeforeARefresh;

	public SittingTreeNodeController(TreeNodeFactory treeNodeFactory, XDialog listDialog, ListModel listModel, Outline listOutline, DefaultTreeModel listTreeModel) {
		super(treeNodeFactory, SittingTreeNodeModel.class, listDialog, listModel, listOutline, listTreeModel);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class<CaseTreeNodeModel> getChildModelType() {
		return CaseTreeNodeModel.class;
	}
	
	@Override
	public boolean isDragSupported() {
		return true;
	}
	
	@Override
	public Transferable createTransferable() {
		return new CourtListingTransferableSitting(getTreeNodeFactory(), getTreeNode());
	}
	
	@Override
	public JPopupMenu getPopupMenu() {
		if (sittingPopupMenu == null) {     
    		sittingPopupMenu = new JPopupMenu();
    		sittingPopupMenu.add(new AmendSittingAction());
    		sittingPopupMenu.add(new DeleteSittingAction());
    	}
    	return sittingPopupMenu;
	}
	
	@Override
	protected boolean dropChild(DefaultMutableTreeNode childNode, int index) {
		boolean dropChild = true;
		
		// Get the controller for the child case node being dropped
		final CaseTreeNodeController caseController = (CaseTreeNodeController) OutlineUtils.getTreeNodeController(childNode);
		final CaseOnListComplexValue caseOnList = caseController.getModel().getCaseOnList();
		

		// Get the date of sitting and the case's current date
		final Date sittingDate = getModel().getSittingOnList().getTimeListedDate();
		final Date caseCurrentDate = caseOnList.getTimeListedDate();
		
		// Update list model with either new case or moved case
		if(caseOnList.getCaseOnListId() == null){
			// adding the case to the list
			final CaseOnListComplexValue newCaseOnList = getListModel().addCase(caseOnList, caseOnList.getCase(), caseOnList.getCaseListingEntry(), caseOnList.getDirectionsForCase(),
																				caseOnList.getHearingType(), CaseOnListType.Sitting, index + 1, getModel().getSittingOnList());

			// add the defendants to the case that are not already listed
			for (DefOnCaseOnListBasicValue defOnCaseOnList : getDefendantsNotListed(caseOnList, sittingDate)) {
				getListModel().addDefendant(newCaseOnList, defOnCaseOnList.getDefendantOnCaseId());
			}

			// Update node model with newly added case on list
			caseController.getModel().setCaseOnList(newCaseOnList);
			
			// If the case has no Hearing Type, present the user with the additional details dialog
			// NB this is the final check to take place as part of a drag and drop
			dropChild = checkAdditionalDetails(newCaseOnList);
		}
		else{
			// if changing date, which excludes moving from Reserve as cases there are already
			// valid for all dates, remove the defendants from the case that are already listed
			if (!YES.equals(caseOnList.getReserved()) && !DateTimeUtilities.isDaySame(sittingDate, caseCurrentDate)) {
				for (DefOnCaseOnListBasicValue defOnCaseOnList : getDefendantsListed(caseOnList, sittingDate)) {
					getListModel().removeDefendant(caseOnList, defOnCaseOnList.getDefendantOnCaseId());
				}
			}

			// move the case in the list model to this sitting
			getListModel().moveCase(caseOnList, CaseOnListType.Sitting, index + 1, getModel().getSittingOnList());
		}
		
		return dropChild;
	}
	
	@Override
	public void setOriginalValues(DefaultMutableTreeNode childNode) {
		final AbstractDailyFirmListModel dailyFirmListModel = (AbstractDailyFirmListModel) this.getListModel(); 
		sittingCVBeforeRefresh = this.getModel().getSittingOnList();
		listOfCasesOnSittingBeforeARefresh = dailyFirmListModel.getCasesOnListForSitting(sittingCVBeforeRefresh.getSittingOnListId());			
	}
	
	/**
	 * Validate that the sitting we are moving to has not had any structural changes when refreshed before a drop.
	 */
	@Override
	public boolean validateStructure(DefaultMutableTreeNode childNode) {	
		boolean valid = false;

		final AbstractDailyFirmListModel dailyFirmListModel = (AbstractDailyFirmListModel) this.getListModel(); 
		final CaseTreeNodeController caseController = (CaseTreeNodeController) OutlineUtils.getTreeNodeController(childNode);
		final CaseOnListComplexValue caseBeforeRefresh = caseController.getModel().getCaseOnList();
		final CaseOnListComplexValue caseAfterRefresh = getCaseAfterRefresh(caseBeforeRefresh);			
		final SittingOnListComplexValue sittingAfterRefresh = getSittingAfterRefresh(sittingCVBeforeRefresh.getTimeListedDate(), sittingCVBeforeRefresh);
		
		// Validate Sitting Structure
		if(validateSittingOnListStructure(sittingCVBeforeRefresh ,sittingAfterRefresh)){
			//ensure still same number of cases under sitting before drop					
			if(getNumberOfNonObsoleteCasesinList(listOfCasesOnSittingBeforeARefresh) == getNumberOfNonObsoleteCasesinList(sittingAfterRefresh.getSittingOnListId())){
				valid = true;
			}
		}
		
		// Validate Case Structure
		if (valid) {
			// reset the flag ready for the next structural checks
			valid = false;
			if(caseBeforeRefresh != null){
				if (validateCaseOnListStructure(caseBeforeRefresh, caseAfterRefresh)) {
					// list of cases on node dropping to - the sitting node
					List<CaseOnListComplexValue> listOfCasesOnSittingDroppingToAfterRefresh = dailyFirmListModel.getCasesOnListForSitting(this.getModel().getSittingOnList().getSittingOnListId());
					
					// Check number of cases not changed to where we dropping since data refreshed.
					if(getNumberOfNonObsoleteCasesinList(listOfCasesOnSittingDroppingToAfterRefresh) == getNumberOfNonObsoleteCasesinList(listOfCasesOnSittingBeforeARefresh)){	
						valid = true;
					}
				}
			}
		}
	
		if(!valid){
			// if the structure has change in the part of the list the user is amending/dropping
			showInvalidStructureMsg();
    		// Fire refresh of the tree from the list model in memory
    		refreshOutline("validateStructure");
		} else {
			// Update itself with the latest version
			getModel().setSittingOnList(sittingAfterRefresh);
			
			// Update child with the latest version
			caseController.setControllerCaseFromListModel(caseController.getModel().getCaseOnList().getCaseOnListId());
		}
		return valid;
	}
	
	@Override
	public String getControllerId() {
		if (this.getModel().getSittingOnList() != null && 
				this.getModel().getSittingOnList().getSittingOnListId() != null) {
			return this.getModel().getSittingOnList().getSittingOnListId().toString();
		} 
		return EMPTY_STRING;
	}

	@Override
	protected boolean validateChild(DefaultMutableTreeNode childNode, int index) {
		boolean validChild = super.validateChild(childNode, index);

		// Get the controller for the child case node being dropped
		final CaseTreeNodeController caseController = (CaseTreeNodeController) OutlineUtils.getTreeNodeController(childNode);
		final CaseOnListComplexValue caseOnList = caseController.getModel().getCaseOnList();

		// Get the date of sitting and the case's current date
		final Date sittingDate = this.getModel().getSittingOnList().getTimeListedDate();
		final Date caseCurrentDate = caseOnList.getTimeListedDate();
		
		// Get the court site id and the case's current court site id
		final Integer courtSiteId = this.getModel().getSittingOnList().getCourtSiteId();
		final Integer caseCurrentSiteId = caseOnList.getCourtSiteId();
		
		// Get the court room for this sitting and the case's current court room
		final Integer courtRoomId = this.getModel().getSittingOnList().getCourtRoomId();
		final Integer caseCurrentRoomId = caseOnList.getCourtRoomId();
		
		// Get the sitting on list id for this sitting and the case's current sitting
		final Integer sittingOnListId = this.getModel().getSittingOnList().getSittingOnListId();
		final Integer caseSittingOnListId = caseOnList.getSittingOnListId();
		final CaseDiaryFixtureComplexValue fixture = getFixtureAfterRefresh(caseOnList.getCaseDiaryFixtureId()); 
					
		// check if moving from LHS so do only LHS to RHS specific validation	
		// one of first checks
		if (validChild && caseOnList.getCaseOnListId() == null ) {
			// validate the fixture is still as-is
			if (validChild && caseOnList.getCaseDiaryFixtureId() != null) {
				validChild = validateStateOfFixture(fixture);
			}
			
			// moving from LHS onto the list
			// this validation of case state will be done first as could result in the user having no choice and having to cancel the drop
			if (validChild) {
				validChild = validateStateOfCase(caseOnList.getCase().getCaseId());
			}
		}

		// validation when changing court site, which includes drop from LHS as no court site when adding
		if (validChild && !courtSiteId.equals(caseCurrentSiteId)) {
			validChild = validateCourtSite(caseOnList, courtSiteId, caseCurrentSiteId);
		}
		
		// validation when changing date, which includes drop from LHS as no date when adding
		// but excludes dropping from Reserve as cases there are already valid for all dates
		if (validChild && !YES.equals(caseOnList.getReserved()) && !DateTimeUtilities.isDaySame(sittingDate, caseCurrentDate)) {
			// validate fixture is moving to correct date
			if(validChild && caseOnList.getCaseDiaryFixtureId() != null){
				validChild = validateFixtureDate(fixture, sittingDate);
			}
			
			// validate if case already listed
			if(validChild){
				if(isMainCaseType(caseOnList.getCase().getCaseType())){
					validChild = validateDefendants(caseOnList, sittingDate, XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationDefendantCaseMove"));
				}
				else{
					validChild = validateCaseListed(caseOnList, sittingDate, XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationDefendantCaseMove"));
				}
			}		

			// validate non available days for case
			if(validChild){
				validChild = validateNonAvailableDays(caseOnList.getCaseId(), sittingDate);
			}		
		}
		
		// validation when changing court room, which includes drop from LHS as no court room when adding
		if(validChild && !courtRoomId.equals(caseCurrentRoomId)){
					
			DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) this.getTreeNode().getParent();
			final CourtRoomTreeNodeController courtRoomController = (CourtRoomTreeNodeController) OutlineUtils.getTreeNodeController(parentNode);
			XhbCourtRoomBasicValue room = courtRoomController.getModel().getCourtRoom();
			
			// validate secure room requirement
			if(validChild){
				validChild = validateCourtRoom(caseOnList.getCase().getSecureCourt(), room.getSecurityInd(), XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationSecureRoomCaseMove"));			
			}			
			
			// validate video link requirement
			if(validChild){
				validChild = validateCourtRoom(caseOnList.getCase().getVideoLinkRequired(), room.getVideoInd(), XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationVideoLinkCaseMove"));			
			}
		}			
		
		// validation when changing sitting, which includes drop from LHS as no sitting when adding
		if (validChild && !sittingOnListId.equals(caseSittingOnListId)) {
			// Validate the required judge
			validChild = validateJudge(caseOnList);
		}
		
		if (validChild && caseOnList.getCaseOnListId() == null ) {
			// one of last checks and only required for moving from LHS to RHS
			// Need to inform the user if not all defendants on the case are on the caseOnList being moved
			warnIfNotAllCaseDefsBeingListed(caseOnList);
		}	
		
		// If not valid then refresh the display with latest model
		if (!validChild) {
			refreshOutlineOnlyOnce("ValidateChild");
		}

		return validChild;
	}

	

	/**
	 * Validates that if the sitting judge does not match the required judge
	 * for the case and returns true if there is no difference or the user
	 * has confirmed it is okay to continue with the drop.
	 * 
	 * @param caseOnList
	 * @return true if okay to continue
	 */
	private boolean validateJudge(CaseOnListComplexValue caseOnList) {
		boolean validJudge = true;

		// Get the judge that is on this sitting
		Integer sittingJudge = getModel().getSittingOnList().getJudgeRefId();
		
		// Validate whether the case requires a required Judge on the sitting dropping to
		// Get the judge that is required for the case
		Integer requiredJudge = null;
		if (caseOnList.getCaseListingEntry() != null) { 
			requiredJudge = caseOnList.getCaseListingEntry().getJudgeId();
		}
		
		// If there is no judge required by the case,
		// then the required judge validation passes as there can be no conflict
		if (requiredJudge == null) {
			validJudge = true;
		}
		// Else if sitting and required judge on case do not match or there is no judge assigned to the sitting
		else if (!requiredJudge.equals(sittingJudge)) {
			validJudge = displayMoveWarningDialog("validateJudge",XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationRequiredJudgeNotMatchCaseMove"));
				
		}

		// Validate whether the case requires a High Court Judge on the sitting dropping to
		if(validJudge){
			validJudge = validateHighCourtJudge(caseOnList);
		}
		
		// Validate whether the case ticket type corresponds to a sitting judge ticket type.
		if(validJudge){
			validJudge = validateJudgeTicketType(caseOnList);
		}
				
		return validJudge;
	}
	
	/**
	 * validates whether a case requires a sitting to have a high court judge and
	 * warns the user if so
	 * @param caseOnList 
	 * @param sittingJudge 
	 * 
	 * @return true if valid
	 */
	private boolean validateHighCourtJudge(CaseOnListComplexValue caseOnList) {
		boolean validJudge = true;
		
		if( !isHighCourtJudgeValidForCaseJudgeType(caseOnList) ){
			validJudge = displayMoveWarningDialog("validateHighCourtJudge",XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationRequiredHighCourtJudgeNotMatchCaseMove"));
		}
		
		return validJudge;
	}

	/**
	 * Validates whether a case requires a sitting to have a High Court Judge assigned.
	 * @param caseOnList
	 * @return true if valid
	 */
	private boolean isHighCourtJudgeValidForCaseJudgeType(CaseOnListComplexValue caseOnList) {
		boolean highCourtJudgeValid = true;
		
		if (caseOnList.getCaseListingEntry() != null) { 
			if(caseOnList.getCaseListingEntry().getRefJudgeTypeId() != null){
				ArrayList<RefSystemCodeBasicValue> judgeTypes = ListingDropdownPopulation.getRefJudgeTypes();
				//check judge type
				for(RefSystemCodeBasicValue refCode : judgeTypes){
					if(refCode.getId().equals(caseOnList.getCaseListingEntry().getRefJudgeTypeId())){
						if(refCode.getCode().equals(HIGH_COURT_JUDGE)){
							// case needs a high court Judge
							// check to see if the sitting has an assigned judge and if it is a High Court Judge
							if (getModel().getSittingOnList().getJudgeRefId() == null || !getModel().getSittingOnList().getRefJudge().getJudgeType().equals(HIGH_COURT_JUDGE)){
								highCourtJudgeValid = false;
							}
						}
						break;
					}
				}
			}
		}
				
		return highCourtJudgeValid;
	}
	
	/**
	 * validates whether a case requires a sitting to have a specific ticket type and
	 * warns the user if so
	 * @param caseOnList 
	 * @param sittingJudge 
	 * 
	 * @return true if valid
	 */
	private boolean validateJudgeTicketType(CaseOnListComplexValue caseOnList) {
		boolean validJudge = true;
	
		Integer caseTicketTypeCode = caseOnList.getCase().getTicketTypeCode();
		if(caseTicketTypeCode != null){
			// retrieve list of the ticket types to validate against
			ArrayList<RefSystemCodeBasicValue> judgeRefTicketTypes = ListingDropdownPopulation.getRefJudgeTicketTypes();
			
			//check if the case ticket type is a judge ticket type
			for(RefSystemCodeBasicValue refCode : judgeRefTicketTypes){
				if(refCode.getId().equals(caseTicketTypeCode)){
					// case ticket type is a judge ticket type (JUDGE_TICKET) - now validate if match any assigned to the judge on the sitting
					boolean caseAndSittingTicketTypeMatch = false;
					// get list of ticket types associated with the judge on the sitting
					if(getModel().getSittingOnList().getJudgeRefId() != null){					
						ArrayList<RefJudgeTicketBasicValue> sittingRefJudgeTicketBasicValues = (ArrayList<RefJudgeTicketBasicValue>) getModel().getSittingOnList().getRefJudge().getRefJudgeTickets();
						if(sittingRefJudgeTicketBasicValues != null && !sittingRefJudgeTicketBasicValues.isEmpty()){
							// check if the sitting judge's ticket type matches the case ticket type
							for(RefJudgeTicketBasicValue sittingTicket : sittingRefJudgeTicketBasicValues){
								if(sittingTicket.getTicketType().equals(refCode.getCode())){
									caseAndSittingTicketTypeMatch = true;
									break;
								}
							}
						}
					}
										
					// display warning message if the case judge ticket type does not match one of the sitting judge's ticket type OR
					// there is no judge assigned to the sitting.
					if (!caseAndSittingTicketTypeMatch || getModel().getSittingOnList().getJudgeRefId() == null){
						// Create error message from case that will be displayed if validation fails
						String errorMsg = MessageFormat.format(XHIBITConstant.getResource(XhibitBundles.Listings, 
								"listValidationRequiredTicketTypeNotMatchCaseMove"), new Object[] { refCode.getCode()});						
						validJudge = displayMoveWarningDialog("validateJudgeTicketType",errorMsg);
					}
					break;
				}
			}
		}
		
		return validJudge;
	}
	
	/**
	 * Returns the latest version of a sitting.
	 * 
	 * @param sittingOnListId
	 * @return
	 */
	protected SittingOnListComplexValue getLatestValueFromModel(Integer sittingOnListId){
		Collection<SittingOnListComplexValue> refreshedValues = getListModel().getSittingsOnList();
		SittingOnListComplexValue refreshedValue = null;
		for(SittingOnListComplexValue latestValue : refreshedValues){
			if(latestValue.getSittingOnListId().equals(sittingOnListId)){
				refreshedValue = latestValue;
			}
		}
		return refreshedValue;
	}
	
	/**
	 * Set the sittingOnList to the sittingOnList from the list model
	 * 
	 * @param sittingOnListId
	 */
	protected void setControllerSittingFromListModel(Integer sittingOnListId) {
		if (sittingOnListId != null) {
    		SittingOnListComplexValue sittingOnList = getLatestValueFromModel(sittingOnListId);
    		SittingTreeNodeController.this.getModel().setSittingOnList(sittingOnList);
		}
	}

	/**
	 * Shows the amend sitting dialog and returns true if the user has
	 * clicked the save button to close the dialog, rather than cancel
	 * 
	 * @return true if sitting saved
	 * @throws CSRecoverableException
	 */
	private boolean amendSitting() throws CSRecoverableException {
		// Create SittingModel populated for use by the sitting dialog
		SittingModel sittingModel = new SittingModel();
		sittingModel.setModelType(SittingModel.ModelType.EDIT);
    	sittingModel.setXac(getListModel().getXac());
    	sittingModel.setSitting(SittingTreeNodeController.this.getModel().getSittingOnList());
    	
    	// open the sitting dialog
    	SittingDialog sittingDialog = new SittingDialog(getListDialog(), sittingModel, getListModel());
		sittingDialog.setVisible(true);
		
		// update the sitting details in the row if saved
		if (sittingModel.isDirty()){
			save("AmendSitting");
		}
		
		// Return true if sitting was saved
		return sittingModel.isDirty();
	}	
	
	private class AmendSittingAction extends ReloadSittingAction {

		private static final long serialVersionUID = 1L;

	    public AmendSittingAction() {
	        super("ListingAmendSitting");
	    }
	
	    @Override
	    protected void doAction() throws Exception {
			amendSitting();			
	    }	
    }

	private class DeleteSittingAction extends ReloadSittingAction { 

        private static final long serialVersionUID = 1L;

        public DeleteSittingAction() {
            super("ListingDeleteSitting");
        }

        @Override
        protected void doAction() throws Exception {
        	// Final check to make sure no-ones added a case before we delete
			SittingOnListComplexValue sittingOnListAfterRefresh = SittingTreeNodeController.this.getModel().getSittingOnList();        		
    		boolean valid = Integer.valueOf(getNumberOfNonObsoleteCasesinList(sittingOnListAfterRefresh.getSittingOnListId())).equals(0);
        	if (!valid){
        		// if there are cases under the sitting cannot delete the sitting
        		XMessageBox.alert	(getListDialog(),
    								XHIBITConstant.getResource(XhibitBundles.Listings, "ErrorSaveTitle"), 
    								true,
    								XMessageBox.ICONERROR, 
    								XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingSittingDeleteCaseExistError"),
    								XMessageBox.OK_ONLY, 
    								XMessageBox.DEFAULTOK);
        	}
        	else{
        		deleteSitting();
        	}
        }
        
        private void deleteSitting() throws CSRecoverableException {        	
    		boolean messageBoxReply = XMessageBox.alert(getListDialog(), 
    													XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingSittingDeleteConfirmationTitle"), 
    													true,
    													XMessageBox.ICONQUESTION, 
    													XHIBITConstant.getResource(XhibitBundles.Listings, "caseListingSittingDeleteConfirmation"), 
    													XMessageBox.YESNO,
    													XMessageBox.DEFAULTNO);
    		
      		if (messageBoxReply) {
      			getListModel().deleteSitting(SittingTreeNodeController.this.getModel().getSittingOnList());
      			save("DeleteSitting");
    		}
    	}		
    }
	
	private abstract class ReloadSittingAction extends ReloadObjectAction {
		private static final long serialVersionUID = 1L;

		public ReloadSittingAction(String actionName) {
			super(actionName, "ReloadSittingAction");
		}

		@Override
		protected void getLatestValues() {
			setOriginalValues(null);
			final Integer sittingOnListId = sittingCVBeforeRefresh.getSittingOnListId();
			refreshDBOnly("ReloadSittingAction");
			setControllerSittingFromListModel(sittingOnListId);
		}

		@Override
		protected boolean isValid() {
			SittingOnListComplexValue sittingOnListAfterRefresh = SittingTreeNodeController.this.getModel().getSittingOnList();
			boolean valid = validateSittingOnListStructure(sittingCVBeforeRefresh, sittingOnListAfterRefresh);
			return valid;
		}		
	}
}
