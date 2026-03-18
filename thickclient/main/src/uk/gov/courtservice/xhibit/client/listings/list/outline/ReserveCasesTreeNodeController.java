package uk.gov.courtservice.xhibit.client.listings.list.outline;

import java.util.Date;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.netbeans.swing.outline.Outline;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;
import uk.gov.courtservice.xhibit.client.listings.CaseOnListType;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListModel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;


/**
 * Controller for tree nodes that have reserve cases.
 * 
 * @author uphillj
 *
 */
public class ReserveCasesTreeNodeController extends AbstractTreeNodeController<ReserveCasesTreeNodeModel> {

	private static final long serialVersionUID = 1L;
	private static final String Reserve = "Reserve";

	public ReserveCasesTreeNodeController(TreeNodeFactory treeNodeFactory, XDialog listDialog, ListModel listModel, Outline listOutline, DefaultTreeModel listTreeModel) {
		super(treeNodeFactory, ReserveCasesTreeNodeModel.class, listDialog, listModel, listOutline, listTreeModel);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class<CaseTreeNodeModel> getChildModelType() {
		return CaseTreeNodeModel.class;
	}
	
	/**
	 * Validate that when moving a case no structural changes have occurred when refreshed before a drop.
	 */
	@Override
	public boolean validateStructure(DefaultMutableTreeNode childNode) {	
		boolean valid = false;
		
		final CaseTreeNodeController caseController = (CaseTreeNodeController) OutlineUtils.getTreeNodeController(childNode);
		final CaseOnListComplexValue caseBeforeRefresh = caseController.getModel().getCaseOnList();
		final CaseOnListComplexValue caseAfterRefresh = getCaseAfterRefresh(caseBeforeRefresh);
		
		// now start to validate no structural changes have occurred
		if(caseBeforeRefresh != null){
			if (validateCaseOnListStructure(caseBeforeRefresh, caseAfterRefresh)) {
				valid = true;
			}
		}
				
		if(!valid){
			// if the structure has change in the part of the list the user is amending/dropping
			showInvalidStructureMsg();
    		// Fire refresh of the tree from the list model in memory
    		refreshOutline("validateStructure");
		} else {
			// Update child with the latest version
			caseController.setControllerCaseFromListModel(caseController.getModel().getCaseOnList().getCaseOnListId());
		}
		return valid;
	}

	@Override
	protected boolean dropChild(DefaultMutableTreeNode childNode, int index) {
		boolean dropChild = true;
		
		// Get the controller for the child case node being dropped
		final CaseTreeNodeController caseController = (CaseTreeNodeController) OutlineUtils.getTreeNodeController(childNode);
		final CaseOnListComplexValue caseOnList = caseController.getModel().getCaseOnList();

		// Get the start and end date of the list
		final Date listStartDate = getListModel().getListStartDate().getTime();
		final Date listEndDate = getListModel().getListEndDate().getTime();

		// Update list model with either new case or moved case
		if(caseOnList.getCaseOnListId() == null){
			// adding a case and defendants to the list
			final CaseOnListComplexValue newCaseOnList = getListModel().addCase(caseOnList, caseOnList.getCase(), caseOnList.getCaseListingEntry(), caseOnList.getDirectionsForCase(),
																				caseOnList.getHearingType(), CaseOnListType.Reserve, index + 1);

			// add the defendants to the case that are not already listed or fixed
			for (DefOnCaseOnListBasicValue defOnCaseOnList : getDefendantsNotListedOrFixed(caseOnList, listStartDate, listEndDate)) {
				getListModel().addDefendant(newCaseOnList, defOnCaseOnList.getDefendantOnCaseId());
			}

			// Update node model with newly added case on list
			caseController.getModel().setCaseOnList(newCaseOnList);
			
			// If the case has no Hearing Type, present the user with the additional details dialog
			// NB this is the final check to take place as part of a drag and drop
			dropChild = checkAdditionalDetails(newCaseOnList);
		}
		else{
			// if moving the case into the reserve folder from elsewhere on the list,
			// remove the defendants from the case that are already listed or fixed
			if (!YES.equals(caseOnList.getReserved())) {
				for (DefOnCaseOnListBasicValue defOnCaseOnList : getDefendantsListedOrFixed(caseOnList, listStartDate, listEndDate)) {
					getListModel().removeDefendant(caseOnList, defOnCaseOnList.getDefendantOnCaseId());
				}
			}

			// moving a case within the list
			getListModel().moveCase(caseOnList, CaseOnListType.Reserve, index + 1);
		}

		return dropChild;
	}	
	
	@Override
	protected boolean validateChild(DefaultMutableTreeNode childNode, int index) {
		boolean validChild = super.validateChild(childNode, index);

		// Get the controller for the child case node being dropped
		final CaseTreeNodeController caseController = (CaseTreeNodeController) OutlineUtils.getTreeNodeController(childNode);
		final CaseOnListComplexValue caseOnList = caseController.getModel().getCaseOnList();

		// Get the start and end date of the list
		final Date listStartDate = getListModel().getListStartDate().getTime();
		final Date listEndDate = getListModel().getListEndDate().getTime();
		
		// validate not dropping a fixture as that immediately cancels the drop
		validChild = validateNotFixture(caseOnList.getCaseDiaryFixtureId());
		
		// check if moving from LHS so do only LHS to RHS specific validation	
		if(validChild && caseOnList.getCaseOnListId() == null){
			// moving from LHS onto the list
			// validation of case state which could result in the user having no choice and having to cancel the drop
			if(validChild){
				validChild = validateStateOfCase(caseOnList.getCaseId());		
			}
		}

		// validation when moving case into the reserve folder, which includes drop from LHS
		if (validChild && !YES.equals(caseOnList.getReserved())) {
			// validate if case already listed or fixed
			if(validChild){
				if(isMainCaseType(caseOnList.getCase().getCaseType())){
					validChild = validateDefendants(caseOnList, listStartDate, listEndDate);
				}
				else{
					validChild = validateCaseListed(caseOnList.getCaseId(), caseOnList.getCaseOnListId());
				}
			}		
	
			// validate non available days for case
			if(validChild){
				validChild = validateNonAvailableDays(caseOnList.getCaseId(), listStartDate, listEndDate);
			}
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
	 * Get the list of defendants on the case on list that are not already listed anywhere on the list
	 * and which are not marked as attending on any fixture which has yet to be dragged onto the list.
	 * 
	 * @param caseOnList
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	protected List<DefOnCaseOnListBasicValue> getDefendantsNotListedOrFixed(CaseOnListComplexValue caseOnList, Date fromDate, Date toDate) {
		// Get the defendants that could be listed as they are not currently listed
		List<DefOnCaseOnListBasicValue> defendants = getDefendantsNotListed(caseOnList);

		// Further restrict the defendants to exclude the ones that are already fixed
		defendants = getDefendantsNotFixed(caseOnList.getCaseId(), fromDate, toDate, defendants);
		
		return defendants;
	}

	/**
	 * Get the list of defendants on the case on list that are already listed anywhere on the list
	 * and which are marked as attending on any fixture which has yet to be dragged onto the list.
	 * 
	 * @param caseOnList
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	protected List<DefOnCaseOnListBasicValue> getDefendantsListedOrFixed(CaseOnListComplexValue caseOnList, Date fromDate, Date toDate) {
		// Get the defendants that are currently listed
		List<DefOnCaseOnListBasicValue> defendants = getDefendantsListed(caseOnList);

		// Add the defendants that are fixed but not listed
		defendants.addAll(getDefendantsFixed(caseOnList.getCaseId(), fromDate, toDate, getDefendantsNotListed(caseOnList)));
		
		return defendants;
	}

	@Override
	public String getControllerId() {
		return Reserve;
	}

	/**
	 * Returns true if any of the defendants on the case on list can be listed.
	 * 
	 * @param caseOnList
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	protected boolean validateDefendants(CaseOnListComplexValue caseOnList, Date fromDate, Date toDate) {
		boolean validChild = true;
		String errorMsg = null;

		// If there are defendants (e.g. B and U cases have no defendants)
		if (!caseOnList.getDefOnCaseOnLists().isEmpty()) {
			// Get the defendants that could be listed as they are not currently listed
			List<DefOnCaseOnListBasicValue> defendants = getDefendantsNotListed(caseOnList);
			
			// If all the defendants on the case on list are already listed, then cannot list the case
			if (defendants.isEmpty()) {
				errorMsg = XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationDefendantCaseMove");
				validChild = false;
			}
			// Else check that the available defendants are not on fixtures that have yet to be listed
			else {
				// Get the defendants that are not fixed from the ones that could be listed
				defendants = getDefendantsNotFixed(caseOnList.getCaseId(), fromDate, toDate, defendants);
				
				// If all the defendants that could be listed are already fixed, then cannot list the case
				if (defendants.isEmpty()) {
					errorMsg = XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationReserveFixturesExistMove");
					validChild = false;
				}
			}
		}
		
		if (!validChild) {
			XMessageBox.alert(getListDialog(),
					XHIBITConstant.getResource(XhibitBundles.Listings, "listMoveErrorTitle"), true,
					XMessageBox.ICONERROR, errorMsg, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
		}
		
		return validChild;
	}
	
	/**
	 * Returns true if the case is not currently listed in the list model
	 * on a different case on list to the one supplied which will be null
	 * when a case is first dragged into the list.
	 * 
	 * @param caseId
	 * @param caseOnListId
	 * @return
	 */
	protected boolean validateCaseListed(Integer caseId, Integer caseOnListId) {
		boolean validChild = true;

		if (getListModel().isCaseListedElsewhere(caseId, caseOnListId)) {
			String errorMsg = XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationReserveCaseMove");
			XMessageBox.alert(getListDialog(),
					XHIBITConstant.getResource(XhibitBundles.Listings, "listMoveErrorTitle"), true,
					XMessageBox.ICONERROR, errorMsg, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			validChild = false;
		}
		
		return validChild;
	}
	
	/**
	 * Returns true if the case diary fixture id is null, i.e.
	 * the case has not been dragged from fixtures for today.
	 * 
	 * @param caseId
	 * @return
	 */
	protected boolean validateNotFixture(Integer caseDiaryFixtureId) {
		boolean validChild = true;

		if (caseDiaryFixtureId != null) {
			String errorMsg = XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationReserveFixtureMove");
			XMessageBox.alert(getListDialog(),
					XHIBITConstant.getResource(XhibitBundles.Listings, "listMoveErrorTitle"), true,
					XMessageBox.ICONERROR, errorMsg, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			validChild = false;
		}
		
		return validChild;
	}	
}
