package uk.gov.courtservice.xhibit.client.im.util.sorters;

// jdk
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: TreeSorter
 * </p>
 * <p>
 * Description: Sorts a tree using the comparator provided.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: TreeSorter.java,v 1.3 2006/06/05 12:30:23 bzjrnl Exp $
 */

public class TreeSorter {
    private static Logger log = CSServices.getLogger(TreeSorter.class);

    private TreeSorter() {
    }

    /**
     * 
     * @param curNode
     *            The node in the tree from which to being the sorting.
     * @param comp
     *            The comparator to use to sort the tree.
     * @return
     */
    public static DefaultMutableTreeNode sortTree(TreeNode curNode, Comparator comp) {
        DefaultMutableTreeNode mutableNode = new DefaultMutableTreeNode(curNode);

        // sort the roots children
        TreeNode[] objs = new TreeNode[((TreeNode) mutableNode.getUserObject()).getChildCount()];
        log.debug("sortDestinations() mutableNode.getChildCount() " + mutableNode.getChildCount());
        Enumeration children = ((TreeNode) mutableNode.getUserObject()).children();
        for (int i = 0; children.hasMoreElements(); i++) {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(children.nextElement());
            objs[i] = child;
        }

        Arrays.sort(objs, comp);
        mutableNode.removeAllChildren();

        // insert newly ordered children
        log.debug("sortDestinations() objs.length " + objs.length);
        for (int i = 0; i < objs.length; i++) {
            DefaultMutableTreeNode orderedNode = (DefaultMutableTreeNode) objs[i];
            if (((TreeNode) orderedNode.getUserObject()).getChildCount() > 0) {
                log.debug("sortDestinations() orderedNode " + orderedNode.toString());
                mutableNode.add(sortTree(((TreeNode) orderedNode.getUserObject()), comp));
            } else {
                mutableNode.add(orderedNode);
            }
        }

        return mutableNode;
    }
}