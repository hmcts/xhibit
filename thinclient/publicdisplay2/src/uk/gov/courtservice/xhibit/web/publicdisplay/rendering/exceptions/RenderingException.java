package uk.gov.courtservice.xhibit.web.publicdisplay.rendering.exceptions;

import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.PublicDisplayRuntimeException;

/**
 * <p/> Title: A general rendering error occured.
 * </p>
 * <p/> <p/> Description: This Exception is recoverable.
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.3 $
 */
public class RenderingException extends PublicDisplayRuntimeException {
    /**
     * A general rendering error occured.
     * 
     * @param message
     *            helpful message.
     */
    public RenderingException(final String message) {
        super(message);
    }

    /**
     * A general rendering error occured.
     * 
     * @param message
     *            helpful message.
     * @param cause
     *            root cause.
     */
    public RenderingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
