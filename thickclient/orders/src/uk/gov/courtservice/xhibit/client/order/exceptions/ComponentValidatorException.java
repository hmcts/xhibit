/**
 * Created by IntelliJ IDEA.
 * User: hzf3bb
 * Date: Jan 22, 2003
 * Time: 10:36:59 AM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.exceptions;

import uk.gov.courtservice.xhibit.client.exceptions.OrdersGenericRuntimeException;

public class ComponentValidatorException extends OrdersGenericRuntimeException {
    /**
     * Constructs a OrderComponentException.
     * 
     * @param s
     *            String representation of the error message.
     * @param t
     *            Throwable object
     */
    public ComponentValidatorException(String s, Throwable t) {
        super(s, t);
    }

    /**
     * Constructs a OrderComponentException.
     * 
     * @param t
     *            Throwable object
     */
    public ComponentValidatorException(Throwable t) {
        super(t);
    }

    /**
     * Constructs a OrderComponentException.
     * 
     * @param s
     *            String representation of the error message.
     */
    public ComponentValidatorException(String s) {
        super(s);
    }

    /**
     * Constructs a OrderComponentException.
     */
    public ComponentValidatorException() {
        super();
    }
}
