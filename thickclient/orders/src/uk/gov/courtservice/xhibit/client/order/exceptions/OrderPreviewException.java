package uk.gov.courtservice.xhibit.client.order.exceptions;

/**
 * A general exception thrown when dealing with the Order Preview functionality.
 * 
 * @author Neil Ellis & Neil Entwistle
 */
public class OrderPreviewException extends uk.gov.courtservice.xhibit.client.exceptions.OrdersGenericException {

    private static final String ERROR_KEY = "ORDERS_XXX";

    /**
     * Create a new exception with a nested exception
     * 
     * @param t
     *            The nested exception.
     */
    public OrderPreviewException(Throwable t) {
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
    public OrderPreviewException(String s, Throwable t) {
        super(ERROR_KEY, s, t);
    }

    /**
     * Create a new exception with the supplied message.
     * 
     * @param s
     * @see Exception
     */
    public OrderPreviewException(String s) {
        super(ERROR_KEY, s);
    }

}
