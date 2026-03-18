package uk.gov.courtservice.xhibit.client.listings.list.outline;

import javax.swing.tree.DefaultMutableTreeNode;

import org.netbeans.swing.outline.Outline;

/**
 * Utility methods for the outline control.
 * 
 * @author uphillj
 *
 */
public class OutlineUtils {

	/**
	 * Get the tree node from the row.
	 * 
	 * @param outline
	 * @param row
	 * @return tree node
	 */
	public static DefaultMutableTreeNode getTreeNode(Outline outline, int row) {
		return (DefaultMutableTreeNode)outline.getValueAt(row, 0);
	}

	/**
	 * Returns true if the tree node has a controller set as its user object.
	 * 
	 * @param outline
	 * @param row
	 * @return true if node has a controller
	 */
	public static boolean hasTreeNodeController(Outline outline, int row) {
		return hasTreeNodeController(getTreeNode(outline, row));
	}

	/**
	 * Returns true if the tree node has a controller set as its user object.
	 * 
	 * @param tree node
	 * @return true if node has a controller
	 */
	public static boolean hasTreeNodeController(Object treeNode) {
		return hasTreeNodeController((DefaultMutableTreeNode)treeNode);
	}

	/**
	 * Returns true if the tree node has a controller set as its user object.
	 * 
	 * @param tree node
	 * @return true if node has a controller
	 */
	public static boolean hasTreeNodeController(DefaultMutableTreeNode treeNode) {
		return (treeNode != null && treeNode.getUserObject() != null && treeNode.getUserObject() instanceof TreeNodeController);
	}
	
	/**
	 * Get the controller from the user object on the tree node.
	 * 
	 * @param outline
	 * @param row
	 * @return controller
	 */
	public static TreeNodeController getTreeNodeController(Outline outline, int row) {
		return getTreeNodeController(getTreeNode(outline, row));
	}
	
	/**
	 * Get the controller from the user object on the tree node.
	 * 
	 * @param treeNode
	 * @return controller
	 */
	public static TreeNodeController getTreeNodeController(Object treeNode) {
		return getTreeNodeController((DefaultMutableTreeNode)treeNode);
	}

	/**
	 * Get the controller from the user object on the tree node.
	 * 
	 * @param treeNode
	 * @return controller
	 */
	public static TreeNodeController getTreeNodeController(DefaultMutableTreeNode treeNode) {
		return (TreeNodeController)treeNode.getUserObject();
	}
}
