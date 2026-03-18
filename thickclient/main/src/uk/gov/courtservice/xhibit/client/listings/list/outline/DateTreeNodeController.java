package uk.gov.courtservice.xhibit.client.listings.list.outline;

import javax.swing.tree.DefaultTreeModel;

import org.netbeans.swing.outline.Outline;

import uk.gov.courtservice.xhibit.client.listings.list.common.ListModel;
import uk.gov.courtservice.xhibit.client.util.XDialog;

/**
 * Controller for tree nodes that display dates.
 * 
 * @author uphillj
 *
 */
public class DateTreeNodeController extends AbstractTreeNodeController<DateTreeNodeModel> {

	private static final long serialVersionUID = 1L;

	public DateTreeNodeController(TreeNodeFactory treeNodeFactory, XDialog listDialog, ListModel listModel, Outline listOutline, DefaultTreeModel listTreeModel) {
		super(treeNodeFactory, DateTreeNodeModel.class, listDialog, listModel, listOutline, listTreeModel);
	}

	@Override
	public String getControllerId() {
		return this.getModel().getListDate().toString();
	}
}
