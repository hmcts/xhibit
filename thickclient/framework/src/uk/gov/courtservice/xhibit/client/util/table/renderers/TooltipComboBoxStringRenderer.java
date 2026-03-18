package uk.gov.courtservice.xhibit.client.util.table.renderers;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 * <p>
 * Title: Tooltip Renderer for Combo boxes
 * </p>
 * <p>
 * Description: This is similar to the tooltip combo bx renderer with the minor
 * difference that the combo box must be populated with strings or the tool tip
 * displayed will be a the toString representation of the object!
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: TooltipComboBoxStringRenderer.java,v 1.1 2004/06/07 16:53:44
 *          sz0t7n Exp $
 */

public class TooltipComboBoxStringRenderer extends BasicComboBoxRenderer {

    public TooltipComboBoxStringRenderer() {
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        if (isSelected) {
            list.setToolTipText(value.toString());
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
