package uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions;

import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.Fatal;
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
public class RetrievalException extends PublicDisplayRuntimeException implements Fatal {
    /**
     * An object reference could not be retrieved.
     * 
     * @param message
     *            an associated message.
     */
    public RetrievalException(final String message) {
        super(message);
    }

    /**
     * An object reference could not be retrieved.
     * 
     * @param message
     *            an associated message.
     * @param cause
     *            the throwable cause.
     */
    public RetrievalException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * An object reference could not be retrieved.
     * 
     * @param cause
     *            the throwable cause.
     */
    public RetrievalException(final Throwable cause) {
        super(cause);
    }
}
