package uk.gov.courtservice.xhibit.client.actions.casemanagement;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.migration.MigrationDetail;
import uk.gov.courtservice.xhibit.business.services.migration.MigrationMessageType;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseMigratedPopup;
import uk.gov.courtservice.xhibit.client.casemanagement.DeleteCase;
import uk.gov.courtservice.xhibit.client.casemanagement.DeleteCaseDialog;
import uk.gov.courtservice.xhibit.client.casemanagement.DeleteCaseModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

public class DeleteCaseAction extends XAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeleteCaseAction() {
		populateFromBundle("DeleteCase");
	}

	public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
		XhibitApplicationController xac = (XhibitApplicationController) getController();

		DeleteCase deleteCase = new DeleteCase(xac);
		if (deleteCase.getCaseId() != null) {
			if (deleteCase.getCaseId() > 0) {
				// returns 0 by default so just null check wasn't enough
				// XDMX8
				MigrationDetail migrationDetail = XhibitDelegateHelper.getMigrateCaseDelegate().getMigrationDetails(deleteCase.getCaseId(), MigrationMessageType.DELETE);
				
				if (migrationDetail != null && migrationDetail.isMigrated) {
					new CaseMigratedPopup(xac, migrationDetail.getMigrationTo()).setVisible(true);
				} else {
					CaseBasicValue basic = XhibitDelegateHelper.getCaseDelegate().getCase(deleteCase.getCaseId());
					DeleteCaseModel caseDeleteModel = new DeleteCaseModel();
					caseDeleteModel.setCaseBasicValue(basic);
					DeleteCaseDialog caseDeleteDialog = new DeleteCaseDialog(xac, caseDeleteModel);
					caseDeleteDialog.setVisible(true);
				}
			}
		}

	}

}
