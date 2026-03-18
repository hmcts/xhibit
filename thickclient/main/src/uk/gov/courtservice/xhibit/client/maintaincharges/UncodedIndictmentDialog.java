package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dialog;
import java.awt.event.ActionEvent;

/**
 * <p>
 * Title: UncodedCountDialog
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
 * @version $Id: UncodedIndictmentDialog.java,v 1.1 2004/11/05 12:42:17 lzqbry
 *          Exp $
 * 
 */
public class UncodedIndictmentDialog extends UncodedOffenceDialog {
    /**
     * AddedOffencesPanelModel addedOffencesPanelModel
     */
    AddedOffencesPanelModel addedOffencesPanelModel;

    /**
     * 
     * @param dialog
     *            Dialog
     * @param title
     *            String
     * @param modal
     *            boolean
     * @param uoc
     *            UncodedOffenceController
     * @param addedOffencesPanelModel
     *            AddedOffencesPanelModel
     */
    public UncodedIndictmentDialog(Dialog dialog, String title, boolean modal, UncodedOffenceController uoc,
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
