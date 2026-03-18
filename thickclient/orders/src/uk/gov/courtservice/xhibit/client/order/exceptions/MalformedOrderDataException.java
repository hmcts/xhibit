package uk.gov.courtservice.xhibit.client.order.exceptions;

import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.xhibit.client.exceptions.OrdersGenericRuntimeException;

/**
 * A runtime exception indicating the OrderData has not been stored correctly
 * 
 * @author Neil Ellis & Neil Entwistle
 */

public class MalformedOrderDataException extends OrdersGenericRuntimeException {
    /**
     * Create a GenericRuntimeException with an inner exception.
     * 
     * @param t
     *            the inner exception
     */
    public MalformedOrderDataException(Throwable t) {
        super(t); // To change body of overridden methods use File |
        // Settings | File Templates.
    }

    /**
     * Create a GenericRuntimeException with an inner exception.
     * 
     * @param t
     *            the inner exception
     * @param s
     *            the message associated with the exception
     */
    public MalformedOrderDataException(String s, Throwable t) {
        super(s, t); // To change body of overridden methods use File |
        // Settings | File Templates.
    }

    /**
     * Constructs a MalformedOrderDataException with the chosen error message.
     * 
     * @param s
     *            String representation of the error message.
     */
    public MalformedOrderDataException(String s) {
        super(s);
    }

    /**
     * @see RuntimeException
     */
    public MalformedOrderDataException() {
        super(); // To change body of overridden methods use File |
        // Settings | File Templates.
    }

    /**
     * Overloaded constructor, taking in a Message as argument.
     * 
     * @param Message
     *            a Message obejct
     */
    public MalformedOrderDataException(Message newUserMessage) {
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
    public MalformedOrderDataException(Message newUserMessage, String newLogMsg) {
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
    public MalformedOrderDataException(Message newUserMessage, Throwable cause) {
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
    public MalformedOrderDataException(Message newUserMessage, Throwable cause, String newLogMsg) {
        super(newUserMessage, cause, newLogMsg); // To change body of
        // overridden methods
        // use File | Settings |
        // File Templates.
    }
}
