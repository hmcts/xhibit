package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.courtlog.MediumEventMTDialog;
import uk.gov.courtservice.xhibit.client.courtlog.MediumEventMTModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */
public class DefendantSummaryOffencesAction extends XAction {
    private static final String eventCode = "20926";

    private static final String schema = "E20926_Defendant_Name";

    public DefendantSummaryOffencesAction() {
        populateFromBundle("DefendantSummaryOffences");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();

        ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.MaintainCharges);

        MediumEventMTModel model = new MediumEventMTModel();

        String panelText = XHIBITConstant.getResource(resources, "DefendantSummaryOffences.panelText");
        model.setSchema(schema);
        model.setPanelText(panelText);
        model.setEventType(eventCode);
        model.setXac(xac);
        ChargesController cc = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel ccModel = cc.getModel();
        model.setEnteredText(buildDefendantName(ccModel.getDefendantValue()));

        MediumEventMTDialog med = new MediumEventMTDialog((java.awt.Frame) getController(), model);
        med.setVisible(true);

        if (med.isCancelClicked()) {
            throw new UserCancelException();
        }

        if (med.isOkClicked()) {
            // Refresh Charges tab
            cc.loadCharges();
        }
    }

    private String buildDefendantName(DefendantValue defendantValue) {
        StringBuffer buf = new StringBuffer("");

        if (defendantValue != null) {
            buf.append(defendantValue.getFirstName());

            if (defendantValue.getMiddleName() != null && defendantValue.getMiddleName().length() > 0) {
                buf.append(buf.length() > 0 ? " " : "");
                buf.append(defendantValue.getMiddleName());
            }

            if (defendantValue.getSurName() != null && defendantValue.getSurName().length() > 0) {
                buf.append(buf.length() > 0 ? " " : "");
                buf.append(defendantValue.getSurName());
            }
        }

        return buf.toString();
    }
}
