package uk.gov.courtservice.xhibit.web.messaging;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.messaging.MessagingException;
import uk.gov.courtservice.xhibit.business.services.messaging.MessagingControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.client.im.util.InstantMessageServices;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.messaging.bean.FlattenedTree;
import uk.gov.courtservice.xhibit.web.messaging.util.MessagingUtil;

/**
 * <p>
 * Title: Instant Messaging Session Information management class for thin
 * client.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class manages the Instant Messaging functionality for a thin client, it
 * binds itself to the thin client session at instantiation. Supporting
 * clustering failover, it uses a MessageStore to accumulate messages from the
 * JMS queue awaiting display.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby & Will Fardell
 * @version 1.0
 */
public class IMSessionInfo implements java.io.Serializable, HttpSessionBindingListener {
    /**
     * The attribute name in the session that this will be bound against.
     */
    public static final String INSTANT_MESSAGE_SESSION = "instant_message_session";

    private static final String IM_TOPIC_SELECTOR = "TopicName = ";

    private static final String IM_QUOTE = "'";

    private static final String STR_SLASH = "/";

    private static final String STR_UNDRSCR = "_";

    private static final char CHAR_SLASH = STR_SLASH.charAt(0);

    private static final char CHAR_UNDRSCR = STR_UNDRSCR.charAt(0);

    // The log4j logger
    private static final Logger log = CSServices.getLogger(IMSessionInfo.class);

    // Holds and receives messages pending display. DO NOT access directly
    // use getMessageStore method.
    private transient MessageStore messageStore = null;

    // The possible targets. DO NOT access directly use getDestinationTree
    // method.
    private transient FlattenedTree destinationTree = null;

    // The identifier of the terminal
    private final String terminalIdentifier;

    // The topic that the terminal should be listening to
    // usually going to be based on a location.
    private final String topicName;

    // The full detination that the terminal should be listening to
    private final String formattedLocation;

    // Used for building the flaterned tree
    private final String destinationRootNode;

    /**
     * Creates an instance of IMSessionInfo, automatically binding it to the
     * user's session under the INSTANT_MESSAGE_SESSION attribute name.
     * 
     * @param terminalIdentifier
     *            The identifier for the terminal from which the sesion is
     *            bound.
     * @param topicName
     *            The name of the topic to be listened to, usually will be based
     *            on the terminal location.
     */
    public IMSessionInfo(String terminalIdentifier, String topicName, String destinationRootNode,
            String formattedLocation) {
        // check arguments
        if (terminalIdentifier == null) {
            throw new IllegalArgumentException("terminalIdentifier: null");
        }
        if (topicName == null) {
            throw new IllegalArgumentException("topicName: null");
        }
        if (destinationRootNode == null) {
            throw new IllegalArgumentException("destinationRootNode: null");
        }
        if (formattedLocation == null) {
            throw new IllegalArgumentException("formattedLocation: null");
        }

        // store arguments
        this.terminalIdentifier = terminalIdentifier;
        this.topicName = topicName;
        this.destinationRootNode = destinationRootNode;
        this.formattedLocation = formattedLocation;

        // log arguments
        log.debug("Terminal Identifier:" + terminalIdentifier);
        log.debug("Topic name:" + topicName);
        log.debug("Destination rootNode:" + destinationRootNode);
        log.debug("Formatted location:" + formattedLocation);
    }

    /**
     * This method attempts to stop and cleanup a message session. It is
     * preferable that this be called as part of any cleanup/shutdown/logout
     * process by the user.
     */
    public synchronized void cleanup() {
        if (messageStore != null) {
            messageStore = null;
        }
        if (destinationTree != null) {
            destinationTree = null;
        }
        InstantMessageServicesManager.getInstance().cleanupInstantMessageServices(terminalIdentifier);
    }

    /**
     * HttpSessionBindingListener implementation: do nothing
     * 
     * @param event
     *            contains a reference to the session.
     */
    public void valueBound(HttpSessionBindingEvent event) {
        log.debug("binding to session: " + event.getSession().toString());
    }

    /**
     * HttpSessionBindingListener implementation: clean up
     * 
     * @param event
     *            contains a reference to the session.
     */
    public void valueUnbound(HttpSessionBindingEvent event) {
        log.debug("Unbinding from session: " + event.getSession().toString());
        this.cleanup();
    }

    /**
     * Get the instant messageing service
     * 
     * @return this session's InstantMessageServices instance.
     */
    public synchronized InstantMessageServices getIMServices() throws FrameworkException {
        return InstantMessageServicesManager.getInstance().getInstantMessageServices(terminalIdentifier);
    }

    /**
     * Get the instant messageing service
     * 
     * @return this session's InstantMessageServices instance.
     */
    public synchronized MessagingControllerBeanBusinessDelegate getMessageCBDelegate() throws FrameworkException {
        return MessagingControllerBeanBusinessDelegate.DelegateFactory.getInstance();
    }

    /**
     * Get the topic name
     * 
     * @return the topic name!
     */
    public String getTopicName() {
        return topicName;
    }

    /**
     * Get the formatted location with "/" prefix
     * 
     * @return the formatted location!
     */
    public String getFormattedLocation() {
        return formattedLocation.startsWith(STR_SLASH) ? formattedLocation : STR_SLASH + formattedLocation;
    }

    /**
     * Get the tree model representing the locations available to this instance.
     * 
     * @return A flattened tree model of valid destination.
     */
    public synchronized FlattenedTree getDestinationTree() throws FrameworkException {
        try {
            if (destinationTree == null) {
                destinationTree = MessagingUtil.walkTree(getIMServices().getSortedTreeModel(topicName,
                        getIMServices().convertToTopicTree(getMessageCBDelegate().getTreeModel(terminalIdentifier))));
            }
            return destinationTree;
        } catch (MessagingException ex) {
            log.error("Error while flattening tree.", ex);
            throw new FrameworkException("im.jms.JMSException", "Error while flattening tree.", ex);
        } catch (JMSException ex) {
            log.error("Error while flattening tree.", ex);
            throw new FrameworkException("im.jms.JMSException", "Error while flattening tree.", ex);
        } catch (NamingException ex) {
            log.error("Error while flattening tree.", ex);
            throw new FrameworkException("im.jms.JMSException", "Error while flattening tree.", ex);
        }

    }

    /**
     * Get the message store
     * 
     * @return the message store
     */
    public synchronized MessageStore getMessageStore() throws FrameworkException {
        try {

            if (messageStore == null) {
                messageStore = new MessageStore();
                getIMServices().initialiseDurableReceipt(topicName, messageStore, getSelector(), false);
            }
            return messageStore;
        } catch (JMSException ex) {
            log.error("Could not register the message store", ex);
            throw new FrameworkException("im.jms.JMSException", "Could not register the message store", ex);
        } catch (NamingException ex) {
            log.error("Could not register the message store", ex);
            throw new FrameworkException("im.jms.JMSException", "Could not register the message store", ex);
        }
    }

    /**
     * Method to return the messages accumulated since the last call.
     * 
     * @return The messages as Strings at the moment. May need more richness.
     */
    public String[] getMessages() throws FrameworkException {
        return getMessageStore().getMessages();
    }

    /**
     * Method that indicates whether there are any new messages to be displayed.
     * 
     * @return whether there are any new messages to be displayed.
     */
    public boolean hasNewMessages() throws FrameworkException {
        return getMessageStore().hasNewMessages();
    }

    private String getSelector() {
        StringBuffer selector = new StringBuffer();
        selector.append(IM_TOPIC_SELECTOR);
        selector.append(IM_QUOTE);
        selector.append(formatSelector(destinationRootNode));
        selector.append(IM_QUOTE);
        return selector.toString();
    }

    private String formatSelector(String sel) {
        log.debug("Selector " + sel);
        String formattedSel = sel.replace(CHAR_SLASH, CHAR_UNDRSCR);
        return formattedSel.startsWith(STR_UNDRSCR) ? formattedSel.substring(1) : formattedSel;
    }
}