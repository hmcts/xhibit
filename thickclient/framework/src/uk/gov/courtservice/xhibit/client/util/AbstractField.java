package uk.gov.courtservice.xhibit.client.util;

/**
 * <p>
 * Title: AbstractField
 * </p>
 * <p>
 * Description: This class provides common functionality used by fields.
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.7 $
 * 
 * $Log: AbstractField.java,v $
 * Revision 1.7  2006/06/05 12:30:24  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.6 2006/05/31 14:23:51 bzjrnl Change:
 * TI901 Comment: Weblogic Upgrade - Standadise code formatting Revision 1.5
 * 2005/04/20 09:05:46 bzjrnl Merged from 1.4.8.1.
 * 
 * Revision 1.4.8.1 2005/04/20 09:04:56 bzjrnl Added function to check for
 * error.
 * 
 * Revision 1.4 2004/08/12 15:56:19 zz7n1c Merged from 6_X_BRANCH on 12/08/2004
 * 
 * Revision 1.3.100.1 2004/08/05 07:46:34 tzj8k5 56339 - DateField validation
 * 
 * Revision 1.3 2003/03/21 11:48:20 fz0n8j Revised thinclient framework!
 * 
 * Revision 1.3 2003/03/17 11:32:04 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.2 2003/03/11 15:46:38 fz0n8j Added CVS Log comments - ecawley
 * 
 */
public abstract class AbstractField {

    /**
     * The value passed to the beand which caused an error.
     */
    private String errorValue;

    /**
     * The key for the error message to be displayed if there is an error.
     */
    private String errorMessageKey;

    /**
     * Empty constructor
     */
    public AbstractField() {
    }

    /**
     * Standard java bean accessor
     * 
     * @return the value which caused an error
     */
    public String getErrorValue() {
        return errorValue;
    }

    /**
     * Return true if an error has occured
     * 
     * @return true if an error has occured
     */
    public boolean hasError() {
        return errorValue != null;
    }

    /**
     * Standard java bean setter
     * 
     * @param newErrorValue
     *            the error value for the field
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setErrorValue(String newErrorValue) throws IllegalArgumentException {
        if (newErrorValue == null) {
            throw new IllegalArgumentException("newErrorvalue");
        }
        errorValue = newErrorValue;
    }

    /**
     * Standard java bean accessor
     * 
     * @return the error message key
     */
    public String getErrorMessageKey() {
        return errorMessageKey;
    }

    /**
     * Standard java bean setter
     * 
     * @param newErrorMessageKey
     *            the error messaeg key for the field
     * @throws IllegalArgumentException
     *             if the value is null
     */
    public void setErrorMessageKey(String newErrorMessageKey) throws IllegalArgumentException {
        if (newErrorMessageKey == null) {
            throw new IllegalArgumentException("newErrorMessageKey");
        }
        errorMessageKey = newErrorMessageKey;
    }

    /**
     * Set the error value and key
     * 
     * @param newErrorValue
     *            the unparsed value
     * @param newErrorMessagekey
     *            the essage key to use
     */
    public void setError(String newErrorValue, String newErrorMessagekey) {
        setErrorValue(newErrorValue);
        setErrorMessageKey(newErrorMessagekey);
    }
}
