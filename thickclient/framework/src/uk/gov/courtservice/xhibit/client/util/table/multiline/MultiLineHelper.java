package uk.gov.courtservice.xhibit.client.util.table.multiline;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JTable;

/**
 * <p>
 * Title: MultiLineHelper
 * </p>
 * <p>
 * Description: This helper is to be used by any renderer that wishes to make
 * the row in the table re-size itself
 * </p>
 * <p>
 * Get the instance of the helper from your instance of XTable using
 * <code>getMultiLineHelper()</code>. In your renderer at the end of the
 * getTableCellRendererComponent method make a call to <code>doRowHeights</code>.
 * This will trigger the resize of the row height if required.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class MultiLineHelper {
    // as a kludge to get resizing correct, we will record the preferred
    // heights
    // of all multicell renderers to a vectors...
    private Map rows = Collections.synchronizedMap(new HashMap());

    JTable _table;

    public MultiLineHelper(JTable table) {
        _table = table;
    }

    /**
     * This stores the height of the cell (identified by the row/column
     * combination) into a map, which is later used to establish the maximum
     * height required by the row. If the row height is different to the current
     * row height, it is changed.
     * 
     * @param row
     * @param column
     * @param newCellHeight
     */
    public void doRowHeights(int row, int column, int newCellHeight) {
        Integer iRow = new Integer(row);
        Map cells = (Map) rows.get(iRow);
        if (cells == null) {
            cells = Collections.synchronizedMap(new HashMap());
            rows.put(iRow, cells);
        }
        cells.put(new Integer(column), new Integer(newCellHeight));
        int newRowHeight = getMaximumHeight(cells);

        if (newRowHeight > 0 && newRowHeight != _table.getRowHeight(row)) {
            _table.setRowHeight(row, newRowHeight);
        }
    }

    /**
     * For the collection of cells passed in, the maximum row height, which is
     * the maximum value of the name value pairs, is returned
     * 
     * @param cells
     * @return maximum value from the collection.
     */
    private int getMaximumHeight(Map cells) {
        // System.out.println("getting maximum height");
        int maximumHeight = 0;
        for (Iterator iter = cells.values().iterator(); iter.hasNext();) {
            int cellHeight = ((Integer) iter.next()).intValue();
            maximumHeight = Math.max(cellHeight, maximumHeight);
        }
        return maximumHeight;
    }
}