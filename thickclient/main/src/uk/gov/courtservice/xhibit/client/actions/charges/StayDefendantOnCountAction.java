package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.Calendar;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerHelper;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.CrestIndictmentLog;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

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
 * @author Bal Bhamra
 * @version 1.0
 */
public class StayDefendantOnCountAction extends XAction {
    public StayDefendantOnCountAction() {
        populateFromBundle("StayDefendantOnCount");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        ChargeControllerBeanBusinessDelegate chargesBD;
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel ccm = chargesController.getModel();

        String count = ccm.getOffenceValue().getCrestOffenceSeqNo().toString();
        String message = getString("AreYouSure") + " " + getString("Stay") + " " + getString("TheDefendant") + "\n"
                + ChargesControllerHelper.buildDefendantName(ccm.getDefendantValue()) + " " + getString("OnCount")
                + " " + count + "?";

        boolean messageBoxReply = XMessageBox.alert(xac, getString("StayDefendantOnCount.Query.Title"), true,
                XMessageBox.ICONQUESTION, message, XMessageBox.YESNO, XMessageBox.DEFAULTNO);

        if (messageBoxReply) {
            chargesBD = ccm.getDelegate();

            Integer defendantOnCaseID = ccm.getDefendantValue().getDefendantID();

            XhbDefendantOnOffenceBasicValue dobv = ccm.getOffenceValue().getDefendantOnOffence(defendantOnCaseID);
            XhbDefendantOnOffenceBasicValue[] defOnOffenceBasicValues = new XhbDefendantOnOffenceBasicValue[1];
            defOnOffenceBasicValues[0] = dobv;

            chargesBD.updateDefendantOnCountStatus(defOnOffenceBasicValues, getCRUDValue(ccm.getChargeValue()
                    .getCaseID()));

            CrestIndictmentLog.getInstance().stayDefendantOnCountLog(
                    xac.getApplicationCaseModel().getScheduledHearingValue().getCaseBasicValue(),
                    ccm.getDefendantValue(), ccm.getChargeValue(), ccm.getOffenceValue());

            // Refresh Charges tab
            chargesController.loadCharges();
        }
    }

    private String getString(String key) {
        return XHIBITConstant.getResource(XhibitBundles.MaintainCharges, key);
    }

    public CourtLogCRUDValue getCRUDValue(Integer caseId) {
        CourtLogCRUDValue cv = new CourtLogCRUDValue();
        cv.setCaseId(caseId);
        cv.setEntryDate(Calendar.getInstance().getTime());
        cv.setEntryFreeText("");
        cv.setEventType(new Integer(40204));
        cv.setInCourt(XhibitSingleton.getInstance().isUserInCourtroom());
        return cv;
    }
}