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
public class InvalidURIForRegExpException extends InvalidURIFormatException {
    
	static final long serialVersionUID = 5997459846703501411L;
	
	/**
     * Creates a new InvalidURIForRegExpException object.
     * 
     * @param uri
     *            the offending URI.
     * @param exp
     *            the regular expression it should have matched.
     */
    public InvalidURIForRegExpException(final String uri, final String exp) {
        super("The uri '" + uri + "' does not match the constraining regular expression '" + exp + "'.");
    }
}
