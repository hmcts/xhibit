package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.netbeans.swing.outline.Outline;

import uk.gov.courtservice.xhibit.client.listings.list.outline.OutlineUtils;
import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeController;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * Transfer handler for processing drag and drop on the outline control.
 * 
 * @author uphillj
 * @amend groenm - amended importData() to call the relevant controller 
 * to update the listModel.
 */
public class CourtListingTransferHandler extends TransferHandler {

	private static final long serialVersionUID = 1L;
	private Outline courtListingOutline;
	private DefaultTreeModel courtListingTreeModel;
	
	public CourtListingTransferHandler(Outline courtListingOutline, DefaultTreeModel courtListingTreeModel) {
		this.courtListingOutline = courtListingOutline;
		this.courtListingTreeModel = courtListingTreeModel;
	}

	/**
	 * This method is called as part of the drag and drop framework, before importData. 
	 */
	@Override
	public boolean canImport(final TransferSupport support) {
		final JTable.DropLocation dropLocation = (JTable.DropLocation) support.getDropLocation();	
		final CourtListingDropLocation location = getParentNode(support, dropLocation.getRow());
		return (location.getParentNode() != null);
	}
	
	@Override
	public int getSourceActions(JComponent c){
		return TransferHandler.MOVE;
	}

	@Override
	public boolean importData(final TransferSupport support) {
		// Oracle documentation recommends calling canImport here
		// because canImport is not called in response to paste operations.
		if (!canImport(support)) {
			return false;
		}
		
		// Find the new parent node of the object dropped on the outline control
		final JTable.DropLocation dropLocation = (JTable.DropLocation) support.getDropLocation();
		final CourtListingDropLocation location = getParentNode(support, dropLocation.getRow());
		DefaultMutableTreeNode newParentNode = location.getParentNode();
		
		// Get the new child node for the object being dropped on the outline control
		DefaultMutableTreeNode newNode;
		try {
			final Transferable transferable = support.getTransferable();
			newNode = (DefaultMutableTreeNode)transferable.getTransferData(AbstractTransferable.targetFlavor);
		} catch (UnsupportedFlavorException e) {
			XHIBITConstant.handleError(e);
			return false;
		} catch (IOException e) {
			XHIBITConstant.handleError(e);
			return false;
		}

		// Tell the controller of the drop as they are responsible for updating the list model
		// with the drop. For a sitting this controller will be a court room controller, for an
		// add or move of a case this can be multiple different controller types
		boolean imported = false;
		try {
			final TreeNodeController controller = OutlineUtils.getTreeNodeController(newParentNode);
			imported = controller.importChild(newNode, location.getIndex());
		} catch (RuntimeException e) {
			XHIBITConstant.handleError(e);
			return false;
		}
		
		if(imported){
			// Finally add the new child node to the outline control
			insertNode(newNode, location);
		}
		// Return true if drop successful
		return imported;
	}
	
	/**
	 * Returns a populated drop location object if there is a parent node available
	 * for the supplied row that supports the data flavour in the transfer support.
	 * If there is no valid parent, then the parent node will be null and index -1.
	 * 
	 * @param support
	 * @param row
	 * @return
	 */
	private CourtListingDropLocation getParentNode(final TransferSupport support, final int row) {
		CourtListingDropLocation location = new CourtListingDropLocation();
		int previousRow = row - 1;

		// Only able to drop under nodes that have controllers as user objects
		if (OutlineUtils.hasTreeNodeController(courtListingOutline, previousRow)) {
			// Get node and controller for the previous row
			DefaultMutableTreeNode previousNode = OutlineUtils.getTreeNode(courtListingOutline, previousRow);
			TreeNodeController previousController = OutlineUtils.getTreeNodeController(previousNode);
			
			// If the previous node supports children of the required data flavour,
			// this is the required parent node but must check if expanded for index
			if (previousController.isDropSupported(support)) {
				// Previous node to drop location is the new parent node
				location.setParentNode(previousNode);
				// If parent is expanded, then user wants node to be the first child
				if (courtListingOutline.isExpanded(new TreePath(previousNode.getPath()))) {
					location.setIndex(0);
				}
				// Else parent is not expanded, so the node needs to be added last
				else {
					location.setIndex(previousNode.getChildCount());
				}
			}
			// Else if the previous node has no children or is not expanded,
			// it is possible that the parent node is further up the tree
			else if (previousNode.isLeaf() || !courtListingOutline.isExpanded(new TreePath(previousNode.getPath()))) {
				location = getParentNode(support, previousNode);
			}
		}

		return location;
	}

	/**
	 * Method used to recursively check if an ancestor node is the parent
	 * node that the transfer support is being dropped onto.
	 * 
	 * @param support
	 * @param node
	 * @return
	 */
	CourtListingDropLocation getParentNode(final TransferSupport support, final DefaultMutableTreeNode node) {
		final CourtListingDropLocation location = new CourtListingDropLocation();
		
		// Get node and controller for the parent node of the supplied node
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();

		// Only able to drop under nodes that have controllers as user objects
		if (OutlineUtils.hasTreeNodeController(parentNode)) {
			// Get controller for the parent node
			TreeNodeController parentController = OutlineUtils.getTreeNodeController(parentNode);
	
			// If the parent node supports children of the required data flavour,
			// this is the required parent node and the new node will be added
			// after the child node supplied to this method
			if (parentController.isDropSupported(support)) {
				location.setParentNode(parentNode);
				location.setIndex(parentNode.getIndex(node) + 1);
			}
			// Else if the supplied node is the last child node of its parent node,
			// it is possible that the parent node is an ancestor of the parent node
			else if (parentNode.getChildCount() == (parentNode.getIndex(node) + 1)) {
				return getParentNode(support, parentNode);
			}
		}

		return location;
	}
	
	/**
	 * Insert the node into tree model in the specified location.
	 * 
	 * @param node
	 * @param location
	 */
	private void insertNode(final DefaultMutableTreeNode node, final CourtListingDropLocation location) {
		// Insert the new node into the tree model in the required location
		courtListingTreeModel.insertNodeInto(node, location.getParentNode(), location.getIndex());
		
		// Ensure the tree is expanded so the user can view the dropped node
		if (location.getParentNode().getChildCount() > 0) {
			DefaultMutableTreeNode firstChild = (DefaultMutableTreeNode)location.getParentNode().getChildAt(location.getIndex());
			courtListingOutline.expandPath(new TreePath(firstChild.getPath()));
		}
	}

	/**
	 * Create transferable for drag and drop within outline control.
	 */
    @Override
    public Transferable createTransferable(JComponent source){
    	Transferable transferable = null;
    	Outline outline = (Outline)source;
    	
    	// Can only drag nodes that have controllers that say they can be dragged
		if (OutlineUtils.hasTreeNodeController(outline, outline.getSelectedRow())) {
			TreeNodeController controller = OutlineUtils.getTreeNodeController(outline, outline.getSelectedRow());
			if (controller.isDragSupported()) {
				transferable = controller.createTransferable();
			}
		}
		
		return transferable;       	  
    }

    /**
     * Drag and drop within outline control has now completed.
     */
	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {
		if (action == TransferHandler.MOVE) {
			try {
				// Get the original node of the data being transferred and delete from tree
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)data.getTransferData(AbstractTransferable.sourceFlavor);
				courtListingTreeModel.removeNodeFromParent(treeNode);
			} catch (IOException e) {
				throw new IllegalArgumentException("Unsupported data flavor", e);
			} catch (UnsupportedFlavorException e) {
				throw new IllegalArgumentException("Unsupported data flavor", e);
			}
		}
	}
}
