package uk.gov.courtservice.xhibit.client.maintaincharges.log;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT 2 - Crest Indictment Dialog
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Antoniou
 * @version 1.0
 */
public class CrestIndictmentLogDialog extends XDialog implements DocumentListener {
    /** Case value that containts the log information to be saved. */
    protected CaseBasicValue caseBasicValue = null;

    /** The text area that containts the log data. */
    private JTextArea logTextArea = null;

    private String logInMemory = null;

    private final int validLogLength = 78 * 6;

    /**
     * Default constructor.
     * 
     * @param parent
     *            the parent of this dialog.
     * @param caseBasicValue
     *            the case basic value that containts the log information to be
     *            saved.
     * @param logInMemory
     *            log text store in memory.
     * @param dialogWidth
     *            width of the dialog in pixels.
     * @param dialogHeight
     *            height of the dialog in pixels.
     */
    public CrestIndictmentLogDialog(JFrame parent, CaseBasicValue caseBasicValue, String logInMemory, int dialogWidth,
            int dialogHeight) {
        super(parent, IndictmentLogResource.DIALOGTITLE + " " + caseBasicValue.getCaseType()
                + caseBasicValue.getCaseNumber(), true);

        this.caseBasicValue = caseBasicValue;
        this.logInMemory = logInMemory;
        init(dialogWidth, dialogHeight);
    }

    public CrestIndictmentLogDialog(JDialog parent, CaseBasicValue caseBasicValue, String logInMemory, int dialogWidth,
            int dialogHeight) {
        super(parent, IndictmentLogResource.DIALOGTITLE + " " + caseBasicValue.getCaseType()
                + caseBasicValue.getCaseNumber(), true, XDialog.OKCANCEL, XDialog.DEFAULTOK);

        this.caseBasicValue = caseBasicValue;
        this.logInMemory = logInMemory;
        init(dialogWidth, dialogHeight);
    }

    /**
     * Setup the GUI components and with the curent log text from the
     * CaseBasicValue.
     * 
     * @param dialogWidth
     *            width of the dialog in pixels.
     * @param dialogHeight
     *            height of the dialog in pixels.
     */
    private void init(int dialogWidth, int dialogHeight) {
        // Set the text area that containts the log.
        this.logTextArea = new JTextArea();
        this.logTextArea.setEditable(true);
        this.logTextArea.setWrapStyleWord(true);
        this.logTextArea.setLineWrap(true);
        this.logTextArea.setText(this.logInMemory);
        this.logTextArea.setFont(XHIBITConstant.getCurrentFont());
        this.logTextArea.getDocument().addDocumentListener(this);

        JTextArea narratorTextArea = new JTextArea(IndictmentLogResource.DIALOGNARRATOR);
        narratorTextArea.setEditable(false);
        narratorTextArea.setFont(XHIBITConstant.getCurrentFont());
        narratorTextArea.setBackground(this.getContentPane().getBackground());
        narratorTextArea.setWrapStyleWord(true);
        narratorTextArea.setLineWrap(true);
        narratorTextArea.setAlignmentY(javax.swing.SwingConstants.CENTER);

        JScrollPane narratorScrollPane = new JScrollPane(narratorTextArea);
        narratorScrollPane.setBorder(null);

        GridBagConstraints gbConstraints = null;
        JPanel narratorPanel = new JPanel(new GridBagLayout());
        gbConstraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
        narratorPanel.add(narratorScrollPane, gbConstraints);
        narratorScrollPane.setBorder(null);
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.add(new JScrollPane(this.logTextArea), BorderLayout.CENTER);

        // Dimension narratorSize = narratorPanel.getPreferredSize();
        narratorScrollPane.setPreferredSize(new Dimension(0, (int) (dialogWidth * 0.15)));
        narratorPanel.setPreferredSize(new Dimension(0, (int) (dialogWidth * 0.15)));
        narratorPanel.setMinimumSize(new Dimension(0, (int) (dialogWidth * 0.15)));

        logPanel.setPreferredSize(new Dimension(0, (int) (dialogWidth * 0.55)));
        logPanel.setMaximumSize(logPanel.getPreferredSize());
        logPanel.setMinimumSize(logPanel.getPreferredSize());

        JPanel mainLogPanel = new JPanel();
        mainLogPanel.setLayout(new GridBagLayout());
        gbConstraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        mainLogPanel.add(narratorPanel, gbConstraints);
        gbConstraints = new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        mainLogPanel.add(logPanel, gbConstraints);
        mainPanel.add(mainLogPanel, BorderLayout.CENTER);
        mainPanel.add(getButtonPanel(), BorderLayout.SOUTH);

        this.validateLog();

        this.setSize(new Dimension(dialogWidth, dialogHeight));
        this.setResizable(false);
        this.centreDialog();
    }

    /**
     * Overrides okClicked in XDialog.
     * 
     * @param e
     *            action event
     * @throws Exception
     */
    public void okClicked(ActionEvent e) throws Exception {
        super.okClicked(e);
        try {
            IndictmentLogHelper.updateIndictmentInfo(caseBasicValue, logTextArea.getText().trim());
            this.setVisible(false);
            this.dispose();
            CrestIndictmentLog.getInstance().clearChanges(this.caseBasicValue);
        } catch (CSValidationException csve) {
            JOptionPane.showMessageDialog(this, IndictmentLogResource.TOOMANYCHARS_MESSAGE,
                    IndictmentLogResource.TOOMANYCHARS_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Overrides cancelClicked in XDialog.
     * 
     * @param e
     *            actionevent
     * @throws Exception
     */
    public void cancelClicked(ActionEvent e) throws Exception {
        super.cancelClicked(e);
        this.setVisible(false);
        this.dispose();
    }

    public void clearChangesForCase() {
        CrestIndictmentLog.getInstance().clearChanges(this.caseBasicValue);
    }

    /**
     * Implementation of changeUpdate() in DocumentListener interface.
     */
    public void changedUpdate(DocumentEvent e) {
        //this.validateLog();
    }

    /**
     * Implementation of insertUpdate() in DocumentListener interface.
     */
    public void insertUpdate(DocumentEvent e) {
        //this.validateLog();
    }

    /**
     * Implementation of removeUpdate() in DocumentListener interface.
     */
    public void removeUpdate(DocumentEvent e) {
        //this.validateLog();
    }

    /**
     * Will check to see if the log is less than 468 characters long (the
     * maximum amount CREST can take) Save button will be disabled if text > 468
     * characters.
     * This is no longer required after RFC2867 so the Save button will always be 
     * enabled
     */
    private void validateLog() {
        String logText = this.logTextArea.getText();
        if (logText != null) {
            ((OkCancelPanel) getButtonPanel()).okButton.setEnabled(true);
        }
    }
}