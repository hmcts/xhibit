/*
 * Created on 08-Jan-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package uk.gov.courtservice.xhibit.web.publicdisplay.initialization.servlet;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.common.publicdisplay.jms.PublicDisplayJMSConstants;
import uk.gov.courtservice.xhibit.web.publicdisplay.configuration.DisplayConfigurationReader;
import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.MessageController;
import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms.MessagingMode;

/**
 * @author pznwc5 <p/> To change the template for this generated type comment go
 *         to Window - Preferences - Java - Code Generation - Code and Comments
 */
public class InitializationService {
    /**
     * One second :-)
     */
    public static final long ONE_SECOND = 1000L;

    /**
     * Logger
     */
    private static Logger log = Logger.getLogger(InitializationService.class);

    /**
     * Singleton instance
     */
    private static final InitializationService self = new InitializationService();

    /**
     * A flag to mark successful initialization
     */
    private boolean initialized;

    /**
     * Should the initialisation fail, this variable will hold the exception
     * that caused it to fail.
     */
    private Throwable initialisationFailure = null;

    /**
     * Number of workers per subscription
     */
    private int numSubscriptionWorkers = 1;

    /**
     * Number of workers for initialization
     */
    private int numInitializationWorkers = 5;

    /**
     * Delay after each initialization
     */
    private long initializationDelay = 120 * ONE_SECOND;

    /**
     * Messaging controller
     */
    private MessageController messageController = new MessageController(numSubscriptionWorkers);

    /**
     * Retry period in case of initialization failure
     */
    private long retryPeriod = 60 * ONE_SECOND;

    /**
     * Messaging mode
     */
    private MessagingMode messagingMode = MessagingMode.PUB_SUB;

    /**
     * DOn't instantiate me
     */
    private InitializationService() {
    }

    /**
     * Whether the service has been initialized
     * 
     * @return
     */
    public synchronized boolean isInitialized() {
        return initialized;
    }

    /**
     * Set the number of workers per subscription
     * 
     * @param numSubscriptionWorkers
     */
    public void setNumSubscriptionWorkers(int numSubscriptionWorkers) {
        this.numSubscriptionWorkers = numSubscriptionWorkers;
        messageController = null;
        messageController = new MessageController(numSubscriptionWorkers);
        log.debug("Message controller initialized with " + numSubscriptionWorkers + " workers");
    }

    /**
     * Set the number of workers for initialization
     * 
     * @param numInitializationWorkers
     */
    public void setNumInitializationWorkers(int numInitializationWorkers) {
        this.numInitializationWorkers = numInitializationWorkers;
    }

    /**
     * Set the delay after each initialization
     * 
     * @param initializationDelay
     */
    public void setInitializationDelay(long initializationDelay) {
        this.initializationDelay = initializationDelay;
    }

    /**
     * Set the retry period
     * 
     * @param retryPeriod
     */
    public void setRetryPeriod(long retryPeriod) {
        this.retryPeriod = retryPeriod;
    }

    /**
     * Set the messaging mode
     * 
     * @param messagingMode
     */
    public void setMessagingMode(MessagingMode messagingMode) {
        this.messagingMode = messagingMode;
    }

    /**
     * Method to start initialization
     */
    public void initialize() {
        Thread th = new Thread(new Runnable() {
            public void run() {
                try {
                    if (log.isDebugEnabled()) {
                        long startTime = System.currentTimeMillis();
                        long startTotalMemory = Runtime.getRuntime().totalMemory();
                        long startFreeMemory = Runtime.getRuntime().freeMemory();
                        long startUsedMemory = startTotalMemory - startFreeMemory;
                        _run();
                        long endTime = System.currentTimeMillis();
                        long endTotalMemory = Runtime.getRuntime().totalMemory();
                        long endFreeMemory = Runtime.getRuntime().freeMemory();
                        long endUsedMemory = endTotalMemory - endFreeMemory;
                        log.debug("Public display initialisation took " + (endTime - startTime)
                                + " ms and aproximatly " + (endUsedMemory - startUsedMemory) + " bytes.");
                    } else {
                        _run();
                    }
                } catch (Throwable t) {
                    log.fatal(t, t);
                    initialisationFailure = t;
                }
            }

            public void _run() throws Throwable {
                // Run until initialization is done
                while (!checkMidtier()) {
                    try {
                        Thread.sleep(retryPeriod);
                    } catch (InterruptedException ex) {
                        log.error(ex.getMessage(), ex);
                    }
                }

                // DO initialization
                doInitialize();

                // Set the initialization flag
                synchronized (InitializationService.class) {
                    initialized = true;
                }

            }
        });
        th.start();
    }

    /**
     * Method to destroy
     */
    public void destroy() {
        messageController.shutdown();
        log.debug("Public display uninitialized");
    }

    /**
     * Singleton accessor
     * 
     * @return
     */
    public static InitializationService getInstance() {
        // TODO Auto-generated method stub
        return self;
    }

    public Throwable getInitialisationFailure() {
        return initialisationFailure;
    }

    /**
     * Perform initialization
     */
    private void doInitialize() {

        // Get initial context to the publicdisplay
        InitialContext ctx = CSServices.getServiceLocator().getInitialContext();
        log.debug("Initial context created");

        try {
            // Lookup the destination and the connection factory
            Destination dest = (Destination) ctx.lookup(PublicDisplayJMSConstants.DEFAULT_DESTINATION);
            log.debug("Destination looked up: " + PublicDisplayJMSConstants.DEFAULT_DESTINATION);

            ConnectionFactory cf = (ConnectionFactory) ctx.lookup(PublicDisplayJMSConstants.DEFAULT_TCF);
            log.debug("Connection factory looked up: " + PublicDisplayJMSConstants.DEFAULT_TCF);

            // Get the court ids
            int courtIds[] = DisplayConfigurationReader.getInstance().getConfiguredCourtIds();
            for (int i = 0; i < courtIds.length; i++) {
                // Add subscription
                messageController.addSubscription(dest, cf, courtIds[i], messagingMode);
                log.debug("Subscription created for court: " + courtIds[i]);
            }
            // Start event processing
            messageController.startEventProcessing();
            log.debug("Event processing started");

            // Start initial rendering
            new DocumentInitializer(courtIds, numInitializationWorkers, initializationDelay).initialize();
        } catch (NamingException ex) {
            log.fatal(ex.getMessage(), ex);
            throw new InitializationException(ex);
        } finally {
            try {
                if (ctx != null)
                    ctx.close();
            } catch (NamingException ex) {
                log.warn(ex.getMessage(), ex);
            }
        }
    }

    /**
     * Method checks whether the publicdisplay is running
     * 
     * @return
     */
    private boolean checkMidtier() {
        try {
            // Check whether we can get a connection to the publicdisplay
            InitialContext initialContext = CSServices.getServiceLocator().getInitialContext();
            initialContext.close();
            log.info("Connected to publicdisplay");
            // Get display configuration reader
            DisplayConfigurationReader.getInstance();
            log.info("Initialized display configuration reader");
            return true;
        } catch (Throwable ne) {
            log.warn(ne, ne);
            log.warn("Midtier unavailable");
            return false;
        }
    }

}
