package uk.gov.courtservice.xhibit.client.im.util;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.im.helper.IMLocationHelper;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;

/**
 * <p>
 * Title: IMSendMessageActionListener
 * </p>
 * <p>
 * Description: Sends an instant message using the IM Services
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class IMSendMessageActionListener extends XAction {
    /**
     * <p>
     * Title: sendMessageActionListener
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
     * @author Neil Entwistle
     * @version 1.0
     */
    private static final String IM_LOCATION_ID = "locationID";

    private static final Logger log = CSServices.getLogger(sendMessageActionListener.class);

    private JTextArea messageTextArea;

    private JTree jt;

    private XDialog parent;

    StringBuffer result = new StringBuffer();

    /**
     * Constructor to set up the messaging services. The instance of instant
     * message needs information from a JTree and a JTextArea to send the
     * message
     * 
     * @param messageTextArea
     * @param jt
     *            JTree showing the available JMS locations
     */
    public IMSendMessageActionListener(JTextArea messageTextArea, JTree jt, XDialog parent) {
        this.parent = parent;
        this.jt = jt;
        this.messageTextArea = messageTextArea;
    }

    /**
     * Sends the appropriate message when the Send button is pressed
     * 
     * @param ae
     *            ActionEvent
     * @throws CSRecoverableException
     */
    public void xActionPerformed(java.awt.event.ActionEvent ae) throws CSRecoverableException {
        log.debug("MESSAGING***: entering xActionPerformed");
        TreeSelectionModel tsm = jt.getSelectionModel();
        TreePath[] paths = tsm.getSelectionPaths();

        if (paths != null) {
            for (int i = 0; i < paths.length; i++) {
                DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
                if (lastNode.getUserObject() instanceof TopicNode) {
                    TopicNode selectedNode = (TopicNode) lastNode.getUserObject();
                    String messageSelector = IMLocationHelper.getMessageSelector(selectedNode);
                    String messageDestination = IMLocationHelper.getMessageDestination(selectedNode);
                    String messageText = IMLocationHelper.getCourtLocation() + messageTextArea.getText();
                    try {
                        log.debug("<<<>>> Message: |" + messageText + "| being sent to: |" + messageSelector
                                + "| <<<>>>");
                        getIMServices().publishTextMessage(messageText, messageSelector, messageDestination);
                    } catch (NamingException ex) {
                        log.debug("sendMessageActionListener:actionPerformed:NamingException " + ex.getMessage());
                        this.parent.dispose();
                        throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable", ex);
                    } catch (JMSException ex) {
                        log.debug("sendMessageActionListener:actionPerformed:JMSException " + ex.getMessage());
                        this.parent.dispose();
                        throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable", ex);
                    } catch (Exception ex) {
                        log.debug("sendMessageActionListener:actionPerformed:Exception " + ex.getMessage());
                        this.parent.dispose();
                        throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable", ex);
                    }
                }
            }
            messageTextArea.setText("");
            messageTextArea.requestFocus();
        }
        log.debug("MESSAGING***: leaving xActionPerformed");
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

}