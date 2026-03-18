package uk.gov.courtservice.framework.exception;

/**
 * <p>
 * Title: CSBusinessException
 * </p>
 * <p>
 * Description: An application exception thrown where there is an error in the
 * business logic of the application.
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

public class CSBusinessException extends CSRecoverableException {
    public CSBusinessException() {
        super();
    }

    /**
     * 
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public CSBusinessException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause);
    }

    /**
     * 
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     */
    public CSBusinessException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }

    /**
     * 
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public CSBusinessException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
    }

    /**
     * 
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     */
    public CSBusinessException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
    }
}