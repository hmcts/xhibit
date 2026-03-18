package uk.gov.courtservice.xhibit.client.util;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

/**
 * <p>
 * Title: Right Justifies Text displayed in a Combo Box
 * </p>
 * <p>
 * Description: Right Justifies Text displayed in a Combo Box< To Use: Create
 * instance and set to combo eg. ComboBoxRendererRightJustify renderer= new
 * ComboBoxRendererRightJustify(); <combo box>.setRenderer(renderer); /p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 * 
 */

public class ComboBoxRendererRightJustify extends JLabel implements ListCellRenderer {
    /**
     * <init>
     */
    public ComboBoxRendererRightJustify() {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    /**
     * getListCellRendererComponent
     * 
     * @param list
     *            parameter for getListCellRendererComponent
     * @param value
     *            parameter for getListCellRendererComponent
     * @param index
     *            parameter for getListCellRendererComponent
     * @param isSelected
     *            parameter for getListCellRendererComponent
     * @param cellHasFocus
     *            parameter for getListCellRendererComponent
     * @return the returned Component
     */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());

        }

        setHorizontalAlignment(JTextField.RIGHT);
        if (list.getSelectedValue() != null) {
            setText(value.toString());
        }
        return this;
    }
}