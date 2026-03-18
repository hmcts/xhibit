package uk.gov.courtservice.xhibit.client.openexistinglist;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;
import uk.gov.courtservice.xhibit.client.listings.ListTypeEnum;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownBoxCellRenderer;
import uk.gov.courtservice.xhibit.client.listings.list.common.TableUtils;
import uk.gov.courtservice.xhibit.client.listings.list.daily.DailyListModel;
import uk.gov.courtservice.xhibit.client.listings.list.daily.DailyListPanel;
import uk.gov.courtservice.xhibit.client.listings.list.firm.FirmListModel;
import uk.gov.courtservice.xhibit.client.listings.list.firm.FirmListPanel;
import uk.gov.courtservice.xhibit.client.listings.list.warned.WarnedListModel;
import uk.gov.courtservice.xhibit.client.listings.list.warned.WarnedListPanel;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class OpenExistingListPanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;

	private static final String EMPTY_STRING = "";	
	private static final String DATE_FORMAT = "dd-MM-yyyy";
	private static final String DATETIME_FORMAT = "dd-MM-yyyy HH:mm";
	
	private static final String DAILY_TABLE = "DAILY_TABLE";
	private static final String NONDAILY_TABLE = "NONDAILY_TABLE";
	
	private static final String DRAFTORFINAL_DRAFT_CODE = "D";
	private static final String DRAFTORFINAL_FINAL_CODE = "F";
	private static final String DRAFTORFINAL_DRAFT_DESC = "Draft";
	private static final String DRAFTORFINAL_FINAL_DESC = "Final";
	
	private static final String PUBLISH_STATUS_SAVED = "Saved";
	private static final String PUBLISH_STATUS_PUBLISHED = "Published";
	private static final String PUBLISH_STATUS_PUBLISH_FAILED = "Publish Failed";
	private static final String PUBLISH_STATUS_NOT_APPLICABLE = "Not Applicable";
	
	private OpenExistingListModel openExistingListModel;
	
    private ButtonGroup listTypeRadioButtonGroup = null;
    private JRadioButton dailyRb = null;
    private JRadioButton firmRb = null;
    private JRadioButton warnedRb = null;
    private JLabel noOfListsLabel;
    private List<DropdownCodeStringValue> noOfListsArray;
    private XComboBox noOfListsCombo; 
   
	private XTable listingsTable;
	private DefaultTableModel listingsTableModel;
	private JScrollPane scrollPane;
	private JButton deleteButton;
	private JButton selectButton;
   
	private Collection<ListBasicValue> lists;
	private TableSelectionListener tableSelectionListener = new TableSelectionListener();
	private FilterChangeActionListener filterChangeActionListener = new FilterChangeActionListener();
    
    private XDialog parent;
	
    public OpenExistingListPanel(XDialog parent, OpenExistingListModel openExistingListModel) throws CSRecoverableException {
    	this.parent = parent;
    	this.openExistingListModel = openExistingListModel;

    	stepInitialise();
    	jbInit();
    	configureTabOrder();
    }

    @Override
	public void validationUpdatedView(ValidationController<?> validationController) {
	}

	@Override
    public void stepInitialise() throws CSRecoverableException {
    	stepUpdateViewState();
    }

    @Override
    public void stepActivate() throws CSRecoverableException {
    	moveModelToScreen();
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
    }
    
    /**
     * Returns the OpenExistingListModel
     * @return OpenExistingListModel
     */
	protected OpenExistingListModel getModel() {
		return openExistingListModel;
	}

	/**
     * Initialise the layout of the panel
     */
    private void jbInit() {
    	this.setLayout(new GridBagLayout());
    	GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
    	
		// Setup a main parent panel with vertical and horizontal scrollbars to prevent
		// resizing of components when the window size is reduced.
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		mainPanel.setPreferredSize(new Dimension(815, 375));
		this.add(scrollPane, gbc);
		
    	CustomButtonPanel buttonPanel = (CustomButtonPanel) parent.getButtonPanel();
		buttonPanel.setShowCancelButton(true);
		buttonPanel.setCancelText("Close");
		buttonPanel.setCancelToolTip("Close");
		
		// Switch button panel settings to place WEST pinned buttons
		buttonPanel.getGridBagConstraints().gridx = 0;
		buttonPanel.getGridBagConstraints().anchor = GridBagConstraints.WEST;
		buttonPanel.getGridBagConstraints().fill = GridBagConstraints.NONE; 
		
		deleteButton = buttonPanel.addButton("ExistingListDelete", false, false);
		deleteButton.addActionListener(new DeleteAction());
		deleteButton.setEnabled(false);
		
		// Switch button panel settings back to placing EAST pinned buttons
		buttonPanel.getGridBagConstraints().gridx = GridBagConstraints.RELATIVE;
		buttonPanel.getGridBagConstraints().fill = GridBagConstraints.HORIZONTAL; 
		buttonPanel.getGridBagConstraints().anchor = GridBagConstraints.EAST;
		
		selectButton = buttonPanel.addButton("ExistingListSelect", true, true);
		selectButton.addActionListener(new SelectAction());
		selectButton.setEnabled(false);

    	JPanel listFilterPanel = initListFilterPanel();    	
    	mainPanel.add(listFilterPanel, gbc);
    	
    	gbc.gridy++;
    	JPanel resultsPanel = initResultsPanel();    	
    	mainPanel.add(resultsPanel, gbc);
    }

    /**
     * Initialises and returns the filter panel
     * @return filter panel
     */
    private JPanel initListFilterPanel() {
    	GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
    	JPanel panel = new JPanel();
    	panel.setBorder(BorderFactory.createTitledBorder("List Filter"));
    	panel.setLayout(new GridBagLayout());

    	JPanel radioButtonPanel = initRadioButtonPanel();
    	panel.add(radioButtonPanel, gbc);
    	
        gbc.gridx++;
        JPanel noOfListsPanel = initNoOfListsPanel();
        panel.add(noOfListsPanel, gbc);
        
        return panel;
    }
    
    /**
     * Initialises and returns the radio button fields in the filter panel
     * @return Radio button panel
     */
    private JPanel initRadioButtonPanel() {
    	GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
    	JPanel panel = new JPanel();
    	panel.setLayout(new GridBagLayout());
    	
    	getListTypeRadioButtonGroup().add(getDailyRb());
    	getListTypeRadioButtonGroup().add(getFirmRb());
    	getListTypeRadioButtonGroup().add(getWarnedRb());
    	getDailyRb().addActionListener(filterChangeActionListener);
    	getFirmRb().addActionListener(filterChangeActionListener);
    	getWarnedRb().addActionListener(filterChangeActionListener);

        panel.add(getDailyRb(), gbc);
    	
    	gbc.gridx++;
    	panel.add(getFirmRb(), gbc);
    	
    	gbc.gridx++;
    	panel.add(getWarnedRb(), gbc);
    	return panel;
    }
        
    /**
     * Initialises and returns the No. Of Lists Panel
     * @return No. Of Lists Panel
     */
    private JPanel initNoOfListsPanel() {
    	GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
    	JPanel panel = new JPanel(new GridBagLayout());
    	    	
    	panel.add(getNoOfListsLabel(), gbc);
    	gbc.gridx++;
    	panel.add(getNoOfListsCombo(), gbc);
    	getNoOfListsCombo().addActionListener(filterChangeActionListener);
    	
    	return panel;
    }
    
    /**
     * Initialises and returns the List Results Table Panel
     * @return List Results Panel
     */
    private JPanel initResultsPanel() {
    	setupTableModel();
		GridBagConstraints gbc = new GridBagConstraints(0, 0, listingsTableModel.getColumnCount(), 1, 1.0, 1.0,
    	GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
    	JPanel panel = new JPanel();
    	panel.setLayout(new GridBagLayout());
    	
		// Add the listingsTable panel elements
    	buildResultTable();
    	
		// Add Table to Scroll Pane
		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(gbc.gridwidth, 280));
		applyTableToScrollPane(listingsTable);
		panel.add(scrollPane, gbc);
        
    	return panel;
    }
    
    /**
     * Returns a reference to the Listings Resource Bundle
     * @param key	Key to search for in the bundle
     * @return	Value for the matching key
     */
    private String getResourceBundle(String key) {
		return XHIBITConstant.getResource(XhibitBundles.Listings, key);
	}
    
    /**
     * Configures the tab order for the screen
     */
    private void configureTabOrder() {
		parent.setFocusTraversalPolicyProvider(true);
		parent.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] {
				dailyRb, 		// List type 'Daily' radio button
				firmRb, 		// List type 'Firm' radio button
				warnedRb, 		// List type 'Warned' radio button
				noOfListsCombo,	// No. Of Lists
				listingsTable, 	// Table of lists
				selectButton, 	// Select button
				deleteButton, 	// Delete button
				parent.getButtonPanel().cancelButton	// Close button
		}, 0));
	}
    
    /**
     * Determines the List Type selected by the radio buttons
     * @return
     */
    private String getTableType() {
    	if (getDailyRb().isSelected()) {
    		return DAILY_TABLE;
    	}
    	return NONDAILY_TABLE;
    }
    
    /**
     * Provided all data is valid, refreshes the data model in the listings table
     */
    private void moveModelToScreen() {
    	// Populate the results table
    	listingsTableModel.setRowCount(0);
    	setupTableModel();
    	listingsTable.setModel(listingsTableModel);
    	
		if (NONDAILY_TABLE.equals(getTableType())) {
			// Set width of version column.
			listingsTable.getColumnModel().getColumn(0).setPreferredWidth(75);
			listingsTable.getColumnModel().getColumn(1).setPreferredWidth(75);
			listingsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
			listingsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
			listingsTable.getColumnModel().getColumn(4).setPreferredWidth(75);
			listingsTable.getColumnModel().getColumn(5).setPreferredWidth(50);
			listingsTable.getColumnModel().getColumn(6).setPreferredWidth(100);
			listingsTable.getColumnModel().getColumn(7).setPreferredWidth(100);
			
		} else {
			// Set width of version column.
			listingsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
			listingsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
			listingsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
			listingsTable.getColumnModel().getColumn(3).setPreferredWidth(50);
			listingsTable.getColumnModel().getColumn(4).setPreferredWidth(25);
			listingsTable.getColumnModel().getColumn(5).setPreferredWidth(100);
			listingsTable.getColumnModel().getColumn(6).setPreferredWidth(100);
		}
    		
		if (!listingsTable.getSelectionModel().isSelectionEmpty()) {
			listingsTable.getSelectionModel().clearSelection();
		}
    }
    
    /**
     * Builds the list results table
     */
    private void buildResultTable() {
    	listingsTable = XTableFactory.getInstance().createDefaultTable(listingsTableModel);
    	
    	TableUtils.setupDefaultsOnJTable(listingsTable);
		listingsTable.setRowSelectionAllowed(true);
		listingsTable.getSelectionModel().addListSelectionListener(tableSelectionListener);
		
    }
    
    /**
     * Apply the table to the scroll pane
     * @param table	Table to be added
     */
    private void applyTableToScrollPane(XTable table) {
    	scrollPane.setViewportView(table);
    }
    
    
    /**
     * Sets up the listing results table data model which is different based upon the
     * listing type selected
     */
    private void setupTableModel() { 
    	String tableType = getTableType();



    	if (DAILY_TABLE.equals(tableType)) {
    		// Setup table for Daily Lists
	    	String[] columnHeaders = new String[] {
					getResourceBundle("openExistingListDate"),
					getResourceBundle("openExistingListStatus"), 
					getResourceBundle("openExistingListPublishDate"),
					getResourceBundle("openExistingListDraftFinal"),
					getResourceBundle("openExistingListVersion"),
					getResourceBundle("openExistingListLastUpdatedBy"),
					getResourceBundle("openExistingListLastUpdatedDate")
				};
	    	    	
	    	listingsTableModel = new DefaultTableModel (new Object[][] {}, columnHeaders) {
				private static final long serialVersionUID = 1L;
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			


    		// Get the data from the database and add them to the data model
    		lists = getListsFromDB();
    		for (ListBasicValue list : lists) {
    			String formattedStartDate = getFormattedDate(list.getListStartDate());
    			String formattedLastUpdateDate = getFormattedDateTime(list.getLastUpdateDate());
    			listingsTableModel.addRow(new Object[] { 
    					formattedStartDate, 
    					getPublishStatusDescription(list),
    					getFormattedDate(list.getPublishDate()),
    					getDraftOrFinal(list.getDraftOrFinal()),
    					list.getListNumber(),
    					list.getLastUpdatedBy(), 
    					formattedLastUpdateDate
    				});
    		}
    	}
    	else if (NONDAILY_TABLE.equals(tableType)) {
    		// Setup table for Firm and Warned Lists
    		String[] columnHeaders = new String[] {
					getResourceBundle("openExistingListStartDate"),
					getResourceBundle("openExistingListEndDate"),
					getResourceBundle("openExistingListStatus"),
					getResourceBundle("openExistingListPublishDate"),
					getResourceBundle("openExistingListDraftFinal"),
					getResourceBundle("openExistingListVersion"),
					getResourceBundle("openExistingListLastUpdatedBy"),
					getResourceBundle("openExistingListLastUpdatedDate")
				};
    		   		
    		listingsTableModel = new DefaultTableModel(new Object[][] {}, columnHeaders){
				private static final long serialVersionUID = 1L;
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};

			// Get the data from the database and add them to the data model
			lists = getListsFromDB();
    		for (ListBasicValue list : lists) {
    			String formattedStartDate = getFormattedDate(list.getListStartDate());
    			String formattedEndDate = getFormattedDate(list.getListEndDate());
    			String formattedLastUpdateDate = getFormattedDateTime(list.getLastUpdateDate());
    			listingsTableModel.addRow(new Object[] { 
    					formattedStartDate,
    					formattedEndDate, 
    					getPublishStatusDescription(list),
    					getFormattedDate(list.getPublishDate()),
    					getDraftOrFinal(list.getDraftOrFinal()),
    					list.getListNumber(),
    					list.getLastUpdatedBy(),
    					formattedLastUpdateDate
    			});
    		}
    	}
    }
    
    /**
     * Returns the Draft or Final description based upon a code supplied
     * @param code The Draft or Final Code
     * @return The corresponding Draft or Final description
     */
    private String getDraftOrFinal(String code)
    {
    	String returnValue = null;
    	if ( code.equals(DRAFTORFINAL_DRAFT_CODE) ) {
    		returnValue = DRAFTORFINAL_DRAFT_DESC;
    	}
    	else if ( code.equals(DRAFTORFINAL_FINAL_CODE) ) {
    		returnValue = DRAFTORFINAL_FINAL_DESC;
    	}
    	return returnValue;
    }
    
    /**
     * Returns the publish status description based upon the database value
     * held on the ListBasicValue item
     * @param list ListBasicValue to interrogate
     * @return description corresponding to the ListBasicValue item
     */
    private String getPublishStatusDescription(ListBasicValue list)
    {
    	String description = null;
    	if ( list.isPublishedSuccess() ) {
    		description = PUBLISH_STATUS_PUBLISHED;
    	}
    	else if ( list.isPublishedFailure() ) {
    		description = PUBLISH_STATUS_PUBLISH_FAILED;
    	}
    	else if ( list.isSaved() ) {
    		description = PUBLISH_STATUS_SAVED;
    	}
    	else if ( list.isDeleted() ) {
    		description = PUBLISH_STATUS_NOT_APPLICABLE;
    	}
    	return description;
    }
    
    /**
     * Utility to format a date into a String 
     * @param date	Date to be converted into a String
     * @return String representing the date
     */
	private String getFormattedDate(Date date) {
		return date != null ? new SimpleDateFormat(DATE_FORMAT).format(date.getTime()) : null;
	}
	
	/**
     * Utility to format a date into a String 
     * @param date	Date to be converted into a String
     * @return String representing the date
     */
	private String getFormattedDateTime(Date date) {
		return date != null ? new SimpleDateFormat(DATETIME_FORMAT).format(date.getTime()) : null;
	}
    	
	/**
	 * Retrieves a collection of matching lists from the database
	 * @return a collection of ListBasicValue items
	 */
	@SuppressWarnings("unchecked")
	private Collection<ListBasicValue> getListsFromDB() {
    	Integer rowNumberLimit = getSelectedNoOfLists();
		Integer courtId = XhibitSingleton.getInstance().getCourtId();
		Collection<ListBasicValue> results = XhibitDelegateHelper.getListingsDelegate().findListsByRowNumber(
				courtId, 
				getSelectedListType().toString(), 
				rowNumberLimit);
    	return results; 
    }
    
	/**
	 * Returns the selected No. Of Lists to display
	 * @return the Radio Button Group
	 */
	private Integer getSelectedNoOfLists() {
		Integer result = null;
		DropdownCodeStringValue selectedItem = (DropdownCodeStringValue) noOfListsCombo.getSelectedItem();
		if (!EMPTY_STRING.equals(selectedItem.getCode())) {
			result = Integer.valueOf(selectedItem.getCode());
		}
		return result;
	}

	/**
	 * Returns a reference to the Radio Button Group, initialising the object if null
	 * @return the Radio Button Group
	 */
    private ButtonGroup getListTypeRadioButtonGroup() {
    	if (listTypeRadioButtonGroup == null) {
    		listTypeRadioButtonGroup = new ButtonGroup();
    	}
    	return listTypeRadioButtonGroup;
    }

    /**
     * Returns a reference to the 'Daily' radio button option, initialising the object if null
     * @return radio button option
     */
    private JRadioButton getDailyRb() {
    	if (dailyRb == null) {
    		dailyRb = new JRadioButton(getResourceBundle("openExistingListDailyRadioLabel"));
    		dailyRb.setSelected(true);
    	}
    	return dailyRb;
    }

    /**
     * Returns a reference to the 'Firm' radio button option, initialising the object if null
     * @return radio button option
     */
    private JRadioButton getFirmRb() {
    	if (firmRb == null) {
    		firmRb = new JRadioButton(getResourceBundle("openExistingListFirmRadioLabel"));
    		firmRb.setSelected(true);
    	}
    	return firmRb;
    }
    
    /**
     * Returns a reference to the 'Warned' radio button option, initialising the object if null
     * @return radio button option
     */
    private JRadioButton getWarnedRb() {
    	if (warnedRb == null) {
    		warnedRb = new JRadioButton(getResourceBundle("openExistingListWarnedRadioLabel"));
    		warnedRb.setSelected(true);
    	}
    	return warnedRb;
    }  
    	
    /**
     * Returns a reference to the label for the No. Of Lists field, initialising the object if null
     * @return field label
     */
    private JLabel getNoOfListsLabel() {
    	if (noOfListsLabel == null) {
    		noOfListsLabel = new JLabel(getResourceBundle("openExistingListNoOfListsLabel"));
    	}
    	return noOfListsLabel;
    }
     
    /**
     * Returns a combobox object for the No. Of Lists, initialising the object if null
     * @return field
     */
    private XComboBox getNoOfListsCombo() {
    	if (noOfListsCombo == null) {
    		noOfListsCombo = new XComboBox();
			setDropdownBoxArray(noOfListsCombo, getNoOfListsArray().toArray());
			noOfListsCombo.setSelectedIndex(1);
    	} 
    	return noOfListsCombo;
    }
    
    /**
     * Returns an array object for the No. Of Lists, initialising the array if null
     * @return field
     */
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
    
    /**
     * Method determines which radio button option has been selected and 
     * returns a String representing the selected List Type.  If none
     * selected, uses a default of 'Daily'
     * @return The selected List Type
     */
    private ListTypeEnum getSelectedListType()
    {
    	ListTypeEnum returnValue = ListTypeEnum.Daily;
    	if ( getWarnedRb().isSelected() )
    	{
    		returnValue = ListTypeEnum.Warned;
    	}
    	else if ( getFirmRb().isSelected() )
    	{
    		returnValue = ListTypeEnum.Firm;
    	}
    	else if ( getDailyRb().isSelected() )
    	{
    		returnValue = ListTypeEnum.Daily;
    	}
    	return returnValue;
    }
    
	/**
	 * Set the array and if the array is empty disable the dropdown box
	 */
	private void setDropdownBoxArray(XComboBox comboBox, final Object[] arrayItems) {
		comboBox.setModel(new DefaultComboBoxModel(arrayItems));
		if (comboBox.getSelectedItem() != null) {
			comboBox.setRenderer(new ListingDropdownBoxCellRenderer());
			comboBox.enableAutoSelect();
		} else {
			comboBox.setEnabled(false);
		}
	}
	
    /**
     * TableSelectionListener class to handle selection of rows in the table
     */
	private class TableSelectionListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			
			ListSelectionModel selectionModel = (ListSelectionModel) e.getSource();
			Integer selectedRow = selectionModel.getMinSelectionIndex();
			
			boolean isRowSelected = !Integer.valueOf(-1).equals(selectedRow); 
			boolean isSelectAllowed = isRowSelected;
			
			
			selectButton.setEnabled(isSelectAllowed);
			deleteButton.setEnabled(true);
		}
	};
	
	/**
     * FilterChangeActionListener class to handle change of filter items
     */
	private class FilterChangeActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			moveModelToScreen();
		}
	};
	
	/**
	 * The Select Action.
	 */
	private class SelectAction extends XAction {

		private static final long serialVersionUID = 1L;
		private final XhibitApplicationController xac = (XhibitApplicationController)parent.getParentFrame();

		public SelectAction() {
			populateFromBundle("openDeleteExistingListSelect");
		}

		/**
		 * The action performed determines which list type is selected and invoke the
		 * Listings screen in the correct mode.
		 */
		public void xActionPerformed(ActionEvent actionEvent) throws CSRecoverableException {
			
			int selectedRow = listingsTable.getSelectedRow();
			if ( selectedRow != -1 )
			{
				ListBasicValue currentlySelectedList = ((List<ListBasicValue>) lists).get(selectedRow);
				if ( currentlySelectedList != null )
				{
					// Determine the type of list and open the list in the Listings screen
					int listId = currentlySelectedList.getListId();
					if ( getSelectedListType().equals(ListTypeEnum.Daily) ) {
						openDailyList(listId);
					}
					else if ( getSelectedListType().equals(ListTypeEnum.Firm) ) {
						openFirmList(listId);
					}
					else if ( getSelectedListType().equals(ListTypeEnum.Warned) ) {
						openWarnedList(listId);
					}
					else {
						openDailyList(listId);
					}
				}
			}
		}
		
		/**
		 * Open Listing Screen for a Daily list
		 * @param openListId	List Id
		 * @throws CSRecoverableException
		 */
	    private void openDailyList(Integer openListId) throws CSRecoverableException {
	    	DailyListModel model = new DailyListModel(xac);
	    	model.openList(openListId);
	    	final DailyListPanel dailyListPanel = new DailyListPanel(parent, model);
	    	dailyListPanel.showPanel();
	    }
	    
	    /**
		 * Open Listing Screen for a Firm list
		 * @param openListId	List Id
		 * @throws CSRecoverableException
		 */
	    private void openFirmList(Integer openListId) throws CSRecoverableException {
	    	FirmListModel model = new FirmListModel(xac);
	    	model.openList(openListId);
	    	FirmListPanel firmListPanel = new FirmListPanel(parent, model);
	    	firmListPanel.showPanel();
	    }
	    
	    /**
		 * Open Listing Screen for a Warned list
		 * @param openListId	List Id
		 * @throws CSRecoverableException
		 */
	    private void openWarnedList(Integer openListId) throws CSRecoverableException {
	    	WarnedListModel model = new WarnedListModel(xac);
	    	model.openList(openListId);
	    	WarnedListPanel warnedListPanel = new WarnedListPanel(parent, model);
	    	warnedListPanel.showPanel();
	    }
	}

	/**
	 * The Delete Action.
	 */
	private class DeleteAction extends XAction {

		private static final long serialVersionUID = 870460344113126945L;

		public DeleteAction() {
			populateFromBundle("openDeleteExistingListDelete");
		}

		/**
		 * Action performed prompts user for confirmation of the delete before invoking a stored
		 * procedure to perform the logical delete and then refreshes the data in the table.
		 */
		public void xActionPerformed(ActionEvent actionEvent) throws CSRecoverableException {
			
			int selectedRow = listingsTable.getSelectedRow();
			if ( selectedRow != -1 )
			{
				ListBasicValue currentlySelectedList = ((List<ListBasicValue>) lists).get(selectedRow);
				if ( currentlySelectedList != null )
				{
					
					//Validate delete will show user error messages if unable to delete.
					//If no errors show user confirm screen.
					if (validateDelete(currentlySelectedList)) {

					
						int doDelete = JOptionPane.showOptionDialog(
								null,
								getResourceBundle("openExistingListDeleteMessage"),
								getResourceBundle("openExistingListDeleteTitle"), 
								JOptionPane.YES_NO_OPTION, 
								JOptionPane.INFORMATION_MESSAGE,
								null,
								new String[]{"    Yes    ", "     No     "},
								"     No     ");
						if (doDelete == JOptionPane.YES_OPTION) {
							XhibitDelegateHelper.getListingsDelegate().deleteList(currentlySelectedList.getListId());
							moveModelToScreen();
						}
					}
				}
			}
		}
		
		private boolean validateDelete(final ListBasicValue currentlySelectedList) {

			return dailyRb.isSelected() ? 
					validateDeleteDailyList(currentlySelectedList)
					: validateDeleteWarmFirmList(currentlySelectedList);
		}
		
		private boolean validateDeleteWarmFirmList(final ListBasicValue currentlySelectedList) {
			String message = getResourceBundle("openExistingListDeleteFirmWarnedError");
			final String title = getResourceBundle("openExistingListDeleteErrorTitle");
			final Calendar today = Calendar.getInstance();
			
			final Calendar listDate = Calendar.getInstance();
			listDate.setTime(currentlySelectedList.getListEndDate());
			
			if (listDate.after(today) && currentlySelectedList.isPublished()) {
				JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
				return false;
			}
			return true;
			
		}
		
		private boolean validateDeleteDailyList(final ListBasicValue currentlySelectedList) {
			String message = getResourceBundle("openExistingListDeleteDailyTodayOrPastError");
			final String title = getResourceBundle("openExistingListDeleteErrorTitle");
			final Calendar today = Calendar.getInstance();
			
			final Calendar listDate = Calendar.getInstance();
			listDate.setTime(currentlySelectedList.getListStartDate());
			

			if (listDate.before(today)) {
				JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			//FS says check publishing date is null but isPublished should be the same.
			else if(currentlySelectedList.isPublished()) {
				message = getResourceBundle("openExistingListDeleteDailyPublishedError");
				JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			return true;
		}
	}
}