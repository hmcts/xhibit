package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class DefendantAppellantSearchPanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;
	private DefendantAppellantSearchModel model;
	private DefendantAppellantSearchDialog parentDialog;

	/**
	 * Fields on Defendant/Appellant Search panel.
	 */
	private JLabel lblSearchCriteria = null;
	private JLabel lblSurname = null;
	private XTextField surnameTextField = null;
	private JLabel lblPrisonerNumber = null;
	private XTextField prisonerNumberTextField = null;
	private JLabel lblFirstName = null;
	private XTextField firstNameTextField = null;
	private JLabel lblGender = null;
	private XComboBox comboGender;
	/**
	 * Fields on Search Button Error Label panel.
	 */
	private JButton btnSearch = null;
	private JLabel lblErrorMessage = null;

	/**
	 * Fields on Defendant/Appellant Search Results panel.
	 */
	private JTable defendantAppellantTable = null;
	private JScrollPane tableScrollPane = null;

	/**
	 * Fields on Button panel.
	 */
	private JButton btnViewCaseDetails = null;
	private JButton btnAddNewDefendantAppellant = null;
	private JButton btnSelectDefendantAppellant = null;
	private JButton btnCancel = null;
	private JButton btnCaseDetailsBack = null;

	/**
	 * Fields on Defendant/Appellant Case Details panel.
	 */
	private JTable defendantAppellantCaseDetailsTable = null;
	private JScrollPane caseDetailsTableScrollPane = null;

	/**
	 * JPanels.
	 */
	private JPanel mainPanel = null;
	private JPanel searchPanel = null;
	private JPanel genderComboBoxPanel = null;
	private JPanel searchResultsPanel = null;
	private JPanel searchButtonErrorLabelPanel = null;
	private JPanel caseDetailsPanel = null;

	/**
	 * Case Details specific variables
	 */
	private String caseNumberFormatted = null;
	private String committalDateFormatted = null;
	private String appealLodgedDateFormatted = null;
	private String sentForTrialDateFormatted = null;
	private String bcStatusFormatted = null;
	private String courtName = null;

	private static final Logger log = CSServices.getLogger(DefendantAppellantSearchPanel.class);

	public DefendantAppellantSearchPanel(final DefendantAppellantSearchDialog parentDialog,
			DefendantAppellantSearchModel model) throws CSRecoverableException {
		this.model = model;
		this.parentDialog = parentDialog;
		stepInitialise();
		jbInit();
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
		gbc.fill = GridBagConstraints.BOTH;

		mainPanel.add(getSearchPanel(), gbc);

		gbc.gridy++;

		gbc.weighty = 0.9;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;
		mainPanel.add(getSearchResultsPanel(), gbc);

		CustomButtonPanel buttonPanel = (CustomButtonPanel) this.parentDialog.getButtonPanel();

		btnViewCaseDetails = buttonPanel.addButton("DefendantAppellantSearchViewCaseDetails", false, false);
		btnSelectDefendantAppellant = buttonPanel.addButton("DefendantAppellantSearchSelect", false, false);
		btnAddNewDefendantAppellant = buttonPanel.addButton("DefendantAppellantSearchAddNew", false, false);
		btnCaseDetailsBack = buttonPanel.addButton("DefendantAppellantSearchCaseDetailsBack", false, false);
		btnCancel = buttonPanel.addButton("DefendantAppellantSearchCancel", true, false);

		btnCaseDetailsBack.setVisible(false);
		btnViewCaseDetails.setEnabled(false);
		btnSelectDefendantAppellant.setEnabled(false);
		btnSearch.setEnabled(false);
		btnAddNewDefendantAppellant.setEnabled(false);
	}

	/**
	 * Returns a panel which contains the main panel
	 * 
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
			mainPanel.setPreferredSize(new Dimension(750, 500));
			this.add(scrollPane, gbc);
		}

		return mainPanel;
	}

	/**
	 * Returns a panel which contains the Defendant/Appellant Search fields
	 * 
	 * @return
	 */
	public JPanel getSearchPanel() {
		GridBagConstraints gbc = getGridBagLayout();
		if (searchPanel == null) {
			searchPanel = new JPanel(new GridBagLayout());
			searchPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant
					.getResource(XhibitBundles.CaseMaintenanceResources, "defendantAppellantSearch.searchPanelTitle")));

			gbc.gridwidth = 3;
			gbc.weightx = 0.05;
			lblSearchCriteria = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantSearch.searchCriteriaLabel"));
			searchPanel.add(lblSearchCriteria, gbc);

			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridwidth = 1;

			gbc.gridy++;
			gbc.weightx = 0.05;
			lblSurname = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantSearch.surnameLabel"));
			gbc.fill = GridBagConstraints.NONE;
			searchPanel.add(lblSurname, gbc);

			gbc.weightx = 0.45;
			gbc.gridx++;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			searchPanel.add(getSurname(), gbc);

			gbc.gridx++;
			// Invisible box to resize text fields
			searchPanel.add(Box.createRigidArea(surnameTextField.getPreferredSize()), gbc);
			gbc.gridx--;

			gbc.gridx = 0;
			gbc.gridy++;
			gbc.weightx = 0.05;
			lblPrisonerNumber = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantSearch.prisonerNumberLabel"));
			gbc.fill = GridBagConstraints.NONE;
			searchPanel.add(lblPrisonerNumber, gbc);

			gbc.weightx = 0.45;
			gbc.gridx++;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			searchPanel.add(getPrisonerNumber(), gbc);

			gbc.gridx = 0;
			gbc.gridy++;
			gbc.weightx = 0.05;
			lblFirstName = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantSearch.firstNameLabel"));
			gbc.fill = GridBagConstraints.NONE;
			searchPanel.add(lblFirstName, gbc);

			gbc.weightx = 0.45;
			gbc.gridx++;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			searchPanel.add(getFirstName(), gbc);

			gbc.gridx = 0;
			gbc.gridy++;
			gbc.weightx = 0.05;
			lblGender = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantSearch.sexLabel"));
			gbc.fill = GridBagConstraints.NONE;
			searchPanel.add(lblGender, gbc);

			gbc.gridx++;
			gbc.gridwidth = 2;
			searchPanel.add(getGenderComboPanel(), gbc);

			gbc.gridx = 0;
			gbc.gridy++;
			gbc.gridwidth = 3;
			searchPanel.add(getSearchButtonErrorLabelPanel(), gbc);
		}

		return searchPanel;
	}

	/**
	 * Returns a panel which contains the Gender combo box (Male, Female and
	 * Company)
	 * 
	 * @return
	 */
	public JPanel getGenderComboPanel() {
		GridBagConstraints gbc = getGridBagLayout();
		if (genderComboBoxPanel == null) {
			genderComboBoxPanel = new JPanel(new GridBagLayout());
			genderComboBoxPanel.add(getComboGender(), gbc);
		}

		return genderComboBoxPanel;
	}
	
	private ArrayList<DropdownCodeStringValue> getGenders() {
		ArrayList<DropdownCodeStringValue> genders = new ArrayList<DropdownCodeStringValue>();
		genders.add(new DropdownCodeStringValue("Select","3")); 
		genders.add(new DropdownCodeStringValue( 
						XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
								"defendantAppellantSearch.maleRadioButtonLabel"),"1"));
		genders.add(new DropdownCodeStringValue( 
				XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
						"defendantAppellantSearch.femaleRadioButtonLabel"),"2"));
		
		genders.add(new DropdownCodeStringValue( 
				XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
						"defendantAppellantSearch.companyRadioButtonLabel"),"0"));

		return genders;
	}
	
	private XComboBox getComboGender() {
		if (comboGender == null) {
			comboGender = new XComboBox(false);
			comboGender.setModel(new DefaultComboBoxModel(getGenders().toArray()));
			DropdownBoxCellRender comboGenderRender = new DropdownBoxCellRender();
			comboGenderRender.setFormat(true);
			comboGender.setRenderer(comboGenderRender);
		}
		return comboGender;
	}

	/**
	 * Returns a panel which contains the Search button and the search error
	 * message
	 * 
	 * @return
	 */
	public JPanel getSearchButtonErrorLabelPanel() {
		GridBagConstraints gbc = getGridBagLayout();
		if (searchButtonErrorLabelPanel == null) {
			searchButtonErrorLabelPanel = new JPanel(new GridBagLayout());

			gbc.insets = XHIBITConstant.containerInsets;
			searchButtonErrorLabelPanel.add(getSearchButton(), gbc);

			gbc.gridx++;
			gbc.weightx = 0.45;
			gbc.gridwidth = 2;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			lblErrorMessage = new JLabel(" ");
			lblErrorMessage.setForeground(Color.RED);
			searchButtonErrorLabelPanel.add(lblErrorMessage, gbc);
		}

		return searchButtonErrorLabelPanel;
	}

	/**
	 * Returns the Search button
	 * 
	 * @return
	 */
	public JButton getSearchButton() {
		if (btnSearch == null) {
			btnSearch = new JButton(new SearchDefendantAppellantAction(this));
		}
		return btnSearch;
	}

	/**
	 * Returns a panel which contains the Defendant/Appellant Search Results
	 * 
	 * @return
	 */
	public JPanel getSearchResultsPanel() {
		GridBagConstraints gbc = getGridBagLayout();
		if (searchResultsPanel == null) {
			searchResultsPanel = new JPanel(new GridBagLayout());
			searchResultsPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(
					XhibitBundles.CaseMaintenanceResources, "defendantAppellantSearch.resultsPanelTitle")));
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weighty = 0.9;

			tableScrollPane = new JScrollPane();
			tableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

			searchResultsPanel.add(tableScrollPane, gbc);

			tableScrollPane.setViewportView(getDefendantAppellantTable());
		}

		return searchResultsPanel;

	}

	/**
	 * Returns the table that will hold the Defendant/Appellant search results
	 * 
	 * @return
	 */
	public JTable getDefendantAppellantTable() {
		if (defendantAppellantTable == null) {
			defendantAppellantTable = new JTable();
			defendantAppellantTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			defendantAppellantTable.setModel(new DefaultTableModel(new Object[][] {},
					new String[] {
							XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
									"defendantAppellantSearch.resultsTableSurnameColumn"),
							XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
									"defendantAppellantSearch.resultsTableFirstNameColumn"),
							XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
									"defendantAppellantSearch.resultsTableSexColumn"),
							XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
									"defendantAppellantSearch.resultsTableDOBColumn"),
							XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
									"defendantAppellantSearch.resultsTablePrisonerNumberColumn"),
							XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
									"defendantAppellantSearch.resultsTableAddressLine1Column"),
							XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
									"defendantAppellantSearch.resultsTableInCustodyColumn"),
							XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
									"defendantAppellantSearch.resultsTablePrisonIdColumn") }) {
				private static final long serialVersionUID = 1L;
				boolean[] columnEditables = new boolean[] { false, false, false, false, false, false, false, false };

				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
			defendantAppellantTable.getColumnModel().getColumn(0).setResizable(false);
			defendantAppellantTable.getColumnModel().getColumn(1).setResizable(false);
			defendantAppellantTable.getColumnModel().getColumn(2).setResizable(false);
			defendantAppellantTable.getColumnModel().getColumn(3).setResizable(false);
			defendantAppellantTable.getColumnModel().getColumn(4).setResizable(false);
			defendantAppellantTable.getColumnModel().getColumn(5).setResizable(false);
			defendantAppellantTable.getColumnModel().getColumn(6).setResizable(false);

			// --- Add a column to hold the Parent/Guardian Object, this column
			// will not be displayed ---

			DefaultTableModel model = (DefaultTableModel) defendantAppellantTable.getModel();
			model.addColumn("Parent/Guardian");
			defendantAppellantTable.getColumnModel().getColumn(model.getColumnCount() - 1).setResizable(false);
			defendantAppellantTable.getColumnModel().getColumn(model.getColumnCount() - 1).setMinWidth(0);
			defendantAppellantTable.getColumnModel().getColumn(model.getColumnCount() - 1).setMaxWidth(0);
			defendantAppellantTable.setPreferredScrollableViewportSize(defendantAppellantTable.getPreferredSize());
		}
		return defendantAppellantTable;
	}

	/**
	 * Returns a panel which contains the Case Details of the selected
	 * Defendant/Appellant
	 * 
	 * @return
	 */
	public JPanel getCaseDetailsPanel() {
		GridBagConstraints gbc = getGridBagLayout();
		if (caseDetailsPanel == null) {
			caseDetailsPanel = new JPanel(new GridBagLayout());
			XhibitApplicationController xac = model.getXhibitApplicationController();

			if (xac.getCaseStatus().isCaseType(CaseType.APPEAL) || xac.getCaseStatus().isCaseType(CaseType.MISC)) {
				caseDetailsPanel.setBorder(BorderFactory
						.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
								"defendantAppellantCaseDetails.caseDetailsPanelAppellant")));
			}
			if (xac.getCaseStatus().isCaseType(CaseType.TRIAL) || xac.getCaseStatus().isCaseType(CaseType.SENTENCE)) {
				caseDetailsPanel.setBorder(BorderFactory
						.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
								"defendantAppellantCaseDetails.caseDetailsPanelDefendant")));
			}

			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridwidth = 3;
			gbc.weightx = 0.05;

			caseDetailsTableScrollPane = new JScrollPane();
			caseDetailsTableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			caseDetailsTableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			caseDetailsPanel.add(caseDetailsTableScrollPane, gbc);

			caseDetailsTableScrollPane.setViewportView(getDefendantAppellantCaseDetailsTable());
		}

		return caseDetailsPanel;
	}

	/**
	 * Returns a table that contains the details of all cases assigned to the
	 * selected Defendant/Appellant
	 * 
	 * @return
	 */
	public JTable getDefendantAppellantCaseDetailsTable() {
		if (defendantAppellantCaseDetailsTable == null) {
			defendantAppellantCaseDetailsTable = new JTable();
			defendantAppellantCaseDetailsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			defendantAppellantCaseDetailsTable.setModel(new DefaultTableModel(new Object[][] {},
					new String[] {
							XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
									"defendantAppellantCaseDetails.caseDetailsTableCaseNumberColumn"),
							XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
									"defendantAppellantCaseDetails.caseDetailsTableCaseTitleColumn"),
							XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
									"defendantAppellantCaseDetails.caseDetailsTableCommittalDateColumn"),
							XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
									"defendantAppellantCaseDetails.caseDetailsTableAppealDateColumn"),
							XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
									"defendantAppellantCaseDetails.caseDetailsTableSentForTrialDateColumn"),
							XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
									"defendantAppellantCaseDetails.caseDetailsTableMagistratesCourtColumn"),
							XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
									"defendantAppellantCaseDetails.caseDetailsTableLiveStatusColumn"),
							XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
									"defendantAppellantCaseDetails.caseDetailsTableLiveBCStatusColumn") }) {
				/**
										 * 
										 */
				private static final long serialVersionUID = 1L;
				boolean[] columnEditables = new boolean[] { false, false, false, false, false, false, false, false };

				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
			defendantAppellantCaseDetailsTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Case
																										// Number
			defendantAppellantCaseDetailsTable.getColumnModel().getColumn(1).setPreferredWidth(250); // Case
																										// Title
			defendantAppellantCaseDetailsTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Committal
																										// Date
			defendantAppellantCaseDetailsTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Appeal
																										// Date
			defendantAppellantCaseDetailsTable.getColumnModel().getColumn(4).setPreferredWidth(125); // Sent
																										// for
																										// Trial
																										// Date
			defendantAppellantCaseDetailsTable.getColumnModel().getColumn(5).setPreferredWidth(125); // Magistrate's
																										// Court
			defendantAppellantCaseDetailsTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Live
																										// Status
			defendantAppellantCaseDetailsTable.getColumnModel().getColumn(7).setPreferredWidth(100); // Live
																										// B/C
																										// Status
		}
		return defendantAppellantCaseDetailsTable;
	}

	public XTextField getSurname() {
		if (surnameTextField == null) {
			surnameTextField = new XTextField();
			surnameTextField.setColumns(10);
			surnameTextField.setUpperCase(true);
			surnameTextField.setMinimumSize(surnameTextField.getPreferredSize());
			surnameTextField.addKeyListener(new DefendantAppellantSearchKeyListener());
		}
		return surnameTextField;
	}

	public XTextField getPrisonerNumber() {
		if (prisonerNumberTextField == null) {
			prisonerNumberTextField = new XTextField();
			prisonerNumberTextField.setColumns(10);
			prisonerNumberTextField.setUpperCase(true);
			prisonerNumberTextField.setMinimumSize(prisonerNumberTextField.getPreferredSize());
			prisonerNumberTextField.addKeyListener(new DefendantAppellantSearchKeyListener());
		}
		return prisonerNumberTextField;
	}

	public XTextField getFirstName() {
		if (firstNameTextField == null) {
			firstNameTextField = new XTextField();
			firstNameTextField.setColumns(10);
			firstNameTextField.setUpperCase(true);
			firstNameTextField.setMinimumSize(firstNameTextField.getPreferredSize());
			firstNameTextField.addKeyListener(new DefendantAppellantSearchKeyListener());
		}
		return firstNameTextField;
	}

	private class DefendantAppellantSearchKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {

		}

		@Override
		public void keyReleased(KeyEvent e) {
			btnSearch.setEnabled(validateTextFields());
		}

	}

	private class SearchDefendantAppellantAction extends XAction {

		private static final long serialVersionUID = 1L;

		public SearchDefendantAppellantAction(DefendantAppellantSearchPanel parent) {
			populateFromBundle("DefendantAppellantSearch");
			setCaller(parent);
		}

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			// TODO Auto-generated method stub
			// if there is no text entered in any of the text fields and no
			// gender selected
			if (!(getPrisonerNumberTextField().length() == 0)
					|| !(getFirstNameTextField().length() == 0) || !(getSurnameTextField().length() == 0)) {
				// search DB and add stuff to table
				searchDBBySurnameGenderFirstNameCourtId();

				// If there are results then select the first row
				if (defendantAppellantTable.getRowCount() > 0) {
					defendantAppellantTable.changeSelection(0, 0, false, false);
				}

				int row = defendantAppellantTable.getSelectedRow();
				// if a row is selected then enable the buttons, else
				// disable them
				if (-1 != row) {
					btnSelectDefendantAppellant.setEnabled(true);
					btnViewCaseDetails.setEnabled(true);
				} else {
					btnSelectDefendantAppellant.setEnabled(false);
					btnViewCaseDetails.setEnabled(false);
				}

				// CTX-1450: Enable add new defendant button only after
				// performing a search
				btnAddNewDefendantAppellant.setEnabled(true);
			}
		}

	}

	public void setTitleFromCaseType() {
		XhibitApplicationController xac = model.getXhibitApplicationController();

		if (xac.getCaseStatus().isCaseType(CaseType.APPEAL) || xac.getCaseStatus().isCaseType(CaseType.MISC)) {
			parentDialog.setTitle(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantSearch.mainTitleAppellant"));
		}
		if (xac.getCaseStatus().isCaseType(CaseType.TRIAL) || xac.getCaseStatus().isCaseType(CaseType.SENTENCE)) {
			parentDialog.setTitle(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantSearch.mainTitleDefendant"));
		}
	}

	@SuppressWarnings("unchecked")
	public void viewCaseDetails(Integer defendantId) {
		parentDialog.setTitle(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
				"defendantAppellantCaseDetails.mainTitle"));

		DefaultTableModel model = (DefaultTableModel) defendantAppellantCaseDetailsTable.getModel();

		ArrayList<DefendantOnCaseBasicValue> defendantOnCase = new ArrayList<DefendantOnCaseBasicValue>();
		DefendantControllerBeanBusinessDelegate del = XhibitDelegateHelper.getDefendantDelegate();

		try {
			defendantOnCase = (ArrayList<DefendantOnCaseBasicValue>) del.findByDefendantId(defendantId);

			// --- If we got results ---
			if (!defendantOnCase.isEmpty()) {
				for (DefendantOnCaseBasicValue defOnCase : defendantOnCase) {
					CaseBasicValue foundCases;
					CaseControllerBeanBusinessDelegate caseDelegate = XhibitDelegateHelper.getCaseDelegate();
					RefCourtBasicValue courtNames = null;

					foundCases = caseDelegate.getCase(defOnCase.getCaseID());
					BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();

					checkCaseDetailsData(foundCases, defOnCase, courtNames, bizRefDelegate);
					String liveStatus = caseDelegate.determineCaseStatus(foundCases.getCaseId());


					model.addRow(new Object[] { this.caseNumberFormatted, foundCases.getCaseTitle(),
							this.committalDateFormatted, this.appealLodgedDateFormatted, this.sentForTrialDateFormatted,
							this.courtName, liveStatus, this.bcStatusFormatted });

				}
			} else {
				log.debug("No cases found for defendant ID: " + defendantId);
			}
		} catch (Exception e) {
			XHIBITConstant.handleError(e);
		}
	}

	private void checkCaseDetailsData(CaseBasicValue foundCases, DefendantOnCaseBasicValue defOnCase,
			RefCourtBasicValue courtNames, BisRefControllerBeanBusinessDelegate bizRefDelegate) {

		String nullDataMessage = "";

		this.caseNumberFormatted = formatCaseNumber(foundCases);

		if (foundCases.getRefCourtID() != null) {
			try {
				courtNames = bizRefDelegate.findCourtByRefId(foundCases.getRefCourtID());
			} catch (Exception e) {
				XHIBITConstant.handleError(e);
			}
			this.courtName = courtNames.getCourtFullName();
		} else {
			this.courtName = nullDataMessage;
		}

		formatDates(foundCases, nullDataMessage);
		formatBcStatus(defOnCase, nullDataMessage);
	}

	private void formatDates(CaseBasicValue foundCases, String nullDataMessage) {

		String dateFormat = "dd-MMM-yyyy";

		if (foundCases.getCommittalDate() != null) {
			this.committalDateFormatted = (new SimpleDateFormat(dateFormat).format(foundCases.getCommittalDate()))
					.toUpperCase();
		} else {
			this.committalDateFormatted = nullDataMessage;
		}

		if (foundCases.getAppealLodgedDate() != null) {
			this.appealLodgedDateFormatted = (new SimpleDateFormat(dateFormat).format(foundCases.getAppealLodgedDate()))
					.toUpperCase();
		} else {
			this.appealLodgedDateFormatted = nullDataMessage;
		}

		if (foundCases.getSentForTrialDate() != null) {
			this.sentForTrialDateFormatted = (new SimpleDateFormat(dateFormat).format(foundCases.getSentForTrialDate()))
					.toUpperCase();
		} else {
			this.sentForTrialDateFormatted = nullDataMessage;
		}
	}

	private void formatBcStatus(DefendantOnCaseBasicValue defOnCase, String nullDataMessage) {
		if (defOnCase.getCurrentBcStatus() != null) {
			if (defOnCase.getCurrentBcStatus().equals("B")) {
				this.bcStatusFormatted = "Bail";
			} else if (defOnCase.getCurrentBcStatus().equals("C")) {
				this.bcStatusFormatted = "In Custody";
			} else {
				this.bcStatusFormatted = nullDataMessage;
			}
		} else {
			this.bcStatusFormatted = nullDataMessage;
		}
	}

	private String formatCaseNumber(CaseBasicValue foundCases) {
		return foundCases.getCaseType() + foundCases.getCaseNumber();
	}

	@SuppressWarnings("unchecked")
	protected void searchDBBySurnameGenderFirstNameCourtId() {
		DefaultTableModel model = (DefaultTableModel) defendantAppellantTable.getModel();
		if (model.getRowCount() > 0) {
			for (int i = model.getRowCount() - 1; i > -1; i--) {
				model.removeRow(i);
			}
		}

		ArrayList<DefendantValue> defendants = new ArrayList<DefendantValue>();
		try {
			Integer courtId = XhibitSingleton.getInstance().getCourtId();
			DefendantControllerBeanBusinessDelegate del = XhibitDelegateHelper.getDefendantDelegate();

			String firstName = getFirstNameTextField();
			String surname = getSurnameTextField();
			String prisonerNumber = getPrisonerNumberTextField();
			int gdr = getGender();

			if (firstName.isEmpty() || firstName == null) {
				firstName = "%";
			}
			if (surname.isEmpty() || surname == null) {
				surname = "%";
			}
			if (gdr == 3) {
				log.debug("search criteria is " + "FirstName: " + firstName + ", Surname: " + surname + ", "
						+ "PNumber:" + prisonerNumber);

				defendants = (ArrayList<DefendantValue>) del.findDefendantByCourtIdSurnameFirstNamePrisonerNumber(
						courtId, firstName, surname, prisonerNumber);

			} else {
				log.debug("search criteria is " + "FirstName: " + firstName + ", Surname: " + surname + ", Gender: "
						+ gdr + " , PNumber: " + prisonerNumber);

				defendants = (ArrayList<DefendantValue>) del.findDefendantByCourtIdSurnameGenderFirstNamePrisonerNumber(
						courtId, firstName, surname, gdr, prisonerNumber);
			}

			// --- If we got results ---
			if (!defendants.isEmpty()) {

				// --- Sort ---
				List<DefendantValue> defendantList = new ArrayList<DefendantValue>(defendants);
				Comparator<DefendantValue> defendantComparator = new Comparator<DefendantValue>() {
					@Override
					public int compare(DefendantValue o1, DefendantValue o2) {
						int rc = 0;
						if (o1.getSurName().equals(o2.getSurName())) {
							if (o1.getFirstName().equals(o2.getFirstName())) {
								if ((null == o1.getDateOfBirth()) || (null == o2.getDateOfBirth())) {
									rc = 0; // Assume both dates are null, so
											// equal
									if (null != o1.getDateOfBirth()) {
										rc = -1; // o1 not null, o1 comes before
													// o2
									}
									if (null != o2.getDateOfBirth()) {
										rc = 1; // o2 not null, o1 comes after
												// o2
									}
								} else {
									rc = o1.getDateOfBirth().compareTo(o2.getDateOfBirth());
								}
							} else {
								rc = o1.getFirstName().compareTo(o2.getFirstName());
							}
						} else {
							rc = o1.getSurName().compareTo(o2.getSurName());
						}
						return rc;
					}
				};
				Collections.sort(defendantList, defendantComparator);
				defendants = (ArrayList<DefendantValue>) defendantList;

				for (DefendantValue def : defendants) {

					Calendar dateOfBirth = def.getDateOfBirth();
					String dob = "";

					if (dateOfBirth != null) {
						dob = new SimpleDateFormat("dd/MM/yyyy").format(dateOfBirth.getTime());
					}
					AddressValue address = def.getAddressValue();
					model.addRow(new Object[] { def.getSurName(), def.getFirstName(), def.getGenderString(), dob,
							(def.getPrisonerNo() == null) ? "" : def.getPrisonerNo().getReferenceValue(),
							address.getAddress1(), def.getCurrentPrisonStatus(), def.getPrisonId(), def });
				}

				lblErrorMessage.setText(" ");

			} else {
				lblErrorMessage.setText(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
						"defendantAppellantSearch.noResultsFoundLabel"));
			}

		} catch (Exception e) {
			XHIBITConstant.handleError(e);
		}

	}

	// Inserted as a btnSearch.setEnabled(validateTextFields()) so that it will
	// enable button
	// if there are any valid text strings other than just a wildcard % operator
	// in the fields
	private boolean validateTextFields() {
		boolean validSearch = false;

		String str = "";

		str = surnameTextField.getText().replace("%", "");
		if (str.length() > 0) {
			validSearch = true;
		}

		str = firstNameTextField.getText().replace("%", "");
		if (str.length() > 0) {
			validSearch = true;
		}

		str = prisonerNumberTextField.getText().replace("%", "");
		if (str.length() > 0) {
			validSearch = true;
		}

		return validSearch;
	}

	/**
	 * @return the surnameTextField
	 */
	protected String getSurnameTextField() {
		return surnameTextField.getText();
	}

	/**
	 * @param surnameTextField
	 *            the surnameTextField to set
	 */
	protected void setSurnameTextField(XTextField surnameTextField) {
		this.surnameTextField = surnameTextField;
	}

	/**
	 * @return the prisonerNumberTextField
	 */
	protected String getPrisonerNumberTextField() {
		return prisonerNumberTextField.getText();
	}

	/**
	 * @param prisonerNumberTextField
	 *            the prisonerNumberTextField to set
	 */
	protected void setPrisonerNumberTextField(XTextField prisonerNumberTextField) {
		this.prisonerNumberTextField = prisonerNumberTextField;
	}

	/**
	 * @return the firstNameTextField
	 */
	protected String getFirstNameTextField() {
		return firstNameTextField.getText();
	}

	/**
	 * @param firstNameTextField
	 *            the firstNameTextField to set
	 */
	protected void setFirstNameTextField(XTextField firstNameTextField) {
		this.firstNameTextField = firstNameTextField;
	}

	protected int getGender() {
		String item = ((DropdownCodeStringValue) comboGender.getSelectedItem()).getCode();
		
		return new Integer(item);
	}

	private void showCancelConfirmationMsg() throws CSRecoverableException {
		boolean messageBoxReply = XMessageBox
				.alert(parentDialog,
						XHIBITConstant.getResource(XhibitBundles.XhibitActionResources,
								"DefendantAppellantCancelConfirmationTitle"),
						true, XMessageBox.ICONQUESTION,
						XHIBITConstant.getResource(XhibitBundles.XhibitActionResources,
								"DefendantAppellantCancelConfirmationMessage"),
						XMessageBox.YESNO, XMessageBox.DEFAULTCANCEL);

		if (!messageBoxReply) {
			throw new UserCancelException();
		}
	}

	private void showDuplicateDefendantMsg() throws CSRecoverableException {
		XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.XhibitActionResources, "DefendantDuplicateErrorTitle"), true,
				XMessageBox.ICONERROR,
				XHIBITConstant.getResource(XhibitBundles.XhibitActionResources, "DefendantDuplicateErrorMessage"),
				XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	}

	private void showDuplicateAppellantMsg() throws CSRecoverableException {
		XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.XhibitActionResources, "AppellantDuplicateErrorTitle"), true,
				XMessageBox.ICONERROR,
				XHIBITConstant.getResource(XhibitBundles.XhibitActionResources, "AppellantDuplicateErrorMessage"),
				XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	}

	/**
	 * If the model is not null then populate the fields with the values. Also
	 * sets the caret to 0 so that if the field is too long then it'll show the
	 * first half of the string instead of the end of the string.
	 */
	private void moveModelToScreen() {
		if (model != null) {
			if (model.getCallingClass() instanceof DefendantAppellantTab) {
				// Setting the title based on case type
				setTitleFromCaseType();
			}
		}
	}

	/**
	 * Set the data entered on screen into the model.
	 */
	private void moveScreenToModel() {
		DefaultTableModel tableModel = (DefaultTableModel) defendantAppellantTable.getModel();
		int row = defendantAppellantTable.getSelectedRow();
		if (model.getCallingClass() instanceof DefendantAppellantTab) {
			model.setDA(new DefendantAppellant());

			// Variables gathered from selected rows
			model.getDA().setFirstName(tableModel.getValueAt(row, 1).toString());
			model.getDA().setInitials(((DefendantValue) tableModel.getValueAt(row, 8)).getInitials());
			model.getDA().setSurname(tableModel.getValueAt(row, 0).toString());
			model.getDA().setDateOfBirth(tableModel.getValueAt(row, 3).toString());
			model.getDA().setDefendantId(((DefendantValue) tableModel.getValueAt(row, 8)).getDefendantID());
			model.getDA().setGender(tableModel.getValueAt(row, 2).toString());

			// parent/guardian is always the last column
			model.getDA().setParentGuardian(((DefendantValue) tableModel.getValueAt(row, 8)).getParentGuardianName());

			// set initial masked flag
			if (model.getDA().isJuvenile()) {
				model.getDA().setJuvenile(false);
			} else {
				model.getDA().setJuvenile(false);
			}

			model.setDV((DefendantValue) tableModel.getValueAt(row, 8));

			if (null != model.getDV().getCurrentPrisonStatus()) {
				model.getDA().setCurrentPrisonStatus(model.getDV().getCurrentPrisonStatus());
			}
		} else {
			model.setDV((DefendantValue) tableModel.getValueAt(row, 8));
		}
	}

	@Override
	public void stepInitialise() throws CSRecoverableException {

	}

	/**
	 * Default gridbag that's used throughout the panels.
	 * 
	 * @return gridbagconstraints
	 */
	private GridBagConstraints getGridBagLayout() {
		return new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				XHIBITConstant.nonContainerInsets, 0, 0);
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
	 * If save button clicked then check validation and then save the database
	 * changes
	 */
	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (btnViewCaseDetails.equals(getDeinitialiseSource())) {
			int row = defendantAppellantTable.getSelectedRow();
			DefaultTableModel model = (DefaultTableModel) defendantAppellantTable.getModel();
			searchPanel.setVisible(false);
			searchResultsPanel.setVisible(false);
			btnViewCaseDetails.setVisible(false);
			btnAddNewDefendantAppellant.setVisible(false);
			btnSelectDefendantAppellant.setVisible(false);
			btnCancel.setVisible(false);
			btnCaseDetailsBack.setVisible(true);
			GridBagConstraints gbc = getGridBagLayout();
			mainPanel.add(getCaseDetailsPanel(), gbc);
			caseDetailsPanel.setVisible(true);
			viewCaseDetails(((DefendantValue) model.getValueAt(row, 8)).getDefendantID());
		} else if (btnAddNewDefendantAppellant.equals(getDeinitialiseSource())) {
			if (model.getCallingClass() instanceof DefendantAppellantTab) {
				DefendantAppellantAddAmendDialog defendantAppellantAdd;
				defendantAppellantAdd = new DefendantAppellantAddAmendDialog(parentDialog.getParentFrame(),
						new DefendantAppellantAddAmendModel((DefendantAppellantTab) model.getCallingClass()),
						this.parentDialog);
				defendantAppellantAdd.setLocationRelativeTo(parentDialog.getParentFrame());
				defendantAppellantAdd.setVisible(true);
			} else {
				DefendantAppellantAddAmendDialog defendantAppellantAdd;
				defendantAppellantAdd = new DefendantAppellantAddAmendDialog(parentDialog.getParentFrame(),
						new DefendantAppellantAddAmendModel((REDELPanel) model.getCallingClass()), this.parentDialog);
				defendantAppellantAdd.setLocationRelativeTo(parentDialog.getParentFrame());
				defendantAppellantAdd.setVisible(true);
			}
		} else if (btnSelectDefendantAppellant.equals(getDeinitialiseSource())) {
			moveScreenToModel();
			if (model.getCallingClass() instanceof DefendantAppellantTab) {
				if (((DefendantAppellantTab) model.getCallingClass()).defendantAppellantIsDuplicate(model.getDA())) {
					XhibitApplicationController xac = model.getXhibitApplicationController();

					if (xac.getCaseStatus().isCaseType(CaseType.APPEAL)
							|| xac.getCaseStatus().isCaseType(CaseType.MISC)) {
						showDuplicateAppellantMsg();
					}
					if (xac.getCaseStatus().isCaseType(CaseType.TRIAL)
							|| xac.getCaseStatus().isCaseType(CaseType.SENTENCE)) {
						showDuplicateDefendantMsg();
					}
				} else {
					((DefendantAppellantTab) model.getCallingClass()).callback(model.getDA(), model.getDV(), true);
					parentDialog.dispose();
				}
			} else {
				if (((REDELPanel) model.getCallingClass()).detectDupliate(model.getDV())) {
					XhibitApplicationController xac = model.getXhibitApplicationController();

					if (xac.getCaseStatus().isCaseType(CaseType.APPEAL)
							|| xac.getCaseStatus().isCaseType(CaseType.MISC)) {
						showDuplicateAppellantMsg();
					}
					if (xac.getCaseStatus().isCaseType(CaseType.TRIAL)
							|| xac.getCaseStatus().isCaseType(CaseType.SENTENCE)) {
						showDuplicateDefendantMsg();
					}
				} else {
					((REDELPanel) model.getCallingClass()).callback(model.getDV());
					parentDialog.dispose();
				}
			}
		} else if (btnCaseDetailsBack.equals(getDeinitialiseSource())) {
			DefaultTableModel model = (DefaultTableModel) defendantAppellantCaseDetailsTable.getModel();
			model.setRowCount(0);
			setTitleFromCaseType();
			caseDetailsPanel.setVisible(false);
			searchPanel.setVisible(true);
			searchResultsPanel.setVisible(true);
			btnCaseDetailsBack.setVisible(false);
			btnViewCaseDetails.setVisible(true);
			btnAddNewDefendantAppellant.setVisible(true);
			btnSelectDefendantAppellant.setVisible(true);
			btnCancel.setVisible(true);
		} else {
			showCancelConfirmationMsg();
		}
	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {

	}

}
