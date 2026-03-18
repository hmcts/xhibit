package uk.gov.courtservice.xhibit.client.listings.list.daily;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.netbeans.swing.outline.Outline;

import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractDailyFirmMainListingPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;

/**
 * Base class for daily list Main Listing Panel.
 * 
 * @author groenm
 * @amend groenm - added the list date a room is associated with.
 *
 */
public class DailyMainListingPanel extends AbstractDailyFirmMainListingPanel<DailyListModel> {

	private static final long serialVersionUID = 1L;

	public DailyMainListingPanel(XDialog parent, DailyListModel listModel, Outline listOutline, DefaultTreeModel listTreeModel) {
		super(parent, listModel, listOutline, listTreeModel);
	}

	@Override
	protected void buildCourtListingNodes(DefaultMutableTreeNode rootNode, boolean expandFirstNode) {
        // Add court sites and all its child nodes including saved sittings and cases
        buildCourtSiteNodes(rootNode, listModel.getListStartDate());

        // Must now reload the model because the nodes have been directly added
        // to the root node rather than being added by using the model interface
        courtListingTreeModel.reload();
		
		// Expand the first court site
    	if (expandFirstNode) {
    		expandFirstChildCourtSite(rootNode);
    	}
    }
}
