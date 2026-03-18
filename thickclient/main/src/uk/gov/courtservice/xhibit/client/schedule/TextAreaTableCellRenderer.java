package uk.gov.courtservice.xhibit.client.schedule;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 * <p>
 * Title: XHIBIT 2 -
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
 * @author Simon Gilmore
 * @version 1.0
 */

public class TextAreaTableCellRenderer implements TableCellRenderer {

    private JTextArea textArea = new JTextArea();

    /**
     * 
     */
    public TextAreaTableCellRenderer() {
    }

    /**
     * 
     * @param table
     *            the JTable that is asking the renderer to draw; can be null
     * @param value
     *            the value of the cell to be rendered. It is up to the specific
     *            renderer to interpret and draw the value. For example, if
     *            value is the string "true", it could be rendered as a string
     *            or it could be rendered as a check box that is checked. null
     *            is a valid value
     * @param isSelected
     *            true if the cell is to be rendered with the selection
     *            highlighted; otherwise false
     * @param hasFocus
     *            if true, render cell appropriately. For example, put a special
     *            border on the cell, if the cell can be edited, render in the
     *            color used to indicate editing
     * @param row
     *            the row index of the cell being drawn. When drawing the
     *            header, the value of row is -1
     * @param column
     *            the column index of the cell being drawn
     * @return the component used for drawing the cell. This method is used to
     *         configure the renderer appropriately before drawing.
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(table.getFont());

        if (isSelected) {
            textArea.setBackground(table.getSelectionBackground());
            textArea.setForeground(table.getSelectionForeground());
        } else {
            textArea.setBackground(table.getBackground());
            textArea.setForeground(table.getForeground());
        }

        textArea.setText((String) value);

        return textArea;
    }

}