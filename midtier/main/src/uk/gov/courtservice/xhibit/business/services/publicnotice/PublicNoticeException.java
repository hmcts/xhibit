package uk.gov.courtservice.xhibit.business.services.publicnotice;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * <p>
 * Title:PublicNoticeException, general Public notice Exception thrown from the
 * Public Notice Subsystem
 * </p>
 * <p>
 * Description:see title
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @authors Pat Fox
 * @version 1.0
 */

public class PublicNoticeException extends CSBusinessException {
	
	static final long serialVersionUID = -2594247490023008245L;
	
    public PublicNoticeException() {
        super();
    }

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
    public PublicNoticeException(String errorKey, String logMessage, Throwable cause) {
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
    public PublicNoticeException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
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
    public PublicNoticeException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     * @param params
     *            parameters for message key
     * @param userMessage
     *            message for user of application
     */
    public PublicNoticeException(String errorKey, Object[] params, String logMessage) {
        super(errorKey, params, logMessage);
    }
}