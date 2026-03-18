package uk.gov.courtservice.xhibit.web.publicdisplay.initialization.servlet;

import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.PublicDisplayRuntimeException;

/**
 * DOCUMENT ME!
 * 
 * @author pznwc5 The exception is thrown when an unexpected JMSEXception occurs
 */
public class InitializationException extends PublicDisplayRuntimeException {
    /**
     * DOCUMENT ME!
     * 
     * @param ex
     *            JMS message that is received
     */
    public InitializationException(Exception ex) {
        super(ex);
    }
}
