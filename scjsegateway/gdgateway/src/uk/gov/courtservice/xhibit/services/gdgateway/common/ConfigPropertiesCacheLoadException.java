package uk.gov.courtservice.xhibit.services.gdgateway.common;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * <p>
 * Title: Config Properties Cache Load Exception
 * </p>
 * <p>
 * Description: 
 * This is an unchecked exception used by the Config Properties Cache
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @version $Id: ConfigPropertiesCacheLoadException.java,v 1.2 2006/11/08 10:58:00 rzvddy Exp $
 */
public class ConfigPropertiesCacheLoadException extends CSUnrecoverableException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates an instance with the message
     * 
     * @param message
     */
    public ConfigPropertiesCacheLoadException(String message) {
        super(message);
    }

    /**
     * Creates an instance with the message and root cause
     * 
     * @param message
     * @param cause
     */
    public ConfigPropertiesCacheLoadException(String message, Throwable cause) {
        super(message, cause);
    }

}