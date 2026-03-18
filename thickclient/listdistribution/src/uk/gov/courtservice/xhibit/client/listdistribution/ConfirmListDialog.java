package uk.gov.courtservice.xhibit.client.listdistribution;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: ConfirmListDialog
 * </p>
 * <p>
 * Description: Dialog for confirming an operation on a (long) list of objects
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
 * @version $Id: ConfirmListDialog.java,v 1.3 2006/06/05 12:30:48 bzjrnl Exp $
 */
public class ConfirmListDialog extends XDialog {
    private final BodyPanel bodyPanel;

    public ConfirmListDialog(XhibitApplicationController xac, String titleKey, String messageKey, String[] list)
            throws CSRecoverableException {
        super(xac, getResource(titleKey), true, OKCANCEL, DEFAULTOK);

        if (list == null) {
            throw new IllegalArgumentException("list: null");
        }

        bodyPanel = new BodyPanel(xac, messageKey, list);

        addBodyPanel(bodyPanel);
        setSize(400, 300);
        centreDialog();
    }

    // Query

    // Components

    private class BodyPanel extends AbstractListDistributionPanel {
        private final JLabel iconLabel;

        private final JLabel messageLabel;

        private final JTextArea listTextArea;

        public BodyPanel(XhibitApplicationController xac, String messageKey, String[] list)
                throws CSRecoverableException {
            super(xac);

            iconLabel = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
            messageLabel = new JLabel(ConfirmListDialog.getResource(messageKey));

            StringBuffer buffer = new StringBuffer();
            if (0 < list.length) {
                buffer.append(list[0]);
                for (int i = 1; i < list.length; i++) {
                    buffer.append("\n");
                    buffer.append(list[i]);
                }
            }
            listTextArea = createLabelArea(buffer.toString());

            stepInitialise();
        }

        /**
         * XPanel Implemenation
         * 
         * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepInitialise()
         *      XPanel
         */
        public void stepInitialise() throws CSRecoverableException {
            add(iconLabel, createIconLabelConstraints());
            add(messageLabel, createMessageLabelConstraints());
            add(new JScrollPane(listTextArea), createListTextAreaConstraints());
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
    }

    //

    private static JTextArea createLabelArea(String text) {
        JTextArea area = new JTextArea(text);
        area.setCaretPosition(0);
        area.setEditable(false);
        area.setOpaque(false);
        return area;
    }

    // Events

    // Environment Utilities

    /**
     * Get the specifed resource from the list distribution resources
     */
    private static String getResource(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.ListDistribution, key);
    }

    // Constraints

    /**
     * Get the constraints for the message label
     */
    private static GridBagConstraints createIconLabelConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(4, 4, 2, 2);
        return constraints;
    }

    /**
     * Get the constraints for the message label
     */
    private static GridBagConstraints createMessageLabelConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(4, 2, 2, 4);
        return constraints;
    }

    /**
     * Get the constraints for the list text area
     */
    private static GridBagConstraints createListTextAreaConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.weighty = 1.0;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(2, 4, 4, 4);
        return constraints;
    }
}
