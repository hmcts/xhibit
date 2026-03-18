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

public class MessagingException extends CSRecoverableException {
	
	static final long serialVersionUID = -4472027771993524273L;
	
    public MessagingException(String code, String logMessage) {
        super(code, logMessage);
    }

    public MessagingException(String code, String logMessage, Throwable throwable) {
        super(code, logMessage, throwable);
    }
}