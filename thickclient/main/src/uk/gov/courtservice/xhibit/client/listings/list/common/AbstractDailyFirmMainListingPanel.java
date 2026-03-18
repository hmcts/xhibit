package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.util.Calendar;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.netbeans.swing.outline.Outline;

import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.court.CourtStructureValue;
import uk.gov.courtservice.xhibit.client.listings.CaseOnListType;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * Base class for daily/firm Main Listing Panel.
 * 
 * @author uphillj
 *
 */
public abstract class AbstractDailyFirmMainListingPanel<T extends AbstractDailyFirmListModel> extends AbstractMainListingPanel<T> {

	private static final long serialVersionUID = 1L;

	public AbstractDailyFirmMainListingPanel(XDialog parent, T listModel, Outline listOutline, DefaultTreeModel listTreeModel) {
		super(parent, listModel, listOutline, listTreeModel);
	}

	/**
	 * Add court sites and all its child nodes including saved sittings and cases
	 * 
	 * @param parentNode
	 * @param listDate
	 */
	protected void buildCourtSiteNodes(DefaultMutableTreeNode parentNode, Calendar listDate) {
    	// Get court sites for court
        CourtStructureValue courtStructure = XhibitSingleton.getInstance().getCourtStructureValue();
        Sorter.sort(courtStructure.getCourtSites(), new String[] { "courtSiteCode" }, Sorter.ASCENDING);
        
        // Add court sites to parent node for supplied list date
        for (int i = 0; i < courtStructure.getCourtSites().length; i++) {
        	// Create court site node
            XhbCourtSiteBasicValue courtSite = courtStructure.getCourtSites()[i];
            DefaultMutableTreeNode courtSiteNode = listModel.getTreeNodeFactory().createCourtSite(courtSite);
            parentNode.add(courtSiteNode);

            // Get rooms for court site
            XhbCourtRoomBasicValue courtRooms[] = courtStructure.getCourtRoomsForSite(courtSite.getCourtSiteId());
            Sorter.sort(courtRooms, new String[] { "crestCourtRoomNo" }, Sorter.ASCENDING);

            // Add court rooms
            for (int j = 0; j < courtRooms.length; j++) {
            	// Create court room node
                XhbCourtRoomBasicValue courtRoom = courtRooms[j];
                DefaultMutableTreeNode courtRoomNode = listModel.getTreeNodeFactory().createCourtRoom(listDate, courtRoom);
                courtSiteNode.add(courtRoomNode);
                
                // Add sittings
                List<SittingOnListComplexValue> sittingsOnList = listModel.getSittingsOnList(listDate.getTime(), courtRoom.getCourtRoomId());
                for (SittingOnListComplexValue sittingOnList : sittingsOnList) {
                	// Create sitting node
                	DefaultMutableTreeNode sittingNode = listModel.getTreeNodeFactory().createCaseParent(CaseOnListType.Sitting, sittingOnList);
                	courtRoomNode.add(sittingNode);
                    
                    // Add sitting cases
                    List<CaseOnListComplexValue> casesOnList = listModel.getCasesOnListForSitting(sittingOnList.getSittingOnListId());
                    for (CaseOnListComplexValue caseOnList : casesOnList) {
                    	// Create case node
                    	DefaultMutableTreeNode caseNode = listModel.getTreeNodeFactory().createCase(caseOnList);
                    	sittingNode.add(caseNode);
                    }
                }
            }

            // Add floater node
            DefaultMutableTreeNode floaterNode = listModel.getTreeNodeFactory().createCaseParent(CaseOnListType.Floater, listDate, courtSite);
            courtSiteNode.add(floaterNode);
            
            // Add floater cases
            List<CaseOnListComplexValue> casesOnList = listModel.getCasesOnListForFloater(listDate.getTime(), courtSite.getCourtSiteId());
            for (CaseOnListComplexValue caseOnList : casesOnList) {
            	// Create case node
            	DefaultMutableTreeNode caseNode = listModel.getTreeNodeFactory().createCase(caseOnList);
            	floaterNode.add(caseNode);
            }
        }
    }

}
