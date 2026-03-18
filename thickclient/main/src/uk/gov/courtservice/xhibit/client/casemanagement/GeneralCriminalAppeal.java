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

import org.apache.log4j.Logger;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import mseries.Calendar.MFieldListener;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefMonitoringCategoryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
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
 * @author C.Kudzin - Feb 27, 2018 - Implemented further date validation and
 *         comparison against other dates - CTX-1395
 *
 */
public class GeneralCriminalAppeal extends GeneralAllCasesTab {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private Integer version;

	Calendar nullDate = null;

	/**
	 * Used for drop down population.
	 */

	private ArrayList<RefCourtBasicValue> receivedFromValues;
	private ArrayList<CourtSiteBasicValue> receivingSite;
	private ArrayList<RefMonitoringCategoryBasicValue> monitoringCategory;
	private ArrayList<RefSystemCodeBasicValue> appealType;
	private ArrayList<RefSystemCodeBasicValue> hearingType;

	// xcheckboxes
	private XCheckBox chckbxRehearing;
	private XCheckBox chckbxTransferIn;

	// xtextfields
	private XTextField txtOrigCaseNo;
	private XTextField txtReceivedFrom;
	private XTextField txtCaseTitle;
	private XTextField txtOriginalJps1;
	private XTextField txtOriginalJps2;
	private XTextField txtOriginalJps3;
	private XTextField txtOriginalJps4;

	// error labels
	private JLabel lblICaseTitle;
	private JLabel lblIDateReceived;
	private JLabel lblIAppealType;
	private JLabel lblIDateAppealLodged;
	private JLabel lblIReceivingSite;
	private JLabel lblIOrigCaseNo;
	private JLabel lblIMonitoringCategory;
	private JLabel lblIReceivedFrom;
	private JLabel lblIReceivedFrom2;
	private JLabel lblIHearingType;
	private JLabel lblIJP1;
	private JLabel lblIJP2;
	private JLabel lblIJP3;
	private JLabel lblIJP4;
	private JLabel lblCaseNumber;

	// xdatepanel
	private XDatePanel dtDateAppealLodged;
	private XDatePanel dtDateReceived;
	private XDatePanel dtTransferDate;

	// xcomboboxes
	private XComboBox cmbReceivedFrom;
	private XComboBox cmbAppealType;
	private XComboBox cmbReceivingSite;
	private XComboBox cmbMonitoringCategory;
	private XComboBox cmbHearingType;
	private XComboBox cmbTransferFrom;

	// Moved to global to be able to access with jbInit
	private JLabel lblAppealOf;
	private JLabel lblDateReceived;
	private JLabel lblAppealType;
	private JLabel lblDateAppealLodged;
	private JLabel lblReceivingSite;
	private JLabel lblITransferDate;
	private JLabel lblTransferDate;
	private JLabel lblOrigCaseNo;
	private JLabel lblITransferFrom;
	private JLabel lblTransferFrom;
	private JLabel lblMonitoringCategory;
	private JLabel lblReceivedFrom;
	private JPanel originalJPsPanel;
	private JLabel lbl1;
	private JLabel lbl2;
	private JLabel lbl3;
	private JLabel lbl4;
	private JLabel lblRehearing;
	private JLabel lblCaseTitle;
	private JLabel lblHearingType;

	private JPanel caseDetailsPanel;
	private JPanel transferDetailsPanel;
	private JPanel magistratesCourtDetailsPanel;

	// parent panel
	private CaseXPanel caseX;

	// array of all the invalid entry fields
	private List<JLabel> validationFields = new ArrayList<JLabel>();
	private List<Object> mandatoryFields = new ArrayList<Object>();

	private boolean fieldsChangedGlobal = false;

	/**
	 * Used for logging and exception handling.
	 */
	private static final String CLASS_NAME = ".GeneralCriminalAppeal";
	
	
	public GeneralCriminalAppeal(final CaseXPanel caseX) {
		
		initDropdownTypes();
		initCivilUnrestPanel(validationFields);

		lblCaseNumber = new JLabel("Case Number");
		lblCaseNumber.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCaseNumber.setBorder(new LineBorder(new Color(0, 0, 0)));

		lblAppealOf = new JLabel("Appeal of");
		lblAppealOf.setHorizontalAlignment(SwingConstants.RIGHT);

		lblICaseTitle = CaseMethods.createErrorLabel(validationFields);

		txtCaseTitle = new XTextField(72, "^.{1,72}$", lblICaseTitle, true);
		txtCaseTitle.setDisabledTextColor(Color.BLACK);
		txtCaseTitle.setGridBagLayout(true);
		txtCaseTitle.setUpperCase(true);
		txtCaseTitle.setMaxLength(72);
		txtCaseTitle.addFocusListener(new MandatoryFieldsFocusListener());
		txtCaseTitle.setColumns(10);
		txtCaseTitle.setMinimumSize(txtCaseTitle.getPreferredSize());
		lblCaseTitle = new JLabel("Case Title");


		CaseMethods.addRemoveFromMandatoryFields(txtCaseTitle, lblCaseTitle, mandatoryFields, true);

		lblDateReceived = new JLabel(CaseMaintenanceConstants.DATE_RECEIVED);

		lblIDateReceived = CaseMethods.createErrorLabel(validationFields);

		dtDateReceived = new XDatePanel(this, Calendar.getInstance(), true, lblIDateReceived, CaseMaintenanceConstants.BEFORE);
		dtDateReceived.setGridBagLayout(true);

		CaseMethods.addRemoveFromMandatoryFields(dtDateReceived, lblDateReceived, mandatoryFields, true);
		
		dtDateReceived.getDateComponent().addMFieldListener(new DateReceivedListener());

		lblIAppealType = CaseMethods.createErrorLabel(validationFields);

		lblAppealType = new JLabel("Appeal Type");

		cmbAppealType = new XComboBox(true, lblIAppealType);
		cmbAppealType.setGridBagLayout(true);
		// Added nullguards as if accessed from court other than snaresbrook
		// there's no data to populate, ctx-1428
		if (!appealType.isEmpty()) {
			cmbAppealType.setModel(new DefaultComboBoxModel(appealType.toArray()));
			cmbAppealType.setRenderer(new DropdownBoxCellRender());
		}
		cmbAppealType.addFocusListener(new MandatoryFieldsFocusListener());
		CaseMethods.addRemoveFromMandatoryFields(cmbAppealType, lblAppealType, mandatoryFields, true);
		
		lblDateAppealLodged = new JLabel("Date Appeal Lodged");

		lblIDateAppealLodged = CaseMethods.createErrorLabel(validationFields);

		dtDateAppealLodged = new XDatePanel(this, (Calendar) null, true, lblIDateAppealLodged, CaseMaintenanceConstants.BEFORE, dtDateReceived,
				"Date Appeal Lodged", CaseMaintenanceConstants.DATE_RECEIVED);
		
		CaseMethods.addRemoveFromMandatoryFields(dtDateAppealLodged, lblDateAppealLodged, mandatoryFields, true);

		dtDateAppealLodged.setGridBagLayout(true);
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

		chckbxRehearing = new XCheckBox();
		chckbxRehearing.addItemListener(new ChcbxItemListener());
		
		chckbxRehearing.setHorizontalTextPosition(SwingConstants.LEFT);

		chckbxTransferIn = new XCheckBox();
		chckbxTransferIn.addItemListener(new TransferInItemListener());
		chckbxTransferIn.setEnabled(true);
		chckbxTransferIn.setHorizontalTextPosition(SwingConstants.LEFT);

		lblITransferDate = CaseMethods.createErrorLabel(validationFields);

		lblTransferDate = new JLabel("Transfer Date");

		dtTransferDate = new XDatePanel(this, nullDate, false, lblITransferDate, CaseMaintenanceConstants.AFTER, dtDateAppealLodged, "Date",
				CaseMaintenanceConstants.DATE_RECEIVED);
		dtTransferDate.setGridBagLayout(true);
		dtTransferDate.setDateEnabled(false);
		dtTransferDate.setEnabledAndFocusable(false);
		dtTransferDate.addFocusListener(new MandatoryFieldsFocusListener());
		dtTransferDate.setCustomWarning(CaseMaintenanceConstants.ON_AFTER_DATE_APPEAL);
		dtTransferDate.getDateComponent().addMFieldListener(new TransferDateListener());

		lblOrigCaseNo = new JLabel("Orig Case No.");

		lblIOrigCaseNo = CaseMethods.createErrorLabel(validationFields);

		txtOrigCaseNo = new XTextField(9, "^[AST]{1}[0-9]{8}$", lblIOrigCaseNo, false);
		txtOrigCaseNo.setGridBagLayout(true);
		txtOrigCaseNo.setUpperCase(true);
		txtOrigCaseNo.setMaxLength(9);
		txtOrigCaseNo.setEnabled(false);
		txtOrigCaseNo.setColumns(10);
		txtOrigCaseNo.setMinimumSize(txtOrigCaseNo.getPreferredSize());

		lblITransferFrom = CaseMethods.createErrorLabel(validationFields);

		lblTransferFrom = new JLabel("Transfer From");

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

		lblMonitoringCategory = new JLabel("Monitoring Category");

		lblIMonitoringCategory = CaseMethods.createErrorLabel(validationFields);

		cmbMonitoringCategory = new XComboBox(true, lblIMonitoringCategory);
		cmbMonitoringCategory.setGridBagLayout(true);
		// Added nullguards as if accessed from court other than snaresbrook
		// there's no data to populate, ctx-1428
		if (!monitoringCategory.isEmpty()) {
			cmbMonitoringCategory.setModel(new DefaultComboBoxModel(monitoringCategory.toArray()));
			cmbMonitoringCategory.setRenderer(new DropdownBoxCellRender());
		}
		
		CaseMethods.addRemoveFromMandatoryFields(cmbMonitoringCategory, lblMonitoringCategory, mandatoryFields, true);
		
		cmbMonitoringCategory.addFocusListener(new MandatoryFieldsFocusListener());

		lblReceivedFrom = new JLabel("Received From");

		lblIReceivedFrom = CaseMethods.createErrorLabel(validationFields);

		txtReceivedFrom = new XTextField(5, "^[a-zA-Z0-9]{1,5}$", lblIReceivedFrom, true);
		txtReceivedFrom.setGridBagLayout(true);
		txtReceivedFrom.setMaxLength(5);
		txtReceivedFrom.setUpperCase(true);
		txtReceivedFrom.setMinimumSize(txtReceivedFrom.getPreferredSize());

		// add a on key press
		txtReceivedFrom.addKeyListener(new TxtReceievedFromKeyListener());
		txtReceivedFrom.addFocusListener(new TxtReceivedFromFocusListener());

		txtReceivedFrom.setColumns(10);
		
		CaseMethods.addRemoveFromMandatoryFields(txtReceivedFrom, lblReceivedFrom, mandatoryFields, true);

		txtReceivedFrom.addFocusListener(new MandatoryFieldsFocusListener());

		lblIReceivedFrom2 = new JLabel(" ");
		lblIReceivedFrom2.setForeground(Color.RED);

		cmbReceivedFrom = new XComboBox(true, lblIReceivedFrom2, txtReceivedFrom);
		cmbReceivedFrom.setGridBagLayout(true);
		// Added nullguards as if accessed from court other than snaresbrook
		// there's no data to populate, ctx-1428
		if (!receivedFromValues.isEmpty()) {
			cmbReceivedFrom.setModel(new DefaultComboBoxModel(receivedFromValues.toArray()));
			cmbReceivedFrom.setRenderer(new DropdownBoxCellRender());
		}
		cmbReceivedFrom.addFocusListener(new MandatoryFieldsFocusListener());
		cmbReceivedFrom.setMandatory(true);
		CaseMethods.addRemoveFromMandatoryFields(cmbReceivedFrom, null, mandatoryFields, true);

		cmbReceivedFrom.addFocusListener(new ReceivedFromFocusListener());

		lblHearingType = new JLabel("Hearing Type");

		lblIHearingType = new JLabel(" ");
		lblIHearingType.setForeground(Color.RED);

		cmbHearingType = new XComboBox(true, lblIHearingType);
		cmbHearingType.setGridBagLayout(true);
		// Added nullguards as if accessed from court other than snaresbrook
		// there's no data to populate, ctx-1428
		if (!hearingType.isEmpty()) {
			cmbHearingType.setModel(new DefaultComboBoxModel(hearingType.toArray()));
			cmbHearingType.setRenderer(new DropdownBoxCellRender());
		}
		
		CaseMethods.addRemoveFromMandatoryFields(cmbHearingType, lblHearingType, mandatoryFields, true);

		cmbHearingType.addFocusListener(new MandatoryFieldsFocusListener());

		lbl1 = new JLabel("1.");

		lbl2 = new JLabel("2.");

		lbl3 = new JLabel("3.");

		lbl4 = new JLabel("4.");

		lblIJP1 = CaseMethods.createErrorLabel(validationFields);
		
		lblIJP3 = CaseMethods.createErrorLabel(validationFields);

		lblIJP2 = CaseMethods.createErrorLabel(validationFields);

		lblIJP4 = CaseMethods.createErrorLabel(validationFields);

		txtOriginalJps1 = new XTextField(255, CaseMaintenanceConstants.JPS_VAL, lblIJP1, false);
		txtOriginalJps1.setGridBagLayout(true);
		txtOriginalJps1.setUpperCase(true);

		txtOriginalJps2 = new XTextField(255, CaseMaintenanceConstants.JPS_VAL, lblIJP2, false);
		txtOriginalJps2.setGridBagLayout(true);
		txtOriginalJps2.setUpperCase(true);

		txtOriginalJps3 = new XTextField(255, CaseMaintenanceConstants.JPS_VAL, lblIJP3, false);
		txtOriginalJps3.setGridBagLayout(true);
		txtOriginalJps3.setUpperCase(true);

		txtOriginalJps4 = new XTextField(255, CaseMaintenanceConstants.JPS_VAL, lblIJP4, false);
		txtOriginalJps4.setGridBagLayout(true);
		txtOriginalJps4.setUpperCase(true);

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
		if (receivedFromValues == null) {
			receivedFromValues = GeneralDropdownPopulation.getReceivedFrom();
		}
		if (monitoringCategory == null) {
			monitoringCategory = GeneralDropdownPopulation.getMonitoringCategory();
		}
		if (appealType == null) {
			appealType = GeneralDropdownPopulation.getAppealType();
		}
		if (hearingType == null) {
			hearingType = GeneralDropdownPopulation.getHearingType();
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


	public class ChcbxItemListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (chckbxRehearing.isSelected()) {
				if (!chckbxTransferIn.isSelected()) {
					txtOrigCaseNo.setEnabled(true);
				}
			} else {
				// Check if transfer in selected
				if (!chckbxTransferIn.isSelected()) {
					CaseMethods.disableTextField(txtOrigCaseNo);
				}
			}
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
		}
	}
	public class TxtReceievedFromKeyListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
			if (txtReceivedFrom.getText() != null) {
				populateDropdown(txtReceivedFrom.getText());
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (txtReceivedFrom.getText() != null) {
				populateDropdown(txtReceivedFrom.getText());
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (txtReceivedFrom.getText() != null) {
				populateDropdown(txtReceivedFrom.getText());
			} else {
				if (txtReceivedFrom.isNullOrEmpty()) {
					lblIReceivedFrom.setText("Mandatory Field");
				}
			}
		}

		void populateDropdown(String value) {
			boolean found = false;
			for (int i = 0; i < receivedFromValues.size(); i++) {
				if (!receivedFromValues.get(i).getCourtFullName().equals("Select Magistrates Court")
						&& receivedFromValues.get(i).getCourtShortName().equalsIgnoreCase(value)) {
					found = true;
					lblIReceivedFrom2.setText(" ");
					cmbReceivedFrom.setSelectedIndex(i);
					break;
				}
			}
			if (!found) {
				txtReceivedFrom.setError();
			}

		}
	}

	/**
	 * Listener for txt received from.
	 *
	 */
	public class TxtReceivedFromFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			//leaving blank as we don't want to do anything on focus gained
		}

		@Override
		public void focusLost(FocusEvent e) {
			boolean found = false;
			if (txtReceivedFrom.getText() != null && !txtReceivedFrom.getText().equals("")) {
				for (int i = 0; i < receivedFromValues.size(); i++) {
					if (!receivedFromValues.get(i).getCourtFullName().equals("Select Magistrates Court")
							&& receivedFromValues.get(i).getCourtShortName()
									.equalsIgnoreCase(txtReceivedFrom.getText())) {
						found = true;
						//Need to do an additional check to see if it's an obsolete court
						if(receivedFromValues.get(i).getObsInd().equals("Y")) {
							lblIReceivedFrom2.setText(CaseMaintenanceConstants.OBSOLETE_COURT);
							cmbReceivedFrom.setSelectedIndex(i);
							txtReceivedFrom.setError(CaseMaintenanceConstants.OBSOLETE_COURT);
						} else {
							lblIReceivedFrom2.setText(" ");
							lblIReceivedFrom.setText(" ");
							cmbReceivedFrom.setSelectedIndex(i);								
						}
						break;
					}
				}
				if (!found) {
					txtReceivedFrom.setError();
					cmbReceivedFrom.setSelectedIndex(0);
					lblIReceivedFrom.setText("Invalid Entry");
				} 
			}

		}

	}
	
	/**
	 * Listener for received from.
	 */
	public class ReceivedFromFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			//leaving blank as we don't want to do anything on focus gained
		}

		@Override
		public void focusLost(FocusEvent e) {
			if(cmbReceivedFrom.getSelectedIndex()>0) {
				//if its an absolete court then show error 
				if(((RefCourtBasicValue)cmbReceivedFrom.getSelectedItem()).getObsInd()!=null &&
	((RefCourtBasicValue)cmbReceivedFrom.getSelectedItem()).getObsInd().equalsIgnoreCase("Y")) {
					lblIReceivedFrom2.setText(CaseMaintenanceConstants.OBSOLETE_COURT);
					lblIReceivedFrom.setText(CaseMaintenanceConstants.OBSOLETE_COURT);
					cmbReceivedFrom.setError(true);
				} else {
					lblIReceivedFrom2.setText(" ");
					lblIReceivedFrom.setText(" ");
					cmbReceivedFrom.setError(false);
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

	public XDatePanel getDateAppealLodgedPanel() {
		return dtDateAppealLodged;
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

	public String getReHearing() {
		if (chckbxRehearing.isSelected()) {
			return "Y";
		} else {
			return "N";
		}
	}

	public Integer getReceivingSite() {
		return ((CourtSiteBasicValue) cmbReceivingSite.getSelectedItem()).getId();
	}

	public Integer getRecFrom() {
		return ((RefCourtBasicValue) cmbReceivedFrom.getSelectedItem()).getId();
	}

	public Integer getMagCourtHearingType() {
		return ((RefSystemCodeBasicValue) cmbHearingType.getSelectedItem()).getId();

	}

	public Integer getRefMonitoringCategoryId() {
		// if return type is refMonitoringCategoryBasicValue
		return ((RefMonitoringCategoryBasicValue) cmbMonitoringCategory.getSelectedItem()).getRefMonitoringCategoryId();
	}

	public String getAppealType() {
		return ((RefSystemCodeBasicValue) cmbAppealType.getSelectedItem()).getCode();
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
	 * Used in the case amend process
	 * 
	 * @param caseNumber
	 * @return
	 */
	public void populateCaseBasicValue(CaseBasicValue basicValue) {
		basicValue.setCaseType("A");
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
		basicValue.setMonitoringCategoryId(getRefMonitoringCategoryId());
		basicValue.setCaseSubType(getAppealType());
		basicValue.setRefCourtID(getRecFrom());
		basicValue.setMagCourtHearingTypeRefId(getMagCourtHearingType());
		basicValue.setCourtID(XhibitSingleton.getInstance().getCourtId());
		basicValue.setChargeImportIndicator("O");
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
		log.debug("GeneralCriminalAppeal.populate() using caseBasicValue");
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

		if (caseBasicValue.getRetrial() != null && caseBasicValue.getRetrial().equalsIgnoreCase("Y")) {
			chckbxRehearing.setSelected(true);
		}		
		
		//populate the various dropdowns
		populateAppealType(caseBasicValue);
		populateReceivingSite(caseBasicValue);
		populateReceivedFrom(caseBasicValue);
		populateHearingType(caseBasicValue);
		populateMonitoringCategory(caseBasicValue);
		populateCCTransFrom(caseBasicValue);
		populateCivilUnrest(caseBasicValue);

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
		if (caseBasicValue.getTransferredCase() != null  && caseBasicValue.getTransferredCase().equals("Y")) {
				chckbxTransferIn.setSelected(true);
		}

		if (caseBasicValue.getOriginalCaseNumber() != null) {
			txtOrigCaseNo.setText(caseBasicValue.getOriginalCaseNumber());
		}

		if (caseBasicValue.getDateTransFrom() != null) {
			dtTransferDate.setDate(caseBasicValue.getDateTransFrom());
		}
	}
	
	/**
	 * Populate appeal type.
	 * @param caseBasicValue
	 */
	private void populateAppealType(CaseBasicValue caseBasicValue) {
		if (caseBasicValue.getCaseSubType() != null) {
			// start from 1 as 0 is select appeal type
			for (int i = 1; i < appealType.size(); i++) {
				if (appealType.get(i).getCode().equals(caseBasicValue.getCaseSubType())) {
					cmbAppealType.setSelectedItem(appealType.get(i));
				}
			}
		}
		
	}

	/**
	 * Populate receiving site.
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
	 * Populate received from
	 * @param caseBasicValue
	 */
	private void populateReceivedFrom(CaseBasicValue caseBasicValue) {
		// start at 1 as first value will be null (select mag court), this
		// will also set the text box as well as dropdown
		if (caseBasicValue.getRefCourtID() != null) {
			for (int i = 1; i < receivedFromValues.size(); i++) {
				if (receivedFromValues.get(i).getId().equals(caseBasicValue.getRefCourtID())) {
					cmbReceivedFrom.setSelectedItem(receivedFromValues.get(i));
				}
			}
		}
	}

	/**
	 * Populate hearing type
	 * used in case amend.
	 */
	private void populateHearingType(CaseBasicValue caseBasicValue) {
		// start at 1 as first value will be select hearing type
		if (caseBasicValue.getMagCourtHearingTypeRefId() != null) {
			for (int i = 1; i < hearingType.size(); i++) {
				if (hearingType.get(i).getId().equals(caseBasicValue.getMagCourtHearingTypeRefId())) {
					cmbHearingType.setSelectedItem(hearingType.get(i));
				}
			}
		}
	}

	/**
	 * Populate monitoring category dropdown
	 * used in case amend.
	 */
	private void populateMonitoringCategory(CaseBasicValue caseBasicValue) {
		// start at 1 as 0 is select monitoring category
		if (caseBasicValue.getMonitoringCategoryId() != null) {
			for (int i = 1; i < monitoringCategory.size(); i++) {
				if (caseBasicValue.getMonitoringCategoryId()
						.equals(monitoringCategory.get(i).getRefMonitoringCategoryId())) {
					cmbMonitoringCategory.setSelectedItem(monitoringCategory.get(i));
					break;
				}
			}
		}
	}

	/**
	 * Populate CC Trans From dropdown
	 * used in case amend.
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

	private void jbInit() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = CaseMethods.getDefaultGridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.setPreferredSize(new Dimension(1000, 600));

		// top, left, bottom, right
		gbc.insets = new Insets(5, 10, 5, 10); // keep gap between panels

		gbc.weighty = 0.4;
		gbc.weightx = 0.8;
		gbc.gridwidth = 2;
		this.add(getCaseDetailsPanel(), gbc);

		gbc.gridy++;
		gbc.weightx = 0.2;
		gbc.weighty = 0.1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(getCivilUnrestPanel(caseX), gbc);
		
		// Spacer
		gbc.gridx++;
		gbc.weightx = 0.6;
		JPanel dummyPanel = new JPanel();
		dummyPanel.setLayout(new GridBagLayout());
		this.add(dummyPanel, gbc);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		
		gbc.gridx = 0;
		gbc.gridy += 1;
		gbc.weightx = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 0.8;
		gbc.weighty = 0.25;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(getMagistratesCourtDetailsPanel(), gbc);

		gbc.gridy++;
		this.add(getOriginalJPsPanel(), gbc);
		
		gbc.gridy = 0;
		gbc.gridx += 2;
		gbc.gridheight = 1;
		gbc.weightx = 0.2;
		gbc.weighty = 0.4;
		this.add(getTransferDetailsPanel(), gbc);
	}

	

	private JPanel getCaseDetailsPanel() {
		if (caseDetailsPanel == null) {
			caseDetailsPanel = new JPanel();
			caseDetailsPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = CaseMethods.getDefaultGridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;

			// First column (labels)
			gbc.weighty = 0.2;
			gbc.weightx = 0.05;
			caseDetailsPanel.add(lblCaseNumber, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblCaseTitle, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblDateReceived, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblAppealType, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblDateAppealLodged, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblReceivingSite, gbc);

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

			gbc.gridx++;
			caseDetailsPanel.add(Box.createRigidArea(txtCaseTitle.getPreferredSize()), gbc);

			gbc.weightx = 0.05;
			gbc.gridx -= 2;
			gbc.gridy += 2;
			caseDetailsPanel.add(dtDateReceived, gbc);

			gbc.gridy += 2;
			gbc.gridwidth = 2;
			caseDetailsPanel.add(cmbAppealType, gbc);

			gbc.gridy += 2;
			gbc.gridwidth = 1;
			caseDetailsPanel.add(dtDateAppealLodged, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(cmbReceivingSite, gbc);

			gbc.gridy = 1;
			gbc.weighty = 0.1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.gridwidth = 5; // to make sure error labels dont move/get
								// truncated in grid bag
			caseDetailsPanel.add(lblICaseTitle, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblIDateReceived, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblIAppealType, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblIDateAppealLodged, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblIReceivingSite, gbc);
			
			CaseMethods.addChangeListeners(caseDetailsPanel.getComponents(), caseX);
		}

		return caseDetailsPanel;
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

			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridwidth = 4;
			gbc.fill = GridBagConstraints.BOTH;
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

			gbc.gridy += 2;
			gbc.gridwidth = 2;
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

			gbc.gridy += 2;
			gbc.gridwidth = 4;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			transferDetailsPanel.add(lblIMonitoringCategory, gbc);
			gbc.insets = XHIBITConstant.nonContainerInsets;

			gbc.gridy++;
			gbc.gridx--;
			gbc.gridwidth = 2;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			transferDetailsPanel.add(lblMonitoringCategory, gbc);

			gbc.gridx++;
			gbc.gridwidth = 4;
			transferDetailsPanel.add(cmbMonitoringCategory, gbc);

			CaseMethods.addChangeListeners(transferDetailsPanelInner.getComponents(), caseX);
			CaseMethods.addChangeListeners(transferDetailsPanel.getComponents(), caseX);
		}

		return transferDetailsPanel;
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
			magistratesCourtDetailsPanel.add(lblReceivedFrom, gbc);

			gbc.gridy = 1;
			gbc.gridx++;
			gbc.insets = new Insets(4, 4, 4, 25);
			gbc.weightx = 0.2;

			gbc.fill = GridBagConstraints.HORIZONTAL;
			txtReceivedFrom.setColumns(5);
			txtReceivedFrom.setMinimumSize(txtReceivedFrom.getPreferredSize());
			txtReceivedFrom.setMaxLength(5);
			txtReceivedFrom.setUpperCase(true);
			magistratesCourtDetailsPanel.add(txtReceivedFrom, gbc);

			gbc.gridx++;
			gbc.weightx = 0.75;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			magistratesCourtDetailsPanel.add(cmbReceivedFrom, gbc);

			gbc.gridwidth = 2;
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.weighty = 0.1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			magistratesCourtDetailsPanel.add(lblIReceivedFrom, gbc);

			gbc.gridx++;
			gbc.weightx = 0.75;
			magistratesCourtDetailsPanel.add(lblIReceivedFrom2, gbc);

			gbc.gridx = 0;
			gbc.gridy += 3;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.weighty = 0.2;
			gbc.gridwidth = 1;
			magistratesCourtDetailsPanel.add(lblHearingType, gbc);

			gbc.gridx++;
			gbc.gridwidth = 2;
			magistratesCourtDetailsPanel.add(cmbHearingType, gbc);

			gbc.gridy--;
			gbc.weighty = 0.1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			magistratesCourtDetailsPanel.add(lblIHearingType, gbc);

			CaseMethods.addChangeListeners(magistratesCourtDetailsPanel.getComponents(), caseX);
		}

		return magistratesCourtDetailsPanel;
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
			txtOriginalJps2.setMaxLength(35);
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
			txtOriginalJps3.setMaxLength(35);
			txtOriginalJps3.setMinimumSize(txtOriginalJps3.getPreferredSize());
			originalJPsPanel.add(txtOriginalJps3, gbc);

			gbc.gridy += 2;
			txtOriginalJps4.setColumns(10);
			txtOriginalJps4.setMaxLength(35);
			txtOriginalJps4.setMinimumSize(txtOriginalJps4.getPreferredSize());
			originalJPsPanel.add(txtOriginalJps4, gbc);

			gbc.gridy = 0;
			gbc.gridx = 1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			originalJPsPanel.add(lblIJP1, gbc);

			gbc.gridx += 2;
			originalJPsPanel.add(lblIJP2, gbc);

			gbc.gridy += 2;
			gbc.gridx = 1;
			originalJPsPanel.add(lblIJP3, gbc);

			gbc.gridx += 2;
			originalJPsPanel.add(lblIJP4, gbc);

			CaseMethods.addChangeListeners(originalJPsPanel.getComponents(), caseX);
		}
		return originalJPsPanel;
	}
	
	public class TransferInItemListener implements ItemListener {
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
					// disable orig case no and clear it
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
				CaseMethods.compareDates(dtTransferDate, dtDateReceived, CaseMaintenanceConstants.BEFORE, CaseMaintenanceConstants.ON_BEFORE_DATE_RECEIVED);

				// If date received condition is satisfied then test
				// other, else dont erase warning message
				if (!dtTransferDate.hasError()) {
					CaseMethods.compareDates(dtTransferDate, dtDateAppealLodged, CaseMaintenanceConstants.AFTER, CaseMaintenanceConstants.ON_AFTER_DATE_APPEAL);
				}
			}
		}
	}
	
	/**
	 * Transfer date listener. 
	 */
	public class TransferDateListener implements MFieldListener {
		
		@Override
		public void fieldEntered(FocusEvent event) {
			//leaving blank as we don't want to do anything on field entered
		}

		@Override
		public void fieldExited(FocusEvent event) {
			// Check mandatory fields to enable create button
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);

			if (dtTransferDate.isDateValidate()) {
				// Transfer date must be equal to or before date received
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
	
	public class DateAppealLodgedListener implements MFieldListener {
		@Override
		public void fieldEntered(FocusEvent event) {
			//leaving blank as we don't want to do anything on field entered
		}

		@Override
		public void fieldExited(FocusEvent event) {
			// Check mandatory fields to enable create button
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
			// Validate other fields that depend on this, i.e. transfer date
			if (dtDateAppealLodged.isDateValidate()) {
				// Transfer date must be equal to or before date received,
				// AND equal to or after date appeal lodged
				CaseMethods.compareDates(dtTransferDate, dtDateAppealLodged, CaseMaintenanceConstants.AFTER, CaseMaintenanceConstants.ON_AFTER_DATE_APPEAL);
				

				// If date appeal lodged condition isnt satisfied then test
				// other, else dont erase warning message
				if (!dtTransferDate.hasError()) {
					CaseMethods.compareDates(dtTransferDate, dtDateReceived, CaseMaintenanceConstants.BEFORE, CaseMaintenanceConstants.ON_BEFORE_DATE_RECEIVED);
				}

				// adding this so it validates start date before you tab
				// into the defendant tab
				// so that you can't save a defendant with invalid date.
				try {
					if (!caseX.getDefendantPanel().validateForDateAppealLodged(dtDateAppealLodged.getDate())) {
						dtDateAppealLodged.setSecondaryError(true);
						dtDateAppealLodged.setSecondaryErrorText(
								"Invalidates Conviction/Orig date of sentence for defendant(s). Please correct these first");
					} else {
						dtDateAppealLodged.setSecondaryError(false);
					}
				} catch (CSValidationException e) {
					log.error("Date appeal lodged is invalid");
					dtDateAppealLodged.setSecondaryError(false);
				}
			}
		}
	}
	
	/**
	 * Used from caseXPanel to set case number
	 * 
	 * @param caseNumber
	 */
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
		return new FocusTraversalOnArray(new Component[] { txtCaseTitle, dtDateReceived, cmbAppealType,
				dtDateAppealLodged, cmbReceivingSite, cmbCivilUnrest, chckbxRehearing, chckbxTransferIn, dtTransferDate,
				cmbTransferFrom, txtOrigCaseNo, cmbMonitoringCategory, txtReceivedFrom, cmbReceivedFrom, cmbHearingType,
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