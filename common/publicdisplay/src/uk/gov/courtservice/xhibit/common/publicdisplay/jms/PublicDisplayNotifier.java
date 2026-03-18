package uk.gov.courtservice.xhibit.common.publicdisplay.jms;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import uk.gov.courtservice.framework.business.services.CSMessageBeanNotifier;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;

/**
 * @author pznwc5
 * @version $Id: PublicDisplayNotifier.java,v 1.8 2006/06/05 12:28:24 bzjrnl Exp $
 */
public class PublicDisplayNotifier extends CSMessageBeanNotifier {
    /**
     * Default constructor
     */
    public PublicDisplayNotifier() {
        this(null);
    }

    /**
     * Constructor that initializes the context with the environment passed in.
     * 
     * @param environment
     *            The environment for the <code>InitialContext</code>.
     */
    public PublicDisplayNotifier(Hashtable environment) {
        super(environment, PublicDisplayJMSConstants.DEFAULT_TCF, PublicDisplayJMSConstants.DEFAULT_DESTINATION);
    }

    /**
     * Sends a public display event
     * 
     * @param event
     *            Public display event
     */
    public void sendMessage(PublicDisplayEvent event) {
        super.sendMessage(event);
    }

    protected void setMessageHeader(ObjectMessage msg) throws JMSException {
        final PublicDisplayEvent event = (PublicDisplayEvent) msg.getObject();

        if (log.isDebugEnabled()) {
            log
                    .debug("Message header:" + PublicDisplayJMSConstants.COURT_ID_PROPERTY_NAME + " : "
                            + event.getCourtId());
        }

        msg.setIntProperty(PublicDisplayJMSConstants.COURT_ID_PROPERTY_NAME, event.getCourtId().intValue());
    }
}
