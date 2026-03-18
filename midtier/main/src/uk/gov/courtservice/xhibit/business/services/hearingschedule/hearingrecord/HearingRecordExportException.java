package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

/**
 * <p>
 * Title: HearingRecordException
 * </p>
 * <p>
 * Description: The super class of all Export Hearing Record exceptions.
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
public class HearingRecordExportException extends HearingRecordException {
	
	static final long serialVersionUID = -3339633110004955000L;

    public HearingRecordExportException() {
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
    public HearingRecordExportException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public HearingRecordExportException(String errorKey, String logMessage) {
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
    public HearingRecordExportException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
    }
}
