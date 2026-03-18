package uk.gov.courtservice.xhibit.client.admin.security.rolemapping;

import javax.swing.table.AbstractTableModel;

/**
 * <p>
 * Title: DerivedRolesModel
 * </p>
 * <p>
 * Description: This is the table model for derived functionality
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
public class DerivedRolesModel extends AbstractTableModel {
    /** Number of columns in this data table */
    private static final int COLUMN_COUNT = 1;

    /** Backing data for the model */
    private GroupRoleValue[] derivedRoles;

    /**
     * Constructor initializes the data
     * 
     * @param newDerivedRoles
     *            the derived roles for the selected group
     */
    public DerivedRolesModel(GroupRoleValue[] newDerivedRoles) {
        if (newDerivedRoles == null)
            throw new IllegalArgumentException("newDerivedRoles is null");

        derivedRoles = newDerivedRoles;
    }

    /**
     * Returns the rowcount for the table
     * 
     * @return the number of rows in the table.
     */
    public int getRowCount() {
        return derivedRoles.length;
    }

    /**
     * Returns the column count for the table
     * 
     * @return the number of columns in the table.
     */
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    /**
     * Returns the cell data
     * 
     * @param row
     *            the row of the cell
     * @param column
     *            the column of the cell
     * @return the object at the given row and column.
     */
    public Object getValueAt(int row, int column) {
        return derivedRoles[row].getDescription();
    }

    /**
     * Refreshes the data
     * 
     * @param newDerivedRoles
     *            the derived roles for the newly selected group
     */
    public void refreshData(GroupRoleValue[] newDerivedRoles) {
        if (newDerivedRoles == null)
            throw new IllegalArgumentException("newDerivedRoles is null");

        derivedRoles = newDerivedRoles;

        // Let the table re-render
        fireTableDataChanged();
    }
}