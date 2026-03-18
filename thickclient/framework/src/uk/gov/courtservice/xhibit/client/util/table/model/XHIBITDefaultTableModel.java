package uk.gov.courtservice.xhibit.client.util.table.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: Default Table Model
 * </p>
 * <p>
 * Description: Implements most of the required methods except for getValueAt
 * which will be dependant per implementation.
 * </p>
 * Note: getData has not been provided as a method as the return type may vary
 * Implementing classes can create a method if required, though in most cases
 * the standard get methods should provide all the information required
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: XHIBITDefaultTableModel.java,v 1.4 2005/02/11 16:42:00 sz0t7n
 *          Exp $
 */

public abstract class XHIBITDefaultTableModel extends XHIBITTableModelInterface {

    /**
     * This object is made protected so implementing class can have access to it
     * either in the getValueAt or so that they can have getData methods
     */
    protected Object[] _data;

    /**
     * String Array of column names
     */
    private String[] _columnNames;

    /**
     * Object array of values that can be passed into XTable.initColumnSizes()
     * to dynamically set the size of the columns
     */
    private Object[] _longValues;

    /**
     * Default constructor
     */
    public XHIBITDefaultTableModel() {
        // Add a listener for debug information
        addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent tme) {
                XHIBITConstant.debug("--tableChanged(TableModelEvent tme)--!!!!! " + tme.getColumn());
            }
        });
    }

    /**
     * Contructor that accepts the data for the table model.
     * 
     * @param data
     */
    public XHIBITDefaultTableModel(Object[] data) {
        this();
        setData(data);
    }

    /**
     * Returns a row. This is typically a value object This method should be
     * used to get the data instead of accessing the local variable directly in
     * case the model has been sorted or filtered. The selected row may not
     * directly map to the row in the array.
     * 
     * @param row
     * @return
     */
    public Object getDataAt(int row) {
        return _data == null ? null : _data[row];
    }

    /**
     * Helper method to convert the collection to an array for storing
     * 
     * @param x
     */
    public void setData(Collection x) {
        setData(x.toArray());
    }

	/**
	 * Helper method to get data as a collection.
	 * 
	 * @return A non null list.
	 */
	public Collection<Object> getDataAsCollection() {
		return _data == null ? new ArrayList<Object>() : (Arrays.asList(_data));
	}

    /**
     * Store the data and notify the model has changed
     * 
     * @param data
     */
    public void setData(Object[] data) {
        int oldLength = _data == null ? 0 : _data.length;
        int newLength = data == null ? 0 : data.length;

        _data = data;

        // Notify the table if the number of rows has changed
        // As we dont know where insert or delete occured perform at end,
        // this works as the table is subsequently notified that the data
        // has changed.
        if (oldLength < newLength) {
            fireTableRowsInserted(oldLength, newLength - 1);
        } else if (oldLength > newLength) {
            fireTableRowsDeleted(newLength, oldLength - 1);
        }

        // Notify the table the data has changed
        fireTableDataChanged();
    }

    /**
     * Returns the array of column names. This method will probably never be
     * used!
     * 
     * @return
     */
    public String[] getColumnNames() {
        return _columnNames;
    }

    /**
     * Store the column names so that getColumnName can access it.
     * 
     * @param columnNames
     */
    public void setColumnNames(String[] columnNames) {
        _columnNames = columnNames;
    }

    /**
     * Return the long values for sizing columns This can be passed into
     * XTable.initColumnSizes()
     * 
     * @return
     */
    public Object[] getLongValues() {
        return _longValues;
    }

    /**
     * Store the long values
     * 
     * @param longValues
     */
    public void setLongValues(Object[] longValues) {
        _longValues = longValues;
    }

    /**
     * Return the number of values in the array, corresponding to the number of
     * rows in the table.
     * 
     * @return int for number of rows
     */
    public int getRowCount() {
        return _data == null ? 0 : _data.length;
    }

    /**
     * Return the number of columns
     * 
     * @return the size of array passed into setColumnNames
     */
    public int getColumnCount() {
        return _columnNames == null ? 0 : _columnNames.length;
    }

    /**
     * Override the super method to get the column name from the local list. If
     * not available use the super method which will return a letter from the
     * Alphabet (like Excel)
     * 
     * @param col
     * @return
     */
    public String getColumnName(int col) {
        if (_columnNames == null)
            return super.getColumnName(col);
        else
            return _columnNames[col];
    }

    public String getNameForPropertyFile() {
        return getClass().getName();
    }
}