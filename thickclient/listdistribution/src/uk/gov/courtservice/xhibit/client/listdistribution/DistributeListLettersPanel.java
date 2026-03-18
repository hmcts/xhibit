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
import uk.gov.courtservice.xhibit.business.vos.listdistribution.WllControlComplexValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.PrintFunction;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.sortable.XSortableTableModel;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XPercentageTableCellRenderer;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * <p>
 * Title: DistributeListLettersPanel
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
 * @version $Id: DistributeListLettersPanel.java,v 1.9 2005/02/01 15:29:56
 *          bzjrnl Exp $
 */
public class DistributeListLettersPanel extends AbstractListDistributionPanel implements PrintFunction {
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
    public DistributeListLettersPanel(XhibitApplicationController xac) throws CSRecoverableException {
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
        // buttonPanel and tabbedPane may not have been initalised yet
        if (buttonPanel != null && tableTabbedPane != null) {
            // Enable the buttons based on weather all the selected
            // values can perform the operation, refresh is allways
            // enabled!
            WllControlComplexValue[] values = getSelectedLists(tableTabbedPane.getSelectedTable());
            if (values.length > 0) {
                boolean printEnabled = canPrint(values);
                buttonPanel.stepUpdateViewState(canAuthorize(values), printEnabled, printEnabled, canDelete(values),
                        true);
            } else {
                buttonPanel.stepUpdateViewState(false, false, false, false, true);
            }
        }
    }

    // Event Callbacks

    private void authorizeSelectedLists() throws CSRecoverableException {
        XTable table = tableTabbedPane.getSelectedTable();
        WllControlComplexValue[] values = getSelectedLists(table);
        if (values.length > 0) {
            if (showConfirmDialog("distributelistletterspanel.confirm.authorizetitle",
                    "distributelistletterspanel.confirm.authorizemessageheader", values)) {
                // Authorize the values, update the db and then update the view
                values = authorizeLists(values);
                updateLists(table, values);
            }
        }
    }

    private void deleteSelectedLists() throws CSRecoverableException {
        XTable table = tableTabbedPane.getSelectedTable();
        WllControlComplexValue[] values = getSelectedLists(table);
        if (values.length > 0) {
            if (showConfirmDialog("distributelistletterspanel.confirm.deletetitle",
                    "distributelistletterspanel.confirm.deletemessageheader", values)) {
                // Delete the values, update the db and then update the view
                deleteLists(values);
                removeLists(table, values);
            }
        }
    }

    public String[] print() throws CSRecoverableException {
        XTable table = tableTabbedPane.getSelectedTable();
        WllControlComplexValue[] values = getSelectedLists(table);
        String[] toPrint = printLists(values);
        updateLists(table, values);

        return toPrint;
    }

    public boolean autoSaveToFile() {
        return false;
    }

    private String[] printLists(WllControlComplexValue[] values) throws CSRecoverableException {
        if (values != null) {
            ArrayList xmlFopStrings = new ArrayList();

            for (int i = 0; i < values.length; i++) {
                DistributeListLettersPrintDialog dialog = new DistributeListLettersPrintDialog(xac, values[i]);
                dialog.setVisible(true);
                if (dialog.isOkClicked()) {
                    xmlFopStrings.add(getFormatedLettersXml(values[i], dialog.asRingOut(), dialog.includePost(), dialog
                            .includeFax(), dialog.includeEmail()));

                    if (values[i].isPrintingRequired()) {
                        values[i] = printedList(values[i]);
                    }
                }
            }

            if (xmlFopStrings.isEmpty())
                throw new UserCancelException();

            return (String[]) xmlFopStrings.toArray(new String[xmlFopStrings.size()]);
        } else {
            return new String[] {};
        }
    }

    private void refreshLists() {
        if (tableTabbedPane != null) {
            tableTabbedPane.refreshLists();
        }
    }

    private boolean showConfirmDialog(String titleKey, String messageHeaderKey, WllControlComplexValue[] values)
            throws CSRecoverableException {
        String[] list = new String[values.length];
        for (int i = 0; i < list.length; i++) {
            list[i] = values[i].getDocumentTitle();
        }

        ConfirmListDialog dialog = new ConfirmListDialog(xac, titleKey, messageHeaderKey, list);
        dialog.setVisible(true);
        return dialog.isOkClicked();
    }

    // Components

    private class TableTabbedPane extends JTabbedPane {
        private final DistributeListLettersTableModel warnedListTableModel;

        private final XTable warnedListTable;

        private final DistributeListLettersTableModel firmListTableModel;

        private final XTable firmListTable;

        private final DistributeListLettersTableModel dailyListTableModel;

        private final XTable dailyListTable;

        public TableTabbedPane() {
            // Load the values
            WllControlComplexValue[] values = getLists();

            // Create the models and tables
            warnedListTableModel = new DistributeListLettersTableModel(getLists(values,
                    WllControlComplexValue.DOCUMENT_TYPE_WARNED_LIST));
            warnedListTable = createTable(warnedListTableModel);
            firmListTableModel = new DistributeListLettersTableModel(getLists(values,
                    WllControlComplexValue.DOCUMENT_TYPE_FIRM_LIST));
            firmListTable = createTable(firmListTableModel);
            dailyListTableModel = new DistributeListLettersTableModel(getLists(values,
                    WllControlComplexValue.DOCUMENT_TYPE_DAILY_LIST));
            dailyListTable = createTable(dailyListTableModel);

            // Initialse the layout and listeners
            stepInitialise();
        }

        public void stepInitialise() {
            // Add the tabs
            add(getResource("distributelistletterspanel.tabname.warnedlist"), new JScrollPane(warnedListTable));
            add(getResource("distributelistletterspanel.tabname.firmlist"), new JScrollPane(firmListTable));
            add(getResource("distributelistletterspanel.tabname.dailylist"), new JScrollPane(dailyListTable));

            // Add the tab change listener
            addChangeListener(new TabbedPaneChangeListener());
        }

        /**
         * Refresh the tables with data from the database
         */
        public void refreshLists() {
            WllControlComplexValue[] values = getLists();

            // Get the current warned list selection, update data then
            // restore
            // selection
            WllControlComplexValue[] warnedSelectedLists = getSelectedLists(warnedListTable);
            warnedListTableModel.setData(getLists(values, WllControlComplexValue.DOCUMENT_TYPE_WARNED_LIST));
            setSelectedLists(warnedListTable, warnedSelectedLists);

            // Get the current firm list selection, update data then restore
            // selection
            WllControlComplexValue[] firmSelectedLists = getSelectedLists(firmListTable);
            firmListTableModel.setData(getLists(values, WllControlComplexValue.DOCUMENT_TYPE_FIRM_LIST));
            setSelectedLists(firmListTable, firmSelectedLists);

            // Get the current daily list selection, update data then
            // restore
            // selection
            WllControlComplexValue[] dailySelectedLists = getSelectedLists(dailyListTable);
            dailyListTableModel.setData(getLists(values, WllControlComplexValue.DOCUMENT_TYPE_DAILY_LIST));
            setSelectedLists(dailyListTable, dailySelectedLists);
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
            table.setDefaultRenderer(Double.class, new XPercentageTableCellRenderer());
            table.getSelectionModel().addListSelectionListener(new TableListSelectionListener());
            return table;
        }
    }

    private class ButtonPanel extends JPanel {
        private final JButton authorizeButton;

        private final JButton printButton;

        private final JButton previewButton;

        private final JButton deleteButton;

        private final JButton refreshButton;

        private XAction printAction;

        private XAction previewAction;

        public ButtonPanel() {
            super(new GridLayout(1, 5, 4, 4));

            authorizeButton = new JButton(new AuthorizeDistributeListLettersAction());
            add(authorizeButton);

            printAction = XhibitActions.getAction(xac, XhibitActions.Print);
            printButton = new JButton();
            printButton.setAction(printAction);
            add(printButton);

            previewAction = XhibitActions.getAction(xac, XhibitActions.PrintPreview);
            previewButton = new JButton();
            previewButton.setAction(previewAction);
            add(previewButton);

            deleteButton = new JButton(new DeleteDistributeListLettersAction());
            add(deleteButton);
            refreshButton = new JButton(new RefreshDistributeListLettersAction());
            add(refreshButton);
        }

        /**
         * Enable Disable the buttons as required
         */
        public void stepUpdateViewState(boolean authorizeEnabled, boolean printEnabled, boolean previewEnabled,
                boolean deleteEnabled, boolean refreshEnabled) {
            authorizeButton.setEnabled(authorizeEnabled);
            printAction.setEnabled(printEnabled);
            previewAction.setEnabled(previewEnabled);
            deleteButton.setEnabled(deleteEnabled);
            refreshButton.setEnabled(refreshEnabled);
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

    private class AuthorizeDistributeListLettersAction extends XAction {
        public AuthorizeDistributeListLettersAction() {
            super("AuthorizeDistributeListLetters");
        }

        public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
            authorizeSelectedLists();
        }
    }

    private class DeleteDistributeListLettersAction extends XAction {
        public DeleteDistributeListLettersAction() {
            super("DeleteDistributeListLetters");
        }

        public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
            deleteSelectedLists();
        }
    }

    private class RefreshDistributeListLettersAction extends XAction {
        public RefreshDistributeListLettersAction() {
            super("RefreshDistributeListLetters");
        }

        public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
            refreshLists();
        }
    }

    // Utilites

    /**
     * Get the fop xml for the letters
     */
    private static String getFormatedLettersXml(WllControlComplexValue value, boolean asRingOut, boolean includePost,
            boolean includeEmail, boolean includeFax) {
        return XhibitDelegateHelper.getListDistribution2Delegate().getLettersFop(value, asRingOut, includePost,
                includeEmail, includeFax);
    }

    /**
     * Notify the table that the values have changed
     */
    private static void updateLists(XTable table, WllControlComplexValue[] values) {
        if (table != null) {
            WllControlComplexValue[] selectedLists = getSelectedLists(table);

            // Iterate through the model retaining unchanged values and
            // replacing
            // modifyed values. Once the new data is constructed update the
            // model
            XSortableTableModel model = (XSortableTableModel) table.getModel();
            List retainValueList = new ArrayList();
            for (int i = 0, c = model.getRowCount(); i < c; i++) {
                WllControlComplexValue value = (WllControlComplexValue) model.getDataAt(i);
                int index = indexOf(values, value);
                if (index == -1) {
                    retainValueList.add(value);
                } else {
                    retainValueList.add(values[index]);
                }
            }
            model.setData(retainValueList.toArray(new WllControlComplexValue[retainValueList.size()]));

            setSelectedLists(table, selectedLists);
        }
    }

    /**
     * Remove the values from the table
     */
    private static void removeLists(XTable table, WllControlComplexValue[] values) {
        if (table != null) {
            // Iterate through the model retaining values that have not been
            // removed
            // Once the new data is constructed update the model
            XSortableTableModel model = (XSortableTableModel) table.getModel();
            List retainValueList = new ArrayList();
            for (int i = 0, c = model.getRowCount(); i < c; i++) {
                WllControlComplexValue value = (WllControlComplexValue) model.getDataAt(i);
                if (indexOf(values, value) == -1) {
                    retainValueList.add(value);
                }
            }
            model.setData(retainValueList.toArray(new WllControlComplexValue[retainValueList.size()]));
        }
    }

    /**
     * Return true if the array contains the value
     */
    private static int indexOf(WllControlComplexValue[] values, WllControlComplexValue value) {
        Integer listId = value.getListId();
        for (int i = 0; i < values.length; i++) {
            if (listId.equals(values[i].getListId())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Set the selected values in the table
     */
    private static void setSelectedLists(XTable table, WllControlComplexValue[] values) {
        if (table != null) {
            XSortableTableModel model = (XSortableTableModel) table.getModel();
            for (int i = 0; i < values.length; i++) {
                Integer listId = values[i].getListId();
                for (int j = 0, c = model.getRowCount(); j < c; j++) {
                    if (listId.equals(((WllControlComplexValue) model.getDataAt(j)).getListId())) {
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
    private static WllControlComplexValue[] getSelectedLists(XTable table) {
        if (table != null) {
            XSortableTableModel model = (XSortableTableModel) table.getModel();
            int[] indicies = table.getSelectedRows();
            WllControlComplexValue[] values = new WllControlComplexValue[indicies.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = (WllControlComplexValue) model.getDataAt(indicies[i]);
            }
            return values;
        } else {
            return new WllControlComplexValue[0];
        }
    }

    /**
     * Check all the values can be authorized
     */
    private static boolean canAuthorize(WllControlComplexValue[] values) {
        for (int i = 0; i < values.length; i++) {
            if (!values[i].canAuthorize()) {
                return false;
            }
        }
        return true;

    }

    /**
     * Check all the values can be printed
     */
    private static boolean canPrint(WllControlComplexValue[] values) {
        for (int i = 0; i < values.length; i++) {
            if (!values[i].canPrint()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check all the values can be deleted
     */
    private static boolean canDelete(WllControlComplexValue[] values) {
        for (int i = 0; i < values.length; i++) {
            if (!values[i].canDelete()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the values of the specified type from the values passes
     */
    private static WllControlComplexValue[] getLists(WllControlComplexValue[] values, String documentType) {
        // Create List
        List selectedValueList = new ArrayList();
        for (int i = 0; i < values.length; i++) {
            if (documentType.equals(values[i].getDocumentType())) {
                selectedValueList.add(values[i]);
            }
        }

        // Convert Into Array
        return (WllControlComplexValue[]) selectedValueList
                .toArray(new WllControlComplexValue[selectedValueList.size()]);
    }

    // Delegate Utilities

    /**
     * Update the values in the database
     */
    private static WllControlComplexValue[] authorizeLists(WllControlComplexValue[] values) {
        return XhibitDelegateHelper.getListDistribution2Delegate().authorizeLists(values);
    }

    /**
     * Update the values in the database
     */
    private static void deleteLists(WllControlComplexValue[] values) {
        XhibitDelegateHelper.getListDistribution2Delegate().deleteLists(values);
    }

    /**
     * Update the values in the database
     */
    private static WllControlComplexValue printedList(WllControlComplexValue value) {
        return XhibitDelegateHelper.getListDistribution2Delegate().printedList(value);
    }

    /**
     * Load the values from the database
     */
    private static WllControlComplexValue[] getLists() {
        return XhibitDelegateHelper.getListDistribution2Delegate().getLists(getCourtId());
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
