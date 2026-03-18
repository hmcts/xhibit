package uk.gov.courtservice.xhibit.client.actions.casemanagement;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.client.casemanagement.REDEL;
import uk.gov.courtservice.xhibit.client.casemanagement.REDELDialog;
import uk.gov.courtservice.xhibit.client.casemanagement.REDELModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

public class ReplaceDeleteDeftAction extends XAction {

	public ReplaceDeleteDeftAction() {
		populateFromBundle("ReplaceDeleteDeft");
	}

	public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
		XhibitApplicationController xac = (XhibitApplicationController) getController();

		REDEL redel = new REDEL(xac);
		if (redel.getCaseId() != null) {
			if (redel.getCaseId() > 0) {
				// returns 0 by default so just
				// null check wasn't enough
				CaseBasicValue basic = XhibitDelegateHelper.getCaseDelegate().getCase(redel.getCaseId());
				REDELModel redelModel = new REDELModel();
				redelModel.setCaseBasicValue(basic);
				REDELDialog redelDialog = new REDELDialog(xac, redelModel);
				redelDialog.setVisible(true);
			}
		}

	}

}
