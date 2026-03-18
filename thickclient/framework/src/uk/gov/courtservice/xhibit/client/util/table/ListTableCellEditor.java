package uk.gov.courtservice.xhibit.client.util.table;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;

/**
 * <p>
 * Title: Table Cell Editor providing a single selection JList as the editing
 * component.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This component is intended to replace as much as possible the use of
 * JComboBox in tables where the JComboBox renderer produces 'outsize' drop down
 * buttons.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
public class ListTableCellEditor extends AbstractCellEditor implements TableCellEditor {
    // Provides the scroll bar.
    private JScrollPane view;

    // The list component.
    private JList list;

    /**
     * Simple constructor that takes an array of objects to act as the data in
     * the list.
     * <p>
     * It is recommended that the objects are either strings or provide a
     * toString() method that produces something that will look 'proper' in the
     * list.
     * 
     * @param refData
     *            The data to populate the list with.
     */
    public ListTableCellEditor(Object[] refData) {
        list = new JList(refData);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        view = new JScrollPane(list);
    }

    /**
     * Used to get the currently selected value.
     * 
     * @return the currently selected value.
     */
    public Object getCellEditorValue() {
        return list.getSelectedValue();
    }

    /**
     * This method is used to return the list component that will be used to
     * support editing of the table cell selected.
     * 
     * @param table
     *            The table that the editor will be used in.
     * @param value
     *            The current value of the cell in the table.
     * @param isSelected
     *            Currently ignored.
     * @param row
     *            The row of the cell in the table.
     * @param column
     *            The column of the cell in the table
     * @return The JList component wrapped in a JScrollPane to provide
     *         scrolling.
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        list.setSelectedValue(value, true);
        return view;
    }
}