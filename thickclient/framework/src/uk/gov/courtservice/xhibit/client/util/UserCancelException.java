package uk.gov.courtservice.xhibit.client.util;

import uk.gov.courtservice.framework.exception.CSRecoverableException;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class UserCancelException extends CSRecoverableException {

    /**
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public UserCancelException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause); // To change body of overridden
        // methods use File | Settings |
        // File Templates.
    }

    /**
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     */
    public UserCancelException(String errorKey, String logMessage) {
        super(errorKey, logMessage); // To change body of overridden methods
        // use File | Settings | File Templates.
    }

    /**
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     */
    public UserCancelException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage); // To change body of
        // overridden methods
        // use File | Settings |
        // File Templates.
    }

    /**
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public UserCancelException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(errorKey, parameters, logMessage, cause); // To change body of
        // overridden methods
        // use File | Settings |
        // File Templates.
    }

    public UserCancelException() {
    }

    /**
     * Extended constructor
     * 
     * @param errorMessage
     *            error message (for logging)
     */
    public UserCancelException(String errorMessage) {
        super(errorMessage, errorMessage);
    }
}