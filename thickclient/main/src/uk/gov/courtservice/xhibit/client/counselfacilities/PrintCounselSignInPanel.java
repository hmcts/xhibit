package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: PrintCounselSignInPanel
 * </p>
 * <p>
 * Description: The panel for printing the counsel sign in
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Steve Tully
 * @version 1.0
 */

public class PrintCounselSignInPanel extends XPanel {

    private static final long serialVersionUID = 1L;

    private String resources = XhibitBundles.CounselFacilities;

    private PrintCounselSignInModel model;

    private OkCancelPanel buttonPanel;

    private GridBagLayout mainPanelLayout = new GridBagLayout();

    private JLabel courtroomLabel;

    private JLabel printOptionsLabel;

    private JTextField courtroomText = null;

    private ButtonGroup printOptionsRadioButtonGroup = null;

    private JRadioButton printAllRadio = null;

    private JRadioButton printCurRadio = null;

    public PrintCounselSignInPanel(XDialog parent, PrintCounselSignInModel model) throws CSRecoverableException {
        super();

        this.buttonPanel = (OkCancelPanel) parent.getButtonPanel();
        this.model = model;

        stepInitialise();

        jbInit();

        stepActivate();
    }

    public void stepInitialise() throws CSRecoverableException {
        model.setCourtRoomKnown(XhibitSingleton.getInstance().isUserInCourtroom());
    }

    public PrintCounselSignInPanel() {
        // empty
    }

    private void jbInit() {
        this.setLayout(mainPanelLayout);
        this.add(getCourtroomLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getCourtroomText(), new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getPrintOptionsLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getPrintCurRadio(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getPrintAllRadio(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        getPrintOptionsRadioButtonGroup().add(getPrintAllRadio());
        getPrintOptionsRadioButtonGroup().add(getPrintCurRadio());
    }

    private ButtonGroup getPrintOptionsRadioButtonGroup() {
        if (printOptionsRadioButtonGroup == null) {
            printOptionsRadioButtonGroup = new ButtonGroup();
        }

        return printOptionsRadioButtonGroup;
    }

    private JTextField getCourtroomText() {
        if (courtroomText == null) {
            courtroomText = new JTextField();
            courtroomText.setMinimumSize(new Dimension(125, XHIBITConstant.getLineHeight()));
            courtroomText.setPreferredSize(new Dimension(125, XHIBITConstant.getLineHeight()));
            enableTextField(courtroomText, false);
        }

        return courtroomText;
    }

    private JLabel getCourtroomLabel() {
        if (courtroomLabel == null) {
            courtroomLabel = new JLabel();
            courtroomLabel.setText(XHIBITConstant.getResource(resources, "lblCurrentCourtroom"));
        }

        return courtroomLabel;
    }

    private JLabel getPrintOptionsLabel() {
        if (printOptionsLabel == null) {
            printOptionsLabel = new JLabel();
            printOptionsLabel.setText(XHIBITConstant.getResource(resources, "lblPrintOptions"));
        }

        return printOptionsLabel;
    }

    private JRadioButton getPrintAllRadio() {
        if (printAllRadio == null) {
            printAllRadio = new JRadioButton();
            printAllRadio.setSelected(!model.isCourtRoomKnown());
            printAllRadio.setToolTipText(XHIBITConstant.getResource(resources, "lblAllCourtrooms"));
            printAllRadio.setActionCommand(CounselFacilitiesHelper.ALL);
            printAllRadio.setMnemonic(XHIBITConstant.getResource(resources, "mnmAll").charAt(0));
            printAllRadio.setText(XHIBITConstant.getResource(resources, "lblAllCourtrooms"));
            printAllRadio.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    printOptionsRadio_actionPerformed(e);
                }
            });
        }

        return printAllRadio;
    }

    private JRadioButton getPrintCurRadio() {
        if (printCurRadio == null) {
            printCurRadio = new JRadioButton();
            printCurRadio.setSelected(model.isCourtRoomKnown());
            printCurRadio.setToolTipText(XHIBITConstant.getResource(resources, "lblCurrentCourtroom"));
            printCurRadio.setActionCommand(CounselFacilitiesHelper.CURRENT);
            printCurRadio.setMnemonic(XHIBITConstant.getResource(resources, "mnmCurrent").charAt(0));
            printCurRadio.setText(XHIBITConstant.getResource(resources, "lblCurrentCourtroom"));
            printCurRadio.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    printOptionsRadio_actionPerformed(e);
                }
            });
        }

        return printCurRadio;
    }

    private void printOptionsRadio_actionPerformed(@SuppressWarnings("unused") ActionEvent e) {
        stepUpdateViewState();
    }

    public void stepActivate() throws CSRecoverableException {
        moveModelToScreen();

        stepUpdateViewState();
    }

    /**
     * Set the courtroom name in the popup dialog for the printing.
     * 
     * @throws CSRecoverableException
     */
    private void moveModelToScreen() throws CSRecoverableException {
        getCourtroomText()
                .setText(
                        model.isCourtRoomKnown() ? XhibitSingleton.getInstance().getCourtRoomBasicValue()
                                .getDisplayName() : "");
    }

    public void stepUpdateViewState() {
        getPrintCurRadio().setEnabled(model.isCourtRoomKnown());
        getPrintAllRadio().setEnabled(model.isCourtRoomKnown());

        buttonPanel.okButton.setEnabled(isMandatoryFieldsCompleted());
    }

    /**
     * No mandatory fields required here so this method will always return true.
     * 
     * @return boolean
     */
    private boolean isMandatoryFieldsCompleted() {
        return true;
    }

    public void stepValidate() throws CSValidationException, CSRecoverableException {
        // empty
    }

    public void stepDeactivate() throws CSRecoverableException {
        moveScreenToModel();

        model.printModel();
    }

    /**
     * Set the selection the user has made to be printed and get the
     * courtroomid.
     */
    private void moveScreenToModel() {
        model.setSelectedOption(getPrintOptionsRadioButtonGroup().getSelection().getActionCommand());
        model.setSelectedCourtRoom(model.isCourtRoomKnown() ? XhibitSingleton.getInstance().getCourtRoomId() : null);
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
            // empty
        }
    }

    private void enableTextField(JTextField textField, boolean state) {
        textField.setEnabled(state);
        textField.setBackground((state ? SystemColor.white : SystemColor.text));
        if (state == false)
            textField.setText("");
    }

    public JComponent getFirstEnterableComponent() {
        return getPrintAllRadio();
    }
}
