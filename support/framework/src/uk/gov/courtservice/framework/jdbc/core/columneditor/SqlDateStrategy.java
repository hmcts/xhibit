package uk.gov.courtservice.framework.jdbc.core.columneditor;

import uk.gov.courtservice.framework.jdbc.core.Row;

/**
 * @author Meeraj
 * @title SqlDateEditor
 * @description Editor for java.sql.Date
 */
public class SqlDateStrategy extends ColumnExtractionStrategy {

    /**
     * Returns the value for the column
     * 
     * @param Text
     *            value
     */
    public Object getValue(Row row, String column) {
        return row.getDate(column);
    }

}
