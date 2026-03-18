package uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.exceptions;

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
public class InvalidURIFormatException extends PublicDisplayRuntimeException {
    
	static final long serialVersionUID = 6723428835811946932L;
	
	/**
     * Creates a new InvalidURIFormatException object.
     * 
     * @param message
     *            the message.
     */
    public InvalidURIFormatException(final String message) {
        super(message);
    }

    /**
     * Creates a new InvalidURIFormatException object.
     * 
     * @param uri
     *            the offending URI.
     * @param message
     *            the message.
     */
    public InvalidURIFormatException(final String uri, final String message) {
        super("The URI '" + uri + "' is not in the correct format." + message);
    }
}
