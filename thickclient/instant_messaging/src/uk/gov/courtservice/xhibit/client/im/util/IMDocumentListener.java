package uk.gov.courtservice.xhibit.client.im.util;

import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * <p>
 * Title: IMDocumentListener
 * </p>
 * <p>
 * Description: Listens for text events on a JTextField Document and
 * enables/disables a button if the length of the text is > 0
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

public class IMDocumentListener implements DocumentListener, TreeSelectionListener {
    private JButton button;

    private JTree destTree;

    private Document doc;

    private boolean isSendExternalMsg;

    /**
     * Constructor
     * 
     * @param btn
     *            the button to enable/disable
     */
    public IMDocumentListener(JButton btn) {
        button = btn;
    }

    /**
     * Constructor
     * 
     * @param btn
     *            the button to enable/disable
     * @param isSendExternalMsg
     *            true if this is an external message
     */
    public IMDocumentListener(JButton btn, boolean isSendExternalMsg) {
        this(btn);
        this.isSendExternalMsg = isSendExternalMsg;
    }

    /**
     * Constructor
     * 
     * @param btn
     *            the button to enable/disable
     * @param tree
     *            the tree to listen on
     */
    public IMDocumentListener(JButton btn, JTree tree) {
        this(btn);
        destTree = tree;
    }

    /**
     * Listener method
     * 
     * @param e
     */
    public void changedUpdate(DocumentEvent e) {
        enableButton(e);
    }

    /**
     * Listener method
     * 
     * @param e
     */
    public void removeUpdate(DocumentEvent e) {
        enableButton(e);
    }

    /**
     * Listener method
     * 
     * @param e
     */
    public void insertUpdate(DocumentEvent e) {
        enableButton(e);
    }

    /**
     * Enable/disable the button
     * 
     * @param e
     */
    private void enableButton(DocumentEvent e) {
        doc = e.getDocument();
        if (destTree != null && isSelected()) {
            // Enable the button if the text field has text entered
            button.setEnabled(doc.getLength() > 0);
        }

        // no destTree for external messages. bug fix 53527
        else if (isSendExternalMsg && doc.getLength() > 0) {
            button.setEnabled(true);
        }

        else {
            button.setEnabled(false);
        }
    }

    /**
     * Listener method
     * 
     * @param e
     */
    public void valueChanged(TreeSelectionEvent e) {
        if (doc != null && isSelected()) {
            // Enable the button if the text field has text entered
            button.setEnabled(doc.getLength() > 0);
        } else {
            button.setEnabled(false);
        }
    }

    /**
     * Checks that a topic node has been selected on the tree
     * 
     * @return
     */
    private boolean isSelected() {
        TreeSelectionModel tsm = destTree.getSelectionModel();
        TreePath[] paths = tsm.getSelectionPaths();
        boolean selected = false;

        if (paths != null) {
            for (int i = 0; i < paths.length; i++) {
                DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
                if (lastNode.getChildCount() == 0)// . instanceof
                // TopicNode)
                {
                    selected = true;
                    return selected;
                }
            }
        }
        return selected;
    }
}