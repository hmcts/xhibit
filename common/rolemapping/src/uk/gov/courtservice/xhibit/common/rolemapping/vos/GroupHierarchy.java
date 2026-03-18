package uk.gov.courtservice.xhibit.common.rolemapping.vos;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * <p>
 * Title: XHIBIT group hierarchy
 * </p>
 * 
 * <p>
 * Description: The class represents the XHIBIT security group hierarchy as a
 * tree structure. The class assumes that individual groups in the hierarchy
 * have unique names
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class GroupHierarchy implements Serializable {
	
	static final long serialVersionUID = -2400515400304845583L;
	
    /** The security group nodes */
    private TreeMap nodes = new TreeMap();

    /** The root group name */
    private String root;

    /**
     * Constructor initializes the root group
     * 
     * @param root
     *            the group name that is the root of the Xhibit security group
     *            hierarchy
     */
    public GroupHierarchy(String root) {
        this.root = root;
        nodes.put(root, new LinkedList());
    }

    /**
     * Get the root group
     * 
     * @return the root (group name) of the group hierarchy.
     */
    public String getRootGroup() {
        return root;
    }

    /**
     * This method adds a new node (group) to the hierarchy
     * 
     * @param parent
     *            the parent group for the new node.
     * @param child
     *            the new node to add to the hierarchy.
     * 
     * @throws IllegalArgumentException
     */
    public void addGroup(String parent, String child) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent is null");
        }

        if (child == null) {
            throw new IllegalArgumentException("Child is null");
        }

        ((LinkedList) nodes.get(parent)).add(child);
        nodes.put(child, new LinkedList());
    }

    /**
     * Returns all groups in the hierarchy as a flat list
     * 
     * @return all the groups.
     */
    public LinkedList getAllGroups() {
        LinkedList groups = new LinkedList();
        groups.addAll(nodes.keySet());

        return groups;
    }

    /**
     * Returns all the ancestors for this node (group)
     * 
     * @param child
     *            for which ancestors are to be found
     * 
     * @return Ancestors for the node
     * 
     * @throws IllegalArgumentException
     */
    public LinkedList getAncestors(String child) {
        if (child == null) {
            throw new IllegalArgumentException("Child is null");
        }

        LinkedList ancestors = new LinkedList();

        String parent = getParent(child);

        while (parent != null) {
            ancestors.add(parent);
            parent = getParent(parent);
        }

        return ancestors;
    }

    /**
     * Returns the immediate ancestor for the child
     * 
     * @param child
     *            for which parent is to be found
     * 
     * @return Parent group of this group
     * 
     * @throws IllegalArgumentException
     */
    public String getParent(String child) {
        if (child == null) {
            throw new IllegalArgumentException("Child is null");
        }

        Iterator it = nodes.keySet().iterator();

        while (it.hasNext()) {
            String parent = (String) it.next();
            LinkedList children = (LinkedList) nodes.get(parent);

            if (children.contains(child)) {
                return parent;
            }
        }

        return null;
    }

    /**
     * Checks whether the group is available
     * 
     * @param group
     *            to be searched
     * 
     * @return Checks whether the group is available
     * 
     * @throws IllegalArgumentException
     */
    public boolean containsGroup(String group) {
        if (group == null) {
            throw new IllegalArgumentException("group is null");
        }

        return nodes.containsKey(group);
    }

    /**
     * Gets all the child groups for the given group
     * 
     * @param group
     *            to be searched
     * 
     * @return A list of all children of the given groups
     * 
     * @throws IllegalArgumentException
     */
    public LinkedList getChildrenForGroup(String group) {
        if (group == null) {
            throw new IllegalArgumentException("group is null");
        }

        LinkedList children = (LinkedList) nodes.get(group);

        return children;
    }

    /**
     * Returns the tree structure for the group
     * 
     * @return Tree structure
     */
    public TreeMap getGroupTree() {
        return nodes;
    }

    /**
     * Pretty print
     * 
     * @return
     */
    public String toString() {
        return nodes.toString();
    }
}
