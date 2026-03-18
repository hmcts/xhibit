package uk.gov.courtservice.framework.services.scheduling;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * 
 * Sub classes must provide a concrete <b>public void load()</b> method and a
 * <b>getName()</b> method. Each method that accesses the loaded data must call
 * the <b>waitOnLoad()</b> method before accesing the data.
 * 
 * @see AsynchronousLoader
 * 
 */
public abstract class AbstractLoadable implements Loadable {
    private boolean loaded = false; // Never loaded in the first instance.

    private Logger log = CSServices.getLogger(AbstractLoadable.class);

    /**
     * A callback method for the loader.
     * 
     * @param loaded
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * Method used to determine whether the job has loaded successfully
     * 
     * @return whether this AbstractLoadable has loaded successfully.
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * This method waits until either the data has loaded or <b>TIMEOUT</b>
     * milliseconds have elapsed.
     * 
     * @throws AsynchronousLoaderException
     *             if the timeout is reached.
     */
    protected void waitOnLoad() throws AsynchronousLoaderException {
        try {
            synchronized (this) {
                if (!loaded) {
                    log.info("Waiting on load.");
                    wait(TIMEOUT);
                    if (!loaded) {
                        throw new AsynchronousLoaderException("Timeout of " + TIMEOUT
                                + " milliseconds reached, data not loaded.");
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new AsynchronousLoaderException("Wait on load interrupted.", e);
        }
    }
}
