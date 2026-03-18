package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.common.publicdisplay.jms.PublicDisplayJMSConstants;

/**
 * @author meekun
 * 
 * Provides a publish/subscribe model subscription
 */
public class PubSubSubscription extends Subscription {

    /** Logger */
    private static Logger log = Logger.getLogger(PubSubSubscription.class);

    /** Topic connection */
    private TopicConnection con;

    /** Topic subscriber */
    private TopicSubscriber subscriber;

    /**
     * Creates a new PubSubSubscription object.
     * 
     * @param topic
     *            Topic to which subscription is made
     * @param tcf
     *            Topic connection factory used to make the subscription
     * @param courtId
     *            Court id for which the subscription is made
     */
    public PubSubSubscription(Topic topic, TopicConnectionFactory tcf, long courtId) throws JMSException {

        String messageSelector = PublicDisplayJMSConstants.COURT_ID_PROPERTY_NAME + "=" + courtId;
        log.debug("Message selector: " + messageSelector);

        con = tcf.createTopicConnection();
        log.debug("Topic connection created for court: " + courtId);

        TopicSession sess = con.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        log.debug("Topic session created for court: " + courtId);

        subscriber = sess.createSubscriber(topic, messageSelector, true);
        log.debug("Topic receiver created for court: " + courtId);

        this.courtId = courtId;
    }

    /**
     * Returns the topic connection
     * 
     * @return Topic connection
     */
    protected Connection getConnection() {
        return con;
    }

    /**
     * Returns the topic subscriber
     * 
     * @return Topic subscriber
     */
    protected MessageConsumer getConsumer() {
        return subscriber;
    }

}
