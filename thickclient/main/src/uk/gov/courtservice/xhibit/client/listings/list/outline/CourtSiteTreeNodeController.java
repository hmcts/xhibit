package uk.gov.courtservice.xhibit.client.listings.list.outline;

import javax.swing.tree.DefaultTreeModel;

import org.netbeans.swing.outline.Outline;

import uk.gov.courtservice.xhibit.client.listings.list.common.ListModel;
import uk.gov.courtservice.xhibit.client.util.XDialog;

/**
 * Controller for tree nodes that display court sites.
 * 
 * @author uphillj
 *
 */
public class CourtSiteTreeNodeController extends AbstractTreeNodeController<CourtSiteTreeNodeModel> {

	private static final long serialVersionUID = 1L;

	public CourtSiteTreeNodeController(TreeNodeFactory treeNodeFactory, XDialog listDialog, ListModel listModel, Outline listOutline, DefaultTreeModel listTreeModel) {
		super(treeNodeFactory, CourtSiteTreeNodeModel.class, listDialog, listModel, listOutline, listTreeModel);
	}

	@Override
	public String getControllerId() {
		if (this.getModel().getCourtSite() != null && 
				this.getModel().getCourtSite().getCourtSiteId() != null) {
			return getParentControllerId() + this.getModel().getCourtSite().getCourtSiteId().toString();
		}
		return EMPTY_STRING;
	}
}
