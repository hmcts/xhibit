package uk.gov.courtservice.framework.jdbc.exception;

/**
 * <p>
 * Title: Unable to read binding exception
 * </p>
 * <p>
 * Description: This is an unchecked exception thrown when an IOEXception occurs
 * trying to read the binding
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Meeraj Kunnumpurath
 * @version 1.0
 */
public class UnableToReadBindingException extends DataAccessException {

    /**
     * Creates an instance with the message
     * 
     * @param Message
     */
    public UnableToReadBindingException(String msg) {
        super(msg);
    }

    /**
     * Creates an instance with the message and root cause
     * 
     * @param Message
     * @param Root
     *            cause
     */
    public UnableToReadBindingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}