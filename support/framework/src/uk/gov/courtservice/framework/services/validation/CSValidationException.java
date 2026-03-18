package uk.gov.courtservice.framework.services.validation;

import java.util.Collection;
import java.util.Vector;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * <p>
 * Title: CSValidationException
 * </p>
 * <p>
 * Description: Encapsulates all the error information gathered during
 * validation
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */

public class CSValidationException extends CSBusinessException {
    private Vector userValidationErrorList;

    public CSValidationException() {
        super();
    }

    /**
     * Extended constructor
     * 
     * @param errorMessage
     *            error message (for logging)
     */
    // public CSValidationException( String errorMessage ) {
    // super( errorMessage );
    // }
    /**
     * @param errorMsg
     *            error message (for logging)
     * @param userMessage
     *            error message to be reported to user of the application
     */
    public CSValidationException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }

    /**
     * @param errorMsg
     *            error message (for logging)
     * @param userMessage
     *            error message to be reported to user of the application
     * @param userValidationErrorList
     *            collection of uk.gov.courtservice.framework.exception.Message:
     *            additional validation error messages.
     */
    public CSValidationException(String errorKey, String logMessage, Vector userValidationErrorList) {
        this(errorKey, logMessage);
        this.userValidationErrorList = userValidationErrorList;
    }

    /**
     * @return collection of uk.gov.courtservice.framework.exception.Message
     */
    public Collection getUserValidationErrorList() {
        return this.userValidationErrorList;
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param parameters
     *            the parameters for the error message
     * @param logMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public CSValidationException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param parameters
     *            the parameters for the error message
     * @param logMessage
     *            error message for log
     */
    public CSValidationException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
    }

}
