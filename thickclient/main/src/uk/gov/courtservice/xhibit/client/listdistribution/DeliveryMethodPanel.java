package uk.gov.courtservice.xhibit.client.listdistribution;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.DocumentDistributionBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.listeners.ComboBoxListener;
import uk.gov.courtservice.xhibit.client.util.listeners.RadioButtonListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: DeliveryMethodPanel
 * </p>
 * <p>
 * Description: Panel containing the list specific options for a list recipient.
 * i.e. Option to choose preferred contact method or to specify a list specific
 * contact method. Form part of the ManageListRecipientPanel.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: DeliveryMethodPanel.java,v 1.12 2014/06/20 18:34:44 atwells Exp $
 */
public class DeliveryMethodPanel extends XPanel {
    // components
    private JLabel requestLetterLabel;

    private JLabel subscriptionLabel;

    private ButtonGroup methodChoiceGroup = new ButtonGroup();

    private JLabel contactMethodLabel;

    private JComboBox contactMethodComboBox;

    private JLabel documentFormatLabel;

    private JComboBox documentFormatComboBox;

    private JRadioButton useThisRadio;

    private JRadioButton usePreferredRadio;

    private TitledBorder deliveryBorder;

    private final ManageListRecipientModel model;

    /**
     * Sets the model and resources for this panel. Calls the
     * <code>stepInitialise()</code> method to load the data required for this
     * panel, calls <code>jbInit()</code> to build the panel components and
     * finally <code>setActivate()</code> to perform any actions necessary
     * before displaying the panel.
     * 
     * @param model
     *            Class to store the data required to build this panel.
     * @throws CSRecoverableException
     *             If there is a problem loading the data.
     */
    public DeliveryMethodPanel(ManageListRecipientModel model) throws CSRecoverableException {
        this.model = model;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    /**
     * Set up the panel components
     */
    private void jbInit() {
        this.setLayout(new GridBagLayout());

        // border and title
        deliveryBorder = new TitledBorder(XHIBITConstant.getResource(XhibitBundles.ManageLists,
                "deliveryMethodBorderTitle"));
        this.setBorder(deliveryBorder);

        // List identification label
        this.add(getRequestLetterLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        // List identification
        this.add(getSubscriptionLabel(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        // 'Use Preferred' radio button
        this.add(getPreferredRadio(), new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        // 'Use This' radio button
        this.add(getUseThisRadio(), new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        // Set initial radio button selection
        getPreferredRadio().setSelected(model.isUsePrefDistType());
        getUseThisRadio().setSelected(!model.isUsePrefDistType());

        // Contact method label
        this.add(getContactMethodLabel(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        // Contact method drop-down selection
        this.add(getContactMethodComboBox(), new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

        // Document format label
        this.add(getDocumentFormatLabel(), new GridBagConstraints(0, 5, 1, 2, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        // Document format drop-down selection
        this.add(getDocumentFormatComboBox(), new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    private JLabel getRequestLetterLabel() {
        if (requestLetterLabel == null) {
            requestLetterLabel = new JLabel();
            requestLetterLabel.setText(XHIBITConstant.getResource(XhibitBundles.ManageLists, "requestListLabel"));
        }
        return requestLetterLabel;
    }

    private JLabel getSubscriptionLabel() {
        if (subscriptionLabel == null) {
            subscriptionLabel = new JLabel();
            subscriptionLabel.setText(model.getListType());
        }
        return subscriptionLabel;
    }

    private JLabel getContactMethodLabel() {
        if (contactMethodLabel == null) {
            contactMethodLabel = new JLabel();
            contactMethodLabel.setText(XHIBITConstant.getResource(XhibitBundles.ManageLists, "contactMethodLabel"));
        }
        return contactMethodLabel;
    }

    private JLabel getDocumentFormatLabel() {
        if (documentFormatLabel == null) {
            documentFormatLabel = new JLabel();
            documentFormatLabel.setText(XHIBITConstant.getResource(XhibitBundles.ManageLists, "documentFormatLabel"));
        }
        return documentFormatLabel;
    }

    private JComboBox getContactMethodComboBox() {
        if (contactMethodComboBox == null) {
            Vector contactMtdVector = ListDistributionUtilities.getComboBoxContents("ldcm_");
            DefaultComboBoxModel dcbm = new DefaultComboBoxModel(contactMtdVector);
            contactMethodComboBox = new JComboBox(dcbm);
            if (model.getDistributionType() != null) {
                ListDistributionUtilities.setSelectedItemByCode(contactMethodComboBox, model.getDistributionType());
            }

            // disable to begin if use preferred is selected
            if (model.isUsePrefDistType())
                contactMethodComboBox.disable();

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

            // disable to begin if use preferred is selected
            if (model.isUsePrefDistType())
                documentFormatComboBox.disable();
        }
        return documentFormatComboBox;
    }

    private JRadioButton getUseThisRadio() {
        if (useThisRadio == null) {
            useThisRadio = new JRadioButton();
            useThisRadio.setText(XHIBITConstant.getResource(XhibitBundles.ManageLists, "useOtherContact"));
            useThisRadio.addActionListener(new RadioButtonListener(this));
            methodChoiceGroup.add(useThisRadio);
        }
        return useThisRadio;
    }

    private JRadioButton getPreferredRadio() {
        if (usePreferredRadio == null) {
            usePreferredRadio = new JRadioButton();
            usePreferredRadio.setText(XHIBITConstant.getResource(XhibitBundles.ManageLists, "usePrefContact"));
            usePreferredRadio.addActionListener(new RadioButtonListener(this));
            methodChoiceGroup.add(usePreferredRadio);
        }
        return usePreferredRadio;
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
        ManageListRecipientPanel parentPanel = null;
        // if we are within a ManageListRecipientPanel and the list specific
        // option
        // is selected, ensure that a valid value for the contact type is
        // supplied
        // i.e. valid email or fax number in parent panel
        if (getParent() instanceof ManageListRecipientPanel && !model.isUsePrefDistType()) {
            parentPanel = (ManageListRecipientPanel) getParent();

            // if email selected ensure an email address has been entered
            if ("Email".equalsIgnoreCase(contactMethodComboBox.getSelectedItem().toString())
                    && (parentPanel.getEmailAddressText().getText().trim().length() == 0)) {
                parentPanel.getEmailAddressText().requestFocus();
                throw new CSValidationException("validation.shortlength", new String[] {
                        XHIBITConstant.getResource(XhibitBundles.ManageLists, "emailAddressLabel"), "0" },
                        "Email address not populated");
            }
            // if fax selected ensure a fax number has been entered
            else if ("Fax".equalsIgnoreCase(contactMethodComboBox.getSelectedItem().toString())
                    && (parentPanel.getFaxNumberText().getText().trim().length() == 0)) {
                parentPanel.getFaxNumberText().requestFocus();
                throw new CSValidationException("validation.shortlength", new String[] {
                        XHIBITConstant.getResource(XhibitBundles.ManageLists, "faxNumberLabel"), "0" },
                        "Fax number not populated");
            }
        }
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
            // set the list specific contact method
            model.setDistributionType(((ComboContentsValue) contactMethodComboBox.getSelectedItem()).getCode());
            model.setMimeType(((ComboContentsValue) documentFormatComboBox.getSelectedItem()).getCode());

            // determine if we using preferred or list specific contact
            // methods
            JRadioButton selectedRB = getSelectedRadio(methodChoiceGroup);
            if (selectedRB != null && selectedRB == getUseThisRadio()) {
                model.setUsePrefDistType(false);
            } else {
                model.setUsePrefDistType(true);
            }

            // set the distribution on the recipient complex value
            // the midtier service updates and doc distributions passed but
            // does
            // not remove any in the update, therefore no problem to
            // overwrite
            // any existing doc distributions here
            Collection temp = new Vector();
            temp.add(model.getDocDistribution());
            model.getComplexRecipient().setDocumentDistribution(temp);
        }
    }

    /**
     * Disable the Contact Method and Document Format drop-downs if the 'Use
     * Preferred Method' radio button is selected. Enable the drop-downs if the
     * 'Use This Method' radio button is selected.
     */
    public void stepUpdateViewState() {
        JRadioButton selectedRB = getSelectedRadio(methodChoiceGroup);
        if (selectedRB != null && selectedRB == getUseThisRadio()) {
            contactMethodComboBox.setEnabled(true);

            // if FAX has been selected default the format to PDF and
            // disable
            if (((ComboContentsValue) contactMethodComboBox.getSelectedItem()).getCode().equals("FAX")) {
                ListDistributionUtilities.setSelectedItemByCode(documentFormatComboBox, "PDF");
                documentFormatComboBox.setEnabled(false);
            } else {
                documentFormatComboBox.setEnabled(true);
            }
            // need to set this here as the stepValidate() is called before
            // the
            // setDeinitialise() and the stepValidate() makes use of this
            // information
            // from the model
            model.setUsePrefDistType(false);
        } else {
            contactMethodComboBox.setEnabled(false);
            documentFormatComboBox.setEnabled(false);
            // need to set this here as the stepValidate() is called before
            // the
            // setDeinitialise() and the stepValidate() makes use of this
            // information
            // from the model
            model.setUsePrefDistType(true);
        }
    }

    private static JRadioButton getSelectedRadio(ButtonGroup bGroup) {
        if (bGroup.isSelected(bGroup.getSelection())) {
            JRadioButton selectedRB = null;
            Enumeration enumeration = bGroup.getElements();
            while (enumeration.hasMoreElements()) {
                JRadioButton item = (JRadioButton) enumeration.nextElement();
                if (item.isSelected()) {
                    selectedRB = item;
                    break;
                }
            }
            return selectedRB;
        }
        return null;
    }

    /**
     * Calls the <code>stepUpdateViewState()</code> method.
     */
    public void stepActivate() {
        stepUpdateViewState();
    }

    /**
     * Load the data we need, if not already loaded by parent panel.
     * 
     * @throws CSRecoverableException
     *             if there is a problem finding the recipient data
     */
    public void stepInitialise() throws CSRecoverableException {
        // load the data we need
        if (model.getDistributionType() != null || model.getRecipientId() == null) {
            // do nothing if the data has already been loaded, or
            // if this is a new recipient and there is no data to load
        } else {
            Vector docD = new Vector();

            model.setComplexRecipient(XhibitDelegateHelper.getMaintainRecipientDelegate().findRecipient(
                    model.getRecipientId(), model.getDocumentType(),
                    XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME)));

            // just set the details relevant to the Delivery Method
            docD.addAll(model.getComplexRecipient().getDocumentDistribution());
            if (docD.size() > 0)
                // there should only be one distribution as we have retrieved
                // the data
                // by recipient Id AND document type
                model.setDocDistribution((DocumentDistributionBasicValue) docD.firstElement());

        }
    }
}