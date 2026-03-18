package uk.gov.courtservice.framework.security.activedirectory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * <p>
 * Title: XHIBIT group hierarchy
 * </p>
 * 
 * <p>
 * Description: The class represents the Active Directory group hierarchy as a
 * graph structure.
 * </p>
 * 
 * @author Will Fardell, Xdevelopment LLP.
 */
public class ActiveDirectoryGroupHierarchy {
    /** The child security group sets indexed by parent group */
    private final TreeMap groupMap = new TreeMap();

    /**
     * Get the set of root groups
     * 
     * @param root
     */
    public ActiveDirectoryGroup[] getRoots() {
        Set rootSet = new HashSet();

        // Check each parent (key) it is a root if it doesnt have its own
        // parent!

        Iterator roots = groupMap.keySet().iterator();

        forEachRoot: while (roots.hasNext()) {
            ActiveDirectoryGroup root = (ActiveDirectoryGroup) roots.next();

            Iterator parents = groupMap.keySet().iterator();
            while (parents.hasNext()) {
                Iterator children = ((Set) groupMap.get(parents.next())).iterator();
                while (children.hasNext()) {
                    if (root.equals(children.next())) {
                        continue forEachRoot;
                    }
                }
            }

            rootSet.add(root);
        }

        return toArray(rootSet);
    }

    /**
     * This method adds a new node (group) to the hierarchy
     * 
     * @param parent
     *            the parent group for the new node.
     * @param children
     *            the new node to add to the hierarchy.
     * 
     * @throws IllegalArgumentException
     *             if children null or parent null or parent already been added
     *             or can not be linked back to the root.
     */
    public void addRelationship(ActiveDirectoryGroup parent, ActiveDirectoryGroup child) {
        if (child == null) {
            throw new IllegalArgumentException("child: null");
        }
        if (parent == null) {
            throw new IllegalArgumentException("parent: " + parent);
        }

        Set childSet = (Set) groupMap.get(parent);
        if (childSet == null) {
            childSet = new HashSet();
            groupMap.put(parent, childSet);
        }
        childSet.add(child);
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
    public ActiveDirectoryGroup[] getParents(ActiveDirectoryGroup child) {
        if (child == null) {
            throw new IllegalArgumentException("child: null");
        }

        Set parentSet = new HashSet();

        Iterator parents = groupMap.keySet().iterator();
        while (parents.hasNext()) {
            ActiveDirectoryGroup parent = (ActiveDirectoryGroup) parents.next();
            Iterator children = ((Set) groupMap.get(parent)).iterator();
            while (children.hasNext()) {
                if (child.equals(children.next())) {
                    parentSet.add(parent);
                }
            }
        }

        return toArray(parentSet);
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
    public ActiveDirectoryGroup[] getChildren(ActiveDirectoryGroup parent) {
        if (parent == null) {
            throw new IllegalArgumentException("parent: null");
        }

        return toArray((Set) groupMap.get(parent));
    }

    /**
     * Get a string for debug
     * 
     * @return a string containing debug info
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("ActiveDirectoryGroupHierarchy[");
        appendGroups(buffer, getRoots(), 4);
        buffer.append("\n]");
        return buffer.toString();
    }

    private void appendGroups(StringBuffer buffer, ActiveDirectoryGroup[] groups, int indent) {
        for (int i = 0; i < groups.length; i++) {
            buffer.append("\n");
            for (int j = 0; j < indent; j++) {
                buffer.append(' ');
            }
            buffer.append(groups[i].getPrincipalName());
            appendGroups(buffer, getChildren(groups[i]), indent + 4);
        }
    }

    //
    // Utils
    //    

    private static ActiveDirectoryGroup[] toArray(Set set) {
        if (set == null || set.size() == 0) {
            return new ActiveDirectoryGroup[0];
        }
        List list = new ArrayList();
        list.addAll(set);
        Collections.sort(list);
        return (ActiveDirectoryGroup[]) list.toArray(new ActiveDirectoryGroup[list.size()]);
    }

}
