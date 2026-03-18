package uk.gov.courtservice.xhibit.client.maintaincharges.joinder;

import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: XHIBIT 2 - Defendant Mapping Table Model
 * </p>
 * <p>
 * Description: Holds the model of data for selected and joinder defendant
 * mappings.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Antoniou
 * @version 1.0
 */

public class DefendantMappingTableModel extends XHIBITDefaultTableModel {
    /** Column index for column containing joinder defendants. */
    public static final int JOINDER_DEFENDANTS = 0;

    /**
     * Column index for column containing defendants from the selected
     * indictment.
     */
    public static final int SELECTED_DEFENDANTS = 1;

    /**
     * The joinder defendant values stored in the
     * <code>JOINDER_DEFENDANTS</code> column.
     */
    // protected Object[] joinderDefendantValues;
    /**
     * The selected defendant values stored in the
     * <code>SELECTED_DEFENDANTS</code> column.
     */
    protected DefendantValue[] selectedDefendantValues;

    /** The column names of the table. */
    // protected String[] columnNames;
    /**
     * Default Constructor. The first column will contain all the defendants
     * from the indictment that will be performing the join - i.e the main
     * joinder indictment. First column is strictly uneditable. The second
     * column is editable, and will represent the defendants from the selected
     * indictment. If the selected defendant id has a match with the joinder
     * defendant id, then this defendant is populated in the cell by default and
     * becomes non-editable.
     * 
     * @param chargeValue
     *            the container of defendants associated to a charge.
     */
    public DefendantMappingTableModel(String columnName1, String columnName2) {
        super();
        setColumnNames(new String[] { columnName1, columnName2 });
    }

    /**
     * Will populate the table with joinder defendants.
     * 
     * @param joinderDefendants
     *            the joinder defendants to populate this table with.
     */
    public void setData(Object[] joinderDefendants) {
        super.setData(joinderDefendants);
        int selectedIndex = _data == null ? 0 : _data.length;
        this.selectedDefendantValues = new DefendantValue[selectedIndex];
    }

    /**
     * Will return a defendant. If the value is coming from the first column
     * then a joinder defendant is returned, otherwise a selected defendant is
     * returned.
     * 
     * @param row
     *            the row from which the value is being retrieved
     * @param col
     *            the column from which the value is being retrieved
     * 
     * @return the defendant
     */
    public Object getValueAt(int row, int col) {
        return col == JOINDER_DEFENDANTS ? _data[row] : this.selectedDefendantValues[row];
    }

    /**
     * Will set a defendant value object in the cell that lies in the given row
     * and column. If the object is not an instance of
     * <code>DefendantValue</code>then null is set.
     * 
     * @param value
     *            the value obect to be set.
     * @param row
     *            the index of the row in which the cell lies.
     * @param col
     *            the index of the column in which the cell lies.
     */
    public void setValueAt(Object value, int row, int col) {
        DefendantValue defendantValue = null;
        if ((value != null) && (value instanceof DefendantValue)) {
            defendantValue = (DefendantValue) value;
        }

        if (col == SELECTED_DEFENDANTS) {
            this.selectedDefendantValues[row] = defendantValue;
        } else if (col == JOINDER_DEFENDANTS) {
            _data[row] = defendantValue;
        }
    }

    /**
     * Will determine if the cell is editable. The first column is always
     * uneditble. A cell in the second column is only uneditable if its
     * defendant id is the same as the joinder defendant id on the same row.
     * 
     * @param rowIndex
     *            the index of the row to which the cell is being referenced
     * @param columnIndex
     *            the index of the column to which the cell is being referenced
     * 
     * @return whether the cell is editable
     */
    public boolean isCellEditable(int row, int column) {
        boolean editable = false;
        if (column == JOINDER_DEFENDANTS) {
            editable = false;
        } else {
            if (this.selectedDefendantValues[row] == null) {
                editable = true;
            } else {
                // We are in the selected column, check to see if its id is the
                // same as the joinder one.
                // If it is, then don't make it editable.
                if (_data[row] == null) {
                    editable = true;
                } else {
                    editable = !this.selectedDefendantValues[row].getDefendantID().equals(
                            ((DefendantValue) _data[row]).getDefendantID());
                }
            }
        }
        return editable;
    }

    /**
     * @return the number of Joinder defendant values as the row count.
     */
    // public int getRowCount ()
    // {
    // return (this.joinderDefendantValues != null) ?
    // this.joinderDefendantValues.length : 0;
    // }
    /**
     * @return the column count.
     */
    // public int getColumnCount ()
    // {
    // return (this.columnNames != null) ? this.columnNames.length : 0;
    // }
    /**
     * Get the column name of the column associated to the given index.
     * 
     * @param colIndex
     *            the index of the column that will have its name set.
     * 
     * @return the column name.
     */
    // public String getColumnName (int colIndex)
    // {
    // return (this.columnNames != null) ? this.columnNames[colIndex] : "";
    // }
    /**
     * Sets the column names of the table associated to this model.
     */
    // private void setColumnNames (String columnName1,String columnName2)
    // {
    // this.columnNames = new String[] {columnName1,columnName2};
    // }
}