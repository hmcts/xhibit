package uk.gov.courtservice.xhibit.client.results.appealresults;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import uk.gov.courtservice.xhibit.business.vos.entities.RefAppResultBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;

/**
 * <p>
 * Title: RefAppResultComboBoxRenderer
 * </p>
 * <p>
 * Description: Renders a RefAppResultBasicValue for use in a JComboBox.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 53684 07-07-2003 AW Daley Modified to extend DefaultListCellRenderer and
 * calls super class getListCellRendererComponent method prior to providing
 * required behaviour.
 */
public class RefAppResultComboBoxRenderer extends DefaultListCellRenderer {
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

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setHorizontalAlignment(LEFT);

        if ((list.getSelectedValue() != null) && (!list.getSelectedValue().equals(" "))) {
            String txt = "";

            if (value instanceof RefAppResultBasicValue) {
                RefAppResultBasicValue rarbv = (RefAppResultBasicValue) value;
                txt = rarbv.getDescription1() + " " + checkNull(rarbv.getDescription2());
            } else if (value instanceof RefSystemCodeBasicValue) {
                RefSystemCodeBasicValue rscbv = (RefSystemCodeBasicValue) value;
                txt = rscbv.getDecode();
            }

            this.setText(txt.length() == 0 ? " " : txt);
            // add tooltip
            if (isSelected) {
                list.setToolTipText(txt.length() == 0 ? null : txt);
            }
        } else {
            this.setText(" ");
        }

        return this;
    }

    private String checkNull(String text) {
        if (text == null) {
            return "";
        } else {
            return text;
        }
    }
}