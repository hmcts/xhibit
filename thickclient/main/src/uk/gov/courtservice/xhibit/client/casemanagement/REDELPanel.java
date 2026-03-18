package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.ejb.FinderException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.FixtureDeftAttendingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.casemanagement.caselinking.CaseLinkingDialog;
import uk.gov.courtservice.xhibit.client.casemanagement.caselinking.CaseLinkingModel;
import uk.gov.courtservice.xhibit.client.casemanagement.caselinking.CaseUnlinkingDialog;
import uk.gov.courtservice.xhibit.client.casemanagement.caselinking.CaseUnlinkingModel;
import uk.gov.courtservice.xhibit.client.listings.casesummary.CaseSummaryDialog;
import uk.gov.courtservice.xhibit.client.listings.casesummary.CaseSummaryModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class REDELPanel extends XPanel implements ValidationListener {
	private static final long serialVersionUID = 1L;
	private REDELModel redelModel = null;
	private REDELDialog parentDialog = null;

	private XhibitApplicationController xac;
	private CaseBasicValue caseBV = null;

	private JPanel topPanel = null;
	private JPanel tablePanel = null;
	private JPanel rightButtonPanel = null;
	private JPanel bottomButtonPanel = null;

	private JLabel lblCaseNumber = new JLabel("Case Number: ");
	private JLabel lblNoOfDefendants = new JLabel("Number of Defendants: ");

	private XTextField txtCaseNumber = null;
	private XTextField txtNoOfDefendants = null;

	private JTable table = null;
	private JScrollPane scrollPane = new JScrollPane();

	private String defAppellant = "Defendant";

	private JButton btnCaseSummary = null;
	private JButton btnSave = null;
	private JButton btnCancel = null;
	private JButton btnDelete = null;
	private JButton btnReplace = null;

	private boolean changesMade = false;

	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
	private ArrayList<DefendantOnCaseBasicValue> defsToDelete = new ArrayList<DefendantOnCaseBasicValue>();
	private ArrayList<DefendantOnCaseBasicValue> defsToUpdate = new ArrayList<DefendantOnCaseBasicValue>();
	private ArrayList<DefendantOnCaseBasicValue> defsToReplace = new ArrayList<DefendantOnCaseBasicValue>();
	
	private final Logger log = CSServices.getLogger(getClass());


	public REDELPanel(REDELDialog parentDialog, REDELModel redelModel) throws CSRecoverableException {
		this.parentDialog = parentDialog;
		this.redelModel = redelModel;
		jbInit();
	}

	private void jbInit() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(800, 400));

		GridBagConstraints gbc = getGridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weighty = 0.1;
		this.add(getTopPanel(), gbc);

		gbc.gridy++;
		gbc.weighty = 0.8;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(getTablePanel(), gbc);

		gbc.gridx++;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weighty = 0.1;
		this.add(getRightButtonPanel(), gbc);

		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = 1;
		gbc.weighty = 0.1;
		gbc.gridy++;
		gbc.gridx--;
		this.add(getBottomButtonPanel(), gbc);

	}

	private GridBagConstraints getGridBagConstraints() {
		GridBagConstraints gbc = null;
		gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				XHIBITConstant.nonContainerInsets, 0, 0);
		return gbc;
	}

	private JPanel getTopPanel() {
		if (topPanel == null) {
			topPanel = new JPanel();
			topPanel.setLayout(new GridBagLayout());

			GridBagConstraints gbc = getGridBagConstraints();
			gbc.weightx = 0.05;
			gbc.fill = GridBagConstraints.NONE;
			topPanel.add(lblCaseNumber, gbc);

			gbc.weightx = 0.45;
			gbc.gridx++;
			topPanel.add(getTxtCaseNumber(), gbc);

			gbc.weightx = 0.5;
			gbc.gridx++;
			topPanel.add(lblNoOfDefendants, gbc);

			gbc.weightx = 0.45;
			gbc.gridx++;
			topPanel.add(getTxtNoOfDefendants(), gbc);

		}
		return topPanel;
	}

	@SuppressWarnings("unchecked")
	private JPanel getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new JPanel();
			tablePanel.setLayout(new GridBagLayout());

			GridBagConstraints gbc = getGridBagConstraints();

			table = new JTable();
			table.setModel(new DefaultTableModel(new String[] { "Deft. No", "Surname", "Forename", "Sex",
					"Date of Birth", "Defendant On Case Basic Value", "Defendant" }, 0));

			table.getColumnModel().getColumn(0).setPreferredWidth(40);
			table.getColumnModel().getColumn(1).setPreferredWidth(40);
			table.getColumnModel().getColumn(2).setPreferredWidth(40);
			table.getColumnModel().getColumn(3).setPreferredWidth(40);
			table.getColumnModel().getColumn(4).setPreferredWidth(40);
			table.removeColumn(table.getColumnModel().getColumn(6));
			table.removeColumn(table.getColumnModel().getColumn(5));

			table.setPreferredScrollableViewportSize(table.getPreferredSize());
			table.setDefaultEditor(Object.class, null);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setRowSorter(new TableRowSorter(table.getModel()));
			table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					// When the row is selected display the values in the second
					// half of the page.
					if (table.getSelectedRowCount() == 1) {
						if (!e.getValueIsAdjusting()) {
							btnDelete.setEnabled(true);
							btnReplace.setEnabled(true);
						}

					}
				}
			});

			scrollPane.setViewportView(table);
			tablePanel.add(scrollPane, gbc);

		}
		return tablePanel;
	}

	private JPanel getRightButtonPanel() {
		if (rightButtonPanel == null) {
			rightButtonPanel = new JPanel();
			rightButtonPanel.setLayout(new GridBagLayout());

			GridBagConstraints gbc = getGridBagConstraints();
			gbc.fill = GridBagConstraints.NONE;
			getReplaceButton();
			rightButtonPanel.add(getDeleteButton());

			gbc.gridy++;
			rightButtonPanel.add(getReplaceButton(), gbc);

		}
		return rightButtonPanel;
	}

	private JPanel getBottomButtonPanel() {
		if (bottomButtonPanel == null) {
			bottomButtonPanel = new JPanel();
			bottomButtonPanel.setLayout(new GridBagLayout());

			GridBagConstraints gbc = getGridBagConstraints();
			gbc.fill = GridBagConstraints.NONE;
			bottomButtonPanel.add(getCaseSummaryButton(), gbc);

			gbc.gridx++;
			bottomButtonPanel.add(getSaveButton(), gbc);

			gbc.gridx++;
			bottomButtonPanel.add(getCancelButton(), gbc);

		}
		return bottomButtonPanel;
	}

	private XTextField getTxtCaseNumber() {
		if (txtCaseNumber == null) {
			txtCaseNumber = new XTextField();
			txtCaseNumber.setColumns(10);
			txtCaseNumber.setEnabled(false);
			txtCaseNumber.setMinimumSize(txtCaseNumber.getPreferredSize());
		}
		return txtCaseNumber;
	}

	private XTextField getTxtNoOfDefendants() {
		if (txtNoOfDefendants == null) {
			txtNoOfDefendants = new XTextField();
			txtNoOfDefendants.setColumns(10);
			txtNoOfDefendants.setEnabled(false);
			txtNoOfDefendants.setMinimumSize(txtNoOfDefendants.getPreferredSize());
		}
		return txtNoOfDefendants;
	}

	private JButton getDeleteButton() {
		if (btnDelete == null) {
			btnDelete = new JButton("Delete Defendant");
			btnDelete.setEnabled(false);
			btnDelete.setMinimumSize(btnDelete.getPreferredSize());
			btnDelete.setPreferredSize(btnReplace.getPreferredSize());
			btnDelete.setAction(new deleteAction(this));
		}
		return btnDelete;
	}

	private JButton getReplaceButton() {
		if (btnReplace == null) {
			btnReplace = new JButton("Replace Defendant");
			btnReplace.setEnabled(false);
			btnReplace.setMinimumSize(btnReplace.getPreferredSize());
			btnReplace.setPreferredSize(btnReplace.getPreferredSize());
			btnReplace.setAction(new replaceAction(this));
		}
		return btnReplace;
	}

	private JButton getCaseSummaryButton() {
		if (btnCaseSummary == null) {
			btnCaseSummary = new JButton("Case Summary");
		}
		btnCaseSummary.setEnabled(false);
		btnCaseSummary.setMinimumSize(btnCaseSummary.getPreferredSize());
		btnCaseSummary.setAction(new caseSummaryAction(this));
		return btnCaseSummary;
	}

	private JButton getSaveButton() {
		if (btnSave == null) {
			btnSave = new JButton("Save");
		}
		btnSave.setEnabled(false);
		btnSave.setMinimumSize(btnSave.getPreferredSize());
		btnSave.setAction(new saveAction(this));
		return btnSave;
	}

	private JButton getCancelButton() {
		if (btnCancel == null) {
			btnCancel = new JButton("Save");
		}
		btnCancel.setEnabled(false);
		btnCancel.setMinimumSize(btnCancel.getPreferredSize());
		btnCancel.setAction(new cancelAction(this));
		return btnCancel;
	}

	@SuppressWarnings("unchecked")
	private void moveModelToScreen() {
		if (redelModel != null) {
			xac = (XhibitApplicationController) parentDialog.getParentFrame();
			if (redelModel.getCaseBasicValue() != null) {
				caseBV = redelModel.getCaseBasicValue();
				parentDialog.setTitle("Replace / Delete Defendant");
				if (caseBV.getCaseType().equals("A")) {
					defAppellant = "Appellant";
					parentDialog.setTitle("Replace / Delete Appellant");

					table.getColumnModel().getColumn(0).setHeaderValue("Appellant No.");
					table.getTableHeader().repaint();
					lblNoOfDefendants.setText("Number of Appellants");
					btnDelete.setText("Delete Appellant");
					btnReplace.setText("Replace Appellant");

				}
				if (caseBV.getCaseNumber() != null) {
					if (txtCaseNumber != null) {
						txtCaseNumber.setText(caseBV.getCaseType() + caseBV.getCaseNumber());
					}
				}
				if (caseBV.getNoDefendantsForCase() != null) {
					if (txtNoOfDefendants != null) {
						txtNoOfDefendants.setText(caseBV.getNoDefendantsForCase().toString());
					}
				}
				try {
					DefendantControllerBeanBusinessDelegate del = XhibitDelegateHelper.getDefendantDelegate();
					ArrayList<DefendantOnCaseBasicValue> arr = (ArrayList<DefendantOnCaseBasicValue>) del
							.findByCaseId(caseBV.getCaseId());

					if (!arr.isEmpty()) {
						// --- Sort ---
						List<DefendantOnCaseBasicValue> defendantList = new ArrayList<DefendantOnCaseBasicValue>(
								arr);
						Comparator<DefendantOnCaseBasicValue> defendantComparator = new Comparator<DefendantOnCaseBasicValue>() {
							@Override
							public int compare(DefendantOnCaseBasicValue o1, DefendantOnCaseBasicValue o2) {
								int rc = 0;
								if (o1.getDefendantNumber() != null && o2.getDefendantNumber() != null) {
									rc = o1.getDefendantNumber().compareTo(o2.getDefendantNumber());
								}
								return rc;
							}
						};		
						
						Collections.sort(defendantList, defendantComparator);
						
						for (int i = 0; i < defendantList.size(); i++) {
							DefendantOnCaseBasicValue current = defendantList.get(i);
							DefendantValue currentDef = del.findByDefId(current.getDefendantID());
							Calendar cal = currentDef.getDateOfBirth();
							SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
							DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
							String formattedDate = "";
							if (currentDef.getDateOfBirth() != null) {
								formattedDate = sdf.format(cal.getTime());
							}

							int defNumber = 0;
							if (current.getDefendantNumber() == null) {
								defNumber = i + 1;
								current.setDefendantNumber(defNumber);
							} else {
								defNumber = current.getDefendantNumber();
							}
							Object[] row = { defNumber, currentDef.getSurName(), currentDef.getFirstName(),
									currentDef.getGenderString(), formattedDate, current, currentDef };
							tableModel.addRow(row);
						}
					}
				} catch (FinderException e) {
					XHIBITConstant.handleError(e, this.getClass());
				}
			}
		}
		btnCancel.setFocusable(true);
		btnDelete.setEnabled(false);
		btnSave.setEnabled(false);
		btnReplace.setEnabled(false);
	}

	@Override
	public void stepInitialise() throws CSRecoverableException {
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
		if (!ValidationControllerFactory.validateComponents(validationControllers)) {
			throw new CSValidationException("validation.general", "Field validation Failed");
		}
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (btnSave.equals(getDeinitialiseSource())) {
			if (!ValidationControllerFactory.validateComponents(validationControllers)) {
				throw new CSValidationException("validation.general", "Field validation failed");
			}

			if (btnCancel.equals(getDeinitialiseSource())) {
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int result = JOptionPane.showConfirmDialog((Component) null,
						getResourceBundle("defendantAppellantReplaceDelete.message.confirmCancel"), "Confirm", dialogButton);
				if (result == JOptionPane.YES_OPTION) {
					parentDialog.clearStatusBarScreenCode();
					parentDialog.dispose();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.gov.courtservice.xhibit.client.util.validation.ValidationListener#
	 * validationUpdatedView(uk.gov.courtservice.xhibit.client.util.validation.
	 * ValidationController)
	 */
	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		if (validationController.hasErrors()
				|| !ValidationControllerFactory.validateComponents(validationControllers)) {
			btnDelete.setEnabled(false);
			btnReplace.setEnabled(false);
		} else {
			btnDelete.setEnabled(true);
			btnReplace.setEnabled(true);
		}

	}

	/**
	 * Action for what happens when Save button is clicked.
	 *
	 */
	private class saveAction extends XAction {

		private static final long serialVersionUID = 1L;

		public saveAction(REDELPanel parent) {
			populateFromBundle("REDELSave");
			setCaller(parent);
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			boolean success = false;
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int result = JOptionPane.showConfirmDialog((Component) null, getResourceBundle("defendantAppellantReplaceDelete.message.save"),
					"Are you sure?", dialogButton);
			if (result == JOptionPane.YES_OPTION) {
				if (caseBV.getCaseType().equals("S") || caseBV.getCaseType().equals("T")) {
					success = saveSentenceTrialCase();
				} else {
					success = saveAppealCase();
				}
			}
			if (success) {
				changesMade = false;
				int jop = 3;
				if (caseBV.getCaseGroupNumber() == null) {
					String[] jopButton = { "Link", "No" };
					jop = JOptionPane.showOptionDialog((Component) null, getResourceBundle("defendantAppellantReplaceDelete.message.reviewLinkedCases"),
							"Linked Cases", JOptionPane.WARNING_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, jopButton,
							jopButton[0]);

					if (jop == 0) {
						CaseLinkingDialog caseLinkingDialog = new CaseLinkingDialog(xac,
								new CaseLinkingModel(caseBV.getCaseId()));
						parentDialog.dispose();
						parentDialog.clearStatusBarScreenCode();
						caseLinkingDialog.setVisible(true);
					} else {
						parentDialog.dispose();
						parentDialog.clearStatusBarScreenCode();
					}

				} else {
					String[] jopButtons = { "Link", "Unlink", "No" };
					jop = JOptionPane.showOptionDialog((Component) null, getResourceBundle("defendantAppellantReplaceDelete.message.reviewLinkedCases"),
							"Linked Cases", JOptionPane.WARNING_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, jopButtons,
							jopButtons[0]);
					if (jop == 0) {
						CaseLinkingDialog caseLinkingDialog = new CaseLinkingDialog(xac,
								new CaseLinkingModel(caseBV.getCaseId()));
						parentDialog.dispose();
						parentDialog.clearStatusBarScreenCode();
						caseLinkingDialog.setVisible(true);
					} else if (jop == 1) {
						CaseUnlinkingDialog caseUnlinkingDialog = new CaseUnlinkingDialog(xac,
								new CaseUnlinkingModel(caseBV.getCaseId()));
						parentDialog.dispose();
						parentDialog.clearStatusBarScreenCode();
						caseUnlinkingDialog.setVisible(true);
					} else {
						parentDialog.dispose();
						parentDialog.clearStatusBarScreenCode();
					}
				}
			} else if (result != JOptionPane.NO_OPTION){
				throw new Exception();
			}

		}
	}

	/**
	 * Action for what happens when case Summary button is clicked.
	 *
	 */
	private class caseSummaryAction extends XAction {

		private static final long serialVersionUID = 1L;

		public caseSummaryAction(REDELPanel parent) {
			populateFromBundle("CaseSummary");
			setCaller(parent);
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			CaseSummaryModel caseSummaryModel = new CaseSummaryModel(caseBV.getCaseId());
			CaseSummaryDialog dialog = new CaseSummaryDialog(xac, caseSummaryModel);
			dialog.setVisible(true);
		}
	}

	/**
	 * Action for what happens when Cancel button is clicked.
	 *
	 */
	private class cancelAction extends XAction {

		private static final long serialVersionUID = 1L;

		public cancelAction(REDELPanel parent) {
			populateFromBundle("btnCancel");
			setCaller(parent);
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			if (changesMade) {
				int option = JOptionPane.showConfirmDialog(null,
						getResourceBundle("defendantAppellantReplaceDelete.message.cancel"), "Are you sure",
						JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					parentDialog.clearStatusBarScreenCode();
					parentDialog.dispose();
				}
			} else {
				parentDialog.clearStatusBarScreenCode();
				parentDialog.dispose();
			}
		}
	}

	/**
	 * Action for what happens when Delete button is clicked.
	 *
	 */
	private class deleteAction extends XAction {

		private static final long serialVersionUID = 1L;

		public deleteAction(REDELPanel parent) {
			populateFromBundle("DeleteDefendantButton");
			if (caseBV != null) {
				if (caseBV.getCaseType().equals("A")) {
					populateFromBundle("DeleteAppellantButton");
				}
			}
			setCaller(parent);
		}

		@SuppressWarnings("unchecked")
		public void xActionPerformed(ActionEvent ae) throws Exception {
			DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
			Integer selectedRow = table.getRowSorter().convertRowIndexToModel(table.getSelectedRow());

			DefendantOnCaseBasicValue def = (DefendantOnCaseBasicValue) tableModel.getValueAt(selectedRow, 5);

			if (tableModel.getRowCount() == 1) {
				int result = JOptionPane.showConfirmDialog(null,
						getResourceBundle("defendantAppellantReplaceDelete.message.lastDefendant", new Object[] { defAppellant }),
						"Confirm Choice", JOptionPane.YES_NO_OPTION);
				if (result != JOptionPane.OK_OPTION) {
					throw new UserCancelException();
				}
			}
			ArrayList<HearingBasicValue> hearingBV = (ArrayList<HearingBasicValue>) XhibitDelegateHelper
					.getBizRefDelegate().findHearingByCaseId(caseBV.getCaseId());
			if (!hearingBV.isEmpty()) {
				JOptionPane.showMessageDialog(null,
						getResourceBundle("defendantAppellantReplaceDelete.message.previousHearingExists", new Object[] { defAppellant }),
						getResourceBundle("defendantAppellantReplaceDelete.message.deleteTitle"), JOptionPane.ERROR_MESSAGE);
			} else {
				CaseControllerBeanBusinessDelegate caseBusinessDelegate = XhibitDelegateHelper.getCaseDelegate();
				// updated check for future fixtures - ctx-3319
				ArrayList<FixtureDeftAttendingBasicValue> fdaBVs = 
						(ArrayList<FixtureDeftAttendingBasicValue>)caseBusinessDelegate.findREDELFutureFixturesDef(caseBV.getCaseId(), def.getDefendantOnCaseId(), new Date());
				// Updated check for future listings - ctx-3319
				ArrayList<DefOnCaseOnListBasicValue> docolBVs = 
						(ArrayList<DefOnCaseOnListBasicValue>)caseBusinessDelegate.findREDELFutureListingsDef(caseBV.getCaseId(), def.getDefendantOnCaseId(), new Date());
				if ((!fdaBVs.isEmpty()) || (!docolBVs.isEmpty())) {
					JOptionPane.showMessageDialog(null,
							getResourceBundle("defendantAppellantReplaceDelete.message.removeFutureListings", new Object[] {defAppellant}),
							getResourceBundle("defendantAppellantReplaceDelete.message.deleteTitle"), JOptionPane.ERROR_MESSAGE);
				} else {

					int result = JOptionPane.showConfirmDialog(null,
							getResourceBundle("defendantAppellantReplaceDelete.message.confirmDelete", new Object[] {defAppellant.toLowerCase()}),
							"Confirm Choice", JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.OK_OPTION) {
						defsToDelete.add(def);
						for (int i = 0; i < defsToUpdate.size(); i++) {
							if (def.getDefendantID() == defsToUpdate.get(i).getDefendantID()) {
								defsToUpdate.remove(i);
							}
						}

						tableModel.removeRow(selectedRow);
						table.repaint();
						table.clearSelection();
						btnSave.setEnabled(true);
						btnDelete.setEnabled(false);
						btnReplace.setEnabled(false);
						int rows = table.getRowCount();
						for (int i = 0; i < rows; i++) {
							DefendantOnCaseBasicValue tableDef = (DefendantOnCaseBasicValue) table.getModel()
									.getValueAt(table.getRowSorter().convertRowIndexToModel(i), 5);
							if (tableDef.getDefendantNumber() > def.getDefendantNumber()) {
								tableDef.setDefendantNumber(tableDef.getDefendantNumber() - 1);
								defsToUpdate.add(tableDef);
								table.getModel().setValueAt(tableDef.getDefendantNumber(), i, 0);

							}
						}
						
						changesMade = true;
						btnSave.requestFocus();
					}
				}

			}
		}
	}

	/**
	 * Action for what happens when replace button is clicked.
	 *
	 */
	private class replaceAction extends XAction {
		REDELPanel parent;
		private static final long serialVersionUID = 1L;

		public replaceAction(REDELPanel parent) {
			this.parent = parent;
			populateFromBundle("ReplaceDefendantButton");
			if (caseBV != null) {
				if (caseBV.getCaseType().equals("A")) {
					populateFromBundle("DeleteAppellantButton");
				}
			}
			setCaller(parent);
		}

		@SuppressWarnings("unchecked")
		public void xActionPerformed(ActionEvent ae) throws Exception {
			DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
			Integer selectedRow = table.getRowSorter().convertRowIndexToModel(table.getSelectedRow());

			DefendantOnCaseBasicValue def = (DefendantOnCaseBasicValue) tableModel.getValueAt(selectedRow, 5);
			
			CaseControllerBeanBusinessDelegate caseBusinessDelegate = XhibitDelegateHelper.getCaseDelegate();
			// updated check for future fixtures - ctx-3319
			ArrayList<FixtureDeftAttendingBasicValue> fdaBVs = 
					(ArrayList<FixtureDeftAttendingBasicValue>)caseBusinessDelegate.findREDELFutureFixturesDef(caseBV.getCaseId(), def.getDefendantOnCaseId(), new Date());
			// Updated check for future listings - ctx-3319
			ArrayList<DefOnCaseOnListBasicValue> docolBVs = 
					(ArrayList<DefOnCaseOnListBasicValue>)caseBusinessDelegate.findREDELFutureListingsDef(caseBV.getCaseId(), def.getDefendantOnCaseId(), new Date());
			if ((!fdaBVs.isEmpty()) || (!docolBVs.isEmpty())) {
				JOptionPane.showMessageDialog(null,
						"You must remove all future listings and fixtures \n before the " + defAppellant
								+ " can be deleted or replaced","Replace Defendant", JOptionPane.ERROR_MESSAGE);
			} else {
				int result = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to replace this " + defAppellant.toLowerCase() + "?", "Confirm Choice",
						JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					DefendantAppellantSearchDialog defendantAppellantSearch;
					try {
						if (defAppellant.equals("Appellant")) {
							xac.getCaseStatus().setCaseType(CaseType.APPEAL);
						} else {
							xac.getCaseStatus().setCaseType(CaseType.TRIAL);
						}
						defendantAppellantSearch = new DefendantAppellantSearchDialog(xac,
								new DefendantAppellantSearchModel(xac, parent));
						defendantAppellantSearch.setLocationRelativeTo(xac);
						defendantAppellantSearch.setVisible(true);
					} catch (CSRecoverableException e1) {
						XHIBITConstant.handleError(e1, this.getClass());
					}
				}
			}
		}
	}

	public boolean detectDupliate(DefendantValue dv) {
		boolean duplicateFound = false;
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		int rowCount = table.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			DefendantValue def = (DefendantValue) tableModel.getValueAt(i, 6);
			if (def.getDefendantID().equals(dv.getDefendantID())) {
				duplicateFound = true;
				break;
			}

		}
		return duplicateFound;
	}

	public void callback(DefendantValue dv) {
		Integer selectedRow = table.getRowSorter().convertRowIndexToModel(table.getSelectedRow());
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		DefendantOnCaseBasicValue defBV = (DefendantOnCaseBasicValue) tableModel.getValueAt(selectedRow, 5);
		defBV.setDefendantID(dv.getDefendantID());
		changesMade = true;
		if (defBV.getDefendantNumber() == null) {
			defBV.setDefendantNumber(selectedRow + 1);
			table.getModel().setValueAt(selectedRow + 1, selectedRow, 0);
		}

		table.getModel().setValueAt(dv.getSurName(), selectedRow, 1);
		table.getModel().setValueAt(dv.getFirstName(), selectedRow, 2);
		table.getModel().setValueAt(dv.getGenderString(), selectedRow, 3);
		Calendar cal = dv.getDateOfBirth();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = "";
		if (dv.getDateOfBirth() != null) {
			formattedDate = sdf.format(cal.getTime());
		}
		for (int i = 0; i < defsToUpdate.size(); i++) {
			if (defBV.getDefendantID() == defsToUpdate.get(i).getDefendantID()) {
				defsToUpdate.remove(i);
			}
		}
		table.getModel().setValueAt(formattedDate, selectedRow, 4);
		table.getModel().setValueAt(defBV, selectedRow, 5);
		table.getModel().setValueAt(dv, selectedRow, 6);
		defsToReplace.add(defBV);
		table.clearSelection();
		btnDelete.setEnabled(false);
		btnReplace.setEnabled(false);
		btnSave.setEnabled(true);
	}

	private boolean saveSentenceTrialCase() {
		boolean success = false;
		for (int i = 0; i < defsToDelete.size(); i++) {
			int defOnCaseId = defsToDelete.get(i).getDefendantOnCaseId();
			CaseControllerBeanBusinessDelegate caseBusinessDelegate = XhibitDelegateHelper.getCaseDelegate();
			caseBusinessDelegate.REDELSentenceTrialDelete(defOnCaseId);
			success = true;
		}

		for (int i = 0; i < defsToReplace.size(); i++) {
			DefendantControllerBeanBusinessDelegate del = XhibitDelegateHelper.getDefendantDelegate();
			try {
				del.updateDefendantOnCaseBasicValue(defsToReplace.get(i), XhibitSingleton.getInstance().getUserSession()
						.getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
				success = true;
			} catch (DefendantControllerException e) {
				success = false;
				log.error("Unable to delete Defendant on case "+e);
			}
		}

		for (int i = 0; i < defsToUpdate.size(); i++) {
			DefendantControllerBeanBusinessDelegate del = XhibitDelegateHelper.getDefendantDelegate();
			try {
				del.updateDefendantOnCaseBasicValue(defsToUpdate.get(i), XhibitSingleton.getInstance().getUserSession()
						.getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
				success = true;
			} catch (DefendantControllerException e) {
				success = false;
				log.error("Unable to update Defendant on case "+e);
			}
		}

		return success;
	}

	private boolean saveAppealCase() {
		boolean success = false;
		for (int i = 0; i < defsToDelete.size(); i++) {
			int defOnCaseId = defsToDelete.get(i).getDefendantOnCaseId();
			CaseControllerBeanBusinessDelegate caseBusinessDelegate = XhibitDelegateHelper.getCaseDelegate();
			caseBusinessDelegate.REDELAppealDelete(caseBV.getCaseId(), defOnCaseId);
			success = true;
		}

		for (int i = 0; i < defsToReplace.size(); i++) {
			DefendantControllerBeanBusinessDelegate del = XhibitDelegateHelper.getDefendantDelegate();
			try {
				del.updateDefendantOnCaseBasicValue(defsToReplace.get(i), XhibitSingleton.getInstance().getUserSession()
						.getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
				success = true;
			} catch (DefendantControllerException e) {
				success = false;
				log.error("Unable to delete Appellant on case "+e);
			}
		}

		for (int i = 0; i < defsToUpdate.size(); i++) {
			DefendantControllerBeanBusinessDelegate del = XhibitDelegateHelper.getDefendantDelegate();
			try {
				del.updateDefendantOnCaseBasicValue(defsToUpdate.get(i), XhibitSingleton.getInstance().getUserSession()
						.getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
				success = true;
			} catch (DefendantControllerException e) {
				success = false;
				log.error("Unable to update Appellant on case "+e);
			}
		}

		return success;
	}

	protected String getResourceBundle(String key) {
		return XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, key);
	}

	protected String getResourceBundle(String resourceKey, Object[] objects) {
		return MessageFormat.format(getResourceBundle(resourceKey), objects);
	}

}
