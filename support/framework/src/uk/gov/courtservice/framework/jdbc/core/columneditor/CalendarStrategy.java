package uk.gov.courtservice.framework.jdbc.core.columneditor;

import java.sql.Timestamp;
import java.util.GregorianCalendar;

import uk.gov.courtservice.framework.jdbc.core.Row;

/**
 * @author Meeraj
 * @title CalendarEditor
 * @description Editor for java.util.Calendar
 */
public class CalendarStrategy extends ColumnExtractionStrategy {

    /**
     * Returns the value for the column
     * 
     * @param Text
     *            value
     */
    public Object getValue(Row row, String column) {

        Timestamp ts = row.getTimestamp(column);
        if (ts == null)
            return null;

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(ts);
        return cal;

    }

}
