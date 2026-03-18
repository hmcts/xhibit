package uk.gov.courtservice.xhibit.client.im.helper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.im.util.IMDocumentListener;
import uk.gov.courtservice.xhibit.client.im.util.IMLimitedTextDocument;
import uk.gov.courtservice.xhibit.client.im.util.IMResourceHelper;
import uk.gov.courtservice.xhibit.client.im.util.IMSendMessageActionListener;
import uk.gov.courtservice.xhibit.client.im.util.InstantMessageServices;
import uk.gov.courtservice.xhibit.client.im.util.InstantMessagingServicesFactory;
import uk.gov.courtservice.xhibit.client.im.util.InstantMessagingServicesHelper;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: IMSenderDialog
 * </p>
 * <p>
 * Description: Instant / Ad-Hoc Messaging main dialog Sends instnat and ad-hoc
 * messages from the client to the selected destinations
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

public class IMSenderHelper {

    private static final String IM_TITLE = "im.dialog.title";

    private static final String IM_DLG_BTN_TXT = "im.dialog.btn.text";

    private static final String IM_BTN_SEND_LBL = "im.send.btn.txt";

    private static final String IM_OK_BTN_TXT = "OK";

    private static final String IM_CAN_BTN_TXT = "Cancel";

    private static final String IM_TAB_LABEL = "im.tab.label";

    private static final String IM_ADHOC_TAB_LABEL = "im.tab.adhoc.label";

    private static final String IM_PAGER_BTN_LBL = "im.rbtn.pager.label";

    private static final String IM_SMS_BTN_LBL = "im.rbtn.sms.label";

    private static final String NETWORK_LABEL = "im.cbx.network.label";

    private static final String DESTINATION_LABEL = "im.cbx.destination.label";

    private static final String NUMBER_LABEL = "im.txt.number.label";

    private static final String TREE_LABEL = "im.tree.label";

    private static final String TEXT_LABEL = "im.text.label";

    private static final int IM_MESSAGE_LEN = 300;

    private static final String INSTANT_MSG_RESOURCES = "XHIBITInstantMessageResources_en_GB";

    private static final String IM_DEFAULT_RETRY = "im.default.retry.number";

    private static final String IM_ERROR_RETRY = "im.onException.retry.number";

    private static final String IM_RETRY_SLEEP = "im.retry.sleep.period";

    private static final int IM_RETRIES = 20;

    private static final GridBagConstraints defaultGridBag = new GridBagConstraints(0, 0, 1, 1, 0.0d, 0.0d,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0);

    private static Logger log = CSServices.getLogger(IMSenderHelper.class);

    private JTextArea messageTextArea;

    private JTree jt;

    private JScrollPane scrollPane;

    private IMDocumentListener messageListener;

    private JButton sendButton;

    private IMSendMessageActionListener sendMessageActionListener;

    private XDialog parent;

    private InstantMessageServices ims = null;

    /**
     * Return the users pc id
     * 
     * @return the terminal id
     * @throws CSRecoverableException
     */
    private String getClientID() throws CSRecoverableException {
        return IMLocationHelper.getTerminalID();
    }

    /**
     * Get the IM JMS tree from the instant mesaging services using the context
     * name
     * 
     * @param startingContextName
     *            the context name
     * @return the JMS tree
     * @throws javax.jms.JMSException
     * @throws javax.naming.NamingException
     */
    private JTree getIMTree(String startingContextName) throws javax.jms.JMSException, javax.naming.NamingException,
            CSRecoverableException {
        log.debug("MESSAGING***: entering getIMTree: context = " + startingContextName);

        XhibitSingleton xSingleton = XhibitSingleton.getInstance();
        TreeModel tm = InstantMessagingServicesHelper.getTreeModel(startingContextName, xSingleton.getCourtId(),
                getInstantMessageServices());

        // If no child nodes, throw exception
        if (((DefaultMutableTreeNode) tm.getRoot()).getDepth() == 0) {
            throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable");
        }

        JTree jt = new JTree();
        jt.setRootVisible(false);
        jt.setShowsRootHandles(true);
        jt.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        // sorting has been removed from this class, now performed in
        // InstantMessageServices where it is customised by court
        jt = new JTree(tm);

        log.debug("MESSAGING***: leaving getIMTree");

        return jt;
    }

    /**
     * Constructs a JPanel for Internal (Instant) messaging. The panel contains
     * a JTree containing the available destinations, a text field to enter the
     * number and a text area for the message. The message is limited to 120
     * characters.
     * 
     * @param startingContextName
     * @return JPanel
     * @throws javax.jms.JMSException
     * @throws javax.naming.NamingException
     */
    public JPanel makeSendInternalPanel(String startingContextName, XDialog parent) throws javax.jms.JMSException,
            javax.naming.NamingException, CSRecoverableException {
        log.debug("MESSAGING***: entering makeSendExternalPanel");
        this.parent = parent;

        Border border = BorderFactory.createLineBorder(Color.black);
        JPanel sendPanel = new JPanel(new GridBagLayout(), true);

        JLabel treeLabel = new JLabel(IMResourceHelper.getResourceString(TREE_LABEL));
        treeLabel.setMinimumSize(new Dimension(200, 17));
        JLabel textLabel = new JLabel(IMResourceHelper.getResourceString(TEXT_LABEL));
        textLabel.setMinimumSize(new Dimension(200, 17));

        jt = getIMTree(startingContextName);

        jt.setBorder(border);

        scrollPane = new JScrollPane(jt);
        scrollPane.setBorder(border);
        scrollPane.setMinimumSize(new Dimension(290, 40));
        scrollPane.setPreferredSize(new Dimension(300, 200));

        messageTextArea = new JTextArea();
        // Set the length of the message to be that maximim - the length of the
        // sending location
        messageTextArea.setDocument(new IMLimitedTextDocument(IM_MESSAGE_LEN
                - IMLocationHelper.getCourtLocation().length()));
        messageTextArea.setBorder(border);
        messageTextArea.setLineWrap(true);
        messageTextArea.setWrapStyleWord(true);

        JScrollPane textScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textScrollPane.setMinimumSize(new Dimension(10, 10));
        textScrollPane.setPreferredSize(new Dimension(200, 100));
        textScrollPane.setMaximumSize(new Dimension(300, 370));
        textScrollPane.getViewport().add(messageTextArea);

        sendButton = new JButton(IMResourceHelper.getResourceString(IM_BTN_SEND_LBL));

        GridBagConstraints gbc = (GridBagConstraints) this.defaultGridBag.clone();

        sendPanel.add(treeLabel, gbc.clone());

        gbc.gridx++;

        sendPanel.add(textLabel, gbc.clone());

        gbc.gridx = 0;
        gbc.gridy++;

        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        sendPanel.add(scrollPane, gbc.clone());
        scrollPane.setMinimumSize(new Dimension(300, 200));

        gbc.gridheight = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        gbc.gridx += 1;
        sendPanel.add(textScrollPane, gbc.clone());

        gbc.gridy += 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        sendPanel.add(sendButton, gbc.clone());

        sendPanel.doLayout();

        log.debug("MESSAGING***: leaving makeSendExternalPanel");

        return sendPanel;
    }

    /**
     * Returns the instance of InstantMessageServices.
     * 
     * @return InstantMessageServices
     */
    public InstantMessageServices getInstantMessageServices() throws CSRecoverableException {
        return InstantMessagingServicesFactory.getInstance().getSubscriptionMessagingServices();
    }

    /**
     * Reset the message area whenthe dialog is redisplayed If there has been a
     * problem creating the panel, e.g. the topic cannot be found in JMS, the
     * text fields will be null.
     */
    public void reset() {
        if (messageTextArea != null) {
            messageTextArea.setText("");
        }
    }

    /**
     * Reset the message area whenthe dialog is redisplayed If there has been a
     * problem creating the panel, e.g. the topic cannot be found in JMS, the
     * text fields will be null.
     */
    public void reset(String startingContextName) throws NamingException, JMSException, CSRecoverableException {
        reset();

        if (scrollPane == null) {
            throw new CSRecoverableException("im.jms.unavailable", "JMS is currently unavailable");
        }
        // Remove the existing tree
        scrollPane.getViewport().remove(jt);
        // Remove the listeners
        sendButton.removeActionListener(sendMessageActionListener);
        messageTextArea.getDocument().removeDocumentListener(messageListener);

        // Rebuild the tree and add to teh scroll pane
        jt = getIMTree(startingContextName);
        jt.getSelectionModel().resetRowSelection();
        // jt.expandRow(1);
        scrollPane.getViewport().add(jt);

        // Add the listeners
        sendMessageActionListener = new IMSendMessageActionListener(messageTextArea, jt, this.parent);
        sendButton.addActionListener(sendMessageActionListener);
        sendButton.setEnabled(false);

        messageListener = new IMDocumentListener(sendButton, jt);
        messageTextArea.getDocument().addDocumentListener(messageListener);
        jt.getSelectionModel().addTreeSelectionListener(messageListener);
    }

    /**
     * Sorts the TreeNode as supplied by InstantMessagingServices
     * 
     * @param curNode
     * @return
     */
    private DefaultMutableTreeNode sortTree(TreeNode curNode) {
        log.debug("$$$ sortTree curNode " + curNode.toString() + " $$$");
        DefaultMutableTreeNode mutableNode = new DefaultMutableTreeNode(curNode);
        // set up comparator
        Comparator comp = new Comparator() {
            public int compare(Object o1, Object o2) {
                return compare((DefaultMutableTreeNode) o1, (DefaultMutableTreeNode) o2);
            }

            public int compare(DefaultMutableTreeNode n1, DefaultMutableTreeNode n2) {
                String s1, s2;
                s1 = (String) n1.getUserObject().toString();
                s2 = (String) n2.getUserObject().toString();
                return (s1.compareTo(s2));
            }
        };

        // sort the roots children
        TreeNode[] objs = new TreeNode[((TreeNode) mutableNode.getUserObject()).getChildCount()];
        log.debug("$$$ sortTree mutableNode.getChildCount() " + mutableNode.getChildCount() + " $$$");
        Enumeration children = ((TreeNode) mutableNode.getUserObject()).children();
        for (int i = 0; children.hasMoreElements(); i++) {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(children.nextElement());
            objs[i] = child;
        }

        Arrays.sort(objs, comp);
        mutableNode.removeAllChildren();

        // insert newly ordered children
        log.debug("$$$ sortTree objs.length " + objs.length + " $$$");
        for (int i = 0; i < objs.length; i++) {
            DefaultMutableTreeNode orderedNode = (DefaultMutableTreeNode) objs[i];
            if (((TreeNode) orderedNode.getUserObject()).getChildCount() > 0) {
                log.debug("$$$ orderedNode " + orderedNode.toString() + " $$$");
                mutableNode.add(sortTree(((TreeNode) orderedNode.getUserObject())));
            } else {
                mutableNode.add(orderedNode);
            }
        }
        return mutableNode;
    }

}