package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.courtlog.DefendantChargesDialog;
import uk.gov.courtservice.xhibit.client.courtlog.DefendantChargesModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
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
public class VoluntaryBillPreferredAction extends XAction {
    private static final String eventCode = "20923";

    private static final String schema = "E20923_Charges_Put_Options";

    public VoluntaryBillPreferredAction() {
        populateFromBundle("VoluntaryBillPreferred");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        ChargesController cc = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel ccModel = cc.getModel();

        DefendantChargesModel model = new DefendantChargesModel();

        model.setSchema(schema);
        model.setEventType(eventCode);
        model.setXac(xac);
        model.setIndictmentNumber(ccModel.getChargeValue().getCrestChargeSeqNo().toString());
        model.setCountNumber(ccModel.getOffenceValue().getCrestOffenceSeqNo().toString());
        model.setDefendantName(buildDefendantName(ccModel.getDefendantValue()));

        DefendantChargesDialog dcd = new DefendantChargesDialog((java.awt.Frame) getController(), model);
        dcd.setVisible(true);

        if (dcd.isCancelClicked()) {
            throw new UserCancelException();
        }

        if (dcd.isOkClicked()) {
            // Refresh charges amendment log
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
