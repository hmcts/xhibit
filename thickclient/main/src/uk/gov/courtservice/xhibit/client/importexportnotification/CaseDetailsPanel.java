package uk.gov.courtservice.xhibit.client.importexportnotification;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDateTimeTableCellRenderer;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;

/**
 * <p>
 * Title: Case Details Panel
 * </p>
 * <p>
 * Description: This forms the case related tab pane for the import/export
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

public class CaseDetailsPanel extends XPanel {

    private ImportExportNotificationHelper helper = new ImportExportNotificationHelper();

    // private GridBagLayout gridBagLayout = new GridBagLayout( );
    private JLabel caseNumberLbl = null;

    private JTextField caseNumberText = null;

    private JScrollPane detailsScrollPane = null;

    private XTable detailsTable = null;

    private XDialog parent;

    private CaseDetailsModel model;

    private OkCancelPanel buttonPanel;

    /**
     * Public constructor
     * 
     * @param parent
     * @param model
     * @throws CSRecoverableException
     */
    public CaseDetailsPanel(XDialog parent, CaseDetailsModel model) throws CSRecoverableException {
        super();

        this.parent = parent;
        this.model = model;
        this.buttonPanel = (OkCancelPanel) parent.getButtonPanel();

        stepInitialise();
        jbInit();
    }

    /**
     * Add components to the screen
     */
    private void jbInit() {
        this.setLayout(new GridBagLayout());

        JPanel caseNumberPanel = new JPanel(new GridBagLayout());
        caseNumberPanel.add(getCaseNumberLbl(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        caseNumberPanel.add(getCaseNumberText(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        this.add(caseNumberPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getDetailsScrollPane(), new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

        getDetailsScrollPane().getViewport().add(getDetailsTable(), null);
    }

    /**
     * Obtains a label for the case number
     * 
     * @return JLabel
     */
    private JLabel getCaseNumberLbl() {
        if (caseNumberLbl == null) {
            caseNumberLbl = new JLabel();
            caseNumberLbl.setText(XHIBITConstant.getResource(XhibitBundles.ImportExportNotification, "lblCaseNumber"));
        }

        return caseNumberLbl;
    }

    /**
     * Obtains a text field for the case number
     * 
     * @return JTextField
     */
    private JTextField getCaseNumberText() {
        if (caseNumberText == null) {
            caseNumberText = JTextFieldFactory.getTextField();
            caseNumberText.setColumns(10);
        }

        return caseNumberText;
    }

    /**
     * Obtains the scroll pane for the import/export statuses
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
     * Obtains a table in which to display the import/export statuses
     * 
     * @return XTable
     */
    private XTable getDetailsTable() {
        if (detailsTable == null) {
            detailsTable = XTableFactory.getInstance().createMultiLineTable(
                    new ImportExportNotificationTableModel(ImportExportNotificationHelper.CASE_TAB));

            detailsTable.makeSortable();
            detailsTable.getTableHeader().setReorderingAllowed(false);

            TableColumn column = null;
            column = detailsTable.getColumnModel().getColumn(ImportExportNotificationTableModel.TRANSACTION);
            column.setPreferredWidth(175);
            column = detailsTable.getColumnModel().getColumn(ImportExportNotificationTableModel.STATUS);
            column.setPreferredWidth(150);
            column = detailsTable.getColumnModel().getColumn(ImportExportNotificationTableModel.DATE);
            column.setPreferredWidth(150);
            column.setCellRenderer(new XDateTimeTableCellRenderer(detailsTable, XDateFormat.DATETIMEINSECSFORMAT));
            column = detailsTable.getColumnModel().getColumn(ImportExportNotificationTableModel.DESCRIPTION);
            column.setPreferredWidth(175);
            column = detailsTable.getColumnModel().getColumn(ImportExportNotificationTableModel.DEFENDANT);
            column.setPreferredWidth(150);
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
        model.setIENTableRowModels(helper.convertToTableModel(model.getCaseDetails()));

        helper.sortByTransaction(model.getIENTableRowModels());
    }

    /**
     * Life-cycle method that is executed when the OK/Cancel button is clicked
     * 
     * @param update -
     *            indicates whether or not the details on the screen should be
     *            saved
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
    }

    /**
     * Life-cycle method that executes when the screen is made invisible
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        moveScreenToModel();
    }

    /**
     * Moves data from the screen to the model
     */
    private void moveScreenToModel() {
    }

    /**
     * Life-cycle method that executes when the OK button is clicked
     * 
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSRecoverableException {
    }

    /**
     * Life-cycle method that executes when the screen is made visible
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        moveModelToScreen();

        stepUpdateViewState();
    }

    /**
     * Refreshes the screen with new data passed in the model
     * 
     * @param newModel
     */
    public void refresh(CaseDetailsModel newModel) {
        model = newModel;

        model.setIENTableRowModels(helper.convertToTableModel(model.getCaseDetails()));

        helper.sortByTransaction(model.getIENTableRowModels());

        moveModelToScreen();

        stepUpdateViewState();
    }

    /**
     * Moves data from the model to the screen
     */
    private void moveModelToScreen() {
        caseNumberText.setText(model.getCaseNumber());

        helper.redisplayTable(getDetailsTable(), model.getIENTableRowModels());
    }

    /**
     * Enables/disables screen components
     */
    public void stepUpdateViewState() {
        helper.enableTextField(getCaseNumberText(), false);

        buttonPanel.okButton.setEnabled(true);
        buttonPanel.cancelButton.setEnabled(true);
    }
}
