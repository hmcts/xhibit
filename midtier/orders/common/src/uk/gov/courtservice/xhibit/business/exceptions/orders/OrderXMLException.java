package uk.gov.courtservice.xhibit.business.exceptions.orders;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * <p>
 * Title: Exception thrown for XML problems in the Orders Service
 * </p>
 * <p>
 * Description: General exception thrown when anything goes wrong in the orders
 * service.
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

public class OrderXMLException extends CSBusinessException {
	
	static final long serialVersionUID = -8130443740524199426L;
	
    /**
     * Standard no arguments constructor.
     */
    public OrderXMLException() {
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
    public OrderXMLException(String errorKey, String logMessage, Throwable cause) {
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
    public OrderXMLException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
    }

    /**
     * @param errorKey
     *            key to the message for the user of application, stored in the
     *            properties file
     * @param logMessage
     *            error message for log
     */
    public OrderXMLException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }
}