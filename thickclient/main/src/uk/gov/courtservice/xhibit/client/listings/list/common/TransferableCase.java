package uk.gov.courtservice.xhibit.client.listings.list.common;

import uk.gov.courtservice.xhibit.client.listings.list.outline.CaseTreeNodeModel;
import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeFactory;

/**
 * Base class used to transfer cases from component to tree and within the tree.
 * 
 * @author groenm
 *
 */
public abstract class TransferableCase extends AbstractTransferable {

	public TransferableCase(TreeNodeFactory treeNodeFactory) {
		super(CaseTreeNodeModel.class, treeNodeFactory);
	}
}