package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.FinderException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;


import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.casehistory.CaseHistoryControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.migration.MigrationDetail;
import uk.gov.courtservice.xhibit.business.services.migration.MigrationMessageType;
import uk.gov.courtservice.xhibit.business.services.monetaryordertracking.MonetaryOrderTrackingControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.monetaryordertracking.MonetaryOrderTrackingControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.casehistory.CaseHistoryValue;
import uk.gov.courtservice.xhibit.business.vos.services.caselinking.CaseLinkingValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCourtCriteria;
import uk.gov.courtservice.xhibit.client.admin.courtofappeal.CourtOfAppealDialog;
import uk.gov.courtservice.xhibit.client.listings.casesummary.CaseSummaryDialog;
import uk.gov.courtservice.xhibit.client.listings.casesummary.CaseSummaryModel;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableMouseListener;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class CaseSearchPanel extends XPanel {
	private static final long serialVersionUID = 1L;
	private CaseSearchModel caseSearchModel = null;
	private XDialog parent;
	// --- Search Panel ---
	private JPanel searchPanel = null;
	private Dimension searchSize = new Dimension(400,200);
	private JLabel lblCaseNumber = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitCaseSearch, "label.caseNumber"));
	private JLabel lblICaseNumber = new JLabel(" ");
	private XTextField txtCaseNumber = new XTextField(9, "^[A-Za-z]{1}[0-9]{8}$", lblICaseNumber, false);
	private JLabel lblDefendantSurname = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitCaseSearch, "label.defendantSurname"));
	private XTextField txtDefendantSurname = new XTextField(250);
	private JLabel lblDefendantFirstName = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitCaseSearch, "label.defendantFirstName"));
	private XTextField txtDefendantFirstName = new XTextField(250);
	
	private DocumentListener searchPanelDocListener;
	// --- Case Panel ---
	private JPanel casePanel = null; // Containing panel
	private Dimension caseSize = new Dimension(800, 400); 
	private JTable caseTable = null;
	private JScrollPane caseScrollPane = null;
	private Collection<CaseBasicValue> caseValues = null;
	private DefaultTableModel caseTableModel;
	private XTableMouseListener caseTableMouseListener;
	// --- Defendant Panel ---
	private JPanel defendantPanel = null; // Containing panel
	private Dimension defendantSize = new Dimension(900, 400); 
	private JTable defendantTable = null;
	private JScrollPane defendantScrollPane = null;
	private Collection<DefendantValue> defendantValues = null;
	private DefaultTableModel defendantTableModel;
	private XTableMouseListener defendantTableMouseListener;

	// --- Common ---
	private enum ActivePanel {
		CASE_PANEL, DEFENDANT_PANEL, SEARCH_PANEL,
	}

	private ActivePanel currentPanel;
	private XhibitApplicationController xac;
	private JButton btnBack;
	private JButton btnSummary;
	private JButton btnOk;
	private JButton btnCancel;
	private Integer caseId;
	private Integer courtId;
	private BisRefControllerBeanBusinessDelegate bisRefDelegate = null;
	private CaseControllerBeanBusinessDelegate caseDelegate = null;
	private DefendantControllerBeanBusinessDelegate defendantDelegate = null;
	private MonetaryOrderTrackingControllerBeanBusinessDelegate monetaryOrderTrackingDelegate = null;
	private CaseHistoryControllerBeanBusinessDelegate caseHistoryDel = null;
	private String caseType;
	private Integer caseNumber;
	private boolean indictmentSearch;
	private boolean redel;
	private boolean transferCase;
	private boolean appealCourt;
	private boolean deleteCase;
	private boolean caseLinking;
	private boolean caseUnlinkNeeded;
	private CaseBasicValue newCBV;
	private CourtOfAppealDialog courtAppealDialog = null;

	// *******************************************************************************
	// * public CaseSearchPanel(XDialog parent, CaseSearchModel caseSearchModel)
	// *
	// * Purpose : Constructor
	// * To call : parent - dialog parent class
	// * caseSearchModel - class model data
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	public CaseSearchPanel(XDialog parent, CaseSearchModel caseSearchModel) throws CSRecoverableException {
		this.parent = parent;
		this.caseSearchModel = caseSearchModel;
		stepInitialise();
		jbInit();
		if (parent.getParentFrame() != null) {
			xac = (XhibitApplicationController) parent.getParentFrame();
		}
	}

	// *******************************************************************************
	// * private void jbInit()
	// *
	// * Purpose : Add components to screen
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private void jbInit() {
		parent.setPreferredSize(new Dimension(400, 200));
		currentPanel = ActivePanel.SEARCH_PANEL;
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		// --- Add panels to dialog ---
		this.add(getSearchPanel(), gbc);
		searchPanel.setVisible(true);
		this.add(getCasePanel(), gbc);
		casePanel.setVisible(false);
		this.add(getDefendantPanel(), gbc);
		defendantPanel.setVisible(false);
		// --- Add custom buttons ---
		CustomButtonPanel buttonPanel = (CustomButtonPanel) parent.getButtonPanel();
		btnBack = buttonPanel.addButton("", false, true);
		btnBack.setText(
				ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "caseSearch.btnBack.btnText"));
		btnBack.setToolTipText(ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
				"caseSearch.btnBack.tooltipText"));
		btnBack.setVisible(false);
		btnSummary = buttonPanel.addButton("", false, false);
		btnSummary.setText(ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
				"caseSearch.btnSummary.btnText"));
		btnSummary.setToolTipText(ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,
				"caseSearch.btnSummary.tooltipText"));
		btnSummary.setVisible(false);
		btnOk = buttonPanel.addButton("btnOk", false, true);
		btnOk.setEnabled(false);
		btnCancel = buttonPanel.addButton("btnCancel", true, false);
		parent.getRootPane().setDefaultButton(btnOk);
		// --- Add document listeners for each panel ---
		searchPanelDocListener = new SearchPanelDocListener();
		txtCaseNumber.getDocument().addDocumentListener(searchPanelDocListener);
		txtDefendantSurname.getDocument().addDocumentListener(searchPanelDocListener);
		txtDefendantFirstName.getDocument().addDocumentListener(searchPanelDocListener);
		// --- Tell mouse listeners about the buttons ---
		caseTableMouseListener.setButton(btnOk);
		defendantTableMouseListener.setButton(btnOk);
		// --- Misc setup ---
		lblICaseNumber.setForeground(Color.RED);
	}

	// *******************************************************************************
	// * private GridBagConstraints getDefaultGridBagConstraints()
	// *
	// * Purpose : Get default set of gridbag constraints
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private GridBagConstraints getDefaultGridBagConstraints() {
		GridBagConstraints gbc = null;
		gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				XHIBITConstant.nonContainerInsets, 0, 0);
		return gbc;
	}

	// *******************************************************************************
	// * private JPanel getSearchPanel()
	// *
	// * Purpose : Get JPanel for case search
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private JPanel getSearchPanel() {
		if (null == searchPanel) {
			searchPanel = new JPanel();
			searchPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getDefaultGridBagConstraints();
			// --- Set initial positions ---
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;
			searchPanel.setBorder(new TitledBorder(null, XHIBITConstant.getResource(XhibitBundles.XhibitCaseSearch, "title.caseNumber"), TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
			// --- Add labels to layout ---
			gbc.gridy++;
			searchPanel.add(lblCaseNumber, gbc);
			if (caseSearchModel.getDeleteCase() == false) {
				gbc.gridy++;
				searchPanel.add(lblDefendantSurname, gbc);
				gbc.gridy++;
				searchPanel.add(lblDefendantFirstName, gbc);
				searchPanel.setBorder(new TitledBorder(null, XHIBITConstant.getResource(XhibitBundles.XhibitCaseSearch, "title.caseNumberDef"),
						TitledBorder.LEADING, TitledBorder.TOP, null, null));
			}
			// --- Add error labels to layout ---
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weighty = 0.1;
			searchPanel.add(lblICaseNumber, gbc);
			// --- Set constraints for text fields ---
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.weightx = 0.9;
			gbc.weighty = 0.2;
			// --- Add text fields ---
			txtCaseNumber.setColumns(10);
			txtCaseNumber.setUpperCase(true);
			txtCaseNumber.setMaxLength(9);
			txtCaseNumber.setGridBagLayout(true);
			txtCaseNumber.setMinimumSize(txtCaseNumber.getPreferredSize());
			txtCaseNumber.setInvalidText(XHIBITConstant.getResource(XhibitBundles.XhibitCaseSearch, "title.caseTypeNumber"));
			searchPanel.add(txtCaseNumber, gbc);
			if (caseSearchModel.getDeleteCase() == false) {
				gbc.gridy++;
				txtDefendantSurname.setColumns(10);
				txtDefendantSurname.setUpperCase(true);
				txtDefendantSurname.setMinimumSize(txtDefendantSurname.getPreferredSize());
				searchPanel.add(txtDefendantSurname, gbc);
				gbc.gridy++;
				txtDefendantFirstName.setColumns(10);
				txtDefendantFirstName.setUpperCase(true);
				txtDefendantFirstName.setMinimumSize(txtDefendantFirstName.getPreferredSize());
				searchPanel.add(txtDefendantFirstName, gbc);
			}
		}
		return searchPanel;
	}

	// *******************************************************************************
	// * private JPanel getCasePanel()
	// *
	// * Purpose : Get JPanel for case table
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private JPanel getCasePanel() {
		if (null == casePanel) {
			casePanel = new JPanel();
			casePanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getDefaultGridBagConstraints();
			gbc.weighty = 0.05;
			gbc.gridy++;

			gbc.weighty = 0.95;
			gbc.fill = GridBagConstraints.BOTH;
			casePanel.add(getCasePanelSearchResults(), gbc);
			casePanel.add(Box.createRigidArea(caseTable.getMinimumSize()), gbc);
		}
		return casePanel;
	}

	private JPanel getCasePanelSearchResults() {
		JPanel casePanelSearchResults = new JPanel();
		casePanelSearchResults.setLayout(new GridBagLayout());
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		casePanelSearchResults
				.setBorder((BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.XhibitCaseSearch, "title.caseSummary"))));
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 0.9;
		caseTable = new JTable();
		// --- Case table model ---
		caseTableModel = new DefaultTableModel(new Object[][] {},
				new String[] { "Case Number", "Case Title", "Sent / Committal Date", "Magistrates Court" }) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		// --- Mouse listener ---
		caseTableMouseListener = new XTableMouseListener(caseTable);
		caseTable.addMouseListener(caseTableMouseListener);
		// --- Add a column to hold the case object, this column will not be
		// displayed ---
		caseTableModel.addColumn("case");
		caseTable.setModel(caseTableModel);
		caseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		caseTable.getColumnModel().getColumn(caseTableModel.getColumnCount() - 1).setMinWidth(0);
		caseTable.getColumnModel().getColumn(caseTableModel.getColumnCount() - 1).setMaxWidth(0);
		// --- Set column widths ---
		caseTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Case
																		// number
		caseTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Case
																		// title
		caseTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Sent/Committal
																		// date
		caseTable.getColumnModel().getColumn(3).setPreferredWidth(300); // Magistrates
																		// court
		caseTable.setPreferredScrollableViewportSize(caseTable.getPreferredSize());
		caseScrollPane = new JScrollPane();
		caseScrollPane.setViewportView(caseTable);
		casePanelSearchResults.add(caseScrollPane, gbc);

		return casePanelSearchResults;

	}

	// *******************************************************************************
	// * private JPanel getDefendantPanel()
	// *
	// * Purpose : Get JPanel for defendant panel
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private JPanel getDefendantPanel() {
		if (null == defendantPanel) {
			defendantPanel = new JPanel();
			defendantPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getDefaultGridBagConstraints();
			defendantPanel.setBorder((BorderFactory
					.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.XhibitCaseSearch, "title.multipleDef"))));
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weighty = 0.9;
			defendantTable = new JTable();
			// --- Defendant table model ---
			defendantTableModel = new DefaultTableModel(new Object[][] {}, new String[] { "Surname", "First Name",
					"Sex", "DOB", "Address Line 1", "Postcode", "Prisoner Number", "In Custody?", "Prison Id" }) {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			defendantTable.setModel(defendantTableModel);
			// --- Mouse listener ---
			defendantTableMouseListener = new XTableMouseListener(defendantTable);
			defendantTable.addMouseListener(defendantTableMouseListener);
			// --- Add a column to hold the defendant object, this column will
			// not be displayed ---
			defendantTableModel.addColumn("defendant");
			defendantTable.setModel(defendantTableModel);
			defendantTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			defendantTable.getColumnModel().getColumn(defendantTableModel.getColumnCount() - 1).setMinWidth(0);
			defendantTable.getColumnModel().getColumn(defendantTableModel.getColumnCount() - 1).setMaxWidth(0);
			// --- Set column widths ---
			defendantTable.getColumnModel().getColumn(0).setPreferredWidth(150); // Surname
			defendantTable.getColumnModel().getColumn(1).setPreferredWidth(150); // First
																					// name
			defendantTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Gender
			defendantTable.getColumnModel().getColumn(3).setPreferredWidth(150); // DOB
			defendantTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Address
																					// line
																					// 1
			defendantTable.getColumnModel().getColumn(5).setPreferredWidth(150); // Postcode
			defendantTable.getColumnModel().getColumn(6).setPreferredWidth(150); // Prisoner
																					// number
			defendantTable.getColumnModel().getColumn(7).setPreferredWidth(150); // In
																					// custody
			defendantTable.getColumnModel().getColumn(8).setPreferredWidth(150); // Prison
																					// id
			defendantTable.setPreferredScrollableViewportSize(defendantTable.getPreferredSize());
			defendantScrollPane = new JScrollPane();
			defendantScrollPane.setViewportView(defendantTable);
			defendantPanel.add(defendantScrollPane, gbc);
		}
		return defendantPanel;
	}

	// *******************************************************************************
	// * private void SetActivePanel(ActivePanel activePanel)
	// *
	// * Purpose : Update dialog with panel for specified type
	// * To call : activePanel - specifies type of panel to make active
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private void setActivePanel(ActivePanel activePanel) {
		if (parent.isVisible()) {
			switch (activePanel) {
			case CASE_PANEL:
				casePanel.setVisible(true);
				defendantPanel.setVisible(false);
				searchPanel.setVisible(false);
				btnBack.setVisible(true);
				btnSummary.setVisible(true);
				parent.setSize(caseSize);
				parent.setLocationRelativeTo(parent.getParentFrame());
				parent.setTitle(XHIBITConstant.getResource(XhibitBundles.XhibitCaseSearch, "title.selectCase"));
				break;
			case DEFENDANT_PANEL:
				casePanel.setVisible(false);
				defendantPanel.setVisible(true);
				searchPanel.setVisible(false);
				btnBack.setVisible(true);
				btnSummary.setVisible(false);
				parent.setSize(defendantSize);
				parent.setLocationRelativeTo(parent.getParentFrame());
				parent.setTitle(XHIBITConstant.getResource(XhibitBundles.XhibitCaseSearch, "title.selectDefendant"));
				break;
			case SEARCH_PANEL:
				casePanel.setVisible(false);
				defendantPanel.setVisible(false);
				searchPanel.setVisible(true);
				btnBack.setVisible(false);
				btnSummary.setVisible(false);
				parent.setSize(searchSize);
				parent.setLocationRelativeTo(parent.getParentFrame());
				parent.setTitle(XHIBITConstant.getResource(XhibitBundles.XhibitCaseSearch, "title.searchForCase"));
				break;
			}
			// --- Set current panel type ---
			currentPanel = activePanel;
		}
	}

	// *******************************************************************************
	// * private void Screen()
	// *
	// * Purpose : Update local data from model
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private void moveModelToScreen() {
		caseId = caseSearchModel.getCaseId();
		indictmentSearch = caseSearchModel.getIndictmentSearch();
		redel = caseSearchModel.getREDEL();
		transferCase = caseSearchModel.getTransferCase();
		caseType = caseSearchModel.getCaseType();
		caseNumber = caseSearchModel.getCaseNumber();
		appealCourt = caseSearchModel.getAppealCourt();
		deleteCase = caseSearchModel.getDeleteCase();
		courtId = caseSearchModel.getCourtId();
		caseLinking = caseSearchModel.getCaseLinking();
		caseUnlinkNeeded = caseSearchModel.isCaseUnlinkNeeded();
	}

	// *******************************************************************************
	// * private void moveScreenToModel
	// *
	// * Purpose : Update model from local data
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private void moveScreenToModel() {
		caseSearchModel.setCaseId(caseId);
		caseSearchModel.setIndictmentSearch(indictmentSearch);
		caseSearchModel.setREDEL(redel);
		caseSearchModel.setTransferCase(transferCase);
		caseSearchModel.setCaseType(caseType);
		caseSearchModel.setCaseNumber(caseNumber);
		caseSearchModel.setDeleteCase(deleteCase);
		caseSearchModel.setCourtId(courtId);
		caseSearchModel.setCaseLinking(caseLinking);
		caseSearchModel.setCaseUnlinkNeeded(caseUnlinkNeeded);
	}

	// *******************************************************************************
	// * public void stepInitialise()
	// *
	// * Purpose :
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	@Override
	public void stepInitialise() throws CSRecoverableException {
		bisRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
		caseDelegate = XhibitDelegateHelper.getCaseDelegate();
		defendantDelegate = XhibitDelegateHelper.getDefendantDelegate();
		courtId = XhibitSingleton.getInstance().getCourtId();
		monetaryOrderTrackingDelegate = XhibitDelegateHelper.getMonetaryOrderTrackingDelegate();
		caseHistoryDel = XhibitDelegateHelper.getCaseHistoryDelegate();

	}

	// *******************************************************************************
	// * public void stepActivate()
	// *
	// * Purpose :
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	@Override
	public void stepActivate() throws CSRecoverableException {
		moveModelToScreen();
	}

	// *******************************************************************************
	// * public void stepUpdateViewState()
	// *
	// * Purpose :
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
	}

	// *******************************************************************************
	// * public void stepValidate()
	// *
	// * Purpose :
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
	}

	// *******************************************************************************
	// * public void stepDeactivate()
	// *
	// * Purpose :
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	@Override
	public void stepDeactivate() throws CSRecoverableException {
	}

	// *******************************************************************************
	// * public void stepDeinitialise(boolean update)
	// *
	// * Purpose : update -
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	@Override
	public void stepDeinitialise(boolean update)  {
		Integer selectedRow;
		Integer selectedColumn;

		courtId = XhibitSingleton.getInstance().getCourtId();
		// === OK Button ===
		if (btnOk.equals(getDeinitialiseSource())) {
			switch (currentPanel) {
			case SEARCH_PANEL:
				// --- Search for cases ---
				if (isEntriesValid()) {
					if (!txtCaseNumber.getText().isEmpty()) {
						caseType = txtCaseNumber.getText().substring(0, 1).toUpperCase();
						String caseNumberString = txtCaseNumber.getText().substring(1,
								txtCaseNumber.getText().length());
						caseNumber = Integer.valueOf(caseNumberString);
						try {
							if (txtDefendantSurname.getText().isEmpty()) {
								try {
									newCBV = caseDelegate.findCase(caseType, caseNumber, courtId);
								} catch (CaseControllerException ex) {
									//case controller thrown means that there is no case ,now need to check the case history table 
									CaseHistoryValue historyValue = caseHistoryDel.findByCaseNumberCaseTypeAndCourtId(caseType, caseNumberString, courtId);
									if(historyValue!=null &&historyValue.getCaseHistoryId()!=null) {
										caseHistoryFound();
										return;
									} else {
										throw ex;
									}	
								}
								
							} else {
								caseValues = caseDelegate.findByDefendantNameAndCaseNumber( txtDefendantFirstName.getText(),
										txtDefendantSurname.getText(),caseType, caseNumberString, courtId);
								if (1 == caseValues.size()) {
									newCBV = caseValues.iterator().next();
								} else {
									if(caseValues.size()>0) {
									throw new CaseControllerException("case.multipleCases", new Object[] { caseNumber.toString(), caseType },
											"Found Multiple results for  = " + caseNumber.toString() + " caseType = " + caseType
													+ " courtId = " + courtId);
									} else {
										CaseHistoryValue historyValue = caseHistoryDel.findByCaseNumberCaseTypeAndCourtId(caseType, caseNumberString, courtId);
										if(historyValue!=null &&historyValue.getCaseHistoryId()!=null) {
											caseHistoryFound();
											return;
										} else {
											caseNotFound();
											return;										
										}
									}
								}
							}
							caseId = newCBV.getCaseId();							
							if (appealCourt) {
								try {
									if ("T".equals(caseType) || "S".equals(caseType)) {	
										showCourtAppeal();
										parent.dispose();
									} else {
										showErrorPopup();
										return;
									}
								} catch (CSRecoverableException ex) {
									XHIBITConstant.handleError(ex);
									return;
								}
							}
							
							if(checkValidationForCase()) {
								moveScreenToModel();
								parent.dispose();
							} else {
								return;
							}
						} catch (CaseControllerException ex) {
							caseNotFound();
							return;
						} catch (FinderException e) {
							XHIBITConstant.handleError(e);
							return;
						}
					} else {
						// --- Search on defendant ---
						try {
							if(txtDefendantFirstName.getText()!=null && !txtDefendantFirstName.getText().equals("")) {
								defendantValues = defendantDelegate.findDefendantByCourtIdFirstNameSurname(courtId,
										txtDefendantFirstName.getText(),
										txtDefendantSurname.getText());
							} else {
								defendantValues = defendantDelegate.findDefendantByCourtIdSurname(courtId,
										txtDefendantSurname.getText());
							}
							if (defendantValues.size() > 0) {
								// --- Sort ---
								List<DefendantValue> defendantList = new ArrayList<DefendantValue>(defendantValues);
								Comparator<DefendantValue> defendantComparator = new Comparator<DefendantValue>() {
									@Override
									public int compare(DefendantValue o1, DefendantValue o2) {
										if (o1.getSurName().equals(o2.getSurName())) {
											if (o1.getFirstName().equals(o2.getFirstName())) {
												if(o1.getDateOfBirth()!=null && o2.getDateOfBirth()!=null) {
													return o1.getDateOfBirth().compareTo(o2.getDateOfBirth());
												} else {
													if(o1.getDateOfBirth()==null) {
														return 1;
													} else {
														return -1;
													}
												}
											} else {
												if(!o1.getFirstName().equals("") && !o2.getFirstName().equals("")) {
													return o1.getFirstName().compareTo(o2.getFirstName());
												} else {
													if(o1.getFirstName().equals("")) {
														return 1;
													} else {
														return -1;
													}
												}
											}
										} else {
											if(!o1.getSurName().equals("") && !o2.getSurName().equals("")) {
												return o1.getSurName().compareTo(o2.getSurName());
											} else {
												if(o1.getSurName().equals("")) {
													return 1;
												} else {
													return -1;
												}
											}
										}
									}
								};
								Collections.sort(defendantList, defendantComparator);
								defendantValues = defendantList;
								// ---
								for (DefendantValue defendantValue : defendantValues) {
									String defSurname = defendantValue.getSurName() == null ? ""
											: defendantValue.getSurName();
									String defFirstName = defendantValue.getFirstName() == null ? ""
											: defendantValue.getFirstName();
									String defGenderString = defendantValue.getGenderString() == null ? ""
											: defendantValue.getGenderString();
									String defDateOfBirth = "";
									if (null != defendantValue.getDateOfBirth()) {
										SimpleDateFormat defDateOfBirthFormat = new SimpleDateFormat("dd-MMM-yyyy");
										defDateOfBirth = defDateOfBirthFormat
												.format(defendantValue.getDateOfBirth().getTime());
									}
									String defAddress1 = defendantValue.getAddressValue() == null ? ""
											: ((defendantValue.getAddressValue().getAddress1() == null) ? ""
													: defendantValue.getAddressValue().getAddress1());
									String defPostcode = defendantValue.getAddressValue() == null ? ""
											: ((defendantValue.getAddressValue().getPostcode() == null) ? ""
													: defendantValue.getAddressValue().getPostcode());
									String defPrisonerNo = defendantValue.getPrisonerNo() == null ? ""
											: defendantValue.getPrisonerNo().getDefendantID().toString();
									String defInCustody = defendantValue.getCurrentPrisonStatus() == null ? ""
											: defendantValue.getCurrentPrisonStatus();
									String defPrisonId = defendantValue.getPrisonId() == null ? ""
											: defendantValue.getPrisonId();
									defendantTableModel.addRow(new Object[] { defSurname, defFirstName, defGenderString,
											defDateOfBirth, defAddress1, defPostcode, defPrisonerNo, defInCustody,
											defPrisonId, defendantValue });
								}
								setActivePanel(ActivePanel.DEFENDANT_PANEL);
								defendantTable.setRowSelectionInterval(0, 0);
							} else {
								caseNotFound();
								return;
							}
						} catch (Exception ex) {
							XHIBITConstant.handleError(ex);
							return;
						}
					}
					if (defendantValues==null || (defendantValues !=null && defendantValues.size()!=1)) {
						return;
					}
				} else if(!isValidCaseType(txtCaseNumber.getText().substring(0,1).toUpperCase())) {
					caseNotFound();
					return;
				}
				
			case DEFENDANT_PANEL:
				selectedRow = defendantTable.getSelectedRow();
				selectedColumn = defendantTable.getColumnModel().getColumnCount() - 1;

				if (selectedRow < 0) {
					return;
				}

				Integer defendantId = ((DefendantValue) defendantTable.getValueAt(selectedRow, selectedColumn))
						.getDefendantID();
				try {
					caseValues = caseDelegate.findAllCasesByDefendantId(defendantId, courtId);
					if (caseValues.size() > 0) {
						// --- Sort ---
						List<CaseBasicValue> caseList = new ArrayList<CaseBasicValue>(caseValues);
						Comparator<CaseBasicValue> comparator = new Comparator<CaseBasicValue>() {
							@Override
							public int compare(CaseBasicValue o1, CaseBasicValue o2) {
								return (o1.getCaseNumber() - o2.getCaseNumber());
							}
						};
						Collections.sort(caseList, comparator);
						caseValues = caseList;
						// --- Add returned entries to table ---
						for (CaseBasicValue caseValue : caseValues) {
							boolean addCase = false;
							String caseType = caseValue.getCaseType() == null ? "" : caseValue.getCaseType();
							if (!isValidCaseType(caseType)) {
								continue;
							}
							String caseNumber = caseValue.getCaseNumber() == null ? ""
									: caseValue.getCaseNumber().toString();
							String caseTitle = caseValue.getCaseTitle() == null ? "" : caseValue.getCaseTitle();
							String caseSentCommDate = "";
							if (null != caseValue.getCommittalDate()) {
								SimpleDateFormat caseSentCommDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
								caseSentCommDate = caseSentCommDateFormat
										.format(caseValue.getCommittalDate().getTime());
							}
							if (null != caseValue.getSentForTrialDate()) {
								SimpleDateFormat caseSentCommDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
								caseSentCommDate = caseSentCommDateFormat
										.format(caseValue.getSentForTrialDate().getTime());
							}
							String caseMagistratesCourt = "";
							if (null != caseValue.getRefCourtID()) {
								// --- Get courts for case ---
								try {
									RefCourtCriteria refCourtCriteria = new RefCourtCriteria();
									refCourtCriteria.setPrimaryKey(caseValue.getRefCourtID());
									ArrayList<RefCourtComplexValue> courtCollection = (ArrayList<RefCourtComplexValue>) bisRefDelegate
											.findCourts(refCourtCriteria);
									if (courtCollection.size() > 0) {
										RefCourtComplexValue court = courtCollection.get(0);
										caseMagistratesCourt = court.getCourtFullName();
									}
								} catch (Exception ex) {
									JOptionPane.showMessageDialog(parent,
											XHIBITConstant.getResource(XhibitBundles.XhibitCaseSearch, "error.courtDetails"),
											XHIBITConstant.getResource(XhibitBundles.Listings, "caseSearchOKConfirmationTitle"),
											JOptionPane.INFORMATION_MESSAGE);
									return; 
								}
							}
							if (null != caseValue.getCaseId()) {
									Collection<DefendantOnCaseBasicValue> defendantsOnCase = (ArrayList<DefendantOnCaseBasicValue>) defendantDelegate
											.findByCaseId(caseValue.getCaseId());
									// ---
									for (DefendantOnCaseBasicValue defendantOnCase : defendantsOnCase) {
										if (defendantOnCase.getDefendantID().equals(defendantId)) {
											addCase = true;
										}
									}
									if (transferCase) {
										for (DefendantOnCaseBasicValue defendantOnCase : defendantsOnCase) {
											if ((null != defendantOnCase.getResultsVerified())
													&& defendantOnCase.getResultsVerified().equals("E")) {
												addCase = false;
											}
										}
									}
							}
							if (addCase) {
								caseTableModel.addRow(new Object[] { caseType + caseNumber, caseTitle, caseSentCommDate,
										caseMagistratesCourt, caseValue });
							}
						}
					}
				} catch (CaseControllerException ex) {
					XHIBITConstant.handleError(ex);
					return;
				}
				if (caseTableModel.getRowCount()==0) {
					JOptionPane.showMessageDialog(parent, XHIBITConstant.getResource(XhibitBundles.XhibitCaseSearch, "error.caseNotFound"));
				} else if (caseTableModel.getRowCount() == 1) {
						caseId = ((CaseBasicValue) caseTable.getValueAt(0, caseTable.getColumnModel().getColumnCount() - 1))
								.getCaseId();
						caseType = ((CaseBasicValue) caseTable.getValueAt(0,
								caseTable.getColumnModel().getColumnCount() - 1)).getCaseType();
						caseNumber = ((CaseBasicValue) caseTable.getValueAt(0,
								caseTable.getColumnModel().getColumnCount() - 1)).getCaseNumber();
						newCBV = ((CaseBasicValue) caseTable.getValueAt(0, caseTable.getColumnModel().getColumnCount() - 1));

						if (appealCourt) {
							try {
								if ("T".equals(caseType) || "S".equals(caseType)) {	
									showCourtAppeal();
									parent.dispose();
								} else {
									caseTableModel.setRowCount(0);
									showErrorPopup();
									return;
								}
							} catch (CSRecoverableException ex) {
								XHIBITConstant.handleError(ex);
								return;
							}
						}
						if(checkValidationForCase()){
							moveScreenToModel();
							parent.dispose();
						} else {
							caseTableModel.setRowCount(0);
							return;
						}

					} else {
						caseTable.setRowSelectionInterval(0, 0);
						setActivePanel(ActivePanel.CASE_PANEL);
					}
				break;
			case CASE_PANEL:
				selectedRow = caseTable.getSelectedRow();
				selectedColumn = caseTable.getColumnModel().getColumnCount() - 1;
				caseId = ((CaseBasicValue) caseTable.getValueAt(selectedRow, selectedColumn)).getCaseId();
				caseType = ((CaseBasicValue) caseTable.getValueAt(selectedRow, selectedColumn)).getCaseType();
				caseNumber = ((CaseBasicValue) caseTable.getValueAt(selectedRow, selectedColumn)).getCaseNumber();
				newCBV = (CaseBasicValue) caseTable.getValueAt(selectedRow, selectedColumn);
				if (appealCourt) {
					try {
						if ("T".equals(caseType) || "S".equals(caseType)) {	
							showCourtAppeal();
							parent.dispose();
						} else {
							showErrorPopup();
							return;
						}

					} catch (CSRecoverableException ex) {
						XHIBITConstant.handleError(ex);
						return;
					}
				} 
				if(checkValidationForCase()){
					moveScreenToModel();
					parent.dispose();
				}
				break;
			}
		}
		// === Back Button ===
		else if (btnBack.equals(getDeinitialiseSource())) {
			switch (currentPanel) {
			case SEARCH_PANEL:
				break;
			case DEFENDANT_PANEL:
				defendantTableModel.setRowCount(0);
				setActivePanel(ActivePanel.SEARCH_PANEL);
				break;
			case CASE_PANEL:
				caseTableModel.setRowCount(0);
				if(defendantTableModel.getRowCount()>1){
					setActivePanel(ActivePanel.DEFENDANT_PANEL);
				} else {
					defendantTableModel.setRowCount(0);
					setActivePanel(ActivePanel.SEARCH_PANEL);
				}
				break;
			}
		}
		// === Case Summary Button ===
		else if (btnSummary.equals(getDeinitialiseSource())) {
			selectedRow = caseTable.getSelectedRow();
			selectedColumn = caseTable.getColumnModel().getColumnCount() - 1;

			if (((CaseBasicValue) caseTable.getValueAt(selectedRow, selectedColumn)).getCaseId() != null) {
				CaseSummaryModel model = new CaseSummaryModel(
						((CaseBasicValue) caseTable.getValueAt(selectedRow, selectedColumn)).getCaseId());
				CaseSummaryDialog dialog;
				try {
					dialog = new CaseSummaryDialog(xac, model);
					dialog.setLocationRelativeTo(parent.getParentFrame());
					dialog.setVisible(true);
				} catch (CSRecoverableException ex) {
					XHIBITConstant.handleError(ex);
					return;
				}
			}
		}
		// === Cancel Button ===
		else if (btnCancel.equals(getDeinitialiseSource())
				|| ((JButton) getDeinitialiseSource()).getText().equals("Cancel")) {
			caseSearchModel.clearmodel();
		}
	}
	
	private void showCourtAppeal() throws CSRecoverableException {
		MigrationDetail migrationDetail = XhibitDelegateHelper.getMigrateCaseDelegate()
				.getMigrationDetails(caseId, MigrationMessageType.EDIT);
		if (migrationDetail != null && migrationDetail.isMigrated) {
			// Case is migrated so display migrated panel and appropriate message
			new CaseMigratedPopup(xac, migrationDetail.migrationTo).setVisible(true);
		}
		else {
			courtAppealDialog = new CourtOfAppealDialog(xac, newCBV);
			courtAppealDialog.setLocationRelativeTo(parent.getParentFrame());
			courtAppealDialog.setVisible(true);
		}		
	}

	private void caseNotFound() {
		if (caseSearchModel.getDeleteCase()) {
			showErrorPopup();
		} else {
			caseSearchModel.setCaseId(0);
			JOptionPane.showMessageDialog(parent,
					XHIBITConstant.getResource(XhibitBundles.Listings, "caseSearchOKConfirmationMessage"),
					XHIBITConstant.getResource(XhibitBundles.Listings, "caseSearchOKConfirmationTitle"),
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private void caseHistoryFound() {
		caseSearchModel.setCaseId(0);
		JOptionPane.showMessageDialog(parent,
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseHistoryConfirmationMessage"),
				XHIBITConstant.getResource(XhibitBundles.Listings, "caseHistoryConfirmationTitle"),
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	
	private void showErrorPopup() { 
		caseSearchModel.setCaseId(0);
		if (caseSearchModel.getDeleteCase()) {
			JOptionPane.showMessageDialog(parent,
					XHIBITConstant.getResource(XhibitBundles.Listings, "caseDeleteErrorMessage"),
					XHIBITConstant.getResource(XhibitBundles.Listings, "caseSearchOKConfirmationTitle"),
					JOptionPane.INFORMATION_MESSAGE);
		} else if(caseSearchModel.getAppealCourt()) {
			JOptionPane.showMessageDialog(parent,
					XHIBITConstant.getResource(XhibitBundles.XhibitCaseSearch, "error.appealCaseMessage"),
					XHIBITConstant.getResource(XhibitBundles.XhibitCaseSearch, "error.appealCaseTitle"),
					JOptionPane.ERROR_MESSAGE);
		} else if (caseSearchModel.isCaseUnlinkNeeded()) {
			String titleString = "Case Number: " + caseType + caseNumber;
			JOptionPane.showMessageDialog((Component) null, "The case is not linked to any others", 
					  titleString, JOptionPane.INFORMATION_MESSAGE);
		} else if(caseSearchModel.isMonetaryOrder()){
			JOptionPane.showMessageDialog(null,
    				"No Monetary Orders exist for case "+ caseType + caseNumber,
    				"Monetary Order Acknowledgement", JOptionPane.INFORMATION_MESSAGE);
		} 
	}
	private boolean isValidCaseType(String caseType) {
		boolean isValid = true;
		if (caseSearchModel.getValidCaseTypes() != null) {
			isValid = caseSearchModel.getValidCaseTypes().contains(caseType);
		}
		return isValid;
	}
	
	/**
	 * Returns true if its a valid case number or if case number is empty and it has a defendant name.
	 * @param txtCaseNumber2 caseNumberField
	 * @param txtDefendantName2 DefendantNameField
	 * @return true/false
	 */
	private boolean isEntriesValid() {
		if(txtCaseNumber.getText().length()>1 
				&& !txtCaseNumber.hasError() 
				&& isValidCaseType(txtCaseNumber.getText().substring(0,1).toUpperCase())
				&& txtCaseNumber.isRegexMatch()) {
			return true;
		} else if(txtCaseNumber.isNullOrEmpty() && !txtDefendantSurname.getText().isEmpty()) {
			return true;
		}
		return false;
	}	
	

	// *******************************************************************************
	// * private class SearchPanelDocListener implements DocumentListener
	// *
	// * Purpose : Custom DocumentListener class to only enable OK button when
	// either
	// * txtCaseNumber or txtDefendantName contain data, otherwise disable.
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private class SearchPanelDocListener implements DocumentListener {
		@Override
		public void insertUpdate(DocumentEvent e) {
			if ((txtCaseNumber.getText() != null && txtCaseNumber.getText().length() > 0)
					|| (txtDefendantSurname.getText() != null && txtDefendantSurname.getText().length() > 0)) {
				btnOk.setEnabled(true);
			} else {
				btnOk.setEnabled(false);
			}
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			if ((txtCaseNumber.getText() != null && txtCaseNumber.getText().length() > 0)
					|| (txtDefendantSurname.getText() != null && txtDefendantSurname.getText().length() > 0)) {
				btnOk.setEnabled(true);
			} else {
				btnOk.setEnabled(false);
			}
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			if ((txtCaseNumber.getText() != null && txtCaseNumber.getText().length() > 0)
					|| (txtDefendantSurname.getText() != null && txtDefendantSurname.getText().length() > 0)) {
				btnOk.setEnabled(true);
			} else {
				btnOk.setEnabled(false);
			}
		}
	}
	
	private boolean checkValidationForCase() {
		if (transferCase || caseLinking) { // ctx-210
			// --- Search defendants on case ---
			Collection<DefendantOnCaseBasicValue> defendantsOnCase = (ArrayList<DefendantOnCaseBasicValue>) defendantDelegate
					.findByCaseId(caseId);
			if(defendantsOnCase.size() >0) {
				
				if(caseUnlinkNeeded) {
					if (newCBV.getCaseGroupNumber() != null) {
						ArrayList<CaseLinkingValue> clvColl = (ArrayList<CaseLinkingValue>) 
								caseDelegate.findActiveCasesWithGroupNumber(XhibitSingleton.getInstance().getCourtId(), 
											newCBV.getCaseGroupNumber());		
						if (clvColl.size() <= 0) {	
							showErrorPopup();
							return false;
						}
					} else {
						showErrorPopup();
						return false;
					}
				}
				
				int defsVerified = 0;
				for (DefendantOnCaseBasicValue defendantOnCase : defendantsOnCase) {
					if (null != defendantOnCase.getResultsVerified()
							&& (defendantOnCase.getResultsVerified().equals("E"))) {
								defsVerified++;

					}
				}
				if (defsVerified == defendantsOnCase.size()) {
					if(caseLinking) {
						caseNotFound();
						return false ;
					} else {
						JOptionPane.showMessageDialog(parent,
								"Case " + caseType + "" + caseNumber + " cannot be transferred as it is closed",
								"Case has been closed", JOptionPane.INFORMATION_MESSAGE);
						return false;
					}
				}
				//only show popup if case linking as we can transfer a case with no defs.
			} else if(caseUnlinkNeeded){
				showErrorPopup();
				return false;
			} else if(caseLinking){
				caseNotFound();
				return false;
			}
		}
		
		else if (indictmentSearch && newCBV.getCaseListed() != null && newCBV.getCaseListed().equals("Y")) {
			JOptionPane.showMessageDialog(parent,
					"Case " + caseType + "" + caseNumber + " already has hearings",
					"Case has been listed", JOptionPane.INFORMATION_MESSAGE);
			return false;
		} else if(deleteCase && newCBV.getPubRunningListId() != null){
			JOptionPane.showMessageDialog(parent,
					"Case " + caseType + "" + caseNumber
							+ " has been on a published running list",
					"Case has been published", JOptionPane.INFORMATION_MESSAGE);
			return false;
		} else if (deleteCase && newCBV.getCaseListed() != null && newCBV.getCaseListed().equals("Y")) {
			JOptionPane.showMessageDialog(parent,
					"Case " + caseType + "" + caseNumber + " already has hearings",
					"Case has been listed", JOptionPane.INFORMATION_MESSAGE);
			return false;
		} else if (redel) {
			Collection<DefendantOnCaseBasicValue> defendantsOnCase = (ArrayList<DefendantOnCaseBasicValue>) defendantDelegate
					.findByCaseId(caseId);
			if (defendantsOnCase.size()<= 0) {
				JOptionPane.showMessageDialog(parent, "Case " + caseType + "" + caseNumber
					+ " has no associated \n defendants to remove or replace",
					"No Defendants On Case", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		} else if(caseSearchModel.isMonetaryOrder()) {
			try {
				Collection monetaryOrders = monetaryOrderTrackingDelegate.findByCaseId(caseId, courtId);
				if (monetaryOrders.size() > 0) {
					caseSearchModel.setMonetaryOrdersCollection(monetaryOrders);
				} else {
					showErrorPopup();
					return false;
				}
			} catch (MonetaryOrderTrackingControllerException e) {
				showErrorPopup();
				return false;
			} catch (FinderException e) {
				showErrorPopup();
				return false;
			}

		}
		return true;

	}
	
}