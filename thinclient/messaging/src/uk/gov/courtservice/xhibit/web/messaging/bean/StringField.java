package uk.gov.courtservice.xhibit.web.messaging.bean;

import uk.gov.courtservice.xhibit.web.framework.bean.AbstractField;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */

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
     * @throws IllegalArgumentException
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
     * @throws IllegalArgumentException
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
     * @throws IllegalArgumentException
     *             if the value is null and it isn't supposed to be
     */
    public void setValue(String newValue) throws IllegalArgumentException {
        if (newValue == null) {
            if (nullable) {
                newValue = "";
            } else {
                throw new IllegalArgumentException("newValue");
            }
        } else if (newValue.length() == 0 && !nullable) {
            setErrorValue(newValue);
            setErrorMessageKey("stringfield.emptyString");
        } else if (newValue.length() > length && length != 0) {
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

}