package uk.gov.courtservice.xhibit.client.util;

import java.util.Collection;
import java.util.Vector;

import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: An EDS - Court Service Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public class XHIBITTableModel extends XHIBITDefaultTableModel {
	/**
	 * Setting internaDebug to true will make table rendering very slow. Only
	 * use this flag during development/test, NOT IN PRODUCTION!
	 */

	protected Vector data;

	private Vector oriData; // a copy of the original data, used to check

	// whether the model has changed

	public boolean xIsChanged = false;

	public static boolean internalDebug = false;

	/**
	 * This method runs when the class is first loaded and establishes whether
	 * internal debug is active for this class. If true, results in considerably
	 * more debug information.
	 */
	static {
		try {
			XHIBITConstant.debug("Started static initialisation of XHIBITTableModel.");
			internalDebug = XHIBITConstant.isInternalDebug("XHIBITTableModel");

		} catch (Exception e) {
			XHIBITConstant.error("Exception during Static initialisation of XHIBITTableModel");
			XHIBITConstant.error(e);
		} finally {
			XHIBITConstant.debug("Static initialisation XHIBITTableModel finished.");
		}
	}

	public XHIBITTableModel() {
		setColumnNames(new String[] {});
		setLongValues(new String[] {});
		this.data = new Vector();
	}

	/**
	 * Sets the value in the cell at columnIndex and rowIndex to aValue.
	 * 
	 * @param aValue
	 * @param rowIndex
	 * @param columnIndex
	 */
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		synchronized (data) {
			Vector row = (Vector) data.elementAt(rowIndex);
			row.setElementAt(aValue, columnIndex);
		}

	}

	/**
	 * Override default implementation so that it points to the local vector
	 * 
	 * @return
	 */
	public int getRowCount() {
		int size = data.size();
		return size;
	}

	public Object getValueAt(int row, int col) {
		Object cell;
		try {
			Vector rowv = (Vector) data.elementAt(row);
			cell = rowv.elementAt(col);
			if (internalDebug)
				XHIBITConstant.debug("XHIBITTableModel : cell[" + row + "," + col + "] = [" + cell.toString() + "].");
		} catch (Exception e) {
			cell = new String("");
			XHIBITConstant.error("XHIBITTableModel : cell[" + row + "," + col + "] is null!");
		}
		return cell;
	}

	/**
	 * Override default implementation so that it points to the local vector
	 * 
	 * @param row
	 * @return
	 */
	public Object getDataAt(int row) {
		return getData().elementAt(row);
	}

	/**
	 * @deprecated use <code>setLongValues(Object[] longValues)</code>
	 *             deprecated because Vector is too heavy weight for this task.
	 * @param x
	 */
	public void setLongValues(Vector x) {
		setLongValues(x.toArray());
	}

	/**
	 * @deprecated Use <code>setColumnNames(String[] columnNames)</code>
	 * @param x
	 */
	public void setColumnNames(Vector x) {
		String[] s = new String[x.size()];
		setColumnNames((String[]) x.toArray(s));
	}

	/**
	 * Overriding the implementation of this method so that the local Vector
	 * data object is used.
	 * 
	 * @param c
	 */
	public void setData(Collection c) {
		final int oldLength = _data == null ? 0 : _data.length;
		final int newLength = c == null ? 0 : c.size();

		if (c == null) {
			this.data = new Vector();
		} else {
			if (c instanceof Vector) {
				this.data = (Vector) c;
			} else {
				this.data = new Vector(c);
			}
		}

		if (this.oriData == null)
			oriData = this.data;

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
		// fireTableChanged(new TableModelEvent(this));
	}

	/**
	 * Overriding the implementation of this method as we only want Vectors
	 * 
	 * @param x
	 */
	public void setData(Object[] x) {
		throw new UnsupportedOperationException("Use setData(Vector)");
	}

	public Vector getData() {
		if (internalDebug)
			XHIBITConstant.debug(this.getClass().getName() + ": returing my data collection");
		return this.data;
	}

	public boolean xIsChanged() {
		return this.data.equals(this.oriData);
	}

}