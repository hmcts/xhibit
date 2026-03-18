package uk.gov.courtservice.xhibit.client.util.tree;

import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Extended Tree Model simplifies the implementation of an abstract tree model.
 * 
 * @author William Fardell
 * @version $Revision: 1.3 $
 */

public interface XTreeModel extends TreeModel {
    /**
     * Get the nodes parent. Simplifies the implementation of an abstract tree
     * model and is used heavilly by the change notification for finding the
     * path to the root.
     * 
     * @return the node's parent <code>Object</code> or <code>null</code> if
     *         the node is the root.
     */
    public Object getParent(Object node);

    /**
     * Get the tree path useful for later manipulation
     * 
     * @return the tree path
     */
    public TreePath getTreePath(Object node);

    /**
     * This method is called after an edit is made to the component the newValue
     * is the one returned by the TreeCellEditor.
     * 
     * @param node
     *            the node that has been changed
     * @param newValue
     *            the new value from the TreeCellEditor
     */
    public void valueChanged(Object node, Object newValue);

    /**
     * Get an iterator over the popupmenu items for the specified node
     * 
     * @return an iterator (empty if no items)
     */
    public Iterator popupMenuItems(Object node);

    /**
     * Get the default popup menu / action
     * 
     * @return the defualt item for the given node.
     */
    public JMenuItem getPopupMenuDefault(Object node);

    /**
     * Returns the icon used to represent non-leaf nodes that are expanded for
     * the specified node.
     * 
     * @param node
     *            the node to look up
     * @return the icon to use or null to use the default
     */
    public Icon getOpenIcon(Object node);

    /**
     * Returns the icon used to represent non-leaf nodes that are not expanded
     * for the specified node.
     * 
     * @param node
     *            the node to look up
     * @return the icon to use or null to use the default
     */
    public Icon getClosedIcon(Object node);

    /**
     * Returns the icon used to represent leaf nodes for the specified node.
     * 
     * @param node
     *            the node to look up
     * @return the icon to use or null to use the default
     */
    public Icon getLeafIcon(Object node);

    /**
     * Returns the tooltip to use for the specified node.
     * 
     * @param node
     *            the node to look up
     * @return the icon to use or null to use the default
     */
    public String getTooltipText(Object node);
}
