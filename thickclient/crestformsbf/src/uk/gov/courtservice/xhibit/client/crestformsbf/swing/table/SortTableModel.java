package uk.gov.courtservice.xhibit.client.crestformsbf.swing.table;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

import uk.gov.courtservice.xhibit.client.crestformsbf.util.AbstractXArray;
import uk.gov.courtservice.xhibit.client.crestformsbf.util.ComparatorFactory;
import uk.gov.courtservice.xhibit.client.crestformsbf.util.XArrays;

/**
 * A table model which allows rows to be sorted
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
public class SortTableModel extends DelegateTableModel {

    /**
     * if true sort ascending else sort decending
     */
    private boolean sortAscending = true;

    /**
     * Sort ascending
     */
    private int sortColumn = -1;

    /**
     * Cached index map
     */
    private int[] rowIndexCache = null; // do not access directly use

    // getRowIndexCache

    /**
     * Construct a hidden column table model without an initial delegate
     */
    public SortTableModel() {
        this(null);
    }

    /**
     * Construct a hidden column table model with an initial delegate
     * 
     * @param newDelegate
     *            the initial delegate
     */
    public SortTableModel(TableModel newDelegate) {
        super(newDelegate);
    }

    /**
     * Sort by specified column in ascending order
     * 
     * @param sortColumn
     *            the column to sort by
     */
    public void sort(int newSortColumn) {
        sort(newSortColumn, newSortColumn != sortColumn ? true : !isSortAscending());
    }

    /**
     * @return the index of the sort column or a negative number if unsorted
     */
    public int getSortedColumn() {
        return sortColumn;
    }

    /**
     * @return true if the sort is ascending
     */
    public boolean isSortAscending() {
        return sortAscending;
    }

    /**
     * Sort by specified column
     * 
     * @param sortColumn
     *            the column to sort by
     * @param sortAscending
     *            true to sort ascending, false to sort decending
     */
    public void sort(int newSortColumn, boolean newSortAscending) {
        if (newSortColumn != sortColumn || newSortAscending != sortAscending) {
            sortColumn = newSortColumn;
            sortAscending = newSortAscending;
            invalidateCache();
            // fireTableRowsUpdated(TableModelEvent.HEADER_ROW,
            // TableModelEvent.HEADER_ROW);
            fireTableDataChanged();
            // fireTableStructureChanged(); // this is two heaviy handed
            // need to find a lighter way to get the table header to repaint
        }
    }

    /**
     * TableModel implementation.
     * 
     * @see javax.swing.table.TableModel#isCellEditable(int,int) TableModel
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return super.isCellEditable(mapRowIndex(rowIndex), columnIndex);
    }

    /**
     * TableModel implementation.
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int) TableModel
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        return super.getValueAt(mapRowIndex(rowIndex), columnIndex);
    }

    /**
     * TableModel implementation.
     * 
     * @see javax.swing.table.TableModel#setValueAt(Object, int, int) TableModel
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, mapRowIndex(rowIndex), columnIndex);
    }

    /**
     * TableListener Implementation, a table change event has been fired on the
     * delegate;
     * 
     * @param e
     *            the event fired
     */
    public void tableChanged(TableModelEvent e) {
        invalidateCache();
        super.tableChanged(e);
    }

    /**
     * Map the row indicies to the underlying row
     * 
     * @param indicies
     *            the indicies to map
     * @return an array containing the mapped indicies
     */
    public int[] mapRowIndicies(int[] indicies) {
        if (indicies != null) {
            int[] buffer = new int[indicies.length];
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = mapRowIndex(indicies[i]);
            }
            return buffer;
        } else {
            return null;
        }
    }

    /**
     * Map the row to the underlying row
     * 
     * @param the
     *            row to map
     * @return the mapped index
     */
    public int mapRowIndex(int rowIndex) {
        int[] cache = getRowIndexCache();
        if (rowIndex < 0 || rowIndex >= cache.length) {
            return rowIndex;
        } else {
            return cache[rowIndex];
        }
    }

    /**
     * Discard the cached column information
     */
    protected void invalidateCache() {
        rowIndexCache = null;
    }

    /**
     * Get the columnIndexCache rebuilding if necisary
     * 
     * @return the cached column index
     */
    private int[] getRowIndexCache() {
        if (rowIndexCache == null) {
            if (sortColumn < 0) {
                rowIndexCache = XArrays.getIdentityIndicies(getRowCount());
            } else {
                rowIndexCache = XArrays.getSortedIndicies(new ColumnXArray(getDelegate(), sortColumn),
                        sortAscending ? ComparatorFactory.getNormalComparator() : ComparatorFactory
                                .getReverseComparator());
            }
        }
        return rowIndexCache;
    }

    /**
     * Column implementaion of XArray interface used for sorting indicies
     */
    private static class ColumnXArray extends AbstractXArray {
        private final int column;

        private final TableModel model;

        public ColumnXArray(TableModel model, int column) {
            this.column = column;
            this.model = model;
        }

        /**
         * XArray implementation
         * 
         * @see XArray.set(int, Object) XArray
         */
        public void set(int index, Object object) {
            model.setValueAt(object, index, column);
        }

        /**
         * XArray implementation
         * 
         * @see XArray.get(int) XArray
         */
        public Object get(int index) {
            return model.getValueAt(index, column);
        }

        /**
         * Return the length of the
         * 
         * @param index
         *            the index
         * @return the value at the given index
         */
        public int length() {
            return model.getRowCount();
        }
    }

    /**
     * @return true if the model is a SortTableModel or delegates to a
     *         SortTableModel
     */
    public static boolean isSortTableModel(TableModel model) {
        if (model != null) {
            if (model instanceof SortTableModel) {
                return true;
            } else if (model instanceof DelegateTableModel) {
                return isSortTableModel(((DelegateTableModel) model).getDelegate());
            }
        }
        return false;
    }

    /**
     * @return the SortTableModel or the SortTableModel delegate
     * @throws IllegalArgumentException
     *             if it cant fine a SortTableModel
     */
    public static SortTableModel getSortTableModel(TableModel model) {
        if (model != null) {
            if (model instanceof SortTableModel) {
                return (SortTableModel) model;
            } else if (model instanceof DelegateTableModel) {
                return getSortTableModel(((DelegateTableModel) model).getDelegate());
            }
        }
        throw new IllegalStateException("SortTableHeader table does not have a SortTableModel.");
    }
}
