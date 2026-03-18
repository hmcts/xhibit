package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddBreachOffenceDefendantDetailsDialog;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerHelper;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class AdditionalBreachOffenceDefendantInfoAction extends XAction {

    public AdditionalBreachOffenceDefendantInfoAction() {
        populateFromBundle("AdditionalBreachOffenceDefendantInfoAction");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac;
        ChargesController chargesController;
        ChargesControllerModel model;
        xac = (XhibitApplicationController) getController();
        chargesController = (ChargesController) xac.getBodyPanel();
        model = chargesController.getModel();
                
        AddBreachOffenceDefendantDetailsDialog addBreachOffenceDefendantDetailsDialog = new AddBreachOffenceDefendantDetailsDialog(
                xac, model, true, ChargesControllerHelper.MODE.EDIT);

        addBreachOffenceDefendantDetailsDialog.setVisible(true);
        if (addBreachOffenceDefendantDetailsDialog.isCancelClicked())
            throw new UserCancelException();

        chargesController.loadCharges();

    }

}
