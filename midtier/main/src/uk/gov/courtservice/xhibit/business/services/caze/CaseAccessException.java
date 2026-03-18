package uk.gov.courtservice.xhibit.business.services.caze;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * <p>
 * Title: CaseAccessException
 * </p>
 * <p>
 * Description: This class deals with case access exceptions
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 */

public class CaseAccessException extends CSBusinessException {
    
	private static final long serialVersionUID = 1870602699583425951L;

	public CaseAccessException() {
        super();
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
    public CaseAccessException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public CaseAccessException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param parameters
     *            the parameters for te error message
     * @param logMessage
     *            error message for log
     */
    public CaseAccessException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
    }
}
