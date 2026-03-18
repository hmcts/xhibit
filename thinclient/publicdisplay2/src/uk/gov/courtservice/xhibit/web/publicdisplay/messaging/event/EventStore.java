package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.event;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;

/**
 * DOCUMENT ME!
 * 
 * @author meekun
 * 
 * The interface that should be implemented by event stores
 */
public interface EventStore {
    /**
     * Pushed an event into the queue
     * 
     * @param event
     *            Event that is pushed
     */
    public void pushEvent(PublicDisplayEvent event);

    /**
     * Pos an event from the queue
     * 
     * @return Event that is popped
     */
    public PublicDisplayEvent popEvent();
}