package uk.gov.courtservice.framework.services.conversion;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 53674 14-08-2003 Neil Entwistle Added new format to include seconds in the
 * time.
 * 
 */

public class XDateFormat {

    public static final int DATEFORMAT = 0;

    public static final int TIMEFORMAT = 1;

    public static final int DATETIMEFORMAT = 2;

    public static final int DATETIMEINSECSFORMAT = 3;

    public static final int TIMEINSECSFORMAT = 4;
    
    public static final int DAYOFWEEKFORMAT = 5;
    
    public static final int FULLMONTHFORMAT = 6;

    public static final String simpleDateFormat = "dd-MMM-yyyy";

    public static final String simpleTimeFormat = "HH:mm";

    public static final String simpleTimeInSecsFormat = "HH:mm:ss";
    
    public static final String simpleDayOfWeekFormat = "EEEE dd MMMM yyyy";
    
    public static final String simpleFullMonthFormat = "dd MMMM yyyy";

    private static final Logger log = CSServices.getLogger(XDateFormat.class);

    private XDateFormat() {
    }

    public static String format(Timestamp date, int dateTimeFormat) {
        return XDateFormat.format((Date) date, dateTimeFormat);
    }

    public static String format(Calendar date, int dateTimeFormat) {
        if (date == null) {
            return "";
        } else {
            return XDateFormat.format(date.getTime(), dateTimeFormat);
        }
    }

    public static String format(Date date, int dateTimeFormat) {
        // String strDateFormat = "dd MMM yyyy";
        // String strTimeFormat = "HH:mm";
        String strSelectedFormat;
        SimpleDateFormat formatter;

        if (date == null) {
            return "";
        } else {
            switch (dateTimeFormat) {
            case DATEFORMAT:
                // Date only
                strSelectedFormat = simpleDateFormat;
                break;
            case TIMEFORMAT:
                // Time only
                strSelectedFormat = simpleTimeFormat;
                break;
            case DATETIMEFORMAT:
                // Date and Time
                 strSelectedFormat = simpleDateFormat + " " + simpleTimeFormat;
                // Fall through to include seconds
                 break;
            case DATETIMEINSECSFORMAT:
                // Date + time with seconds
                strSelectedFormat = simpleDateFormat + " " + simpleTimeInSecsFormat;
                break;
            case TIMEINSECSFORMAT:
                // Added format for Time including seconds to allow for Court
                // Log sorting issues
                // Time with seconds
                strSelectedFormat = simpleTimeInSecsFormat;
                break;
            case DAYOFWEEKFORMAT:
            	strSelectedFormat = simpleDayOfWeekFormat;
            	break;
            case FULLMONTHFORMAT:
            	strSelectedFormat = simpleFullMonthFormat;
            	break;
            default:
                strSelectedFormat = simpleDateFormat + " " + simpleTimeFormat;
                break;
            }
            formatter = new SimpleDateFormat(strSelectedFormat, Locale.getDefault());
            return formatter.format(date);
        }
    }

    /**
     * Supply a date as a String and this will attempt to parse it.
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    public static java.util.Date parseAsDate(String date) throws ParseException {
        java.util.Date parsedDate = null;
        ParseException pe;
        // Attempt to parse date using XHIBIT date format
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(simpleDateFormat + " " + simpleTimeInSecsFormat, Locale
                    .getDefault());
            parsedDate = formatter.parse(date);
            return parsedDate;
        } catch (ParseException ex) {
            pe = ex;
        }
        // If that fails attempt to parse using universal formats.
        try {
            parsedDate = DateFormat.getDateInstance(DateFormat.SHORT).parse(date);
            return parsedDate;
        } catch (ParseException ex) {
            pe = ex;
        }
        try {
            parsedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).parse(date);
            return parsedDate;
        } catch (ParseException ex) {
            pe = ex;
        }
        try {
            parsedDate = DateFormat.getDateInstance(DateFormat.LONG).parse(date);
            return parsedDate;
        } catch (ParseException ex) {
            pe = ex;
        }
        try {
            parsedDate = DateFormat.getDateInstance(DateFormat.FULL).parse(date);
            return parsedDate;
        } catch (ParseException ex) {
            log.error(ex);
            throw pe;
        }
    }

    public static java.util.Calendar parse(String date) throws ParseException {
        Date parsedDate = parseAsDate(date);
        Calendar c = Calendar.getInstance();
        c.setTime(parsedDate);
        return c;
    }

}