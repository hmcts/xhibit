package uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing;

/**
 * <p>
 * Title: HearingEndedAlreadyException
 * </p>
 * <p>
 * Description: Exception that will be thrown when a hearing has already been
 * ended and an attempt to end it again is made.
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

public class HearingEndedAlreadyException extends HearingEndedValidationException {
	
	static final long serialVersionUID = -8972758893939295726L;

    /**
     * @roseuid 3E476C880373
     */
    public HearingEndedAlreadyException() {
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
    public HearingEndedAlreadyException(String errorKey, String logMessage, Throwable cause) {
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
    public HearingEndedAlreadyException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public HearingEndedAlreadyException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }

}
