package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.work;

/**
 * @author Meeraj
 *
 * Generic thread pool implementation. Original version written by R Oberg for JBoss.
 */
import java.util.Stack;

import org.apache.log4j.Logger;

public final class ThreadPool {

    /** Pool size */
    private int numWorkers = 1;

    /** Timeout * */
    private long timeout = 60 * 1000l;

    /** Logger */
    private static Logger log = Logger.getLogger(ThreadPool.class);

    /** Stack of workers in the pool */
    private Stack workers = new Stack();

    /** Whether the pool is active */
    private boolean active = true;

    /**
     * Initialize the pool with the number of workers
     * 
     * @param numWorkers
     *            Number of workers
     */
    public ThreadPool(int numWorkers) {
        this.numWorkers = numWorkers;
        for (int i = 0; i < numWorkers; i++) {
            Worker worker = new Worker(this, i);
            workers.push(worker);
            worker.start();
            log.debug("Worker started: " + i);
        }
    }

    /**
     * Initialize the pool with the number of workers
     * 
     * @param numWorkers
     *            Number of workers
     */
    public ThreadPool(int numWorkers, long timeout) {
        this(numWorkers);
        this.timeout = timeout;
    }

    /**
     * Method called by clients to schedule a work
     * 
     * @param work
     *            Work that needs to be done
     */
    public void scheduleWork(Runnable work) {

        // Check whether the thread pool is active
        if (!active) {
            throw new ThreadPoolInactiveException();
        }

        Worker worker = getWorker();
        log.debug("Worker retieved: " + worker.getId());
        worker.scheduleWork(work);
        log.debug("Work scheduled: " + work);
    }

    /**
     * Shuts down the pool
     * 
     */
    public synchronized void shutdown() {

        active = false;

        // Wait till or the workers are returned
        waitForPoolFull();

        while (workers.size() > 0) {
            Worker worker = (Worker) workers.pop();
            log.debug("Worker shutting down: " + worker.getId());
            worker.shutdown();
            // Wait till the worker dies
            while (worker.isAlive()) {
                log.debug("Worker still alive - worker:" + worker.getId());
            }
            log.debug("Worker shutdown: " + worker.getId());
        }
        log.debug("Thread pool shutdown");
    }

    /**
     * Returns the number of workers
     * 
     * @return
     */
    public synchronized int getNumFreeWorkers() {
        return workers.size();
    }

    /**
     * Gets the worker
     * 
     * @return
     */
    private synchronized Worker getWorker() {
        try {
            log.debug("Start getWorker");
            while (workers.empty()) {
                try {
                    log.debug("Waiting for workers");
                    wait(timeout);
                } catch (InterruptedException ex) {
                    throw new WorkerUnavailableException(ex);
                }
            }
            return (Worker) workers.pop();
        } finally {
            log.debug("End getWorker");
        }
    }

    /**
     * Returns the worker back to the pool
     * 
     * @param worker
     */
    private synchronized void returnWorker(Worker worker) {
        try {
            log.debug("Start returnWorker");
            workers.push(worker);
            log.debug("Worker returned: " + worker.getId());
            notifyAll();
        } finally {
            log.debug("End returnWorker");
        }
    }

    /**
     * Wait till the pool is full
     * 
     */
    private void waitForPoolFull() {
        // Wait till all the workers are back in the pool
        while (workers.size() != numWorkers) {
            try {
                log.debug("Waiting for workers to be returned");
                wait(timeout);
            } catch (InterruptedException e) {
                log.fatal(e.getMessage(), e);
                return;
            }
        }
    }

    private class Worker extends Thread {

        /** Logger */
        private Logger log = Logger.getLogger(Worker.class);

        /** Whether the worker is running */
        private boolean running = true;

        /** Work that is being processed */
        private Runnable work;

        /** Thread pool to which the worker belongs */
        private ThreadPool pool;

        /** Worker id */
        private int number;

        /** Initializes the worker as a daemon */
        Worker(ThreadPool pool, int number) {
            this.pool = pool;
            this.number = number;
            setDaemon(true);
        }

        /** Returns the id of the worker */
        int getNumber() {
            return number;
        }

        /** Kills the worker */
        synchronized void shutdown() {
            running = false;
            notify();
            log.debug("Notifying for shutdown - worker: " + number);
        }

        /** Schedules a work */
        synchronized void scheduleWork(Runnable work) {
            if (work != null) {
                this.work = work;
                notify();
                log.debug("Work scheduled: " + work);
            }
        }

        /**
         * Worker runs while the pool is active
         */
        public void run() {
            while (running) {
                // Wait for the next work
                synchronized (this) {
                    while (work == null && running) {
                        try {
                            log.debug("Waiting for work - worker: " + number);
                            this.wait();
                        } catch (InterruptedException ex) {
                            log.error(ex.getMessage(), ex);
                        }
                    }
                    // Do the work
                    doWork();
                }
            }
            log.debug("Worker killed - worker:" + number);
        }

        /**
         * Dows the work
         * 
         */
        private void doWork() {
            if (work == null)
                return;

            // Run the work
            try {
                log.debug("Starting work - worker:" + number);
                work.run();
                log.debug("Work done - worker: " + number);
            } catch (Throwable th) {
                // We son't want the worker to bomb out in case of an exception
                log.fatal(th.getMessage(), th);
            } finally {
                // Set the work to null
                work = null;

                // Return the worker to the pool
                pool.returnWorker(this);
            }

        }

    }

}
