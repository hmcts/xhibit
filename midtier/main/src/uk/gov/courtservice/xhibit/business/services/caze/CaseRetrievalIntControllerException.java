package uk.gov.courtservice.xhibit.business.services.caze;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * <p>
 * Title: CaseRetrievalIntControllerException
 * </p>
 * <p>
 * Description: This class deals with CaseRetrievalIntController exceptions
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 */

public class CaseRetrievalIntControllerException extends CSBusinessException {
	
	static final long serialVersionUID = -1314219063179524111L;
	
    public CaseRetrievalIntControllerException() {
        super();
    }

    /**
     * 
     * @param userMessage
     *            message for user of application
     * @param cause
     *            original exception caught
     */
    public CaseRetrievalIntControllerException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }

    /**
     * 
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public CaseRetrievalIntControllerException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause);
    }
}
