package uk.gov.courtservice.xhibit.client.util.table.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
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
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class XDefaultComboBoxCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
    private JComboBox comboBox;

    private JPanel rendererPanel;

    private JPanel spacerPanel = new JPanel();

    public XDefaultComboBoxCellRenderer() {
        super();
        this.comboBox = new JComboBox();
        this.rendererPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbConstraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0);
        rendererPanel.setOpaque(true);
        spacerPanel.setOpaque(true);
        this.rendererPanel.add(this.comboBox, gbConstraints);
        this.rendererPanel.add(spacerPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        comboBox.removeAllItems();
        comboBox.addItem(value == null ? " " : value);

        this.rendererPanel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        this.spacerPanel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

        if (hasFocus) {
            rendererPanel.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            if (table.isCellEditable(row, column)) {
                rendererPanel.setForeground(UIManager.getColor("Table.focusCellForeground"));
                rendererPanel.setBackground(UIManager.getColor("Table.focusCellBackground"));
                spacerPanel.setForeground(UIManager.getColor("Table.focusCellForeground"));
                spacerPanel.setBackground(UIManager.getColor("Table.focusCellBackground"));
            }
        } else {
            rendererPanel.setBorder(noFocusBorder);
        }

        // ---- begin optimization to avoid painting background ----
        Color back = this.rendererPanel.getBackground();
        boolean colorMatch = (back != null) && (back.equals(table.getBackground())) && table.isOpaque();
        setOpaque(!colorMatch);
        // ---- end optimization to aviod painting background ----

        return this.rendererPanel;
    }
}