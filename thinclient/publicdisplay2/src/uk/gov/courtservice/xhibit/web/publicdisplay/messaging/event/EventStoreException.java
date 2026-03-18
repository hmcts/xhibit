package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.event;

import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.PublicDisplayRuntimeException;

/**
 * DOCUMENT ME!
 * 
 * @author pznwc5 The exception is thrown when we can't create the event store
 */
public class EventStoreException extends PublicDisplayRuntimeException {
    /**
     * 
     * @param eventStoreClass
     *            Event store class
     * @param th
     *            Root exception
     */
    public EventStoreException(String eventStoreClass, Throwable th) {
        super("Invalid event store type: " + eventStoreClass, th);
    }
}
