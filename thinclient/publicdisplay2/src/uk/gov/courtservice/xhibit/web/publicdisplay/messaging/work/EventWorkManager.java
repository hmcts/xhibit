package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.work;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.event.EventStore;

/**
 * @author pznwc5
 * 
 * The class is responsible for pulling an event from the event store and
 * assining it to the thread pool. If we need event coalesce, set the num
 * workers to one
 */
public class EventWorkManager extends Thread {

    /** Logger */
    private static Logger log = Logger.getLogger(EventWorkManager.class);

    /** Whether the work manager is active */
    private boolean active = true;

    /** Event store that is managed */
    private EventStore eventStore;

    /** Thread pool used by the event manager */
    private ThreadPool threadPool;

    /**
     * If we need event coalesce, set the num workers to one
     * 
     * @param eventSTore
     *            Event store that is used
     * @param numWorkers
     *            Number of worker threads used by the event manager
     */
    public EventWorkManager(EventStore eventStore, int numWorkers) {
        this.eventStore = eventStore;
        threadPool = new ThreadPool(numWorkers);

        log.debug("Event store: " + eventStore);
        log.debug("Number of workers: " + numWorkers);
    }

    /**
     * Shuts down the work manager
     * 
     */
    public void shutDown() {
        active = false;
        threadPool.shutdown();
    }

    /**
     * Processes the events
     */
    public void run() {
        // This is not a continuosly running loop as it does a wait on the event
        // store
        while (active) {
            PublicDisplayEvent event = eventStore.popEvent();
            if (event != null) {
                log.debug("Event received: " + event);
                threadPool.scheduleWork(new EventWork(event));
                log.debug("Event processed: " + event);

            }
        }

        log.debug("Event work manager shutdown.");
    }

}
