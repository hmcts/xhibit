package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.CrnValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XMaskedCellEditor;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * <p>
 * Title:AddDefendantsToCountTablePanel
 * </p>
 * <p>
 * Description: Panel that allows the user to input a Common Reference Number
 * (CRN) or allows the user to automatically generate the CRN
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author AW Daley
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 28-07-2003 AW Daley Initial Version
 * 
 * 54215 02-9-2003 AW Daley Panel now listens to table model changes and if CRN
 * generation not required the View CRNS button is disabled.
 */

public class DefendantsCountsTablePanel extends JPanel implements ActionListener, TableModelListener {

    private final static String DEFENDANTS_BORDER_TITLE_KEY = "defendantsBorderTitle";

    private final static String DEFENDANTS_TABLE_COLUMN_LABEL_KEY = "defendantsBorderTitle";

    private final static String OFFENCES_BORDER_TITLE_KEY = "offenceDetailsBorderTitle";

    private final static String OFFENCES_TABLE_COLUMN_LABEL_KEY = "offenceDetailsBorderTitle";

    private final static String OFFENCES_SELECT_COLUMN_NAME_KEY = "onOffence";

    private final static String DEFENDANTS_SELECT_COLUMN_NAME_KEY = "onCount";

    private final static String SELECT_ALL_BUTTON_LABEL_KEY = "selectAllButtonLabel";

    private final static String VIEW_CRNS_BUTTON_LABEL_KEY = "viewCRNsButtonLabel";

    private final static String INVALID_NO_OF_CRNS_ERROR_KEY = "crn.invalidNoCRNsErrorMessage";

    private final static String BUSINESS_DELEGATE_NAME = "ChargeController";

    private final static int TABLE_WIDTH = 500;

    private final static int VIEWPORT_HEIGHT = 150;

    private final static Logger log = CSServices.getLogger(DefendantsCountsTablePanel.class);

    private ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.AddCountsDefendantsResources);

    private ResourceBundle errorResources = XHIBITConstant.getResourceBundle(XhibitBundles.ErrorText);

    private DefendantsCountsTableModel tableModel;

    private XTable table;

    private JTextField crnField;

    private JButton btnSelectAll;

    private JButton btnViewCRNs;

    private int courtId;

    private boolean crnsGenerated = false;

    private OkCancelPanel okCancelPanel = null;

    public DefendantsCountsTablePanel(DefendantValue[] defendants, int courtId, OkCancelPanel okCancelPanel,
            AddDefendantsToOffenceDialog.TITLE offenceType) {
        this.courtId = courtId;
        this.okCancelPanel = okCancelPanel;

        // Gets Defendants Table Column Label
        String columnLabel = XHIBITConstant.getResource(resources, DEFENDANTS_TABLE_COLUMN_LABEL_KEY);

        // Gets Defendants Selection Table Column Label
        String selectColumnLabel = "";
        if (offenceType == AddDefendantsToOffenceDialog.TITLE.DEFENDANT_TO_COUNT) {
            selectColumnLabel = XHIBITConstant.getResource(resources, DEFENDANTS_SELECT_COLUMN_NAME_KEY);
        } else {
            selectColumnLabel = XHIBITConstant.getResource(resources, OFFENCES_SELECT_COLUMN_NAME_KEY);
        }

        // Create the table model
        tableModel = new DefendantsCountsTableModel(defendants, columnLabel, selectColumnLabel);

        // Build the panel
        String borderTitle = XHIBITConstant.getResource(resources, DEFENDANTS_BORDER_TITLE_KEY);

        buildPanel(borderTitle);
    }

    public DefendantsCountsTablePanel(OffenceValue[] offences, int courtId, OkCancelPanel okCancelPanel) {
        this.courtId = courtId;
        this.okCancelPanel = okCancelPanel;

        // Gets Defendants Table Column Label
        String columnLabel = XHIBITConstant.getResource(resources, OFFENCES_TABLE_COLUMN_LABEL_KEY);

        // Gets Defendants Selection Table Column Label
        String selectColumnLabel = XHIBITConstant.getResource(resources, OFFENCES_SELECT_COLUMN_NAME_KEY);

        // Create the table model
        tableModel = new DefendantsCountsTableModel(offences, columnLabel, selectColumnLabel);

        // Build the panel
        String borderTitle = XHIBITConstant.getResource(resources, OFFENCES_BORDER_TITLE_KEY);

        buildPanel(borderTitle);
    }

    /**
     * Builds the panel
     */
    private void buildPanel(String borderTitle) {
        this.setLayout(new GridBagLayout());

        // Creates the titled border
        this.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(EtchedBorder.LOWERED), borderTitle));

        // Register panel to listen for table model changes. So that the
        // panel control when the view CRN button is enabled and disabled.
        tableModel.addTableModelListener(this);

        // Create table
        table = XTableFactory.getInstance().createDefaultTable(tableModel);

        // Set Table Column widths
        table.initColumnSizes(getColumnWidths(), TABLE_WIDTH);

        // Set Table Dimensions
        // Dimension tableDimension = new Dimension
        // (TABLE_WIDTH, getTableHeight(tableModel));

        table.setPreferredScrollableViewportSize(new Dimension(TABLE_WIDTH, VIEWPORT_HEIGHT));

        // Render the Common Reference Number Column
        crnField = JTextFieldFactory.getTextField();
        table.getColumnModel().getColumn(DefendantsCountsTableModel.CRN_COLUMN_NO).setCellEditor(
                new XMaskedCellEditor(crnField, CrnValue.MASK));

        // Create Scroll Pane
        JScrollPane scrollPane = new JScrollPane(table);

        this.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));

        // Add Button Panel
        this.add(buildButtonPanel(), new GridBagConstraints(0, 1, 2, 1, 1, 1, GridBagConstraints.EAST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    private JPanel buildButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());

        // Create Select All Button
        String selectAllLabel = XHIBITConstant.getResource(resources, SELECT_ALL_BUTTON_LABEL_KEY);
        btnSelectAll = new JButton(selectAllLabel);

        buttonPanel.add(btnSelectAll, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

        // Register the panel to listen for button clicks
        btnSelectAll.addActionListener(this);

        // Create View CRNs Button
        String viewCRNsLabel = XHIBITConstant.getResource(resources, VIEW_CRNS_BUTTON_LABEL_KEY);
        btnViewCRNs = new JButton(viewCRNsLabel);
        btnViewCRNs.setEnabled(false);

        buttonPanel.add(btnViewCRNs, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

        // Register the panel to listen for button clicks
        btnViewCRNs.addActionListener(this);

        return buttonPanel;
    }

    /**
     * Returns the table model so that the parent panel can retreive the data
     * from the table.
     * 
     * @return
     */
    public DefendantsCountsTableModel getModel() {
        return this.tableModel;
    }

    /**
     * Defines the column widths based ant array of Strings !!!!!!!!!!!
     * 
     * @return
     */
    private Object[] getColumnWidths() {
        Object[] colWidths = new Object[tableModel.getColumnCount()];

        colWidths[DefendantsCountsTableModel.VALUE_COLUMN_NO] = new String("01234567890");

        colWidths[DefendantsCountsTableModel.ON_COUNT_COLUMN_NO] = new Boolean(false);
        colWidths[DefendantsCountsTableModel.CRN_COLUMN_NO] = CrnValue.MASK;
        colWidths[DefendantsCountsTableModel.AUTO_CRN_COLUMN_NO] = new Boolean(true);

        return colWidths;
    }

    /**
     * Generates CRNs for the entries in the table that have 'Auto Generate CRN'
     * selected
     */
    public void generateCRNs() throws CSRecoverableException {
        CrnValue[] generatedCRNs = null;
        // String methodName = "generateCRNs";

        // From the model get the entires to generate CRNs for
        int numberSelected = tableModel.getNumberAutoCRNs();
        // Generate the requested number of CRNs
        generatedCRNs = XhibitDelegateHelper.getChargeDelegate().getNextCrnBatch(new Integer(courtId), numberSelected);

        // Dummy CRNs
        // generatedCRNs = getDummyCRNs(numberSelected);

        // Check that the number of CRNs returned matches the number requested.
        if (generatedCRNs.length != numberSelected) {
            String logMessage = XHIBITConstant.getResource(errorResources, INVALID_NO_OF_CRNS_ERROR_KEY);

            throw new CSRecoverableException(INVALID_NO_OF_CRNS_ERROR_KEY, logMessage);
        } else
            // Set CRNs on table model
            crnsGenerated = true;
        tableModel.setCRNs(generatedCRNs);
    }

    /**
     * Listens for click on select all button or View CRNs button
     * 
     * @param evt
     */
    public void actionPerformed(ActionEvent evt) {
        JButton source = (JButton) evt.getSource();

        // Selects all entires in the the table
        if (source.equals(btnSelectAll))
            tableModel.selectAll();

        // generates CRNs
        if (source.equals(btnViewCRNs)) {
            try {
                generateCRNs();
            } catch (Throwable t) {
                log.error("Error generating CRNs: " + t.getMessage());
            }
        }
    }

    /**
     * Used by the OK button action to determine if the CRNS have already being
     * generated
     * 
     * @return
     */
    public boolean isCRNsGenerated() {
        return this.crnsGenerated;
    }

    /**
     * When the user clicks OK this method checks if the last entry is valid
     * 
     * @throws CSValidationException
     */
    public void stopEditingCurrentCell() throws CSValidationException {
        if (table.isEditing()) {
            // get value entered
            Object value = table.getCellEditor().getCellEditorValue();

            // validate entry
            if (value instanceof String && CrnValue.isValid((String) value)) {
                getModel().setValueAt(value, table.getSelectedRow(), table.getSelectedColumn());
                return;
            }

            // If invalid throw exception
            String errorMessage = XHIBITConstant.getResource(errorResources,
                    DefendantsCountsTableModel.INVALID_CRN_ERROR_MESSAGE_KEY);
            throw new CSValidationException(DefendantsCountsTableModel.INVALID_CRN_ERROR_MESSAGE_KEY, errorMessage);
        }

    }

    /**
     * Listens for changes to the table model. Used to control the enabling of
     * the OK and View CRN buttons.
     * 
     * @param e
     *            TableModelEvent.
     */
    public void tableChanged(TableModelEvent e) {
        // enable the View CRNs button if at least one defendant or offence is
        // selected and its associated generate CRN is also selected.
        btnViewCRNs.setEnabled(tableModel.getNumberAutoCRNs() > 0);

        // enable the OK button if one or more defendants or offences has been
        // selected, otherwise disable it.
        okCancelPanel.getOkAction().setEnabled(tableModel.getSelectedValues().size() > 0);
    }
}