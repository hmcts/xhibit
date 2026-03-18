package uk.gov.courtservice.xhibit.client.util.table.renderers;

import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * <p>
 * Title: Used for rendering Percentage's in correct format in tables
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell, Xdevelopment (2004)
 * @version 1.0
 */
public class XPercentageTableCellRenderer extends DefaultTableCellRenderer {
    // DecimalFormat
    private final DecimalFormat format = new DecimalFormat("0.00%");

    /**
     * Construct a new renderer for percentages
     */
    public XPercentageTableCellRenderer() {
        setHorizontalAlignment(JLabel.RIGHT);
    }

    protected void setValue(Object value) {
        setText(value == null ? "" : format.format(value));
    }
}