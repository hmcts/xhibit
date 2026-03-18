package uk.gov.courtservice.xhibit.common.publicdisplay.initialization.exceptions;

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
public class InitializationException extends PublicDisplayRuntimeException {
    
	static final long serialVersionUID = 5188270237868907417L;
	
	/**
     * Creates a new InitializationException object.
     * 
     * @param message
     *            TODO:
     */
    public InitializationException(final String message) {
        super(message);
    }

    /**
     * Creates a new InitializationException object.
     * 
     * @param message
     *            TODO:
     * @param cause
     *            TODO:
     */
    public InitializationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new InitializationException object.
     * 
     * @param cause
     *            TODO:
     */
    public InitializationException(final Throwable cause) {
        super(cause);
    }
}
