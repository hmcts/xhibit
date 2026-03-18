package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddBailActOffenceDialog;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: AddBailActOffenceAction
 * </p>
 * <p>
 * Description: Action for adding Bail act offences which also creates the Breach Charge
 * that is associated with the offence.
 * 
 * </p>
 * Company: Logica
 * </p>
 * 
 * @author Luis Valenzuela
 * @version 1.0
 */
public class AddBailActOffenceAction extends XAction {

    public AddBailActOffenceAction() {
        populateFromBundle("AddBailActOffence");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac;
        ChargesController chargesController;
        ChargesControllerModel model;

        xac = (XhibitApplicationController) getController();
        chargesController = (ChargesController) xac.getBodyPanel();
        model = chargesController.getModel();

        AddBailActOffenceDialog addBAODialog = new AddBailActOffenceDialog(model);
        addBAODialog.setVisible(true);
        
        //TODO REMOVE uk.gov.courtservice.xhibit.client.util.XMessageBox.alert("This action has not been implemented yet");
        
        //Always load charges to ensure seqnolist is correct
        chargesController.loadCharges();
        
        // if cancel not clicked
        if (addBAODialog.getLatestEvent() == XWizardDialog.CANCEL_EVENT) {
            throw new UserCancelException();
        }        

    }
}