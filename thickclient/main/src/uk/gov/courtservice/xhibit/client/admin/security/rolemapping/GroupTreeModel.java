package uk.gov.courtservice.xhibit.client.admin.security.rolemapping;

//Support classes for the tree structure
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * <p>
 * Title: XHIBIT group hierarchy
 * </p>
 * <p>
 * Description: The class represents the XHIBIT group hierarchy as a tree
 * structure. The class assumes that individual group in the hierarchy have
 * unique names
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class GroupTreeModel implements TreeModel {

    // List of nodes
    private TreeMap nodes = new TreeMap();

    // Tree model listeners
    private LinkedList listeners = new LinkedList();

    // Root group
    private String root;

    /**
     * Constructor initializes the root group
     * 
     * @param root
     */
    public GroupTreeModel(TreeMap nodes, String root) {
        this.nodes = nodes;
        this.root = root;
    }

    /**
     * Adds tree model listener
     * 
     * @param l
     */
    public void addTreeModelListener(TreeModelListener l) {
        listeners.add(l);
    }

    /**
     * Gets the specified sub group
     * 
     * @param parent
     *            group
     * @param index
     *            of the sub group
     * @return
     */
    public Object getChild(Object parent, int index) {

        LinkedList children = (LinkedList) nodes.get(parent);
        return children.get(index);

    }

    /**
     * Gets the number of sub groups for the parent group
     * 
     * @param parent
     *            group
     * @return
     */
    public int getChildCount(Object parent) {

        LinkedList children = (LinkedList) nodes.get(parent);
        return children.size();

    }

    /**
     * Gets the index of sub group in parent group
     * 
     * @param parent
     * @param child
     * @return
     */
    public int getIndexOfChild(Object parent, Object child) {

        LinkedList children = (LinkedList) nodes.get(parent);
        return children.indexOf(child);

    }

    /**
     * Returns the root group
     * 
     * @return
     */
    public Object getRoot() {
        return root;
    }

    /**
     * Returns whether the group is a leaf
     * 
     * @param node
     * @return
     */
    public boolean isLeaf(Object node) {

        if (node == null)
            return false;
        LinkedList children = (LinkedList) nodes.get(node);
        return children.size() == 0;

    }

    /**
     * Removes the tree model listener
     * 
     * @param l
     */
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(l);
    }

    /**
     * Notified whenever the treepath is changed
     * 
     * @param path
     * @param newValue
     */
    public void valueForPathChanged(TreePath path, Object newValue) {

        Iterator it = ((LinkedList) listeners.clone()).iterator();
        while (it.hasNext())
            ((TreeModelListener) it.next()).treeNodesChanged(new TreeModelEvent(this, path));

    }

    /**
     * Refreshes the data
     * 
     * @param newDerRoles
     */
    public void refreshData(TreeMap nodes, String root) {
        this.nodes = nodes;
        this.root = root;
    }

}