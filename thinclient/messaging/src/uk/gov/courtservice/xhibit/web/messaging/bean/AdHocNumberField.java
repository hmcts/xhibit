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

public class AdHocNumberField extends AbstractField {
    /**
     * The maximum length of the field.
     */
    public static final int MAX_LENGTH = 20;

    /**
     * The valid characters at the start of the field.
     */
    public static final String VALID_STARTING_CHARS = "+0123456789";

    /**
     * The valid characters in the body of the field.
     */
    public static final String VALID_CHARS = "0123456789";

    /**
     * Wheather the field can be empty or null.
     */
    protected boolean nullable = false;

    /**
     * The value of the field.
     */
    protected String number;

    /**
     * Construct a String field with a given value, assumes no length limit
     * 
     * @param newValue
     *            the value
     */
    public AdHocNumberField(String newNumber) {
        setValue(newNumber);
    }

    /**
     * Standard java bean accessor
     * 
     * @return the value
     */
    public String getValue() {
        return number;
    }

    /**
     * Standard java bean setter
     * 
     * @param newValue
     *            the new value
     * @throws IllegalArgumentException
     *             if the value is null and it isn't supposed to be
     */
    public void setValue(String newNumber) {
        if (newNumber == null) {
            newNumber = "";
        }

        if (newNumber.length() == 0) {
            setErrorValue(newNumber);
            setErrorMessageKey("adhocnumberfield.emptyString");
        } else if (newNumber.length() > MAX_LENGTH) {
            setErrorValue(newNumber);
            setErrorMessageKey("adhocnumberfield.tooLong");
        } else if (VALID_STARTING_CHARS.indexOf(newNumber.charAt(0)) < 0) {
            setErrorValue(newNumber);
            setErrorMessageKey("adhocnumberfield.startingDigitInvalid");
        } else {
            char[] characters = newNumber.toCharArray();
            int fieldLength = characters.length;
            for (int i = 1; i < fieldLength; i++) {
                if (VALID_CHARS.indexOf(newNumber.charAt(i)) < 0) {
                    setErrorValue(newNumber);
                    setErrorMessageKey("adhocnumberfield.digitInvalid");
                    i = fieldLength;// drop out of loop.
                }
            }
        }
        number = newNumber;
    }
}