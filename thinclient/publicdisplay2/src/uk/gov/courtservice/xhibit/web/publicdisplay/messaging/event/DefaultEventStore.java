package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.event;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;

/**
 * @author meekun
 * 
 * A FIFO implementation of event queue
 */
public class DefaultEventStore implements EventStore {

    /** Logger */
    private static Logger log = Logger.getLogger(DefaultEventStore.class);

    /** Queue of events */
    private LinkedList events = new LinkedList();

    /**
     * Pushes an event to the queue
     * 
     * @param event
     *            Event to be pushed into the queue
     */
    public synchronized void pushEvent(PublicDisplayEvent event) {
        events.addLast(event);
        // Notify the waiting thread when an event arrives
        notifyAll();
        log.debug("Pushed event to the queue: " + event);

    }

    /**
     * Pops an event from the queue
     * 
     * @return Next event in the queue
     */
    public synchronized PublicDisplayEvent popEvent() {
        while (events.isEmpty()) {
            try {
                // Wait if the queue is empty
                wait();
            } catch (InterruptedException ex) {
                log.error(ex.getMessage(), ex);
            }
        }

        // Pop the event of the queue
        PublicDisplayEvent event = (PublicDisplayEvent) events.getFirst();
        events.remove(0);
        log.debug("Popped event into the queue: " + event);

        return event;

    }

}
