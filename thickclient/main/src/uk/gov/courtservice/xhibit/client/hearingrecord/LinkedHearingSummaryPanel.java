package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordConstants;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordException;
import uk.gov.courtservice.xhibit.business.vos.entities.BwHistoryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRDefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRHearingDisplayValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingListSummaryValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.PrintFunction;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sherie De Silva
 * @version 1.0
 */
public class LinkedHearingSummaryPanel extends XPanel implements PrintFunction {

	private static final long serialVersionUID = 1L;
	private static final String NO = "N";
	private static final String SAVED = "S";

	private final Logger log = CSServices.getLogger(LinkedHearingSummaryPanel.class);

	// only to be set in the constructor
	private HearingRecordModel model;
	private LinkedHearingSummaryDialog parentDialog;
	private LinkedHearingSummaryPanel thisClass;

	private JTable table;

	private JButton saveBtn;

	private JButton cfaBtn;

	private JButton printBtn;

	private JButton linkBtn;

	private JButton unlinkBtn;

	private JTextField statusText;

	private JTextField ccText;

	private Vector<String> columnNames = new Vector<String>();

	public LinkedHearingSummaryPanel(HearingRecordModel model, LinkedHearingSummaryDialog parent) {
		super();
		this.model = model;
		this.parentDialog = parent;
		thisClass = this;

		// nothing else is utilising these objects, so setting all to
		// use the same dimension object
		final Dimension size = new Dimension(560, 420);

		this.setMaximumSize(size);
		this.setMinimumSize(size);
		this.setPreferredSize(size);

		try {
			stepInitialise();
			jbInit();
			stepActivate();
		} catch (Exception e) {
			log.error("Error constructing LinkedHearingSummaryPanel", e);
			e.printStackTrace();

		}
	}

	public void jbInit() {
		// create all of the panels...
		final JPanel panel = new JPanel();
		final JPanel buttonPanel = new JPanel();
		final JPanel exportInfoPanel = new JPanel();

		// scroll pane created later in method...
		final JScrollPane scrollPane;

		// create the constraints object used...
		final GridBagConstraints constraints = new GridBagConstraints(GridBagConstraints.RELATIVE, 0, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0);

		// set each panels layout manager...
		panel.setLayout(new GridBagLayout());
		buttonPanel.setLayout(new GridBagLayout());
		exportInfoPanel.setLayout(new GridBagLayout());

		// set the border...
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
				getResource("linkedHearings")));

		// create all of the buttons...
		this.saveBtn = new JButton(new SaveFormAAction(this));
		this.cfaBtn = new JButton(new OpenFormAAction(this));
		this.printBtn = createJButton(XhibitActions.PrintCrestFormA);
		this.linkBtn = createJButton(XhibitActions.LinkHearing);
		this.unlinkBtn = createJButton(XhibitActions.UnlinkHearing);

		// ... and all of the labels...
		final JLabel statusLabel = new JLabel(getResource("status"));
		final JLabel courtClerkLabel = new JLabel(getResource("courtClerk"));

		columnNames.add(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "case"));
		columnNames.add(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "defendant"));
		columnNames.add("Model");

		Vector columnData = getTableData();

		table = new JTable();
		// Sort this out to be one set model
		table.setModel(new DefaultTableModel(columnData, columnNames));
		this.table.setPreferredScrollableViewportSize(new Dimension(450, 250));
		this.table.setDefaultEditor(Object.class, null);
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane = new JScrollPane(this.table);
		scrollPane.setMinimumSize(new Dimension(450, 250));

		// populate the button panel with all of the buttons...
		buttonPanel.add(linkBtn, constraints);
		buttonPanel.add(unlinkBtn, constraints);
		buttonPanel.add(printBtn, constraints);
		buttonPanel.add(saveBtn, constraints);
		buttonPanel.add(cfaBtn, constraints);

		statusText = new JTextField(12);
		statusText.setEditable(false);
		ccText = new JTextField(12);
		ccText.setEditable(false);

		exportInfoPanel.add(statusLabel, constraints);
		exportInfoPanel.add(statusText, constraints);
		exportInfoPanel.add(courtClerkLabel, constraints);
		exportInfoPanel.add(ccText, constraints);

		// now put all created panels on to the main one
		// as we are re-using the constraints object, set certain fields
		constraints.gridx = 0;
		constraints.gridy = GridBagConstraints.RELATIVE;
		constraints.fill = GridBagConstraints.REMAINDER;
		panel.add(scrollPane, constraints);
		constraints.fill = GridBagConstraints.NONE;
		panel.add(exportInfoPanel, constraints);
		panel.add(buttonPanel, constraints);

		// and add the main panel to this panel...
		this.add(panel);
	}

	/**
	 * @return
	 */
	public Vector getTableData() {
		Vector columnData = new Vector();

		for (int i = 0; i < model.getHearingSummaryVal().getHearingListSummaryValues().size(); i++) {
			HearingListSummaryValue hlsVal = (HearingListSummaryValue) ((ArrayList) model.getHearingSummaryVal()
					.getHearingListSummaryValues()).get(i);

			for (int j = 0; j < hlsVal.getHrDefendantValues().size(); j++) {
				Vector v = new Vector();

				String caseNo = "";
				caseNo = hlsVal.getCaseType() + hlsVal.getCaseNumber();
				v.add(caseNo);

				HRDefendantValue hrdVal = (HRDefendantValue) ((ArrayList) hlsVal.getHrDefendantValues()).get(j);
				String dName = "";
				if ((hrdVal.getFirstName() != null) && (!hrdVal.getFirstName().equals(""))) {
					dName = dName + hrdVal.getFirstName() + " ";
				}
				if ((hrdVal.getMiddleName() != null) && (!hrdVal.getMiddleName().equals(""))) {
					dName = dName + hrdVal.getMiddleName() + " ";
				}
				if ((hrdVal.getSurname() != null) && (!hrdVal.getSurname().equals(""))) {
					dName = dName + hrdVal.getSurname();
				}
				v.add(dName);

				HearingRecordModel hrModel = new HearingRecordModel();
				hrModel.setHearingId(hlsVal.getHearingID());
				hrModel.setDefendantId(hrdVal.getDefendantID());
				hrModel.setXac((XhibitApplicationController) parentDialog.getParentFrame());
				hrModel.setCaseId(hlsVal.getCaseID());
				hrModel.setCaseNumber(hlsVal.getCaseType()+hlsVal.getCaseNumber());

				try {
					hrModel.setHearingRecordVal(XhibitDelegateHelper.getHearingDelegate().retrieveHearingRecord(
							hrModel.getHearingId(), hrModel.getDefendantId(), XhibitSingleton.getInstance()
									.getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME)));

					hrModel.setDefOnCaseBasicValue(XhibitDelegateHelper.getDefendantDelegate()
							.getDefendantOnCaseBasicValue(hrModel.getDefendantId(), hlsVal.getCaseID()));

				} catch (Exception eee) {
					eee.printStackTrace();
				}

				v.add(hrModel);
				columnData.add(v);
			}

			// Display cases without defendants e.g. U-cases.
			if (hlsVal.getHrDefendantValues() == null || hlsVal.getHrDefendantValues().size() == 0) {
				Vector v = new Vector();
				v.add(hlsVal.getCaseType() + hlsVal.getCaseNumber());
				v.add("");

				for (int k = 0; k < hlsVal.getHrDefendantValues().size(); k++) {
					HRDefendantValue hrdVal = (HRDefendantValue) ((ArrayList) hlsVal.getHrDefendantValues()).get(k);

					HearingRecordModel hrModel = new HearingRecordModel();
					hrModel.setHearingId(hlsVal.getHearingID());
					hrModel.setDefendantId(hrdVal.getDefendantID());

					try {
						hrModel.setHearingRecordVal(XhibitDelegateHelper.getHearingDelegate().retrieveHearingRecord(
								hrModel.getHearingId(), hrModel.getDefendantId(), XhibitSingleton.getInstance()
										.getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME)));

						hrModel.setDefOnCaseBasicValue(XhibitDelegateHelper.getDefendantDelegate()
								.getDefendantOnCaseBasicValue(hrModel.getDefendantId(), hlsVal.getCaseID()));

					} catch (HearingRecordException e) {
						e.printStackTrace();
					} catch (DefendantControllerException e) {
						e.printStackTrace();
					}
					v.add(hrModel);
				}

				columnData.add(v);
			}

		}
		return columnData;
	}

	public HearingRecordModel getModel() {
		return model;
	}

	public void stepInitialise() throws CSRecoverableException {
		log.debug("Hearing Id " + model.getHearingId());

		model.setHearingSummaryVal(XhibitDelegateHelper.getHearingDelegate()
				.retrieveHearingSummaryValue(model.getLeadHearingId(), XhibitSingleton.getInstance().getUserSession()
						.getSessionProperty(UserTerminalProperties.DISPLAY_NAME)));
	}

	public void stepDeactivate() throws CSRecoverableException {
		// Empty
	}

	public void stepValidate() throws CSRecoverableException, CSValidationException {
		// Empty
	}

	/**
	 * XPanel implementation call when screen state changes
	 * 
	 * @throws CSRecoverableException
	 */
	public void stepUpdateViewState() throws CSRecoverableException {
		final boolean inEditMode = model.getXac().getApplicationCaseModel()
				.isInEditMode(FunctionList.EExportHearingRecord);
		final boolean hearingSelected = (table.getSelectedRow() != -1);
		final boolean canViewFormA = isFormAViewableCase(table.getSelectedRow());
		calculateExportStatus();
		final boolean defendantSelected = defendantSelected();

		// check to see if selection is made in table before enabling buttons
		cfaBtn.setEnabled(hearingSelected && defendantSelected && canViewFormA);
		printBtn.setEnabled(hearingSelected && canViewFormA);

		linkBtn.setEnabled(inEditMode && hearingSelected);
		unlinkBtn.setEnabled(inEditMode && hearingSelected);

		// also checking if export status is ok to allow export before enabling
		// export button.
		saveBtn.setEnabled(inEditMode && hearingSelected);
	}

	// PR5673 It is possible for all the defendants to have been marked as
	// obsolete
	// in which case the defendant column will be empty.
	private boolean defendantSelected() {
		final int selectedRow = this.table.getSelectedRow();
		if (selectedRow == -1)
			return false;
		String str = (String) this.table.getModel().getValueAt(selectedRow, 1);
		return !str.equals("");
	}

	/**
	 * Determines if the case for the selected row has a case type/case sub-type
	 * combination that is acceptable for viewing in CREST Form A. This is a
	 * cut-down clone of the getSelectedRowDetails( ) method.
	 * 
	 * @param theRow
	 *            - the index of the selected row. This will be -1 when no row
	 *            is selected
	 * @return - true if the case may be viewed in CREST Form A
	 */
	private boolean isFormAViewableCase(int theRow) {
		boolean canViewFormA = false;
		int count = 0;

		final ArrayList hearingListSummaryValuesList = (ArrayList) model.getHearingSummaryVal()
				.getHearingListSummaryValues();

		for (int x = 0; x < hearingListSummaryValuesList.size(); x++) {
			HearingListSummaryValue hlsVal = (HearingListSummaryValue) hearingListSummaryValuesList.get(x);

			final ArrayList defendantValuesList = (ArrayList) hlsVal.getHrDefendantValues();

			if (defendantValuesList.size() == 0) {
				if (theRow == count) {
					canViewFormA = checkValidCaseType(hlsVal.getCaseType(), hlsVal.getCaseSubType());
				}

				count++;
			} else {
				for (int y = 0; y < defendantValuesList.size(); y++) {
					if (theRow == count) {
						canViewFormA = checkValidCaseType(hlsVal.getCaseType(), hlsVal.getCaseSubType());
					}

					count++;
				}
			}
		}

		return canViewFormA;
	}

	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		// Empty
	}

	public void stepActivate() throws CSRecoverableException {

		// adding selection listener to table for enabling/disabling buttons
		final ListSelectionModel lsm = new DefaultListSelectionModel();
		lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		lsm.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent lse) {
				try {
					if (!lse.getValueIsAdjusting()) {
						stepUpdateViewState();
					}
				} catch (Exception e) {
					XHIBITErrorHandler.handleError(e);
				}
			}
		});

		this.table.setSelectionModel(lsm);

		// resetting table model data and repainting table when updating the
		// screen.
		Vector columnData = getTableData();
		this.table.setModel(new DefaultTableModel(columnData, columnNames));
		this.table.repaint();
		table.getColumnModel().getColumn(2).setMinWidth(0);
		table.getColumnModel().getColumn(2).setMaxWidth(0);
		table.getColumnModel().getColumn(2).setWidth(0);

		// Change 10/11/2014 to populate the display name instead of the
		// username into the court clerk field
		calculateExportStatus();

		// always determine if the buttons should be disabled or not...
		stepUpdateViewState();
	}

	/**
	 * 
	 */
	public void calculateExportStatus() {
		String courtClerkDisplayName = "";
		if (XhibitSingleton.getInstance().getUserSession()
				.getSessionProperty(UserTerminalProperties.DISPLAY_NAME) != null
				&& XhibitSingleton.getInstance().getUserSession()
						.getSessionProperty(UserTerminalProperties.DISPLAY_NAME).length() > 0) {
			courtClerkDisplayName = XhibitSingleton.getInstance().getUserSession()
					.getSessionProperty(UserTerminalProperties.DISPLAY_NAME);
		} else {
			courtClerkDisplayName = XhibitSingleton.getInstance().getUserSession().getUserName();
		}
		
		if (table.getSelectedRow() != -1) {
			HearingRecordModel hrModel = (HearingRecordModel) table.getValueAt(table.getSelectedRow(), 2);
			if(hrModel!=null && hrModel.getHearingRecordUpdateVal()!=null && hrModel.getHearingRecordUpdateVal().getDefHearingRecordValue()!=null) {
				String courtClerkName = hrModel.getHearingRecordUpdateVal().getDefHearingRecordValue()
						.getFormACourtClerk();
				String statusFlag = hrModel.getHearingRecordUpdateVal().getDefHearingRecordValue().getFormAStatus();
				
				if (statusFlag == null || statusFlag.equals(NO)){
					this.statusText.setText(getResource("notExported"));
					hrModel.setCourtClerk(courtClerkDisplayName);
					this.ccText.setText(hrModel.getCourtClerk());
				} else {
					// as used in several places, store locally
					getSelectedRowDetails(null);
					model = getModel();			
	
					// setting court clerk
					this.ccText.setText(courtClerkName);
					hrModel.setCourtClerk(courtClerkName);
	
					if (HearingRecordConstants.READY_FOR_EXPORT.equals(statusFlag)) {
						this.statusText.setText(getResource("readyForExport"));
					} else if (HearingRecordConstants.IN_PROGRESS.equals(statusFlag)) {
						this.statusText.setText(getResource("exportInProgress"));
					} else if (HearingRecordConstants.EXPORT_SUCCESS.equals(statusFlag)) {
						this.statusText.setText(getResource("exportSuccess"));
						hrModel.setCourtClerk(courtClerkDisplayName);
					} else if (HearingRecordConstants.EXPORT_FAILED.equals(statusFlag)) {
						this.statusText.setText(getResource("exportFailed"));
						hrModel.setCourtClerk(courtClerkDisplayName);
						this.ccText.setText(model.getCourtClerk());
					} else if (HearingRecordConstants.EXPORT_LOCKED.equals(statusFlag)) {
						this.statusText.setText(getResource("exportLocked"));
					} else if (SAVED.equals(statusFlag)) {
						this.statusText.setText(getResource("exportSuccess"));
						hrModel.setCourtClerk(courtClerkDisplayName);
					}
				}
				table.setValueAt(hrModel, table.getSelectedRow(), 2);
			}
		}
	}

	public void getSelectedRowDetails(Integer selectedRow) {
		// setting the Case Id, Hearing Id and Defendant Id in model from
		// selected row in table
		int count = 0;
		// we only want to cast it to its required type once, so caching!
		final ArrayList hearingListSummaryValuesList = (ArrayList) model.getHearingSummaryVal()
				.getHearingListSummaryValues();

		// do not calculate this every loop, it will not change between
		// iterations
		if (selectedRow == null) {
			selectedRow = this.table.getSelectedRow();
		}

		for (int i = 0; i < hearingListSummaryValuesList.size(); i++) {
			final HearingListSummaryValue hlsVal = (HearingListSummaryValue) hearingListSummaryValuesList.get(i);

			// we only want to cast it to its required type once, so
			// caching!
			final ArrayList defendantValuesList = (ArrayList) hlsVal.getHrDefendantValues();

			if (defendantValuesList.size() == 0) {
				count++;
			} else {
				for (int j = 0; j < defendantValuesList.size(); j++) {
					if (selectedRow == count) {
						model.setCaseId(hlsVal.getCaseID());
						model.setHearingId(hlsVal.getHearingID());
						HRDefendantValue hrdVal = (HRDefendantValue) defendantValuesList.get(j);
						model.setDefendantId(hrdVal.getDefendantID());
						model.setCaseNumber(hlsVal.getCaseType() + hlsVal.getCaseNumber());
						model.setLegallyAided(hrdVal.getLegallyAided());
					}
					count++;
				}

			}
		}
	}

	public boolean autoSaveToFile() {
		return false;
	}

	public String[] print() throws UserCancelException, CSRecoverableException {
		HearingRecordControllerHelper hrch = new HearingRecordControllerHelper();
		return hrch.formatHearingRecordForPrinting(model);
	}

	/**
	 * Private helper method used to factor out repetitive button creation code
	 * 
	 * @param action
	 * @return A <code>JButton</code>
	 */
	private JButton createJButton(String action) {
		final JButton button = new JButton();
		button.setAction(XhibitActions.getAction(this.model.getXac(), action));
		XhibitActions.getAction(this.model.getXac(), action).setCaller(this);
		button.setEnabled(false);

		return button;
	}

	/**
	 * Action for what happens when the Delete button is clicked
	 * 
	 * @author kudzinc
	 *
	 */
	private class OpenFormAAction extends XAction {
		private static final long serialVersionUID = 1L;

		public OpenFormAAction(LinkedHearingSummaryPanel parent) {
			populateFromBundle("FormA");
			setCaller(parent);
		}

		/**
		 * If button is clicked set obsolete to Y in the database for the
		 * objects.
		 */
		public void xActionPerformed(ActionEvent ae) throws Exception {
			XHIBITConstant.debug("in open crest form a action");
			HearingRecordModel hrModel;
			if ((HearingRecordModel) table.getValueAt(table.getSelectedRow(), 2) != null) {
				hrModel = (HearingRecordModel) table.getValueAt(table.getSelectedRow(), 2);
			} else {
				getSelectedRowDetails(null);
				hrModel = ((LinkedHearingSummaryPanel) this.getCaller()).getModel();
				hrModel.setHearingRecordVal(XhibitDelegateHelper.getHearingDelegate().retrieveHearingRecord(
						model.getHearingId(), model.getDefendantId(), XhibitSingleton.getInstance().getUserSession()
								.getSessionProperty(UserTerminalProperties.DISPLAY_NAME)));
			}
			hrModel.setCallingClass(thisClass);
			HearingRecordDialog d = new HearingRecordDialog((XhibitApplicationController) parentDialog.getParentFrame(),
					hrModel);
			d.setVisible(true);
		}
	}

	/**
	 * Action for what happens when the Delete button is clicked
	 * 
	 * @author kudzinc
	 *
	 */
	private class SaveFormAAction extends XAction {
		private static final long serialVersionUID = 1L;

		public SaveFormAAction(LinkedHearingSummaryPanel parent) {
			populateFromBundle("FormASave");
			setCaller(parent);
		}

		/**
		 * If button is clicked set obsolete to Y in the database for the
		 * objects.
		 */
		public void xActionPerformed(ActionEvent ae) throws Exception {
			HearingRecordModel hrModel = (HearingRecordModel) table.getModel().getValueAt(table.getSelectedRow(), 2);
			
			// set up the court clerk 
			String courtClerkDisplayName = "";
			if (XhibitSingleton.getInstance().getUserSession()
					.getSessionProperty(UserTerminalProperties.DISPLAY_NAME) != null
					&& XhibitSingleton.getInstance().getUserSession()
							.getSessionProperty(UserTerminalProperties.DISPLAY_NAME).length() > 0) {
				courtClerkDisplayName = XhibitSingleton.getInstance().getUserSession()
						.getSessionProperty(UserTerminalProperties.DISPLAY_NAME);
			} else {
				courtClerkDisplayName = XhibitSingleton.getInstance().getUserSession().getUserName();
			}
			
			boolean isJudgeAssigned = false;
			if (hrModel != null) {
				final HRHearingDisplayValue hRDisplayValue = hrModel.getHearingRecordVal().getHearingRecordDisplayValue().getHrHearingDisplayValue();

				if (hRDisplayValue != null) {
				Integer judgeId = hRDisplayValue.getHrJudgeValue().getRefJudgeID();
					if (judgeId == null || judgeId == 0){
						// display message and then stop further processing
						JOptionPane.showMessageDialog(thisClass,
								getErrorResource("gui.hearingrecord.hearingNoJudge"),
								getErrorResource("gui.formA.cannotSave.title"),
								JOptionPane.ERROR_MESSAGE);
						// save the status as not saved
						 saveFormAStatus(hrModel, courtClerkDisplayName, NO);
					}
					else{
						isJudgeAssigned = true;	
					}
				}
			}
			
			if (isJudgeAssigned && hrModel != null) {		

				/*
				 * ctx-2054, kudzinc. Find BwHistory by defOnCaseId, if
				 * record exists and BW_END_DATE is not null and
				 * bc_status_bw_ended is null should you insert a value into
				 * bc_staus_bw_ended
				 */
				ArrayList<BwHistoryBasicValue> bwVal = (ArrayList<BwHistoryBasicValue>) XhibitDelegateHelper
						.getBwHistoryDelegate().findByDefendantOnCaseId(hrModel.getHearingRecordUpdateVal()
								.getDefHearingRecordValue().getDefendantOnCaseID());
				if (bwVal != null) {
					for (int i = 0; i < bwVal.size(); i++) {
						if (bwVal.get(i) != null) {
							if (bwVal.get(i).getBwEndDate() != null && bwVal.get(i).getBcStatusBwEnded() == null) {
								if (hrModel.getHearingRecordUpdateVal().getDefHearingRecordValue()
										.getEndBailStatus() != null) {
									bwVal.get(i).setBcStatusBwEnded(hrModel.getHearingRecordUpdateVal()
											.getDefHearingRecordValue().getEndBailStatus());
									XhibitDelegateHelper.getBwHistoryDelegate().updateBwHistory(bwVal.get(i),
											XhibitSingleton.getInstance().getUserSession()
													.getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
								}
							}
						}

					}
				}
				calculateExportStatus(); 
			}				
		
			// reintroduced code to validate against business rules and to save the username and status
			// reusing the old export method and will amend to suit
			 try {  
				 if (isJudgeAssigned && hrModel != null) {
					 // call the old code to validate business rules	            		  
					 // the validateHearingRecord method checks the business rules for the form a
					 // does this at defendantHeraingRecord level
	                 XhibitDelegateHelper.getHearingDelegate().validateHearingRecord(
	                		 								hrModel.getDefOnCaseBasicValue().getDefendantOnCaseId(),
	                		 								hrModel.getHearingId(),
	                		 								hrModel.getCourtClerk(),
	                										XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME)
	                															  );
	                 // if get here business rules have passed so save the hearing record. Mainly want to save the status and court clerk
					 saveFormAStatus(hrModel, courtClerkDisplayName, SAVED);
				 }
	                  
                 model.setUpdated(false);
	 			 model.setDurationUpdated(false);
	 			 model.setRepresentationUpdated(false);
 				 if (table.getValueAt(table.getSelectedRow(), 2) != null) {
 				 	table.setValueAt(null, table.getSelectedRow(), 2);
 				 }
 				
 				 stepInitialise();
 				 stepActivate();
	  			 
	         } 
			 catch (HearingRecordException hre) {
	                // if an exception is thrown that the hearing is not yet ended
	                // then give the user the option to end the hearing or cancel
	                // the export.
	                // Note: we only want to check for this specific key since other
	                // exceptions can be thrown and if so just throw them again.
	                if (hre.getUserMessageAsMessage().getKey() != null
	                        && hre.getUserMessageAsMessage().getKey().equalsIgnoreCase(
	                                "hearingrecord.export.hearing_not_ended")) {
	                	
		                handleHearingNotEnded(ae);                  
	                   
	                } else {
	                	// set the status to not saved as failed business rules
		                saveFormAStatus(hrModel, courtClerkDisplayName, NO);
		                stepInitialise();
		 				stepActivate();
		 				// set blank as no row selected
		 				clearSelection();
	                    throw hre; // just re-throw the exception
	                }
	            }
			
			// set blank as no row selected
			 clearSelection();
			
		}

		/**
		 * clear the selected row and clear the statuses 
		 */
		private void clearSelection() {
			table.clearSelection();
			statusText.setText("");
			ccText.setText("");
		}
		
		/**
		 * Saves the status and court clerk
		 * @param hrModel
		 * @param courtClerkDisplayName
		 * @param newStatus
		 * @throws HearingRecordException
		 */
		private void saveFormAStatus(HearingRecordModel hrModel, String courtClerkDisplayName, String newStatus)
				throws HearingRecordException {
			
			// only save if the status needs to be changed
			if(!newStatus.equals( hrModel.getHearingRecordVal().getHearingRecordUpdateValue().getDefHearingRecordValue().getFormAStatus())){
			
				 String caseType = hrModel.getHearingRecordVal().getHearingRecordUpdateValue().getCaseType(); 
				 hrModel.getHearingRecordVal().getHearingRecordUpdateValue().getDefHearingRecordValue().setFormACourtClerk(courtClerkDisplayName); 
				 hrModel.getHearingRecordVal().getHearingRecordUpdateValue().getDefHearingRecordValue().setFormAStatus(newStatus); 
				 
				 XhibitDelegateHelper.getHearingDelegate().saveFormADetails(caseType, 
						 hrModel.getHearingRecordVal().getHearingRecordUpdateValue().getDefHearingRecordValue(),
							XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
			}
		}
		   /**
	     * Method to be used to populate a dialog to end a hearing. It will re-call
	     * the action event passed in after the hearing has been ended. The user can
	     * also cancel the whole transaction.
	     * 
	     * @param actionEvent
	     *            this ActionEvent
	     * @throws Exception
	     */
	    private void handleHearingNotEnded(ActionEvent actionEvent) throws Exception {
	        XHIBITConstant.debug("hearing not ended exception");

	        int reply = JOptionPane.showConfirmDialog(	thisClass, 
	        											XHIBITConstant.getResource(XhibitBundles.ViewCrestForms, "formA.export.hearingNotEnded"), 
	        											XHIBITConstant.getResource(XhibitBundles.ViewCrestForms, "formA.export.hearingNotEndedTitle"),
	        											JOptionPane.YES_NO_OPTION, 
	        											JOptionPane.ERROR_MESSAGE);
			
	        if (reply == JOptionPane.YES_OPTION) {
	        	HearingRecordModel hrModel = (HearingRecordModel) table.getValueAt(table.getSelectedRow(), 2);
	            XhibitDelegateHelper.getHearingDelegate().endUnendedHearing(
	            		hrModel.getDefOnCaseBasicValue().getDefendantOnCaseId(), hrModel.getHearingId(),
	                    XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
	            
	            // reset the Form data as don't want a lock exception
	            hrModel.setHearingRecordVal(XhibitDelegateHelper.getHearingDelegate().retrieveHearingRecord(
						hrModel.getHearingId(), hrModel.getDefendantId(), XhibitSingleton.getInstance()
								.getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME)));
	            this.xActionPerformed(actionEvent);
	        }
	    }
	}

	public void modelCallback(HearingRecordModel hrModel) throws CSRecoverableException {
		if (hrModel != null) {
			int savedSelectedRow = -1;
			if (table.getSelectedRow() != -1) {
				savedSelectedRow = table.getSelectedRow();				
				if (hrModel.isUpdated() || hrModel.isDurationUpdated()) {
					calculateExportStatus();					
				}
			}
			
			hrModel.setDurationUpdated(false);
			hrModel.setRepresentationUpdated(false); 
			stepInitialise();
			stepActivate();
			// highlight the previously selected row
			if(savedSelectedRow != -1){
				table.setRowSelectionInterval(savedSelectedRow, savedSelectedRow);
				if(NO.equals(hrModel.getHearingRecordUpdateVal().getDefHearingRecordValue().getFormAStatus()) ||
						hrModel.getHearingRecordUpdateVal().getDefHearingRecordValue().getFormAStatus() == null){
					this.statusText.setText(getResource("notExported"));					
				}
				else{
					this.statusText.setText(getResource("exportSuccess"));	
				}
		
			}
		}
	}

	/**
	 * Gets the resource for the given key from the ErrorText resource bundle.
	 * 
	 * @param key
	 *            the key to lookup in the ErrorText resource bundle
	 * @return the resource string for the given key
	 */
	private String getErrorResource(String key) {
		return ResourceBundleHelper.getResource(XhibitBundles.ErrorText, key);
	}

	/**
	 * Validate if a case if of valid type to proceed with CREST form A. A case
	 * is invalid if the case is of type - Bail (caseType: B) - U (caseType: U)
	 * - Misc appeal (caseType: A & caseSubType: O)
	 * 
	 * @param caseType
	 *            String
	 * @param caseSubType
	 *            String
	 * @return boolean - false if the case is not valid and true if valid.
	 */
	private boolean checkValidCaseType(String caseType, String caseSubType) {
		boolean validCase = false;

		if (caseType == null) {
			return validCase;
		}

		if (caseType.equalsIgnoreCase(HearingRecordConstants.CASE_TYPE_BAIL)) {
			validCase = false;
		} else if (caseType.equalsIgnoreCase(HearingRecordConstants.CASE_TYPE_U_CASE)) {
			validCase = false;
		} else if (caseType.equalsIgnoreCase(HearingRecordConstants.CASE_TYPE_APPEAL)) {
			if (caseSubType != null && caseSubType.equalsIgnoreCase(HearingRecordConstants.CASE_SUB_TYPE_MISC_APPEAL)) {
				validCase = false;
			} else {
				validCase = true;
			}
		} else {
			validCase = true;
		}

		return validCase;
	}

	/**
	 * Gets the resource for the given key from the HearingRecord resource
	 * bundle.
	 * 
	 * @param key
	 *            the key to lookup in the HearingRecord resource bundle
	 * @return the resource string for the given key
	 */
	private String getResource(String key) {
		return ResourceBundleHelper.getResource(XhibitBundles.HearingRecord, key);
	}		    
 
}