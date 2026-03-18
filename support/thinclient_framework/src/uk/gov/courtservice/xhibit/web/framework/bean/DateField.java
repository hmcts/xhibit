package uk.gov.courtservice.xhibit.web.framework.bean;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: DateField
 * </p>
 * <p>
 * Description: This class provides a java object to be used by a bean mapping
 * an HTML page where the data can be modified.
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 */
public class DateField extends AbstractField {

    /**
     * value of the field
     */
    private Date value;

    /**
     * format of the Date
     */
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

    static {
        dateFormat.setLenient(false);
    }

    private static Logger log = CSServices.getLogger(DateField.class);

    /**
     * Check the value matches the date format exactly, setLenient(false) is not
     * strict enough
     * 
     * @param value
     *            the value to check
     * @return true if the date is valid
     */
    private static boolean isValid(String value) {
        return value.length() == 8 && Character.isDigit(value.charAt(0)) && Character.isDigit(value.charAt(1))
                && value.charAt(2) == '/' && Character.isDigit(value.charAt(3)) && Character.isDigit(value.charAt(4))
                && value.charAt(5) == '/' && Character.isDigit(value.charAt(6)) && Character.isDigit(value.charAt(7));
    }

    /**
     * Construct a Date field with a given value
     * 
     * @param newValue
     *            the value
     */
    public DateField(String newValue) {
        setValue(newValue);
    }

    /**
     * Construct a Date field with a given value
     * 
     * @param newValue
     *            the value
     */
    public DateField(Date newValue) {
        setValue(newValue);
    }

    /**
     * Standard java bean accessor
     * 
     * @return the value
     */
    public Date getValue() {
        return value;
    }

    public Timestamp getTimestamp() {
        if (value == null) {
            return null;
        } else {
            return new Timestamp(value.getTime());
        }
    }

    /**
     * Standard java bean setter
     * 
     * @param newValue
     *            the new value
     */
    public void setValue(Date newValue) {
        value = newValue;
    }

    /**
     * Standard java bean setter
     * 
     * @param newValue
     *            the new value
     */
    public void setValue(String newValue) {
        if (newValue.length() > 0) {
            value = parseValue(newValue);
            if (value == null) {
                setError(newValue, "datefield.invalid");
            }
        }
    }

    /**
     * Parse the date value
     * 
     * @param newValue
     *            the value to parse
     * @return the new date value or null if invalid
     */
    public Date parseValue(String newValue) {
        if (isValid(newValue)) {
            try {
                return dateFormat.parse(newValue);
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }
}
