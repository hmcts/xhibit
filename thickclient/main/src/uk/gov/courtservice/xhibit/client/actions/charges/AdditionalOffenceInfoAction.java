package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddOffenceDetailsDialog;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerHelper;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class AdditionalOffenceInfoAction extends XAction {

    public AdditionalOffenceInfoAction() {
        populateFromBundle("AdditionalOffenceInfoAction");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel model = chargesController.getModel();

        // No need to HO Proc Code when adding additional Offence. 
        AddOffenceDetailsDialog addOffenceDetailsDialog = new AddOffenceDetailsDialog(AddOffenceDetailsDialog.TITLE.OFFENCE, model.getOffenceValue(), xac,
                null, ChargesControllerHelper.MODE.EDIT);
        addOffenceDetailsDialog.setVisible(true);

        if (addOffenceDetailsDialog.isCancelClicked())
            throw new UserCancelException();

        //Refresh Charges screen
        chargesController.loadCharges();
    }

}
