package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;

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
 * @author unascribed
 * @version 1.0
 */

public class ComboBoxTableCellEditor extends DefaultCellEditor {
    private JComboBox myComboBox;

    private JPanel rendererPanel;

    private JPanel spacerPanel = new JPanel();

    public ComboBoxTableCellEditor(JComboBox comboBox) {
        super(comboBox);
        myComboBox = comboBox;
        this.rendererPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbConstraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(2, 4, 2, 4), 0, 0);
        spacerPanel.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent fe) {
            }

            public void focusGained(FocusEvent fe) {
                myComboBox.requestFocus();
            }
        });
        this.rendererPanel.add(super.editorComponent, gbConstraints);
        this.rendererPanel.add(spacerPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        value = table.getModel().getValueAt(row, column) == null ? "" : table.getModel().getValueAt(row, column);
        // If the value is an empty string then this item does not exist in the
        // combo box,
        // so just have the top item selected IF the combo box has more than 0
        // items.
        if ((value.toString().trim().equals("")) && (this.myComboBox.getItemCount() > 0)) {
            this.myComboBox.setSelectedIndex(0);
        } else {
            // If there is more than 1 item here then select the item that
            // was retrieved from
            // the table model.
            if (this.myComboBox.getItemCount() > 0) {
                this.myComboBox.setSelectedItem(value);
            }
        }
        this.rendererPanel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground()); // Color.white
        this.spacerPanel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        return this.rendererPanel;
    }

}