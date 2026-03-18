package uk.gov.courtservice.xhibit.web.publicdisplay.initialization.servlet;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.ConfigurationChangeEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtConfigurationChange;
import uk.gov.courtservice.xhibit.web.publicdisplay.initialization.ProcessingInstance;
import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.work.EventWork;
import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.work.ThreadPool;

/**
 * @author pznwc5
 * 
 * Performs initial document rendering
 */
public class DocumentInitializer {

    /** Logger */
    private static Logger log = Logger.getLogger(DocumentInitializer.class);

    /** Courts to be initialized */
    private int courtIds[];

    /** Number of workers to do the initialization */
    private int numWorkers;

    /** Delay after each initialization */
    private long delay;

    /**
     * Constructs the initializer
     * 
     * @param courtIds
     *            Courts to be initialized
     * @param numWorkers
     *            Number of workers to do the initialization
     * @param delay
     *            Delay after each initialization
     */
    public DocumentInitializer(int courtIds[], int numWorkers, long delay) {
        this.courtIds = courtIds;
        this.delay = delay;
        this.numWorkers = numWorkers;
    }

    /**
     * Performs the initialization
     * 
     */
    public void initialize() {

        // Create the thread pool
        ThreadPool pool = new ThreadPool(numWorkers);

        try {
            for (int i = 0; i < courtIds.length; i++) {
                // Create the court configuration change
            	String courtName = "";
                CourtConfigurationChange change = new CourtConfigurationChange(courtIds[i], courtName, true);
                log.debug("Change: " + change);

                // Create the event
                ConfigurationChangeEvent event = new ConfigurationChangeEvent(change);
                log.debug("Event: " + event);

                // Create the work
                EventWork work = new EventWork(event, delay, ProcessingInstance.STARTUP);
                log.debug("Work: " + work);

                // Schedule the work
                pool.scheduleWork(work);
                log.debug("Work scheduled ....");
            }
        } finally {
            // Shutdown the thread pool
            pool.shutdown();
            log.info("Thread pool shutdown");
        }
    }

}
