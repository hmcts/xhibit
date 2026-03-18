package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.jms;

import javax.jms.Message;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;

/**
 * @author pznwc5
 * 
 * An interface for transforming JMS messages to public display events
 */
public interface MessageTransformer {
    public PublicDisplayEvent transform(Message msg);
}
