package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dialog;
import java.awt.event.ActionEvent;

/**
 * <p>
 * Title: UncodedBreachDialog
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
 * @version $Id: UncodedBreachDialog.java,v 1.3 2006/06/05 12:31:29 bzjrnl Exp $
 * 
 */
public class UncodedBreachDialog extends UncodedOffenceDialog {

    /**
     * AddedOffencesPanelModel addedOffencesPanelModel
     */
    private AddedOffencesPanelModel addedOffencesPanelModel;

    /**
     * <init>
     * 
     * @param dialog
     *            parameter for <init>
     * @param title
     *            parameter for <init>
     * @param modal
     *            parameter for <init>
     * @param uoc
     *            parameter for <init>
     * @param addedOffencesPanelModel
     *            parameter for <init>
     */
    public UncodedBreachDialog(Dialog dialog, String title, boolean modal, UncodedOffenceController uoc,
            AddedOffencesPanelModel addedOffencesPanelModel) {
        super(dialog, title, modal, uoc);
        this.addedOffencesPanelModel = addedOffencesPanelModel;
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
        getController().saveModel(addedOffencesPanelModel);
        getController().closeDialog();
    }
}
