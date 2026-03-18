/**
 * <p>
 * Title: ListResultsPanel
 * </p>
 * <p>
 * Description: ListResultsPanel represents the panel for the List Results screen
 * CTX-1314
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Chris Vincent
 * @version 1.0
 */

package uk.gov.courtservice.xhibit.client.listings.listresults;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListingResultsInformation;
import uk.gov.courtservice.xhibit.client.casemanagement.DropdownBoxCellRender;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.listings.casesummary.CaseSummaryDialog;
import uk.gov.courtservice.xhibit.client.listings.casesummary.CaseSummaryModel;
import uk.gov.courtservice.xhibit.client.listings.details.CaseListingDetailDialog;
import uk.gov.courtservice.xhibit.client.listings.details.CaseListingDetailModel;
import uk.gov.courtservice.xhibit.client.listings.list.common.TableUtils;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDatePanelWithEvent;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.listeners.PopupListener;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.util.table.model.sortable.XSortableTableModel;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class ListResultsPanel extends XPanel implements ValidationListener {
	
	private static final long serialVersionUID = 1L;
	
	private static final int COURTSITE_ALL_ID = 0;
	private static final int COURTROOM_ALL_ID = 0;
	private static final int COURTROOM_FLOATER_ID = -1;
	
	private ListResultsModel listResultsModel;
	private JLabel listDateLabel = null;
	private JScrollPane scrollPane;
	private XDatePanelWithEvent listDate = null;	
	private ArrayList<CourtSiteBasicValue> courtSiteArray;
	private XComboBox courtSite = null;
	private ArrayList<CourtRoomBasicValue> courtRoomArray;
	private XComboBox courtRoom = null;
	private XTable table;

    private XDialog parent;
    private JPopupMenu tsPopup = null;
	
    /**
     * Constructor
     * @param parent	Parent Dialog object
     * @param listResultsModel	Model object
     * @throws CSRecoverableException
     */
    public ListResultsPanel(XDialog parent, ListResultsModel listResultsModel) throws CSRecoverableException {
    	this.parent = parent;
    	this.listResultsModel = listResultsModel;

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
     * Returns the ListResultsModel
     * @return ListResultsModel
     */
	protected ListResultsModel getModel() {
		return listResultsModel;
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
		mainPanel.setPreferredSize(new Dimension(1150, 350));
		this.add(scrollPane, gbc);
    	
		gbc.weighty = 0.1;
    	JPanel listFilterPanel = initListFilterPanel();    	
    	mainPanel.add(listFilterPanel, gbc);
    	
    	gbc.weighty = 0.9;
    	gbc.gridy++;
    	JPanel resultsPanel = initResultsPanel();    	
    	mainPanel.add(resultsPanel, gbc);
    }

    /**
     * Initialises the List Results screen filter panel
     * @return	Panel to be added to the parent
     */
    private JPanel initListFilterPanel() {
    	GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
    	JPanel panel = new JPanel();
    	panel.setLayout(new GridBagLayout());
    	
    	// List Date label
		gbc.weightx = 0.05;
		panel.add(getListDateLabel(), gbc);
		
		// List Date component
		gbc.gridx++;
		gbc.weightx = 0.15;
		panel.add(getListDate(), gbc);
		
		// Court Site label
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx++;
		gbc.weightx = 0.05;
		panel.add(getCourtSiteLabel(), gbc);
		
		// Court Site Component
		gbc.gridx++;
		gbc.weightx = 0.15;
		panel.add(getCourtSite(), gbc);

		// Court Room label
		gbc.gridx++;
		gbc.weightx = 0.05;
		panel.add(getCourtRoomLabel(), gbc);
		
		// Court Room component
		gbc.gridx++;
		gbc.weightx = 0.15;
		panel.add(getCourtRoom(), gbc);
		
		// Blank space
		gbc.gridx++;
		gbc.weightx = 0.4;
		panel.add(Box.createRigidArea(new Dimension(100, 0)), gbc);
        
        return panel;
    }
    
    /**
     * Initialises the Results table panel
     * @return	Panel to be added to the parent
     */
    private JPanel initResultsPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 7, 1, 1.0, 1.0,
    	GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
    	JPanel panel = new JPanel();
    	panel.setLayout(new GridBagLayout());
    	
		// Add the table panel elements
    	buildResultTable();

		// Add Table to Scroll Pane
		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(gbc.gridwidth, 100));
		scrollPane.setViewportView(table);
		panel.add(scrollPane, gbc);
        
    	return panel;
    }
    
    /**
     * Builds the Results table, specifying column widths and rendering rules
     * @param tableModel	DefaultTableModel to configure
     */
    private void buildResultTable() {
    	table = XTableFactory.getInstance().createDefaultTable( new ListResultsTableModel());
    	table.makeSortable();
    	
    	refreshDataModel();
    	
    	TableUtils.setupDefaultsOnJTable(table);
    	table.getCellSelectionEnabled();
		
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(6).setPreferredWidth(350);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		
		JPopupMenu jp = getPopupMenu();
        if (jp != null) {
        	table.add(jp);
            MouseListener popupListener = new ListResultsPopupListener(jp);
            table.addMouseListener(popupListener);
        }
    }
    
    /**
     * Generates the Popup Menu invoked when the user right clicks a row in the table.  The table
     * is initialised when first called
     * @return The JPopupMenu
     */
    private JPopupMenu getPopupMenu() {
        if (tsPopup == null) {
        	tsPopup = new JPopupMenu();
        	JMenuItem menuItemCaseSummary = new JMenuItem( new CaseSummaryMenuItemAction() );
        	JMenuItem menuItemCaseListing = new JMenuItem( new CaseListingMenuItemAction() );
        	tsPopup.add(menuItemCaseSummary);
        	tsPopup.add(menuItemCaseListing);
        }
        return tsPopup;
    }
    
    /**
     * Refreshes the data model provided the fields are valid
     */
    private void refreshDataModel()
    {
    	// Check to ensure that the List Date is not in the future
    	if ( isValidListDate() ) {
	    	// Retrieve case details from the database and populate the table
    		table.clearSelection();
    		XHIBITTableModelInterface tableModel = (XHIBITTableModelInterface) table.getModel();
	    	tableModel.setData( getCasesFromDB() );
    	}
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
				listDate, 
				courtSite, 
				courtRoom, 
				table, 
				parent.getButtonPanel().cancelButton
		}, 0));
	}
    
    /**
     * Invokes a stored procedure to query the database for matching case on list
     * records based upon the search criteria entered on screen.
     * @return	A collection of ListingResultsInformation objects
     */
    @SuppressWarnings("unchecked")
	private Collection<ListingResultsInformation> getCasesFromDB() {
    	Collection<ListingResultsInformation> results = null;
    	try {
			Date listDate = getListDate().getDate().getTime();
			Integer courtSiteId = getSelectedCourtSiteId();
			Integer courtRoomId = getSelectedCourtRoomId();
			boolean isAllCourtSites = ( courtSiteId == COURTSITE_ALL_ID ) ? true : false;
			boolean isAllCourtRooms = ( courtRoomId == COURTROOM_ALL_ID ) ? true : false;
			boolean isFloaterCourtRooms = ( courtRoomId == COURTROOM_FLOATER_ID ) ? true : false;
			Integer courtId = XhibitSingleton.getInstance().getCourtId();
			
			results = XhibitDelegateHelper.getListingsDelegate().findCasesOnListByDateSiteAndRoom(
					listDate, 
					courtSiteId, 
					courtRoomId,
					courtId,
					isAllCourtSites,
					isAllCourtRooms,
					isFloaterCourtRooms);
					
		} catch (CSRecoverableException e) {
			XHIBITConstant.handleError(e);
		}
    	return results; 
    }
    
    /**
     * Set the array and if the array is empty disable the combo box
     * @param comboBox	Combo Box object to set up
     * @param arrayItems	Array to populate the combo box with
     */
    private void setComboBoxArray(XComboBox comboBox, final Object[] arrayItems) 
    {
    	comboBox.setModel(new DefaultComboBoxModel(arrayItems));
		if (comboBox.getSelectedItem() != null) {
	    	comboBox.setRenderer(new DropdownBoxCellRender());
			comboBox.enableAutoSelect();
		} else {
			comboBox.setEnabled(false);
		}
    }
    
    /**
     * Returns a List of reference data items for the Court Site combo box
     * @return	List of reference data items
     */
	private ArrayList<CourtSiteBasicValue> getCourtSiteArray() {
		ArrayList<CourtSiteBasicValue> results = ListingDropdownPopulation.getCourtSites();
		
		// Option of 'All Sites' at end of list
		CourtSiteBasicValue all = new CourtSiteBasicValue(COURTSITE_ALL_ID,1);
		all.setCourtSiteName(getResourceBundle("listResultsCourtSiteAllSites"));
		all.setDisplayName(getResourceBundle("listResultsCourtSiteAllSites"));
		results.add(all);
	    return results;
	}
	
	/**
     * Returns a List of reference data items for the Court Room combo box
     * @return	List of reference data items
     */
	private ArrayList<CourtRoomBasicValue> getCourtRoomArray() {
		ArrayList<CourtRoomBasicValue> results = ListingDropdownPopulation.getCourtRooms(
				getSelectedCourtSiteId());
		
		// Option of 'All' at top of list
		CourtRoomBasicValue all = new CourtRoomBasicValue(COURTROOM_ALL_ID,1);
		all.setCourtRoomName(getResourceBundle("listResultsCourtRoomAll"));
		all.setDisplayName(getResourceBundle("listResultsCourtRoomAll"));
		results.add(0, all);
		
		// 'Option of 'Floater Cases' at end of list
		CourtRoomBasicValue floater = new CourtRoomBasicValue(COURTROOM_FLOATER_ID,1);
		floater.setCourtRoomName(getResourceBundle("listResultsCourtRoomFloater"));
		floater.setDisplayName(getResourceBundle("listResultsCourtRoomFloater"));
		results.add(floater);

	    return results;
	}
    
	/**
	 * Returns the label for the List Date field from the bundle
	 * @return	Field label
	 */
    private JLabel getListDateLabel() {
    	if ( listDateLabel == null ){
    		listDateLabel = new JLabel(getResourceBundle("listResultsLabelListDate"));
    	}
		return listDateLabel;
	}
	
    /**
	 * Returns the label for the Court Site field from the bundle
	 * @return	Field label
	 */
	private JLabel getCourtSiteLabel() {
		return new JLabel(getResourceBundle("listResultsLabelCourtSite"));
	}
	
	/**
	 * Returns the label for the Court Room field from the bundle
	 * @return	Field label
	 */
	private JLabel getCourtRoomLabel() {
		return new JLabel(getResourceBundle("listResultsLabelCourtRoom"));
	}
	
	/**
	 * Return calendar if date valid, or null if empty or invalid.
	 * Use with hasDate to know if safe to call to get date.
	 * 
	 * @param component
	 * @return
	 */
	protected Calendar getDateAsCalendar(XDatePanel component) {
		Calendar calendar;
		try {
			calendar = component.getDate();
		} catch (CSValidationException e) {
			calendar = null;
		}
		return calendar;
	}
    
	/**
	 * Returns the List Date field object
	 * @return	XDatePanel object
	 */
	private XDatePanel getListDate() {
		if (listDate == null) {
			Calendar date = DateTimeUtilities.stripTimeToCalendar(new Date());
			getModel().setListDate(date.getTime());
			listDate = new XDatePanelWithEvent(this, date, false) {
        		
				private static final long serialVersionUID = 1L;

				@Override
				protected void fireEvent() {
					try {
						// Only fire the event when the date changes
						if ( getDateAsCalendar(listDate) != null ) {
							Date selectedDate = getModel().getDateWithoutTime(listDate.getDateComponent().getValue());
							if ( getModel().getListDate() == null || !getModel().getListDate().equals(selectedDate)) {
								// Keep the model date in sync
								getModel().setListDate(selectedDate);
								// Refresh Data
								refreshDataModel();
							}
						}
						else
						{
							getModel().setListDate(null);
						}
					} 
					catch (ParseException ex) {
						XHIBITConstant.handleError(ex);
					}
				}
			};
		}
		return listDate;
	}
	
	/**
	 * Determines if the List Date is populated with a valid date (cannot be a date in the future)
	 * @return true if list date is valid, else false
	 */
	private boolean isValidListDate() {
		boolean isValidDate = false;
		Calendar cld = getDateAsCalendar(listDate);
		if ( null != cld ) {
			if (cld.after(DateTimeUtilities.stripTimeToCalendar(new Date()))) {
				XMessageBox.alert(parent,
						  XHIBITConstant.getResource(XhibitBundles.XhibitConstant,"exception.validation.title"), 
						  true,
						  XMessageBox.ICONERROR, 
						  XHIBITConstant.getResource(XhibitBundles.ErrorText,"listings.listResults.futureListingDate"), 
						  XMessageBox.OK_ONLY, 
						  XMessageBox.DEFAULTOK);
			}
			else {
				isValidDate = true;
			}
		}
		return isValidDate;
	}
	
	/**
	 * Returns the Court Site field object
	 * @return	XComboBox object
	 */
	private XComboBox getCourtSite() {
		if (courtSite == null) {
			courtSite = new XComboBox();
			if (courtSiteArray == null) {
				courtSiteArray = getCourtSiteArray();
			}
			setComboBoxArray(courtSite, courtSiteArray.toArray());
			
	    	ActionListener dropdownListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					Integer courtSiteId = getSelectedCourtSiteId();
					if ( courtSiteId == COURTSITE_ALL_ID ) {
						// If 'All Sites' is selected, disable the Court Room dropdown and set it to 'All'
						getCourtRoom().setEnabled(false);
						getCourtRoom().setSelectedItemById(COURTROOM_ALL_ID);
					}
					else {
						// A specific court site has been selected
						// Re-generate the list of court rooms in the dropdown and enable the field
						courtRoomArray = getCourtRoomArray();
						setComboBoxArray(courtRoom, courtRoomArray.toArray());
						getCourtRoom().setEnabled(true);
					}
					
					// Refresh Data
					refreshDataModel();
				}
			};
			courtSite.addActionListener(dropdownListener);
		}
		return courtSite;
	}
	
	/**
	 * Returns the court site id for the currently selected court site
	 * @return court site id
	 */
	private Integer getSelectedCourtSiteId() {
		Integer selectedCourtSiteId = 0;
		if (courtSite != null) {
			selectedCourtSiteId = ((CourtSiteBasicValue) courtSite.getSelectedItem()).getId();
		}
		return selectedCourtSiteId;
	}
	
	/**
	 * Returns the Court Room field object
	 * @return	XComboBox object
	 */
	private XComboBox getCourtRoom() {
		if (courtRoom == null) {
			courtRoom = new XComboBox();
			if (courtRoomArray == null) {
				courtRoomArray = getCourtRoomArray();
			}
			setComboBoxArray(courtRoom, courtRoomArray.toArray());
			
			ActionListener dropdownListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Refresh Data Model if not disabled
					if ( courtRoom.isEnabled() ) {
						refreshDataModel();
					}
				}
			};
			courtRoom.addActionListener(dropdownListener);
		}
		return courtRoom;
	}
	
	/**
	 * Returns the court site id for the currently selected court site
	 * @return court site id
	 */
	private Integer getSelectedCourtRoomId() {
		Integer selectedCourtRoomId = 0;
		if (courtRoom != null) {
			selectedCourtRoomId = ((CourtRoomBasicValue) courtRoom.getSelectedItem()).getId();
		}
		return selectedCourtRoomId;
	}
	
	/**
	 * Private class for the popup listener associated with the List Results
	 * table.
	 */
	public class ListResultsPopupListener extends PopupListener {
	    private JPopupMenu thisPopup = null;

	    public ListResultsPopupListener(JPopupMenu pMenu) {
	        super(pMenu);
	    	thisPopup = pMenu;
	    }

	    public void mousePressed(MouseEvent e) {
	        maybeShowPopup(e);
	    }

	    public void mouseReleased(MouseEvent e) {
	        maybeShowPopup(e);
	    }

	    protected void maybeShowPopup(MouseEvent e) {
	    	// Determine where in the table the user right clicked and select that row
	    	int row = table.rowAtPoint(e.getPoint());
            table.setRowSelectionInterval(row, row);
	    	
	    	if (e.isPopupTrigger()) {
	    		// If right click then launch popup
	            if (thisPopup != null) {
	                thisPopup.show(e.getComponent(), e.getX(), e.getY());
	            }
	        }
	    }
	}
	
	/**
	 * Private class to handle the popup menu item for Case Summary
	 */
	private class CaseSummaryMenuItemAction extends XAction {

        private static final long serialVersionUID = 1L;

        public CaseSummaryMenuItemAction() {
            populateFromBundle("ListResultsCaseSummary");
        }

        public void xActionPerformed(ActionEvent ae) throws Exception {
        	ListingResultsInformation valueObject = (ListingResultsInformation) (((XSortableTableModel) table.getModel())
                    .getDataAt(table.getSelectedRow()));
        	
			CaseSummaryModel model = new CaseSummaryModel(valueObject.getCaseId());
			CaseSummaryDialog dialog;
			dialog = new CaseSummaryDialog(parent, model);
			dialog.setVisible(true);
        }		
    } 
	
	/**
	 * Private class to handle the popup menu item for Case Listings
	 */
	private class CaseListingMenuItemAction extends XAction {

        private static final long serialVersionUID = 1L;

        public CaseListingMenuItemAction() {
            populateFromBundle("ListResultsCaseListing");
        }

        public void xActionPerformed(ActionEvent ae) throws Exception {
        	ListingResultsInformation valueObject = (ListingResultsInformation) (((XSortableTableModel) table.getModel())
                    .getDataAt(table.getSelectedRow()));
        	
        	CaseListingDetailModel model = new CaseListingDetailModel((XhibitApplicationController) parent.getParentFrame(), 
        			(Integer)valueObject.getCaseId(), 
        			(XhibitSingleton.getInstance().getCourtId()));
        	CaseListingDetailDialog dialog;
			dialog = new CaseListingDetailDialog(parent, model);
			dialog.setVisible(true);
        }		
    }
	

}