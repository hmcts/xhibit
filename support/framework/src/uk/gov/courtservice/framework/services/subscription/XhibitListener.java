package uk.gov.courtservice.framework.services.subscription;

// JDK
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Joseph Babad
 * @version 1.0
 */

public abstract class XhibitListener implements MessageListener {
    private static Logger log = CSServices.getLogger(XhibitListener.class);

    public XhibitListener() {
    }

    /**
     * Called on receipt of a JMS message.
     * 
     * @param msg
     *            Message received.
     *            <P>
     *            <B>NOTE: No exception thrown here because of the JMS interface</B>
     */
    public void onMessage(Message msg) {
        String methodName = "onMessage() - ";
        ObjectMessage objectMessage;
        CSAbstractValue object;

        log.debug(methodName + "called");

        try {
            if (msg instanceof ObjectMessage) {
                objectMessage = (ObjectMessage) msg;
                // Enumeration enum = objectMessage.getPropertyNames();
                // if (enum != null )
                // {
                // while (enum.hasMoreElements())
                // {
                // String prop = (String)enum.nextElement();
                // log.debug( prop + " " +
                // objectMessage.getStringProperty(prop));
                // }
                // }

                // Get the object
                object = (CSAbstractValue) objectMessage.getObject();
                // Call the specific handler...
                newMessage(object);
            }
        } catch (JMSException jms) {
            log.fatal(methodName + jms.toString());
        }
    }

    public abstract String getListenerType();

    public abstract void newMessage(CSAbstractValue object);
}
