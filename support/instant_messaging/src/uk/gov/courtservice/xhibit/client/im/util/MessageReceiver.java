package uk.gov.courtservice.xhibit.client.im.util;

import java.awt.GridLayout;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * <p>
 * Title: Primitive message reception class that displays a frame containing the
 * content of a TextMessage.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class is used to display received messages either from Topics or Queues.
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
 * @see javax.jms.Topic
 * @see javax.jms.Queue
 */

public class MessageReceiver implements MessageListener {

    /**
     * Provides basic display functionality on receiving a message.
     * 
     * @param message
     *            The message to be displayed.
     */
    public void onMessage(Message message) {
        System.out.println("Message Received: " + message.getClass().getName());
        if (message instanceof TextMessage) {
            System.out.println("Text Message");
            JFrame dialog = new JFrame("New Message:");
            dialog.setBounds(0, 0, 200, 200);
            dialog.getContentPane().setLayout(new GridLayout(1, 1));
            try {
                Message m = message;

                String text = ((TextMessage) message).getText();
                dialog.getContentPane().add(new JLabel(text));
            } catch (JMSException ex) {
                ex.printStackTrace();
            }
            dialog.show();
            dialog.toFront();
        } else {
            //
        }
    }
}