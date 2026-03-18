package uk.gov.courtservice.xhibit.web.publicdisplay.messaging;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.event.EventStore;
import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.event.EventStoreFactory;
import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms.MessageReceiver;
import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms.MessagingMode;
import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms.Reinitializer;
import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms.Subscription;
import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.work.EventWorkManager;

/**
 * This class acts as the mediator for processing the asynchronous messages. The
 * class manages the JMS subscriptions and an event store used to buffer the
 * events. The JMS subscriptions push the messages to the event store which are
 * subsequently processed using a thread pool.
 * 
 * @author pznwc5
 */
public class MessageController {

    /** Logger */
    private static Logger log = Logger.getLogger(MessageController.class);

    /** List of JMS subscriptions */
    private List subscriptions = new ArrayList();

    /** Number of workers used by the subscriptions */
    private int numWorkers;

    /**
     * Creates a new MessageController object.
     * 
     * @param numWorkers
     *            The number of workers used by the thread pool.
     */
    public MessageController(int numWorkers) {
        this.numWorkers = numWorkers;
        log.info("Message controller created with no of workers: " + numWorkers);
    }

    /**
     * Adds a JMS subscription
     * 
     * @param dest
     *            The JMS destination
     * @param cf
     *            The JMS connection factory
     */
    public void addSubscription(Destination dest, ConnectionFactory cf, long courtId, MessagingMode messagingMode) {
        try {
            // Create the event store for the subscription
            EventStore eventStore = EventStoreFactory.getEventStore();
            log.debug("Event store created for court: " + courtId);

            // Create the event work manager
            EventWorkManager workManager = new EventWorkManager(eventStore, numWorkers);
            log.debug("Event work manager created for court: " + courtId);

            // Create the JMS subscription and add it to the list of
            // subscriptions
            Subscription sub = Subscription.getSubscription(dest, cf, courtId, messagingMode);
            sub.setWorkManager(workManager);
            sub.setExceptionListener(new Reinitializer());
            subscriptions.add(sub);
            log.debug("JMS subscription created for court: " + courtId);

            // Set the message listener and start the subscription
            MessageReceiver receiver = new MessageReceiver(eventStore);
            sub.setMessageListener(receiver);
            sub.start();
            log.debug("JMS subscription started for court: " + courtId);
        } catch (JMSException ex) {
            throw new UnexpectedJmsException(ex);
        }
    }

    /**
     * Starts processing the event
     */
    public void startEventProcessing() {
        for (Iterator it = subscriptions.iterator(); it.hasNext();) {
            Subscription sub = (Subscription) it.next();
            // Start event processing
            sub.startEventWorkManager();
            log.debug("Event processing started for court: " + sub.getCourtId());
        }
    }

    /**
     * Shutdown all the subscriptions
     * 
     */
    public void shutdown() {
        for (Iterator it = subscriptions.iterator(); it.hasNext();) {
            Subscription sub = (Subscription) it.next();
            // Close subscription
            sub.close();
            log.debug("Subscription closed for court: " + sub.getCourtId());
        }
    }

}
