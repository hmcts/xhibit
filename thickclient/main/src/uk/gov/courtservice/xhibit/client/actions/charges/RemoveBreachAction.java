package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.charge.ResultsFoundException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
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
 * @author
 * @version 1.0
 */

public class RemoveBreachAction extends XAction {

    public RemoveBreachAction() {
        populateFromBundle("RemoveBreach");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel ccm = chargesController.getModel();
        ChargeControllerBeanBusinessDelegate chargesBD = ccm.getDelegate();

        boolean resultsFoundMBReply = false;
        boolean breachRemoved = false;
        ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.MaintainCharges);

        boolean messageBoxReply = XMessageBox.alert(xac, XHIBITConstant.getResource(resources,
                "RemoveBreach.Query.Title"), true, XMessageBox.ICONQUESTION, XHIBITConstant.getResource(resources,
                "RemoveBreach.Query.Message"), XMessageBox.YESNO, XMessageBox.DEFAULTNO);

        if (messageBoxReply) {
            Integer chargeID = ccm.getChargeValue().getChargeID();
            Integer courtID = new Integer(ccm.getCourtId());
            Integer caseID = ccm.getACM().getCaseId();
            Integer crestChargeSeqNo = ccm.getChargeValue().getCrestChargeSeqNo();
            Integer crestChargeID = ccm.getChargeValue().getCrestChargeID();

            DelChargeValue dcv = new DelChargeValue(chargeID, courtID, caseID, crestChargeSeqNo, crestChargeID, ccm
                    .isUserInCourtRoom());
            dcv.setCourtLogDate(java.util.Calendar.getInstance());

            try {
                dcv.setDeleteResults(false);
                chargesBD.deleteCharge(dcv);
                breachRemoved = true;
            } catch (ResultsFoundException rfe) {
                resultsFoundMBReply = XMessageBox.alert(xac, XHIBITConstant.getResource(resources,
                        "RemoveBreach.Results.Query.Title"), true, XMessageBox.ICONQUESTION, XHIBITConstant
                        .getResource(resources, "RemoveBreach.Results.Query.Message"), XMessageBox.YESNO,
                        XMessageBox.DEFAULTNO);

                if (resultsFoundMBReply) {
                    try {
                        dcv.setDeleteResults(true);
                        chargesBD.deleteCharge(dcv);
                        breachRemoved = true;
                    } catch (ResultsFoundException rfe2) {
                        // Should not get this exception when passing
                        // true to the method deleteOffence
                        String errMsg = XHIBITConstant.getResource(resources, "RemoveBreach.ResultsFoundException2");
                        XHIBITErrorHandler.handleError(rfe2, null, errMsg, this, e);
                        XHIBITConstant.debug(errMsg);
                    }
                }
            }
            if (breachRemoved) {
                // Refresh Charges tab
                chargesController.loadCharges();
            }
        }
    }
}