package uk.gov.courtservice.xhibit.client.listdistribution;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.DocumentDistributionBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.listeners.ComboBoxListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: ManageLetterRecipientPanel
 * </p>
 * <p>
 * Description: Displays a Solicitor Firm for edit within the context of the
 * Warned List Letter distribution - i.e. includes distribution details
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * <p>
 * Author: G S Rajasekaran
 * </p>
 * 
 * @author G S Rajasekaran
 * @author Sarah Tong
 */
public class ManageLetterRecipientPanel extends XPanel {
    private final ManageLetterRecipientModel model;

    private JLabel panelTitleLabel;

    private JLabel solicitorFirmNameLabel;

    private JTextField solicitorFirmNameText;

    private JLabel postalAddressLabel;

    private JTextField postalAddressText;

    private JLabel contactMethodLabel;

    private JLabel subscriptionLabel;

    private JComboBox contactMethodComboBox;

    private JLabel emailAddressLabel;

    private JTextField emailAddressText;

    private JLabel faxNumberLabel;

    private JTextField faxNumberText;

    private JLabel requestLetterLabel;

    private JLabel documentFormatLabel;

    private JComboBox documentFormatComboBox;

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
    public ManageLetterRecipientPanel(XDialog parent, ManageLetterRecipientModel model) throws CSRecoverableException {
        super();
        this.model = model;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    private void jbInit() {
        this.setLayout(new GridBagLayout());

        // the heading
        this.add(getPanelTitleLabel(), new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        // name label
        this.add(getSolicitorFirmNameLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        // name text
        this.add(getSolicitorFirmNameText(), new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        // post address label
        this.add(getPostalAddressLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        // post address text
        this.add(getPostalAddressText(), new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        // email label
        this.add(getEmailAddressLabel(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        // email text
        this.add(getEmailAddressText(), new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        // fax label
        this.add(getFaxNumberLabel(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        // fax text
        this.add(getFaxNumberText(), new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        // letter identification label
        this.add(getRequestLetterLabel(), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        // letter identification text
        this.add(getSubscriptionLabel(), new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        // contact method label
        this.add(getContactMethodLabel(), new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        // contact method drop down
        this.add(getContactMethodComboBox(), new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        // doc format label
        this.add(getDocumentFormatLabel(), new GridBagConstraints(0, 7, 1, 2, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        // doc format drop down
        this.add(getDocumentFormatComboBox(), new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    // Get label values
    private JLabel getPanelTitleLabel() {
        if (panelTitleLabel == null) {
            panelTitleLabel = new PanelTitleLabel(model.getPanelTitle());
        }
        return panelTitleLabel;
    }

    private JLabel getSolicitorFirmNameLabel() {
        if (solicitorFirmNameLabel == null) {
            solicitorFirmNameLabel = new JLabel();
            solicitorFirmNameLabel.setText(XHIBITConstant.getResource(XhibitBundles.ManageLists,
                    "solicitorFirmNameLabel"));
        }
        return solicitorFirmNameLabel;
    }

    private JLabel getPostalAddressLabel() {
        if (postalAddressLabel == null) {
            postalAddressLabel = new JLabel();
            postalAddressLabel.setText(XHIBITConstant.getResource(XhibitBundles.ManageLists, "postalAddressLabel"));
        }
        return postalAddressLabel;
    }

    private JLabel getContactMethodLabel() {
        if (contactMethodLabel == null) {
            contactMethodLabel = new JLabel();
            contactMethodLabel.setText(XHIBITConstant.getResource(XhibitBundles.ManageLists, "contactMethodLabel"));
        }
        return contactMethodLabel;
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

    private JLabel getRequestLetterLabel() {
        if (requestLetterLabel == null) {
            requestLetterLabel = new JLabel();
            requestLetterLabel.setText(XHIBITConstant.getResource(XhibitBundles.ManageLists, "requestLetterLabel"));
        }
        return requestLetterLabel;
    }

    private JLabel getDocumentFormatLabel() {
        if (documentFormatLabel == null) {
            documentFormatLabel = new JLabel();
            documentFormatLabel.setText(XHIBITConstant.getResource(XhibitBundles.ManageLists, "documentFormatLabel"));
        }
        return documentFormatLabel;
    }

    private JLabel getSubscriptionLabel() {
        if (subscriptionLabel == null) {
            subscriptionLabel = new JLabel();
            subscriptionLabel.setText(model.getListType());
        }
        return subscriptionLabel;
    }

    // Define text fields
    private JTextField getSolicitorFirmNameText() {
        if (solicitorFirmNameText == null) {
            solicitorFirmNameText = new JTextField();
            solicitorFirmNameText.setText(model.getWLLRecipientName());
            // not editable as this is reference data from CREST and
            // therefore not
            // updateable by the Xhibit system
            solicitorFirmNameText.setEditable(false);
            // CR47: Set border to line to indicate that it is not editable
            solicitorFirmNameText.setBorder(new LineBorder(Color.black));
        }
        return solicitorFirmNameText;
    }

    private JTextField getPostalAddressText() {
        if (postalAddressText == null) {
            postalAddressText = new JTextField();
            postalAddressText.setText(model.getSolicitorFirmAddress());
            // not editable as this is reference data from CREST and
            // therefore not
            // updateable by the Xhibit system
            postalAddressText.setEditable(false);
            // CR47: Set border to line to indicate that it is not editable
            postalAddressText.setBorder(new LineBorder(Color.black));
        }
        return postalAddressText;
    }

    private JComboBox getContactMethodComboBox() {
        if (contactMethodComboBox == null) {
            Vector contactMtdVector = ListDistributionUtilities.getComboBoxContents("ldcmwll_");
            DefaultComboBoxModel dcbm = new DefaultComboBoxModel(contactMtdVector);
            contactMethodComboBox = new JComboBox(dcbm);
            if (model.getDistributionType() != null) {
                ListDistributionUtilities.setSelectedItemByCode(contactMethodComboBox, model.getDistributionType());
            }

            // add listener - need to know if FAX selected as we will
            // default the
            // mime type in this case
            contactMethodComboBox.addActionListener(new ComboBoxListener(this));
        }

        return contactMethodComboBox;
    }

    private JComboBox getDocumentFormatComboBox() {
        if (documentFormatComboBox == null) {
            Vector docFormatVector = ListDistributionUtilities.getComboBoxContents("lddf_");

            DefaultComboBoxModel dcbm = new DefaultComboBoxModel(docFormatVector);
            documentFormatComboBox = new JComboBox(dcbm);
            if (model.getMimeType() != null) {
                ListDistributionUtilities.setSelectedItemByCode(documentFormatComboBox, model.getMimeType());
            }
        }
        return documentFormatComboBox;
    }

    private JTextField getEmailAddressText() {
        if (emailAddressText == null) {
            emailAddressText = new JTextField();
            emailAddressText.setText(model.getSolicitorFirmEmailAddress());
            emailAddressText.setEditable(false);
            // CR47: Set border to line to indicate that it is not editable
            emailAddressText.setBorder(new LineBorder(Color.black));
            // emailAddressText.setInputVerifier(new
            // EmailAddressVerifier());
        }
        return emailAddressText;
    }

    private JTextField getFaxNumberText() {
        if (faxNumberText == null) {
            faxNumberText = new JTextField();
            faxNumberText.setText(model.getSolicitorFirmFaxNumber());
        }
        return faxNumberText;
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
        if (update) {
            if (model.getTransactionType().equals(ManageLetterRecipientModel.POPULATECOMPLEX)) {
                model.setSolicitorFirmEmailAddress(emailAddressText.getText());
                model.setSolicitorFirmFaxNumber(faxNumberText.getText());
                model.setDistributionType(((ComboContentsValue) contactMethodComboBox.getSelectedItem()).getCode());
                model.setMimeType(((ComboContentsValue) documentFormatComboBox.getSelectedItem()).getCode());
            } else if (model.getPanelTitle().equals(
                    XHIBITConstant.getResource(XhibitBundles.ManageLists, "deleteWLLPanelTitleLabel"))) {
                deleteRecipient();
            } else {
                model.setSolicitorFirmEmailAddress(emailAddressText.getText());
                model.setSolicitorFirmFaxNumber(faxNumberText.getText());
                model.setDistributionType(((ComboContentsValue) contactMethodComboBox.getSelectedItem()).getCode());
                model.setMimeType(((ComboContentsValue) documentFormatComboBox.getSelectedItem()).getCode());

                if (model.getPanelTitle().equals(
                        XHIBITConstant.getResource(XhibitBundles.ManageLists, "addWLLPanelTitleLabel"))) {
                    addRecipient();
                }
                if (model.getPanelTitle().equals(
                        XHIBITConstant.getResource(XhibitBundles.ManageLists, "editWLLPanelTitleLabel"))) {
                    editRecipient();
                }
            }
        }
    }

    /**
     * Empty implementation of abstract class method.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
    }

    /**
     * Validates the user input. Ensures that a value had been entered for the
     * selected contact method.
     * 
     * @throws CSValidationException
     *             If there is a validation error.
     */
    public void stepValidate() throws CSValidationException {
        // if email selected ensure an email address has been entered
        // CR47 if
        // ("Email".equalsIgnoreCase(contactMethodComboBox.getSelectedItem().toString()))
        // {
        // if ((getEmailAddressText().getText().trim().length() == 0))
        // {
        // getEmailAddressText().requestFocus();
        // throw new CSValidationException("validation.shortlength",
        // new String[]{XHIBITConstant.getResource(resources,
        // "emailAddressLabel"), "0"},
        // "Email address not populated");
        // }
        // else if (!new
        // EmailAddressVerifier().isEmailAddressValid(getEmailAddressText().getText().trim()))
        // {
        // getEmailAddressText().requestFocus();
        // throw new CSValidationException("gui.email.address.error.message",
        // new String[]{XHIBITConstant.getResource(resources,
        // "gui.email.address.error.title"), "0"},
        // "Email address not correct format");
        // }
        // }
        //
        // if fax selected ensure a fax number has been entered
        if ("Fax".equalsIgnoreCase(contactMethodComboBox.getSelectedItem().toString())
                && (getFaxNumberText().getText().trim().length() == 0)) {
            getFaxNumberText().requestFocus();
            throw new CSValidationException("validation.shortlength", new String[] {
                    XHIBITConstant.getResource(XhibitBundles.ManageLists, "faxNumberLabel"), "0" },
                    "Fax number not populated");
        }
    }

    /**
     * Empty implementation of abstract class method.
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() {
        // do nothing , ldcm_FAX
        // if FAX has been selected then default the format to PDF and disable
        if (((ComboContentsValue) contactMethodComboBox.getSelectedItem()).getCode().equals(
                XHIBITConstant.getResource(XhibitBundles.ManageLists, "ldcmwll_FAX"))) {
            ListDistributionUtilities.setSelectedItemByCode(documentFormatComboBox, XHIBITConstant.getResource(
                    XhibitBundles.ManageLists, "lddf_PDF"));
            documentFormatComboBox.setEnabled(false);
        } else {
            documentFormatComboBox.setEnabled(true);
        }
    }

    /**
     * Calls the <code>stepUpdateViewState()</code> method.
     */
    public void stepActivate() {
        stepUpdateViewState();
    }

    /**
     * Find the data we need for this panel and store in the model.
     * 
     * @throws CSRecoverableException
     *             If there is a problem loading the data
     */
    public void stepInitialise() throws CSRecoverableException {
        if (model.getWLLRecipientId() == null) {
            // do nothing
        } else {
            WLLRecipientComplexValue complexWLLRecipient;

            if (model.getCallType() == "BASIC") {
                // ref solicitor firm if first adding
                complexWLLRecipient = XhibitDelegateHelper.getMaintainRecipientDelegate()
                        .findRefSolicitorFirmByCrestId(model.getCrestSolicitorFirmId(), model.getCourtId());
            } else {
                // WLLRecipient if in the context of the list
                complexWLLRecipient = XhibitDelegateHelper.getMaintainRecipientDelegate().findWLLRecipient(
                        model.getWLLRecipientId(),
                        XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
            }

            model.setComplexWLLRecipient(complexWLLRecipient);

            if (model.getCallType() != "BASIC") {
                DocumentDistributionBasicValue docDistribution;
                docDistribution = complexWLLRecipient.getDocumentDistribution();
                model.setDocDistribution(docDistribution);
            }
        }
    }

    /**
     * Uses the midtier service to add a new recipient to the letter
     * distribution
     * 
     * @throws CSRecoverableException
     *             if there is a problem adding the recipient
     */
    private void addRecipient() throws CSRecoverableException {
        // update the WLLComplexRecipient with the new DocumentDistribution
        model.getComplexWLLRecipient().setDocumentDistribution(model.getDocDistribution());

        WLLRecipientComplexValue[] recipientsToAdd = new WLLRecipientComplexValue[] { model.getComplexWLLRecipient() };

        /** @todo: handle adding multiple recipients in one go */
        model.setComplexWLLRecipient(XhibitDelegateHelper.getMaintainRecipientDelegate().addWLLRecipients(
                recipientsToAdd,
                XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME))[0]);
    }

    /**
     * Uses the midtier service to edit an existing recipient in the context of
     * a particular list
     * 
     * @throws CSRecoverableException
     *             if there is a problem updating the recipient
     */
    private void editRecipient() throws CSRecoverableException {
        XhibitDelegateHelper.getMaintainRecipientDelegate().updateWLLRecipient(model.getComplexWLLRecipient(),
                XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
    }

    /**
     * Uses the midtier service to delete a recipient from the letter
     * distribution
     * 
     * @throws CSRecoverableException
     *             if there is a problem deleting the recipient
     */
    public void deleteRecipient() throws CSRecoverableException {
        /** @todo: Handle removing multiple recipients in one go */
        WLLRecipientComplexValue[] recipientsToDelete = new WLLRecipientComplexValue[] { model.getComplexWLLRecipient() };

        XhibitDelegateHelper.getMaintainRecipientDelegate().removeWLLRecipients(recipientsToDelete);
    }
}