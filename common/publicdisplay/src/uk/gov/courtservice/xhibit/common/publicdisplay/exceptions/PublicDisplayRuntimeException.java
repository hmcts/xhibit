package uk.gov.courtservice.xhibit.common.publicdisplay.exceptions;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p/> Title:
 * </p>
 * <p/> <p/> Description:
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.4 $
 */
public class PublicDisplayRuntimeException extends CSUnrecoverableException {
    
	static final long serialVersionUID = 7634714536930897386L;
	
	private static final Logger log = CSServices.getLogger(PublicDisplayRuntimeException.class);

    /**
     * Creates a new PublicDisplayRuntimeException object.
     * 
     * @param message
     *            the message.
     */
    public PublicDisplayRuntimeException(String message) {
        super(message);

        if (this instanceof Warning) {
            log.warn(message, this);
        } else {
            log.fatal(message, this);
        }

    }

    /**
     * Creates a new PublicDisplayRuntimeException object.
     * 
     * @param throwable
     *            the root cause.
     */
    public PublicDisplayRuntimeException(Throwable throwable) {
        super(throwable);

        if (this instanceof Warning) {
            log.warn("No message.", throwable);
        } else {
            log.fatal("No message.", throwable);
        }
    }

    /**
     * Creates a new PublicDisplayRuntimeException object.
     * 
     * @param message
     *            the message.
     * @param throwable
     *            the roor cause.
     */
    public PublicDisplayRuntimeException(String message, Throwable throwable) {
        if (this instanceof Warning) {
            log.warn(message, throwable);
        } else {
            log.fatal(message, throwable);
        }
    }

}
