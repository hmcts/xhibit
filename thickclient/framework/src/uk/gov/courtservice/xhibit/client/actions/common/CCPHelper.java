package uk.gov.courtservice.xhibit.client.actions.common;

import java.util.ArrayList;

import javax.swing.JTable;

import uk.gov.courtservice.xhibit.client.util.table.style.StyleTableCellValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class CCPHelper {

    private CCPHelper() {
    }

    /**
     * Returns the contents of the selected cell in a JTable as a String
     * 
     * @param jTable
     * @return The text contents of the cell.
     */
    public static String getCellContents(JTable jTable) {
        StringBuffer sbf = new StringBuffer();

        int numcols = jTable.getSelectedColumnCount();
        int numrows = jTable.getSelectedRowCount();
        int[] rowsselected = jTable.getSelectedRows();
        int[] colsselected = jTable.getSelectedColumns();
        //
        // int numrows = jTable.getSelectedRowCount();
        // int[] rowsselected = jTable.getSelectedRows();
        //
        // int numcols = 0;
        // int[] colsselected = jTable.getSelectedColumns();
        // if (jTable.getColumnSelectionAllowed()) {
        // numcols = jTable.getSelectedColumnCount();
        // } else {
        // numcols = jTable.getColumnCount();
        // colsselected = new int[numcols];
        // for (int i = 0; i < numcols; i++)
        // {
        // colsselected[i] = i;
        // }
        // }

        for (int i = 0; i < numrows; i++) {
            boolean gotRowText = false;
            for (int j = 0; j < numcols; j++) {
                boolean gotColText = false;
                if (jTable.getValueAt(rowsselected[i], colsselected[j]) instanceof ArrayList) {
                    ArrayList list = (ArrayList) jTable.getValueAt(rowsselected[i], colsselected[j]);
                    for (int k = 0; k < list.size(); k++) {
                        if (list.get(k) instanceof StyleTableCellValue) {
                            StyleTableCellValue styleValue = (StyleTableCellValue) list.get(k);
                            if (styleValue.getText().length() > 0) {
                                sbf.append(styleValue.getText());
                                gotRowText = true;
                                gotColText = true;
                            }
                        }
                    }
                } else {
                    if (jTable.getValueAt(rowsselected[i], colsselected[j]) != null
                            && jTable.getValueAt(rowsselected[i], colsselected[j]).toString().length() > 0) {
                        sbf.append(jTable.getValueAt(rowsselected[i], colsselected[j]));
                        gotRowText = true;
                        gotColText = true;
                    }
                }
                if (j < numcols - 1 && gotColText)
                    sbf.append("\t");
            }
            if (gotRowText && i != numrows - 1)
                sbf.append("\n");
        }

        return sbf.toString();
    }

    public static boolean isSelectionEdittable(JTable jTable) {
        int tableRow = jTable.getSelectedRow();
        int tableCol = jTable.getSelectedColumn();

        if (tableRow < 0 || tableCol < 0) {
            return false;
        }

        int numcols = jTable.getSelectedColumnCount();
        int numrows = jTable.getSelectedRowCount();
        int[] rowsselected = jTable.getSelectedRows();
        int[] colsselected = jTable.getSelectedColumns();
        //
        // int numrows = jTable.getSelectedRowCount();
        // int[] rowsselected = jTable.getSelectedRows();
        //
        // int numcols = 0;
        // int[] colsselected = jTable.getSelectedColumns();
        // if (jTable.getColumnSelectionAllowed()) {
        // numcols = jTable.getSelectedColumnCount();
        // } else {
        // numcols = jTable.getColumnCount();
        // colsselected = new int[numcols];
        // for (int i = 0; i < numcols; i++)
        // {
        // colsselected[i] = i;
        // }
        // }

        for (int i = 0; i < numrows; i++) {
            for (int j = 0; j < numcols; j++) {
                if (!jTable.isCellEditable(rowsselected[i], colsselected[j])
                        || !jTable.getColumnClass(colsselected[j]).isInstance(new String())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void setSelectionValue(Object newValue, JTable jTable) {
        int numcols = jTable.getSelectedColumnCount();
        int numrows = jTable.getSelectedRowCount();
        int[] rowsselected = jTable.getSelectedRows();
        int[] colsselected = jTable.getSelectedColumns();
        //
        // int numrows = jTable.getSelectedRowCount();
        // int[] rowsselected = jTable.getSelectedRows();
        //
        // int numcols = 0;
        // int[] colsselected = jTable.getSelectedColumns();
        // if (jTable.getColumnSelectionAllowed()) {
        // numcols = jTable.getSelectedColumnCount();
        // } else {
        // numcols = jTable.getColumnCount();
        // colsselected = new int[numcols];
        // for (int i = 0; i < numcols; i++)
        // {
        // colsselected[i] = i;
        // }
        // }

        for (int i = 0; i < numrows; i++) {
            for (int j = 0; j < numcols; j++) {
                if (jTable.isCellEditable(rowsselected[i], colsselected[j])
                        && jTable.getColumnClass(colsselected[j]).isInstance(new String())) {
                    // jTable.editCellAt(rowsselected[i], colsselected[j]);
                    jTable.setValueAt(newValue, rowsselected[i], colsselected[j]);
                    // jTable.getColumnModel().getColumn(colsselected[j]).getCellEditor().stopCellEditing();
                }
            }
        }
    }

}