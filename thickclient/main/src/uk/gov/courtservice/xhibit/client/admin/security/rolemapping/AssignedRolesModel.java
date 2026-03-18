package uk.gov.courtservice.xhibit.client.admin.security.rolemapping;

import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: AssignedRolesModel
 * </p>
 * <p>
 * Description: This is the table model for the assigned role functionality for
 * the current group.
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
public class AssignedRolesModel extends AbstractTableModel {
    private static final Logger log = Logger.getLogger(AssignedRolesModel.class);

    private static final int COLUMN_ROLE = 0;

    private static final int COLUMN_ASSIGNED = 1;

    /** Number of columns in this data table */
    private static final int COLUMN_COUNT = 2;

    /** The group role mapping */
    private GroupRoleMapping mapping;

    /** Backing data for the model */
    private GroupRoleValue[] assRoles;

    /** Backing data for the derived table model. */
    private GroupRoleValue[] derivedRoles;

    /**
     * Constructor initializes the data
     * 
     * @param mapping
     *            The GroupRoleMapping.
     */
    public AssignedRolesModel(GroupRoleMapping mapping) {
        if (mapping == null)
            throw new IllegalArgumentException("GroupRoleMapping is null");

        this.mapping = mapping;

        assRoles = new GroupRoleValue[] {};
    }

    /**
     * Returns the rowcount for the table
     * 
     * @return the number of rows in the table.
     */
    public int getRowCount() {
        return assRoles.length;
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
     * Returns the column class
     * 
     * @param columnIndex
     *            the column being queried.
     * @return the class for the given column.
     */
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
        case COLUMN_ROLE:
            return String.class;
        case COLUMN_ASSIGNED:
            return Boolean.class;
        default:
            throw new IllegalArgumentException("Invalid column");
        }
    }

    /**
     * Checks whether the cell is editable
     * 
     * @param rowIndex
     *            the row being queried.
     * @param columnIndex
     *            the column being queried.
     * @return whether or not the cell for the given row and column is editable
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean isEditable = true;
        if (columnIndex == COLUMN_ROLE) {
            isEditable = false;
        } else if (columnIndex == COLUMN_ASSIGNED) {
            String roleDescription = (String) getValueAt(rowIndex, COLUMN_ROLE);
            for (int i = 0; i < derivedRoles.length; i++) {
                if (roleDescription.equalsIgnoreCase(derivedRoles[i].getDescription())) {
                    isEditable = false;
                    break;
                }
            }
        }
        return isEditable;
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
        switch (column) {
        case COLUMN_ROLE:
            return assRoles[row].getDescription();
        case COLUMN_ASSIGNED:
            return (assRoles[row].isEnabled()) ? Boolean.TRUE : Boolean.FALSE;
        default:
            throw new IllegalArgumentException("Invalid column");
        }
    }

    /**
     * Set the value for the given row an column
     * 
     * @param value
     *            the object to set
     * @param row
     *            the row of the cell
     * @param column
     *            the column of the cell
     */
    public void setValueAt(Object value, int row, int column) {
        if (column == COLUMN_ASSIGNED) {
            assRoles[row].setEnabled(((Boolean) value).booleanValue());
            mapping.enableRole(assRoles[row].getGroupName(), assRoles[row]);

            if (log.isDebugEnabled()) {
                log.debug("setValueAt(" + value + ", " + row + ", " + column + ")" + " groupName is "
                        + assRoles[row].getGroupName());
            }

            assRoles[row].setChanged(true);
        } else {
            throw new IllegalArgumentException("Invalid column");
        }
        fireTableCellUpdated(row, column);
        return;
    }

    /**
     * Refreshes the data
     * 
     * @param group
     *            the newly selected group
     * @param mapping
     *            the Group to Role mapping
     * @param newDerivedRoles
     *            the derived roles for the newly selected group
     */
    public void refreshData(String group, GroupRoleMapping mapping, GroupRoleValue[] newDerivedRoles) {
        if (group == null)
            throw new IllegalArgumentException("group is null");

        this.mapping = mapping;
        this.derivedRoles = newDerivedRoles;

        int oldLength = assRoles == null ? 0 : assRoles.length;

        assRoles = mapping.getAllRolesForGroup(group);
        mapping.enableChildRoles(group);

        int newLength = assRoles == null ? 0 : assRoles.length;

        // Notify the table if the number of rows has changed
        // As we dont know where insert or delete occured perform at end,
        // this works as the table is subsequently notified that the data
        // has changed.
        if (oldLength < newLength) {
            fireTableRowsInserted(oldLength, newLength - 1);
        } else if (oldLength > newLength) {
            fireTableRowsDeleted(newLength, oldLength - 1);
        }

        // Let the table re-render
        fireTableDataChanged();
    }
}