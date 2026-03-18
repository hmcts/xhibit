package uk.gov.courtservice.xhibit.client.util;

/**
 * <p>Title: RegExpField</p>
 * <p>Description: Used for evaluating a string against a regular expression</p>
 *
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 *
 */
import org.exolab.castor.util.JakartaOroEvaluator;
import org.exolab.castor.util.RegExpEvaluator;

public abstract class RegExpField extends AbstractField {

    protected final static String NULL_ERROR = "stringfield.emptyString";

    /**
     * The value of the field.
     */
    protected String value;

    /**
     * Wheather the field can be empty or null.
     */
    protected final boolean nullable;

    /**
     * Construct a String field with a given value
     * 
     * @param newValue
     *            the value
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public RegExpField(String newValue) throws IllegalArgumentException {
        this(newValue, false);
    }

    /**
     * Construct a String field with a given value and nullable flag
     * 
     * @param newValue
     *            the value
     * @param newNullable
     *            wheather the field can be blank or null
     * @throws IllegalArgumentException
     *             if the value is null and it isn't supposed to be
     */
    public RegExpField(String newValue, boolean newNullable) throws IllegalArgumentException {
        nullable = newNullable;
        setValue(newValue);
    }

    /**
     * Get the RegExpEvaluator
     * 
     * @return
     */
    public abstract RegExpEvaluator getEvaluator();

    /**
     * Get the appropriate key for the message
     * 
     * @return
     */
    public abstract String getErrorKey();

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
        if (newValue == null || newValue.length() == 0) {
            if (!nullable) {
                setErrorValue(" ");
                setErrorMessageKey(getNullErrorKey());

                // throw new IllegalArgumentException("newValue");
            }
            value = "";
        } else {

            // check to see if value matches regular expression
            if (!getEvaluator().matches(transformValue(newValue))) {
                setErrorValue(newValue);
                setErrorMessageKey(getErrorKey());
            }
            value = transformValue(newValue);
        }
    }

    protected String transformValue(String value) {
        return value;
    }

    protected String getNullErrorKey() {
        return NULL_ERROR;
    }

    /**
     * Return a Regular Expression Evaluator for the given Regular Expression
     * 
     * @param regExp
     *            Regular Expression String
     * @return RegExpEvaluator
     */
    protected static RegExpEvaluator createEvaluator(String regExp) {
        JakartaOroEvaluator evaluator = new JakartaOroEvaluator();
        evaluator.setExpression(regExp);
        return evaluator;
    }
}
