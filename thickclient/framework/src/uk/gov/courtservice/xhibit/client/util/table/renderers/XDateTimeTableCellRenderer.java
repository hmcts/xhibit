package uk.gov.courtservice.xhibit.client.util.table.renderers;

import java.util.Calendar;
import java.util.Date;

import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.multiline.MultiLineCellRenderer;

/**
 * <p>
 * Title: Used for rendering Date's in correct format in tables
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
 * @version 1.0
 */

public class XDateTimeTableCellRenderer extends MultiLineCellRenderer {
    private int dateTimeFormat;

    public XDateTimeTableCellRenderer(XTable table, int dateTimeFormat) {
        super(table, table.getMultiLineHelper());
        this.dateTimeFormat = dateTimeFormat;
    }

    public XDateTimeTableCellRenderer(XTable table) {
        this(table, XDateFormat.DATETIMEFORMAT);
    }

    protected void setValue(Object value) {
        if (value instanceof String) {
            setText((String) value);
        } else if (value instanceof Date) {
            setText(value == null ? "" : XDateFormat.format((Date) value, dateTimeFormat));
        } else if (value instanceof Calendar) {
            setText(value == null ? "" : XDateFormat.format((Calendar) value, dateTimeFormat));
        } else {
            setText("");
        }
    }
}
