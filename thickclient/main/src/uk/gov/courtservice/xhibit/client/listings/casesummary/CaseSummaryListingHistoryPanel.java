package uk.gov.courtservice.xhibit.client.listings.casesummary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.listing.ListingsControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingHistoryInformation;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CaseDiaryFixtureValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownBoxCellRenderer;
import uk.gov.courtservice.xhibit.client.listings.details.CaseListingDeleteFixtureDialog;
import uk.gov.courtservice.xhibit.client.listings.details.CaseListingDeleteFixtureModel;
import uk.gov.courtservice.xhibit.client.listings.list.common.RemoveCaseFromListDialog;
import uk.gov.courtservice.xhibit.client.listings.list.common.RemoveCaseFromListModel;
import uk.gov.courtservice.xhibit.client.listings.list.common.TableUtils;
import uk.gov.courtservice.xhibit.client.listings.list.daily.DailyListModel;
import uk.gov.courtservice.xhibit.client.listings.list.daily.DailyListPanel;
import uk.gov.courtservice.xhibit.client.listings.list.firm.FirmListModel;
import uk.gov.courtservice.xhibit.client.listings.list.firm.FirmListPanel;
import uk.gov.courtservice.xhibit.client.listings.list.warned.WarnedListModel;
import uk.gov.courtservice.xhibit.client.listings.list.warned.WarnedListPanel;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.listeners.PopupListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;


public class CaseSummaryListingHistoryPanel extends CaseSummaryTab {

	private static final long serialVersionUID = 1L;
	private static final String EMPTY_STRING = "";
	private static final String DELIMITER_STRING = ", ";
	private static final String SPACE_STRING = " ";
	private XDialog parent;
	private CaseSummaryModel model;
	private DisplayOnlyField caseTitleText;
	private JTable listingHistoryTable;
	private JTable defendantsInHearingTable;
	private List<DropdownCodeStringValue> noOfListsArray;
    private XComboBox noOfListsCombo;
    private DisplayOnlyField listStatusText;
    private JPopupMenu listingHistoryPopupMenu;
    private JMenuItem changeRemovalReasonMenuOption;
    private JMenuItem reinstateInListMenuOption;
    private JMenuItem goToListMenuOption;
    private JMenuItem removeFromListMenuOption;
    private Collection<CaseListingHistoryInformation> caseOnLists;
    private FilterChangeActionListener filterChangeActionListener = new FilterChangeActionListener();

	public CaseSummaryListingHistoryPanel(XDialog parent, CaseSummaryModel model) {
		super();
		this.parent = parent;
		this.model = model;
		jbInit();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void moveModelToScreen() {
		this.caseTitleText.setText(model.getCase().getCaseTitle());
		Integer rowNumberLimit = getSelectedNoOfLists();
		caseOnLists = XhibitDelegateHelper.getListingsDelegate().findCaseListHistory(model.getCaseId(), rowNumberLimit);
		CaseSummaryListingHistoryTableModel tableModel =  (CaseSummaryListingHistoryTableModel) this.listingHistoryTable.getModel();
		tableModel.setTableSource((ArrayList<CaseListingHistoryInformation>)caseOnLists);
	}

	private void jbInit() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,15,0,15), 0, 0);
		
		gbc.weighty = 0.1;
	    JPanel caseTitlePanel = initCaseTitlePanel();
	    this.add(caseTitlePanel, gbc);
		
	    gbc.gridy++;
		gbc.weighty = 0.05;
	    JPanel listingHistoryFilterPanel = initListingHistoryFilterPanel();
	    this.add(listingHistoryFilterPanel, gbc);
	    
		gbc.gridy++;
		gbc.weighty = 0.4;
	    JPanel listingHistoryPanel = initListingHistoryPanel();
	    this.add(listingHistoryPanel, gbc);
	    
	    gbc.gridy++;
		gbc.weighty = 0.05;
	    JPanel listStatusPanel = initListStatusPanel();
	    this.add(listStatusPanel, gbc);
	    
	    gbc.gridy++;
		gbc.weighty = 0.4;
	    JPanel defendantsInHearingPanel = initDefendantsInHearingPanel();
	    this.add(defendantsInHearingPanel, gbc);

	}

	private JPanel initCaseTitlePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel caseTitlePanel = new JPanel();
		caseTitlePanel.setLayout(new GridBagLayout());

		// Add the panel elements
		gbc.weightx = 0.05;
		JLabel caseTitleLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalCaseTitle"));
		caseTitlePanel.add(caseTitleLabel, gbc);
		
		gbc.gridx++;		
		gbc.weightx = 0.95;
		caseTitleText = new DisplayOnlyField();
		caseTitlePanel.add(caseTitleText, gbc);
	
		return caseTitlePanel;
	}

	private JPanel initListingHistoryFilterPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = new JPanel(new GridBagLayout());
		
		gbc.weightx = 0.05;
		JLabel noOfListsLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "listHistoryNoOfListsDisplayed"));
		panel.add(noOfListsLabel, gbc);
		
		gbc.gridx++;		
		gbc.weightx = 0.95;
		panel.add(getNoOfListsCombo(), gbc);
		getNoOfListsCombo().addActionListener(filterChangeActionListener);
		
		return panel;
	}
	
	private JPanel initListingHistoryPanel() {
		CaseSummaryListingHistoryTableModel tableModel = new CaseSummaryListingHistoryTableModel();
		this.listingHistoryTable = new JTable(tableModel); 
		JScrollPane listingHistoryTableScrollPanel = new JScrollPane(listingHistoryTable);
		listingHistoryTableScrollPanel.setPreferredSize(listingHistoryTable.getPreferredSize());
		TableUtils.setupDefaultsOnJTable(listingHistoryTable);
		setTableColumnWidths(listingHistoryTable, tableModel.getColumnWidths());
		this.listingHistoryTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()){				
					handleSelectionEvent(e);
				}
			}
		});
		JPopupMenu popupMenu = getListingHistoryPopupMenu();
		listingHistoryTable.add(popupMenu);
		listingHistoryTable.addMouseListener(new PopupListener(popupMenu) {
			
			@Override
		    protected void maybeShowPopup(MouseEvent e) {
		    	// Select the row clicked
				JTable table = (JTable) e.getSource();
				CaseSummaryListingHistoryTableModel tableModel = (CaseSummaryListingHistoryTableModel) table.getModel();
		    	int row = table.rowAtPoint(e.getPoint());
		    	table.setRowSelectionInterval(row, row);
		    	CaseListingHistoryInformation rowValue = tableModel.getRow(row);
		    	
		    	// Hide/Show the menu options
		    	changeRemovalReasonMenuOption.setVisible(rowValue.isChangeRemovalReasonMenuValid()); 
		    	reinstateInListMenuOption.setVisible(rowValue.isReinstateMenuValid());
		    	goToListMenuOption.setVisible(rowValue.isGoToListMenuValid());
		    	removeFromListMenuOption.setVisible(rowValue.isRemoveFromListMenuValid());
		    	
		    	// Show the popup menu
		    	if (changeRemovalReasonMenuOption.isVisible() ||
		    		reinstateInListMenuOption.isVisible() ||
					goToListMenuOption.isVisible() || 
		    		removeFromListMenuOption.isVisible()) {
		    		super.maybeShowPopup(e);
		    	}
		    }
		});
		
		JPanel listingHistoryPanel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,  XHIBITConstant.nonContainerInsets, 0, 0);
		listingHistoryPanel.setLayout(new GridBagLayout());
		listingHistoryPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"listingHistoryPanelTitle")));
		listingHistoryPanel.add(listingHistoryTableScrollPanel, gbc);
		return listingHistoryPanel;
	}
	
	private CaseListingHistoryInformation getSelectedListingHistoryRow() {
		CaseListingHistoryInformation selectedCaseListingHistory = null;
		int rowNo = listingHistoryTable.getSelectedRow();
		if (caseOnLists != null && !caseOnLists.isEmpty() && rowNo != -1) {
			selectedCaseListingHistory = ((List<CaseListingHistoryInformation>) caseOnLists).get(rowNo);
		}
		return selectedCaseListingHistory;
	}
	
	@SuppressWarnings("unchecked")
	private void handleSelectionEvent(ListSelectionEvent e) {
		CaseSummaryListingHistoryTableModel tableModel = (CaseSummaryListingHistoryTableModel)this.listingHistoryTable.getModel();
		CaseListingHistoryInformation selectedCaseListingHistory = null;
		for(int index = e.getFirstIndex(); index<=e.getLastIndex();index++)		{
			if(((ListSelectionModel) e.getSource()).isSelectedIndex(index)){
				selectedCaseListingHistory = tableModel.getListHistory(index);
				break;
			}
		}
		
		setListStatus(selectedCaseListingHistory);
				
		ArrayList<DefendantValue> defendantsOnList = null;
		if (selectedCaseListingHistory == null) {
			defendantsOnList = new ArrayList<DefendantValue>();
		} else if (selectedCaseListingHistory.isFixture()) {
			defendantsOnList = (ArrayList<DefendantValue>) XhibitDelegateHelper.getListingsDelegate().findDefendantsOnFixture(selectedCaseListingHistory.getCaseDiaryFixtureId());			
		} else {
				defendantsOnList = (ArrayList<DefendantValue>) XhibitDelegateHelper.getListingsDelegate().findDefendantsOnList(selectedCaseListingHistory.getCaseOnListId());
			}
		CaseSummaryDefendantsTableModel defendantsInHearingTableModel = (CaseSummaryDefendantsTableModel) this.defendantsInHearingTable.getModel();
		defendantsInHearingTableModel.setTableSource(defendantsOnList);
		
		this.repaint();
	}

	private JPanel initListStatusPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel parentPanel = new JPanel(new GridBagLayout());
		JPanel childPanel = new JPanel(new GridBagLayout());
		
		listStatusText = new DisplayOnlyField();
		childPanel.add(listStatusText, gbc);
		
		parentPanel.add(childPanel, gbc);
		
		return parentPanel;
	}
	
	private JPanel initDefendantsInHearingPanel() {
		CaseSummaryDefendantsTableModel tableModel = new CaseSummaryDefendantsTableModel(CaseSummaryDefendantsTableModel.TableId.LIST_HISTORY_PANEL);
		this.defendantsInHearingTable = new JTable(tableModel);
		JScrollPane defendantsInHearingTableScrollPanel = new JScrollPane(defendantsInHearingTable);
		defendantsInHearingTableScrollPanel.setPreferredSize(defendantsInHearingTable.getPreferredSize());
		TableUtils.setupDefaultsOnJTable(defendantsInHearingTable);
		setTableColumnWidths(defendantsInHearingTable, tableModel.getColumnWidths());
		JPanel defendantsInHearingPanel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,  XHIBITConstant.nonContainerInsets, 0, 0);
		defendantsInHearingPanel.setLayout(new GridBagLayout());
		defendantsInHearingPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"defendantsInHearingPanelTitle")));
		defendantsInHearingPanel.add(defendantsInHearingTableScrollPanel, gbc);
		return defendantsInHearingPanel;
	}

    private XComboBox getNoOfListsCombo() {
    	if (noOfListsCombo == null) {
    		noOfListsCombo = new XComboBox();
			setDropdownBoxArray(noOfListsCombo, getNoOfListsArray().toArray());
			noOfListsCombo.setSelectedIndex(1);
    	} 
    	return noOfListsCombo;
    }
    
    private List<DropdownCodeStringValue> getNoOfListsArray() {
    	if (noOfListsArray == null) {
    		noOfListsArray = new ArrayList<DropdownCodeStringValue>();
    		Integer[] rowNumbers = new Integer[] {10, 20, 50, 100};    		
    		for (Integer rowNumber : rowNumbers) { 
    			noOfListsArray.add(new DropdownCodeStringValue(rowNumber.toString(),rowNumber.toString()));
    		}
    		noOfListsArray.add(new DropdownCodeStringValue("All", EMPTY_STRING));
    	}
    	return noOfListsArray;
    }

    private void setListStatus(CaseListingHistoryInformation row) {
    	StringBuilder listStatus = new StringBuilder(EMPTY_STRING);
    	if (row != null && !row.isFixture()) {
    		listStatus.append(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "listHistoryListStatus"));
    		listStatus.append(SPACE_STRING);
    		if (row.getDraftOrFinal() != null) {
    			listStatus.append(getResourceBundle("listHistoryListStatus_"+row.getDraftOrFinal()));
    		}
    		listStatus.append(SPACE_STRING);
    		if (row.getListNumber() != null) {
    			listStatus.append(row.getListNumber());
    		}
	    	listStatus.append(DELIMITER_STRING);
	    	if (row.isPublished()) {
	    		listStatus.append(getResourceBundle("listHistoryListStatusPublishedOn", new Object[] { getFormattedDate(row.getPublishDate()) }));
	    	} else {
	    		listStatus.append(getResourceBundle("listHistoryListStatusSavedOn", new Object[] { getFormattedDate(row.getLastUpdateDate()) }));
	    	}
    	}
    	listStatusText.setText(listStatus.toString());
    }
    
	private String getFormattedDate(Timestamp date) {
		return XDateFormat.format(date, XDateFormat.DATEFORMAT);
	}

	private Integer getSelectedNoOfLists() {
		Integer result = null;
		DropdownCodeStringValue selectedItem = (DropdownCodeStringValue) noOfListsCombo.getSelectedItem();
		if (!EMPTY_STRING.equals(selectedItem.getCode())) {
			result = Integer.valueOf(selectedItem.getCode());
		}
		return result;
	}

	private void setDropdownBoxArray(XComboBox comboBox, final Object[] arrayItems) {
		comboBox.setModel(new DefaultComboBoxModel(arrayItems));
		if (comboBox.getSelectedItem() != null) {
			comboBox.setRenderer(new ListingDropdownBoxCellRenderer());
			comboBox.enableAutoSelect();
		} else {
			comboBox.setEnabled(false);
		}
	}
	
	private CaseDiaryFixtureComplexValue getCaseDiaryFixture(Integer id) {
		CaseDiaryFixtureComplexValue result = null;
		try {
			result = XhibitDelegateHelper.getListingsDelegate().findCaseDiaryFixture(id);
		} catch (ListingsControllerException ex) {
			XHIBITConstant.handleError(ex);
		}
		return result;
	}
	
	private CaseOnListComplexValue getCaseOnList(Integer id) {
		CaseOnListComplexValue result = null;
		try {
			result = XhibitDelegateHelper.getListingsDelegate().findCaseOnListComplexValue(id);
		} catch (ListingsControllerException ex) {
			XHIBITConstant.handleError(ex);
		}
		return result;
	}
	
	private boolean changeFixtureRemovalReason(Integer caseDiaryFixtureId) throws CSRecoverableException {
		// Get the latest values for the fixture
		CaseDiaryFixtureComplexValue caseDiaryFixtureComplexValue = getCaseDiaryFixture(caseDiaryFixtureId);
		Integer predefinedReasonId = caseDiaryFixtureComplexValue.getVacationPreDefinedRsonId();
		String reasonFreeText = caseDiaryFixtureComplexValue.getVacationFreetextReason();
		// Call the popup
		CaseListingDeleteFixtureModel childModel = new CaseListingDeleteFixtureModel(caseDiaryFixtureComplexValue);
		CaseListingDeleteFixtureDialog childDialog = new CaseListingDeleteFixtureDialog(parent,childModel);
		childDialog.setVisible(true);
		boolean preDefinedChanged = !((predefinedReasonId == null && childModel.getFixture().getVacationPreDefinedRsonId() == null) ||
				(predefinedReasonId != null && predefinedReasonId.equals(childModel.getFixture().getVacationPreDefinedRsonId())));
		boolean freeTextChanged = !((reasonFreeText == null && childModel.getFixture().getVacationFreetextReason() == null) ||
				(reasonFreeText != null && reasonFreeText.equals(childModel.getFixture().getVacationFreetextReason())));
		// Upon return from popup - Refresh screen or not
		return preDefinedChanged || freeTextChanged;
	}
	
	private boolean changeListingRemovalReason(Integer caseOnListId) throws CSRecoverableException {
		CaseOnListComplexValue caseOnListComplexValue = getCaseOnList(caseOnListId);
		Integer predefinedReasonId = caseOnListComplexValue.getVacationPreDefinedRsonId();
		String reasonFreeText = caseOnListComplexValue.getReasonForRemoval();
		// Call the popup
		RemoveCaseFromListModel childModel = new RemoveCaseFromListModel(caseOnListComplexValue, true);
		childModel.setHearingTypeCode(getSelectedListingHistoryRow().getHearingTypeCode());
		RemoveCaseFromListDialog childDialog = new RemoveCaseFromListDialog(parent, childModel);
		childDialog.setVisible(true);
		boolean preDefinedChanged = !((predefinedReasonId == null && childModel.getCaseOnListComplexValue().getVacationPreDefinedRsonId() == null) ||
				(predefinedReasonId != null && predefinedReasonId.equals(childModel.getCaseOnListComplexValue().getVacationPreDefinedRsonId())));
		boolean freeTextChanged = !((reasonFreeText == null && childModel.getCaseOnListComplexValue().getReasonForRemoval() == null) ||
				(reasonFreeText != null && reasonFreeText.equals(childModel.getCaseOnListComplexValue().getReasonForRemoval())));
		// Upon return from popup - Refresh screen or not
		return preDefinedChanged || freeTextChanged;
	}
	
	private void reinstateFixture(Integer caseDiaryFixtureId) throws CSRecoverableException {
		// Get the latest values for the fixture
		CaseDiaryFixtureComplexValue caseDiaryFixtureComplexValue = getCaseDiaryFixture(caseDiaryFixtureId);
		if(XhibitDelegateHelper.getListingsDelegate().isCaseDiaryFixture(model.getCaseId(), caseDiaryFixtureComplexValue.getListingDate(), caseDiaryFixtureComplexValue.getListingDate())) {
			XMessageBox.alert(parent, 
					XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "listHistoryFixtureAlreadyExistsErrorTitle"), true,
					XMessageBox.ICONERROR, XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "listHistoryFixtureAlreadyExistsErrorMessage"), XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
		}
		else {
			caseDiaryFixtureComplexValue.setVacationFreetextReason(null);
			caseDiaryFixtureComplexValue.setVacationPreDefinedRsonId(null);
			caseDiaryFixtureComplexValue.setDateVacated(null);
			caseDiaryFixtureComplexValue.setObsInd(null);
			caseDiaryFixtureComplexValue.setStatus("A");
			CaseDiaryFixtureValue caseDiaryFixture = new CaseDiaryFixtureValue(caseDiaryFixtureComplexValue, model.getCase().getCourtID(), model.getCaseId());
			XhibitDelegateHelper.getListingsDelegate().saveCaseDiaryFixture(caseDiaryFixture,
					XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
		}
	}
	
    private JPopupMenu getListingHistoryPopupMenu() {
        if (listingHistoryPopupMenu == null) {
        	listingHistoryPopupMenu = new JPopupMenu();
        	changeRemovalReasonMenuOption = new JMenuItem(new ChangeRemovalReasonAction());
        	listingHistoryPopupMenu.add(changeRemovalReasonMenuOption);
        	reinstateInListMenuOption = new JMenuItem(new ReinstateInListAction());
        	listingHistoryPopupMenu.add(reinstateInListMenuOption);
			goToListMenuOption = new JMenuItem(new GoToListAction());
        	listingHistoryPopupMenu.add(goToListMenuOption);
        	removeFromListMenuOption = new JMenuItem(new RemoveFromListAction());
        	listingHistoryPopupMenu.add(removeFromListMenuOption);
        }
        return listingHistoryPopupMenu;
    }
	
	private static XhibitApplicationController getXac(final XDialog parent) {
		XhibitApplicationController xac = (XhibitApplicationController)parent.getParentFrame();
		if (xac == null) {
			XDialog grandParent = (XDialog) parent.getParent();
			xac = (XhibitApplicationController)grandParent.getParentFrame();
		}
		return xac;
	}

	private class FilterChangeActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			moveModelToScreen();
		}
	};
	
	public class ChangeRemovalReasonAction extends XAction {
		
		private static final long serialVersionUID = 1L;

		public ChangeRemovalReasonAction()
		{
			super("ChangeRemovalReason");
		} 
				
		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			CaseListingHistoryInformation selectedRow = getSelectedListingHistoryRow();
			if (selectedRow != null) {
				boolean refreshScreen = false;
				if (selectedRow.isFixture()) {
					refreshScreen = changeFixtureRemovalReason(selectedRow.getCaseDiaryFixtureId());
				} else {
					refreshScreen = changeListingRemovalReason(selectedRow.getCaseOnListId());
				}
				
				// Refresh the screen
				if (refreshScreen) {
					moveModelToScreen();
				}
			}
		}
	}

	public class ReinstateInListAction extends XAction {
		
		private static final long serialVersionUID = 1L;

		public ReinstateInListAction()
		{
			super("ReinstateInList");
		} 

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			CaseListingHistoryInformation selectedRow = getSelectedListingHistoryRow();
			if (selectedRow != null){
				reinstateFixture(selectedRow.getCaseDiaryFixtureId());
				model.setDataChanged(true);
				// Refresh the screen
				moveModelToScreen();
			}
		}
	}
	public class GoToListAction extends XAction {
		
		private static final long serialVersionUID = 1L;
		private final XhibitApplicationController xac = getXac(parent);

		public GoToListAction()
		{
			super("GoToList");
			this.setEnabled(model.isFromListScreen() ? false : true);
		}
			
		private void openDailyList(Integer listId) throws CSRecoverableException {
			DailyListModel model = new DailyListModel(xac);
			model.openList(listId);
			final DailyListPanel dailyListPanel = new DailyListPanel(parent, model);
			dailyListPanel.showPanel();
		}
		
		private void openFirmList(Integer listId) throws CSRecoverableException {
			FirmListModel model = new FirmListModel(xac);
			model.openList(listId);	
			FirmListPanel firmListPanel = new FirmListPanel(parent, model);
			firmListPanel.showPanel();
	    } 
		
		private void openWarnedList(Integer openListId) throws CSRecoverableException {
			WarnedListModel model = new WarnedListModel(xac);
			model.openList(openListId);
			WarnedListPanel warnedListPanel = new WarnedListPanel(parent, model);
			warnedListPanel.showPanel();
		}

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			CaseListingHistoryInformation selectedRow = getSelectedListingHistoryRow();
			if (selectedRow != null && selectedRow.getCaseOnListId() != null) {
				CaseOnListComplexValue caseOnList = getCaseOnList(selectedRow.getCaseOnListId());
				if (caseOnList != null) {
					//Open list
					if (selectedRow.isWarned()) {
						openWarnedList(caseOnList.getListId());
					} else if (selectedRow.isFirm()) {
						openFirmList(caseOnList.getListId());
					} else {
						openDailyList(caseOnList.getListId());
					}
					// Refresh the screen
					moveModelToScreen();
				}
			}
		}
	}
	public class RemoveFromListAction extends XAction {
		
		private static final long serialVersionUID = 1L;

		public RemoveFromListAction()
		{
			super("RemoveFromList");
		} 

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			CaseListingHistoryInformation selectedRow = getSelectedListingHistoryRow();
			if (selectedRow != null){
				// Refresh the screen if case is removed from list
				if ( changeListingRemovalReason(selectedRow.getCaseOnListId()) ) {
					moveModelToScreen();
				}
			}
		}
	}
}
