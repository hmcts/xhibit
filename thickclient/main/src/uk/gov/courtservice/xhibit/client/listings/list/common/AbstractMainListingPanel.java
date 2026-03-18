package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.Outline;
import org.netbeans.swing.outline.OutlineModel;

import mseries.Calendar.MFieldListener;
import mseries.ui.MChangeListener;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCalendarBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CreateListOptionsValue;
import uk.gov.courtservice.xhibit.client.listings.ListTypeEnum;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.listings.createlist.CreateListOptionsModel;
import uk.gov.courtservice.xhibit.client.listings.list.outline.AbstractTreeNodeController;
import uk.gov.courtservice.xhibit.client.listings.list.outline.AbstractTreeNodeModelPopupListener;
import uk.gov.courtservice.xhibit.client.listings.list.outline.OutlineUtils;
import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeController;
import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeModelRenderRow;
import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeModelRenderTree;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDatePanelWithEvent;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractDateValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateEqualOrAfterDateValidator;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationUtils;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * Main Listing Panel class.
 * 
 * @author uphillj
 * 
 * @amend groenm
 * changed to an abstract class to be used across all list types
 * 23/3/18 - added sitting context menu and calling amend sitting
 *
 */
public abstract class AbstractMainListingPanel<T extends ListModel> extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final String SELECTED_NODE = "SELECTED_NODE";
	
	protected static final Integer ONE = 1;

	protected static final int CASE_NUMBER_COLUMN = 0;

	protected static final int CASE_TITLE_COLUMN = 1;

	protected static final int CASE_GROUP_COLUMN = 2;

	protected static final int TIME_ESTIMATE_COLUMN = 3;

	protected static final int HEARING_TYPE_COLUMN = 4;

	protected static final int TIME_MARKING_COLUMN = 5;
	
	protected static final int COURT_ROOM_LIST_COLUMN = 6;

	protected T listModel;

    protected ListDateRange listStartDatePanel;

    protected ListDateRange listEndDatePanel;
	
    protected ButtonGroup listTypeRadioButtonGroup;

    protected JRadioButton draftRadioButton;

    protected JRadioButton finalRadioButton;
	
	protected DefaultTreeModel courtListingTreeModel;
	
	protected Outline courtListingOutline;

	protected OutlineModel courtListingOutlineModel;	
    
    protected XDialog parent;
    
    protected boolean initialised;
	
    protected ArrayList<RefCalendarBasicValue> workingDays;

    private JButton publishButton;
    private XAction refreshAction;
    private XAction refreshFixturesAction;
    private XAction saveAction;

	public AbstractMainListingPanel(XDialog parent, T listModel, Outline listOutline, DefaultTreeModel listTreeModel) {
		super(new BorderLayout());
    	this.parent = parent;
		this.listModel = listModel;
		this.courtListingOutline = listOutline;
		this.courtListingTreeModel = listTreeModel;
		
		// Clear the court data cache
		XhibitSingleton.getInstance().clearCourtDataCache();
		
        jbInit();
	}
	
	public boolean isInitialised() {
		return initialised;
	}
	
	protected void jbInit() {
    	// Create panel for the listing controls
    	JPanel controls = new JPanel(new GridBagLayout());
		this.setPreferredSize(new Dimension(600, XHIBITConstant.getLineHeight()));
    	this.add(controls, BorderLayout.NORTH);
    	
    	// Create list date(s) on the left of the panel
    	if (ListTypeEnum.Daily.equals(listModel.getListType())) {
	    	controls.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingListDate")),
                  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
	                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
	    	controls.add(getListStartDatePanel(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
	                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
    	} else {
        	controls.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingListStartDate")),
                    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        	controls.add(getListStartDatePanel(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        	controls.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingListEndDate")),
                    new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        	controls.add(getListEndDatePanel(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
    	}
    	
    	// Create empty label between date(s) and buttons to share extra space 
    	controls.add(new JLabel(), new GridBagConstraints(GridBagConstraints.RELATIVE, 0, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
    	
    	// Create expand/collapse buttons in the centre of the panel
        GridBagConstraints gbcCenter = new GridBagConstraints(GridBagConstraints.RELATIVE, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
        		GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
        controls.add(new JButton(new ExpandAllNodesAction()), gbcCenter);
        controls.add(new JButton(new CollapseAllNodesAction()), gbcCenter);
        
    	// Create empty label between buttons and draft/final options to share extra space 
    	controls.add(new JLabel(), new GridBagConstraints(GridBagConstraints.RELATIVE, 0, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

    	// Create controls on the right of the panel
        GridBagConstraints gbcEast = new GridBagConstraints(GridBagConstraints.RELATIVE, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
        		GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
        buildListTypeRadioButtonGroup();
        controls.add(getDraftRadioButton(), gbcEast);
        controls.add(getFinalRadioButton(), gbcEast);
        controls.add(getPublishButton(), gbcEast);

    	// Create court listing table/tree
    	JScrollPane scrollPane = new JScrollPane(getCourtListingOutline());
    	this.add(scrollPane, BorderLayout.CENTER);
    	
		// Populate the outline control once the model is fully populated
    	populateCourtListingOutline();
	}
    
    protected ListDateRange getListStartDatePanel() {
        if (listStartDatePanel == null) {
            listStartDatePanel = new ListDateRange(this, listModel.getListStartDate());
        }
        return listStartDatePanel;
    }
    
    protected ListDateRange getListEndDatePanel() {
        if (listEndDatePanel == null) {
            listEndDatePanel = new ListDateRange(this, listModel.getListEndDate()) {
				private static final long serialVersionUID = 1L;
				@Override
				protected boolean isDateEditable() {
					return true;
				}
				@Override
				protected void dateChanged(Calendar date) {
					if (!listModel.getListEndDate().equals(date)) {
						listModel.setListEndDate(date);
						listModel.getList().setDirty(true);
					}
				}
			};
			// Validate the end date is after the start date
			listEndDatePanel.addValidationController(new ListEndDateBeforeStartDateValidator());
			// Validate for working days
			listEndDatePanel.addValidationController(new DateInWorkingDayValidator());
		}
        return listEndDatePanel;
    }
    
    /**
     * Get the ref calendars for the Firm/Warn List dates, which are
     * used to check if each date on the list is a working day.
     * 
     * @return
     */
    protected ArrayList<RefCalendarBasicValue> getWorkingDays() {
    	if (workingDays == null) {
    		workingDays = ListingDropdownPopulation.getRefCalendar(listModel.getListStartDate(), listModel.getListEndDate());
    	}
    	return workingDays;
    }

	protected void addListEndDateChangeListener(MChangeListener changeListener) {
		if (listEndDatePanel != null) {
			listEndDatePanel.getDateComponent().addMChangeListener(changeListener);
			listEndDatePanel.getDateComponent().addMFieldListener(new MFieldListener() {
				@Override
				public void fieldEntered(FocusEvent event) {
					// Do nothing
				}
				@Override
	            public void fieldExited(FocusEvent event) {
	                if (!event.isTemporary()) {
	                	listEndDatePanel.fireEvent();
	                	if (listModel.getList().isDirty()) {
	                		save("ListEndDatePanel-FieldExit");
	                	}
	                }
	            }
	        });
		}
	}

	protected List<String> getErrors() {
		List<String> errors = new ArrayList<String>();
		if (listEndDatePanel != null) {
			errors.addAll(listEndDatePanel.getErrors());
		}
		return errors;
	}
	
    protected JRadioButton getDraftRadioButton() {
    	if (draftRadioButton == null) {
    		draftRadioButton = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingOptionsDraft"));
    		draftRadioButton.setSelected(listModel.isListDraft());
    		draftRadioButton.addItemListener(new ItemListener() {
				@Override
                public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED && listModel.isListFinal()) {
						// It is okay to change from Final back to Draft if the list has yet to be published
						// or if it has that the user confirms the change back to Draft is okay to happen
						if (!showErrorMsg() && !listModel.hasListPublished() ||
							XMessageBox.alert(parent, 
							XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingDraftWarningTitle"),  
							true,
							XMessageBox.ICONQUESTION, 
							XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingDraftWarningMesssage"), 
							XMessageBox.YESNO,
							XMessageBox.DEFAULTNO)) {
							listModel.setListDraft();
							refreshDBOnly("draftRadioButton");
							save("draftRadioButton");
						}
						// Else revert change back to Draft by re-selecting Final
						else {
							finalRadioButton.setSelected(true);
						}
					}
				}
			});
		}
    	return draftRadioButton;
    }

    protected JRadioButton getFinalRadioButton() {
    	if (finalRadioButton == null) {
    		finalRadioButton = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingOptionsFinal"));
    		finalRadioButton.setSelected(listModel.isListFinal());
    		finalRadioButton.addItemListener(new ItemListener() {
				@Override
                public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED && listModel.isListDraft()) {
						if (!showErrorMsg()) {
							listModel.setListFinal();
							refreshDBOnly("finalRadioButton");
							save("finalRadioButton");
						} else {
							draftRadioButton.setSelected(true);
						}
					}
				}
			});
    	}
    	return finalRadioButton;
    }
    
    protected JButton getPublishButton() {
    	if (publishButton == null) {
    		publishButton = new JButton("");
    	}
    	return publishButton;
    }
    
    protected Outline getCourtListingOutline() {
    	if (courtListingOutlineModel == null) {
    		// Create table/tree model
    		courtListingOutlineModel = DefaultOutlineModel.createOutlineModel(courtListingTreeModel, new TreeNodeModelRenderRow(), true,
    													XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableCaseNumber"));

    		// Create table/tree handler
			TransferHandler transferHandler = new CourtListingTransferHandler(courtListingOutline, courtListingTreeModel);

    		// Setup defaults and drag and drop
    		TableUtils.setupDefaultsOnJTable(courtListingOutline);
			TableUtils.setupDragAndDropOnJTable(courtListingOutline, transferHandler);

			// Set display preferences
    		courtListingOutline.setRootVisible(false);
    		courtListingOutline.setIntercellSpacing(new Dimension(0, 0));
    		courtListingOutline.setShowGrid(false);    		
			
    		// Disable ability to reorder & hide columns and sort rows in UI
    		courtListingOutline.getTableHeader().setReorderingAllowed(false);
    		courtListingOutline.setColumnHidingAllowed(false);
    		courtListingOutline.setRowSorter(null);
    		
    		// Set the model and renderer for displaying model
    		courtListingOutline.setRenderDataProvider(new TreeNodeModelRenderTree());
    		courtListingOutline.setModel(courtListingOutlineModel);
    		
    		// Add listener for displaying popup menu
    		courtListingOutline.addMouseListener(new CourtListingPopupMenuListener(courtListingOutline));
    		
    		// Setup tooltips for column headers
    		List<String> columnHeaderToolTips = new ArrayList<String>();
    		columnHeaderToolTips.add(CASE_NUMBER_COLUMN, null);
    		columnHeaderToolTips.add(CASE_TITLE_COLUMN, null);
    		columnHeaderToolTips.add(CASE_GROUP_COLUMN, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableCaseGroupToolTip"));
    		columnHeaderToolTips.add(TIME_ESTIMATE_COLUMN, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableTimeEstimateToolTip"));
    		columnHeaderToolTips.add(HEARING_TYPE_COLUMN, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableHearingTypeToolTip"));
    		columnHeaderToolTips.add(TIME_MARKING_COLUMN, null);
    		columnHeaderToolTips.add(COURT_ROOM_LIST_COLUMN, XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingTableCourtRoomListToolTip"));
    		TableUtils.setupColumnHeaderToolTips(courtListingOutline, columnHeaderToolTips);
    		
    		// Set the min/max/preferred widths of the columns
    		courtListingOutline.getColumnModel().getColumn(CASE_NUMBER_COLUMN).setPreferredWidth(200);
    		courtListingOutline.getColumnModel().getColumn(CASE_TITLE_COLUMN).setPreferredWidth(100);
    		courtListingOutline.getColumnModel().getColumn(CASE_GROUP_COLUMN).setMinWidth(50);
    		courtListingOutline.getColumnModel().getColumn(CASE_GROUP_COLUMN).setMaxWidth(50);
    		courtListingOutline.getColumnModel().getColumn(TIME_ESTIMATE_COLUMN).setMinWidth(35);
    		courtListingOutline.getColumnModel().getColumn(TIME_ESTIMATE_COLUMN).setMaxWidth(35);
    		courtListingOutline.getColumnModel().getColumn(HEARING_TYPE_COLUMN).setMinWidth(40);
    		courtListingOutline.getColumnModel().getColumn(HEARING_TYPE_COLUMN).setMaxWidth(40);
    		courtListingOutline.getColumnModel().getColumn(TIME_MARKING_COLUMN).setMinWidth(100);
    		courtListingOutline.getColumnModel().getColumn(TIME_MARKING_COLUMN).setMaxWidth(100);
    		courtListingOutline.getColumnModel().getColumn(COURT_ROOM_LIST_COLUMN).setMinWidth(35);
    		courtListingOutline.getColumnModel().getColumn(COURT_ROOM_LIST_COLUMN).setMaxWidth(35);
    	}
    	return courtListingOutline;
    }

    protected void buildListTypeRadioButtonGroup() {
		listTypeRadioButtonGroup = new ButtonGroup();
		listTypeRadioButtonGroup.add(getDraftRadioButton());
		listTypeRadioButtonGroup.add(getFinalRadioButton());
    }

    protected void populateCourtListingOutline() {
		SwingWorker worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				while (!listModel.isListPopulated()) {
					Thread.sleep(100);
				}
				return null;
			}

			@Override
			protected void done() {
				try {
			    	// Populate court listing table/tree now model populated
					refreshNodesFromTreeModel(true);
				} finally {				
					// Panel is now ready for user interaction
			    	initialised = true;
				}
			}
		};
		worker.execute();
    }
    
    /**
     * Sub-classes build and populate all the nodes in the tree
     */
    protected abstract void buildCourtListingNodes(DefaultMutableTreeNode rootNode, boolean expandFirstNode);
    
    /**
     * Refresh the main listing panel 
     */
    protected void refresh() {
    	// Update list values from the model
    	if (!listModel.getList().isDirty()) {
    		getListEndDatePanel().setDate(listModel.getListEndDate());
    		getListEndDatePanel().fireEvent();
    		draftRadioButton.setSelected(listModel.isListDraft());
    		finalRadioButton.setSelected(listModel.isListFinal());
    	}
    	
    	// Get the currently expanded nodes
    	Map<String,List<String>> currentExpandedNodes = getExpandedNodes();   
    	// Rebuild the nodes from the latest tree model
    	refreshNodesFromTreeModel(false);
    	// Expand the nodes that were previously open
    	expandNodes(currentExpandedNodes);
    }

    private void refreshNodesFromTreeModel(boolean expandFirstNode) {
    	DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)courtListingTreeModel.getRoot();
    	rootNode.removeAllChildren();
    	buildCourtListingNodes(rootNode, expandFirstNode);
    }

	private Map<String,List<String>> getExpandedNodes() {
		Map<String,List<String>> result = new HashMap<String,List<String>>();
		AbstractTreeNodeController controller;
		// If there is a selected row then store that 
		if (courtListingOutline.getSelectedRow() > -1) {
			controller = (AbstractTreeNodeController) OutlineUtils.getTreeNodeController(courtListingOutline, courtListingOutline.getSelectedRow());
			if (!"".equals(controller.getControllerId())) {
				String selectedNode = controller.getControllerType() + controller.getControllerId();
				result.put(SELECTED_NODE, new ArrayList<String>(Arrays.asList(selectedNode)));
			}
		}
		// Get the currently expanded nodes
		for (int row = 0; row < courtListingOutline.getRowCount(); row++) {
			TreePath path = courtListingOutlineModel.getLayout().getPathForRow(row);
			if (courtListingOutline.isExpanded(path)) {
				// Get the current controller 
				controller = (AbstractTreeNodeController) OutlineUtils.getTreeNodeController(courtListingOutline, row);
				// Get the list of nodes for this controller type
				List<String> nodeIdsToExpand = result.get(controller.getControllerType());
				if (nodeIdsToExpand == null) {
					nodeIdsToExpand = new ArrayList<String>();
				}
				// Add the current node to the list of expanded nodes
				nodeIdsToExpand.add(controller.getControllerId());
				result.put(controller.getControllerType(), nodeIdsToExpand);
			}
		}
		return result;
	}

	private void expandNodes(Map<String,List<String>> nodesToExpand) {
		// Get the previously selected node
		String selectedNode = nodesToExpand.get(SELECTED_NODE) != null ? nodesToExpand.get(SELECTED_NODE).get(0) : null;		
		// Expand the rows that exist
		for (int row = 0; row < courtListingOutline.getRowCount(); row++) {
			AbstractTreeNodeController controller = (AbstractTreeNodeController) OutlineUtils.getTreeNodeController(courtListingOutline, row);
			List<String> nodeIdsToExpand = nodesToExpand.get(controller.getControllerType());
			if (nodeIdsToExpand != null && nodeIdsToExpand.contains(controller.getControllerId())) {
				TreePath path = courtListingOutlineModel.getLayout().getPathForRow(row);
				courtListingOutline.expandPath(path);
			}
			// Check if this node was the previously selected one
			String currentTypeAndId = controller.getControllerType() + controller.getControllerId();
			if (selectedNode != null && currentTypeAndId.equals(selectedNode)) {
				courtListingOutline.setRowSelectionInterval(row, row);
			}
		}
	}

	protected DefaultMutableTreeNode getFirstChildCourtSiteNode(DefaultMutableTreeNode rootNode) {
		return rootNode.getChildCount() > 0 ? (DefaultMutableTreeNode)rootNode.getChildAt(0) : null;
	}

	protected void expandFirstChildCourtSite(DefaultMutableTreeNode rootNode) {
		DefaultMutableTreeNode nodeToExpand = getFirstChildCourtSiteNode(rootNode);
		if (nodeToExpand != null) {
			courtListingOutline.expandPath(new TreePath(nodeToExpand.getPath()));
		}
	}

	public boolean showErrorMsg() {
		List<String> errors = getErrors();
		if (!errors.isEmpty()) {
			XMessageBox.alert(parent, 
					XHIBITConstant.getResource(XhibitBundles.XhibitConstant, "exception.validation.title"), true,  
					XMessageBox.ICONERROR, errors.get(0), XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
		}
		return !errors.isEmpty();
	}

    protected class CourtListingPopupMenuListener extends AbstractTreeNodeModelPopupListener {
		
    	public CourtListingPopupMenuListener(Outline outline) {
			super(outline);
		}

		@Override
    	protected void showPopupMenu(MouseEvent e, TreeNodeController controller) {
			if (controller.getPopupMenu() != null) {
				controller.getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
			}
    	}
    }
    
    protected class ExpandAllNodesAction extends XAction {

		private static final long serialVersionUID = 1L;

		public ExpandAllNodesAction() {
			super("MainListingExpandAll");
		}

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			for (int row = 0; row < courtListingOutline.getRowCount(); row++) {
				TreePath path = courtListingOutlineModel.getLayout().getPathForRow(row);
				courtListingOutline.expandPath(path);
			}
		}
    	
    }
    
    protected class CollapseAllNodesAction extends XAction {

		private static final long serialVersionUID = 1L;

		public CollapseAllNodesAction() {
			super("MainListingCollapseAll");
		}

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			for (int row = courtListingOutline.getRowCount() - 1; row >= 0; row--) {
				TreePath path = courtListingOutlineModel.getLayout().getPathForRow(row);
				courtListingOutline.collapsePath(path);
			}
		}
    	
    }

	protected class ListDateRange extends XDatePanelWithEvent {

		private static final long serialVersionUID = 1L;
		private Border originalBorder;
		private Border errorBorder;
		private List<String> errors = new ArrayList<String>();
		private List<AbstractDateValidator> validationControllers = new ArrayList<AbstractDateValidator>();

		public ListDateRange(JPanel containingPanel, Calendar defaultDate) {
			super(containingPanel, defaultDate, true);
			getDateComponent().getDisplay().setDisabledTextColor(Color.BLACK);
			setEnabled(isDateEditable());
			setWidth(100);
			originalBorder = this.getBorder();
			if(this.getBorder() != null) {
				Insets copyInsets = this.getBorder().getBorderInsets(this);
				Insets newInsets = new Insets(copyInsets.top - 1, copyInsets.left - 1, copyInsets.bottom - 1, copyInsets.right - 1);
				errorBorder = BorderFactory.createCompoundBorder(new LineBorder(java.awt.Color.RED, 1), new EmptyBorder(newInsets));
			} else {
				errorBorder = BorderFactory.createLineBorder(java.awt.Color.RED);
			}
		}

		public List<String> getErrors() {
			fireValidation();
			return errors;
		}

		public void addValidationController(AbstractDateValidator validationController) {
			validationControllers.add(validationController);
		}
		
		private String validateOverlappingDates() {
			String result = null;
			try {
				Date startDate = getListStartDatePanel().getDate().getTime();
				Date endDate = getListEndDatePanel().getDate().getTime();
				// Check for existing lists
				CreateListOptionsValue optionsValue = XhibitDelegateHelper.getListingsDelegate().
						findCreateListOptions(XhibitSingleton.getInstance().getCourtId(), 
								listModel.getListType().toString(), startDate, endDate);

				// Use the Optional model
				CreateListOptionsModel optionsModel = new CreateListOptionsModel(listModel.getListType(), startDate, endDate);
				optionsModel.setPreviousDailyList(optionsValue.getPreviousDailyList());
				optionsModel.setPreviousFirmList(optionsValue.getPreviousFirmList());
				optionsModel.setPreviousWarnList(optionsValue.getPreviousWarnList());

				// Check if the new dates clash with an existing one
				if (optionsModel.isOptional() ) {
					if (listModel.getListType().isWarned()) {
						if (!listModel.getList().getListId().equals(optionsValue.getPreviousWarnList().getListId())) {
							result = XHIBITConstant.getResource(XhibitBundles.Listings,"mainListingsWarnListExists");
						}
					} else if (listModel.getListType().isFirm()) {
						if (!listModel.getList().getListId().equals(optionsValue.getPreviousFirmList().getListId())) {
							result = XHIBITConstant.getResource(XhibitBundles.Listings,"mainListingsFirmListExists");
						}
					}
				}
			} catch (CSValidationException ex) {
				XHIBITConstant.handleError(ex);
			}
			return result;
		}

		protected boolean fireValidation() {
			this.setBorder(originalBorder);
			errors.clear();
			// Validate the field
			for (AbstractDateValidator validationController :validationControllers) {
				validationController.validate(this, errors);
			}	
			// Validate if there are any overlapping dates
			if (errors.isEmpty()) {
				String overlappingError = validateOverlappingDates();
				if (overlappingError != null) {
					errors.add(overlappingError);
				}
			}
			// Validation failed - Display error
			if (!errors.isEmpty()) {
				this.setBorder(errorBorder);
			}
			return errors.isEmpty();
		}

		protected boolean isDateEditable() {
			return false;
		}

		protected boolean isDateValid() {
			return isDateEditable() && ValidationUtils.hasDate(this);
		}

		protected void dateChanged(Calendar date) { 
		}

		@Override
		protected void fireEvent() {
			if ( isDateValid() && getErrors().isEmpty()) {
				try {
					dateChanged(getDate());
				} catch (CSValidationException ex) {
					XHIBITConstant.handleError(ex);
				}
			}
		}
	}	

	protected class ListEndDateBeforeStartDateValidator extends DateEqualOrAfterDateValidator {		
		
		public ListEndDateBeforeStartDateValidator() {
			super(XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingListEndDate"), 
					XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingListStartDate"), 
					listStartDatePanel);
		}
	}

	protected class DateInWorkingDayValidator extends AbstractDateValidator {	
	
		@Override
		public void validate(XDatePanel target, List<String> errors) {
			if (hasDate(target) && !ListingDropdownPopulation.isWorkingDay(getWorkingDays(), getDate(target))){
				errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText, "listings.validation.notWorkingDay"));
			}
		}
	}

	public void setPublishAction(final XAction publishAction) {
		this.publishButton.setAction(publishAction);
	}

	public XAction getRefreshAction() {
		return refreshAction;
	}

	public void setRefreshAction(final XAction refreshAction) {
		this.refreshAction = refreshAction;
	}

	public XAction getRefreshFixturesAction() {
		return refreshFixturesAction;
	}

	public void setRefreshFixturesAction(final XAction refreshFixturesAction) {
		this.refreshFixturesAction = refreshFixturesAction;
	}

	public XAction getSaveAction() {
		return saveAction;
	}

	public void setSaveAction(final XAction saveAction) {
		this.saveAction = saveAction;
	}

	private void refreshDBOnly(String source) {
		getRefreshAction().setModel("RefreshDB");
		getRefreshAction().actionPerformed(new ActionEvent(this, -1, "RefreshAction - " + source));
	}

	private boolean isExiting() {
		return getSaveAction() == null;
	}

	private void save(String source) {
		if (!isExiting()) {
			getSaveAction().actionPerformed(new ActionEvent(this, 0, "SaveAction - " + source));
		}
	}
}
