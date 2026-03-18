package uk.gov.courtservice.xhibit.client.im.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.swing.tree.TreeNode;

/**
 * <p>
 * Title: TreeNode implementation representing a javax.naming.Context that
 * contains JMS Topics
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class is designed to provide support for dynamic representation of
 * topics for a JMS Court topic
 * </p>
 * <p>
 * Currently we only provide support for topics as at present Queues are out of
 * scope for requirements, in future a QueueNode may be added.
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
 * 
 * this class has a unit test - please update it
 */
public class JMSContextNode implements TreeNode {

    // private static Logger log =
    // Logger.getLogger(JMSContextNode.class.getName());

    private LinkedList children = new LinkedList();

    private JMSContextNode parent = null;

    private String name;

    private String jndiName;

    /**
     * Overloaded constructor for child context nodes.
     * 
     * @param context
     *            The context to create a node for.
     * @param parent
     *            The parent JMSContextNode.
     * @throws NamingException
     *             Usually thrown when an invalid name is passed.
     * @throws JMSException
     *             Usually thrown when there is an issue with the server.
     */
    public JMSContextNode(JMSContextNode parent, String jndiName) throws NamingException, JMSException {
        this.jndiName = jndiName;
        this.name = jndiName;
        this.parent = parent;
    }

    public TreeNode getChildAt(int childIndex) {
        return (TreeNode) children.get(childIndex);
    }

    public int getChildCount() {
        return children.size();
    }

    public TreeNode getParent() {
        return parent;
    }

    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public boolean isLeaf() {
        return children.size() == 0;
    }

    public Enumeration children() {
        return Collections.enumeration(children);
    }

    /**
     * Overriden version of the basic toString(), used to provide the display
     * name of the node in the tree.
     * 
     * @todo sort out naming convention such that a meaningful name can be
     *       provided.
     * @return the name of this node.
     */
    public String toString() {
        return NodeFormat.displayNodeName(name);
    }

    public String getJndiName() {
        return jndiName;
    }

    /**
     * Add a node to the list
     * 
     * @param node
     */
    public void add(TreeNode node) {
        children.add(node);
    }

}