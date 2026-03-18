package uk.gov.courtservice.xhibit.client.listings.list.common;

import javax.swing.tree.DefaultMutableTreeNode;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.listings.list.outline.CaseTreeNodeController;
import uk.gov.courtservice.xhibit.client.listings.list.outline.CaseTreeNodeModel;
import uk.gov.courtservice.xhibit.client.listings.list.outline.OutlineUtils;
import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeController;
import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeFactory;

/**
 * Class used to move cases within the tree.
 * 
 * @author uphillj
 *
 */
public class CourtListingTransferableCase extends TransferableCase {
	
	private DefaultMutableTreeNode caseNode;
	
	public CourtListingTransferableCase(TreeNodeFactory treeNodeFactory, DefaultMutableTreeNode caseNode) {
		super(treeNodeFactory);
		this.caseNode = caseNode;
	}

	@Override
	protected DefaultMutableTreeNode getTransferTarget() throws CSRecoverableException {
		TreeNodeController controller = OutlineUtils.getTreeNodeController(caseNode);
		CaseTreeNodeModel model = ((CaseTreeNodeController)controller).getModel();
		return getTreeNodeFactory().createCase(model.getCaseOnList());
	}

	@Override
	protected Object getTransferSource() {
		return caseNode;
	}

}
