package uk.gov.courtservice.xhibit.client.im.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.NamingException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

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

public class TestIMServices {
    private InstantMessageServices instantMessageServices;

    private static final GridBagConstraints defaultGridBag = new GridBagConstraints(0, 0, 1, 1, 0.0d, 0.0d,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0);

    public TestIMServices(String clientID) throws javax.jms.JMSException, javax.naming.NamingException {
        instantMessageServices = new InstantMessageServices(clientID, Session.AUTO_ACKNOWLEDGE);
    }

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++)
            System.out.println(args[i]);

        try {
            if (args.length > 2 && args.length < 5) {

                final TestIMServices tims = new TestIMServices(args[0]);
                JFrame dialog = new JFrame();
                dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dialog.setBounds(0, 0, 500, 400);
                dialog.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent we) {
                        tims.cleanup();
                    }
                });
                if (args.length == 3 && args[1].equals("sender")) {
                    dialog.setTitle("Message sender: " + args[2]);
                    dialog.getContentPane().setLayout(new GridLayout(1, 1));
                    dialog.getContentPane().add(tims.makeSendPanel(args[2]));
                    dialog.setResizable(false);
                    dialog.doLayout();
                    dialog.show();
                } else if (args.length == 3 && args[1].equals("receiver")) {
                    dialog.setTitle("Message receiver: " + args[2]);
                    tims.instantMessageServices.initialiseDurableReceipt(args[2], new MessageReceiver());
                    dialog.setResizable(false);
                    dialog.doLayout();
                    dialog.show();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void cleanup() {
        try {
            this.instantMessageServices.cleanup();
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }

    public JTree getIMTree(String startingContextName) throws javax.jms.JMSException, javax.naming.NamingException {
        TreeModel tm = instantMessageServices.getTreeModel(startingContextName);
        JTree jt = new JTree(tm);
        jt.setRootVisible(false);
        jt.setShowsRootHandles(true);
        jt.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        return jt;
    }

    public JPanel makeSendPanel(String startingContextName) throws javax.jms.JMSException, javax.naming.NamingException {
        Border border = BorderFactory.createLineBorder(Color.black);
        JPanel sendPanel = new JPanel(new GridBagLayout(), true);

        JTree jt = getIMTree(startingContextName);
        jt.setBorder(border);
        jt.setMinimumSize(new Dimension(290, 390));

        JTextArea messageTextArea = new JTextArea(30, 30);
        messageTextArea.setBorder(border);
        messageTextArea.setMaximumSize(new Dimension(300, 370));
        messageTextArea.setLineWrap(true);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new sendMessageActionListener(messageTextArea, jt, instantMessageServices));

        GridBagConstraints gbc = (GridBagConstraints) defaultGridBag.clone();

        gbc.gridheight = 2;
        sendPanel.add(jt, gbc.clone());

        gbc.gridheight = 1;
        gbc.gridx += 1;
        sendPanel.add(messageTextArea, gbc.clone());

        gbc.gridy += 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        sendPanel.add(sendButton, gbc.clone());

        sendPanel.doLayout();

        return sendPanel;
    }
}

class sendMessageActionListener implements ActionListener {
    private JTextArea messageTextArea;

    private JTree jt;

    private InstantMessageServices instantMessageServices;

    sendMessageActionListener(JTextArea messageTextArea, JTree jt, InstantMessageServices instantMessageServices) {
        this.instantMessageServices = instantMessageServices;
        this.jt = jt;
        this.messageTextArea = messageTextArea;
    }

    public void actionPerformed(java.awt.event.ActionEvent ae) {
        TreeSelectionModel tsm = jt.getSelectionModel();
        TreePath[] paths = tsm.getSelectionPaths();

        for (int i = 0; i < paths.length; i++) {
            Object lastNode = paths[i].getLastPathComponent();
            if (lastNode instanceof TopicNode) {
                Topic topic = ((TopicNode) lastNode).getTopic();
                String messageText = messageTextArea.getText();
                try {
                    instantMessageServices.publishTextMessage(messageText, "", "");
                } catch (NamingException ex) {
                    ex.printStackTrace();
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
            }
        }
        messageTextArea.setText("");
    }

}
