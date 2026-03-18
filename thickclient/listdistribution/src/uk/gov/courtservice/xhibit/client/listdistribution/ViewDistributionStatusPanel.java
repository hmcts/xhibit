package uk.gov.courtservice.xhibit.client.listdistribution;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.DistributionStatusComplexValue;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.PrintFunction;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.sort.SortButtonRenderer;
import uk.gov.courtservice.xhibit.client.util.table.model.sortable.XSortableTableModel;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDateTimeTableCellRenderer;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * <p>
 * Title: ViewDistributionStatusPanel
 * </p>
 * <p>
 * Description: Builds the screen for viewing distribution status.
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
 * @version $Id: ViewDistributionStatusPanel.java,v 1.3 2005/02/06 15:49:28
 *          bzjrnl Exp $
 */
public class ViewDistributionStatusPanel extends AbstractListDistributionPanel implements PrintFunction {
    private static final String PRINT_DISTRIBUTION_STATUS_SCHEMA = getProperty("xsl.schema.printdistributionstatus");

    // The components!
    private final XTable table;

    private final ButtonPanel buttonPanel;

    /**
     * Create a panel for maintaining the list recipients.
     * 
     * @param xac
     *            The Application Controller
     * @throws CSRecoverableException
     *             if an error occures creating the panel
     */
    public ViewDistributionStatusPanel(XhibitApplicationController xac) throws CSRecoverableException {
        super(xac);

        ViewDistributionStatusTableModel model = new ViewDistributionStatusTableModel(getDistributionStatus());
        table = createTable(model, model.getDefaultSortColumnIndex(), model.getDefaultSortAsscending());
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
        add(new JScrollPane(table), createTableTabbedPaneConstraints());
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
        if (buttonPanel != null && table != null) {
            // Enable the buttons based on weather all the selected
            // values can perform the operation, refresh is allways
            // enabled!
            DistributionStatusComplexValue[] values = getSelected(table);
            if (values.length > 0) {
                buttonPanel.stepUpdateViewState(canDelete(values), true);
            } else {
                buttonPanel.stepUpdateViewState(false, true);
            }
        }

    }

    /**
     * Print Function Implementation
     */
    public String[] print() throws CSRecoverableException {
        return new String[] { table.toFop(getResource("viewdistributionstatus.print.title")) };
    }

    /**
     * Print Function Implementation
     */
    public boolean autoSaveToFile() {
        return false;
    }

    // Event Callbacks

    private void deleteSelected() throws CSRecoverableException {
        DistributionStatusComplexValue[] values = getSelected(table);
        if (values.length > 0) {
            if (showConfirmDialog("distributelistletterspanel.confirm.deletetitle",
                    "distributelistletterspanel.confirm.deletemessageheader", values)) {
                // Delete the values, update the db and then update the view
                deleteDocuments(values);
                remove(table, values);
            }
        }
    }

    private void refreshDocuments() {
        setData(table, getDistributionStatus());
    }

    private boolean showConfirmDialog(String titleKey, String messageHeaderKey, DistributionStatusComplexValue[] values)
            throws CSRecoverableException {
        String[] list = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            list[i] = ViewDistributionStatusTableModel.getType(values[i]) + " - "
                    + format(ViewDistributionStatusTableModel.getCreationDate(values[i]));
        }

        ConfirmListDialog dialog = new ConfirmListDialog(xac, titleKey, messageHeaderKey, list);
        dialog.setVisible(true);
        return dialog.isOkClicked();
    }

    private static final String format(Date date) {
        return XDateFormat.format(date, XDateFormat.DATETIMEINSECSFORMAT);
    }

    // Components

    private XTable createTable(TableModel tableModel, int columnIndex, boolean ascending) {
        XTable table = XTableFactory.getInstance().createDefaultTable(tableModel);
        table.makeSortable(true);

        // Default Sort Start
        TableModel sortTableModel = table.getModel();
        TableCellRenderer sortHeaderRenderer = table.getColumn(table.getColumnName(columnIndex)).getHeaderRenderer();
        if (sortTableModel instanceof XSortableTableModel && sortHeaderRenderer instanceof SortButtonRenderer) {
            ((XSortableTableModel) sortTableModel).sortByColumn(columnIndex, ascending);
            // Once For descending
            ((SortButtonRenderer) sortHeaderRenderer).setSelectedColumn(columnIndex);
            if (!ascending) {
                // Twice For ascending
                ((SortButtonRenderer) sortHeaderRenderer).setSelectedColumn(columnIndex);
            }
        }
        // Default Sort End

        table.setDefaultRenderer(Date.class, new XDateTimeTableCellRenderer(table, XDateFormat.DATETIMEINSECSFORMAT));
        table.getSelectionModel().addListSelectionListener(new TableListSelectionListener());
        return table;
    }

    private class ButtonPanel extends JPanel {
        private final JButton deleteButton;

        private final JButton refreshButton;

        public ButtonPanel() {
            super(new GridLayout(1, 2, 4, 4));

            deleteButton = new JButton(new DeleteViewDistributionStatusAction());
            add(deleteButton);

            refreshButton = new JButton(new RefreshViewDistributionStatusAction());
            add(refreshButton);
        }

        /**
         * Enable Disable the buttons as required
         */
        public void stepUpdateViewState(boolean deleteEnabled, boolean refreshEnabled) {
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

    // Actions

    private class DeleteViewDistributionStatusAction extends XAction {
        public DeleteViewDistributionStatusAction() {
            super("DeleteViewDistributionStatus");
        }

        public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
            deleteSelected();
        }
    }

    private class RefreshViewDistributionStatusAction extends XAction {
        public RefreshViewDistributionStatusAction() {
            super("RefreshViewDistributionStatus");
        }

        public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
            refreshDocuments();
        }
    }

    // Utilites

    /**
     * Remove the values from the table
     */
    private static void remove(XTable table, DistributionStatusComplexValue[] values) {
        if (table != null) {
            // Iterate through the model retaining values that have not been
            // removed
            // Once the new data is constructed update the model
            XSortableTableModel model = (XSortableTableModel) table.getModel();
            List retainValueList = new ArrayList();
            for (int i = 0, c = model.getRowCount(); i < c; i++) {
                DistributionStatusComplexValue value = (DistributionStatusComplexValue) model.getDataAt(i);
                if (indexOf(values, value) == -1) {
                    retainValueList.add(value);
                }
            }
            model.setData(retainValueList.toArray(new DistributionStatusComplexValue[retainValueList.size()]));
        }
    }

    /**
     * Return true if the array contains the value
     */
    private static int indexOf(DistributionStatusComplexValue[] values, DistributionStatusComplexValue value) {
        for (int i = 0; i < values.length; i++) {
            if (value.equals(values[i])) {
                return i;
            }
        }
        return -1;
    }

    // /**
    // * Get the values from the table as Xml formated as fop
    // */
    // private static String getDistributionStatusPrintValuesFop(XTable
    // table)
    // {
    // String fop = _getDistributionStatusPrintValuesFop(table);
    // //System.out.println("fop: " + fop);
    // return fop;
    // }
    //
    // private static String _getDistributionStatusPrintValuesFop(XTable
    // table)
    // {
    // return
    // XSLServices.getInstance().transform(getDistributionStatusPrintValuesXml(table),
    // PRINT_DISTRIBUTION_STATUS_SCHEMA);
    // }
    //
    // /**
    // * Get the values from the table as Xml
    // */
    // private static String getDistributionStatusPrintValuesXml(XTable
    // table)
    // {
    // String xml = _getDistributionStatusPrintValuesXml(table);
    // //System.out.println("xml: " + xml);
    // return xml;
    // }
    //
    // private static String _getDistributionStatusPrintValuesXml(XTable
    // table)
    // {
    //
    // return DistributionStatusPrint.getInstance()
    // .createDistributionStatusPrintValuesXml(getDistributionStatusComplexValues(table));
    // }

    /**
     * Get the values from the table
     */
    private static DistributionStatusComplexValue[] getDistributionStatusComplexValues(XTable table) {
        if (table == null) {
            return null;
        }
        TableModel model = table.getModel();
        if (!(model instanceof XSortableTableModel)) {
            return null;
        }
        int c = model.getRowCount();
        DistributionStatusComplexValue[] values = new DistributionStatusComplexValue[c];
        for (int i = 0; i < c; i++) {
            values[i] = (DistributionStatusComplexValue) ((XSortableTableModel) model).getDataAt(i);
        }
        return values;
    }

    /**
     * Set the values in the table
     */
    private static void setData(XTable table, DistributionStatusComplexValue[] values) {
        if (table != null) {
            DistributionStatusComplexValue[] selected = getSelected(table);
            XSortableTableModel model = (XSortableTableModel) table.getModel();
            model.setData(values);
            setSelected(table, selected);
        }
    }

    /**
     * Set the selected values in the table
     */
    private static void setSelected(XTable table, DistributionStatusComplexValue[] values) {
        if (table != null) {
            XSortableTableModel model = (XSortableTableModel) table.getModel();
            for (int i = 0; i < values.length; i++) {
                for (int j = 0, c = model.getRowCount(); j < c; j++) {
                    if (values[i].equals(model.getDataAt(j))) {
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
    private static DistributionStatusComplexValue[] getSelected(XTable table) {
        if (table != null) {
            XSortableTableModel model = (XSortableTableModel) table.getModel();
            int[] indicies = table.getSelectedRows();
            DistributionStatusComplexValue[] values = new DistributionStatusComplexValue[indicies.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = (DistributionStatusComplexValue) model.getDataAt(indicies[i]);
            }
            return values;
        } else {
            return new DistributionStatusComplexValue[0];
        }
    }

    /**
     * Check all the values can be deleted
     */
    private static boolean canDelete(DistributionStatusComplexValue[] values) {
        for (int i = 0; i < values.length; i++) {
            if (!values[i].canDelete()) {
                return false;
            }
        }
        return true;
    }

    // Delegate Utilities

    /**
     * Get the values of the specified type from the values passes
     */
    private static DistributionStatusComplexValue[] getDistributionStatus() {
        return XhibitDelegateHelper.getListDistribution2Delegate().getDistributionStatus(getCourtId());
    }

    /**
     * Update the values in the database
     */
    private static void deleteDocuments(DistributionStatusComplexValue[] values) {
        XhibitDelegateHelper.getListDistribution2Delegate().delete(values);
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
