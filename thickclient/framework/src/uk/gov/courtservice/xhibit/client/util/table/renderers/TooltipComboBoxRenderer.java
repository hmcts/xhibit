package uk.gov.courtservice.xhibit.client.util.table.renderers;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Renderer to show tool tips on the drop down of a combo box.
 * </p>
 * Code coutesy of the Sun Developer Forum
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

public class TooltipComboBoxRenderer extends BasicComboBoxRenderer {
    String[] _tipList = null;

    public TooltipComboBoxRenderer(String[] tipList) {
        super();
        _tipList = tipList;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        if (isSelected) {
            if (index >= 0 && index < _tipList.length)
                list.setToolTipText(_tipList[index]);
            else if (index >= _tipList.length)
                list.setToolTipText("Not Specified ... ");
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}