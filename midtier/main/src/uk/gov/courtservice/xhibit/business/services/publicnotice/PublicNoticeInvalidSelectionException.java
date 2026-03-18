package uk.gov.courtservice.xhibit.business.services.publicnotice;

/**
 * <p>
 * Title:PublicNoticeInvalidSelectionException, thrown when the public notices
 * selected as Active break the Validation rules
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @authors Pat Fox
 * @version 1.0
 */

public class PublicNoticeInvalidSelectionException extends PublicNoticeException {
	static final long serialVersionUID = 1985744394271724507L;
    private static final String MAX_PN_EXCEEDED = "publicnotice.selection.maxexceeded";

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
    public PublicNoticeInvalidSelectionException(Integer maxAllowed, String logMessage, Throwable cause) {
        super(MAX_PN_EXCEEDED, new Integer[] { maxAllowed }, logMessage, cause);
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
    public PublicNoticeInvalidSelectionException(Integer maxAllowed, String logMessage) {
        super(MAX_PN_EXCEEDED, new Integer[] { maxAllowed }, logMessage);
    }
}