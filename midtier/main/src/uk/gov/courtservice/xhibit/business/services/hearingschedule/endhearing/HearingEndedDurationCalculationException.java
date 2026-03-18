package uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing;

/**
 * <p>
 * Title: HearingEndedDurationCalculationException
 * </p>
 * <p>
 * Description: Exception that will be thrown when the calculation for hearing
 * dates fails.
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

public class HearingEndedDurationCalculationException extends HearingEndedValidationException {
	
	static final long serialVersionUID = 5095697297822286880L;

    /**
     * @roseuid 3E476C890176
     */
    public HearingEndedDurationCalculationException() {
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
    public HearingEndedDurationCalculationException(String errorKey, String logMessage, Throwable cause) {
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
    public HearingEndedDurationCalculationException(String errorKey, Object[] parameters, String logMessage,
            Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public HearingEndedDurationCalculationException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }

}
