package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.caseprosecutoragency.CaseProsecutorAgencyControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefProsecutorAgencyComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.caseprosecutoragency.CaseProsecutorAgencyValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/*@author - kudzinc - CTX-1870, 1871, 1827 04/05/2018 */

public class ProsecutorRespondentSearchPanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;
	private ProsecutorRespondentSearchModel model;
	private ProsecutorRespondentSearchDialog parentDialog;

	private final Logger log = CSServices.getLogger(getClass());
	private Integer courtId;

	// JPanels
	private JPanel searchPanel;
	private JPanel resultsPanel;
	private JPanel searchPanelButtons;
	private JPanel mainPanel;

	private CustomButtonPanel buttonPanel;

	// Text Fields
	private XTextField txtName;
	private XTextField txtCpsCode;

	// Tables
	private JTable tableSearchResults;

	// Buttons
	private JButton btnSearch;
	private JButton btnAddNew;
	private JButton btnSelectProsResp;
	private JButton btnDelete;
	private JButton btnUpdate;
	private JButton btnCancel;

	// Labels
	private JLabel lblIName;
	private JLabel lblName;
	private JLabel lblICpsCode;
	private JLabel lblCpsCode;
	private JLabel lblNoResults;
	private JLabel lblOr;

	private JScrollPane scrollPane;
	private Collection prosecutorAgencies = null;

	// Error messages
	private String noResultsFound = "<html>Search did not return any records.<br>\r\nPlease refine your search criteria.</html>";
	private String prosResp;
	private ProsecutorRespondentSearchPanel thisClass;
	private BisRefControllerBeanBusinessDelegate bizRefDelegate;
	private CaseProsecutorAgencyControllerBeanBusinessDelegate caseProsDelegate; 
	private CaseControllerBeanBusinessDelegate caseDelegate;
	

	public ProsecutorRespondentSearchPanel(ProsecutorRespondentSearchDialog parent,
			ProsecutorRespondentSearchModel model) throws CSRecoverableException {

		bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
		caseProsDelegate = XhibitDelegateHelper.getCaseProsecutorAgencyDelegate() ;
		caseDelegate = XhibitDelegateHelper.getCaseDelegate();
		courtId = XhibitSingleton.getInstance().getCourtId();
		this.model = model;
		this.parentDialog = parent;
		thisClass = this;
		jbInit();
	}

	/**
	 * Initialises the look and feel of the panel.
	 */
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(900, 600));
		moveModelToScreen();
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;

		JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainPanel.setPreferredSize(new Dimension(750, 500));
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 0, 0, 0);
		this.add(scrollPane, gbc);

		// top, left, bottom, right
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 10, 5, 10); // keep gap between panels

		gbc.weighty = 0.05;
		gbc.weightx = 0.95;
		mainPanel.add(getSearchPanel(), gbc);

		gbc.gridy += 1;
		gbc.weighty = 0.9;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		mainPanel.add(getResultsPanel(), gbc);

		gbc.gridy += 1;
		gbc.weighty = 0.1;
		gbc.anchor = GridBagConstraints.SOUTHEAST;
		gbc.fill = GridBagConstraints.NONE;
		buttonPanel = (CustomButtonPanel) parentDialog.getButtonPanel();

		if (prosResp.equals("Prosecutor")) {
			btnSelectProsResp = buttonPanel.addButton("prosecutorSelectButton", false, false);
			btnSelectProsResp.setEnabled(false);
			btnSelectProsResp.setVisible(true);
		} else if (prosResp.equals("Respondent")) {
			btnSelectProsResp = buttonPanel.addButton("respondentSelectButton", false, false);
			btnSelectProsResp.setEnabled(false);
			btnSelectProsResp.setVisible(true);
		}
		btnDelete = buttonPanel.addButton("prosecutorRespondentDeleteButton", false, false);
		btnUpdate = buttonPanel.addButton("prosecutorRespondentUpdateButton", false, false);
		btnCancel = buttonPanel.addButton("prosecutorRespondentCancelButton", false, false);

		btnDelete.setEnabled(false);
		btnUpdate.setEnabled(false);
		btnCancel.setEnabled(true);
		btnAddNew.setVisible(true);
		lblOr.setVisible(true);
	}

	private JPanel getSearchPanel() {
		if (searchPanel == null) {
			searchPanel = new JPanel();
		}
		searchPanel.setLayout(new GridBagLayout());
		searchPanel.setBorder(
				(BorderFactory.createTitledBorder(prosResp + " Search - Enter at least one field to search")));
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;

		// First Column and Search Button
		gbc.gridy = 2;
		gbc.weighty = 0.2;
		gbc.weightx = 0.05;
		lblName = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosecutorRespondent.nameLabel"));
		searchPanel.add(lblName, gbc);

		gbc.gridy += 2;
		lblCpsCode = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
				"prosecutorRespondent.cpsCodeLabel"));
		searchPanel.add(lblCpsCode, gbc);

		// Error Labels
		lblIName = new JLabel(" ");
		lblIName.setForeground(Color.RED);
		lblICpsCode = new JLabel(" ");
		lblICpsCode.setForeground(Color.RED);

		gbc.gridy = 2;
		gbc.gridx++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.95;
		gbc.weighty = 0.2;
		gbc.gridwidth = 2;
		searchPanel.add(getTxtName(), gbc);

		gbc.gridwidth = 1;
		gbc.gridx += 2;
		searchPanel.add(Box.createRigidArea(txtName.getPreferredSize()), gbc);
		gbc.gridx -= 2;

		gbc.gridy += 2;
		// gbc.weightx = 0.95;
		gbc.weighty = 0.2;
		gbc.gridwidth = 2;
		searchPanel.add(getTxtCpsCode(), gbc);

		// Gridbag for the error labels
		gbc.gridy = 1;
		gbc.gridx = 2;
		gbc.weighty = 0.1;
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.gridwidth = 5; // to make sure error labels dont move/get truncated
							// in grid bag
		searchPanel.add(lblIName, gbc);

		gbc.gridy += 2;
		searchPanel.add(lblICpsCode, gbc);

		// error label for no results found
		gbc.gridx++;
		gbc.gridy = 2;
		lblNoResults = new JLabel(" ");
		lblNoResults.setForeground(Color.RED);
		searchPanel.add(lblNoResults, gbc);

		// Add the buttons to the bottom of the panel
		gbc.gridy += 3;
		gbc.gridx = 0;
		gbc.gridwidth = 3;
		searchPanel.add(getSearchButtonsPanel(), gbc);

		return searchPanel;
	}

	private JPanel getSearchButtonsPanel() {
		if (searchPanelButtons == null) {
			searchPanelButtons = new JPanel();
		}
		searchPanelButtons.setLayout(new GridBagLayout());
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;

		gbc.weighty = 0.2;
		gbc.weightx = 0.05;

		btnSearch = new JButton(new SearchAction(this));
		btnSearch.setEnabled(false);
		searchPanelButtons.add(btnSearch, gbc);

		gbc.gridx++;
		lblOr = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosecutorRespondent.orLabel"));
		lblOr.setVisible(false);
		searchPanelButtons.add(lblOr, gbc);

		gbc.gridx++;
		btnAddNew = new JButton(new AddNewAction(this));
		btnAddNew.setVisible(false);
		searchPanelButtons.add(btnAddNew, gbc);

		return searchPanelButtons;
	}

	private JPanel getResultsPanel() {
		if (resultsPanel == null) {
			resultsPanel = new JPanel();
		}
		resultsPanel.setVisible(false);
		resultsPanel.setLayout(new GridBagLayout());
		resultsPanel.setBorder((BorderFactory.createTitledBorder("Search Results")));
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;

		gbc.weighty = 0.9;

		resultsPanel.add(Box.createRigidArea(txtName.getPreferredSize()), gbc);

		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		tableSearchResults = new JTable();
		tableSearchResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableSearchResults.setPreferredScrollableViewportSize(tableSearchResults.getPreferredSize());

		DefaultTableModel tableModel = new DefaultTableModel(new Object[][] {},
				new String[] { "Name", "CPS Code", "Address Line 1", "Post code", "Telephone Number" });
		tableSearchResults.setModel(tableModel);
		// --- Add a column to hold the prosecutor object, this column will
		// not
		// be displayed ---

		tableModel.addColumn("prosecutorResondent");

		tableSearchResults.getColumnModel().getColumn(tableModel.getColumnCount() - 1).setMinWidth(0);
		tableSearchResults.getColumnModel().getColumn(tableModel.getColumnCount() - 1).setMaxWidth(0);
		tableSearchResults.getColumnModel().getColumn(0).setPreferredWidth(250); // Name
		tableSearchResults.getColumnModel().getColumn(1).setPreferredWidth(75); // CPS
																				// Code
		tableSearchResults.getColumnModel().getColumn(2).setPreferredWidth(200); // Address
																					// Line
																					// 1
		tableSearchResults.getColumnModel().getColumn(3).setPreferredWidth(75); // Post
																				// code
		tableSearchResults.getColumnModel().getColumn(4).setPreferredWidth(125); // Telephone
																					// Number
		tableSearchResults.setDefaultEditor(Object.class, null);
		// Enable the Select Delete and Update button when a result is selected
		tableSearchResults.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (btnSelectProsResp != null) {
					btnSelectProsResp.setEnabled(!lsm.isSelectionEmpty());
				}
				btnDelete.setEnabled(!lsm.isSelectionEmpty());
				btnUpdate.setEnabled(!lsm.isSelectionEmpty());
			}

		});
		scrollPane.setViewportView(tableSearchResults);

		resultsPanel.add(scrollPane, gbc);

		return resultsPanel;
	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
	}

	@Override
	public void stepInitialise() throws CSRecoverableException {
		moveModelToScreen();
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
		moveScreenToModel();
		if (btnSelectProsResp != null && btnSelectProsResp.equals(getDeinitialiseSource())) {
			RefProsecutorAgencyComplexValue selectedProsecutor = getSelectedProsecutor();
			if (model.getCallingClass() instanceof ProsecutorRespondentTab) {
				// Move the screen to the model

				((ProsecutorRespondentTab) model.getCallingClass()).addProsecutorRespondent(selectedProsecutor, null);
			}
			parentDialog.dispose();
		}

		if (btnDelete != null && btnDelete.equals(getDeinitialiseSource())) {
			log.debug("delete button clicked");
			int confirmed = JOptionPane.showConfirmDialog(this, "Are you sure?", "Are you sure?",
					JOptionPane.YES_NO_OPTION);
			if (confirmed == 0) {
				RefProsecutorAgencyComplexValue selectedProsecutor = getSelectedProsecutor();

				if (prosecutorIsOnCase(selectedProsecutor.getRefProsecutorAgencyId())) {
					// Show message indicating prosecutor can't be deleted
					JOptionPane.showMessageDialog(this, "The record cannot be deleted as it is present on an Open Case",
							"Delete Prosecutor/Respondent", JOptionPane.ERROR_MESSAGE);
				} else {
					deleteProsecutorRepondent(selectedProsecutor);
					// Show deletion success message
					JOptionPane.showMessageDialog(this, "Record deleted successfully", "Delete Prosecutor/Respondent",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}

		if (btnUpdate != null && btnUpdate.equals(getDeinitialiseSource())) {
			try {
				RefProsecutorAgencyComplexValue selectedProsecutor = getSelectedProsecutor();
				ProsecutorRespondentAddAmendDialog dialog = new ProsecutorRespondentAddAmendDialog(
						model.getXhibitApplicationController(),
						new ProsecutorRespondentAddAmendModel(selectedProsecutor, thisClass));
				dialog.setLocationRelativeTo(model.getXhibitApplicationController());
				dialog.setVisible(true);
			} catch (CSRecoverableException e1) {
				XHIBITConstant.handleError(e1);
			}
		}

		if (btnCancel != null && btnCancel.equals(getDeinitialiseSource())) {
			parentDialog.dispose();
		}
	}

	private void moveScreenToModel() {
		if (model.getProsecutorRespondent() == null) {
			model.setProsecutorRespondent(new ProsecutorRespondent(null));
		}
		if (tableSearchResults.getSelectedRow() != -1) {
			model.setProsecutorRespondent(
					(ProsecutorRespondent) tableSearchResults.getValueAt(tableSearchResults.getSelectedRow(), 5));
		}
	}

	private void moveModelToScreen() {
		if (model != null) {
			if (model.getCallingClass() != null) {
				if (model.getCallingClass() instanceof ProsecutorRespondentTab) {
					if (model.getXhibitApplicationController() != null) {
						XhibitApplicationController xac = model.getXhibitApplicationController();
						if (xac.getCaseStatus().isCaseType(CaseType.TRIAL)
								|| xac.getCaseStatus().isCaseType(CaseType.SENTENCE)) {
							parentDialog.setTitle("Prosecutor Search");
							prosResp = "Prosecutor";
						} else {
							parentDialog.setTitle("Respondent Search");
							prosResp = "Respondent";
						}
					}
				}
			} else {
				prosResp = "Prosecutor/Respondent";
			}
		}
	}

	public XTextField getTxtName() {
		if (txtName == null) {
			txtName = new XTextField(35, "^[%]{0,1}[A-Za-z0-9]{1,35}[%]{0,1}$", lblIName, false);
		}
		txtName.setGridBagLayout(true);
		txtName.setMaxLength(35);
		txtName.setUpperCase(true);
		txtName.setColumns(10);
		txtName.setMinimumSize(txtName.getPreferredSize());
		txtName.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				validateTextFields();
			}

			public void removeUpdate(DocumentEvent e) {
				validateTextFields();
			}

			public void changedUpdate(DocumentEvent e) {
				validateTextFields();
			}
		});

		return txtName;
	}

	public void setTxtName(XTextField txtName) {
		this.txtName = txtName;
	}

	public XTextField getTxtCpsCode() {
		if (txtCpsCode == null) {
			txtCpsCode = new XTextField(4, "^[%]{0,1}[A-Za-z0-9]{1,4}[%]{0,1}$", lblICpsCode, false);
		}
		txtCpsCode.setGridBagLayout(true);
		txtCpsCode.setMaxLength(4);
		txtCpsCode.setUpperCase(true);
		txtCpsCode.setColumns(10);
		txtCpsCode.setMinimumSize(txtCpsCode.getPreferredSize());
		txtCpsCode.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				validateTextFields();
			}

			public void removeUpdate(DocumentEvent e) {
				validateTextFields();
			}

			public void changedUpdate(DocumentEvent e) {
				validateTextFields();
			}
		});
		return txtCpsCode;
	}

	public void setTxtCpsCode(XTextField txtCpsCode) {
		this.txtCpsCode = txtCpsCode;
	}

	public void addProsecutorRespondentToTable(ProsecutorRespondent prosResp) {
		DefaultTableModel model = (DefaultTableModel) tableSearchResults.getModel();
		AddressBasicValue address = prosResp.getAddress();
		model.addRow(new Object[] { prosResp.getFullName(), prosResp.getCpsCode(), address.getAddress1(),
				address.getPostcode(), prosResp.getTelephoneNumber(), prosResp });
		// Show the search results panel if calling from Reference Data
		// Prosecutor/Respondent Search
		resultsPanel.setVisible(true);
	}

	public Boolean prosecutorIsOnCase(Integer refProsecutorAgencyId) throws CaseControllerException, CSUnrecoverableException{
		Boolean casePresent = false;
			ArrayList<CaseProsecutorAgencyValue> prosecutorAgencies = (ArrayList<CaseProsecutorAgencyValue>) caseProsDelegate
					.getCaseProsecutorAgenciesByRefProsecutorId(refProsecutorAgencyId);

			if (!prosecutorAgencies.isEmpty()) {
				for (CaseProsecutorAgencyValue prosecutorAgency : prosecutorAgencies) {
					log.debug("Got case ID: " + prosecutorAgency.getCaseID() + " For refProsecutorAgencyID: "
							+ refProsecutorAgencyId);

					CaseBasicValue foundCases;

					foundCases = caseDelegate.getCase(prosecutorAgency.getCaseID());
					log.debug(
							"Case ID: " + prosecutorAgency.getCaseID() + " Case status: " + foundCases.getCaseStatus());
					if (foundCases.getCaseStatus() != null) {
						if (foundCases.getCaseStatus().equals("O")) {
							log.debug("Case ID: " + prosecutorAgency.getCaseID() + " is still open!");
							casePresent = true;
							break;
						} else if (foundCases.getCaseStatus().equals("C")) {
							log.debug("Case ID: " + prosecutorAgency.getCaseID() + " is closed!");
							casePresent = false;
						} else {
							log.debug("Case ID: " + prosecutorAgency.getCaseID() + " is neither open or closed!");
							casePresent = true;
							break;
						}
					} else {
						log.debug("WARNING: null case status found!");
						casePresent = true;
						break;
					}
				}
			} else {
				log.debug("No cases found for refProsecutorAgencyID: " + refProsecutorAgencyId);
				casePresent = false;
			}
		return casePresent;
	}

	private void validateTextFields() {
		boolean nameValid = false;
		boolean cpsCodeValid = false;

		String nameText = txtName.getText().replace("%", "");
		String cpsCodeText = txtCpsCode.getText().replace("%", "");

		String nameRegex = "^[A-Za-z0-9 ]{1,35}$";
		String cpsCodeRegex = "^[A-Za-z0-9]{1,4}$";

		// Both cps code and name are written into
		if (!txtCpsCode.getText().isEmpty() && !txtCpsCode.getText().equals("%") && !txtName.getText().isEmpty()
				&& !txtName.getText().equals("%")) {
			if (cpsCodeText.matches(cpsCodeRegex) && nameText.matches(nameRegex)) {
				btnSearch.setEnabled(true);
				nameValid = true;
				cpsCodeValid = true;
			} else {
				btnSearch.setEnabled(false);
				nameValid = nameText.matches(nameRegex);
				cpsCodeValid = cpsCodeText.matches(cpsCodeRegex);
			}
		} else { // only cps code
			if (!txtCpsCode.getText().isEmpty() && !txtCpsCode.getText().equals("%")) {
				if (cpsCodeText.matches(cpsCodeRegex)) { // valid
					btnSearch.setEnabled(true);
					cpsCodeValid = true;
					nameValid = true;
				} else {
					btnSearch.setEnabled(false);
					nameValid = true; // empty but not invalid
				}
			} else { // only name
				if (!txtName.getText().isEmpty() && !txtName.getText().equals("%")) {
					if (nameText.matches(nameRegex)) { // valid
						btnSearch.setEnabled(true);
						nameValid = true;
						cpsCodeValid = true;
					}
				} else {
					btnSearch.setEnabled(false);
					nameValid = nameText.matches(nameRegex);
				}
			}
		}
	}

	public void deleteProsecutorRepondent(RefProsecutorAgencyComplexValue selectedProsecutor) {
		int row = tableSearchResults.getSelectedRow();

		DefaultTableModel model = (DefaultTableModel) tableSearchResults.getModel();
		try {
			// Mark Prosecutor for deletion
			bizRefDelegate.deleteRefProsecutorAgency(selectedProsecutor,
					XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
			// Remove from display
			model.removeRow(row);

		} catch (Exception ex) {
			XHIBITConstant.handleError(ex);
		}
	}

	public RefProsecutorAgencyComplexValue getSelectedProsecutor() {
		DefaultTableModel model = (DefaultTableModel) tableSearchResults.getModel();
		ProsecutorRespondent selectedProsResp = (ProsecutorRespondent) (model
				.getValueAt(tableSearchResults.getSelectedRow(), model.getColumnCount() - 1));
		RefProsecutorAgencyComplexValue returnProsResp = null;
		returnProsResp = bizRefDelegate.findByRefProsecutorAgencyId(selectedProsResp.getRefProsecutorAgencyId());
		
		return returnProsResp;
	}

	public void addProsecutorCallSave(ProsecutorRespondent newProsecutor) {
		if (model.getCallingClass() != null) {
			if (model.getCallingClass() instanceof ProsecutorRespondentTab) {
				((ProsecutorRespondentTab) model.getCallingClass()).addProsecutorRespondent(newProsecutor, null);
				parentDialog.dispose();
			}
		}
	}
	
	/*ctx-2302*/
	public void updateProsecutorRespondentCallback(RefProsecutorAgencyComplexValue prosResp) {
		RefProsecutorAgencyComplexValue returnProsResp = null;
		int selected = tableSearchResults.getSelectedRow();
		DefaultTableModel model = (DefaultTableModel) tableSearchResults.getModel();
		
			returnProsResp = bizRefDelegate.findByRefProsecutorAgencyId(prosResp.getRefProsecutorAgencyId());
			ProsecutorRespondent newProsResp = new ProsecutorRespondent(returnProsResp, true);
			//Update the search results table with the new result
			tableSearchResults.setValueAt(newProsResp.getFullName(), selected, 0);
			tableSearchResults.setValueAt(newProsResp.getCpsCode(), selected, 1);
			tableSearchResults.setValueAt(newProsResp.getAddress().getAddress1(), selected, 2);
			tableSearchResults.setValueAt(newProsResp.getAddress().getPostcode(), selected, 3);
			tableSearchResults.setValueAt(newProsResp.getTelephoneNumber(), selected, 4);
			tableSearchResults.setValueAt(newProsResp, selected, model.getColumnCount() - 1);
	}

	private static GridBagConstraints getDefaultGridBagConstraints() {
		return new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				XHIBITConstant.nonContainerInsets, 0, 0);
	}

	/**
	 * Action for what happens when Add New button is clicked.
	 *
	 */
	private class AddNewAction extends XAction {

		private static final long serialVersionUID = 1L;

		public AddNewAction(ProsecutorRespondentSearchPanel parent) {
			if (prosResp.equals("Prosecutor")) {
				populateFromBundle("prosecutorAddNewButton");
			} else if (prosResp.equals("Respondent")) {
				populateFromBundle("respondentAddNewButton");
			} else {
				populateFromBundle("prosecutorRespondentAddNewButton");
			}
			setCaller(parent);
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			try {
				ProsecutorRespondentAddAmendDialog dialog = new ProsecutorRespondentAddAmendDialog(
						(XhibitApplicationController) parentDialog.getParentFrame(),
						new ProsecutorRespondentAddAmendModel(null, thisClass));
				dialog.setLocationRelativeTo((XhibitApplicationController) parentDialog.getParentFrame());
				dialog.setVisible(true);
			} catch (CSRecoverableException e1) {
				XHIBITConstant.handleError(e1);
			}

		}
	}

	/**
	 * Action for what happens when Search button is clicked.
	 *
	 */
	private class SearchAction extends XAction {

		private static final long serialVersionUID = 1L;

		public SearchAction(ProsecutorRespondentSearchPanel parent) {
			populateFromBundle("prosecutorRespondentSearchButton");
			setCaller(parent);
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
				lblNoResults.setVisible(false);
				// --- Clear existing data from table ---
				DefaultTableModel model = (DefaultTableModel) tableSearchResults.getModel();
				model.setRowCount(0);
				// --- Create storage for prosecutors in table ---
				LinkedList<ProsecutorRespondent> prosResp = new LinkedList<ProsecutorRespondent>();
				// --- Get results from DB ---
				prosecutorAgencies = new LinkedList<RefProsecutorAgencyComplexValue>();
					String c = "%";
					String n = "%";
					if (!txtName.isNullOrEmpty()) {
						n = txtName.getText();
					}
					if (!txtCpsCode.isNullOrEmpty()) {
						c = txtCpsCode.getText();
					}
					prosecutorAgencies = bizRefDelegate.findByCourtIdProsecutorNameAndCpsCode(courtId, n, c);
	
				// --- If we got results ---
				if (!prosecutorAgencies.isEmpty()) {
					Boolean isProsecutor;
					for (RefProsecutorAgencyComplexValue prosecutorAgency : (LinkedList<RefProsecutorAgencyComplexValue>) prosecutorAgencies) {
						isProsecutor = true;
						ProsecutorRespondent selectedProsecutor = new ProsecutorRespondent(prosecutorAgency,
								isProsecutor);
						selectedProsecutor.setRefProsecutorAgencyId(prosecutorAgency.getRefProsecutorAgencyId());
						selectedProsecutor.setTitle(prosecutorAgency.getTitle());
						selectedProsecutor.setInitials(prosecutorAgency.getInitials());
						selectedProsecutor.setProsecutorName1(prosecutorAgency.getProsecutorName1());
						selectedProsecutor.setProsecutorName2(prosecutorAgency.getProsecutorName2());
						selectedProsecutor.setProsecutorName3(prosecutorAgency.getProsecutorName3());
						selectedProsecutor.setFullName();
						selectedProsecutor.setCpsCode(prosecutorAgency.getCpsCode());
						selectedProsecutor.setAddress(prosecutorAgency.getAddress());
						selectedProsecutor.setTelephoneNumber(prosecutorAgency.getTelephoneNumber());
						selectedProsecutor.setFaxNumber(prosecutorAgency.getFaxNumber());
						selectedProsecutor.setNonsecureEmailAddress(prosecutorAgency.getNonsecureEmailAddress());
						selectedProsecutor.setSecureEmailAddress(prosecutorAgency.getSecureEmailAddress());
						selectedProsecutor.setDxRef(prosecutorAgency.getDxRef());
						prosResp.add(selectedProsecutor);
					}
				}
				if (prosResp.isEmpty()) {
					lblNoResults.setText(noResultsFound);
					lblNoResults.setVisible(true);
				} else {
					// --- Add new prosecutor/respondent details to table ---
					for (ProsecutorRespondent selectedProsecutor : prosResp) {
						addProsecutorRespondentToTable(selectedProsecutor);
					}
					tableSearchResults.setRowSelectionInterval(0, 0);
					resultsPanel.setVisible(true);
				}

		}
	}
}
