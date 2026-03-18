/**
 * Created by IntelliJ IDEA.
 * User: EDS
 * Date: Dec 4, 2002
 * Time: 2:09:51 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.exceptions;

/**
 * An exception thrown when transforming an order to a specific display or print
 * format.
 * 
 * @author Neil Ellis & Neil Entwistle
 */
public class OrderTransformException extends uk.gov.courtservice.xhibit.client.exceptions.OrdersGenericException {
    private static final String ERROR_KEY = "order.transform.error";

    /**
     * Create a new exception with a nested exception
     * 
     * @param t
     *            The nested exception.
     */
    public OrderTransformException(Throwable t) {
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
    public OrderTransformException(String s, Throwable t) {
        super(ERROR_KEY, s, t);
    }

    /**
     * Create a new exception with the supplied message.
     * 
     * @param s
     * @see Exception
     */
    public OrderTransformException(String s) {
        super(ERROR_KEY, s);
    }

}
