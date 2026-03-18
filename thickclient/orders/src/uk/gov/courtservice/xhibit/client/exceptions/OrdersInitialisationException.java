package uk.gov.courtservice.xhibit.client.exceptions;

/**
 * @author David Duncan??
 */
public class OrdersInitialisationException extends OrdersGenericException {

    /**
     * 
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     * @param cause
     *            original exception caught
     */
    public OrdersInitialisationException(String errorKey, String logMessage, Throwable cause) {
        super(errorKey, logMessage, cause);
    }

    /**
     * 
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     */
    public OrdersInitialisationException(String errorKey, String logMessage) {
        super(errorKey, logMessage);
    }

    /**
     * 
     * @param userMessage
     *            message for user of application
     * @param errorMessage
     *            error message for log
     */
    public OrdersInitialisationException(String errorKey, Object[] parameters, String logMessage) {
        super(errorKey, parameters, logMessage);
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
    public OrdersInitialisationException(String errorKey, Object[] parameters, String logMessage, Throwable cause) {
        super(errorKey, parameters, logMessage, cause);
    }

}
