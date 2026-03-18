package uk.gov.courtservice.xhibit.web.framework.bean;

/**
 * <p>
 * Title: IntField
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

public class IntField extends AbstractField {

    /**
     * The value of the field.
     */
    private int value;

    /**
     * Construct a Int field with a given value
     * 
     * @param newValue
     *            the value
     */
    public IntField(int newValue) {
        setValue(newValue);
    }

    /**
     * Standard java bean accessor
     * 
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Standard java bean setter
     * 
     * @param newValue
     *            the new value
     */
    public void setValue(int newValue) {
        value = newValue;
    }

}
