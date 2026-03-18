package uk.gov.courtservice.xhibit.client.actions.listdistribution;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseMaintain;
import uk.gov.courtservice.xhibit.client.listings.details.CaseListingDetailDialog;
import uk.gov.courtservice.xhibit.client.listings.details.CaseListingDetailModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class CaseListingEntryAction extends XAction {

	private static final long serialVersionUID = 1L;

	private XhibitApplicationController xac;
	private CaseMaintain searchController;
	private Integer caseId = Integer.valueOf(0);
	private boolean exitImmediately = false;
	
	public CaseListingEntryAction() {
        populateFromBundle("CaseListingEntry");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
    	this.xac = (XhibitApplicationController) getController();

    	if(xac.getApplicationCaseModel() != null && xac.getApplicationCaseModel().getCaseId() > 0) {
			if (!CaseListingDetailModel.ValidValues.CASE_TYPES.contains(xac.getApplicationCaseModel().getCaseType())) {				
				XMessageBox.alert(xac, getErrorResourceBundle("validation.caseType.title"), true,
					XMessageBox.ICONERROR, getErrorResourceBundle("validation.caseType.TSA"), XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			} else {    		
	    		// A case is already open so use it
	    		caseId = xac.getApplicationCaseModel().getCaseId();
	    		callDetailScreen(false);
			}
		} else {
			// For those awkward processes where we want to close the screen and not return to the search
			exitImmediately = false;
			//Repeat screen asking for a case Id until we don't have a valid case Id.
			//Do while loop since we don't have a case Id first time around.
			do {
				if (exitImmediately) {break;}
				showSearchScreen();
				callDetailScreen(true);
			} while (hasValidCaseId());
		}
    } 
        
	private void showSearchScreen() throws CSRecoverableException {
		this.searchController = new CaseMaintain(this.xac, CaseListingDetailModel.ValidValues.CASE_TYPES);
		caseId = searchController.getCaseId();
	}

	private boolean hasValidCaseId() {
		return caseId != null && !Integer.valueOf(0).equals(caseId);
	}

	private void callDetailScreen(boolean isChildScreen) throws CSRecoverableException {
		if (hasValidCaseId()) {
			if (isChildScreen) {
				CaseListingDetailModel model = new CaseListingDetailModel(xac, caseId,
						(XhibitSingleton.getInstance().getCourtId()));
				CaseListingDetailDialog dialog;
				dialog = new CaseListingDetailDialog(xac, model);
				dialog.setVisible(true);
				if (model.isExitImmediately()) {
					exitImmediately = true;
				}
			} else {
				// Fire the event to call the Case Listing Detail Action
				XAction action = XhibitActions.getAction(this.xac, XhibitActions.CaseListingDetail);
				action.setModel(new CaseListingDetailModel(xac, caseId, (XhibitSingleton.getInstance().getCourtId())));
				action.actionPerformed(new ActionEvent(this.xac, 0, "Call CaseListingDetailAction"));
			}
		}
	}
    	
    
	private String getErrorResourceBundle(String resourceKey) {
		return XHIBITConstant.getResource(XhibitBundles.ErrorText, resourceKey); 
	}	
}