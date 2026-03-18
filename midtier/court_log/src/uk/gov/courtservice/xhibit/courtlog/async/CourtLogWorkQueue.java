package uk.gov.courtservice.xhibit.courtlog.async;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.threadpool.ThreadPool;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.OperationType;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber;

/**
 * This class is a singleton and models a FIFO queue for scheduling asynchrnous
 * court log subscriptions. The subscriptions are executed by threads from a
 * thread pool.
 * 
 * The number of workers in the pool can be specified by the system property
 * <code>courtLog.async.workers<code>. The default value is 10.
 * 
 * The number of milliseconds a requesting thread will wait when the thread
 * pool is fully in use can be specified by the system property <code>
 * courtLog.async.timeout</code>. The default value is 60 seconds.
 *
 * @author pznwc5
 * @version $Id: CourtLogWorkQueue.java,v 1.9 2006/06/05 12:28:53 bzjrnl Exp $
 */
public class CourtLogWorkQueue {
    /** Peoperty by which the number of timeout is defined */
    private static final String TIMEOUT_PROPERTY = "courtLog.async.timeout";

    /** Peoperty by which the number of workers is defined */
    private static final String WORKERS_PROPERTY = "courtLog.async.workers";

    /** Default number of workers */
    private static final int DEFAULT_WORKERS = 10;

    /** Default timeout */
    private static final long DEFAULT_TIMEOUT = 60 * 1000;

    /** Logger */
    private static final Logger log = Logger.getLogger(CourtLogWorkQueue.class);

    /** Singleton instance */
    private static final CourtLogWorkQueue INSTANCE = new CourtLogWorkQueue();

    /** Thread pool that is used */
    private ThreadPool pool;

    /** Queue of events */
    private LinkedList works = new LinkedList();

    /** Active indicator */
    private boolean active = true;

    /**
     * Private constructor
     */
    private CourtLogWorkQueue() {
        String sWorkers = System.getProperty(WORKERS_PROPERTY);
        String sTimeout = System.getProperty(TIMEOUT_PROPERTY);

        int workers = (sWorkers == null) ? DEFAULT_WORKERS : Integer.parseInt(sWorkers);
        log.debug("workers: " + workers);
        long timeout = (sTimeout == null) ? DEFAULT_TIMEOUT : Integer.parseInt(sTimeout);
        log.debug("timeout: " + timeout);

        pool = new ThreadPool(workers, timeout);
        log.debug("Thread pool created");

        Thread th = new Thread() {
            public void run() {
                while (active) {
                    CourtLogWork work = popWork();
                    pool.scheduleWork(work);
                }
            }
        };
        th.start();
        log.debug("Poller started");
    }

    /**
     * Singleton accessor
     * 
     * @return SIngleton instance
     */
    public static final CourtLogWorkQueue getInstance() {
        return INSTANCE;
    }

    /**
     * Pushes an work to the queue
     * 
     * @param work
     *            Work to be pushed into the queue
     */
    public synchronized void pushWork(Subscriber subscriber, OperationContext context, OperationType type) {
        CourtLogWork work = new CourtLogWork(subscriber, context, type);
        works.addLast(work);
        // Notify the waiting thread when a workt arrives
        notifyAll();
        log.debug("Pushed work to the queue: " + work);
    }

    /**
     * Shuts down the queue and the underlying thread pool
     */
    public synchronized void shutdown() {
        active = false;
        pool.shutdown();
    }

    /**
     * Pops an work from the queue
     * 
     * @return Next Work in the queue
     */
    private synchronized CourtLogWork popWork() {
        while (works.isEmpty()) {
            try {
                // Wait if the queue is empty
                wait();
            } catch (InterruptedException ex) {
                log.error(ex.getMessage(), ex);
            }
        }

        // Pop the work from the queue
        CourtLogWork work = (CourtLogWork) works.getFirst();
        works.remove(0);
        log.debug("Popped work from the queue: " + work);

        return work;
    }
}
