package uk.gov.courtservice.xhibit.client.actions.casemanagement;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseMaintain;
import uk.gov.courtservice.xhibit.client.casemanagement.ManageCase;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationControllerImpl;

public class AddOffencesOrChargesAction extends XAction {

	public AddOffencesOrChargesAction() {
		populateFromBundle("AddOffenceOrCharges");
	}
	
	@Override
	public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
		if (getController() != null) {
			if (getModel() != null) {
				ChargesController cc = new ChargesController((ApplicationCaseModel) getModel());
				((XhibitApplicationController) getController()).open(cc);
			}
		}
	}
}
