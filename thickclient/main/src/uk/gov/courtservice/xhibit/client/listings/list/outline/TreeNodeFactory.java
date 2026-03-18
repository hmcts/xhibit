package uk.gov.courtservice.xhibit.client.listings.list.outline;

import java.util.Calendar;

import javax.swing.tree.DefaultMutableTreeNode;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.client.listings.CaseOnListType;

/**
 * Interface for all factories used to create tree nodes for lists.
 * 
 * @author uphillj
 * @amend groenm - added the list date a room is associated with.
 *
 */
public interface TreeNodeFactory {

	/**
	 * Create date tree node.
	 * 
	 * @param listDate
	 * @return tree node
	 */
	DefaultMutableTreeNode createDate(Calendar listDate);

	/**
	 * Create court site tree node.
	 * 
	 * @param courtSite
	 * @return tree node
	 */
	DefaultMutableTreeNode createCourtSite(XhbCourtSiteBasicValue courtSite);

	/**
	 * Create court room tree node.
	 *  
	 * @param listDate
	 * @param courtRoom
	 * @return tree node
	 */
	DefaultMutableTreeNode createCourtRoom(Calendar listDate, XhbCourtRoomBasicValue courtRoom);
	
	/**
	 * Create case tree node.
	 * 
	 * @param caseOnList
	 * @return tree node
	 */
	DefaultMutableTreeNode createCase(CaseOnListComplexValue caseOnList);

	/**
	 * Create tree node that has cases as children.
	 * 
	 * @param parentType
	 * @param params
	 * @return tree node
	 */
	DefaultMutableTreeNode createCaseParent(CaseOnListType parentType, Object... params);
	
}
