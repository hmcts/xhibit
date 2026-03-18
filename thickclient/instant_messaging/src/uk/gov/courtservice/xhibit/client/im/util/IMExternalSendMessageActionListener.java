package uk.gov.courtservice.xhibit.client.im.util;

import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.messaging.InvalidDeviceException;
import uk.gov.courtservice.xhibit.business.exceptions.messaging.MessagingException;
import uk.gov.courtservice.xhibit.business.services.messaging.MessagingControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.services.messaging.AdHocMessageValue;
import uk.gov.courtservice.xhibit.client.im.entry.IMTextArea;
import uk.gov.courtservice.xhibit.client.im.helper.IMLocationHelper;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * 
 * <p>
 * Title: IMExternalSendMessageActionListener
 * </p>
 * <p>
 * Description: Sends messages from the External (Ad Hoc) message panel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class IMExternalSendMessageActionListener extends XAction {
    private static final Logger log = CSServices.getLogger(sendMessageActionListener.class);

    private JTextArea messageTextArea;

    private JComboBox cbxDevice;

    private JTextField phoneNumber;

    private XDialog parent;

    private MessagingControllerBeanBusinessDelegate messagingControllerBeanBusinessDelegate;

    /**
     * Constructor
     * 
     * @param messageTextArea
     *            the text area containing th emessage
     * @param cbxDevice
     *            the combo box listing the device types
     * @param phoneNumber
     *            the text field containing the number
     * @param adhocMessageServices
     *            the instance of adhoc messaging services
     * @param parent
     *            the parent of the dialog
     */
    public IMExternalSendMessageActionListener(IMTextArea messageTextArea, JComboBox cbxDevice, JTextField phoneNumber,
            XDialog parent) {
        this.parent = parent;
        this.messageTextArea = messageTextArea;
        this.cbxDevice = cbxDevice;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sends the appropriate message when the Send button is pressed
     * 
     * @param ae
     *            ActionEvent
     * @throws CSRecoverableException
     */
    public void xActionPerformed(java.awt.event.ActionEvent ae) throws CSRecoverableException {

        String messageText = messageTextArea.getText();
        AdHocMessageValue adhocMessageValue = new AdHocMessageValue(messageText, IMLocationHelper.getExtTopicID(),
                (String) cbxDevice.getSelectedItem(), IMStringFormatter.stripSpaces(phoneNumber.getText()));
        log.debug("MESSAGING*** Device : " + adhocMessageValue.getReceiverDeviceType());
        log.debug("MESSAGING*** Message : " + adhocMessageValue.getMessage());
        log.debug("MESSAGING*** Number : " + adhocMessageValue.getReceiverNumber());
        log.debug("MESSAGING*** Location : " + adhocMessageValue.getSenderLocation());
        messagingControllerBeanBusinessDelegate = XhibitDelegateHelper.getMessagingDelegate();

        try {
            messagingControllerBeanBusinessDelegate.sendMessage(adhocMessageValue);
        } catch (InvalidDeviceException ex) {
            this.parent.dispose();
            throw new CSRecoverableException("im.jms.unavailable", "Invalid Device", ex);
        } catch (MessagingException ex) {
            this.parent.dispose();
            throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable", ex);
        } catch (Exception ex) {
            this.parent.dispose();
            throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable", ex);
        }

        messageTextArea.setText("");
        phoneNumber.setText("");
        phoneNumber.requestFocus();
    }
}