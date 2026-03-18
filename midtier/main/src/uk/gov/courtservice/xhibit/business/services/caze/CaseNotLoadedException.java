package uk.gov.courtservice.xhibit.business.services.caze;

/**
 * <p>
 * Title:CaseNotLoadedException
 * </p>
 * <p>
 * Description:The class deals with the case not loaded exception.
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

public class CaseNotLoadedException extends CaseAccessException {
    
	private static final long serialVersionUID = 4627457125941399831L;

	public CaseNotLoadedException() {
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
    public CaseNotLoadedException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public CaseNotLoadedException(String errorKey, String logMessage) {
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
    public CaseNotLoadedException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
    }
}