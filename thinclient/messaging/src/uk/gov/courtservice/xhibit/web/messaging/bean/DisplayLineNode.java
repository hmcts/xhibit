package uk.gov.courtservice.xhibit.web.messaging.bean;

import javax.jms.JMSException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.im.util.JMSContextNode;
import uk.gov.courtservice.xhibit.client.im.util.TopicNode;
import uk.gov.courtservice.xhibit.web.messaging.util.MessagingFormatHelper;

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

public class DisplayLineNode implements java.io.Serializable, java.lang.Comparable {
    public static final String TOPIC = "topic";

    public static final String QUEUE = "queue";

    public static final String CONTEXT = "context";

    private static final Logger log = CSServices.getLogger(DisplayLineNode.class);

    private String type;

    private int depth;

    private String displayName;

    private String jndiName;

    public DisplayLineNode(int depth, JMSContextNode node) {
        this.depth = depth;
        type = CONTEXT;
        displayName = node.toString();
        jndiName = getMessageParameters(node);
    }

    public DisplayLineNode(int depth, TopicNode node) throws JMSException {
        this.depth = depth;
        type = TOPIC;
        displayName = node.toString();
        jndiName = getMessageParameters(node);
    }

    public DisplayLineNode(int depth, DefaultMutableTreeNode node) throws JMSException {
        this.depth = depth;
        type = TOPIC;
        displayName = node.toString();
        jndiName = getMessageParameters(node);
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getjndiName() {
        return this.jndiName;
    }

    public int getDepth() {
        return this.depth;
    }

    public String getType() {
        return this.type;
    }

    public String toString() {
        return type + " : " + depth + ":" + jndiName + ":" + displayName;
    }

    public int compareTo(Object o) {
        if (o instanceof DisplayLineNode) {
            return displayName.compareTo(((DisplayLineNode) o).getDisplayName());
        }
        return 0;
    }

    /**
     * Produce the external message selector from the TreeNode
     * 
     * @param node
     * @return
     */
    private String getMessageParameters(TreeNode node) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(MessagingFormatHelper.getMessageDestination(node));
        buffer.append(MessagingFormatHelper.DEST_SEL_DELIM);
        buffer.append(MessagingFormatHelper.getMessageSelector(node));
        log.debug("buffer = " + buffer.toString());
        return buffer.toString();
    }
}