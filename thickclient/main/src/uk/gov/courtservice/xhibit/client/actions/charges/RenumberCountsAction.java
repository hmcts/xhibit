package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.RenumberCountsListDialog;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: RenumberCountsAction
 * </p>
 * <p>
 * Description: Renumber Counts Action
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company:Logica
 * </p>
 * 
 * @author Krishna Pokala
 * @version 1.0
 */
public class RenumberCountsAction extends XAction {

    private static final long serialVersionUID = 1L;
    
    public RenumberCountsAction() {
        populateFromBundle("RenumberCounts");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        RenumberCountsListDialog dialog = new RenumberCountsListDialog(xac);
        dialog.setVisible(true);
        if (dialog.isCancelClicked())
            throw new UserCancelException();
        chargesController.loadCharges();
    }
}