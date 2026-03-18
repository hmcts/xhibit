package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.caseprosecutoragency.CaseProsecutorAgencyControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.legalaidorder.LegalAidOrderControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.prosecutorrefsolfirm.ProsecutorRefSolFirmControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.refsolicitorfirm.RefSolicitorFirmControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.LegalAidOrderBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefProsecutorAgencyComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.caseprosecutoragency.CaseProsecutorAgencyValue;
import uk.gov.courtservice.xhibit.business.vos.services.prosecutorrefsolfirm.ProsecutorRefSolFirmValue;
import uk.gov.courtservice.xhibit.client.casemanagement.privaterep.PrivateRepresentationDialog;
import uk.gov.courtservice.xhibit.client.casemanagement.privaterep.PrivateRepresentationModel;
import uk.gov.courtservice.xhibit.client.casemanagement.publicrep.PrivateToPublicRepresentationDialog;
import uk.gov.courtservice.xhibit.client.casemanagement.publicrep.PrivateToPublicRepresentationModel;
import uk.gov.courtservice.xhibit.client.casemanagement.publicrep.PublicRepresentationDialog;
import uk.gov.courtservice.xhibit.client.casemanagement.publicrep.PublicRepresentationModel;
import uk.gov.courtservice.xhibit.client.casemanagement.util.CaseMethods;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

public class ProsecutorRespondentTab extends XPanel {
	private static final long serialVersionUID = 1L;
	private static final Logger log = CSServices.getLogger(ProsecutorRespondentTab.class);

	private JPanel respondentPanel = null;
	private JPanel tablePanel = null;
	private JTable prosecutorRespondentTable;
	DefaultTableModel model;
	private JPanel respondentButtonPanel = null;
	private JButton btnAddProsecutorRespondent;
	private JButton btnRemoveProsecutorRespondent;

	private JPanel representaionButtonPanel = null;
	private JButton btnAddAmendPrivateRepresentation;
	private JButton btnAddAmendPublicRepresentation;

	private JPanel detailsPanel = null;

	// Text Fields
	private XTextField txtAddressLine1;
	private XTextField txtAddressLine2;
	private XTextField txtAddressLine3;
	private XTextField txtAddressLine4;
	private XTextField txtTown;
	private XTextField txtCounty;
	private XTextField txtPostcode;
	private XTextField txtTelephone;
	private XTextField txtFax;
	private XTextField txtSecureEmail;
	private XTextField txtNonSecureEmail;
	private XTextField txtDocExRef;
	private XTextField txtCaseNumber;
	private XTextField txtCaseTitle;

	// Combo Boxes
	private JComboBox statusComboBox;
	// Integers
	private Integer refProsecutorAgencyId;
	private Integer caseId;
	// All other
	private XhibitApplicationController xac;
	private ProsecutorRespondentTab thisClass;
	// Boolens
	private boolean isProsecutor;
	// Strings
	private String prosResp;
	private static final String RESPONDENT = "Respondent";
	private static final String PROSECUTOR = "Prosecutor";
	private static final String OBJECTOR = "Objector";
	private final String[] statusStrings = { RESPONDENT, OBJECTOR };
	
	private CaseXPanel caseX = null;
	
	/**
	 * Used for logging and exception handling.
	 */
	private static final String PACKAGE_NAME = "uk.gov.courtservice.xhibit.client.casemanagement";
	private static final String CLASS_NAME = ".ProsecutorResponsentTab";
	private static final String ERROR_IN = "Error in ";
	
	private CaseProsecutorAgencyControllerBeanBusinessDelegate caseProsDelegate; 


	// *******************************************************************************
	// * public ProsecutorRespondentTab(final XhibitApplicationController xac)
	// *
	// * Purpose : ProsecutorRrespondentTab constructor
	// * To call : caseStatus - case process status
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	public ProsecutorRespondentTab(final XhibitApplicationController xac, CaseXPanel caseX) {
		caseProsDelegate = XhibitDelegateHelper.getCaseProsecutorAgencyDelegate() ;
		
		this.xac = xac;
		setLayout(null);
		this.caseX = caseX;

		thisClass = this;
		this.setLayout(new GridBagLayout());
		// Set the isProsecutor variable to signify whether it will be a
		// prosecutor or respondent.
		if ((xac.getCaseStatus().isCaseType(CaseType.SENTENCE)) || (xac.getCaseStatus().isCaseType(CaseType.TRIAL))) {
			isProsecutor = true;
			prosResp = PROSECUTOR;
		} else if ((xac.getCaseStatus().isCaseType(CaseType.APPEAL))
				|| (xac.getCaseStatus().isCaseType(CaseType.MISC))) {
			isProsecutor = false;
			prosResp = RESPONDENT;
		}
		
		jbInit();
		
		btnAddAmendPrivateRepresentation.setEnabled(false);
		btnAddAmendPublicRepresentation.setEnabled(false);
		btnRemoveProsecutorRespondent.setEnabled(false);
	}

	// Initialises layout of prosecutorRespondent tab
	private void jbInit() {
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		
		// Add case number and title text fields (read-only).
		txtCaseNumber = new XTextField();
		txtCaseNumber.setEnabled(false);
		txtCaseNumber.setDisabledTextColor(Color.BLACK);
		txtCaseNumber.setColumns(4);
		txtCaseNumber.setMinimumSize(txtCaseNumber.getPreferredSize());
		gbc.weightx = 0.05;
		gbc.weighty = 0.075;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 20, 5, 20);
		this.add(txtCaseNumber, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.45;
		txtCaseTitle = new XTextField();
		txtCaseTitle.setEnabled(false);
		txtCaseTitle.setDisabledTextColor(Color.BLACK);
		txtCaseTitle.setColumns(10);
		txtCaseTitle.setMinimumSize(txtCaseTitle.getPreferredSize());
		this.add(txtCaseTitle, gbc);
		
		gbc.gridx++;
		this.add(Box.createRigidArea(txtCaseTitle.getPreferredSize()), gbc);

		gbc.gridx-=2;
		gbc.gridy++;
		
		// Respondent Panel
		gbc.weighty = 0.3;
		gbc.gridwidth = 3;
		gbc.insets = new Insets(4, 24, 4, 24);
		gbc.fill = GridBagConstraints.BOTH;
		this.add(getRespondentPanel(), gbc);

		gbc.gridy++;
		gbc.weighty = 0.05;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		this.add(getRepresentationButtonPanel(), gbc);

		gbc.gridy++;
		gbc.weighty = 0.65;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(getDetailsPanel(), gbc);

	}

	private GridBagConstraints getDefaultGridBagConstraints() {
		return new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				XHIBITConstant.nonContainerInsets, 0, 0);

	}

	private JPanel getRespondentPanel() {
		if (respondentPanel == null) {
			respondentPanel = new JPanel();
			respondentPanel.setBorder(BorderFactory.createTitledBorder(
					prosResp + " - Add case " + prosResp + " here with private representation (if required)"));
			respondentPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getDefaultGridBagConstraints();

			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 0.9;
			respondentPanel.add(getTablePanel(), gbc);

			gbc.weightx = 0.1;
			gbc.gridx++;
			respondentPanel.add(getRespondentButtonPanel(), gbc);
		}
		return respondentPanel;
	}

	private JPanel getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new JPanel();
			tablePanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getDefaultGridBagConstraints();

			gbc.fill = GridBagConstraints.BOTH;
			prosecutorRespondentTable = new JTable();
			if (!xac.getCaseStatus().isCaseType(CaseType.MISC)) {
				model = new DefaultTableModel(new Object[][] {}, new String[] { "Name", "CPS Code" });
			} else {
				model = new DefaultTableModel(new Object[][] {}, new String[] { "Name", "CPS Code", "Status" });
				
				model.addTableModelListener(new TableModelListener() {
					@Override
					public void tableChanged(TableModelEvent e) {
						// Status column
						if (e.getColumn() == 2 && e.getType() == TableModelEvent.UPDATE) {	
							enableSaveButton(true);
						}
					}
				});				
			}
			prosecutorRespondentTable.setModel(model);

			// --- Add a column to hold the prosecutor object, this column will
			// not
			// be displayed ---
			model.addColumn("ProsecutorRespondent");
			model.addColumn("BasicValue");

			prosecutorRespondentTable.getColumnModel().getColumn(0).setPreferredWidth(250);
			prosecutorRespondentTable.getColumnModel().getColumn(1).setPreferredWidth(75);
			prosecutorRespondentTable.getColumnModel().getColumn(2).setPreferredWidth(100);
			prosecutorRespondentTable.getColumnModel().getColumn(model.getColumnCount() - 1).setMinWidth(0);
			prosecutorRespondentTable.getColumnModel().getColumn(model.getColumnCount() - 1).setMaxWidth(0);
			prosecutorRespondentTable.getColumnModel().getColumn(model.getColumnCount() - 2).setMinWidth(0);
			prosecutorRespondentTable.getColumnModel().getColumn(model.getColumnCount() - 2).setMaxWidth(0);

			prosecutorRespondentTable.setDefaultEditor(Object.class, null);
			// --- List selection listener - lets us know when list changes ---
			prosecutorRespondentTable.getSelectionModel()
					.addListSelectionListener(new ProsecutorRespondentListSelectionListener());
			if (xac.getCaseStatus().isCaseType(CaseType.MISC)) {

				statusComboBox = new JComboBox(statusStrings);
				// --- Watch for changes in statusComboBox ---
				statusComboBox.addItemListener(new StatusComboBoxListener());
				prosecutorRespondentTable.getColumnModel().getColumn(2)
						.setCellEditor(new DefaultCellEditor(statusComboBox));
			}
			prosecutorRespondentTable.setModel(model);
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportView(prosecutorRespondentTable);
			tablePanel.add(scrollPane, gbc);

		}
		return tablePanel;

	}

	public Integer getProsecutorRespondentCount() {
		Integer count = 0;
		if (prosecutorRespondentTable.getModel().getRowCount() > 0) {
			for (Integer row = 0; row < prosecutorRespondentTable.getModel().getRowCount(); row++) {
				if (model.getValueAt(row, 2) != null  && model.getValueAt(row, 2).equals(RESPONDENT)) {
					count++;
				}
			}
		}
		return count;
	}

	private JPanel getRespondentButtonPanel() {
		if (respondentButtonPanel == null) {
			respondentButtonPanel = new JPanel();
			respondentButtonPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getDefaultGridBagConstraints();

			gbc.anchor = GridBagConstraints.NORTH;
			btnAddProsecutorRespondent = new JButton();
			btnAddProsecutorRespondent.setAction(new AddButtonAction(this));
			respondentButtonPanel.add(btnAddProsecutorRespondent, gbc);

			gbc.gridy++;
			gbc.anchor = GridBagConstraints.SOUTH;
			btnRemoveProsecutorRespondent = new JButton();
			btnRemoveProsecutorRespondent.setAction(new RemoveButtonAction(this));
			respondentButtonPanel.add(btnRemoveProsecutorRespondent, gbc);

		}
		return respondentButtonPanel;
	}

	private JPanel getRepresentationButtonPanel() {
		if (representaionButtonPanel == null) {
			representaionButtonPanel = new JPanel();
			representaionButtonPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getDefaultGridBagConstraints();

			btnAddAmendPublicRepresentation = new JButton();
			btnAddAmendPublicRepresentation.setEnabled(false);
			btnAddAmendPublicRepresentation.setAction(new AddAmendPublicAction(this));
			representaionButtonPanel.add(btnAddAmendPublicRepresentation, gbc);
			if (getIsProsecutor()) {
				btnAddAmendPublicRepresentation.setVisible(false);
			}

			gbc.gridx++;
			btnAddAmendPrivateRepresentation = new JButton();
			btnAddAmendPrivateRepresentation.setAction(new AddAmendPrivateAction(this));
			representaionButtonPanel.add(btnAddAmendPrivateRepresentation);
			btnAddAmendPrivateRepresentation.setEnabled(false);
		}
		return representaionButtonPanel;
	}

	private JPanel getDetailsPanel() {
		if (detailsPanel == null) {
			detailsPanel = new JPanel();
			detailsPanel.setLayout(new GridBagLayout());
			detailsPanel.setBorder(BorderFactory.createTitledBorder("Details of selected " + prosResp));
			GridBagConstraints gbc = getDefaultGridBagConstraints();

			// Add labels in first column
			gbc.weightx = 0.05;
			gbc.insets = new Insets(4, 24, 4, 4);
			JLabel lblAddress = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRepTab.Address"));
			detailsPanel.add(lblAddress, gbc);

			gbc.gridy += 4; // Lines 2-4 have no labels
			JLabel lblTown = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRepTab.Town"));
			detailsPanel.add(lblTown, gbc);

			gbc.gridy++;
			JLabel lblCounty = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRepTab.County"));
			detailsPanel.add(lblCounty, gbc);

			gbc.gridy++;
			JLabel lblPostcode = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRepTab.Postcode"));
			detailsPanel.add(lblPostcode, gbc);

			// Add Labels in the third column
			gbc.gridy = 0;
			gbc.gridx += 2;
			JLabel lblTelephone = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRepTab.Telephone"));
			detailsPanel.add(lblTelephone, gbc);

			gbc.gridy++;
			JLabel lblFax = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRepTab.Fax"));
			detailsPanel.add(lblFax, gbc);

			gbc.gridy++;
			JLabel lblSecureEmail = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRepTab.SecureEmail"));
			detailsPanel.add(lblSecureEmail, gbc);

			gbc.gridy++;
			JLabel lblNonSecureEmail = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRepTab.NonSecureEmail"));
			detailsPanel.add(lblNonSecureEmail, gbc);

			gbc.gridy++;
			JLabel lblDocExRef = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRepTab.DocExRef"));
			detailsPanel.add(lblDocExRef, gbc);

			gbc.insets = new Insets(4, 4, 4, 4);

			// Add Text Fields in second column
			gbc.gridy = 0;
			gbc.gridx = 1;
			gbc.weightx = 0.95;

			txtAddressLine1 = new XTextField();
			txtAddressLine1.setColumns(10);
			txtAddressLine1.setMinimumSize(txtAddressLine1.getPreferredSize());
			txtAddressLine1.setEnabled(false);
			detailsPanel.add(txtAddressLine1, gbc);

			gbc.gridy++;
			txtAddressLine2 = new XTextField();
			txtAddressLine2.setEnabled(false);
			txtAddressLine2.setMinimumSize(txtAddressLine2.getPreferredSize());
			detailsPanel.add(txtAddressLine2, gbc);

			gbc.gridy++;
			txtAddressLine3 = new XTextField();
			txtAddressLine3.setEnabled(false);
			txtAddressLine3.setMinimumSize(txtAddressLine3.getPreferredSize());
			detailsPanel.add(txtAddressLine3, gbc);

			gbc.gridy++;
			txtAddressLine4 = new XTextField();
			txtAddressLine4.setEnabled(false);
			txtAddressLine4.setMinimumSize(txtAddressLine4.getPreferredSize());
			detailsPanel.add(txtAddressLine4, gbc);

			gbc.gridy++;
			txtTown = new XTextField();
			txtTown.setEnabled(false);
			txtTown.setMinimumSize(txtTown.getPreferredSize());
			detailsPanel.add(txtTown, gbc);

			gbc.gridy++;
			if (txtCounty == null) {
				txtCounty = new XTextField();
				txtCounty.setEnabled(false);
				txtCounty.setMinimumSize(txtCounty.getPreferredSize());
			}
			detailsPanel.add(txtCounty, gbc);

			gbc.gridy++;
			txtPostcode = new XTextField();
			txtPostcode.setEnabled(false);
			txtPostcode.setMinimumSize(txtPostcode.getPreferredSize());
			detailsPanel.add(txtPostcode, gbc);

			// Add Fields in fourth Column
			gbc.gridy = 0;
			gbc.gridx = 3;
			txtTelephone = new XTextField();
			txtTelephone.setColumns(10);
			txtTelephone.setMinimumSize(txtTelephone.getPreferredSize());
			txtTelephone.setEnabled(false);
			detailsPanel.add(txtTelephone, gbc);

			gbc.gridy++;
			txtFax = new XTextField();
			txtFax.setEnabled(false);
			txtFax.setMinimumSize(txtFax.getPreferredSize());
			detailsPanel.add(txtFax, gbc);

			gbc.gridy++;
			txtSecureEmail = new XTextField();
			txtSecureEmail.setEnabled(false);
			txtSecureEmail.setMinimumSize(txtSecureEmail.getPreferredSize());
			detailsPanel.add(txtSecureEmail, gbc);

			gbc.gridy++;
			txtNonSecureEmail = new XTextField();
			txtNonSecureEmail.setEnabled(false);
			txtNonSecureEmail.setMinimumSize(txtNonSecureEmail.getPreferredSize());
			detailsPanel.add(txtNonSecureEmail, gbc);

			gbc.gridy++;
			txtDocExRef = new XTextField();
			txtDocExRef.setEnabled(false);
			txtDocExRef.setMinimumSize(txtDocExRef.getPreferredSize());
			detailsPanel.add(txtDocExRef, gbc);

			gbc.gridx++;
			detailsPanel.add(Box.createRigidArea(txtTelephone.getMinimumSize()), gbc);

		}
		return detailsPanel;
	}

	/**
	 * Action for what happens when Add ProsecutorRespondent button is clicked.
	 *
	 */
	private class AddButtonAction extends XAction {

		private static final long serialVersionUID = 1L;

		public AddButtonAction(ProsecutorRespondentTab parent) {
			if (prosResp.equals(PROSECUTOR)) {
				populateFromBundle("ProsResp.AddProsecutor");
			} else {
				populateFromBundle("ProsResp.AddRespondent");
			}
			setCaller(parent);
		}

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			try {
				ProsecutorRespondentSearchDialog prosRespSearchDialog = new ProsecutorRespondentSearchDialog(xac,
						new ProsecutorRespondentSearchModel(xac, thisClass));
				prosRespSearchDialog.setLocationRelativeTo(xac);
				prosRespSearchDialog.setVisible(true);
			} catch (CSRecoverableException e1) {
				log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+" : "+e1);
				XHIBITConstant.handleError(e1, this.getClass());
			}

		}

	}

	/**
	 * Action for what happens when Remove ProsecutorRespondent button is
	 * clicked.
	 *
	 */
	private class RemoveButtonAction extends XAction {

		private static final long serialVersionUID = 1L;

		public RemoveButtonAction(ProsecutorRespondentTab parent) {
			if (prosResp.equals(PROSECUTOR)) {
				populateFromBundle("ProsResp.RemoveProsecutor");
			} else {
				populateFromBundle("ProsResp.RemoveRespondent");
			}

			setCaller(parent);
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			DefaultTableModel defModel = (DefaultTableModel) prosecutorRespondentTable.getModel();
			Integer selectedRow = prosecutorRespondentTable.getSelectedRow();

			if (selectedRow != -1) {
				defModel.removeRow(selectedRow);
				setRefProsecutorAgencyID(0);
				txtAddressLine1.setText("");
				txtAddressLine2.setText("");
				txtAddressLine3.setText("");
				txtAddressLine4.setText("");
				txtTown.setText("");
				txtCounty.setText("");
				txtPostcode.setText("");
				txtTelephone.setText("");
				txtFax.setText("");
				txtSecureEmail.setText("");
				txtNonSecureEmail.setText("");
				txtDocExRef.setText("");
				btnAddProsecutorRespondent.setEnabled(true);
				if (prosecutorRespondentTable.getRowCount() <= 0) {
					btnRemoveProsecutorRespondent.setEnabled(false);
					btnAddAmendPrivateRepresentation.setEnabled(false);
					btnAddAmendPublicRepresentation.setEnabled(false);
				} else {
					prosecutorRespondentTable.setRowSelectionInterval(0, 0);
				}
			}
			
			enableSaveButton(true);
		}

	}

	/**
	 * Action for what happens when Add Amend Public Representation button is
	 * clicked.
	 *
	 */
	private class AddAmendPublicAction extends XAction {

		private static final long serialVersionUID = 1L;

		public AddAmendPublicAction(ProsecutorRespondentTab parent) {
			populateFromBundle("ProsResp.AddAmendPublic");
			setCaller(parent);
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			if (getProsecutorRespondentInTableCount() > 0) {
				if (prosecutorRespondentTable.getValueAt(prosecutorRespondentTable.getSelectedRow(),
						prosecutorRespondentTable.getColumnCount() - 3) != null) {
					String status = String
							.valueOf(prosecutorRespondentTable.getValueAt(prosecutorRespondentTable.getSelectedRow(),
									prosecutorRespondentTable.getColumnCount() - 3));
					if (status.equals(OBJECTOR)) {
						JOptionPane.showMessageDialog((Component) null, "Legal aid cannot be granted to Objectors",
								"Error", JOptionPane.ERROR_MESSAGE);
					} else {
						showPublicRep();

					}
				} else {
					showPublicRep();
				}
			}
		}
		private void showPublicRep() {
			try {
				// if it doesn't have a case id then we know that it's a new
				// public rep and show new public rep screen
				if (getCaseId() == null) {
					PublicRepresentationDialog dialog = new PublicRepresentationDialog(xac,
							new PublicRepresentationModel(null, thisClass));
					dialog.setLocationRelativeTo(xac);
					dialog.setVisible(true);
				} else {
					// if it has rep then
					ArrayList<ProsecutorRefSolFirmValue> crsfv = getRep();
					if (!crsfv.isEmpty()) {
						// if its private rep then show private rep with the
						// info from getPrivateToPublicModel.
						if (crsfv.get(0).getRepType().equals("P") && crsfv.get(0).getRepEndDate()==null) {
							PrivateToPublicRepresentationDialog dialog = new PrivateToPublicRepresentationDialog(xac,
									getPrivateToPublicModel(crsfv.get(0)));
							dialog.setLocationRelativeTo(xac);
							dialog.setVisible(true);
						}
						// otherwise open up public rep
						else {
							PublicRepresentationDialog dialog = new PublicRepresentationDialog(xac,
									new PublicRepresentationModel(crsfv.get(0).getCaseProsAgencyId(), thisClass));
							dialog.setLocationRelativeTo(xac);
							dialog.setVisible(true);
						}
					}
					// else show public rep
					else {
						DefaultTableModel defModel = (DefaultTableModel) prosecutorRespondentTable.getModel();

						CaseProsecutorAgencyValue prosRespVal = (CaseProsecutorAgencyValue) (defModel
								.getValueAt(prosecutorRespondentTable.getSelectedRow(), defModel.getColumnCount() - 1));
						PublicRepresentationDialog dialog = new PublicRepresentationDialog(xac,
								new PublicRepresentationModel(prosRespVal.getCaseProsAgencyID(), thisClass));
						dialog.setLocationRelativeTo(xac);
						dialog.setVisible(true);
					}
				}
			} catch (CSRecoverableException e1) {
				log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+" : "+e1);
				XHIBITConstant.handleError(e1, this.getClass());
			}
		}
		
		private PrivateToPublicRepresentationModel getPrivateToPublicModel(ProsecutorRefSolFirmValue crsfv) {

			PrivateToPublicRepresentationModel privateToPublicModel = new PrivateToPublicRepresentationModel(
					crsfv.getCaseProsAgencyId(), thisClass);

			RefSolicitorFirmComplexValue refSolValue = null;
			if (crsfv.getRefSolicitorFirmId() != null) {
				RefSolicitorFirmControllerBeanBusinessDelegate refSol = XhibitDelegateHelper.getRefSolicitorFirmController();
				refSolValue = refSol.findByPK(crsfv.getRefSolicitorFirmId());
			}
			if (refSolValue != null) {
				privateToPublicModel.setSolicitorName(refSolValue.getSolicitorFirmName());
				privateToPublicModel.setSolicitorAddress1(refSolValue.getAddress1());
				privateToPublicModel.setSolicitorAddress2(refSolValue.getAddress2());
				privateToPublicModel.setSolicitorAddress3(refSolValue.getAddress3());
				privateToPublicModel.setSolicitorAddress4(refSolValue.getAddress4());
				privateToPublicModel.setSolicitorTown(refSolValue.getTown());
				privateToPublicModel.setSolicitorCounty(refSolValue.getCounty());
				privateToPublicModel.setSolicitorPostCode(refSolValue.getPostcode());
				privateToPublicModel.setSolicitorDocExRef(refSolValue.getDxRef());
				privateToPublicModel.setSolicitorTelephoneNo(refSolValue.getTelephoneNumber());
				privateToPublicModel.setSolicitorFaxNo(refSolValue.getFaxNumber());
				privateToPublicModel.setSolicitorSecureEmail(refSolValue.getSecureEmailAddress());
				privateToPublicModel.setSolicitorNonSecureEmail(refSolValue.getNonsecureEmailAddress());
			}
			privateToPublicModel.setSolicitorRef(crsfv.getSolicitorRef());
			privateToPublicModel.setStartDate(crsfv.getRepStDate());
			privateToPublicModel.setEndDate(crsfv.getRepEndDate());
			return privateToPublicModel;
		}

	}

	/**
	 * Action for what happens when Add Amend Private Representation button is
	 * clicked.
	 *
	 */
	private class AddAmendPrivateAction extends XAction {

		private static final long serialVersionUID = 1L;

		public AddAmendPrivateAction(ProsecutorRespondentTab parent) {
			populateFromBundle("ProsResp.AddAmendPrivate");
			setCaller(parent);
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {

			ArrayList<ProsecutorRefSolFirmValue> crsfv = getRep();
			CaseProsecutorAgencyValue cpaBasicValue = ((CaseProsecutorAgencyValue) (model
					.getValueAt(prosecutorRespondentTable.getSelectedRow(), model.getColumnCount() - 1)));

			//if case type is misc/criminal and it has a public rep
			if (!getIsProsecutor() && getCaseId() != null
					&& !crsfv.isEmpty() && crsfv.get(0).getRepType().equals("L")) {
				
				//check if its revoked or not, if yes then we are ok to add a private rep 
				LegalAidOrderControllerBeanBusinessDelegate legailAidDelegate = XhibitDelegateHelper.getLegalAidDelegate();
				ArrayList<LegalAidOrderBasicValue> legalAid = (ArrayList<LegalAidOrderBasicValue>)legailAidDelegate.findByCaseProsAgencyId(crsfv.get(0).getCaseProsAgencyId());
				//else it can't add new rep
				if (!legalAid.isEmpty() && legalAid.get(0).getDateOfRevocation()==null) {
					JOptionPane.showMessageDialog(null, "The Respondent already has a public representation order.\nPlease click the Representation Order button to add/amend a representation", 
						"Existing representation", JOptionPane.INFORMATION_MESSAGE);
				} else {
					findPrivateRep(null, cpaBasicValue);
				}				
			}
			else if(!crsfv.isEmpty()){
					findPrivateRep(crsfv.get(0), cpaBasicValue);
			} else {
				findPrivateRep(null, cpaBasicValue);
			}
		}

	}

	// *******************************************************************************
	// * private class ProsecutorListSelectionListener implements
	// ListSelectionListener
	// *
	// * Purpose : ListSelectionListener for Prosecutor table
	// * To call : Nothing
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	private class ProsecutorRespondentListSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			// --- If one row selected, display info from that row ---
			if (prosecutorRespondentTable.getSelectedRowCount() == 1) {
				try {
					// --- Last column contains Prosecutor object ---
					DefaultTableModel defModel = (DefaultTableModel) prosecutorRespondentTable.getModel();
					ProsecutorRespondent selectedProsecutorRespondent = (ProsecutorRespondent) (defModel
							.getValueAt(prosecutorRespondentTable.getSelectedRow(), defModel.getColumnCount() - 2));
					txtAddressLine1.setText(selectedProsecutorRespondent.getAddress().getAddress1());
					txtAddressLine2.setText(selectedProsecutorRespondent.getAddress().getAddress2());
					txtAddressLine3.setText(selectedProsecutorRespondent.getAddress().getAddress3());
					txtAddressLine4.setText(selectedProsecutorRespondent.getAddress().getAddress4());
					txtTown.setText(selectedProsecutorRespondent.getAddress().getTown());
					txtCounty.setText(selectedProsecutorRespondent.getAddress().getCounty());
					txtPostcode.setText(selectedProsecutorRespondent.getAddress().getPostcode());
					txtTelephone.setText(selectedProsecutorRespondent.getTelephoneNumber());
					txtFax.setText(selectedProsecutorRespondent.getFaxNumber());
					txtSecureEmail.setText(selectedProsecutorRespondent.getSecureEmailAddress());
					txtNonSecureEmail.setText(selectedProsecutorRespondent.getNonsecureEmailAddress());
					txtDocExRef.setText(selectedProsecutorRespondent.getDxRef());
					// set the agency id
					refProsecutorAgencyId = selectedProsecutorRespondent.getRefProsecutorAgencyId();
					
					// ctx-1747					
					enableRepresentationButtons();
				} catch (Exception ex) {
					log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+" : "+ex);
					XHIBITConstant.handleError(ex, this.getClass());
				}
			}
		}
	}

	// *******************************************************************************
	// * public void populate(CaseBasicValue caseBasicValue)
	// *
	// * Purpose : Pre-populate fields from given caseBasicValue
	// * To call : caseId - ID of case to populate fields from
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	public void populate(CaseBasicValue caseBasicValue) {
		log.debug("ProsecutorRespondentTab.populate() using caseBasicValue");
		List<CaseProsecutorAgencyValue> prosecutorRespondentAgencies = new ArrayList<CaseProsecutorAgencyValue>();
		if (caseBasicValue != null) {
			setCaseId(caseBasicValue.getCaseId());
			populateCaseNumberAndTitle(caseBasicValue.getCaseType()+""+caseBasicValue.getCaseNumber(), caseBasicValue.getCaseTitle());	
			// --- Get CaseProsecutorAgency record ---
			prosecutorRespondentAgencies= caseProsDelegate
								.findByCaseId(caseBasicValue.getCaseId());
		}
			// for non misc cases only one respondent
			if (xac.getCaseStatus().getCaseType() != CaseType.MISC && !prosecutorRespondentAgencies.isEmpty()) {
				CaseProsecutorAgencyValue thisAgency = prosecutorRespondentAgencies.get(0);
				addProsIfNeeded(thisAgency);
			}
			// for misc may have more than one respondent
			else if (xac.getCaseStatus().getCaseType() == CaseType.MISC
					&& !prosecutorRespondentAgencies.isEmpty()) {
				for (CaseProsecutorAgencyValue respondent : prosecutorRespondentAgencies) {
					addProsIfNeeded(respondent);
				}
			}
	}

	/**
	 * Seperate into a separate method from above as both were doing the same thing.
	 * @param caseProsAgencyValue
	 */
	private void addProsIfNeeded(CaseProsecutorAgencyValue caseProsAgencyValue) {
		BisRefControllerBeanBusinessDelegate bisRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
		Integer prosecutorAgencyId = caseProsAgencyValue.getRefProsecutorAgencyID();
		RefProsecutorAgencyComplexValue refProsecutorAgencyComplexValue = bisRefDelegate
				.findByRefProsecutorAgencyId(prosecutorAgencyId);
		
		ProsecutorRespondent prosecutorRespondent = new ProsecutorRespondent(
				refProsecutorAgencyComplexValue, isProsecutor);
		prosecutorRespondent.setFullName();
		prosecutorRespondent.setRefProsecutorAgencyId(prosecutorAgencyId);
		addProsecutorRespondent(prosecutorRespondent, caseProsAgencyValue);
		
	}

	/**
	 * @return the caseId
	 */
	public Integer getCaseId() {
		return caseId;
	}

	/**
	 * @param caseId
	 *            the caseId to set
	 */
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public boolean getIsProsecutor() {
		return isProsecutor;
	}

	public void setIsProsecutor(boolean isProsecutor) {
		this.isProsecutor = isProsecutor;
	}

	public Integer getRefProsecutorAgencyID() {
		return refProsecutorAgencyId;
	}

	public void setRefProsecutorAgencyID(Integer id) {
		refProsecutorAgencyId = id;
	}

	public Integer getProsecutorRespondentInTableCount() {
		return prosecutorRespondentTable.getRowCount();
	}

	// *******************************************************************************
	// * public void addProsecutor(Prosecutor prosecutor)
	// *
	// * Purpose : Add prosecutor to table
	// * To call : caseId - ID of case to populate fields from
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************
	public void addProsecutorRespondent(RefProsecutorAgencyComplexValue prosecutorRespondent,
			CaseProsecutorAgencyValue cpaBasicValue) {
		boolean addNewProsecutor = true;
		DefaultTableModel defModel = (DefaultTableModel) prosecutorRespondentTable.getModel();
		// --- Determine if prosecutor/respondent already exists in table
		// ---
		for (int i = 0; i < prosecutorRespondentTable.getRowCount(); i++) {
			int refId = ((ProsecutorRespondent) defModel.getValueAt(i, prosecutorRespondentTable.getColumnCount() - 2))
					.getRefProsecutorAgencyId();
			int newRef = prosecutorRespondent.getRefProsecutorAgencyId();
			if (refId == newRef) {
				addNewProsecutor = false;
				break;
			}
		}
		// --- Add prosecutor/respondent to table ---
		if (addNewProsecutor) {
			if (cpaBasicValue == null) {
				cpaBasicValue = new CaseProsecutorAgencyValue();
			}
			addNewProsecutor(cpaBasicValue, prosecutorRespondent);
		}
		// --- If nothing selected, try to select first row ---
		if (prosecutorRespondentTable.getSelectedRowCount() == 0) {
			prosecutorRespondentTable.setRowSelectionInterval(0, 0);
		}
		btnRemoveProsecutorRespondent.setEnabled(true);

		enableSaveButton(true);
	} 

	private void addNewProsecutor(CaseProsecutorAgencyValue cpaBasicValue, RefProsecutorAgencyComplexValue prosecutorRespondent) {

		ProsecutorRespondent newProsecutor = new ProsecutorRespondent(prosecutorRespondent, true);
		newProsecutor.setRefProsecutorAgencyId(prosecutorRespondent.getRefProsecutorAgencyId());
		newProsecutor.setTitle(prosecutorRespondent.getTitle());
		newProsecutor.setInitials(prosecutorRespondent.getInitials());
		newProsecutor.setProsecutorName1(prosecutorRespondent.getProsecutorName1());
		newProsecutor.setProsecutorName2(prosecutorRespondent.getProsecutorName2());
		newProsecutor.setProsecutorName3(prosecutorRespondent.getProsecutorName3());
		newProsecutor.setFullName();
		newProsecutor.setCpsCode(prosecutorRespondent.getCpsCode());
		newProsecutor.setAddress(prosecutorRespondent.getAddress());
		newProsecutor.setTelephoneNumber(prosecutorRespondent.getTelephoneNumber());
		newProsecutor.setFaxNumber(prosecutorRespondent.getFaxNumber());
		newProsecutor.setNonsecureEmailAddress(prosecutorRespondent.getNonsecureEmailAddress());
		newProsecutor.setSecureEmailAddress(prosecutorRespondent.getSecureEmailAddress());
		newProsecutor.setDxRef(prosecutorRespondent.getDxRef());

		if (xac.getCaseStatus().getCaseType() == CaseType.MISC) {
			String index = RESPONDENT;

			if (cpaBasicValue.getRespondentStatus() != null && cpaBasicValue.getRespondentStatus().equals("O")) {
					index = OBJECTOR; 
			} else if (cpaBasicValue.getRespondentStatus() == null && prosecutorRespondentTable.getRowCount() > 0) {
				for (int i = 0; i < prosecutorRespondentTable.getRowCount(); i++) {
					if (prosecutorRespondentTable.getValueAt(i,
						prosecutorRespondentTable.getColumnCount() - 3) != null && String.valueOf(prosecutorRespondentTable.getValueAt(i,
							prosecutorRespondentTable.getColumnCount() - 3)).equals(RESPONDENT)) {
						index = OBJECTOR;
					}
				}
				
			}

			model.addRow(new Object[] { prosecutorRespondent.getFullName(), prosecutorRespondent.getCpsCode(),
					index, newProsecutor, cpaBasicValue });
		} else {
			model.addRow(new Object[] { prosecutorRespondent.getFullName(), prosecutorRespondent.getCpsCode(),
					newProsecutor, cpaBasicValue });
			btnAddProsecutorRespondent.setEnabled(false);
		}

		btnRemoveProsecutorRespondent.setEnabled(true);
		
	}

	public Integer getRespondentCount() {
		Integer respondentCount = 0;
		if (prosecutorRespondentTable.getModel().getRowCount() > 0) {
			for (Integer row = 0; row < prosecutorRespondentTable.getModel().getRowCount(); row++) {
				if (prosecutorRespondentTable.getValueAt(row, 2) != null && prosecutorRespondentTable.getValueAt(row, 2).equals(RESPONDENT)) {
					respondentCount++;
				}
			}
		}
		return respondentCount;
	}

	public Integer getObjectorCount() {
		Integer objectorCount = 0;
		if (prosecutorRespondentTable.getModel().getRowCount() > 0) {
			for (Integer row = 0; row < prosecutorRespondentTable.getModel().getRowCount(); row++) {
				if (prosecutorRespondentTable.getValueAt(row, 2) != null && prosecutorRespondentTable.getValueAt(row, 2).equals(OBJECTOR)) {
					objectorCount++;
				}
			}
		}
		return objectorCount;
	}

	public List<CaseProsecutorAgencyValue> getAmendProsecutorBasicValue() {

		Integer columnCount = prosecutorRespondentTable.getColumnCount();
		ArrayList<CaseProsecutorAgencyValue> cpaBasicValue = new ArrayList<CaseProsecutorAgencyValue>();
		for (int i = 0; i < prosecutorRespondentTable.getRowCount(); i++) {
			cpaBasicValue.add(
					(CaseProsecutorAgencyValue) (prosecutorRespondentTable.getValueAt(i, columnCount - 1)));
			ProsecutorRespondent pros = (ProsecutorRespondent) (prosecutorRespondentTable.getValueAt(i,
					columnCount - 2));

			//only set the status if its a respondent hence the additional !getIsProsecutor() check
			if (!getIsProsecutor()){
				if (xac.getCaseStatus().getCaseType().equals(CaseType.MISC)) {
					String status = String.valueOf(
							prosecutorRespondentTable.getValueAt(i, prosecutorRespondentTable.getColumnCount() - 3));
					
					if (!status.isEmpty()) {
						cpaBasicValue.get(i).setRespondentStatus(String.valueOf(status.charAt(0)));
					}
				} else {
					cpaBasicValue.get(i).setRespondentStatus("R");
				}
				
				if (cpaBasicValue.get(i).getProsecutorType() == null) {
					cpaBasicValue.get(i).setProsecutorType("R");

				}
			}
			else if (cpaBasicValue.get(i).getProsecutorType() == null) {
				cpaBasicValue.get(i).setProsecutorType("P");
			}

			cpaBasicValue.get(i).setCaseID(caseId);
			cpaBasicValue.get(i).setRefProsecutorAgencyID(pros.getRefProsecutorAgencyId());
		}
		return cpaBasicValue;

	}

		public void findPrivateRep(ProsecutorRefSolFirmValue prosValue, CaseProsecutorAgencyValue cpaBasicValue) {
			if (prosValue == null) {
				prosValue = new ProsecutorRefSolFirmValue();	
				prosValue.setCaseProsAgencyId(cpaBasicValue.getCaseProsAgencyID());
			}
			try {

				prosValue.setPosInTable(prosecutorRespondentTable.getSelectedRow());
				PrivateRepresentationDialog dialog = new PrivateRepresentationDialog(xac,
						getPrivateModel(prosValue, cpaBasicValue.getProsecutorType()));
				dialog.setLocationRelativeTo(xac);
				dialog.setVisible(true);
			} catch (CSRecoverableException e) {
				log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+" : "+e);
				XHIBITConstant.handleError(e, this.getClass());
			}
			
		}

	private PrivateRepresentationModel getPrivateModel(ProsecutorRefSolFirmValue prosValue, String type) {

		PrivateRepresentationModel privateRepModel = new PrivateRepresentationModel(prosValue.getCaseProsAgencyId(),
				thisClass);
		privateRepModel.setProsRefSolFirm(prosValue);
		privateRepModel.setRepType(type);
		return privateRepModel;

	}

	private ArrayList<ProsecutorRefSolFirmValue> getRep() {

		// get the currently selected prosecutor
		DefaultTableModel defModel = (DefaultTableModel) prosecutorRespondentTable.getModel();

		CaseProsecutorAgencyValue cpaBasicValue = ((CaseProsecutorAgencyValue) (defModel
				.getValueAt(prosecutorRespondentTable.getSelectedRow(), defModel.getColumnCount() - 1)));
		ProsecutorRefSolFirmControllerBeanBusinessDelegate prosRefSolFirmDelegate = XhibitDelegateHelper.getProsRefSolFirmDelegate();
		return  (ArrayList<ProsecutorRefSolFirmValue>)prosRefSolFirmDelegate
					.findPrivateRepByCaseProsAgency(cpaBasicValue.getCaseProsAgencyID());
	}
	
	private void enableSaveButton(boolean enabled) {
		caseX.getCreateButton().setEnabled(enabled);
		caseX.getFinishButton().setEnabled(!enabled);

		CaseMethods.enableTabbedPane(caseX);
	}
	
	public void clearTable() {
		model.setRowCount(0);
	}
	
	public void populateCaseNumberAndTitle(String caseNumber, String caseTitle) {
		txtCaseNumber.setText(caseNumber);
		txtCaseTitle.setText(caseTitle);
	}
	
	public void populateCaseTitle(String caseTitle) {
		txtCaseTitle.setText(caseTitle);
	}	

	public void enableRepresentationButtons() {
		if (prosecutorRespondentTable.getSelectedRowCount() == 1) {
				// --- Last column contains Prosecutor object ---
				DefaultTableModel defModel = (DefaultTableModel) prosecutorRespondentTable.getModel();
				ProsecutorRespondent selectedProsecutorRespondent = (ProsecutorRespondent) (defModel
						.getValueAt(prosecutorRespondentTable.getSelectedRow(), defModel.getColumnCount() - 2));	
				
				List<CaseProsecutorAgencyValue> prosecutorRespondentAgencies = caseProsDelegate
						.findByCaseId(caseId);
				
				boolean foundPros = false;
				boolean publicRepEnabled = true;
				
				// check whether any of the attached prosecutor respondent agencies are the selected
				for (int i = 0; i < prosecutorRespondentAgencies.size(); i++) {					
					if (selectedProsecutorRespondent.getRefProsecutorAgencyId().equals( 
							prosecutorRespondentAgencies.get(i).getRefProsecutorAgencyID())) {
						
						foundPros = true;
						if (xac.getCaseStatus().getCaseType().equals(CaseType.MISC) && prosecutorRespondentAgencies.get(i).getRespondentStatus() != null && 
									prosecutorRespondentAgencies.get(i).getRespondentStatus().equals("O")) {
								publicRepEnabled = false;
						}
						
						break;
					}
				}	
				
				btnAddAmendPrivateRepresentation.setEnabled(foundPros);
				btnAddAmendPublicRepresentation.setEnabled(foundPros && publicRepEnabled);
			
		}
	}

	@Override
	public void stepInitialise() throws CSRecoverableException {
		//nothing to do so leaving blank.		
	}

	@Override
	public void stepActivate() throws CSRecoverableException {
		//nothing to do so leaving blank.		
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		//nothing to do so leaving blank.		
	}

	@Override
	public void stepValidate() throws CSRecoverableException {
		//nothing to do so leaving blank.		
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		//nothing to do so leaving blank.		
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		//nothing to do so leaving blank.		
	}
	
	/**
	 * Listener for when the status combo box is changed
	 * 
	 *
	 */
	public class StatusComboBoxListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			// --- If user is trying to select respondent, check
			// respondent count ---
			Integer columnCount = prosecutorRespondentTable.getColumnCount();

			if (e.getItem().equals(RESPONDENT) && (e.getStateChange() == ItemEvent.SELECTED) && getProsecutorRespondentCount() > 0) {
					statusComboBox.setSelectedIndex(1);

					ProsecutorRespondent selectedRespondent = (ProsecutorRespondent) (model
							.getValueAt(prosecutorRespondentTable.getSelectedRow(), columnCount - 2));
					selectedRespondent.setProsResp(RESPONDENT);
				
			}
			// --- Set status string in respondent in final column
			if (((ProsecutorRespondent) (model.getValueAt(prosecutorRespondentTable.getSelectedRow(),
					columnCount - 2))) != null) {
				ProsecutorRespondent p = ((ProsecutorRespondent) (model
						.getValueAt(prosecutorRespondentTable.getSelectedRow(), columnCount - 2)));
				if (statusComboBox.getSelectedItem() != null) {
					p.setProsResp(statusComboBox.getSelectedItem().toString());
					prosecutorRespondentTable.setValueAt(p, prosecutorRespondentTable.getSelectedRow(),
							columnCount - 2);
				}
			}
		}
	}
}
