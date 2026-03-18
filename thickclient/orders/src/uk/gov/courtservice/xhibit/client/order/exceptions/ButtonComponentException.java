/**
 * Created by IntelliJ IDEA.
 * User: hzf3bb
 * Date: Jan 3, 2003
 * Time: 2:16:21 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.exceptions;

import uk.gov.courtservice.xhibit.client.exceptions.OrdersGenericRuntimeException;

public class ButtonComponentException extends OrdersGenericRuntimeException {
    /**
     * Constructs a ButtonComponentException.
     * 
     * @param s
     *            String representation of the error message.
     * @param t
     *            Throwable object
     */
    public ButtonComponentException(String s, Throwable t) {
        super(s, t);
    }

    /**
     * Constructs a ButtonComponentException.
     * 
     * @param t
     *            Throwable object
     */
    public ButtonComponentException(Throwable t) {
        super(t);
    }

    /**
     * Constructs a ButtonComponentException.
     * 
     * @param s
     *            String representation of the error message.
     */
    public ButtonComponentException(String s) {
        super(s);
    }

    /**
     * Constructs a ButtonComponentException.
     */
    public ButtonComponentException() {
        super();
    }
}
