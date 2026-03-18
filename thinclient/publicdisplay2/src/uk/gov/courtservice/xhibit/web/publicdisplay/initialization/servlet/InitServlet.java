/*
 * Created on 17-Dec-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package uk.gov.courtservice.xhibit.web.publicdisplay.initialization.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms.MessagingMode;

/**
 * DOCUMENT ME!
 * 
 * @author pznwc5 Servlet used for managing the initialization remove
 */
public class InitServlet extends HttpServlet {

    /** Logger */
    private static Logger log = Logger.getLogger(InitServlet.class);

    /** Retry period */
    private static final String RETRY_PERIOD = "retry.period";

    /** Number of workers per subscription */
    private static final String NUM_SUBSCRIPTION_WORKERS = "num.subscription.workers";

    /** Messaging mode */
    private static final String MESSAGING_MODE = "messaging.mode";

    /** Number of workers for initialization */
    private static final String NUM_INITIALIZATION_WORKERS = "num.initialization.workers";

    /** Delay after each initialization */
    private static final String INITIALIZATION_DELAY = "initialization.delay";

    /** P2P messaging mode constant */
    private static final String P2P = "P2P";

    /** Pub/Sub messaging mode constant */
    private static final String PUB_SUB = "PubSub";

    /**
     * Override the init method to initialize services
     * 
     * @param config
     *            the servlet config
     * 
     * @throws ServletException
     *             TODO:
     */
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        // Get an instance of the initialization service
        InitializationService service = InitializationService.getInstance();

        // Set the retry interval for initialization
        setRetryPeriod(service);

        // Set the number of workers per subscription
        setNumSubscriptionWorkers(service);

        // Set the delay after each initialization
        setInitializationDelay(service);

        // Set the number of workers for initialization
        setNumInitializationWorkers(service);

        // Set the messaging mode
        setMessagingMode(service);

        // Start initialization
        service.initialize();

        log.debug("Initialization service scheduled");

    }

    /**
     * Override the service method to stop the servlet from serving requests
     * 
     * @param req
     *            The HTTP request
     * @param res
     *            The HTTP response
     */
    public void service(HttpServletRequest req, HttpServletResponse res) {
        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    /**
     * Override the destroy method to cleanup resources
     */
    public void destroy() {
        InitializationService.getInstance().destroy();
    }

    /**
     * Sets the messaging mode
     * 
     * @param service
     */
    private void setMessagingMode(InitializationService service) {
        String msgMode = this.getInitParameter(MESSAGING_MODE);
        if (msgMode != null) {
            if (msgMode.equals(P2P)) {
                service.setMessagingMode(MessagingMode.P2P);
            } else if (msgMode.equals(PUB_SUB)) {
                service.setMessagingMode(MessagingMode.PUB_SUB);
            }
            log.debug("Messaging mode: " + msgMode);
        }
    }

    /**
     * Sets the number of initialization workers
     * 
     * @param service
     */
    private void setNumInitializationWorkers(InitializationService service) {
        String numInitializationWorkers = this.getInitParameter(NUM_INITIALIZATION_WORKERS);
        if (numInitializationWorkers != null) {
            service.setNumInitializationWorkers(Integer.parseInt(numInitializationWorkers));
            log.debug("Initialization workers: " + numInitializationWorkers);
        }
    }

    /**
     * Sets initialization delay
     * 
     * @param service
     */
    private void setInitializationDelay(InitializationService service) {
        String initializationDelay = this.getInitParameter(INITIALIZATION_DELAY);
        if (initializationDelay != null) {
            service.setInitializationDelay(Long.parseLong(initializationDelay));
            log.debug("Initialization delay: " + initializationDelay);
        }
    }

    /**
     * Sets the number of subscription workers
     * 
     * @param service
     */
    private void setNumSubscriptionWorkers(InitializationService service) {
        String numSubscriptionWorkers = getInitParameter(NUM_SUBSCRIPTION_WORKERS);
        if (numSubscriptionWorkers != null) {
            service.setNumSubscriptionWorkers(Integer.parseInt(numSubscriptionWorkers));
            log.debug("Subscription workers: " + numSubscriptionWorkers);
        }
    }

    /**
     * Sets the retry period for initialization
     * 
     * @param service
     */
    private void setRetryPeriod(InitializationService service) {
        String retryPeriod = this.getInitParameter(RETRY_PERIOD);
        if (retryPeriod != null) {
            service.setRetryPeriod(Long.parseLong(retryPeriod));
            log.debug("Retry period: " + retryPeriod);
        }
    }

}
