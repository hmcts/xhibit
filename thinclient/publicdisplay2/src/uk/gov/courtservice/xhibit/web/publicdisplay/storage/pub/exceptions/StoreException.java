package uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions;

import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.PublicDisplayRuntimeException;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.3 $
 */
public class StoreException extends PublicDisplayRuntimeException {
    /**
     * Creates a new StoreException object.
     * 
     * @param message
     *            an associated message.
     */
    public StoreException(final String message) {
        super(message);
    }

    /**
     * Creates a new StoreException object.
     * 
     * @param message
     *            an associated message.
     * @param cause
     *            the Throwable cause.
     */
    public StoreException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
