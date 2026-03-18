package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.common.publicdisplay.jms.PublicDisplayJMSConstants;

/**
 * @author meekun
 * 
 * The class provides a P2P subscription
 */
public class P2PSubscription extends Subscription {

    /** Logger */
    private static Logger log = Logger.getLogger(P2PSubscription.class);

    /** Queue connection used by the subscription */
    private QueueConnection con;

    /** Receiver used by the subscription */
    private QueueReceiver receiver;

    /** Court id for which the subscription is made */
    private long courtId;

    /**
     * Creates a new P2PSubscription object.
     * 
     * @param queue
     *            Queue for which the subscription is made
     * @param qcf
     *            Queue connection factory that is used
     * @param courtId
     *            Court Id for which the subscription is made
     */
    public P2PSubscription(Queue queue, QueueConnectionFactory qcf, long courtId) throws JMSException {

        String messageSelector = PublicDisplayJMSConstants.COURT_ID_PROPERTY_NAME + "=" + courtId;
        log.debug("Message selector: " + messageSelector);

        con = qcf.createQueueConnection();
        log.debug("Queue connection created for court: " + courtId);

        QueueSession sess = con.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        log.debug("Queue session created for court: " + courtId);

        receiver = sess.createReceiver(queue, messageSelector);
        log.debug("Queue receiver created for court: " + courtId);

        this.courtId = courtId;
    }

    /**
     * Returns the queue connection
     * 
     * @return Queue connection
     */
    protected Connection getConnection() {
        return con;
    }

    /**
     * Returns the queue receiver
     * 
     * @return Queue receiver
     */
    protected MessageConsumer getConsumer() {
        return receiver;
    }

}
