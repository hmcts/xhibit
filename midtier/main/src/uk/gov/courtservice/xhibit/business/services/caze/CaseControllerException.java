package uk.gov.courtservice.xhibit.business.services.caze;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * <p>
 * Title:
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
 * @author Sarah Tong
 * @version 1.0
 */

public class CaseControllerException extends CSBusinessException {
    
	private static final long serialVersionUID = -1110305379889701522L;

	public CaseControllerException() {
        super();
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public CaseControllerException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public CaseControllerException(String errorKey, String logMessage, Throwable e) {
        super(errorKey, logMessage, e);
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
    public CaseControllerException(String errorKey, Object[] parameters, String logMessage, Throwable e) {
        super(errorKey, parameters, logMessage, e);
    }
    
    /**
     * 
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     */
    public CaseControllerException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
    }
}