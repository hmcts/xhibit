package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefChamberComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefLegalRepresentativeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class ChamberAndAdvocateDetailsPanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;
	private static final int COUNSEL_TABLE_COUNSEL_NAME = 0;
	private static final int COUNSEL_TABLE_IS_DELETED = 1;
	private static final int COUNSEL_TABLE_CHAMBER_REF_NO = 2;
	private static final int COUNSEL_TABLE_CHAMBER_FIRM_NAME = 3;
	private static final int COUNSEL_TABLE_CHAMBER_ADDRESS = 4;
	private static final int CHAMBER_TABLE_IS_DELETED = 0;
	private static final int CHAMBER_TABLE_CHAMBER_REF_NO = 1;
	private static final int CHAMBER_TABLE_CHAMBER_FIRM_NAME = 2;
	private static final int CHAMBER_TABLE_CHAMBER_ADDRESS = 3;
	private static final int CHAMBER_TABLE_TOWN = 4;
	private static final int CHAMBER_TABLE_POSTCODE = 5;
	private static final int CHAMBER_TABLE_CLERK_NAME = 6;
	private ChamberAndAdvocateDetailsModel model;
	private ChamberAndAdvocateDetailsDialog parentDialog;
	
	private boolean refreshSearchResults;
		
	/**
	 * Fields on Search panel.
	 */
	private JLabel lblCounselSurname = null;
	private JLabel lblCounselInitials = null;
	private JLabel lblChamberRefNo = null;
	private JLabel lblChamberName = null;
	private XTextField txtCounselSurname = null;
	private XTextField txtCounselInitials = null;
	private XTextField txtChamberRefNo = null;
	private XTextField txtChamberName = null;	
	private JLabel lblCounselSurnameInvalidEntry = null;
	private JLabel lblCounselInitialsInvalidEntry = null;
	private JLabel lblChamberRefNoInvalidEntry = null;
	private JLabel lblChamberNameInvalidEntry = null;
	
	/**
	 * Fields on Counsel Button panel.
	 */
	private JButton btnSearchCounsel = null;
	
	/**
	 * Fields on Chamber Button panel.
	 */
	private JButton btnAddChamber = null;
	private JButton btnSearchChamber = null;
	
	/**
	 * Fields on Counsel Search Results panel.
	 */
	private JTable counselSearchResultsTable = null;
	private JScrollPane counselSearchResultsTableScrollPane = null;
	
	/**
	 * Fields on Chamber Search Results panel.
	 */
	private JTable chamberSearchResultsTable = null;
	private JScrollPane chamberSearchResultsTableScrollPane = null;
	
	/**
	 * Fields on Button panel.
	 */
	private JButton btnChamberDetails = null;
	private JButton btnCounselDetails = null;
	private JButton btnUpdateChamberRef = null;
	private JButton btnCancel = null;
	
	/**
	 * JPanels.
	 */
	private JPanel mainPanel = null;
	private JPanel searchPanel = null;
	private JPanel counselButtonPanel = null;
	private JPanel chamberButtonPanel = null;
	private JPanel counselSearchResultsPanel = null;
	private JPanel chamberSearchResultsPanel = null;
	
	private static final Logger log = CSServices.getLogger(ChamberAndAdvocateDetailsPanel.class);	

	public ChamberAndAdvocateDetailsPanel(ChamberAndAdvocateDetailsDialog parentDialog,
			ChamberAndAdvocateDetailsModel model) throws CSRecoverableException {
		this.model = model;
		this.parentDialog = parentDialog;
		stepInitialise();
		jbInit();
	}
	
	public void setRefreshSearchResults(boolean refresh, boolean counselAdded) {
		boolean setRefresh = refresh;
		// If no updates made, but a Counsel has been added from the Chamber Details screen, 
		// need to refresh the search results provided the Counsel Search Results table is visible
		if ( !refresh && counselAdded && counselSearchResultsPanel.isVisible() ) {
			setRefresh = true;
		}
		refreshSearchResults = setRefresh;
	}
	
	private boolean getRefreshSearchResults() {
		return refreshSearchResults;
	}
	
	/**
	 * Initialises the look and feel of the panel.	
	 */
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(920, 600));	
		GridBagConstraints gbc = getGridBagLayout();
		
		mainPanel = getMainPanel();
		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weighty = 0.05;
		gbc.weightx = 0.95;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		mainPanel.add(getSearchPanel(), gbc);
		
		gbc.gridy++;
		gbc.weighty = 0.9;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill=GridBagConstraints.BOTH;
		mainPanel.add(getCounselSearchResultsPanel(), gbc);
		
		//Get the chamber search results panel but hide it until we do a chamber search
		mainPanel.add(getChamberSearchResultsPanel(), gbc);
		chamberSearchResultsPanel.setVisible(false);
		
		
		CustomButtonPanel buttonPanel = (CustomButtonPanel) this.parentDialog.getButtonPanel();
				
		btnChamberDetails = buttonPanel.addButton("ChamberAndAdvocateDetailsChamberDetails", false, false);
		btnCounselDetails = buttonPanel.addButton("ChamberAndAdvocateDetailsCounselDetails", false, false);
		btnUpdateChamberRef = buttonPanel.addButton("ChamberAndAdvocateDetailsUpdateChamberRef", false, false);
		btnCancel = buttonPanel.addButton("ChamberAndAdvocateDetailsCancel", true, false);
		
		btnSearchCounsel.setEnabled(false);
		btnSearchChamber.setEnabled(false);
		disableCounselSearchResultsPanel();
		
		configureTabOrder();
		
		if(model.getReadOnly()) {
			btnAddChamber.setEnabled(false);
		}
	}
	
	/**
	 * Returns a panel which contains the main panel
	 * @return
	 */
	public JPanel getMainPanel() {
		GridBagConstraints gbc = getGridBagLayout();
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setBorder(BorderFactory.createEmptyBorder());
			mainPanel.setPreferredSize(new Dimension(800, 450));
			this.add(scrollPane, gbc);
		}

		return mainPanel;
	}
	
	/**
	 * Returns a panel which contains the Chamber/Counsel Search fields
	 * @return
	 */
	public JPanel getSearchPanel() {
		GridBagConstraints gbc = getGridBagLayout();
		if(searchPanel == null) {
			searchPanel = new JPanel(new GridBagLayout());
			searchPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"ChamberAndAdvocateDetails.searchPanelTitle")));			
			gbc.anchor = GridBagConstraints.WEST;
			
			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			lblCounselSurname = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"ChamberAndAdvocateDetails.counselSurnameLabel"));
			searchPanel.add(lblCounselSurname, gbc);
			gbc.gridy += 2;
			
			lblCounselInitials = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"ChamberAndAdvocateDetails.counselInitialsLabel"));
			searchPanel.add(lblCounselInitials, gbc);
			
			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 1;

			searchPanel.add(getCounselSurname(), gbc);
			gbc.gridy += 2;
			
			searchPanel.add(getCounselInitials(), gbc);
			gbc.gridy += 2;
			
			gbc.fill = GridBagConstraints.NONE;
			searchPanel.add(getCounselButtonPanel(), gbc);
			
			/* Top Of Column */
			gbc.gridx = 1;
			gbc.gridy = 0;

			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;
			gbc.fill = GridBagConstraints.HORIZONTAL;

			searchPanel.add(lblCounselSurnameInvalidEntry, gbc);
			gbc.gridy += 2;
			
			searchPanel.add(lblCounselInitialsInvalidEntry, gbc);
			
			/* Next Column */
			gbc.gridy = 1;
			gbc.gridx++;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;
			
			lblChamberRefNo = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"ChamberAndAdvocateDetails.chamberRefNoLabel"));
			searchPanel.add(lblChamberRefNo, gbc);
			gbc.gridy += 2;
			
			lblChamberName = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"ChamberAndAdvocateDetails.chamberNameLabel"));
			searchPanel.add(lblChamberName, gbc);
			
			/* Top of Column */
			gbc.gridy = 1;
			gbc.gridx++;

			searchPanel.add(getChamberRefNo(), gbc);
			gbc.gridy += 2;
			
			searchPanel.add(getChamberName(), gbc);
			gbc.gridy += 2;
			
			gbc.fill = GridBagConstraints.NONE;
			searchPanel.add(getChamberButtonPanel(), gbc);
			
			/* Top Of Column */
			gbc.gridy = 0;

			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;
			gbc.fill = GridBagConstraints.HORIZONTAL;

			searchPanel.add(lblChamberRefNoInvalidEntry, gbc);
			gbc.gridy += 2;
			
			searchPanel.add(lblChamberNameInvalidEntry, gbc);
		}
		
		return searchPanel;		
	}
	
	/**
	 * Returns a panel which contains the Search Counsel button
	 * @return
	 */
	public JPanel getCounselButtonPanel() {
		GridBagConstraints gbc = getGridBagLayout();
		if(counselButtonPanel == null) {
			counselButtonPanel = new JPanel(new GridBagLayout());
			
			gbc.insets = XHIBITConstant.containerInsets;
			gbc.fill = GridBagConstraints.NONE;
			counselButtonPanel.add(getSearchCounselButton(), gbc);
			
			gbc.gridx++;
			counselButtonPanel.add(Box.createRigidArea(btnSearchCounsel.getPreferredSize()));
		}
		
		return counselButtonPanel;
	}
	
	/**
	 * Returns a panel which contains the Add Chamber and Search Chamber buttons
	 * @return
	 */
	public JPanel getChamberButtonPanel() {
		GridBagConstraints gbc = getGridBagLayout();
		if(chamberButtonPanel == null) {
			chamberButtonPanel = new JPanel(new GridBagLayout());
			
			gbc.insets = XHIBITConstant.containerInsets;
			gbc.fill = GridBagConstraints.NONE;
			chamberButtonPanel.add(getAddChamberButton(), gbc);
			
			gbc.gridx++;
			gbc.fill = GridBagConstraints.NONE;
			chamberButtonPanel.add(getSearchChamberButton(), gbc);
		}
		
		return chamberButtonPanel;
	}
	
	/**
	 * Returns a panel which contains the Counsel Search Results
	 * @return
	 */
	public JPanel getCounselSearchResultsPanel() {
		GridBagConstraints gbc = getGridBagLayout();
		if(counselSearchResultsPanel == null) {
			counselSearchResultsPanel = new JPanel(new GridBagLayout());
			counselSearchResultsPanel.setBorder(
					BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
							"ChamberAndAdvocateDetails.counselSearchResultsPanelTitle")));
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.WEST;
			
			counselSearchResultsTableScrollPane = new JScrollPane();
			counselSearchResultsTableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);			
			counselSearchResultsPanel.add(counselSearchResultsTableScrollPane, gbc);				
			counselSearchResultsTableScrollPane.setViewportView(getCounselSearchResultsTable());
		}
		
		return counselSearchResultsPanel;		
	}
	
	/**
	 * Returns the table that will hold the Counsel search results table
	 * @return
	 */
	public JTable getCounselSearchResultsTable() {
		if (counselSearchResultsTable == null) {
			counselSearchResultsTable = new JTable();
			counselSearchResultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			counselSearchResultsTable.setModel(new DefaultTableModel(new Object[][] {},
					new String[] {
							XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
									"ChamberAndAdvocateDetails.counselSearchResultsTableCounselNameColumn"),
							XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
									"ChamberAndAdvocateDetails.counselSearchResultsTableCounselDeletedColumn"),
							XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
									"ChamberAndAdvocateDetails.counselSearchResultsTableChamberRefNoColumn"),
							XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
									"ChamberAndAdvocateDetails.counselSearchResultsTableChamberFirmNameColumn"),
							XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
									"ChamberAndAdvocateDetails.counselSearchResultsTableChamberAddressColumn") }) {
				/**
										 * 
										 */
				private static final long serialVersionUID = 1L;
				// Only Chamber Ref No will be editable when not in read-only mode
				boolean[] columnEditables = new boolean[] { false, false, (model.getReadOnly() ? false : true), false,
						false };

				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
				
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					switch (columnIndex) {
					case COUNSEL_TABLE_COUNSEL_NAME:
						return String.class;
					case COUNSEL_TABLE_IS_DELETED:
						// Render the Deleted? column as a checkbox
						return Boolean.class;
					case COUNSEL_TABLE_CHAMBER_REF_NO:
						return String.class;
					case COUNSEL_TABLE_CHAMBER_FIRM_NAME:
						return String.class;
					case COUNSEL_TABLE_CHAMBER_ADDRESS:
						return String.class;
					default:
						return super.getColumnClass(columnIndex);
					}
				}
			});
			counselSearchResultsTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					Boolean isDeleted = (Boolean) table.getValueAt(row, COUNSEL_TABLE_IS_DELETED);
					if (!isSelected) {
						// Make the search result red if the deleted checkbox is
						// ticked
						c.setForeground(isDeleted ? Color.RED : Color.BLACK);
					} else {
						c.setForeground(isDeleted ? Color.RED : Color.WHITE);
					}
					return c;
				}
			});
		
			// --- Add a column to hold the counsel object, this column will not
			// be displayed ---
			DefaultTableModel model = (DefaultTableModel) counselSearchResultsTable.getModel();
			model.addColumn("counsel");
			counselSearchResultsTable.getColumnModel().getColumn(model.getColumnCount() - 1).setMinWidth(0);
			counselSearchResultsTable.getColumnModel().getColumn(model.getColumnCount() - 1).setMaxWidth(0);
			counselSearchResultsTable.setPreferredScrollableViewportSize(counselSearchResultsTable.getPreferredSize());
			counselSearchResultsTable.getSelectionModel().addListSelectionListener(getSearchResultsSelectionListener());
			setCounselSearchResultsTableModelListener();
		}
		return counselSearchResultsTable;
	}
	
	private void setCounselSearchResultsTableModelListener() {
		TableModelListener counselSearchResultsTableModelListener = new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					DefaultTableModel model = (DefaultTableModel) counselSearchResultsTable.getModel();
					int selected = counselSearchResultsTable.getSelectedRow();
					String inputVal = model.getValueAt(selected, COUNSEL_TABLE_CHAMBER_REF_NO).toString();
					//do nothing if entered value is the same
					if (!inputVal.equals(((Counsel) model.getValueAt(selected, model.getColumnCount() - 1))
							.getCrestChamberId().toString())) {
						btnChamberDetails.setEnabled(false);
						btnCounselDetails.setEnabled(false);
						btnUpdateChamberRef.setEnabled(true);
					}
				}
			}
		};
		counselSearchResultsTable.getModel().addTableModelListener(counselSearchResultsTableModelListener);
	}
	
	/**
	 * Returns a panel which contains the Chamber Search Results
	 * @return
	 */
	public JPanel getChamberSearchResultsPanel() {
		GridBagConstraints gbc = getGridBagLayout();
		if(chamberSearchResultsPanel == null) {
			chamberSearchResultsPanel = new JPanel(new GridBagLayout());
			chamberSearchResultsPanel.setBorder(
					BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
							"ChamberAndAdvocateDetails.chamberSearchResultsPanelTitle")));
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.WEST;
			
			chamberSearchResultsTableScrollPane = new JScrollPane();
			chamberSearchResultsTableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			chamberSearchResultsPanel.add(chamberSearchResultsTableScrollPane, gbc);				
			chamberSearchResultsTableScrollPane.setViewportView(getChamberSearchResultsTable());
		}
		
		return chamberSearchResultsPanel;		
	}
	
	/**
	 * Returns the table that will hold the Chamber search results table
	 * @return
	 */
	public JTable getChamberSearchResultsTable() {
		if (chamberSearchResultsTable == null) {
			chamberSearchResultsTable = new JTable();
			chamberSearchResultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			chamberSearchResultsTable.setModel(new DefaultTableModel(new Object[][] {},
					new String[] {
							XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
									"ChamberAndAdvocateDetails.chamberSearchResultsTableChamberDeletedColumn"),
							XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
									"ChamberAndAdvocateDetails.chamberSearchResultsTableChamberRefNoColumn"),
							XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
									"ChamberAndAdvocateDetails.chamberSearchResultsTableChamberFirmNameColumn"),
							XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
									"ChamberAndAdvocateDetails.chamberSearchResultsTableChamberAddressColumn"),
							XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
									"ChamberAndAdvocateDetails.chamberSearchResultsTableChamberTownColumn"),
							XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
									"ChamberAndAdvocateDetails.chamberSearchResultsTableChamberPostcodeColumn"),
							XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
									"ChamberAndAdvocateDetails.chamberSearchResultsTableChamberClerkNameColumn")}) {
				/**
										 * 
										 */
				private static final long serialVersionUID = 1L;
				boolean[] columnEditables = new boolean[] { false, false, false, false, false, false, false, false };

				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
				
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					switch (columnIndex) {
					case CHAMBER_TABLE_IS_DELETED:
						// Render the Deleted? column as a checkbox
						return Boolean.class;
					case CHAMBER_TABLE_CHAMBER_REF_NO:
						return String.class;
					case CHAMBER_TABLE_CHAMBER_FIRM_NAME:
						return String.class;
					case CHAMBER_TABLE_CHAMBER_ADDRESS:
						return String.class;
					case CHAMBER_TABLE_TOWN:
						return String.class;
					case CHAMBER_TABLE_POSTCODE:
						return String.class;
					case CHAMBER_TABLE_CLERK_NAME:
						return String.class;
					default:
						return super.getColumnClass(columnIndex);
					}
				}
			});
			chamberSearchResultsTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer(){
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					Boolean isDeleted = (Boolean) table.getValueAt(row, CHAMBER_TABLE_IS_DELETED);
					if (!isSelected) {
						// Make the search result red if the deleted checkbox is
						// ticked
						c.setForeground(isDeleted ? Color.RED : Color.BLACK);
					} else {
						c.setForeground(isDeleted ? Color.RED : Color.WHITE);
					}
					return c;
				}
			});
			
			// --- Add a column to hold the chamber object, this column will not
			// be displayed ---
			DefaultTableModel model = (DefaultTableModel) chamberSearchResultsTable.getModel();
			model.addColumn("chamber");
			chamberSearchResultsTable.getColumnModel().getColumn(model.getColumnCount() - 1).setMinWidth(0);
			chamberSearchResultsTable.getColumnModel().getColumn(model.getColumnCount() - 1).setMaxWidth(0);
			chamberSearchResultsTable.setPreferredScrollableViewportSize(chamberSearchResultsTable.getPreferredSize());
			chamberSearchResultsTable.getSelectionModel().addListSelectionListener(getSearchResultsSelectionListener());
		}
		return chamberSearchResultsTable;
	}
	
	public ListSelectionListener getSearchResultsSelectionListener() {
		ListSelectionListener listSelectionListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (counselSearchResultsPanel.isVisible()) {
					btnCounselDetails.setEnabled(!lsm.isSelectionEmpty());
				}
				btnChamberDetails.setEnabled(!lsm.isSelectionEmpty());
				btnUpdateChamberRef.setEnabled(false);
			}
		};
		return listSelectionListener;
	}
	
	public XTextField getCounselSurname() {
		if (lblCounselSurnameInvalidEntry == null) {
			lblCounselSurnameInvalidEntry = new JLabel(" ");
		}
		if (txtCounselSurname == null) {
			txtCounselSurname = new XTextField(50, "^.{1,50}$", lblCounselSurnameInvalidEntry, false);
			txtCounselSurname.setGridBagLayout(true);
			txtCounselSurname.setMaxLength(50);
			txtCounselSurname.setColumns(10);
			txtCounselSurname.setUpperCase(true);
			txtCounselSurname.setMinimumSize(txtCounselSurname.getPreferredSize());
			txtCounselSurname.addKeyListener(new CounselKeyListener());
		}
		return txtCounselSurname;
	}
	
	public XTextField getCounselInitials() {
		if (lblCounselInitialsInvalidEntry == null) {
			lblCounselInitialsInvalidEntry = new JLabel(" ");
		}
		if (txtCounselInitials == null) {
			txtCounselInitials = new XTextField(4, "^.{1,4}$", lblCounselInitialsInvalidEntry, false);
			txtCounselInitials.setGridBagLayout(true);
			txtCounselInitials.setMaxLength(4);
			txtCounselInitials.setColumns(10);
			txtCounselInitials.setUpperCase(true);
			txtCounselInitials.setMinimumSize(txtCounselInitials.getPreferredSize());	
			txtCounselInitials.addKeyListener(new CounselKeyListener());
		}
		return txtCounselInitials;
	}
	
	public XTextField getChamberRefNo() {
		if (lblChamberRefNoInvalidEntry == null) {
			lblChamberRefNoInvalidEntry = new JLabel(" ");
		}
		if (txtChamberRefNo == null) {
			txtChamberRefNo = new XTextField(7, "^[0-9]{1,7}$", lblChamberRefNoInvalidEntry, false);
			txtChamberRefNo.setGridBagLayout(true);
			txtChamberRefNo.setMaxLength(7);
			txtChamberRefNo.setNumeric(true);
			txtChamberRefNo.setColumns(10);
			txtChamberRefNo.setUpperCase(true);
			txtChamberRefNo.setMinimumSize(txtChamberRefNo.getPreferredSize());
			txtChamberRefNo.addKeyListener(new ChamberKeyListener());
		}
		return txtChamberRefNo;
	}
	
	public XTextField getChamberName() {
		if (lblChamberNameInvalidEntry == null) {
			lblChamberNameInvalidEntry = new JLabel(" ");
		}
		if (txtChamberName == null) {
			txtChamberName = new XTextField(35, "^.{1,35}$", lblChamberNameInvalidEntry, false);
			txtChamberName.setGridBagLayout(true);
			txtChamberName.setMaxLength(35);
			txtChamberName.setColumns(10);
			txtChamberName.setUpperCase(true);
			txtChamberName.setMinimumSize(txtChamberName.getPreferredSize());	
			txtChamberName.addKeyListener(new ChamberKeyListener());
		}
		return txtChamberName;
	}
	
	/**
	 * Returns the Search Counsel button
	 * @return
	 */
	public JButton getSearchCounselButton() {
		if (btnSearchCounsel == null) {
			btnSearchCounsel = new JButton(new SearchCounselAction(this));
		}
		return btnSearchCounsel;
	}
	
	/**
	 * Returns the Add Chamber button
	 * @return
	 */
	public JButton getAddChamberButton() {
		if (btnAddChamber == null) {
			btnAddChamber = new JButton(new AddChamberAction(this));
		}
		return btnAddChamber;
	}
	
	/**
	 * Returns the Search Chamber button
	 * @return
	 */
	public JButton getSearchChamberButton() {
		if (btnSearchChamber == null) {
			btnSearchChamber = new JButton(new SearchChamberAction(this));
		}
		return btnSearchChamber;
	}
	
	public void configureTabOrder() {
		setFocusTraversalPolicyProvider(true);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { txtCounselSurname, txtCounselInitials,
				btnSearchCounsel, txtChamberRefNo, txtChamberName, btnSearchChamber, btnAddChamber, btnCancel }));
	}
	
	private void disableCounselSearchResultsPanel() {
		counselSearchResultsTable.setEnabled(false);
		btnChamberDetails.setEnabled(false);
		btnCounselDetails.setEnabled(false);
		btnUpdateChamberRef.setEnabled(false);
	}
	
	private class CounselKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {

		}

		@Override
		public void keyReleased(KeyEvent e) {
			btnSearchCounsel.setEnabled(validateCounselTextFields());
		}

	}
	
	private boolean validateCounselTextFields() {
		boolean validSearch = false;
		
		String str = "";

		str = getTxtCounselSurname().replace("%", "");
		if (str.length() > 0) {
			validSearch = true;
		}
		
		str = getTxtCounselInitials().replace("%", "");
		if (str.length() > 0) {
			validSearch = true;
		}
		
		return validSearch;
	}
	
	private class ChamberKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {

		}

		@Override
		public void keyReleased(KeyEvent e) {
			btnSearchChamber.setEnabled(validateChamberTextFields());
		}

	}
	
	private boolean validateChamberTextFields() {
		boolean validSearch = false;
		
		String str = "";

		str = getTxtChamberRefNo().replace("%", "");
		if (str.length() > 0) {
			validSearch = true;
		}
		
		str = getTxtChamberName().replace("%", "");
		if (str.length() > 0) {
			validSearch = true;
		}
		
		return validSearch;
	}
	
	private class AddChamberAction extends XAction {

		private static final long serialVersionUID = 1L;

		public AddChamberAction(ChamberAndAdvocateDetailsPanel parent) {
			populateFromBundle("ChamberAndAdvocateDetailsAddChamber");
			setCaller(parent);
		}

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			addChamber();
		}

	}
	
	public void addChamber() {		
		try {
			ChamberDetailsDialog chamberDetailsDialog;
			chamberDetailsDialog = new ChamberDetailsDialog(parentDialog.getParentFrame(),
					new ChamberDetailsModel(this, null, model.getReadOnly()));
			chamberDetailsDialog.setLocationRelativeTo(parentDialog.getParentFrame());
			chamberDetailsDialog.setVisible(true);
			
			// Deal with refreshing screen after exit Chamber Details screen (only if the current search results are for a search on Chambers)
			if (chamberSearchResultsPanel.isVisible()) {
				if ( btnSearchChamber.isEnabled() ) {
					// Search button is enabled so search to refresh the chamber search results
					searchChamber(false);	// Call with false so the Chamber Details popup does not open again if single row is returned
				}
			}
		} catch (CSRecoverableException e) {
			XHIBITConstant.handleError(e);
		}

	}
	
	private class SearchCounselAction extends XAction {
		
		private static final long serialVersionUID = 1L;

		public SearchCounselAction(ChamberAndAdvocateDetailsPanel parent) {
			populateFromBundle("ChamberAndAdvocateDetailsSearchCounsel");
			setCaller(parent);
		}

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			clearChamberSearchFields();
			searchCounsel();
		}
		
	}
	
	public void clearChamberSearchFields() {
		setTxtChamberName("");
		setTxtChamberRefNo("");
		btnSearchChamber.setEnabled(false);
		counselSearchResultsPanel.setVisible(true);
		chamberSearchResultsPanel.setVisible(false);
		DefaultTableModel model = (DefaultTableModel) chamberSearchResultsTable.getModel();
		// --- Clear existing data from table ---
		model.setRowCount(0);
		disableButtons();
	}
	
	@SuppressWarnings("unchecked")
	public void searchCounsel() {
		try {
			String initials = "%";
			String surname = "%";
			if (!txtCounselInitials.isNullOrEmpty()) {
				initials = getTxtCounselInitials();
			}
			if (!txtCounselSurname.isNullOrEmpty()) {
				surname = getTxtCounselSurname();
			}
			BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
			ArrayList<RefAdvocateComplexValue> counsels = new ArrayList<RefAdvocateComplexValue>();

			counsels = (ArrayList<RefAdvocateComplexValue>) bizRefDelegate
					.findCounselBySurnameInitialsCourtId(surname, initials,
							XhibitSingleton.getInstance().getCourtId());

			showCounselResults(counsels);
		} catch (Exception er) {
			er.printStackTrace();
			XHIBITConstant.handleError(er);
		}
	}
	
	public Boolean hasChamberRefNoChanged() {
		Boolean fieldsChanged = false;

		if (!getTxtChamberRefNo().equals("") && (!lblChamberRefNoInvalidEntry.equals(" "))) {
			log.debug("Chamber Ref No changed");
			fieldsChanged = true;
		}

		return fieldsChanged;
	}
	
	public Boolean hasChamberNameChanged() {
		Boolean fieldsChanged = false;

		if (!getTxtChamberName().equals("") && (!lblChamberNameInvalidEntry.equals(" "))
				&& (!getTxtChamberName().equals("%"))) {
			log.debug("Chamber Name changed");
			fieldsChanged = true;
		}

		return fieldsChanged;
	}
	
	public void showCounselResults(ArrayList<RefAdvocateComplexValue> counsels) {
		// --- If we got results ---
		if (!counsels.isEmpty()) {
			counselSearchResultsTable.setEnabled(true);
			DefaultTableModel model = (DefaultTableModel) counselSearchResultsTable.getModel();
			// --- Clear existing data from table ---
			model.setRowCount(0);

			for (RefAdvocateComplexValue counsel : counsels) {
				Counsel newCounsel = new Counsel(counsel);

				model.addRow(
						new Object[] { newCounsel.getFullName(), newCounsel.IsDeleted(), counsel.getCrestChamberId(),
								counsel.getFirmName(), newCounsel.getFullAddress(), newCounsel });
			}
		} else {
			log.debug("No counsels found");
			showNoRecordsFoundDialog();
		}
	}
	
	public void showNoRecordsFoundDialog() {
		XMessageBox.alert(parentDialog, XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, 
				"ChamberAndAdvocateDetails.noRecordsFoundTitle"), true,
				XMessageBox.ICONINFORMATION, XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, 
						"ChamberAndAdvocateDetails.noRecordsFoundMessage"), XMessageBox.OK_ONLY,
				XMessageBox.DEFAULTOK);
	}
	
	private class SearchChamberAction extends XAction {
		
		private static final long serialVersionUID = 1L;

		public SearchChamberAction(ChamberAndAdvocateDetailsPanel parent) {
			populateFromBundle("ChamberAndAdvocateDetailsSearchChamber");
			setCaller(parent);
		}

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			clearCounselSearchFields();
			searchChamber(true);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void searchChamber(boolean openSingleResult) {
		Boolean hasChamberRefNo = false;
		Boolean hasChamberName = false;
		hasChamberRefNo = hasChamberRefNoChanged();
		hasChamberName = hasChamberNameChanged();
		String chamberName = "";

		BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
		ArrayList<RefChamberComplexValue> chambers = new ArrayList<RefChamberComplexValue>();

		try {
			
			if ((hasChamberRefNo) && (hasChamberName)) {
				log.debug ("Combined search");
				chambers = (ArrayList<RefChamberComplexValue>) bizRefDelegate
						.findChamberByFirmNameCrestChamberIdCourtId(getTxtChamberName(),
								Integer.parseInt(getTxtChamberRefNo()), XhibitSingleton.getInstance().getCourtId());
			}
			
			else if (hasChamberRefNo) {
				log.debug("Chamber ref no search only");
				chambers = (ArrayList<RefChamberComplexValue>) bizRefDelegate
						.findChamberByCrestChamberIdCourtId(Integer.parseInt(getTxtChamberRefNo()),
								XhibitSingleton.getInstance().getCourtId(), true);
			}
					
			else if (hasChamberName) {
				log.debug("Chamber name search only");
				chamberName = getTxtChamberName();
				chambers = (ArrayList<RefChamberComplexValue>) bizRefDelegate.findChamberByFirmNameCourtId(chamberName,
						XhibitSingleton.getInstance().getCourtId());
			}
			
			showChamberResults(chambers, openSingleResult);
		} catch (Exception er) {
			er.printStackTrace();
			XHIBITConstant.handleError(er);
		}
	}
	
	public void showChamberResults(ArrayList<RefChamberComplexValue> chambers, boolean openSingleResult) {
		// --- If we got results ---
		if (!chambers.isEmpty()) {
			DefaultTableModel tableModel = (DefaultTableModel) chamberSearchResultsTable.getModel();
			// --- Clear existing data from table ---
			tableModel.setRowCount(0);

			for (RefChamberComplexValue chamber : chambers) {
				Chamber newChamber = new Chamber(chamber);
				
				String address1 = "";
				String town = "";
				String postcode = "";
				
				if (chamber.getAddress() != null) {
					address1 = chamber.getAddress().getAddress1();
					town = chamber.getAddress().getTown();
					postcode = chamber.getAddress().getPostcode();
				}

				tableModel.addRow(new Object[] { newChamber.IsDeleted(), chamber.getCrestChamberId(), chamber.getFirmName(), address1, town,
						postcode, chamber.getClerkName(), newChamber });
			}
			
			if (tableModel.getRowCount() == 1 && openSingleResult) {
				log.debug("One result only");
				RefChamberComplexValue val = new RefChamberComplexValue();
				val = getSingleChamberFromChamberResults();

				ChamberDetailsDialog chamberDetailsDialog;
				try {
					refreshSearchResults = false;
					chamberDetailsDialog = new ChamberDetailsDialog(parentDialog.getParentFrame(),
							new ChamberDetailsModel(this, val, model.getReadOnly()));
					chamberDetailsDialog.setLocationRelativeTo(parentDialog.getParentFrame());
					chamberDetailsDialog.setVisible(true);
					
					// Deal with refreshing screen after exit Chamber Details screen (only in update mode)
					if ( !model.getReadOnly() && getRefreshSearchResults() ) {
						if (chamberSearchResultsPanel.isVisible()) {
							if ( btnSearchChamber.isEnabled() ) {
								// Search button is enabled so search to refresh the chamber search results
								searchChamber(false);	// Call with false so the Chamber Details popup does not open again if single row is returned
							}
						}
					}
				} catch (CSRecoverableException e) {
					e.printStackTrace();
				}
			}
		} else {
			log.debug("No chambers found");
			showNoRecordsFoundDialog();
		}
	}
	
	public void clearCounselSearchFields() {
		setTxtCounselSurname("");
		setTxtCounselInitials("");
		btnSearchCounsel.setEnabled(false);
		counselSearchResultsPanel.setVisible(false);
		chamberSearchResultsPanel.setVisible(true);
		DefaultTableModel model = (DefaultTableModel) counselSearchResultsTable.getModel();
		// --- Clear existing data from table ---
		model.setRowCount(0);
		disableButtons();
	}
	
	public void disableButtons() {
		btnChamberDetails.setEnabled(false);
		btnCounselDetails.setEnabled(false);
		btnUpdateChamberRef.setEnabled(false);
	}
	
	public RefChamberComplexValue getSelectedChamberFromCounselResults() {
		DefaultTableModel model = (DefaultTableModel) counselSearchResultsTable.getModel();
		Counsel selectedChamber = (Counsel) (model.getValueAt(counselSearchResultsTable.getSelectedRow(),
				model.getColumnCount() - 1));
		RefChamberComplexValue returnedChamber = null;
		try {
			BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
			returnedChamber = bizRefDelegate.findChamberByRefChamberId(selectedChamber.getRefChamberId());
		} catch (Exception er) {
			er.printStackTrace();
			XHIBITConstant.handleError(er);
		}
		
		return returnedChamber;
	}

	public RefAdvocateComplexValue getSelectedCounselFromCounselResults() {
		DefaultTableModel model = (DefaultTableModel) counselSearchResultsTable.getModel();
		Counsel selectedCounsel = (Counsel) (model.getValueAt(counselSearchResultsTable.getSelectedRow(),
				model.getColumnCount() - 1));
		RefAdvocateComplexValue returnedCounsel = null;
		try {
			BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
			returnedCounsel = bizRefDelegate.findCounselByRefAdvocateId(selectedCounsel.getId());
		} catch (Exception er) {
			er.printStackTrace();
			XHIBITConstant.handleError(er);
		}

		return returnedCounsel;
	}
	
	public RefChamberComplexValue getSelectedChamberFromChamberResults() {
		DefaultTableModel model = (DefaultTableModel) chamberSearchResultsTable.getModel();
		Chamber selectedChamber = (Chamber) (model.getValueAt(chamberSearchResultsTable.getSelectedRow(),
				model.getColumnCount() - 1));
		RefChamberComplexValue returnedChamber = null;
		try {
			BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
			returnedChamber = bizRefDelegate.findChamberByRefChamberId(selectedChamber.getId());
		} catch (Exception er) {
			er.printStackTrace();
			XHIBITConstant.handleError(er);
		}
		
		return returnedChamber;
	}
	
	public RefChamberComplexValue getSingleChamberFromChamberResults() {
		DefaultTableModel model = (DefaultTableModel) chamberSearchResultsTable.getModel();
		Chamber selectedChamber = (Chamber) (model.getValueAt(0, model.getColumnCount() - 1));
		RefChamberComplexValue returnedChamber = null;
		try {
			BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
			returnedChamber = bizRefDelegate.findChamberByRefChamberId(selectedChamber.getId());
		} catch (Exception er) {
			er.printStackTrace();
			XHIBITConstant.handleError(er);
		}
		
		return returnedChamber;
	}
	
	public RefLegalRepresentativeBasicValue getLegalRepValueFromRefAdvocate(Integer legalRepId) {
		RefLegalRepresentativeBasicValue returnedLegalRep = null;
		try {
			BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
			returnedLegalRep = bizRefDelegate.findLegalRepresentativeFromAdvocateLegalRepId(legalRepId);
		} catch (Exception er) {
			er.printStackTrace();
			XHIBITConstant.handleError(er);
		}
		
		return returnedLegalRep;
	}
	
	public void refreshCounselSearchResultsRow(Integer refAdvocateId) {
		DefaultTableModel model = (DefaultTableModel) counselSearchResultsTable.getModel();
		int selected = counselSearchResultsTable.getSelectedRow();
		RefAdvocateComplexValue returnedCounsel = null;
		try {
			BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
			returnedCounsel = bizRefDelegate.findCounselByRefAdvocateId(refAdvocateId);
			Counsel newCounsel = new Counsel(returnedCounsel);
			counselSearchResultsTable.setValueAt(newCounsel.getFirmName(), selected, COUNSEL_TABLE_CHAMBER_FIRM_NAME);
			counselSearchResultsTable.setValueAt(newCounsel.getFullAddress(), selected, COUNSEL_TABLE_CHAMBER_ADDRESS);
			counselSearchResultsTable.setValueAt(newCounsel, selected, model.getColumnCount() - 1);
		} catch (Exception er) {
			er.printStackTrace();
			XHIBITConstant.handleError(er);
		}
	}
	
	public void showUpdateChamberRefSuccessDialog() {
		XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
						"ChamberAndAdvocateDetails.updateChamberRefSuccessTitle"),
				true, XMessageBox.ICONINFORMATION,
				XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
						"ChamberAndAdvocateDetails.updateChamberRefSuccessMessage"),
				XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	}
	
	public void showUpdateChamberRefInvalidDialog() {
		XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
						"ChamberAndAdvocateDetails.updateChamberRefInvalidChamberRefTitle"),
				true, XMessageBox.ICONERROR,
				XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
						"ChamberAndAdvocateDetails.updateChamberRefInvalidChamberRefMessage"),
				XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	}
	
	public void showUpdateChamberRefDeletedCounselDialog() {
		XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
						"ChamberAndAdvocateDetails.updateChamberRefDeletedCounselTitle"),
				true, XMessageBox.ICONERROR,
				XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
						"ChamberAndAdvocateDetails.updateChamberRefDeletedCounselMessage"),
				XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	}
	
	public void revertCounselSearchResultsRow(Integer oldChamberRefNo) {
		int selected = counselSearchResultsTable.getSelectedRow();
		counselSearchResultsTable.setValueAt(oldChamberRefNo, selected, COUNSEL_TABLE_CHAMBER_REF_NO);
		counselSearchResultsTable.changeSelection(selected, COUNSEL_TABLE_CHAMBER_REF_NO, true, false);
		counselSearchResultsTable.requestFocus();
		btnUpdateChamberRef.setEnabled(false);
	}
	
	@SuppressWarnings("unchecked")
	public void updateChamberRef() {
		DefaultTableModel model = (DefaultTableModel) counselSearchResultsTable.getModel();
		int selected = counselSearchResultsTable.getSelectedRow();
		Integer oldChamberRefNo = ((Counsel) model.getValueAt(selected, model.getColumnCount() - 1))
				.getCrestChamberId();
		String newValue = (String) model.getValueAt(selected, COUNSEL_TABLE_CHAMBER_REF_NO);
		Boolean isDeleted = (Boolean) model.getValueAt(selected, COUNSEL_TABLE_IS_DELETED);
		if (isValidChamberRefNo(newValue)) {
			if (isDeleted) {
				// Counsel is deleted
				showUpdateChamberRefDeletedCounselDialog();
				revertCounselSearchResultsRow(oldChamberRefNo);
			} else {
				RefAdvocateComplexValue refAdvocateComplexVal = new RefAdvocateComplexValue();
				Integer newChamberRefNo = Integer.parseInt(newValue);
				BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
				ArrayList<RefChamberComplexValue> chambers = new ArrayList<RefChamberComplexValue>();
				try {
					chambers = (ArrayList<RefChamberComplexValue>) bizRefDelegate.findChamberByCrestChamberIdCourtId(
							newChamberRefNo, XhibitSingleton.getInstance().getCourtId(), false);

					// --- If we got results ---
					if (!chambers.isEmpty()) {
						refAdvocateComplexVal.setCrestChamberId(newChamberRefNo);
						refAdvocateComplexVal
								.setId(((Counsel) model.getValueAt(counselSearchResultsTable.getSelectedRow(),
										model.getColumnCount() - 1)).getId());
						refAdvocateComplexVal.setCrestAdvocateId(
								((Counsel) model.getValueAt(counselSearchResultsTable.getSelectedRow(),
										model.getColumnCount() - 1)).getCrestAdvocateId());
						bizRefDelegate.updateChamberRef(refAdvocateComplexVal, XhibitSingleton.getInstance()
								.getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
						refreshCounselSearchResultsRow(refAdvocateComplexVal.getId());
						showUpdateChamberRefSuccessDialog();
						btnUpdateChamberRef.setEnabled(false);
						counselSearchResultsTable.getSelectionModel().clearSelection();
					} else {
						// No chambers found
						showUpdateChamberRefInvalidDialog();
						revertCounselSearchResultsRow(oldChamberRefNo);
					}
				} catch (Exception er) {
					XHIBITConstant.handleError(er);
				}
			}
		} else {
			// Invalid chamber ref
			showUpdateChamberRefInvalidDialog();
			revertCounselSearchResultsRow(oldChamberRefNo);
		}
	}
	
	public Boolean isValidChamberRefNo(String str) {
		return str.matches("^[0-9]{1,7}$");
	}
	
	protected String getTxtCounselSurname() {
		return txtCounselSurname.getText();
	}

	protected void setTxtCounselSurname(String txtCounselSurname) {
		this.txtCounselSurname.setText(txtCounselSurname);
	}

	protected String getTxtCounselInitials() {
		return txtCounselInitials.getText();
	}

	protected void setTxtCounselInitials(String txtCounselInitials) {
		this.txtCounselInitials.setText(txtCounselInitials);
	}

	protected String getTxtChamberRefNo() {
		return txtChamberRefNo.getText();
	}

	protected void setTxtChamberRefNo(String txtChamberRefNo) {
		this.txtChamberRefNo.setText(txtChamberRefNo);
	}

	protected String getTxtChamberName() {
		return txtChamberName.getText();
	}

	protected void setTxtChamberName(String txtChamberName) {
		this.txtChamberName.setText(txtChamberName);
	}
	
    /**
	 * If the model is not null then populate the fields with the values.
	 * Also sets the caret to 0 so that if the field is too long then it'll
	 * show the first half of the string instead of the end of the string.
	 */
	private void moveModelToScreen() {
		if (model != null) {

		}
	}
	
	 @Override
	public void stepInitialise() throws CSRecoverableException {
		
	}
	/**
	 * Default gridbag that's used throughout the panels.
	 * @return gridbagconstraints
	 */
	private GridBagConstraints getGridBagLayout() {
		return new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
	}

	/**
	 * moves the actual values into the fields.
	 */
	@Override
	public void stepActivate() throws CSRecoverableException {
		moveModelToScreen();
		
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		
	}

	/**
	 * Checks if any of the validation on the page is incorrect.
	 */
	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		
	}

	/**
	 * If save button clicked then check validation and then save the database changes
	 */
	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		
		if (btnChamberDetails.equals(getDeinitialiseSource())) {
			refreshSearchResults = false;
			RefChamberComplexValue val = new RefChamberComplexValue();

			if (counselSearchResultsPanel.isVisible()) {
				log.debug("Counsel search results panel visible");
				val = getSelectedChamberFromCounselResults();				
			} else if (chamberSearchResultsPanel.isVisible()) {
				log.debug("Chamber search results panel visible");
				val = getSelectedChamberFromChamberResults();							
			}
			
			ChamberDetailsDialog chamberDetailsDialog;
			chamberDetailsDialog = new ChamberDetailsDialog(parentDialog.getParentFrame(),
					new ChamberDetailsModel(this, val, model.getReadOnly()));
			chamberDetailsDialog.setLocationRelativeTo(parentDialog.getParentFrame());
			chamberDetailsDialog.setVisible(true);
			
			// Deal with refreshing screen after exit Chamber Details screen (only in update mode)
			if ( !model.getReadOnly() && getRefreshSearchResults() ) {
				if (counselSearchResultsPanel.isVisible()) {
					if ( btnSearchCounsel.isEnabled() ) {
						// Search button is enabled so search to refresh the counsel search results
						searchCounsel();
					}
				} 
				else if (chamberSearchResultsPanel.isVisible()) {
					if ( btnSearchChamber.isEnabled() ) {
						// Search button is enabled so search to refresh the chamber search results
						searchChamber(false);	// Call with false so the Chamber Details popup does not open again if single row is returned
					}
				}
			}
		}
		if (btnUpdateChamberRef.equals(getDeinitialiseSource())) {
			updateChamberRef();
		}
		
		if (btnCounselDetails.equals(getDeinitialiseSource())) {
			refreshSearchResults = false;
			RefAdvocateComplexValue refAdvocate = new RefAdvocateComplexValue();
			RefLegalRepresentativeBasicValue refLegalRep = new RefLegalRepresentativeBasicValue();
			refAdvocate = getSelectedCounselFromCounselResults();
			refLegalRep = getLegalRepValueFromRefAdvocate(refAdvocate.getLegalRepId());
			
			CounselDetailsDialog counselDetailsDialog;
			counselDetailsDialog = new CounselDetailsDialog(parentDialog.getParentFrame(),
					new CounselDetailsModel(this, refAdvocate, refLegalRep, model.getReadOnly()));
			counselDetailsDialog.setLocationRelativeTo(parentDialog.getParentFrame());
			counselDetailsDialog.setVisible(true);
			
			// Deal with refreshing screen after exit Counsel Details screen (only in update mode)
			if ( !model.getReadOnly() && getRefreshSearchResults() ) {
				if (counselSearchResultsPanel.isVisible()) {
					if ( btnSearchCounsel.isEnabled() ) {
						// Search button is enabled so search to refresh the counsel search results
						searchCounsel();
					}
				} 
				else if (chamberSearchResultsPanel.isVisible()) {
					if ( btnSearchChamber.isEnabled() ) {
						// Search button is enabled so search to refresh the chamber search results
						searchChamber(false);	// Call with false so the Chamber Details popup does not open again if single row is returned
					}
				}
			}
		}

	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		
	}
	
}
