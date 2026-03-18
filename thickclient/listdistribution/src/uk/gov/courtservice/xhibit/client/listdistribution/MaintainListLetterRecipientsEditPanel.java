package uk.gov.courtservice.xhibit.client.listdistribution;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.WllRecipientComplexValue;
import uk.gov.courtservice.xhibit.client.util.text.LineLengthValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: EditMaintainListLetterRecipientsDialog
 * </p>
 * <p>
 * Description: Screen for setting the distribution options for list letter
 * recipients.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * <p>
 * Author: Will Fardell, Xdevelopment (2004)
 * </p>
 * 
 * @version $Id: MaintainListLetterRecipientsEditPanel.java,v 1.4 2005/02/24
 *          11:08:24 bzjrnl Exp $
 */

public class MaintainListLetterRecipientsEditPanel extends AbstractListDistributionPanel {
    private final WllRecipientComplexValue value;

    private final JTextField typeTextField;

    private final JTextField nameTextField;

    private final JTextField addressTextField;

    private final JTextField emailTextField;

    private final JTextField faxTextField;

    private final JRadioButton postRadioButton;

    private final JRadioButton faxRadioButton;

    private final JRadioButton emailRadioButton;

    private final JRadioButton pdfRadioButton;

    private final JRadioButton htmlRadioButton;

    public MaintainListLetterRecipientsEditPanel(XhibitApplicationController xac, WllRecipientComplexValue value)
            throws CSRecoverableException {
        super(xac);

        if (value == null) {
            throw new IllegalArgumentException("value: " + null);
        }
        this.value = value;

        setBorder(BorderFactory.createEtchedBorder());

        // Header
        add(createLabel("maintainlistletterrecipientseditpanel.label.type"), createTypeLabelConstraints());
        typeTextField = createLabelField();
        add(typeTextField, createTypeTextFieldConstraints());

        add(createLabel("maintainlistletterrecipientseditpanel.label.name"), createNameLabelConstraints());
        nameTextField = createLabelField();
        add(nameTextField, createNameTextFieldConstraints());

        add(createLabel("maintainlistletterrecipientseditpanel.label.address"), createAddressLabelConstraints());
        addressTextField = createLabelField();
        add(addressTextField, createAddressTextFieldConstraints());

        add(createLabel("maintainlistletterrecipientseditpanel.label.email"), createEmailLabelConstraints());
        emailTextField = createLabelField();
        add(emailTextField, createEmailTextFieldConstraints());

        // Fax
        TextFieldDocumentListener documentListener = new TextFieldDocumentListener();

        add(createLabel("maintainlistletterrecipientseditpanel.label.fax"), createFaxLabelConstraints());
        faxTextField = createTextField(14, documentListener);
        add(faxTextField, createFaxTextFieldConstraints());

        RadioButtonActionListener actionListener = new RadioButtonActionListener();

        // Distribution
        ButtonGroup distributionGroup = new ButtonGroup();
        add(createLabel("maintainlistletterrecipientseditpanel.label.distribution"),
                createDistributionLabelConstraints());

        postRadioButton = createRadioButton("maintainlistletterrecipientseditpanel.radio.post", actionListener,
                distributionGroup);
        add(postRadioButton, createPostRadioButtonConstraints());

        faxRadioButton = createRadioButton("maintainlistletterrecipientseditpanel.radio.fax", actionListener,
                distributionGroup);
        add(faxRadioButton, createFaxRadioButtonConstraints());

        emailRadioButton = createRadioButton("maintainlistletterrecipientseditpanel.radio.email", actionListener,
                distributionGroup);
        add(emailRadioButton, createEmailRadioButtonConstraints());

        // Mime
        ButtonGroup mimeGroup = new ButtonGroup();
        add(createLabel("maintainlistletterrecipientseditpanel.label.mime"), createMimeLabelConstraints());

        pdfRadioButton = createRadioButton("maintainlistletterrecipientseditpanel.radio.pdf", actionListener, mimeGroup);
        add(pdfRadioButton, createPdfRadioButtonConstraints());

        htmlRadioButton = createRadioButton("maintainlistletterrecipientseditpanel.radio.html", actionListener,
                mimeGroup);
        add(htmlRadioButton, createHtmlRadioButtonConstraints());

        stepInitialise();
    }

    private JTextField createLabelField() {
        JTextField textField = new JTextField();
        textField.setOpaque(false);
        textField.setEditable(false);
        return textField;
    }

    private JTextField createTextField(int length, DocumentListener documentListener) {
        JTextField textField = new JTextField(new LineLengthValidatingDocumentDecorator(length), "", length);
        textField.getDocument().addDocumentListener(documentListener);
        return textField;
    }

    private JLabel createLabel(String key) {
        return new JLabel(getResource(key));
    }

    private JRadioButton createRadioButton(String key, ActionListener listener, ButtonGroup group) {
        JRadioButton radioButton = new JRadioButton(getResource(key));
        radioButton.addActionListener(listener);
        group.add(radioButton);
        return radioButton;
    }

    /**
     * XPanel Implemenation
     * 
     * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepInitialise()
     *      XPanel
     */
    public void stepInitialise() throws CSRecoverableException {
        setText(typeTextField, getFormatedType());
        setText(nameTextField, value.getName());
        setText(addressTextField, value.getAddress());
        setText(emailTextField, value.getEmail());
        setText(faxTextField, value.getFax());

        if (value.isDistributionEmail()) {
            emailRadioButton.setSelected(true);
        } else if (value.isDistributionFax()) {
            faxRadioButton.setSelected(true);
        } else // if(value.isDistributionPost())
        {
            postRadioButton.setSelected(true);
        }

        if (value.isMimeHtml()) {
            htmlRadioButton.setSelected(true);
        } else // if(value.isMimePdf())
        {
            pdfRadioButton.setSelected(true);
        }
    }

    private void setText(JTextField field, String text) {
        field.setText(text);
        field.setCaretPosition(0);
    }

    /**
     * XPanel Implemenation
     * 
     * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepUpdateViewState()
     *      XPanel
     */
    public void stepUpdateViewState() {
        // Can only be email if we have an email addresss
        if (emailTextField.getText().length() == 0) {
            if (emailRadioButton.isSelected()) {
                postRadioButton.setSelected(true);
            }
            emailRadioButton.setEnabled(false);
        } else {
            emailRadioButton.setEnabled(true);
        }

        // Can only be fax if we have a fax number
        if (faxTextField.getText().length() == 0) {
            if (faxRadioButton.isSelected()) {
                postRadioButton.setSelected(true);
            }
            faxRadioButton.setEnabled(false);
        } else {
            faxRadioButton.setEnabled(true);
        }

        // Can only select mime type if deliverd by email
        if (emailRadioButton.isSelected()) {
            htmlRadioButton.setEnabled(true);
        } else if (faxRadioButton.isSelected()) {
            pdfRadioButton.setSelected(true);
            htmlRadioButton.setEnabled(false);
        } else // if (postRadioButton.isSelected())
        {
            pdfRadioButton.setSelected(true);
            htmlRadioButton.setEnabled(false);
        }
    }

    /**
     * XPanel Implementaion:
     * 
     * @throws CSRecoverableException
     *             if an error occures
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
            value.setFax(faxTextField.getText());

            if (emailRadioButton.isSelected()) {
                value.setDistributionEmail();
            } else if (faxRadioButton.isSelected()) {
                value.setDistributionFax();
            } else // if (postRadioButton.isSelected())
            {
                value.setDistributionPost();
            }

            if (htmlRadioButton.isSelected()) {
                value.setMimeHtml();
            } else // if (pdfRadioButton.isSelected())
            {
                value.setMimePdf();
            }
        }
    }

    public WllRecipientComplexValue getValue() {
        return value;
    }

    private String getFormatedType() {
        if (value.isOpposer()) {
            return getResource("maintainlistletterrecipientseditpanel.recipienttype.prosecution");
        } else // if (value.isSolicitor())
        {
            return getResource("maintainlistletterrecipientseditpanel.recipienttype.solicitor");
        }
    }

    // Events

    public class RadioButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            stepUpdateViewState();
        }
    }

    public class TextFieldDocumentListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
            stepUpdateViewState();
        }

        public void removeUpdate(DocumentEvent e) {
            stepUpdateViewState();
        }

        public void changedUpdate(DocumentEvent e) {
            stepUpdateViewState();
        }
    }

    // Constraints

    private static GridBagConstraints createTypeLabelConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(4, 4, 2, 2);
        return constraints;
    }

    private static GridBagConstraints createTypeTextFieldConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(4, 2, 2, 4);
        return constraints;
    }

    private static GridBagConstraints createNameLabelConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(2, 4, 2, 2);
        return constraints;
    }

    private static GridBagConstraints createNameTextFieldConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 2, 2, 4);
        return constraints;
    }

    private static GridBagConstraints createAddressLabelConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(2, 4, 2, 2);
        return constraints;
    }

    private static GridBagConstraints createAddressTextFieldConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 2, 2, 4);
        return constraints;
    }

    private static GridBagConstraints createEmailLabelConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(2, 4, 2, 2);
        return constraints;
    }

    private static GridBagConstraints createEmailTextFieldConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 3;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 2, 2, 4);
        return constraints;
    }

    private static GridBagConstraints createFaxLabelConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(2, 4, 2, 2);
        return constraints;
    }

    private static GridBagConstraints createFaxTextFieldConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 2, 2, 4);
        return constraints;
    }

    private static GridBagConstraints createDistributionLabelConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(2, 4, 2, 2);
        return constraints;
    }

    private static GridBagConstraints createPostRadioButtonConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(2, 2, 2, 2);
        return constraints;
    }

    private static GridBagConstraints createFaxRadioButtonConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 5;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(2, 2, 2, 2);
        return constraints;
    }

    private static GridBagConstraints createEmailRadioButtonConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 3;
        constraints.gridy = 5;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(2, 2, 2, 4);
        return constraints;
    }

    private static GridBagConstraints createMimeLabelConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(2, 4, 4, 2);
        return constraints;
    }

    private static GridBagConstraints createPdfRadioButtonConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 6;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(2, 2, 4, 2);
        return constraints;
    }

    private static GridBagConstraints createHtmlRadioButtonConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 6;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(2, 2, 4, 4);
        return constraints;
    }
}
