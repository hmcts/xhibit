package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @deprecated
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
 * @author Sherie De Silva
 * @version 1.0
 */

public class ComboBoxTableCellRenderer implements TableCellRenderer {
    private JComboBox comboBox;

    private JPanel rendererPanel;

    private JPanel spacerPanel = new JPanel();

    private HearingRecordModel model;

    public ComboBoxTableCellRenderer(HearingRecordModel model) {
        super();
        this.comboBox = new JComboBox();
        // this.comboBox.setPreferredSize(new Dimension(10, 10));
        // this.comboBox.setMinimumSize(new Dimension(10, 10));
        this.model = model;
        this.rendererPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbConstraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 4, 2, 4), 0, 0);
        spacerPanel.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent fe) {
            }

            public void focusGained(FocusEvent fe) {
                comboBox.requestFocus();
            }
        });
        this.rendererPanel.add(this.comboBox, gbConstraints);
        this.rendererPanel.add(spacerPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug ("setting
        // preferred size onto combo box");
        // comboBox.setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        comboBox.removeAllItems();
        comboBox.addItem(value == null ? " " : value);

        this.rendererPanel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        this.spacerPanel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        return this.rendererPanel;

    }
}