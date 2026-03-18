package uk.gov.courtservice.xhibit.client.crestformsbf.util;

/**
 * Util contains only static methods (mainly for manipulating primitives) for
 * use by other classes.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment 2003
 */

public final class Util {

    /**
     * Stop unnecisary instantiation of this class
     */
    private Util() {
    }

    //
    // Manipulation of primitives
    //

    /**
     * Compare two booleans, false comes before false
     * 
     * @param value
     *            the long to be compared.
     * @param other
     *            the long to compare to.
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     */
    public static final int compareTo(boolean value, boolean other) {
        return value == other ? 0 : value && !other ? 1 : -1;
    }

    /**
     * Compare two longs, this implements the same algorithm as
     * Long.compareTo(Object)
     * 
     * @param value
     *            the long to be compared.
     * @param other
     *            the long to compare to.
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     */
    public static final int compareTo(long value, long other) {
        return (value < other ? -1 : (value == other ? 0 : 1));
    }

    /**
     * Compare two doubles, this implements the same algorithm as
     * Double.compareTo(Object)
     * 
     * @param value
     *            the double to be compared.
     * @param other
     *            the double to compare to.
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     */
    public static final int compareTo(double value, double other) {
        if (value < other)
            return -1; // Neither val is NaN, thisVal is smaller
        if (value > other)
            return 1; // Neither val is NaN, thisVal is larger

        long valueBits = Double.doubleToLongBits(value);
        long otherBits = Double.doubleToLongBits(other);

        return (valueBits == otherBits ? 0 // Values are equal
                : (valueBits < otherBits ? -1 // (-0.0, 0.0) or (!NaN, NaN)
                        : 1)); // (0.0, -0.0) or (NaN, !NaN)
    }

    /**
     * Hash a long value this implements the same algorithm as Long.hash()
     * 
     * @param value
     *            the value to hash long
     * @return a hash code value for this object.
     */
    public static final int hash(long value) {
        return (int) (value ^ (value >>> 32));
    }

    /**
     * Hash a double value this implements the same algorithm as Double.hash()
     * 
     * @param value
     *            the value ti hash long
     * @return a hash code value for this object.
     */
    public static final int hash(double value) {
        return hash(Double.doubleToLongBits(value));
    }

    /**
     * Convert a String into a boolean valid values are (case insensitive)
     * "true" or "yes" => true "false" or "no" => false
     * 
     * @param value
     *            the value to parse
     * @throws BooleanFormatException
     *             if the value is not a valid boolean
     */
    public static final boolean parseBoolean(String value) throws BooleanFormatException {
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes") || value.equals("1")) {
            return true;
        } else if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no") || value.equals("0")) {
            return false;
        } else {
            throw new BooleanFormatException(value);
        }
    }

    /**
     * The representation of an int array as a String is as follows
     * 
     * [x1, x2, x3, ..., xN]
     * 
     * @return a <code>String</code> containing the int array representation
     */
    public static String valueOf(int[] values) {
        if (values == null) {
            return "null";
        } else {
            if (0 < values.length) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("[");
                buffer.append(values[0]);
                for (int i = 1; i < values.length; i++) {
                    buffer.append(", ");
                    buffer.append(values[i]);
                }
                buffer.append("]");
                return buffer.toString();
            } else {
                return "[]";
            }
        }
    }

}
