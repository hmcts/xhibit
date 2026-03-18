package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.ConfigurationChangeEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.jms.PublicDisplayJMSConstants;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtConfigurationChange;
import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.UnexpectedJmsException;

/**
 * @author pznwc5
 * 
 * Transformer for transforming text messages. Currently a text message is sent
 * when a daily list is processed by Mercator
 */
public class TextMessageTransformer implements MessageTransformer {

    /** Logger */
    private static Logger log = Logger.getLogger(TextMessageTransformer.class);

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms.MessageTransformer#transformMessage(javax.jms.Message)
     */
    public PublicDisplayEvent transform(Message msg) {
        try {
            if (!(msg instanceof TextMessage)) {
                throw new InvalidMessageException(msg);
            }

            TextMessage txtMsg = (TextMessage) msg;
            log.debug("Text message received: " + txtMsg);

            // The property should be there as we use selector based on this
            int courtId = txtMsg.getIntProperty(PublicDisplayJMSConstants.COURT_ID_PROPERTY_NAME);
            log.debug("Court id: " + courtId);

            // Create the court configuration change
            String courtName = "";
            CourtConfigurationChange change = new CourtConfigurationChange(courtId, courtName, true);
            log.debug("Change: " + change);

            // Create the event
            return new ConfigurationChangeEvent(change);

        } catch (JMSException ex) {
            log.fatal(ex.getMessage(), ex);
            throw new UnexpectedJmsException(ex);
        }
    }

}
