package uk.gov.courtservice.xhibit.client.crestformsbf.swing.table;

import javax.swing.table.TableModel;

/**
 * Classes implementing this interface implement a stratergy for selecting rows
 * bassed on a key press in a given column
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
public interface TableKeySelectionManager {
    /**
     * Implements a stratergy for row selection
     * 
     * @param aKey
     *            the key pressed
     * @param column
     *            the column the key was pressed in
     * @param model
     *            the table model with the data in
     * @return the row to select
     */
    public int selectionForKey(char aKey, int column, TableModel model);
}
