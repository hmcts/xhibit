package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerHelper;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.CrestIndictmentLog;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
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
public class StayDefendantOnIndictmentAction extends XAction {
    public StayDefendantOnIndictmentAction() {
        populateFromBundle("StayDefendantOnIndictment");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel ccm = chargesController.getModel();
        XhbDefendantOnOffenceBasicValue dobv;

        String indictment = ccm.getChargeValue().getCrestChargeSeqNo().toString();
        String message = getString("AreYouSure") + " " + getString("Stay") + " " + getString("TheDefendant") + "\n"
                + ChargesControllerHelper.buildDefendantName(ccm.getDefendantValue()) + " " + getString("OnIndictment")
                + " " + indictment + "?";

        boolean messageBoxReply = XMessageBox.alert(xac, getString("StayDefendantOnIndictment.Query.Title"), true,
                XMessageBox.ICONQUESTION, message, XMessageBox.YESNO, XMessageBox.DEFAULTNO);

        if (messageBoxReply) {
            Integer defendantOnCaseID = ccm.getDefendantValue().getDefendantID();
            Collection offenceValueCol = ccm.getChargeValue().getOffenceValues();
            Collection dobvCol = new Vector();

            Iterator OffenceValuesIterator = offenceValueCol.iterator();
            OffenceValue offenceValue;
            while (OffenceValuesIterator.hasNext()) {
                offenceValue = (OffenceValue) OffenceValuesIterator.next();
                dobv = offenceValue.getDefendantOnOffence(defendantOnCaseID);
                if (dobv != null) {
                    dobvCol.add(dobv);
                }
            }

            XhbDefendantOnOffenceBasicValue[] dobvArray = new XhbDefendantOnOffenceBasicValue[dobvCol.size()];
            dobvCol.toArray(dobvArray);
            XhibitDelegateHelper.getChargeDelegate().updateDefendantOnCountStatus(dobvArray,
                    getCRUDValue(ccm.getChargeValue().getCaseID()));

            CrestIndictmentLog.getInstance().stayDefendantOnIndictmentLog(
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
        cv.setEventType(new Integer(40205));
        cv.setInCourt(XhibitSingleton.getInstance().isUserInCourtroom());
        return cv;
    }
}