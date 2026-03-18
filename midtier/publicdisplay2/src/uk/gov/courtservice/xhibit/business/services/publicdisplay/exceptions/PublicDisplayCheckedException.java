package uk.gov.courtservice.xhibit.business.services.publicdisplay.exceptions;

import uk.gov.courtservice.framework.exception.CSRecoverableException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: PublicDisplayCheckedException.java,v 1.1 2004/01/15 10:38:16
 *          pznwc5 Exp $
 */

public class PublicDisplayCheckedException extends CSRecoverableException {
	
	static final long serialVersionUID = 1772687658620261019L;
	
    public PublicDisplayCheckedException() {
        super();
    }

    public PublicDisplayCheckedException(String errorKey, String logMessage, Throwable throwable) {
        super(errorKey, logMessage, throwable);
    }

    public PublicDisplayCheckedException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }

    public PublicDisplayCheckedException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
    }

    public PublicDisplayCheckedException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
    }

}