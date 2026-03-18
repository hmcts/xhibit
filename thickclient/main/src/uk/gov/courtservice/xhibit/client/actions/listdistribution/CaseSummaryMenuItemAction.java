package uk.gov.courtservice.xhibit.client.actions.listdistribution;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseMaintain;
import uk.gov.courtservice.xhibit.client.listings.casesummary.CaseSummaryDialog;
import uk.gov.courtservice.xhibit.client.listings.casesummary.CaseSummaryModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class CaseSummaryMenuItemAction extends XAction {
	
	private static final long serialVersionUID = 1L;

	public CaseSummaryMenuItemAction()
	{
		populateFromBundle("CaseSummary");
	}

	@Override
	public void xActionPerformed(ActionEvent e) throws Exception {
		XhibitApplicationController xac = (XhibitApplicationController) getController(); 
		
		if(xac.getApplicationCaseModel() != null && xac.getApplicationCaseModel().getCaseId() > 0)		{
			if (!CaseSummaryModel.ValidValues.CASE_TYPES.contains(xac.getApplicationCaseModel().getCaseType())) {				
				XMessageBox.alert(xac, getErrorResourceBundle("validation.caseType.title"), true,
					XMessageBox.ICONERROR, getErrorResourceBundle("validation.caseType.TSA"), XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			} else {
				displayCaseSummaryDialog(xac, xac.getApplicationCaseModel().getCaseId());
			}
		}
		else 		{
			// Call Case Search Popup
	    	CaseMaintain searchController = new CaseMaintain(xac, CaseSummaryModel.ValidValues.CASE_TYPES);
	    	// Call the case summary screen 
	    	if (searchController.getCaseId() > 0) {
	    		displayCaseSummaryDialog(xac, searchController.getCaseId());
	    	}
	    } 
	}
	
	private void displayCaseSummaryDialog(XhibitApplicationController xac, Integer caseId) throws CSRecoverableException{
		CaseSummaryModel model = new CaseSummaryModel(caseId);
		CaseSummaryDialog dialog = new CaseSummaryDialog(xac, model);
		dialog.setVisible(true);
	}
	
	private String getErrorResourceBundle(String resourceKey) {
		return XHIBITConstant.getResource(XhibitBundles.ErrorText, resourceKey); 
	}	
}
