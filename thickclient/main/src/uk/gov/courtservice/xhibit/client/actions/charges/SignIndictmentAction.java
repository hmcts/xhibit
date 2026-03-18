package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.charge.OutOfTimeException;
import uk.gov.courtservice.xhibit.business.services.charge.IndictmentSignDateInTheFuture;
import uk.gov.courtservice.xhibit.business.vos.services.charge.SignIndValue;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
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
 * @author Bal Bhamra
 * @version 1.0
 */
public class SignIndictmentAction extends XAction {

    private static final long serialVersionUID = 1L;

    public SignIndictmentAction() {
        populateFromBundle("SignIndictment");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel ccm = chargesController.getModel();
        ChargeControllerBeanBusinessDelegate chargesBD = ccm.getDelegate();

        boolean signMBReply;
        boolean outOfDateMBReply = false;
        boolean indictmentSigned = false;
        ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.MaintainCharges);

        signMBReply = XMessageBox.alert(xac, XHIBITConstant.getResource(resources, "SignIndictment.Query.Title"), true,
                XMessageBox.ICONQUESTION, XHIBITConstant.getResource(resources, "SignIndictment.Query.Message"),
                XMessageBox.YESNO, XMessageBox.DEFAULTNO);

        if (signMBReply) {
            ApplicationCaseModel acm = ccm.getACM();
            SignIndValue signIndVal = new SignIndValue();
            
            String judgeName = acm.getScheduledHearingValue().getJudge();
            if (judgeName == null || judgeName.trim().equals("")) {
                throw new CSRecoverableException("gui.SignIndictmentAction.noJudge",
                        "No judge specified, cannot Sign Indictment");
            } else {
                signIndVal.setJudgeName(judgeName);
            }
            signIndVal.setCaseID(acm.getCaseId());
            signIndVal.setChargeID(ccm.getChargeValue().getChargeID());

            signIndVal.setCourtID(new Integer(ccm.getCourtId()));

            signIndVal.setInCourt(ccm.isUserInCourtRoom());
            signIndVal.setIndSignedDate(ccm.getChargeValue().getIndSignedDate());
            signIndVal.setSignOutOfTime(false);
            signIndVal.setCourtLogDate(java.util.Calendar.getInstance());

            try {
                chargesBD.signIndictment(signIndVal);
                indictmentSigned = true;
            } catch (OutOfTimeException oote1) {
                outOfDateMBReply = XMessageBox.alert(xac, XHIBITConstant.getResource(resources,
                        "SignIndictment.OutOfTime.Query.Title"), true, XMessageBox.ICONQUESTION, XHIBITConstant
                        .getResource(resources, "SignIndictment.OutOfTime.Query.Message"), XMessageBox.YESNO,
                        XMessageBox.DEFAULTNO);

                if (outOfDateMBReply) {
                    try {
                        // setSignOutOfTime to true to force the indictment to
                        // be signed.
                        signIndVal.setSignOutOfTime(true);
                        chargesBD.signIndictment(signIndVal);
                        indictmentSigned = true;
                    } catch (OutOfTimeException oote2) {
                        // Should not get this exception when passing
                        // true to the method setSignOutOfTime
                        XHIBITConstant
                                .debug("SignIndictmentAction: OutOfTimeException caught when passing setSignOutOfTime(true) ???");
                    }
                }
            } catch (IndictmentSignDateInTheFuture indSignDateError) {
                XMessageBox.alert(xac, 
                        XHIBITConstant.getResource(resources, "signeddate.date.future.error.title"), 
                        false, 
                        XMessageBox.ICONERROR, 
                        XHIBITConstant.getResource(resources, "signeddate.date.future.error.message"), 
                        XMessageBox.OK_ONLY,
                        XMessageBox.DEFAULTOK);
            }

            if (indictmentSigned) {
                // Refresh Charges tabs
                chargesController.loadCharges();
            }
        }
    }
}