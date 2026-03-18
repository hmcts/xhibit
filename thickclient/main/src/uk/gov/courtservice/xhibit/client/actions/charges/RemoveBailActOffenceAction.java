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
 * Title: RemoveBailActOffenceAction
 * </p>
 * <p>
 * Description: Action for removing Bail act offences which also removing the Breach Charge
 * that is associated with the offence.
 * 
 * </p>
 * Company: Logica
 * </p>
 * 
 * @author Luis Valenzuela
 * @version 1.0
 */ 

public class RemoveBailActOffenceAction extends XAction {

    private static final long serialVersionUID = 1L;

    public RemoveBailActOffenceAction() {
        populateFromBundle("RemoveBailActOffence");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException 
    {
        
        /* Get the Charge Model */
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel ccm = chargesController.getModel();
        ChargeControllerBeanBusinessDelegate chargesBD = ccm.getDelegate();

        boolean resultsFoundMBReply = false;
        boolean bailActOffenceRemoved = false;
        ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.MaintainCharges);

        /* Confirmation box to remove Bail Act Offence */
        boolean messageBoxReply = XMessageBox.alert(xac, 
                                                    XHIBITConstant.getResource(resources,"RemoveBailActOffence.Query.Title"), 
                                                    true, 
                                                    XMessageBox.ICONQUESTION, 
                                                    XHIBITConstant.getResource(resources,"RemoveBailActOffence.Query.Message"), 
                                                    XMessageBox.YESNO, 
                                                    XMessageBox.DEFAULTNO);

        /* Yes Response */
        if (messageBoxReply) 
        {
            Integer chargeID = ccm.getChargeValue().getChargeID();
            Integer courtID = new Integer(ccm.getCourtId());
            Integer caseID = ccm.getACM().getCaseId();
            Integer crestChargeSeqNo = ccm.getChargeValue().getCrestChargeSeqNo();
            Integer crestChargeID = ccm.getChargeValue().getCrestChargeID();

            DelChargeValue dcv = new DelChargeValue(chargeID, courtID, caseID, crestChargeSeqNo, crestChargeID, 
                    ccm.isUserInCourtRoom());
            dcv.setCourtLogDate(java.util.Calendar.getInstance());

            try 
           {
                dcv.setDeleteResults(false);
                chargesBD.deleteCharge(dcv);
                bailActOffenceRemoved = true;
            } 
            // Results have been found, query the user whether they wish to proceed and delete results also.
            catch (ResultsFoundException rfe) 
            {
                resultsFoundMBReply = XMessageBox.alert(xac,
                                                        XHIBITConstant.getResource(resources,"RemoveBailActOffence.Results.Query.Title"), 
                                                        true, 
                                                        XMessageBox.ICONQUESTION, 
                                                        XHIBITConstant.getResource(resources, "RemoveBailActOffence.Results.Query.Message"), 
                                                        XMessageBox.YESNO,
                                                        XMessageBox.DEFAULTNO);

                if (resultsFoundMBReply) {
                    try {
                        dcv.setDeleteResults(true);
                        chargesBD.deleteCharge(dcv);
                        bailActOffenceRemoved = true;
                    } catch (ResultsFoundException rfe2) {
                        // Should not get this exception when passing
                        // true to the method deleteOffence
                        String errMsg = XHIBITConstant.getResource(resources, "RemoveBailActOffence.ResultsFoundException2");
                        XHIBITErrorHandler.handleError(rfe2, null, errMsg, this, e);
                        XHIBITConstant.debug(errMsg);
                    }
                }
            }// end of results found catch
            
            if (bailActOffenceRemoved) {
                // Refresh Charges tab
                chargesController.loadCharges();
            }
        }
        
    } // end of xActionPerformed
}