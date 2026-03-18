package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
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
public class StayIndictmentAction extends XAction {
    public StayIndictmentAction() {
        populateFromBundle("StayIndictment");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        ChargeControllerBeanBusinessDelegate chargesBD;
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel ccm = chargesController.getModel();

        String indictment = ccm.getChargeValue().getCrestChargeSeqNo().toString();

        boolean messageBoxReply = XMessageBox.alert(xac, getString("StayIndictment.Query.Title"), true,
                XMessageBox.ICONQUESTION, getString("StayIndictment.Query.Message") + " " + indictment + "?",
                XMessageBox.YESNO, XMessageBox.DEFAULTNO);

        if (messageBoxReply) {
            chargesBD = ccm.getDelegate();

            Collection offenceValueCol = ccm.getChargeValue().getOffenceValues();
            Collection dobvCol = new Vector();

            OffenceValue offenceValue;
            HashMap dobvMap;
            Iterator OffenceValuesIterator = offenceValueCol.iterator();

            while (OffenceValuesIterator.hasNext()) {
                offenceValue = (OffenceValue) OffenceValuesIterator.next();
                // dobvMap = ccm.getOffenceValue().getDefOnOffenceBasicValues();
                dobvMap = offenceValue.getDefOnOffenceBasicValues();
                dobvCol.addAll(dobvMap.values());
            }

            XhbDefendantOnOffenceBasicValue[] dobvArray = new XhbDefendantOnOffenceBasicValue[dobvCol.size()];
            dobvCol.toArray(dobvArray);
            chargesBD.updateDefendantOnCountStatus(dobvArray, getCRUDValue(ccm.getChargeValue().getCaseID()));

            CrestIndictmentLog.getInstance().stayIndictmentLog(
                    xac.getApplicationCaseModel().getScheduledHearingValue().getCaseBasicValue(), ccm.getChargeValue());

            // Refresh Charges tabs
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
        cv.setEventType(new Integer(40206));
        cv.setInCourt(XhibitSingleton.getInstance().isUserInCourtroom());
        return cv;
    }
}