package uk.gov.courtservice.xhibit.business.exceptions.messaging;

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
 * @author Bob Boothby
 * @version 1.0
 */

public class InvalidDeviceException extends CSRecoverableException {
	
	static final long serialVersionUID = 4137176313955198584L;
	
    public InvalidDeviceException(String code, String logMessage) {
        super(code, logMessage);
    }

    public InvalidDeviceException(String code, String logMessage, Throwable throwable) {
        super(code, logMessage, throwable);
    }
}