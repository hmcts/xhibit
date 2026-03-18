package uk.gov.courtservice.xhibit.client.util;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import uk.gov.courtservice.xhibit.business.vos.entities.RefAppResultBasicValue;

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
 * @author Bal Bhamra
 * @version 1.0
 */

public class RefAppResultComboBoxRenderer extends JLabel implements ListCellRenderer {
    private boolean displayCode;

    /**
     * Select whether to display the code and the description in the drop down.
     * 
     * @param displayCode
     */
    public RefAppResultComboBoxRenderer(boolean displayCode) {
        this.displayCode = displayCode;
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    /**
     * Display "code - description" in the drop down
     */
    public RefAppResultComboBoxRenderer() {
        this(false);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("LOV:");
        // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("list :"
        // + (list==null?"NULL":list.toString()));
        // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("Value :"
        // + (value==null?"NULL":value.toString()));
        // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("index :"
        // + index);
        // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("isSel :"
        // + isSelected);
        // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("hasFoc:"
        // + isSelected);

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setHorizontalAlignment(LEFT);
        RefAppResultBasicValue refAppResultBasicValue = (RefAppResultBasicValue) value;
        String txt;
        if (refAppResultBasicValue.getCode().equals("") && refAppResultBasicValue.getDescription1().equals("")) {
            txt = " ";
        } else {
            txt = (displayCode ? refAppResultBasicValue.getCode() + " - " : "")
                    + refAppResultBasicValue.getDescription1();
        }
        this.setText(txt);
        this.setToolTipText(txt);
        return this;
    }
}