package uk.gov.courtservice.xhibit.business.services.listdistribution;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * <p>
 * Title: MaintainRecipientException
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Laurent Bossard
 * @version 1.0
 */

public class MaintainRecipientException extends CSBusinessException {
	
	static final long serialVersionUID = 3332298713372318028L;
	
    public MaintainRecipientException() {
        super();
    }

    /**
     * 
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public MaintainRecipientException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause);
    }

    /**
     * 
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param parameters
     *            the parameters for the error message
     * @param logMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public MaintainRecipientException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     * @param userMessage
     *            message for user of application
     */
    public MaintainRecipientException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }
}