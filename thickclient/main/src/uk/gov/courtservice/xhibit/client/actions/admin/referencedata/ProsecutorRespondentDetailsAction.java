package uk.gov.courtservice.xhibit.client.actions.admin.referencedata;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.casemanagement.ProsecutorRespondentSearchDialog;
import uk.gov.courtservice.xhibit.client.casemanagement.ProsecutorRespondentSearchModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class ProsecutorRespondentDetailsAction extends XAction {

	private static final long serialVersionUID = 1L;

	public ProsecutorRespondentDetailsAction() {
		populateFromBundle("ProsecutorRespondentDetails");
	}

	public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
		XhibitApplicationController xac = (XhibitApplicationController) getController();
		xac.close();
		try {
			ProsecutorRespondentSearchDialog prosRespSearchDialog = new ProsecutorRespondentSearchDialog(xac,
					new ProsecutorRespondentSearchModel(xac, null));
			prosRespSearchDialog.setLocationRelativeTo(xac);
			prosRespSearchDialog.setVisible(true);
		} catch (CSRecoverableException e1) {
		}
	}

}