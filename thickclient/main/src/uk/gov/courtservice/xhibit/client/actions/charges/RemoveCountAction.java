package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.ResourceBundle;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.charge.ResultsFoundException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelOffenceValue;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.CrestIndictmentLog;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
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
 * @author Simon Gilmore
 * @version 1.0
 */

public class RemoveCountAction extends XAction {

    public RemoveCountAction() {
        populateFromBundle("RemoveCount");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel ccm = chargesController.getModel();
        ChargeControllerBeanBusinessDelegate chargesBD = ccm.getDelegate();

        boolean resultsFoundMBReply = false;
        boolean offenceRemoved = false;
        ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.MaintainCharges);

        boolean messageBoxReply = XMessageBox.alert(xac, XHIBITConstant.getResource(resources,
                "RemoveCount.Query.Title"), true, XMessageBox.ICONQUESTION, XHIBITConstant.getResource(resources,
                "RemoveCount.Query.Message"), XMessageBox.YESNO, XMessageBox.DEFAULTNO);

        if (messageBoxReply) {
            Integer offenceID = ccm.getOffenceValue().getOffenceID();
            Integer courtID = new Integer(ccm.getCourtId());
            Integer caseID = ccm.getACM().getCaseId();
            Integer chargeID = ccm.getChargeValue().getChargeID();
            Collection defendantIDs = ccm.getOffenceValue().getDefendantIDs();

            DelOffenceValue dov = new DelOffenceValue(offenceID, courtID, caseID, chargeID, defendantIDs, ccm
                    .isUserInCourtRoom());
            dov.setCourtLogDate(java.util.Calendar.getInstance());

            try {
                dov.setDeleteResults(false);
                chargesBD.deleteOffence(dov);
                offenceRemoved = true;
            } catch (ResultsFoundException rfe) {
                resultsFoundMBReply = XMessageBox.alert(xac, XHIBITConstant.getResource(resources,
                        "RemoveCount.Results.Query.Title"), true, XMessageBox.ICONQUESTION, XHIBITConstant.getResource(
                        resources, "RemoveCount.Results.Query.Message"), XMessageBox.YESNO, XMessageBox.DEFAULTNO);

                if (resultsFoundMBReply) {
                    try {
                        dov.setDeleteResults(true);
                        chargesBD.deleteOffence(dov);
                        offenceRemoved = true;
                    } catch (ResultsFoundException rfe2) {
                        // Should not get this exception when passing
                        // true to the method deleteOffence
                        String errMsg = XHIBITConstant.getResource(resources, "RemoveCount.ResultsFoundException2");
                        XHIBITConstant.handleError(rfe2, null, errMsg);
                        XHIBITConstant
                                .debug("RemoveCountAction: ResultsFoundException caught when passing setDeleteResults(true) ???");
                    }
                }
            }
            if (offenceRemoved) {
                // Log that a count has been removed.
                CrestIndictmentLog.getInstance().removeCountLog(
                        xac.getApplicationCaseModel().getScheduledHearingValue().getCaseBasicValue(),
                        ccm.getOffenceValue().getCrestOffenceSeqNo(), ccm.getChargeValue().getCrestChargeSeqNo());

                // Refresh Charges tab
                chargesController.loadCharges();
            }
        }
    }
}