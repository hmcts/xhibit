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
import uk.gov.courtservice.xhibit.client.listings.list.warned.WarnedListModel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * Controller for tree nodes that have no date cases.
 * 
 * @author uphillj
 *
 */
public class NoDateCasesTreeNodeController extends AbstractTreeNodeController<NoDateCasesTreeNodeModel> {

	private static final long serialVersionUID = 1L;
	private static final String NoDate = "NoDate";	
	
	protected List<CaseOnListComplexValue> listOfCasesOnNodeBeforeARefresh;
	
	public NoDateCasesTreeNodeController(TreeNodeFactory treeNodeFactory, XDialog listDialog, ListModel listModel, Outline listOutline, DefaultTreeModel listTreeModel) {
		super(treeNodeFactory, NoDateCasesTreeNodeModel.class, listDialog, listModel, listOutline, listTreeModel);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Class<CaseTreeNodeModel> getChildModelType() {
		return CaseTreeNodeModel.class;
	}
	
	@Override
	public void setOriginalValues(DefaultMutableTreeNode childNode) {
		final WarnedListModel warnedListModel = (WarnedListModel) this.getListModel(); 
		listOfCasesOnNodeBeforeARefresh = warnedListModel.getCasesOnListForNoDate(this.getModel().getCourtSite().getCourtSiteId());			
	}
	
	@Override
	public String getControllerId() {
		if (this.getModel().getCourtSite() != null && 
				this.getModel().getCourtSite().getCourtSiteId() != null) {
			return NoDate + this.getModel().getCourtSite().getCourtSiteId().toString();
		} 
		return EMPTY_STRING;
	}

	/**
	 * Validate that when moving a case within the no date node no structural changes have occurred when refreshed before a drop.
	 */
	@Override
	public boolean validateStructure(DefaultMutableTreeNode childNode) {	
		boolean valid = false;
		final CaseTreeNodeController caseController = (CaseTreeNodeController) OutlineUtils.getTreeNodeController(childNode);
		final CaseOnListComplexValue caseBeforeRefresh = caseController.getModel().getCaseOnList();		
		final CaseOnListComplexValue caseAfterRefresh = getCaseAfterRefresh(caseBeforeRefresh);	
				
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

		// Update list model with either new case or moved case
		final CaseOnListComplexValue caseOnList = caseController.getModel().getCaseOnList();
		if(caseOnList.getCaseOnListId() == null){
			// adding a case and defendants to the list
			final CaseOnListComplexValue newCaseOnList = getListModel().addCase(caseOnList, caseOnList.getCase(), caseOnList.getCaseListingEntry(), caseOnList.getDirectionsForCase(),
																				caseOnList.getHearingType(), CaseOnListType.NoDate, index + 1, getModel().getCourtSite());
			
			// add the defendants to the case that are not already listed
			for (DefOnCaseOnListBasicValue defOnCaseOnList : getDefendantsNotListed(caseOnList, getListModel().getListStartDate().getTime())) {
				getListModel().addDefendant(newCaseOnList, defOnCaseOnList.getDefendantOnCaseId());
			}
			caseController.getModel().setCaseOnList(newCaseOnList);
			
			// If the case has no Hearing Type, present the user with the additional details dialog
			// NB this is the final check to take place as part of a drag and drop
			dropChild = checkAdditionalDetails(newCaseOnList);
		}
		else{
			// moving a case within the list
			getListModel().moveCase(caseOnList, CaseOnListType.NoDate, index + 1, getModel().getCourtSite());
		}
		
		return dropChild;
	}	
	
	@Override
	protected boolean validateChild(DefaultMutableTreeNode childNode, int index) {
		boolean validChild = super.validateChild(childNode, index);
		
		// Get the start and end date of the list
		final Date listStartDate = getListModel().getListStartDate().getTime();
		final Date listEndDate = getListModel().getListEndDate().getTime();
		
		// Get the controller for the child case node being dropped
		final CaseTreeNodeController caseController = (CaseTreeNodeController) OutlineUtils.getTreeNodeController(childNode);
		final CaseOnListComplexValue caseOnList = caseController.getModel().getCaseOnList();
		
		// Get the court site id and the case's current court site id
		final Integer courtSiteId = this.getModel().getCourtSite().getCourtSiteId();
		final Integer caseCurrentSiteId = caseOnList.getCourtSiteId();
		
		// check if moving from LHS so do only LHS to RHS specific validation		
		if (validChild && caseOnList.getCaseOnListId() == null ) {
			// moving from LHS onto the list
			
			// this validation of case state will be done first as could result in the user having no choice and having to cancel the drop
			validChild = validateStateOfCase(caseOnList.getCase().getCaseId());
			
			// validate fixtures for case as that immediately cancels the drop
			if(validChild){
				validChild = validateNoFixtures(caseOnList.getCaseId(), listStartDate, listEndDate, XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationNoDateFixturesExistMove"));
			}
			
			// validate if case already listed
			// use startDate of warn list as that is what saved with for this type of list
			if(validChild){
				if(isMainCaseType(caseOnList.getCase().getCaseType())){
					validChild = validateDefendants(caseOnList, listStartDate, XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationDefendantCaseMove"));
				}
				else{
					validChild = validateCaseListed(caseOnList, listStartDate, XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationDefendantCaseMove"));							
				}
			}	

			// validate non available days for case
			if(validChild){
				validChild = validateNonAvailableDays(caseOnList.getCaseId(), listStartDate, listEndDate);
			}				
		}
		
		// validation when changing court site, which includes drop from LHS as no court site when adding
		if (validChild && !courtSiteId.equals(caseCurrentSiteId)) {
			validChild = validateCourtSite(caseOnList, courtSiteId, caseCurrentSiteId);
		}
		
		// If not valid then refresh the display with latest model
		if (!validChild) {
			refreshOutlineOnlyOnce("ValidateChild");
		}

		return validChild;
	}

}
