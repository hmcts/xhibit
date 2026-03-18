package uk.gov.courtservice.framework.services.scheduling;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * Created by IntelliJ IDEA. User: qzd3k3 Date: 13-Mar-2003 Time: 12:42:59 To
 * change this template use Options | File Templates.
 */
public class AsynchronousLoaderException extends CSUnrecoverableException {
    /**
     * 
     * @param logMessage
     */
    public AsynchronousLoaderException(String logMessage) {
        super(logMessage);
    }

    /**
     * 
     * @param cause
     *            original exception caught
     */
    public AsynchronousLoaderException(Throwable cause) {
        super(cause);
    }

    /**
     * 
     * @param cause
     *            original exception caught
     */
    public AsynchronousLoaderException(String logMessage, Throwable cause) {
        super(logMessage, cause);
    }

    public AsynchronousLoaderException() {
        super();
    }
}
