package uk.gov.courtservice.framework.jdbc.core.columneditor;

import uk.gov.courtservice.framework.jdbc.core.Row;

/**
 * @author Meeraj
 * @title DoubleEditor
 * @description Property editor for double
 */
public class DoubleStrategy extends ColumnExtractionStrategy {

    /**
     * Returns the value for the column
     * 
     * @param Text
     *            value
     */
    public Object getValue(Row row, String column) {
        // This will return 0 if the column is null or 0 in the database
        double val = row.getDouble(column);
        // Return null if the column is null in the database
        if (val == 0 && row.getObject(column) == null)
            return null;

        return new Double(val);
    }

}
