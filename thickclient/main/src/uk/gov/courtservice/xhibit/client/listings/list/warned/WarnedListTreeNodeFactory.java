package uk.gov.courtservice.xhibit.client.listings.list.warned;

import java.util.Calendar;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.netbeans.swing.outline.Outline;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.client.listings.CaseOnListType;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListModel;
import uk.gov.courtservice.xhibit.client.listings.list.outline.AbstractTreeNodeFactory;
import uk.gov.courtservice.xhibit.client.util.XDialog;

/**
 * Factory for creating all tree nodes for warned lists.
 * 
 * @author uphillj
 * @amend groenm - added the list date a room is associated with.
 *
 */
public class WarnedListTreeNodeFactory extends AbstractTreeNodeFactory {

	public WarnedListTreeNodeFactory(XDialog listDialog, ListModel listModel, Outline listOutline, DefaultTreeModel listTreeModel) {
		super(listDialog, listModel, listOutline, listTreeModel);
	}

	@Override
	public DefaultMutableTreeNode createDate(Calendar listDate) {
		throw new UnsupportedOperationException("Unsupported node");
	}

	@Override
	public DefaultMutableTreeNode createCourtSite(XhbCourtSiteBasicValue courtSite) {
		return createCourtSiteTreeNode(courtSite);
	}

	@Override
	public DefaultMutableTreeNode createCourtRoom(Calendar listDate, XhbCourtRoomBasicValue courtRoom) {
		throw new UnsupportedOperationException("Unsupported node");
	}

	@Override
	public DefaultMutableTreeNode createCase(CaseOnListComplexValue caseOnList) {
		return createCaseTreeNode(caseOnList);
	}

	@Override
	public DefaultMutableTreeNode createCaseParent(CaseOnListType parentType, Object... params) {
		DefaultMutableTreeNode treeNode = null;
		
		if (CaseOnListType.NoDate.equals(parentType)) {
			treeNode = createNoDateCasesTreeNode((XhbCourtSiteBasicValue)params[0]);
		}
		else {
			throw new UnsupportedOperationException("Unsupported node");
		}
		
		return treeNode;
	}
}
