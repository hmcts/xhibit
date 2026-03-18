package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddIndictmentWizard;
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
 * 
 * Change Log
 * @version 1.1 - Frederik Vandendrie ssche - Rework to work with Iteration 2
 *          Search functionality.
 * 
 * @version 1.12 - Simon Gilmore - Change to use wizard for add indictment.
 */

public class AddIndictmentAction extends XAction {
    public AddIndictmentAction() {
        populateFromBundle("AddIndictment");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException, Exception {
        XhibitApplicationController xac;
        ChargesController chargesController;
        ChargesControllerModel model;

        xac = (XhibitApplicationController) getController();
        chargesController = (ChargesController) xac.getBodyPanel();
        model = chargesController.getModel();

        AddIndictmentWizard addIndictmentWizard = new AddIndictmentWizard(model);
        addIndictmentWizard.setVisible(true);

        if (addIndictmentWizard.getLatestEvent() == XWizardDialog.CANCEL_EVENT) {
            throw new UserCancelException();
        }
        chargesController.loadCharges();
    }

}