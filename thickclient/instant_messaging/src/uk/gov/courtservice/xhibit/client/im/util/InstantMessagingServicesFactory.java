package uk.gov.courtservice.xhibit.client.im.util;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.im.helper.IMLocationHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: InstantMessagingServicesFactory
 * </p>
 * <p>
 * Description: Returns an instance of InstantMessagingServicesFactory that is
 * used to return an instance of InstantMessageServices. this may be extended if
 * single instance of ad messaging services is required
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

public class InstantMessagingServicesFactory {

    private static final Logger log = CSServices.getLogger(InstantMessagingServicesFactory.class);

    private static InstantMessageServices ims;

    private static InstantMessagingServicesFactory imsFactory;

    private static XhibitSingleton xSingleton = XhibitSingleton.getInstance();

    private String terminalID;

    static {
        imsFactory = new InstantMessagingServicesFactory();
    }

    /**
     * Private constructor to prevent instantiation
     */
    private InstantMessagingServicesFactory() {
        // private constructor
    }

    /**
     * Returns an instance of InstantMessagingServicesFactory. This currently
     * only returns an instance of InstantMessagingServices, but may be extended
     * to allow for ad hoc messages
     * 
     * @return InstantMessagingServicesFactory
     */
    public static InstantMessagingServicesFactory getInstance() {
        log.debug("MESSAGING***: Returning InstantMessagingServicesFactory");
        return imsFactory;
    }

    /**
     * Returns an instance of InstantMessagingServices. Only one instance shold
     * be run per client ID - for send and receive
     * 
     * @return InstantMessageServices
     * @throws NamingException
     * @throws JMSException
     */
    public synchronized InstantMessageServices getSubscriptionMessagingServices() throws CSRecoverableException {
        log.debug("MESSAGING***: clientID" + IMLocationHelper.getTerminalID());

        // If the InstantMessageServices has lost connection to the JMS
        // reset the connections and set it to null so that a
        // new instance is created
        if (ims != null && !ims.isConnectionMade()) {
            cleanupIMS();
        }

        // If the InstantMessageServices is null or the terminal id has chnaged
        // create a new instance of InstantMessageServices
        if (ims == null
                || (this.terminalID != null && this.terminalID.equalsIgnoreCase(IMLocationHelper.getTerminalID()) == false)) {
            this.terminalID = IMLocationHelper.getTerminalID();
            try {
                ims = new InstantMessageServices(this.terminalID, Session.AUTO_ACKNOWLEDGE);
            } catch (NamingException ex) {
                log.debug("NamingException " + ex.getMessage());
                throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable", ex);
            } catch (JMSException ex) {
                log.debug("JMSException " + ex.getMessage());
                throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable", ex);
            } catch (Exception ex) {
                log.debug("Exception " + ex.getMessage());
                throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable", ex);
            }
        }
        log.debug("MESSAGING***: Returning InstantMessageServices");
        return ims;
    }

    /**
     * Stop all existing InsantMessageServices connections
     * 
     * @throws CSRecoverableException
     */
    public void cleanupIMS() throws CSRecoverableException {
        log.debug("<<<<<>>>>> cleanupIMS <<<<<>>>>>");
        if (ims != null) {
            try {
                log.debug("<<<<<>>>>> CLEANING UP IMS <<<<<>>>>>");
                ims.cleanup();
                ims = null;
                try {
                    // Pause while JMS sorts itself out
                    ims = null;
                    Thread.sleep(1L * 1000L);
                } catch (InterruptedException ext) {
                    //
                }
            } catch (JMSException ex) {
                throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable", ex);
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