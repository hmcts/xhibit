package uk.gov.courtservice.xhibit.client.actions.listdistribution;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.migration.MigrationDetail;
import uk.gov.courtservice.xhibit.business.services.migration.MigrationMessageType;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseMigratedPopup;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseSearchDialog;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseSearchModel;
import uk.gov.courtservice.xhibit.client.casemanagement.caselinking.CaseLinkingDialog;
import uk.gov.courtservice.xhibit.client.casemanagement.caselinking.CaseLinkingModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

public class NewLinkCasesAction extends XAction {

	private static final long serialVersionUID = 1L;
	
    private CaseSearchModel caseSearchModel;
    private CaseSearchDialog caseSearchDialog;
    private XhibitApplicationController xac; 
    private Integer caseId;

	private CaseLinkingModel caseLinkingModel;
    private CaseLinkingDialog caseLinkingDialog;
    
	public NewLinkCasesAction() {
		populateFromBundle("NewLinkCases");
	}

	public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
		xac = (XhibitApplicationController)getController();
		
		// call close before opening link cases
		xac.close();
		
		// initialise and open case search dialog
		caseSearchModel = new CaseSearchModel();
		caseSearchModel.setCaseLinking(true);
		caseSearchDialog = new CaseSearchDialog(xac, caseSearchModel);
		caseSearchDialog.setVisible(true);
		
		// return the found case's case id
		caseId = caseSearchModel.getCaseId();
		
		// if a valid case was returned
		if (caseId > 0) {
			// XDMX10
			MigrationDetail migrationDetail = XhibitDelegateHelper.getMigrateCaseDelegate().getMigrationDetails(caseId, MigrationMessageType.EDIT);
			
			if (migrationDetail != null && migrationDetail.isMigrated) {
				new CaseMigratedPopup(xac, migrationDetail.getMigrationTo()).setVisible(true);
			} else {
				caseLinkingModel = new CaseLinkingModel();
				caseLinkingModel.setCaseId(caseId);
				caseLinkingDialog = new CaseLinkingDialog(xac, caseLinkingModel);
				caseLinkingDialog.setVisible(true);
			}
		}
	}
}