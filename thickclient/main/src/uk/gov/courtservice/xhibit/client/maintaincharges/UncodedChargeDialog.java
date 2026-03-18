package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Frame;
import java.awt.event.ActionEvent;

/**
 * <p>
 * Title: UncodedChargeDialog
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Cag Onganer
 * @version $Id: UncodedChargeDialog.java,v 1.3 2006/06/05 12:31:29 bzjrnl Exp $
 * 
 */
public class UncodedChargeDialog extends UncodedOffenceDialog {

    /**
     * ChargesController chargesController
     */
    private ChargesController chargesController;

    /**
     * <init>
     * 
     * @param frame
     *            parameter for <init>
     * @param title
     *            parameter for <init>
     * @param modal
     *            parameter for <init>
     * @param uoc
     *            parameter for <init>
     * @param chargesController
     *            parameter for <init>
     */
    public UncodedChargeDialog(Frame frame, String title, boolean modal, UncodedOffenceController uoc,
            ChargesController chargesController) {
        super(frame, title, modal, uoc);
        this.chargesController = chargesController;
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
        getController().editModel(chargesController);
        getController().closeDialog();
    }

}
