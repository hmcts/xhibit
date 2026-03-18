package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.UnexpectedJmsException;
import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.event.EventStore;

/**
 * DOCUMENT ME!
 * 
 * @author meekun
 * 
 * Asynchronouse message listener
 */
public class MessageReceiver implements MessageListener {

    /** Logger */
    private static Logger log = Logger.getLogger(MessageReceiver.class);

    /** Event store to which the messages are pushed */
    private EventStore eventStore;

    /**
     * Creates the message receiver with the event store
     * 
     * @param eventStore
     */
    public MessageReceiver(EventStore eventStore) {
        this.eventStore = eventStore;
        log.debug("Message receiver created");
    }

    /**
     * Asynchronous notification
     * 
     * @param JMS
     *            Message
     * @throws InvalidMessageException
     *             If the JMS message is not an object message or it doesn't
     *             contain a public display event
     * @throws UnexpectedJmsException
     *             If an unexpected JMS exception occurs
     */
    public void onMessage(Message msg) throws InvalidMessageException, UnexpectedJmsException {
        PublicDisplayEvent event = MessageTransformerFactory.transform(msg);

        eventStore.pushEvent(event);
        log.debug("Event pushed to the event queue");
    }

}
