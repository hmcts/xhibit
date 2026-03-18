package uk.gov.courtservice.xhibit.web.messaging;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jms.InvalidClientIDException;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.im.util.InstantMessageServices;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Manages the creation and destruction of instant message services
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class manages the InstantMessagingServices objects in particular it
 * allows for the sharing of InstantMessagingServices objects bettween sessions
 * and their tidying up on failover.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Will Fardell (Xdevelopment) 2003
 * @version 1.0
 */

public class InstantMessageServicesManager extends Thread {

    // The log4j logger
    private static final Logger log = CSServices.getLogger(InstantMessageServicesManager.class);

    /**
     * The singleton instance
     */
    private static InstantMessageServicesManager instance;

    /**
     * Get access to the singleton instance
     * 
     * @return the singleton
     */
    public static synchronized InstantMessageServicesManager getInstance() {
        if (instance == null) {
            instance = new InstantMessageServicesManager();
        }
        return instance;
    }

    /**
     * The managed instant messaging services keyed against terminal identifier
     */
    private final Map instantMessageServicesMap = new HashMap();

    /**
     * Stop this singleton being constructed externally
     */
    private InstantMessageServicesManager() {
        Runtime.getRuntime().addShutdownHook(this);
    }

    /**
     * Get an instance of InstantMessageServices (note this can be shared)
     * 
     * @param terminalIdentifier
     *            the services to get
     * @return the InstantMessageServices
     */
    public synchronized InstantMessageServices getInstantMessageServices(String terminalIdentifier)
            throws FrameworkException {
        InstantMessageServices instantMessageServices = (InstantMessageServices) instantMessageServicesMap
                .get(terminalIdentifier);
        if (instantMessageServices == null) {
            log.debug("Creating InstantMessageServices " + terminalIdentifier + ".");
            instantMessageServices = createInstantMessageServices(terminalIdentifier);
            instantMessageServicesMap.put(terminalIdentifier, instantMessageServices);
        }
        return instantMessageServices;
    }

    /**
     * Cleanup an instance of the InstantMessagingServices
     * 
     * @param terminalIdentifier
     *            the services to cleanup
     */
    public synchronized void cleanupInstantMessageServices(String terminalIdentifier) {
        try {
            log.debug("Cleaning InstantMessageServices " + terminalIdentifier + ".");
            InstantMessageServices instantMessageServices = (InstantMessageServices) instantMessageServicesMap
                    .get(terminalIdentifier);
            if (instantMessageServices != null) {
                instantMessageServices.cleanup();
                instantMessageServicesMap.remove(terminalIdentifier);
            }
        } catch (JMSException ex) {
            log.error("Could not clean up the JMS session properly for InstantMessageServices", ex);
        }

    }

    /**
     * Thread Implementation: Used as a shutdown hook to clean up the instant
     * message services
     */
    public synchronized void run() {
        Iterator terminaIdentifiers = instantMessageServicesMap.keySet().iterator();
        while (terminaIdentifiers.hasNext()) {
            cleanupInstantMessageServices((String) terminaIdentifiers.next());
        }
    }

    /**
     * Create the instant messageing service
     * 
     * @return this session's InstantMessageServices instance.
     */
    private InstantMessageServices createInstantMessageServices(String terminalIdentifier) throws FrameworkException {
        try {
            return new InstantMessageServices(terminalIdentifier, Session.CLIENT_ACKNOWLEDGE); // The
                                                                                                // messages
                                                                                                // must
                                                                                                // be
            // acknowledged.
        } catch (InvalidClientIDException ex) {
            log
                    .error(
                            "Could not initialise the JMS session properly for InstantMessageServices. InvalidClientIDException: ",
                            ex);
            throw new FrameworkException("im.jms.InvalidClientIDException",
                    "Could not initialise the JMS session properly for InstantMessageServices.", ex);
        } catch (JMSException ex) {
            log.error("Could not initialise the JMS session properly for InstantMessageServices. JMSException: ", ex);
            throw new FrameworkException("im.jms.JMSException",
                    "Could not initialise the JMS session properly for InstantMessageServices.", ex);
        } catch (NamingException ex) {
            log.error("Could not initialise the JMS session properly for InstantMessageServices. NamingException:", ex);
            throw new FrameworkException("im.jms.JMSException",
                    "Could not initialise the JMS session properly for InstantMessageServices.", ex);
        }
    }

}
