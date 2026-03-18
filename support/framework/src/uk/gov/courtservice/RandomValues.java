package uk.gov.courtservice;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Collection of methods for generating random vos.
 * 
 * 07/01/02 NL: Removed & and < from randomTextChar() and randomTextString() as
 * causing xml parsing problems in tests.
 * 
 * 30/11/01 NL: Corrected error in randomInt, now may return max value. This has
 * a knock-on correction on random char and String methods. Added randomLong(),
 * randomFloat(), and randomDouble(). Added randomPattern().
 * 
 * @author Nick Lawson
 */
public class RandomValues {

    // /**
    // * Test, prints some random vos.
    // */
    // public static void main(String[] args) {
    // RandomValues rv = new RandomValues();
    //
    // System.out.print("randomInt(-10,10): ");
    // for (int i = 0; i < 100; i++) {
    // System.out.print(rv.randomInt(-10,10));
    // System.out.print(',');
    // }
    // System.out.println();
    //
    // System.out.println();
    // System.out.print("randomDouble(1,0,true): ");
    // for (int i = 0; i < 100; i++) {
    // System.out.print(rv.randomDouble(1,0,true));
    // System.out.print(',');
    // }
    // System.out.println();
    //
    // System.out.println();
    // System.out.print("randomDouble(5,2): ");
    // for (int i = 0; i < 100; i++) {
    // System.out.print(rv.randomDouble(5,2));
    // System.out.print(',');
    // }
    // System.out.println();
    //
    // System.out.println();
    // System.out.print("randomPattern(\"uLA9N:/u/L/A/9/N///\"): ");
    // for (int i = 0; i < 100; i++) {
    // System.out.print(rv.randomPattern("uLA9N:/u/L/A/9/N///"));
    // System.out.print(',');
    // }
    // System.out.println();
    // }

    /**
     * Get a random double value x, uniformly distributed over 0 <= x < 1. This
     * is used to generate all the other random vos.
     */
    public double random() {
        return Math.random();
    }

    /**
     * @return a random integer uniformly distributed in the specified range. If
     *         min == max then returns max. If the range is >= Integer.MAX_VALUE
     *         the distribution is not uniform, but concentrated in the middle
     *         of the range.
     * @throws IllegalArgumentException
     *             if max < min.
     */
    public int randomInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min: " + min + " > max: " + max);
        }
        if (min == max) {
            return max;
        }
        if (min < 0 && min + Integer.MAX_VALUE <= max) {
            return (int) (min * random()) + (int) (max * random());
        }
        return min + (int) ((max - min + 1) * random());
        // long l = (long)min + (long)(random()*((long)max - (long)min));
        // return (int)l;
    }

    /**
     * @return a random long uniformly distributed in the specified range. If
     *         min == max then returns max. If the range is >= Long.MAX_VALUE
     *         the distribution is not uniform, but concentrated in the middle
     *         of the range.
     * @throws IllegalArgumentException
     *             if max < min.
     */
    public long randomLong(long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException("min: " + min + " > max: " + max);
        }
        if (min == max) {
            return max;
        }
        if (min < 0 && min + Long.MAX_VALUE <= max) {
            return (long) (min * random()) + (long) (max * random());
        }
        return min + (long) ((max - min + 1) * random());
    }

    public int randomInt() {
        return randomInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * @return a random short in the specified range. If min == max then returns
     *         max.
     * @throws IllegalArgumentException
     *             if max < min.
     */
    public short randomShort(short min, short max) {
        return (short) randomInt(min, max);
    }

    public short randomShort() {
        return randomShort(Short.MIN_VALUE, Short.MAX_VALUE);
    }

    /**
     * @return a random byte in the specified range. If min == max then returns
     *         max.
     * @throws IllegalArgumentException
     *             if max < min.
     */
    public byte randomByte(byte min, byte max) {
        return (byte) randomInt(min, max);
    }

    public byte randomByte() {
        return randomByte(Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    private final static int MAX_FLOAT_DIGITS = 16;

    private static long[] powersOfTen = new long[MAX_FLOAT_DIGITS + 1];
    static {
        long p = 1L;
        powersOfTen[0] = p;
        for (int i = 1; i < powersOfTen.length; i++) {
            powersOfTen[i] = p *= 10;
        }
    }

    /**
     * Generates a random double.
     * 
     * @param digits
     *            is the number of decimal digits required.
     * @param places
     *            is the number of digits after the decimal point.
     * @param positive
     *            if true produces only non-negative numbers.
     * @throws IllegalArgumentException
     *             if 0 > places, places > digits , or digits > 16.
     */
    public double randomDouble(int digits, int places, boolean positive) {
        if (places < 0) {
            throw new IllegalArgumentException("places negative: " + places);
        }
        if (digits < places) {
            throw new IllegalArgumentException("digits " + digits + " less than places " + places);
        }
        if (digits > 16) {
            throw new IllegalArgumentException("digits " + digits + " greater than max.");
        }
        long p10 = powersOfTen[digits];
        double bigger = (double) randomLong(positive ? 0 : -p10 + 1, p10 - 1);
        return bigger / (double) powersOfTen[places];
    }

    /**
     * Generates a random double. See {@link randomDouble(int,int,boolean)}.
     */
    public double randomDouble(int digits, int places) {
        return randomDouble(digits, places, false);
    }

    /**
     * Generates a random float. See {@link randomDouble(int,int,boolean)}.
     */
    public float randomFloat(int digits, int places, boolean positive) {
        return (float) randomDouble(digits, places, positive);
    }

    /**
     * Generates a random float. See {@link randomDouble(int,int,boolean)}.
     */
    public float randomFloat(int digits, int places) {
        return (float) randomDouble(digits, places, false);
    }

    /**
     * @param bias
     *            determines relative frequency of true and false. A bias <= 0
     *            always return false, a bias >= 1 always return true.
     * @return a random boolean, chosen according to bias.
     */
    public boolean randomBoolean(double bias) {
        return random() < bias;
    }

    public boolean randomBoolean() {
        return randomBoolean(0.5);
    }

    /**
     * Choose a character randomly from the array.
     */
    public char randomCharFromChars(char[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("zero length array.");
        }
        return array[randomInt(0, array.length - 1)];
    }

    /**
     * Choose a character randomly from the String.
     */
    public char randomCharFromString(String chars) {
        if (chars.length() == 0) {
            throw new IllegalArgumentException("zero length chars String.");
        }
        return randomCharFromChars(chars.toCharArray());
    }

    private final static char[] UPPER_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * @return a random upper-case alphabetic character A-Z.
     */
    public char randomUpperCaseChar() {
        return randomCharFromChars(UPPER_CHARS);
    }

    private final static char[] LOWER_CHARS = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    /**
     * @return a random lower-case alphabetic character a-z.
     */
    public char randomLowerCaseChar() {
        return randomCharFromChars(LOWER_CHARS);
    }

    private final static char[] ALPHA_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    /**
     * @return a random alphabetic character A-Z or a-z.
     */
    public char randomAlphaChar() {
        return randomCharFromChars(ALPHA_CHARS);
    }

    private final static char[] NUMERIC_CHARS = "123456789".toCharArray();

    /**
     * @return a random numeric character 0-9.
     */
    public char randomNumericChar() {
        return randomCharFromChars(NUMERIC_CHARS);
    }

    private final static char[] ALPHANUMERIC_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            .toCharArray();

    /**
     * @return a random alpha-numeric character A-Z or a-z or 0-9.
     */
    public char randomAlphaNumericChar() {
        return randomCharFromChars(ALPHANUMERIC_CHARS);
    }

    private final static char[] TEXT_CHARS =
    // "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"£$%^&*()_-+={[}]:;@'~#<,>.?/|\\".toCharArray();
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"£$%^*()_-+={[}]:;@'~#,>.?/|\\".toCharArray();

    /**
     * @return a random text character A-Z or a-z or 0-9 or
     *         !"£$%^&*()_-+={[}]:;@'~#<,>.?/|\\.
     */
    public char randomTextChar() {
        return randomCharFromChars(TEXT_CHARS);
    }

    /**
     * Return a String whose length is randomly chosen between min and max, made
     * up of characters from the provided array.
     */
    public String randomStringFromChars(char[] array, int minLength, int maxLength) {
        if (array.length == 0) {
            throw new IllegalArgumentException("zero length array.");
        }
        if (minLength < 0) {
            throw new IllegalArgumentException("minLength is < 0");
        }
        int length = randomInt(minLength, maxLength);
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = randomCharFromChars(array);
        }
        return new String(chars);
    }

    /**
     * Return a String of given length, made up of characters from the provided
     * array.
     */
    public String randomStringFromChars(char[] array, int length) {
        return randomStringFromChars(array, length, length);
    }

    /**
     * Return a String whose length is randomly chosen between min and max, made
     * up of characters from the provided array.
     */
    public String randomStringFromStringChars(String chars, int minLength, int maxLength) {
        return randomStringFromChars(chars.toCharArray(), minLength, maxLength);
    }

    /**
     * Return a String of given length, made up of characters from the provided
     * array.
     */
    public String randomStringFromStringChars(String chars, int length) {
        return randomStringFromChars(chars.toCharArray(), length, length);
    }

    /**
     * Return a String whose length is randomly chosen between min and max, made
     * up of numeric characters 0-9.
     */
    public String randomNumericString(int minLength, int maxLength) {
        return randomStringFromChars(NUMERIC_CHARS, minLength, maxLength);
    }

    /**
     * Return a String of given length, made up of numeric characters 0-9.
     */
    public String randomNumericString(int length) {
        return randomStringFromChars(NUMERIC_CHARS, length, length);
    }

    /**
     * Return a String whose length is randomly chosen between min and max, made
     * up of alpha-numeric characters A-Z or a-z or 0-9.
     */
    public String randomAlphaNumericString(int minLength, int maxLength) {
        return randomStringFromChars(ALPHANUMERIC_CHARS, minLength, maxLength);
    }

    /**
     * Return a String of given length, made up of alpha-numeric characters A-Z
     * or a-z or 0-9.
     */
    public String randomAlphaNumericString(int length) {
        return randomStringFromChars(ALPHANUMERIC_CHARS, length, length);
    }

    /**
     * @return a String whose length is randomly chosen between min and max,
     *         made up of random text charactera A-Z or a-z or 0-9 or
     *         !"£$%^&*()_-+={[}]:;@'~#<,>.?/|\\.
     */
    public String randomTextString(int minLength, int maxLength) {
        return randomStringFromChars(TEXT_CHARS, minLength, maxLength);
    }

    /**
     * Produces a random String matching a pattern. Each character in the
     * pattern is copied to the output as follows: u produces a random
     * upper-case alpha character. L produces a random lower-case alpha
     * character. A produces a random upper- or lower-case alpha character. 9
     * produces a random numeric digit 0-9. N produces a random alpha-numeric
     * character A-Z, a-z, or 0-9. / is removed, and the following character
     * copied unchanged. Other characters copied unchanged.
     */
    public String randomPattern(String pattern) {
        char[] patternChars = pattern.toCharArray();
        char[] resultChars = new char[patternChars.length];
        int length = 0;
        for (int i = 0; i < patternChars.length; i++) {
            char ch = patternChars[i];
            switch (ch) {
            case 'u':
                resultChars[length++] = randomUpperCaseChar();
                break;
            case 'L':
                resultChars[length++] = randomLowerCaseChar();
                break;
            case 'A':
                resultChars[length++] = randomAlphaChar();
                break;
            case '9':
                resultChars[length++] = randomNumericChar();
                break;
            case 'N':
                resultChars[length++] = randomAlphaNumericChar();
                break;
            case '/':
                if (++i < patternChars.length) {
                    resultChars[length++] = patternChars[i];
                }
                break;
            default:
                resultChars[length++] = ch;
            }
        }
        return String.valueOf(resultChars, 0, length);
    }

    /**
     * @return a String of given length, made up of random text charactera A-Z
     *         or a-z or 0-9 or !"£$%^&*()_-+={[}]:;@'~#<,>.?/|\\.
     */
    public String randomTextString(int length) {
        return randomStringFromChars(TEXT_CHARS, length, length);
    }

    private final static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * @return a random java.util.Date between two given dates. The date is
     *         always rounded to midnight.
     */
    public Date randomDate(Date from, Date to) {

        Date newDate = randomDateTime(from, to);
        try {
            return df.parse(df.format(newDate));
        } catch (Exception e) {
            // impossible?
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @return a random java.util.Date between two given dates.
     * 
     */
    public Date randomDateTime(Date from, Date to) {
        if (from.after(to)) {
            throw new IllegalArgumentException("from date: " + from + " after to date: " + to);
        }
        // long diff = to.getTime() - from.getTime();
        // diff = 123456 * (long)randomInt( 0, (int)(diff/123456) );
        // Date newDate = new Date(from.getTime() + diff);
        Date newDate = new Date(randomLong(from.getTime(), to.getTime()));
        return newDate;
    }

    // Removed: there is no way to control the length,
    // so can get obscure exceptions from DB.
    // Use randomWords(int minChars, int maxChars) instead.
    // Nick.
    // private String randomTextNotes(int length){
    //
    // StringBuffer notes = new StringBuffer();
    //
    // for (int i = 0; i < length; i++ ) {
    // notes.append(randomTextString(3,7));
    // notes.append(" ");
    // }
    // return notes.toString();
    // }

    private final static char[] WORDS_CHARS = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            + "                                                              " + "!\"£$%^&*()_-+={[}]:;@'~#<,>.?/|\\")
            .toCharArray();

    public String randomWords(int minLength, int maxLength) {
        return randomStringFromChars(WORDS_CHARS, minLength, maxLength);
    }
}