package uk.gov.courtservice.framework.services.subscription;

// JDK
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Joseph Babad
 * @version $Id: XhibitSubscriptionHelper.java,v 1.7 2006/04/12 13:18:33 bzjrnl
 *          Exp $
 * 
 * <Change History/>
 * 
 * <P>
 * 27/02/03 - JB - Added facility to remove a subscriber.
 * </P>
 */

public class XhibitSubscriptionHelper {
    private static Logger log = CSServices.getLogger(XhibitSubscriptionHelper.class);

    private Properties configProperties;

    private InitialContext initialContext;

    private TopicConnectionFactory topicConnectionFactory = null;

    private TopicConnection topicConnection = null;

    private Topic subsTopic = null;

    private TopicSubscriber topicSubscriber = null;

    private TopicSession topicSession = null;

    private TopicPublisher topicPublisher = null;

    private HashMap subscribers = new HashMap();

    public XhibitSubscriptionHelper() {
    }

    /**
     * Add a JMS subscriber.
     * 
     * @param xhibitListener
     *            User implemented class.
     * @param selectorProperties
     *            Selection criteria.
     * @throws SubscriptionException
     */
    public void addSubscriber(XhibitListener xhibitListener, Properties selectorProperties)
            throws SubscriptionException {
        String methodName = "addSubscriber() - ";
        String topicName = null;
        String selector = null;

        log.debug(methodName + "called");

        if (selectorProperties != null) {
            selector = generateSelector(selectorProperties);
            log.debug(selector);
        }

        // Get topic name...
        topicName = getTopicName(xhibitListener);

        // Init JMS...
        JmsInit(topicName);

        try {
            if (selector == null)
                topicSubscriber = topicSession.createSubscriber(subsTopic);
            else
                topicSubscriber = topicSession.createSubscriber(subsTopic, selector, true);
            topicSubscriber.setMessageListener(xhibitListener);

            // Add to list of subscribers....
            subscribers.put(xhibitListener.getListenerType(), topicConnection);

            // Start connection listening...
            topicConnection.start();
        } catch (JMSException jms) {
            CSServices.getDefaultErrorHandler().handleError(jms, getClass(), jms.toString());
            log.debug(methodName + jms.toString());
            throw new SubscriptionException("Xhibit_Messaging.Unable_To_Subscribe", jms.getMessage(), jms);
        }
    }

    public void addDurableSubscriber(XhibitListener xhibitListener, String clientId) throws SubscriptionException {
        log.debug("addDurableSubscriber() start clientId=" + clientId);
        String topicName = getTopicName(xhibitListener);

        // Init JMS...
        JmsInit(topicName, clientId);

        try {
            topicSubscriber = topicSession.createDurableSubscriber(subsTopic, clientId);
            topicSubscriber.setMessageListener(xhibitListener);

            // Add to list of subscribers....
            subscribers.put(xhibitListener.getListenerType(), topicConnection);

            // Start connection listening...
            topicConnection.start();
        } catch (JMSException jms) {
            CSServices.getDefaultErrorHandler().handleError(jms, getClass(), jms.toString());
            log.debug(jms.toString());
            throw new SubscriptionException("Xhibit_Messaging.Unable_To_Subscribe", jms.getMessage(), jms);
        }

    }

    public void addSubscriber(XhibitListener xhibitListener) throws SubscriptionException {
        addSubscriber(xhibitListener, null);
    }

    public void removeSubscriber(XhibitListener xhibitListener) throws SubscriptionException {
        String methodName = "removeSubscriber() - ";
        log.debug(methodName + "called :: listenerType: " + xhibitListener.getListenerType());

        try {
            TopicConnection tc = (TopicConnection) subscribers.get(xhibitListener.getListenerType());
            if (tc != null)
                tc.close();
        } catch (JMSException jms) {
            CSServices.getDefaultErrorHandler().handleError(jms, getClass(), jms.toString());
            log.debug(methodName + jms.toString());
            throw new SubscriptionException("Xhibit_Messaging.Unable_To_R", jms.getMessage(), jms);
        }
    }

    /**
     * Publish an event onto a JMS topic.
     * 
     * @param listenerType
     * @param obj
     *            Object to place on queue.
     * @throws SubscriptionException
     */
    public void publishEvent(String listenerType, CSAbstractValue obj) throws SubscriptionException {
        /** @todo Fill in publishEvent( listenerType, obj ) */
        String methodName = "publishEvent( listenerType, obj ) - ";
        ObjectMessage objMessage = null;
        String topicName = null;

        log.debug(methodName + "called");

        // Get topic name...
        topicName = getTopicName(listenerType);

        // JMS Initialisation...
        JmsInit(topicName);

        try {
            topicPublisher = topicSession.createPublisher(subsTopic);
            objMessage = topicSession.createObjectMessage(obj);
            topicPublisher.publish(objMessage);
        } catch (JMSException jms) {
            CSServices.getDefaultErrorHandler().handleError(jms, getClass(), jms.toString());
            log.debug(methodName + jms.toString());
            throw new SubscriptionException("Xhibit_Messaging.Unable_To_Publish_Message", jms.getMessage(), jms);
        }
    }

    /**
     * Publish an event onto a JMS topic together with any additional message
     * properties.
     * 
     * @param listenerType
     * @param obj
     *            Object to place on queue.
     * @param props
     *            Message properties.
     * @throws SubscriptionException
     */
    public void publishEvent(String listenerType, CSAbstractValue obj, Properties props) throws SubscriptionException {
        /** @todo Fill in publishEvent( listenerType, obj, props ) */
        String methodName = "publishEvent( listenerType, obj, props ) - ";
        ObjectMessage objMessage = null;
        String topicName = null;

        log.debug(methodName + "called");

        // Get topic name...
        topicName = getTopicName(listenerType);

        // JMS Initialisation...
        JmsInit(topicName);

        try {
            topicPublisher = topicSession.createPublisher(subsTopic);
            objMessage = topicSession.createObjectMessage(obj);

            // Set properties on message...
            if (props != null) {
                String key = null;
                String prop = null;
                Enumeration enumeration = props.keys();
                while (enumeration.hasMoreElements()) {
                    key = (String) enumeration.nextElement();
                    prop = props.getProperty(key);
                    log.debug(methodName + " " + key + " " + prop);
                    objMessage.setStringProperty(key, prop);
                }
            }

            // Publish and be damned...
            topicPublisher.publish(objMessage);
        } catch (JMSException jms) {
            CSServices.getDefaultErrorHandler().handleError(jms, getClass(), jms.toString());
            log.debug(methodName + jms.toString());
            throw new SubscriptionException("Xhibit_Messaging.Unable_To_Publish_Message", jms.getMessage(), jms);
        }
    }

    /*
     * Private Methods ---------------
     */
    /**
     * Thin wrapper created to allow existing call to be unaffected by allowing
     * durable subscriptions to be added
     * 
     * @param topicName
     * @throws SubscriptionException
     */
    private void JmsInit(String topicName) throws SubscriptionException {
        JmsInit(topicName, null);
    }

    /**
     * JMS initialisation. Gets the context and sets up topic connection
     * factory, connection and session.
     * 
     * @param topicName
     * @throws SubscriptionException
     */
    private void JmsInit(String topicName, String clientId) throws SubscriptionException {
        String methodName = "JmsInit() - ";
        String connectionFactory;
        log.debug(methodName + topicName);

        try {
            getSubscriptionProperties();
            connectionFactory = configProperties.getProperty("ConnectionFactory");
            initialContext = CSServices.getServiceLocator().getInitialContext();
            topicConnectionFactory = (TopicConnectionFactory) initialContext.lookup(connectionFactory);
            subsTopic = (Topic) initialContext.lookup(topicName);
            topicConnection = topicConnectionFactory.createTopicConnection();
            if (clientId != null) {
                log.debug("creting durable connection: clientId=" + clientId);
                topicConnection.setClientID(clientId);
            }
            topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (NamingException n) {
            CSServices.getDefaultErrorHandler().handleError(n, getClass(), n.toString());
            log.debug(methodName + n.toString());
            throw new SubscriptionException("Xhibit_Messaging.Unable_To_Find_Topic", n.getMessage(), n);
        } catch (JMSException jms) {
            CSServices.getDefaultErrorHandler().handleError(jms, getClass(), jms.toString());
            log.debug(methodName + jms.toString());
            throw new SubscriptionException("Xhibit_Messaging.Unable_To_Connect_To_Topic", jms.getMessage(), jms);
        }
    }

    /**
     * Gets the topic name based on the XhibitListener.
     * 
     * @param xhibitListener
     * @return
     * @throws SubscriptionException
     */
    private String getTopicName(XhibitListener xhibitListener) throws SubscriptionException {
        String methodName = "getTopicName() - ";
        String topicName = null;

        log.debug(methodName + "called");

        // Get listener type...
        String listenerType = xhibitListener.getListenerType();
        log.debug(listenerType);

        // Get topic name...
        getSubscriptionProperties();
        topicName = configProperties.getProperty(listenerType);

        return topicName;
    }

    /**
     * Gets the topic name based on the listener type.
     * 
     * @param listenerType
     * @return
     * @throws SubscriptionException
     */
    private String getTopicName(String listenerType) throws SubscriptionException {
        String methodName = "getTopicName() - ";
        String topicName = null;

        log.debug(methodName + "called");

        getSubscriptionProperties();
        topicName = configProperties.getProperty(listenerType);
        return topicName;
    }

    private void getSubscriptionProperties() throws SubscriptionException {
        String propertiesFileName = "subscription.jmsTopics";
        String methodName = "getSubscriptionProperties() - ";

        try {
            configProperties = CSServices.getConfigServices().getProperties(propertiesFileName);
        } catch (CSConfigurationException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            log.error(methodName + "Cannot find properties file: " + propertiesFileName, e);
            throw new SubscriptionException("Xhibit_Messaging.Unable_To_Locate_Properties", e.getMessage(), e);
        }
    }

    private String generateSelector(Properties selectorProperties) {
        int counter = 0;
        String key = null;
        String prop = null;
        StringBuffer sb = new StringBuffer();
        Enumeration enumeration = selectorProperties.keys();
        while (enumeration.hasMoreElements()) {
            if (counter > 0)
                sb.append(" AND ");
            key = (String) enumeration.nextElement();
            prop = selectorProperties.getProperty(key);
            sb.append(key + "=" + prop);
            counter++;
        }

        return sb.toString();
    }
}