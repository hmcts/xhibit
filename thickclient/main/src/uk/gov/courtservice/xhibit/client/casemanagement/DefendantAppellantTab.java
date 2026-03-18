package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.defoncaserefsolfirm.DefOnCaseRefSolFirmControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.legalaidorder.LegalAidOrderControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.refsolicitorfirm.RefSolicitorFirmControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.LegalAidOrderBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.defoncasesolfirm.DefOnCaseRefSolFirmValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.casemanagement.privaterep.PrivateRepresentationDialog;
import uk.gov.courtservice.xhibit.client.casemanagement.privaterep.PrivateRepresentationModel;
import uk.gov.courtservice.xhibit.client.casemanagement.publicrep.PrivateToPublicRepresentationDialog;
import uk.gov.courtservice.xhibit.client.casemanagement.publicrep.PrivateToPublicRepresentationModel;
import uk.gov.courtservice.xhibit.client.casemanagement.publicrep.PublicRepresentationDialog;
import uk.gov.courtservice.xhibit.client.casemanagement.publicrep.PublicRepresentationModel;
import uk.gov.courtservice.xhibit.client.casemanagement.util.CaseMethods;
import uk.gov.courtservice.xhibit.client.comparator.DropdownCodeStringComparator;
import uk.gov.courtservice.xhibit.client.comparator.TicketTypeComparator;
import uk.gov.courtservice.xhibit.client.updatecase.AsnHelper;
import uk.gov.courtservice.xhibit.client.updatecase.PtiurnHelper;
import uk.gov.courtservice.xhibit.client.util.CaseMaintenanceConstants;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.XCheckBox;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractDateValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * 
 * @author C.Kudzin - Feb 21, 2018 - Implemented max length for text fields -
 *         CTX-1507
 * @author C.Kudzin - Mar 06, 2018 - Removing "Remove Defendant" button -
 *         CTX-1611
 * 
 * @author N.Walters - Mar 13 , 2018 - Updates for CTX-1413
 * @author C.Kudzin - 20 Mar 2018 - Changed old references to
 *         XHB_Legal_aid_order to use XHB_DEF_ON_CASE_REF_SOL_FIRM - CTX-1709
 */
public class DefendantAppellantTab extends XPanel implements ValidationListener {
	private static final long serialVersionUID = 1L;

	// Panels
	private JPanel defendantAppellantDetailWrapper;
	private JPanel maskedFlagPanel;
	private JPanel generalPanel;

	// Tables
	private JTable defendantAppellantSearchTable;

	// Text Fields
	private XTextField aSNTextBox;
	private XTextField nameTextBox;
	private XTextField pNCTextBox;
	private XTextField pTIURNTextBox;
	private XTextField maskNameTextBox;

	// Labels
	private JLabel aSNInvalidEntry;
	private JLabel pNCInvalidEntry;
	private JLabel pTIInvalidEntry;
	private JLabel juvenileInvalidEntry;
	private JLabel numberOfDefendantsLabel;
	private JLabel maskNameLabel;
	private JLabel magCourtConvictionWarningLabel;
	private JLabel originalDateOfSentenceWarningLabel;
	private JLabel dateDrivingDisqualSuspensionErrorLabel;
	private JLabel bCStatusWarningLabel;
	private JLabel firstDateWarningLabel;
	private JLabel finalDateWarningLabel;

	// DatePanels
	private XDatePanel magCourtConvictionDatePicker;
	private XDatePanel originalDateOfSentenceDatePicker;
	private XDatePanel dateDrivingDisqualSuspensionDatePicker;
	private XDatePanel firstDatePicker;
	private XDatePanel finalDatePicker;

	// Combo Boxes
	private XComboBox hateCrimeDropDownBox;
	private XComboBox bCStatusDropDown;
	private XComboBox nationalityComboBox = null;

	// Check Boxes
	private XCheckBox hateCrimeCheckBox = new XCheckBox();
	private XCheckBox maskedFlagCheckBox = new XCheckBox();
	private XCheckBox juvenileCheckBox;

	// Buttons
	private JButton defendantAppellantDetailsButton;
	private JButton addDefendantAppellantButton;
	private JButton privateRepresentationButton;
	private JButton representationOrderButton;

	// Strings
	private String defendantAppellant = "Defendant";
	private String gender = "";

	// Other variables
	private Calendar calendarDate = null;
	private ArrayList<RefSystemCodeBasicValue> collectionHateCodes;
	private DefaultComboBoxModel hateCodesModel;
	private final CaseType caseType;
	private final CaseStatus caseStatus;
	private XhibitApplicationController xac;

	private int defendantCount;
	private DefendantAppellantTab parentTab;
	private Integer caseID;
	private final Logger log = CSServices.getLogger(getClass());


	private final PtiurnHelper ptiurnHelper = new PtiurnHelper(); // specific
																	// validation
																	// for pti
																	// urn
	private final AsnHelper asnHelper = new AsnHelper(); // specific validation
															// for asn

	// Rework for grid bag
	private JPanel defendantPanel;
	private JPanel defendantDetailPanel;
	private JScrollPane scrollPane;
	private JPanel hateCrimePanel;
	private JPanel magistrateHearingPanel;
	private JLabel aSNLabel;
	private JLabel nameLabel;
	private JLabel pNCLabel;
	private JLabel pTILabel;
	private JLabel bCStatusLabel;
	private JLabel juvenileLabel;
	private JLabel magCourtConvDateLabel;
	private JLabel originalDateOfSentenceLabel;
	private JLabel dateDrivingDisqSuspLabel;
	private JLabel maskedFlagLabel;
	private JLabel nationalityLabel;
	private JLabel firstDateLabel;
	private JLabel finalDateLabel;
	private XTextField txtCaseNumber;
	private XTextField txtCaseTitle;

	private JPanel defendantDetailLeftPanel;
	private JPanel defendantDetailMidPanel;
	private JPanel defendantDetailRightPanel;

	private JPanel representationButtonPanel;

	// used for newer validation introduced using J.Uphill validation
	private DateValidationController firstDateValidation;
	private DateValidationController finalDateValidation;
	private DateValidationController originalDateOfSentenceDateValidation;
	private DateValidationController drivingDisqualSuspensionDateValidation;
	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
	private boolean invalid = false;

	private CaseXPanel caseX = null;

	// Added for SAYG as enabling save button wasnt done previously
	private boolean fieldsChangedGlobal = false;
	private List<Object> mandatoryFields = new ArrayList<Object>();

	private boolean ignoreChanges = false;

	private static final String CLASS_NAME = ".DefendantAppellantTab";

	/**
	 * Create the panel.
	 */
	public DefendantAppellantTab(final XhibitApplicationController xac, final CaseXPanel caseX) {
		this.setLayout(new GridBagLayout());
		this.caseX = caseX;
		parentTab = this;

		this.xac = xac;
		this.caseStatus = xac.getCaseStatus();
		caseType = caseStatus.getCaseType();
		// Set the value of the defendant / appellant string dependent on case
		// type.
		if (caseType == CaseType.APPEAL || caseType == CaseType.MISC) {
			defendantAppellant = "Appellant";
		}
		generalPanel = caseX.getGeneralPanel();

		// Defendant Appellant details
		addDefendantAppellantButton = new JButton("Add " + defendantAppellant);
		addDefendantAppellantButton.addActionListener(new DefendantAppellantSearchActionListener(this));

		// Defendant Appellant Details button
		defendantAppellantDetailsButton = new JButton(defendantAppellant + " Details");
		defendantAppellantDetailsButton.setEnabled(false);
		defendantAppellantDetailsButton.addActionListener(new DefendantAppellantAddAmendActionListener(this));

		// Table Wrapper
		scrollPane = new JScrollPane();

		// Defendant / Appellant Table
		defendantAppellantSearchTable = new JTable();
		scrollPane.setViewportView(defendantAppellantSearchTable);
		// Sort this out to be one set model
		defendantAppellantSearchTable
				.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Number", "Surname", "First Name",
						"Parent/Guardian", "DefendantAppellant", "DefendantValue", "DefendantOnCaseBasicValue" }));
		defendantAppellantSearchTable.setDefaultEditor(Object.class, null);
		defendantAppellantSearchTable.getCellSelectionEnabled();
		defendantAppellantSearchTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if (this.caseType != CaseType.MISC) {
			numberOfDefendantsLabel = new JLabel("Number of " + defendantAppellant + "(s) required: " + defendantCount);
			defendantAppellantSearchTable.getModel()
					.addTableModelListener(new DefAppTableModelListener(defendantAppellantSearchTable));

		} else {
			numberOfDefendantsLabel = new JLabel();
			numberOfDefendantsLabel.setText(" ");
		}
		defendantAppellantSearchTable.getColumnModel().getColumn(0).setPreferredWidth(90);
		defendantAppellantSearchTable.getColumnModel().getColumn(1).setPreferredWidth(180);
		defendantAppellantSearchTable.getColumnModel().getColumn(1).setMaxWidth(200);
		defendantAppellantSearchTable.getColumnModel().getColumn(2).setResizable(false);
		defendantAppellantSearchTable.getColumnModel().getColumn(2).setPreferredWidth(230);
		defendantAppellantSearchTable.getColumnModel().getColumn(3).setPreferredWidth(190);

		// Hide the Defendant Appellant
		defendantAppellantSearchTable.getColumnModel().getColumn(4).setMinWidth(0);
		defendantAppellantSearchTable.getColumnModel().getColumn(4).setMaxWidth(0);

		// Hide defendant Value
		defendantAppellantSearchTable.getColumnModel().getColumn(5).setMinWidth(0);
		defendantAppellantSearchTable.getColumnModel().getColumn(5).setMaxWidth(0);

		// Hide DefendantOnCase basic value
		defendantAppellantSearchTable.getColumnModel().getColumn(6).setMinWidth(0);
		defendantAppellantSearchTable.getColumnModel().getColumn(6).setMaxWidth(0);

		// Name Label
		nameLabel = new JLabel("Name");

		// Name Text Box
		nameTextBox = new XTextField();
		nameTextBox.setEditable(false);

		// ASN Label
		aSNLabel = new JLabel("ASN");

		// ASN Invalid Entry warning text
		aSNInvalidEntry = new JLabel(" ");
		aSNInvalidEntry.setForeground(Color.RED);

		// ASN TextBox
		aSNTextBox = new XTextField(20);
		aSNTextBox.setUpperCase(true);
		aSNTextBox.setMaxLength(20);
		aSNTextBox.setToolTipText(ResourceBundleHelper.getResource(XhibitBundles.UpdateDefendant, "AsnFormatToolTip"));
		aSNTextBox.addFocusListener(new ASNFocusListener());

		// PNC Label
		pNCLabel = new JLabel("PNC");

		// PNC Invalid Entry warning text
		pNCInvalidEntry = new JLabel(" ");
		pNCInvalidEntry.setForeground(Color.RED);

		// PNC Text Box
		pNCTextBox = new XTextField(10, "^[0-9]{9}[A-Z]$", pNCInvalidEntry, false);
		pNCTextBox.setUpperCase(true);
		pNCTextBox.setMaxLength(10);
		pNCTextBox.setGridBagLayout(true);

		// PTI Label
		pTILabel = new JLabel("PTI URN");

		// PTI Invalid Entry Warning Text
		pTIInvalidEntry = new JLabel(" ");
		pTIInvalidEntry.setForeground(Color.RED);

		// PTI Text Box
		pTIURNTextBox = new XTextField(11);
		pTIURNTextBox.setUpperCase(true);
		pTIURNTextBox.setMaxLength(11);
		pTIURNTextBox
				.setToolTipText(ResourceBundleHelper.getResource(XhibitBundles.UpdateDefendant, "PtiurnFormatToolTip"));

		pTIURNTextBox.addFocusListener(new PTIURNListener());

		// BC Label Status
		bCStatusLabel = new JLabel("B/C Status");
		CaseUtils.addMandatoryLabel(bCStatusLabel);

		// BC Status Warning Label
		bCStatusWarningLabel = new JLabel(" ");
		bCStatusWarningLabel.setForeground(Color.RED);

		// BC Status Drop down Box
		DropdownCodeStringValue[] bcStatus = { new DropdownCodeStringValue("Select B/C Status", ""),
				new DropdownCodeStringValue("Bail", "B"), new DropdownCodeStringValue("Custody", "C"),
				new DropdownCodeStringValue("In Care", "J"), new DropdownCodeStringValue("Not Applicable", "N") };
		bCStatusDropDown = new XComboBox(true, bCStatusWarningLabel);
		bCStatusDropDown.setGridBagLayout(true);
		bCStatusDropDown.setModel(new DefaultComboBoxModel(bcStatus));
		bCStatusDropDown.setRenderer(new DropdownBoxCellRender());
		bCStatusDropDown.setSelectedIndex(0);

		if (this.caseType != CaseType.MISC) {
			mandatoryFields.add(bCStatusDropDown);
		// If in care is selected then set the juvenile checkbox to ticked
		bCStatusDropDown.addItemListener(new BcStatusDropDownListener());
		}

		// juvenile Label
		juvenileLabel = new JLabel("Juvenile");

		// juvenile Check box
		juvenileCheckBox = new XCheckBox();
		juvenileCheckBox.addActionListener(new JuvenileCheckBoxListener());

		juvenileInvalidEntry = new JLabel(" ");
		juvenileInvalidEntry.setForeground(Color.RED);

		// Magistrates Court Conviction Date Label
		magCourtConvDateLabel = new JLabel("Mag. Court Conviction Date");
		
		// Magistrates Court conviction date warning label
		magCourtConvictionWarningLabel = new JLabel(" ");
		magCourtConvictionWarningLabel.setForeground(Color.RED);

		// Magistrates Court Conviction Date Chooser
		if (caseStatus.getCaseType() == CaseType.APPEAL || caseStatus.getCaseType() == CaseType.MISC) {
			magCourtConvictionDatePicker = new XDatePanel(defendantAppellantDetailWrapper, calendarDate, true);
			DateValidationController magCourtConvictionDateValidation = ValidationControllerFactory.createDateRequired(this,
					magCourtConvictionDatePicker, magCourtConvictionWarningLabel, new MagCourtValidator());
			
			validationControllers.add(magCourtConvictionDateValidation);
			magCourtConvictionDatePicker.addFocusListener(new MandatoryFieldsFocusListener());
		} else {
			magCourtConvictionDatePicker = new XDatePanel(defendantAppellantDetailWrapper, calendarDate, true,
					magCourtConvictionWarningLabel, CaseMaintenanceConstants.BEFORE);
			magCourtConvictionDatePicker.setGridBagLayout(true);
			magCourtConvictionDatePicker.setEnabledAndFocusable(false);

		}
		
		// Original Date of Sentence Label
		originalDateOfSentenceLabel = new JLabel("Original Date of Sentence");

		// Original Date of Sentence warning Label
		originalDateOfSentenceWarningLabel = new JLabel(" ");
		originalDateOfSentenceWarningLabel.setForeground(Color.RED);		

		// Date Driving Disqualification Suspension Label
		dateDrivingDisqSuspLabel = new JLabel("Date Driving Disqual Susp");

		// Date Driving Disqualification Warning Label
		dateDrivingDisqualSuspensionErrorLabel = new JLabel(" ");
		dateDrivingDisqualSuspensionErrorLabel.setForeground(Color.RED);

		// Date Driving Disqual Suspension Date picker & Original Date of Sentence Date picker
		if (caseStatus.getCaseType() == CaseType.APPEAL) {
			CaseMethods.addRemoveFromMandatoryFields(magCourtConvictionDatePicker, magCourtConvDateLabel, mandatoryFields, true);

			originalDateOfSentenceDatePicker = new XDatePanel(defendantAppellantDetailWrapper, calendarDate);
			originalDateOfSentenceDateValidation = ValidationControllerFactory.createDateRequired(this,
					originalDateOfSentenceDatePicker, originalDateOfSentenceWarningLabel, new OrigDateOfSentenceValidator());
			validationControllers.add(originalDateOfSentenceDateValidation);
			originalDateOfSentenceDatePicker.addFocusListener(new MandatoryFieldsFocusListener());
			CaseMethods.addRemoveFromMandatoryFields(originalDateOfSentenceDatePicker, originalDateOfSentenceLabel, mandatoryFields, true);
			
			dateDrivingDisqualSuspensionDatePicker = new XDatePanel(defendantAppellantDetailWrapper, calendarDate, false);
			drivingDisqualSuspensionDateValidation = ValidationControllerFactory.createDateValid(this,
					dateDrivingDisqualSuspensionDatePicker, dateDrivingDisqualSuspensionErrorLabel,
					new DrivingDisqualSuspensionValidator());
			validationControllers.add(drivingDisqualSuspensionDateValidation);
			
		} else {
			
			originalDateOfSentenceDatePicker = new XDatePanel(defendantAppellantDetailWrapper, calendarDate, true,
					originalDateOfSentenceWarningLabel, CaseMaintenanceConstants.BEFORE);
			originalDateOfSentenceDatePicker.setGridBagLayout(true);
			
			dateDrivingDisqualSuspensionDatePicker = new XDatePanel(defendantAppellantDetailWrapper, calendarDate,
					false, dateDrivingDisqualSuspensionErrorLabel, CaseMaintenanceConstants.BEFORE);
			dateDrivingDisqualSuspensionDatePicker.setGridBagLayout(true);
		}

		if (caseStatus.getCaseType() == CaseType.TRIAL || caseStatus.getCaseType() == CaseType.SENTENCE) {
			originalDateOfSentenceDatePicker.setEnabledAndFocusable(false);
			dateDrivingDisqualSuspensionDatePicker.setEnabledAndFocusable(false);
		}

		// Masked Flag Check Box
		if (getIsJuvenile()) {
			setIsMasked(true);
		}
		maskedFlagCheckBox.addItemListener(new MaskedFlagItemListener());

		// Masked Flag Label
		maskedFlagLabel = new JLabel("Mask Name");

		// Mask Name warning Label
		maskNameLabel = new JLabel(" ");
		maskNameLabel.setForeground(Color.RED);

		// Masked Flag Text Box
		maskNameTextBox = new XTextField(255, "^.{0,255}$", maskNameLabel, false);
		maskNameTextBox.setGridBagLayout(true);
		maskNameTextBox.setUpperCase(true);
		maskNameTextBox.setMaxLength(255);
		maskNameTextBox.setEnabled(!isEnabled());

		hateCrimeCheckBox.addItemListener(new HateCrimeListener());

		// Nationality Label
		nationalityLabel = new JLabel("Nationality");

		// First Date Label
		firstDateLabel = new JLabel("First Date");

		// First Date warning label
		firstDateWarningLabel = new JLabel(" ");
		firstDateWarningLabel.setForeground(Color.RED);

		// Final Date Label
		finalDateLabel = new JLabel("Final Date");


		// Final Date Warning Label
		finalDateWarningLabel = new JLabel(" ");
		finalDateWarningLabel.setForeground(Color.RED);

		// Final Date Date Picker
		if (caseStatus.getCaseType() == CaseType.TRIAL) {			
			firstDatePicker = new XDatePanel(magistrateHearingPanel, calendarDate);
			firstDateValidation = ValidationControllerFactory.createDateValid(this, firstDatePicker,
					firstDateWarningLabel, new FirstDateValidator());
			
			finalDatePicker = new XDatePanel(magistrateHearingPanel, calendarDate);
			finalDateValidation = ValidationControllerFactory.createDateValid(this, finalDatePicker,
					finalDateWarningLabel, new FinalDateValidator());
			
			// --- CTX-1844 - Start ---
			enableDisableTrialDate(firstDatePicker, firstDateValidation, firstDateLabel);
			enableDisableTrialDate(finalDatePicker, finalDateValidation, finalDateLabel);
			
			// --- CTX-1844 - End ---
		} else {

			defendantCount = 1;
			
			firstDatePicker = new XDatePanel(magistrateHearingPanel, calendarDate, false, firstDateWarningLabel);
			firstDatePicker.setGridBagLayout(true);
			firstDatePicker.setEnabledAndFocusable(false);
			
			finalDatePicker = new XDatePanel(magistrateHearingPanel, calendarDate, false, finalDateWarningLabel,
					CaseMaintenanceConstants.BEFORE);
			finalDatePicker.setGridBagLayout(true);
			finalDatePicker.setEnabledAndFocusable(false);
		}

		// Private Representation Button
		privateRepresentationButton = new JButton("Private Representation");
		privateRepresentationButton.setEnabled(false);
		privateRepresentationButton.addActionListener(new PrivateRepresentationButtonActionListener(xac));

		// Representation Order button
		representationOrderButton = new JButton("Representation Order");
		representationOrderButton.setEnabled(false);
		representationOrderButton.addActionListener(new RepresentationOrderListener());

		jbInit();

		defendantAppellantSearchTable.getSelectionModel().addListSelectionListener(new DefendantSearchTableListener());
	}

	private void saveValuesToDefendant(DefendantAppellant defendant) {
		// save text
		defendant.setPNC(getPNC());
		defendant.setMaskName(getMaskName());
		defendant.setGender(getGender());

		defendant.setPTIURN(getPtiUrn());
		defendant.setASN(getASN());

		// save dropdowns
		defendant.setBCStatus(getBCStatus());
		defendant.setNationality(getNationality());
		defendant.setHateCrime(getHateType());

		// save checkboxes
		defendant.setIsMasked(getIsMasked());
		defendant.setHasHateCrime(getHateCrime());
		defendant.setJuvenile(getIsJuvenile());

		// save dates
		if (caseStatus.getCaseType() == CaseType.APPEAL && getMagCourtConvictionDatePicker() != null) {
			defendant.setMagCourtConviction(getMagCourtConvictionDatePicker().getTime());
		}
		
		if (caseStatus.getCaseType() == CaseType.APPEAL && getOriginalDateOfSentenceDatePicker() != null) {
			defendant.setOriginalDateOfSentence(getOriginalDateOfSentenceDatePicker().getTime());
		}
		
		if (caseStatus.getCaseType() == CaseType.APPEAL && getDrivingDisqSuspendDate() != null) {
			defendant.setDateDrivingDisqual(getDrivingDisqSuspendDate().getTime());
		} else {
			defendant.setDateDrivingDisqual(null);
		}
	
		if (caseStatus.getCaseType() == CaseType.TRIAL && getMagCourtFirstHearingDate() != null) {
			defendant.setFirstDate(getMagCourtFirstHearingDate().getTime());
		}
		if (caseStatus.getCaseType() == CaseType.TRIAL && getMagCourtFinalHearingDate() != null) {
			defendant.setFinalDate(getMagCourtFinalHearingDate().getTime());
		}
		
	}

	/**
	 * Set the defendants details based on the defendant object passed through.
	 * 
	 * @param defendant
	 */
	private void setDefendantAppellantDetails(DefendantAppellant defendant) {
		ignoreChanges = true;
		// Populate the text boxes
		String name =  defendant.getFirstName() + " " + defendant.getSurname();
		setNameTextBox(name);
		setASN(defendant.getASN());
		setPNC(defendant.getPNC());
		setPtiUrn(defendant.getPTIURN());
		setGender(defendant.getGender());
		// populate the dropdowns
		setMaskName(defendant.getMaskName());
		if (defendant.getCurrentPrisonStatus() != null && defendant.getCurrentPrisonStatus().equals("Y")) {
			bCStatusDropDown.setEnabled(true);
			bCStatusDropDown.setSelectedIndex(2);
		}
		if (defendant.getGender().equals("Company")) {
			bCStatusDropDown.setSelectedIndex(4);
			bCStatusDropDown.setEnabled(false);
			CaseUtils.removeMandatoryLabel(bCStatusLabel);
			bCStatusWarningLabel.setText(" ");

			juvenileCheckBox.setSelected(false);
			juvenileCheckBox.setEnabled(false);
			CaseUtils.removeMandatoryLabel(juvenileLabel);

			nationalityComboBox.setSelectedIndex(0);
			nationalityComboBox.setEnabled(false);
			CaseUtils.removeMandatoryLabel(nationalityLabel);
		} else {
			CaseUtils.addMandatoryLabel(bCStatusLabel);
			bCStatusDropDown.setEnabled(true);
			setBCStatus(defendant.getBCStatus());

			juvenileCheckBox.setEnabled(true);
			setIsJuvenile(defendant.isJuvenile());

			nationalityComboBox.setEnabled(true);
			setNationality(defendant.getNationality());
			
		}
		setHateType(defendant.getHateCrime());
		

		// enable fields based on check box values
		setHateCrime(defendant.gethasHateCrime());
		hateCrimeDropDownBox.setEnabled(defendant.gethasHateCrime());

		// populate the date fields
		if (defendant.getMagCourtConviction() != null) {
			magCourtConvictionDatePicker.setDate(defendant.getMagCourtConviction());
		} 
		setOriginalDateOfSentenceDatePicker(defendant.getOriginalDateOfSentence());
		
		setDrivingDisqSuspendDate(defendant.getDateDrivingDisqual());

		if (defendant.getFirstDate() != null) {
			setMagCourtFirstHearingDate(defendant.getFirstDate());
		} else {
			firstDatePicker.clear();
			if (caseStatus.getCaseType() == CaseType.TRIAL) {
				enableDisableTrialDate(firstDatePicker, firstDateValidation, firstDateLabel);
			}
		}
		if (defendant.getFinalDate() != null) {
			setMagCourtFinalHearingDate(defendant.getFinalDate());
		} else {
			finalDatePicker.clear();
			if (caseStatus.getCaseType() == CaseType.TRIAL) {
				enableDisableTrialDate(finalDatePicker, finalDateValidation, finalDateLabel);
			}
		}

		// populate Masked check box
		setIsMasked(defendant.getIsMasked());
		maskNameTextBox.setEnabled(defendant.getIsMasked());
		ignoreChanges = false;
	}

	
	private boolean isValidReceiptType() {
		return((GeneralTrial) generalPanel).getReceiptType().equals("EW")
		|| ((GeneralTrial) generalPanel).getReceiptType().equals("IO")
		|| ((GeneralTrial) generalPanel).getReceiptType().equals("ST");
	}

	/**
	 * Called for trial cases , if receipt type is valid it enables first/final date, otherwise disables it
	 */
	private void enableDisableTrialDate(XDatePanel datePicker, DateValidationController validation, JLabel label) {
		if (((GeneralTrial) generalPanel).getReceiptType() != null) {
			if (isValidReceiptType()) {
				datePicker.setEnabledAndFocusable(true);
				validationControllers.add(validation);
				CaseMethods.addRemoveFromMandatoryFields(datePicker, label, mandatoryFields, true);
				datePicker.addFocusListener(new MandatoryFieldsFocusListener());
			} else {
				datePicker.clear();
				datePicker.setEnabledAndFocusable(false);
				CaseMethods.addRemoveFromMandatoryFields(datePicker, label, mandatoryFields, false);
				validationControllers.remove(validation);
			}
		}
	}
	public void callback(DefendantAppellant da, DefendantValue dv, boolean isNew) {
		log.debug("DefendantAppellantSearch.Callback da: (" + da.toString() + ")");
		log.debug("DefendantAppellantSearch.Callback dv: (" + dv.toString() + ")");
		String surname = da.getSurname();
		String firstName = da.getFirstName();
		String parentGuardian = da.getParentGuardian();
		DefaultTableModel model = (DefaultTableModel) defendantAppellantSearchTable.getModel();
		int noRows = model.getRowCount();

		int selected = defendantAppellantSearchTable.getSelectedRow();

		DefendantOnCaseBasicValue copyDOC = null;

		if (selected >= 0)
			copyDOC = (DefendantOnCaseBasicValue) defendantAppellantSearchTable.getValueAt(selected, 6);

		if (isNew) {
			Object[] row = { noRows + 1, surname, firstName, parentGuardian, da, dv, null };
			model.addRow(row);
			defendantAppellantSearchTable.setRowSelectionInterval(noRows, noRows);

		} else {
			defendantAppellantSearchTable.setValueAt(surname, selected, 1);
			defendantAppellantSearchTable.setValueAt(firstName, selected, 2);
			defendantAppellantSearchTable.setValueAt(parentGuardian, selected, 3);
			defendantAppellantSearchTable.setValueAt(da, selected, 4);
			defendantAppellantSearchTable.setValueAt(dv, selected, 5);
			defendantAppellantSearchTable.setValueAt(copyDOC, selected, 6);

			try {
				CaseBasicValue cbv = XhibitDelegateHelper.getCaseDelegate().getCase(getCaseID());
				if (cbv.getCaseListed() != null && cbv.getCaseListed().equals("Y") && (da.isDataChanged() || dv.isDataChanged())) {
						getDefDelegate().updateDifferenceReport("Y",
								da.getDefendantOnCaseId(), XhibitSingleton.getInstance().getUserSession()
										.getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
						/**
						 * update the object stored in the table and then
						 * update with the values as the database object
						 * would have been changed now
						 **/
						DefendantOnCaseBasicValue val = getDefDelegate()
								.getDefendantOnCaseDetails(da.getDefendantOnCaseId());
						defendantAppellantSearchTable.setValueAt(val, selected, 6);
				}
			} catch (CaseControllerException e) {
				log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e);
				XHIBITConstant.handleError(e, this.getClass());
			} catch (DefendantControllerException e1) {
				log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e1);
				XHIBITConstant.handleError(e1, this.getClass());
			}
		}

		noRows = model.getRowCount();
		setNumberOfDefendantsLabel(getDefendantCount());

		defendantAppellantDetailsButton.setEnabled(true);
		fieldsChangedGlobal = true;
		caseX.getFinishButton().setEnabled(false);

		setDefendantAppellantDetails(da);
		checkMandatoryFields();
		// save current details of current object
		if (noRows > 0) {
			saveCurrentDetailsOnCaseCreate();
		}
		defendantAppellantDetailsButton.requestFocusInWindow();
	}

	public void updateCaseCallback(DefendantAppellant da, DefendantValue dv, DefendantOnCaseBasicValue def) {
		String surname = da.getSurname();
		String firstName = da.getFirstName();
		String parentGuardian = da.getParentGuardian();
		DefaultTableModel model = (DefaultTableModel) defendantAppellantSearchTable.getModel();
		int noRows = model.getRowCount();

		// save current details of current object
		if (noRows > 0) {
			saveCurrentDetailsOnCaseCreate();
		}

		Integer positionInTable = da.getPositionInTable();
		if (positionInTable != null && (!model.getValueAt(positionInTable, 1).equals(""))) {
				model.removeRow(positionInTable);
		}
		noRows = model.getRowCount();
		Object[] row = { noRows + 1, surname, firstName, parentGuardian, da, dv, def };
		model.addRow(row);

		// --- Hide last columns ---
		defendantAppellantSearchTable.getColumnModel().getColumn(model.getColumnCount() - 1).setMinWidth(0);
		defendantAppellantSearchTable.getColumnModel().getColumn(model.getColumnCount() - 1).setMaxWidth(0);
		defendantAppellantSearchTable.getColumnModel().getColumn(model.getColumnCount() - 2).setMinWidth(0);
		defendantAppellantSearchTable.getColumnModel().getColumn(model.getColumnCount() - 2).setMaxWidth(0);
		defendantAppellantSearchTable.getColumnModel().getColumn(model.getColumnCount() - 3).setMinWidth(0);
		defendantAppellantSearchTable.getColumnModel().getColumn(model.getColumnCount() - 3).setMaxWidth(0);

		setNumberOfDefendantsLabel(getDefendantCount());
		defendantAppellantSearchTable.setRowSelectionInterval(noRows, noRows);

		defendantAppellantDetailsButton.setEnabled(true);
		setDefendantAppellantDetails(da);
	}

	public boolean defendantAppellantIsDuplicate(DefendantAppellant da) {
		log.debug("DefendantAppellantSearch.defendantAppellantIsDuplicate(" + da.toString() + ")");
		int defendantId = da.getDefendantId();
		boolean defendantIdDuplicate = false;

		DefaultTableModel model = (DefaultTableModel) defendantAppellantSearchTable.getModel();
		int noRows = model.getRowCount();

		// checking for duplicate entries
		for (int i = 0; i < noRows; i++) {
			int defIdb = ((DefendantValue) model.getValueAt(i, 5)).getDefendantID();
			if (defIdb == defendantId) {
				log.debug("Defendant ID duplicate found");
				defendantIdDuplicate = true;
			}
		}
		// end of checking
		return (defendantIdDuplicate);
	}

	private class DefendantAppellantSearchActionListener implements ActionListener {
		DefendantAppellantTab parent;

		DefendantAppellantSearchActionListener(DefendantAppellantTab parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			addDefendantAppellantButton.setEnabled(defendantCount != defendantAppellantSearchTable.getRowCount());

			DefendantAppellantSearchDialog defendantAppellantSearch;
			try {
				defendantAppellantSearch = new DefendantAppellantSearchDialog(xac,
						new DefendantAppellantSearchModel(xac, parent));
				defendantAppellantSearch.setLocationRelativeTo(xac);
				defendantAppellantSearch.setVisible(true);
			} catch (CSRecoverableException e1) {
				log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e1);
				XHIBITConstant.handleError(e1, this.getClass());
			}
		}
	}

	private class DefendantAppellantAddAmendActionListener implements ActionListener {
		DefendantAppellantTab parent;

		DefendantAppellantAddAmendActionListener(DefendantAppellantTab parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			/*
			 * C.Kudzin - CTX-1262 Required to save the details so that call
			 * back method dosn't clear the screen
			 */
			saveCurrentDetailsOnCaseCreate();

			DefaultTableModel model = (DefaultTableModel) defendantAppellantSearchTable.getModel();
			int row = model.getRowCount();
			int selected = defendantAppellantSearchTable.getSelectedRow();
			if (row != 0 && selected != -1) {

				// Variables gathered from selected rows
				DefendantAppellant dA = (DefendantAppellant) model.getValueAt(selected, 4);
				dA.setPositionInTable(selected);
				dA.setCourtId(XhibitSingleton.getInstance().getCourtId());
				dA.setCaseId(getCaseID());

				DefendantAppellantAddAmendDialog defendantAppellantAmend;
				try {
					DefendantAppellantAddAmendModel mod = new DefendantAppellantAddAmendModel(dA.getDefendantId(),
							dA.getPositionInTable(), parent);
					mod.setDA(dA);
					defendantAppellantAmend = new DefendantAppellantAddAmendDialog(xac, mod, null);
					defendantAppellantAmend.setLocationRelativeTo(xac);
					defendantAppellantAmend.setVisible(true);
				} catch (CSRecoverableException e1) {
					log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e1);
					XHIBITConstant.handleError(e1, this.getClass());
				}
			}
		}
	}

	/**
	 * Returns a JComboBox that displays a list of Countries and their
	 * associated description.
	 * 
	 * @return JComboBox
	 */
	private XComboBox getNationalityComboBox() {
		if (nationalityComboBox == null) {
			try {
				Collection<String> countriesCollection = getBizDelegate().findAllNationalities();
				nationalityComboBox = new XComboBox();
				nationalityComboBox.setEnabled(true);
				ArrayList<DropdownCodeStringValue> nationalities = new ArrayList<DropdownCodeStringValue>();
				DropdownCodeStringValue ukVal = null;
				Iterator<String> it = countriesCollection.iterator();
				while (it.hasNext()) {
					String full = it.next();
					DropdownCodeStringValue val = new DropdownCodeStringValue(
							WordUtils.capitalizeFully(full.split(": ")[1]), full.split(": ")[0]);
					if(val.getDisplayName().equalsIgnoreCase("United Kingdom")) {
						ukVal = val;
					} else {
					nationalities.add(val);
				}
				}

				Collections.sort(nationalities, new DropdownCodeStringComparator());
				// Default value
				DropdownCodeStringValue val = new DropdownCodeStringValue("Select Nationality", null);
				nationalities.add(0, val);
				if(ukVal !=null) {
					nationalities.add(1,ukVal);
				}
				if (!nationalities.isEmpty()) {
					nationalityComboBox.setModel(new DefaultComboBoxModel(nationalities.toArray()));
					nationalityComboBox.setRenderer(new DropdownBoxCellRender());
				}

			} catch (SysRefControllerException e) {
				CSRecoverableException csre = new CSRecoverableException(
						"gui.updateDefendantActtion.defendantvalueupdate",
						"Exception whilst reading the xhb_ref_nationality table", e);
				XHIBITErrorHandler.handleError(csre);
			}
		}
		return nationalityComboBox;
	}

	private XComboBox gethateCrimeDropDownBox() throws CSRecoverableException {
		if (collectionHateCodes == null) {
			collectionHateCodes = getHateCodes();
			hateCodesModel = new DefaultComboBoxModel(collectionHateCodes.toArray());
		}

		if (hateCrimeDropDownBox == null  && !collectionHateCodes.isEmpty()) {
			hateCrimeDropDownBox = new XComboBox(hateCodesModel);
			hateCrimeDropDownBox.setRenderer(new DropdownBoxCellRender());
			hateCrimeDropDownBox.setEnabled(getHateCrime());
		}
		return hateCrimeDropDownBox;

	}

	private ArrayList<RefSystemCodeBasicValue> getHateCodes() throws CSRecoverableException {
		RefSystemCodeBasicValue val = new RefSystemCodeBasicValue();
		val.setDecode("Select Type");
		ArrayList<RefSystemCodeBasicValue> hateList = new ArrayList<RefSystemCodeBasicValue>();
		Collections.sort(hateList, new TicketTypeComparator());

		try {
			RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
			criteria.setCodeType("HATE_TYPE");
			criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
			hateList = (ArrayList<RefSystemCodeBasicValue>) (getBizDelegate()
					.findSystemCodes(criteria));
			Collections.sort(hateList, new TicketTypeComparator());
		} catch (BisRefControllerException e) {
			log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e);
			XHIBITConstant.handleError(e, this.getClass());
		}
		hateList.add(0, val);

		return hateList;
	}

	public BisRefControllerBeanBusinessDelegate getBizDelegate() {
		return XhibitDelegateHelper.getBizRefDelegate();
	}
	
	public LegalAidOrderControllerBeanBusinessDelegate getLegalDelegate() {
		return XhibitDelegateHelper.getLegalAidDelegate();
	}
	
	public  DefendantControllerBeanBusinessDelegate getDefDelegate() {
		return XhibitDelegateHelper.getDefendantDelegate();
	}


	protected class DefAppTableModelListener implements TableModelListener {
		JTable table;

		DefAppTableModelListener(JTable table) {
			this.table = table;
		}

		public void tableChanged(TableModelEvent e) {
			// if a row is not selected hide the wrapper
			int row = e.getFirstRow();
			if (row > -1) {
				enableTabsIfMisc();
			} else {
				defendantDetailPanel.setEnabled(false);
				defendantDetailPanel.setVisible(true);
			}
		}
	}

	public XComboBox getbCStatusDropDown() {
		return bCStatusDropDown;
	}

	public int getDefendantInTableCount() {
		return defendantAppellantSearchTable.getRowCount();
	}

	public int getDefendantCount() {
		return defendantCount;
	}

	public void setDefendantCount(Integer defendantCount) {
		this.defendantCount = defendantCount;
		setNumberOfDefendantsLabel(defendantCount);
	}

	public void setNumberOfDefendantsLabel(Integer defendantCount) {
		int rows = defendantAppellantSearchTable.getRowCount();
		this.numberOfDefendantsLabel
				.setText("Number of " + defendantAppellant + "(s) required: " + (defendantCount - rows));
		addDefendantAppellantButton.setEnabled(defendantCount > this.defendantAppellantSearchTable.getRowCount());
		
	}

	public List<DefendantAppellant> getDefendants() {
		ArrayList<DefendantAppellant> def = new ArrayList<DefendantAppellant>();
		for (int i = 0; i < defendantAppellantSearchTable.getRowCount(); i++) {
			def.add(((DefendantAppellant) (defendantAppellantSearchTable.getValueAt(i,
					defendantAppellantSearchTable.getColumnCount() - 3))));
		}
		return def;
	}

	// saves the current defendant details when user clicks on create case
	public void saveCurrentDetailsOnCaseCreate() {
		saveValuesToDefendant((DefendantAppellant) defendantAppellantSearchTable.getValueAt(
				defendantAppellantSearchTable.getSelectedRow(), defendantAppellantSearchTable.getColumnCount() - 3));
	}

	public boolean anyErrors() {
		return (!aSNInvalidEntry.getText().equals(" ") || !pNCInvalidEntry.getText().equals(" ")
				|| !pTIInvalidEntry.getText().equals(" ") || !maskNameLabel.getText().equals(" ")
				|| !bCStatusWarningLabel.getText().equals(" ") || !juvenileInvalidEntry.getText().equals(" ")
				|| invalid);
	}

	public boolean multipleAnyErrors() {
		for (int i = 0; i < defendantAppellantSearchTable.getRowCount(); i++) {

			DefendantAppellant d = (DefendantAppellant) (defendantAppellantSearchTable.getValueAt(i,
					defendantAppellantSearchTable.getColumnCount() - 3));
			if(!isValidPNC(d)) {
				log.error("PNC is invalid for defendant " + (i + 1));
				return true;
			}
			
			if(!isValidASN(d)) {
				log.error("Asn is invalid for defendant " + (i + 1));
				return true;
			}
			
			if(!isValidPTIURN(d)) {
				log.error("PTI URN is invalid for defendant " + (i + 1));			
				return true;
			}
			
			if (d.getBCStatus() != null && d.getBCStatus().getDisplayName().equals(CaseMaintenanceConstants.IN_CARE) && !d.isJuvenile()) {
				log.error("Cannot have BC status in care and juvenile unticked for defendant " + (i + 1));
				return true;
			}
			// all the date field validations
			if (invalid) {
				return true;
			}

		}
		return false;
	}

	/**
	 * returns true if valid ptiurn.
	 */
	private boolean isValidPTIURN(DefendantAppellant d) {
		// ptiurn
		if (d.getPTIURN() != null && !d.getPTIURN().equals("")) {
			try {
				ptiurnHelper.validatePtiurn(d.getPTIURN());
			} catch (CSValidationException e) {
				return false;
			}
		}
		return true;
	}
	

	/**
	 * True if valid asn.
	 */
	private boolean isValidASN(DefendantAppellant d) {
		// asn
		if (d.getASN() != null && !d.getASN().equals("")) {
			try {
				asnHelper.validateAsn(d.getASN());
			} catch (CSValidationException e) {
				return false;
			}
		}
		return true;
	}

	/**
	 * True if valid pnc else false.
	 * @return boolean
	 */
	private boolean isValidPNC(DefendantAppellant d) {
		// check all values are valid - pnc
		if (d.getPNC() != null && !d.getPNC().equals("")) {
			String regex = "^[0-9]{9}[A-Z]$";
			if (!d.getPNC().matches(regex)) {
				return false;
			}
		}
		return true;
	}

	public JButton getDefendantAppellantDetailsButton() {
		return defendantAppellantDetailsButton;
	}

	// *******************************************************************************
	// * public void populate(CaseBasicValue caseBasicValue)
	// *
	// * Purpose : Pre-populate fields from given case-id
	// * To call : caseId - ID of case to populate fields from
	// * Returns : Nothing
	// * Notes :
	// *******************************************************************************

	public void populate(CaseBasicValue caseBasicValue) {
		log.debug("DefendantAppellantTab.populate");

		setCaseID(caseBasicValue.getCaseId());
		populateCaseNumberAndTitle(caseBasicValue.getCaseType() + "" + caseBasicValue.getCaseNumber(),
				caseBasicValue.getCaseTitle());

		setMagCourtConvictionDatePicker(caseBasicValue.getMagConvictionDate());
		setOriginalDateOfSentenceDatePicker(caseBasicValue.getLcSentDate());

		if (caseBasicValue.getNoDefendantsForCase() != null) {
			setDefendantCount(caseBasicValue.getNoDefendantsForCase());
			if (caseBasicValue.getNoDefendantsForCase() > 0) {
				ArrayList<DefendantOnCaseBasicValue> arr = new ArrayList<DefendantOnCaseBasicValue>();
				try {
					// find defendant by case Id
					arr = (ArrayList<DefendantOnCaseBasicValue>) getDefDelegate().findByCaseId(caseBasicValue.getCaseId());
					//sort by defendant number 
					Collections.sort(arr, new Comparator<DefendantOnCaseBasicValue>() {
						public int compare(DefendantOnCaseBasicValue defendant1, DefendantOnCaseBasicValue defendant2) {
							return defendant1.getDefendantNumber() - defendant2.getDefendantNumber();
						}
					});
					if (!arr.isEmpty()) {
						// only set to visible if it has any results and if
						// case type is not misc
						enableTabsIfMisc();
						
						for (DefendantOnCaseBasicValue def : arr) {
							updateVals(def, caseBasicValue);
						}
					}
				} catch (Exception e) {
					log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e);
					XHIBITConstant.handleError(e, this.getClass());
				}
			}
		}		
	}

	/**
	 * If misc then enable defendant details panel
	 * @param caseType2
	 */
	private void enableTabsIfMisc() {
		if (caseType != CaseType.MISC) {
			defendantDetailPanel.setEnabled(true);
			defendantDetailPanel.setVisible(true);
		}
		
	}

	private void updateVals(DefendantOnCaseBasicValue def, CaseBasicValue caseBasicValue) throws FinderException {
		DefendantValue dV;
		//defendant is mandatory in db so can't be null
		Integer defId=def.getDefendantID();
		DefendantAppellant dA = new DefendantAppellant();
		
		//Set date fields
		dA.setDateDrivingDisqual(def.getDrivingDisqSuspendedDate());
		dA.setOriginalDateOfSentence(caseBasicValue.getLcSentDate());
		dA.setFirstDate(def.getMagCourtFirstHearingDate());
		dA.setFinalDate(def.getMagCourtFinalHearingDate());
		//Have to have null as magconvictiondate could be null
		if (caseBasicValue.getMagConvictionDate() != null) {
			dA.setMagCourtConviction(caseBasicValue.getMagConvictionDate().getTime());
		}

		dV = getDefDelegate().findByDefId(defId);
		dA.setDefendantId(dV.getDefendantID());
		dA.setDefendantOnCaseId(def.getId());

		//set the strings
		dA.setSurname(dV.getSurName());
		dA.setOtherName(dV.getMiddleName());
		dA.setFirstName(dV.getFirstName());
		dA.setParentGuardian(dV.getParentGuardianName());
		dA.setInitials(dV.getInitials());
		dA.setGender(dV.getGenderString());
		dA.setASN(def.getAsn());
		dA.setCurrentPrisonStatus(dV.getCurrentPrisonStatus());
		dA.setPNC(def.getPncId());
		dA.setPTIURN(def.getPtiurn());

		if (def.getIsJuvenile() != null) {
			dA.setJuvenile(convertStringToBoolean(def.getIsJuvenile()));
		}
		
		if (def.getIsMasked() != null) {
			dA.setIsMasked(convertStringToBoolean(def.getIsMasked()));
			dA.setMaskName(def.getMaskedName());
		}

		if (def.getHateIndicator() != null) {
			dA.setHasHateCrime(convertStringToBoolean(def.getHateIndicator()));
		}

		// dropdown values
		if (getBCStatusDropdownVal(def.getCommBcStatus()) != null) {
			dA.setBCStatus(getBCStatusDropdownVal(def.getCommBcStatus()));
		}
		if (getHateTypeDropdownVal(def.getHateType()) != null) {
			dA.setHateCrime(getHateTypeDropdownVal(def.getHateType()));
		}
		if (getNationalityDropdownVal(def.getNationality()) != null) {
			dA.setNationality(getNationalityDropdownVal(def.getNationality()));
		}
		updateCaseCallback(dA, dV, def);
	}

	public XDatePanel getMagCourtConvictionDatePanel() {
		return magCourtConvictionDatePicker;
	}

	public Calendar getMagCourtConvictionDatePicker() {
		try {
			if (magCourtConvictionDatePicker.getDate() != null) {
				return magCourtConvictionDatePicker.getDate();
			} else {
				return null;
			}
		} catch (CSValidationException e2) {
			// At this point we don't want to throw an error because we are
			// displaying the 'invalid date'
			// message already on the screen so below is there for completeness
			log.error("Mag court conviction date is incorrect");
			return null;
		}
	}

	public void setMagCourtConvictionDatePicker(Calendar date) {
		if (date != null) {
			magCourtConvictionDatePicker.setDate(date);
		}
	}

	public XDatePanel getOriginalDateOfSentenceDatePanel() {
		return originalDateOfSentenceDatePicker;
	}

	public Calendar getOriginalDateOfSentenceDatePicker() {
		try {
			if (originalDateOfSentenceDatePicker.getDate() != null) {
				return originalDateOfSentenceDatePicker.getDate();
			} else {
				return null;
			}
		} catch (CSValidationException e) {
			// At this point we don't want to throw an error because we are
			// displaying the 'invalid date'
			// message already on the screen so below is there for completeness
			log.error("Original date of sentence is incorrect");
			return null;
		}
	}

	public JLabel getOriginalDateOfSentenceWarningLabel() {
		return originalDateOfSentenceWarningLabel;
	}

	public void setOriginalDateOfSentenceDatePicker(Date date) {
		if (date != null) {
			originalDateOfSentenceDatePicker.setDate(date);
		}
	}

	public String getASN() {
		return aSNTextBox.getText();
	}

	public void setASN(String text) {
		if (text != null) {
			aSNTextBox.setText(text);
		} else {
			aSNTextBox.setText("");
		}
	}

	public String getPNC() {
		return pNCTextBox.getText();
	}

	public void setPNC(String text) {
		if (text != null) {
			pNCTextBox.setText(text);
		} else {
			pNCTextBox.setText("");
		}

	}

	public String getPtiUrn() {
		return pTIURNTextBox.getText();
	}

	public void setPtiUrn(String text) {
		if (text != null) {
			pTIURNTextBox.setText(text);
		} else {
			pTIURNTextBox.setText("");
		}
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMaskName() {
		return maskNameTextBox.getText();
	}

	public void setMaskName(String text) {
		if (text != null) {
			maskNameTextBox.setText(text);
		} else {
			maskNameTextBox.setText("");
		}
	}

	public boolean getIsMasked() {
		return maskedFlagCheckBox.isSelected();
	}

	public void setIsMasked(boolean selected) {
		maskedFlagCheckBox.setSelected(selected);
	}

	public boolean getHateCrime() {
		return hateCrimeCheckBox.isSelected();
	}

	public void setHateCrime(boolean selected) {
		hateCrimeCheckBox.setSelected(selected);
	}

	public Calendar getMagCourtFirstHearingDate() {
		try {
			if (firstDatePicker.getDate() != null) {
				return firstDatePicker.getDate();
			} else {
				return null;
			}
		} catch (CSValidationException e) {
			log.error("First hearing date is incorrect");
			return null;
		}
	}

	public void setMagCourtFirstHearingDate(Calendar date) {
		log.debug("setMagCourtFirstHearingDate");
		if (date != null) {
			firstDatePicker.setDate(date);
		}
		// --- CTX-1884 - Start ---
		if (caseType.equals(CaseType.TRIAL)) {
			enableDisableTrialDate(firstDatePicker, firstDateValidation, firstDateLabel);
	}
		// --- CTX-1884 - End ---
	}

	public void setMagCourtFirstHearingDate(Date date) {
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			setMagCourtFirstHearingDate(cal);
		}
	}

	public Calendar getMagCourtFinalHearingDate() {
		try {
			if (finalDatePicker.getDate() != null) {
				return finalDatePicker.getDate();
			} else {
				return null;
			}
		} catch (CSValidationException e) {
			// At this point we don't want to throw an error because we are
			// displaying the 'invalid date'
			// message already on the screen so below is there for completeness
			log.error("Final hearing date is incorrect");
			return null;
		}
	}

	public void setMagCourtFinalHearingDate(Calendar date) {
		log.debug("setMagCourtFinalHearingDate");
		if (date != null) {
			finalDatePicker.setDate(date);
		}
		// --- CTX-1884 - Start ---
		if (caseType.equals(CaseType.TRIAL)) {
			enableDisableTrialDate(finalDatePicker, finalDateValidation, finalDateLabel);
	}
		// --- CTX-1884 - End ---
	}

	public void setMagCourtFinalHearingDate(Date date) {
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			setMagCourtFinalHearingDate(cal);
		}
	}

	public DropdownCodeStringValue getNationality() {
		return ((DropdownCodeStringValue) nationalityComboBox.getSelectedItem());
	}

	public void setNationality(DropdownCodeStringValue value) {
		if (value != null) {
			nationalityComboBox.setSelectedItem(value);
		} else {
			nationalityComboBox.setSelectedIndex(0);
		}
	}

	private DropdownCodeStringValue getNationalityDropdownVal(String code) {
		if (code != null && !code.isEmpty()) {
			for (int j = 1; j < nationalityComboBox.getItemCount(); j++) {
				if (((DropdownCodeStringValue) nationalityComboBox.getItemAt(j)).getCode().equals(code)) {
					return (DropdownCodeStringValue) nationalityComboBox.getItemAt(j);
				}
			}
		}
		return null;
	}

	public RefSystemCodeBasicValue getHateType() {
		return ((RefSystemCodeBasicValue) hateCrimeDropDownBox.getSelectedItem());
	}

	public void setHateType(RefSystemCodeBasicValue value) {
		if (value != null) {
			hateCrimeDropDownBox.setSelectedItem(value);
		} else {
			hateCrimeDropDownBox.setSelectedIndex(0);
		}
	}

	private RefSystemCodeBasicValue getHateTypeDropdownVal(String code) {
		if (code != null && !code.isEmpty()) {
			for (int j = 1; j < hateCrimeDropDownBox.getItemCount(); j++) {
				if (((RefSystemCodeBasicValue) hateCrimeDropDownBox.getItemAt(j)).getCode().equals(code)) {
					return (RefSystemCodeBasicValue) hateCrimeDropDownBox.getItemAt(j);
				}
			}
		}
		return null;
	}

	public Calendar getDrivingDisqSuspendDate() {
		try {
			if (dateDrivingDisqualSuspensionDatePicker.getDate() != null) {
				return dateDrivingDisqualSuspensionDatePicker.getDate();
			} else {
				return null;
			}
		} catch (CSValidationException e3) {
			// At this point we don't want to throw an error because we are
			// displaying the 'invalid date'
			// message already on the screen so below is there for completeness
			log.error("Driving disq suspend date is incorrect");
			return null;
		}
	}

	public void setDrivingDisqSuspendDate(Date date) {
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			dateDrivingDisqualSuspensionDatePicker.setDate(cal);
		}
	}

	public boolean getIsJuvenile() {
		return juvenileCheckBox.isSelected();
	}

	public void setIsJuvenile(boolean selected) {
		juvenileCheckBox.setSelected(selected);
	}

	public boolean convertStringToBoolean(String selected) {
		if (selected != null) {
			return selected.equals("Y");
		}
		return false;
	}

	public DropdownCodeStringValue getBCStatus() {
		return ((DropdownCodeStringValue) bCStatusDropDown.getSelectedItem());
	}

	public void setBCStatus(DropdownCodeStringValue value) {
		if (value != null) {
			bCStatusDropDown.setSelectedItem(value);
		} else {
			bCStatusDropDown.setSelectedIndex(0);
		}
	}

	private DropdownCodeStringValue getBCStatusDropdownVal(String code) {
		if (code != null) {
			for (int j = 1; j < bCStatusDropDown.getItemCount(); j++) {
				if (((DropdownCodeStringValue) bCStatusDropDown.getItemAt(j)).getCode().equals(code)) {
					return (DropdownCodeStringValue) bCStatusDropDown.getItemAt(j);
				}
			}
		}
		return null;
	}

	public String getNameTextBox() {
		return nameTextBox.getText();
	}

	public void setNameTextBox(String text) {
		if (text != null) {
			nameTextBox.setText(text);
		}
	}

	public XDatePanel getFirstDatePicker() {
		return firstDatePicker;
	}

	public XDatePanel getFinalDatePicker() {
		return finalDatePicker;
	}

	private class PrivateRepresentationButtonActionListener implements ActionListener {
		private final XhibitApplicationController xac;

		public PrivateRepresentationButtonActionListener(XhibitApplicationController xac) {
			this.xac = xac;
		}

		public void actionPerformed(ActionEvent e)  {
			DefaultTableModel model = (DefaultTableModel) defendantAppellantSearchTable.getModel();
			saveCurrentDetailsOnCaseCreate();
			DefendantAppellant defApp = (DefendantAppellant) (model
					.getValueAt(defendantAppellantSearchTable.getSelectedRow(), model.getColumnCount() - 3));
			ArrayList<DefOnCaseRefSolFirmValue> privateOrPublicRep = new ArrayList<DefOnCaseRefSolFirmValue>();
			try {
				privateOrPublicRep = getRep();
				
				if (!privateOrPublicRep.isEmpty() && (privateOrPublicRep.get(0).getRepType().equals("L"))) {
					ArrayList<LegalAidOrderBasicValue> legalAid = (ArrayList<LegalAidOrderBasicValue>)(getLegalDelegate().findByDefendantOnCaseId(privateOrPublicRep.get(0).getDefendantOnCaseId()));
					if (!legalAid.isEmpty() && legalAid.get(0).getDateOfRevocation()==null) {
						// If the defendant has public representation open
						// the following pop up
						JOptionPane.showMessageDialog(null, "The " + defendantAppellant
								+ " already has a public representation order.\nPlease click the Representation Order button to add/amend a representation");
					}
					else {
						DefOnCaseRefSolFirmValue def = new DefOnCaseRefSolFirmValue();
						def.setDefendantOnCaseId(defApp.getDefendantOnCaseId());
						showPrivateRepWindow(def);
					}
				} else if (!privateOrPublicRep.isEmpty() && (privateOrPublicRep.get(0).getRepType().equals("P"))) {
					showPrivateRepWindow(privateOrPublicRep.get(0));
				} else {
					DefOnCaseRefSolFirmValue def = new DefOnCaseRefSolFirmValue();
					def.setDefendantOnCaseId(defApp.getDefendantOnCaseId());
					showPrivateRepWindow(def);
				}
			} catch (CSRecoverableException e1) {
				log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e1);
				XHIBITConstant.handleError(e1, this.getClass());

			} catch (CSUnrecoverableException e1) {
				log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e1);
				XHIBITConstant.handleError(e1, this.getClass());
			}
		}
		private PrivateRepresentationModel getPrivateModel(DefOnCaseRefSolFirmValue docRefSolFirmValue) {

			PrivateRepresentationModel model = new PrivateRepresentationModel(docRefSolFirmValue.getDefendantOnCaseId(),
					parentTab, caseType);
			model.setDocRefSolFirm(docRefSolFirmValue);
			return model;
		}
		private void showPrivateRepWindow(DefOnCaseRefSolFirmValue def) throws CSRecoverableException {
			PrivateRepresentationDialog dialog = new PrivateRepresentationDialog(xac, getPrivateModel(def));
			dialog.setLocationRelativeTo(xac);
			dialog.setVisible(true);

		}

	}

	public List<DefendantOnCaseBasicValue> populateDefendantOnCaseBasicValue(Integer caseId) {
		ArrayList<DefendantOnCaseBasicValue> defList = new ArrayList<DefendantOnCaseBasicValue>();

		for (int i = 0; i < defendantAppellantSearchTable.getRowCount(); i++) {
			DefendantAppellant defApp = (DefendantAppellant) (defendantAppellantSearchTable.getValueAt(i,
					defendantAppellantSearchTable.getColumnCount() - 3));
			DefendantOnCaseBasicValue defendant = ((DefendantOnCaseBasicValue) (defendantAppellantSearchTable
					.getValueAt(i, defendantAppellantSearchTable.getColumnCount() - 1)));
			if (defendant == null) {
				defendant = new DefendantOnCaseBasicValue(defApp.getDefendantOnCaseId(), null);
				defendant.setCaseID(caseId);
				defendant.setDefendantID(defApp.getDefendantId());
				defendant = createNewDefForPopulating(defendant, defApp);
			} else {
				if (defendant.getCurrentBcStatus() != null && !defendant.getCurrentBcStatus()
							.equals(defApp.getBCStatus() != null ? defApp.getBCStatus().getCode() : null) ){
						JOptionPane.showMessageDialog((Component) null,
								"CAUTION: B/C status on Committal is " + defApp.getBCStatus().getCode()
										+ " but current B/C status is " + defendant.getCurrentBcStatus()
										+ " ; contact the Listing Officer to amend if necessary");
				}
				// set values of defendant on case
				defendant = createNewDefForPopulating(defendant, defApp);
			}

			// ctx-1922, defendant number wasn't saved
			defendant.setDefendantNumber(i + 1);

			CaseBasicValue caseBasicValue;
			try {
				caseBasicValue = XhibitDelegateHelper.getCaseDelegate().getCase(caseId);
				defendant.setDateOfCommittal((caseX.returnCommittalDate(caseBasicValue)));
			} catch (CaseControllerException e) {
				log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e);
				XHIBITConstant.handleError(e, this.getClass());
			}

			defList.add(defendant);
		}
		return defList;
	}

	private DefendantOnCaseBasicValue createNewDefForPopulating(DefendantOnCaseBasicValue def1, DefendantAppellant defApp) {
		def1.setAsn(defApp.getASN());
		def1.setCommBcStatus(defApp.getBCStatus().getCode() != null ? defApp.getBCStatus().getCode() : null);
		def1.setIsJuvenile(defApp.isJuvenile() ? "Y" : "N");
		def1.setDrivingDisqSuspendedDate(defApp.getDateDrivingDisqual() != null
						? new Timestamp(defApp.getDateDrivingDisqual().getTime()) : null);
		def1.setPncId(defApp.getPNC());
		def1.setPtiurn(defApp.getPTIURN());
		def1.setIsMasked(defApp.getIsMasked() ? "Y" : "N");
		def1.setMaskedName(defApp.getIsMasked() ? defApp.getMaskName() : "");
		def1.setHateIndicator(defApp.gethasHateCrime() ? "Y" : "N");
		def1.setHateType(defApp.gethasHateCrime() && defApp.getHateCrime() != null ? defApp.getHateCrime().getCode()
						: null);
		def1.setMagCourtFirstHearingDate(defApp.getFirstDate() != null ? new Timestamp(defApp.getFirstDate().getTime()) : null);
		def1.setMagCourtFinalHearingDate(defApp.getFinalDate() != null ? new Timestamp(defApp.getFinalDate().getTime()) : null);
		def1.setNationality(defApp.getNationality().getCode() != null ? defApp.getNationality().getCode() : null);
		
		
		return def1;
	}

	public Integer getCaseID() {
		return caseID;
	}

	public void setCaseID(Integer caseID) {
		if(caseID!=null) {
			this.caseID = caseID;
		}
	}

	private ArrayList<DefOnCaseRefSolFirmValue> getRep() {

		// get the currently selected defendant
		DefaultTableModel model = (DefaultTableModel) defendantAppellantSearchTable.getModel();
		saveCurrentDetailsOnCaseCreate();
		DefendantAppellant defApp = (DefendantAppellant) (model
				.getValueAt(defendantAppellantSearchTable.getSelectedRow(), model.getColumnCount() - 3));
		DefOnCaseRefSolFirmControllerBeanBusinessDelegate docRSFCBBD = XhibitDelegateHelper
				.getDefOnCaseRefSolFirmDelegate();
		
		return  (ArrayList<DefOnCaseRefSolFirmValue>)(docRSFCBBD.findPrivateRepByDefendantOnCaseId(defApp.getDefendantOnCaseId()));

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

	// Initialises layout of defendant tab
	private void jbInit() {
		GridBagConstraints gbc = getDefaultGridBagConstraints();

		txtCaseNumber = new XTextField();
		txtCaseNumber.setDisabledTextColor(Color.BLACK);
		txtCaseNumber.setEnabled(false);
		txtCaseNumber.setColumns(4);
		txtCaseNumber.setMinimumSize(txtCaseNumber.getPreferredSize());
		gbc.weightx = 0.05;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 20, 5, 20);
		this.add(txtCaseNumber, gbc);

		gbc.gridx++;
		gbc.weightx = 0.45;
		txtCaseTitle = new XTextField();
		txtCaseTitle.setDisabledTextColor(Color.BLACK);
		txtCaseTitle.setEnabled(false);
		txtCaseTitle.setColumns(10);
		txtCaseTitle.setMinimumSize(txtCaseTitle.getPreferredSize());
		this.add(txtCaseTitle, gbc);

		gbc.gridx++;
		this.add(Box.createRigidArea(txtCaseTitle.getPreferredSize()), gbc);

		gbc.gridx -= 2;
		gbc.gridy++;
		gbc.gridwidth = 3;
		gbc.weightx = 1;
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		this.add(getDefendantPanel(), gbc);

		gbc.gridy++;
		JSeparator separator = new JSeparator();
		this.add(separator, gbc);

		gbc.gridy++;
		this.add(getDefendantDetailPanel(), gbc);
		this.add(Box.createRigidArea(defendantDetailPanel.getPreferredSize()), gbc);

		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridy++;
		this.add(getRepresentationButtonPanel(), gbc);
	}

	/**
	 * Default gridbag that's used throughout the panels.
	 * 
	 * @return gridbagconstraints
	 */
	private GridBagConstraints getDefaultGridBagConstraints() {
		return new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				XHIBITConstant.nonContainerInsets, 0, 0);
	}

	/*
	 * Initialises the defendant panel (top-half of screen)
	 */
	private JPanel getDefendantPanel() {
		if (defendantPanel == null) {
			defendantPanel = new JPanel();
			defendantPanel.setLayout(new GridBagLayout());
			defendantPanel.setBorder(BorderFactory.createTitledBorder(defendantAppellant));
			GridBagConstraints gbc = getDefaultGridBagConstraints();

			gbc.anchor = GridBagConstraints.WEST;
			gbc.weightx = 0.9;
			defendantPanel.add(numberOfDefendantsLabel, gbc);

			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridy++;
			gbc.gridheight = 4;
			defendantPanel.add(scrollPane, gbc);

			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.gridx++;
			gbc.weightx = 0.1;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			defendantPanel.add(addDefendantAppellantButton, gbc);

			gbc.gridy++;
			defendantPanel.add(defendantAppellantDetailsButton, gbc);

			gbc.gridx--;
			gbc.gridy++;
			defendantPanel.add(Box.createRigidArea(numberOfDefendantsLabel.getPreferredSize()), gbc);

			CaseMethods.addChangeListeners(defendantPanel.getComponents(), caseX);
		}
		return defendantPanel;
	}

	/*
	 * Initialises the defendant detail panel (bottom-half of screen)
	 */
	private JPanel getDefendantDetailPanel() {
		if (defendantDetailPanel == null) {
			defendantDetailPanel = new JPanel();
			defendantDetailPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getDefaultGridBagConstraints();

			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.fill = GridBagConstraints.BOTH;

			// Separated into three separate panels to minimize resize issues
			gbc.weightx = 0.7;
			defendantDetailPanel.add(getDefendantDetailLeftPanel(), gbc);

			gbc.gridx++;
			gbc.weightx = 0.3;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			// gbc.insets = new Insets(4, 4, 4, 40); // to separate panels
			// nicely
			defendantDetailPanel.add(getDefendantDetailMidPanel(), gbc);

			gbc.gridx++;
			gbc.weightx = 0.2;
			// top, left, bottom, right
			gbc.insets = new Insets(4, 40, 4, 40); // to separate panels nicely
			gbc.anchor = GridBagConstraints.CENTER;
			defendantDetailPanel.add(getDefendantDetailRightPanel(), gbc);

			defendantDetailPanel.setEnabled(false);
			defendantDetailPanel.setVisible(false);

			CaseMethods.addChangeListeners(defendantDetailPanel.getComponents(), caseX);
		}
		return defendantDetailPanel;
	}

	private JPanel getDefendantDetailLeftPanel() {
		if (defendantDetailLeftPanel == null) {
			defendantDetailLeftPanel = new JPanel();
			defendantDetailLeftPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getDefaultGridBagConstraints();
			gbc.fill = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.HORIZONTAL;

			// First column - labels
			// Empty label to get same alignment as mid detail panel due to
			// error label not above Name field
			gbc.weighty = 0.1;
			gbc.weightx = 0.1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			defendantDetailLeftPanel.add(new JLabel(" "), gbc);

			gbc.gridy++;
			gbc.weighty = 0.2;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			defendantDetailLeftPanel.add(nameLabel, gbc);

			gbc.gridy += 2;
			defendantDetailLeftPanel.add(aSNLabel, gbc);

			gbc.gridy += 2;
			defendantDetailLeftPanel.add(bCStatusLabel, gbc);

			gbc.gridy += 2;
			defendantDetailLeftPanel.add(juvenileLabel, gbc);

			gbc.gridy += 2;
			defendantDetailLeftPanel.add(magCourtConvDateLabel, gbc);

			gbc.gridy += 2;
			defendantDetailLeftPanel.add(originalDateOfSentenceLabel, gbc);

			gbc.gridy += 2;
			defendantDetailLeftPanel.add(dateDrivingDisqSuspLabel, gbc);

			// Second column - entry fields
			gbc.gridy = 1;
			gbc.gridx++;
			gbc.weightx = 0.3;
			nameTextBox.setColumns(10);
			nameTextBox.setMinimumSize(nameTextBox.getPreferredSize());
			defendantDetailLeftPanel.add(nameTextBox, gbc);

			gbc.gridy += 2;
			aSNTextBox.setColumns(10);
			aSNTextBox.setMinimumSize(aSNTextBox.getPreferredSize());
			defendantDetailLeftPanel.add(aSNTextBox, gbc);

			gbc.gridy += 2;
			defendantDetailLeftPanel.add(bCStatusDropDown, gbc);

			gbc.gridy += 2;
			gbc.fill = GridBagConstraints.NONE;
			defendantDetailLeftPanel.add(juvenileCheckBox, gbc);

			gbc.gridy += 2;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			defendantDetailLeftPanel.add(magCourtConvictionDatePicker, gbc);

			gbc.gridy += 2;
			defendantDetailLeftPanel.add(originalDateOfSentenceDatePicker, gbc);

			gbc.gridy += 2;
			defendantDetailLeftPanel.add(dateDrivingDisqualSuspensionDatePicker, gbc);

			// Second column - error labels
			gbc.gridx++;
			defendantDetailLeftPanel.add(Box.createRigidArea(new Dimension(50, 10)), gbc);
			gbc.gridx--;

			gbc.gridy = 2;
			gbc.weighty = 0.1;
			gbc.gridwidth = 2;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			defendantDetailLeftPanel.add(aSNInvalidEntry, gbc);

			gbc.gridy += 2;
			defendantDetailLeftPanel.add(bCStatusWarningLabel, gbc);

			// For proper spacing of Juvenile checkbox
			gbc.gridy += 2;
			defendantDetailLeftPanel.add(juvenileInvalidEntry, gbc);

			gbc.gridy += 2;
			defendantDetailLeftPanel.add(magCourtConvictionWarningLabel, gbc);

			gbc.gridy += 2;
			defendantDetailLeftPanel.add(originalDateOfSentenceWarningLabel, gbc);

			gbc.gridy += 2;
			defendantDetailLeftPanel.add(dateDrivingDisqualSuspensionErrorLabel, gbc);

			CaseMethods.addChangeListeners(defendantDetailLeftPanel.getComponents(), caseX);
		}
		return defendantDetailLeftPanel;
	}

	private JPanel getDefendantDetailMidPanel() {
		if (defendantDetailMidPanel == null) {
			defendantDetailMidPanel = new JPanel();
			defendantDetailMidPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getDefaultGridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;

			// First column - labels and also panels
			gbc.gridy = 1; // 0 is where first error label is
			gbc.weightx = 0.1;
			gbc.weighty = 0.2; // 0.2 for entry fields, 0.1 for error labels
			defendantDetailMidPanel.add(pNCLabel, gbc);

			gbc.gridy += 2;
			defendantDetailMidPanel.add(pTILabel, gbc);

			gbc.gridy++;
			gbc.anchor = GridBagConstraints.NORTHEAST;
			gbc.fill = GridBagConstraints.NONE;
			Insets newInsets = new Insets(4, 0, 0, -51); // To get checkboxes to
															// line up in panel
			gbc.insets = newInsets;
			defendantDetailMidPanel.add(maskedFlagCheckBox, gbc);

			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridwidth = 2;
			defendantDetailMidPanel.add(getMaskedFlagPanel(), gbc);

			gbc.gridy++;
			gbc.anchor = GridBagConstraints.NORTHEAST;
			gbc.fill = GridBagConstraints.NONE;
			Insets newInsets2 = new Insets(4, 0, 0, -51); // To get checkboxes
															// to line up in
															// panel
			gbc.insets = newInsets2;
			gbc.gridwidth = 1;
			defendantDetailMidPanel.add(hateCrimeCheckBox, gbc);

			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridwidth = 2;
			defendantDetailMidPanel.add(getHateCrimePanel(), gbc);

			gbc.gridy++;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			defendantDetailMidPanel.add(nationalityLabel, gbc);

			// Second column - entry fields
			gbc.gridwidth = 1;
			gbc.gridy = 1;
			gbc.gridx++;
			gbc.weightx = 0.3;
			pNCTextBox.setColumns(10);
			pNCTextBox.setMinimumSize(pNCTextBox.getPreferredSize());
			defendantDetailMidPanel.add(pNCTextBox, gbc);

			gbc.gridy += 2;
			pTIURNTextBox.setColumns(10);
			pTIURNTextBox.setMinimumSize(pTIURNTextBox.getPreferredSize());
			defendantDetailMidPanel.add(pTIURNTextBox, gbc);

			gbc.gridy += 3; // account for the masked flag and hate crime panels
			defendantDetailMidPanel.add(getNationalityComboBox(), gbc);

			// Second column - error labels
			gbc.gridy = 0;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weighty = 0.1;
			defendantDetailMidPanel.add(pNCInvalidEntry, gbc);

			gbc.gridy += 2;
			defendantDetailMidPanel.add(pTIInvalidEntry, gbc);

			gbc.gridx++;
			defendantDetailMidPanel.add(Box.createRigidArea(new Dimension(50, 10)), gbc);

			CaseMethods.addChangeListeners(defendantDetailMidPanel.getComponents(), caseX);
		}
		return defendantDetailMidPanel;
	}

	private JPanel getDefendantDetailRightPanel() {
		if (defendantDetailRightPanel == null) {
			defendantDetailRightPanel = new JPanel();
			defendantDetailRightPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getDefaultGridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;

			defendantDetailRightPanel.add(getMagistrateHearingPanel(), gbc);
		}
		return defendantDetailRightPanel;
	}

	private JPanel getMaskedFlagPanel() {
		if (maskedFlagPanel == null) {
			maskedFlagPanel = new JPanel();
			maskedFlagPanel.setLayout(new GridBagLayout());
			maskedFlagPanel.setBorder(BorderFactory.createTitledBorder("Masked Flag"));

			GridBagConstraints gbc = getDefaultGridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;

			gbc.weightx = 0.1;
			gbc.gridy++;
			maskedFlagPanel.add(maskedFlagLabel, gbc);

			gbc.gridx++;
			gbc.weightx = 0.9;
			maskedFlagPanel.add(maskNameTextBox, gbc);

			gbc.gridy--;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			maskedFlagPanel.add(maskNameLabel, gbc);

			CaseMethods.addChangeListeners(maskedFlagPanel.getComponents(), caseX);
		}
		return maskedFlagPanel;
	}

	private JPanel getHateCrimePanel() {
		if (hateCrimePanel == null) {
			hateCrimePanel = new JPanel();
			hateCrimePanel.setLayout(new GridBagLayout());
			hateCrimePanel.setBorder(BorderFactory.createTitledBorder("Hate Crime"));

			GridBagConstraints gbc = getDefaultGridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;

			try {
				hateCrimePanel.add(gethateCrimeDropDownBox(), gbc);
			} catch (CSRecoverableException e) {
				log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e);
				XHIBITConstant.handleError(e, this.getClass());
			}

			CaseMethods.addChangeListeners(hateCrimePanel.getComponents(), caseX);
		}
		return hateCrimePanel;
	}

	private JPanel getMagistrateHearingPanel() {
		if (magistrateHearingPanel == null) {
			magistrateHearingPanel = new JPanel();
			magistrateHearingPanel.setLayout(new GridBagLayout());
			magistrateHearingPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
					"Magistrate Hearing", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc = getDefaultGridBagConstraints();

			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 0.15;
			gbc.gridy = 1;
			gbc.weighty = 0.2;
			magistrateHearingPanel.add(firstDateLabel, gbc);

			gbc.gridy += 2;
			magistrateHearingPanel.add(finalDateLabel, gbc);

			gbc.weightx = 0.85;
			gbc.gridx++;
			gbc.gridy -= 2;
			magistrateHearingPanel.add(firstDatePicker, gbc);

			gbc.gridy += 2;
			magistrateHearingPanel.add(finalDatePicker, gbc);

			gbc.gridy = 0;
			gbc.weighty = 0.1;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			magistrateHearingPanel.add(firstDateWarningLabel, gbc);

			gbc.gridy += 2;
			magistrateHearingPanel.add(finalDateWarningLabel, gbc);

			CaseMethods.addChangeListeners(magistrateHearingPanel.getComponents(), caseX);
		}
		return magistrateHearingPanel;
	}

	private JPanel getRepresentationButtonPanel() {
		if (representationButtonPanel == null) {
			representationButtonPanel = new JPanel();
			representationButtonPanel.setLayout(new GridBagLayout());

			GridBagConstraints gbc = getDefaultGridBagConstraints();
			gbc.insets = new Insets(5, 20, 5, 20);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			representationButtonPanel.add(representationOrderButton, gbc);

			gbc.gridx++;
			representationButtonPanel.add(privateRepresentationButton, gbc);
		}

		return representationButtonPanel;
	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		invalid = (validationController.hasErrors()
				|| !ValidationControllerFactory.validateComponents(validationControllers));
	}

	/**
	 * Called from CaseXPanel. Loops over defendants for future changes - in
	 * case there's multiple defendants in future
	 */
	public boolean validateForDateAppealLodged(Calendar cal) {
		for (int i = 0; i < defendantAppellantSearchTable.getRowCount(); i++) {
			DefendantAppellant d = (DefendantAppellant) (defendantAppellantSearchTable.getValueAt(i,
					defendantAppellantSearchTable.getColumnCount() - 3));
			if (d.getOriginalDateOfSentence() != null && d.getOriginalDateOfSentence().after(cal.getTime())) {
				return false;
			}
			if (d.getMagCourtConviction() != null && d.getMagCourtConviction().after(cal.getTime())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Called from CaseXPanel.
	 */
	public boolean validateForSentForTrial(Calendar cal) {

		// loop over every defendant
		for (int i = 0; i < defendantAppellantSearchTable.getRowCount(); i++) {
			DefendantAppellant d = (DefendantAppellant) (defendantAppellantSearchTable.getValueAt(i,
					defendantAppellantSearchTable.getColumnCount() - 3));

			if (d.getFirstDate() != null && d.getFirstDate().after(cal.getTime())) {
				return false;
			}
			if (d.getFinalDate() != null && d.getFinalDate().after(cal.getTime())) {
				return false;
			}
		}
		return true;
	}

	public void setFieldsChangedGlobal(boolean fieldsChangedGlobal) {
		if (!ignoreChanges) {
			this.fieldsChangedGlobal = fieldsChangedGlobal;
			checkMandatoryFields();
		}
	}
	
	public class MandatoryFieldsFocusListener implements FocusListener {
		@Override
		public void focusGained(FocusEvent e) {
			//Don't want to do anything on gained only on lost
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (!ignoreChanges) {
				checkMandatoryFields();
			}		
		}
	}
	
	public void checkMandatoryFields() {
		// enable createcase button if all mandatory fields are entered
		boolean isValid = true;

		if (!xac.getCaseStatus().isCaseType(CaseType.MISC)) {
			isValid = CaseMethods.isAllMandatoryFieldsEntered(mandatoryFields);
		} else {
			fieldsChangedGlobal = true;
		}

		boolean fieldsChanged = isValid && fieldsChangedGlobal;

		caseX.getCreateButton().setEnabled(fieldsChanged);
		if (fieldsChangedGlobal)
			caseX.getFinishButton().setEnabled(!fieldsChangedGlobal);

		CaseMethods.enableTabbedPane(caseX);
	}

	public void clearChangedState() {
		fieldsChangedGlobal = false;
	}

	public void updateXAC(XhibitApplicationController newXac) {
		this.xac = newXac;
	}


	public void populateCaseNumberAndTitle(String caseNumber, String caseTitle) {
		txtCaseNumber.setText(caseNumber);
		txtCaseTitle.setText(caseTitle);
	}

	public void populateCaseTitle(String caseTitle) {
		txtCaseTitle.setText(caseTitle);
	}

	/**
	 * Called from CaseXPanel.
	 */
	public void clearTable() {
		DefaultTableModel model = (DefaultTableModel) defendantAppellantSearchTable.getModel();
		model.setRowCount(0);
	}

	/**
	 * Used for traversal policy
	 */
	public FocusTraversalOnArray getTabbedPaneOrder() {
		return new FocusTraversalOnArray(new Component[] { addDefendantAppellantButton, defendantAppellantDetailsButton,
				aSNTextBox, bCStatusDropDown, juvenileCheckBox, magCourtConvictionDatePicker,
				originalDateOfSentenceDatePicker, dateDrivingDisqualSuspensionDatePicker, pNCTextBox, pTIURNTextBox,
				maskedFlagCheckBox, maskNameTextBox, hateCrimeCheckBox, hateCrimeDropDownBox, nationalityComboBox,
				firstDatePicker, finalDatePicker, representationOrderButton, privateRepresentationButton,
				caseX.getCreateButton(), caseX.getFinishButton(), caseX.getCancelButton() });

	}
	//Clear errors - this is used when you flip between defendants on a trial/sentence case
	public void clearErrorLabels() {
		pNCInvalidEntry.setText(" ");
		aSNInvalidEntry.setText(" ");
		pTIInvalidEntry.setText(" ");
		bCStatusWarningLabel.setText(" ");
		juvenileInvalidEntry.setText(" ");
	    firstDateWarningLabel.setText(" ");
		finalDateWarningLabel.setText(" ");

	}	
	
	/**
	 * When ASN is changed.
	 *
	 */
	public class ASNFocusListener implements FocusListener {
		@Override
		public void focusGained(FocusEvent e) {
			//Don't want to do anything on gained only on lost

		}

		@Override
		public void focusLost(FocusEvent e) {
			if (aSNTextBox.getText() != null && !aSNTextBox.getText().equals("")) {
				try {
					asnHelper.validateAsn(aSNTextBox.getText());
					aSNInvalidEntry.setText(" ");
				} catch (CSValidationException exc) {
					log.error("Invalid ASN entered");
					aSNInvalidEntry.setText(CaseMaintenanceConstants.INVALID_ENTRY);
				}
			} else {
				aSNInvalidEntry.setText(" ");
			}
		}
	}
	
	/**
	 * When PTIUrn is changed.
	 */
	public class PTIURNListener implements FocusListener {
		@Override
		public void focusGained(FocusEvent e) { 
			//Don't want to do anything on gained only on lost
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (pTIURNTextBox.getText() != null && !pTIURNTextBox.getText().equals("")) {
				try {
					ptiurnHelper.validatePtiurn(pTIURNTextBox.getText());
					pTIInvalidEntry.setText(" ");
				} catch (CSValidationException exc) {
					log.error("Invalid PTI URN entered");
					pTIInvalidEntry.setText(CaseMaintenanceConstants.INVALID_ENTRY);
				}
			} else {
				pTIInvalidEntry.setText(" ");
			}
		}
	}
	
	public class JuvenileCheckBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			AbstractButton abstractButton = (AbstractButton) e.getSource();
			boolean selected = abstractButton.getModel().isSelected();
			if(!ignoreChanges) {
				fieldsChangedGlobal = true;
				checkMandatoryFields();
			}

			DefendantOnCaseBasicValue defendant = ((DefendantOnCaseBasicValue) (defendantAppellantSearchTable.getValueAt(
					defendantAppellantSearchTable.getSelectedRow(), defendantAppellantSearchTable.getColumnCount() - 1)));
			
			if (!selected) {
				if(defendant == null) {
					maskedFlagCheckBox.setSelected(false);
					maskNameTextBox.setText("");
				}

				// if the dropdown is in care then display error
				if (((DropdownCodeStringValue) bCStatusDropDown.getSelectedItem()).getDisplayName()
						.equals(CaseMaintenanceConstants.IN_CARE)) {
					JOptionPane.showMessageDialog((Component) null,
							"Bail/Custody status implies this defendant is a juvenile. Defendant cannot be saved until the combination is corrected",
							"Error", JOptionPane.ERROR_MESSAGE);
					juvenileInvalidEntry.setText("Must be selected");
				} else {
					juvenileInvalidEntry.setText(" ");
				}

			} else {
				if(defendant == null) {
					maskedFlagCheckBox.setSelected(true);
					maskNameTextBox.setText("*****");
				}
				juvenileInvalidEntry.setText(" ");
			}
		}
	}
	
	/**
	 * When value changes in the def search table.
	 */
	public class DefendantSearchTableListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			log.debug("Selection event" + e);
			//clear the error labels
			clearErrorLabels();
			// When the row is selected display the values in the second
			// half of the page.
			if (defendantAppellantSearchTable.getSelectedRowCount() == 1) {
				DefaultTableModel model = (DefaultTableModel) defendantAppellantSearchTable.getModel();
				if (model.getRowCount() > 1) {
					int fromRow;
					int toRow;
					toRow = defendantAppellantSearchTable.getSelectedRow();
					if (toRow == e.getFirstIndex()) {
						fromRow = e.getLastIndex();
					} else {
						fromRow = e.getFirstIndex();
					}
					if (!e.getValueIsAdjusting()) {
							//setErrorLabels
							setErrorLabels((DefendantAppellant) (model.getValueAt(toRow, model.getColumnCount() - 3)));
						saveValuesToDefendant(
								(DefendantAppellant) (model.getValueAt(fromRow, model.getColumnCount() - 3)));
						setDefendantAppellantDetails(
								(DefendantAppellant) (model.getValueAt(toRow, model.getColumnCount() - 3)));
					}
				}
			}
			checkDefendantSaved();
		}
		private void checkDefendantSaved() {
			if (defendantAppellantSearchTable.getSelectedRow() < 0) {
				return;
			}
			DefendantOnCaseBasicValue defendant = ((DefendantOnCaseBasicValue) (defendantAppellantSearchTable.getValueAt(
					defendantAppellantSearchTable.getSelectedRow(), defendantAppellantSearchTable.getColumnCount() - 1)));

			if (defendant != null) {
				int defOnCaseId = defendant.getDefendantOnCaseId();

				if (defOnCaseId > 0) {
					// this is attached to case
					privateRepresentationButton.setEnabled(true);
					representationOrderButton.setEnabled(true);
				} else {
					privateRepresentationButton.setEnabled(false);
					representationOrderButton.setEnabled(false);
				}
			} else {
				privateRepresentationButton.setEnabled(false);
				representationOrderButton.setEnabled(false);
			}
		}
		private void setErrorLabels(DefendantAppellant defendant) {
			pNCTextBox.validate(defendant.getPNC());
			if(isValidASN(defendant)) {
				aSNInvalidEntry.setText(" ");
				aSNTextBox.setForeground(Color.black);
			} else {
				log.error("Invalid ASN entered");
				aSNInvalidEntry.setText(CaseMaintenanceConstants.INVALID_ENTRY);
				aSNTextBox.setForeground(new Color(244, 0, 0));
			}	
			
			if(isValidPTIURN(defendant)){
				pTIInvalidEntry.setText(" ");
				pTIURNTextBox.setForeground(Color.black);
			} else {
				log.error("Invalid PTI URN entered");
				pTIInvalidEntry.setText(CaseMaintenanceConstants.INVALID_ENTRY);
				pTIURNTextBox.setForeground(new Color(244, 0, 0));
			}
		}
	}
	
	/**
	 * When rep order button is clicked.
	 */
	public class RepresentationOrderListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				// if it has rep then
				ArrayList<DefOnCaseRefSolFirmValue> crsfv = getRep();
				if (!crsfv.isEmpty() && crsfv.get(0).getRepType() != null) {
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
								new PublicRepresentationModel(crsfv.get(0).getDefendantOnCaseId(), parentTab, caseType));
						dialog.setLocationRelativeTo(xac);
						dialog.setVisible(true);
					}
				}
				// else show public rep
				else {
					DefaultTableModel model = (DefaultTableModel) defendantAppellantSearchTable.getModel();
					saveCurrentDetailsOnCaseCreate();

					DefendantAppellant defApp = (DefendantAppellant) (model.getValueAt(
							defendantAppellantSearchTable.getSelectedRow(), model.getColumnCount() - 3));
					PublicRepresentationDialog dialog = new PublicRepresentationDialog(xac,
							new PublicRepresentationModel(defApp.getDefendantOnCaseId(), parentTab, caseType));
					dialog.setLocationRelativeTo(xac);
					dialog.setVisible(true);
				}
			} catch (CSRecoverableException e1) {
				log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e1);
				XHIBITConstant.handleError(e1, this.getClass());
			} catch (CSUnrecoverableException e1) {
				log.error(CaseMaintenanceConstants.ERROR_IN + CaseMaintenanceConstants.PACKAGE_NAME + CLASS_NAME + " : " + e1);
				XHIBITConstant.handleError(e1, this.getClass());
			}

		}
		private PrivateToPublicRepresentationModel getPrivateToPublicModel(DefOnCaseRefSolFirmValue crsfv) {

			PrivateToPublicRepresentationModel model = new PrivateToPublicRepresentationModel(
					crsfv.getDefendantOnCaseId(), parentTab, caseType);
			RefSolicitorFirmControllerBeanBusinessDelegate refSol = XhibitDelegateHelper
					.getRefSolicitorFirmController();
				RefSolicitorFirmComplexValue refSolValue = null;
			if (crsfv.getRefSolicitorFirmId() != null) {
				refSolValue = refSol.findByPK(crsfv.getRefSolicitorFirmId());
			}
			if (refSolValue != null) {
				model.setSolicitorName(refSolValue.getSolicitorFirmName());
				model.setSolicitorAddress1(refSolValue.getAddress1());
				model.setSolicitorAddress2(refSolValue.getAddress2());
				model.setSolicitorAddress3(refSolValue.getAddress3());
				model.setSolicitorAddress4(refSolValue.getAddress4());
				model.setSolicitorTown(refSolValue.getTown());
				model.setSolicitorCounty(refSolValue.getCounty());
				model.setSolicitorPostCode(refSolValue.getPostcode());
				model.setSolicitorDocExRef(refSolValue.getDxRef());
				model.setSolicitorTelephoneNo(refSolValue.getTelephoneNumber());
				model.setSolicitorFaxNo(refSolValue.getFaxNumber());
				model.setSolicitorSecureEmail(refSolValue.getSecureEmailAddress());
				model.setSolicitorNonSecureEmail(refSolValue.getNonsecureEmailAddress());
			}
			model.setSolicitorRef(crsfv.getSolicitorRef());
			model.setStartDate(crsfv.getRepStDate());
			model.setEndDate(crsfv.getRepEndDate());
			return model;
		}
	}
	
	/**
	 * Enabled/disables hate crime dropdown box.
	 *
	 */
	public class HateCrimeListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if(!ignoreChanges) {
				fieldsChangedGlobal = true;
			}

			if (hateCrimeCheckBox.isSelected()) {
				hateCrimeDropDownBox.setEnabled(true);
			} else {
				hateCrimeDropDownBox.setSelectedIndex(0);
				hateCrimeDropDownBox.setEnabled(false);
			}
			checkMandatoryFields();
		}
	}
	
	/**
	 * Enables/disables masked name text box when clicked.
	 * @author waltersn
	 *
	 */
	public class MaskedFlagItemListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			// change has been made
			if(!ignoreChanges) {
				fieldsChangedGlobal = true;
				checkMandatoryFields();
			}
			if (maskedFlagCheckBox.isSelected()) {
				maskNameTextBox.setEnabled(true);
			} else {
				maskNameTextBox.setEnabled(false);
				maskNameLabel.setText(" ");
				maskNameTextBox.setText("");
			}
		}
	}
	
	/**
	 * When dropdown is changed if Incare then set juvenile checkbox to be selected.
	 */
	public class BcStatusDropDownListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent event) {
			if (event.getStateChange() == ItemEvent.SELECTED) {
				if (((DropdownCodeStringValue) event.getItem()).getDisplayName().equals(CaseMaintenanceConstants.IN_CARE)) {
					juvenileCheckBox.setSelected(true);
				} else {
					juvenileInvalidEntry.setText(" ");
				}
			}
		}
	}
	
	/**
	 * Validator :
	 * Mag Court must not exceed date appeal lodged
	 * Also checks orig date validation and driving disq 
	 * validation.
	 */
	public class MagCourtValidator extends AbstractDateValidator {
		@Override
		public void validate(XDatePanel target, List<String> errors) {
			if (hasDate(target) && target.isDateValidate() && getDate(target).after(
					getDate(((GeneralCriminalAppeal) generalPanel).getDateAppealLodgedPanel()))) {
				errors.add("Must not Exceed Date Appeal Lodged");
			}
			originalDateOfSentenceDateValidation.validate(true);
			drivingDisqualSuspensionDateValidation.validate(true);
		}
	}
	
	/**
	 * Validator :
	 * Orig date of sentence must not be before conviction date and 
	 * must not exceed date appeal lodged.
	 */
	public class OrigDateOfSentenceValidator extends AbstractDateValidator {
		@Override
		public void validate(XDatePanel target, List<String> errors) {
			if (hasDate(target) && target.isDateValidate() && hasDate(magCourtConvictionDatePicker)) {
				if (getDate(target).before(getDate(magCourtConvictionDatePicker))) {
					errors.add("Must Not Be Before Conviction Date");
				} else if (getDate(target).after(
						getDate(((GeneralCriminalAppeal) generalPanel).getDateAppealLodgedPanel()))) {
					errors.add("Must not Exceed Date Appeal Lodged");
				}
			}
		}

	}
	
	/**
	 * Validator :
	 * Driving disqual must not be before conviction date.
	 */
	public class DrivingDisqualSuspensionValidator extends AbstractDateValidator {
		@Override
		public void validate(XDatePanel target, List<String> errors) {
			if (hasDate(target) && target.isDateValidate() && hasDate(magCourtConvictionDatePicker) 
					&& getDate(target).before(getDate(magCourtConvictionDatePicker))) {
				errors.add("Must Not Be Before Conviction Date");
			}
		}
	}
	
	/**
	 * Validator :
	 * First date must be before/on SFT , 
	 * if final date is empty it populates the date
	 * otherwise it's mandatory so shows the mandatory error and 
	 * then validates final date .
	 */
	public class FirstDateValidator extends AbstractDateValidator {
		@Override
		public void validate(XDatePanel target, List<String> errors) {
			if (hasDate(target)) {
				if (hasDate(target) && target.isDateValidate() && getDate(target)
						.after(getDate(((GeneralTrial) generalPanel).getSentForTrialPanel()))) {
					errors.add("First Date Must be before/on Sent for trial");
				} else if (hasDate(target) && target.isDateValidate() && !hasDate(finalDatePicker)) {
					setMagCourtFinalHearingDate(getDate(target));
					firstDateWarningLabel.setText(" ");
				}
			} else {
				errors.add("Field is Mandatory");
			}
			// call final date validation as the change in
			// this field may now mean final date is valid
			finalDateValidation.validate(true);
		}
	}
	
	/**
	 * Validator :
	 * Final date must be after/on first date
	 * must be before/on sent for trial
	 * and is also mandatory if enabled.
	 *
	 */
	public class FinalDateValidator extends AbstractDateValidator {
		@Override
		public void validate(XDatePanel target, List<String> errors) {
			if (hasDate(target)) {
				XDatePanel sentForTrial = ((GeneralTrial) generalPanel).getSentForTrialPanel();
				if (hasDate(target) && hasDate(firstDatePicker) && target.isDateValidate()
						&& firstDatePicker.isDateValidate()
						&& getDate(target).before(getDate(firstDatePicker))) {
					errors.add("Final Date must be after/on first date");
				} else if (hasDate(target) && hasDate(sentForTrial) && target.isDateValidate()
						&& sentForTrial.isDateValidate()
						&& getDate(target).after(getDate(sentForTrial))) {
					errors.add("Final Date Must be before/on Sent for trial");
				} else {
					finalDateWarningLabel.setText(" ");
				}
			} else {
				errors.add("Field is Mandatory");
			}
		}
	}

}