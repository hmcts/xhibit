package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import uk.gov.courtservice.xhibit.client.courtlog.SimpleEventDialog;
import uk.gov.courtservice.xhibit.client.courtlog.SimpleEventModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Sign Indictment Refused
 * </p>
 * <p>
 * Description: Charges action to create the "Sign Indictment - Refused" court
 * log event. This is only used on the Charges screen.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */

public class SignIndictmentRefusedAction extends XAction {
    /** The unique Event Code for the Court Log event Sign Indictment Refused */
    private static final String EVENT_CODE = "40215";

    /**
     * Default constructor that calls populateFromBundle to setup the action
     * with its name, descriptions, mnemonics and icon etc.
     */
    public SignIndictmentRefusedAction() {
        populateFromBundle("SignIndictmentRefused");
    }

    /**
     * This method is called from the actionPerformed method of XAction and is
     * triggered when the action is fired, e.g. from a button or menu. Creates
     * and displays the court log simple event dialog. If the user clicks OK on
     * this dialog then the charges screen is reloaded.
     * 
     * @param e
     *            Event which indicates that a component-defined action occured.
     * @throws Exception
     *             General exception including UserCancelException that is
     *             handled by the actionPerformed method of XAction.
     */
    public void xActionPerformed(ActionEvent e) throws Exception {
        ChargesController chargesController;
        XhibitApplicationController xac = (XhibitApplicationController) getController();

        ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.MaintainCharges);

        SimpleEventModel model = new SimpleEventModel();

        String panelText = XHIBITConstant.getResource(resources, "SignIndictmentRefused.panelText");
        model.setPanelText(panelText);
        model.setEventType(EVENT_CODE);
        model.setXac(xac);

        xac = (XhibitApplicationController) getController();
        chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel ccm = chargesController.getModel();

        String freeText = MessageFormat.format(XHIBITConstant.getResource(resources, "SignIndictmentRefused.freeText"),
                new Object[] { ccm.getChargeValue().getCrestChargeSeqNo() });

        // Create the Court Log simple eventg dialog.
        SimpleEventDialog sed = new SimpleEventDialog((Frame) getController(), model);
        sed.getBodyPanel().setFreeText(freeText);
        sed.setVisible(true);

        // If Cancel is clicked on the Court Log simple eventg dialog, then
        // throw a UserCancelException to bypass further processing (including
        // any more general processing in XAction's actionPerformed method).
        if (sed.isCancelClicked()) {
            throw new UserCancelException();
        }

        // If OK is clicked on the Court Log simple eventg dialog, then refresh
        // the Charges screen.
        if (sed.isOkClicked()) {
            ChargesController cc = (ChargesController) xac.getBodyPanel();
            cc.loadCharges();
        }
    }
}