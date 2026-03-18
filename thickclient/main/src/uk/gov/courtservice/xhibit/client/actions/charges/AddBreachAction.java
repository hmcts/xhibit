package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddBreachWizardDialog;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
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
public class AddBreachAction extends XAction {

    public AddBreachAction() {
        populateFromBundle("AddBreach");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac;
        ChargesController chargesController;
        ChargesControllerModel model;

        xac = (XhibitApplicationController) getController();
        chargesController = (ChargesController) xac.getBodyPanel();
        model = chargesController.getModel();

        AddBreachWizardDialog addBreachWizardDialog = new AddBreachWizardDialog(model);
        addBreachWizardDialog.setVisible(true);

        //Always load charges to ensure seqnolist is correct
        chargesController.loadCharges();
        
        // if cancel not clicked
        if (addBreachWizardDialog.getLatestEvent() == XWizardDialog.CANCEL_EVENT) {
            throw new UserCancelException();
        }        

    }
}