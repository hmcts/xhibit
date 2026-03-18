package uk.gov.courtservice.xhibit.client.util.tree;

import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

/**
 * Provides abstract implementation of the tree model to facilitate the
 * development of alternative tree models.
 * 
 * @author William Fardell
 * @version $Revision: 1.3 $
 */
public abstract class AbstractXTreeModel implements XTreeModel {
    /**
     * Listeners.
     */
    private EventListenerList listenerList = new EventListenerList();

    /**
     * TreeModel implementation.
     * 
     * @see javax.swing.tree.TreeModel#addTreeModelListener(TreeModelListener)
     *      TreeModel
     */
    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
    }

    /**
     * TreeModel implementation.
     * 
     * @see javax.swing.tree.TreeModel#removeTreeModelListener(TreeModelListener)
     *      TreeModel
     */
    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
    }

    /**
     * XTreeModel implementation
     * 
     * @see uk.co.xdevelopment.swing.tree.XTreeModel#getTreePath(Object)
     *      XTreeModel
     */
    public TreePath getTreePath(Object node) {
        return new TreePath(getPathToRoot(node));
    }

    /**
     * XTreeModel implementation
     * 
     * @see uk.co.xdevelopment.swing.tree.XTreeModel#getTreePath(Object)
     *      XTreeModel
     */
    public Iterator popupMenuItems(Object node) {
        return null;
    }

    /**
     * XTreeModel implementation
     * 
     * @see uk.co.xdevelopment.swing.tree.XTreeModel#getTreePath(Object)
     *      XTreeModel
     */
    public JMenuItem getPopupMenuDefault(Object node) {
        return null;
    }

    /**
     * XTreeModel implementation
     * 
     * @see uk.co.xdevelopment.swing.tree.XTreeModel#getOpenIcon(Object)
     *      XTreeModel
     */
    public Icon getOpenIcon(Object node) {
        return null;
    }

    /**
     * XTreeModel implementation
     * 
     * @see uk.co.xdevelopment.swing.tree.XTreeModel#getClosedIcon(Object)
     *      XTreeModel
     */
    public Icon getClosedIcon(Object node) {
        return null;

    }

    /**
     * XTreeModel implementation
     * 
     * @see uk.co.xdevelopment.swing.tree.XTreeModel#getLeafIcon(Object)
     *      XTreeModel
     */
    public Icon getLeafIcon(Object node) {
        return null;
    }

    /**
     * XTreeModel implementation
     * 
     * @see uk.co.xdevelopment.swing.tree.XTreeModel#getTooltipText(Object)
     *      XTreeModel
     */
    public String getTooltipText(Object node) {
        return null;
    }

    /**
     * TreeModel implementation.
     * 
     * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath,
     *      Object) TreeModel
     */
    public void valueForPathChanged(TreePath path, Object newValue) {
        valueChanged(path.getLastPathComponent(), newValue);
    }

    /**
     * Invoke this method after you've inserted a child into the node. (This
     * method is optomised.)
     * 
     * @param parent
     *            the node which has had a child inserted
     * @param child
     *            the child which has been inserted
     */
    public void nodeInserted(Object parent, Object child) {
        if (parent != null && child != null) {
            fireTreeNodesInserted(this, getPathToRoot(parent), new int[] { getIndexOfChild(parent, child) },
                    new Object[] { child });
        }
    }

    /**
     * Invoke this method after you've inserted a child into the node. (This
     * method is optomised.)
     * 
     * @param parent
     *            the node which has had a child inserted
     * @param index
     *            the index of the child which has been inserted
     */
    public void nodeInserted(Object parent, int index) {
        if (parent != null && index >= 0 && index < getChildCount(parent)) {
            fireTreeNodesInserted(this, getPathToRoot(parent), new int[] { index }, new Object[] { getChild(parent,
                    index) });
        }
    }

    /**
     * Invoke this method after you've inserted children into the node.
     * 
     * @param parent
     *            the node which has had the children inserted
     * @param children
     *            the children which have been inserted
     */
    public void nodesInserted(Object parent, Object[] children) {
        if (parent != null && children != null && children.length > 0) {
            fireTreeNodesInserted(this, getPathToRoot(parent), getIndiciesOfChildren(parent, children), children);
        }
    }

    /**
     * Invoke this method after you've inserted children into the node.
     * 
     * @param parent
     *            the node which has had the children inserted
     * @param indicies
     *            the indicies of the children which have been inserted
     */
    public void nodesInserted(Object parent, int[] indicies) {
        if (parent != null && indicies != null && indicies.length > 0) {
            fireTreeNodesInserted(this, getPathToRoot(parent), indicies, getChildren(parent, indicies));
        }
    }

    /**
     * Invoke this method after you've removed the child from the node. Note:
     * cant work out indicies as removed!
     * 
     * @param node
     *            the node which has had its child removed
     * @param index
     *            the index of the child which have been removed
     * @param child
     *            the child which has been removed
     */
    public void nodeRemoved(Object parent, int index, Object child) {
        if (parent != null && index >= 0 && index <= getChildCount(parent)) { // <=
            // getChildCount
            // as
            // node
            // has
            // been
            // removed!
            fireTreeNodesRemoved(this, getPathToRoot(parent), new int[] { index }, new Object[] { child });
        }
    }

    /**
     * Invoke this method after you've removed some children from the node.
     * Note: cant work out indicies as removed!
     * 
     * @param node
     *            the node which has had its representation changed
     * @param indicies
     *            the indicies of the children which have been changed
     * @param children
     *            the children which have been removed
     */
    public void nodesRemoved(Object parent, int[] indicies, Object[] children) {
        if (parent != null && indicies != null && indicies.length > 0) {
            fireTreeNodesRemoved(this, getPathToRoot(parent), indicies, children);
        }
    }

    /**
     * Invoke this method after you've changed how the child is to be
     * represented in the tree. (This method is optomised.)
     * 
     * @param parent
     *            the node which has had its child changed
     * @param child
     *            the child which have been changed
     */
    public void nodeChanged(Object parent, Object child) {
        if (parent != null) {
            if (child == null) {
                fireTreeNodesChanged(this, getPathToRoot(parent), new int[] { getIndexOfChild(parent, child) },
                        new Object[] { child });
            } else {
                fireTreeNodesChanged(this, getPathToRoot(parent), null, null);
            }
        }
    }

    /**
     * Invoke this method after you've changed how the child identified by index
     * is to be represented in the tree. (This method is optomised.)
     * 
     * @param parent
     *            the node which has had its child changed
     * @param index
     *            the index of the child which have been changed
     */
    public void nodeChanged(Object parent, int index) {
        if (parent != null) {
            if (index > 0 && index < getChildCount(parent)) {
                fireTreeNodesChanged(this, getPathToRoot(parent), new int[] { index }, new Object[] { getChild(parent,
                        index) });
            } else {
                fireTreeNodesChanged(this, getPathToRoot(parent), null, null);
            }
        }
    }

    /**
     * Invoke this method after you've changed how the children are to be
     * represented in the tree.
     * 
     * @param parent
     *            the node which has had its child repsentations changed
     * @param children
     *            the children which have been changed
     */
    public void nodesChanged(Object parent, Object[] children) {
        if (parent != null) {
            if (children != null && children.length > 0) {
                fireTreeNodesChanged(this, getPathToRoot(parent), getIndiciesOfChildren(parent, children), children);
            } else {
                fireTreeNodesChanged(this, getPathToRoot(parent), null, null);
            }
        }
    }

    /**
     * Invoke this method after you've changed how the children identified by
     * childIndicies are to be represented in the tree.
     * 
     * @param node
     *            the node which has had its representation changed
     * @param indicies
     *            the indicies of the children which have been changed
     */
    public void nodesChanged(Object parent, int[] indices) {
        if (parent != null) {
            if (indices != null && indices.length > 0) {
                fireTreeNodesChanged(this, getPathToRoot(parent), indices, getChildren(parent, indices));
            } else {
                fireTreeNodesChanged(this, getPathToRoot(parent), null, null);
            }
        }
    }

    /**
     * Invoke this method after you've changed how node is to be represented in
     * the tree.
     * 
     * @param node
     *            the node which has had its representation changed
     */
    public void nodeChanged(Object node) {
        if (node != null) {
            Object parent = getParent(node);
            if (parent != null) {
                fireTreeNodesChanged(this, getPathToRoot(parent), new int[] { getIndexOfChild(parent, node) },
                        new Object[] { node });
            } else {
                fireTreeNodesChanged(this, new Object[] { node }, null, null);
            }
        }
    }

    /**
     * Call this method to indicate a nodes children (and their children) hava
     * completely changed.
     * 
     * @param node
     *            the node which has had its structure changed
     */
    public void nodeStructureChanged(Object node) {
        if (node != null) {
            fireTreeStructureChanged(this, getPathToRoot(node), null, null);
        }
    }

    /**
     * Get an <code>Object</code> array of the specified child nodes.
     * 
     * @param parent
     *            the node to get the children for
     * @param indicies
     *            the indicies of the children to retrieve
     * @return an <code>Object</code> array containing the specified children
     */
    public Object[] getChildren(Object parent, int[] indicies) {
        Object[] children = new Object[indicies.length];
        for (int i = 0; i < indicies.length; i++) {
            children[i] = getChild(parent, indicies[i]);
        }
        return children;
    }

    /**
     * Get an <code>int</code> array of the indicies of the specified child
     * nodes.
     * 
     * @param parent
     *            the node to get the children for
     * @param children
     *            the children to get the indicies of
     * @return an <code>int</code> array containing the specified indicies
     */
    public int[] getIndiciesOfChildren(Object parent, Object[] children) {
        int[] indicies = new int[children.length];
        for (int i = 0; i < children.length; i++) {
            indicies[i] = getIndexOfChild(parent, children[i]);
        }
        return indicies;
    }

    /**
     * Builds the parents of node up to and including the root node, where the
     * original node is the last element in the returned array. The length of
     * the returned array gives the node's depth in the tree.
     * 
     * @param node
     *            the node <code>Object</code> to get the path for
     * @return an <code>Object</code> array giving the path from the root to
     *         the specified node
     */
    public Object[] getPathToRoot(Object node) {
        return getPathToRoot(node, 0);
    }

    /**
     * Builds the parents of node up to and including the root node, where the
     * original node is the last element in the returned array. The length of
     * the returned array gives the node's depth in the tree.
     * 
     * @param node
     *            the node <code>Object</code> to get the path for
     * @param depth
     *            an <code>int</code> giving the number of steps already taken
     *            towards the root (on recursive calls), used to size the
     *            returned array
     * @return an <code>Object</code> array giving the path from the root to
     *         the specified node
     */
    private Object[] getPathToRoot(Object node, int depth) {
        if (node == null) {
            return new Object[depth];
        } else {
            Object[] path = getPathToRoot(getParent(node), ++depth);
            path[path.length - depth] = node;
            return path;
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     * 
     * @param source
     *            the node where elements are being removed
     * @param path
     *            the path to the root node
     * @param childIndicies
     *            the indices of the removed elements
     * @param children
     *            the elements inserted
     * @see EventListenerList
     */
    protected void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                if (e == null)
                    e = new TreeModelEvent(source, path, childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     * 
     * @param source
     *            the node where elements are being removed
     * @param path
     *            the path to the root node
     * @param childIndicies
     *            the indices of the removed elements
     * @param children
     *            the elements inserted
     * @see EventListenerList
     */
    protected void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices, Object[] children) {
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                if (e == null)
                    e = new TreeModelEvent(source, path, childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     * 
     * @param source
     *            the node where elements are being removed
     * @param path
     *            the path to the root node
     * @param childIndicies
     *            the indices of the removed elements
     * @param children
     *            the elements removed
     * @see EventListenerList
     */
    protected void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices, Object[] children) {
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                if (e == null)
                    e = new TreeModelEvent(source, path, childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     * 
     * @param source
     *            the node where elements are being removed
     * @param path
     *            the path to the root node
     * @param childIndicies
     *            the indices of the removed elements
     * @param children
     *            the affected elements
     * @see EventListenerList
     */
    protected void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                if (e == null)
                    e = new TreeModelEvent(source, path, childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }
}
