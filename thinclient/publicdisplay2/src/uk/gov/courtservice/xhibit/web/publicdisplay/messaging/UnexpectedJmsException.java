package uk.gov.courtservice.xhibit.web.publicdisplay.messaging;

import javax.jms.JMSException;

import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.PublicDisplayRuntimeException;

/**
 * DOCUMENT ME!
 * 
 * @author pznwc5 The exception is thrown when an unexpected JMSEXception occurs
 */
public class UnexpectedJmsException extends PublicDisplayRuntimeException {
    /**
     * DOCUMENT ME!
     * 
     * @param ex
     *            JMS message that is received
     */
    public UnexpectedJmsException(JMSException ex) {
        super(ex);
    }
}
