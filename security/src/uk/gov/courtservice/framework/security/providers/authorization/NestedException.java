package uk.gov.courtservice.framework.security.providers.authorization;

/**
 * A nested runtime exception
 * 
 * @author Meeraj Kunnumpurath
 * @version $Id: NestedException.java,v 1.4 2007/02/12 11:14:37 rzvddy Exp $
 */
public class NestedException extends RuntimeException {
    private static final long serialVersionUID = 6796697781410224417L;

    /**
     * Initializes with the specified cause
     * 
     * @param cause
     *            exception
     */
    public NestedException(Throwable cause) {
        super(cause);
    }

    /**
     * Initializes with the message
     * 
     * @param message
     */
    public NestedException(String message) {
        super(message);
    }

}
