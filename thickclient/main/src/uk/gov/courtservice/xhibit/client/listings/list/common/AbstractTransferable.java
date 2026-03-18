package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;

import javax.activation.ActivationDataFlavor;
import javax.swing.tree.DefaultMutableTreeNode;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeFactory;
import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeModel;

/**
 * Base class for all transferable classes that move data to and within the outline.
 * 
 * @author uphillj
 *
 */
public abstract class AbstractTransferable implements Transferable {

	public static final DataFlavor sourceFlavor = new ActivationDataFlavor(Object.class, DataFlavor.javaJVMLocalObjectMimeType, "Source");

	public static final DataFlavor targetFlavor = new ActivationDataFlavor(DefaultMutableTreeNode.class, DataFlavor.javaJVMLocalObjectMimeType, "Target");
	
	private final DataFlavor[] supportedFlavors;

	private TreeNodeFactory treeNodeFactory;

	public <T extends TreeNodeModel> AbstractTransferable(Class<T> modelType, TreeNodeFactory treeNodeFactory) {
		DataFlavor modelFlavor = new ActivationDataFlavor(modelType, DataFlavor.javaJVMLocalObjectMimeType, "Model");
		this.supportedFlavors = new DataFlavor[] { modelFlavor, targetFlavor, sourceFlavor };
		this.treeNodeFactory = treeNodeFactory;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return supportedFlavors;
	}

	public boolean isDataFlavorSupported(final DataFlavor flavor) {
		return Arrays.asList(supportedFlavors).contains(flavor);
	}

	protected TreeNodeFactory getTreeNodeFactory() {
		return treeNodeFactory;
	}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if (targetFlavor.equals(flavor)) {
			try {
				return getTransferTarget();
			} catch (CSRecoverableException ex) {
				throw new IOException(ex);
			}
		} else if (sourceFlavor.equals(flavor)) {
			return getTransferSource();
		} else {
			throw new UnsupportedFlavorException(flavor);
		}
	}
	
	/**
	 * Sub-classes override this method to return the source object
	 * being transferred.
	 * 
	 * @return source object
	 */
	abstract protected Object getTransferSource();
	
	/**
	 * Sub-classes override this method to return a new correctly
	 * configured tree node for the data that is being transferred.
	 * 
	 * @return new tree node
	 * @throws CSRecoverableException
	 */
	abstract protected DefaultMutableTreeNode getTransferTarget() throws CSRecoverableException;
}
