package uk.gov.courtservice.xhibit.client.util.listeners;

import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTable;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Extension on PopupListener for tables Selects the row in the
 * table first before showing popup
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

public class TablePopupListener extends PopupListener {
    private JTable myTable;

    public TablePopupListener(JPopupMenu popup, JTable table) {
        super(popup);
        myTable = table;
    }

    protected void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            int row = myTable.rowAtPoint(e.getPoint());
            int[] selection = myTable.getSelectedRows();
            boolean rowSelected = false;
            // establish if the right click occurred on an already selected
            // row
            for (int i = 0; i < selection.length; i++) {
                if (row == selection[i]) {
                    rowSelected = true;
                    break;
                }
            }

            // If the right click was on an unselected row, select the row
            // in
            // where user clicked.
            if (!rowSelected)
                myTable.setRowSelectionInterval(row, row);

            super.maybeShowPopup(e);
        }
    }
}