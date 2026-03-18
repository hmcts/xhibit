package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import mseries.ui.MChangeEvent;
import mseries.ui.MChangeListener;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.listing.ListingsControllerException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.listings.details.CaseListingDetailDialog;
import uk.gov.courtservice.xhibit.client.listings.list.outline.TreeNodeFactory;
import uk.gov.courtservice.xhibit.client.listings.preview.PreviewListDialog;
import uk.gov.courtservice.xhibit.client.listings.preview.PreviewListModel;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanelForXPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * Base class for daily/warned/firm list panels.
 * 
 * @author uphillj
 *
 */
public abstract class AbstractListPanel<T extends ListModel, S extends AbstractListingDiaryPanel, U extends AbstractMainListingPanel, V extends  ListingDiaryModel> extends XPanel {

	private static final long serialVersionUID = 1L;

	protected CustomButtonPanel buttonPanel;
	
	protected T listModel;
	
	protected S listingDiaryPanel;

	protected U mainListingPanel;
	
	protected CourtListingOutline listOutline;
	
	protected DefaultTreeModel listTreeModel;
	
	protected JSplitPane splitPane;
	
	protected JPanel leftPanel;
	
	protected OtherCasesPanel otherCasesPanel;
	
	protected JButton statsButton;
	
	protected JButton refreshButton;

	protected JButton previewButton;
    
    protected XDialog parent;
    
    protected V listingDiaryModel;

    private XAction saveAction = new SaveAction();
    private XAction publishAction = new PublishAction();
    private XAction refreshAction = new RefreshAction();
    private XAction refreshFixturesAction = new RefreshFixturesAction();

	public AbstractListPanel(final XDialog parent, final T listModel, final V listingDiaryModel) throws CSRecoverableException {
		this.parent = parent;
		this.listModel = listModel;
		this.listingDiaryModel = listingDiaryModel;
		stepInitialise();
		jbInit();
		initTitle();
	}

	protected void jbInit() {
		// Initialisation of dialog uses asynchronous background processing due to the
		// size of data that can be retrieved, so prevent the user clicking on anything
		parent.shield();
		
		// Initialise this panel
        this.setLayout(new BorderLayout());
        this.add(getSplitPane(), BorderLayout.CENTER);

        // Add custom buttons to the dialog button panel
        CustomButtonPanel buttonPanel = getButtonPanel();
        buttonPanel.setShowCancelButton(true);
        buttonPanel.getCancelAction().populateFromBundle("Close");
        statsButton = buttonPanel.addButton("MainListingStats", false, false);
        refreshButton = buttonPanel.addButton("MainListingRefresh", false, false);
		previewButton = buttonPanel.addButton("MainListingPreview", false, false);

		// Only allow interaction with dialog once all panels are fully initialised
		unshieldPostPanelInit();

		// Add the change listener for the editable end date
		mainListingPanel.addListEndDateChangeListener(new MChangeListener() {

			private boolean isEventEnabled = true;

			@Override
			public void valueChanged(MChangeEvent event) {
				if (event.getType() == MChangeEvent.PULLDOWN_OPENED) {
					// Disable event whilst in the calendar popup
					isEventEnabled = false;
				} else if (event.getType() == MChangeEvent.PULLDOWN_CLOSED) {
					// Enable event when exiting the calendar popup 
					isEventEnabled = true;
				}
				else if (event.getType() == MChangeEvent.CHANGE) {
					if (isEventEnabled) {
						validateMainListingPanel();
					}
				}
			}

			private void validateMainListingPanel() {
				@SuppressWarnings("unchecked")
				List<String> errors = mainListingPanel.getErrors();
				listingDiaryPanel.setPanelEnabled(errors.isEmpty());
				otherCasesPanel.setPanelEnabled(errors.isEmpty());
				if (!errors.isEmpty()) {
					XMessageBox.alert((JDialog)parent.getRootPane().getParent(),
							XHIBITConstant.getResource(XhibitBundles.XhibitConstant,"exception.validation.title"),
							true, XMessageBox.ICONERROR, errors.get(0), XMessageBox.OK_ONLY,
							XMessageBox.DEFAULTOK);
				} else {
					saveAndRefresh("ListEndDateChanged");
				}
			}
		});
	}

    protected JSplitPane getSplitPane() {
    	if (splitPane == null) {
    		// LHS is the listing diary and other cases wrapped in a scroll pane
    		JComponent leftComponent = new JScrollPane(getLeftPanel(), ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
    													ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    		leftComponent.setBorder(BorderFactory.createEmptyBorder());

    		// RHS is the main listing panel which already has a scroll bar on outline control
    		JComponent rightComponent = getMainListingPanel();

    		// Create split pane with LHS and RHS with majority of resize given to RHS
    		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftComponent, rightComponent);
    		splitPane.setResizeWeight(0.50);
    	}
        return splitPane;
    }

	protected JPanel getLeftPanel() {
		if (leftPanel == null) {
			leftPanel = new JPanel(new GridBagLayout());
		
			leftPanel.add(getListingDiaryPanel(), new GridBagConstraints(0, 0, 1, 3, 1.0, 0.75, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));

			leftPanel.add(getOtherCasesPanel(), new GridBagConstraints(0, 3, 1, 1, 1.0, 0.25, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
		}
		return leftPanel;
	}

    protected U getMainListingPanel() {
    	if (mainListingPanel == null) {
    		mainListingPanel = createMainListingPanel();
    		listOutline.setParentPanel(mainListingPanel);
    		setChildPanelActions();
    	}
    	return mainListingPanel;
    }
   
    protected JPanel getOtherCasesPanel() {
    	if (otherCasesPanel == null) {
    		otherCasesPanel = new OtherCasesPanel(listModel);
    	}  
    	return otherCasesPanel;
    }

    protected void unshieldPostPanelInit() {
		SwingWorker worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				while (!getListingDiaryPanel().isInitialised() ||
						!getMainListingPanel().isInitialised()) {
					Thread.sleep(100);
				}
				return null;
			}

			@Override
			protected void done() {
				parent.unshield();
			}
		};
		worker.execute();
    }
    
	@Override
	public void stepInitialise() throws CSRecoverableException {
		// Initialise outline and tree model here rather than in main listing
		// panel because they are needed to construct the tree node factory
		listOutline = new CourtListingOutline();
		listTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode(), true);
		listModel.setTreeNodeFactory(createTreeNodeFactory());
	}

	@Override
	public void stepActivate() throws CSRecoverableException {
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
	}

	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (!update) {
			if (statsButton.equals(getDeinitialiseSource())) {
				showStats();
			} else if (refreshButton.equals(getDeinitialiseSource())) {
				refresh("RefreshButton", true);
				refreshFixtures("RefreshButton");
			} else if (previewButton.equals(getDeinitialiseSource())) {	
				previewList();
			} else {
				confirmCloseWhenDirty();
			}
		}
	}
    
    /**
     * Sub-classes return a new instance of their tree node factory
     */
    protected abstract TreeNodeFactory createTreeNodeFactory();
    
    /**
     * Sub-classes return a new instance of their main listing panel
     */
    protected abstract U createMainListingPanel();
    
    /**
     * Sub-classes return a new instance of their listing diary panel
     */
    protected abstract S createListingDiaryPanel();
    
    /**
     * 
     * Sub-classes return the instance of their listing diary panel
     */
    protected S getListingDiaryPanel() {
    	if (listingDiaryPanel == null) {
    		listingDiaryPanel = createListingDiaryPanel();
    	}
    	return listingDiaryPanel;
    }
    
    /**
     * Show the stats for the list which is currently only available for Warned.
     * 
     * @throws CSRecoverableException 
     */
    protected void showStats() throws CSRecoverableException {
    	// Refresh the list to include changes from any other users
    	refresh("showStats", true);
    	
    	// Get the list of hearing types that are used to retrieve the stats
    	List<String> trialHearingTypes = ListingDropdownPopulation.getRefListingDataValues("LIST_STATS_CASE_TRIAL_HEARING_TYPES");
    	
    	// Get the relevant numbers for displaying in the pop-up
    	int fixtureCount = XhibitDelegateHelper.getListingsDelegate().getFixtureCount(
    															XhibitSingleton.getInstance().getCourtId(),
    															listModel.getListStartDate().getTime(),
    															listModel.getListEndDate().getTime());
    	int caseCount = listModel.getCasesOnList().size();
    	int trialCaseCount = listModel.getCasesOnList(trialHearingTypes).size();
    	
    	// Create message with required numbers about list
		String statsMsg = MessageFormat.format(XHIBITConstant.getResource(XhibitBundles.Listings, 
				"mainListingStatsMesssage"), new Object[] { caseCount, trialCaseCount, fixtureCount });
    	
		XMessageBox.alert(parent, 
				XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingStatsTitle"),  
				true,
				XMessageBox.ICONINFORMATION, 
				statsMsg,  
				XMessageBox.OK_ONLY,
				XMessageBox.DEFAULTOK);
    }

    private void setChildPanelActions() {
    	mainListingPanel.setSaveAction(saveAction);
    	mainListingPanel.setPublishAction(publishAction);
		mainListingPanel.setRefreshAction(refreshAction);
		mainListingPanel.setRefreshFixturesAction(refreshFixturesAction);
    }

    /**
     * Refresh the list based on the elements on the listRefreshModel 
     * 
     */
    private void refresh(final String source, final boolean refreshDB) {
    	// Fetch the latest data from the DB
		refreshAction.setModel(refreshDB ? "RefreshDB" : null);
		refreshAction.actionPerformed(new ActionEvent(this, 0, "RefreshAction - " + source));
	}

	private boolean isExiting() {
		return saveAction == null;
	}

	private void saveAndRefresh(String source) {
		if (!isExiting()) {
			saveAction.actionPerformed(new ActionEvent(this, 0, "SaveAction - " + source));
			refresh(source+"-AfterSave", true);
		}
	}

	private void refreshFixtures(final String source) {
		// Fetch the latest data from the DB
		refreshFixturesAction.actionPerformed(new ActionEvent(this, 0, "RefreshFixturesAction - " + source));
	}

    /**
	 * Create the button panel
	 */
	private CustomButtonPanel getButtonPanel() {
		if (buttonPanel == null) {
			// Create a new button panel at the foot of this XPanel
			buttonPanel = new CustomButtonPanelForXPanel(listModel.getXac(), this);
			this.add(buttonPanel, BorderLayout.SOUTH);
		}
		return buttonPanel;
	}

	/**
	 * Set title based on whether create or open and list type.
	 */
	private void initTitle() {
		// Title of dialog varies according to whether the list is new or existing
		String resourceKey = listModel.isListNew() ? "mainListingCreateListTitle" : "mainListingOpenListTitle";
		// Format title with the type of the list and then set it on the dialog
		listModel.getXac().setTitle(MessageFormat.format(XHIBITConstant.getResource(XhibitBundles.Listings, resourceKey),
												new Object[] { listModel.getListType().toString() } ));
	}

    /**
     * Open the dialog which allows the user to preview the saved list.
     * 
     * @throws CSRecoverableException
     */
    protected void previewList() throws CSRecoverableException {
    	// Refresh the list to include changes from any other users
    	refresh("previewList", true);
    	
		PreviewListModel model = new PreviewListModel(listModel.getList().getListId(), listModel.getListType());
		PreviewListDialog dialog = new PreviewListDialog(parent.getParentFrame(), model);
        dialog.setVisible(true);            
    }
    
    /**
     * Publish the saved list which if successful gives the user an option
     * to close the dialog or displays an error if fails.
     * 
     * @throws CSRecoverableException
     */
    protected void publishList() throws CSRecoverableException {
    	// Refresh the list to include changes from any other users
    	refresh("publishList", true);
    	try {
    		// Publish the list
    		listModel.publishList();
    		
    		// Publish successful so confirm close
    		confirmCloseAfterPublish();
    		
    	} catch (ListingsControllerException e) {
    		// Published failed so display error
			XMessageBox.alert(parent, 
				XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingPublishErrorTitle"),  
				true,
				XMessageBox.ICONERROR, 
				XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingPublishErrorMesssage"),  
				XMessageBox.OK_ONLY,
				XMessageBox.DEFAULTOK);
			
			// Cancel the closing of the dialog
			throw new UserCancelException();
    	}
    }
    
    /**
     * If list published successfully, ask the user to confirm close.
     * 
     * @throws UserCancelException
     */
    protected void confirmCloseAfterPublish() throws UserCancelException {
		boolean confirm = XMessageBox.alert(parent, 
			XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingPublishCloseTitle"),  
			true,
			XMessageBox.ICONQUESTION, 
			XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingPublishCloseMesssage"),  
			XMessageBox.YESNO,
			XMessageBox.DEFAULTNO);

		// If user did not confirm, cancel the closing of the dialog
		if (!confirm) {
			throw new UserCancelException();
		}
    }
    
    /**
     * If there are unsaved changes, ask the user to confirm close
     * which will lose the changes they have made but not saved.
     * 
     * @throws UserCancelException
     */
    protected void confirmCloseWhenDirty() throws UserCancelException {
		if (listModel.hasUnsavedChanges()) {
			// If list has unsaved changes, ask the user to confirm close
			boolean confirm = XMessageBox.alert(parent, 
				XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingUnsavedChangesTitle"),  
				true,
				XMessageBox.ICONQUESTION, 
				XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingUnsavedChangesCloseMessage"),  
				XMessageBox.YESNO,
				XMessageBox.DEFAULTNO);

			// If user did not confirm, cancel the closing of the dialog
			if (!confirm) {
				throw new UserCancelException();
			}
		}
    }
 
    /**
     * Close all parents (as a main child screen cannot support a parent pop-up)
     */
    private void closeParents(JDialog parent) {
    	JDialog grandParent = parent.getParent() instanceof JDialog ? (JDialog) parent.getParent() : null;
    	parent.dispose();
    	if (grandParent != null) {
    		if (grandParent instanceof CaseListingDetailDialog) {
    			// Stop the Search screen from popping up after we exit the case listing entry screen
    			((CaseListingDetailDialog) grandParent).getModel().setExitImmediately(true);
    		}
    		closeParents(grandParent);
    	}
    }

    /**
     * Show this panel and close the parent frames
     */
	public void showPanel() {
		closeParents(parent);
		listModel.getXac().open(this);
	}

	/**
     * Exit the screen without saving
     */
	private void exitScreen() {
		//Prevent firing the save upon exit
		saveAction = null;
		setChildPanelActions();
		// Exit the screen
		XAction action = XhibitActions.getAction(listModel.getXac(), XhibitActions.Close);
		action.actionPerformed(new ActionEvent(listModel.getXac(), 0, action.getName()));
	}

	public class SaveAction extends XAction {
		
		private static final long serialVersionUID = 1L;

		public SaveAction() {
			super();
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			super.actionPerformed(actionEvent);
			// Unset the dirty flag on the list, so it refreshes
            listModel.getList().setDirty(false);
			refresh("SaveAction", true);
		}

		@Override
		public void xActionPerformed(ActionEvent ae) throws Exception {
			try {
				listModel.saveList();
				if ("Y".equals(listModel.getList().getObsInd())) {
					exitScreen();
				}
 			} catch (ListingsControllerException e) {
				if (XHIBITConstant.isOptimisticLockError(e)) {
					listModel.showOptimisticLockMsg();
				} else {
					throw e;
				}
			}
		}
	}

	public class PublishAction extends XAction {
		
		private static final long serialVersionUID = 1L;

		public PublishAction() {
			super("MainListingPublish");
		}

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			publishList();
			exitScreen();
		} 
	}
	
	public class RefreshFixturesAction extends XAction {
		
		private static final long serialVersionUID = 1L;

		public RefreshFixturesAction() {
			super();
		}

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			listingDiaryPanel.refreshFixturesCaseTable();
		} 
	}
	
	public class RefreshAction extends XAction {
		
		private static final long serialVersionUID = 1L;
		private Integer preventRefreshOfScreen = Integer.valueOf(-1);

		public RefreshAction() {
			super();
		}

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			try {
				// Fetch the latest data from the DB
				if (this.getModel() != null) {
					listModel.refresh();
				}

				// Update the screen with the latest data
				if (!preventRefreshOfScreen.equals(e.getID())) {
					mainListingPanel.refresh();
				}
			} catch (UserCancelException ex) {
				exitScreen();
			}
		} 
	}
}
