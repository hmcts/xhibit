package uk.gov.courtservice.xhibit.client.listings.list.outline;

import java.awt.datatransfer.Transferable;

import javax.swing.JPopupMenu;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Interface for all tree node controllers used to perform tree node specific processing.
 * 
 * @author uphillj
 * @amend groenm - added dropChild and getPopupMenu()
 *
 */
public interface TreeNodeController<T extends TreeNodeModel> {

	/**
	 * Get the tree node factory used to create this controller.
	 * 
	 * @return tree node factory
	 */
	TreeNodeFactory getTreeNodeFactory();

	/**
	 * Get the class of the model that this controller manages.
	 * 
	 * @return model class
	 */
	Class<T> getModelType ();

	/**
	 * Get the tree node.
	 * 
	 * @return tree node
	 */
	DefaultMutableTreeNode getTreeNode();
	
	/**
	 * Set the parent node.
	 * 
	 * @param treeNode
	 */
	void setTreeNode(DefaultMutableTreeNode treeNode);
	
	/**
	 * Get the model.
	 * 
	 * @return
	 */
	T getModel();
	
	/**
	 * Set the model.
	 * 
	 * @param model
	 */
	void setModel(T model);
	
	/**
	 * Get the popup menu for node.
	 * 
	 * @return
	 */
	JPopupMenu getPopupMenu();
	
	/**
	 * Return true if this tree node supports importing
	 * the supplied transfer data as a child node.
	 * 
	 * @param support
	 * @return true if data can be added as child
	 */
	boolean isDropSupported(TransferSupport support);
	
	/**
	 * Return true if this tree node supports being dragged.
	 * 
	 * @return true if this node supports being dragged
	 */
	boolean isDragSupported();

	/**
	 * Returns this tree node as a transferable object if
	 * it supports being dragged.
	 * 
	 * @return transferable
	 */
	Transferable createTransferable();
	
	/**
	 * Child object has been dropped at specified index which
	 * must be added/moved in the outline control.
	 * 
	 * @param childNode
	 * @param index
	 * @return true if child data imported into list model
	 */
	boolean importChild(DefaultMutableTreeNode childNode, int index);
	
}
