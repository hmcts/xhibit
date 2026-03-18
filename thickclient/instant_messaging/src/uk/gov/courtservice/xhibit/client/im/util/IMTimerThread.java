package uk.gov.courtservice.xhibit.client.im.util;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Timer to delay start of Instant Messaging receiver. This will allow
 * multiple messages to be displayed together on startup.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */
public class IMTimerThread extends Thread {
    private Logger log = CSServices.getLogger(IMTimerThread.class);

    private static final int updateDelay;

    // Need to so update.
    private IMMessageReceiver receiver;

    // Need to stop update temporarily.
    private boolean suspended = false;

    static {
        updateDelay = Integer.parseInt(CSServices.getConfigServices().getProperties("messaging").getProperty(
                "client.update.delay"));
    }

    /**
     * Constructs the timer with the specified thread
     * 
     * @param str
     *            name of thread.
     * @param pane
     * @see OrderDisplayHelper.
     */
    public IMTimerThread(String str) {
        super(str);
        this.setPriority(9);

    }

    /**
     * Constructs the timer with the specified thread and a reference to the
     * IMMessageReceiver to be updated.
     * 
     * @param str
     *            name of thread.
     * @param receiver
     * @see IMMessageReceiver.
     */
    public IMTimerThread(String str, IMMessageReceiver receiver) {
        this(str);
        this.receiver = receiver;

    }

    /**
     * Thread sleeps for updateDelay ms and then updates the IMMessageReceiver.
     */
    public void run() {
        try {
            log.debug("isInitialised() before " + receiver.isInitialised());
            sleep(updateDelay);

            // Set started flag once the Timer has run
            receiver.setIsInitialised(true);

            // Display the dialog showing stored messages on startup
            receiver.showMultipleMessages();
            log.debug("isInitialised() after " + receiver.isInitialised());
        } catch (InterruptedException e) {
            log.error(e);
        } catch (CSRecoverableException ex) {
            log.error(ex);
        }
    }
}
