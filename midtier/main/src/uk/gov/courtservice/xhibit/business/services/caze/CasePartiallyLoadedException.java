package uk.gov.courtservice.xhibit.business.services.caze;

/**
 * <p>
 * Title: CasePartiallyLoadedException
 * </p>
 * <p>
 * Description: This class deals with Case partially loaded exceptions
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

public class CasePartiallyLoadedException extends CaseAccessException {
	
	static final long serialVersionUID = -1845747783753837108L;
	
    public CasePartiallyLoadedException() {
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
    public CasePartiallyLoadedException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public CasePartiallyLoadedException(String errorKey, String logMessage) {
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
    public CasePartiallyLoadedException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
    }
}
