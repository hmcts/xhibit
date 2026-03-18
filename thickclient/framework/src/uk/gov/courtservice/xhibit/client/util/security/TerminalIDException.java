package uk.gov.courtservice.xhibit.client.util.security;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * <p>
 * Title: TerminalIDException
 * </p>
 * <p>
 * Description: Thrown by the TerminalID when an error occurs.
 */
public class TerminalIDException extends CSUnrecoverableException {
    private static final long serialVersionUID = 1L;

    /**
     * Construct a new TerminalIDException with the specified cause.
     */
    public TerminalIDException(Throwable cause) {
        super(cause);
    }

    /**
     * Construct a new TerminalIDException with the specified message and
     * cause.
     */
    public TerminalIDException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Construct a new TerminalIDException with the specified message and
     * cause.
     */
    public TerminalIDException(String msg) {
        super(msg);
    }
}