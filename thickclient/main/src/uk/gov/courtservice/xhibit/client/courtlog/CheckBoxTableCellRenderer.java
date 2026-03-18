package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class CheckBoxTableCellRenderer implements TableCellRenderer {
    private JCheckBox checkBox = new JCheckBox();

    /** The foreground color of a cell when it is unselected. */
    private Color unselectedForeground;

    /** The backgruond color of a cell when it is unselected. */
    private Color unselectedBackground;

    public CheckBoxTableCellRenderer() {
        super();
        checkBox.setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        if (isSelected) {
            checkBox.setForeground(table.getSelectionForeground());
            checkBox.setBackground(table.getSelectionBackground());

        } else {
            checkBox.setForeground((unselectedForeground != null) ? unselectedForeground : table.getForeground());
            checkBox.setBackground((unselectedBackground != null) ? unselectedBackground : table.getBackground());
        }

        if (value instanceof Boolean) {
            checkBox.setSelected(((Boolean) value).booleanValue());
            checkBox.setEnabled(table.isCellEditable(row, column));
        }
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        checkBox.setVerticalAlignment(SwingConstants.CENTER);

        return checkBox;
    }

    /*
     * public void setForeground(JTable t, Color c) { checkBox.setForeground(c);
     * unselectedForeground = c; }
     * 
     * public void setBackground(Color c) { checkBox.setBackground(c);
     * unselectedBackground = c; }
     * 
     * public JCheckBox getCheckBox() { return checkBox; }
     * 
     * public void setCheckBox(JCheckBox c) { checkBox = c; }
     */
}