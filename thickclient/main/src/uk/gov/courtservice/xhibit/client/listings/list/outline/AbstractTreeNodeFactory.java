package uk.gov.courtservice.xhibit.client.listings.list.outline;

import java.util.Calendar;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.netbeans.swing.outline.Outline;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListComplexValue;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListModel;
import uk.gov.courtservice.xhibit.client.util.XDialog;

/**
 * Base class for all factories used to create tree nodes for lists.
 * 
 * @author uphillj
 * @amend groenm - added the list date a room is associated with.
 *
 */
public abstract class AbstractTreeNodeFactory implements TreeNodeFactory {

    protected XDialog listDialog;

    protected ListModel listModel;
	
	protected Outline listOutline;
	
	protected DefaultTreeModel listTreeModel;

	public AbstractTreeNodeFactory(XDialog listDialog, ListModel listModel, Outline listOutline, DefaultTreeModel listTreeModel) {
		this.listDialog = listDialog;
		this.listModel = listModel;
		this.listOutline = listOutline;
		this.listTreeModel = listTreeModel;
	}

	/**
	 * Create tree node from supplied parameters.
	 * 
	 * @param listDate
	 * @return tree node
	 */
	protected DefaultMutableTreeNode createDateTreeNode(Calendar listDate) {
		DateTreeNodeModel model = createDateTreeNodeModel(listDate);
		DateTreeNodeController controller = createDateTreeNodeController(model);
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(controller);
		controller.setTreeNode(treeNode);
		return treeNode;
	}

	/**
	 * Create model for the tree node.
	 * 
	 * @param listDate
	 * @return model
	 */
	protected DateTreeNodeModel createDateTreeNodeModel(Calendar listDate) {
		final DateTreeNodeModel model = new DateTreeNodeModel();
		model.setListDate(listDate);
		return model;
	}

	/**
	 * Create controller for the tree node.
	 * 
	 * @param model
	 * @return controller
	 */
	protected DateTreeNodeController createDateTreeNodeController(DateTreeNodeModel model) {
		final DateTreeNodeController controller = new DateTreeNodeController(this, listDialog, listModel, listOutline, listTreeModel);
		controller.setModel(model);
		return controller;
	}

	/**
	 * Create tree node from supplied parameters.
	 * 
	 * @param courtSite
	 * @return tree node
	 */
	protected DefaultMutableTreeNode createCourtSiteTreeNode(XhbCourtSiteBasicValue courtSite) {
		CourtSiteTreeNodeModel model = createCourtSiteTreeNodeModel(courtSite);
		CourtSiteTreeNodeController controller = createCourtSiteTreeNodeController(model);
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(controller);
		controller.setTreeNode(treeNode);
		return treeNode;
	}

	/**
	 * Create model for the tree node.
	 * 
	 * @param courtSite
	 * @return model
	 */
	protected CourtSiteTreeNodeModel createCourtSiteTreeNodeModel(XhbCourtSiteBasicValue courtSite) {
		final CourtSiteTreeNodeModel model = new CourtSiteTreeNodeModel();
		model.setCourtSite(courtSite);
		return model;
	}

	/**
	 * Create controller for the tree node.
	 * 
	 * @param model
	 * @return controller
	 */
	protected CourtSiteTreeNodeController createCourtSiteTreeNodeController(CourtSiteTreeNodeModel model) {
		final CourtSiteTreeNodeController controller = new CourtSiteTreeNodeController(this, listDialog, listModel, listOutline, listTreeModel);
		controller.setModel(model);
		return controller;
	}

	/**
	 * Create tree node from supplied parameters.
	 *
	 * @param listDate
	 * @param courtRoom
	 * @return tree node
	 */
	protected DefaultMutableTreeNode createCourtRoomTreeNode(Calendar listDate, XhbCourtRoomBasicValue courtRoom) {
		CourtRoomTreeNodeModel model = createCourtRoomTreeNodeModel(listDate, courtRoom);
		CourtRoomTreeNodeController controller = createCourtRoomTreeNodeController(model);
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(controller);
		controller.setTreeNode(treeNode);
		return treeNode;
	}

	/**
	 * Create model for the tree node.
	 * 
	 * @param listDate
	 * @param courtRoom
	 * @return model
	 */
	protected CourtRoomTreeNodeModel createCourtRoomTreeNodeModel(Calendar listDate, XhbCourtRoomBasicValue courtRoom) {
		final CourtRoomTreeNodeModel model = new CourtRoomTreeNodeModel();
		model.setCourtRoom(courtRoom);
		model.setListDate(listDate);
		return model;
	}

	/**
	 * Create controller for the tree node.
	 * 
	 * @param model
	 * @return controller
	 */
	protected CourtRoomTreeNodeController createCourtRoomTreeNodeController(CourtRoomTreeNodeModel model) {
		final CourtRoomTreeNodeController controller = new CourtRoomTreeNodeController(this, listDialog, listModel, listOutline, listTreeModel);
		controller.setModel(model);
		return controller;
	}

	/**
	 * Create tree node from supplied parameters.
	 * 
	 * @param listDate
	 * @param courtSite
	 * @return tree node
	 */
	protected DefaultMutableTreeNode createFloaterCasesTreeNode(Calendar listDate, XhbCourtSiteBasicValue courtSite) {
		FloaterCasesTreeNodeModel model = createFloaterCasesTreeNodeModel(listDate, courtSite);
		FloaterCasesTreeNodeController controller = createFloaterCasesTreeNodeController(model);
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(controller);
		controller.setTreeNode(treeNode);
		return treeNode;
	}

	/**
	 * Create model for the tree node.
	 * 
	 * @param listDate
	 * @param courtSite
	 * @return model
	 */
	protected FloaterCasesTreeNodeModel createFloaterCasesTreeNodeModel(Calendar listDate, XhbCourtSiteBasicValue courtSite) {
		final FloaterCasesTreeNodeModel model = new FloaterCasesTreeNodeModel();
		model.setCourtSite(courtSite);
		model.setListDate(listDate);
		return model;
	}

	/**
	 * Create controller for the tree node.
	 * 
	 * @param model
	 * @return controller
	 */
	protected FloaterCasesTreeNodeController createFloaterCasesTreeNodeController(FloaterCasesTreeNodeModel model) {
		final FloaterCasesTreeNodeController controller = new FloaterCasesTreeNodeController(this, listDialog, listModel, listOutline, listTreeModel);
		controller.setModel(model);
		return controller;
	}

	/**
	 * Create tree node from supplied parameters.
	 * 
	 * @return tree node
	 */
	protected DefaultMutableTreeNode createReserveCasesTreeNode() {
		ReserveCasesTreeNodeModel model = createReserveCasesTreeNodeModel();
		ReserveCasesTreeNodeController controller = createReserveCasesTreeNodeController(model);
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(controller);
		controller.setTreeNode(treeNode);
		return treeNode;
	}

	/**
	 * Create model for the tree node.
	 * 
	 * @return model
	 */
	protected ReserveCasesTreeNodeModel createReserveCasesTreeNodeModel() {
		final ReserveCasesTreeNodeModel model = new ReserveCasesTreeNodeModel();
		return model;
	}

	/**
	 * Create controller for the tree node.
	 * 
	 * @param model
	 * @return controller
	 */
	protected ReserveCasesTreeNodeController createReserveCasesTreeNodeController(ReserveCasesTreeNodeModel model) {
		final ReserveCasesTreeNodeController controller = new ReserveCasesTreeNodeController(this, listDialog, listModel, listOutline, listTreeModel);
		controller.setModel(model);
		return controller;
	}

	/**
	 * Create tree node from supplied parameters.
	 *
	 * @param courtSite
	 * @return tree node
	 */
	protected DefaultMutableTreeNode createNoDateCasesTreeNode(XhbCourtSiteBasicValue courtSite) {
		NoDateCasesTreeNodeModel model = createNoDateCasesTreeNodeModel(courtSite);
		NoDateCasesTreeNodeController controller = createNoDateCasesTreeNodeController(model);
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(controller);
		controller.setTreeNode(treeNode);
		return treeNode;
	}

	/**
	 * Create model for the tree node.
	 * 
	 * @param courtSite
	 * @return model
	 */
	protected NoDateCasesTreeNodeModel createNoDateCasesTreeNodeModel(XhbCourtSiteBasicValue courtSite) {
		final NoDateCasesTreeNodeModel model = new NoDateCasesTreeNodeModel();
		model.setCourtSite(courtSite);
		return model;
	}

	/**
	 * Create controller for the tree node.
	 * 
	 * @param model
	 * @return controller
	 */
	protected NoDateCasesTreeNodeController createNoDateCasesTreeNodeController(NoDateCasesTreeNodeModel model) {
		final NoDateCasesTreeNodeController controller = new NoDateCasesTreeNodeController(this, listDialog, listModel, listOutline, listTreeModel);
		controller.setModel(model);
		return controller;
	}

	/**
	 * Create tree node from supplied parameters.
	 * 
	 * @param sitting
	 * @return tree node
	 */
	protected DefaultMutableTreeNode createSittingTreeNode(SittingOnListComplexValue sitting) {
		SittingTreeNodeModel model = createSittingTreeNodeModel(sitting);
		SittingTreeNodeController controller = createSittingTreeNodeController(model);
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(controller);
		controller.setTreeNode(treeNode);
		return treeNode;
	}
	
	/**
	 * Create model for the tree node.
	 * 
	 * @param sitting
	 * @return model
	 */
	protected SittingTreeNodeModel createSittingTreeNodeModel(SittingOnListComplexValue sitting) {
		final SittingTreeNodeModel model = new SittingTreeNodeModel();
		model.setSittingOnList(sitting);
		return model;
	}

	/**
	 * Create controller for the tree node.
	 * 
	 * @param model
	 * @return controller
	 */
	protected SittingTreeNodeController createSittingTreeNodeController(SittingTreeNodeModel model) {
		final SittingTreeNodeController controller = new SittingTreeNodeController(this, listDialog, listModel, listOutline, listTreeModel);
		controller.setModel(model);
		return controller;
	}
	
	/**
	 * Create tree node from supplied parameters.
	 * 
	 * @param caseOnList
	 * @return tree node
	 */
	protected DefaultMutableTreeNode createCaseTreeNode(CaseOnListComplexValue caseOnList) {
		CaseTreeNodeModel model = createCaseTreeNodeModel(caseOnList);
		final CaseTreeNodeController controller = createCaseTreeNodeController(model);
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(controller, false);
		controller.setTreeNode(treeNode);
		return treeNode;
	}

	/**
	 * Create model for the tree node.
	 * 
	 * @param caseOnList
	 * @return model
	 */
	protected CaseTreeNodeModel createCaseTreeNodeModel(CaseOnListComplexValue caseOnList) {
		final CaseTreeNodeModel model = new CaseTreeNodeModel();
		model.setCaseOnList(caseOnList);
		return model;
	}

	/**
	 * Create controller for the tree node.
	 * 
	 * @param model
	 * @return controller
	 */
	protected CaseTreeNodeController createCaseTreeNodeController(CaseTreeNodeModel model) {
		final CaseTreeNodeController controller = new CaseTreeNodeController(this, listDialog, listModel, listOutline, listTreeModel);
		controller.setModel(model);
		return controller;
	}
	
}
