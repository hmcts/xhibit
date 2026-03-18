package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.maintaincharges.BreachPanel;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.EditBreachDialog;
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
public class EditBreachAction extends XAction {

    private static final long serialVersionUID = 1L;

    public EditBreachAction() {
        populateFromBundle("EditBreach");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac;
        ChargesController chargesController;

        xac = (XhibitApplicationController) getController();
        chargesController = (ChargesController) xac.getBodyPanel();

        EditBreachDialog editBreachDialog = new EditBreachDialog(xac, BreachPanel.EDIT_MODE);
        editBreachDialog.setVisible(true);

        // REMOVE CCM REF
        // if (!model.isCancelClicked())
        // {
        // //Refresh Charges tab
        // chargesController.loadCharges();
        // }

        if (editBreachDialog.isCancelClicked())
            throw new UserCancelException();
        chargesController.loadCharges();

    }
}