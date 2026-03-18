package uk.gov.courtservice.xhibit.client.updatecase;

import javax.swing.table.AbstractTableModel;

/**
 * <p>
 * Title: Xhibit
 * </p>
 * <p>
 * Description: Xhibit 2 Client Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS UK Solutions Consulting
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 * 
 */

public class DefendantTableModel extends AbstractTableModel {
    private final boolean debug = true;

    final String[] columnNames = { "First Name", "Middle Names", "Last Name", "Defence Advocate(s)", "Sign In" };

    final Object[][] data = { { "Mary", "Campione", "Snowboarding", new Integer(5), new Boolean(false) },
            { "Alison", "Huml", "Rowing", new Integer(3), new Boolean(true) },
            { "Kathy", "Walrath", "Chasing toddlers", new Integer(2), new Boolean(false) },
            { "Mark", "Andrews", "Speed reading", new Integer(20), new Boolean(true) },
            { "Angela", "Lih", "Teaching high school", new Integer(4), new Boolean(false) } };

    public DefendantTableModel() {
    }

    public static void main(String[] args) {
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public int getRowCount() {
        return data.length;
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public void setValueAt(Object value, int row, int col) {
        if (debug) {
            uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("Setting value at " + row + "," + col + " to "
                    + value + " (an instance of " + value.getClass() + ")");
        }

        if (data[0][col] instanceof Integer && !(value instanceof Integer)) { // With
            // JFC/Swing
            // 1.1
            // and
            // JDK
            // 1.2,
            // we
            // need
            // to
            // create
            // an Integer from the value; otherwise, the column
            // switches to contain Strings. Starting with v 1.3,
            // the table automatically converts value to an Integer,
            // so you only need the code in the 'else' part of this
            // 'if' block.
            // XXX: See TableEditDemo.java for a better solution!!!
            try {
                data[row][col] = new Integer(value.toString());
                fireTableCellUpdated(row, col);
            } catch (NumberFormatException e) {
                uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("The \"" + getColumnName(col)
                        + "\" column accepts only integer vos.");
            }

        } else {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }

        if (debug) {
            uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("New value of data:");
            printDebugData();
        }
    }

    private void printDebugData() {
        int numRows = getRowCount();
        int numCols = getColumnCount();
        for (int i = 0; i < numRows; i++) {
            uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("    row " + i + ":");
            for (int j = 0; j < numCols; j++) {
                uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("  " + data[i][j]);
            }
        }
        uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("--------------------------");
    }

}