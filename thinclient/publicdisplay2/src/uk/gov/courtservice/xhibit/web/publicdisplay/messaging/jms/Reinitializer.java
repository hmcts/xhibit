package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.web.publicdisplay.initialization.servlet.InitializationService;

/**
 * @author pznwc5 <p/> Handles a JMS exception schedules a re-initialization
 */
public class Reinitializer implements ExceptionListener {

    // Logger
    private static final Logger LOG = Logger.getLogger(Reinitializer.class);

    /**
     * Handles a JMS exception schedules a re-initialization
     * 
     * @param ex
     *            JMS exception
     */
    public void onException(JMSException ex) {
        LOG.fatal(ex.getMessage(), ex);
        InitializationService service = InitializationService.getInstance();
        try {
            // Destroy
            service.destroy();
        } finally {
            // Re-initialize
            service.initialize();
            LOG.info("Scheduled reinitialization");
        }
    }

}
