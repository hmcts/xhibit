package uk.gov.courtservice.xhibit.client.listings.list.firm;

import java.util.Calendar;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.netbeans.swing.outline.Outline;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.client.listings.CaseOnListType;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractDailyFirmMainListingPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;

/**
 * Base class for firm list Main Listing Panel.
 * 
 * @author uphillj
 *
 */
public class FirmMainListingPanel extends AbstractDailyFirmMainListingPanel<FirmListModel> {

	private static final long serialVersionUID = 1L;
	
	protected final String YES = "Y";

	public FirmMainListingPanel(XDialog parent, FirmListModel listModel, Outline listOutline, DefaultTreeModel listTreeModel) {
		super(parent, listModel, listOutline, listTreeModel);

		// Hide the court room list column in the outline
		courtListingOutline.getColumnModel().removeColumn(courtListingOutline.getColumnModel().getColumn(COURT_ROOM_LIST_COLUMN));
	}

	@Override
	protected void buildCourtListingNodes(DefaultMutableTreeNode rootNode, boolean expandFirstNode) {
        // Get first date of firm list
        Calendar listDate = listModel.getListStartDate();
        
        do {
        	// Only working days are added to the list
        	if (ListingDropdownPopulation.isWorkingDay(getWorkingDays(), listDate)) {
        		
	        	// Add list date
	            DefaultMutableTreeNode dateNode = listModel.getTreeNodeFactory().createDate(listDate);
	            rootNode.add(dateNode);
	        	
	            // Add court sites and all its child nodes including saved sittings and cases
	            buildCourtSiteNodes(dateNode, listDate);
        	}
	        
	        // Clone and then increment the list date by a day as otherwise the
	        // date on already created court room nodes would be changed too
	        listDate = (Calendar)listDate.clone();
	        listDate.add(Calendar.DATE, 1);
	        
        } while (!listDate.after(listModel.getListEndDate()));
    	
        // Add reserve node
        DefaultMutableTreeNode reserveNode = listModel.getTreeNodeFactory().createCaseParent(CaseOnListType.Reserve);
        rootNode.add(reserveNode);
        
        // Add reserve cases
        List<CaseOnListComplexValue> casesOnList = listModel.getCasesOnListForReserve();
        for (CaseOnListComplexValue caseOnList : casesOnList) {
        	// Create case node
        	DefaultMutableTreeNode caseNode = listModel.getTreeNodeFactory().createCase(caseOnList);
        	reserveNode.add(caseNode);
        }
        
        // Must now reload the model because the nodes have been directly added
        // to the root node rather than being added by using the model interface
        courtListingTreeModel.reload();
		
		// Expand the first court site under first date
    	if (expandFirstNode) {
    		expandFirstChildCourtSite(rootNode);
    	}
    }

	@Override
	protected DefaultMutableTreeNode getFirstChildCourtSiteNode(DefaultMutableTreeNode rootNode) {
		return rootNode.getChildCount() > 0 && rootNode.getChildAt(0).getChildCount() > 0 ?
				(DefaultMutableTreeNode)rootNode.getChildAt(0).getChildAt(0) : null;
	}
}
