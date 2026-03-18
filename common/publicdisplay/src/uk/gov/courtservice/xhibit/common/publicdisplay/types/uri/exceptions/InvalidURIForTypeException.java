package uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.exceptions;

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
public class InvalidURIForTypeException extends InvalidURIFormatException {
    
	static final long serialVersionUID = 5632705046548281867L;
	
	/**
     * Creates a new InvalidURIForTypeException object.
     * 
     * @param uri
     *            the URI.
     * @param type
     *            the type.
     */
    public InvalidURIForTypeException(final String uri, final String type) {
        super("The type '" + type + "' supplied in the URI '" + uri + "' was not valid for this URI type.");
    }
}
