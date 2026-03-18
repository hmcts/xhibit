package uk.gov.courtservice.xhibit.business.services.publicnotice;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * <p>
 * Title:PublicNoticeCourtRoomUnknownException :- thrown if courtRoom is not
 * known in the public Notice Subsystem.
 * </p>
 * <p>
 * Description: see title
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

public class PublicNoticeCourtRoomUnknownException extends CSBusinessException {
	static final long serialVersionUID = -642601943262533056L;
	
    private static final String ERROR_KEY = "publicnotice.courtroom.notfound";

    private static final String LOG_MESSAGE = "Could not find court room for id: ";

    public PublicNoticeCourtRoomUnknownException(Integer courtRoomId) {
        super(ERROR_KEY, LOG_MESSAGE + courtRoomId);
    }

    /**
     * 
     * @param logMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public PublicNoticeCourtRoomUnknownException(Integer courtRoomId, Throwable cause) {
        super(ERROR_KEY, new Object[] {}, LOG_MESSAGE + courtRoomId, cause);
    }
}