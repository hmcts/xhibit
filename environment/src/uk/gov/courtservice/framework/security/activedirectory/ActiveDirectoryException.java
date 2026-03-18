package uk.gov.courtservice.framework.security.activedirectory;

import java.io.Serializable;

/**
 * A nested runtime exception
 */
public class ActiveDirectoryException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -834145855023005304L;

    /**
     * Initializes with the specified cause
     * 
     * @param cause
     *            exception
     */
    public ActiveDirectoryException(Throwable cause) {
        super(cause);
    }

    /**
     * Initializes with the message
     * 
     * @param message
     */
    public ActiveDirectoryException(String message) {
        super(message);
    }

}
