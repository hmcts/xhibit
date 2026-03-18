package uk.gov.courtservice.xhibit.client.im.util;

import java.util.Enumeration;

import javax.jms.Topic;
import javax.swing.tree.TreeNode;

/**
 * <p>
 * Title: TreeNode representing a Topic
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class is part of a set of classes that represents topics new Topics
 * within a JNDI Court context.
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
 */

public class TopicNode implements TreeNode {
    private JMSContextNode parent;

    private String name;

    private Topic topic;

    private String jndiName;

    /**
     * Constructor for a TopicNode.
     * 
     * @param parent
     *            The parent JMSContextNode.
     * @param topic
     *            The topic that this node represents.
     * @throws javax.jms.JMSException
     */
    public TopicNode(String name, JMSContextNode parent, Topic topic, String jndiName) throws javax.jms.JMSException {
        this.name = name;
        this.parent = parent;
        this.topic = topic;
        this.jndiName = jndiName;
    }

    /**
     * Constructor for a TopicNode.
     * 
     * @param parent
     *            The parent JMSContextNode.
     * @param topic
     *            The topic that this node represents.
     * @throws javax.jms.JMSException
     */
    public TopicNode(String name, JMSContextNode parent) throws javax.jms.JMSException {
        this.name = name;
        this.parent = parent;
    }

    /**
     * The Topic that this node represents.
     * 
     * @return The Topic that this node represents.
     */
    public Topic getTopic() {
        return topic;
    }

    public TreeNode getChildAt(int childIndex) {
        return null;
    }

    public int getChildCount() {
        return 0;
    }

    public TreeNode getParent() {
        return parent;
    }

    public int getIndex(TreeNode node) {
        return -1;
    }

    public boolean getAllowsChildren() {
        return false;
    }

    public boolean isLeaf() {
        return true;
    }

    public Enumeration children() {
        return null;
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

    public String getName() {
        return name;
    }
}