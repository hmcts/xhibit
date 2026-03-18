package uk.gov.courtservice.framework.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import java.text.SimpleDateFormat;

/**
 * <p>
 * Title: DateTimeUtilities
 * </p>
 * <p>
 * Description: This class is used as a repositiry for common functions and
 * utilities relating to dates and time
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford / Marie Holmberg
 * @version 1.0
 */
public class DateTimeUtilities {
    // The logger for this class.
    private static Logger LOG = CSServices.getLogger(DateTimeUtilities.class);

    private static final SimpleDateFormat ORACLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	public static final String DAY = "day";
	public static final String MONTH = "month";
	public static final String YEAR = "year";
	public static final String WEEK = "week";
	public static final String HOUR = "hour";
    
    // Singleton instance
    private static final DateTimeUtilities INSTANCE = new DateTimeUtilities();

    public DateTimeUtilities() {
        super();
    }

    /**
     * Returns the singleton
     * 
     * @deprecated Access to methods should be direct as now static
     */
    public static final DateTimeUtilities getInstance() {
        return INSTANCE;
    }

    /**
     * Retrieve only the "date" part of a Date
     * 
     * @param date
     * @return
     * @deprecated - replaced by #stripTimeToSQLDate(java.util.Date)
     */
    public static java.sql.Date stripTime(Date date) {
        return stripTimeToSQLDate(date);
    }

    /**
     * Retrieve only the "date" part of a Date
     * 
     * @param date
     *            the <code>java.util.Date</code> without the time portion
     * @return
     */
    public static java.util.Date stripTimeToUtilDate(Date date) {
        LOG.debug("DateTimeUtilities.stripTimeToUtilDate() called");

        Calendar old = Calendar.getInstance();
        old.setTime(date);

        final int year = old.get(Calendar.YEAR);
        final int month = old.get(Calendar.MONTH);
        final int dayOfMonth = old.get(Calendar.DATE);

        Calendar gre = new GregorianCalendar(year, month, dayOfMonth);
        LOG.debug("DateTimeUtilities.stripTimeToUtilDate() finsished");
        return gre.getTime();
    }

    /**
     * Retrieve only the "date" part of a Date
     * 
     * @param date
     *            the <code>java.sql.Date</code> without the time portion
     */
    public static java.sql.Date stripTimeToSQLDate(Date date) {
        return new java.sql.Date(stripTimeToUtilDate(date).getTime());
    }

    /**
     * Retrieve only the "date" part of a Date
     * 
     * @param date
     * @return
     */
    public static Calendar stripTimeToCalendar(Date date) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(stripTimeToUtilDate(date));
    	return cal;
    }
    
    /**
     * Converts a formatted String to a Calendar in the Oracle date format
     * Expected format of Oracel Date is yyyy-mm-ddTHH:mm:ss
     * @param String timeStamp
     * @return Calendar
     */
    public static Calendar processOracleDateParameter(String timeStamp) throws ParseException {
        return processDateParameter(timeStamp, ORACLE_DATE_FORMAT);
    }

    /**
     * Converts a formatted String to a Date in the Oracle date format
     * @param timeStamp
     * @return
     * @throws ParseException
     */
    public static Date processOracleDateParameterForDate(String timeStamp) throws ParseException {
        Calendar cal = processDateParameter(timeStamp, ORACLE_DATE_FORMAT);
        return cal.getTime();
    }

    /**
     * Converts a formatted String to a Calendar
     * @param String timeStamp (mandatory and non-null)
     * @param SimpleDateFormat format (mandatory and non-null)
     * @return Calendar
     */
    public static Calendar processDateParameter(String timeStamp, SimpleDateFormat format) throws ParseException {
        LOG.debug("processDateParameter, timeStamp: " + timeStamp);

        Calendar time = Calendar.getInstance();

        time.setTime(format.parse(timeStamp));        
        return time;
    }
    
    /**
     * Converts a Date to a formatted String in the Oracle date format
     * @param Date
     * @return String
     * @throws ParseException
     */
    public static String convertOracleDate(Date date) throws ParseException {
        return convertDate(date,ORACLE_DATE_FORMAT);
    }
    
    /**
     * Converts a Date to a formatted String
     * @param Date
     * @param SimpleDateFormat
     * @return String
     * @throws ParseException
     */
    public static String convertDate(Date date, SimpleDateFormat formatter) throws ParseException {
        return formatter.format(date);
    }

    /**
     * Converts a Date to a Calendar object
     * @param Date
     * @return Calendar
     */
    public static Calendar convertToCalendar(Date date) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
        return cal;
    }

    /**
     * Add a passed in number of months to a Calendar object
     * @param Calendar
     * @return Calendar
     */
    public static Calendar addMonths(Calendar date, int months) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date.getTime());
    	cal.add(Calendar.MONTH, months);
    	return cal;
    }
    
    /**
     * Add a passed in number of seconds to a Calendar object
     * @param Calendar
     * @return Calendar
     */
    public static Date addSeconds(Date date, int seconds) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.add(Calendar.SECOND, seconds);
    	return cal.getTime();
    }

    /**
     * Return the day of the week for a passed in Calendar.DAY_OF_WEEK value
     * @param Calendar date
     * @param int dayOfWeek (ie Calendar.FRIDAY)
     * @return Calendar
     */
    public static  Calendar getDateOnDayOfWeek(Calendar date, Integer dayOfWeek) {
    	Calendar dateOnDayOfWeek = date;
    	Integer DOW = dateOnDayOfWeek.get(Calendar.DAY_OF_WEEK);
    	if (!DOW.equals(dayOfWeek)) {
    		dateOnDayOfWeek.add(Calendar.DAY_OF_WEEK, -(DOW));
    		dateOnDayOfWeek.add(Calendar.DAY_OF_WEEK, dayOfWeek);
    	}
    	return dateOnDayOfWeek;
    }

    /**
     * Check if the passed in date is a weekend
     * @param Calendar date
     * @return boolean
     */
    public static boolean isWeekend(Calendar date) {
    	Integer DOW = date.get(Calendar.DAY_OF_WEEK);
    	return DOW.equals(Calendar.SATURDAY) || DOW.equals(Calendar.SUNDAY);
    }
	
	/**
	 * Return true if the two dates are on the same day,
	 * i.e. day, month, year are equal regardless of time.
	 * 
	 * @param date1
	 * @param date2
	 * @return true if days are the same
	 */
	public static boolean isDaySame(Date date1, Date date2) {
		// If either date is null, it is impossible for the days to be identical
		if (date1 == null || date2 == null) {
			return false;
		}
		
		// Convert to calendar and compare the date components of the date
		Calendar cal1 = convertToCalendar(date1);
		Calendar cal2 = convertToCalendar(date2);
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
	}
	
	public static Integer[] getDuration(final Integer durationValue, final String durationUnit, Integer[] daysMonthsYears) {
		int days = daysMonthsYears[0]; 
		int months = daysMonthsYears[1];
		int years = daysMonthsYears[2];
		Integer number =  new Integer(durationValue);
		
	    if(durationUnit.contains(DAY)){	    	 
	    	days += number;
		}
		else if (durationUnit.contains(MONTH)){
			months += number;
		}
	    else if(durationUnit.contains(YEAR)){
	    	years += number;
	    }
	    else if(durationUnit.contains(HOUR)){
	    	// Partial days not required (so 37H = 1 day)
	    	days +=Math.abs(number/24);
	    }
	    else if(durationUnit.contains(WEEK)) {
	    	days += (number * 7);
	    }

	    // Carry over the days 
	    if (days > 30) {	    	
	    	double monthsInADay = 0.0328767;
	    	double actualNoOfMonths = days * monthsInADay;
	    	int noOfMonths = (int) Math.abs(actualNoOfMonths);
	    	double remainderDays = (actualNoOfMonths - noOfMonths) / monthsInADay;
	    	int noOfDays = (int) Math.abs(remainderDays);
	    	
	    	days = noOfDays > 0 ? noOfDays : 0;
	    	months += noOfMonths;
	    }
	   
	    // Carry over the months 
	    if(months > 12 || (months == 12 && (days > 0 || years > 0))) {
	    	years += months/12;
	    	months = months%12;
	    }
	    
	    // Limit the max
	    if (years>99) {
    		years = 99;
    		days=0;
    		months=0;
    	}

	    return new Integer[] {days,months,years};
    }
}