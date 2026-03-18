package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;


import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ExportChargeInProgressException;
import uk.gov.courtservice.xhibit.business.services.charge.NoChargesToExportException;
import uk.gov.courtservice.xhibit.business.services.charge.NoDefendantForIndictmentException;
import uk.gov.courtservice.xhibit.business.services.charge.NoIndictmentsForChargeException;
import uk.gov.courtservice.xhibit.business.services.charge.NoOffencesForIndictmentException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.SeqNoHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * <p>
 * Title: XHIBIT2 ExportChargesAction
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public class ExportChargesAction extends XAction {

    public ExportChargesAction() {
        populateFromBundle("ExportCharges");
    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
        try {
            final XhibitApplicationController xac = (XhibitApplicationController) getController();

            //Check that sequence numbers are sequential, otherwise warn user
            checkSeqNosSequential(xac);
            
            // Ensure that there is at least one defendant per indictment
            if (xac.getBodyPanel() instanceof ChargesController) {
                final ChargesController cc = (ChargesController) xac.getBodyPanel();
                ensureIndictmentHasDefendant(cc);
            }
            
            
            // Export the charges
            XhibitDelegateHelper.getChargeDelegate().exportCharges(xac.getApplicationCaseModel().getCaseId(),
                    xac.getApplicationCaseModel().getScheduledHearingId());
            
        } catch (NoIndictmentsForChargeException ex) {
            handleException(e, "NoIndictmentsForChargeException");
        } catch (NoDefendantForIndictmentException ex) {
            handleException(e, "NoDefendantForIndictmentException");
        } catch (NoOffencesForIndictmentException ex) {
            handleException(e, "NoOffencesForIndictmentException");
        } catch (ExportChargeInProgressException ex) {
            handleException(e, "ExportChargeInProgressException");
        } catch (NoChargesToExportException ex) {
            handleException(e, "NoChargesToExportException");
        }
    }
    
    
    /**
     * This method is designed to show the user a warning message if the sequence numbers for each defendant are not sequential.
     * The user can then continue with the export 
     * @param xac
     * @throws CSRecoverableException
     * @throws UserCancelException
     */
    private void checkSeqNosSequential(XhibitApplicationController xac) throws CSRecoverableException,UserCancelException{
        //Use hashmap stored in app case model
        HashMap<Integer,List> defOnCaseSeqNosMap = xac.getApplicationCaseModel().getDefOnCaseSeqNosMap();
              
        if (!SeqNoHelper.areListsSequential(defOnCaseSeqNosMap)){
            //If any list is not sequential, then warn user
            String message = XHIBITConstant.getResource(XhibitBundles.MaintainCharges,"SeqNosNotSequentialText1");
            message += "\n\n"+XHIBITConstant.getResource(XhibitBundles.MaintainCharges,"SeqNosNotSequentialText2");
            String title = XHIBITConstant.getResource(XhibitBundles.MaintainCharges,"SeqNosNotSequentialTitle");
            if(JOptionPane.showConfirmDialog(xac,message , title,JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.OK_OPTION){
                throw new UserCancelException();
            }
       }
    }

    /**
     * Ensures that there is at least one defendant per indictment charge type.
     * 
     * @throws NoDefendantForIndictmentException
     * @throws NoOffencesForIndictmentException
     * @throws NoIndictmentsForChargeException
     */
    private void ensureIndictmentHasDefendant(ChargesController cc) throws NoDefendantForIndictmentException,
            NoOffencesForIndictmentException, NoIndictmentsForChargeException {
        boolean foundIndictment = false;

        /**
         * Iterate thru the indictments. Each indictment contains a collection
         * of offences. Each offence contains a collection of defendants. Check
         * that at least one of the offences for the indictments has a
         * defendant.
         */
        Iterator cvIter = cc.getIndictmentsList().iterator();
        while (cvIter.hasNext()) {
            foundIndictment = true;
            boolean foundOffenceForIndictment = false;

            ChargeValue cv = (ChargeValue) cvIter.next();
            if (cv.getOffenceValues() != null && cv.getOffenceValues().size() > 0) {
                foundOffenceForIndictment = true;
                boolean foundDefendantForIndictment = false;

                Iterator ovIter = cv.getOffenceValues().iterator();
                while (ovIter.hasNext()) {
                    OffenceValue ov = (OffenceValue) ovIter.next();
                    if (ov.getDefendantValues() != null && ov.getDefendantValues().size() > 0) {
                        foundDefendantForIndictment = true;
                        break;
                    }
                }

                if (foundDefendantForIndictment == false) {
                    throw new NoDefendantForIndictmentException();
                }
            }

            if (foundOffenceForIndictment == false) {
                throw new NoOffencesForIndictmentException();
            }
        }

        if (foundIndictment == false) {
            throw new NoIndictmentsForChargeException();
        }
    }

    private void handleException(ActionEvent e, String ExceptionName) {
        Window parentWindow = null;
        if (e != null && e.getSource() instanceof Component) {
            parentWindow = XSwingUtilities.getWindowAncestor((Component) e.getSource());
        }

        JOptionPane.showMessageDialog(parentWindow, XHIBITConstant.getResource(XhibitBundles.MaintainCharges,
                ExceptionName + ".Text"), XHIBITConstant.getResource(XhibitBundles.MaintainCharges, ExceptionName
                + ".Title"), JOptionPane.ERROR_MESSAGE);
    }
}
