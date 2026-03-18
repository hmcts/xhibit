package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.joinder.JoinderConstants;
import uk.gov.courtservice.xhibit.client.maintaincharges.joinder.JoinderIndictmentController;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Join Indictments
 * </p>
 * <p>
 * Description: This action displays the Join Indictment Wizard which allows the
 * user to select indictments to add to the currently selected indictment.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author unascribed
 * @version 1.0
 */

public class JoinAction extends XAction {
    public JoinAction() {
        populateFromBundle("Join");
    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel model = chargesController.getModel();
        //CaseBasicValue caseBO = model.getACM().getScheduledHearingValue().getCaseBasicValue();
        XhbCaseBasicValue caseBO = model.getCCV().getCaseBasicValue();

        String title = ResourceBundleHelper.getResource(XhibitBundles.JoinderResources, JoinderConstants.DIALOG_TITLE);

        JoinderIndictmentController joinController = new JoinderIndictmentController(caseBO, model, title);

        joinController.setVisible(true);

        if (joinController.getLatestEvent() == XWizardDialog.CANCEL_EVENT) {
            throw new UserCancelException();
        }
        chargesController.loadCharges();
    }
}