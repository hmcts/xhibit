package uk.gov.courtservice.xhibit.client.publicdisplayconfig.tablemodels;

import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: XHIBIT 2 - Public Display
 * </p>
 * <p>
 * Description: Superclass Model to be used tables that are effected by an item
 * being selected in another table or list.
 * </p>
 * <p>
 * getValueAt should be implemented based on getting the relevant object from a
 * collection/other table model based on the selected row.
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: AbstractSelectRowTableModel.java,v 1.2 2004/01/15 16:57:48
 *          sz0t7n Exp $
 */

abstract public class AbstractSelectRowTableModel extends XHIBITDefaultTableModel {
    private int _selectedRow = -1;

    public void setSelectedRow(int selectedRow) {
        _selectedRow = selectedRow;
        this.fireTableDataChanged();
    }

    public int getSelectedRow() {
        return _selectedRow;
    }
}