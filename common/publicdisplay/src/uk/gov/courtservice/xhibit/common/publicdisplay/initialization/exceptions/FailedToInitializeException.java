package uk.gov.courtservice.xhibit.common.publicdisplay.initialization.exceptions;

import uk.gov.courtservice.xhibit.common.publicdisplay.initialization.Initializable;

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
public class FailedToInitializeException extends InitializationException {
	
	static final long serialVersionUID = 6557782042400982156L;
	
    /**
     * Creates a new FailedToInitializeException object.
     * 
     * @param initializable
     *            TODO:
     * @param e
     *            TODO:
     */
    public FailedToInitializeException(Initializable initializable, Exception e) {
        super("Could not initialize '" + initializable.getClass().getName() + "'.");
    }
}
