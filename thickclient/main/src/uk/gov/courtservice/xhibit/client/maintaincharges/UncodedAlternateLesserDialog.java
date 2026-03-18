package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.results.util.table.AdditionalInfoTableCell;

/**
 * <p>
 * Title: UncodedAlternateLesserDialog
 * </p>
 * <p>
 * Description: The view component when entering alternate or lesser offences.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Cag Onganer
 * @version $Id: UncodedAlternateLesserDialog.java,v 1.5 2005/01/21 12:33:43
 *          xztnfq Exp $
 * 
 */
public class UncodedAlternateLesserDialog extends UncodedOffenceDialog {

    /**
     * AdditionalInfoTableCell info
     */
    private AdditionalInfoTableCell info;

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
     * @param info
     *            parameter for <init>
     */
    public UncodedAlternateLesserDialog(Frame frame, String title, boolean modal, UncodedOffenceController uoc,
            AdditionalInfoTableCell info) {
        super(frame, title, modal, uoc);
        this.info = info;
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
        getController().saveModel(info);
        getController().closeDialog();
    }

}
