package uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * <p>
 * Title: HearingEndedValidationException
 * </p>
 * <p>
 * Description: Superclass for all end hearing exceptions.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */
public class HearingEndedValidationException extends CSBusinessException {
	
	static final long serialVersionUID = -8802660138417876823L;

    /**
     * @roseuid 3E476C89036B
     */
    public HearingEndedValidationException() {
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
    public HearingEndedValidationException(String errorKey, String logMessage, Throwable cause) {
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
    public HearingEndedValidationException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public HearingEndedValidationException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }
}
