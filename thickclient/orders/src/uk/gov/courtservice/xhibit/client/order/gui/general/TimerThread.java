package uk.gov.courtservice.xhibit.client.order.gui.general;

import org.apache.fop.apps.FOPException;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.print.OrderDisplayHelper;

/**
 * <p>
 * Title: Timer to schedule updates to OrderPreviewPane
 * </p>
 * <p>
 * Description: Uses thread class to schedule an update to the OrderPreviewPane
 * rather than have it update immediately.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Duncan
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 01-10-2003 AW Daley run method now uses the synchronised setUpdated method
 * rather than setting the update attribute in open code.
 */
public class TimerThread extends Thread {
    private Logger log = CSServices.getLogger(TimerThread.class);

    // OrderPreviewPane
    private OrderDisplayHelper panel;

    // OrderPreviewPane
    private OrderPreviewPane pane;

    private static final int updateDelay;

    // Need to so update.
    private boolean updated = false;

    // Need to stop update temporarily.
    private boolean suspended = false;

    private boolean continueThread = true;

    /**
     * Constructs the timer with the specified thread and a reference to the
     * OrderDisplayHelper to be updated.
     * 
     * @param str
     *            name of thread.
     * @param pane
     * @see OrderDisplayHelper.
     */
    public TimerThread(String str, OrderDisplayHelper pane) {
        super(str);
        this.panel = pane;
        this.setPriority(9);

    }

    /**
     * Constructs the timer with the specified thread and a reference to the
     * OrderPreviewPane to be updated.
     * 
     * @param str
     *            name of thread.
     * @param pane
     * @see OrderPreviewPane.
     * @deprecated
     */
    public TimerThread(String str, OrderPreviewPane pane) {
        super(str);
        this.pane = pane;

    }

    /**
     * Thread sleeps for 1000 ms and then updates the OrderPreviewPane.
     */
    public void run() {

        try {
            while (continueThread) {
                try {
                    sleep(updateDelay);
                    // Only do update transform while there is a pending
                    // update and no other activity is occurring requiring
                    // suspension.
                    if (getUpdated() && !suspended) {
                        log.info("Updating Preview Panel in Orders Client.");

                        panel.loadTransform(panel.getDOM());
                        setUpdated(false);
                    }
                } catch (FOPException ote) {
                    log.error(ote);
                    panel.displayCriticicalFailure(ote);
                }
            }
        } catch (InterruptedException e) {
            log.error(e);
        }
    }

    /**
     * Make thread aware that an update is due.
     */
    public synchronized void setUpdated(boolean update) {
        this.updated = update;
    }

    public synchronized boolean getUpdated() {
        return this.updated;
    }

    /**
     * Temporarily suspend update. While other things are going on that may
     * cause the update to fail.
     */
    public synchronized void suspendThread() {
        this.suspended = true;
    }

    /**
     * Resume update.
     */
    public synchronized void resumeThread() {
        this.suspended = false;
        this.setUpdated(true);
    }

    /**
     * Stop update.
     */
    public void stopThread() {
        this.continueThread = false;
    }

    static {
        updateDelay = Integer.parseInt(CSServices.getConfigServices().getProperties("orders").getProperty(
                "client.update.delay"));
    }

}
