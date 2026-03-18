package uk.gov.courtservice.xhibit.business.services.systemadmin;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * <p>
 * Title: Import Export Notification Statuses Exception
 * </p>
 * <p>
 * Description: Exception class for use with impoer export notification
 * statuses.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford
 * @version 1.0
 */

public class ImportExportStatusesControllerException extends CSBusinessException {
	
	static final long serialVersionUID = 676586593716489489L;

    /**
     * 
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public ImportExportStatusesControllerException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause);
    }

    /**
     * 
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
    public ImportExportStatusesControllerException(String errorKey, Object[] parameters, String logMessage,
            Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
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
    public ImportExportStatusesControllerException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }
}