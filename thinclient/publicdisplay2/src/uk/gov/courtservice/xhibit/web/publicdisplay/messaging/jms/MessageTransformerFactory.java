package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms;

import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;

/**
 * @author pznwc5
 * 
 * A factory class for transforming JMS messages to public display events. The
 * current logic is primitive.
 * 
 * TextMessage: Received from mercator for daily list notifications
 * ObjectMessage: Received from midtier for configuration and data changes
 * 
 * In the future this can use the JMSType message header to have a message
 * repository
 */
public class MessageTransformerFactory {

    /** Text message transformer */
    private static MessageTransformer textMessageTransformer = new TextMessageTransformer();

    /** Object message transformer */
    private static MessageTransformer objectMessageTransformer = new ObjectMessageTransformer();

    /**
     * Don't instantiate me
     * 
     */
    private MessageTransformerFactory() {
    }

    /**
     * Creates a transformer
     * 
     * @param msg
     *            Message to be transformed
     * @return Message to be transformed
     */
    public static PublicDisplayEvent transform(Message msg) {
        if (msg instanceof TextMessage) {
            return textMessageTransformer.transform(msg);
        } else if (msg instanceof ObjectMessage) {
            return objectMessageTransformer.transform(msg);
        } else {
            throw new InvalidMessageException(msg);
        }
    }

}
