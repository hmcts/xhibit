package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.util.OkCancelPanelConsumer;
import uk.gov.courtservice.xhibit.client.util.XDialog;

/**
 * <p>
 * Title: UncodedOffenceDialog
 * </p>
 * <p>
 * Description: The View Component when you bring up Uncoded Offences.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Cag Onganer
 * @version $Id: UncodedOffenceDialog.java,v 1.5 2006/06/05 12:31:29 bzjrnl Exp $
 * 
 */
public class UncodedOffenceDialog extends XDialog implements OkCancelPanelConsumer {
    /**
     * UncodedOffenceController controller
     */
    private UncodedOffenceController controller;

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
     */
    public UncodedOffenceDialog(Frame frame, String title, boolean modal, UncodedOffenceController uoc) {
        super(frame, title, modal);

        this.controller = uoc;

        addPanel();
    }

    /**
     * <init>
     * 
     * @param parentDialog
     *            parameter for <init>
     * @param title
     *            parameter for <init>
     * @param modal
     *            parameter for <init>
     * @param uoc
     *            parameter for <init>
     */
    public UncodedOffenceDialog(Dialog parentDialog, String title, boolean modal, UncodedOffenceController uoc) {
        super(parentDialog, title, modal);
        this.controller = uoc;

        addPanel();
    }

    private void addPanel() {
        this.setSize(400, 300);
        this.centreDialog();

        this.bodyPanel = new UncodedOffencePanel(this);
        this.getContentPane().add(bodyPanel, BorderLayout.CENTER);
        this.getContentPane().add(super.buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * cancelClicked
     * 
     * @param e
     *            parameter for cancelClicked
     * @throws Exception -
     */
    public void cancelClicked(ActionEvent e) throws Exception {
        getController().closeDialog();
    }

    /**
     * getController
     * 
     * @return the returned UncodedOffenceController
     */
    public UncodedOffenceController getController() {
        return controller;
    }
}
