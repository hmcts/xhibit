package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dialog;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.results.pleas.PleaControllerModel;

/**
 * <p>
 * Title: UncodedPleaDialog
 * </p>
 * <p>
 * Description: Allows the user to create new offence codes. Used in the
 * MultiplePleas dialog.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */

public class UncodedPleaDialog extends UncodedOffenceDialog {
    PleaControllerModel model;

    /**
     * @param dialog
     *            Dialog
     * @param title
     *            String
     * @param modal
     *            boolean
     * @param controller
     *            UncodedOffenceController
     * @param model
     *            PleaControllerModel
     */
    public UncodedPleaDialog(Dialog dialog, String title, boolean modal, UncodedOffenceController controller,
            PleaControllerModel model) {
        super(dialog, title, modal, controller);
        this.model = model;
        UncodedOffencePanel panel = (UncodedOffencePanel) bodyPanel;
        panel.getlblHOClass().setVisible(false);
        panel.getlblHOSubClass().setVisible(false);
        panel.gettxtFldHOClass().setVisible(false);
        panel.gettxtFldHOSubClass().setVisible(false);
        panel.getlblDVLCClass().setVisible(false);
    }

    /**
     * okClicked
     * 
     * @param e
     *            parameter for okClicked
     * @throws Exception -
     */
    public void okClicked(ActionEvent e) throws Exception {
        bodyPanel.stepUpdateViewState();
        getController().updateParentPanel(model);
        getController().closeDialog();
    }
}
