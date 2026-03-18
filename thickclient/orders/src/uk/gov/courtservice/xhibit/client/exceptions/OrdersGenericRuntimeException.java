/**
 * Created by IntelliJ IDEA.
 * User: EDS
 * Date: Nov 27, 2002
 * Time: 3:05:25 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.exceptions;

import java.io.PrintStream;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;

/**
 * All runtime exceptions used within orders are based on the
 * GenericRuntimeException.
 * 
 * @author Neil Ellis & Neil Entwistle
 * @see RuntimeException
 */
public class OrdersGenericRuntimeException extends CSUnrecoverableException {
    private Throwable inner;

    /**
     * Create a GenericRuntimeException with an inner exception.
     * 
     * @param t
     *            the inner exception
     */
    public OrdersGenericRuntimeException(Throwable t) {
        inner = t;
    }

    /**
     * Create a GenericRuntimeException with an inner exception.
     * 
     * @param t
     *            the inner exception
     * @param s
     *            the message associated with the exception
     */
    public OrdersGenericRuntimeException(String s, Throwable t) {
        super(s);
        inner = t;
    }

    /**
     * @param s
     *            The message.
     * @see RuntimeException
     */
    public OrdersGenericRuntimeException(String s) {
        super(s);
    }

    /**
     * @see RuntimeException
     */
    public OrdersGenericRuntimeException() {
        super();
    }

    /**
     * Overloaded constructor, taking in a Message as argument.
     * 
     * @param Message
     *            a Message obejct
     */
    public OrdersGenericRuntimeException(Message newUserMessage) {
        super(newUserMessage); // To change body of overridden methods use File
        // | Settings | File Templates.
    }

    /**
     * Overloaded constructor, taking in a Message and a string log message as
     * arguments.
     * 
     * @param Message
     *            a Message obejct
     */
    public OrdersGenericRuntimeException(Message newUserMessage, String newLogMsg) {
        super(newUserMessage, newLogMsg); // To change body of overridden
        // methods use File | Settings |
        // File Templates.
    }

    /**
     * Overloaded constructor, taking in a Message and Throwable object as
     * arguments.
     * 
     * @param Message
     *            a Message obejct
     */
    public OrdersGenericRuntimeException(Message newUserMessage, Throwable cause) {
        super(newUserMessage, cause); // To change body of overridden methods
        // use File | Settings | File Templates.
    }

    /**
     * Overloaded constructor, taking in a Message, Throwable object and a
     * string with the error messaage as arguments.
     * 
     * @param Message
     *            a Message obejct
     */
    public OrdersGenericRuntimeException(Message newUserMessage, Throwable cause, String newLogMsg) {
        super(newUserMessage, cause, newLogMsg); // To change body of
        // overridden methods
        // use File | Settings |
        // File Templates.
    }

    /**
     * @param ps
     *            The PrintStream to use for the stack trace.
     * @see RuntimeException
     */
    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
        if (inner != null) {
            ps.println("Inner: ");
            inner.printStackTrace(ps);
        }

    }
}
