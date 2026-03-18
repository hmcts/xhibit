package uk.gov.courtservice.xhibit.client.courtlog.directions;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Shows defendant names
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

public class DefendantNameRenderer extends JLabel implements ListCellRenderer {

    public DefendantNameRenderer() {
        setOpaque(true);
        // this.setPreferredSize( new Dimension(250,
        // XHIBITConstant.getLineHeight()) );
        // this.setMaximumSize( new Dimension(500,
        // XHIBITConstant.getLineHeight()) );
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setHorizontalAlignment(LEFT);
        String txt = PDHConstants.buildDefendantName((DefendantBasicValue) value);
        this.setText(txt);
        if (isSelected)
            list.setToolTipText(txt);

        return this;
    }
}