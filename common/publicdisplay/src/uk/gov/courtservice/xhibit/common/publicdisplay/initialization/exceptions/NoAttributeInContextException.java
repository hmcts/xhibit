package uk.gov.courtservice.xhibit.common.publicdisplay.initialization.exceptions;

import uk.gov.courtservice.xhibit.common.publicdisplay.initialization.InitializationContext;

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
public class NoAttributeInContextException extends InitializationException {
    
	static final long serialVersionUID = -5674126310347034644L;
	
	/**
     * Creates a new NoAttributeInContextException object.
     * 
     * @param context
     *            TODO:
     * @param s
     *            TODO:
     */
    public NoAttributeInContextException(InitializationContext context, String s) {
        super("The attribute '" + s + "' was not found in the InitializationContext '" + context + "'.");
    }
}
