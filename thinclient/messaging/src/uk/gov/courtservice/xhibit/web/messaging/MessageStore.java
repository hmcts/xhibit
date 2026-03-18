package uk.gov.courtservice.xhibit.web.messaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: MessageStore suporting the accumulation of Messages.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class is designed to decouple the receipt of messages from requests by
 * the user.
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
 * @history Kevin Buckthorpe 13 Oct 2003 Added date and time to message text.
 */

public class MessageStore implements MessageListener {
    private static Logger log = CSServices.getLogger(MessageStore.class);

    // Holds the messages that have been accumulated.
    private ArrayList pendingMessages = new ArrayList();

    /**
     * Creates a new message store object, ready to receive messages.
     */
    public MessageStore() {
    }

    /**
     * Implementation from the MessageListener interface, called when messages
     * are received.
     * 
     * @param message
     *            The message received, for now should only be of type
     *            TextMessage.
     */
    public void onMessage(Message message) {
        // Note that the messages will not be acknowledged until a client
        // requires comes
        // in to retrieve them.
        addMessage(message);
    }

    /**
     * This method adds a message to the store, for now only supports
     * TextMessages.
     * 
     * @param message
     *            the message to be added.
     */
    private synchronized void addMessage(Message message) {
        if (message instanceof TextMessage) {
            pendingMessages.add(message);
        }
    }

    /**
     * Get the accumulated messages, emptying the store. Acknowledge the
     * successful receipt of the messages.
     * 
     * @return the accumulated mesages.
     */
    public synchronized String[] getMessages() {
        ArrayList messages = new ArrayList();
        Iterator iter = pendingMessages.iterator();
        while (iter.hasNext()) {
            try {
                TextMessage message = (TextMessage) iter.next();
                Date date = new Date(message.getJMSTimestamp());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
                String fDate = formatter.format(date);
                String textMessage = fDate + "\n" + message.getText();

                messages.add(textMessage);
                // Acknowledge succesful receipt.
                message.acknowledge();
            } catch (JMSException ex) {
                ex.printStackTrace();
                log.error("Could not retrieve message:", ex);
            }
        }
        String[] returnArray = new String[messages.size()];
        messages.toArray(returnArray);
        pendingMessages.clear();
        return returnArray;
    }

    /**
     * Says if there are any new messages.
     * 
     * @return if there are any new messages.
     */
    public synchronized boolean hasNewMessages() {
        return this.pendingMessages.size() > 0;
    }
}