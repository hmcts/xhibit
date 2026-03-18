package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

/**
 * <p>
 * Title: FormAException
 * </p>
 * <p>
 * Description: Sub class of Hearing Record exceptions. Will be used when an
 * illegal entry is being used for formA validation.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author KudzinC
 * @version 1.0
 */
public class FormAException extends HearingRecordException {

    public FormAException() {
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
    public FormAException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public FormAException(String errorKey, String logMessage) {
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
    public FormAException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
    }
}
