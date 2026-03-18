package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.work;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.web.publicdisplay.error.ErrorGatherer;
import uk.gov.courtservice.xhibit.web.publicdisplay.error.ProcessingError;
import uk.gov.courtservice.xhibit.web.publicdisplay.initialization.ProcessingInstance;
import uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.WorkFlowContext;
import uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.WorkFlowManager;

/**
 * @author pznwc5
 * 
 * The class represents a unit of work involved in processing an event
 */
public class EventWork implements Runnable {

    /** Logger */
    private static Logger log = Logger.getLogger(EventWork.class);

    /** Event that needs to be processed */
    private PublicDisplayEvent event;

    /** Delay after initialization */
    private long delay;

    /** Processing instance */
    private ProcessingInstance processingInstance;

    /**
     * @param event
     *            Event that needs to be processed
     */
    public EventWork(PublicDisplayEvent event) {
        this(event, 0, ProcessingInstance.ASYNCHRONOUS);
    }

    /**
     * @param event
     *            Event that needs to be processed
     * @param delay
     *            Delay after initialization
     */
    public EventWork(PublicDisplayEvent event, long delay, ProcessingInstance processingInstance) {
        this.event = event;
        this.delay = delay;
        this.processingInstance = processingInstance;

        log.debug("Event: " + event);
        log.debug("Delay: " + delay);

    }

    /**
     * Processes the message
     */
    public void run() {
        try {
            WorkFlowContext ctx = WorkFlowContext.newInstance();
            WorkFlowManager.getInstance(ctx).process(event);
            log.debug("Event processed: " + event);

            if (delay > 0) {
                Thread.sleep(delay);
            }
        } catch (Throwable th) {
            log.fatal(th.getMessage(), th);
            ProcessingError error = new ProcessingError(event, th, processingInstance);
            ErrorGatherer.getInstance().addError(error);
        }
    }

}