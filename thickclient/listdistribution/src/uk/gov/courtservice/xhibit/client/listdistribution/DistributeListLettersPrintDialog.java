package uk.gov.courtservice.xhibit.client.listdistribution;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.WllControlComplexValue;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: DistributeListLettersPrintDialog
 * </p>
 * <p>
 * Description: Builds the screen for selecting print options.
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
 * @version $Id: DistributeListLettersPrintDialog.java,v 1.4 2005/02/22 09:25:33
 *          bzjrnl Exp $
 */
public class DistributeListLettersPrintDialog extends XDialog {

    private final BodyPanel bodyPanel;

    public DistributeListLettersPrintDialog(XhibitApplicationController xac, WllControlComplexValue value) {
        super(xac, getResource("distributelistletterprintdialog.title"), true, OKCANCEL, DEFAULTOK);

        bodyPanel = new BodyPanel(value);

        addBodyPanel(bodyPanel);
        pack();
    }

    // Query

    public boolean includePost() {
        return bodyPanel.includePost();
    }

    public boolean includeFax() {
        return bodyPanel.includeFax();
    }

    public boolean includeEmail() {
        return bodyPanel.includeEmail();
    }

    public boolean asLetters() {
        return bodyPanel.asLetters();
    }

    public boolean asRingOut() {
        return bodyPanel.asRingOut();
    }

    // Components

    private class BodyPanel extends XPanel {
        private final JCheckBox postCheckBox;

        private final JCheckBox faxCheckBox;

        private final JCheckBox emailCheckBox;

        private final JRadioButton lettersRadioButton;

        private final JRadioButton callOutRadioButton;

        public BodyPanel(WllControlComplexValue value) {
            super(new GridBagLayout());
            setBorder(BorderFactory.createEtchedBorder());

            if (value == null) {
                throw new IllegalArgumentException("value: null");
            }

            CheckBoxActionListener actionListener = new CheckBoxActionListener();

            postCheckBox = new JCheckBox(getResource("distributelistletterprintdialog.checkbox.includepost"), true);
            if (value.isPrintingRequired()) {
                postCheckBox.setEnabled(false);
            }
            postCheckBox.addActionListener(actionListener);
            add(postCheckBox, createPostCheckBoxConstraints());

            emailCheckBox = new JCheckBox(getResource("distributelistletterprintdialog.checkbox.includeemail"), false);
            emailCheckBox.addActionListener(actionListener);
            add(emailCheckBox, createEmailCheckBoxConstraints());

            faxCheckBox = new JCheckBox(getResource("distributelistletterprintdialog.checkbox.includefax"), false);
            faxCheckBox.addActionListener(actionListener);
            add(faxCheckBox, createFaxCheckBoxConstraints());

            if (value.isDailyList()) {
                callOutRadioButton = new JRadioButton(getResource("distributelistletterprintdialog.radio.ringout"),
                        true);

                lettersRadioButton = new JRadioButton(getResource("distributelistletterprintdialog.radio.letters"),
                        true);

                ButtonGroup group = new ButtonGroup();
                group.add(callOutRadioButton);
                group.add(lettersRadioButton);

                add(callOutRadioButton, createCallOutRadioButtonConstraints());
                add(lettersRadioButton, createLettersRadioButtonConstraints());
            } else {
                lettersRadioButton = null;
                callOutRadioButton = null;
            }
        }

        // Get state

        public boolean includePost() {
            return postCheckBox.isSelected();
        }

        public boolean includeFax() {
            return faxCheckBox.isSelected();
        }

        public boolean includeEmail() {
            return emailCheckBox.isSelected();
        }

        public boolean asLetters() {
            // Return true by default
            return lettersRadioButton == null ? true : lettersRadioButton.isSelected();
        }

        public boolean asRingOut() {
            // Return false by default
            return callOutRadioButton == null ? false : callOutRadioButton.isSelected();
        }

        /**
         * XPanel Implemenation
         * 
         * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepInitialise()
         *      XPanel
         */
        public void stepInitialise() throws CSRecoverableException {
            // Framework Implementaion
        }

        /**
         * XPanel Implemenation
         * 
         * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepActivate()
         *      XPanel
         */
        public void stepActivate() throws CSRecoverableException {
            stepUpdateViewState();
        }

        /**
         * XPanel Implemenation
         * 
         * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepUpdateViewState()
         *      XPanel
         */
        public void stepUpdateViewState() throws CSRecoverableException {
            // Framework Implementaion
        }

        /**
         * XPanel Implemenation
         * 
         * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepValidate()
         *      XPanel
         */
        public void stepValidate() throws CSValidationException, CSRecoverableException {
            // Framework Implementaion
        }

        /**
         * XPanel Implemenation
         * 
         * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepDeactivate()
         *      XPanel
         */
        public void stepDeactivate() throws CSRecoverableException {
            // Framework Implementaion
        }

        /**
         * XPanel Implemenation
         * 
         * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepDeinitialise()
         *      XPanel
         */
        public void stepDeinitialise(boolean update) throws CSRecoverableException {
            // Framework Implementaion
        }
    }

    // Events

    private class CheckBoxActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            buttonPanel.okButton.setEnabled(includePost() || includeFax() || includeEmail());
        }
    }

    // Environment Utilities

    /**
     * Get the specifed resource from the list distribution resources
     */
    private static String getResource(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.ListDistribution, key);
    }

    // Constraints

    /**
     * Get the constraints for the post check box
     */
    private static GridBagConstraints createPostCheckBoxConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(4, 4, 2, 4);
        return constraints;
    }

    /**
     * Get the constraints for the email check box
     */
    private static GridBagConstraints createEmailCheckBoxConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(2, 4, 2, 4);
        return constraints;
    }

    /**
     * Get the constraints for the fax check box
     */
    private static GridBagConstraints createFaxCheckBoxConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(2, 4, 2, 4);
        return constraints;
    }

    /**
     * Get the constraints for the call out radio button constraints
     */
    private static GridBagConstraints createLettersRadioButtonConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(2, 2, 4, 4);
        return constraints;
    }

    /**
     * Get the constraints for the letters radio button constraints
     */
    private static GridBagConstraints createCallOutRadioButtonConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(2, 4, 4, 2);
        return constraints;
    }

}
