package uk.gov.courtservice.framework.services.conversion;

import java.util.Date;

/**
 * Basic String Converter.
 * 
 * Provides methods for converting various common types to/from String.
 * 
 */
public interface ValueConverter {

    /**
     * Extract a boolean value from a String.
     * 
     * @param value
     *            the String to parse.
     * @throws ValueConvertException
     *             if the format is invalid.
     */
    boolean parseBoolean(String value) throws ValueConvertException;

    /**
     * Extract a byte value from a String.
     * 
     * @param value
     *            the String to parse.
     * @throws ValueConvertException
     *             if the format is invalid.
     */
    byte parseByte(String value) throws ValueConvertException;

    /**
     * Extract a short value from a String.
     * 
     * @param value
     *            the String to parse.
     * @throws ValueConvertException
     *             if the format is invalid.
     */
    short parseShort(String value) throws ValueConvertException;

    /**
     * Extract an integer value from a String.
     * 
     * @param value
     *            the String to parse.
     * @throws ValueConvertException
     *             if the format is invalid.
     */
    int parseInt(String value) throws ValueConvertException;

    /**
     * Extract a long value from a String.
     * 
     * @param value
     *            the String to parse.
     * @throws ValueConvertException
     *             if the format is invalid.
     */
    long parseLong(String value) throws ValueConvertException;

    /**
     * Extract a float value from a String.
     * 
     * @param value
     *            the String to parse.
     * @throws ValueConvertException
     *             if the format is invalid.
     */
    float parseFloat(String value) throws ValueConvertException;

    /**
     * Extract a double value from a String.
     * 
     * @param value
     *            the String to parse.
     * @throws ValueConvertException
     *             if the format is invalid.
     */
    double parseDouble(String value) throws ValueConvertException;

    /**
     * Extract a date value from a String.
     * 
     * @param value
     *            the String to parse.
     * @throws ValueConvertException
     *             if the format is invalid.
     */
    Date parseDate(String value) throws ValueConvertException;

    /**
     * Format a boolean as a String.
     * 
     * @param value
     *            the boolean to format.
     */
    String formatBoolean(boolean value);

    /**
     * Format a byte as a String.
     * 
     * @param value
     *            the byte to format.
     */
    String formatByte(byte value);

    /**
     * Format a short as a String.
     * 
     * @param value
     *            the short to format.
     */
    String formatShort(short value);

    /**
     * Format an integer as a String.
     * 
     * @param value
     *            the integer to format.
     */
    String formatInt(int value);

    /**
     * Format a long as a String.
     * 
     * @param value
     *            the long to format.
     */
    String formatLong(long value);

    /**
     * Format a float as a String.
     * 
     * @param value
     *            the float to format.
     */
    String formatFloat(float value);

    /**
     * Format a double as a String.
     * 
     * @param value
     *            the double to format.
     */
    String formatDouble(double value);

    /**
     * Format a Date as a String.
     * 
     * @param value
     *            the Date to format.
     */
    String formatDate(Date value);
}