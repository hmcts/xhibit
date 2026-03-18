package uk.gov.courtservice.xhibit.client.im.exception;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: IMGenericException
 * </p>
 * <p>
 * Description: Generic Messaging Exception
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class IMGenericException extends CSRecoverableException {

    private Logger log = CSServices.getLogger(IMGenericException.class);

    /**
     * 
     */
    public IMGenericException() {
        log.error(this);
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
    public IMGenericException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause);
        log.error(logMessage, this);
    }

    /**
     * 
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     */
    public IMGenericException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
        log.error(logMessage, this);
    }

    /**
     * 
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     */
    public IMGenericException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
        log.error(logMessage, this);
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
    public IMGenericException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
        log.error(logMessage, this);
    }
}