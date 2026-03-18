/**
 * Created by IntelliJ IDEA.
 * User: EDS
 * Date: Nov 29, 2002
 * Time: 4:39:33 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.exceptions;

import uk.gov.courtservice.xhibit.client.exceptions.OrdersGenericRuntimeException;

/**
 * An exception which is thrown if an attempt to write the order data fails.
 * 
 * @author Neil Ellis & Neil Entwistle
 */
public class OrderWriterException extends OrdersGenericRuntimeException {
    /**
     * Create a new exception with a nested exception
     * 
     * @param t
     *            The nested exception.
     */
    public OrderWriterException(Throwable t) {
        super(t);
    }

    /**
     * Create a new exception with the supplied message and a nested exception.
     * 
     * @param s
     *            a string based message describing the exception.
     * @param t
     *            the nested exception.
     */
    public OrderWriterException(String s, Throwable t) {
        super(s, t);
    }

    /**
     * Create a new exception with the supplied message.
     * 
     * @param s
     * @see Exception
     */
    public OrderWriterException(String s) {
        super(s);
    }

}
