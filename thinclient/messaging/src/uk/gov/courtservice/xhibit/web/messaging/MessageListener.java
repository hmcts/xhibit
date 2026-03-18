package uk.gov.courtservice.xhibit.web.messaging;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Message Listener class, check the session for imsessioninfo when the
 * session is invalidated, clean the instant messasging session.
 * </p>
 * <p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward CAwley
 * @version 1.0
 */
public class MessageListener implements HttpSessionListener {

    private static final Logger logger = CSServices.getLogger(MessageListener.class);

    public MessageListener() {
    }

    public void sessionCreated(HttpSessionEvent se) { // do nothing . . .
    }

    public void sessionDestroyed(HttpSessionEvent se) { // clean the messaging
        // session if one exists
        HttpSession session = se.getSession();
        logger.debug("Calling sessionDestroyed in MessageListener");
        if (session.getAttribute(IMSessionInfo.INSTANT_MESSAGE_SESSION) != null) {
            IMSessionInfo imsession = (IMSessionInfo) session.getAttribute(IMSessionInfo.INSTANT_MESSAGE_SESSION);
            logger.debug("Calling imsession.cleanup() in sessionDestroyed in MessageListener");
            imsession.cleanup();
        }
    }
}