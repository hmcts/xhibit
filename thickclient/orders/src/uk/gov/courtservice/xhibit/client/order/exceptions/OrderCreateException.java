package uk.gov.courtservice.xhibit.client.order.exceptions;

import uk.gov.courtservice.xhibit.client.exceptions.OrdersGenericException;

/**
 * <p>
 * Title: Xhibit2 OrderCreateException
 * </p>
 * <p>
 * Description: Thrown when an attempt is made to create an order but it already
 * exists. This exception is thrown to indicate that a choice has been made to
 * proceed.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class OrderCreateException extends OrdersGenericException {
    private static final String ERROR_KEY = "ORDERS_XXX";

    /**
     * 
     */
    public OrderCreateException() {
    }

    /**
     * 
     * @param logMessage
     *            message
     */
    public OrderCreateException(String logMessage) {
        super(ERROR_KEY, logMessage);
    }

    /**
     * 
     * @param cause
     *            original exception caught
     */
    public OrderCreateException(Throwable cause) {
        super(ERROR_KEY, ERROR_KEY, cause);
    }

    /**
     * 
     * @param cause
     *            original exception caught
     */
    public OrderCreateException(String logMessage, Throwable cause) {
        super(logMessage, ERROR_KEY, cause);
    }
}