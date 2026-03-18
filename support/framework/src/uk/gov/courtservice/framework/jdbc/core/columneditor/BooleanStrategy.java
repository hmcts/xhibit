package uk.gov.courtservice.framework.jdbc.core.columneditor;

import java.util.ArrayList;

import uk.gov.courtservice.framework.jdbc.core.Row;

/**
 * @author Meeraj
 * @title BooleanEditor
 * @description Property editor for booleans
 */
public class BooleanStrategy extends ColumnExtractionStrategy {

    private static ArrayList trueTexts = new ArrayList();
    static {
        trueTexts.add("true");
        trueTexts.add("y");
        trueTexts.add("1");
    }

    /**
     * Returns the value for the column
     * 
     * @param Text
     *            value
     */
    public Object getValue(Row row, String column) {

        String val = row.getString(column);
        if (val == null)
            return new Boolean(false);

        return new Boolean(trueTexts.contains(val.toLowerCase()));

    }

}
