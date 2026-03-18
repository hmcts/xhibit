package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * Thrown when a problem is encountered in the Utilities.
 * <p>
 * Renamed when moved to new Package to better distinguish from old.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.1
 */
public class QueryUtilsException extends CSBusinessException {
	private static final long serialVersionUID = -1983041727190924099L;
    public QueryUtilsException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause);
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
    public QueryUtilsException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
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
    public QueryUtilsException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     * @param userMessage
     *            message for user of application
     */
    public QueryUtilsException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }
}