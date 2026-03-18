package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.apache.log4j.Logger;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import mseries.Calendar.MFieldListener;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefMonitoringCategoryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.client.casemanagement.util.CaseMethods;
import uk.gov.courtservice.xhibit.client.casemanagement.util.NoOfDefendantsListener;
import uk.gov.courtservice.xhibit.client.util.CaseMaintenanceConstants;
import uk.gov.courtservice.xhibit.client.util.XCheckBox;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * @author C.Kudzin - Feb 21, 2018 - Implemented MaxLength on text fields
 * @author C.Kudzin - Feb 27, 2018 - CTX-1395 Implementing further date
 *         validation
 */
public class GeneralSentence extends GeneralAllCasesTab {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private CaseXPanel caseX;
	private Integer version;

	Calendar todaysDate = null;
	private ArrayList<RefCourtBasicValue> receivedFromValues;
	private ArrayList<RefSystemCodeBasicValue> policeForceCodes;
	private ArrayList<RefSystemCodeBasicValue> ticketTypeCodes;
	private ArrayList<RefSystemCodeBasicValue> receiptTypes;

	private ArrayList<CourtSiteBasicValue> receivingSite;
	private ArrayList<RefMonitoringCategoryBasicValue> monitoringCategory;

	// Error Labels
	private JLabel lbICaseTitle;
	private JLabel lbIMonitoringCategory;
	private JLabel lbIDateReceived;
	private JLabel lbITicketType;
	private JLabel lbIReceivingSite;
	private JLabel lbICommittalDate;
	private JLabel lbIReceiptType;
	private JLabel lbIReceivedFrom1;
	private JLabel lbIReceivedFrom2;
	private JLabel lbIPoliceForce1;
	private JLabel lbIPoliceForce2;
	private JLabel lblCaseNumber;
	private JLabel lblIOrigCaseNo; 
	private JLabel lblNoOfDefendants; 
	private JLabel lblINoOfDefendants;

	// text fields
	private XTextField txtReceivedFrom;
	private XTextField txtPoliceForce;
	private XTextField txtCaseTitle;
	private XTextField txtOrigCaseNo;
	private XTextField txtNoOfDefendants;

	// comboxes
	private XComboBox cmbReceivedFrom;
	private XComboBox cmbPoliceForce;
	private XComboBox cmbTicketType;
	private XComboBox cmbReceiptType;
	private XComboBox cmbReceivingSite;
	private XComboBox cmbMonitoringCategory;
	private XComboBox cmbTransferFrom;

	// xdatefields
	private XDatePanel dtDateReceived;
	private XDatePanel dtDateOfCommittal;
	private XDatePanel dtTransferDate;

	// checkboxes
	private XCheckBox chckbxSecureCourt;
	private XCheckBox chckbxTicketRequired;
	private XCheckBox chckbxTransferIn;

	// Refactor for grid bag
	private JLabel lblCaseTitle;
	private JLabel lblDateReceived;
	private JLabel lblReceiptType;
	private JLabel lblDateOfCommittal;
	private JLabel lblReceivingSite;
	private JLabel lblReceivedFrom;
	private JLabel lblITransferDate;
	private JLabel lblTransferDate;
	private JLabel lblOrigCaseNo;
	private JLabel lblTransferFrom;
	private JLabel lblITransferFrom;
	private JLabel lblMonitoringCategory;
	private JLabel lblTicketType;
	private JLabel lblPoliceForce;
	private JLabel lblSecureCourt;

	private JPanel caseDetailsPanel;
	private JPanel transferAndTicketPanel;
	private JPanel magistratesCourtDetailsPanel;
	private JPanel policeForcePanel;

	// array of all the invalid entry fields
	private List<JLabel> validationFields = new ArrayList<JLabel>();
	private List<Object> mandatoryFields = new ArrayList<Object>();

	private boolean fieldsChangedGlobal = false;
	private int numOfCharges = 0;
	private RefSystemCodeBasicValue previousReceiptType;

	/**
	 * Used for logging and exception handling.
	 */
	private static final String CLASS_NAME = ".GeneralSentence";
	
	
	public GeneralSentence(final CaseXPanel caseX) {
		
		initDropdownTypes();
		initCivilUnrestPanel(validationFields);

		lblCaseNumber = new JLabel("Case Number");
		lblCaseNumber.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCaseNumber.setBorder(new LineBorder(new Color(0, 0, 0)));

		lblCaseTitle = new JLabel("Case Title   R-v-");

		lbICaseTitle = CaseMethods.createErrorLabel(validationFields);

		txtCaseTitle = new XTextField(72, "^.{1,72}$", lbICaseTitle, true);
		txtCaseTitle.setDisabledTextColor(Color.BLACK);
		txtCaseTitle.setGridBagLayout(true);
		txtCaseTitle.setUpperCase(true);
		txtCaseTitle.setMaxLength(72);
		
		CaseMethods.addRemoveFromMandatoryFields(txtCaseTitle, lblCaseTitle, mandatoryFields, true);

		txtCaseTitle.addFocusListener(new MandatoryFieldsFocusListener());
		txtCaseTitle.setColumns(10);
		txtCaseTitle.setMinimumSize(txtCaseTitle.getPreferredSize());

		lbIDateReceived = CaseMethods.createErrorLabel(validationFields);

		lblDateReceived = new JLabel("Date Received");

		dtDateReceived = new XDatePanel(this, Calendar.getInstance(), true, lbIDateReceived, CaseMaintenanceConstants.BEFORE);
		dtDateReceived.setGridBagLayout(true);
		
		CaseMethods.addRemoveFromMandatoryFields(dtDateReceived, lblDateReceived, mandatoryFields, true);

		dtDateReceived.getDateComponent().addMFieldListener(new DateReceivedListener());

		lblNoOfDefendants = new JLabel("No. of Defendants");

		lblINoOfDefendants = CaseMethods.createErrorLabel(validationFields);

		txtNoOfDefendants = new XTextField();
		NoOfDefendantsListener noDefListener = new NoOfDefendantsListener(caseX, lblINoOfDefendants);
		txtNoOfDefendants.addFocusListener(noDefListener);
		txtNoOfDefendants.setMaxLength(3);
		txtNoOfDefendants.setNumeric(true);
		CaseMethods.addRemoveFromMandatoryFields(txtNoOfDefendants, lblNoOfDefendants, mandatoryFields, true);

		txtNoOfDefendants.addFocusListener(new MandatoryFieldsFocusListener());
		txtNoOfDefendants.setColumns(10);
		txtNoOfDefendants.setMinimumSize(txtNoOfDefendants.getPreferredSize());

		lblReceiptType = new JLabel("Receipt Type");

		lbIReceiptType = CaseMethods.createErrorLabel(validationFields);

		cmbReceiptType = new XComboBox(true, lbIReceiptType);
		cmbReceiptType.setGridBagLayout(true);
		// Added nullguards as if accessed from court other than snaresbrook
		// there's no data to populate, ctx-1428
		if (!receiptTypes.isEmpty()) {
			cmbReceiptType.setModel(new DefaultComboBoxModel(receiptTypes.toArray()));
			cmbReceiptType.setRenderer(new DropdownBoxCellRender());
		}
		
		CaseMethods.addRemoveFromMandatoryFields(cmbReceiptType, lblReceiptType, mandatoryFields, true);
		
		cmbReceiptType.addFocusListener(new MandatoryFieldsFocusListener());
		cmbReceiptType.addActionListener(new ReceiptTypeListener());

		lblDateOfCommittal = new JLabel("Date of Committal");

		lbICommittalDate = CaseMethods.createErrorLabel(validationFields);

		dtDateOfCommittal = new XDatePanel(this, null, true, lbICommittalDate, CaseMaintenanceConstants.BEFORE, dtDateReceived,
				"Date of Committal", "Date Received");
		dtDateOfCommittal.setGridBagLayout(true);

		CaseMethods.addRemoveFromMandatoryFields(dtDateOfCommittal, lblDateOfCommittal, mandatoryFields, true);

		dtDateOfCommittal.setCustomWarning(CaseMaintenanceConstants.ON_BEFORE_DATE_RECEIVED);
		dtDateOfCommittal.getDateComponent().addMFieldListener(new DateOfCommittalListener()); 

		lblReceivingSite = new JLabel("Receiving Site");

		lbIReceivingSite = CaseMethods.createErrorLabel(validationFields);

		cmbReceivingSite = new XComboBox(false, lbIReceivingSite);
		cmbReceivingSite.setGridBagLayout(true);
		// Added nullguards as if accessed from court other than snaresbrook
		// there's no data to populate, ctx-1428
		if (!receivingSite.isEmpty()) {
			cmbReceivingSite.setModel(new DefaultComboBoxModel(receivingSite.toArray()));
			cmbReceivingSite.setRenderer(new DropdownBoxCellRender());
		}

		lblReceivedFrom = new JLabel("Received From");

		lbIReceivedFrom1 = CaseMethods.createErrorLabel(validationFields);

		txtReceivedFrom = new XTextField(5, "^[a-zA-z0-9]{1,5}$", lbIReceivedFrom1, true);
		txtReceivedFrom.setGridBagLayout(true);
		txtReceivedFrom.setUpperCase(true);
		txtReceivedFrom.setMaxLength(5);
		// add a on key press
		txtReceivedFrom.addKeyListener(new ReceivedFromKeyListener());

		txtReceivedFrom.setColumns(10);
		txtReceivedFrom.setMinimumSize(txtReceivedFrom.getPreferredSize());
		
		CaseMethods.addRemoveFromMandatoryFields(txtReceivedFrom, lblReceivedFrom, mandatoryFields, true);

		txtReceivedFrom.addFocusListener(new MandatoryFieldsFocusListener());
		txtReceivedFrom.addFocusListener(new ReceivedFromFocusListener());

		lbIReceivedFrom2 = new JLabel(" ");
		lbIReceivedFrom2.setForeground(Color.RED);

		cmbReceivedFrom = new XComboBox(true, lbIReceivedFrom2, txtReceivedFrom);
		cmbReceivedFrom.setGridBagLayout(true);
		// Added nullguards as if accessed from court other than snaresbrook
		// there's no data to populate, ctx-1428
		if (!receivedFromValues.isEmpty()) {
			cmbReceivedFrom.setModel(new DefaultComboBoxModel(receivedFromValues.toArray()));
			cmbReceivedFrom.setRenderer(new DropdownBoxCellRender());
		}
		cmbReceivedFrom.addFocusListener(new MandatoryFieldsFocusListener());
		cmbReceivedFrom.addFocusListener(new ReceivedFromDropdownFocusListener());
		chckbxTransferIn = new XCheckBox();
		chckbxTransferIn.addItemListener(new TransferInListener());
		chckbxTransferIn.setEnabled(true);
		chckbxTransferIn.setHorizontalTextPosition(SwingConstants.LEFT);

		lblITransferDate = CaseMethods.createErrorLabel(validationFields);

		lblTransferDate = new JLabel("Transfer Date");

		dtTransferDate = new XDatePanel(this, todaysDate, false, lblITransferDate, CaseMaintenanceConstants.BEFORE, dtDateReceived, "Date",
				"Transfer Date");
		dtTransferDate.setGridBagLayout(true);
		dtTransferDate.setCustomWarning(CaseMaintenanceConstants.ON_BEFORE_DATE_RECEIVED);
		dtTransferDate.setDateEnabled(false);
		dtTransferDate.setEnabledAndFocusable(false);
		dtTransferDate.getDateComponent().addMFieldListener(new TransferDateListener());

		lblIOrigCaseNo = CaseMethods.createErrorLabel(validationFields);

		lblOrigCaseNo = new JLabel("Orig Case No.");

		txtOrigCaseNo = new XTextField(9, "^[AST]{1}[0-9]{8}$", lblIOrigCaseNo, false);
		txtOrigCaseNo.setGridBagLayout(true);
		txtOrigCaseNo.setUpperCase(true);
		txtOrigCaseNo.setMaxLength(9);
		txtOrigCaseNo.setEnabled(false);
		txtOrigCaseNo.setColumns(10);
		txtOrigCaseNo.setMinimumSize(txtOrigCaseNo.getPreferredSize());

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

		lblMonitoringCategory = new JLabel("Monitoring Category");

		lbIMonitoringCategory = new JLabel(" ");
		lbIMonitoringCategory.setForeground(Color.RED);

		cmbMonitoringCategory = new XComboBox(true, lbIMonitoringCategory);
		cmbMonitoringCategory.setGridBagLayout(true);
		// Added nullguards as if accessed from court other than snaresbrook
		// there's no data to populate, ctx-1428
		if (!monitoringCategory.isEmpty()) {
			cmbMonitoringCategory.setModel(new DefaultComboBoxModel(monitoringCategory.toArray()));
			cmbMonitoringCategory.setRenderer(new DropdownBoxCellRender());
		}

		CaseMethods.addRemoveFromMandatoryFields(cmbMonitoringCategory, lblMonitoringCategory, mandatoryFields, true);

		cmbMonitoringCategory.addFocusListener(new MandatoryFieldsFocusListener());

		lblSecureCourt = new JLabel("Secure Court");

		chckbxSecureCourt = new XCheckBox();
		chckbxSecureCourt.setHorizontalTextPosition(SwingConstants.LEFT);

		chckbxTicketRequired = new XCheckBox();
		chckbxTicketRequired.addItemListener(new TicketRequiredListener());
		chckbxTicketRequired.setHorizontalTextPosition(SwingConstants.LEFT);

		lblTicketType = new JLabel("Ticket Type");

		lbITicketType = new JLabel(" ");
		lbITicketType.setForeground(Color.RED);

		cmbTicketType = new XComboBox(false, lbITicketType, chckbxTicketRequired);
		cmbTicketType.setGridBagLayout(true);
		cmbTicketType.setEnabled(false);
		// Added nullguards as if accessed from court other than snaresbrook
		// there's no data to populate, ctx-1428
		if (!ticketTypeCodes.isEmpty()) {
			cmbTicketType.setModel(new DefaultComboBoxModel(ticketTypeCodes.toArray()));
			cmbTicketType.setRenderer(new DropdownBoxCellRender());
		}
		cmbTicketType.addFocusListener(new MandatoryFieldsFocusListener());

		lblPoliceForce = new JLabel("Police Force");

		lbIPoliceForce1 = CaseMethods.createErrorLabel(validationFields);

		txtPoliceForce = new XTextField(2, "^[0-9]{1,2}$", lbIPoliceForce1, true);
		txtPoliceForce.setGridBagLayout(true);
		txtPoliceForce.setUpperCase(true);
		txtPoliceForce.setMaxLength(2);
		txtPoliceForce.setColumns(10);
		txtPoliceForce.addKeyListener(new PoliceForceListener());
		
		CaseMethods.addRemoveFromMandatoryFields(txtPoliceForce, lblPoliceForce, mandatoryFields, true);

		txtPoliceForce.addFocusListener(new MandatoryFieldsFocusListener());
		txtPoliceForce.setMinimumSize(txtPoliceForce.getPreferredSize());

		lbIPoliceForce2 = new JLabel(" ");
		lbIPoliceForce2.setForeground(Color.RED);

		cmbPoliceForce = new XComboBox(false, lbIPoliceForce2, txtPoliceForce);
		cmbPoliceForce.setGridBagLayout(true);
		// Added nullguards as if accessed from court other than snaresbrook
		// there's no data to populate, ctx-1428
		if (!policeForceCodes.isEmpty()) {
			cmbPoliceForce.setModel(new DefaultComboBoxModel(policeForceCodes.toArray()));
			cmbPoliceForce.setRenderer(new DropdownBoxCellRender());
		}
		cmbPoliceForce.addFocusListener(new MandatoryFieldsFocusListener());
		try {
			if (XhibitSingleton.getInstance().getCourtBasicValue().getPoliceForceCode() != null) {
				Integer code = XhibitSingleton.getInstance().getCourtBasicValue().getPoliceForceCode();
				populatePoliceForce(code);
			}
		} catch (CSRecoverableException e1) {
			log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e1);
			XHIBITConstant.handleError(e1, this.getClass());
		}
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
		if (policeForceCodes == null) {
			policeForceCodes = GeneralDropdownPopulation.getPoliceForceCode();
		}
		if (monitoringCategory == null) {
			monitoringCategory = GeneralDropdownPopulation.getMonitoringCategory();
		}
		if (ticketTypeCodes == null) {
			ticketTypeCodes = GeneralDropdownPopulation.getTicketType();
		}
		if (receiptTypes == null) {
			receiptTypes = GeneralDropdownPopulation.getReceiptTypes(RefSystemCodeCriteria.CodeType.RECEIPT_TYPE_S);
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

	public List<JLabel> getValidationFields() {
		return validationFields;
	}
	
	public void validateMandatoryFields() {
		CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
	}
	
	// used by CaseXPanel to store the values in the DB.
	public String getCaseTitle() {
		return txtCaseTitle.getText();
	}

	public Integer getRefMonitoringCategoryId() {
		// if return type is refMonitoringCategoryBasicValue
		return ((RefMonitoringCategoryBasicValue) cmbMonitoringCategory.getSelectedItem()).getRefMonitoringCategoryId();
	}

	public Timestamp getDateReceived() {
		Calendar c = null;
		if (dtDateReceived.isDateValidate()) {
			try {
				c = dtDateReceived.getDate();
			} catch (CSValidationException e) {
				log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e);
			}
			if(c!=null) {
				return new Timestamp(c.getTimeInMillis());
			} else {
				return null;
			}
		} else {
			log.error("Date received is incorrect");
			return null;
		}
	}

	public String getTicketRequired() {
		if (chckbxTicketRequired.isSelected()) {
			return "Y";
		} else {
			return "N";
		}
	}

	public Integer getTicketType() {
		if (chckbxTicketRequired.isSelected()) {
			return ((RefSystemCodeBasicValue) cmbTicketType.getSelectedItem()).getId();
		} else {
			return null;
		}
	}

	public Integer getReceivingSite() {
		return ((CourtSiteBasicValue) cmbReceivingSite.getSelectedItem()).getId();
	}

	public Timestamp getDateOfCommittal() {
		Calendar c = null;
		if (dtDateOfCommittal.isDateValidate()) {
			try {
				c = dtDateOfCommittal.getDate();
			} catch (CSValidationException e) {
				log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e);
			}
			if(c!=null) {
				return new Timestamp(c.getTimeInMillis());
			} else {
				return null;
			}
		} else {
			log.error("Date of committal is incorrect");
			return null;
		}
	}

	public String getReceiptType() {
		return ((RefSystemCodeBasicValue) cmbReceiptType.getSelectedItem()).getCode();
	}

	public String getSecureCourt() {
		if (chckbxSecureCourt.isSelected()) {
			return "Y";
		} else {
			return "N";
		}
	}

	public Integer getRecFrom() {
		return ((RefCourtBasicValue) cmbReceivedFrom.getSelectedItem()).getId();
	}

	public Integer getPoliceForceCode() {
		return ((RefSystemCodeBasicValue) cmbPoliceForce.getSelectedItem()).getId();
	}

	public String getTransferIn() {
		if (chckbxTransferIn.isSelected())
			return "Y";
		else
			return "N";
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
		return basicValue;
	}

	/**
	 * Used in the case amend process
	 * 
	 * @param caseNumber
	 * @return
	 */
	public void populateCaseBasicValue(CaseBasicValue basicValue) {
		basicValue.setCaseType("S");
		basicValue.setVersion(version);
		basicValue.setCaseTitle(getCaseTitle());
		basicValue.setMonitoringCategoryId(getRefMonitoringCategoryId());
		basicValue.setReceivedDate(getDateReceived());
		basicValue.setTicketRequired(getTicketRequired());
		basicValue.setTicketTypeCode(getTicketType());
		basicValue.setCourtIdReceivingSite(getReceivingSite());
		basicValue.setCommittalDate(getDateOfCommittal());
		basicValue.setMagConvictionDate(DateTimeUtilities.convertToCalendar(getDateOfCommittal()));

		basicValue.setReceiptType(getReceiptType());


		basicValue.setNoDefendantsForCase(getNoOfDefendants());
		basicValue.setSecureCourt(getSecureCourt());
		basicValue.setRefCourtID(getRecFrom());
		basicValue.setPoliceForceCode(getPoliceForceCode());
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
	public void populate(CaseBasicValue caseBasicValue) throws ChargeControllerException {
		log.debug("GeneralSentence.populate() using caseBasicValue");
		version = caseBasicValue.getVersion();
		lblCaseNumber.setText(caseBasicValue.getCaseType() + caseBasicValue.getCaseNumber().toString());

		if (caseBasicValue.getCaseTitle() != null) {
			txtCaseTitle.setText(caseBasicValue.getCaseTitle());
		}

		if (caseBasicValue.getReceivedDate() != null) {
			dtDateReceived.setDate(caseBasicValue.getReceivedDate());
		}

		if (caseBasicValue.getNoDefendantsForCase() != null) {
			txtNoOfDefendants.setText(Integer.toString(caseBasicValue.getNoDefendantsForCase()));
		}

		if (caseBasicValue.getCommittalDate() != null) {
			dtDateOfCommittal.setDate(caseBasicValue.getCommittalDate());
		}

		//populate the various dropdowns
		populateReceivingSite(caseBasicValue);		
		populateReceivedFrom(caseBasicValue);
		populateReceiptType(caseBasicValue);
		populateMonitoringCategory(caseBasicValue);
		populateTicketType(caseBasicValue);
		populatePoliceForce(caseBasicValue.getPoliceForceCode());
		populateTransferFrom(caseBasicValue);
		populateCivilUnrest(caseBasicValue);
		
		// tick or untick the various checkboxes on the page.
		populateCheckBoxes(caseBasicValue);

		if (caseBasicValue.getOriginalCaseNumber() != null) {
			txtOrigCaseNo.setText(caseBasicValue.getOriginalCaseNumber());
		}

		if (caseBasicValue.getDateTransFrom() != null) {
			dtTransferDate.setDate(caseBasicValue.getDateTransFrom());
		}
		setCharges(caseBasicValue.getCaseId());
	}

	/**
	 * Called from populate method to tick or untick the various checkboxes.
	 * @param caseBasicValue
	 */
	private void populateCheckBoxes(CaseBasicValue caseBasicValue) {

		chckbxSecureCourt.setSelected(caseBasicValue.getSecureCourt() != null && caseBasicValue.getSecureCourt().equalsIgnoreCase(("Y")));
		
		chckbxTicketRequired.setSelected(caseBasicValue.getTicketRequired() != null && caseBasicValue.getTicketRequired().equalsIgnoreCase(("Y")));
		
		// CTX-1255 additions
		chckbxTransferIn.setSelected(caseBasicValue.getTransferredCase() != null && caseBasicValue.getTransferredCase().equals("Y"));

	}

	/**
	 * Populate the received from dropdown.
	 * @param caseBasicValue
	 */
	private void populateReceivedFrom(CaseBasicValue caseBasicValue) {
		if (caseBasicValue.getRefCourtID() != null) {
			for (int i = 1; i < receivedFromValues.size(); i++) {
				if (receivedFromValues.get(i).getId().equals(caseBasicValue.getRefCourtID())) {
					cmbReceivedFrom.setSelectedItem(receivedFromValues.get(i));
				}
			}	
		}
	}

	/**
	 * Populate the receiving site dropdown.
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
	 * Populate the monitoring category dropdown.
	 * @param caseBasicValue
	 */
	private void populateMonitoringCategory(CaseBasicValue caseBasicValue) {
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
	 * Populate the ticket type dropdown.
	 * @param caseBasicValue
	 */
	private void populateTicketType(CaseBasicValue caseBasicValue) {
		if (caseBasicValue.getTicketTypeCode() != null) {
			for (int i = 1; i < ticketTypeCodes.size(); i++) {
				if (ticketTypeCodes.get(i).getId().equals(caseBasicValue.getTicketTypeCode())) {
					cmbTicketType.setSelectedItem(ticketTypeCodes.get(i));
					break;
				}
			}
		}
	}

	/**
	 * Populate the transfer from dropdown.
	 * @param caseBasicValue
	 */
	private void populateTransferFrom(CaseBasicValue caseBasicValue) {
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

	/**
	 * Populate the police force code dropdown.
	 * @param caseBasicValue
	 */
	private void populatePoliceForce(Integer policeForceCode) {
		if (policeForceCode != null) {
			for (int i = 0; i < policeForceCodes.size(); i++) {
				if (policeForceCodes.get(i).getId().equals(policeForceCode)) {
					cmbPoliceForce.setSelectedItem(policeForceCodes.get(i));
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
		this.add(getCaseDetailsPanel(), gbc);

		gbc.gridx++;
		gbc.weightx = 0.6;
		this.add(getTransferAndTicketPanel(), gbc);
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
			caseDetailsPanel.add(lblReceiptType, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblNoOfDefendants, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblDateOfCommittal, gbc);

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
			caseDetailsPanel.add(cmbReceiptType, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(txtNoOfDefendants, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(dtDateOfCommittal, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(cmbReceivingSite, gbc);

			gbc.gridy = 1;
			gbc.weighty = 0.1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.gridwidth = 5; // to make sure error labels dont move/get
								// truncated in grid bag
			caseDetailsPanel.add(lbICaseTitle, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lbIDateReceived, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lbIReceiptType, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblINoOfDefendants, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lbICommittalDate, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lbIReceivingSite, gbc);

			gbc.gridy += 2;
			gbc.gridx = 0;
			gbc.gridwidth = 4;
			gbc.gridheight = 3;
			gbc.insets = new Insets(15, 0, 15, 0);
			caseDetailsPanel.add(getMagistratesCourtDetailsPanel(), gbc);

			gbc.gridx = 0;
			gbc.gridy += 3;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridheight = 3;
			gbc.gridwidth = 2;
			caseDetailsPanel.add(getCivilUnrestPanel(caseX), gbc);
			
			CaseMethods.addChangeListeners(caseDetailsPanel.getComponents(), caseX);
		}
		return caseDetailsPanel;
	}

	private JPanel getTransferAndTicketPanel() {
		if (transferAndTicketPanel == null) {
			transferAndTicketPanel = new JPanel();
			transferAndTicketPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = CaseMethods.getDefaultGridBagConstraints();

			Insets newInsets = new Insets(4, 0, 0, -21);
			gbc.insets = newInsets;
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.NORTHEAST;
			gbc.gridwidth = 2;
			gbc.weightx = 0.05;
			transferAndTicketPanel.add(chckbxTransferIn, gbc);

			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridwidth = 4;
			gbc.gridheight = 3;
			gbc.fill = GridBagConstraints.BOTH;
			JPanel transferDetailsPanelInner = new JPanel();
			transferDetailsPanelInner.setLayout(new GridBagLayout());
			transferDetailsPanelInner.setBorder(BorderFactory.createTitledBorder("Transfer In"));

			gbc.weightx = 0.95;

			GridBagConstraints gbcInner = CaseMethods.getDefaultGridBagConstraints();
			gbcInner.anchor = GridBagConstraints.WEST;
			gbc.weighty = 0.2;
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

			transferAndTicketPanel.add(transferDetailsPanelInner, gbc);

			gbc.gridy += 4;
			gbc.gridwidth = 2;
			gbc.gridheight = 1;
			gbc.weighty = 0.2;
			gbc.weightx = 0.05;
			gbc.fill = GridBagConstraints.NONE;
			transferAndTicketPanel.add(lblOrigCaseNo, gbc);

			gbc.gridx += 2;
			transferAndTicketPanel.add(txtOrigCaseNo, gbc);

			gbc.gridy--;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weighty = 0.1;
			transferAndTicketPanel.add(lblIOrigCaseNo, gbc);

			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.gridy += 2;
			gbc.weighty = 0.1;
			transferAndTicketPanel.add(lbIMonitoringCategory, gbc);

			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.gridy += 2;
			gbc.gridx--;
			transferAndTicketPanel.add(lblMonitoringCategory, gbc);

			gbc.gridx += 2;
			transferAndTicketPanel.add(cmbMonitoringCategory, gbc);

			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.gridy += 2;
			gbc.gridx -= 2;
			transferAndTicketPanel.add(lblSecureCourt, gbc);

			gbc.gridx += 2;
			gbc.weighty = 0.2;
			gbc.weightx = 0.95;
			gbc.anchor = GridBagConstraints.WEST;
			transferAndTicketPanel.add(chckbxSecureCourt, gbc);
			
			gbc.gridy++;
			gbc.gridx = 1;
			// Bodge to get ticket required checkbox to appear at correct place
			// (in-line with secure court)
			gbc.anchor = GridBagConstraints.NORTHEAST;
			gbc.fill = GridBagConstraints.NONE;
			newInsets = new Insets(4, 0, 0, -21);
			gbc.insets = newInsets;
			gbc.gridwidth = 1;
			transferAndTicketPanel.add(chckbxTicketRequired, gbc);

			gbc.gridwidth = 4;
			gbc.gridheight = 2;
			gbc.weightx = 1;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			JPanel ticketPanel = new JPanel();
			ticketPanel.setLayout(new GridBagLayout());
			ticketPanel.setBorder(BorderFactory.createTitledBorder("Ticket Required"));
			transferAndTicketPanel.add(ticketPanel, gbc);

			GridBagConstraints ticketPanelGbc = CaseMethods.getDefaultGridBagConstraints();
			ticketPanelGbc.fill = GridBagConstraints.HORIZONTAL;
			ticketPanelGbc.weighty = 0.2;
			ticketPanelGbc.weightx = 0.2;
			ticketPanelGbc.gridy++;
			ticketPanel.add(lblTicketType, ticketPanelGbc);

			ticketPanelGbc.gridx++;
			ticketPanelGbc.weightx = 0.8;
			ticketPanelGbc.gridwidth = 1;
			ticketPanel.add(cmbTicketType, ticketPanelGbc);

			ticketPanelGbc.gridy--;
			ticketPanelGbc.weighty = 0.1;
			ticketPanelGbc.insets = XHIBITConstant.errorLabelInsets;
			ticketPanel.add(lbITicketType, ticketPanelGbc);

			gbc.gridx--;
			transferAndTicketPanel.add(ticketPanel, gbc);

			gbc.gridy += 2;
			gbc.gridx = 0;
			gbc.gridwidth = 4;
			gbc.insets = new Insets(5, 0, 15, 0);
			transferAndTicketPanel.add(getPoliceForcePanel(), gbc);

			CaseMethods.addChangeListeners(ticketPanel.getComponents(), caseX);
			CaseMethods.addChangeListeners(transferDetailsPanelInner.getComponents(), caseX);
			CaseMethods.addChangeListeners(transferAndTicketPanel.getComponents(), caseX);
		}

		return transferAndTicketPanel;
	}

	private JPanel getMagistratesCourtDetailsPanel() {
		if (magistratesCourtDetailsPanel == null) {
			magistratesCourtDetailsPanel = new JPanel();
			magistratesCourtDetailsPanel.setLayout(new GridBagLayout());
			magistratesCourtDetailsPanel.setBorder(BorderFactory.createTitledBorder("Magistrates Court Details"));
			GridBagConstraints gbc = CaseMethods.getDefaultGridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;

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
			magistratesCourtDetailsPanel.add(lbIReceivedFrom1, gbc);

			gbc.gridx++;
			gbc.weightx = 0.75;
			magistratesCourtDetailsPanel.add(lbIReceivedFrom2, gbc);
			CaseMethods.addChangeListeners(magistratesCourtDetailsPanel.getComponents(), caseX);
		}
		return magistratesCourtDetailsPanel;
	}

	private JPanel getPoliceForcePanel() {
		if (policeForcePanel == null) {
			policeForcePanel = new JPanel();
			policeForcePanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = CaseMethods.getDefaultGridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;

			gbc.gridx++;
			policeForcePanel.add(lbIPoliceForce1, gbc);

			gbc.gridy++;
			gbc.gridx--;
			policeForcePanel.add(lblPoliceForce, gbc);

			gbc.gridx++;
			policeForcePanel.add(txtPoliceForce, gbc);

			gbc.gridx++;
			gbc.gridwidth = 2;
			policeForcePanel.add(cmbPoliceForce, gbc);

			CaseMethods.addChangeListeners(policeForcePanel.getComponents(), caseX);
		}
		return policeForcePanel;
	}

	public void setCaseNumber(String caseNumber) {
		if (lblCaseNumber != null)
			lblCaseNumber.setText(caseNumber);
	}

	

	public void clearChangedState() {
		fieldsChangedGlobal = false;
	}

	public Integer getNoOfDefendants() {
		return Integer.parseInt(txtNoOfDefendants.getText());
	}

	public int getNumDefendantsToAdd() {
		if (Pattern.matches("[0-9]+", txtNoOfDefendants.getText())) {
			return Integer.parseInt(txtNoOfDefendants.getText());
		} else {
			return -1;
		}
	}

	public void populateReceiptType(CaseBasicValue caseBasicValue) {
		if (caseBasicValue.getReceiptType() != null) {
			for (int i = 1; i < receiptTypes.size(); i++) {
				if (receiptTypes.get(i).getCode().equals(caseBasicValue.getReceiptType())) {
					cmbReceiptType.setSelectedItem(receiptTypes.get(i));
					previousReceiptType = receiptTypes.get(i);
					break;
				}
			}
			setFieldsBasedOnReceipt(caseBasicValue.getReceiptType());
		}
	}
	
	/**
	 * Sets mandatory fields, enables or disable fields depending on
	 * what the receipt type is.
	 * @param caseBasicValue
	 */
	public void setFieldsBasedOnReceipt(String code) {
		if (code.equals("BB")) {
			txtOrigCaseNo.setEnabled(isEnabled());
			txtReceivedFrom.setMandatory(false);
			txtReceivedFrom.clearError();
			
			CaseMethods.addRemoveFromMandatoryFields(txtReceivedFrom, lblReceivedFrom, mandatoryFields, false);
			
			txtReceivedFrom.setEnabled(false);
			cmbReceivedFrom.setMandatory(false);
			cmbReceivedFrom.clearError();
			
			if (!chckbxTransferIn.isSelected() && (txtOrigCaseNo.hasError())) {
				txtOrigCaseNo.clearError();
			}

			CaseMethods.addRemoveFromMandatoryFields(cmbReceivedFrom, null, mandatoryFields, false);

			cmbReceivedFrom.setSelectedIndex(0);
			cmbReceivedFrom.setEnabled(false);
			txtReceivedFrom.setText("");
			
		} else {
			if (!chckbxTransferIn.isSelected()) {
				txtOrigCaseNo.setEnabled(false);
				txtOrigCaseNo.setText("");
				txtReceivedFrom.setMandatory(true);
				txtOrigCaseNo.clearError();
			}

			CaseMethods.addRemoveFromMandatoryFields(txtReceivedFrom, lblReceivedFrom, mandatoryFields, true);

			cmbReceivedFrom.setMandatory(true);
			CaseMethods.addRemoveFromMandatoryFields(cmbReceivedFrom, null, mandatoryFields, true);

			cmbReceivedFrom.setEnabled(true);
			txtReceivedFrom.setEnabled(true);					
		}
	}

	/**
	 * The traversal policy used on sentence tab, called when sentence tab is
	 * displayed (from CaseXPanel).
	 * 
	 * @return FocusTraversalOnArray - all components used in traversal.
	 */
	public FocusTraversalOnArray getTabbedPaneOrder() {

		return new FocusTraversalOnArray(new Component[] { txtCaseTitle, dtDateReceived, cmbReceiptType,
				txtNoOfDefendants, dtDateOfCommittal, cmbReceivingSite, txtReceivedFrom, cmbReceivedFrom,
				cmbCivilUnrest,	chckbxTransferIn, dtTransferDate, cmbTransferFrom, txtOrigCaseNo, cmbMonitoringCategory,
				chckbxSecureCourt, chckbxTicketRequired, cmbTicketType, txtPoliceForce, cmbPoliceForce,
				caseX.getCreateButton(), caseX.getFinishButton(), caseX.getCancelButton() });
	}

	/**
	 * Needs to be set to know whether we can change the receipt type or not during amend
	 * @param caseId
	 * @throws ChargeControllerException 
	 */
	public void setCharges(Integer caseId) throws ChargeControllerException {
		
		ChargeCompositeValue ccv = XhibitDelegateHelper.getChargeDelegate().getCharges(caseId, true);
		if(ccv!=null && ccv.getCharges()!=null) {
			numOfCharges = ccv.getCharges().size();
		} else {
			numOfCharges = 0;
		}
	}
	
	/**
	 * Called when something changes in the date received field.
	 *
	 */
	public class DateReceivedListener implements MFieldListener {
		@Override
		public void fieldEntered(FocusEvent event) {
			//Don't want to do anything on field entered so leaving blank.
		}

		@Override
		public void fieldExited(FocusEvent event) {
			// Check mandatory fields to enable create button
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
			// Validate other fields that depend on this, i.e date date of
			// committal & transfer date
			if (dtDateReceived.isDateValidate()) {
				dtDateOfCommittal.crossValidateDate();

				// Transfer date must be equal to or before date received
				// AND equal to or after date of committal
				dtTransferDate.setToCompare(dtDateReceived);
				dtTransferDate.setBeforeOrAfter(CaseMaintenanceConstants.BEFORE);
				dtTransferDate.setCustomWarning(CaseMaintenanceConstants.ON_BEFORE_DATE_RECEIVED);
				dtTransferDate.crossValidateDate();

				// If date received condition isnt satisfied then test
				// other, else dont erase warning message
				if (!dtTransferDate.hasError()) {
					dtTransferDate.setToCompare(dtDateOfCommittal);
					dtTransferDate.setBeforeOrAfter(CaseMaintenanceConstants.AFTER);
					dtTransferDate.setCustomWarning(CaseMaintenanceConstants.ON_AFTER_DATE_OF_COMMITTAL);
					dtTransferDate.crossValidateDate();
				}
			}
		}
	}
	
	/**
	 * Called when anything changes in date of committal field.
	 */
	public class DateOfCommittalListener implements MFieldListener {
		@Override
		public void fieldEntered(FocusEvent event) {
			//Don't want to do anything on field entered so leaving blank.
		}

		@Override
		public void fieldExited(FocusEvent event) {
			// Check mandatory fields to enable create button
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
			// Validate other fields that depend on this, i.e. transfer date
			if (dtDateOfCommittal.isDateValidate()) {
				// Transfer date must be equal to or before date received
				// AND equal to or after date of committal
				dtTransferDate.setToCompare(dtDateOfCommittal);
				dtTransferDate.setBeforeOrAfter(CaseMaintenanceConstants.AFTER);
				dtTransferDate.setCustomWarning(CaseMaintenanceConstants.ON_AFTER_DATE_OF_COMMITTAL);
				dtTransferDate.crossValidateDate();

				// If date of committal condition isnt satisfied then test
				// other, else dont erase warning message
				if (!dtTransferDate.hasError()) {
					dtTransferDate.setToCompare(dtDateReceived);
					dtTransferDate.setBeforeOrAfter(CaseMaintenanceConstants.BEFORE);
					dtTransferDate.setCustomWarning(CaseMaintenanceConstants.ON_BEFORE_DATE_RECEIVED);
					dtTransferDate.crossValidateDate();
				}
			}
		}

	}
	
	public class ReceivedFromFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			//don't want to do anything on focus gained so leaving
			//blank.
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
							lbIReceivedFrom2.setText(CaseMaintenanceConstants.OBSOLETE_COURT);
							cmbReceivedFrom.setSelectedIndex(i);
							txtReceivedFrom.setError(CaseMaintenanceConstants.OBSOLETE_COURT);
						} else {
							lbIReceivedFrom2.setText(" ");
							lbIReceivedFrom1.setText(" ");
							cmbReceivedFrom.setSelectedIndex(i);								
						}
						break;
					}
				}
				if (!found) {
					txtReceivedFrom.setError();
					cmbReceivedFrom.setSelectedIndex(0);
					lbIReceivedFrom1.setText("Invalid Entry");
				} 
			}
		}
	}
	
	/**
	 * Used when something changes in the recevied from dropdown.
	 */
	public class ReceivedFromDropdownFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			//don't want to do anything on focus gained so leaving
			//blank.
		}

		@Override
		public void focusLost(FocusEvent e) {
			if(cmbReceivedFrom.getSelectedIndex()>0) {
				//if its an absolete court then show error 
				if(((RefCourtBasicValue)cmbReceivedFrom.getSelectedItem()).getObsInd()!=null &&
	((RefCourtBasicValue)cmbReceivedFrom.getSelectedItem()).getObsInd().equalsIgnoreCase("Y")) {
					lbIReceivedFrom2.setText(CaseMaintenanceConstants.OBSOLETE_COURT);
					lbIReceivedFrom1.setText(CaseMaintenanceConstants.OBSOLETE_COURT);
					cmbReceivedFrom.setError(true);
				} else {
					lbIReceivedFrom2.setText(" ");
					lbIReceivedFrom1.setText(" ");
					cmbReceivedFrom.setError(false);

				}
			}
		}

	}

	
	public class ReceiptTypeListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			//if its case amend and has any charges or offences show a pop up and don't change the value.
			if (caseX.getXac().getCaseStatus().getCaseProcess() == CaseProcess.AMEND 
					&& numOfCharges>0 
					&& previousReceiptType != cmbReceiptType.getSelectedItem()) {
				JOptionPane.showMessageDialog(caseX.getXac(), "Existing charges/breaches for this case have to be deleted first.");	
				cmbReceiptType.setSelectedItem(previousReceiptType);
			}
			else {
				// Changes made for CTX-1607
				RefSystemCodeBasicValue value = (RefSystemCodeBasicValue) cmbReceiptType.getSelectedItem();
				if(value.getCode()!=null) {
					setFieldsBasedOnReceipt(value.getCode());
				}	
			}
		}
	}
	
	/**
	 * Called when transfer in checkbox is ticked/unticked
	 */
	public class TransferInListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			String value = ((RefSystemCodeBasicValue) cmbReceiptType.getSelectedItem()).getDecode();
			if (chckbxTransferIn.isSelected()) { // checked
				// enable orig case no & make mandatory
				txtOrigCaseNo.setEnabled(true);

				// enable transfer date & transfer from & make mandatory
				dtTransferDate.setEnabledAndFocusable(true);
				dtTransferDate.setRequired(true);
				CaseMethods.addRemoveFromMandatoryFields(dtTransferDate, lblTransferDate, mandatoryFields, true);

				cmbTransferFrom.setEnabled(true);
				cmbTransferFrom.setMandatory(true);
				
				CaseMethods.addRemoveFromMandatoryFields(cmbTransferFrom, lblTransferFrom, mandatoryFields, true);

			} else { // unchecked
				if (!value.equals("BRING BACK")) {
					// disable orig case no, clear it
					txtOrigCaseNo.setText("");
					txtOrigCaseNo.setEnabled(false);
					txtOrigCaseNo.clearError();
				} else {
					// enable orig case no
					txtOrigCaseNo.setEnabled(true);
				}

				// disable transfer date & transfer from & reset
				cmbTransferFrom.setEnabled(false);
				cmbTransferFrom.setMandatory(false);
				cmbTransferFrom.setSelectedIndex(0);
				cmbTransferFrom.clearError();
				
				CaseMethods.addRemoveFromMandatoryFields(cmbTransferFrom, lblTransferFrom, mandatoryFields, false);


				dtTransferDate.setEnabledAndFocusable(false);
				dtTransferDate.setRequired(false);
				dtTransferDate.clear();
				dtTransferDate.clearError();
				lblITransferDate.setText(" ");
				CaseMethods.addRemoveFromMandatoryFields(dtTransferDate, lblTransferDate, mandatoryFields, false);

			}

			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
		}
	}
	
	/**
	 * Called when transfer date is changed.
	 */
	public class TransferDateListener implements MFieldListener {
		@Override
		public void fieldEntered(FocusEvent event) {
			//Don't want to do anything on field entered so leaving blank.
		}

		@Override
		public void fieldExited(FocusEvent event) {
			// Check mandatory fields to enable create button
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);

			if (dtTransferDate.isDateValidate()) {
				// Transfer date must be equal to or before date received
				// AND equal to or after date of committal
				// Work around as XDateField only allows one date comparison
				dtTransferDate.setToCompare(dtDateOfCommittal);
				dtTransferDate.setBeforeOrAfter(CaseMaintenanceConstants.AFTER);
				dtTransferDate.setCustomWarning(CaseMaintenanceConstants.ON_AFTER_DATE_OF_COMMITTAL);
				dtTransferDate.crossValidateDate();

				// If date of committal condition isnt satisfied then test
				// other, else dont erase warning message
				if (!dtTransferDate.hasError()) {
					dtTransferDate.setToCompare(dtDateReceived);
					dtTransferDate.setBeforeOrAfter(CaseMaintenanceConstants.BEFORE);
					dtTransferDate.setCustomWarning(CaseMaintenanceConstants.ON_BEFORE_DATE_RECEIVED);
					dtTransferDate.crossValidateDate();
				}
			}
		}
	}
	
	/**
	 * Called when ticket required is ticked.
	 */
	public class TicketRequiredListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (chckbxTicketRequired.isSelected()) {
				cmbTicketType.setEnabled(true);
				cmbTicketType.setMandatory(true);

				CaseMethods.addRemoveFromMandatoryFields(cmbTicketType, lblTicketType, mandatoryFields, true);

				if (cmbTicketType.getSelectedIndex() != 0) {
					lbITicketType.setText(" ");
					cmbTicketType.setError(false);
				} else {
					lbITicketType.setText("Mandatory Field");
					cmbTicketType.setError(true);
				}
			} else {
				cmbTicketType.setSelectedIndex(0);
				cmbTicketType.setEnabled(false);
				cmbTicketType.setMandatory(false);
				lbITicketType.setText(" ");
				cmbTicketType.setError(false);
				
				CaseMethods.addRemoveFromMandatoryFields(cmbTicketType, lblTicketType, mandatoryFields, false);

			}
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
		}
	}
	
	/**
	 * Called when anything is typed in the police force box.
	 */
	public class PoliceForceListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
			if (txtPoliceForce.getText() != null) {
				populateDropdown(txtPoliceForce.getText());
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (txtPoliceForce.getText() != null) {
				populateDropdown(txtPoliceForce.getText());
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (txtPoliceForce.getText() != null) {
				populateDropdown(txtPoliceForce.getText());
			}

		}

		void populateDropdown(String value) {
			boolean found = false;
			for (int i = 0; i < policeForceCodes.size(); i++) {
				if (policeForceCodes.get(i).getCode().equalsIgnoreCase(value)) {
					found = true;
					cmbPoliceForce.setSelectedIndex(i);
				}
			}
			if (!found) {
				txtPoliceForce.setError();
			}
		}
	}
	
	/**
	 * Called when something is typed in the received from text box.
	 */
	public class ReceivedFromKeyListener implements KeyListener {
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
					lbIReceivedFrom1.setText("Mandatory Field");
				}
			}

		}

		void populateDropdown(String value) {
			boolean found = false;
			for (int i = 0; i < receivedFromValues.size(); i++) {
				if (!receivedFromValues.get(i).getCourtFullName().equals("Select Magistrates Court")
						&& receivedFromValues.get(i).getCourtShortName().equalsIgnoreCase(value)) {
					found = true;
					lbIReceivedFrom2.setText(" ");
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
