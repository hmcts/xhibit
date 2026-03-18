package uk.gov.courtservice.xhibit.client.results;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Title: ResultUtil
 * </p>
 * <p>
 * Description: Utility methods used by results.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.5 $
 */
public class ResultsUtil {

    /**
     * Stop construction of utility (all static) class
     */
    private ResultsUtil() {
        // Change permisions of default constructor
    }

    /**
     * Return the index of criteria in data ignoring case differences or -1 if
     * not found
     */
    public static int indexOfIgnoreCase(String data, String criteria) {
        if (data == null || criteria == null) {
            return -1;
        } else {
            return data.toLowerCase().indexOf(criteria.toLowerCase());
        }
    }

    /**
     * Return the given score if the if the values are equal
     */
    public static boolean equals(Object value1, Object value2) {
        return (value1 == null && value2 == null) || (value1 != null && value1.equals(value2));
    }

    /**
     * Return an array of lines split from the source string
     */

    public static String[] getLines(String input) {
        // if the input is invalid, then return an empty array...
        if ((input == null) || (input.length() == 0)) {
            return new String[0];
        }

        final List lines = new ArrayList();
        final char[] array = input.toCharArray();
        // the first character in the new line...
        int firstChar = 0;

        for (int i = 0, n = array.length; i < n; i++) {
            if (array[i] == '\n' || array[i] == '\r') {
                lines.add(new String(array, firstChar, i - firstChar));
                firstChar = i + 1;
            }
        }

        // ensure that we get the last characters if there are any
        if (firstChar != array.length) {
            lines.add(new String(array, firstChar, array.length - firstChar));
        }

        return (String[]) lines.toArray(new String[lines.size()]);
    }

    /**
     * Return the lines formated as text
     */
    public static String getText(String[] lines) {
        if (lines == null || lines.length == 0) {
            return "";
        }

        if (lines.length == 1) {
            return lines[0];
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(lines[0]);
        for (int i = 1; i < lines.length; i++) {
            buffer.append('\n');
            buffer.append(lines[i]);
        }
        return buffer.toString();
    }

    /**
     * checkForEmptyLines in String array passed in.
     * 
     * @param linesIn
     * @return
     */
    public static boolean checkForEmptyLines(String[] linesIn) {
        String emptyStr = "";
        for (int i = 0; i < linesIn.length; i++) {
            if (linesIn[i].trim().equals(emptyStr) || linesIn[i].trim().equals(null)) {
                return true;
            }
        }
        return false;
    }

    /**
     * remove empty lines from String array passed in.
     * 
     * @param linesIn
     * @return
     */
    public static String[] removeEmptyLines(String[] linesIn) {
        String emptyStr = "";

        List linesOut = new ArrayList();
        for (int i = 0; i < linesIn.length; i++) {
            if (!linesIn[i].trim().equals(emptyStr) && !linesIn[i].trim().equals(null)) {
                linesOut.add(linesIn[i]);
            }
        }
        return (String[]) linesOut.toArray(new String[linesOut.size()]);
    }
}
