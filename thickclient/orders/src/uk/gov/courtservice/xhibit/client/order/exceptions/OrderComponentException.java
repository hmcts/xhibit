package uk.gov.courtservice.xhibit.client.order.exceptions;

import uk.gov.courtservice.xhibit.client.exceptions.OrdersGenericRuntimeException;

/**
 * This is a general runtime exception thrown by OrderComponents.
 * 
 * @author Neil Ellis & Neil Entwistle
 */
public class OrderComponentException extends OrdersGenericRuntimeException {
    /**
     * Constructs a OrderComponentException.
     * 
     * @param s
     *            String representation of the error message.
     * @param t
     *            Throwable object
     */
    public OrderComponentException(String s, Throwable t) {
        super(s, t);
    }

    /**
     * Constructs a OrderComponentException.
     * 
     * @param t
     *            Throwable object
     */
    public OrderComponentException(Throwable t) {
        super(t);
    }

    /**
     * Constructs a OrderComponentException.
     * 
     * @param s
     *            String representation of the error message.
     */
    public OrderComponentException(String s) {
        super(s);
    }

    /**
     * Constructs a OrderComponentException.
     */
    public OrderComponentException() {
        super();
    }
}
