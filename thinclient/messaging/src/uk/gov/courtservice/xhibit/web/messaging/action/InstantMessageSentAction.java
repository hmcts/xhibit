package uk.gov.courtservice.xhibit.web.messaging.action;

import java.util.Enumeration;
import java.util.HashMap;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.im.util.AdHocMessageServices;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.messaging.IMSessionInfo;
import uk.gov.courtservice.xhibit.web.messaging.bean.DisplayLineNode;
import uk.gov.courtservice.xhibit.web.messaging.bean.FlattenedTree;
import uk.gov.courtservice.xhibit.web.messaging.util.MessagingFormatHelper;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */

public class InstantMessageSentAction extends AbstractAction {
    private static final Logger log = CSServices.getLogger(InstantMessageSentAction.class);

    protected void internalPerformAction(ActionEnvironment actionEnvironment)
            throws uk.gov.courtservice.xhibit.web.framework.util.FrameworkException {
        /*
         * Not really necessary here. if( !checkToken()) { throw new
         * DuplicateFormSubmissionException();}
         */

        HashMap errors = new HashMap();
        boolean error = false;
        try {
            String message = new String(AdHocMessageServices.htmlUnEscapeCharacters((String) actionEnvironment
                    .getRequestParameter("message")));

            if (message.length() > 0) {
                errors.put("messageValue", message);
            } else {
                errors.put("messageError", "stringfield.emptyString");
            }
            IMSessionInfo imsi = (IMSessionInfo) actionEnvironment
                    .getSessionParameter(IMSessionInfo.INSTANT_MESSAGE_SESSION);

            Enumeration enumeration = (Enumeration) actionEnvironment.getRequestParameterNames();
            int i = 0;
            if (!error) {
                while (enumeration.hasMoreElements()) {
                    String parmName = (String) enumeration.nextElement();
                    if (parmName.startsWith("CHB_")) {
                        String destination = parmName.substring(4);
                        String topic = MessagingFormatHelper.splitJMSParameter(destination, 1);
                        String selector = MessagingFormatHelper.splitJMSParameter(destination, 2);
                        log.debug("Selector = " + selector);
                        log.debug("Topic = " + topic);
                        // The fromname is now obtained from the senders
                        // location,
                        // rather than from the Topic, as previously
                        String fromname = imsi.getFormattedLocation();
                        fromname = fromname.substring(fromname.lastIndexOf("/") + 1);
                        fromname = fromname.replace('_', ' ');
                        log.debug("fromname = " + fromname);
                        imsi.getIMServices().publishTextMessage("From : " + fromname + "\n" + message, selector, topic);
                        i++;
                    }
                }
                if (i == 0) // they selected no recipients!
                {
                    errors.put("recipientError", "norecipientsselected");
                    error = true;
                }
            }
        } catch (JMSException ex) {
            throw new FrameworkException(ex);
        } catch (NamingException ex) {
            throw new FrameworkException(ex);
        }
        if (error) {
            FlattenedTree tree = ((IMSessionInfo) actionEnvironment
                    .getSessionParameter(IMSessionInfo.INSTANT_MESSAGE_SESSION)).getDestinationTree();

            if (tree == null) {
                throw new FrameworkException("im.jms.nodestinations",
                        "Unable to build tree of instant message destinations.");
            }
            DisplayLineNode[] lines = tree.getLines();
            // sorting has been removed from this class, now performed in
            // InstantMEssageServices where it is customised by court
            actionEnvironment.setRequestParameter("lines", lines);
            actionEnvironment.setRequestParameter("errors", errors);

            actionEnvironment.setResponseName("sendinstantmessagecomplete");
        } else {
            actionEnvironment.setResponseName("instantmessagesentcomplete");
        }
    }

}