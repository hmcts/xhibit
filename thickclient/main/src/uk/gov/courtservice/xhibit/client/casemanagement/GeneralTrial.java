package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
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
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.text.Document;

import org.apache.log4j.Logger;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import mseries.Calendar.MFieldListener;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.StringUtil;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ChargesLogBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefMonitoringCategoryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefusedBroadcastCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.casemanagement.util.CaseMethods;
import uk.gov.courtservice.xhibit.client.casemanagement.util.NoOfDefendantsListener;
import uk.gov.courtservice.xhibit.client.util.CaseMaintenanceConstants;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XCheckBox;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTextArea;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * @author C.Kudzin - Feb 21, 2018 - Implemented MaxLength on text fields
 * @author C.Kudzin - Feb 27, 2018 - Implemented Date validation - ctx-1395
 *
 */
public class GeneralTrial extends GeneralAllCasesTab {
	private static final long serialVersionUID = 1L;
	private static final String EMPTY_STRING = "";
	private static final int CHARGE_LOG_MAX_LENGTH = 80;
	private final Logger log = CSServices.getLogger(getClass());

	Calendar todaysDate = null;

	private JLabel lblCaseNumber;
	private Integer version;
	private JScrollPane refusedOtherScroll;
	// xtextfields
	private XTextField txtCaseTitle;
	private XTextField txtNoOfDefendants;
	private XTextField txtReceivedFrom;
	private XTextField txtPoliceForce;
	private XTextField txtOrigCaseNo;
	private XTextField txtPagesOfEvidence;

	// xcomboboxes
	private XComboBox cmbClassOfCase;
	private XComboBox cmbMonitoringCategory;
	private XComboBox cmbReceivingSite;
	private XComboBox cmbReceiptType;
	private XComboBox cmbEitherWayType;
	private XComboBox cmbReceivedFrom;
	private XComboBox cmbPoliceForce;
	private XComboBox cmbTicketType;
	private XComboBox cmbTransferFrom;
	private XTextField txtReceiptType;

	// xdatepanel
	private XDatePanel dtDateReceived;
	private XDatePanel dtSentForTrial;
	private XDatePanel dtDateOfCommittal;
	private XDatePanel dtTransferDate;
	private XDatePanel dtFirstHearingDate;
	private XDatePanel dtAppMade;

	private JTextArea txtCharges;
	private XTextArea txtRefusedOther;
	
	private JButton btnSearchCharges;

	// xcheckbox
	private XCheckBox chckbxRetrial;
	private XCheckBox chckbxSecureCourt;
	private XCheckBox chckbxTicketRequired;
	private XCheckBox chckbxTransferIn;
	private XCheckBox chckbxAppMade;
	private XCheckBox chckbxDefenceRep;
	private XCheckBox chckbxProsRep;
	private XCheckBox chckbxCaseUnsuitable;
	private XCheckBox chckbxAppLate;
	private XCheckBox chckbxLikelyDisruption;
	private XCheckBox chckbxOther;
	
	private ButtonGroup appGrantedBg = null;
	private JRadioButton appYesRb = null;
	private JRadioButton appNoRb = null;
	private JRadioButton appNARb = null;

	private ButtonGroup senRemarksFilmedBg = null;
	private JRadioButton senRemarksFilmedYesRb = null;
	private JRadioButton senRemarksFilmedNoRb = null;
	private JRadioButton senRemarksFilmedNARb = null;

	private JLabel lblISentForTrial;
	private JLabel lblIDateOfCommittal;
	private JLabel lblIPagesOfEvidence;
	private JLabel lblIOrigCaseNo;
	private JLabel lblIEitherWayType;
	private JLabel lblIReceivedFrom2;
	private JLabel lblITicketType;
	private JLabel lblIdtAppMade;
	private JLabel lblIRefusedOther;

	// rework for implementing grid bag
	private JLabel lblCaseTitle;
	private JLabel lblCaseTitleError;
	private JLabel lblDateReceived;
	private JLabel lblIDateReceived;
	private JLabel lblEitherWayType;
	private JLabel lblIReceiptType;
	private JLabel lblReceiptType;
	private JLabel lblNoOfDefendants;
	private JLabel lblINoOfDefendants;
	private JLabel lblSentForTrial;
	private JLabel lblDateOfCommittal;
	private JLabel lblIReceivingSite;
	private JLabel lblReceivingSite;
	private JLabel lblIReceivedFrom;
	private JLabel lblReceivedFrom;
	private JLabel lblITransferDate;
	private JLabel lblTransferDate;
	private JLabel lblOrigCaseNo;
	private JLabel lblTransferFrom;
	private JLabel lblITransferFrom;
	private JLabel lblPagesOfEvidence;
	private JLabel lblIFirstHearingDate;
	private JLabel lblFirstHearingDate;
	private JLabel lblIMonitoringCategory;
	private JLabel lblMonitoringCategory;
	private JLabel lblIClassOfCase;
	private JLabel lblClassOfCase;
	private JLabel lblSecureCourt;
	private JLabel lblTicketType;
	private JLabel lblIPoliceForce;
	private JLabel lblIPoliceForce2;
	private JLabel lblPoliceForce;
	private JLabel lblNewLabel;
	private JLabel lblAppMade;
	private JLabel lblAppGranted;
	private JLabel lblSenRemarksFilmed;

	private JPanel caseDetailsPanel;
	private JPanel magistratesCourtDetailsPanel;
	private JPanel transferDetailsPanel;
	private JPanel rightPanel;
	private JPanel policeForcePanel;
	private JPanel chargesPanel;
	private JPanel televisedPanel;
	private JPanel refusedReasonPanel;

	private JScrollPane chargesScrollPane;

	// parent panel
	private CaseXPanel caseX;
	private ArrayList<RefCourtBasicValue> receivedFromValues;
	private ArrayList<RefSystemCodeBasicValue> policeForceCodes;
	private ArrayList<CourtSiteBasicValue> receivingSite;
	private ArrayList<RefMonitoringCategoryBasicValue> monitoringCategory;
	private ArrayList<RefSystemCodeBasicValue> ticketTypeCodes;
	private ArrayList<RefSystemCodeBasicValue> receiptTypes;
	private ArrayList<RefSystemCodeBasicValue> broadcastTypes;

	// array of all the invalid entry fields
	private List<JLabel> validationFields = new ArrayList<JLabel>();
	private List<Object> mandatoryFields = new ArrayList<Object>();

	private boolean fieldsChangedGlobal = false;

	/**
	 * Used for logging and exception handling.
	 */
	private static final String CLASS_NAME = ".GeneralTrial";
	
	
	private static final String DEFENCE_REPRESENTATION = "DR";
	private static final String PROSECUTION_REPRESENTATION = "PR";
	private static final String UNSUITABLE_FOR_BROADCAST = "UB";
	private static final String APPLICATION_LATE = "AL";
	private static final String LIKELY_DISRUPTION = "LD";
	private static final String OTHER = "O";

	public GeneralTrial(final CaseXPanel caseX) {
		// check if receivedFromValues is null, if not then populate it
		initDropdownTypes();
		initCivilUnrestPanel(validationFields);

		lblCaseNumber = new JLabel("Case Number");
		lblCaseNumber.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCaseNumber.setBorder(new LineBorder(new Color(0, 0, 0)));

		lblCaseTitle = new JLabel("Case Title   R-v-");
		lblCaseTitleError = CaseMethods.createErrorLabel(validationFields);

		txtCaseTitle = new XTextField(72, "^.{1,72}$", lblCaseTitleError, true);
		txtCaseTitle.setDisabledTextColor(Color.BLACK);
		txtCaseTitle.setGridBagLayout(true);
		txtCaseTitle.setUpperCase(true);
		txtCaseTitle.setMaxLength(72);
		
		CaseMethods.addRemoveFromMandatoryFields(txtCaseTitle, lblCaseTitle, mandatoryFields, true);

		txtCaseTitle.addFocusListener(new MandatoryFieldsFocusListener());
		txtCaseTitle.setColumns(10);
		txtCaseTitle.setMinimumSize(txtCaseTitle.getPreferredSize());

		lblDateReceived = new JLabel(CaseMaintenanceConstants.DATE_RECEIVED);

		lblIDateReceived = CaseMethods.createErrorLabel(validationFields);

		dtDateReceived = new XDatePanel(this, Calendar.getInstance(), true, lblIDateReceived, CaseMaintenanceConstants.BEFORE);
		dtDateReceived.setGridBagLayout(true);
		
		CaseMethods.addRemoveFromMandatoryFields(dtDateReceived, lblDateReceived, mandatoryFields, true);

		dtDateReceived.setFocusable(true);
		dtDateReceived.getDateComponent().addMFieldListener(new DateReceivedListener());

		lblIEitherWayType = CaseMethods.createErrorLabel(validationFields);

		lblEitherWayType = new JLabel("Either Way Type");

		Vector<String> eitherWayTypeValues = ResourceBundleHelper.getResourcesStartingWith(
				ResourceBundleHelper.getResourceBundle(XhibitBundles.CaseMaintenanceResources),
				"generalTrial.eitherWayType");

		cmbEitherWayType = new XComboBox(true, lblIEitherWayType);
		cmbEitherWayType.setGridBagLayout(true);
		// Added nullguards as if accessed from court other than snaresbrook
		// there's no data to populate, ctx-1428
		if (!eitherWayTypeValues.isEmpty()) {
			cmbEitherWayType.setModel(new DefaultComboBoxModel(
					createDropdownValues("Select Either Way Type", eitherWayTypeValues).toArray()));
			cmbEitherWayType.setRenderer(new DropdownBoxCellRender());
		}
		cmbEitherWayType.addFocusListener(new MandatoryFieldsFocusListener());
		cmbEitherWayType.setEnabled(false);

		lblIReceiptType = CaseMethods.createErrorLabel(validationFields);

		lblReceiptType = new JLabel("Receipt Type");

		cmbReceiptType = new XComboBox(true, lblIReceiptType);
		cmbReceiptType.setGridBagLayout(true);
		
		CaseMethods.addRemoveFromMandatoryFields(cmbReceiptType, lblReceiptType, mandatoryFields, true);

		cmbReceiptType.addFocusListener(new MandatoryFieldsFocusListener());
		// Added null guards as if accessed from court other than snaresbrook
		// there's no data to populate, ctx-1428
		if (!receiptTypes.isEmpty()) {
			cmbReceiptType.setModel(new DefaultComboBoxModel(receiptTypes.toArray()));
			cmbReceiptType.setRenderer(new DropdownBoxCellRender());
		}

		txtReceiptType = new XTextField(2, "^.$", lblIReceiptType, true);
		txtReceiptType.setGridBagLayout(true);
		txtReceiptType.setEnabled(false);
		txtReceiptType.setVisible(false);
		if (caseX.getXac().getCaseStatus().getCaseProcess() == CaseProcess.AMEND) {
			cmbReceiptType.setEnabled(false);
			cmbReceiptType.setVisible(false);
			
			CaseMethods.addRemoveFromMandatoryFields(cmbReceiptType, null, mandatoryFields, false);
			txtReceiptType.setVisible(true);
		}

		cmbReceiptType.addItemListener(new ReceiptTypeItemListener());

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

		lblSentForTrial = new JLabel("Sent For Trial");

		lblISentForTrial = CaseMethods.createErrorLabel(validationFields);

		dtSentForTrial = new XDatePanel(this, null, true, lblISentForTrial, CaseMaintenanceConstants.BEFORE, dtDateReceived, "SFT",
				CaseMaintenanceConstants.DATE_RECEIVED);
		dtSentForTrial.setGridBagLayout(true);
		
		CaseMethods.addRemoveFromMandatoryFields(dtSentForTrial, lblSentForTrial, mandatoryFields, true);

		dtSentForTrial.setFocusable(true);
		dtSentForTrial.setCustomWarning(CaseMaintenanceConstants.ON_BEFORE_DATE_RECEIVED);
		dtSentForTrial.getDateComponent().addMFieldListener(new DateSentForTrialListener());

		lblDateOfCommittal = new JLabel("Date of Committal");
		lblIDateOfCommittal = CaseMethods.createErrorLabel(validationFields);

		dtDateOfCommittal = new XDatePanel(this, todaysDate, true, lblIDateOfCommittal, CaseMaintenanceConstants.BEFORE, dtDateReceived,
				"Date of Committal", CaseMaintenanceConstants.DATE_RECEIVED);
		dtDateOfCommittal.setGridBagLayout(true);
		dtDateOfCommittal.setEnabledAndFocusable(false);
		CaseMethods.addRemoveFromMandatoryFields(dtDateOfCommittal, null, mandatoryFields, true);

		dtDateOfCommittal.setCustomWarning(CaseMaintenanceConstants.ON_BEFORE_DATE_RECEIVED);
		dtDateOfCommittal.getDateComponent().addMFieldListener(new DateOfCommittalListener());

		lblIReceivingSite = CaseMethods.createErrorLabel(validationFields);

		lblReceivingSite = new JLabel("Receiving Site");

		cmbReceivingSite = new XComboBox(false, lblIReceivingSite);
		cmbReceivingSite.setGridBagLayout(true);
		// Added nullguards as if accessed from court other than snaresbrook
		// there's no data to populate, ctx-1428
		if (!receivingSite.isEmpty()) {
			cmbReceivingSite.setModel(new DefaultComboBoxModel(receivingSite.toArray()));
			cmbReceivingSite.setRenderer(new DropdownBoxCellRender());
		}

		lblIReceivedFrom = CaseMethods.createErrorLabel(validationFields);

		lblIReceivedFrom2 = new JLabel(" ");
		lblIReceivedFrom2.setForeground(Color.RED);

		lblReceivedFrom = new JLabel("Received From*");

		txtReceivedFrom = new XTextField(5, "^[a-zA-Z0-9]{1,5}$", lblIReceivedFrom, true);
		txtReceivedFrom.setGridBagLayout(true);
		txtReceivedFrom.setUpperCase(true);
		txtReceivedFrom.setMaxLength(5);
		// add a on key press
		txtReceivedFrom.addKeyListener(new ReceivedFromKeyListener());
		txtReceivedFrom.addFocusListener(new ReceivedFromFocusListener()); 
		
		CaseMethods.addRemoveFromMandatoryFields(txtReceivedFrom, lblReceivedFrom, mandatoryFields, true);

		txtReceivedFrom.addFocusListener(new MandatoryFieldsFocusListener());
		txtReceivedFrom.setColumns(10);
		txtReceivedFrom.setMinimumSize(txtReceivedFrom.getPreferredSize());

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
		cmbReceivedFrom.addFocusListener(new ReceivedFromComboFocusListener()); 

		chckbxTransferIn = new XCheckBox();
		chckbxTransferIn.addItemListener(new ChckbxTransferListener());
		

		chckbxTransferIn.setEnabled(true);
		chckbxTransferIn.setHorizontalTextPosition(SwingConstants.LEFT);

		lblITransferDate = CaseMethods.createErrorLabel(validationFields);

		lblTransferDate = new JLabel("Transfer Date");

		dtTransferDate = new XDatePanel(this, todaysDate, false, lblITransferDate, CaseMaintenanceConstants.BEFORE, dtDateReceived,
				"Transfer Date", CaseMaintenanceConstants.DATE_RECEIVED);
		dtTransferDate.setGridBagLayout(true);
		dtTransferDate.setDateEnabled(false);
		dtTransferDate.setEnabledAndFocusable(false);
		dtTransferDate.setCustomWarning(CaseMaintenanceConstants.ON_BEFORE_DATE_RECEIVED);
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
		
		chckbxRetrial = new XCheckBox();
		chckbxRetrial.addItemListener(new RetrialListener()); 
		chckbxRetrial.setHorizontalTextPosition(SwingConstants.LEFT);

		lblPagesOfEvidence = new JLabel("Pages of Evidence");

		lblIPagesOfEvidence = CaseMethods.createErrorLabel(validationFields);

		txtPagesOfEvidence = new XTextField(8, "^[0-9]{1,8}$", lblIPagesOfEvidence, false);
		txtPagesOfEvidence.setGridBagLayout(true);
		txtPagesOfEvidence.setMaxLength(8);
		txtPagesOfEvidence.setEnabled(false);
		txtPagesOfEvidence.setNumeric(true);
		txtPagesOfEvidence.addFocusListener(new MandatoryFieldsFocusListener());
		txtPagesOfEvidence.setColumns(10);
		txtPagesOfEvidence.setMinimumSize(txtPagesOfEvidence.getPreferredSize());

		lblIFirstHearingDate = CaseMethods.createErrorLabel(validationFields);

		lblFirstHearingDate = new JLabel("First Hearing Date");

		dtFirstHearingDate = new XDatePanel(this, todaysDate, false, lblIFirstHearingDate, CaseMaintenanceConstants.AFTER, dtDateReceived,
				"First hearing date", CaseMaintenanceConstants.DATE_RECEIVED);
		dtFirstHearingDate.setGridBagLayout(true);
		dtFirstHearingDate.setCustomWarning("Must be on/after Date Received");

		lblIMonitoringCategory = CaseMethods.createErrorLabel(validationFields);

		lblMonitoringCategory = new JLabel("Monitoring Category");

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

		lblIClassOfCase = CaseMethods.createErrorLabel(validationFields);

		lblClassOfCase = new JLabel("Class of Case*");

		Vector<String> classOfCase = ResourceBundleHelper.getResourcesStartingWith(
				ResourceBundleHelper.getResourceBundle(XhibitBundles.CaseMaintenanceResources),
				"generalTrial.classOfCase");

		cmbClassOfCase = new XComboBox(false, lblIClassOfCase);
		cmbClassOfCase.setGridBagLayout(true);
		if (!classOfCase.isEmpty()) {
			cmbClassOfCase.setModel(new DefaultComboBoxModel(classOfCase.toArray()));
		}
		cmbClassOfCase.setSelectedItem("3");

		lblSecureCourt = new JLabel("Secure Court");

		chckbxSecureCourt = new XCheckBox();
		chckbxSecureCourt.setHorizontalTextPosition(SwingConstants.LEFT);

		chckbxTicketRequired = new XCheckBox();
		chckbxTicketRequired.setHorizontalTextPosition(SwingConstants.LEFT);
		chckbxTicketRequired.setFocusable(true);

		lblITicketType = CaseMethods.createErrorLabel(validationFields);

		lblTicketType = new JLabel("Ticket Type");

		cmbTicketType = new XComboBox(true, lblITicketType);
		cmbTicketType.setGridBagLayout(true);
		// Added nullguards as if accessed from court other than snaresbrook
		// there's no data to populate, ctx-1428
		if (!ticketTypeCodes.isEmpty()) {
			cmbTicketType.setModel(new DefaultComboBoxModel(ticketTypeCodes.toArray()));
			cmbTicketType.setRenderer(new DropdownBoxCellRender());
		}
		cmbTicketType.addFocusListener(new MandatoryFieldsFocusListener());
		cmbTicketType.setEnabled(false);

		chckbxTicketRequired.addItemListener(new TicketRequiredListener());

		lblIPoliceForce = CaseMethods.createErrorLabel(validationFields);

		lblIPoliceForce2 = new JLabel(" ");
		lblIPoliceForce2.setForeground(Color.RED);

		lblPoliceForce = new JLabel("Police Force");

		txtPoliceForce = new XTextField(2, "^[0-9]{1,2}$", lblIPoliceForce, true);
		txtPoliceForce.setGridBagLayout(true);
		txtPoliceForce.setUpperCase(true);
		txtPoliceForce.setMaxLength(2);
		txtPoliceForce.setColumns(10);
		txtPoliceForce.setMinimumSize(txtPoliceForce.getPreferredSize());
		txtPoliceForce.addKeyListener(new PoliceForceListener());
		
		CaseMethods.addRemoveFromMandatoryFields(txtPoliceForce, lblPoliceForce, mandatoryFields, true);

		txtPoliceForce.addFocusListener(new MandatoryFieldsFocusListener());

		cmbPoliceForce = new XComboBox(false, lblIPoliceForce2, txtPoliceForce);
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
				populatePoliceForce(XhibitSingleton.getInstance().getCourtBasicValue().getPoliceForceCode());
			}
		} catch (CSRecoverableException e1) {
			log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e1);
			XHIBITConstant.handleError(e1, this.getClass());
		}
		// 20 = Vertical scroll bar as needed, 31 is horizontal never
		chargesScrollPane = new JScrollPane(txtCharges, 20, 31);
		txtCharges = new JTextArea();
		txtCharges.setLineWrap(true);
		// Need this so it doesn't fill the entire panel the charges text area
		// is attached to
		txtCharges.setColumns(10);
		txtCharges.setRows(5);
		//set max chars to 4000
		
		ArrayList<Capability> capabilities = new ArrayList<Capability>();
			capabilities.add(Capability.limitedText(4000));
		
		 final Document doc = DocumentFactory.newDocument(capabilities.toArray(new Capability[capabilities.size()]));
		 txtCharges.setDocument(doc);
		
		txtCharges.setMaximumSize(txtCharges.getPreferredSize());
		// so that tabbing out goes to next button
		txtCharges.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
		txtCharges.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
		chargesScrollPane.setViewportView(txtCharges);

		CaseMethods.addChangeListeners(new Component[]{txtCharges}, caseX);


		btnSearchCharges = new JButton("Search Charges");
		XAction openSearchOffence = XhibitActions.getAction(caseX.getXac(), XhibitActions.OpenSearchOffence);
		openSearchOffence.setCaller(this);
		btnSearchCharges.setAction(openSearchOffence);

		lblNewLabel = new JLabel("CoA Re-Trial?");

		//New Televised case options
		lblAppMade = new JLabel("Application made?");
		chckbxAppMade = new XCheckBox();
		chckbxAppMade.addItemListener(new AppMadeListener());
		
		lblIdtAppMade = CaseMethods.createErrorLabel(validationFields);
		
		dtAppMade = new XDatePanel(this, null, true, lblIdtAppMade, CaseMaintenanceConstants.BEFORE);
		dtAppMade.setGridBagLayout(true);
		dtAppMade.getDateComponent().addMFieldListener(new MandatoryFieldsMFieldListener());

		lblAppGranted = new JLabel("Application granted?");
		appGrantedBg = new ButtonGroup();
		appYesRb = new JRadioButton("Yes",false);
		appYesRb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableRefusedCheckboxes(false);
			}
		});
		appNoRb = new JRadioButton("No",false);
		appNoRb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableRefusedCheckboxes(true);
			}
		});
		appNARb = new JRadioButton("N/A",false);
		appNARb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableRefusedCheckboxes(false);
			}
		});
		appGrantedBg.add(appYesRb);
		appGrantedBg.add(appNoRb);
		appGrantedBg.add(appNARb);
		
		chckbxDefenceRep = new XCheckBox("Defence Representations", false);
		chckbxProsRep = new XCheckBox("Prosecution Representations", false);
		chckbxCaseUnsuitable = new XCheckBox("Case Unsuitable for Broadcast", false);
		chckbxAppLate = new XCheckBox("Application Late", false);
		chckbxLikelyDisruption = new XCheckBox("Likely disruption to Court", false);
		chckbxOther = new XCheckBox("Other", false);
		lblIRefusedOther = new JLabel(" ");
		lblIRefusedOther.setForeground(Color.RED);
		
		chckbxOther.addItemListener(new OtherCheckboxListener());
	
		lblSenRemarksFilmed = new JLabel("Sentencing remarks filmed?");
		senRemarksFilmedBg = new ButtonGroup();
		senRemarksFilmedYesRb = new JRadioButton("Yes",false);
		senRemarksFilmedNoRb = new JRadioButton("No",false);
		senRemarksFilmedNARb = new JRadioButton("N/A",false);
		senRemarksFilmedBg.add(senRemarksFilmedYesRb);
		senRemarksFilmedBg.add(senRemarksFilmedNoRb);
		senRemarksFilmedBg.add(senRemarksFilmedNARb);
		
		
		refusedOtherScroll = new JScrollPane(txtRefusedOther, 20, 31);
		txtRefusedOther = new XTextArea(lblIRefusedOther, true, 280);
		txtRefusedOther.setLineWrap(true);
		txtRefusedOther.setGridBagLayout(true);
		// Need this so it doesn't fill the entire panel. 
		txtRefusedOther.setColumns(10);
		txtRefusedOther.setRows(5);
		
		txtRefusedOther.setMaximumSize(txtRefusedOther.getPreferredSize());
		txtRefusedOther.addFocusListener(new MandatoryFieldsFocusListener());

		// so that tabbing out goes to next button
		txtRefusedOther.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
		txtRefusedOther.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
		CaseMethods.addChangeListeners(new Component[]{txtRefusedOther}, caseX);
		refusedOtherScroll.setViewportView(txtRefusedOther);

		setInitialTelevisedFields(false);
		appNARb.setSelected(true);
		senRemarksFilmedNARb.setSelected(true);

		
		this.caseX = caseX;
		jbInit();
	}

	/**
	 * initialise all the array's used for the dropdowns
	 */
	protected void initDropdownTypes() {
		super.initDropdownTypes();
		if (receivedFromValues == null) {
			receivedFromValues = GeneralDropdownPopulation.getReceivedFrom();
		}
		if (policeForceCodes == null) {
			policeForceCodes = GeneralDropdownPopulation.getPoliceForceCode();
		}
		if (receivingSite == null) {
			receivingSite = GeneralDropdownPopulation.getReceivingSite();
		}
		if (monitoringCategory == null) {
			monitoringCategory = GeneralDropdownPopulation.getMonitoringCategory();
		}
		if (ticketTypeCodes == null) {
			ticketTypeCodes = GeneralDropdownPopulation.getTicketType();
		}
		if (receiptTypes == null) {
			receiptTypes = GeneralDropdownPopulation.getReceiptTypes(RefSystemCodeCriteria.CodeType.RECEIPT_TYPE_T);
		}
		
		if(broadcastTypes == null ) {
			broadcastTypes = GeneralDropdownPopulation.getBroadcastTypes();
		}		
	}

	/**
	 * Either enables or disabled the refused checkbox and txt area depending 
	 * on the boolean passed in.
	 * @param isEnabled
	 */
	protected void enableRefusedCheckboxes(final boolean isEnabled) {
		chckbxDefenceRep.setEnabled(isEnabled);
		chckbxProsRep.setEnabled(isEnabled);
		chckbxCaseUnsuitable.setEnabled(isEnabled);
		chckbxAppLate.setEnabled(isEnabled);
		chckbxLikelyDisruption.setEnabled(isEnabled);
		chckbxOther.setEnabled(isEnabled);
		if(!isEnabled) {
			chckbxDefenceRep.setSelected(isEnabled);
			chckbxProsRep.setSelected(isEnabled);
			chckbxCaseUnsuitable.setSelected(isEnabled);
			chckbxAppLate.setSelected(isEnabled);
			chckbxLikelyDisruption.setSelected(isEnabled);
			chckbxOther.setSelected(isEnabled);
			txtRefusedOther.setEditable(isEnabled);
			txtRefusedOther.setEnabled(isEnabled);
			txtRefusedOther.setText(null);
		}		
	}

	
	/**
	 * Method that sets the state of all the individual televised fields
	 * the first time you enter the screen.
	 */
	private void setInitialTelevisedFields(boolean isEnabled) {
		dtAppMade.setEnabledAndFocusable(isEnabled);
		appYesRb.setEnabled(isEnabled);
		appNoRb.setEnabled(isEnabled);
		appNARb.setEnabled(isEnabled);
		
		enableRefusedCheckboxes(isEnabled);

		senRemarksFilmedYesRb.setEnabled(isEnabled);
		senRemarksFilmedNoRb.setEnabled(isEnabled);
		senRemarksFilmedNARb.setEnabled(isEnabled);

		
	}
	
	protected void validateMandatoryFields() {
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

	public String getTicketRequired() {
		return chckbxTicketRequired.isSelected() ? "Y" : "N";
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
			if (c == null) {
				return null;
			} else {
				return new Timestamp(c.getTimeInMillis());
			}
		} else {
			log.error("Date of committal is incorrect ");
		}
		return null;
	}

	public Integer getNoOfDefendants() {
		return Integer.parseInt(txtNoOfDefendants.getText());
	}

	public String getReceiptType() {
		if (cmbReceiptType.isEnabled()) {
			return ((RefSystemCodeBasicValue) cmbReceiptType.getSelectedItem()).getCode();
		} else {
			return txtReceiptType.getText();
		}

	}

	public String getOrigCaseNumber() {
		return txtOrigCaseNo.isEnabled() ? txtOrigCaseNo.getText() : null;
	}

	public String getSecureCourt() {
		return chckbxSecureCourt.isSelected() ? "Y" : "N";
	}

	public Integer getRecFrom() {
		return ((RefCourtBasicValue) cmbReceivedFrom.getSelectedItem()).getId();
	}

	public Integer getPoliceForceCode() {
		return ((RefSystemCodeBasicValue) cmbPoliceForce.getSelectedItem()).getId();
	}

	public Integer getClassCode() {
		return Integer.parseInt(((String) cmbClassOfCase.getSelectedItem()));
	}

	public Timestamp getSentForTrial() {
		if (dtSentForTrial.isEnabled() && dtSentForTrial.isDateValidate()) {
			Calendar c = null;
			try {
				c = dtSentForTrial.getDate();
			} catch (CSValidationException e) {
				log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e);
			}
			if (c != null) {
				return new Timestamp(c.getTimeInMillis());
			}
		} else if (!dtSentForTrial.isDateValidate()) {
			log.error("Sent for trial date is incorrect");
		}
		return null;
	}

	public String getEitherWayType() {
		if (cmbEitherWayType.isEnabled()) {
			return ((DropdownCodeStringValue) cmbEitherWayType.getSelectedItem()).getCode();
		} else {
			return null;
		}
	}

	public String getRetrial() {
		return chckbxRetrial.isSelected() ? "Y" : "N";
	}

	public Timestamp getPrelimHearingDate() {
		Calendar c = null;
		if (dtFirstHearingDate.isDateValidate()) {
			try {
				c = dtFirstHearingDate.getDate();
			} catch (CSValidationException e) {
				log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e);
			}
			if (c == null) {
				return null;
			} else {
				return new Timestamp(c.getTimeInMillis());
			}
		} else {
			log.error("Prelim hearing date is incorrect");
		}
		return null;
	}

	public List<ChargesLogBasicValue> getCharges() {
		ArrayList<ChargesLogBasicValue> values = new ArrayList<ChargesLogBasicValue>();
		String text = txtCharges.getText();
		int position = 0;
		StringBuilder chargeInfo = new StringBuilder(EMPTY_STRING);
		int chargeInfoLength = 0;
		// Loop through the characters in the text and break it down into chargeLog entries
		for (int i = 0; i < text.length(); i++) {
			char chr = text.charAt(i);
			int lengthOfChar = StringUtil.getLengthOfChar(chr);
			if (chargeInfoLength + lengthOfChar > CHARGE_LOG_MAX_LENGTH) {
				// Add the charge to the array
				values.add(new ChargesLogBasicValue(null, position, chargeInfo.toString()));
				position++;	
				// Reset the charge 
				chargeInfo = new StringBuilder(EMPTY_STRING);
				chargeInfoLength = 0;
			}
			chargeInfo.append(chr);
			chargeInfoLength = chargeInfoLength + lengthOfChar; 
		}
		// Add the final charge to the array
		if (chargeInfoLength > 0) {
			values.add(new ChargesLogBasicValue(null, position, chargeInfo.toString()));
		}
		return values;
	}

	public Integer getPagesOfEvidence() {
		if (txtPagesOfEvidence.isEnabled()) {
			return Integer.parseInt(txtPagesOfEvidence.getText());
		} else {
			return null;
		}
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
	
	public Timestamp getAppMadeDate() {
		if (dtAppMade.isEnabled() && dtAppMade.isDateValidate()) {
			Calendar c = null;
			try {
				c = dtAppMade.getDate();
			} catch (CSValidationException e) {
				log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e);
			}
			if (c == null) {
				return null;
			} else {
				return new Timestamp(c.getTimeInMillis());
			}
		} else if (!dtAppMade.isDateValidate()) {
			log.error("App Made date is incorrect");
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
	public CaseBasicValue populateCaseBasicValue(List<RefusedBroadcastCaseBasicValue> refusedBroadcastCaseArray) {
		CaseBasicValue basicValue = new CaseBasicValue();
		populateCaseBasicValue(basicValue, refusedBroadcastCaseArray);
		return basicValue;
	}	

	/**
	 * Get the broadcast String code
	 * @param Integer id to get the string
	 * @return string of code
	 */
	private String getBroadcastString(Integer id) {
		String code = null;
		for (int i = 0; i < broadcastTypes.size(); i++) {
			if (broadcastTypes.get(i).getId().equals(id)) {
				code = broadcastTypes.get(i).getCode();
				break;
			}
		}
		return code;
	}
	
	/**
	 * Get the broadcast case's id
	 * @param string code to search for
	 * @return id of the code 
	 */
	private Integer getBroadcast(String string) {
		Integer id = null;
		for (int i = 0; i < broadcastTypes.size(); i++) {
			if (broadcastTypes.get(i).getCode().equals(string)) {
				id = broadcastTypes.get(i).getId();
				break;
			}
		}
		return id;
	}

	/**
	 * Used in the case amend process
	 * 
	 * @param caseNumber
	 * @return
	 */
	public void populateCaseBasicValue(CaseBasicValue amendCase, List<RefusedBroadcastCaseBasicValue> refusedBroadcastCaseArray) {
		amendCase.setVersion(version);
		amendCase.setCaseType("T");
		amendCase.setCaseTitle(getCaseTitle());
		amendCase.setMonitoringCategoryId(getRefMonitoringCategoryId());
		amendCase.setReceivedDate(getDateReceived());
		amendCase.setTicketRequired(getTicketRequired());
		amendCase.setTicketTypeCode(getTicketType());
		amendCase.setNoDefendantsForCase(getNoOfDefendants());
		amendCase.setCourtIdReceivingSite(getReceivingSite());
		amendCase.setCommittalDate(getDateOfCommittal());
		if (txtReceiptType != null && txtReceiptType.getText() != null && !"".equals(txtReceiptType.getText())) {
			amendCase.setReceiptType(txtReceiptType.getText());
		} else {
			amendCase.setReceiptType(getReceiptType());
		}
		amendCase.setOriginalCaseNumber(getOrigCaseNumber());
		amendCase.setSecureCourt(getSecureCourt());
		amendCase.setRefCourtID(getRecFrom());
		amendCase.setClassCode(getClassCode());
		amendCase.setSentForTrialDate(getSentForTrial());
		amendCase.setEitherWayType(getEitherWayType());
		amendCase.setRetrial(getRetrial());
		amendCase.setPreliminaryDateOfHearing(getPrelimHearingDate());
		amendCase.setNoPageProsEvidence(getPagesOfEvidence());
		amendCase.setPoliceForceCode(getPoliceForceCode());
		amendCase.setCourtID(XhibitSingleton.getInstance().getCourtId());
		// CTX-1255 additions
		amendCase.setDateTransFrom(getTransferDate());
		amendCase.setCccTransFromRefCourtId(getTransferFromCourtID());
		amendCase.setOriginalCaseNumber(getOrigCaseNo());
		amendCase.setTransferredCase(getTransferIn());
		amendCase.setVideoLinkRequired("N");
		amendCase.setChargeImportIndicator("O");
		
		///XLC additions
		amendCase.setTelevisedApplicationMade(getTelevisedAppMade());
		amendCase.setTelevisedAppGranted(getTelevisedAppGranted());
		amendCase.setTelevisedAppMadeDate(getAppMadeDate());
		amendCase.setTelevisedAppRefusedFreetext(getOtherFreeText());
		
		amendCase.setCivilUnrest(getCivilUnrest());
		
		populateBroadcastVal(refusedBroadcastCaseArray);

	}
	
	private void populateBroadcastVal(List<RefusedBroadcastCaseBasicValue> refusedBroadcastCaseArray) {
		
		addOrObsoleteRefBroadcastCaseArray(chckbxDefenceRep.isSelected(), refusedBroadcastCaseArray, DEFENCE_REPRESENTATION );
		addOrObsoleteRefBroadcastCaseArray(chckbxProsRep.isSelected(), refusedBroadcastCaseArray, PROSECUTION_REPRESENTATION );
		addOrObsoleteRefBroadcastCaseArray(chckbxCaseUnsuitable.isSelected(), refusedBroadcastCaseArray, UNSUITABLE_FOR_BROADCAST );
		addOrObsoleteRefBroadcastCaseArray(chckbxAppLate.isSelected(), refusedBroadcastCaseArray, APPLICATION_LATE );
		addOrObsoleteRefBroadcastCaseArray(chckbxLikelyDisruption.isSelected(), refusedBroadcastCaseArray, LIKELY_DISRUPTION );
		addOrObsoleteRefBroadcastCaseArray(chckbxOther.isSelected(), refusedBroadcastCaseArray, OTHER );
			
	}

	/**
	 * Used during case amend to set the values in the array correctly 
	 * (either create a new row or obsolete existing if unticked)
	 * @param selected
	 * @param refusedBroadcastCaseArray
	 * @param defenceRepresentation
	 */
	private void addOrObsoleteRefBroadcastCaseArray(boolean selected,
			List<RefusedBroadcastCaseBasicValue> refusedBroadcastCaseArray, String stringCode) {
		boolean found = false;
		for(int i=0;i<refusedBroadcastCaseArray.size();i++) {
			if(refusedBroadcastCaseArray.get(i).getTeleAppRefusedReasonId().equals(getBroadcast(stringCode))) {
				found = true;
				if(!selected) {
					refusedBroadcastCaseArray.get(i).setObsolete("Y");
				} else {
					refusedBroadcastCaseArray.get(i).setObsolete("N");
				}
				break;
			}
		}
		
		if(!found && selected) {
			refusedBroadcastCaseArray.add(new RefusedBroadcastCaseBasicValue(getBroadcast(stringCode)));
		} 
		
	}

	public List<JLabel> getValidationFields() {
		return validationFields;
	}

	/**
	 * needed for setting the values from the search page.
	 * 
	 * @return textarea
	 */
	public JTextArea getTxtCharges() {
		return txtCharges;
	}

	public XTextField getTxtCaseTitle() {
		return txtCaseTitle;
	}

	public XDatePanel getSentForTrialPanel() {
		return dtSentForTrial;
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
	public void populate(CaseBasicValue caseBasicValue, List<RefusedBroadcastCaseBasicValue> refusedBroadcastBasicValues) {
		log.debug("GeneralTrial.populate() using caseBasicValue");

		version = caseBasicValue.getVersion();
		// try and get the charges log
		String chargesLog = XhibitDelegateHelper.getCaseDelegate().getChargesLog(caseBasicValue.getId());
		if (chargesLog != null) {
			txtCharges.setText(chargesLog);
		}
		lblCaseNumber.setText(caseBasicValue.getCaseType() + caseBasicValue.getCaseNumber().toString());

		if (caseBasicValue.getCaseTitle() != null) {
			txtCaseTitle.setText(caseBasicValue.getCaseTitle());
		}
		if (caseBasicValue.getReceivedDate() != null) {
			dtDateReceived.setDate(caseBasicValue.getReceivedDate());
		}
		if (caseBasicValue.getNoPageProsEvidence() != null) {
			txtPagesOfEvidence.setText(Integer.toString(caseBasicValue.getNoPageProsEvidence()));
		}
		populateReceiptTypeAndLinkedFields(caseBasicValue);

		if (caseBasicValue.getEitherWayType() != null) {
			cmbEitherWayType.setSelectedItemByCode(caseBasicValue.getEitherWayType());
		}
		if (caseBasicValue.getNoDefendantsForCase() != null) {
			txtNoOfDefendants.setText(Integer.toString(caseBasicValue.getNoDefendantsForCase()));
		}
		if (caseBasicValue.getSentForTrialDate() != null) {
			dtSentForTrial.setDate(caseBasicValue.getSentForTrialDate());
		}
		if (caseBasicValue.getCommittalDate() != null) {
			dtDateOfCommittal.setDate(caseBasicValue.getCommittalDate());
		}

		populateReceivingSite(caseBasicValue);
		
		if (caseBasicValue.getPreliminaryDateOfHearing() != null) {
			dtFirstHearingDate.setDate(caseBasicValue.getPreliminaryDateOfHearing());
		}
		
		populateMonitoringCategory(caseBasicValue);
		if (caseBasicValue.getClassCode() != null) {
			cmbClassOfCase.setSelectedItem(Integer.toString(caseBasicValue.getClassCode()));
		}

		populateTicketType(caseBasicValue);
		populateCheckBoxes(caseBasicValue);
		
		populatePoliceForce(caseBasicValue.getPoliceForceCode());
		populateReceivedFrom(caseBasicValue);

		populateTransferFrom(caseBasicValue);
		populateCivilUnrest(caseBasicValue);

		if (caseBasicValue.getOriginalCaseNumber() != null) {
			txtOrigCaseNo.setText(caseBasicValue.getOriginalCaseNumber());
		}

		if (caseBasicValue.getDateTransFrom() != null) {
			dtTransferDate.setDate(caseBasicValue.getDateTransFrom());
		}
		populateBroadcastScreen(caseBasicValue, refusedBroadcastBasicValues);
	}

	/**
	 * Populate the checkboxes on the screen, used during case ammend.
	 * @param caseBasicValue
	 */
	private void populateCheckBoxes(CaseBasicValue caseBasicValue) {
		chckbxSecureCourt.setSelected(caseBasicValue.getSecureCourt() != null && caseBasicValue.getSecureCourt().equalsIgnoreCase(("Y")));

		chckbxTicketRequired.setSelected(caseBasicValue.getTicketRequired() != null && caseBasicValue.getTicketRequired().equalsIgnoreCase(("Y")));

		chckbxRetrial.setSelected(caseBasicValue.getRetrial() != null && caseBasicValue.getRetrial().equalsIgnoreCase(("Y")));

		// CTX-1255 additions
		chckbxTransferIn.setSelected(caseBasicValue.getTransferredCase() != null && caseBasicValue.getTransferredCase().equals("Y"));
		
	}

	/**
	 * Called during case amend to populate transfer from
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
	 * Called during case amend to populate received from.
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
	 * Called during case amend to populate police force.
	 * @param caseBasicValue
	 */
	private void populatePoliceForce(Integer policeForceCode) {
		// police force - will also set the text box
		if (policeForceCode != null) {
			for (int i = 0; i < policeForceCodes.size(); i++) {
				if (policeForceCodes.get(i).getId().equals(policeForceCode)) {
					cmbPoliceForce.setSelectedItem(policeForceCodes.get(i));
					break;
				}
			}
		}
	}

	/**
	 * Called during case amend to populate ticket type.
	 * @param caseBasicValue
	 */
	private void populateTicketType(CaseBasicValue caseBasicValue) {
		// start at 1 as 0 is select ticket type
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
	 * Called during case amend to populate monitoring category. 
	 * @param caseBasicValue
	 */
	private void populateMonitoringCategory(CaseBasicValue caseBasicValue) {
		// start at 1 as 0 is select monitoring category
		if (caseBasicValue.getMonitoringCategoryId() != null) {
			for (int i = 1; i < monitoringCategory.size(); i++) {
				if (caseBasicValue.getMonitoringCategoryId().equals(monitoringCategory.get(i).getRefMonitoringCategoryId())) {
					cmbMonitoringCategory.setSelectedItem(monitoringCategory.get(i));
					break;
				}
			}
		}
	}

	/**
	 * Called during case amend to populate the cmb receiving site.
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
	 * Called during case amend to populate various fields.
	 * @param caseBasicValue
	 */
	private void populateReceiptTypeAndLinkedFields(CaseBasicValue caseBasicValue) {
		if (caseBasicValue.getReceiptType() != null) {
			txtReceiptType.setText(caseBasicValue.getReceiptType());

			if (caseBasicValue.getReceiptType().equals("EW")) {
				cmbEitherWayType.setEnabled(true);
				cmbEitherWayType.setMandatory(true);
				CaseMethods.addRemoveFromMandatoryFields(cmbEitherWayType, null, mandatoryFields, true);
				
			} else {
				cmbEitherWayType.setEnabled(false);
				cmbEitherWayType.setSelectedIndex(0);
				cmbEitherWayType.setMandatory(false);
				lblIEitherWayType.setText(" ");
				cmbEitherWayType.setError(false);
				CaseMethods.addRemoveFromMandatoryFields(cmbEitherWayType, null, mandatoryFields, false);
			}
			if (caseBasicValue.getReceiptType().equals("VB") || caseBasicValue.getReceiptType().equals("TC")
					|| caseBasicValue.getReceiptType().equals("CT")) {
				setVBTCFields();
			} else {
				setNonVBCTFields();

			}
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
		}
	}

	private void populateBroadcastScreen(CaseBasicValue caseBasicValue,
			List<RefusedBroadcastCaseBasicValue> refusedBroadcastBasicValues) {
		
		if(caseBasicValue.getTelevisedApplicationMade()!=null && caseBasicValue.getTelevisedApplicationMade().equals("Y")) {
			chckbxAppMade.setSelected(true);
			if(caseBasicValue.getTelevisedAppMadeDate()!=null) {
				dtAppMade.setDate(caseBasicValue.getTelevisedAppMadeDate());
			} else {
				Date d = null;
				dtAppMade.setDate(d);

			}
			if(caseBasicValue.getTelevisedAppGranted()==null) {
				appNARb.setSelected(true);
				enableRefusedCheckboxes(false);
			} else if(caseBasicValue.getTelevisedAppGranted().equals("Y")) {
				appYesRb.setSelected(true);
				enableRefusedCheckboxes(false);
			} else {
				appNoRb.setSelected(true);
				enableRefusedCheckboxes(true);
				populateRefusedReason(caseBasicValue, refusedBroadcastBasicValues);
			}

		}
		if(caseBasicValue.getTelevisedRemarksFilmed()!=null && caseBasicValue.getTelevisedRemarksFilmed().equals("Y")) {
			senRemarksFilmedYesRb.setSelected(true);
		} else if(caseBasicValue.getTelevisedRemarksFilmed()!=null && caseBasicValue.getTelevisedRemarksFilmed().equals("N")) {
			senRemarksFilmedNoRb.setSelected(true);
		} else  {
			senRemarksFilmedNARb.setSelected(true);
		}		
	}

	private void populateRefusedReason(CaseBasicValue caseBasicValue, List<RefusedBroadcastCaseBasicValue> refusedBroadcastBasicValues) {
		for (int i=0; i<refusedBroadcastBasicValues.size();i++){
			RefusedBroadcastCaseBasicValue ref = refusedBroadcastBasicValues.get(i);
			String code  = getBroadcastString(ref.getTeleAppRefusedReasonId());
			if(code!=null) {
				if(code.equals(DEFENCE_REPRESENTATION)) {
					chckbxDefenceRep.setSelected(true);
				} else if(code.equals(PROSECUTION_REPRESENTATION)) {
					chckbxProsRep.setSelected(true);
				} else if(code.equals(UNSUITABLE_FOR_BROADCAST)) {
					chckbxCaseUnsuitable.setSelected(true);
				} else if(code.equals(APPLICATION_LATE)) {
					chckbxAppLate.setSelected(true);
				} else if(code.equals(LIKELY_DISRUPTION)) {
					chckbxLikelyDisruption.setSelected(true);
				} else if(code.equals(OTHER)) {
					txtRefusedOther.setText(caseBasicValue.getTelevisedAppRefusedFreetext());	
					chckbxOther.setSelected(true);
				}
			}
		}
	}

	private ArrayList<DropdownCodeStringValue> createDropdownValues(String firstVal, Vector<String> codes) {
		ArrayList<DropdownCodeStringValue> val = new ArrayList<DropdownCodeStringValue>();
		val.add(new DropdownCodeStringValue(firstVal, ""));
		for (int i = 0; i < codes.size(); i++) {
			String [] toAdd = codes.get(i).split(":");
			val.add(new DropdownCodeStringValue(toAdd[0], toAdd[1]));
		}
		return val;
	}

	private void jbInit() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = CaseMethods.getDefaultGridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.setPreferredSize(new Dimension(1000, 850));

		gbc.weightx = 0.6;
		gbc.insets = new Insets(15, 15, 15, 15);
		this.add(getCaseDetailsPanel(), gbc);

		gbc.weightx = 0.6;
		gbc.gridx++;
		gbc.insets = new Insets(15, 30, 15, 15);
		this.add(getRightPanel(), gbc);
	}

	private JPanel getCaseDetailsPanel() {
		if (caseDetailsPanel == null) {
			caseDetailsPanel = new JPanel();
			caseDetailsPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = CaseMethods.getDefaultGridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;

			// First column (labels)
			gbc.weightx = 0.05;
			gbc.weighty = 0.01;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridheight = 2;
			caseDetailsPanel.add(lblCaseNumber, gbc);

			gbc.gridy += 2;
			gbc.weighty = 0.2;
			gbc.gridheight = 1;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			caseDetailsPanel.add(lblCaseTitle, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblDateReceived, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblReceiptType, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblEitherWayType, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblNoOfDefendants, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblSentForTrial, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblDateOfCommittal, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblReceivingSite, gbc);

			// Second column (entry fields)
			gbc.gridx++;
			gbc.gridy = 2;
			gbc.weightx = 0.8;
			gbc.gridwidth = 3;
			caseDetailsPanel.add(txtCaseTitle, gbc);

			gbc.gridwidth = 1;
			gbc.weightx = 0.8;
			gbc.gridx++;
			caseDetailsPanel.add(Box.createRigidArea(txtCaseTitle.getPreferredSize()), gbc);

			gbc.gridx++;
			caseDetailsPanel.add(Box.createRigidArea(txtCaseTitle.getPreferredSize()), gbc);

			gbc.gridx -= 2;
			gbc.gridy += 2;
			caseDetailsPanel.add(dtDateReceived, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(cmbReceiptType, gbc);
			caseDetailsPanel.add(txtReceiptType, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(cmbEitherWayType, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(txtNoOfDefendants, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(dtSentForTrial, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(dtDateOfCommittal, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(cmbReceivingSite, gbc);

			// Second column (error labels)
			gbc.gridy = 1;
			gbc.gridwidth = 3;
			gbc.weighty = 0.1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			caseDetailsPanel.add(lblCaseTitleError, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblIDateReceived, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblIReceiptType, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblIEitherWayType, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblINoOfDefendants, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblISentForTrial, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblIDateOfCommittal, gbc);

			gbc.gridy += 2;
			caseDetailsPanel.add(lblIReceivingSite, gbc);

			gbc.gridx--;
			gbc.gridy += 2;
			gbc.gridwidth = 4;
			gbc.gridheight = 3;
			gbc.weighty = 0.2;
			gbc.insets = new Insets(15, 0, 0, 0);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			caseDetailsPanel.add(getMagistratesCourtDetailsPanel(), gbc);

			gbc.gridy += 4;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			caseDetailsPanel.add(lblNewLabel, gbc);

			gbc.gridx++;
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(20, 0, 20, 0);
			caseDetailsPanel.add(chckbxRetrial, gbc);

			gbc.gridx--;
			gbc.gridy++;
			Insets newInsets = new Insets(4, 0, 0, -18);
			gbc.insets = newInsets;
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.NORTHEAST;
			caseDetailsPanel.add(chckbxTransferIn, gbc);

			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridheight = 3;
			gbc.gridwidth = 4;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			caseDetailsPanel.add(getTransferDetailsPanel(), gbc);

			gbc.fill = GridBagConstraints.NONE;
			gbc.gridy += 5;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			caseDetailsPanel.add(lblOrigCaseNo, gbc);

			gbc.gridx++;
			caseDetailsPanel.add(txtOrigCaseNo, gbc);
			
			gbc.gridy--;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			caseDetailsPanel.add(lblIOrigCaseNo, gbc);
			
			gbc.gridx--;
			gbc.gridy += 2;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridheight = 3;
			gbc.gridwidth = 2;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			caseDetailsPanel.add(getCivilUnrestPanel(caseX), gbc);

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
			gbc.fill = GridBagConstraints.HORIZONTAL;

			gbc.gridy++;
			gbc.weightx = 0.05;
			magistratesCourtDetailsPanel.add(lblReceivedFrom, gbc);

			gbc.gridy = 1;
			gbc.gridx++;
			gbc.insets = new Insets(4, 4, 4, 25);
			gbc.weightx = 0.2;
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
			CaseMethods.addChangeListeners(magistratesCourtDetailsPanel.getComponents(), caseX);
		}
		return magistratesCourtDetailsPanel;
	}

	private JPanel getTransferDetailsPanel() {
		if (transferDetailsPanel == null) {
			transferDetailsPanel = new JPanel();
			transferDetailsPanel.setLayout(new GridBagLayout());
			transferDetailsPanel.setBorder(BorderFactory.createTitledBorder("Transfer In"));

			GridBagConstraints gbcInner = CaseMethods.getDefaultGridBagConstraints();

			gbcInner.fill = GridBagConstraints.HORIZONTAL;
			gbcInner.gridy += 2;
			transferDetailsPanel.add(lblTransferDate, gbcInner);

			gbcInner.gridy += 2;
			transferDetailsPanel.add(lblTransferFrom, gbcInner);

			gbcInner.gridx++;
			gbcInner.gridy = 1;
			gbcInner.insets = XHIBITConstant.errorLabelInsets;
			gbcInner.weighty = 0.1;
			gbcInner.gridwidth = 2;
			transferDetailsPanel.add(lblITransferDate, gbcInner);

			gbcInner.gridy += 2;
			gbcInner.gridwidth = 1;
			transferDetailsPanel.add(lblITransferFrom, gbcInner);

			gbcInner.insets = XHIBITConstant.nonContainerInsets;
			gbcInner.weighty = 0.2;
			gbcInner.gridy = 2;
			transferDetailsPanel.add(dtTransferDate, gbcInner);

			gbcInner.gridy += 2;
			gbcInner.gridwidth = 2;
			transferDetailsPanel.add(cmbTransferFrom, gbcInner);

			CaseMethods.addChangeListeners(transferDetailsPanel.getComponents(), caseX);
		}

		return transferDetailsPanel;
	}

	private JPanel getRightPanel() {
		if (rightPanel == null) {
			rightPanel = new JPanel();
			rightPanel.setLayout(new GridBagLayout());

			GridBagConstraints gbc = CaseMethods.getDefaultGridBagConstraints();
			gbc.weightx = 0.45;
			gbc.weighty = 0.2;
			gbc.gridy = 1;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridwidth = 2;
			rightPanel.add(lblPagesOfEvidence, gbc);

			gbc.gridy += 2;
			rightPanel.add(lblFirstHearingDate, gbc);

			gbc.gridy += 2;
			rightPanel.add(lblMonitoringCategory, gbc);

			gbc.gridy += 2;
			rightPanel.add(lblClassOfCase, gbc);

			gbc.gridy += 2;
			rightPanel.add(lblSecureCourt, gbc);

			gbc.gridy += 2;
			JPanel ticketPanel = new JPanel();
			ticketPanel.setLayout(new GridBagLayout());
			ticketPanel.setBorder(BorderFactory.createTitledBorder("Ticket Required"));

			GridBagConstraints ticketPanelGbc = CaseMethods.getDefaultGridBagConstraints();
			ticketPanelGbc.fill = GridBagConstraints.BOTH;
			ticketPanelGbc.anchor = GridBagConstraints.WEST;
			ticketPanelGbc.weightx = 0.05;
			ticketPanelGbc.gridy++;
			ticketPanel.add(lblTicketType, ticketPanelGbc);

			ticketPanelGbc.weightx = 0.80;
			ticketPanelGbc.gridx++;
			ticketPanelGbc.gridwidth = 2;
			ticketPanel.add(cmbTicketType, ticketPanelGbc);

			ticketPanelGbc.gridy--;
			ticketPanelGbc.insets = XHIBITConstant.errorLabelInsets;
			ticketPanel.add(lblITicketType, ticketPanelGbc);

			CaseMethods.addChangeListeners(ticketPanel.getComponents(), caseX);

			// Bodge to get ticket required checkbox to appear at correct place
			// (in-line with secure court)
			gbc.anchor = GridBagConstraints.NORTHEAST;
			gbc.fill = GridBagConstraints.NONE;
			Insets newInsets = new Insets(4, 0, 0, -21);
			gbc.insets = newInsets;
			rightPanel.add(chckbxTicketRequired, gbc);

			gbc.gridwidth = 4;
			gbc.gridheight = 2;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			rightPanel.add(ticketPanel, gbc);

			gbc.gridy += 2;
			gbc.gridwidth = 3;
			rightPanel.add(getPoliceForcePanel(), gbc);

			gbc.gridy += 2;
			gbc.gridwidth = 4;
			gbc.gridheight = 3;
			rightPanel.add(getChargesPanel(), gbc);

			gbc.gridy += 3;
			gbc.gridwidth = 4;
			rightPanel.add(getTelevisedCasePanel(), gbc);

			gbc.gridy = 1;
			gbc.gridx += 2;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			rightPanel.add(txtPagesOfEvidence, gbc);

			gbc.gridy += 2;
			rightPanel.add(dtFirstHearingDate, gbc);

			gbc.gridy += 2;
			rightPanel.add(cmbMonitoringCategory, gbc);

			gbc.gridy += 2;
			rightPanel.add(cmbClassOfCase, gbc);

			gbc.gridy += 2;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.NONE;
			rightPanel.add(chckbxSecureCourt, gbc);

			gbc.gridx++;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			rightPanel.add(Box.createRigidArea(txtPagesOfEvidence.getMinimumSize()), gbc);

			gbc.gridx--;
			gbc.gridy = 0;
			gbc.weighty = 0.1;
			gbc.gridwidth = 2;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			rightPanel.add(lblIPagesOfEvidence, gbc);

			gbc.gridy += 2;
			rightPanel.add(lblIFirstHearingDate, gbc);

			gbc.gridy += 2;
			rightPanel.add(lblIMonitoringCategory, gbc);

			gbc.gridy += 2;
			rightPanel.add(lblIClassOfCase, gbc);

			CaseMethods.addChangeListeners(rightPanel.getComponents(), caseX);
		}

		return rightPanel;
	}

	private JPanel getTelevisedCasePanel() {
		if (televisedPanel == null) {
			televisedPanel = new JPanel();
			televisedPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = CaseMethods.getDefaultGridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;

			televisedPanel.setBorder(BorderFactory.createTitledBorder("Televised Case"));
			gbc.gridwidth=2;
			gbc.insets = XHIBITConstant.errorLabelInsets;

			rightPanel.add(Box.createRigidArea(lblAppMade.getMinimumSize()), gbc);
			gbc.gridx=gbc.gridx+2;
			gbc.gridwidth=1;
			televisedPanel.add(lblIdtAppMade, gbc);
			
			gbc.gridx=0;
			gbc.gridy++;
			gbc.insets=XHIBITConstant.nonContainerInsets;
			
			televisedPanel.add(lblAppMade, gbc);
			gbc.gridx++;
			televisedPanel.add(chckbxAppMade, gbc);
			gbc.gridx++;
			gbc.fill = GridBagConstraints.BOTH;
			televisedPanel.add(dtAppMade, gbc);
			
			gbc.fill = GridBagConstraints.NONE;

			gbc.gridy++;
			gbc.gridx=0;
			gbc.gridwidth=1;
			televisedPanel.add(lblAppGranted, gbc);
			gbc.gridx++;
			televisedPanel.add(appYesRb, gbc);
			gbc.gridx++;
			televisedPanel.add(appNoRb, gbc);
			gbc.gridx++;
			televisedPanel.add(appNARb, gbc);
			
			gbc.gridy++;
			gbc.gridx=0;
			gbc.gridwidth = 4;
			gbc.fill = GridBagConstraints.BOTH;
			televisedPanel.add(getRefusedReasonPanel(), gbc);
			
			gbc.fill = GridBagConstraints.NONE;
			gbc.gridx=0;
			gbc.gridwidth = 1;
			gbc.gridy++;
			televisedPanel.add(lblSenRemarksFilmed, gbc);
			gbc.gridx++;
			televisedPanel.add(senRemarksFilmedYesRb, gbc);
			gbc.gridx++;
			televisedPanel.add(senRemarksFilmedNoRb, gbc);
			gbc.gridx++;
			televisedPanel.add(senRemarksFilmedNARb, gbc);
			CaseMethods.addChangeListeners(televisedPanel.getComponents(), caseX);

		}

		return televisedPanel;
	}

	private JPanel getRefusedReasonPanel() {
		if ( refusedReasonPanel== null) {
			refusedReasonPanel = new JPanel();
			refusedReasonPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = CaseMethods.getDefaultGridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;

			refusedReasonPanel.setBorder(BorderFactory.createTitledBorder("Refused reason"));
			refusedReasonPanel.add(chckbxDefenceRep, gbc);
			gbc.gridx++;
			refusedReasonPanel.add(chckbxProsRep, gbc);
			gbc.gridx++;
			refusedReasonPanel.add(chckbxCaseUnsuitable, gbc);
			gbc.gridx++;
			refusedReasonPanel.add(chckbxAppLate, gbc);
			
			gbc.gridy++;
			gbc.gridx=0;
			gbc.gridwidth=2;
		    Insets tmpInsets = new Insets(-2, 4, -2, 4);

			gbc.insets =tmpInsets;

			rightPanel.add(Box.createRigidArea(lblAppMade.getMinimumSize()), gbc);
			gbc.gridx=gbc.gridx+2;
			gbc.gridwidth=1;
			refusedReasonPanel.add(lblIRefusedOther,gbc);
			
			gbc.gridy++;
			gbc.gridx=0;			
			gbc.insets=XHIBITConstant.nonContainerInsets;
			refusedReasonPanel.add(chckbxLikelyDisruption, gbc);

			gbc.gridx++;
			refusedReasonPanel.add(chckbxOther, gbc);
			
			gbc.gridx++;
			gbc.fill = GridBagConstraints.BOTH;

			refusedReasonPanel.add(refusedOtherScroll, gbc);

		}
		CaseMethods.addChangeListeners(refusedReasonPanel.getComponents(), caseX);

		return refusedReasonPanel;

	}

	private JPanel getPoliceForcePanel() {
		if (policeForcePanel == null) {
			policeForcePanel = new JPanel();
			policeForcePanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = CaseMethods.getDefaultGridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;

			gbc.gridx++;
			policeForcePanel.add(lblIPoliceForce, gbc);

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

	private JPanel getChargesPanel() {
		if (chargesPanel == null) {
			chargesPanel = new JPanel();
			chargesPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = CaseMethods.getDefaultGridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;

			chargesPanel.setBorder(BorderFactory.createTitledBorder("Charges"));

			gbc.gridheight = 6;
			gbc.gridwidth = 3;
			gbc.weighty = 0.8;
			gbc.fill = GridBagConstraints.BOTH;
			chargesPanel.add(chargesScrollPane, gbc);

			gbc.gridy++;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			chargesPanel.add(Box.createRigidArea(txtCaseTitle.getPreferredSize()), gbc);

			gbc.gridy++;
			chargesPanel.add(Box.createRigidArea(txtCaseTitle.getPreferredSize()), gbc);

			gbc.gridy++;
			chargesPanel.add(Box.createRigidArea(txtCaseTitle.getPreferredSize()), gbc);

			gbc.gridy++;
			chargesPanel.add(Box.createRigidArea(txtCaseTitle.getPreferredSize()), gbc);

			gbc.gridy++;
			chargesPanel.add(Box.createRigidArea(txtCaseTitle.getPreferredSize()), gbc);

			gbc.gridy++;
			gbc.weighty = 0.2;
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.EAST;
			chargesPanel.add(btnSearchCharges, gbc);

			CaseMethods.addChangeListeners(chargesPanel.getComponents(), caseX);
		}

		return chargesPanel;
	}

	public void setCaseNumber(String caseNumber) {
		if (lblCaseNumber != null)
			lblCaseNumber.setText(caseNumber);
	}

	// Method to add change listeners to each component within form
	

	public void clearChangedState() {
		fieldsChangedGlobal = false;
	}

		

	public int getNumDefendantsToAdd() {
		if (Pattern.matches("[0-9]+", txtNoOfDefendants.getText())) {
			return Integer.parseInt(txtNoOfDefendants.getText());
		} else {
			return -1;
		}
	}

	public void populateReceiptType(CaseBasicValue caseBasicValue) {
		cmbReceiptType.setEnabled(false);
		cmbReceiptType.setVisible(false);

		txtReceiptType.setVisible(true);
		txtReceiptType.setEnabled(false);
		txtReceiptType.setText(caseBasicValue.getReceiptType());

	}
	
	/**
	 * [0297.BRD.004]
	 * Returns the free text if its enabled
	 * otherwise returns null.
	 * @return text or null
	 */
	private String getOtherFreeText() {
		String otherFreeText = null;
		if(txtRefusedOther.isEnabled()) {
			otherFreeText = txtRefusedOther.getText();
		}
		return otherFreeText;
	}

	/**
	 * If app made is clicked returns Y, else returns N
	 * @return Y or N
	 */
	private String getTelevisedAppMade() {
		
		String televisedAppMade = null;
		if(chckbxAppMade.isSelected()) {
			televisedAppMade ="Y";

		} else {
			televisedAppMade = "N";
		} 
		
		return televisedAppMade;
	}

	/**
	 * Returns the value to set televised app granted
	 * if buttons are enabled (i.e. app granted is ticked) and either yes or 
	 * no is clicked then returns Y or N else returns null and saves null to db.
	 * @return Y, N or null.
	 */
	private String getTelevisedAppGranted() {
		String appGranted = null;
		if(appYesRb.isEnabled() && appYesRb.isSelected()) {
			appGranted = "Y";
		} else if (appNoRb.isEnabled() && appNoRb.isSelected()) {
			appGranted = "N";			
		}
		return appGranted;

	}

	/**
	 * The traversal policy used on sentence tab, called when sentence tab is
	 * displayed (from CaseXPanel).
	 * 
	 * @return FocusTraversalOnArray - all components used in traversal.
	 */
	public FocusTraversalOnArray getTabbedPaneOrder() {

		return new FocusTraversalOnArray(new Component[] { txtCaseTitle, dtDateReceived, cmbReceiptType,
				cmbEitherWayType, txtNoOfDefendants, dtSentForTrial, dtDateOfCommittal, cmbReceivingSite,
				txtReceivedFrom, cmbReceivedFrom, chckbxRetrial, chckbxTransferIn, dtTransferDate, cmbTransferFrom,
				txtOrigCaseNo, cmbCivilUnrest, txtPagesOfEvidence, dtFirstHearingDate, cmbMonitoringCategory, cmbClassOfCase,
				chckbxSecureCourt, chckbxTicketRequired, cmbTicketType, txtPoliceForce, cmbPoliceForce, txtCharges,
				btnSearchCharges, chckbxAppMade, dtAppMade, appYesRb, appNoRb, appNARb, chckbxDefenceRep, chckbxProsRep, 
				chckbxCaseUnsuitable, chckbxAppLate, chckbxLikelyDisruption, chckbxOther, txtRefusedOther, senRemarksFilmedYesRb, 
				senRemarksFilmedNoRb, senRemarksFilmedNARb, caseX.getCreateButton(), caseX.getFinishButton(), caseX.getCancelButton() });
	}
	
	/**
	 * Method is called from CaseXPanel if app granted is no to 
	 * ensure a reason has been selected.
	 * Req.[0297.BRD.001]
	 * @return 
	 */
	public boolean isAppGrantedFalsedAndReasonProvided () {
		boolean isValid = false;
		if(chckbxDefenceRep.isSelected() || chckbxProsRep.isSelected() ||
		chckbxCaseUnsuitable.isSelected() || chckbxAppLate.isSelected() ||
		chckbxLikelyDisruption.isSelected() || chckbxOther.isSelected()) {
			isValid=true;
		}
		return isValid;
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

	/**
	 * Listener attached to date received field 
	 */
	public class DateReceivedListener implements MFieldListener {
		@Override
		public void fieldEntered(FocusEvent event) {
			//Don't want to do anything on field entered only on exit
		}

		@Override
		public void fieldExited(FocusEvent event) {
			// Check mandatory fields to enable create button
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
			// Validate other fields that depend on this, i.e date date of
			// committal & transfer date
			if (dtDateReceived.isDateValidate()) {
				dtDateOfCommittal.crossValidateDate();
				dtSentForTrial.crossValidateDate();
				dtFirstHearingDate.crossValidateDate();

				// Transfer date must be equal to or before date received,
				// AND equal to or after date sent for trial
				if (dtSentForTrial.isEnabled()) {
					dtTransferDate.setToCompare(dtSentForTrial);
					dtTransferDate.setCustomWarning(CaseMaintenanceConstants.ON_AFTER_SFT);
				} else {
					dtTransferDate.setToCompare(dtDateOfCommittal);
					dtTransferDate.setCustomWarning(CaseMaintenanceConstants.ON_AFTER_DATE_OF_COMMITTAL);
				}

				dtTransferDate.setBeforeOrAfter(CaseMaintenanceConstants.AFTER);
				dtTransferDate.crossValidateDate();

				// If date appeal lodged condition isnt satisfied then test
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
	 * Listener attached to receipt type, called when item state changed.
	 * @author waltersn
	 *
	 */
	public class ReceiptTypeItemListener implements ItemListener {

		public void itemStateChanged(ItemEvent e) {
			RefSystemCodeBasicValue value = (RefSystemCodeBasicValue) cmbReceiptType.getSelectedItem();
			if (value.getCode() != null) {
				if (value.getCode().equals("EW")) {
					cmbEitherWayType.setEnabled(true);
					cmbEitherWayType.setMandatory(true);
					CaseMethods.addRemoveFromMandatoryFields(cmbEitherWayType, lblEitherWayType, mandatoryFields, true);
					CaseUtils.removeMandatoryLabel(lblDateOfCommittal);
				} else {
					cmbEitherWayType.setEnabled(false);
					cmbEitherWayType.setSelectedIndex(0);
					cmbEitherWayType.setMandatory(false);
					lblIEitherWayType.setText(" ");
					cmbEitherWayType.setError(false);
					
					CaseMethods.addRemoveFromMandatoryFields(cmbEitherWayType, lblEitherWayType, mandatoryFields, false);

				}
				if (value.getCode().equals("VB") || value.getCode().equals("TC")) {
					setVBTCFields();
				} else {
					setNonVBCTFields();
				}
				enableDisableFinalFirstDefendantFields(value);
				
				CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
			} else {
				cmbEitherWayType.setEnabled(false);
				lblIEitherWayType.setText(" ");

				CaseMethods.addRemoveFromMandatoryFields(cmbEitherWayType, lblEitherWayType, mandatoryFields, false);
			
				dtDateOfCommittal.setEnabled(false);
				CaseMethods.addRemoveFromMandatoryFields(dtDateOfCommittal, lblDateOfCommittal, mandatoryFields, false);
				lblIDateOfCommittal.setText(" ");
			}
		}
	}
	
	/**
	 * Listener attached to date sent for trial.
	 * @author waltersn
	 *
	 */
	public class DateSentForTrialListener implements MFieldListener {
		@Override
		public void fieldEntered(FocusEvent event) {
			//Don't want to do anything on field entered only on exit
		}

		@Override
		public void fieldExited(FocusEvent event) {
			// Check mandatory fields to enable create button
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
			// Validate other fields that depend on this, i.e. transfer date
			if (dtSentForTrial.isDateValidate()) {
				// Transfer date must be equal to or before date received,
				// AND equal to or after date sent for trial
				dtTransferDate.setToCompare(dtSentForTrial);
				dtTransferDate.setCustomWarning(CaseMaintenanceConstants.ON_AFTER_SFT);
				dtTransferDate.setBeforeOrAfter(CaseMaintenanceConstants.AFTER);
				dtTransferDate.crossValidateDate();

				// If date appeal lodged condition isn't satisfied then test
				// other, else don't erase warning message
				if (!dtTransferDate.hasError()) {
					dtTransferDate.setToCompare(dtDateReceived);
					dtTransferDate.setBeforeOrAfter(CaseMaintenanceConstants.BEFORE);
					dtTransferDate.setCustomWarning(CaseMaintenanceConstants.ON_BEFORE_DATE_RECEIVED);
					dtTransferDate.crossValidateDate();
				}

				// adding this so it validates start date before you tab
				// into the defendant tab
				// so that you can't save a defendant with invalid date.
				try {
					if (!caseX.getDefendantPanel().validateForSentForTrial(dtSentForTrial.getDate())) {
						dtSentForTrial.setSecondaryError(true);
						dtSentForTrial.setSecondaryErrorText(
								"Invalidates First/Final date for defendant(s).  Please correct these first");
					} else {
						dtSentForTrial.setSecondaryError(false);
					}
				} catch (CSValidationException e) {
					log.error("Sent for trial date is invalid");
					dtSentForTrial.setSecondaryError(false);
				}

			}
		}
	}
	
	/**
	 * Listener attached to date of committal.
	 */
	public class DateOfCommittalListener implements MFieldListener {
		@Override
		public void fieldEntered(FocusEvent event) {
			//Don't want to do anything on field entered only on exit
		}

		@Override
		public void fieldExited(FocusEvent event) {
			// Check mandatory fields to enable create button
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
			// Validate other fields that depend on this, i.e. transfer date
			if (dtDateOfCommittal.isDateValidate()) {
				// Transfer date must be equal to or before date received,
				// AND equal to or after date sent for trial
				dtTransferDate.setToCompare(dtDateOfCommittal);
				dtTransferDate.setCustomWarning(CaseMaintenanceConstants.ON_AFTER_DATE_OF_COMMITTAL);
				dtTransferDate.setBeforeOrAfter(CaseMaintenanceConstants.AFTER);
				dtTransferDate.crossValidateDate();

				// If date appeal lodged condition isnt satisfied then test
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
	 * Listener attached to received from.
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
					lblIReceivedFrom.setText(CaseMaintenanceConstants.MANDATORY_FIELD);
				}
			}

		}

		void populateDropdown(String value) {
			boolean found = false;
			for (int i = 0; i < receivedFromValues.size(); i++) {
				if (!receivedFromValues.get(i).getCourtFullName().equals("Select Magistrates Court")
						&& receivedFromValues.get(i).getCourtShortName().equalsIgnoreCase(value)) {
					found = true;
					cmbReceivedFrom.setSelectedIndex(i);
					lblIReceivedFrom2.setText("");
					lblIReceivedFrom2.setText(" ");
					break;
				}
			}
			if (!found) {
				txtReceivedFrom.setError();
			}

		}
	}
	
	/**
	 * Listener attached to received from .
	 *
	 */
	public class ReceivedFromFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			//Don't want to do anything on gained only on lost

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
	 * Attached to received from combo box.
	 */
	public class ReceivedFromComboFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			//Don't want to do anything on gained only on lost
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
	
	/**
	 * Listener attached to transfer checkbox.
	 */
	public class ChckbxTransferListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (chckbxTransferIn.isSelected()) { // checked
				if (!chckbxRetrial.isSelected()) {
					// enable orig case no & make mandatory
					txtOrigCaseNo.setEnabled(true);
				}

				// enable transfer date & transfer from & make mandatory
				dtTransferDate.setEnabledAndFocusable(true);
				dtTransferDate.setRequired(true);
				CaseMethods.addRemoveFromMandatoryFields(dtTransferDate, lblTransferDate, mandatoryFields, true);

				cmbTransferFrom.setEnabled(true);
				cmbTransferFrom.setMandatory(true);
				CaseMethods.addRemoveFromMandatoryFields(cmbTransferFrom, lblTransferFrom, mandatoryFields, true);

			} else { // unchecked
				if (!chckbxRetrial.isSelected()) { // re-trial checkbox
													// enables/disables orig
													// case no.
					// disable orig case no, clear it, make non-mandatory
					txtOrigCaseNo.setText("");
					txtOrigCaseNo.setEnabled(false);
					txtOrigCaseNo.clearError();
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
				
				CaseMethods.addRemoveFromMandatoryFields(dtTransferDate, lblTransferDate, mandatoryFields, false);

				lblITransferDate.setText(" ");
			}

			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
		}
	}
	
	/**
	 * Transfer date listener.
	 */
	public class TransferDateListener implements MFieldListener {
		@Override
		public void fieldEntered(FocusEvent event) {
			//Don't want to do anything on field entered only on exit
		}

		@Override
		public void fieldExited(FocusEvent event) {
			// Check mandatory fields to enable create button
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);

			if (dtTransferDate.isDateValidate()) {
				// Transfer date must be equal to or before date received
				// AND equal to or after sent for trial
				// Work around as XDateField only allows one date comparison
				if (dtSentForTrial.isFocusable()) {
					dtTransferDate.setToCompare(dtSentForTrial);
					dtTransferDate.setCustomWarning(CaseMaintenanceConstants.ON_AFTER_SFT);
				} else {
					dtTransferDate.setToCompare(dtDateOfCommittal);
					dtTransferDate.setCustomWarning(CaseMaintenanceConstants.ON_AFTER_DATE_OF_COMMITTAL);
				}

				dtTransferDate.setBeforeOrAfter(CaseMaintenanceConstants.AFTER);
				dtTransferDate.crossValidateDate();

				// If date appeal lodged condition isnt satisfied then test
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
	 * Listener attached to retrial.
	 */
	public class RetrialListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (chckbxRetrial.isSelected()) {
				if (!chckbxTransferIn.isSelected()) {
					txtOrigCaseNo.setEnabled(true);
				}
			} else {
				// Check if transfer in selected
				if (!chckbxTransferIn.isSelected()) {
					txtOrigCaseNo.setText("");
					txtOrigCaseNo.setEnabled(false);
					txtOrigCaseNo.clearError();
				}
			}
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
		}
	}
	
	/**
	 * Ticket required listener.
	 */
	public class TicketRequiredListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if (chckbxTicketRequired.isSelected()) {
				cmbTicketType.setEnabled(true);
				cmbTicketType.setMandatory(true);
				
				CaseMethods.addRemoveFromMandatoryFields(cmbTicketType, lblTicketType, mandatoryFields, true);

				if (cmbTicketType.getSelectedIndex() != 0) {
					lblITicketType.setText(" ");
					cmbTicketType.setError(false);
				} else {
					lblITicketType.setText(CaseMaintenanceConstants.MANDATORY_FIELD);
					cmbTicketType.setError(true);
				}
			} else {
				cmbTicketType.setSelectedIndex(0);
				cmbTicketType.setEnabled(false);
				cmbTicketType.setMandatory(false);
				lblITicketType.setText(" ");
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
	 * called when app made checkbox is selected/not selected.
	 */
	public class AppMadeListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			displayAppMade(chckbxAppMade.isSelected());
			if(!chckbxAppMade.isSelected()) {
				lblIdtAppMade.setText(" ");
				CaseMethods.addRemoveFromMandatoryFields(dtAppMade, null, mandatoryFields, false);

				Date d = null;
				dtAppMade.setDate(d);
				
			} else {
				CaseMethods.addRemoveFromMandatoryFields(dtAppMade, null, mandatoryFields, true);
			}
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
		}
		/**
		 * Display or hide the app made fields
		 * depending on whether checkbox has been ticked or not
		 * @param selected - true if checkbox ticked
		 */
		private void displayAppMade(final boolean selected) {
			
			//ensure all other fields are disabled.
			if(!selected) {
				appNARb.setSelected(true);
				setInitialTelevisedFields(selected);
			} else {
				dtAppMade.setEnabledAndFocusable(selected);
				appYesRb.setEnabled(selected);
				appNoRb.setEnabled(selected);
				appNARb.setEnabled(selected);
			}
			
		}
	}
	
	/**
	 * Called when other checkbox is ticked/unticked.
	 */
	public class OtherCheckboxListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			txtRefusedOther.setEditable(chckbxOther.isSelected());
			txtRefusedOther.setEnabled(chckbxOther.isSelected());

			if(!chckbxOther.isSelected()) {
				txtRefusedOther.setText(null);
				lblIRefusedOther.setText(" ");
				CaseMethods.addRemoveFromMandatoryFields(txtRefusedOther, null, mandatoryFields, false);

				
			} else {
				CaseMethods.addRemoveFromMandatoryFields(txtRefusedOther, null, mandatoryFields, true);

				if(txtRefusedOther.getText()==null || "".equals(txtRefusedOther.getText())){
					lblIRefusedOther.setText(CaseMaintenanceConstants.MANDATORY_FIELD);
				}

			}
			CaseMethods.checkMandatoryFields(caseX, mandatoryFields, fieldsChangedGlobal);
		}
	}

	/**
	 * If EW, IO or ST  have been selected in receipt type
	 * then enable the first/final hearing dates on the defendant tab otherwise don't 
	 * @param value
	 */
	public void enableDisableFinalFirstDefendantFields(RefSystemCodeBasicValue value) {
		// --- CTX-1884 - Start ---
		if (value.getCode().equals("EW") || value.getCode().equals("IO") || value.getCode().equals("ST")) {
			log.debug("Enable magistrate hearing dates on defendant/appellant tab");
			caseX.getDefendantPanel().getFirstDatePicker().setEnabledAndFocusable(true);
			caseX.getDefendantPanel().getFinalDatePicker().setEnabledAndFocusable(true);
		} else {
			log.debug("Disable magistrate hearing dates on defendant/appellant tab");
			caseX.getDefendantPanel().getFirstDatePicker().clear();
			caseX.getDefendantPanel().getFirstDatePicker().setEnabledAndFocusable(false);
			caseX.getDefendantPanel().getFinalDatePicker().clear();
			caseX.getDefendantPanel().getFinalDatePicker().setEnabledAndFocusable(false);
		}
		// --- CTX-1884 - End ---
		
	}

	/**
	 * Disables SFT - not mandatory
	 * Enabled Date of Committal - mandatory 
	 * Enables pages of evidence - mandatory
	 */
	public void setVBTCFields() {
		dtSentForTrial.setDate(todaysDate);
		dtSentForTrial.setEnabledAndFocusable(false);
		dtSentForTrial.setRequired(false);
		lblISentForTrial.setText(" ");
		dtSentForTrial.setError(false);
		
		CaseMethods.addRemoveFromMandatoryFields(dtSentForTrial, lblSentForTrial, mandatoryFields, false);

		dtDateOfCommittal.setEnabledAndFocusable(true);
		dtDateOfCommittal.setRequired(true);
		CaseMethods.addRemoveFromMandatoryFields(dtDateOfCommittal, lblDateOfCommittal, mandatoryFields, true);
		
		dtTransferDate.setCustomWarning(CaseMaintenanceConstants.ON_BEFORE_COMMITTAL);
		dtTransferDate.setToCompare(dtDateOfCommittal);
		dtTransferDate.crossValidateDate();

		txtPagesOfEvidence.setEnabled(true);
		txtPagesOfEvidence.setMandatory(true);

		CaseMethods.addRemoveFromMandatoryFields(txtPagesOfEvidence, lblPagesOfEvidence, mandatoryFields, true);
		
		if (txtPagesOfEvidence.getText() == null || txtPagesOfEvidence.getText().isEmpty()) {
			txtPagesOfEvidence.setError(true);
		} else {
			lblIPagesOfEvidence.setText(" ");
			txtPagesOfEvidence.setError(false);
		}
		
	}
	
	/**
	 * Disables date of comittal - non mandatory
	 * enables SFT - mandatory
	 * disables pages of evidence - non mandatory
	 */
	public void setNonVBCTFields () {
		dtDateOfCommittal.setEnabledAndFocusable(false);
		dtDateOfCommittal.setDate(todaysDate);
		dtDateOfCommittal.setRequired(false);
		lblIDateOfCommittal.setText(" ");
		dtDateOfCommittal.setError(false);
		
		CaseMethods.addRemoveFromMandatoryFields(dtDateOfCommittal, lblDateOfCommittal, mandatoryFields, false);

		dtSentForTrial.setEnabledAndFocusable(true);
		dtSentForTrial.setRequired(true);
		CaseMethods.addRemoveFromMandatoryFields(dtSentForTrial, lblSentForTrial, mandatoryFields, true);

		dtTransferDate.setCustomWarning(CaseMaintenanceConstants.ON_BEFORE_SFT);
		dtTransferDate.setToCompare(dtSentForTrial);
		dtTransferDate.crossValidateDate();

		txtPagesOfEvidence.setText(null);
		txtPagesOfEvidence.setEnabled(false);
		txtPagesOfEvidence.setMandatory(false);
		lblIPagesOfEvidence.setText(" ");
		txtPagesOfEvidence.setError(false);
		
		CaseMethods.addRemoveFromMandatoryFields(txtPagesOfEvidence, lblPagesOfEvidence, mandatoryFields, false);

	}
}