package uk.gov.courtservice.xhibit.business.exceptions.crestformsbf;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * <p>
 * Title: Exception thrown for XML problems in the CrestForms Service
 * </p>
 * <p>
 * Description: General exception thrown when anything goes wrong in the
 * CrestForm service.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Surtar Bachra
 * @version 1.0
 */

public class CrestFormBFXMLException extends CSBusinessException {
	
	static final long serialVersionUID = 8772070450117069307L;
	
    /**
     * Standard no arguments constructor.
     */
    public CrestFormBFXMLException() {
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
    public CrestFormBFXMLException(String errorKey, String logMessage, Throwable cause) {
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
    public CrestFormBFXMLException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public CrestFormBFXMLException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }

}
