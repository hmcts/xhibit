package uk.gov.courtservice.xhibit.client.listings.list.warned;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.netbeans.swing.outline.Outline;

import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.court.CourtStructureValue;
import uk.gov.courtservice.xhibit.client.listings.CaseOnListType;
import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractMainListingPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * Base class for warned list Main Listing Panel.
 * 
 * @author uphillj
 *
 */
public class WarnedMainListingPanel extends AbstractMainListingPanel<WarnedListModel> {

	private static final long serialVersionUID = 1L;

	public WarnedMainListingPanel(XDialog parent, WarnedListModel listModel, Outline listOutline, DefaultTreeModel listTreeModel) {
		super(parent, listModel, listOutline, listTreeModel);

		// Hide the time marking and court room list columns in the outline
		courtListingOutline.getColumnModel().removeColumn(courtListingOutline.getColumnModel().getColumn(COURT_ROOM_LIST_COLUMN));
		courtListingOutline.getColumnModel().removeColumn(courtListingOutline.getColumnModel().getColumn(TIME_MARKING_COLUMN));
	}

	@Override
	protected void buildCourtListingNodes(DefaultMutableTreeNode rootNode, boolean expandFirstNode) {
    	// Get court sites for court
        CourtStructureValue courtStructure = XhibitSingleton.getInstance().getCourtStructureValue();
        Sorter.sort(courtStructure.getCourtSites(), new String[] { "courtSiteCode" }, Sorter.ASCENDING);
        
        // Add court sites
        for (int i = 0; i < courtStructure.getCourtSites().length; i++) {
        	// Create court site node
            XhbCourtSiteBasicValue courtSite = courtStructure.getCourtSites()[i];
            DefaultMutableTreeNode courtSiteNode = listModel.getTreeNodeFactory().createCourtSite(courtSite);
            rootNode.add(courtSiteNode);

            // Add no date node
            DefaultMutableTreeNode noDateNode = listModel.getTreeNodeFactory().createCaseParent(CaseOnListType.NoDate, courtSite);
            courtSiteNode.add(noDateNode);

            // Add no date cases
            List<CaseOnListComplexValue> casesOnList = listModel.getCasesOnListForNoDate(courtSite.getCourtSiteId());
            for (CaseOnListComplexValue caseOnList : casesOnList) {
            	// Create case node
            	DefaultMutableTreeNode caseNode = listModel.getTreeNodeFactory().createCase(caseOnList);
            	noDateNode.add(caseNode);
            }
        }
        
        // Must now reload the model because the nodes have been directly added
        // to the root node rather than being added by using the model interface
        courtListingTreeModel.reload();
		
		// Expand the first court site
    	if (expandFirstNode) {
    		expandFirstChildCourtSite(rootNode);
    	}
	}
}
