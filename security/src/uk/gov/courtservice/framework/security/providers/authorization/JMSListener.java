package uk.gov.courtservice.framework.security.providers.authorization;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 * Custom JMS Topic listener class, implemented in a similar fashion to a EJB
 * message bean. Implemented without the use of a message bean as this project
 * is deployed prior to the JNDI tree (and therefore the EJB tier) being started
 * (as they use these security classes to determine if they are allowed to
 * start).
 * 
 * This listener is used to force a refresh of the systems security settings if
 * it has been notified of change (listening on the topic whose name is passed
 * into the constructor).
 * 
 * @author tz0d5m
 * @version $Id: JMSListener.java,v 1.4 2007/02/08 12:28:40 rzvddy Exp $
 */
public class JMSListener implements MessageListener {
    /** The log4j <code>Logger</code> instance */
    private final Logger log = Logger.getLogger(getClass());

    // variables values passed into the constructor...
    private final RoleMappings roleMappings;

    private final String factoryName;

    private final String destinationName;

    private final int connectionAttempts;

    private final long connectionSleepTime;

    // both populated in the initialise() method...
    private TopicConnection connection = null;

    private TopicSubscriber subscriber = null;

    /**
     * Only available constructor. Initialises all of the required variables
     * used by this listener.
     * 
     * @param rolemappings
     *            A reference to the <code>RoleMappings</code> instance
     *            deployed to the current server.
     * @param factoryName
     *            The name of the JMS factory used to acquire a
     *            <code>Connection</code> from.
     * @param destinationName
     *            The name of the JMS destination that this class should listen
     *            on for notifications.
     * @param connectionAttempts
     *            The number of attempts allowed to connect to the JMS
     *            destination.
     * @param connectionSleepTime
     *            The amount of time to sleep between each attempt to connect to
     *            the JMS destination (in milliseconds).
     */
    public JMSListener(final RoleMappings rolemappings, final String factoryName, final String destinationName,
            final int connectionAttempts, final long connectionSleepTime) {
        this.factoryName = factoryName;
        this.destinationName = destinationName;
        this.roleMappings = rolemappings;

        this.connectionAttempts = connectionAttempts;
        this.connectionSleepTime = connectionSleepTime;
    }

    /**
     * Whenever a message is received reloads the database role-mappings.
     * 
     * @param message
     *            The posted message, whose value is ignored as this method only
     *            cares that a message occured.
     * 
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    public void onMessage(final Message message) {
        if (log.isDebugEnabled()) {
            log.debug("onMessage() - msg=" + message);
        }

        roleMappings.loadData();

        if (log.isDebugEnabled()) {
            log.debug("onMessage() - END");
        }
}

    /**
     * Indicate that this listener should attempt to start listening. When this
     * method returns there is no guarantee that the listener will actually
     * start, as it is started in an asynchronous call.
     */
    public void startListening() {
        log.debug("startListening() - starting thread...");
        (new Loader(connectionAttempts, connectionSleepTime)).start();
    }

    /**
     * Indicate that this listener should stop listening for notifications. This
     * method will never throw any exceptions, as all will be logged but
     * otherwise ignored.
     * 
     * All JMS resources currently opened by this listener will be closed.
     */
    public void stopListening() {
        log.debug("stopListening() - Stopping Listening");

        closeSubscriber(this.subscriber);
        closeConnection(this.connection);

        // and set them to null to prevent re-running close in finalize...
        this.subscriber = null;
        this.connection = null;
    }

    /**
     * Ensure that this JMS listener class closes any currently open JMS
     * connections. This method directly calls <code>stopListening</code> to
     * close all open resources.
     * 
     * @see java.lang.Object#finalize()
     * @see #stopListening
     */
    public void finalize() {
        stopListening();
    }

    /**
     * Private extracted initialisation method used to attempt to connect to the
     * topic identified in the constructor. This method does not handle any
     * errors thrown whilst attempting to connect to the topic as these can and
     * should by the calling methods.
     * 
     * @throws NamingException
     *             If there is any problems dealing with the required
     *             <code>Context</code>.
     * @throws JMSException
     *             If there is any error whilst creating the JMS
     *             connection/session.
     */
    private void initialise() throws NamingException, JMSException {
        log.debug("initialise() - Initialising");
        Context context = null;

        try {
            context = ContextFactory.getContext();
            log.debug("initialise() - InitialContext created");

            log.info("initialise() - Attempting to locate factory: \"" + factoryName + "\"");
            TopicConnectionFactory topicconnectionfactory = (TopicConnectionFactory) context.lookup(factoryName);

            log.info("initialise() - Attempting to locate topic: \"" + destinationName + "\"");
            Topic topic = (Topic) context.lookup(destinationName);

            this.connection = topicconnectionfactory.createTopicConnection();
            log.debug("initialise() - Connection created");

            final TopicSession topicsession = connection.createTopicSession(false, 1);
            log.debug("initialise() - Session created");

            this.subscriber = topicsession.createSubscriber(topic, null, true);
            log.debug("initialise() - subscriber created");

            this.subscriber.setMessageListener(this);

            // finally indicate that all has been set up and we should now
            // start listening for messages...
            this.connection.start();
        } finally {
            closeContext(context);
        }

        log.debug("initialise() - Completed");
    }

    /**
     * Extracted internal helper method used to close the passed in
     * <code>TopicSubscriber</code> and to ignore/log any errors but allow to
     * continue as normal.
     * 
     * @param topicSubscriber
     *            The <code>TopicSubscriber</code> to close.
     */
    private void closeSubscriber(final TopicSubscriber topicsubscriber) {
        log.debug("closeSubscriber() - Closing subscriber");
        if (topicsubscriber != null) {
            try {
                topicsubscriber.close();
            } catch (final Throwable t) {
                log.error("Error whilst closing subscriber", t);
            }
        }
    }

    /**
     * Extracted internal helper method used to close the passed in
     * <code>Connection</code> and to ignore/log any errors but allow to
     * continue as normal.
     * 
     * @param connection
     *            The <code>Connection</code> to close.
     */
    private void closeConnection(final Connection con) {
        log.debug("closeConnection() - Closing connection");
        if (con != null) {
            try {
                con.stop();
            } catch (final Throwable t) {
                log.error("Error whilst stopping connection", t);
            }

            try {
                con.close();
            } catch (final Throwable t) {
                log.error("Error whilst closing connection", t);
            }
        }
    }

    /**
     * Extracted internal helper method used to close the passed in
     * <code>Context</code> and to ignore/log any errors but allow to continue
     * as normal.
     * 
     * @param context
     *            The <code>Context</code> to close.
     */
    private final void closeContext(final Context context) {
        log.debug("closeContext() - Closing context");
        if (context != null) {
            try {
                context.close();
            } catch (final Throwable t) {
                log.error("Error whilst closing context", t);
            }
        }
    }

    /**
     * Private helper <code>Thread</code> used to initialise the
     * <code>JMSListener</code> that holds this reference. As the servers JNDI
     * tree is probably not loaded when this starts (as this is security code
     * that is called prior to creating the tree) we need to keep attempting to
     * start the listener but allow the rest of the startup to continue.
     * 
     * @author tz0d5m
     */
    private class Loader extends Thread {
        /** The log4j <code>Logger</code> instance */
        private final Logger log = Logger.getLogger(getClass());

        private final long sleepTime;

        private final int allowedAttempts;

        /**
         * Only available constructor, used to create the instance and set all
         * of the required properties.
         * 
         * @param connectionAttempts
         *            The number of attempts allowed to connect to the JMS
         *            destination.
         * @param connectionSleepTime
         *            The amount of time to sleep between each attempt to
         *            connect to the JMS destination (in milliseconds).
         */
        protected Loader(final int allowedAttempts, final long sleepTime) {
            this.allowedAttempts = allowedAttempts;
            this.sleepTime = sleepTime;
        }

        /**
         * Attempt X number of times to start listening to the required JMS
         * queue. As the JNDI tree is probably not available when this is first
         * loaded log errors N number of times, but log a fatal error if failed
         * too many times.
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() {
            // the counter is updated in the catch section...
            for (int i = 1; i <= this.allowedAttempts; i++) {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("run::Sleeping for " + sleepTime + "ms");
                    }

                    Thread.sleep(sleepTime);

                    // access the containing classes initialise method...
                    JMSListener.this.initialise();

                    // would only get here if initialisation was
                    // successful...
                    if (log.isDebugEnabled()) {
                        log.debug("run::Initialisation done on attempt " + i);
                    }

                    // no need to attempt any more as now listening...
                    break;
                } catch (final Throwable t) {
                    log.warn("run::failed attempt " + i);
                    // if this is the last attempt, log it as a fatal error...
                    if (i == this.allowedAttempts) {
                        log.fatal("run::Error initialising listener " + " on attempt " + i, t);
                    }
                    // otherwise, we will only treat it as a warning...
                    else {
                        log.warn("run::Error initialising listener" + " on attempt " + i, t);
                    }
                }
            }
        }
    }
}
