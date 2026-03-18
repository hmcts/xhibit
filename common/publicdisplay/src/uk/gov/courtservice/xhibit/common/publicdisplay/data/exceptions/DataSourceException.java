package uk.gov.courtservice.xhibit.common.publicdisplay.data.exceptions;

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
public class DataSourceException extends PublicDisplayRuntimeException {
	
	static final long serialVersionUID = -6803329782083147232L;
	
    /**
     * Creates a new DataSourceException object.
     * 
     * @param message
     *            the message.
     */
    public DataSourceException(final String message) {
        super(message);
    }

    /**
     * Creates a new DataSourceException object.
     * 
     * @param message
     *            the message.
     * @param cause
     *            the root cause.
     */
    public DataSourceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new DataSourceException object.
     * 
     * @param cause
     *            the root cause.
     */
    public DataSourceException(final Throwable cause) {
        super(cause);
    }
}
