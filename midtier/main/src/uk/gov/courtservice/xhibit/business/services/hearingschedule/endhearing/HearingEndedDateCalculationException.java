package uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing;

/**
 * <p>
 * Title: HearingEndedDateCalculationException
 * </p>
 * <p>
 * Description: Exception that will be thrown when the calculation for date
 * failed
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
public class HearingEndedDateCalculationException extends HearingEndedValidationException {
	
	static final long serialVersionUID = 3893366724760705589L;

    /**
     * @roseuid 3E476C890021
     */
    public HearingEndedDateCalculationException() {
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
    public HearingEndedDateCalculationException(String errorKey, String logMessage, Throwable cause) {
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
    public HearingEndedDateCalculationException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public HearingEndedDateCalculationException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }

}
