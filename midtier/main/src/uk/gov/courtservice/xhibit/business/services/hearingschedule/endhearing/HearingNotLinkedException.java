package uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing;

/**
 * <p>
 * Title: HearingNotLinkedException
 * </p>
 * <p>
 * Description: Exception that will be thrown when attempting to end all
 * hearings and where there are no linked hearings.
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

public class HearingNotLinkedException extends HearingEndedValidationException {
	
	static final long serialVersionUID = -7735086030477258899L;

    /**
     * @roseuid 3E476C8A0308
     */
    public HearingNotLinkedException() {
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
    public HearingNotLinkedException(String errorKey, String logMessage, Throwable cause) {
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
    public HearingNotLinkedException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public HearingNotLinkedException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }
}
