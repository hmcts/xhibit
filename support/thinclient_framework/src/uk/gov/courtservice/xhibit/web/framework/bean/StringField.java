package uk.gov.courtservice.xhibit.web.framework.bean;

/**
 * <p>Title: StringField</p>
 * <p>Description: This class provides a java object to be used by a bean
 * mapping an HTML page where the data can be modified.
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * 
 * @author  Edward Cawley, Xdevelopment LLP (2003)
 * @version $id$
 *
 */
import java.io.UnsupportedEncodingException;

public class StringField extends AbstractField {

    /**
     * The value of the field.
     */
    protected String value;

    /**
     * The maximum length of the field.
     */
    protected int length = 0;

    /**
     * Wheather the field can be empty or null.
     */
    protected boolean nullable = false;

    /**
     * Construct a String field with a given value, assumes no length limit
     * 
     * @param newValue
     *            the value
     * @throws java.lang.IllegalArgumentException
     *             if the value is null
     */
    public StringField(String newValue) throws IllegalArgumentException {
        setValue(newValue);
    }

    /**
     * Construct a String field with a given value and length limit, and
     * nullable flag
     * 
     * @param newValue
     *            the value
     * @param newLength
     *            the max length of the field
     * @param newNullable
     *            wheather the field can be blank or null
     * @throws java.lang.IllegalArgumentException
     *             if the value is null and it isn't supposed to be
     */
    public StringField(String newValue, int newLength, boolean newNullable) throws IllegalArgumentException {
        length = newLength;
        nullable = newNullable;
        setValue(newValue);
    }

    /**
     * Standard java bean accessor
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Standard java bean setter
     * 
     * @param newValue
     *            the new value
     * @throws java.lang.IllegalArgumentException
     *             if the value is null and it isn't supposed to be
     */
    public void setValue(String newValue) throws IllegalArgumentException {
        if (newValue == null) {
            if (nullable) {
                newValue = "";
            } else {
                throw new IllegalArgumentException("newValue");
            }
        }

        int newValueLength = getUtf8Length(newValue);

        if (newValueLength == 0 && !nullable) {
            setErrorValue(newValue);
            setErrorMessageKey("stringfield.emptyString");
        }
        if (newValueLength > length && length != 0) {
            setErrorValue(newValue);
            setErrorMessageKey("stringfield.tooLong");
        }
        value = newValue;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the length
     */
    public int getLength() {
        return length;
    }

    private static int getUtf8Length(String str) {
        // Note this is not a very efficient way of calculating this but should
        // not matter
        // for the small number of strings used in judges comments if we cant
        // work it out
        // assume worst case, ie each char is encoded over 3 bytes!
        try {
            return str.length() == 0 ? 0 : str.getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException uee) {
            return str.length() * 3;
        }
    }

}
