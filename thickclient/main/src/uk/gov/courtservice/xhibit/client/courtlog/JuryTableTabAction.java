package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTable;

/**
 * <p>
 * Title: JuryTableTabAction
 * </p>
 * <p>
 * Description: Jury Table is an ordered list. The first column is the index
 * number of an entry in the table and is readonly. This class ensures that this
 * column in the table cannot be moved into using tab or shift tab
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author AW Daley
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 52368 21-03-2003 AW Daley Initial version
 */
public class JuryTableTabAction extends AbstractAction {
    private final int FIRST_EDITABLE_COL_NO = 1;

    private final int FIRST_EDITABLE_ROW_NO = 0;

    private boolean forwards;

    public JuryTableTabAction(boolean forwards) {
        // Determines if navigation required is forward or backward tab
        // action
        this.forwards = forwards;
    }

    public void actionPerformed(ActionEvent e) {
        // Gets number of columns and rows in table
        JTable juryTable = (JTable) e.getSource();
        int lastColumnNo = juryTable.getColumnCount() - 1;
        int lastRowNo = juryTable.getRowCount() - 1;

        if (forwards)
            // Performs tab action
            handleNavigation(juryTable, FIRST_EDITABLE_COL_NO, FIRST_EDITABLE_ROW_NO, lastColumnNo, lastRowNo, 1);
        else
            // Performs shift tab action
            handleNavigation(juryTable, lastColumnNo, lastRowNo, FIRST_EDITABLE_COL_NO, FIRST_EDITABLE_ROW_NO, -1);
    }

    /**
     * Handles navigation within the table if tab or shift tab is pressed.
     * 
     * @param table
     *            Jury table
     * @param firstColNo
     *            first editable column in the table
     * @param firstRowNo
     *            first editable row in the table
     * @param lastColNo
     *            last editable column in the table
     * @param lastRowNo
     *            last editable row in the table
     * @param step
     *            direction of tabbing 1 - forwards, -1 - backwards
     * @return void
     * @throws None
     */
    private void handleNavigation(JTable table, int firstColNo, int firstRowNo, int lastColNo, int lastRowNo, int step) {
        if (table.isEditing())
            table.getCellEditor().stopCellEditing();

        // Gets current table cell that has focus
        int currentCol = table.getSelectedColumn();
        int currentRow = table.getSelectedRow();

        int nextRow = currentRow;
        int nextCol = currentCol;

        if (currentCol == lastColNo) {
            // Handles end/start of row condition
            nextCol = firstColNo;
            if (currentRow == lastRowNo)
                nextRow = firstRowNo;
            else
                nextRow = currentRow + step;
        } else
            nextCol = currentCol + step;

        if (currentRow == -1)
            nextRow = FIRST_EDITABLE_ROW_NO;
        if (currentCol == -1)
            nextCol = FIRST_EDITABLE_COL_NO;

        if (table.isEditing()) {
            table.getDefaultEditor(String.class).stopCellEditing();
        }
        table.setColumnSelectionInterval(nextCol, nextCol);
        table.setRowSelectionInterval(nextRow, nextRow);
    }
}
