package uk.gov.courtservice.xhibit.client.actions.listdistribution;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseMaintain;
import uk.gov.courtservice.xhibit.client.listings.casesummary.CaseSummaryModel;
import uk.gov.courtservice.xhibit.client.listings.nonavailabledays.NonAvailableDaysDialog;
import uk.gov.courtservice.xhibit.client.listings.nonavailabledays.NonAvailableDaysModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class NonAvailableDaysAction extends XAction {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public NonAvailableDaysAction() {
		populateFromBundle("NonAvailableDays");
	}

	/**
	 * If case has already been loaded, display the screen for that case, else invoke case search popup
	 * to search for a case and then load it into the Non Available Days screen
	 */
	public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
		XhibitApplicationController xac = (XhibitApplicationController)getController();

		if(xac.getApplicationCaseModel() != null && xac.getApplicationCaseModel().getCaseId() > 0)		{
			if (!CaseSummaryModel.ValidValues.CASE_TYPES.contains(xac.getApplicationCaseModel().getCaseType())) {				
				XMessageBox.alert(xac, getErrorResourceBundle("validation.caseType.title"), true,
					XMessageBox.ICONERROR, getErrorResourceBundle("validation.caseType.TSA"), XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			} else {
				displayNonAvailableDaysDialog(xac, xac.getApplicationCaseModel().getCaseId());
			}
		}
		else 		{
			// Call Case Search Popup
	    	CaseMaintain searchController = new CaseMaintain(xac, NonAvailableDaysModel.ValidValues.CASE_TYPES);
	    	// Call the Non Available Days screen 
	    	if (searchController.getCaseId() > 0) {
	    		displayNonAvailableDaysDialog(xac, searchController.getCaseId());
	    	}
	    } 
	}
	
	/**
	 * Display the Non Available Days screen
	 * @param xac XhibitApplicationController
	 * @param caseId Identifier of case record to load
	 * @throws CSRecoverableException
	 */
	private void displayNonAvailableDaysDialog(XhibitApplicationController xac, Integer caseId) throws CSRecoverableException{
		NonAvailableDaysModel nonAvailableDaysModel = new NonAvailableDaysModel(caseId);
		NonAvailableDaysDialog nonAvailableDaysDialog = new NonAvailableDaysDialog(xac, nonAvailableDaysModel);
		nonAvailableDaysDialog.setVisible(true);
		nonAvailableDaysDialog.setResizable(true);
	}
	
	private String getErrorResourceBundle(String resourceKey) {
		return XHIBITConstant.getResource(XhibitBundles.ErrorText, resourceKey); 
	}
}