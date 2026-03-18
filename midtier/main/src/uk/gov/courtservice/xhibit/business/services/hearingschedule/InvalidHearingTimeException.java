package uk.gov.courtservice.xhibit.business.services.hearingschedule;

/**
 * <p>
 * Title: InvalidHearingTimeException
 * </p>
 * <p>
 * Description: Exception thrown when now valid time is available for the
 * creation of a scheduled hearing when creating a new sitting
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Pete Raymond
 */
public class InvalidHearingTimeException extends HearingScheduleException {
	
	static final long serialVersionUID = -2905078147229883344L;
	
    public InvalidHearingTimeException() {
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
    public InvalidHearingTimeException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause);
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
    public InvalidHearingTimeException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
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
    public InvalidHearingTimeException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }
}
