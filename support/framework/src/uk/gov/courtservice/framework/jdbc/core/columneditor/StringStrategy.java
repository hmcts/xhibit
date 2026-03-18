package uk.gov.courtservice.framework.jdbc.core.columneditor;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;

import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.jdbc.exception.ExceptionTranslator;

/**
 * @author Meeraj
 * @editor Rakesh Lakhani Added code to handle Clob's on the assumption that the
 *         result will be stored in a String in the value object
 * @title StringStrategy
 * @description Property editor for strings
 */
public class StringStrategy extends ColumnExtractionStrategy {
    /** Buffer size for reading from a Clob */
    private static final int BUFFER_SIZE = 4096;

    /**
     * Returns the value for the column
     * 
     * @param Text
     *            value
     */
    public Object getValue(Row row, String column) {
        Object o = row.getObject(column);
        if (o instanceof Clob) {
            return getClobString((Clob) o);
        } else if (o instanceof String) {
            return (String) o;
        } else {
            return row.getString(column);
        }
    }
    
    /**
     * Returns the value for the clob as a string.
     * 
     * @param clob
     *            the clob to convert to a string.
     * @return the clob as a string.
     */
    public static String getValue(Clob clob) {
        return getClobString(clob);
    }

    /**
     * Gets the given clob as a string.
     * 
     * @param clob
     *            the clob to convert to a string.
     * @return the clob as a string.
     */
    private static String getClobString(Clob clob) {
        if (clob == null)
            return null;

        try {
            // set the default size to a reasonable amount to reduce the
            // number
            // of dynamic extensions to the buffer...
            StringBuffer sb = new StringBuffer(1000);
            char[] buffer = new char[BUFFER_SIZE];

            Reader reader = clob.getCharacterStream();

            for (int charsRead = reader.read(buffer); charsRead != -1; charsRead = reader.read(buffer)) {
                sb.append(buffer, 0, charsRead);
            }
            reader.close();

            return sb.toString();
        } catch (IOException ex) {
            throw ExceptionTranslator.getInstance().translate(ex);
        } catch (SQLException ex) {
            throw ExceptionTranslator.getInstance().translate(ex);
        }
    }
}