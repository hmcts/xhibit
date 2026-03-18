package uk.gov.courtservice.xhibit.client.im.util;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.DateFormat;
import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.im.helper.IMLocationHelper;
import uk.gov.courtservice.xhibit.client.util.XFrame;

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
 * @author Bob Boothby, Neil Entwistle
 * @version 1.0
 * @see javax.jms.Topic
 * @see javax.jms.Queue
 */

public class IMMessageReceiver extends MessageReceiver {

    private static final Logger log = CSServices.getLogger(IMMessageReceiver.class);

    private static final String JMS_DELIM = "/";

    private static final String MSG_SEPARATOR = "\n\n************************************\n";

    private static final String IM_TOPIC_SELECTOR = "TopicName = ";

    private static final String IM_COURT_SELECTOR = "Court = ";

    private static final String IM_COURT_SITE_SELECTOR = "CourtSite = ";

    private static final String IM_COURT_ROOM_SELECTOR = "CourtRoom = ";

    private static final String IM_AND = " AND ";

    private static final String IM_QUOTE = "'";

    private static IMTimerThread timer;

    private boolean initialised = false;

    private boolean firstTime = true;

    private StringBuffer bufferedMessage = new StringBuffer();

    private String messageLabel;

    static {
        // XhibitSingleton single = XhibitSingleton.getInstance();
        // AccessInfo accessInfo = single.getUserSession().getAccessInfo();

    }

    /**
     * Constructor
     * 
     * @throws CSRecoverableException
     */
    public IMMessageReceiver() throws CSRecoverableException {
        log.debug("IMMessageReceiver: constructor");

        try {
            log.debug("\nMESSAGING***: IMMessageReceiver: TerminalID = " + IMLocationHelper.getTerminalID());
            log.debug("\nMESSAGING***: IMMessageReceiver: TopicID = " + IMLocationHelper.getTopicID());
            // Set this as the JMS Durable Receiver
            setIMSDurableReceipt();
        } catch (JMSException ex) {
            throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable", ex);
        } catch (NamingException ex) {
            throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable", ex);
        }

        // Start the timer thread to pick up any outstanding messages
        startTimer();

        log.debug("IMMessageReceiver: constructor end");
    }

    /**
     * Creates a thread to run and pause for a while, so that any outstanding
     * messages are processed and displayed in a single frame
     */
    synchronized private void startTimer() {
        if (timer == null || !timer.isAlive()) {
            timer = new IMTimerThread("timer", this);
            timer.start();
        }
    }

    /**
     * Set this as the JMS Durable Receiver
     * 
     * @throws CSRecoverableException
     * @throws JMSException
     * @throws NamingException
     */
    private void setIMSDurableReceipt() throws CSRecoverableException, JMSException, NamingException {
        getIMServices().initialiseDurableReceipt(IMLocationHelper.getTopicIDForCourt(), this, getSelector(), false);
    }

    /**
     * Reset the insance of InstantMessageServices
     * 
     * @throws CSRecoverableException
     */
    public void reset() throws CSRecoverableException {
        log.debug("<<<<>>>>> IMMessageReceiver.reset <<<<>>>>>");
        try {
            this.initialised = false;
            this.firstTime = true;
            setIMSDurableReceipt();
            startTimer();
        } catch (NamingException ex) {
            throw new CSRecoverableException("IMMessageReceiver.reset", "IMMessageReceiver.reset", ex);
        } catch (JMSException ex) {
            throw new CSRecoverableException("IMMessageReceiver.reset", "IMMessageReceiver.reset", ex);
        }
    }

    /**
     * Provides basic display functionality on receiving a message.
     * 
     * @param message
     *            The message to be displayed.
     */
    public void onMessage(Message message) {
        log.debug("MESSAGING***: entering onMessage");
        if (isInitialised()) {
            // Check that the message is a text message
            if (message instanceof TextMessage) {
                try {
                    // Get the timestamp from the message and format it for
                    // display
                    DateFormat dispDate = DateFormat.getInstance();
                    bufferedMessage.append(dispDate.format(new Date(message.getJMSTimestamp())));

                    bufferedMessage.append(":\n");
                    bufferedMessage.append(formatMessage(((TextMessage) message).getText()));
                    getDisplayDialog(bufferedMessage.toString());
                } catch (JMSException ex) {
                    CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
                } catch (CSRecoverableException ex) {
                    CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
                }
            } else {
                // Do nothing - not a text message
            }

        } else {
            try {
                // If this is the first time that the message display has been
                // shown
                // the messages should be separated
                if (!firstTime) {
                    bufferedMessage.append(MSG_SEPARATOR);
                }
                log.debug("BufferedMessage = " + bufferedMessage.toString());

                DateFormat dispDate = DateFormat.getInstance();

                bufferedMessage.append(dispDate.format(new Date(message.getJMSTimestamp())));
                bufferedMessage.append(":\n");
                bufferedMessage.append(formatMessage(((TextMessage) message).getText()));
            } catch (JMSException ex) {
                CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
            }
            firstTime = false;
        }
        log.debug("MESSAGING***: leaving onMessage");
    }

    /**
     * Updates a boolean to indicate that the receive process has been fully
     * initialised
     * 
     * @param started
     */
    public synchronized void setIsInitialised(boolean started) {
        this.initialised = started;
    }

    /**
     * Returns a boolean to indicate that the receive process has been fully
     * initialised
     * 
     * @return boolean
     */
    public synchronized boolean isInitialised() {
        return this.initialised;
    }

    /**
     * Displays the message dialog on startup. This is controlled by an
     * IMTimerThread and loads stored messages in one frame, rather than
     * multiple frames.
     */
    public void showMultipleMessages() throws CSRecoverableException {
        log.debug("MESSAGING***: entering showMultipleMessages");
        if (bufferedMessage.length() > 0) {
            getDisplayDialog(bufferedMessage.toString());
        }
        log.debug("MESSAGING***: leaving showMultipleMessages");
    }

    /**
     * Method to return the location - the court concatenated with the terminal
     * id
     * 
     * @param s
     * @return
     */
    private String getSiteID() throws CSRecoverableException {
        StringBuffer id = new StringBuffer();
        id.append(NodeFormat.displayNodeName(formatString(IMLocationHelper.getLocationID())));
        id.append(JMS_DELIM + formatString(IMLocationHelper.getTerminalName()));

        return id.toString();
    }

    /**
     * Show the message(s) in a frame
     * 
     * @param text
     *            the message text
     * @throws CSRecoverableException
     */
    private void getDisplayDialog(String text) throws CSRecoverableException {
        log.debug("<<<>>> MESSAGE : " + text + " <<<>>>");
        // XFrame rather than JFrame as we need to display the correct icon
        XFrame dialog = new XFrame();
        dialog.setTitle("New Message: " + getSiteID());
        dialog.setBounds(0, 0, 300, 300);
        dialog.getContentPane().setLayout(new GridLayout(1, 1));

        JTextArea textArea = new JTextArea(text);
        textArea.setMinimumSize(new Dimension(300, 300));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);

        dialog.getContentPane().add(scrollPane);
        dialog.show();

        // Clear down the buffer once the message has been displayed.
        bufferedMessage.delete(0, bufferedMessage.length());
    }

    private String formatString(String s) {
        return s.toLowerCase().replace(' ', '_');
    }

    /**
     * Returns the current instance of InstantMessageServices. This is required
     * to close all connections when the action (IMReceiverAction) is closed.
     * 
     * @return InstantMessageServices
     */
    public InstantMessageServices getIMServices() throws CSRecoverableException {
        return InstantMessagingServicesFactory.getInstance().getSubscriptionMessagingServices();
    }

    private String formatMessage(String s) {
        StringBuffer buf = new StringBuffer();
        int index = s.indexOf(": ");
        if (index >= 0) {
            buf.append(s.substring(0, s.indexOf(": ") + 1));
            buf.append("\n");
            buf.append(s.substring(s.indexOf(": ") + 2));
        } else {
            buf.append(s);
        }
        return buf.toString();
    }

    private String getSelector() throws CSRecoverableException {
        StringBuffer selector = new StringBuffer();
        selector.append(IM_TOPIC_SELECTOR);
        selector.append(IM_QUOTE);
        selector.append(IMStringFormatter.formatJNDIToName(IMLocationHelper.getLocationID()));
        selector.append(IM_QUOTE);
        return selector.toString();
    }
}
