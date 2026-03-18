package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms;

import javax.jms.Message;

import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.PublicDisplayRuntimeException;

/**
 * 
 * @author pznwc5 The exception is thrown when an unknown JMS message is
 *         received
 */
public class InvalidMessageException extends PublicDisplayRuntimeException {
    /**
     * @param ex
     *            JMS message that is received
     */
    public InvalidMessageException(Message ex) {
        super("Invalid message type: " + ex.getClass());
    }

    /**
     * @param obj
     *            Object that is wrapped in the JMS message
     */
    public InvalidMessageException(Object obj) {
        super("Invalid object type: " + obj.getClass());
    }
}
