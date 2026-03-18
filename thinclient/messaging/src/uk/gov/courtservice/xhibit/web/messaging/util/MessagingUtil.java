package uk.gov.courtservice.xhibit.web.messaging.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.jms.JMSException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.im.util.JMSContextNode;
import uk.gov.courtservice.xhibit.client.im.util.TopicNode;
import uk.gov.courtservice.xhibit.web.messaging.bean.DisplayLineNode;
import uk.gov.courtservice.xhibit.web.messaging.bean.FlattenedTree;

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
 * 
 * Edward CAwley - This code has been modified to deal with the wrapped classes
 * from the tree sorter.
 * 
 */

public class MessagingUtil {
    private static Logger log = CSServices.getLogger(MessagingUtil.class);

    private MessagingUtil() {
    }

    /**
     * Non recursive method that iterates over a tree model, returning an array
     * of DisplayLineNode[] that will simplify display of the tree.
     * 
     * @param treeModel
     *            The model to traverse.
     * @return an array of DisplayLineNode that are easier to deal with.
     */
    public static FlattenedTree walkTree(DefaultTreeModel treeModel) throws CSUnrecoverableException {
        // Heap containing the enumerations as we traverse them.
        Vector enumHeap = new Vector();

        // Array list that contains the results as they are built
        ArrayList output = new ArrayList();

        int maxDepth = 0;

        // Get the starting node.
        Object root = treeModel.getRoot();
        if (root != null) {
            Object rootUserObject = getUserObject(root);
            log.debug("Root user object : " + rootUserObject.getClass().getName());
            if (rootUserObject instanceof JMSContextNode) { // instance is
                // what we
                // expect?
                TreeNode currentNode;
                currentNode = (TreeNode) root;
                // initialise the depth variable that will keep track of our
                // position.
                int depth = 0;

                log.debug("Number of children for Root : " + currentNode.getChildCount());
                // Look at first node and put it's child enumeration on the
                // heap.
                Enumeration currentEnumeration = currentNode.children();
                enumHeap.add(depth, currentEnumeration);

                // Start first display line, we know it's a jms context here
                output.add(new DisplayLineNode(depth, (JMSContextNode) rootUserObject));

                // While there is something on the heap.
                while (depth >= 0) {
                    // Are there any children left. Remember we come back
                    // around here
                    // in the while loop.
                    if (currentEnumeration.hasMoreElements()) {
                        // Get the next child element
                        Object unknown = currentEnumeration.nextElement(); // need
                        // this
                        // for
                        // the
                        // structure
                        Object unknownUserObject = getUserObject(unknown); // need
                        // this
                        // for
                        // the
                        // value

                        log.debug("unknown user object class : " + unknownUserObject.getClass().getName());
                        if (unknownUserObject instanceof JMSContextNode) {// Another
                            // context.
                            // Increase depth.
                            depth++;

                            // if new maximum depth record it.
                            if (depth > maxDepth)
                                maxDepth = depth;

                            // Make new node, current node and get its's
                            // children.
                            currentNode = (TreeNode) unknown;
                            currentEnumeration = currentNode.children();

                            // Add the new enumeration to the heap.
                            enumHeap.add(depth, currentEnumeration);

                            // Output as display line.
                            output.add(new DisplayLineNode(depth, (JMSContextNode) unknownUserObject));
                        } else if (unknownUserObject instanceof TopicNode) {// A
                            // topic.
                            try {
                                // Output as a display line.
                                output.add(new DisplayLineNode(depth + 1, (TopicNode) unknownUserObject));
                            } catch (JMSException ex) {
                                log.error("Problem with a node", ex);
                                // Log the problem, but otherwise carry on
                                // ignoring the node.
                            }
                        }
                    } else { // Finished the current enumeration so.
                        // Step up in depth.
                        depth--;

                        if (depth >= 0) {// Only if there is anything
                            // left.
                            // go back to originating enumeration.
                            currentEnumeration = (Enumeration) enumHeap.elementAt(depth);
                        }
                    }
                }
            } else {
                throw new CSUnrecoverableException("Root user object unexpected class, "
                        + rootUserObject.getClass().getName());
            }
        } else {
            throw new CSUnrecoverableException("Tree model root null!");
        }
        // Cast to appropriate Array and return.
        DisplayLineNode[] returnArray = new DisplayLineNode[output.size()];
        output.toArray(returnArray);
        return new FlattenedTree(maxDepth, returnArray);
    }

    private static Object getUserObject(Object node) {
        Object dmtn = new DefaultMutableTreeNode(node);
        while (dmtn instanceof DefaultMutableTreeNode) {
            dmtn = ((DefaultMutableTreeNode) dmtn).getUserObject();
        }
        return dmtn;
    }

}