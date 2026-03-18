package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

/**
 * <p>
 * Title: HearingRecordHearingNotEndedException
 * </p>
 * <p>
 * Description: If the export has started and someone else is trying to export
 * while the exporting is going on.
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

public class HearingRecordExportInProgressException extends HearingRecordExportException {
	
	static final long serialVersionUID = 813391107679004406L;

    public HearingRecordExportInProgressException() {
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
    public HearingRecordExportInProgressException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public HearingRecordExportInProgressException(String errorKey, String logMessage) {
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
    public HearingRecordExportInProgressException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
    }
}
