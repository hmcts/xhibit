package uk.gov.courtservice.xhibit.client.results.verdicts;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JTable;

import uk.gov.courtservice.xhibit.client.util.table.renderers.XDefaultComboBoxCellRenderer;

/**
 * <p>
 * Title: Combo Box renderer that is only displayed when the cell is edittable
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
 * @version $Id: OptionalComboBoxCellRenderer.java,v 1.1 2004/06/09 08:57:33
 *          sz0t7n Exp $
 */

public class OptionalComboBoxCellRenderer extends XDefaultComboBoxCellRenderer {

    public OptionalComboBoxCellRenderer() {
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setComponentAndChildrenVisibility(c, table.isCellEditable(row, column));
        return c;
    }

    private void setComponentAndChildrenVisibility(Component c, boolean visible) {
        c.setVisible(visible);
        if (c instanceof Container) {
            Component[] components = ((Container) c).getComponents();
            for (int i = 0; i < components.length; i++) {
                setComponentAndChildrenVisibility(components[i], visible);
            }
        }
    }
}