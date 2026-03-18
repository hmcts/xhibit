package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTable;

/**
 * <p>
 * Title: JuryTableFocusListener
 * </p>
 * <p>
 * Description: Jury Table is an ordered list. The first column is the index
 * number of an entry in the table and is readonly. This class ensures that when
 * the user tabs into the table that the focus is set to the secong column not
 * the fisrt
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
 * 53620 23-07-2003 AW Daley Initial version
 */
public class JuryTableFocusListener extends FocusAdapter {
    private static final int FIRST_EDITABLE_COL_NO = 1;

    private static final int FIRST_EDITABLE_ROW_NO = 0;

    public JuryTableFocusListener() {
        super();
    }

    public void focusGained(FocusEvent e) {
        // Perform default focus gained function
        super.focusGained(e);

        // Get source of event, the jury table
        JTable juryTable = (JTable) e.getSource();

        // Edit first editable cell in the table
        if (juryTable.getSelectedColumn() <= FIRST_EDITABLE_COL_NO) {
            juryTable.setColumnSelectionInterval(FIRST_EDITABLE_COL_NO, FIRST_EDITABLE_COL_NO);
        }
        if (juryTable.getSelectedRow() <= FIRST_EDITABLE_ROW_NO) {
            juryTable.setRowSelectionInterval(FIRST_EDITABLE_ROW_NO, FIRST_EDITABLE_ROW_NO);
        }
        // juryTable.editCellAt(FIRST_EDITABLE_ROW_NO, FIRST_EDITABLE_COL_NO);
    }

}