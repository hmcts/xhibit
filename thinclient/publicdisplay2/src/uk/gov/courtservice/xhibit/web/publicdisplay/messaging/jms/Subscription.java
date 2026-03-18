package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.work.EventWorkManager;

/**
 * @author meekun
 * 
 * Common abstraction for P2P and publish/subscribe messaging
 */
public abstract class Subscription {

    /** Logger */
    private static Logger log = Logger.getLogger(Subscription.class);

    /** Court id for which the subscription is made */
    protected long courtId;

    /** Work manager used for the subscription */
    private EventWorkManager eventWorkManager;

    /**
     * Returns the connection associated with the subscription
     * 
     * @return JMS connection
     */
    protected abstract Connection getConnection();

    /**
     * Returns the consumer associated with subscription
     * 
     * @return JMS subscription
     */
    protected abstract MessageConsumer getConsumer();

    /**
     * Factory method for getting a subscription
     * 
     * @param dest
     *            Destination to which the subscription is made
     * @param cf
     *            Connection factory used to make the subscription
     * @param courtId
     *            Court id for which the subscription is made
     * 
     * @return Subscription for receiving JMS messages
     */
    public static Subscription getSubscription(Destination dest, ConnectionFactory cf, long courtId,
            MessagingMode messagingMode) throws JMSException {
        Subscription sub;

        // Publish/SUbscribe subscription
        if (messagingMode == MessagingMode.PUB_SUB) {
            Topic topic = (Topic) dest;
            TopicConnectionFactory tcf = (TopicConnectionFactory) cf;
            sub = new PubSubSubscription(topic, tcf, courtId);
        }
        // P2P subscription
        else if (messagingMode == MessagingMode.P2P) {
            Queue queue = (Queue) dest;
            QueueConnectionFactory qcf = (QueueConnectionFactory) cf;
            sub = new P2PSubscription(queue, qcf, courtId);
        } else {
            throw new IllegalArgumentException("Unknown messaging mode");
        }
        log.debug("Subscription type: " + sub.getClass());

        return sub;
    }

    /**
     * Set the message listener
     * 
     * @param listener
     *            Message listener
     */
    public void setMessageListener(MessageListener listener) throws JMSException {
        getConsumer().setMessageListener(listener);
        log.debug("Message listener set for court: " + courtId);
    }

    /**
     * Set the exception listener
     * 
     * @param listener
     *            Exception listener
     */
    public void setExceptionListener(ExceptionListener listener) throws JMSException {
        getConnection().setExceptionListener(listener);
        log.debug("Exception listener set for court: " + courtId);
    }

    /**
     * Receives a message blockingly
     * 
     * @return
     */
    public Message receive() throws JMSException {
        return getConsumer().receive();
    }

    /**
     * Receives a message blockingly
     * 
     * @return
     */
    public Message receive(long timeout) throws JMSException {
        return getConsumer().receive(timeout);
    }

    /**
     * Starts JMS message reception
     */
    public void start() throws JMSException {
        getConnection().start();
        log.debug("Connection started for court: " + courtId);
    }

    /**
     * Closes the subscription and shuts down the event manager
     */
    public void close() {
        try {
            getConnection().close();
            log.debug("Connection closed for court: " + courtId);
        } catch (JMSException ex) {
            log.error(ex.getMessage(), ex);
        }
        if (eventWorkManager != null) {
            eventWorkManager.shutDown();
            log.debug("Work manager shutdown for court: " + courtId);
        }
    }

    /**
     * Starts the event work manager
     */
    public void startEventWorkManager() {
        eventWorkManager.start();
        log.debug("Work manager started for court: " + courtId);
    }

    /**
     * Sets the work manager for the subscription
     * 
     * @param eventWorkManager
     *            Work manager
     */
    public void setWorkManager(EventWorkManager eventWorkManager) {
        this.eventWorkManager = eventWorkManager;
        log.debug("Work manager set for court: " + courtId);
    }

    /**
     * Returns the court id
     * 
     * @return courtId
     */
    public long getCourtId() {
        return courtId;
    }

}
