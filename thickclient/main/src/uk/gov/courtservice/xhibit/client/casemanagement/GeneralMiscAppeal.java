package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import mseries.Calendar.MFieldListener;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.client.casemanagement.util.CaseMethods;
import uk.gov.courtservice.xhibit.client.util.CaseMaintenanceConstants;
import uk.gov.courtservice.xhibit.client.util.XCheckBox;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * @author C.Kudzin - Feb 21, 2018 - Implemented MaxLength on text fields, and
 *         changed JP's from 255 to 30
 * @author C.Kudzin - Feb 27, 2018 - CTX-1395 Implementing further date
 *         validation
 */
public class GeneralMiscAppeal extends GeneralAllCasesTab {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private Integer version;

	Calendar nullDate = null;

	// error labels.
	private JLabel lblICaseTitle;
	private JLabel lblIDateAppealLodged;
	private JLabel lblIDateReceived;
	private JLabel lblIReceivingSite;
	private JLabel lblIOrigCaseNo;
	private JLabel lblInvalidAppealDescription;
	private JLabel lblIAppealedAgainst;
	private JLabel lblIAppealedAgainst2;
	private JLabel lblIOriginatingBodyDecisionDate;
	private JLabel lblIOriginalJP1;
	private JLabel lblIOriginalJP2;
	private JLabel lblIOriginalJP3;
	private JLabel lblIOriginalJP4;
	private JLabel lblCaseNumber;
	private JLabel lblITransferDate;

	// xtextfields
	private XTextField txtCaseTitle;
	private XTextField txtOrigCaseNo;
	private XTextField txtAppealedAgainst;
	private XTextField txtOriginalJps1;
	private XTextField txtOriginalJps2;
	private XTextField txtOriginalJps3;
	private XTextField txtOriginalJps4;

	// xcheckboxes
	private XCheckBox chckbxRehearing;
	private XCheckBox chckbxTransferIn;

	// xcomboboxes
	private XComboBox cmbAppealedAgainst;
	private XComboBox cmbReceivingSite;
	private XComboBox cmbTransferFrom;

	// xdatepanel
	private XDatePanel dtDateAppealLodged;
	private XDatePanel dtDateReceived;
	private XDatePanel dtTransferDate;
	private XDatePanel dtOriginatingBodyDecisionDate;

	// dropdown values
	private ArrayList<RefCourtBasicValue> receivedFromValues;
	private ArrayList<CourtSiteBasicValue> receivingSite;

	// parent case panel
	private CaseXPanel caseX;

	private JLabel lblCaseTitle;
	private JLabel lblAppealOf;
	private JLabel lblReceivingSite;
	private JLabel lblAppealDescription;
	private JLabel lblDateReceived;
	private JLabel lblDateAppealLodged;
	private JPanel pnlMagistratesCourtDetails;
	private JLabel lblAppealedAgainst;
	private JLabel lbl1;
	private JLabel lbl2;
	private JLabel lbl3;
	private JLabel lbl4;

	private JLabel lblITransferFrom;
	private JLabel lblTransferDate;
	private JLabel lblTransferFrom;
	private JLabel lblOrigCaseNo;
	private JLabel lblOriginatingBodyDecisionDate;
	private JLabel lblRehearing;

	private JPanel caseDetailsPanel;
	private JPanel magistratesCourtDetailsPanel;
	private JPanel transferDetailsPanel;
	private JPanel originalJPsPanel;

	// textarea
	private XTextField txtAreaAppealDescription;

	// array of all the invalid entry fields
	private List<JLabel> validationFields = new ArrayList<JLabel>();
	private List<Object> mandatoryFields = new ArrayList<Object>();

	private boolean fieldsChangedGlobal = false;

	/**
	 * Used for logging and exception handling.
	 */
	private static final String CLASS_NAME = ".GeneralMiscAppeal";

	public GeneralMiscAppeal(final CaseXPanel caseX) {
		initDropdownTypes();
		initCivilUnrestPanel(validationFields);

		lblCaseNumber = new JLabel("Case Number");
		lblCaseNumber.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCaseNumber.setBorder(new LineBorder(new Color(0, 0, 0)));

		lblCaseTitle = new JLabel("Case Title");

		lblAppealOf = new JLabel("Appeal of");
		lblAppealOf.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblAppealOf.setHorizontalAlignment(SwingConstants.RIGHT);

		lblICaseTitle = CaseMethods.createErrorLabel(validationFields);

		txtCaseTitle = new XTextField(72, "^.{1,72}$", lblICaseTitle, true);
		txtCaseTitle.setDisabledTextColor(Color.BLACK);
		txtCaseTitle.setGridBagLayout(true);
		txtCaseTitle.setUpperCase(true);
		txtCaseTitle.setMaxLength(72);

		CaseMethods.addRemoveFromMandatoryFields(txtCaseTitle, lblCaseTitle, mandatoryFields, true);

		txtCaseTitle.addFocusListener(new MandatoryFieldsFocusListener());

		lblDateReceived = new JLabel("Date Received");

		lblIDateReceived = CaseMethods.createErrorLabel(validationFields);

		dtDateReceived = new XDatePanel(this, Calendar.getInstance(), true, lblIDateReceived, CaseMaintenanceConstants.BEFORE);
		dtDateReceived.setGridBagLayout(true);
		
		CaseMethods.addRemoveFromMandatoryFields(dtDateReceived, lblDateReceived, mandatoryFields, true);

		dtDateReceived.getDateComponent().addMFieldListener(new DateReceivedListener());

		lblDateAppealLodged = new JLabel(CaseMaintenanceConstants.DATE_APPEAL_LODGED);

		lblIDateAppealLodged = CaseMethods.createErrorLabel(validationFields);

		dtDateAppealLodged = new XDatePanel(this, nullDate, true, lblIDateAppealLodged, CaseMaintenanceConstants.BEFORE, dtDateReceived,
				CaseMaintenanceConstants.DATE_APPEAL_LODGED, "Date Received");
		dtDateAppealLodged.setGridBagLayout(true);
		
		CaseMethods.addRemoveFromMandatoryFields(dtDateAppealLodged, lblDateAppealLodged, mandatoryFields, true);

		dtDateAppealLodged.setCustomWarning(CaseMaintenanceConstants.ON_BEFORE_DATE_RECEIVED);
		dtDateAppealLodged.getDateComponent().addMFieldListener(new DateAppealLodgedListener());

		lblReceivingSite = new JLabel("Receiving Site");

		lblIReceivingSite = CaseMethods.createErrorLabel(validationFields);

		cmbReceivingSite = new XComboBox(false, lblIReceivingSite);
		cmbReceivingSite.setGridBagLayout(true);
		// Added nullguards as if accessed from court other than snaresbrook
		// there's no data to populate, ctx-1428
		if (!receivingSite.isEmpty()) {
			cmbReceivingSite.setModel(new DefaultComboBoxModel(receivingSite.toArray()));
			cmbReceivingSite.setRenderer(new DropdownBoxCellRender());
		}
		

		lblAppealDescription = new JLabel("Appeal Description");

		lblInvalidAppealDescription = CaseMethods.createErrorLabel(validationFields);

		// (.|\n) change made so that it accepts new lines as well (enter key)
		txtAreaAppealDescription = new XTextField(70, "^(.|\n){1,70}$", lblInvalidAppealDescription, true);
		txtAreaAppealDescription.setMaxLength(70);
		txtAreaAppealDescription.setGridBagLayout(true);
		txtAreaAppealDescription.setUpperCase(true);
		
		CaseMethods.addRemoveFromMandatoryFields(txtAreaAppealDescription, lblAppealDescription, mandatoryFields, true);

		txtAreaAppealDescription.addFocusListener(new MandatoryFieldsFocusListener());

		txtAreaAppealDescription.getDocument().addDocumentListener(new AppealDescriptionListener());
		txtAreaAppealDescription.setColumns(10);
		txtAreaAppealDescription.setMinimumSize(txtAreaAppealDescription.getPreferredSize());

		pnlMagistratesCourtDetails = new JPanel();

		lblIAppealedAgainst2 = CaseMethods.createErrorLabel(validationFields);

		lblAppealedAgainst = new JLabel("Appealed Against");

		lblIAppealedAgainst = CaseMethods.createErrorLabel(validationFields);

		txtAppealedAgainst = new XTextField(5, "^[a-zA-Z0-9]{1,5}$", lblIAppealedAgainst, false);
		txtAppealedAgainst.setGridBagLayout(true);
		txtAppealedAgainst.setUpperCase(true);

		// add a on key press
		txtAppealedAgainst.addKeyListener(new AppealAgainstKeyListener());
		txtAppealedAgainst.addFocusListener(new AppealAgainstFocusListener());
		pnlMagistratesCourtDetails.add(txtAppealedAgainst);

		cmbAppealedAgainst = new XComboBox(false, lblIAppealedAgainst2, txtAppealedAgainst);
		cmbAppealedAgainst.setGridBagLayout(true);
		// Added nullguards as if accessed from court other than snaresbrook
		// there's no data to populate, ctx-1428
		if (!receivedFromValues.isEmpty()) {
			cmbAppealedAgainst.setModel(new DefaultComboBoxModel(receivedFromValues.toArray()));
			cmbAppealedAgainst.setRenderer(new DropdownBoxCellRender());
		}
		cmbAppealedAgainst.addFocusListener(new AppealAgainstComboListener()); 
		

		lbl1 = new JLabel("1.");

		lbl2 = new JLabel("2.");

		lbl3 = new JLabel("3.");

		lbl4 = new JLabel("4.");

		lblIOriginalJP1 = CaseMethods.createErrorLabel(validationFields);

		lblIOriginalJP2 = CaseMethods.createErrorLabel(validationFields);

		lblIOriginalJP3 = CaseMethods.createErrorLabel(validationFields);

		lblIOriginalJP4 = CaseMethods.createErrorLabel(validationFields);

		txtOriginalJps1 = new XTextField(255, CaseMaintenanceConstants.JPS_VAL, lblIOriginalJP1, false);
		txtOriginalJps1.setGridBagLayout(true);
		txtOriginalJps1.setUpperCase(true);
		txtOriginalJps1.setMaxLength(35);

		txtOriginalJps2 = new XTextField(255, CaseMaintenanceConstants.JPS_VAL, lblIOriginalJP2, false);
		txtOriginalJps2.setGridBagLayout(true);
		txtOriginalJps2.setMaxLength(35);
		txtOriginalJps2.setUpperCase(true);

		txtOriginalJps3 = new XTextField(255, CaseMaintenanceConstants.JPS_VAL, lblIOriginalJP3, false);
		txtOriginalJps3.setGridBagLayout(true);
		txtOriginalJps3.setUpperCase(true);
		txtOriginalJps3.setMaxLength(35);

		txtOriginalJps4 = new XTextField(255, CaseMaintenanceConstants.JPS_VAL, lblIOriginalJP4, false);
		txtOriginalJps4.setGridBagLayout(true);
		txtOriginalJps4.setUpperCase(true);
		txtOriginalJps4.setMaxLength(35);

		chckbxRehearing = new XCheckBox();
		chckbxRehearing.addItemListener(new ChckbxRehearingListener());

		chckbxRehearing.setHorizontalTextPosition(SwingConstants.LEFT);
		chckbxTransferIn = new XCheckBox();
		chckbxTransferIn.addItemListener(new ChckbxTransferListener());
		chckbxTransferIn.setEnabled(true);
		chckbxTransferIn.setHorizontalTextPosition(SwingConstants.LEFT);

		lblIOrigCaseNo = CaseMethods.createErrorLabel(validationFields);

		lblITransferDate = CaseMethods.createErrorLabel(validationFields);

		lblTransferDate = new JLabel("Transfer Date");

		dtTransferDate = new XDatePanel(this, nullDate, false, lblITransferDate, CaseMaintenanceConstants.BEFORE, dtDateAppealLodged, "Date",
				CaseMaintenanceConstants.DATE_APPEAL_LODGED);
		dtTransferDate.setGridBagLayout(true);
		dtTransferDate.setDateEnabled(false);
		dtTransferDate.setEnabledAndFocusable(false);
		dtTransferDate.addFocusListener(new MandatoryFieldsFocusListener());
		dtTransferDate.setCustomWarning("Must be on/before Date Appeal Lodged");
		dtTransferDate.getDateComponent().addMFieldListener(new TransferDateListener());

		lblOrigCaseNo = new JLabel("Orig Case No.");

		txtOrigCaseNo = new XTextField(9, "^[AST]{1}[0-9]{8}$", lblIOrigCaseNo, false);
		txtOrigCaseNo.setGridBagLayout(true);
		txtOrigCaseNo.setUpperCase(true);
		txtOrigCaseNo.setEnabled(false);
		txtOrigCaseNo.setMaxLength(9);

		lblTransferFrom = new JLabel("Transfer From");

		lblITransferFrom = CaseMethods.createErrorLabel(validationFields);

		cmbTransferFrom = new XComboBox(true, lblITransferFrom, chckbxTransferIn);
		cmbTransferFrom.setGridBagLayout(true);
		cmbTransferFrom.setEnabled(false);
		// Added nullguards as if accessed from court other than snaresbrook
		// there's no data to populate, ctx-1428
		if (GeneralDropdownPopulation.getCourts().length > 0) {
			cmbTransferFrom.setModel(new DefaultComboBoxModel(GeneralDropdownPopulation.getCourts()));
			cmbTransferFrom.setRenderer(new DropdownBoxCellRender(true));
		}
		cmbTransferFrom.addFocusListener(new MandatoryFieldsFocusListener());

		lblOriginatingBodyDecisionDate = new JLabel("Originating Body Decision Date");

		lblIOriginatingBodyDecisionDate = CaseMethods.createErrorLabel(validationFields);

		dtOriginatingBodyDecisionDate = new XDatePanel(this, nullDate, true, lblIOriginatingBodyDecisionDate,
				CaseMaintenanceConstants.BEFORE, dtDateAppealLodged, "Date", CaseMaintenanceConstants.DATE_APPEAL_LODGED);
		dtOriginatingBodyDecisionDate.setGridBagLayout(true);
		
		CaseMethods.addRemoveFromMandatoryFields(dtOriginatingBodyDecisionDate, lblOriginatingBodyDecisionDate, mandatoryFields, true);

		dtOriginatingBodyDecisionDate.getDateComponent().addMFieldListener(new MandatoryFieldsMFieldListener());
		dtOriginatingBodyDecisionDate.addFocusListener(new MandatoryFieldsFocusListener());
		dtOriginatingBodyDecisionDate.setCustomWarning("Must be on/before Date Appeal Lodged");

		lblRehearing = new JLabel("Re-Hearing?");

		this.caseX = caseX;
		jbInit();


	}
	
	/**
	 * initialise all the array's used for the dropdowns
	 */
	protected void initDropdownTypes() {
		super.initDropdownTypes();
		if (receivingSite == null) {
			receivingSite = GeneralDropdownPopulation.getReceivingSite();
		}
		// check if receivedFromValues is null, if not then populate it
		if (receivedFromValues == null) {
			receivedFromValues = GeneralDropdownPopulation.getReceivedFrom();
		}
		
	}

	public JButton getCreateButton() {
		return caseX.getCreateButton();
	}

	public JButton getCancelButton() {
		return caseX.getCancelButton();
	}

	public JButton getFinishButton() {
		return caseX.getFinishButton();
	}
	
	public class TransferDateListener implements MFieldListener {
		@Override
		public void fieldEntered(FocusEvent event) {
			//leaving blank as don't need to do anything on field entered.
		}

		@Override
		public void fieldExited(FocusEvent event) {
			// Check mandatory fields to enable create button
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);

			if (dtTransferDate.isDateValidate()) {
				// Transfer date must be equal to or before date received
				// AND equal to or after date appeal lodged
				// Work around as XDateField only allows one date comparison
				CaseMethods.compareDates(dtTransferDate, dtDateAppealLodged, CaseMaintenanceConstants.AFTER, CaseMaintenanceConstants.ON_AFTER_DATE_APPEAL);

				// If date appeal lodged condition is satisfied then test
				// other, else don't erase warning message
				if (!dtTransferDate.hasError()) {
					CaseMethods.compareDates(dtTransferDate, dtDateReceived, CaseMaintenanceConstants.BEFORE, CaseMaintenanceConstants.ON_BEFORE_DATE_RECEIVED);
				}
			}
		}
	}
	
	/**
	 * Listener for transfer checkbox that enables/disables orig case no,
	 * transfer date and transfer from depending on whether it's ticked or not.
	 */
	public class ChckbxTransferListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (chckbxTransferIn.isSelected()) { // checked
				if (!chckbxRehearing.isSelected()) {
					// enable orig case no & make mandatory
					txtOrigCaseNo.setEnabled(true);
				}

				// enable transfer date & transfer from & make mandatory
				dtTransferDate.setEnabledAndFocusable(true);
				dtTransferDate.setRequired(true);
				
				CaseMethods.addRemoveFromMandatoryFields(dtTransferDate, lblTransferDate, mandatoryFields, true);

				CaseMethods.disableEnableComboBox(cmbTransferFrom, true);
				
				CaseMethods.addRemoveFromMandatoryFields(cmbTransferFrom, lblTransferFrom, mandatoryFields, true);

			} else { // unchecked
				if (!chckbxRehearing.isSelected()) {
					// disable orig case no, clear it, make non-mandatory
					CaseMethods.disableTextField(txtOrigCaseNo);
				}

				// disable transfer date & transfer from & reset
				CaseMethods.disableEnableComboBox(cmbTransferFrom, false);
				
				CaseMethods.addRemoveFromMandatoryFields(cmbTransferFrom, lblTransferFrom, mandatoryFields, false);


				dtTransferDate.setEnabledAndFocusable(false);
				dtTransferDate.setRequired(false);
				dtTransferDate.clear();
				dtTransferDate.clearError();
				
				CaseMethods.addRemoveFromMandatoryFields(dtTransferDate, lblTransferDate, mandatoryFields, false);

				lblITransferDate.setText(" ");
			}

			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
		}
	}
	
	public class ChckbxRehearingListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (chckbxRehearing.isSelected()) {
				if (!chckbxTransferIn.isSelected()) {
					txtOrigCaseNo.setEnabled(true);
				}
			} else {
				if (!chckbxTransferIn.isSelected()) {
					CaseMethods.disableTextField(txtOrigCaseNo);
				}
			}
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
		}
	}
	public class AppealAgainstComboListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			//nothing to do on focus gained.
		}

		@Override
		public void focusLost(FocusEvent e) {
			if(cmbAppealedAgainst.getSelectedIndex()>0) {
				//if its an absolete court then show error 
				if(((RefCourtBasicValue)cmbAppealedAgainst.getSelectedItem()).getObsInd()!=null &&
	((RefCourtBasicValue)cmbAppealedAgainst.getSelectedItem()).getObsInd().equalsIgnoreCase("Y")) {
					lblIAppealedAgainst2.setText(CaseMaintenanceConstants.OBSOLETE_COURT);
					lblIAppealedAgainst.setText(CaseMaintenanceConstants.OBSOLETE_COURT);
					cmbAppealedAgainst.setError(true);
				} else {
					lblIAppealedAgainst2.setText(" ");
					lblIAppealedAgainst.setText(" ");
					cmbAppealedAgainst.setError(false);

				}
			}
		}

	}
	
	public class AppealAgainstKeyListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
			if (txtAppealedAgainst.getText() != null) {
				populateDropdown(txtAppealedAgainst.getText());
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (txtAppealedAgainst.getText() != null) {
				populateDropdown(txtAppealedAgainst.getText());
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (txtAppealedAgainst.getText() != null) {
				populateDropdown(txtAppealedAgainst.getText());
			}
		}

		void populateDropdown(String value) {
			boolean found = false;
			for (int i = 0; i < receivedFromValues.size(); i++) {
				if (!receivedFromValues.get(i).getCourtFullName().equals("Select Magistrates Court")
						&& receivedFromValues.get(i).getCourtShortName().equalsIgnoreCase(value)) {
					found = true;
					lblIAppealedAgainst2.setText(" ");
					cmbAppealedAgainst.setSelectedIndex(i);
					break;
				}
			}
			if (!found) {
				txtAppealedAgainst.setError();
			}

		}
	}
	
	public class AppealDescriptionListener implements DocumentListener {
		@Override
		public void insertUpdate(DocumentEvent e) {
			setFieldsChangedGlobal(true);
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			setFieldsChangedGlobal(true);
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			setFieldsChangedGlobal(true);
		}
	}

	public class AppealAgainstFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			//Nothing to do on focus gained.
		}

		@Override
		public void focusLost(FocusEvent e) {
			boolean found = false;
			if (txtAppealedAgainst.getText() != null && !txtAppealedAgainst.getText().equals("")) {
				for (int i = 0; i < receivedFromValues.size(); i++) {
					if (!receivedFromValues.get(i).getCourtFullName().equals("Select Magistrates Court")
							&& receivedFromValues.get(i).getCourtShortName()
									.equalsIgnoreCase(txtAppealedAgainst.getText())) {
						found = true;
						//Need to do an additional check to see if it's an obsolete court
						if(receivedFromValues.get(i).getObsInd().equals("Y")) {
							lblIAppealedAgainst2.setText(CaseMaintenanceConstants.OBSOLETE_COURT);
							cmbAppealedAgainst.setSelectedIndex(i);
							txtAppealedAgainst.setError(CaseMaintenanceConstants.OBSOLETE_COURT);
						} else {
							lblIAppealedAgainst2.setText(" ");
							lblIAppealedAgainst.setText(" ");
							cmbAppealedAgainst.setSelectedIndex(i);								
						}
						break;
					}
				}
				if (!found) {
					txtAppealedAgainst.setError();
					cmbAppealedAgainst.setSelectedIndex(0);
					lblIAppealedAgainst.setText("Invalid Entry");
				} 
			}

		}

	}

	public class DateAppealLodgedListener implements MFieldListener {
		@Override
		public void fieldEntered(FocusEvent event) {
			//leaving blank as don't need to do anything on field entered.
		}

		@Override
		public void fieldExited(FocusEvent event) {
			// Check mandatory fields to enable create button
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
			// Validate other fields that depend on this, i.e. transfer date
			if (dtDateAppealLodged.isDateValidate()) {
				dtOriginatingBodyDecisionDate.crossValidateDate();

				// Transfer date must be equal to or before date received,
				// AND equal to or after date appeal lodged
				CaseMethods.compareDates(dtTransferDate, dtDateAppealLodged, CaseMaintenanceConstants.AFTER, CaseMaintenanceConstants.ON_AFTER_DATE_APPEAL);


				// If date appeal lodged condition is satisfied then test
				// other, else don't erase warning message
				if (!dtTransferDate.hasError()) {
					CaseMethods.compareDates(dtTransferDate, dtDateReceived, CaseMaintenanceConstants.BEFORE, CaseMaintenanceConstants.ON_BEFORE_DATE_RECEIVED);
				}
			}
		}
	}
	public class DateReceivedListener implements MFieldListener {
		@Override
		public void fieldEntered(FocusEvent event) {
			//leaving blank as we don't want to do anything on field entered
		}

		@Override
		public void fieldExited(FocusEvent event) {
			// Check mandatory fields to enable create button
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
			// Validate other fields that depend on this, i.e date appeal
			// lodged & transfer date
			if (dtDateReceived.isDateValidate()) {
				dtDateAppealLodged.crossValidateDate();

				// Transfer date must be equal to or before date received
				// AND equal to or after date appeal lodged
				CaseMethods.compareDates(dtTransferDate,dtDateReceived, CaseMaintenanceConstants.BEFORE, CaseMaintenanceConstants.ON_BEFORE_DATE_RECEIVED);


				// If date received condition is satisfied then test
				// other, else don't erase warning message
				if (!dtTransferDate.hasError()) {
					CaseMethods.compareDates(dtTransferDate,dtDateAppealLodged, CaseMaintenanceConstants.AFTER, CaseMaintenanceConstants.ON_AFTER_DATE_APPEAL);
				}
			}
		}
	}
	
	public void validateMandatoryFields() {
		CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
	}

	// used by CaseXPanel to store the values in the DB.
	public String getCaseTitle() {
		return txtCaseTitle.getText();
	}

	public Timestamp getDateReceived() {
		Calendar c = null;
		if (dtDateReceived.isDateValidate()) {
			try {
				c = dtDateReceived.getDate();
			} catch (CSValidationException e) {
				log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e);
			}
			if (c == null) {
				return null;
			} else {
				return new Timestamp(c.getTimeInMillis());
			}
			// At this point we don't want to throw an error because we are
			// displaying the 'invalid date'
			// message already on the screen so below is there for completeness
		} else {
			log.error("Date received is incorrect");
			return null;
		}
	}

	public String getTransferIn() {
		if (chckbxTransferIn.isSelected())
			return "Y";
		else
			return "N";
	}

	public String getOrigCaseNumber() {
		if (txtOrigCaseNo.isEnabled()) {
			return txtOrigCaseNo.getText();
		} else {
			return null;
		}
	}

	public String getJps1() {
		return txtOriginalJps1.getText();
	}

	public String getJps2() {
		return txtOriginalJps2.getText();
	}

	public String getJps3() {
		return txtOriginalJps3.getText();
	}

	public String getJps4() {
		return txtOriginalJps4.getText();
	}

	public Timestamp getDateAppealLodged() {
		if (dtDateAppealLodged.isDateValidate()) {
			Calendar c = null;
			try {
				c = dtDateAppealLodged.getDate();
			} catch (CSValidationException e) {
				log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e);
			}
			if (c == null) {
				return null;
			} else {
				return new Timestamp(c.getTimeInMillis());
			}
		} else {
			log.error("Date appeal lodged is incorrect");
		}

		return null;
	}

	public XDatePanel getDateAppealLodgedPanel() {
		return dtDateAppealLodged;
	}

	public String getReHearing() {
		if (chckbxRehearing.isSelected()) {
			return "Y";
		} else {
			return "N";
		}
	}

	public String getAppealDescription() {
		return txtAreaAppealDescription.getText();
	}

	public Timestamp getDateOriginatingBodyDecision() {
		Calendar c = null;
		if (dtOriginatingBodyDecisionDate.isDateValidate()) {
			try {
				c = dtOriginatingBodyDecisionDate.getDate();
			} catch (CSValidationException e) {
				log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e);
			}
			if (c == null) {
				return null;
			} else {
				return new Timestamp(c.getTimeInMillis());
			}
			// At this point we don't want to throw an error because we are
			// displaying the 'invalid date'
			// message already on the screen so below is there for completeness
		} else {
			log.error("Originating body decision is incorrect");
			return null;
		}
	}

	public Integer getAppealedAgainst() {
		return ((RefCourtBasicValue) cmbAppealedAgainst.getSelectedItem()).getId();

	}

	public Integer getReceivingSite() {
		return ((CourtSiteBasicValue) cmbReceivingSite.getSelectedItem()).getId();
	}

	public String getOrigCaseNo() {
		if (txtOrigCaseNo.isEnabled()) {
			return txtOrigCaseNo.getText();
		} else {
			return null;
		}
	}

	public Timestamp getTransferDate() {
		if (dtTransferDate.isEnabled() && dtTransferDate.isDateValidate()) {
			Calendar c = null;
			try {
				c = dtTransferDate.getDate();
			} catch (CSValidationException e) {
				log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e);
			}
			if (c == null) {
				return null;
			} else {
				return new Timestamp(c.getTimeInMillis());
			}
		} else if (!dtTransferDate.isDateValidate()) {
			log.error("Transfer date is incorrect");
		}

		return null;
	}

	public Integer getTransferFromCourtID() {
		if (cmbTransferFrom.isEnabled()) {
			return ((RefCourtBasicValue) cmbTransferFrom.getSelectedItem()).getId();
		} else {
			return null;
		}
	}

	/**
	 * Used in the case creation process
	 * 
	 * @param caseNumber
	 * @return
	 */
	public CaseBasicValue populateCaseBasicValue() {
		CaseBasicValue basicValue = new CaseBasicValue();
		populateCaseBasicValue(basicValue);
		basicValue.setNoDefendantsForCase(1);
		return basicValue;
	}

	/**
	 * Used in the case update process
	 * 
	 * @param caseNumber
	 * @return
	 */
	public void populateCaseBasicValue(CaseBasicValue basicValue) {
		basicValue.setCaseType(String.valueOf("A"));
		basicValue.setVersion(version);
		basicValue.setCaseTitle(getCaseTitle());
		basicValue.setReceivedDate(getDateReceived());
		basicValue.setOriginalCaseNumber(getOrigCaseNumber());
		basicValue.setOriginalJps1(getJps1());
		basicValue.setOriginalJps2(getJps2());
		basicValue.setOriginalJps3(getJps3());
		basicValue.setOriginalJps4(getJps4());
		basicValue.setAppealLodgedDate(getDateAppealLodged());
		basicValue.setCourtIdReceivingSite(getReceivingSite());
		basicValue.setRetrial(getReHearing());
		basicValue.setCaseDescription(getAppealDescription());
		basicValue.setOrigBodyDecisionDate(getDateOriginatingBodyDecision());
		basicValue.setRefCourtID(getAppealedAgainst());
		basicValue.setCourtID(XhibitSingleton.getInstance().getCourtId());
		basicValue.setChargeImportIndicator("O");
		basicValue.setCaseSubType("O");
		// CTX-1255 additions
		basicValue.setDateTransFrom(getTransferDate());
		basicValue.setCccTransFromRefCourtId(getTransferFromCourtID());
		basicValue.setOriginalCaseNumber(getOrigCaseNo());
		basicValue.setTransferredCase(getTransferIn());
		basicValue.setVideoLinkRequired("N");
		basicValue.setCivilUnrest(getCivilUnrest());
	}

	public List<JLabel> getValidationFields() {
		return validationFields;
	}

	public XTextField getTxtCaseTitle() {
		return txtCaseTitle;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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
		log.debug("GeneralMiscAppeal.populate() using caseBasicValue");
		version = caseBasicValue.getVersion();
		lblCaseNumber.setText(caseBasicValue.getCaseType() + caseBasicValue.getCaseNumber().toString());

		if (caseBasicValue.getCaseTitle() != null) {
			txtCaseTitle.setText(caseBasicValue.getCaseTitle());
		}

		if (caseBasicValue.getReceivedDate() != null) {
			dtDateReceived.setDate(caseBasicValue.getReceivedDate());
		}
		if (caseBasicValue.getAppealLodgedDate() != null) {
			dtDateAppealLodged.setDate(caseBasicValue.getAppealLodgedDate());
		}

		populateReceivingSite(caseBasicValue);
		
		if (caseBasicValue.getCaseDescription() != null) {
			txtAreaAppealDescription.setText(caseBasicValue.getCaseDescription());
		}

		if (caseBasicValue.getRetrial() != null && caseBasicValue.getRetrial().equalsIgnoreCase("Y")) {
			chckbxRehearing.setSelected(true);
		} 

		if (caseBasicValue.getOrigBodyDecisionDate() != null) {
			dtOriginatingBodyDecisionDate.setDate(caseBasicValue.getOrigBodyDecisionDate());
		}

		populateAppealAgainst(caseBasicValue);

		if (caseBasicValue.getOriginalJps1() != null) {
			txtOriginalJps1.setText(caseBasicValue.getOriginalJps1());
		}
		if (caseBasicValue.getOriginalJps2() != null) {
			txtOriginalJps2.setText(caseBasicValue.getOriginalJps2());
		}
		if (caseBasicValue.getOriginalJps3() != null) {
			txtOriginalJps3.setText(caseBasicValue.getOriginalJps3());
		}
		if (caseBasicValue.getOriginalJps4() != null) {
			txtOriginalJps4.setText(caseBasicValue.getOriginalJps4());
		}
		// CTX-1255 additions
		if (caseBasicValue.getTransferredCase() != null && caseBasicValue.getTransferredCase().equals("Y")) {
			chckbxTransferIn.setSelected(true);
		}

		populateCCTransFrom(caseBasicValue);

		if (caseBasicValue.getOriginalCaseNumber() != null) {
			txtOrigCaseNo.setText(caseBasicValue.getOriginalCaseNumber());
		}

		if (caseBasicValue.getDateTransFrom() != null) {
			dtTransferDate.setDate(caseBasicValue.getDateTransFrom());
		}
		
		populateCivilUnrest(caseBasicValue);
	}

	/**
	 * Populate receiving site combo box.
	 * @param caseBasicValue
	 */
	private void populateReceivingSite(CaseBasicValue caseBasicValue) {
		if (caseBasicValue.getCourtIdReceivingSite() != null) {
			for (int i = 0; i < receivingSite.size(); i++) {
				if (receivingSite.get(i).getId().equals(caseBasicValue.getCourtIdReceivingSite())) {
					cmbReceivingSite.setSelectedItem(receivingSite.get(i));
				}
			}
		}
		
	}

	/**
	 * Populate the appeal against combo box.
	 * @param caseBasicValue
	 */
	private void populateAppealAgainst(CaseBasicValue caseBasicValue) {
		// start at 1 as first value will be null (select mag court), this
		// will also set the text box as well as dropdown
		if (caseBasicValue.getRefCourtID() != null) {
			for (int i = 1; i < receivedFromValues.size(); i++) {
				if (receivedFromValues.get(i).getId().equals(caseBasicValue.getRefCourtID())) {
					cmbAppealedAgainst.setSelectedItem(receivedFromValues.get(i));
				}
			}
		}
	}

	/**
	 * Populate transfer from combo box.
	 * @param caseBasicValue
	 */
	private void populateCCTransFrom(CaseBasicValue caseBasicValue) {
		if (caseBasicValue.getCccTransFromRefCourtId() != null) {
			RefCourtBasicValue[] courtList = GeneralDropdownPopulation.getCourts();

			for (int i = 1; i < courtList.length; i++) {
				if (courtList[i].getId().equals(caseBasicValue.getCccTransFromRefCourtId())) {
					cmbTransferFrom.setSelectedIndex(i);
					break;
				}
			}
		}
		
	}

	// Initialise grid bag layout
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = CaseMethods.getDefaultGridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.BOTH;
		this.setPreferredSize(new Dimension(1000, 600));

		// top, left, bottom, right
		gbc.insets = new Insets(5, 10, 5, 10); // keep gap between panels

		gbc.weighty = 0.4;
		gbc.gridheight = 2;
		gbc.weightx = 0.8;
		this.add(getCaseDetailsPanel(), gbc);

		gbc.gridx++;
		gbc.gridheight = 1;
		gbc.weightx = 0.2;
		this.add(getTransferDetailsPanel(), gbc);

		gbc.weighty = 0.4;
		gbc.gridx = 0;
		gbc.gridy += 2;
		gbc.weightx = 1;
		gbc.gridwidth = 2;
		this.add(getOriginalJPsPanel(), gbc);

	}

	private JPanel getCaseDetailsPanel() {
		if (caseDetailsPanel == null) {
			caseDetailsPanel = new JPanel();
			caseDetailsPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = CaseMethods.getDefaultGridBagConstraints();
			gbc.anchor = GridBagConstraints.NORTHWEST;

			// First column (labels)
			gbc.weighty = 0.2;
			gbc.weightx = 0.05;
			caseDetailsPanel.add(lblCaseNumber, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblCaseTitle, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblDateReceived, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblDateAppealLodged, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblReceivingSite, gbc);

			gbc.gridy += 2;
			gbc.weighty = 0.8;
			caseDetailsPanel.add(lblAppealDescription, gbc);

			gbc.gridy = 2;
			gbc.gridx++;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 0.05;
			gbc.weighty = 0.2;
			gbc.gridwidth = 3;
			caseDetailsPanel.add(txtCaseTitle, gbc);

			gbc.gridwidth = 1;
			gbc.weightx = 0.9;
			gbc.gridx++;
			caseDetailsPanel.add(Box.createRigidArea(txtCaseTitle.getPreferredSize()), gbc);

			gbc.weightx = 0.05;
			gbc.gridx--;
			gbc.gridy += 2;
			caseDetailsPanel.add(dtDateReceived, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(dtDateAppealLodged, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(cmbReceivingSite, gbc);

			gbc.gridy += 2;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridheight = 1;
			gbc.gridwidth = 3;
			caseDetailsPanel.add(txtAreaAppealDescription, gbc);

			gbc.gridx--;
			gbc.gridy++;
			gbc.gridwidth = 4;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weighty = 0.4;
			caseDetailsPanel.add(getMagistratesCourtDetailsPanel(), gbc);

			gbc.gridx--;
			gbc.gridy += 2;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridheight = 3;
			gbc.gridwidth = 2;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			caseDetailsPanel.add(getCivilUnrestPanel(caseX), gbc);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			
			gbc.gridx++;
			gbc.gridy = 1;
			gbc.weighty = 0.1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.gridwidth = 2;
			caseDetailsPanel.add(lblICaseTitle, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblIDateReceived, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblIDateAppealLodged, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblIReceivingSite, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblInvalidAppealDescription, gbc);

			CaseMethods.addChangeListeners(caseDetailsPanel.getComponents(), caseX);
		}
		return caseDetailsPanel;
	}

	private JPanel getMagistratesCourtDetailsPanel() {
		if (magistratesCourtDetailsPanel == null) {
			magistratesCourtDetailsPanel = new JPanel();
			magistratesCourtDetailsPanel.setLayout(new GridBagLayout());
			magistratesCourtDetailsPanel.setBorder(BorderFactory.createTitledBorder("Magistrates Court Details"));
			GridBagConstraints gbc = CaseMethods.getDefaultGridBagConstraints();

			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridy++;
			gbc.weightx = 0.05;
			magistratesCourtDetailsPanel.add(lblAppealedAgainst, gbc);

			gbc.gridy = 1;
			gbc.gridx++;
			gbc.insets = new Insets(4, 4, 4, 25);
			gbc.weightx = 0.2;

			gbc.fill = GridBagConstraints.HORIZONTAL;
			txtAppealedAgainst.setColumns(5);
			txtAppealedAgainst.setMinimumSize(txtAppealedAgainst.getPreferredSize());
			txtAppealedAgainst.setMaxLength(5);
			txtAppealedAgainst.setUpperCase(true);
			magistratesCourtDetailsPanel.add(txtAppealedAgainst, gbc);

			gbc.gridx++;
			gbc.weightx = 0.75;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			magistratesCourtDetailsPanel.add(cmbAppealedAgainst, gbc);

			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.weighty = 0.1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.gridwidth = 2;
			magistratesCourtDetailsPanel.add(lblIAppealedAgainst, gbc);

			gbc.gridx++;
			gbc.weightx = 0.75;
			magistratesCourtDetailsPanel.add(lblIAppealedAgainst2, gbc);

			CaseMethods.addChangeListeners(magistratesCourtDetailsPanel.getComponents(), caseX);
		}
		return magistratesCourtDetailsPanel;
	}

	private JPanel getTransferDetailsPanel() {
		if (transferDetailsPanel == null) {
			transferDetailsPanel = new JPanel();
			transferDetailsPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = CaseMethods.getDefaultGridBagConstraints();

			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.weighty = 0.2;
			gbc.weightx = 0.1;
			// for correct alignment between R & LHS
			transferDetailsPanel.add(Box.createRigidArea(lblCaseNumber.getPreferredSize()), gbc);

			gbc.gridy++;
			gbc.weighty = 0.1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			transferDetailsPanel.add(Box.createRigidArea(lblICaseTitle.getPreferredSize()), gbc);

			gbc.weighty = 0.2;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.gridy++;
			transferDetailsPanel.add(lblRehearing, gbc);

			gbc.gridx++;
			gbc.weightx = 0.4;
			transferDetailsPanel.add(chckbxRehearing, gbc);

			gbc.gridx++;
			gbc.weightx = 0.4;
			transferDetailsPanel.add(new JLabel(" "), gbc);

			gbc.gridx++;
			transferDetailsPanel.add(new JLabel(" "), gbc);

			gbc.gridy++;
			gbc.gridx = 0;

			Insets newInsets = new Insets(4, 0, 0, -21);
			gbc.insets = newInsets;
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.NORTHEAST;
			transferDetailsPanel.add(chckbxTransferIn, gbc);

			gbc.gridwidth = 5;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			JPanel transferDetailsPanelInner = new JPanel();
			transferDetailsPanelInner.setLayout(new GridBagLayout());
			transferDetailsPanelInner.setBorder(BorderFactory.createTitledBorder("Transfer In"));

			GridBagConstraints gbcInner = CaseMethods.getDefaultGridBagConstraints();
			gbcInner.weighty = 0.2;
			gbcInner.fill = GridBagConstraints.HORIZONTAL;
			gbcInner.gridy += 2;
			transferDetailsPanelInner.add(lblTransferDate, gbcInner);

			gbcInner.gridy += 2;
			transferDetailsPanelInner.add(lblTransferFrom, gbcInner);

			gbcInner.gridx++;
			gbcInner.gridy = 1;
			gbcInner.insets = XHIBITConstant.errorLabelInsets;
			gbcInner.weighty = 0.1;
			gbcInner.gridwidth = 2;
			transferDetailsPanelInner.add(lblITransferDate, gbcInner);

			gbcInner.gridy += 2;
			gbcInner.gridwidth = 1;
			transferDetailsPanelInner.add(lblITransferFrom, gbcInner);

			gbcInner.insets = XHIBITConstant.nonContainerInsets;
			gbcInner.weighty = 0.2;
			gbcInner.gridy = 2;
			dtTransferDate.setEnabled(false);
			transferDetailsPanelInner.add(dtTransferDate, gbcInner);

			gbcInner.gridy += 2;
			gbcInner.gridwidth = 2;
			cmbTransferFrom.setEnabled(false);
			transferDetailsPanelInner.add(cmbTransferFrom, gbcInner);

			transferDetailsPanel.add(transferDetailsPanelInner, gbc);

			gbc.gridwidth = 2;
			gbc.gridy += 2;
			transferDetailsPanel.add(lblOrigCaseNo, gbc);

			gbc.gridx++;
			txtOrigCaseNo.setColumns(10);
			txtOrigCaseNo.setMinimumSize(txtOrigCaseNo.getPreferredSize());
			txtOrigCaseNo.setMaxLength(9);
			txtOrigCaseNo.setEnabled(false);
			txtOrigCaseNo.setUpperCase(true);
			transferDetailsPanel.add(txtOrigCaseNo, gbc);

			gbc.gridy--;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weighty = 0.1;
			transferDetailsPanel.add(lblIOrigCaseNo, gbc);

			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.gridx--;
			gbc.gridy += 3;
			transferDetailsPanel.add(lblOriginatingBodyDecisionDate, gbc);

			gbc.gridy--;
			gbc.gridx += 2;
			gbc.gridwidth = 3;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			transferDetailsPanel.add(lblIOriginatingBodyDecisionDate, gbc);

			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.gridx += 3;
			gbc.gridwidth = 1;
			transferDetailsPanel.add(Box.createRigidArea(txtCaseTitle.getPreferredSize()));

			gbc.gridx -= 3;
			gbc.gridy++;
			transferDetailsPanel.add(dtOriginatingBodyDecisionDate, gbc);

			CaseMethods.addChangeListeners(transferDetailsPanelInner.getComponents(), caseX);
			CaseMethods.addChangeListeners(transferDetailsPanel.getComponents(), caseX);
		}

		return transferDetailsPanel;
	}

	private JPanel getOriginalJPsPanel() {
		if (originalJPsPanel == null) {
			originalJPsPanel = new JPanel();
			originalJPsPanel.setLayout(new GridBagLayout());
			originalJPsPanel.setBorder(BorderFactory.createTitledBorder("Original JPs"));
			GridBagConstraints gbc = CaseMethods.getDefaultGridBagConstraints();

			gbc.gridy++;
			gbc.weightx = 0.02;
			originalJPsPanel.add(lbl1, gbc);

			gbc.gridy += 2;
			originalJPsPanel.add(lbl2, gbc);

			gbc.gridy = 1;
			gbc.gridx++;
			gbc.weightx = 0.48;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			txtOriginalJps1.setColumns(10);
			txtOriginalJps1.setMaxLength(35);
			txtOriginalJps1.setMinimumSize(txtOriginalJps1.getPreferredSize());
			originalJPsPanel.add(txtOriginalJps1, gbc);

			gbc.gridy += 2;
			txtOriginalJps2.setColumns(10);
			txtOriginalJps2.setMinimumSize(txtOriginalJps2.getPreferredSize());
			originalJPsPanel.add(txtOriginalJps2, gbc);

			gbc.gridy = 1;
			gbc.gridx++;
			gbc.weightx = 0.02;
			gbc.fill = GridBagConstraints.NONE;
			originalJPsPanel.add(lbl3, gbc);

			gbc.gridy += 2;
			originalJPsPanel.add(lbl4, gbc);

			gbc.gridy = 1;
			gbc.gridx++;
			gbc.weightx = 0.48;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			txtOriginalJps3.setColumns(10);
			txtOriginalJps3.setMinimumSize(txtOriginalJps3.getPreferredSize());
			originalJPsPanel.add(txtOriginalJps3, gbc);

			gbc.gridy += 2;
			txtOriginalJps4.setColumns(10);
			txtOriginalJps4.setMinimumSize(txtOriginalJps4.getPreferredSize());
			originalJPsPanel.add(txtOriginalJps4, gbc);

			gbc.gridy = 0;
			gbc.gridx = 1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			originalJPsPanel.add(lblIOriginalJP1, gbc);

			gbc.gridx += 2;
			originalJPsPanel.add(lblIOriginalJP3, gbc);

			gbc.gridy += 2;
			gbc.gridx = 1;
			originalJPsPanel.add(lblIOriginalJP2, gbc);

			gbc.gridx += 2;
			originalJPsPanel.add(lblIOriginalJP4, gbc);

			originalJPsPanel.setMinimumSize(new Dimension(1000, 150)); // to
																		// override
																		// issue
																		// with
																		// resizing
																		// minimizing
																		// only
																		// this

			CaseMethods.addChangeListeners(originalJPsPanel.getComponents(), caseX);
		}
		return originalJPsPanel;
	}

	public void setCaseNumber(String caseNumber) {
		if (lblCaseNumber != null)
			lblCaseNumber.setText(caseNumber);
	}

	

	public void clearChangedState() {
		fieldsChangedGlobal = false;
	}
	
	/**
	 * The traversal policy used on sentence tab, called when sentence tab is
	 * displayed (from CaseXPanel).
	 * 
	 * @return FocusTraversalOnArray - all components used in traversal.
	 */
	public FocusTraversalOnArray getTabbedPaneOrder() {

		return new FocusTraversalOnArray(new Component[] { txtCaseTitle, dtDateReceived, dtDateAppealLodged,
				cmbReceivingSite, txtAreaAppealDescription, 
				txtAppealedAgainst, cmbAppealedAgainst, cmbCivilUnrest, 
				chckbxRehearing, chckbxTransferIn, dtTransferDate, cmbTransferFrom, txtOrigCaseNo,
				dtOriginatingBodyDecisionDate, 
				txtOriginalJps1, txtOriginalJps2, txtOriginalJps3, txtOriginalJps4, getCreateButton(),
				getFinishButton(), getCancelButton() });
	}
	
	/**
	 * Return mandatory fields.
	 * @return all mandatory fields
	 */
	public List<Object> getMandatoryFields() {
		return mandatoryFields;
	}
	
	/**
	 * Return whether any global fields have been changed.
	 * @return true if they have
	 */
	public boolean getFieldsChangedGlobal() {
		return fieldsChangedGlobal;
	}
	
	/**
	 * Set fields changed local.
	 * @param fieldsChangedGlobal
	 */
	public void setFieldsChangedGlobal(boolean fieldsChangedGlobal) {
		this.fieldsChangedGlobal = fieldsChangedGlobal;
		if (caseX.getCreateButton().getText().equals("Save")) {
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
		}
	}
}