package uk.gov.courtservice.xhibit.client.im.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.jms.ExceptionListener;
import javax.jms.InvalidClientIDException;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.im.util.sorters.DestinationSorter;
import uk.gov.courtservice.xhibit.client.im.util.sorters.DestinationSorterFactory;

/**
 * <p>
 * Title: Class providing the core JMS functionality for an Instant Messaging
 * application
 * </p>
 * <p>
 * Description:
 * </p>
 * <p/> Provides the capability to publish messages to a topic and to add
 * subscriptions. At the moment all subscriptions go to a single message Re
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
public class InstantMessageServices implements ExceptionListener {
    private static final Logger log = CSServices.getLogger(InstantMessageServices.class);

    private static final String INSTANT_MSG_RESOURCES = "XHIBITInstantMessagingResources_en_GB";

    private static final String IM_DEFAULT_RETRY = "im.default.retry.number";

    private static final String IM_CONN_SLEEP = "im.connection.sleep.period";

    private static final int IM_RETRIES = 20;

    private static final int DEFAULT_SLEEP = 2;

    private static final String JMS_JNDI_PREFIX = "/jms/im/";

    private static final char IM_CHAR_UNDRSCR = '_';

    private static final char IM_CHAR_SLASH = '/';

    private TopicConnection topicConnection;

    private TopicSession topicSession;

    private HashMap subscriptions = new HashMap();

    private HashMap durableSubscriptions = new HashMap();

    private String clientId;

    private int acknowledgment;

    private TopicConnectionFactory topicConnectionFactory;

    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private int _treeDepth = 0;

    private boolean _connectionMade = false;

    /**
     * This constructor allows an instance of InstantMessageServices to be
     * created for use. The methods are designed to only provide the subset of
     * functionality required by Instant Messaging.
     * 
     * @param clientID
     *            the identifier to use to retrieve the correct durable
     *            subscriptions. If this is null, then it will rely on the
     *            clientID provided by the ConnectionFactory/Connection
     *            configuration on the server.
     * @param acknowledgement
     *            the acknowledgement mode to use on the topic session used in
     *            the subscriptions. It is anticipated that the thick client
     *            will use Session.AUTO_ACKNOWLEDGE and the thin client will use
     *            Session.CLIENT_ACKNOWLEDGE.
     * @throws javax.naming.NamingException
     *             when there is a problem in the initial context.
     * @throws javax.jms.JMSException
     *             when there is a problem with the JMS instance.
     */
    public InstantMessageServices(String clientID, int acknowledgment) throws javax.naming.NamingException,
            javax.jms.JMSException {
        log.debug("constructor InstantMessageServices(String clientID, "
                + "int acknowledgment, Subject subject) with clientID = " + clientID + " acknowledgment = "
                + acknowledgment);
        this.clientId = clientID;
        this.acknowledgment = acknowledgment;
        // get TopicConnectionFactory.
        Object obj = getContext().lookup("weblogic/jms/ConnectionFactory");
        topicConnectionFactory = (TopicConnectionFactory) obj;
        // Set-up connection to JMS
        establishConnection(getConnectionRetries(IM_DEFAULT_RETRY));
        log.debug("exiting constructor");
    }

    /**
     * Establish the connections to the JMS topics
     * 
     * @throws JMSException
     * @throws NamingException
     */
    private void establishConnection(int retries) throws JMSException, NamingException {
        log.debug("<<<<<<>>>>> entering establishConnection() retries: " + retries + " <<<<<<>>>>>");
        long sleepPeriod = getRetrySleepPeriod();

        while (topicConnection == null && retries != 0) {
            try {
                log.debug("<<<<<<>>>>>> Trying to get connection <<<<<<>>>>>>");
                // get TopicConnection and start it.
                topicConnection = topicConnectionFactory.createTopicConnection();
                topicConnection.setClientID(this.clientId);
                topicConnection.start();

                // get TopicSession
                topicSession = topicConnection.createTopicSession(false, this.acknowledgment);
                // Set this as the exception listener
                topicConnection.setExceptionListener(this);
            }
            // For the thin client it is useful to know if the clientid is
            // already in use
            // if we specifically catch this exception, it will highlight
            // that the user need to delete their cookies and reestablish
            // their
            // connection
            catch (InvalidClientIDException jmse) {
                try {
                    log.error("JMS Exception " + jmse.getMessage(), jmse);
                    // Sleep for a period and then retry to connect
                    Thread.sleep(sleepPeriod);
                    topicConnection = null;
                    retries--;
                    if (retries == 0) {
                        throw jmse;
                    }
                } catch (InterruptedException ie) {
                    continue;
                }
            } catch (Exception jmse) {
                try {
                    log.error("JMS Exception " + jmse.getMessage(), jmse);
                    // Sleep for a period and then retry to connect
                    Thread.sleep(sleepPeriod);
                    topicConnection = null;
                    retries--;
                    if (retries == 0) {
                        throw new JMSException("JMS is not currently available: " + jmse.getMessage());
                    }
                } catch (InterruptedException ie) {
                    continue;
                }
            }
        }
        _connectionMade = true;
        log.debug("<<<<<>>>>>> GOT CONNECTION <<<<<>>>>>");
    }

    /**
     * This method publishes a text message to the named JMS topic. At the
     * moment, this method should suffice, may need a different publication
     * method in the future.
     * 
     * @param message
     *            The message to be published
     * @param topicName
     *            The name of the topic to publish to
     * @throws javax.naming.NamingException
     *             when there is a problem in the initial context.
     * @throws javax.jms.JMSException
     *             when there is a problem with the JMS instance.
     */
    public void publishTextMessage(String message, String topicName) throws javax.naming.NamingException,
            javax.jms.JMSException {
        publishTextMessage(message, getSelector(topicName), getTopicFromJNDIName(topicName));
    }

    /**
     * This method publishes a text message to the named JMS topic. At the
     * moment, this method should suffice, may need a different publication
     * method in the future.
     * 
     * @param message
     *            The message to be published
     * @param selector
     *            the selector to set on the message
     * @param topicName
     *            The name of the topic to publish to
     * @throws javax.naming.NamingException
     *             when there is a problem in the initial context.
     * @throws javax.jms.JMSException
     *             when there is a problem with the JMS instance.
     */
    public void publishTextMessage(String message, String selector, String topicName)
            throws javax.naming.NamingException, javax.jms.JMSException {
        // get Topic
        Object obj = getContext().lookup(topicName);
        Topic topic = (Topic) obj;

        publishTextMessage(message, topic, selector);
    }

    /**
     * This method publishes a text message to the defined JMS topic. At the
     * moment, this method should suffice, may need a different publication
     * method in the future.
     * 
     * @param message
     *            The message to be published
     * @param topic
     *            The topic to publish to
     * @param selector
     *            the selector to set on the message
     * @throws javax.naming.NamingException
     *             when there is a problem in the initial context.
     * @throws javax.jms.JMSException
     *             when there is a problem with the JMS instance.
     */
    public void publishTextMessage(String message, Topic topic, String selector) throws javax.naming.NamingException,
            javax.jms.JMSException {
        if (topicSession == null) {
            final String NULL_SESSION_ERROR = "Topic Session is null";
            throw new JMSException(NULL_SESSION_ERROR);
        }
        // set up text message
        TextMessage textMessage = topicSession.createTextMessage();
        textMessage.clearBody();
        textMessage.setText(message);
        if (selector.trim().length() > 0) {
            textMessage.setStringProperty("TopicName", selector);
        }
        // retrieve publisher
        TopicPublisher topicPublisher = topicSession.createPublisher(topic);

        topicPublisher.setTimeToLive(12L * 3600L * 1000L); // 12 hours.

        // Publish the message.
        topicPublisher.publish(textMessage);

        // Tidy up.
        topicPublisher.close();
    }

    /**
     * This method sets up the receipt of messages on a non-durable
     * subscription. If a subscription already exists, will replace the
     * MessageListener with the new one. <p/> Probably will not be the favoured
     * receipt method.
     * 
     * @param topicName
     *            The topic to subscribe to
     * @param messageReceiver
     *            the designated class to receive all notifications of new
     *            messages on the subscribed topic.
     * @throws javax.naming.NamingException
     *             when there is a problem in the initial context.
     * @throws javax.jms.JMSException
     *             when there is a problem with the JMS instance.
     */
    public void initialiseNonDurableReceipt(String topicName, MessageListener messageReceiver)
            throws javax.naming.NamingException, javax.jms.JMSException {
        TopicSubscriber topicSubscriber = (TopicSubscriber) subscriptions.get(topicName);
        if (topicSubscriber == null) {
            // Get the topic
            Topic topic = (Topic) getContext().lookup(topicName);

            // Initialise subscriber and add the message receiver.
            topicSubscriber = topicSession.createSubscriber(topic);

            // Store in map for reuse and eventual tidy up.
            subscriptions.put(topicName, topicSubscriber);
        }

        // Update the message listener replacing if already set. Note this is
        // not thread safe !
        topicSubscriber.setMessageListener(messageReceiver);
    }

    /**
     * This method sets up the receipt of messages on a durable subscription. If
     * a subscription already exists, will replace the MessageListener with the
     * new one. <p/> Probably will be the favoured receipt method.
     * 
     * @param topicName
     *            The topic to subscribe to
     * @param messageReceiver
     *            the designated class to receive all notifications of new
     *            messages on the subscribed topic.
     * @throws javax.naming.NamingException
     *             when there is a problem in the initial context.
     * @throws javax.jms.JMSException
     *             when there is a problem with the JMS instance.
     */
    public void initialiseDurableReceipt(String topicName, MessageListener messageReceiver)
            throws javax.naming.NamingException, javax.jms.JMSException {
        initialiseDurableReceipt(topicName, messageReceiver, null, false);
    }

    /**
     * This method sets up the receipt of messages on a durable subscription. If
     * a subscription already exists, will replace the MessageListener with the
     * new one. <p/> Probably will be the favoured receipt method.
     * 
     * @param topicName
     *            The topic to subscribe to
     * @param messageReceiver
     *            the designated class to receive all notifications of new
     *            messages on the subscribed topic.
     * @param selector
     *            The messageselector
     * @param local
     *            If true, stops the receipt of messages the connection has
     *            produced
     * @throws javax.naming.NamingException
     *             when there is a problem in the initial context.
     * @throws javax.jms.JMSException
     *             when there is a problem with the JMS instance.
     */
    public void initialiseDurableReceipt(String topicName, MessageListener messageReceiver, String selector,
            boolean local) throws javax.naming.NamingException, javax.jms.JMSException {
        TopicSubscriber topicSubscriber = (TopicSubscriber) durableSubscriptions.get(topicName);
        if (topicSubscriber == null) {
            // Get the Topic
            Topic topic = (Topic) getContext().lookup(topicName);

            // Initialise durable subscriber and add the message receiver.
            topicSubscriber = topicSession.createDurableSubscriber(topic, topicName, selector, local);

            // Store in map for eventual tidy up.
            durableSubscriptions.put(topicName, topicSubscriber);
        }

        // Update the message listener replacing if already set. Note this is
        // not thread safe !
        topicSubscriber.setMessageListener(messageReceiver);
    }

    /**
     * @return The correct initial context
     * @throws javax.naming.NamingException
     *             when there is a problem with the initial context.
     * @todo replace with the CSServices implementation soon... Or with
     *       appropriate client side equivalent.
     */
    private Context getContext() throws javax.naming.NamingException {
        return CSServices.getServiceLocator().getInitialContext();
    }

    /**
     * Method to be called when done with this instance of
     * InstantMessageServices. Need to consider whether to put in a finalize
     * method, I prefer not, but may be required.
     * 
     * @throws javax.jms.JMSException
     *             when there is a problem cleaning up.
     */
    public void cleanup() throws javax.jms.JMSException {
        // close subscriptions
        Iterator iter = this.subscriptions.values().iterator();
        while (iter.hasNext()) {
            ((TopicSubscriber) iter.next()).close();
        }

        // close durable subscriptions
        iter = this.durableSubscriptions.values().iterator();
        while (iter.hasNext()) {
            ((TopicSubscriber) iter.next()).close();
        }

        durableSubscriptions.clear();

        // close session
        if (topicSession != null) {
            topicSession.close();
        }

        // close connection.
        if (topicConnection != null) {
            topicConnection.close();
        }
    }

    /**
     * This method returns a dynamically constructed TreeModel representing the
     * contexts and Topics contained within them. It is intended to provide the
     * model needed to do a tree view of the available message destinations.
     * <p/> In the future we may wish to provide support for Queues as well.
     * 
     * @param startName
     * @return A populated tree model containing the sub contexts and Topics
     *         below the passed in starting position.
     * @throws NamingException
     *             when there is a naming problem or when the starting name is
     *             nor a Context.
     * @throws JMSException
     *             When there is a problem in the JMS instance.
     */
    public DefaultTreeModel getTreeModel(String startName) throws NamingException, JMSException {
        log.debug("start getTreeModel(String startName) with startName = " + startName);

        Context rootContext = getContext();
        // get the object bound to the start name and confirm it is a Context,
        Object obj = rootContext.lookup(startName);
        if (!(obj instanceof Context))
            throw new NamingException("The name supplied as the root node of the " + "tree is not a context.");

        log.debug("end getTreeModel(String startName)");
        return null; // jndiModel;
    }

    /**
     * This method returns a dynamically constructed TreeModel representing the
     * contexts and Topics contained within them. It is intended to provide the
     * model needed to do a tree view of the available message destinations.
     * <p/> In the future we may wish to provide support for Queues as well.
     * 
     * @param startName
     * @return A populated tree model containing the sub contexts and Topics
     *         below the passed in starting position.
     * @throws NamingException
     *             when there is a naming problem or when the starting name is
     *             nor a Context.
     * @throws JMSException
     *             When there is a problem in the JMS instance.
     */
    public DefaultTreeModel getTreeModel(Integer terminalID) throws NamingException, JMSException {
        log.debug("end getTreeModel(String startName)");
        return null; // jndiModel;
    }

    /**
     * This method returns a dynamically constructed TreeModel representing the
     * contexts and Topics contained within them. It is intended to provide the
     * model needed to do a tree view of the available message destinations.
     * <p/> In the future we may wish to provide support for Queues as well.
     * 
     * @param startName
     * @return A populated tree model containing the sub contexts and Topics
     *         below the passed in starting position.
     * @throws NamingException
     *             when there is a naming problem or when the starting name is
     *             nor a Context.
     * @throws JMSException
     *             When there is a problem in the JMS instance.
     */
    public DefaultTreeModel getSortedTreeModel(String startName, DefaultTreeModel tree) throws NamingException,
            JMSException {
        log.debug("start getTreeModel(String startName) with startName = " + startName);

        // sort tree
        DestinationSorterFactory factory = DestinationSorterFactory.getInstance();
        DestinationSorter sorter = factory.getDestinationSorter(getCourtName(startName));
        DefaultTreeModel jndiModel = new DefaultTreeModel(sorter.sortDestinations((TreeNode) tree.getRoot()));

        setTreeModelDepth(((DefaultMutableTreeNode) jndiModel.getRoot()).getDepth());

        log.debug("end getTreeModel(String startName)");
        return jndiModel;
    }

    private String getCourtName(String startName) {
        // trim everything before the last '/'
        System.err.println("getCourtName() for " + startName + " returning "
                + startName.substring(startName.lastIndexOf("/") + 1).toUpperCase());
        return startName.substring(startName.lastIndexOf("/") + 1).toUpperCase();
    }

    /**
     * Listen for exceptions being thrown by JMS. If an exception is thrown, we
     * need to inform any listening clients to reset the connection. Can't reset
     * the connections internally as the Context is not correct. On testing it
     * was found that we don't always get this exception, so the client should
     * also have a mechanism for resetting the JMS connection.
     * 
     * @param exception
     */
    public void onException(JMSException exception) {
        log.error("<<<<<>>>>>> INstantMessageServices.onException <<<<<>>>>>");
        // Set connection flag to false to indicate that connection has been
        // lost
        boolean originalConnection = _connectionMade;
        _connectionMade = false;

        // Fire event to any listeners currently listening
        changeSupport.firePropertyChange("status", originalConnection, _connectionMade);
    }

    private int getConnectionRetries(String key) throws CSConfigurationException {
        // Get properties for disposal references...
        try {
            return Integer.parseInt(getResource(key));
        } catch (NumberFormatException ex) {
            return IM_RETRIES;
        } catch (MissingResourceException ex) {
            return IM_RETRIES;
        }
    }

    /**
     * Return the configurable period to sleep between connection retrys
     * 
     * @return
     */
    private long getRetrySleepPeriod() {
        try {
            return Long.parseLong(getResource(IM_CONN_SLEEP)) * 1000L;
        } catch (CSConfigurationException ex) {
            handleError(ex);
        } catch (NumberFormatException ex) {
            handleError(ex);
        } catch (MissingResourceException ex) {
            handleError(ex);
        }
        return DEFAULT_SLEEP * 1000L;
    }

    /**
     * Get the resource given the key
     * 
     * @param key
     * @return the resource value
     * @throws CSConfigurationException
     * @throws NumberFormatException
     * @throws MissingResourceException
     */
    private String getResource(String key) throws CSConfigurationException, NumberFormatException,
            MissingResourceException {
        ResourceBundle bundle = CSServices.getConfigServices().getBundle(INSTANT_MSG_RESOURCES);
        return bundle.getString(key);
    }

    /**
     * Delegate error handling to default handler
     * 
     * @param ex
     */
    private void handleError(Exception ex) {
        CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
    }

    /**
     * This method returns a the depth of the dynamically constructed TreeModel
     * representing the contexts and Topics contained within them. As JMS topics
     * are only on one server, we can determine if the connection is to the
     * correct server. JMS will connect to the other server, but will not be
     * able to retrieve any topics therefore, the depth of the tree will be
     * zero. <p/>
     * 
     * @return The depth of the tree under the root. If the depth is zero, the
     *         tree has not been contructed proiperly (possibly due to a server
     *         failure)
     */
    public int getTreeModelDepth() {
        return _treeDepth;
    }

    /**
     * This method sets the depth of the dynamically constructed TreeModel
     * representing the contexts and Topics contained within them. It is
     * intended to provide the model needed to do a tree view of the available
     * message destinations. <p/>
     * 
     * @param The
     *            depth of the tree under the root. If the depth is zero, the
     *            tree has not been contructed proiperly (possibly due to a
     *            server failure)
     */
    private void setTreeModelDepth(int depth) {
        _treeDepth = depth;
    }

    /**
     * This method returns true if a connection has been made to the server.
     * Used in combination with the tree depth, it can be determined if the
     * connection is to the server holding the topics. <p/>
     * 
     * @return true if connection made
     */
    public boolean isConnectionMade() {
        return _connectionMade;
    }

    /**
     * Adds a property change listener to the OrderStatus
     * 
     * @param propertyChangeListener
     *            The PropertyChangeListener to use.
     * @see java.beans.PropertyChangeSupport
     */
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        changeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    /**
     * @param propertyChangeListener
     * @see java.beans.PropertyChangeSupport
     */
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        changeSupport.removePropertyChangeListener(propertyChangeListener);
    }

    /**
     * If the string contains the full jms.jndi name, we need to strip off the
     * prefix (/jms/im/) and convert all forward slashes to underscores to
     * produce the JMS selector
     * 
     * @param topic
     *            the topic as a string
     * @return the selector
     */
    private String getSelector(String topic) {
        if (topic.startsWith(JMS_JNDI_PREFIX)) {
            return topic.substring(JMS_JNDI_PREFIX.length()).replace(IM_CHAR_SLASH, IM_CHAR_UNDRSCR);
        } else {
            return topic;
        }
    }

    /**
     * Return the JNDI name for the court topic from the full destination
     * 
     * @param topic
     *            the full topic
     * @return the truncated topic name
     */
    private String getTopicFromJNDIName(String topic) {
        StringBuffer buf = new StringBuffer();
        if (topic.startsWith(JMS_JNDI_PREFIX)) {
            buf.append(topic.substring(0, JMS_JNDI_PREFIX.length()).replace(IM_CHAR_UNDRSCR, IM_CHAR_SLASH));
            buf.append(getCourtFromTopic(topic.substring(JMS_JNDI_PREFIX.length())));
        } else {
            return topic;
        }
        return buf.toString();
    }

    /**
     * Remove the court element form the full JNDI topic name
     * 
     * @param topic
     *            the topic (with the "/jms/im/" prefix removed)
     * @return the court name
     */
    private String getCourtFromTopic(String topic) {
        final String SLASH_DELIM = "/";
        StringTokenizer nameToken = new StringTokenizer(topic, SLASH_DELIM);
        // Retrun the first token - the Court
        return nameToken.nextToken();
    }

    /**
     * Public interface for tree conversion
     * 
     * @param tree
     *            the tree to restructure
     * @return the new tree
     * @throws JMSException
     *             when creating the nodes
     * @throws NamingException
     *             when creating the nodes
     */
    public DefaultTreeModel convertToTopicTree(DefaultTreeModel tree) throws JMSException, NamingException {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getRoot();
        return new DefaultTreeModel(walkTree(root, new JMSContextNode(null, ((DefaultMutableTreeNode) root)
                .getUserObject().toString())));
    }

    /**
     * Convert the tree supplied (with DefaultMutableTreeNodes) into one using
     * JMSContextNodes and TopicNodes
     * 
     * @param root
     *            the root node
     * @param parent
     *            the parent node
     * @return the retrsuctured tree
     * @throws NamingException
     *             when creating the nodes
     * @throws JMSException
     *             when creating the nodes
     */
    private TreeNode walkTree(TreeNode root, TreeNode parent) throws NamingException, JMSException {
        JMSContextNode ctxNode = new JMSContextNode((JMSContextNode) parent, ((DefaultMutableTreeNode) root)
                .getUserObject().toString());

        DefaultMutableTreeNode checkedRoot = new DefaultMutableTreeNode(root);
        Enumeration children = root.children();
        for (int i = 0; children.hasMoreElements(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
            if (child.getChildCount() > 0) {
                if (child.getChildCount() > 0) {
                    ctxNode.add(walkTree(child, parent));
                }
            } else {
                ctxNode.add(new TopicNode(child.getUserObject().toString(), ctxNode));
            }
        }

        return ctxNode;
    }

}
