package uk.gov.courtservice.xhibit.client.publicdisplayconfig.tablemodels;

import java.util.ArrayList;

import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetDDComplexValue;

/**
 * <p>
 * Title: XHIBIT 2 - Public Display
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: SelectListTableModel.java,v 1.8 2006/06/05 12:32:08 bzjrnl Exp $
 */

public class SelectListTableModel extends XHIBITDefaultTableModel {
    public static final int ORDER_COL = 0;

    public static final int DELAY_COL = 2;

    private static final String TABLECOL_HIDDEN = "HiddenSort";

    private static final String TABLECOL_KEY1 = "pd.tablecol.list";

    private static final String TABLECOL_KEY2 = "pd.tablecol.delay";

    private ArrayList _data;

    public SelectListTableModel(ArrayList data) {
        _data = data;

        setColumnNames(new String[] { TABLECOL_HIDDEN, PublicDisplayUtils.getResource(TABLECOL_KEY1),
                PublicDisplayUtils.getResource(TABLECOL_KEY2) });

        setLongValues(new Object[] { "Jury Current Status", "999" });
        sort();
    }

    /**
     * Get the RotationSetComplexValue for the selected row in the table model
     * passed in and extracts the description
     * 
     * @param rowIndex
     * @param columnIndex
     * @return String for document name
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        RotationSetDDComplexValue list = (RotationSetDDComplexValue) getDataAt(rowIndex);

        switch (columnIndex) {
        case 0:
            return list.getRotationSetDDBasicValue().getOrdering();
        case 1:
            String documentName = PublicDisplayUtils.getResource(PublicDisplayUtils.PRE_DISPLAYDOCUMENT.concat(
                    list.getDisplayDocumentBasicValue().getDescriptionCode()).concat(
                    nulltoString(list.getDisplayDocumentBasicValue().getLanguage())).concat(
                    nulltoString(list.getDisplayDocumentBasicValue().getCountry())));
            return documentName;
        case 2:
            return list.getRotationSetDDBasicValue().getPageDelay();
        default:
            return "";
        }
    }

    private String nulltoString(Object value) {
        return value != null ? (String) value : "";
    }

    /**
     * @return The number of display documents for the selected rotation set
     */
    public int getRowCount() {
        return _data.size();
    }

    /**
     * Return a VO for required position
     * 
     * @param row
     *            required item from array
     * @return RotationSetDDComplexValue
     */
    public Object getDataAt(int row) {
        return _data.get(getIndexedRow(row));
    }

    public void setValueAt(Object data, int rowIndex, int columnIndex) {
        RotationSetDDComplexValue list = (RotationSetDDComplexValue) getDataAt(rowIndex);
        if (columnIndex == DELAY_COL) {
            Integer delay = new Integer((String) data);
            list.getRotationSetDDBasicValue().setPageDelay(delay);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void addElement(RotationSetDDComplexValue newObject) {
        _data.add(newObject);
        sort();
        fireTableDataChanged();
    }

    public void removeElement(RotationSetDDComplexValue oldObject) {
        if (_data.contains(oldObject)) {
            int pos = _data.indexOf(oldObject);
            _data.remove(pos);
            sort();
            fireTableRowsDeleted(pos, pos);
        }
    }

    public void removeAllElements() {
        int lastPos = getRowCount() - 1;
        _data.clear();
        sort();
        fireTableRowsDeleted(0, lastPos);
    }

    public Object[] getData() {
        return _data.toArray();
    }

    public boolean isCellEditable(int row, int col) {
        // return (col == DELAY_COL || col == ORDER_COL);
        return (col == DELAY_COL);
    }

    // /////////// Internal Sorting
    private int[] indexes;

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
    public void sort() {
        int rowCount = this.getRowCount();
        indexes = new int[rowCount];
        for (int row = 0; row < rowCount; row++)
            indexes[row] = row;
        // re-sort the array based on selected sort criteria

        shuttlesort((int[]) indexes.clone(), indexes, 0, indexes.length);

        fireTableDataChanged();
    }

    // This code is knicked from XSortableTableModel
    private void shuttlesort(int from[], int to[], int low, int high) {
        if (high - low < 2) {
            return;
        }
        int middle = (low + high) / 2;
        shuttlesort(to, from, low, middle);
        shuttlesort(to, from, middle, high);

        int p = low;
        int q = middle;

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

    // private int[] sortingColumns = new int[]{0};
    private boolean ascending = true;

    private int compare(int row1, int row2) {
        int result = compareRowsByColumn(row1, row2);
        if (result != 0)
            return ascending ? result : -result;

        return 0;
    }

    // *************************
    // * Sort specific methods *
    // *************************
    private int compareRowsByColumn(int row1, int row2) {
        // Retrieve data from model
        Integer o1 = ((RotationSetDDComplexValue) _data.get(row1)).getRotationSetDDBasicValue().getOrdering();
        Integer o2 = ((RotationSetDDComplexValue) _data.get(row2)).getRotationSetDDBasicValue().getOrdering();

        // If both values are null, return 0.
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) { // Define null less than everything.
            return -1;
        } else if (o2 == null) {
            return 1;
        }

        return o1.compareTo(o2);
    }

}