package uk.gov.courtservice.xhibit.client.maintaincharges.joinder;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: Select Case Panel
 * </p>
 * <p>
 * Description: This class provide the user a gui for selecting a case. It is
 * the first screen in the joinder indictment wizard.
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

public class SelectCasePanel extends JoinderIndictmentPanel implements DocumentListener {
    /** Text field used for entering the case number. */
    private JTextField caseNumberTextField = null;

    /**
     * The dialog that shows the gavel while it retrieves the case and cits
     * charges.
     */
    private OpenCaseDialog dialog;

    /**
     * Prevents activate reentrancy issues due to setVisible changes in the
     * underlying JDK 1.5
     */
    private boolean subActivateReentrancy = false;

    /**
     * Creates a select case panel.
     * 
     * @param wizardDialog
     *            the containing wizard.
     */
    public SelectCasePanel(XWizardDialog wizardDialog) {
        super(wizardDialog);
        try {
            this.stepInitialise();
        } catch (CSRecoverableException e) {
            XHIBITErrorHandler.handleError(e);
        }
    }

    /**
     * Overrides the abstract method in JoinderIndictmentPanel to set up the
     * screen.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        // Create the gui components.
        PanelTitleLabel titleLabel = new PanelTitleLabel(ResourceBundleHelper.getResource(resources,
                JoinderConstants.SELECT_CASE1));
        JLabel caseNumberLabel = new JLabel(ResourceBundleHelper.getResource(resources, JoinderConstants.CASE_NUMBER));
        this.caseNumberTextField = new JTextField();
        this.caseNumberTextField.setDocument(new CaseTypeDocument());

        // Initialise the dialog used to render the process of opening a case.
        dialog = new OpenCaseDialog(wizardDialog);

        // Add the document listener to the case number text field.
        this.caseNumberTextField.getDocument().addDocumentListener(this);

        // Add the components to the panel.
        setLayout(gbLayout);

        // Add the title label.
        gbConstraints = new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
        add(titleLabel, gbConstraints);

        // Add the Case Number Label
        gbConstraints = new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE,
                XHIBITConstant.nonContainerInsets, 0, 0);
        add(caseNumberLabel, gbConstraints);

        // Add the Case Number TextField
        gbConstraints = new GridBagConstraints(1, 1, 1, 1, 0.0, 1.0, GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
        add(caseNumberTextField, gbConstraints);

        caseNumberTextField.requestFocus();
    }

    /**
     * Implementation of abstract method in XPanel. Validates the case number.
     * 
     * @throws CSValidationException
     */
    public void stepValidate() throws CSValidationException {
        String numberPart = caseNumberTextField.getText().substring(1);
        try {
            Integer.parseInt(numberPart);
        } catch (NumberFormatException ex) {
            caseNumberTextField.requestFocus();
            throw new CSValidationException("validation.casenumber.badformat", "Letters entered in the case number");
        } catch (ArrayIndexOutOfBoundsException ex) {
            caseNumberTextField.requestFocus();
            throw new CSValidationException("validation.casenumber.badformat", "Letters entered in the case number");
        }
    }

    /**
     * XPanel implementation called each time the screen is shown.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        super.stepActivate();
        // Refer to JoinderIndictmentPanel superclass for reentrancy details
        if (subActivateReentrancy) {
            log.debug("SelectCasePanel - stepActivate reentrancy");
            return;
        } else {
            subActivateReentrancy = true;
        }
        wizardDialog.getButtonPanel().getNext().setEnabled(false);
        this.caseNumberTextField.requestFocus();
    }

    /**
     * XPanel implementation
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        // super.stepUpdateViewState();
        if (model != null)
            model.reset();
    }

    /**
     * This method is called by stepDeactivate () in JoinderIndictmentPanel. Is
     * only ever called when the next even is fired from the dialog.
     * <P>
     * Opens the case for the given number, synchronizes the case and then
     * collects the charges which all become stored in the model.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivateOnNext() throws CSRecoverableException {
        String fullCaseNo = caseNumberTextField.getText().trim();
        String caseType = fullCaseNo.substring(0, 1);
        String caseNumber = fullCaseNo.substring(1);

        model.setSelectedCaseType(caseType);
        model.setSelectedCaseNumber(Integer.valueOf(caseNumber));
        model.setSelectedCaseTypeNumber(fullCaseNo);

        dialog.openCase(model);

        log.debug("ERROR TYPE: " + dialog.getErrorType());

        switch (dialog.getErrorType()) {
        case OpenCaseDialog.NO_ERROR:
            break;

        case OpenCaseDialog.CASE_OPEN_CANCEL:
            throw new UserCancelException();

        case OpenCaseDialog.CASE_NOT_FOUND:
            throw new CSRecoverableException("case.casenotfound", new Object[] { caseNumber, caseType }, "case "
                    + fullCaseNo + " not found");

        case OpenCaseDialog.CHARGE_ERROR:
            throw new CSRecoverableException("gui.ChargesController.loadCharges",
                    "Exception whilst getting the charge composite value object");

        case OpenCaseDialog.CASE_OPEN_ERROR:
            displayError();

        case OpenCaseDialog.CASE_OPEN_UNKNOWN_ERROR:
            displayError();
        }
    }

    /**
     * Sets reentrancy variable
     */
    public void stepDeactivateOnAll() {
        subActivateReentrancy = false;
    }

    /**
     * Called from stepDeactivateOnNext() to display the error message for the
     * user, then throws a user cancel exception to stop the wizard moving to
     * the next screen.
     * 
     * @throws UserCancelException
     *             to stop the wizard moving to the next screen.
     */
    private void displayError() throws UserCancelException {
        JOptionPane.showMessageDialog(wizardDialog, dialog.getErrorMessage(), dialog.getErrorTitle(),
                JOptionPane.ERROR_MESSAGE);

        throw new UserCancelException();
    }

    /**
     * Implementation of changeUpdate() in DocumentListener interface.
     * 
     * Will check to see if there is data in the case number text field after
     * the document event has been generated. If there is no data, then the
     * nextButton becomes disabled, otherwise it becomes enabled
     * 
     * @param e
     *            the document event.
     */
    public void changedUpdate(DocumentEvent e) {
        guiValidate();
    }

    /**
     * Implementation of insertUpdate() in DocumentListener interface.
     * 
     * Will check to see if there is data in the case number text field after
     * the document event has been generated. If there is no data, then the
     * nextButton becomes disabled, otherwise it becomes enabled
     * 
     * @param e
     *            the document event.
     */
    public void insertUpdate(DocumentEvent e) {
        guiValidate();
    }

    /**
     * Implementation of removeUpdate() in DocumentListener interface.
     * 
     * Will check to see if there is data in the case number text field after
     * the document event has been generated. If there is no data, then the
     * nextButton becomes disabled, otherwise it becomes enabled
     * 
     * @param e
     *            the document event.
     */
    public void removeUpdate(DocumentEvent e) {
        guiValidate();
    }

    /**
     * Clears the text field.
     */
    public void reset() {
        this.caseNumberTextField.setText("");
        this.caseNumberTextField.requestFocus();
    }

    private void guiValidate() {
        int noLength = caseNumberTextField.getText().trim().length();
        wizardDialog.getButtonPanel().getNext().setEnabled(noLength > 0);
        setDefaultButton();
    }

    /**
     * 
     * <p>
     * Title: Case Type Document
     * </p>
     * <p>
     * Description: A document for enforcing input to conform to the standard in
     * which the full case number is recognised.
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
    protected class CaseTypeDocument extends PlainDocument {
        /**
         * Creates a case type document.
         */
        public CaseTypeDocument() {
        }

        /**
         * Overrides the method of the superclass, to convert the first letter
         * to a capital.
         * 
         * @param offset
         *            the starting offset >= 0
         * @param str
         *            the string to insert; does nothing with null/empty strings
         * @param a
         *            the attributes for the inserted content.
         */
        public void insertString(int offset, String str, AttributeSet a) {
            try {
                if (offset == 0 && str != null && str.length() > 0 && Character.isLetter(str.charAt(0))) {
                    String restOfCaseNumber = "";
                    if (str.length() > 1) {
                        restOfCaseNumber = str.substring(1);
                    }
                    String caseNumber = "" + Character.toUpperCase(str.charAt(0)) + restOfCaseNumber;
                    super.insertString(offset, caseNumber, a);
                } else {
                    super.insertString(offset, str, a);
                }
            } catch (BadLocationException e) {
                XHIBITConstant.error("BAD LOCATION EXCEPTION FIRED: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}