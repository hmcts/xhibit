package uk.gov.courtservice.xhibit.client.admin.security.rolemapping;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class StripedTableCellRenderer implements TableCellRenderer {
    protected static TableModel tableModel;

    protected TableCellRenderer targetRenderer;

    protected Color background;

    protected Color foreground;

    protected Color disabledBackground;

    protected Color disabledForeground;

    public StripedTableCellRenderer(TableCellRenderer targetRenderer, Color background, Color foreground,
            Color disabledBackground, Color disabledForeground) {
        this.targetRenderer = targetRenderer;
        this.background = background;
        this.foreground = foreground;
        this.disabledBackground = disabledBackground;
        this.disabledForeground = disabledForeground;
    }

    // Implementation of TableCellRenderer interface
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        TableCellRenderer renderer = targetRenderer;
        if (renderer == null) {
            // Get default renderer from the table
            renderer = table.getDefaultRenderer(table.getColumnClass(column));
        }

        // Let the real renderer create the component
        Component comp = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Now apply the stripe effect
        if (isSelected == false && hasFocus == false) {
            if (tableModel.isCellEditable(row, 1)) {
                comp.setBackground(table.getBackground());
                comp.setForeground(table.getForeground());
            } else {
                comp.setBackground(disabledBackground != null ? disabledBackground : table.getBackground());
                comp.setForeground(disabledForeground != null ? disabledForeground : table.getForeground());
            }
        }

        return comp;
    }

    // Convenience method to apply this renderer to single column
    public static void installInColumn(JTable table, int columnIndex, Color background, Color foreground,
            Color disabledBackground, Color disabledForeground) {
        TableColumn tc = table.getColumnModel().getColumn(columnIndex);

        // Get the cell renderer for this column, if any
        TableCellRenderer targetRenderer = tc.getCellRenderer();

        // Create a new StripedTableCellRenderer and install it
        tc.setCellRenderer(new StripedTableCellRenderer(targetRenderer, background, foreground, disabledBackground,
                disabledForeground));
    }

    // Convenience method to apply this renderer to an entire table
    public static void installInTable(JTable table, Color background, Color foreground, Color disabledBackground,
            Color disabledForeground) {
        StripedTableCellRenderer sharedInstance = null;
        tableModel = table.getModel();
        table.setOpaque(false);
        int columns = table.getColumnCount();
        for (int i = 0; i < columns; i++) {
            TableColumn tc = table.getColumnModel().getColumn(i);
            TableCellRenderer targetRenderer = tc.getCellRenderer();
            if (targetRenderer != null) {
                System.err.println("column = " + i + " has a renderer - " + targetRenderer.getClass().getName());
                // This column has a specific renderer
                tc.setCellRenderer(new StripedTableCellRenderer(targetRenderer, background, foreground,
                        disabledBackground, disabledForeground));
            } else {
                // This column uses a class renderer - use a shared renderer
                if (sharedInstance == null) {
                    sharedInstance = new StripedTableCellRenderer(null, background, foreground, disabledBackground,
                            disabledForeground);
                }
                tc.setCellRenderer(sharedInstance);
            }
        }
    }
}
