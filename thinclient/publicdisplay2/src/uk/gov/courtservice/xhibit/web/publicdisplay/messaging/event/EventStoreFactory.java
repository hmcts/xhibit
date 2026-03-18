package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.event;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * @author meekun
 * 
 * Factory that returns a pre-configured event type
 */
public class EventStoreFactory {

    /** Logger */
    private static Logger log = Logger.getLogger(EventStoreFactory.class);

    /** The name of the property that contains the event store type */
    private static final String EVENT_STORE_TYPE = "public.display.event.store.type";

    /**
     * Creates the event store
     * 
     * @return Event store
     */
    public static EventStore getEventStore() {

        String eventStoreClass = CSServices.getConfigServices().getProperty(EVENT_STORE_TYPE);
        EventStore eventStore;

        if (eventStoreClass == null) {
            // Create the default event store
            eventStore = new DefaultEventStore();
        } else {
            try {
                // Dynamically instantiate the event store
                eventStore = (EventStore) Class.forName(eventStoreClass).newInstance();
            } catch (InstantiationException e) {
                log.fatal(e.getMessage(), e);
                throw new EventStoreException(eventStoreClass, e);
            } catch (IllegalAccessException e) {
                log.fatal(e.getMessage(), e);
                throw new EventStoreException(eventStoreClass, e);
            } catch (ClassNotFoundException e) {
                log.fatal(e.getMessage(), e);
                throw new EventStoreException(eventStoreClass, e);
            }
        }

        log.debug("Event store type: " + eventStore.getClass());
        return eventStore;
    }

}
