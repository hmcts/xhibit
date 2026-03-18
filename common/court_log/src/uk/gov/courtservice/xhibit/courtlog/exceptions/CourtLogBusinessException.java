package uk.gov.courtservice.xhibit.courtlog.exceptions;

import uk.gov.courtservice.framework.exception.CSBusinessException;
import uk.gov.courtservice.framework.exception.CSException;
import uk.gov.courtservice.framework.exception.Message;

/**
 * Super class of all business exceptions used by the court log component
 * 
 * @author pznwc5
 */
public class CourtLogBusinessException extends CSBusinessException {
    
	static final long serialVersionUID = 4641564509865568703L;
	
	/**
     * Local instance of the cause of the exception. If populated, the error
     * messages logged/displayed will be acquired from the originating exception
     */
    private CSException cause;

    /**
     * A flag to set the whole process as aborted. Default is true
     */
    private boolean aborted = true;

    /**
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public CourtLogBusinessException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause);
    }

    /**
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     */
    public CourtLogBusinessException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }

    /**
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public CourtLogBusinessException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
    }

    /**
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     */
    public CourtLogBusinessException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
    }

    /**
     * Constructs a <code>CourtLogBusinessException</code> to act as a wrapper
     * around the passed in instance of <code>CSBusinessException</code>.
     * 
     * @param cause
     *            The original cause of the exception that we want wrapped.
     */
    public CourtLogBusinessException(CSBusinessException cause) {
        // also need to ensure the causes stack trace, etc, will be logged
        super(null, null, cause);
        this.cause = cause;
    }

    /**
     * Overridden method to acquire the message from the original cause if it
     * has been populated. Otherwise the parents getMessage() will be returned.
     * 
     * @see uk.gov.courtservice.framework.exception.CSException#getMessage()
     * @see #CourtLogBusinessException(uk.gov.courtservice.framework.exception
     *      .CSBusinessException)
     */
    public String getMessage() {
        return ((this.cause != null) ? this.cause.getMessage() : super.getMessage());
    }

    /**
     * Overridden method to acquire the user message from the original cause if
     * it has been populated. Otherwise the parents getUserMessage() will be
     * returned.
     * 
     * @see uk.gov.courtservice.framework.exception.CSException#getUserMessage()
     * @see #CourtLogBusinessException(uk.gov.courtservice.framework.exception
     *      .CSBusinessException)
     */
    public String getUserMessage() {
        return ((this.cause != null) ? this.cause.getUserMessage() : super.getUserMessage());
    }

    /**
     * Overridden method to acquire the user message (as Message) from the
     * original cause if it has been populated. Otherwise the parents
     * getUserMessageAsMessage() will be returned.
     * 
     * @see uk.gov.courtservice.framework.exception.CSException#getUserMessageAsMessage()
     * @see #CourtLogBusinessException(uk.gov.courtservice.framework.exception
     *      .CSBusinessException)
     */
    public Message getUserMessageAsMessage() {
        return ((this.cause != null) ? this.cause.getUserMessageAsMessage() : super.getUserMessageAsMessage());
    }

    /**
     * Set the CRUD process as aborted
     * 
     * @param aborted
     */
    protected void setAborted(boolean aborted) {
        this.aborted = aborted;
    }

    /**
     * Checks whether the process has been aborted
     * 
     * @return
     */
    public boolean isAborted() {
        return aborted;
    }
}
