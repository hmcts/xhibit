package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Frame;
import java.awt.event.ActionEvent;

/**
 * <p>
 * Title: UncodedSummaryDialog
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
 * @version $Id: UncodedSummaryDialog.java,v 1.3 2006/06/05 12:31:29 bzjrnl Exp $
 * 
 */
public class UncodedSummaryDialog extends UncodedOffenceDialog {

    /**
     * ChargesController chargesController
     */
    private ChargesController chargesController;

    /**
     * boolean isCreate
     */
    private boolean isCreate = true;

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
     * @param isCreate
     *            parameter for <init>
     */
    public UncodedSummaryDialog(Frame frame, String title, boolean modal, UncodedOffenceController uoc,
            ChargesController chargesController, boolean isCreate) {
        super(frame, title, modal, uoc);
        this.chargesController = chargesController;
        this.isCreate = isCreate;
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
        if (isCreate) {
            getController().saveModel(chargesController);
        } else {
            getController().updateModel(chargesController);
        }
        getController().closeDialog();
    }

}
