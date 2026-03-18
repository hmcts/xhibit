package uk.gov.courtservice.framework.services;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Pete Raymond
 * @version 1.0
 */

public class CSServicesException extends CSUnrecoverableException {

    public CSServicesException() {
    }

    public CSServicesException(String msg) {
        super(msg);
    }

    public CSServicesException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CSServicesException(Throwable cause) {
        super(cause);
    }
}