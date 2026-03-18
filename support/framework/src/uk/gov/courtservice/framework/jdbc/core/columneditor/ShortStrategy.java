package uk.gov.courtservice.framework.jdbc.core.columneditor;

import uk.gov.courtservice.framework.jdbc.core.Row;

/**
 * @author Meeraj
 * @title ShortEditor
 * @description Property editor for shorts
 */
public class ShortStrategy extends ColumnExtractionStrategy {

    /**
     * Returns the value for the column
     * 
     * @param Text
     *            value
     */
    public Object getValue(Row row, String column) {
        // This will return 0 if the column is null or 0 in the database
        short val = row.getShort(column);
        // Return null if the column is null in the database
        if (val == 0 && row.getObject(column) == null)
            return null;

        return new Short(val);
    }

}
