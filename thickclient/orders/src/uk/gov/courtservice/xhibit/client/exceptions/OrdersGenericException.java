/**
 * Created by IntelliJ IDEA.
 * User: EDS
 * Date: Nov 27, 2002
 * Time: 3:05:25 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.exceptions;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * A GenericException is a general exception from which all the client
 * exceptions are derived. It supports nested exceptions.
 * 
 * @author Neil Ellis & Neil Entwistle
 */
public class OrdersGenericException extends CSRecoverableException {

    private Logger log = CSServices.getLogger(OrdersGenericException.class);

    /**
     * 
     */
    public OrdersGenericException() {
        log.error(this);
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
    public OrdersGenericException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause);
        log.error(logMessage, this);
    }

    /**
     * 
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     */
    public OrdersGenericException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
        log.error(logMessage, this);
    }

    /**
     * 
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     */
    public OrdersGenericException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
        log.error(logMessage, this);
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
    public OrdersGenericException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
        log.error(logMessage, this);
    }
}
