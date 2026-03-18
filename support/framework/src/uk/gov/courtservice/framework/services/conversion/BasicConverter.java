package uk.gov.courtservice.framework.services.conversion;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * Basic Parser implementation.
 * 
 * Other implementations could use this as a base to provide defaults.
 * 
 */
public class BasicConverter implements ValueConverter, Serializable {

    private final static SimpleDateFormat ddMMyyyyDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    static {
        ddMMyyyyDateFormat.setLenient(false);
    }

    protected final DateFormat dateFormat;

    public BasicConverter(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * Default constructor uses date format: dd/MM/yyyy.
     */
    public BasicConverter() {
        this(ddMMyyyyDateFormat);
    }

    /**
     * Extract a boolean value from a String. Accepts "true" or "false".
     * 
     * @param value
     *            the String to parse.
     * @throws ValueConvertException
     *             if the value is invalid.
     */
    public boolean parseBoolean(String value) throws ValueConvertException {
        if ("true".equals(value))
            return true;
        if ("false".equals(value))
            return false;
        throw new ValueConvertException(value);
    }

    /**
     * Extract a byte value from a String. Uses Byte.parseByte.
     * 
     * @param value
     *            the String to parse.
     * @throws ValueConvertException
     *             if the format is invalid.
     */
    public byte parseByte(String value) throws ValueConvertException {
        try {
            return Byte.parseByte(value);
        } catch (NumberFormatException e) {
            ValueConvertException ex = new ValueConvertException(value, e);
            CSServices.getDefaultErrorHandler().handleError(ex, BasicConverter.class);
            throw ex;
        }
    }

    /**
     * Extract a short value from a String. Uses Short.parseShort.
     * 
     * @param value
     *            the String to parse.
     * @throws ValueConvertException
     *             if the format is invalid.
     */
    public short parseShort(String value) throws ValueConvertException {
        try {
            return Short.parseShort(value);
        } catch (NumberFormatException e) {
            // throw new ValueConvertException(value);
            ValueConvertException ex = new ValueConvertException(value, e);
            CSServices.getDefaultErrorHandler().handleError(ex, BasicConverter.class);
            throw ex;
        }
    }

    /**
     * Extract an integer value from a String. Uses Integer.parseInt.
     * 
     * @param value
     *            the String to parse.
     * @throws ValueConvertException
     *             if the format is invalid.
     */
    public int parseInt(String value) throws ValueConvertException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            // throw new ValueConvertException(value);
            ValueConvertException ex = new ValueConvertException(value, e);
            CSServices.getDefaultErrorHandler().handleError(ex, BasicConverter.class);
            throw ex;
        }
    }

    /**
     * Extract a long value from a String. Uses Long.parseLong.
     * 
     * @param value
     *            the String to parse.
     * @throws ValueConvertException
     *             if the format is invalid.
     */
    public long parseLong(String value) throws ValueConvertException {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            // throw new ValueConvertException(value);
            ValueConvertException ex = new ValueConvertException(value, e);
            CSServices.getDefaultErrorHandler().handleError(ex, BasicConverter.class);
            throw ex;
        }
    }

    /**
     * Extract a float value from a String. Uses Float.parseFloat.
     * 
     * @param value
     *            the String to parse.
     * @throws ValueConvertException
     *             if the format is invalid.
     */
    public float parseFloat(String value) throws ValueConvertException {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            // throw new ValueConvertException(value);
            ValueConvertException ex = new ValueConvertException(value, e);
            CSServices.getDefaultErrorHandler().handleError(ex, BasicConverter.class);
            throw ex;
        }
    }

    /**
     * Extract a double value from a String. Uses Double.parseDouble.
     * 
     * @param value
     *            the String to parse.
     * @throws ValueConvertException
     *             if the format is invalid.
     */
    public double parseDouble(String value) throws ValueConvertException {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            // throw new ValueConvertException(value);
            ValueConvertException ex = new ValueConvertException(value, e);
            CSServices.getDefaultErrorHandler().handleError(ex, BasicConverter.class);
            throw ex;
        }
    }

    /**
     * Extract a date value from a String. Uses a SimpleDataFormat("dd/MM/yyyy")
     * 
     * @param value
     *            the String to parse.
     * @throws ValueConvertException
     *             if the format is invalid.
     */
    public Date parseDate(String value) throws ValueConvertException {
        try {
            return ddMMyyyyDateFormat.parse(value);
        } catch (ParseException e) {
            // throw new ValueConvertException(value);
            ValueConvertException ex = new ValueConvertException(value, e);
            CSServices.getDefaultErrorHandler().handleError(ex, BasicConverter.class);
            throw ex;
        }
    }

    /**
     * Format a boolean as a String. Returns "true" or "false".
     * 
     * @param value
     *            the boolean to format.
     */
    public String formatBoolean(boolean value) {
        return value ? "true" : "false";
    }

    /**
     * Format a byte as a String. Returns String.valueOf(value).
     * 
     * @param value
     *            the byte to format.
     */
    public String formatByte(byte value) {
        return String.valueOf(value);
    }

    /**
     * Format a short as a String. Returns String.valueOf(value).
     * 
     * @param value
     *            the short to format.
     */
    public String formatShort(short value) {
        return String.valueOf(value);
    }

    /**
     * Format an integer as a String. Returns String.valueOf(value).
     * 
     * @param value
     *            the integer to format.
     */
    public String formatInt(int value) {
        return String.valueOf(value);
    }

    /**
     * Format a long as a String. Returns String.valueOf(value).
     * 
     * @param value
     *            the long to format.
     */
    public String formatLong(long value) {
        return String.valueOf(value);
    }

    /**
     * Format a float as a String. Returns String.valueOf(value).
     * 
     * @param value
     *            the float to format.
     */
    public String formatFloat(float value) {
        return String.valueOf(value);
    }

    /**
     * Format a double as a String. Returns String.valueOf(value).
     * 
     * @param value
     *            the double to format.
     */
    public String formatDouble(double value) {
        return String.valueOf(value);
    }

    /**
     * Format a Date as a String. Uses a SimpleDataFormat("dd/MM/yyyy")
     * 
     * @param value
     *            the Date to format.
     */
    public String formatDate(Date value) {
        return ddMMyyyyDateFormat.format(value);
    }
}