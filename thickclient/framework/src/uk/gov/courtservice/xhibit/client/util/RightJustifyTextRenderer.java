package uk.gov.courtservice.xhibit.client.util;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * <p>
 * Title: RightJustifyTextRenderer is used for table cells which require right
 * jusification
 * </p>
 * <p>
 * Description: A JLabel displaying the entered text with right justification is
 * displayed
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */

public class RightJustifyTextRenderer extends DefaultTableCellRenderer // JLabel
// implements
// TableCellRenderer
{
    public RightJustifyTextRenderer() {
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component thisCell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (thisCell instanceof JLabel) {
            ((JLabel) thisCell).setHorizontalAlignment(JLabel.RIGHT);
        }
        return thisCell;
    }
}