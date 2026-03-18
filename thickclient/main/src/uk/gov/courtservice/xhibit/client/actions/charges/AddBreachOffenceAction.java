package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddBreachOffenceDialog;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
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
public class AddBreachOffenceAction extends XAction {

    public AddBreachOffenceAction() {
        populateFromBundle("AddBreachOffence");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac;
        ChargesController chargesController;

        xac = (XhibitApplicationController) getController();
        chargesController = (ChargesController) xac.getBodyPanel();

        AddBreachOffenceDialog addBreachOffenceDialog = new AddBreachOffenceDialog(chargesController.getModel());
        addBreachOffenceDialog.setVisible(true);

        //Always reload after calling this dialog to ensure seqNoMap is correct
        chargesController.loadCharges();
        
        if (addBreachOffenceDialog.isCancelClicked())
            throw new UserCancelException();
        

    }
}