package uk.gov.courtservice.xhibit.client.listings.list.outline;

import java.util.Date;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.netbeans.swing.outline.Outline;

import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;
import uk.gov.courtservice.xhibit.client.listings.CaseOnListType;
import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractDailyFirmListModel;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListModel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * Controller for tree nodes that have floater cases.
 * 
 * @author uphillj
 *
 * @amend groenmg - ctx-1789 - added validation for in complete case
 * @amend groenmg - ctx-1182 - added functionality to check if hearing 
 * 					type on case when dragging - if not display additional details dialog.
 * @amend groenmg - ctx-1189 - added validation when a case is closed
 * @amend groenmg - ctx-1190 - added validation for bench warrants on a case
 * @amend groenmg - ctx-1188 - added validation for Court Site
 */
public class FloaterCasesTreeNodeController extends AbstractTreeNodeController<FloaterCasesTreeNodeModel> {

	private static final long serialVersionUID = 1L;
	private static final String Floater = "Floater";
	
	protected List<CaseOnListComplexValue> listOfCasesOnFloaterBeforeARefresh;

	public FloaterCasesTreeNodeController(TreeNodeFactory treeNodeFactory, XDialog listDialog, ListModel listModel, Outline listOutline, DefaultTreeModel listTreeModel) {
		super(treeNodeFactory, FloaterCasesTreeNodeModel.class, listDialog, listModel, listOutline, listTreeModel);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class<CaseTreeNodeModel> getChildModelType() {
		return CaseTreeNodeModel.class;
	}
	
	@Override
	public void setOriginalValues(DefaultMutableTreeNode childNode) {
		final AbstractDailyFirmListModel dailyFirmListModel = (AbstractDailyFirmListModel) this.getListModel(); 
		listOfCasesOnFloaterBeforeARefresh = dailyFirmListModel.getCasesOnListForFloater(	this.getModel().getListDate().getTime(), 
																							this.getModel().getCourtSite().getCourtSiteId());		
	}
	
	@Override
	public String getControllerId() {
		if (this.getModel().getCourtSite() != null && 
				this.getModel().getCourtSite().getCourtSiteId() != null) {
			return Floater + this.getModel().getCourtSite().getCourtSiteId().toString();
		} 
		return EMPTY_STRING;
	}
	
	/**
	 * Validate that when moving a case no structural changes have occurred when refreshed before a drop.
	 */
	@Override
	public boolean validateStructure(DefaultMutableTreeNode childNode) {	
		boolean valid = false;
				
		final AbstractDailyFirmListModel dailyFirmListModel = (AbstractDailyFirmListModel) this.getListModel(); 
		final CaseTreeNodeController caseController = (CaseTreeNodeController) OutlineUtils.getTreeNodeController(childNode);
		final CaseOnListComplexValue caseBeforeRefresh = caseController.getModel().getCaseOnList();
		final CaseOnListComplexValue caseAfterRefresh = getCaseAfterRefresh(caseBeforeRefresh);
		
		// list of cases on node dropping to - the floater node
		List<CaseOnListComplexValue> listOfCasesOnFloaterDroppingToAfterRefresh = dailyFirmListModel.getCasesOnListForFloater(	this.getModel().getListDate().getTime(), 
																																this.getModel().getCourtSite().getCourtSiteId());

		// now start to validate no structural changes have occurred
		if(caseBeforeRefresh != null){
			if (validateCaseOnListStructure(caseBeforeRefresh, caseAfterRefresh)) {
				// Check number of cases not changed to where we dropping since data refreshed.
				if(getNumberOfNonObsoleteCasesinList(listOfCasesOnFloaterDroppingToAfterRefresh) == getNumberOfNonObsoleteCasesinList(listOfCasesOnFloaterBeforeARefresh)){			
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

		// Get the date of floater and the case's current date
		final Date floaterDate = getModel().getListDate().getTime();
		final Date caseCurrentDate = caseOnList.getTimeListedDate();

		// Update list model with either new case or moved case
		if(caseOnList.getCaseOnListId() == null){
			// adding the case to the list
			final CaseOnListComplexValue newCaseOnList = getListModel().addCase(caseOnList, caseOnList.getCase(), caseOnList.getCaseListingEntry(), caseOnList.getDirectionsForCase(),
																				caseOnList.getHearingType(), CaseOnListType.Floater, index + 1, getModel().getListDate(), getModel().getCourtSite());

			// add the defendants to the case that are not already listed
			for (DefOnCaseOnListBasicValue defOnCaseOnList : getDefendantsNotListed(caseOnList, floaterDate)) {
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
			if (!YES.equals(caseOnList.getReserved()) && !DateTimeUtilities.isDaySame(floaterDate, caseCurrentDate)) {
				for (DefOnCaseOnListBasicValue defOnCaseOnList : getDefendantsListed(caseOnList, floaterDate)) {
					getListModel().removeDefendant(caseOnList, defOnCaseOnList.getDefendantOnCaseId());
				}
			}

			// move the case in the list model to this floater
			getListModel().moveCase(caseOnList, CaseOnListType.Floater, index + 1, getModel().getListDate(), getModel().getCourtSite());
		}
		
		return dropChild;
	}	
	
	@Override
	protected boolean validateChild(DefaultMutableTreeNode childNode, int index) {
		boolean validChild = super.validateChild(childNode, index);

		// Get the controller for the child case node being dropped
		final CaseTreeNodeController caseController = (CaseTreeNodeController) OutlineUtils.getTreeNodeController(childNode);
		final CaseOnListComplexValue caseOnList = caseController.getModel().getCaseOnList();
		
		// Get the court site id and the case's current court site id
		final Integer courtSiteId = this.getModel().getCourtSite().getCourtSiteId();
		final Integer caseCurrentSiteId = caseOnList.getCourtSiteId();

		// Get the date of floater and the case's current date
		final Date floaterDate = this.getModel().getListDate().getTime();
		final Date caseCurrentDate = caseOnList.getTimeListedDate();
		
		final CaseDiaryFixtureComplexValue fixture = getFixtureAfterRefresh(caseOnList.getCaseDiaryFixtureId());
		
		// check if moving from LHS so do only LHS to RHS specific validation	
		if (validChild && caseOnList.getCaseOnListId() == null) {
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
		if (validChild && !YES.equals(caseOnList.getReserved()) && !DateTimeUtilities.isDaySame(floaterDate, caseCurrentDate)) {
			// validate fixture is moving to correct date
			if(validChild && caseOnList.getCaseDiaryFixtureId() != null){
				validChild = validateFixtureDate(fixture, floaterDate);						 		
			}
			
			// validate if case already listed
			if(validChild){
				if(isMainCaseType(caseOnList.getCase().getCaseType())){
					validChild = validateDefendants(caseOnList, floaterDate, XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationDefendantCaseMove"));
				}
				else{
					validChild = validateCaseListed(caseOnList, floaterDate, XHIBITConstant.getResource(XhibitBundles.Listings, "listValidationDefendantCaseMove"));							
				}
			}

			// validate non available days for case
			if(validChild){
				validChild = validateNonAvailableDays(caseOnList.getCaseId(), floaterDate);
			}	
			
			if (validChild && caseOnList.getCaseOnListId() == null ) {
				// one of last checks and only required for moving from LHS to RHS
				// Need to inform the user if not all defendants on the case are on the caseOnList being moved
				warnIfNotAllCaseDefsBeingListed(caseOnList);
			}
			
		}

		// If not valid then refresh the display with latest model
		if (!validChild) {
			refreshOutlineOnlyOnce("ValidateChild");
		}

		return validChild;
	}

}
