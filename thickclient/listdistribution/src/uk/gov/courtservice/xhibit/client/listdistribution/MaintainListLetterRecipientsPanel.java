package uk.gov.courtservice.xhibit.client.listdistribution;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.WllRecipientComplexValue;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.sortable.XSortableTableModel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * <p>
 * Title: maintainlistletterrecipientspanel
 * </p>
 * <p>
 * Description: Builds the screen for distributing list letters.
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
 * @version $Id: MaintainListLetterRecipientsPanel.java,v 1.5 2006/05/10
 *          08:01:41 bzjrnl Exp $
 */
public class MaintainListLetterRecipientsPanel extends AbstractListDistributionPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// The components!
    private final TableTabbedPane tableTabbedPane;

    private final ButtonPanel buttonPanel;

    /**
     * Create a panel for maintaining the list recipients.
     * 
     * @param xac
     *            The Application Controller
     * @throws CSRecoverableException
     *             if an error occures creating the panel
     */
    public MaintainListLetterRecipientsPanel(XhibitApplicationController xac) throws CSRecoverableException {
        super(xac);

        tableTabbedPane = new TableTabbedPane();
        buttonPanel = new ButtonPanel();

        stepInitialise();
    }

    // Framework

    /**
     * XPanel Implementaion: Called by the constructor to initialise the panel
     * 
     * @throws CSRecoverableException
     *             if an error occures
     */
    public void stepInitialise() throws CSRecoverableException {
        add(tableTabbedPane, createTableTabbedPaneConstraints());
        add(buttonPanel, createButtonPanelConstraints());
    }

    /**
     * XPanel Implementaion: Called by listeners when the view needs refreshing
     * from the model. Also called as last step in step activate.
     * 
     * @throws CSRecoverableException
     *             if an error occures
     */
    public void stepUpdateViewState() {
        WllRecipientComplexValue[] values = getSelectedRecipients(tableTabbedPane.getSelectedTable());
        buttonPanel.stepUpdateViewState(canSet(values), canClear(values), canEdit(values));
    }

    // Event Callbacks

    private void setSelectedRecipients() throws CSRecoverableException {
        WllRecipientComplexValue[] values = getSelectedRecipients(tableTabbedPane.getSelectedTable());
        if (values != null && values.length > 0) {
            // Set values discarding changes if cancel pressed (note deep
            // copy of value)
            for (int i = 0; i < values.length; i++) {
                MaintainListLetterRecipientsSetDialog dialog = new MaintainListLetterRecipientsSetDialog(xac, values[i]
                        .copy());
                dialog.setVisible(true);
                if (!dialog.isOkClicked()) {
                    return; // Cancel Set!
                }

                values[i] = dialog.getValue();
            }

            // Update DB
            values = setDeliveryMethod(values);

            // Move values to specified table!
            removeRecipients(tableTabbedPane.getDefaultTable(), values);
            addRecipients(tableTabbedPane.getSpecifiedTable(), values);
        }
    }

    private void clearSelectedRecipients() throws CSRecoverableException {
        WllRecipientComplexValue[] values = getSelectedRecipients(tableTabbedPane.getSelectedTable());
        if (values != null && values.length > 0) {
            // Confirm Clear
            if (showConfirmDialog("maintainlistletterrecipientscleardialog.title",
                    "maintainlistletterrecipientscleardialog.messageheader", values)) {
                // Update DB
                values = clearDeliveryMethod(values);

                // Move values to default table!
                removeRecipients(tableTabbedPane.getSpecifiedTable(), values);
                addRecipients(tableTabbedPane.getDefaultTable(), values);
            }
        }
    }

    private boolean showConfirmDialog(String titleKey, String messageKey, WllRecipientComplexValue[] values)
            throws CSRecoverableException {
        String[] list = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            list[i] = values[i].getName();
        }

        ConfirmListDialog dialog = new ConfirmListDialog(xac, titleKey, messageKey, list);
        dialog.setVisible(true);
        return dialog.isOkClicked();
    }

    private void editSelectedRecipients() throws CSRecoverableException {
        WllRecipientComplexValue[] values = getSelectedRecipients(tableTabbedPane.getSelectedTable());
        if (values != null && values.length > 0) {
            // Set values discarding changes if cancel pressed (note deep
            // copy of value)
            for (int i = 0; i < values.length; i++) {
                MaintainListLetterRecipientsEditDialog dialog = new MaintainListLetterRecipientsEditDialog(xac,
                        values[i].copy());
                dialog.setVisible(true);
                if (!dialog.isOkClicked()) {
                    return; // Cancel Set!
                }

                values[i] = dialog.getValue();
            }

            // Update DB
            values = setDeliveryMethod(values);

            // Move values to specified table!
            updateRecipients(tableTabbedPane.getSelectedTable(), values);
        }
    }

    // Components

    private class TableTabbedPane extends JTabbedPane {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private final SpecifiedMaintainListLetterRecipientsTableModel specifiedRecipientTableModel;

        private final XTable specifiedRecipientTable;

        private final DefaultMaintainListLetterRecipientsTableModel defaultRecipientTableModel;

        private final XTable defaultRecipientTable;

        public TableTabbedPane() {
            // Create the models and tables
            specifiedRecipientTableModel = new SpecifiedMaintainListLetterRecipientsTableModel(getSpecifiedRecipients());
            specifiedRecipientTable = createTable(specifiedRecipientTableModel);

            defaultRecipientTableModel = new DefaultMaintainListLetterRecipientsTableModel(getDefaultRecipients());
            defaultRecipientTable = createTable(defaultRecipientTableModel);

            // Initialse the layout and recipienteners
            stepInitialise();
        }

        public void stepInitialise() {
            // Add the tabs
            add(getResource("maintainlistletterrecipientspanel.tabname.specifiedrecipients"), new JScrollPane(
                    specifiedRecipientTable));
            add(getResource("maintainlistletterrecipientspanel.tabname.defaultrecipients"), new JScrollPane(
                    defaultRecipientTable));

            // Add the tab change recipientener
            addChangeListener(new TabbedPaneChangeListener());
        }

        /**
         * Get the default recipient table
         */
        public XTable getDefaultTable() {
            return defaultRecipientTable;
        }

        /**
         * Get the specified recipient table
         */
        public XTable getSpecifiedTable() {
            return specifiedRecipientTable;
        }

        /**
         * Get all the selected values from the current tab
         */
        public XTable getSelectedTable() {
            JScrollPane scrollPane = (JScrollPane) getSelectedComponent();
            if (scrollPane != null) {
                return (XTable) scrollPane.getViewport().getView();
            } else {
                return null;
            }

        }

        private XTable createTable(TableModel tableModel) {
            XTable table = XTableFactory.getInstance().createDefaultTable(tableModel);
            table.makeSortable(true);
            table.getSelectionModel().addListSelectionListener(new TableListSelectionListener());
            return table;
        }
    }

    private class ButtonPanel extends JPanel {
        private final JButton setButton;

        private final JButton clearButton;

        private final JButton editButton;

        public ButtonPanel() {
            super(new GridLayout(1, 5, 4, 4));

            setButton = new JButton(new SetMaintainListLetterRecipientsAction());
            add(setButton);
            clearButton = new JButton(new ClearMaintainListLetterRecipientsAction());
            add(clearButton);
            editButton = new JButton(new EditMaintainListLetterRecipientsAction());
            add(editButton);
        }

        /**
         * Enable Disable the buttons as required
         */
        public void stepUpdateViewState(boolean setEnabled, boolean clearEnabled, boolean editEnabled) {
            setButton.setEnabled(setEnabled);
            clearButton.setEnabled(clearEnabled);
            editButton.setEnabled(editEnabled);
        }
    }

    // Events

    private class TableListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            stepUpdateViewState();
        }
    }

    private class TabbedPaneChangeListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            stepUpdateViewState();
        }
    }

    // Actions

    private class SetMaintainListLetterRecipientsAction extends XAction {
        public SetMaintainListLetterRecipientsAction() {
            super("SetMaintainListLetterRecipients");
        }

        public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
            setSelectedRecipients();
        }
    }

    private class ClearMaintainListLetterRecipientsAction extends XAction {
        public ClearMaintainListLetterRecipientsAction() {
            super("ClearMaintainListLetterRecipients");
        }

        public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
            clearSelectedRecipients();
        }
    }

    private class EditMaintainListLetterRecipientsAction extends XAction {
        public EditMaintainListLetterRecipientsAction() {
            super("EditMaintainListLetterRecipients");
        }

        public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
            editSelectedRecipients();
        }
    }

    // Utilites

    /**
     * Return true if there are values and they dont have delivery methods set
     */
    private static boolean canSet(WllRecipientComplexValue[] values) {
        if (values == null || values.length == 0) {
            return false;
        }

        for (int i = 0; i < values.length; i++) {
            if (!values[i].isDeliveryMethodSpecified()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Return true if there are values and they are have delivery methods set
     */
    private static boolean canClear(WllRecipientComplexValue[] values) {
        return canEdit(values);
    }

    /**
     * Return true if there are values and they are have delivery methods set
     */
    private static boolean canEdit(WllRecipientComplexValue[] values) {
        if (values == null || values.length == 0) {
            return false;
        }

        for (int i = 0; i < values.length; i++) {
            if (values[i].isDeliveryMethodSpecified()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Notify the table that the values have changed
     */
    private static void addRecipients(XTable table, WllRecipientComplexValue[] values) {
        if (table != null) {
            WllRecipientComplexValue[] selectedRecipients = getSelectedRecipients(table);

            // Iterate through the model retaining unchanged values and
            // replacing
            // modifyed values. Once the new data is constructed update the
            // model
            XSortableTableModel model = (XSortableTableModel) table.getModel();
            List retainValueList = new ArrayList();
            for (int i = 0, c = model.getRowCount(); i < c; i++) {
                retainValueList.add(model.getDataAt(i));
            }
            for (int i = 0; i < values.length; i++) {
                retainValueList.add(values[i]);
            }
            model.setData(retainValueList.toArray(new WllRecipientComplexValue[retainValueList.size()]));

            setSelectedRecipients(table, selectedRecipients);
        }
    }

    /**
     * Notify the table that the values have changed
     */
    private static void updateRecipients(XTable table, WllRecipientComplexValue[] values) {
        if (table != null) {
            WllRecipientComplexValue[] selectedRecipients = getSelectedRecipients(table);

            // Iterate through the model retaining unchanged values and
            // replacing
            // modifyed values. Once the new data is constructed update the
            // model
            XSortableTableModel model = (XSortableTableModel) table.getModel();
            List retainValueList = new ArrayList();
            for (int i = 0, c = model.getRowCount(); i < c; i++) {
                WllRecipientComplexValue value = (WllRecipientComplexValue) model.getDataAt(i);
                int index = indexOf(values, value);
                if (index == -1) {
                    retainValueList.add(value);
                } else {
                    retainValueList.add(values[index]);
                }
            }
            model.setData(retainValueList.toArray(new WllRecipientComplexValue[retainValueList.size()]));

            setSelectedRecipients(table, selectedRecipients);
        }
    }

    /**
     * Remove the values from the table
     */
    private static void removeRecipients(XTable table, WllRecipientComplexValue[] values) {
        if (table != null) {
            // Iterate through the model retaining values that have not been
            // removed
            // Once the new data is constructed update the model
            XSortableTableModel model = (XSortableTableModel) table.getModel();
            List retainValueList = new ArrayList();
            for (int i = 0, c = model.getRowCount(); i < c; i++) {
                WllRecipientComplexValue value = (WllRecipientComplexValue) model.getDataAt(i);
                if (indexOf(values, value) == -1) {
                    retainValueList.add(value);
                }
            }
            model.setData(retainValueList.toArray(new WllRecipientComplexValue[retainValueList.size()]));
        }
    }

    /**
     * Return true if the array contains the value
     */
    private static int indexOf(WllRecipientComplexValue[] values, WllRecipientComplexValue value) {
        Integer listId = value.getId();
        for (int i = 0; i < values.length; i++) {
            if (listId.equals(values[i].getId())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Set the selected values in the table
     */
    private static void setSelectedRecipients(XTable table, WllRecipientComplexValue[] values) {
        if (table != null) {
            XSortableTableModel model = (XSortableTableModel) table.getModel();
            for (int i = 0; i < values.length; i++) {
                Integer listId = values[i].getId();
                for (int j = 0, c = model.getRowCount(); j < c; j++) {
                    if (listId.equals(((WllRecipientComplexValue) model.getDataAt(j)).getId())) {
                        table.addRowSelectionInterval(j, j);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Get the selected values from the table
     */
    private static WllRecipientComplexValue[] getSelectedRecipients(XTable table) {
        if (table != null) {
            XSortableTableModel model = (XSortableTableModel) table.getModel();
            int[] indicies = table.getSelectedRows();
            WllRecipientComplexValue[] values = new WllRecipientComplexValue[indicies.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = (WllRecipientComplexValue) model.getDataAt(indicies[i]);
            }
            return values;
        } else {
            return new WllRecipientComplexValue[0];
        }
    }

    // Delegate Utilities

    /**
     * Get the wll recipient values for the specified court who have specified a
     * delivery method.
     */
    public WllRecipientComplexValue[] getSpecifiedRecipients() {
        return XhibitDelegateHelper.getListDistribution2Delegate().getSpecifiedRecipients(getCourtId());
    }

    /**
     * Get the wll recipient for the specified court with no delivery method
     * specified.
     * 
     */
    public WllRecipientComplexValue[] getDefaultRecipients() {
        return XhibitDelegateHelper.getListDistribution2Delegate().getDefaultRecipients(getCourtId());
    }

    /**
     * Set or update the delivery method for the specified values
     */
    public WllRecipientComplexValue[] setDeliveryMethod(WllRecipientComplexValue[] values) {
        return XhibitDelegateHelper.getListDistribution2Delegate().setDeliveryMethod(values);
    }

    /**
     * Clear the delivery method for the specified values
     */
    public WllRecipientComplexValue[] clearDeliveryMethod(WllRecipientComplexValue[] values) {
        return XhibitDelegateHelper.getListDistribution2Delegate().clearDeliveryMethod(values);
    }

    // Constraints

    /**
     * Get the constraints for the tabbed pane
     */
    private static GridBagConstraints createTableTabbedPaneConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(4, 4, 2, 4);
        return constraints;
    }

    /**
     * Get the constraints for the button panel
     */
    private static GridBagConstraints createButtonPanelConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(2, 4, 4, 4);
        return constraints;
    }
}
