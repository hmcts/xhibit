package uk.gov.courtservice.xhibit.client.listdistribution;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.Document;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.DocumentDistributionBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RecipientBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.listeners.ComboBoxListener;
import uk.gov.courtservice.xhibit.client.util.text.LineLengthValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.verifiers.EmailAddressVerifier;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: ManageListRecipientPanel
 * </p>
 * <p>
 * Description: Builds the screen which provides the edit and create options for
 * a list recipient.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @version $Id: ManageListRecipientPanel.java,v 1.31 2005/08/26 11:30:45 szfnvt
 *          Exp $
 * @author G S Rajasekaran
 * @author Sarah Tong
 */
public class ManageListRecipientPanel extends XPanel {
    private JLabel panelTitleLabel;

    private ManageListRecipientModel model;

    private JLabel recipientNameLabel;

    private JTextField recipientNameText;

    private JLabel prefContactMethodLabel;

    private JComboBox prefContactMethodComboBox;

    private JLabel emailAddressLabel;

    private JTextField emailAddressText;

    private JLabel faxNumberLabel;

    private JTextField faxNumberText;

    private JLabel prefDocumentFormatLabel;

    private JComboBox prefDocumentFormatComboBox;

    private DeliveryMethodPanel dmp;

    /**
     * Sets the model, parent and resources for this panel. Stores a reference
     * to the parents <code>OkCancelPanel</code> Calls the
     * <code>stepInitialise()</code> method to load the data required for this
     * panel, calls <code>jbInit()</code> to build the panel components and
     * finally <code>setActivate()</code> to perform any actions necessary
     * before displaying the panel.
     * 
     * @param parent
     *            The parent screen to this panel.
     * @param model
     *            Class to store the data required to build this panel.
     * @throws CSRecoverableException
     *             If there is a problem loading the data.
     */
    public ManageListRecipientPanel(XDialog parent, ManageListRecipientModel model) throws CSRecoverableException {
        this.model = model;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    private void jbInit() throws CSRecoverableException {
        this.setLayout(new GridBagLayout());

        // The heading
        this.add(getPanelTitleLabel(), new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        // Name label
        this.add(getRecipientNameLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        // Name textbox
        this.add(getRecipientNameText(), new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        // Email label
        this.add(getEmailAddressLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        // Email text box
        this.add(getEmailAddressText(), new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        // Fax num label
        this.add(getFaxNumberLabel(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        // Fax num text box
        this.add(getFaxNumberText(), new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

        // Preferred contact method label
        this.add(getPrefContactMethodLabel(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        // Preferred contact method drop-down selection
        this.add(getPrefContactMethodComboBox(), new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

        // Preferred document format label
        this.add(getPrefDocumentFormatLabel(), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        // Preferred document format drop-down selection
        this.add(getPrefDocumentFormatComboBox(), new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

        // If this is not an 'Add' we want to identify the list we are working
        // on
        // and give the user the option to select a contact method which is
        // different from the preferred contact method
        if (model.getPanelTitle().equals(
                XHIBITConstant.getResource(XhibitBundles.ManageLists, "editListPanelTitleLabel"))) {
            dmp = new DeliveryMethodPanel(model);
            this.add(dmp, new GridBagConstraints(0, 7, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.BOTH, XHIBITConstant.containerInsets, 0, 0));
        }
    }

    // Get label values
    private JLabel getPanelTitleLabel() {
        if (panelTitleLabel == null) {
            panelTitleLabel = new PanelTitleLabel(model.getPanelTitle());
        }
        return panelTitleLabel;
    }

    private JLabel getRecipientNameLabel() {
        if (recipientNameLabel == null) {
            recipientNameLabel = new JLabel();
            recipientNameLabel.setText(XHIBITConstant.getResource(XhibitBundles.ManageLists, "recipientNameLabel"));
        }
        return recipientNameLabel;
    }

    private JLabel getPrefContactMethodLabel() {
        if (prefContactMethodLabel == null) {
            prefContactMethodLabel = new JLabel();
            prefContactMethodLabel.setText(XHIBITConstant.getResource(XhibitBundles.ManageLists,
                    "prefContactMethodLabel"));
        }
        return prefContactMethodLabel;
    }

    private JLabel getEmailAddressLabel() {
        if (emailAddressLabel == null) {
            emailAddressLabel = new JLabel();
            emailAddressLabel.setText(XHIBITConstant.getResource(XhibitBundles.ManageLists, "emailAddressLabel"));
        }
        return emailAddressLabel;
    }

    private JLabel getFaxNumberLabel() {
        if (faxNumberLabel == null) {
            faxNumberLabel = new JLabel();
            faxNumberLabel.setText(XHIBITConstant.getResource(XhibitBundles.ManageLists, "faxNumberLabel"));
        }
        return faxNumberLabel;
    }

    private JLabel getPrefDocumentFormatLabel() {
        if (prefDocumentFormatLabel == null) {
            prefDocumentFormatLabel = new JLabel();
            prefDocumentFormatLabel.setText(XHIBITConstant.getResource(XhibitBundles.ManageLists,
                    "prefDocumentFormatLabel"));
        }
        return prefDocumentFormatLabel;
    }

    // Define text fields
    private JTextField getRecipientNameText() {
        if (recipientNameText == null) {
            recipientNameText = createTextField(model.getRecipientName(), 255, 50);
            if (model.getPanelTitle().equals(
                    XHIBITConstant.getResource(XhibitBundles.ManageLists, "deletePanelTitleLabel"))) {
                recipientNameText.setDisabledTextColor(SystemColor.controlText);
                recipientNameText.setEditable(false);
            }
        }
        return recipientNameText;
    }

    private JComboBox getPrefContactMethodComboBox() {
        if (prefContactMethodComboBox == null) {
            Vector contactMtdVector = ListDistributionUtilities.getComboBoxContents("ldcm_");
            DefaultComboBoxModel dcbm = new DefaultComboBoxModel(contactMtdVector);
            prefContactMethodComboBox = new JComboBox(dcbm);
            // get the value from the model
            if (model.getPrefDistributionType() != null) {
                ListDistributionUtilities.setSelectedItemByCode(prefContactMethodComboBox, model
                        .getPrefDistributionType());
            }

            // add listener - need to know if FAX selected as we will
            // default the
            // mime type in this case
            prefContactMethodComboBox.addActionListener(new ComboBoxListener(this));

        }
        return prefContactMethodComboBox;
    }

    private JComboBox getPrefDocumentFormatComboBox() {
        if (prefDocumentFormatComboBox == null) {
            Vector docFormatVector = ListDistributionUtilities.getComboBoxContents("lddf_");
            DefaultComboBoxModel dcbm = new DefaultComboBoxModel(docFormatVector);
            prefDocumentFormatComboBox = new JComboBox(dcbm);
            if (model.getPrefMimeType() != null) {
                ListDistributionUtilities.setSelectedItemByCode(prefDocumentFormatComboBox, model.getPrefMimeType());
            }
        }
        return prefDocumentFormatComboBox;
    }

    /**
     * Given protected access so that can be accessed from child panel
     * DeliveryMethodPanel
     * 
     * @return JTextField
     */
    protected JTextField getEmailAddressText() {
        if (emailAddressText == null) {
            emailAddressText = createTextField(model.getRecipientEmailAddress(), 255, 50);
            if (model.getPanelTitle().equals(
                    XHIBITConstant.getResource(XhibitBundles.ManageLists, "deletePanelTitleLabel"))) {
                emailAddressText.setDisabledTextColor(SystemColor.controlText);
                emailAddressText.setEditable(false);
            }
            emailAddressText.setInputVerifier(new EmailAddressVerifier());
        }
        return emailAddressText;
    }

    /**
     * Given protected access so that can be accessed from child panel
     * DeliveryMethodPanel
     * 
     * @return JTextField
     */
    protected JTextField getFaxNumberText() {
        if (faxNumberText == null) {
            faxNumberText = createNumericJTextField(model.getRecipientFaxNumber(), 14);
            if (model.getPanelTitle().equals(
                    XHIBITConstant.getResource(XhibitBundles.ManageLists, "deletePanelTitleLabel"))) {
                faxNumberText.setDisabledTextColor(SystemColor.controlText);
                faxNumberText.setEditable(false);
            }
        }
        return faxNumberText;
    }

    /**
     * Private method used to create text numeric limited text fields
     * 
     * @param size
     *            The maximum number of characters that the text field should
     *            allow.
     * @return The newly create <code>JTextField</code> with decorators and
     *         listeners added
     */
    private JTextField createNumericJTextField(String value, int length) {
        // create the required capabilities (document decorators/validators)
        Capability numeric = Capability.numeric();
        Capability limitedText = Capability.limitedText(length);

        // Create the text document with the required capabilities.
        Document doc = DocumentFactory.newDocument(new Capability[] { numeric, limitedText });

        // create the text field to return
        final JTextField textField = JTextFieldFactory.getTextField(doc);
        textField.setText(clearNull(value));

        return textField;
    }

    /**
     * Create a text field with the specified text for entering length
     * characters
     */
    private JTextField createTextField(String text, int length) {
        return createTextField(text, length, length);
    }

    /**
     * Create a text field with the specified text for entering length
     * characters
     */
    private JTextField createTextField(String text, int textLength, int textFieldLength) {
        return new JTextField(new LineLengthValidatingDocumentDecorator(textLength), text, textFieldLength);
    }

    /**
     * Private helper method (will be in-lined by the compiler) to prevent
     * <i>null</i> value being passed around as <code>String</code>'s
     * 
     * @param obj
     * @return A guaranteed non-null <code>String</code>, if the passed in
     *         <code>Object</code> is <i>null</i> then a new, 0-length
     *         <code>String</code> will be returned
     */
    private String clearNull(Object obj) {
        return ((obj == null) ? "" : obj.toString());
    }

    /**
     * Moves user entered \ updated data from the panel to the model. Calls the
     * relevant method to perform the action requested (add\edit\delete)
     * 
     * @param update
     *            true if an action is to be performed on closing the window,
     *            false otherwise (e.g. if cancel pressed)
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        // set the values from the delivery method panel if applicable, the
        // delivery
        // panel is not present if adding a new recipient to the system
        if (dmp != null)
            dmp.stepDeinitialise(update);

        if (update) {
            if (model.getPanelTitle().equals(
                    XHIBITConstant.getResource(XhibitBundles.ManageLists, "deletePanelTitleLabel"))) {
                deleteRecipient();
            } else {
                // move screen to model...
                model.setRecipientName(recipientNameText.getText());
                model.setRecipientEmailAddress(emailAddressText.getText());
                model.setRecipientFaxNumber(faxNumberText.getText());
                model.setPrefDistributionType(((ComboContentsValue) prefContactMethodComboBox.getSelectedItem())
                        .getCode());
                model.setPrefMimeType(((ComboContentsValue) prefDocumentFormatComboBox.getSelectedItem()).getCode());
                // if we are using the pref dist type then overwrite the other
                if (model.isUsePrefDistType()) {
                    model.setDistributionType(model.getPrefDistributionType());
                    model.setMimeType(model.getPrefMimeType());
                }

                // if we're adding a new recipient
                if (model.getPanelTitle().equals(
                        XHIBITConstant.getResource(XhibitBundles.ManageLists, "addPanelTitleLabel"))) {
                    addRecipient();
                }

                // if we're editing a recipient or a list recipient
                else if (model.getPanelTitle().equals(
                        XHIBITConstant.getResource(XhibitBundles.ManageLists, "editPanelTitleLabel"))
                        || model.getPanelTitle().equals(
                                XHIBITConstant.getResource(XhibitBundles.ManageLists, "editListPanelTitleLabel"))) {
                    editRecipient();
                }
            }

            // Now close the dialog window
            // model.setOkClicked(true);
        }
    }

    /**
     * Empty implementation of abstract class method.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        // do nothing
    }

    /**
     * Validates the user input. Ensures that a recipient name has been entered,
     * a value had been entered for the selected contact method.
     * 
     * @throws CSValidationException
     *             If there is a validation error.
     */
    public void stepValidate() throws CSValidationException {
        if (recipientNameText != null) {
            // make sure there is a recipient name preset
            if (getRecipientNameText().getText().trim().length() == 0) {
                getRecipientNameText().requestFocus();
                throw new CSValidationException("validation.shortlength", new String[] {
                        XHIBITConstant.getResource(XhibitBundles.ManageLists, "recipientNameLabel"), "0" },
                        "Recipient name not populated");
            }
        }
        if (prefContactMethodComboBox != null) {
            // make sure there is a value for the email address
            if ("Email".equalsIgnoreCase(prefContactMethodComboBox.getSelectedItem().toString())) {
                if (getEmailAddressText().getText().trim().length() == 0) {
                    getEmailAddressText().requestFocus();
                    throw new CSValidationException("validation.shortlength", new String[] {
                            XHIBITConstant.getResource(XhibitBundles.ManageLists, "emailAddressLabel"), "0" },
                            "Email address not populated");
                } else if (!EmailAddressVerifier.isEmailAddressValid(getEmailAddressText().getText().trim())) {
                    getEmailAddressText().requestFocus();
                    throw new CSValidationException("gui.email.address.error.message",
                            new String[] {
                                    XHIBITConstant.getResource(XhibitBundles.ManageLists,
                                            "gui.email.address.error.title"), "0" }, "Email address not correct format");
                }
            }
            // make sure there is a value for the fax number
            else if ("Fax".equalsIgnoreCase(prefContactMethodComboBox.getSelectedItem().toString())
                    && (getFaxNumberText().getText().trim().length() == 0)) {
                getFaxNumberText().requestFocus();
                throw new CSValidationException("validation.shortlength", new String[] {
                        XHIBITConstant.getResource(XhibitBundles.ManageLists, "faxNumberLabel"), "0" },
                        "Fax number not populated");
            }
        }

        // validate the child panel if present
        if (dmp != null) {
            dmp.stepValidate();
        }
    }

    /**
     * Empty implementation of abstract class method.
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() {
        // if FAX has been selected then default the format to PDF and disable
        if (((ComboContentsValue) prefContactMethodComboBox.getSelectedItem()).getCode().equals(
                XHIBITConstant.getResource(XhibitBundles.ManageLists, "ldcm_FAX"))) {
            ListDistributionUtilities.setSelectedItemByCode(prefDocumentFormatComboBox, XHIBITConstant.getResource(
                    XhibitBundles.ManageLists, "lddf_PDF"));
            prefDocumentFormatComboBox.setEnabled(false);
        } else {
            prefDocumentFormatComboBox.setEnabled(true);
        }
    }

    /**
     * Calls the <code>stepUpdateViewState()</code> method.
     */
    public void stepActivate() {
        stepUpdateViewState();
    }

    /**
     * Find the data we need for this panel and stores in the model.
     * 
     * @throws CSRecoverableException
     *             If there is a problem loading the data
     */
    public void stepInitialise() throws CSRecoverableException {
        // load the data we need
        if (model.getRecipientId() == null) {
            // do nothing
        } else {
            Vector docD = new Vector();

            model.setComplexRecipient(XhibitDelegateHelper.getMaintainRecipientDelegate().findRecipient(
                    model.getRecipientId(), model.getDocumentType(),
                    XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME)));

            if (!model.getCallType().equals("BASIC")) {
                // there should only be one distribution as we have retrieved
                // the data
                // by recipient Id AND document type
                docD.addAll(model.getComplexRecipient().getDocumentDistribution());
                if (docD.size() > 0)
                    model.setDocDistribution((DocumentDistributionBasicValue) docD.firstElement());
            }
        }
    }

    /**
     * Uses the midtier service to add a new recipient to the system
     */
    private void addRecipient() {
        model.setBasicRecipient(XhibitDelegateHelper.getMaintainRecipientDelegate().addRecipient(
                model.getBasicRecipient(),
                XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME)));
    }

    /**
     * Uses the midtier service to edit an existing recipient in the context of
     * a particular list
     * 
     * @throws CSRecoverableException
     *             if there is a problem updating the recipient
     */
    private void editRecipient() throws CSRecoverableException {
        model.setBasicRecipient(XhibitDelegateHelper.getMaintainRecipientDelegate().updateRecipient(
                model.getComplexRecipient(),
                XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME)));
    }

    /**
     * Uses the midtier service to delete a recipient from the system
     * 
     * @throws CSRecoverableException
     *             if there is a problem deleting the recipient
     */
    private void deleteRecipient() throws CSRecoverableException {
        /** @todo: Handle removing multiple recipients in one go */
        RecipientBasicValue[] recipientsToDelete = new RecipientBasicValue[] { model.getBasicRecipient() };

        XhibitDelegateHelper.getMaintainRecipientDelegate().removeRecipients(recipientsToDelete);
    }
}