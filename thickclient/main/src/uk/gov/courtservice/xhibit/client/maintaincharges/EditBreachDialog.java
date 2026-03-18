package uk.gov.courtservice.xhibit.client.maintaincharges;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

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

public class EditBreachDialog extends XDialog implements BreachController {

    private static final long serialVersionUID = 1L;

    private BreachPanel breachPanel;

    private XhibitApplicationController xac;

    private ChargesControllerModel model;
   

    // Editing an existing breach ACTUAL
    public EditBreachDialog(XhibitApplicationController xac, int mode) throws CSRecoverableException {
        super(xac, "", true);
        this.xac = xac;
        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        model = chargesController.getModel();

        if (mode == BreachPanel.EDIT_MODE) {
            super.setTitle(ResourceBundleHelper.getResource(XhibitBundles.Breaches, "editBreachDialogTitle"));

            breachPanel = new BreachPanel(this, model);

            addBodyPanel(breachPanel);
            pack();
        } else {
            // There is some problem!!!
        }
    }

    public XhibitApplicationController getXac() {
        return xac;
    }

    public void stepUpdateViewState() {
        boolean enableOK = true;

        // If Text is entered in Original Sentance
        if (breachPanel != null) {
            enableOK = breachPanel.breachEnableOK();
        } else {
            enableOK = false;
        }

        // OK Enabled for Edit
        if (buttonPanel != null) {
            buttonPanel.okButton.setEnabled(enableOK);
        }
    }

    public void stepDeinitialise() throws CSRecoverableException {
        model.getDelegate().updateBreach(model.getBreachValue(), xac.getApplicationCaseModel().getScheduledHearingId()!=null);
    }
}