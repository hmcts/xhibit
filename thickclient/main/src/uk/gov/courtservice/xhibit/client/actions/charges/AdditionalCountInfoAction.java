package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddOffenceDetailsDialog;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerHelper;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.HOProcCodeHelper;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class AdditionalCountInfoAction extends XAction {

    public AdditionalCountInfoAction() {
        populateFromBundle("AdditionalCountInfoAction");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac;
        ChargesController chargesController;
        ChargesControllerModel model;
        xac = (XhibitApplicationController) getController();
        chargesController = (ChargesController) xac.getBodyPanel();
        model = chargesController.getModel();
        model.getOffenceValue();

        AddOffenceDetailsDialog addOffenceDetailsDialog = 
            new AddOffenceDetailsDialog(AddOffenceDetailsDialog.TITLE.COUNT, model.getOffenceValue(), xac, HOProcCodeHelper.TRIAL, 
                    ChargesControllerHelper.MODE.EDIT);
        addOffenceDetailsDialog.setVisible(true);
        if( addOffenceDetailsDialog.isCancelClicked())
                throw new UserCancelException();
        
        chargesController.loadCharges();
    }
}
