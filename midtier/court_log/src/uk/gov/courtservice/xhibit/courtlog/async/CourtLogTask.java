package uk.gov.courtservice.xhibit.courtlog.async;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.scheduler.Stoppable;

/**
 * @author pznwc5
 * 
 * The task is responsible for initializing the <code>CourtLogWorkQueue</code>
 * singleton. The initialization is done in the Servlet container to stop thread
 * spawning in the EJB container. Since the web and EJB components share the
 * same classloader in Weblogic (the web classloader being the child of the EAR
 * classloader), the singleton instance should be then visible in the EJB
 * conttainer.
 * 
 * The task also provides the lifecycle method <code>stop</code> so that the
 * queue is properly cleanedup on server shutdown.
 */
public class CourtLogTask implements Stoppable {

    // Logger
    private static final Logger LOG = Logger.getLogger(CourtLogTask.class);

    /**
     * Cleanup the work queue to make sure all the work are completed and the
     * underlying thread pool is properly shutdown.
     * 
     * @see uk.gov.courtservice.framework.scheduler.Stoppable#stop()
     */
    public void stop() {
        CourtLogWorkQueue.getInstance().shutdown();
        LOG.info("Queue shutdown");
    }

    /**
     * The task is configured to execute only once. When executed the task
     * initializes the court log work queue,
     * 
     * @see uk.gov.courtservice.framework.scheduler.JavaTask#doTask()
     */
    public void doTask() {
        CourtLogWorkQueue.getInstance();
        LOG.info("Queue initialized");
    }
}
