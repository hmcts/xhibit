package uk.gov.courtservice.xhibit.courtlog.subscriptions;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogRuntimeException;
import uk.gov.courtservice.xhibit.courtlog.helpers.xml.CourtLogXmlHelper;

/**
 * The class provides a facade over the XML subscription configuration.
 *
 * @author pznwc5
 * @version $Revision: 1.20 $
 */
public class SubscriptionDom {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(SubscriptionDom.class);

    /** List of global subscriptions */
    private final Map globalSubscriptions = new TreeMap();

    /** List of subscriptions defined at category level */
    private final Map categorySubscriptions = new TreeMap();

    /** List of subscriptions defined at event level */
    private final Map eventSubscriptions = new TreeMap();

    /**
     * @param Name
     *            of the configuration XML
     */
    public SubscriptionDom(String configXml) {
        init(configXml);
        LOG.info("Global subscriptions: " + globalSubscriptions);
        LOG.info("Category subscriptions: " + categorySubscriptions);
        LOG.info("Event subscriptions: " + eventSubscriptions);
    }

    /**
     * Gets the list of subscribers for the event type and categories
     *
     * @param categories
     *            Categories to which the event belongs
     * @param eventType
     *            Event type
     * @return List of configured subscribers
     */
    public List getSubscribers(String categories[], String eventType) {

        if (LOG.isInfoEnabled()) {
            LOG.info("eventType: " + eventType);
            for (int i = 0; i < categories.length; i++) {
                LOG.info("category[" + i + "]: " + categories[i]);
            }
        }

        List subscribers = new ArrayList();

        // Populate global subscribers
        getGlobalSubscribers(subscribers);
        // Populate category subscribers
        getCategorySubscribers(categories, subscribers);
        // Populate event subscribers
        getEventSubscribers(eventType, subscribers);

        // Sort the asynchronous subscribers to the last
        Collections.sort(subscribers);

        LOG.info("Subscribers selected for event");
        LOG.info(eventType + "=>" + subscribers);

        return subscribers;
    }

    /**
     * Initializes the configuration
     *
     * @param Name
     *            of the configuration XML
     */
    private void init(String configXml) {
        // Parse the XML configuration
        Element root = getDocument(configXml).getDocumentElement();
        LOG.info("Configuration XML parsed");

        // Populate the global subscriptions
        populateGlobalSubscriptions(root);
        LOG.debug("Global subsciptions initialized");
        // Populate the category level subscriptions
        populateCategorySubscriptions(root);
        LOG.debug("Category level subsciptions initialized");
        // Populate the event level subscriptions
        populateEventSubscriptions(root);
        LOG.debug("Event level subsciptions initialized");
    }

    /**
     * Returns the parsed XML configuration document
     *
     * @param Name
     *            of the configuration XML
     * @return XML DOM for the configuration
     */
    private Document getDocument(String configXml) {
        // Get the XML stream
        InputStream in = getClass().getClassLoader().getResourceAsStream(configXml);

        LOG.info("XML stream received");

        return CourtLogXmlHelper.createDocument(in);
    }

    /**
     * Initializes the global subscriptions
     *
     * @param root
     *            The document element of the configuration
     */
    private void populateGlobalSubscriptions(Element root) {
        String expr = "//subscriber[@level='global']";
        populateSubscriptions(root, expr, globalSubscriptions, SubscriberType.GLOBAL);
    }

    /**
     * Initializes the category level subscriptions
     *
     * @param root
     *            The document element of the configuration
     */
    private void populateCategorySubscriptions(Element root) {
        String expr = "//subscriber[@level='category']";
        populateSubscriptions(root, expr, categorySubscriptions, SubscriberType.CATEGORY);
    }

    /**
     * Initializes the event level subscriptions
     *
     * @param root
     *            The document element of the configuration
     */
    private void populateEventSubscriptions(Element root) {
        String expr = "//subscriber[@level='event']";
        populateSubscriptions(root, expr, eventSubscriptions, SubscriberType.EVENT);
    }

    /**
     * Utility method for populating subscriptions
     *
     * @param root
     *            The root element of the configuration
     * @param expr
     *            The XPath expression to get the subscription elements
     * @param subscriptions
     * @param subscriptions
     *            The map in which subscriptions are populated
     */
    private void populateSubscriptions(Element root, String expr, Map subscriptions, SubscriberType type) {
        try {
            NodeList list = XPathAPI.selectNodeList(root, expr);

            for (int i = 0; i < list.getLength(); i++) {
                Element el = (Element) list.item(i);
                String name = el.getAttribute("name");
                String clazz = el.getAttribute("class");
                String sAsync = el.getAttribute("async");
                boolean async = sAsync != null && "true".equals(sAsync);

                Subscriber sub = getSubscriber(clazz);
                sub.setName(name);
                sub.setType(type);
                sub.setAsync(async);
                subscriptions.put(name, sub);

                LOG.info("Subscription added - name: " + name + ", class:" + clazz);
            }
        } catch (TransformerException e) {
            throw new CourtLogRuntimeException(e);
        }
    }

    /**
     * Initializes the list of subscriber references
     *
     * @param clazz
     *            Fully qualified name of the subscriber class
     * @return Subscriber instance
     */
    private Subscriber getSubscriber(String clazz) {
        try {
            return (Subscriber) Class.forName(clazz).newInstance();
        } catch (InstantiationException e) {
            throw new CourtLogRuntimeException("Unable to instantiate: " + clazz, e);
        } catch (IllegalAccessException e) {
            throw new CourtLogRuntimeException("Illegal access: " + clazz, e);
        } catch (ClassNotFoundException e) {
            throw new CourtLogRuntimeException("Class not found: " + clazz, e);
        }
    }

    /**
     * Populate global level subscribers
     *
     * @param subscribers
     *            List of subscribers
     */
    private void getGlobalSubscribers(List subscribers) {
        // Populate subscriptions
        Iterator it = globalSubscriptions.keySet().iterator();
        while (it.hasNext()) {
            String global = (String) it.next();
            LOG.info("Adding global subscriptions: " + global);
            Subscriber subscriber = (Subscriber) globalSubscriptions.get(global);
            if (subscriber != null) {
                subscribers.add(subscriber);
            }
        }
    }

    /**
     * Populate category level subscribers
     *
     * @param categories
     *            Category levels
     * @param subscribers
     *            List of subscribers
     */
    private void getCategorySubscribers(String[] categories, List subscribers) {
        for (int i = 0; i < categories.length; i++) {
            LOG.info("Adding category subscriptions: " + categories[i]);
            Subscriber subscriber = (Subscriber) categorySubscriptions.get(categories[i]);
            if (subscriber != null) {
                subscribers.add(subscriber);
            }
        }
    }

    /**
     * Populate event level subscribers
     *
     * @param eventType
     *            Event type
     * @param subscribers
     *            List of subscribers
     */
    private void getEventSubscribers(String eventType, List subscribers) {
        Subscriber subscriber = (Subscriber) eventSubscriptions.get(eventType);
        if (subscriber != null) {
            LOG.info("Event subscription found " + subscriber + " for " + eventType);
            subscribers.add(subscriber);
        }
    }
}
