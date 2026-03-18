package uk.gov.courtservice.xhibit.courtlog.exceptions;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * Exception class used when a hearing has already ended, but an attempt has
 * been made to end the hearing again (or update an end hearing event)
 * 
 * @author tz0d5m
 * @version $Revision: 1.6 $
 */
public class HearingAlreadyEndedException extends CourtLogBusinessException {
    
	static final long serialVersionUID = 8753048507539751323L;

	// declared this initialisation externally to the constructor to prevent
    // it
    // being left out when adding extra constructors...
    {
        // when processing linked cases, this should not abort the process if
        // the linked case has already ended...
        this.setAborted(false);
    }

    /**
     * Constructor that takes all of the details to be logged and/or raised as a
     * business exception
     * 
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param parameters
     *            The parameters for the message
     * @param logMessage
     *            error message for log
     */
    public HearingAlreadyEndedException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
    }

    /**
     * Constructor that takes all of the details to be logged and/or raised as a
     * business exception
     * 
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public HearingAlreadyEndedException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }

    /**
     * Constructor that takes another business exception to allow this exception
     * to simply wrap the exception.
     * 
     * @param cause
     *            The original exception.
     */
    public HearingAlreadyEndedException(CSBusinessException cause) {
        super(cause);
    }
}
