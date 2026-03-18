package uk.gov.courtservice.xhibit.client.order.exceptions;

/**
 * A general exception thrown when attempting to read an order.
 * 
 * @author Neil Ellis & Neil Entwistle
 */
public class OrderReaderException extends uk.gov.courtservice.xhibit.client.exceptions.OrdersGenericException {
    private static final String ERROR_KEY = "order.reader.error";

    /**
     * Create a new exception with a nested exception
     * 
     * @param t
     *            The nested exception.
     */
    public OrderReaderException(Throwable t) {
        super(ERROR_KEY, ERROR_KEY, t);
    }

    /**
     * Create a new exception with the supplied message and a nested exception.
     * 
     * @param s
     *            a string based message describing the exception.
     * @param t
     *            the nested exception.
     */
    public OrderReaderException(String s, Throwable t) {
        super(ERROR_KEY, s, t);
    }

    /**
     * Create a new exception with the supplied message.
     * 
     * @param s
     * @see Exception
     */
    public OrderReaderException(String s) {
        super(ERROR_KEY, s);
    }

}
