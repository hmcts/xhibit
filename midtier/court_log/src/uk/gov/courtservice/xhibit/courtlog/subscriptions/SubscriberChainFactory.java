package uk.gov.courtservice.xhibit.courtlog.subscriptions;

import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.helpers.category.CategoryHelper;
import uk.gov.courtservice.xhibit.courtlog.helpers.event.EventHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * Factory class for creating the subscriber chain
 * 
 * @author pznwc5
 * @version $Revision: 1.20 $
 */
public class SubscriberChainFactory {
	private static final Logger log = CSServices.getLogger(SubscriberChainFactory.class);
	
    /** The name of the subscription configuration XML file */
    private static final String DEFAULT_CONFIG = "subscriptions/subscriptions.xml";

    /** Singleton instance */
    private static SubscriberChainFactory instance;

    /** XML configuration */
    private SubscriptionDom subscriptionDom;

    /**
     * Private constructor to stop instantiation
     */
    private SubscriberChainFactory(String configXml) {
        subscriptionDom = new SubscriptionDom(configXml);
    }

    /**
     * Singleton access method
     * 
     * @return Singleton instance
     */
    public static SubscriberChainFactory getInstance() {
        return getInstance(DEFAULT_CONFIG);
    }

    /**
     * Singleton access method
     * 
     * @param Name
     *            of the configuration XML
     * @return Singleton instance
     */
    public static SubscriberChainFactory getInstance(String configXml) {
        if (instance == null) {
            synchronized (SubscriberChainFactory.class) {
                if (instance == null) {
                    instance = new SubscriberChainFactory(configXml);
                }
            }
        }

        return instance;
    }

    /**
     * Gets the subscriber chain for the current court log operation
     * 
     * @param courtLogValue
     *            Court log entry
     * @param operationType
     *            Court log entry operation
     * @param categories
     *            Categories to which the court log event belongs
     * @param eventType
     *            Court log event type
     * @return Subsciber chain for the operation
     */
    public SubscriberChain getSubscriberChain(CourtLogCRUDValue crudVal) {
        log.debug("getSubscriberChain()");
    	String[] categories = CategoryHelper.getDescriptions(crudVal);
        String eventType = EventHelper.getXhbCourtLogEventDescByEventType(crudVal).getShortDescription();
        OperationContext context = OperationContext.newInstance(crudVal);
        context.setCategories(categories);
        List subscriberList = subscriptionDom.getSubscribers(categories, eventType);

        return new SubscriberChain(subscriberList, context);
    }
}
