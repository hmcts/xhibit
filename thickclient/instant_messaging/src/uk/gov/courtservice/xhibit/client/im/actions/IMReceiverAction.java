package uk.gov.courtservice.xhibit.client.im.actions;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.jms.JMSException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.im.helper.IMConnectionResourceHelper;
import uk.gov.courtservice.xhibit.client.im.helper.IMLocationHelper;
import uk.gov.courtservice.xhibit.client.im.screens.IMDisplayPanel;
import uk.gov.courtservice.xhibit.client.im.util.IMMessageReceiver;
import uk.gov.courtservice.xhibit.client.im.util.IMStringFormatter;
import uk.gov.courtservice.xhibit.client.im.util.InstantMessageServices;
import uk.gov.courtservice.xhibit.client.im.util.InstantMessagingReceiverFactory;
import uk.gov.courtservice.xhibit.client.im.util.InstantMessagingServicesFactory;
import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * <p>
 * Title: IMReceiverAction
 * </p>
 * <p>
 * Description: XAction to start up Messaging receiver process
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

public class IMReceiverAction extends XAction implements PropertyChangeListener {
    private static final String INSTANT_MSG_RESOURCES = "XHIBITInstantMessageResources_en_GB";

    private static final String IM_DEFAULT_RETRY = "im.default.retry.number";

    private static final String IM_ERROR_RETRY = "im.onException.retry.number";

    private static final String IM_RETRY_SLEEP = "im.retry.sleep.period";

    private static final int IM_RETRIES = 10;

    private static final Logger log = CSServices.getLogger(IMReceiverAction.class);

    private IMMessageReceiver imReceiver;

    private InstantMessageServices ims = null;

    private IMDisplayPanel imDisplayPanel;

    /**
     * Constructor
     * 
     * @throws CSRecoverableException
     */
    public IMReceiverAction() throws CSRecoverableException {
        initialiseIMS();
    }

    public void xActionPerformed(ActionEvent parm1) throws java.lang.Exception, CSRecoverableException {
        InstantMessagingReceiverFactory.getInstance().getIMReceiver().getIMServices().cleanup();
    }

    private void initialiseIMS() {
        // Create a new JFrame to keep the receiver open
        // We could have Xhibit pass a reference to an object, but we
        // have to keep the builds separate
        try {
            String clientId = IMLocationHelper.getTerminalID();
            imDisplayPanel = new IMDisplayPanel();

            imReceiver = InstantMessagingReceiverFactory.getInstance().getIMReceiver();
            getIMServices().addPropertyChangeListener(this);
            log.debug("IMReceiverAction: Leaving default constructor");
        } catch (CSRecoverableException ex) {
            log.error("Could not initialise Instant Message Services: " + ex.getMessage());
        }

    }

    /**
     * Returns the current instance of InstantMessageServices. This is required
     * to close all connections when the action (IMReceiverAction) is closed.
     * 
     * @return InstantMessageServices
     */
    public InstantMessageServices getIMServices() throws CSRecoverableException {
        InstantMessageServices ims = InstantMessagingServicesFactory.getInstance().getSubscriptionMessagingServices();
        if (ims == null) {
            throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable");
        }
        return ims;
    }

    /**
     * As the receiver is initially created by the main Xhibit application, we
     * need to know when it is closed down so that the IMS connections can be
     * removed
     * 
     * @param enable
     */
    public void setEnabled(boolean enable) {
        log.debug("<<<<<>>>>>> IMRECEIVERACTION setEnabled <<<<>>>>>> " + enable);
        if (enable == false) {
            try {
                InstantMessageServices ims = getIMServices();
                try {
                    log.debug("IMReceiverAction: cleanup instantMessageServices");
                    // Cleanup the connections and subscriptions
                    ims.cleanup();
                    ims = null;
                } catch (JMSException ex) {
                    log.error("InstantMessageServices error on close " + ex.getMessage());
                }
            } catch (CSRecoverableException ex) {
                log.error("InstantMessageServices error on close " + ex.getMessage());
            }
        }
    }

    /**
     * PropertyChangeEvent is fired from InstantMessageServices onException.
     * When this occurs, the InstantMessageServices must be re-established in
     * order to continue the messagng process. retries is a configurable number
     * of times to retry to establishthe connection. There is a signifivant
     * sleep, so performance should not be impacted.
     * 
     * @param event
     */
    public void propertyChange(PropertyChangeEvent event) {
        log.debug("<<<>>> IMReceiverAction.propertyChange <<<>>>");

        ims = null;
        try {
            ims = getIMServices();
        } catch (CSRecoverableException ex) {
            log.error("CSRecoverableException retrieving InstantMessageServices " + ex.getMessage());
        } catch (Exception ex) {
            log.error("Exception retrieving InstantMessageServices " + ex.getMessage());
        }

        establishConnection(ims);
    }

    /**
     * Try to establish the connection with JMS
     * 
     * @param ims
     */
    private void establishConnection(InstantMessageServices ims) {
        int retries = 0;
        try {
            // Get the number of times to retry the connection process
            retries = IMConnectionResourceHelper.getConnectionRetries(IM_ERROR_RETRY);
        } catch (CSConfigurationException ex) {
            // Use the default
            retries = IM_RETRIES;
        }

        // Try to connect while InsnatMessgaeServices is null or the depth
        // of the tree is zero (connected to wrong server) AND the
        // number of retries has not been exceeded.
        while (ims == null && retries > 0) {
            log.debug("<<<<<< Inside connection loop >>>>>>>");

            try {
                // Try to get the instance of InstantMessageServices that
                // has the correct connection
                ims = getIMServices();
                // Add this action to the InstantMessageServices as the
                // propertyChangeListener
                ims.addPropertyChangeListener(this);

                // If we have a receiver, reset the connections to JMS
                if (imReceiver != null) {
                    imReceiver.reset();
                }
            } catch (CSRecoverableException ex) {
                log.error("Could not initialise Instant Message Services: " + ex.getMessage());
                try {
                    // Get sleep period and convert it to minutes
                    // If the server is being restarted, we need to wait a
                    // reasonable
                    // amaount of timebefore we try to connect again
                    log.debug("<<<>>>> IMRECEIVERACTION SLEEPING <<<>>>");
                    ims = null;
                    Thread.sleep(IMConnectionResourceHelper.getConnectionRetries(IM_RETRY_SLEEP) * 1000 * 60);
                    retries--;
                } catch (InterruptedException ext) {
                    // Do nothing
                }
            }
        }
    }

    private String getContext() throws CSRecoverableException {
        // sets the context to be the root context (e.g. jms/im) and the court
        // name.
        // This only shows locations under the court name, so the actual court
        // name is
        // not displayed in the tree.
        return IMLocationHelper.getRootContext() + "/"
                + IMStringFormatter.formatTreeNode(IMLocationHelper.getCourtID());
    }
}