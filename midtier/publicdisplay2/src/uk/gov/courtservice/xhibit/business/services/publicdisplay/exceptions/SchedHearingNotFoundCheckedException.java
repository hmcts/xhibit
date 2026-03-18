package uk.gov.courtservice.xhibit.business.services.publicdisplay.exceptions;

/**
 * <p>
 * Title: Scheduled Hearing not found exception
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: SchedHearingNotFoundCheckedException.java,v 1.2 2004/03/29
 *          15:59:45 pznwc5 Exp $
 */

public class SchedHearingNotFoundCheckedException extends PublicDisplayCheckedException {
	
	static final long serialVersionUID = -2777208239038638872L;
	
    private static final String errorKey = "publicdisplay.schedhearingnotfound";

    private static final String errorLog = "XhbScheduledHearing not found for id: ";

    public SchedHearingNotFoundCheckedException() {
        super(errorKey, "Scheduled Hearing not found");
    }

    public SchedHearingNotFoundCheckedException(Integer scheduledHearingId) {
        super(errorKey, new Object[] { scheduledHearingId }, errorLog + scheduledHearingId);
    }

    public SchedHearingNotFoundCheckedException(Integer scheduledHearingId, Throwable throwable) {
        super(errorKey, new Object[] { scheduledHearingId }, errorLog + scheduledHearingId, throwable);
    }

}
