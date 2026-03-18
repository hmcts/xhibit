package uk.gov.courtservice.framework.jdbc.core.columneditor;

import uk.gov.courtservice.framework.jdbc.core.Row;

/**
 * @author Meeraj
 * @title ByteEditor
 * @description Property editor for bytes
 */
public class ByteStrategy extends ColumnExtractionStrategy {

    /**
     * Returns the value for the column
     * 
     * @param Text
     *            value
     */
    public Object getValue(Row row, String column) {
        // This will return 0 if the column is null or 0 in the database
        byte val = row.getByte(column);
        // Return null if the column is null in the database
        if (val == 0)
            return row.getObject(column);

        return new Byte(val);
    }

}
