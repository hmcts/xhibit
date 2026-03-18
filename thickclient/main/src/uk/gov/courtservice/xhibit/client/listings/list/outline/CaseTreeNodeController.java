package uk.gov.courtservice.xhibit.client.listings.list.outline;

import java.awt.datatransfer.Transferable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.netbeans.swing.outline.Outline;

import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.listings.casesummary.CaseSummaryDialog;
import uk.gov.courtservice.xhibit.client.listings.casesummary.CaseSummaryModel;
import uk.gov.courtservice.xhibit.client.listings.details.CaseListingDetailDialog;
import uk.gov.courtservice.xhibit.client.listings.details.CaseListingDetailModel;
import uk.gov.courtservice.xhibit.client.listings.list.additional.AdditionalDetailsDialog;
import uk.gov.courtservice.xhibit.client.listings.list.additional.AdditionalDetailsModel;
import uk.gov.courtservice.xhibit.client.listings.list.common.CourtListingTransferableCase;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListModel;
import uk.gov.courtservice.xhibit.client.listings.list.common.RemoveCaseFromListDialog;
import uk.gov.courtservice.xhibit.client.listings.list.common.RemoveCaseFromListModel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * Controller for tree nodes that display cases.
 * 
 * @author uphillj
 * @amend groenmg - add pop menu functionality
 * @amend groenmg - ctx-1192 - validation regards removing a list from a case.
 */
public class CaseTreeNodeController extends AbstractTreeNodeController<CaseTreeNodeModel> {

	private static final long serialVersionUID = 1L;
	
	private JPopupMenu casePopupMenu;
	private final List<String> SUPPORTED_CONTEXT_MENU_CASE_TYPES = Arrays.asList("T", "S", "A");

	public CaseTreeNodeController(TreeNodeFactory treeNodeFactory, XDialog listDialog, ListModel listModel, Outline listOutline, DefaultTreeModel listTreeModel) {
		super(treeNodeFactory, CaseTreeNodeModel.class, listDialog, listModel, listOutline, listTreeModel);
	}

	@Override
	public boolean isDragSupported() {
		return true;
	}

	@Override
	public Transferable createTransferable() {
		return new CourtListingTransferableCase(getTreeNodeFactory(), getTreeNode());
	}
	
	@Override
	public JPopupMenu getPopupMenu() {
		if (casePopupMenu == null) {
    		casePopupMenu = new JPopupMenu();
    		casePopupMenu.add( new AdditionalDetailsAction());
    		casePopupMenu.add( new RemoveCaseFromListAction());
    		if(isSupportedForPopUp()){
    			casePopupMenu.add( new CaseEntryListAction());
    			casePopupMenu.add( new CaseSummaryListAction());
    		}    		
    	}
    	return casePopupMenu;
	}
	
	private boolean isSupportedForPopUp() {
		String caseType = CaseTreeNodeController.this.getModel().getCaseOnList().getCase().getCaseType();
		for (final String supported : SUPPORTED_CONTEXT_MENU_CASE_TYPES) {
			if (supported.equals(caseType)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the latest version of a case.
	 * 
	 * @param caseOnListId
	 * @return
	 */
	protected CaseOnListComplexValue getLatestValueFromModel(Integer caseOnListId){
		Collection<CaseOnListComplexValue> refreshedValues = getListModel().getCasesOnList();
		CaseOnListComplexValue refreshedValue = null;
		for(CaseOnListComplexValue latestValue : refreshedValues){
			if(latestValue.getCaseOnListId().equals(caseOnListId)){
				refreshedValue = latestValue;
				break;
			}
		}
		return refreshedValue;
	}

	/**
	 * Set the caseOnList to the caseOnList from the list model
	 * 
	 * @param caseOnListId
	 */
	protected void setControllerCaseFromListModel(Integer caseOnListId) {
		if (caseOnListId != null) {
    		CaseOnListComplexValue caseOnList = getLatestValueFromModel(caseOnListId);
    		CaseTreeNodeController.this.getModel().setCaseOnList(caseOnList);
		}
	}

	@Override
	public String getControllerId() {
		if (this.getModel().getCaseOnList() != null && 
				this.getModel().getCaseOnList().getCaseOnListId() != null) {
			return this.getModel().getCaseOnList().getCaseOnListId().toString();
		}
		return EMPTY_STRING;
	}

	@Override
	public boolean validateStructure(DefaultMutableTreeNode childNode) {
		boolean valid = false;
		
		final CaseOnListComplexValue caseBeforeRefresh = getModel().getCaseOnList();
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
			setControllerCaseFromListModel(getModel().getCaseOnList().getCaseOnListId());
		}
		return valid;
	}

	private class RemoveCaseFromListAction extends ReloadCaseAction {

        private static final long serialVersionUID = 1L;

        public RemoveCaseFromListAction() {
            super("ListingRemoveCaseFromList");
        }

		@Override
		protected void doAction() throws Exception {
			boolean removeCase = false;
			if ((getListModel().hasListPublished() && getListModel().isListFinal()) || 
					(getListModel().getListType().isWarned() && (isTrialHearingCode(CaseTreeNodeController.this.getModel().getCaseOnList())))){
				
				// display a pop up to confirm and capture the remove case from list reason as required
				RemoveCaseFromListModel removeCaseModel = new RemoveCaseFromListModel(CaseTreeNodeController.this.getModel().getCaseOnList());
				removeCaseModel.setHearingTypeCode(CaseTreeNodeController.this.getModel().getCaseOnList().getHearingType().getHearingTypeCode());
				RemoveCaseFromListDialog caseListingDeleteFixtureDialog = new RemoveCaseFromListDialog(getListDialog(), removeCaseModel);
				caseListingDeleteFixtureDialog.setVisible(true);
				
				// do we need to remove the case?
				removeCase = removeCaseModel.getCaseOnListComplexValue().isDirty();
			}
			// Else show a standard warning
			else {
				removeCase = XMessageBox.alert(getListDialog(), 
						XHIBITConstant.getResource(XhibitBundles.Listings, "listingRemoveCaseTitle"),  
						true,
						XMessageBox.ICONQUESTION, 
						XHIBITConstant.getResource(XhibitBundles.Listings, "listingRemoveCaseWarningMessage"), 
						XMessageBox.YESNO,
						XMessageBox.DEFAULTNO);
			}
			
			if(removeCase){
				getListModel().removeCase(CaseTreeNodeController.this.getModel().getCaseOnList());
				save("RemoveCaseFromListAction");
				refreshFixtures("RemoveCaseFromListAction");
			}

		}		
		
		private boolean isTrialHearingCode(CaseOnListComplexValue caseOnListComplexValue) throws BisRefControllerException {
			boolean result = false;
			List<String> trialHearingTypes = ListingDropdownPopulation.getRefListingDataValues("PREDEFINED_DELETION_REASON_HEARING_TYPES");
			if (trialHearingTypes != null && !trialHearingTypes.isEmpty()) {
				if ( caseOnListComplexValue.getHearingType() != null) {
					String hearingTypeCode = caseOnListComplexValue.getHearingType().getHearingTypeCode();
					result = trialHearingTypes.contains(hearingTypeCode);
				}
			}
			return result;
		}
    }
    
    private class AdditionalDetailsAction extends ReloadCaseAction {

        private static final long serialVersionUID = 1L;

        public AdditionalDetailsAction() {
            super("ListingCaseAdditionalDetails");
        }

        @Override
        protected void doAction() throws Exception {
    		// create details model populated for use by the details dialog
        	AdditionalDetailsModel detailsModel = getAdditionalDetailsModel(CaseTreeNodeController.this.getModel().getCaseOnList());

        	// open the details dialog
			final AdditionalDetailsDialog dialog = new AdditionalDetailsDialog(getListDialog(), detailsModel);
			dialog.setVisible(true);
			
			// update the case details in the row if saved
			if (detailsModel.isDirty()) {
				save("AdditionalDetailsAction");
			}
        }      
    }
    
    private class CaseEntryListAction extends ReloadCaseAction {

		private static final long serialVersionUID = 1L;

		public CaseEntryListAction() {
			super("ListingCaseListingEntry");
		}

		@Override
		protected void doAction() throws Exception {
			final CaseListingDetailModel model = new CaseListingDetailModel(getListModel().getXac(),
					(CaseTreeNodeController.this.getModel().getCaseOnList().getCaseId()), (XhibitSingleton.getInstance().getCourtId()));
			final CaseListingDetailDialog dialog = new CaseListingDetailDialog(getListModel().getXac(), model, true);
			dialog.setVisible(true);
			// Refresh any changes that happened in the screen
			refreshList("CaseEntryListAction");
		}
    }
    
    private class CaseSummaryListAction extends ReloadCaseAction {

		private static final long serialVersionUID = 1L;
		public CaseSummaryListAction() {
			super("ListingCaseSummary");			
		}

		@Override
		protected void doAction() throws Exception {
			final CaseSummaryModel summaryModel = new CaseSummaryModel(CaseTreeNodeController.this.getModel().getCaseOnList().getCaseId());
			summaryModel.setFromListScreen(true);
			CaseSummaryDialog caseSummaryDialog = new CaseSummaryDialog(getListModel().getXac(), summaryModel);
			caseSummaryDialog.setVisible(true);
			// Refresh any changes that happened in the screen
			refreshList("CaseSummaryListAction");
		}
	}

	private abstract class ReloadCaseAction extends ReloadObjectAction {
		private static final long serialVersionUID = 1L;
		private CaseOnListComplexValue caseOnListBeforeRefresh;

		public ReloadCaseAction(String actionName) {
			super(actionName, "ReloadCaseAction");
		}

		@Override
		protected void getLatestValues() {
			caseOnListBeforeRefresh = CaseTreeNodeController.this.getModel().getCaseOnList();
			final Integer caseOnListId = caseOnListBeforeRefresh.getCaseOnListId();
			refreshDBOnly("ReloadCaseAction");
			setControllerCaseFromListModel(caseOnListId);
		}

		@Override
		protected boolean isValid() {
			CaseOnListComplexValue caseOnListAfterRefresh = CaseTreeNodeController.this.getModel().getCaseOnList();
			boolean valid = validateCaseOnListStructure(caseOnListBeforeRefresh, caseOnListAfterRefresh);
			return valid;
		} 
	}
}


