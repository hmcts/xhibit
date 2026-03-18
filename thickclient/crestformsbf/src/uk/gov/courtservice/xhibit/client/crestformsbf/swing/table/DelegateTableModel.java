package uk.gov.courtservice.xhibit.client.crestformsbf.swing.table;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Implements TableModel through composition inheritence, delegates to another
 * table model.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment 2003
 */
public class DelegateTableModel extends AbstractTableModel implements TableModelListener {

    /**
     * The table model to delegate to
     */
    private TableModel delegate;

    /**
     * Construct a delegate table model without an initial delegate
     */
    public DelegateTableModel() {
        this(null);
    }

    /**
     * Construct a delegate table model with an initial delegate
     * 
     * @param newDelegate
     *            the initial delegate
     */
    public DelegateTableModel(TableModel newDelegate) {
        setDelegate(newDelegate);
    }

    /**
     * Return the current delegate or null if it has not been set
     * 
     * @return the delegate TableModel
     */
    public TableModel getDelegate() {
        return delegate;
    }

    /**
     * Set the TableModel to delegate to. Adds table model listener to new
     * delegate and removes it from the old delegate. This allows events
     * triggered on the delegate model to fire the listeners added to this
     * model.
     * 
     * @param newDelegate
     *            the model to delegate to
     */
    public void setDelegate(TableModel newDelegate) {
        if (delegate != null) {
            if (newDelegate != null) {
                if (!delegate.equals(newDelegate)) {
                    delegate.removeTableModelListener(this);
                    newDelegate.addTableModelListener(this);
                    delegate = newDelegate;
                }
            } else {
                delegate.removeTableModelListener(this);
                delegate = null;
            }
        } else {
            if (newDelegate != null) {
                newDelegate.addTableModelListener(this);
                delegate = newDelegate;
            }
        }
    }

    /**
     * TableListener Implementation, notify any registered listeners that an a
     * table change event has occured, as added to delegates this notifies
     * registered listeners of changes to delegate.
     * 
     * @param e,
     *            the event fired
     */
    public void tableChanged(TableModelEvent e) {
        fireTableChanged(e);
    }

    /**
     * TableModel implementation.
     * 
     * @see javax.swing.table.TableModel#getRowCount() TableModel
     */
    public int getRowCount() {
        return (delegate == null) ? 0 : delegate.getRowCount();
    }

    /**
     * TableModel implementation.
     * 
     * @see javax.swing.table.TableModel#getColumnCount() TableModel
     */
    public int getColumnCount() {
        return (delegate == null) ? 0 : delegate.getColumnCount();
    }

    /**
     * TableModel implementation.
     * 
     * @see javax.swing.table.TableModel#getColumnName() TableModel
     */
    public String getColumnName(int aColumn) {
        return (delegate == null) ? null : delegate.getColumnName(aColumn);
    }

    /**
     * TableModel implementation.
     * 
     * @see javax.swing.table.TableModel#getColumnClass(int) TableModel
     */
    public Class getColumnClass(int aColumn) {
        return (delegate == null) ? null : delegate.getColumnClass(aColumn);
    }

    /**
     * TableModel implementation.
     * 
     * @see javax.swing.table.TableModel#isCellEditable(int,int) TableModel
     */
    public boolean isCellEditable(int row, int column) {
        return (delegate == null) ? false : delegate.isCellEditable(row, column);
    }

    /**
     * TableModel implementation.
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int) TableModel
     */
    public Object getValueAt(int aRow, int aColumn) {
        return (delegate == null) ? null : delegate.getValueAt(aRow, aColumn);
    }

    /**
     * TableModel implementation.
     * 
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     *      TableModel
     */
    public void setValueAt(Object aValue, int aRow, int aColumn) {
        if (delegate != null) {
            delegate.setValueAt(aValue, aRow, aColumn);
        }
    }

}
