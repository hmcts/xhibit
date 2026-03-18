package uk.gov.courtservice.xhibit.client.order.gui.helpers;

/**
 * <p>
 * Title: DataFieldHelper
 * </p>
 * <p>
 * Description: Class that returns date values to CustomDurationComboBox
 * component
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David
 * @version 1.0
 */

public class DateFieldHelper {
    private String[] day;
    
    private String[] week;

    private String[] month;

    private String[] year;
    
    private String[] hour;

    // default values
    private int yearsMax = 100;

    private int monthsMax = 100;
    
    private int weeksMax = 100;

    private int daysMax = 1000;
    
    private int hoursMax = 24;

    public DateFieldHelper() {
    }

    private void populateHour() {
        day = new String[getHoursMax()];
        int element = 0;
        for (int i = 1; i <= getHoursMax(); i++) {
            day[i - 1] = String.valueOf(element);
            element = element + 1;
        }
    }
    
    private void populateDay() {
        day = new String[getDaysMax()];
        int element = 0;
        for (int i = 1; i <= getDaysMax(); i++) {
            day[i - 1] = String.valueOf(element);
            element = element + 1;
        }
    }
    
    private void populateWeek() {
        week = new String[getWeeksMax()];
        int element = 0;
        for (int i = 1; i <= getWeeksMax(); i++) {
            week[i - 1] = String.valueOf(element);
            element = element + 1;
        }
    }

    private void populateMonth() {
        month = new String[getMonthsMax()];
        int element = 0;
        for (int i = 1; i <= getMonthsMax(); i++) {
            month[i - 1] = String.valueOf(element);
            element = element + 1;
        }
    }

    private void populateYear() {
        year = new String[getYearsMax()];
        int element = 0;
        for (int i = 1; i <= getYearsMax(); i++) {
            year[i - 1] = String.valueOf(element);
            element = element + 1;
        }
    }

    /**
     * @return year
     */
    public String[] getYear() {
        populateYear();
        return this.year;
    }

    /**
     * @return month
     */
    public String[] getMonth() {
        populateMonth();
        return this.month;
    }
    
    /**
     * @return week
     */
    public String[] getWeek() {
        populateWeek();
        return this.week;
    }

    /**
     * @return day
     */
    public String[] getDay() {
        populateDay();
        return this.day;
    }
    
    /**
     * @return hour
     */
    public String[] getHour() {
        populateHour();
        return this.hour;
    }

    /**
     * 
     * @return years Max
     */
    public int getYearsMax() {
        return this.yearsMax;
    }

    /**
     * 
     * @return months Max
     */
    public int getMonthsMax() {
        return this.monthsMax;
    }
    
    /**
     * 
     * @return weeks Max
     */
    public int getWeeksMax() {
        return this.weeksMax;
    }

    /**
     * 
     * @return days Max
     */
    public int getDaysMax() {
        return this.daysMax;
    }
    
    /**
     * 
     * @return hours Max
     */
    public int getHoursMax() {
        return this.hoursMax;
    }

    /**
     * 
     * @param newMaxYear +
     *            1 to accomodate the first zero element
     */
    public void setYearsMax(int newMaxYear) {
        this.yearsMax = newMaxYear + 1;
    }

    /**
     * 
     * @param newMaxMonths +
     *            1 to accomodate the first zero element
     */
    public void setMonthsMax(int newMaxMonths) {
        this.monthsMax = newMaxMonths + 1;
    }
    
    /**
     * 
     * @param newMaxWeekss +
     *            1 to accomodate the first zero element
     */
    public void setWeeksMax(int newMaxWeeks) {
        this.weeksMax = newMaxWeeks + 1;
    }

    /**
     * 
     * @param newMaxDays +
     *            1 to accomodate the first zero element
     */
    public void setDaysMax(int newMaxDays) {
        this.daysMax = newMaxDays + 1;
    }
    
    /**
     * 
     * @param newMaxHours +
     *            1 to accomodate the first zero element
     */
    public void setHoursMax(int newMaxHours) {
        this.hoursMax = newMaxHours + 1;
    }
}