package uk.gov.courtservice.xhibit.client.util.table.renderers;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

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
 * @author Simon Gilmore
 * @editor Rakesh Lakhani
 * @version 1.0
 */

public class XDefaultComboBoxCellEditor extends DefaultCellEditor {
    protected static final Logger log = CSServices.getLogger(XDefaultComboBoxCellEditor.class);

    private JPanel jp = new JPanel();

    private JPanel spacerPanel = new JPanel();

    /**
     * Creates a XDefaultComboBoxCellEditor
     * 
     * @param combo
     *            the JComboBox to use as an editor.
     */
    public XDefaultComboBoxCellEditor(JComboBox combo) {
        super(combo);
        jp.setLayout(new GridBagLayout());
        jp.setOpaque(true);
        spacerPanel.setOpaque(true);
        jp.add(getEditor(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
        jp.add(spacerPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        // check that another renderer has not been added already
        // and that the combo has some items in it
        if (combo.getRenderer() instanceof BasicComboBoxRenderer && combo.getModel().getSize() > 0) {
            // // Create an array of tool tips
            // int tipSize = combo.getModel().getSize();
            // String[] tips = new String[tipSize];
            // for (int i=0; i<tipSize; i++)
            // {
            // tips[i] = combo.getModel().getElementAt(i).toString();
            // }
            // add a renderer that shows a tooltip for each row in the drop
            // down
            combo.setRenderer(new TooltipComboBoxStringRenderer());
        }
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        log.debug("[ComboBoxEditor] getTableCellEditorComponent: ");

        JComboBox myComboBox = (JComboBox) getEditor();

        value = table.getModel().getValueAt(row, column) == null ? "" : table.getModel().getValueAt(row, column);
        // If the value is an empty string then this item does not exist in the
        // combo box,
        // so just have the top item selected IF the combo box has more than 0
        // items.
        if ((value.toString().trim().equals("")) && (myComboBox.getItemCount() > 0)) {
            myComboBox.setSelectedIndex(0);
        } else {
            // If there is more than 1 item here then select the item that
            // was retrieved from
            // the table model.
            if (myComboBox.getItemCount() > 0) {
                myComboBox.setSelectedItem(value);
            }
        }

        setCellBackground(table, isSelected);

        return jp;
    }

    public void setCellBackground(JTable table, boolean isSelected) {
        this.jp.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground()); // Color.white
        this.spacerPanel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

        jp.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
        jp.setForeground(UIManager.getColor("Table.focusCellForeground"));
        jp.setBackground(UIManager.getColor("Table.focusCellBackground"));
        spacerPanel.setForeground(UIManager.getColor("Table.focusCellForeground"));
        spacerPanel.setBackground(UIManager.getColor("Table.focusCellBackground"));
    }

    public JPanel getPanel() {
        return jp;
    }

    protected JComponent getEditor() {
        return editorComponent;
    }
}