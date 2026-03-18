package uk.gov.courtservice.xhibit.client.importexportnotification;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JScrollPane;
import javax.swing.table.TableColumn;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDateTimeTableCellRenderer;

/**
 * <p>
 * Title: Court Details Panel
 * </p>
 * <p>
 * Description: This forms the court related tab pane for the import/export
 * notification screen
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */

public class CourtDetailsPanel extends XPanel {
    private ImportExportNotificationHelper helper = new ImportExportNotificationHelper();

    private GridBagLayout gridBagLayout = new GridBagLayout();

    private JScrollPane detailsScrollPane = null;

    private XTable detailsTable = null;

    private XDialog parent;

    private CourtDetailsModel model;

    private OkCancelPanel buttonPanel;

    /**
     * Public constructor
     * 
     * @param parent
     * @param model
     * @throws CSRecoverableException
     */
    public CourtDetailsPanel(XDialog parent, CourtDetailsModel model) throws CSRecoverableException {
        super();

        this.parent = parent;
        this.model = model;
        this.buttonPanel = (OkCancelPanel) parent.getButtonPanel();

        stepInitialise();
        jbInit();
    }

    /**
     * Drop the components onto the screen
     */
    private void jbInit() {
        this.setLayout(gridBagLayout);

        this.add(getDetailsScrollPane(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        getDetailsScrollPane().getViewport().add(getDetailsTable(), null);
    }

    /**
     * Returns a scroll pane in which to display the court related details
     * 
     * @return JScrollPane
     */
    private JScrollPane getDetailsScrollPane() {
        if (detailsScrollPane == null) {
            detailsScrollPane = new JScrollPane();
            detailsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            detailsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            detailsScrollPane.getViewport().add(getDetailsTable(), null);
            detailsScrollPane.setPreferredSize(new Dimension(800, 496));
        }

        return detailsScrollPane;
    }

    /**
     * Returns an XTable to contain the court related rows
     * 
     * @return XTable
     */
    private XTable getDetailsTable() {
        if (detailsTable == null) {
            detailsTable = XTableFactory.getInstance().createMultiLineTable(
                    new ImportExportNotificationTableModel(ImportExportNotificationHelper.COURT_TAB));

            detailsTable.makeSortable();
            detailsTable.getTableHeader().setReorderingAllowed(false);

            TableColumn column = null;
            column = detailsTable.getColumnModel().getColumn(ImportExportNotificationTableModel.TRANSACTION);
            column.setPreferredWidth(250);
            column = detailsTable.getColumnModel().getColumn(ImportExportNotificationTableModel.STATUS);
            column.setPreferredWidth(150);
            column = detailsTable.getColumnModel().getColumn(ImportExportNotificationTableModel.DATE);
            column.setPreferredWidth(150);
            column.setCellRenderer(new XDateTimeTableCellRenderer(detailsTable, XDateFormat.DATETIMEINSECSFORMAT));
            column = detailsTable.getColumnModel().getColumn(ImportExportNotificationTableModel.DESCRIPTION);
            column.setPreferredWidth(250);
        }

        return detailsTable;
    }

    /**
     * Obtains the data to be displayed
     * 
     * @throws CSRecoverableException
     * 
     */
    public void stepInitialise() throws CSRecoverableException {
        model.setIENTableRowModels(helper.convertToTableModel(model.getCourtDetails()));

        helper.sortByTransaction(model.getIENTableRowModels());
    }

    /**
     * Life-cycle method invoked when the OK/Cancel button is clicked
     * 
     * @param update -
     *            indicates whether or not any changes should be applied to the
     *            D/B
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
    }

    /**
     * Life-cycle method invoked when the screen is made invisible, e.g. when a
     * subsequent screen is made visible. Changes made on the screen are saved
     * in the model
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        moveScreenToModel();
    }

    /**
     * Saves data from the screen into the model
     */
    private void moveScreenToModel() {
    }

    /**
     * Life-cycle method invoked when the OK button is clicked.
     * 
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSRecoverableException {
    }

    /**
     * Life-cycle method invoked when the screen is made visible
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        moveModelToScreen();

        stepUpdateViewState();
    }

    /**
     * Refreshes the screen with new data passed to it i the model
     * 
     * @param newModel -
     *            the new data to be displayed
     */
    public void refresh(CourtDetailsModel newModel) {
        model = newModel;

        model.setIENTableRowModels(helper.convertToTableModel(model.getCourtDetails()));

        helper.sortByTransaction(model.getIENTableRowModels());

        moveModelToScreen();

        stepUpdateViewState();
    }

    /**
     * Invokes a helper method to redisplay the table
     */
    private void moveModelToScreen() {
        helper.redisplayTable(getDetailsTable(), model.getIENTableRowModels());
    }

    /**
     * Life-cycle method that enables/disables screen components in response to
     * user interaction
     */
    public void stepUpdateViewState() {
        buttonPanel.okButton.setEnabled(true);
        buttonPanel.cancelButton.setEnabled(true);
    }
}
