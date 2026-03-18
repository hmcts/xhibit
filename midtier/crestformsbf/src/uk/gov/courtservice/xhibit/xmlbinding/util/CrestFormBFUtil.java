package uk.gov.courtservice.xhibit.xmlbinding.util;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

/**
 * <p>
 * Title: Utility class intended to hold any form xml binding specific
 * formatting.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * All formatting related variables and methods should be placed here providing
 * a common place for formatting related functionality.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Surtar Bachra
 * @version 1.0
 */

public class CrestFormBFUtil {
    /**
     * Will format a case number to be padded to 8 digits using '0's as
     * necessary.
     */
    public static final DecimalFormat EIGHT_DIGIT = new DecimalFormat("00000000");

    /**
     * Utility class to format a date string to the correct format
     * 
     * @param date
     *            The string to be formatted
     * @return formatted date
     */
    public static java.util.Date formatDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
        ParsePosition pos = new ParsePosition(0);
        java.util.Date formattedDate = formatter.parse(date, pos);
        return formattedDate;
    }
}
