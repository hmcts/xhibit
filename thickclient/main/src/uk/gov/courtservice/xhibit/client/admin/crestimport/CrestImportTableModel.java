package uk.gov.courtservice.xhibit.client.admin.crestimport;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import uk.gov.courtservice.xhibit.business.services.crestimport.CrestImportException;
import uk.gov.courtservice.xhibit.business.vos.services.crestimport.CrestImportStatus;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: CrestImportTableModel
 * </p>
 * <p>
 * Description: This is the table model for crest import status
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class CrestImportTableModel extends AbstractTableModel {

    // Backing data for the model
    private CrestImportStatus[] model;

    // Whether the status is checked
    private Boolean[] checked;

    private String[] columnNames = {};

    /**
     * Constructor initializes the data
     * 
     * @param newDerivedRoles
     */
    public CrestImportTableModel(CrestImportStatus[] newModel) {
        setModel(newModel);
        setColumnNames(new String[] { XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "SelectColumn"),
                XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RefDataColumn"),
                XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "StatusColumn") });
    }

    public void setColumnNames(String[] x) {
        this.columnNames = x;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     * Returns the rowcount for the table
     * 
     * @return
     */
    public int getRowCount() {
        return model.length;
    }

    /**
     * Returns the column count for the table
     * 
     * @return
     */
    public int getColumnCount() {
        return 3;
    }

    /**
     * Returns the column class
     * 
     * @param columnIndex
     * @return
     */
    public Class getColumnClass(int columnIndex) {

        switch (columnIndex) {
        case 0:
            return Boolean.class;
        default:
            return String.class;
        }

    }

    /**
     * Checks whether the cell is editable
     * 
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }

    /**
     * Returns the cell data
     * 
     * @param row
     * @param column
     * @return
     */
    public Object getValueAt(int row, int column) {

        switch (column) {
        case 0:
            return checked[row];
        case 1:
            return model[row].getDescription();
        case 2:
            String status = model[row].getStatus();
            if (status.equals("N") || status.equals("R") || status.equals("S") || status.equals("F")
                    || status.equals("I"))
                status = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, status);
            return status;
        default:
            throw new IllegalArgumentException("Invalid column");
        }

    }

    /**
     * Returns the cell data
     * 
     * @param row
     * @param column
     * @return
     */
    public void setValueAt(Object value, int row, int column) {
        if (column == 0)
            checked[row] = (Boolean) value;
    }

    /**
     * Refreshes the data
     * 
     * @param newDerRoles
     */
    public void setModel(CrestImportStatus[] newModel) {

        if (newModel == null)
            throw new IllegalArgumentException("newModel is null");

        model = newModel;
        checked = new Boolean[model.length];
        for (int i = 0; i < model.length; i++)
            checked[i] = new Boolean(false);

        // Let the table re-render
        fireTableDataChanged();

    }

    /**
     * Return the selected CrestImportStatus objects to import
     * 
     * @return
     */
    public CrestImportStatus[] getSelectedImportList() throws CrestImportException {

        ArrayList types = new ArrayList();
        for (int i = 0; i < checked.length; i++) {
            if (checked[i].booleanValue() == true) {
                if (model[i].getStatus() != null
                        && model[i].getStatus().equalsIgnoreCase(CrestImportStatus.IN_PROGRESS)) {
                    throw new CrestImportException(CrestImportException.IMPORT_IN_PROGRESS,
                            "The import is in progress and can therefore not be requested whilst importing");
                }
                types.add(model[i]);
            }
        }

        return (CrestImportStatus[]) types.toArray(new CrestImportStatus[types.size()]);

    }

}