package uk.gov.courtservice.framework.jdbc.core.columneditor;

import uk.gov.courtservice.framework.jdbc.core.Row;

/**
 * @author Meeraj
 * @title SqlTimeEditor
 * @description Editor for java.sql.Time
 */
public class SqlTimeStrategy extends ColumnExtractionStrategy {

    /**
     * Returns the value for the column
     * 
     * @param Text
     *            value
     */
    public Object getValue(Row row, String column) {
        return row.getTime(column);
    }

}
