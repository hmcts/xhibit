package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.UnexpectedJmsException;

/**
 * @author pznwc5
 * 
 * Transformer for transforming object messages. Currently an object message is
 * sent by the midtier when a configuration or hearing data change occurs
 */
public class ObjectMessageTransformer implements MessageTransformer {

    /** Logger */
    private static Logger log = Logger.getLogger(ObjectMessageTransformer.class);

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms.MessageTransformer#transformMessage(javax.jms.Message)
     */
    public PublicDisplayEvent transform(Message msg) {
        try {
            if (!(msg instanceof ObjectMessage)) {
                throw new InvalidMessageException(msg);
            }

            ObjectMessage objMsg = (ObjectMessage) msg;
            log.debug("Object message received: " + objMsg);

            Serializable payload = objMsg.getObject();
            if (!(payload instanceof PublicDisplayEvent)) {
                throw new InvalidMessageException(payload);
            }
            log.debug("Public display event extracted: " + payload);

            return (PublicDisplayEvent) payload;

        } catch (JMSException ex) {
            log.fatal(ex.getMessage(), ex);
            throw new UnexpectedJmsException(ex);
        }
    }

}
