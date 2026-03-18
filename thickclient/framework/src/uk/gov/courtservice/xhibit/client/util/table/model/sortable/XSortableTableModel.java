package uk.gov.courtservice.xhibit.client.util.table.model.sortable;

import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelDecoratorInterface;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;

/**
 * <p>
 * Title: XHIBIT 2 XSortableTabelModel
 * </p>
 * <p>
 * Description: This table model takes an XHIBITTableModel as constructor
 * argument and returns the same data but in a sortable model.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @editor Rakesh Lakhani Changed to extend AbstractTableModel and implement
 *         XHIBITTableModelInterface so that no data is stored in this model,
 *         but passed onto the xhibit
 * @version 1.0
 */

public class XSortableTableModel extends XHIBITTableModelInterface implements XHIBITTableModelDecoratorInterface {
    private int indexes[];

    private Vector sortingColumns = new Vector();

    private boolean ascending = true;

    private int compares;

    private XHIBITTableModelInterface xmodel;

    private static boolean internalDebug = false;

    static {
        try {
            internalDebug = XHIBITConstant.isInternalDebug("XSortableTableModel");
        } catch (Exception e) {
            XHIBITConstant.error("Exception during Static initialisation of XSortableTableModel");
            XHIBITConstant.error(e);
        } finally {
            XHIBITConstant.debug("Static initialisation XSortableTableModel finished.");
        }
    }

    public XSortableTableModel(XHIBITTableModelInterface model) {
        super();
        setModel(model);
        model.addTableModelListener(new SortTableModelListener(this));
    }

    public TableModel getModel() {
        return xmodel;
    }

    /**
     * <p>
     * Title: Table Model listener
     * </p>
     * <p>
     * Description: A listener to pick up table changes to the underlying model
     * (xmodel) and reallocate indexes internally
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: Electronic Data Systems
     * </p>
     * 
     * @author Rakesh Lakhani
     * @version 1.0
     */
    class SortTableModelListener implements TableModelListener {
        private XSortableTableModel _tm;

        public SortTableModelListener(XSortableTableModel tm) {
            _tm = tm;
        }

        public void tableChanged(TableModelEvent tme) {
            _tm.checkReallocateIndexes();
            _tm.fireTableChanged(tme);
        }
    }

    /*
     * ********************************* Methods specific to this class *
     * *********************************
     */
    public void setModel(XHIBITTableModelInterface model) {
        xmodel = model;
        /** @todo Should this call table changed instead? */
        reallocateIndexes();
    }

    public int getIndexedRow(int row) {
        int indexedRow = row;
        try {
            indexedRow = indexes[row];
        } catch (Exception e) {
            XHIBITConstant.error("Exception in XTableSorter.getIndexedRow() " + e);
        }
        // XHIBITConstant.debug("XTableSorter.getIndexedRow() " + row + " --> "
        // + indexedRow);
        return indexedRow;
    }

    /**
     * Sets up a new array of indexes with the right number of elements for the
     * new data model.
     */
    private void reallocateIndexes() {
        int rowCount = xmodel.getRowCount();
        indexes = new int[rowCount];
        for (int row = 0; row < rowCount; row++)
            indexes[row] = row;
        // re-sort the array based on selected sort criteria
        sort(this);
    }

    private void checkReallocateIndexes() {
        if (indexes.length != xmodel.getRowCount()) {
            reallocateIndexes();
        }
    }

    /**
     * Handle the table changed event
     * 
     * @param e
     */
    public void tableChanged(TableModelEvent e) {
        if (internalDebug)
            XHIBITConstant.debug("Sorter: tableChanged");
        reallocateIndexes();
        // xmodel.fireTableChanged(e);
        super.fireTableChanged(e);
    }

    /***************************************************************************
     * Implementation of interface methods *
     * ************************************* Performs pre-processing as required
     * before passing on to underlying model to handle request
     */

    public Object getValueAt(int aRow, int aColumn) {
        checkModel();
        try {
            return xmodel.getValueAt(indexes[aRow], aColumn);
        } catch (Exception e) {
            /**
             * @todo Throw an error that tells the user to reload the screen cos
             *       the data has gone screwy
             */
            // Create CSRecoverable
            // CSRecoverableException csr = new
            // CSRecoverableException(message, new String[] {}, "Sortable
            // table model and underlying table model have gone out of
            // sync", e);
            // Handle in XHIBITConstant
            // XHIBITConstant.handleError(csr);
            return "";
        }
    }

    public Object getDataAt(int row) {
        checkModel();
        return xmodel.getDataAt(indexes[row]);
    }

    public Class getColumnClass(int columnIndex) {
        return xmodel.getColumnClass(columnIndex);
    }

    public int getRowCount() {
        return xmodel.getRowCount();
    }

    public int getColumnCount() {
        return xmodel.getColumnCount();
    }

    public String getColumnName(int col) {
        return xmodel.getColumnName(col);
    }

    public String[] getColumnNames() {
        return xmodel.getColumnNames();
    }

    public Object[] getLongValues() {
        return xmodel.getLongValues();
    }

    // public Vector getData() {
    // return xmodel.getData();
    // }

    public void setValueAt(Object aValue, int aRow, int aColumn) {
        checkModel();
        xmodel.setValueAt(aValue, indexes[aRow], aColumn);
    }

    public void setLongValues(Object[] x) {
        xmodel.setLongValues(x);
    }

    public void setColumnNames(String[] x) {
        xmodel.setColumnNames(x);
    }

    public void setData(java.util.Collection x) {
        xmodel.setData(x);
        tableChanged(new TableModelEvent(this));
    }

    public void setData(Object[] x) {
        xmodel.setData(x);
        tableChanged(new TableModelEvent(this));
    }

    // *************************
    // * Sort specific methods *
    // *************************
    private int compareRowsByColumn(int row1, int row2, int column) {
        Class type = xmodel.getColumnClass(column);

        // Retrieve data from model
        Object o1 = xmodel.getValueAt(row1, column);
        Object o2 = xmodel.getValueAt(row2, column);

        // If both values are null, return 0.
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) { // Define null less than everything.
            return -1;
        } else if (o2 == null) {
            return 1;
        }

        /*
         * We copy all returned values from the getValue call in case an
         * optimised model is reusing one object to return many values. The
         * Number subclasses in the JDK are immutable and so will not be used in
         * this way but other subclasses of Number might want to do this to save
         * space and avoid unnecessary heap allocation.
         */

        if (o1 instanceof Comparable) {
            Comparable s1 = (Comparable) o1;
            Comparable s2 = (Comparable) o2;

            int result;
            if (s1 instanceof String) {
                result = ((String) s1).compareToIgnoreCase((String) s2);
            } else {
                result = s1.compareTo(s2);
            }

            if (result < 0) {
                return -1;
            } else if (result > 0) {
                return 1;
            } else {
                return 0;
            }
        } else if (type == Boolean.class) {
            Boolean bool1 = (Boolean) o1;
            boolean b1 = bool1.booleanValue();
            Boolean bool2 = (Boolean) o2;
            boolean b2 = bool2.booleanValue();

            if (b1 == b2) {
                return 0;
            } else if (b1) { // Define false < true
                return 1;
            } else {
                return -1;
            }
        } else if (type.getSuperclass() == java.lang.Number.class) {
            // This code should never be executed as all current
            // implementing
            // classes of Number implement Comparable
            Number n1 = (Number) o1;
            double d1 = n1.doubleValue();
            Number n2 = (Number) o2;
            double d2 = n2.doubleValue();

            if (d1 < d2) {
                return -1;
            } else if (d1 > d2) {
                return 1;
            } else {
                return 0;
            }
        } else {
            String s1 = o1.toString();
            String s2 = o2.toString();
            int result = s1.compareToIgnoreCase(s2);

            if (result < 0) {
                return -1;
            } else if (result > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private int compare(int row1, int row2) {
        compares++;
        for (int level = 0; level < sortingColumns.size(); level++) {
            Integer column = (Integer) sortingColumns.elementAt(level);
            int result = compareRowsByColumn(row1, row2, column.intValue());
            if (result != 0)
                return ascending ? result : -result;
        }
        return 0;
    }

    /**
     * This methods is called for logging only. If the length of the data is not
     * the same as the length in the index array, then the underlying data has
     * been changed without notifying this class, therefore the sort will no
     * longer function
     */
    public void checkModel() {
        if (indexes.length != xmodel.getRowCount()) {
            XHIBITConstant.error("Sorter not informed of a change in model.");
        }
    }

    public void sort(Object sender) {
        checkModel();
        compares = 0;
        // n2sort();
        shuttlesort((int[]) indexes.clone(), indexes, 0, indexes.length);
        if (internalDebug)
            XHIBITConstant.debug(this.getClass().getName() + ": Compares: " + compares);
    }

    /**
     * An implementation of a sort algorithm. Not currently used but available
     * if required
     */
    private void n2sort() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = i + 1; j < getRowCount(); j++) {
                if (compare(indexes[i], indexes[j]) == -1)
                    swap(i, j);
            }
        }
    }

    // This is a home-grown implementation which we have not had time
    // to research - it may perform poorly in some circumstances. It
    // requires twice the space of an in-place algorithm and makes
    // NlogN assigments shuttling the values between the two
    // arrays. The number of compares appears to vary between N-1 and
    // NlogN depending on the initial order but the main reason for
    // using it here is that, unlike qsort, it is stable.
    private void shuttlesort(int from[], int to[], int low, int high) {
        if (high - low < 2) {
            return;
        }
        int middle = (low + high) / 2;
        shuttlesort(to, from, low, middle);
        shuttlesort(to, from, middle, high);

        int p = low;
        int q = middle;

        /*
         * This is an optional short-cut; at each recursive call, check to see
         * if the elements in this subset are already ordered. If so, no further
         * comparisons are needed; the sub-array can just be copied. The array
         * must be copied rather than assigned otherwise sister calls in the
         * recursion might get out of sinc. When the number of elements is three
         * they are partitioned so that the first set, [low, mid), has one
         * element and and the second, [mid, high), has two. We skip the
         * optimisation when the number of elements is three or less as the
         * first compare in the normal merge will produce the same sequence of
         * steps. This optimisation seems to be worthwhile for partially ordered
         * lists but some analysis is needed to find out how the performance
         * drops to Nlog(N) as the initial order diminishes - it may drop very
         * quickly.
         */

        if (high - low >= 4 && compare(from[middle - 1], from[middle]) <= 0) {
            for (int i = low; i < high; i++) {
                to[i] = from[i];
            }
            return;
        }

        // A normal merge.

        for (int i = low; i < high; i++) {
            if (q >= high || (p < middle && compare(from[p], from[q]) <= 0)) {
                to[i] = from[p++];
            } else {
                to[i] = from[q++];
            }
        }
    }

    private void swap(int i, int j) {
        int tmp = indexes[i];
        indexes[i] = indexes[j];
        indexes[j] = tmp;
    }

    public void sortByColumn(int column) {
        sortByColumn(column, true);
    }

    public void sortByColumn(int column, boolean ascending) {
        this.ascending = ascending;
        sortingColumns.removeAllElements();
        sortingColumns.addElement(new Integer(column));
        sort(this);
        xmodel.fireTableChanged(new TableModelEvent(this));
    }

}