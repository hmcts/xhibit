package uk.gov.courtservice.xhibit.client.listings.list.outline;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.activation.ActivationDataFlavor;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.netbeans.swing.outline.Outline;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.services.caze.CaseStatusIndicator;
import uk.gov.courtservice.xhibit.business.services.listing.ListingsControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtSiteCriteria;
import uk.gov.courtservice.xhibit.client.listings.ListTypeEnum;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.listings.list.additional.AdditionalDetailsDialog;
import uk.gov.courtservice.xhibit.client.listings.list.additional.AdditionalDetailsModel;
import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractDailyFirmListModel;
import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractMainListingPanel;
import uk.gov.courtservice.xhibit.client.listings.list.common.CourtListingOutline;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListModel;
import uk.gov.courtservice.xhibit.client.listings.list.warned.WarnedListModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * Base class for all tree node controllers used to perform tree node specific
 * processing.
 * 
 * @author uphillj
 * @amend groenm -added dropChild() and getPopupMenu()
 * @amend groenm -added validate methods. ctx-1186/7
 * @amend groenmg - ctx-1789 - added validation for in complete case
 * @amend groenmg - ctx-1182 - added functionality to check if hearing 
 * 					type on case when dragging - if not display additional details dialog.
 * @amend groenmg - ctx-1189 - added validation when a case is closed
 * @amend groenmg - ctx-1190 - added validation for bench warrants on a case
 * @amend groenmg - ctx-1188 - added validation for Court Site
 * 
 * 
 */
public abstract class AbstractTreeNodeController<T extends TreeNodeModel>
		implements TreeNodeController<T>, Serializable {

	private static final long serialVersionUID = 1L;

	protected final static String YES = "Y";
	protected final static String EMPTY_STRING = "";
	
	private final List<String> MAIN_CASE_TYPES = Arrays.asList("T", "S", "A");
	
	private TreeNodeFactory treeNodeFactory;
	private Class<T> modelType;
	private XDialog listDialog;
	private ListModel listModel;
	private Outline listOutline;
	private DefaultTreeModel listTreeModel;
	private DataFlavor childDataFlavor;
	private DefaultMutableTreeNode treeNode;
	private T model;
	private boolean refreshedOutline = false;

	public AbstractTreeNodeController(TreeNodeFactory treeNodeFactory, Class<T> modelType, XDialog listDialog,
			ListModel listModel, Outline listOutline, DefaultTreeModel listTreeModel) {
		// Required variables
		this.treeNodeFactory = treeNodeFactory;
		this.modelType = modelType;
		this.listDialog = listDialog;
		this.listModel = listModel;
		this.listOutline = listOutline;
		this.listTreeModel = listTreeModel;

		// Optional child data flavour which is used in drag and drop
		Class childModelType = getChildModelType();
		if (childModelType != null) {
			this.childDataFlavor = new ActivationDataFlavor(childModelType, DataFlavor.javaJVMLocalObjectMimeType,
					"Model");
		}
	}

	protected XDialog getListDialog() {
		return listDialog;
	}

	protected ListModel getListModel() {
		return listModel;
	}

	protected Outline getOutline() {
		return listOutline;
	}

	protected DefaultTreeModel getTreeModel() {
		return listTreeModel;
	}

	@Override
	public TreeNodeFactory getTreeNodeFactory() {
		return treeNodeFactory;
	}

	@Override
	public Class<T> getModelType() {
		return modelType;
	}

	@Override
	public DefaultMutableTreeNode getTreeNode() {
		return treeNode;
	}

	@Override
	public void setTreeNode(DefaultMutableTreeNode treeNode) {
		this.treeNode = treeNode;
	}

	@Override
	public T getModel() {
		return model;
	}

	@Override
	public void setModel(T model) {
		this.model = model;
	}

	/**
	 * Default implementation returns null and sub-classes which support a popup
	 * menu must return a popup.
	 */
	@Override
	public JPopupMenu getPopupMenu() {
		return null;
	}

	/**
	 * Default implementation returns false and sub-classes which support drag
	 * must override and return true.
	 */
	@Override
	public boolean isDragSupported() {
		return false;
	}

	/**
	 * Default implementation returns null and sub-classes which support drag
	 * must override and return a new transferable.
	 */
	@Override
	public Transferable createTransferable() {
		return null;
	}

	/**
	 * Returns true if this node supports children and the data flavour being
	 * passed is the child model type.
	 */
	@Override
	public boolean isDropSupported(TransferSupport support) {
		return (treeNode.getAllowsChildren() && childDataFlavor != null
				&& support.isDataFlavorSupported(childDataFlavor));
	}

	/**
	 * Default implementation but sub-classes which support drop
	 * must implement SAYG functionality.,
	 * Method gets current values required for comparison for structural changes.
	 */
	//public abstract void getOriginalValues();	
	protected void setOriginalValues(DefaultMutableTreeNode childNode) {
		// Get the original values from listModel - 
		// Each controller will implement its own version, as they all need different values
		// (eg moving case requires copy of sittingOnListBV and list of 
		// cases already under the sitting)
	}
	
	protected void refreshList(String source) {
		refresh(source, true);
	}
	
	protected boolean validateCourtRoomStructure(XhbCourtRoomBasicValue courtRoomBeforeRefresh, XhbCourtRoomBasicValue courtRoomAfterRefresh) {
		boolean valid = false;
		if(courtRoomAfterRefresh != null && !YES.equals(courtRoomAfterRefresh.getObsInd())){
			//ensure still flagged as secure (or not)
			if(isSame(courtRoomAfterRefresh.getSecurityInd(),courtRoomBeforeRefresh.getSecurityInd()) ){
				//ensure has video link capabilities (or not)
				if(isSame(courtRoomAfterRefresh.getVideoInd(),courtRoomBeforeRefresh.getVideoInd()) ){
					valid = true;
				}
			}
		}
		return valid;
	}
	
	protected boolean validateSittingOnListStructure(SittingOnListComplexValue sittingBeforeRefresh, SittingOnListComplexValue sittingAfterRefresh) {
		boolean valid = false;
		if(sittingAfterRefresh != null && !YES.equals(sittingAfterRefresh.getObsInd())){
			//ensure sitting not moved from room
			if(sittingAfterRefresh.getCourtRoomId().equals(sittingBeforeRefresh.getCourtRoomId()) ){
				//ensure sitting not moved within the room
				if(sittingAfterRefresh.getSittingNumber().equals(sittingBeforeRefresh.getSittingNumber()) ){
					valid = true;
				}
			}
		}
		return valid;
	}
		
	
	protected boolean validateCaseOnListStructure(CaseOnListComplexValue caseBeforeRefresh, CaseOnListComplexValue caseAfterRefresh) {
		boolean valid = false;
		if(caseBeforeRefresh != null){
			if(caseBeforeRefresh.getCaseOnListId() == null ){
				// LHS -> RHS specific
				valid = true;
			}
			else if(caseAfterRefresh != null){ // if null the case has been deleted - so still false
				//RHS to RHS specific
				//ensure case not moved to another sitting in another court room
				if(isSame(caseBeforeRefresh.getCourtRoomId(),caseAfterRefresh.getCourtRoomId())){
					//ensure case not moved to another sitting in same court room
					if(isSame(caseBeforeRefresh.getSittingOnListId(),caseAfterRefresh.getSittingOnListId())){	
						//ensure case not moved to/from floater 
						if(isSame(caseBeforeRefresh.getFloaterCase(),caseAfterRefresh.getFloaterCase())){
							//ensure case not moved to/from reserved 
							if(isSame(caseBeforeRefresh.getReserved(),caseAfterRefresh.getReserved())){
								// ensure not deleted
								if(!YES.equals(caseAfterRefresh.getObsInd()) ){									
									// as not moved sitting or court room, ensure case not moved within node. Don't check in all scenarios
									boolean isNoDate = this instanceof NoDateCasesTreeNodeController ||
														getParentController() instanceof NoDateCasesTreeNodeController;
									boolean checkSeqNo = !isNoDate && !YES.equals(caseBeforeRefresh.getReserved());
									if(checkSeqNo){
										if(caseBeforeRefresh.getSeqNo().equals(caseAfterRefresh.getSeqNo())){
											valid = true;
										}										
									}
									else{
										valid = true;
									}
								}
							}
						}
					}
				}
			}
		}
		return valid;
	}

	private boolean isSame(Integer before, Integer after) {
		return (before == null && after == null) || 
				(before != null && after != null && before.equals(after));
	}
	
	private boolean isSame(String before, String after) {
		return (before == null && after == null) || 
				(before != null && after != null && before.equals(after));
	}
	
	private List<CaseOnListComplexValue> getListOfCasesOnOriginalNodeAfterRefresh(CaseOnListComplexValue caseBeforeRefresh) {
		List<CaseOnListComplexValue> listOfCasesOnOriginalNodeAfterRefresh;
		// Warned List
		if (this.getListModel() instanceof WarnedListModel) {
			final WarnedListModel warnedListModel = (WarnedListModel) this.getListModel();
			listOfCasesOnOriginalNodeAfterRefresh = warnedListModel.getCasesOnListForNoDate(caseBeforeRefresh.getCourtSiteId());
		} 
		// Daily or Firm
		else {
			final AbstractDailyFirmListModel dailyFirmListModel = (AbstractDailyFirmListModel) this.getListModel();
			if(YES.equals(caseBeforeRefresh.getFloaterCase())){
				listOfCasesOnOriginalNodeAfterRefresh = dailyFirmListModel.getCasesOnListForFloater(caseBeforeRefresh.getTimeListed().getTime(), 
																									caseBeforeRefresh.getCourtSiteId());
			} else if (YES.equals(caseBeforeRefresh.getReserved())) {
				listOfCasesOnOriginalNodeAfterRefresh = dailyFirmListModel.getCasesOnListForReserve();
			} else {
				listOfCasesOnOriginalNodeAfterRefresh = dailyFirmListModel.getCasesOnListForSitting(caseBeforeRefresh.getSittingOnListId());
			}
		}
		return listOfCasesOnOriginalNodeAfterRefresh;
	}

	private List<SittingOnListComplexValue> getListOfSittingsOnOriginalNodeAfterRefresh(Date date, SittingOnListComplexValue sittingBeforeRefresh) {
		final AbstractDailyFirmListModel dailyFirmListModel = (AbstractDailyFirmListModel) this.getListModel();
		List<SittingOnListComplexValue> listOfSittingsOnOriginalNodeAfterRefresh = dailyFirmListModel.getSittingsOnList(date, sittingBeforeRefresh.getCourtRoomId());
		return listOfSittingsOnOriginalNodeAfterRefresh;
	}

	protected CaseDiaryFixtureComplexValue getFixtureAfterRefresh(Integer caseDiaryFixtureId) {
		CaseDiaryFixtureComplexValue fixtureAfterRefresh = null;
		if (caseDiaryFixtureId != null) {
			try {
				fixtureAfterRefresh = XhibitDelegateHelper.getListingsDelegate().findCaseDiaryFixture(caseDiaryFixtureId);				
			} catch (ListingsControllerException e) {
				// Re-throw as runtime to cancel drop as unable to check fixture validation
				throw new CSUnrecoverableException(e);
			}
		}
		return fixtureAfterRefresh;
	}
	
	protected CaseOnListComplexValue getCaseAfterRefresh(CaseOnListComplexValue caseBeforeRefresh) {
		CaseOnListComplexValue caseAfterRefresh = null;	
		List<CaseOnListComplexValue> listOfCasesOnOriginalNodeAfterRefresh = getListOfCasesOnOriginalNodeAfterRefresh(caseBeforeRefresh);
		if (listOfCasesOnOriginalNodeAfterRefresh != null) {
			for(CaseOnListComplexValue caze : listOfCasesOnOriginalNodeAfterRefresh ){
				if(caze.getCaseOnListId().equals(caseBeforeRefresh.getCaseOnListId())){
					caseAfterRefresh = caze;
					break;
				}
			}
		}
		return caseAfterRefresh;
	}
	
	protected SittingOnListComplexValue getSittingAfterRefresh(Date date, SittingOnListComplexValue sittingBeforeRefresh){
		List<SittingOnListComplexValue> listOfSittingsOnOriginalNodeAfterRefresh = getListOfSittingsOnOriginalNodeAfterRefresh(date, sittingBeforeRefresh);
		SittingOnListComplexValue refreshedSittingCV = null;
		for(SittingOnListComplexValue sitting : listOfSittingsOnOriginalNodeAfterRefresh){
			if(sitting.getSittingOnListId().equals(sittingBeforeRefresh.getSittingOnListId())){
				refreshedSittingCV = sitting;
				break;
			}
		}
		return refreshedSittingCV;
	}

	/**
	 * Default implementation returns true but sub-classes which support drop
	 * must implement SAYG functionality and return true if valid
	 */
	protected boolean validateStructure(DefaultMutableTreeNode childNode) {
		// Each controller will implement its own version of this as required
		return true; 
	}
	
	/**
	 * Structure of the list has been changed by another user
	 */
	protected void showInvalidStructureMsg() {
		XMessageBox.alert	(getListDialog(),
				XHIBITConstant.getResource(XhibitBundles.Listings, "ErrorSaveTitle"), 
				true,
				XMessageBox.ICONERROR, 
				XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationStructureChangedByAnotherUser"),
				XMessageBox.OK_ONLY, 
				XMessageBox.DEFAULTOK);
	}

	/**
	 * Structure of the fixture has been changed by another user
	 */
	protected void showInvalidFixtureStructureMsg() {
		XMessageBox.alert	(getListDialog(),
				XHIBITConstant.getResource(XhibitBundles.Listings, "ErrorSaveTitle"), 
				true,
				XMessageBox.ICONERROR, 
				XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationFixtureStructureChangedByAnotherUser"),
				XMessageBox.OK_ONLY, 
				XMessageBox.DEFAULTOK);
	}
	
	/**
	 * Returns true if child data imported into list model.
	 */
	@Override
	public boolean importChild(DefaultMutableTreeNode childNode, int index) {
		boolean imported = false;
		setOriginalValues(childNode);
		refreshDBOnly("ImportChild");
		if (!showErrorMsg() && validateStructure(childNode) && validateChild(childNode, index)) {
			imported = dropChild(childNode, index);
			if (imported) {
				save("importChild");
			}
		}
		return imported;
	}
	
	
	/**
	 * Method used to obtain the number of cases in the list 
	 * that are not marked as obsolete.
	 * 
	 * @param caseList
	 * @return number of cases not that are not set to obs
	 */
	protected int getNumberOfNonObsoleteCasesinList(List<CaseOnListComplexValue> caseList){
		int noCasesNotObs = 0;
		
		if(caseList != null){
			for(CaseOnListComplexValue caze : caseList){
				if(caze.getObsInd() == null || caze.getObsInd().equals("N")){
					noCasesNotObs++;
				}
			}
		}
		
		return noCasesNotObs;
	}

	/**
	 * Method used to obtain the number of cases in the list 
	 * that are not marked as obsolete.
	 * 
	 * @param sittingOnListId
	 * @return number of cases not that are not set to obs
	 */
	protected int getNumberOfNonObsoleteCasesinList(final Integer sittingOnListId) {
		final AbstractDailyFirmListModel dailyFirmListModel = (AbstractDailyFirmListModel) this.getListModel();
		List<CaseOnListComplexValue> listOfCases = dailyFirmListModel.getCasesOnListForSitting(sittingOnListId);
		return getNumberOfNonObsoleteCasesinList(listOfCases);
	}

	/**
	 * Default implementation returns null and sub-classes which support drop
	 * must override and return class.
	 * 
	 * @return
	 */
	protected <S extends TreeNodeModel> Class<S> getChildModelType() {
		return null;
	}

	/**
	 * Default implementation returns true but sub-classes which support drop
	 * must implement add/move functionality and return true if successful.
	 */
	protected boolean dropChild(DefaultMutableTreeNode childNode, int index) {
		// Default implementation always returns valid
		return true;
	}

	/**
	 * Default implementation returns true and sub-classes which support drop
	 * must override and return true if valid and false if not valid.
	 */
	protected boolean validateChild(DefaultMutableTreeNode childNode, int index) {
		// Reset the flag so we only refresh the screen with the latest data once
		refreshedOutline = false;
		
		// Default implementation always returns valid
		return true;
	}

	/**
	 * Ensures a hearing is entered against a case by opening the additional details screen
	 * if one is not currently set on the case on list.
	 * 
	 * @param CaseOnListComplexValue caseOnList
	 * @return boolean true if additional details complete, false if not and user cancelled
	 * 
	 * @throws CSUnrecoverableException
	 */
	protected boolean checkAdditionalDetails(CaseOnListComplexValue caseOnList) throws CSUnrecoverableException {	
		boolean checkOK = false;
		
		// open the additional details dialog if there is no hearing type to force the user to enter one
		if (caseOnList.getHearingType() == null){
			try {
				// create details model populated for use by the details dialog and display dialog
		    	AdditionalDetailsModel detailsModel = getAdditionalDetailsModel(caseOnList);
				final AdditionalDetailsDialog dialog = new AdditionalDetailsDialog(getListDialog(), detailsModel);
				dialog.setVisible(true);	
				
				// user has saved dialog and so must have added a hearing type to pass validation
				if (detailsModel.isDirty()) {
					checkOK = true;
				}
				// user has cancelled and so must remove the case as not dropping after all
				else {
					getListModel().removeCase(caseOnList);
				}
			} catch (CSRecoverableException e) {
				// must remove the case as not dropping if exception thrown by dialog
				getListModel().removeCase(caseOnList);
				// Re-throw as runtime to cancel drop as unable to complete details
				throw new CSUnrecoverableException(e);
			}
		}
		// else the case has sufficient information for the case to be added
		else {
			checkOK = true;
		}
		
		return checkOK;
	}
	
	/**
	 * Returns new additional details model containing supplied caseOnList.
	 * 
	 * @param caseOnList
	 * @return
	 */
	protected  AdditionalDetailsModel getAdditionalDetailsModel(CaseOnListComplexValue caseOnList) {
    	final AdditionalDetailsModel model = new  AdditionalDetailsModel();
    	model.setXac(getListModel().getXac());
    	model.setCaseOnList(caseOnList);
    	model.setListModel(getListModel());
    	return model;
    }

	/**
	 * Returns true if room has facility or user has confirmed it is okay that
	 * it does not have the required facility.
	 * 
	 * @param requiredRoomFacility
	 *            - whether the case requires a specific room facility, e.g.
	 *            secure, or video link
	 * @param roomHasFacility
	 *            - whether the room offers the facility required, e.g. secure
	 *            or video link
	 * @param warningMessage
	 * @return
	 */
	protected boolean validateCourtRoom(String requiredRoomFacility, String roomHasFacility, String warningMessage) {
		boolean validChild = true;
		if (YES.equals(requiredRoomFacility)) {
			// check if court room has facility
			if (!YES.equals(roomHasFacility)) {
				validChild = displayMoveWarningDialog("validateCourtRoom", warningMessage);
			}
		}
		return validChild;
	}

	/**
	 * Returns true if there are no non-available days for the case on the
	 * required date or the user has confirmed it is okay if there are.
	 * 
	 * @param caseId
	 * @param date
	 * @return
	 */
	protected boolean validateNonAvailableDays(Integer caseId, Date date) {
		boolean validChild = true;
		try {
			if (XhibitDelegateHelper.getListingsDelegate().isCaseNonAvailDay(caseId, date)) {
				validChild = displayMoveWarningDialog("validateNonAvailableDays-daily",
						XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationNonAvailableCaseMove"));
			}
		} catch (ListingsControllerException e) {
			// Re-throw as runtime to cancel drop as unable to check days
			throw new CSUnrecoverableException(e);
		}
		return validChild;
	}
	
	/**
	 * Returns true if there are no non-available days for the case during
	 * the list period or the user has confirmed it is okay if there are.
	 * 
	 * @param caseId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	protected boolean validateNonAvailableDays(Integer caseId, Date fromDate, Date toDate) {
		boolean validChild = true;
		try {
			if (XhibitDelegateHelper.getListingsDelegate().isCaseNonAvailDay(caseId, fromDate, toDate)) {
				validChild = displayMoveWarningDialog("validateNonAvailableDays-dateRange",
						XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationNonAvailableMove"));
			}
		} catch (ListingsControllerException e) {
			// Re-throw as runtime to cancel drop as unable to check days
			throw new CSUnrecoverableException(e);
		}
		return validChild;
	}

	
	/**
	 * Returns true if there are no issues regards moving a  case to a site. 
	 * 
	 * @param CaseOnListComplexValue caseOnList
	 * @param Integer courtSiteDroppingOn
	 * @return boolean
	 */
	protected boolean validateCourtSite( final CaseOnListComplexValue caseOnList, Integer courtSiteIdDroppingOn, Integer caseCurrentSiteId){
		// validate case is moving to the correct court site when dropping from LHS
		// or moving the case from the reserve folder, i.e. no current court site
		if (caseCurrentSiteId == null) {
			return validateCourtSite(caseOnList, courtSiteIdDroppingOn);
		}
		// else warn the user if moving within list and the court site is changing
		else {
			return displayMoveWarningDialog("validateCourtSite-ChangeCourt",XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationChangeCourtSiteWithinListCaseMove"));
		}	
	}
	
	/**
	 * Returns true if there are no issues regards moving a case to a site (where there is no current court site). 
	 * 
	 * @param CaseOnListComplexValue caseOnList
	 * @param Integer courtSiteDroppingOn
	 * @return boolean
	 */
	private boolean validateCourtSite( final CaseOnListComplexValue caseOnList, Integer courtSiteIdDroppingOn){
		boolean validChild = true;
		String warningMessage = null;
		Integer draggedCaseCourtSiteId = null;
		
		CaseDiaryFixtureComplexValue fixture = null;
		Integer caseDiaryFixtureId = caseOnList.getCaseDiaryFixtureId();
		
		try{		
			if(caseDiaryFixtureId != null){
				fixture = XhibitDelegateHelper.getListingsDelegate().findCaseDiaryFixture(caseDiaryFixtureId);
			}
			
			// if fixture being dropped
			if(fixture != null){				
				//moving a fixture. Set warning message and court site id if moving to a different court site
				if(!fixture.getCourtSiteId().equals(courtSiteIdDroppingOn)){
					draggedCaseCourtSiteId = fixture.getCourtSiteId();
					String draggedCaseCourtSiteName = getCourtSiteName(draggedCaseCourtSiteId);
					String draggedToCourtSiteName = getCourtSiteName(courtSiteIdDroppingOn);
					warningMessage = XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationCourtSiteCaseFixedAtCaseMove") +
							" " + draggedCaseCourtSiteName + 
							" " + XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationCourtSiteListCaseQuestionCaseMove") +
							" " + draggedToCourtSiteName +"?";
				}	
			}
			//else if on the firm or warned list
			else if(caseOnList.getParentCaseOnListId() != null){
				CaseOnListBasicValue parentCaseOnListBV = XhibitDelegateHelper.getListingsDelegate().findCaseOnList(caseOnList.getParentCaseOnListId());
				ListBasicValue listBV = XhibitDelegateHelper.getListingsDelegate().findList(parentCaseOnListBV.getListId());
				ListTypeEnum listType = ListingDropdownPopulation.getListType(listBV.getListTypeId());
				if(listType.isWarned()){
					// moving a warned case. 
					// Set warning message and court site id if moving to a different court site
					if(!parentCaseOnListBV.getCourtSiteId().equals(courtSiteIdDroppingOn)){
						draggedCaseCourtSiteId = parentCaseOnListBV.getCourtSiteId();
						String draggedCaseCourtSiteName = getCourtSiteName(draggedCaseCourtSiteId);
						String draggedToCourtSiteName = getCourtSiteName(courtSiteIdDroppingOn);
						warningMessage = XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationCourtSiteCaseWarnedAtCaseMove") +
								" " + draggedCaseCourtSiteName + 
								" " + XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationCourtSiteListCaseQuestionCaseMove") +
								" " + draggedToCourtSiteName +"?";
					}
				}
			}
			// from other cases table
			else{
				CaseListingEntryBasicValue caseListingEntryBV = caseOnList.getCaseListingEntry();
				if(caseListingEntryBV != null && caseListingEntryBV.getCourtSiteId() != null){
					//Set warning message and court site id if moving to different court site
					if(!courtSiteIdDroppingOn.equals(caseListingEntryBV.getCourtSiteId())){
						draggedCaseCourtSiteId = caseListingEntryBV.getCourtSiteId();
						String draggedCaseCourtSiteName = getCourtSiteName(draggedCaseCourtSiteId);
						String draggedToCourtSiteName = getCourtSiteName(courtSiteIdDroppingOn);
						warningMessage = XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationCourtSiteCaseListingEntryCaseMove") + 
								" " + draggedCaseCourtSiteName + 
								" " + XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationCourtSiteListCaseQuestionCaseMove") +
								" " + draggedToCourtSiteName +"?";
					}
				}
			}			
		} catch (ListingsControllerException e) {
			// Re-throw as runtime to cancel drop as unable to check site validation
			throw new CSUnrecoverableException(e);
		}

		//display warning message if required
		if(warningMessage != null &&  draggedCaseCourtSiteId != null){
			validChild = displayMoveWarningDialog("validateCourtSite",warningMessage);
		}
		return validChild;
	}	
	
	/**
	 * Validate the case before it can be dropped
	 * There must be at least one defendant/appellant AND a prosecutor/respondent on the case.
	 * The number of defendants stated on the case must also equal the number of defendants
	 * @return true if case is valid, else false
	 */
	protected boolean validateStateOfCase(Integer caseId)
	{
		boolean valid = true;
		
		String caseStatus = XhibitDelegateHelper.getCaseDelegate().determineCaseStatus(caseId);
		
		if ( CaseStatusIndicator.INCOMPLETE_N.equals(caseStatus) ) {
			// There must be at least one defendant/appellant AND a prosecutor/respondent on the case.
			String errorMsg = XHIBITConstant.getResource(XhibitBundles.ErrorText, "listings.caseListingDetails.alert.partyDetailsIncomplete");
			XMessageBox.alert(getListDialog(),
					XHIBITConstant.getResource(XhibitBundles.ErrorText, "listings.caseListingDetails.alert.title"), true,
					XMessageBox.ICONERROR, errorMsg, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			valid = false;
		}
		else if ( CaseStatusIndicator.INCOMPLETE_I.equals(caseStatus) ) {
			// The number of defendants stated on the case must also equal the number of defendants
			String errorMsg = XHIBITConstant.getResource(XhibitBundles.ErrorText, "listings.caseListingDetails.alert.incorrectNoDefendants");
			XMessageBox.alert(getListDialog(),
					XHIBITConstant.getResource(XhibitBundles.ErrorText, "listings.caseListingDetails.alert.title"), true,
					XMessageBox.ICONERROR, errorMsg, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			valid = false;
		}
		else if ( CaseStatusIndicator.DEALT_WITH.equals(caseStatus) ) {
			// case is closed
			valid = displayMoveWarningDialog("validateStateOfCase-DW",XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationClosedCaseCaseMove"));
		}
		else if ( CaseStatusIndicator.BENCH_WARRANT.equals(caseStatus) ) {
			// case has an outstanding bench warrant
			valid = displayMoveWarningDialog("validateStateOfCase-BW",XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationBenchWarrantCaseMove"));
		}
		else if ( CaseStatusIndicator.TRANSFERRED_OUT.equals(caseStatus) ) {
			// case has been transferred out
			valid = displayMoveWarningDialog("validateStateOfCase-TO", XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationTransferredOutCaseMove"));
		}
		
		return valid;
	}

	/**
	 * Returns true if the fixture is still valid.
	 * 
	 * @param caseDiaryFixture
	 * @return
	 */
	protected boolean validateStateOfFixture(CaseDiaryFixtureComplexValue fixture) {
		boolean valid = false;
		if(fixture != null && !YES.equals(fixture.getObsInd())){
			valid = true;
		}
		if (!valid) {
			showInvalidFixtureStructureMsg();
		}
		return valid;
	}

	/**
	 * Returns true if the fixture is for the required date
	 * or the user has confirmed it is okay to change date.
	 * 
	 * @param caseDiaryFixture
	 * @param date
	 * @return
	 */
	protected boolean validateFixtureDate(CaseDiaryFixtureComplexValue fixture, Date date) {
		boolean validChild = true;
		
		if (!DateTimeUtilities.isDaySame(fixture.getListingDate(), date)) {
			validChild = displayMoveWarningDialog("validateFixtureDate",XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationFixtureDateCaseMove"));
		}
		return validChild;
	}
	
	/**
	 * Returns true if any of the defendants on the case on list can be listed.
	 * 
	 * @param caseOnList
	 * @param date
	 * @param errorMessage
	 * @return
	 */
	protected boolean validateDefendants(final CaseOnListComplexValue caseOnList, Date date, String errorMessage) {
		boolean validChild = true;

		// If there are defendants (e.g. B and U cases have no defendants)
		// and all the defendants on the case on list are already listed,
		// then cannot list the case and so display an error message
		if (!caseOnList.getDefOnCaseOnLists().isEmpty() && getDefendantsNotListed(caseOnList, date).isEmpty()) {
			XMessageBox.alert(getListDialog(),
					XHIBITConstant.getResource(XhibitBundles.Listings, "listMoveErrorTitle"), true,
					XMessageBox.ICONERROR, errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			validChild = false;
		}

		
		return validChild;
	}
	
	/**
	 * Returns true if any of the defendants on the case on list can be listed.
	 * 
	 * @param caseOnList
	 * @param date
	 * @param errorMessage
	 * @return
	 */
	protected boolean validateCaseListed(final CaseOnListComplexValue caseOnList, Date date, String errorMessage) {
		boolean validChild = true;

		// If the case is listed on the same day already display message.
		if (getListModel().isCaseListed(caseOnList.getCaseId(), date)) {
			XMessageBox.alert(getListDialog(),
					XHIBITConstant.getResource(XhibitBundles.Listings, "listMoveErrorTitle"), true,
					XMessageBox.ICONERROR, errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			validChild = false;
		}
		return validChild;
	}
	
	/**
	 * Returns true if case is T, S or A and so not B or U.
	 * 
	 * @param caseType
	 * @return
	 */
	protected boolean isMainCaseType(String caseType) {		
		for (final String supported : MAIN_CASE_TYPES) {
			if (supported.equals(caseType)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get the list of defendants on the case on list that are already listed anywhere else on the list.
	 * 
	 * @param caseOnList
	 * @return
	 */
	protected List<DefOnCaseOnListBasicValue> getDefendantsListed(CaseOnListComplexValue caseOnList) {
		List<DefOnCaseOnListBasicValue> defendants = new ArrayList<DefOnCaseOnListBasicValue>();
		
		for (DefOnCaseOnListBasicValue defOnCaseOnList : caseOnList.getDefOnCaseOnLists()) {
			// If the defendant is already listed elsewhere, add it to list of defendants
			if (listModel.isDefendantListedElsewhere(caseOnList.getCaseId(),
					defOnCaseOnList.getDefendantOnCaseId(), caseOnList.getCaseOnListId())) {
				defendants.add(defOnCaseOnList);
			}
		}
		
		return defendants;
	}
	
	/**
	 * Get the list of defendants on the case on list that are already listed on a specific date.
	 * 
	 * @param caseOnList
	 * @param date
	 * @return
	 */
	protected List<DefOnCaseOnListBasicValue> getDefendantsListed(CaseOnListComplexValue caseOnList, Date date) {
		List<DefOnCaseOnListBasicValue> defendants = new ArrayList<DefOnCaseOnListBasicValue>();
		
		for (DefOnCaseOnListBasicValue defOnCaseOnList : caseOnList.getDefOnCaseOnLists()) {
			// If the defendant is already listed, add it to list of defendants
			if (listModel.isDefendantListed(date, caseOnList.getCaseId(), defOnCaseOnList.getDefendantOnCaseId())) {
				defendants.add(defOnCaseOnList);
			}
		}
		
		return defendants;
	}
	
	/**
	 * Get the list of defendants on the case on list that are not already listed anywhere else on the list.
	 * 
	 * @param caseOnList
	 * @return
	 */
	protected List<DefOnCaseOnListBasicValue> getDefendantsNotListed(CaseOnListComplexValue caseOnList) {
		List<DefOnCaseOnListBasicValue> defendants = new ArrayList<DefOnCaseOnListBasicValue>();
		
		for (DefOnCaseOnListBasicValue defOnCaseOnList : caseOnList.getDefOnCaseOnLists()) {
			// If the defendant is not already listed elsewhere, add it to list of defendants
			if (!listModel.isDefendantListedElsewhere(caseOnList.getCaseId(),
					defOnCaseOnList.getDefendantOnCaseId(), caseOnList.getCaseOnListId())) {
				defendants.add(defOnCaseOnList);
			}
		}
		
		return defendants;
	}
	
	/**
	 * Get the list of defendants on the case on list that are not already listed on a specific date.
	 * 
	 * @param caseOnList
	 * @param date
	 * @return
	 */
	protected List<DefOnCaseOnListBasicValue> getDefendantsNotListed(CaseOnListComplexValue caseOnList, Date date) {
		List<DefOnCaseOnListBasicValue> defendants = new ArrayList<DefOnCaseOnListBasicValue>();
		
		for (DefOnCaseOnListBasicValue defOnCaseOnList : caseOnList.getDefOnCaseOnLists()) {
			// If the defendant is not already listed, add it to list of defendants
			if (!listModel.isDefendantListed(date, caseOnList.getCaseId(), defOnCaseOnList.getDefendantOnCaseId())) {
				defendants.add(defOnCaseOnList);
			}
		}
		
		return defendants;
	}
	
	/**
	 * Get the list of defendants from the supplied list which are attending
	 * a fixture which has yet to be dragged onto the list, i.e. the defendant
	 * will be listed once all the fixtures have been dragged onto the list.
	 * 
	 * @param caseId
	 * @param fromDate
	 * @param toDate
	 * @param defOnCaseOnLists
	 * @return
	 */
	protected List<DefOnCaseOnListBasicValue> getDefendantsFixed(Integer caseId, Date fromDate, Date toDate, Collection<DefOnCaseOnListBasicValue> defOnCaseOnLists) {
		List<DefOnCaseOnListBasicValue> defendants = new ArrayList<DefOnCaseOnListBasicValue>();
		
		for (DefOnCaseOnListBasicValue defOnCaseOnList : defOnCaseOnLists) {
			// If the defendant is already fixed, add it to list of defendants
			if (isDefendantFixed(caseId, fromDate, toDate, defOnCaseOnList.getDefendantOnCaseId())) {
				defendants.add(defOnCaseOnList);
			}
		}
		
		return defendants;
	}
	
	/**
	 * Get the list of defendants from the supplied list which are not attending
	 * a fixture which has yet to be dragged onto the list, i.e. the defendant
	 * will not be listed once all the fixtures have been dragged onto the list.
	 * 
	 * @param caseId
	 * @param fromDate
	 * @param toDate
	 * @param defOnCaseOnLists
	 * @return
	 */
	protected List<DefOnCaseOnListBasicValue> getDefendantsNotFixed(Integer caseId, Date fromDate, Date toDate, Collection<DefOnCaseOnListBasicValue> defOnCaseOnLists) {
		List<DefOnCaseOnListBasicValue> defendants = new ArrayList<DefOnCaseOnListBasicValue>();
		
		for (DefOnCaseOnListBasicValue defOnCaseOnList : defOnCaseOnLists) {
			// If the defendant is not already fixed, add it to list of defendants
			if (!isDefendantFixed(caseId, fromDate, toDate, defOnCaseOnList.getDefendantOnCaseId())) {
				defendants.add(defOnCaseOnList);
			}
		}
		
		return defendants;
	}
	
	/**
	 * Return true if the defendant is marked as attending on a fixture which
	 * has yet to be dragged onto the list, i.e. the defendant will be listed
	 * once all the fixtures have been dragged onto the list.
	 * 
	 * @param caseId
	 * @param fromDate
	 * @param toDate
	 * @param defendantOnCaseId
	 * @return
	 */
	protected boolean isDefendantFixed(Integer caseId, Date fromDate, Date toDate, Integer defendantOnCaseId) {
		boolean fixed = false;
		
		try {
			// Get all the fixtures that have the defendant marked as attending during the list period
			@SuppressWarnings("unchecked")
			Collection<CaseDiaryFixtureComplexValue> fixtures = XhibitDelegateHelper.getListingsDelegate().
				findCaseDiaryFixturesByCaseIdAndListingDateAndDefendantOnCaseId(caseId, fromDate, toDate, defendantOnCaseId);
			
			// If any of the fixtures that the defendant is attending are not yet listed,
			// then the defendant cannot be listed as they are considered to be fixed.
			// If they are on a fixture that has been listed but have been de-selected,
			// they are no longer considered to be fixed and that scenario is checked
			// elsewhere by checking whether the defendant is already on the list.
			for (CaseDiaryFixtureComplexValue fixture : fixtures) {
				if (!getListModel().isFixtureListed(caseId, fixture.getCaseDiaryFixtureId())) {
					fixed = true;
					break;
				}
			}
		} catch (ListingsControllerException e) {
			// Re-throw as runtime to cancel drop as unable to check fixtures
			throw new CSUnrecoverableException(e);
		}

		return fixed;
	}
	
	/**
	 * Displays a warning if not all the case's defendants are on the case being listed.
	 * @param caseOnList
	 */
	protected void warnIfNotAllCaseDefsBeingListed(CaseOnListComplexValue caseOnList) {
		// only check T, S or A cases and so not B or U as they have no defendants
		if (isMainCaseType(caseOnList.getCase().getCaseType())) {
			Integer noOfDefendantsOnCase = caseOnList.getCase().getNoDefendantsForCase();
			Integer noDefOnCaseOnList = caseOnList.getDefOnCaseOnLists().size();
			
			// display the warning if there are defendants not listed 
			boolean displayWarning = noDefOnCaseOnList<noOfDefendantsOnCase;
	
			if (displayWarning){
				XMessageBox.alert(getListDialog(),
						XHIBITConstant.getResource(XhibitBundles.Listings, "listMoveConfirmationTitle"), 
									true,
									XMessageBox.ICONQUESTION, 
									XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationWarningNotAllDefsListed"), 
									XMessageBox.OK_ONLY, 
									XMessageBox.DEFAULTOK);
			}
		}
	}
	
	/**
	 * displays the supplied message and returns true or false depending on the
	 * answer selected by the user
	 * 
	 * @param source
	 * @param moveWarningMessage
	 * @return
	 */
	protected boolean displayMoveWarningDialog(String source, String moveWarningMessage) {
		refreshOutlineOnlyOnce(source);
		boolean messageBoxReply = XMessageBox.alert(getListDialog(),
				XHIBITConstant.getResource(XhibitBundles.Listings, "listMoveConfirmationTitle"), true,
				XMessageBox.ICONQUESTION, moveWarningMessage, XMessageBox.YESNO, XMessageBox.DEFAULTNO);

		return messageBoxReply;
	}
	
	/**
	 * Returns the case number in the format for displaying to user.
	 * 
	 * @param caseBasicValue
	 * @return
	 */
    protected String getDisplayCaseNumber(CaseBasicValue caseBasicValue) {
        StringBuffer caseNumber = new StringBuffer();
        caseNumber.append(caseBasicValue.getCaseType());
        caseNumber.append(caseBasicValue.getCaseNumber());
        return caseNumber.toString();
    }
	
	/**
	 * Returns the Court Site Name.
	 * 
	 * @param String courtId
	 * @param Integer courtSiteId
	 * 
	 * @return String court site name
	 */
	@SuppressWarnings("unchecked")
	private String getCourtSiteName(Integer courtSiteId) {
		String courtSiteName = null;
		try {
			CourtSiteCriteria criteria = new CourtSiteCriteria();
			criteria.setPrimaryKey(courtSiteId);
			ArrayList<CourtSiteBasicValue> courtSites = (ArrayList<CourtSiteBasicValue>) XhibitDelegateHelper.getBizRefDelegate()
					.findCourtSites(criteria);
			
			for(CourtSiteBasicValue cs : courtSites){
				if(cs.getId().equals(courtSiteId)){
					courtSiteName = cs.getCourtSiteName();
				}
			}
		} catch (SysRefControllerException e) {
			// Re-throw as runtime to cancel drop as unable to check court site
			throw new CSUnrecoverableException(e);
		}
    	return courtSiteName;
	}
	
	/**
	 * Returns true if no fixtures exist for the case during the list period.
	 * 
	 * @param caseId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	protected boolean validateNoFixtures(Integer caseId, Date fromDate, Date toDate, String errorMsg) {
		boolean validChild = true;

		try {
			if (XhibitDelegateHelper.getListingsDelegate().isCaseDiaryFixture(caseId, fromDate, toDate)) {				
				XMessageBox.alert(getListDialog(),
						XHIBITConstant.getResource(XhibitBundles.Listings, "listMoveErrorTitle"), true,
						XMessageBox.ICONERROR, errorMsg, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
				validChild = false;
			}
		} catch (ListingsControllerException e) {
			// Re-throw as runtime to cancel drop as unable to check days
			throw new CSUnrecoverableException(e);
		}
		
		return validChild;
	}

	/**
	 * Returns name of the controller type.
	 *
	 * @return controller type
	 */
	public String getControllerType() {
		return this.getClass().getName();
	}

	protected DefaultMutableTreeNode getParentTreeNode() {
		return (DefaultMutableTreeNode) treeNode.getParent();
	}

	protected AbstractTreeNodeController getParentController() {
		return getParentTreeNode() != null ?
					(AbstractTreeNodeController) getParentTreeNode().getUserObject() : null;
	}

	/**
	 * Returns the controller id of the parent.
	 *
	 * @return parent controller id
	 */
	public String getParentControllerId() {
		AbstractTreeNodeController parentController = getParentController();
		return parentController != null ? parentController.getControllerId() : EMPTY_STRING;
	}

	/**
	 * Returns the controller id
	 *
	 * @return controller id
	 */
	public abstract String getControllerId();

	/**
	 * Refresh the tree outline from the model but only if we haven't already 
	 */
	protected void refreshOutlineOnlyOnce(String source) {
		if (!refreshedOutline) {
			refreshOutline(source);
			refreshedOutline = true;
		}
	}

	/**
	 * Refresh the tree outline from the model 
	 */
	protected void refreshOutline(String source) {
		refresh(source, false);
	}

	protected AbstractMainListingPanel getParentPanel() {
		return ((CourtListingOutline) listOutline).getParentPanel();
	}

	protected boolean showErrorMsg() {
		return getParentPanel().showErrorMsg();
	}

	/**
	 * Refresh the DB (if a model is not null) and the screen
	 */
	protected void refresh(String source, boolean refreshDB) {
		getRefreshAction().setModel(refreshDB ? "RefreshDB" : null);
		getRefreshAction().actionPerformed(new ActionEvent(this, 0, "RefreshAction - " + source));
	}

	private XAction getRefreshAction() {
		return ((CourtListingOutline) listOutline).getRefreshAction();
	}

	protected void refreshDBOnly(String source) {
		getRefreshAction().setModel("RefreshDB" );
		getRefreshAction().actionPerformed(new ActionEvent(this, -1, "RefreshAction - " + source));
	}
	
	private boolean isExiting() {
		return getSaveAction() == null;
	}

	private XAction getSaveAction() {
		return ((CourtListingOutline) listOutline).getSaveAction();
	}

	private XAction getRefreshFixturesAction() {
		return ((CourtListingOutline) listOutline).getRefreshFixturesAction();
	}

	/**
	 * Save action
	 */
	protected void save(String source) {
		getSaveAction().actionPerformed(new ActionEvent(this, 0, "SaveAction - " + source));
	}

	protected void refreshFixtures(String source) {
		getRefreshFixturesAction().actionPerformed(new ActionEvent(this, 0, "RefreshFixturesAction - " + source));
	}

	
	protected abstract class ReloadObjectAction extends XAction {
		private static final long serialVersionUID = 1L;
		private final String source;

		public ReloadObjectAction(String actionName, String source) {
			super(actionName);
			this.source = source;
		}

		protected abstract void getLatestValues();
		protected abstract boolean isValid();
		protected abstract void doAction() throws Exception; 

		@Override
		public void xActionPerformed(ActionEvent ae) throws Exception {
			getLatestValues();
			if (!isExiting()) {
				refreshOutline(source);
				if (isValid()) {
					doAction();
				} else {
					showInvalidStructureMsg();
				}
			}
		}
	}
}
