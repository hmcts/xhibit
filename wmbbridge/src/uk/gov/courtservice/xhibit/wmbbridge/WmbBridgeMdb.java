package uk.gov.courtservice.xhibit.wmbbridge;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

/**
 * A low latency multi instance bridge, designed to take advantage of WebLogic's
 * resource wrappers.
 * 
 * @author fardellwi
 */
public class WmbBridgeMdb implements MessageDrivenBean, MessageListener {

    /**
     * Serialization id, increment if class structure changes.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The class's logger.
     */
    private static final Logger log = Logger.getLogger(WmbBridgeMdb.class);

    /**
     * The connection factory to use to forward messsages
     */
    private QueueConnectionFactory destinationConnectionFactory;

    /**
     * The destination to forward messages to.
     */
    private Queue destination;

    /**
     * @see MessageDrivenBean#setMessageDrivenContext(MessageDrivenContext)
     */
    public void setMessageDrivenContext(MessageDrivenContext context) {
    }

    /**
     * Factory method.
     */
    public void ejbCreate() throws CreateException {
        try {
            InitialContext ctx = new InitialContext();
            try {
                destinationConnectionFactory = (QueueConnectionFactory) ctx
                        .lookup("java:comp/env/jms/DestinationConnectionFactory");
                if (log.isInfoEnabled()) {
                    log.info("Found connection factory: " + destinationConnectionFactory);
                }
                destination = (Queue) ctx.lookup("java:comp/env/jms/Destination");
                if (log.isInfoEnabled()) {
                    log.info("Found destination: " + destination);
                }
            } finally {
                ctx.close();
            }
        } catch (Exception ne) {
            log.error("An error occurred initialising bridge.", ne);
            throw new CreateException(ne.toString());
        }
    }

    /**
     * @see MessageDrivenBean#ejbRemove()
     */
    public void ejbRemove() {
        // Do Nothing
    }

    /**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(final Message message) {
        long starttime = System.currentTimeMillis();
        try {
            QueueConnection destinationConnection = destinationConnectionFactory.createQueueConnection();
            destinationConnection.start();
            try {
                QueueSession destinationSession = destinationConnection.createQueueSession(false,
                        QueueSession.AUTO_ACKNOWLEDGE);
                try {
                    QueueSender producer = destinationSession.createSender(destination);
                    try {
                        if (message instanceof TextMessage) {
                            String text = ((TextMessage) message).getText();
                            
                            if (log.isDebugEnabled()) {
                                log.debug("About to send message (" + text + ").");
                            }
                            
                            producer.send(destinationSession.createTextMessage(text));
                        }
                    } finally {
                        producer.close();
                    }
                } finally {
                    destinationSession.close();
                }
            } finally {
                destinationConnection.close();
            }
            if (log.isDebugEnabled()) {
                log.debug("Sent message (" + (System.currentTimeMillis() - starttime) + "ms).");
            }
        } catch (JMSException jmse) {
            log.error("A JMS error occurred sending message (" + (System.currentTimeMillis() - starttime) + "ms).", jmse);
            log.error("JMS Linked exception is:");
            Exception ex = jmse.getLinkedException();
            ex.printStackTrace();
        } catch (Exception jmse) {
            log.error("An error occurred sending message (" + (System.currentTimeMillis() - starttime) + "ms).", jmse);
            throw new EJBException(jmse);
        }
    }
}
