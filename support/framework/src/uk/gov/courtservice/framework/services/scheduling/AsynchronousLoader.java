package uk.gov.courtservice.framework.services.scheduling;

import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * The Asynchronous loader is used to load data in an asychnronous manner.
 * Designed to instantiate (using a no args constructor) the list of classes in
 * asynchronousloader.properties init.classes(delimited by a ;), that can on
 * instantiation, register either themselves (if loadable)or another class (that
 * is loadable) with the AsynchronousLoader.
 * 
 * To have your data scheduled the class must extend AbstractLoadable.
 */
public class AsynchronousLoader implements Runnable {
    private Vector jobStack = new Vector();

    private static final int TIMEOUT = 100000;

    private Thread loaderThread;

    private Logger log = CSServices.getLogger(AsynchronousLoader.class);

    private static AsynchronousLoader instance = new AsynchronousLoader();

    private boolean continueRunning = true;
    private boolean initStarted = false;

    private AsynchronousLoader() {
        loaderThread = new Thread(this);
        loaderThread.start();
    }

    public synchronized void init() {
        // Ensure init is only called once
        if (initStarted) {
            return;
        }
        initStarted = true;
        continueRunning = true;
        StringTokenizer st = new StringTokenizer(CSServices.getConfigServices().getProperties("asynchronousloader")
                .getProperty("init.classes"), ";");
        while (st.hasMoreTokens()) {
            try // Note hardened against all expected exceptions - if exception
            // unexpected then big bang.
            {
                log.debug("!!!!!!!!!!!!!!!!!!!!!!!!!INITIALISING CLASS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                Class myClass = Class.forName(st.nextToken()); // Should do
                // static stuff
                // here.
                myClass.newInstance(); // Initiate to allow non static
                // registration with AsynchronousLoader
            } catch (ClassNotFoundException cnfe) {
                log.error("Could not find class.", cnfe);
            } catch (ExceptionInInitializerError eiie) {
                log.error("Could not initialise class.", eiie);
            } catch (LinkageError le) {
                log.error("Could not link class.", le);
            } catch (IllegalAccessException iae) {
                log.error("Could not access class.", iae);
            } catch (InstantiationException ie) {
                log.error("Could not instantiate class.", ie);
            } catch (SecurityException se) {
                log.error("Security problem in instantiating class.", se);
            } catch (Throwable t) {
                log.error("Unexpected problem.", t);
            }

        }
    }

    /**
     * Request the loader to load the job.
     * 
     * @param job
     *            the
     */
    public void addToLoader(Loadable job) {
        jobStack.add(job);
        synchronized (loaderThread) {
            loaderThread.notify();
        }

    }

    /**
     * Run the current Jobs.
     */
    public void run() {
        runCurrentJobs();

    }

    /**
     * Attempt to run all jobs, once they have run successfully, stop running
     * them.
     */
    private void runCurrentJobs() {
        while (continueRunning) {
            try {
                synchronized (jobStack) {
                    for (int i = 0; i < jobStack.size(); i++) {
                        Loadable referenceData = (Loadable) jobStack.elementAt(i);
                        if (!referenceData.isLoaded()) // Only attempt load if
                            // not loaded.
                            performLoad(referenceData);
                    }
                }
                synchronized (this) {
                    wait(TIMEOUT);
                }
            } catch (Throwable e) // Continue running no matter what...
            // the matter might rectify itself.
            {
                e.printStackTrace(); // To change body of catch statement use
                // Options | File Templates.
            }
        }
    }

    /**
     * Perform the load.
     */
    private void performLoad(Loadable referenceData) {
        log.info("Data Loader loading job: " + referenceData.getName());
        synchronized (referenceData) {
            referenceData.setLoaded(false);
            referenceData.load();
            referenceData.setLoaded(true);
            referenceData.notify();
        }
        log.info("Data Loader loaded job: " + referenceData.getName());
    }

    /**
     * Factory method to provide an instamce of an AsynchronousLoader.
     * 
     * @return an instamce of an AsynchronousLoader
     */
    public static AsynchronousLoader getInstance() {
        return instance;
    }

    /**
     * Finish running the Asynchronous Loader, usually to be called when exiting
     * the application.
     */
    public void finishLoading() {
        if (!initStarted) {
            // PR6080 - finishLoading called before init() prevents subsequent
            // calls to init() doing its work
            return;
        }
        continueRunning = false;
        synchronized (this) {
            notifyAll();
        }
    }
}
