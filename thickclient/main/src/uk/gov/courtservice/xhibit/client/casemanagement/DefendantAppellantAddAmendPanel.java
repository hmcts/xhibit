package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import mseries.ui.MChangeEvent;
import mseries.ui.MChangeListener;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendantreference.DefendantReferenceControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantReferenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.caselinking.CaseLinkingValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.comparator.TicketTypeComparator;
import uk.gov.courtservice.xhibit.client.updatecase.UpdateDefendantPanel;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractTextValidator;
import uk.gov.courtservice.xhibit.client.util.validation.AgeValidator;
import uk.gov.courtservice.xhibit.client.util.validation.ComboBoxValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.DateEqualOrBeforeTodayValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.TextRegexValidator;
import uk.gov.courtservice.xhibit.client.util.validation.TextValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.framework.util.StringUtil;

public class DefendantAppellantAddAmendPanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;
	private DefendantAppellantAddAmendModel model;
	private DefendantAppellantAddAmendDialog parentDialog;
	private DefendantAppellantAddAmendPanel thisClass;

	/**
	 * Fields on Defendant/Appellant Details panel.
	 */
	private JLabel lblSurname = null;
	private JLabel lblFirstName = null;
	private JLabel lblDateOfBirth = null;
	private JLabel lblEthnicAppearance = null;
	private JLabel lblEthnicitySelfDefined = null;
	private XTextField surnameTextField = null;
	private XTextField initialTextField = null;
	private XTextField firstNameTextField = null;
	private XTextField otherNameTextField = null;
	private XTextField parentGuardianTextField = null;
	private XTextField address1TextField = null;
	private XTextField address2TextField = null;
	private XTextField address3TextField = null;
	private XTextField address4TextField = null;
	private XTextField townTextField = null;
	private XTextField countyTextField = null;
	private XTextField postcodeTextField = null;
	private XTextField driverNumberTextField = null;
	private XTextField licenceIssueNumberTextField = null;
	private XDatePanel dateOfBirth;
	private Calendar todaysDate = null;
	private XComboBox comboEthnicAppearance;
	private XComboBox comboSelectEthnicity;
	private XComboBox comboSelectLicenceType;
	private JLabel lblManSurname;
	private JLabel lblIInitials;
	private JLabel lblManFName;
	private JLabel lblIOther;
	private JLabel lblIPGuardian;
	private JLabel lblManDate;
	private JLabel lblManEthnicAppearance;
	private JLabel lblManEthnicityDefined;
	private JLabel lblManAddress1;
	private JLabel lblIAddress2;
	private JLabel lblIAddress3;
	private JLabel lblIAddress4;
	private JLabel lblITown;
	private JLabel lblICounty;
	private JLabel lblIPostcode;
	private JLabel lblIDriver;
	private JLabel lblILicenceIssueNumber;
	private JLabel lblILicenceType;

	/**
	 * Fields on Defendant/Appellant Gender Combo Box panel.
	 */
	private XComboBox comboGender;

	/**
	 * Fields on Defendant/Appellant Custody Details panel.
	 */
	private JLabel lblInCustody = null;
	private JLabel lblIPrisonerNumber = null;
	private JLabel lblIPrisonerID = null;
	private JCheckBox custodyCheckBox;
	private XTextField prisonerNumberTextField = null;
	private XComboBox comboPrisonID;

	/**
	 * Fields on Save/Cancel Button panel.
	 */
	private JButton btnSave = null;
	private JButton btnCancel = null;

	/**
	 * JPanels.
	 */
	private JPanel mainPanel = null;
	private JPanel defendantAppellantDetailsPanel = null;
	private JPanel custodyDetailsPanel = null;

	/**
	 * Combo Box data.
	 */
	private ArrayList<RefSystemCodeBasicValue> ethnicAppearanceList;
	private ArrayList<RefSystemCodeBasicValue> ethnicClassificationList;
	private ArrayList<RefSystemCodeBasicValue> prisonIDList;
	private ArrayList<DropdownCodeStringValue> licenceTypeList;

	private DefendantAppellantSearchDialog dialogParent = null;

	private DefendantAppellant defAppOld = null;
	private DefendantValue defValOld = null;
	private AgeValidator dobValidator = null;

	private boolean inCustodyValidation = false;
	private boolean changesMade = false;

	// array of all the invalid entry fields
	Vector<Object> mandatoryFields = new Vector<Object>();
	/**
	 * Validators.
	 */
	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();

	private static final Logger log = CSServices.getLogger(DefendantAppellantAddAmendPanel.class);

	public DefendantAppellantAddAmendPanel(final DefendantAppellantAddAmendDialog parent,
			DefendantAppellantAddAmendModel model) throws CSRecoverableException {
		this.model = model;
		this.parentDialog = parent;
		thisClass = this;
		stepInitialise();
		jbInit();
		if (model.getDA() != null) {
			defAppOld = new DefendantAppellant(model.getDA().getFirstName(), model.getDA().getSurname(),
					model.getDA().getOtherName(), model.getDA().getDateOfBirth(), model.getDA().getGender());
		}
	}

	public DefendantAppellantAddAmendPanel(final DefendantAppellantAddAmendDialog parent,
			DefendantAppellantAddAmendModel model, DefendantAppellantSearchDialog dialogParent)
			throws CSRecoverableException {
		this(parent, model);
		this.dialogParent = dialogParent;
	}

	/**
	 * Initialises the look and feel of the panel.
	 */
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(800, 550));
		GridBagConstraints gbc = getGridBagLayout();

		mainPanel = getMainPanel();

		gbc.weighty = 0.5;
		gbc.fill = GridBagConstraints.BOTH;
		mainPanel.add(getDefendantAppellantDetailsPanel(), gbc);

		CustomButtonPanel buttonPanel = (CustomButtonPanel) this.parentDialog.getButtonPanel();
		btnSave = buttonPanel.addButton("DefendantAppellantAddAmendSave", false, false);
		btnCancel = buttonPanel.addButton("btnCancel", true, false);

		configureTabOrder();

		addMandatoryLabel(lblSurname);
		addMandatoryLabel(lblFirstName);
		addMandatoryLabel(lblEthnicAppearance);
		addMandatoryLabel(lblEthnicitySelfDefined);
		changesMade = false;
	}

	/**
	 * Returns a panel which contains the main panel
	 * 
	 * @return
	 */
	public JPanel getMainPanel() {
		GridBagConstraints gbc = getGridBagLayout();
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setBorder(BorderFactory.createEmptyBorder());
			mainPanel.setPreferredSize(new Dimension(750, 500));
			this.add(scrollPane, gbc);
		}

		return mainPanel;
	}

	/**
	 * Returns a panel which contains the Defendant/Appellant Details Panel
	 * 
	 * @return
	 */
	public JPanel getDefendantAppellantDetailsPanel() {
		GridBagConstraints gbc = getGridBagLayout();
		if (defendantAppellantDetailsPanel == null) {
			defendantAppellantDetailsPanel = new JPanel(new GridBagLayout());
			gbc.anchor = GridBagConstraints.WEST;

			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			lblSurname = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.surnameLabel"));
			defendantAppellantDetailsPanel.add(lblSurname, gbc);
			gbc.gridy += 2;

			JLabel lblSex = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.sexLabel"));
			defendantAppellantDetailsPanel.add(lblSex, gbc);
			gbc.gridy += 2;

			JLabel lblInitials = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.initialsLabel"));
			defendantAppellantDetailsPanel.add(lblInitials, gbc);
			gbc.gridy += 2;

			lblFirstName = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.firstNameLabel"));
			defendantAppellantDetailsPanel.add(lblFirstName, gbc);
			gbc.gridy += 2;

			JLabel lblOtherNames = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.otherNamesLabel"));
			defendantAppellantDetailsPanel.add(lblOtherNames, gbc);
			gbc.gridy += 2;

			JLabel lblParentGuardian = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.parentGuardianLabel"));
			defendantAppellantDetailsPanel.add(lblParentGuardian, gbc);
			gbc.gridy += 2;

			lblDateOfBirth = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.dateOfBirthLabel"));
			defendantAppellantDetailsPanel.add(lblDateOfBirth, gbc);
			gbc.gridy += 2;

			lblEthnicAppearance = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.ethnicAppearanceLabel"));
			defendantAppellantDetailsPanel.add(lblEthnicAppearance, gbc);
			gbc.gridy += 2;

			lblEthnicitySelfDefined = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.ethnicitySelfDefinedLabel"));
			defendantAppellantDetailsPanel.add(lblEthnicitySelfDefined, gbc);
			gbc.gridy += 2;

			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.fill = GridBagConstraints.HORIZONTAL;

			defendantAppellantDetailsPanel.add(getSurname(), gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(getComboGender(), gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(getInitials(), gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(getFirstName(), gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(getOtherNames(), gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(getParentGuardian(), gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(getDateOfBirth(), gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(getEthnicAppearance(), gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(getEthnicitySelfDefined(), gbc);
			gbc.gridy += 2;
			
			// Custody details
			gbc.gridx = 0;
			gbc.gridwidth = 2;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;
			defendantAppellantDetailsPanel.add(getCustodyDetailsPanel(), gbc);
			//gbc.gridy += 2;

			/* Top Of Column */
			gbc.gridx = 1;
			gbc.gridy = 0;

			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;

			defendantAppellantDetailsPanel.add(lblManSurname, gbc);
			// No need for mandatory label for gender
			gbc.gridy += 4;

			defendantAppellantDetailsPanel.add(lblIInitials, gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(lblManFName, gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(lblIOther, gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(lblIPGuardian, gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(lblManDate, gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(lblManEthnicAppearance, gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(lblManEthnicityDefined, gbc);
			gbc.gridy += 2;


			// Next Column
			gbc.gridwidth = 1;
			gbc.gridy = 1;
			gbc.gridx = 2;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			JLabel lblManAddress = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.addressLabel"));
			defendantAppellantDetailsPanel.add(lblManAddress, gbc);
			// No need for address 2, 3 and 4 labels
			gbc.gridy += 8;

			JLabel lblTown = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.townLabel"));
			defendantAppellantDetailsPanel.add(lblTown, gbc);
			gbc.gridy += 2;

			JLabel lblCounty = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.countyLabel"));
			defendantAppellantDetailsPanel.add(lblCounty, gbc);
			gbc.gridy += 2;

			JLabel lblPostcode = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.postcodeLabel"));
			defendantAppellantDetailsPanel.add(lblPostcode, gbc);
			gbc.gridy += 2;

			JLabel lblDriverNumber = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.driverNumberLabel"));
			defendantAppellantDetailsPanel.add(lblDriverNumber, gbc);
			gbc.gridy += 2;
			
			JLabel lblLicenceType = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.licenceTypeLabel"));
			defendantAppellantDetailsPanel.add(lblLicenceType, gbc);
			gbc.gridy += 2;
			
			gbc.anchor = GridBagConstraints.NORTH;
			JLabel lblLicenceIssueNumber = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.licenceIssueNumberLabel"));
			defendantAppellantDetailsPanel.add(lblLicenceIssueNumber, gbc);

			// Top of Column
			gbc.gridy = 1;
			gbc.gridx++;

			defendantAppellantDetailsPanel.add(getAddress1(), gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(getAddress2(), gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(getAddress3(), gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(getAddress4(), gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(getTown(), gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(getCounty(), gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(getPostcode(), gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(getDriverNumber(), gbc);
			gbc.gridy += 2;
			
			defendantAppellantDetailsPanel.add(getLicenceType(), gbc);
			gbc.gridy += 2;
			
			defendantAppellantDetailsPanel.add(getLicenceIssueNumber(), gbc);
			gbc.gridy += 2;

			// Top Of Column
			gbc.gridy = 0;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;

			defendantAppellantDetailsPanel.add(lblManAddress1, gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(lblIAddress2, gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(lblIAddress3, gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(lblIAddress4, gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(lblITown, gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(lblICounty, gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(lblIPostcode, gbc);
			gbc.gridy += 2;

			defendantAppellantDetailsPanel.add(lblIDriver, gbc);
			gbc.gridy += 2;
			
			defendantAppellantDetailsPanel.add(lblILicenceType, gbc);
			gbc.gridy += 2;
			
			defendantAppellantDetailsPanel.add(lblILicenceIssueNumber, gbc);
			gbc.gridy++;
		}

		return defendantAppellantDetailsPanel;
	}
		
	private ArrayList<DropdownCodeStringValue> getGenders() {
		ArrayList<DropdownCodeStringValue> genders = new ArrayList<DropdownCodeStringValue>();
		genders.add(new DropdownCodeStringValue("Select","3")); 
		genders.add(new DropdownCodeStringValue( 
						XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
								"defendantAppellantSearch.maleRadioButtonLabel"),"1"));
		genders.add(new DropdownCodeStringValue( 
				XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
						"defendantAppellantSearch.femaleRadioButtonLabel"),"2"));
		
		genders.add(new DropdownCodeStringValue( 
				XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
						"defendantAppellantSearch.companyRadioButtonLabel"),"0"));

		return genders;
	}
	
	private XComboBox getComboGender() {
		if (comboGender == null) {
			comboGender = new XComboBox(false);
			comboGender.setModel(new DefaultComboBoxModel(getGenders().toArray()));
			DropdownBoxCellRender comboGenderRender = new DropdownBoxCellRender();
			comboGenderRender.setFormat(true);
			comboGender.setRenderer(comboGenderRender);
			comboGender.addItemListener(new GenderComboBoxListener());
			mandatoryFields.add(comboGender);
		}
		return comboGender;
	}
	
	protected int getGender() {
		String item = ((DropdownCodeStringValue) comboGender.getSelectedItem()).getCode();
		
		return Integer.parseInt(item);
	}
	
	public class GenderComboBoxListener implements ItemListener {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				int state = itemEvent.getStateChange();
				String selection = ((DropdownCodeStringValue) itemEvent.getItem()).getCode();
				if (state == ItemEvent.SELECTED) {
					// used to disable/enable custody details on gender/sex
					// change
					enableSaveButton();
					
					if ("1".equals(selection) || "2".equals(selection)) {
						firstNameTextField.setEnabled(true);
						// --- Enable fields for male/female ---
						otherNameTextField.setEnabled(true);
						custodyCheckBox.setEnabled(true);
						dateOfBirth.setEnabled(true);
						comboEthnicAppearance.setEnabled(true);
						comboSelectEthnicity.setEnabled(true);
						initialTextField.setEnabled(true);
						parentGuardianTextField.setEnabled(true);
						addMandatoryLabel(lblSurname);
						addMandatoryLabel(lblFirstName);
						addMandatoryLabel(lblEthnicAppearance);
						addMandatoryLabel(lblEthnicitySelfDefined);

					} else if ("0".equals(selection)) {
						// clearing/disabling custody details when gender is
						// company
						custodyCheckBox.setSelected(false);
						custodyCheckBox.setEnabled(false);

						setFirstNameTextField("");
						firstNameTextField.setEnabled(false);
						lblManFName.setVisible(false);
						// --- Disable fields for Company ---
						otherNameTextField.setText("");
						otherNameTextField.setEnabled(false);
						dateOfBirth.clear();
						dateOfBirth.setEnabled(false);
						lblManDate.setVisible(false);
						setPrisonerNumberTextField("");
						prisonerNumberTextField.setEnabled(false);
						lblIPrisonerID.setVisible(false);

						comboPrisonID.setEnabled(false);
						comboPrisonID.setSelectedIndex(0);

						comboEthnicAppearance.setEnabled(false);
						setEthnicAppearance("N");
						comboSelectEthnicity.setEnabled(false);
						setEthnicitySelfDefined("NS");
						initialTextField.setEnabled(false);
						setInitialTextField("");
						parentGuardianTextField.setEnabled(false);
						setParentGuardianTextField("");

						removeMandatoryLabel(lblFirstName);
						removeMandatoryLabel(lblDateOfBirth);
						removeMandatoryLabel(lblEthnicAppearance);
						removeMandatoryLabel(lblEthnicitySelfDefined);
					}
				}
			}
		}
	


	/**
	 * Returns a panel which contains the Custody Details of a
	 * Defendant/Appellant
	 * 
	 * @return
	 * @throws CSValidationException
	 */
	public JPanel getCustodyDetailsPanel() {
		GridBagConstraints gbc = getGridBagLayout();
		if (custodyDetailsPanel == null) {
			custodyDetailsPanel = new JPanel(new GridBagLayout());
			custodyDetailsPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(
					XhibitBundles.CaseMaintenanceResources, "defendantAppellantAddAmend.custodyDetailsPanelLabel")));
			gbc.anchor = GridBagConstraints.WEST;

			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			lblInCustody = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.inCustodyLabel"));
			custodyDetailsPanel.add(lblInCustody, gbc);
			gbc.gridy += 2;

			JLabel lblPrisonerNumber = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.prisonerNumberLabel"));
			custodyDetailsPanel.add(lblPrisonerNumber, gbc);
			gbc.gridy += 2;

			JLabel lblPrisonId = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources,
					"defendantAppellantAddAmend.prisonIdLabel"));
			custodyDetailsPanel.add(lblPrisonId, gbc);

			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 1;

			custodyDetailsPanel.add(getInCustodyCheckBox(), gbc);
			gbc.gridy += 2;

			custodyDetailsPanel.add(getPrisonerNumber(), gbc);
			gbc.gridy += 2;

			custodyDetailsPanel.add(getComboPrisonId(), gbc);

			/* Top Of Column */
			gbc.gridx = 1;
			gbc.gridy = 2;

			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;

			custodyDetailsPanel.add(lblIPrisonerNumber, gbc);
			gbc.gridy += 2;

			custodyDetailsPanel.add(lblIPrisonerID, gbc);

			if (custodyCheckBox.isSelected()) {
				prisonerNumberTextField.setEnabled(true);
				comboPrisonID.setEnabled(true);
			} else {
				prisonerNumberTextField.setEnabled(false);
				prisonerNumberTextField.setText("");
				comboPrisonID.setEnabled(false);
				comboPrisonID.setSelectedIndex(0);
			}
		}

		return custodyDetailsPanel;
	}

	public JCheckBox getInCustodyCheckBox() {
		if (custodyCheckBox == null) {
			custodyCheckBox = new JCheckBox();
			custodyCheckBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JCheckBox cb = (JCheckBox) e.getSource();
					if (cb.isSelected()) {
						prisonerNumberTextField.setEnabled(true);
						comboPrisonID.setEnabled(true);
					} else {
						if (inCustodyValidation) {
							custodyCheckBox.setSelected(true);
							JOptionPane.showMessageDialog(thisClass,
									"Cannot change value as defendant has a B/C \n status of C or J in a current case",
									"Error", JOptionPane.ERROR_MESSAGE);
						} else {
							prisonerNumberTextField.setEnabled(false);
							prisonerNumberTextField.setText("");
							comboPrisonID.setEnabled(false);
							comboPrisonID.setSelectedIndex(0);
						}

					}
				}
			});
		}
		return custodyCheckBox;
	}

	public XComboBox getComboPrisonId() {
		if (lblIPrisonerID == null) {
			lblIPrisonerID = new JLabel(" ");
		}
		if (comboPrisonID == null) {
			comboPrisonID = new XComboBox(false);
			comboPrisonID.setModel(new DefaultComboBoxModel(getPrisonIDCodes().toArray()));
			DropdownBoxCellRender comboPrisonIdRender = new DropdownBoxCellRender();
			comboPrisonIdRender.setFormat(true);
			comboPrisonID.setRenderer(comboPrisonIdRender);
		}
		return comboPrisonID;
	}

	public XTextField getSurname() {
		if (lblManSurname == null) {
			lblManSurname = new JLabel(" ");
		}
		if (surnameTextField == null) {
			surnameTextField = new XTextField();
			surnameTextField.setMaxLength(35);
			surnameTextField.setColumns(10);
			surnameTextField.setUpperCase(true);
			surnameTextField.setMinimumSize(surnameTextField.getPreferredSize());
			TextValidationController surnameTxtValidation = ValidationControllerFactory.createTextRequired(this,
					surnameTextField, lblManSurname, new TextRegexValidator("^.{1,35}$"));
			validationControllers.add(surnameTxtValidation);
			mandatoryFields.add(surnameTextField);
		}
		return surnameTextField;
	}

	public XTextField getInitials() {
		if (lblIInitials == null) {
			lblIInitials = new JLabel(" ");
		}
		if (initialTextField == null) {
			initialTextField = new XTextField();
			initialTextField.setMaxLength(4);
			initialTextField.setColumns(10);
			initialTextField.setUpperCase(true);
			initialTextField.setMinimumSize(initialTextField.getPreferredSize());
			initialTextField.requestFocusInWindow();
			TextValidationController initialsTxtValidation = ValidationControllerFactory.createText(this,
					initialTextField, lblIInitials, new AbstractTextValidator() {
				@Override
				public void validate(JTextComponent target, List<String> errors) {
					char[] charArr =target.getText().toCharArray();
					int len=0;
					for(int i=0;i<charArr.length;i++){
						len=len+StringUtil.getLengthOfChar(charArr[i]);
						if(len>4) {
							errors.add("Invalid entry");
							break;
						}
					}
				}});
			validationControllers.add(initialsTxtValidation);
		}
		return initialTextField;
	}

	public XTextField getFirstName() {
		if (lblManFName == null) {
			lblManFName = new JLabel(" ");
		}
		if (firstNameTextField == null) {
			firstNameTextField = new XTextField();
			firstNameTextField.setMaxLength(35);
			firstNameTextField.setColumns(10);
			firstNameTextField.setUpperCase(true);
			firstNameTextField.setMinimumSize(firstNameTextField.getPreferredSize());
			TextValidationController firstNameTxtValidation = ValidationControllerFactory.createTextRequired(this,
					firstNameTextField, lblManFName, new TextRegexValidator("^.{1,35}$"));
			validationControllers.add(firstNameTxtValidation);
			mandatoryFields.add(firstNameTextField);
		}
		return firstNameTextField;
	}

	public XTextField getOtherNames() {
		if (lblIOther == null) {
			lblIOther = new JLabel(" ");
		}
		if (otherNameTextField == null) {
			otherNameTextField = new XTextField();
			otherNameTextField.setMaxLength(35);
			otherNameTextField.setColumns(10);
			otherNameTextField.setUpperCase(true);
			otherNameTextField.setMinimumSize(otherNameTextField.getPreferredSize());
			TextValidationController otherNamesTxtValidation = ValidationControllerFactory.createText(this,
					otherNameTextField, lblIOther, new TextRegexValidator("^.{1,35}$"));
			validationControllers.add(otherNamesTxtValidation);
		}
		return otherNameTextField;
	}

	public XTextField getParentGuardian() {
		if (lblIPGuardian == null) {
			lblIPGuardian = new JLabel(" ");
		}
		if (parentGuardianTextField == null) {
			parentGuardianTextField = new XTextField();
			parentGuardianTextField.setMaxLength(35);
			parentGuardianTextField.setColumns(10);
			parentGuardianTextField.setUpperCase(true);
			parentGuardianTextField.setMinimumSize(parentGuardianTextField.getPreferredSize());
			TextValidationController parentGuardianTxtValidation = ValidationControllerFactory.createText(this,
					parentGuardianTextField, lblIPGuardian, new TextRegexValidator("^.{1,35}$"));
			validationControllers.add(parentGuardianTxtValidation);
		}
		return parentGuardianTextField;
	}

	public XDatePanel getDateOfBirth() {
		if (lblManDate == null) {
			lblManDate = new JLabel(" ");
		}
		if (dateOfBirth == null) {
			dateOfBirth = new XDatePanel(defendantAppellantDetailsPanel, todaysDate, false);
			dobValidator = new AgeValidator("", "", 10, 70, dateOfBirth);
			dobValidator.setWarningIsError(false);
			DateValidationController dateOfBirthValCon = ValidationControllerFactory.createDateValid(this, dateOfBirth,
					lblManDate, new DateEqualOrBeforeTodayValidator());
			validationControllers.add(dateOfBirthValCon);
		}
		return dateOfBirth;
	}

	public XComboBox getEthnicAppearance() {
		if (lblManEthnicAppearance == null) {
			lblManEthnicAppearance = new JLabel(" ");
		}
		if (comboEthnicAppearance == null) {
			comboEthnicAppearance = new XComboBox();
			ComboBoxValidationController ethnicAppearanceComboValidation = ValidationControllerFactory
					.createComboBoxRequired(this, comboEthnicAppearance, lblManEthnicAppearance);
			validationControllers.add(ethnicAppearanceComboValidation);
			comboEthnicAppearance.setModel(new DefaultComboBoxModel(getEthnicAppearanceCodes().toArray()));
			DropdownBoxCellRender comboEthnicRender = new DropdownBoxCellRender();
			comboEthnicRender.setFormat(true);
			comboEthnicAppearance.setRenderer(comboEthnicRender);
			mandatoryFields.add(comboEthnicAppearance);
		}
		return comboEthnicAppearance;
	}

	public XComboBox getEthnicitySelfDefined() {
		if (lblManEthnicityDefined == null) {
			lblManEthnicityDefined = new JLabel(" ");
		}
		if (comboSelectEthnicity == null) {
			comboSelectEthnicity = new XComboBox();
			ComboBoxValidationController ethnicitySelfDefinedComboValidation = ValidationControllerFactory
					.createComboBoxRequired(this, comboSelectEthnicity, lblManEthnicityDefined);
			validationControllers.add(ethnicitySelfDefinedComboValidation);
			comboSelectEthnicity.setModel(new DefaultComboBoxModel(getEthnicClassificationCodes().toArray()));
			DropdownBoxCellRender comboEthnicRender = new DropdownBoxCellRender();
			comboEthnicRender.setFormat(true);
			comboSelectEthnicity.setRenderer(comboEthnicRender);
			mandatoryFields.add(comboSelectEthnicity);
		}
		return comboSelectEthnicity;
	}

	public XTextField getAddress1() {
		if (lblManAddress1 == null) {
			lblManAddress1 = new JLabel(" ");
		}
		if (address1TextField == null) {
			address1TextField = new XTextField();
			address1TextField.setMaxLength(30);
			address1TextField.setColumns(10);
			address1TextField.setUpperCase(true);
			address1TextField.setMinimumSize(address1TextField.getPreferredSize());
			TextValidationController address1TxtValidation = ValidationControllerFactory.createTextRequired(this,
					address1TextField, lblManAddress1, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(address1TxtValidation);
			mandatoryFields.add(address1TextField);
		}
		return address1TextField;
	}

	public XTextField getAddress2() {
		if (lblIAddress2 == null) {
			lblIAddress2 = new JLabel(" ");
		}
		if (address2TextField == null) {
			address2TextField = new XTextField();
			address2TextField.setMaxLength(30);
			address2TextField.setColumns(10);
			address2TextField.setUpperCase(true);
			address2TextField.setMinimumSize(address2TextField.getPreferredSize());
			TextValidationController address2TxtValidation = ValidationControllerFactory.createText(this,
					address2TextField, lblIAddress2, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(address2TxtValidation);
		}
		return address2TextField;
	}

	public XTextField getAddress3() {
		if (lblIAddress3 == null) {
			lblIAddress3 = new JLabel(" ");
		}
		if (address3TextField == null) {
			address3TextField = new XTextField();
			address3TextField.setMaxLength(30);
			address3TextField.setColumns(10);
			address3TextField.setUpperCase(true);
			address3TextField.setMinimumSize(address3TextField.getPreferredSize());
			TextValidationController address3TxtValidation = ValidationControllerFactory.createText(this,
					address3TextField, lblIAddress3, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(address3TxtValidation);
		}
		return address3TextField;
	}

	public XTextField getAddress4() {
		if (lblIAddress4 == null) {
			lblIAddress4 = new JLabel(" ");
		}
		if (address4TextField == null) {
			address4TextField = new XTextField();
			address4TextField.setMaxLength(30);
			address4TextField.setColumns(10);
			address4TextField.setUpperCase(true);
			address4TextField.setMinimumSize(address4TextField.getPreferredSize());
			TextValidationController address4TxtValidation = ValidationControllerFactory.createText(this,
					address4TextField, lblIAddress4, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(address4TxtValidation);
		}
		return address4TextField;
	}

	public XTextField getTown() {
		if (lblITown == null) {
			lblITown = new JLabel(" ");
		}
		if (townTextField == null) {
			townTextField = new XTextField();
			townTextField.setMaxLength(30);
			townTextField.setColumns(10);
			townTextField.setUpperCase(true);
			townTextField.setMinimumSize(townTextField.getPreferredSize());
			TextValidationController townTxtValidation = ValidationControllerFactory.createText(this, townTextField,
					lblITown, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(townTxtValidation);
		}
		return townTextField;
	}

	public XTextField getCounty() {
		if (lblICounty == null) {
			lblICounty = new JLabel(" ");
		}
		if (countyTextField == null) {
			countyTextField = new XTextField();
			countyTextField.setMaxLength(30);
			countyTextField.setColumns(10);
			countyTextField.setUpperCase(true);
			countyTextField.setMinimumSize(countyTextField.getPreferredSize());
			TextValidationController countyTxtValidation = ValidationControllerFactory.createText(this, countyTextField,
					lblICounty, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(countyTxtValidation);
		}
		return countyTextField;
	}

	public XTextField getPostcode() {
		if (lblIPostcode == null) {
			lblIPostcode = new JLabel(" ");
			lblIPostcode.setForeground(Color.RED);
		}
		if (postcodeTextField == null) {
			postcodeTextField = new XTextField(8, "", lblIPostcode, false, true);
			postcodeTextField.setMaxLength(8);
			postcodeTextField.setColumns(10);
			postcodeTextField.setUpperCase(true);
			postcodeTextField.setMinimumSize(postcodeTextField.getPreferredSize());
			postcodeTextField.setGridBagLayout(true);
		}
		return postcodeTextField;
	}

	public XTextField getDriverNumber() {
		if (lblIDriver == null) {
			lblIDriver = new JLabel(" ");
		}
		if (driverNumberTextField == null) {
			driverNumberTextField = new XTextField();
			driverNumberTextField.setMaxLength(50);
			driverNumberTextField.setColumns(10);
			driverNumberTextField.setUpperCase(true);
			driverNumberTextField.setMinimumSize(driverNumberTextField.getPreferredSize());
			TextValidationController driverNumberTxtValidation = ValidationControllerFactory.createText(this,
					driverNumberTextField, lblIDriver, new TextRegexValidator("^[a-zA-Z0-9]{1,50}$"));
			validationControllers.add(driverNumberTxtValidation);
		}
		return driverNumberTextField;
	}
	
	public XComboBox getLicenceType() {
		if (lblILicenceType == null) {
			lblILicenceType = new JLabel(" ");
		}
		if (comboSelectLicenceType == null) {
			comboSelectLicenceType = new XComboBox();
			//ComboBoxValidationController licenceTypeComboValidation = ValidationControllerFactory
					//.createComboBoxRequired(this, comboSelectLicenceType, lblILicenceType);
			//validationControllers.add(licenceTypeComboValidation);
			comboSelectLicenceType.setModel(new DefaultComboBoxModel(getLicenceTypes().toArray()));
			DropdownBoxCellRender comboLicenceTypeRender = new DropdownBoxCellRender();
			comboLicenceTypeRender.setFormat(true);
			comboSelectLicenceType.setRenderer(comboLicenceTypeRender);
			//mandatoryFields.add(comboSelectLicenceType);
		}
		
		if (comboPrisonID == null) {
			comboPrisonID = new XComboBox(false);
			comboPrisonID.setModel(new DefaultComboBoxModel(getPrisonIDCodes().toArray()));
			DropdownBoxCellRender comboPrisonIdRender = new DropdownBoxCellRender();
			comboPrisonIdRender.setFormat(true);
			comboPrisonID.setRenderer(comboPrisonIdRender);
		}
		
		return comboSelectLicenceType;
	}
	
	public XTextField getLicenceIssueNumber() {
		if (lblILicenceIssueNumber == null) {
			lblILicenceIssueNumber = new JLabel(" ");
		}
		if (licenceIssueNumberTextField == null) {
			licenceIssueNumberTextField = new XTextField();
			licenceIssueNumberTextField.setMaxLength(2);
			licenceIssueNumberTextField.setColumns(2);
			licenceIssueNumberTextField.setUpperCase(true);
			licenceIssueNumberTextField.setMinimumSize(licenceIssueNumberTextField.getPreferredSize());
			TextValidationController licenceIssueNumberTxtValidation = ValidationControllerFactory.createText(this,
					licenceIssueNumberTextField, lblILicenceIssueNumber, new TextRegexValidator("[0-9]{1,2}$"));
			validationControllers.add(licenceIssueNumberTxtValidation);
		}
		return licenceIssueNumberTextField;
	}

	public XTextField getPrisonerNumber() {
		if (lblIPrisonerNumber == null) {
			lblIPrisonerNumber = new JLabel(" ");
		}
		if (prisonerNumberTextField == null) {
			prisonerNumberTextField = new XTextField();
			prisonerNumberTextField.setMaxLength(8);
			prisonerNumberTextField.setColumns(10);
			prisonerNumberTextField.setUpperCase(true);
			prisonerNumberTextField.setMinimumSize(prisonerNumberTextField.getPreferredSize());
			TextValidationController prisonerNumberTxtValidation = ValidationControllerFactory.createText(this,
					prisonerNumberTextField, lblIPrisonerNumber, new TextRegexValidator("^.{1,8}$"));
			validationControllers.add(prisonerNumberTxtValidation);
		}
		return prisonerNumberTextField;
	}

	public void configureTabOrder() {
		setFocusTraversalPolicyProvider(true);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { surnameTextField, comboGender,
				initialTextField, firstNameTextField, otherNameTextField, parentGuardianTextField,
				dateOfBirth, comboEthnicAppearance, comboSelectEthnicity, custodyCheckBox, prisonerNumberTextField,
				comboPrisonID, address1TextField, address2TextField, address3TextField, address4TextField,
				townTextField, countyTextField, postcodeTextField, driverNumberTextField, comboSelectLicenceType,
				licenceIssueNumberTextField, btnSave, btnCancel }));
	}

	/**
	 * @return the surnameTextField
	 */
	protected String getSurnameTextField() {
		return surnameTextField.getText();
	}

	/**
	 * @param surnameTextField
	 *            the surnameTextField to set
	 */
	protected void setSurnameTextField(String textField) {
		if (textField != null) {
			surnameTextField.setText(textField);
		}
	}

	/**
	 * @return the firstNameTextField
	 */
	protected String getFirstNameTextField() {
		return firstNameTextField.getText();
	}

	/**
	 * @param firstNameTextField
	 *            the firstNameTextField to set
	 */
	protected void setFirstNameTextField(String textField) {
		if (textField != null) {
			firstNameTextField.setText(textField);
		}
	}

	/**
	 * @return the otherNameTextField
	 */
	protected String getOtherNameTextField() {
		return otherNameTextField.getText();
	}

	/**
	 * @param otherNameTextField
	 *            the otherNameTextField to set
	 */
	protected void setOtherNameTextField(String textField) {
		if (textField != null) {
			otherNameTextField.setText(textField);
		}
	}

	/**
	 * @return the parentGuardianTextField
	 */
	protected String getParentGuardianTextField() {
		return parentGuardianTextField.getText();
	}

	/**
	 * @param parentGuardianTextField
	 *            the parentGuardianTextField to set
	 */
	protected void setParentGuardianTextField(String textField) {
		if (textField != null) {
			parentGuardianTextField.setText(textField);
		}
	}

	/**
	 * @return the initialTextField
	 */
	protected String getInitialTextField() {
		return initialTextField.getText();
	}

	/**
	 * @param initialTextField
	 *            the initialTextField to set
	 */
	protected void setInitialTextField(String textField) {
		if (textField != null) {
			initialTextField.setText(textField);
		}
	}

	/**
	 * @return the address1TextField
	 */
	protected String getAddress1TextField() {
		return address1TextField.getText();
	}

	/**
	 * @param address1TextField
	 *            the address1TextField to set
	 */
	protected void setAddress1TextField(String textField) {
		if (textField != null) {
			address1TextField.setText(textField);
		}
	}

	/**
	 * @return the address2TextField
	 */
	protected String getAddress2TextField() {
		return address2TextField.getText();
	}

	/**
	 * @param address2TextField
	 *            the address2TextField to set
	 */
	protected void setAddress2TextField(String textField) {
		if (textField != null) {
			address2TextField.setText(textField);
		}
	}

	/**
	 * @return the address3TextField
	 */
	protected String getAddress3TextField() {
		return address3TextField.getText();
	}

	/**
	 * @param address3TextField
	 *            the address3TextField to set
	 */
	protected void setAddress3TextField(String textField) {
		if (textField != null) {
			address3TextField.setText(textField);
		}
	}

	/**
	 * @return the address4TextField
	 */
	protected String getAddress4TextField() {
		return address4TextField.getText();
	}

	/**
	 * @param address4TextField
	 *            the address4TextField to set
	 */
	protected void setAddress4TextField(String textField) {
		if (textField != null) {
			address4TextField.setText(textField);
		}
	}

	/**
	 * @return the townTextField
	 */
	protected String getTownTextField() {
		return townTextField.getText();
	}

	/**
	 * @param townTextField
	 *            the townTextField to set
	 */
	protected void setTownTextField(String textField) {
		if (textField != null) {
			townTextField.setText(textField);
		}
	}

	/**
	 * @return the countyTextField
	 */
	protected String getCountyTextField() {
		return countyTextField.getText();
	}

	/**
	 * @param countyTextField
	 *            the countyTextField to set
	 */
	protected void setCountyTextField(String textField) {
		if (textField != null) {
			countyTextField.setText(textField);
		}
	}

	/**
	 * @return the postcodeTextField
	 */
	protected String getPostcodeTextField() {
		return postcodeTextField.getText();
	}

	/**
	 * @param postcodeTextField
	 *            the postcodeTextField to set
	 */
	protected void setPostcodeTextField(String textField) {
		if (textField != null) {
			postcodeTextField.setText(textField);
		}
	}

	/**
	 * @return the driverNumberTextField
	 */
	protected String getDriverNumberTextField() {
		if (driverNumberTextField.getText().equals("")) {
			return "";
		} else {
			return driverNumberTextField.getText();
		}
	}

	/**
	 * @param driverNumberTextField
	 *            the driverNumberTextField to set
	 */
	protected void setDriverNumberTextField(String textField) {
		if (textField != null) {
			driverNumberTextField.setText(textField);
		}
	}
	
	/**
	 * @return the licenceIssueNumberTextField
	 */
	protected String getLicenceIssueNumberTextField() {
		if (licenceIssueNumberTextField.getText().equals("")) {
			return "";
		} else {
			return formatLicenceIssueNumber(licenceIssueNumberTextField.getText());
		}
	}

	/**
	 * @param licenceIssueNumberTextField
	 *            the licenceIssueNumberTextField to set
	 */
	protected void setLicenceIssueNumberTextField(String textField) {
		// If only one digit supplied then put in a leading zero
		if (textField != null) {
			if (textField.length() == 1) {
				licenceIssueNumberTextField.setText("0" + textField);
			} else {
				licenceIssueNumberTextField.setText(textField);
			}
		}
	}

	/**
	 * @return the prisonerNumberTextField
	 */
	protected String getPrisonerNumberTextField() {
		if (prisonerNumberTextField.getText().equals("")) {
			return null;
		} else {
			return prisonerNumberTextField.getText();
		}
	}

	/**
	 * @param prisonerNumberTextField
	 *            the prisonerNumberTextField to set
	 */
	protected void setPrisonerNumberTextField(String textField) {
		if (textField != null) {
			prisonerNumberTextField.setText(textField);
		}
	}

	/**
	 * @return the prisonIDTextField
	 */
	protected String getPrisonID() {
		if (comboPrisonID.getSelectedIndex() != 0)
			return ((RefSystemCodeBasicValue) (comboPrisonID.getSelectedItem())).getCode();
		else
			return "";
	}

	/**
	 * @param prisonIDTextField
	 *            the prisonIDTextField to set
	 */
	protected void setPrisonID(String oldString) {
		if (oldString != null) {
			for (int index = 1; index < prisonIDList.size(); index++) {
				if (prisonIDList.get(index).getCode().equals(oldString))
					comboPrisonID.setSelectedIndex(index);
			}
		}
	}

	protected void setGender(String code) {
		comboGender.setSelectedItemByCode(code);
		if ("0".equals(code)) {
			custodyCheckBox.setEnabled(false);
			firstNameTextField.setEnabled(false);
			comboPrisonID.setEnabled(false);
		} else {
			firstNameTextField.setEnabled(true);
			custodyCheckBox.setEnabled(true);
			comboPrisonID.setEnabled(true);
		}
	}

	protected String getIsCompany(int g) {
		if (g == 0) {
			return "Y";
		} else {
			return "N";
		}
	}

	protected String getDateTextField() {
		return dateOfBirth.getText();
	}

	protected Calendar getDateTextFieldCalendar() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			if (!getDateTextField().isEmpty()) {
				Date parsedDate = dateFormat.parse(getDateTextField());
				Calendar cal = Calendar.getInstance();
				cal.setTime(parsedDate);
				return cal;
			}
		} catch (ParseException e) {
			log.error("unable to get date field");
		}
		return null;
	}

	protected void setDateTextFieldCalendar(Calendar date) {
		dateOfBirth.setDate(date);
		if (dobValidator != null) {
			dobValidator.setOrgDate(date);
		}
	}

	protected void setCustodyCheckBox(Boolean check) {
		if (check != null) {
			custodyCheckBox.setSelected(check);
		}
	}

	protected boolean getCustodyCheckBox() {
		return custodyCheckBox.isSelected();
	}

	protected String getCustodyCheckBoxString() {
		if (getCustodyCheckBox())
			return "Y";
		else
			return "N";
	}

	/**
	 * Set ethnic appearance when you're viewing details of a defendant. Loop
	 * starts at 1 as 0 is just 'Select Ethnic appearance'
	 * 
	 * @param code
	 *            the Code of the ethnic appearance
	 */
	private void setEthnicAppearance(String code) {
		if (code != null && !code.isEmpty()) {
			for (int j = 1; j < comboEthnicAppearance.getItemCount(); j++) {
				if (((RefSystemCodeBasicValue) comboEthnicAppearance.getItemAt(j)).getCode().equals(code)) {
					comboEthnicAppearance.setSelectedIndex(j);
				}
			}
		}
	}

	/**
	 * Set ethnic self defined when you're viewing details of a defendant. Loop
	 * starts at 1 as 0 is just 'Select Ethnic appearance'
	 * 
	 * @param code
	 *            the Code of Select Ethnicity
	 */
	private void setEthnicitySelfDefined(String code) {
		if (code != null && !code.isEmpty()) {
			for (int j = 1; j < comboSelectEthnicity.getItemCount(); j++) {
				if (((RefSystemCodeBasicValue) comboSelectEthnicity.getItemAt(j)).getCode().equals(code)) {
					comboSelectEthnicity.setSelectedItem(comboSelectEthnicity.getItemAt(j));
				}
			}
		}
	}
	
	/**
	 * Set licence type when you're viewing details of a defendant. Loop
	 * starts at 1 as 0 is just 'Select Licence Type'
	 * 
	 * @param displayName
	 *            the Display name of Selected Licence Type
	 */
	private void setLicenceType(String displayName) {
		if (displayName != null && !displayName.isEmpty()) {
			for (int j = 1; j < comboSelectLicenceType.getItemCount(); j++) {
				if (((DropdownCodeStringValue) comboSelectLicenceType.getItemAt(j)).getDisplayName().equals(displayName)) {
					comboSelectLicenceType.setSelectedItem(comboSelectLicenceType.getItemAt(j));
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private ArrayList<RefSystemCodeBasicValue> getEthnicAppearanceCodes() {
		if (ethnicAppearanceList != null) {
			return ethnicAppearanceList;
		}
		RefSystemCodeBasicValue defValue = new RefSystemCodeBasicValue();
		defValue.setDecode("Select Ethnic Appearance");
		try {
			RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
			criteria.setCodeType(RefSystemCodeCriteria.CodeType.ETHNIC_APPEARANCE);
			criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
			ethnicAppearanceList = (ArrayList<RefSystemCodeBasicValue>) (XhibitDelegateHelper.getBizRefDelegate()
					.findSystemCodes(criteria));
			Collections.sort(ethnicAppearanceList, new TicketTypeComparator());

		} catch (Exception e) {
			XHIBITConstant.handleError(e);
		}
		ethnicAppearanceList.add(0, defValue);
		return ethnicAppearanceList;

	}

	@SuppressWarnings("unchecked")
	private ArrayList<RefSystemCodeBasicValue> getEthnicClassificationCodes() {
		if (ethnicClassificationList != null) {
			return ethnicClassificationList;
		}
		RefSystemCodeBasicValue defValue = new RefSystemCodeBasicValue();
		defValue.setDecode("Select Ethnicity");
		try {
			RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
			criteria.setCodeType(RefSystemCodeCriteria.CodeType.ETHNIC_CLASSIFICATIN);
			criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
			ethnicClassificationList = (ArrayList) (XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria));
			Collections.sort(ethnicClassificationList, new TicketTypeComparator());

		} catch (Exception e) {
			XHIBITConstant.handleError(e);
		}
		ethnicClassificationList.add(0, defValue);

		return ethnicClassificationList;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<RefSystemCodeBasicValue> getPrisonIDCodes() {
		if (prisonIDList != null) {
			return prisonIDList; // in case performed multiple times
		}

		RefSystemCodeBasicValue defValue = new RefSystemCodeBasicValue();
		defValue.setDecode("Select Prison");
		try {
			RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
			criteria.setCodeType(RefSystemCodeCriteria.CodeType.PRISON_ID);
			criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
			prisonIDList = (ArrayList) (XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria));

			Sorter.sort(prisonIDList, new String[] { "code" }, Sorter.ASCENDING);
			prisonIDList.add(0, defValue);
		} catch (Exception e) {
			log.error("Error occurred in DefendantAppellantAddAmend " + e);
			XHIBITConstant.handleError(e);
		}

		return prisonIDList;
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<DropdownCodeStringValue> getLicenceTypes() {
		if (licenceTypeList != null) {
			return licenceTypeList;
		}
		
		try {
			
			String TYPE_DEFAULT = "-1", TYPE_NONE = "0", TYPE_PROV = "1", TYPE_FULL = "2", TYPE_NONE_UK = "3", TYPE_DVLA = "5";
			licenceTypeList = new ArrayList<DropdownCodeStringValue>();
			
			licenceTypeList.add(0, new DropdownCodeStringValue("Select Licence Type", TYPE_DEFAULT));
			licenceTypeList.add(new DropdownCodeStringValue(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "typeNone"), TYPE_NONE));
			licenceTypeList.add(new DropdownCodeStringValue(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "typeProv"), TYPE_PROV));
			licenceTypeList.add(new DropdownCodeStringValue(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "typeFull"), TYPE_FULL));
			licenceTypeList.add(new DropdownCodeStringValue(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "typeNonUk"), TYPE_NONE_UK));
			licenceTypeList.add(new DropdownCodeStringValue(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "typeDvla"), TYPE_DVLA));
			
		} catch (Exception e) {
			XHIBITConstant.handleError(e);
		}
		return licenceTypeList;

	}

	/**
	 * 
	 * @param dA
	 * @param dV
	 * @param df1
	 * @param df2
	 * @param df3
	 * @param df4
	 * @throws CSRecoverableException
	 */
	public void saveDefendantAppellant(DefendantAppellant dA, DefendantValue dV, DefendantReferenceBasicValue df1,
			DefendantReferenceBasicValue df2, DefendantReferenceBasicValue df3, DefendantReferenceBasicValue df4) throws CSRecoverableException {
		DefendantBasicValue def = null;
		
		DefendantControllerBeanBusinessDelegate del = XhibitDelegateHelper.getDefendantDelegate();
		String userName = XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME);

		def = del.createDefendant(dV, userName, df1, df2, df3, df4 );
		dV.setAddressId(def.getAddressID());
		dA.setAddressId(def.getAddressID());
		
		DefendantReferenceControllerBeanBusinessDelegate delRef = XhibitDelegateHelper.getDefendantReferenceDelegate();

		DefendantReferenceBasicValue dReference1 = delRef.findByDefendantIdAndReferenceName(def.getId(), "DRIVER_NO");
		if(dReference1 !=null ) {
			df1.setId(dReference1.getId());
			dA.setDefRef1(dReference1.getId());
		}
		DefendantReferenceBasicValue dReference2 = delRef.findByDefendantIdAndReferenceName(def.getId(), "Prisoner Number");
		if(dReference2 !=null ) {
			df2.setId(dReference2.getId());
			dA.setDefRef2(dReference2.getId());
		}
		
		// Where do we save the licence type and licence issue no?
		DefendantReferenceBasicValue dReference3 = delRef.findByDefendantIdAndReferenceName(def.getId(), "LICENCE_TYPE");
		if(dReference3 !=null ) {
			df3.setId(dReference3.getId());
			dA.setDefRefLicenceTypeId(dReference3.getId());
		}
		
		DefendantReferenceBasicValue dReference4 = delRef.findByDefendantIdAndReferenceName(def.getId(), "LICENCE_ISSUE_NUMBER");
		if (dReference4 !=null) {
			df4.setId(dReference4.getId());
			dA.setDefRefLicenceIssueNumberId(dReference4.getId());
		}

		dA.setDefendantId(def.getId());
		dV.setDefendantID(def.getId());
		if (model.getCallingClass() instanceof DefendantAppellantTab) {
			((DefendantAppellantTab) model.getCallingClass()).callback(dA, dV, true);
		} else {
			((REDELPanel) model.getCallingClass()).callback(dV);

		}
	}

	/**
	 * 
	 * @param dA
	 * @param dV
	 * @param addBV
	 * @param df1
	 * @param df2
	 * @param df3
	 * @param df4
	 * @throws CSRecoverableException
	 */
	public void updateDefendantAppellant(DefendantAppellant dA, DefendantValue dV, AddressBasicValue addBV,
			DefendantReferenceBasicValue df1, DefendantReferenceBasicValue df2, DefendantReferenceBasicValue df3,
			DefendantReferenceBasicValue df4) throws CSRecoverableException {
	
		String userName = XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME);
		
		DefendantReferenceControllerBeanBusinessDelegate delRef = XhibitDelegateHelper.getDefendantReferenceDelegate();

		DefendantReferenceBasicValue dReference1 = delRef.findByDefendantIdAndReferenceName(dA.getDefendantId(), "DRIVER_NO");
		DefendantReferenceBasicValue dReference2 = delRef.findByDefendantIdAndReferenceName(dA.getDefendantId(), "Prisoner Number");
		DefendantReferenceBasicValue dReference3 = delRef.findByDefendantIdAndReferenceName(dA.getDefendantId(), "LICENCE_TYPE");
		DefendantReferenceBasicValue dReference4 = delRef.findByDefendantIdAndReferenceName(dA.getDefendantId(), "LICENCE_ISSUE_NUMBER");

		if (dReference1 != null) {
			df1.setId(dA.getDefRef1());
		}
		if (dReference2 != null) {
			df2.setId(dA.getDefRef2());
		}
		if (dReference3 != null) {
			df3.setId(dA.getDefRefLicenceTypeId());
			if (df3.getReferenceValue().equals("Select Licence Type")) {
				df3.setReferenceValue("");
			}
		}
		if (dReference4 != null) {
			df4.setId(dA.getDefRefLicenceIssueNumberId());
		}
		
		
		// Update the defendants detail
		XhibitDelegateHelper.getDefendantDelegate().updateDefendant(dV, addBV, userName, dA.getDefendantId(), df1, df2, df3, df4);
		if (dA.getAddressId() != null) {
			addBV.setId(dA.getAddressId());
			dV.setAddressId(dA.getAddressId());
		}
		dV.setDefendantID(dV.getDefendantID());
		dA.setDefendantId(dA.getDefendantId());

		dA.setDataChanged(dA.compareDefApp(defAppOld));
		dV.setDataChanged(dV.compareDefValueAddress(defValOld));

		((DefendantAppellantTab) model.getCallingClass()).callback(dA, dV, false);
	}

	/**
	 * If the model is not null then populate the fields with the values. Also
	 * sets the caret to 0 so that if the field is too long then it'll show the
	 * first half of the string instead of the end of the string.
	 */
	private void moveModelToScreen() {
		if (model != null) {
			if (model.getCallingClass() instanceof DefendantAppellantTab) {
				DefendantControllerBeanBusinessDelegate del = XhibitDelegateHelper.getDefendantDelegate();
				try {
					if (model.getDA() != null) {
						if (model.getDA().getDefendantId() != null) {
							String parentGuardian = null;
							String dfD1 = null;
							Integer dfId1 = null;
							String dfD2 = null;
							Integer dfId2 = null;
							String defRefLicenceType = null;
							Integer defRefLicenceTypeId = null;
							String defRefLicenceIssueNumber = null;
							Integer defRefLicenceIssueNumberId = null;

							model.setDV(new DefendantValue());
							model.setAddBV(new AddressBasicValue());
							model.setDV(del.findByDefId(model.getDA().getDefendantId()));
							int addressId = model.getDV().getAddressId();
							BisRefControllerBeanBusinessDelegate brcbbd = XhibitDelegateHelper.getBizRefDelegate();
							model.setAddBV(brcbbd.findByPK(addressId));

							if (model.getAddBV() != null) {
								defValOld = new DefendantValue(
										new AddressValue(model.getAddBV().getAddress1(), model.getAddBV().getAddress2(),
												model.getAddBV().getAddress3(), model.getAddBV().getAddress4(),
												model.getAddBV().getTown(), model.getAddBV().getCounty(),
												model.getAddBV().getPostcode(), model.getAddBV().getCountry()));
							}

							dfD1 = brcbbd.findAllByDefendantIdAndReferenceName(model.getDA().getDefendantId(), "DRIVER_NO");
							if (!(dfD1 == null || dfD1.equals(""))) {
								dfId1 = brcbbd.findReferenceNameIdByDefendantId(model.getDA().getDefendantId(), "DRIVER_NO");
								model.getDA().setDefRef1(dfId1);
							}
							dfD2 = brcbbd.findAllByDefendantIdAndReferenceName(model.getDA().getDefendantId(), "Prisoner Number");
							if (!(dfD2 == null || dfD2.equals(""))) {
								dfId2 = brcbbd.findReferenceNameIdByDefendantId(model.getDA().getDefendantId(), "Prisoner Number");
								model.getDA().setDefRef2(dfId2);
							}
							defRefLicenceType = brcbbd.findAllByDefendantIdAndReferenceName(model.getDA().getDefendantId(), "LICENCE_TYPE");
							if (!(defRefLicenceType == null || defRefLicenceType.equals(""))) {
								defRefLicenceTypeId = brcbbd.findReferenceNameIdByDefendantId(model.getDA().getDefendantId(), "LICENCE_TYPE");
								model.getDA().setDefRefLicenceTypeId(defRefLicenceTypeId);
							}
							defRefLicenceIssueNumber = brcbbd.findAllByDefendantIdAndReferenceName(model.getDA().getDefendantId(),
									"LICENCE_ISSUE_NUMBER");
							if (!(defRefLicenceIssueNumber == null || defRefLicenceIssueNumber.equals(""))) {
								defRefLicenceIssueNumberId = brcbbd.findReferenceNameIdByDefendantId(model.getDA().getDefendantId(),
										"LICENCE_ISSUE_NUMBER");
								model.getDA().setDefRefLicenceIssueNumberId(defRefLicenceIssueNumberId);
							}
							
							// Get existing licence type and licence issue no (if they exist) and add details for that to be sent to screen?

							if (model.getDV().getDateOfBirth() != null) {
								setDateTextFieldCalendar(model.getDV().getDateOfBirth());
							}
							if (model.getDV().getParentGuardianName() != null) {
								parentGuardian = model.getDV().getParentGuardianName();
							}
							if (model.getDV().getSurName() != null) {
								setSurnameTextField(model.getDV().getSurName());
							}
							if (model.getDV().getFirstName() != null) {
								setFirstNameTextField(model.getDV().getFirstName());
							}
							if (model.getDV().getInitials() != null) {
								setInitialTextField(model.getDV().getInitials());
							}
							if (model.getDV().getMiddleName() != null) {
								setOtherNameTextField(model.getDV().getMiddleName());
							}
							if (model.getDV().getDateOfBirth() != null) {
								setDateTextFieldCalendar(model.getDV().getDateOfBirth());
							}
							if (model.getDV().getEthnicAppearanceCode() != null) {
								setEthnicAppearance(model.getDV().getEthnicAppearanceCode());
							}
							if (model.getDV().getEthnicitySelfDefined() != null) {
								setEthnicitySelfDefined(model.getDV().getEthnicitySelfDefined());
							}
							if (model.getDV().getGender() != null) {
								setGender(String.valueOf(model.getDV().getGender()));
							}
							if (model.getDV().getPrisonId() != null) {
								setPrisonID(model.getDV().getPrisonId());
							}

							// CTX-1446
							if (model.getDV().getCurrentPrisonStatus() != null) {
								setCustodyCheckBox(model.getDV().getCurrentPrisonStatus().equals("Y"));
							}
							if (this.custodyCheckBox.isSelected()) {
								prisonerNumberTextField.setEnabled(isEnabled());
								comboPrisonID.setEnabled(isEnabled());
							} else {
								prisonerNumberTextField.setEnabled(!isEnabled());
								comboPrisonID.setEnabled(!isEnabled());
							}
							model.getDA().setAddressId(model.getAddBV().getAddressId());
							setAddress1TextField(model.getAddBV().getAddress1());
							setAddress2TextField(model.getAddBV().getAddress2());
							setAddress3TextField(model.getAddBV().getAddress3());
							setAddress4TextField(model.getAddBV().getAddress4());
							setTownTextField(model.getAddBV().getTown());
							setCountyTextField(model.getAddBV().getCounty());
							setPostcodeTextField(model.getAddBV().getPostcode());

							if (!(dfD1 == null || dfD1.equals(""))) {
								model.getDA().setDriverNumber(dfD1);
								setDriverNumberTextField(dfD1);
							}
							
							// Do the licence type and licence issue number
							if (!(defRefLicenceType == null || defRefLicenceType.equals(""))) {
								model.getDA().setLicenceType(defRefLicenceType);
								setLicenceType(defRefLicenceType);
							}
							
							if (!(defRefLicenceIssueNumber == null || defRefLicenceIssueNumber.equals(""))) {
								model.getDA().setLicenceIssueNumber(defRefLicenceIssueNumber);
								setLicenceIssueNumberTextField(defRefLicenceIssueNumber);
							}
							
							if (!(dfD2 == null || dfD2.equals(""))) {
								model.getDA().setPrisonerNumber(dfD2);
								setPrisonerNumberTextField(dfD2);
							}
							if (model.getDA().getPrisonId() != null) {
								setPrisonID(model.getDA().getPrisonId());
							}
							if (parentGuardian != null) {
								if (parentGuardian.length() != 0) {
									parentGuardianTextField.setEnabled(true);
									setParentGuardianTextField(parentGuardian);
								}
							} else {
								setParentGuardianTextField("");
							}
						}
						ArrayList defCollection = (ArrayList) XhibitDelegateHelper.getDefendantDelegate()
								.findByDefendantId(model.getDA().getDefendantId());
						ArrayList caseCollection = (ArrayList) XhibitDelegateHelper.getDefendantDelegate()
								.findLinkedCases(model.getDA().getCourtId(), model.getDA().getDefendantId(), null);

						for (int j = 0; j < caseCollection.size(); j++) {
							CaseLinkingValue cBV = (CaseLinkingValue) caseCollection.get(j);
							if (cBV.getDateTransTo() != null) {
								break;
							}
						}
						for (int i = 0; i < defCollection.size(); i++) {
							DefendantOnCaseBasicValue dVal = (DefendantOnCaseBasicValue) defCollection.get(i);
							if (dVal.getCaseID() == model.getDA().getCaseId()
									|| "E".equals(dVal.getResultsVerified())) {
								break;
							}
							if ("C".equals(dVal.getCurrentBcStatus()) || "J".equals(dVal.getCurrentBcStatus())) {
								inCustodyValidation = true;
							}
						}

					}
				} catch (Exception er) {
					log.error("exception occurred " + er);
					XHIBITConstant.handleError(er);
				}
			}
		}
	}

	/**
	 * Set the data entered on screen into the model.
	 */
	private void moveScreenToModel() {
		if (model.getDV() == null) {
			model.setDV(new DefendantValue());
		}
		DefendantValue dV = model.getDV();

		if (model.getDA() == null) {
			model.setDA(new DefendantAppellant());
		}
		DefendantAppellant dA = model.getDA();

		if (model.getDf1() == null) {
			model.setDf1(new DefendantReferenceBasicValue());
		}
		DefendantReferenceBasicValue df1 = model.getDf1();

		if (model.getDf2() == null) {
			model.setDf2(new DefendantReferenceBasicValue());
		}
		DefendantReferenceBasicValue df2 = model.getDf2();
		
		if (model.getDefRefLicenceType() == null) {
			model.setDefRefLicenceType(new DefendantReferenceBasicValue());
		}
		DefendantReferenceBasicValue defRefLicenceType = model.getDefRefLicenceType();
		
		if (model.getDefRefLicenceIssueNumber() == null) {
			model.setDefRefLicenceIssueNumber(new DefendantReferenceBasicValue());
		}
		DefendantReferenceBasicValue defRefLicenceIssueNumber = model.getDefRefLicenceIssueNumber();

		if (model.getAddBV() == null) {
			model.setAddBV(new AddressBasicValue());
		}
		AddressBasicValue addBV = model.getAddBV();

		String currentPrisonStatus = getCustodyCheckBoxString();
		String isCompany = getIsCompany(getGender());
		String dOB = null;
		Calendar calDOB = null;

		AddressValue av = new AddressValue();
		dV.setSurName(getSurnameTextField());
		dA.setSurname(getSurnameTextField());
		dV.setGender(getGender());
		dA.setGender(getGender());
		dV.setInitials(getInitialTextField());
		dA.setInitials(getInitialTextField());
		dV.setFirstName(getFirstNameTextField());
		dA.setFirstName(getFirstNameTextField());
		dV.setMiddleName(getOtherNameTextField());
		dA.setOtherName(getOtherNameTextField());
		dV.setParentGuardianName(getParentGuardianTextField());
		dA.setParentGuardian(getParentGuardianTextField());
		if (!isCompany.equalsIgnoreCase("Y")) {
			dOB = getDateTextField();
			calDOB = getDateTextFieldCalendar();
		}
		dV.setDateOfBirth(calDOB);
		dA.setDateOfBirth(dOB);

		dV.setEthnicAppearanceCode(((RefSystemCodeBasicValue) comboEthnicAppearance.getSelectedItem()).getCode());
		dA.setEthnicAppearance(((RefSystemCodeBasicValue) comboEthnicAppearance.getSelectedItem()).getCode());
		dV.setEthnicitySelfDefined(((RefSystemCodeBasicValue) comboSelectEthnicity.getSelectedItem()).getCode());
		dA.setEthnicSelfDefined(((RefSystemCodeBasicValue) comboSelectEthnicity.getSelectedItem()).getCode());
		
		av.setAddress1(getAddress1TextField());
		addBV.setAddress1(getAddress1TextField());
		av.setAddress2(getAddress2TextField());
		addBV.setAddress2(getAddress2TextField());
		av.setAddress3(getAddress3TextField());
		addBV.setAddress3(getAddress3TextField());
		av.setAddress4(getAddress4TextField());
		addBV.setAddress4(getAddress4TextField());
		av.setTown(getTownTextField());
		addBV.setTown(getTownTextField());
		av.setCounty(getCountyTextField());
		addBV.setCounty(getCountyTextField());
		av.setPostcode(getPostcodeTextField());
		addBV.setPostcode(getPostcodeTextField());
		dV.setAddressValue(av);

		df1.setReferenceValue(getDriverNumberTextField());
		df1.setReferenceName("DRIVER_NO");
		dV.setDriverNo(df1);
		dA.setDriverNumber(getDriverNumberTextField());
		
		defRefLicenceType.setReferenceName("LICENCE_TYPE");
		defRefLicenceType.setReferenceValue(formatLicenceType(getLicenceType().getSelectedItem().toString()));
		dV.setLicenceType(defRefLicenceType);
		dA.setLicenceType(((DropdownCodeStringValue) comboSelectLicenceType.getSelectedItem()).getCode());
		
		defRefLicenceIssueNumber.setReferenceName("LICENCE_ISSUE_NUMBER");
		defRefLicenceIssueNumber.setReferenceValue(getLicenceIssueNumberTextField());
		dV.setLicenceIssueNumber(defRefLicenceIssueNumber);
		dA.setLicenceIssueNumber(getLicenceIssueNumberTextField());
		
		// Do the licence type and licence issue number
		
		// in custody checkbox
		if (currentPrisonStatus != null) {
			dA.setCurrentPrisonStatus(currentPrisonStatus);
			dV.setCurrentPrisonStatus(currentPrisonStatus);
		} else { // CTX-1446
			dA.setCurrentPrisonStatus("N");
			dV.setCurrentPrisonStatus("N");
		}
		df2.setReferenceValue(getPrisonerNumberTextField());
		df2.setReferenceName("Prisoner Number");
		dV.setPrisonerNo(df2);
		dA.setPrisonerNumber(getPrisonerNumberTextField());
		dV.setPrisonId(getPrisonID());
		dA.setPrisonId(getPrisonID());
		dV.setCourtID(XhibitSingleton.getInstance().getCourtId());
		dA.setCourtId(XhibitSingleton.getInstance().getCourtId());
		dV.setIsCompany(isCompany);
		dA.setIsCompany(isCompany);
		model.setDV(dV);
		model.setDA(dA);
		model.setAddBV(addBV);
	}

	private void showCancelConfirmationMsg() throws CSRecoverableException {
		boolean messageBoxReply = XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.XhibitActionResources,
						"DefendantAppellantAddAmendCancelConfirmationTitle"),
				true, XMessageBox.ICONQUESTION,
				XHIBITConstant.getResource(XhibitBundles.XhibitActionResources,
						"DefendantAppellantAddAmendCancelConfirmationMessageUnsavedData"),
				XMessageBox.YESNO, XMessageBox.DEFAULTCANCEL);
		if (!messageBoxReply) {
			throw new UserCancelException();
		}
	}

	@Override
	public void stepInitialise() throws CSRecoverableException {
	}

	/**
	 * Default gridbag that's used throughout the panels.
	 * 
	 * @return gridbagconstraints
	 */
	private GridBagConstraints getGridBagLayout() {
		return new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				XHIBITConstant.nonContainerInsets, 0, 0);
	}

	/**
	 * moves the actual values into the fields.
	 */
	@Override
	public void stepActivate() throws CSRecoverableException {
		moveModelToScreen();
		btnSave.setEnabled(false);
		addChangeListeners(defendantAppellantDetailsPanel.getComponents());
		addChangeListeners(custodyDetailsPanel.getComponents());
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
	}

	/**
	 * Checks if any of the validation on the page is incorrect.
	 */
	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		// Throw exception if validation failures to prevent saving
		if (!ValidationControllerFactory.validateComponents(validationControllers)) {
			throw new CSValidationException("validation.general", "Field validation failed");
		}
		if (!(lblIPostcode.getText().equals(" "))) {
			throw new CSValidationException("validation.general", "Field validation failed");
		}
		if (inCustodyValidation && !custodyCheckBox.isSelected()) {
			throw new CSValidationException("gui.updateDefendantDialog.invalidDriverLicense", "Invalid driver license number entered");
		}
		
		boolean licenceValid = validateLicence();
		if (!licenceValid) {
			throw new CSValidationException("validation.licenceInvalid", "Field validation failed");
		}
	}
	
	/**
	 * Licence validation:
	 * - If "Full" or "Provisional" licence option shown then a licence issue number must be entered and a driver number 
	 * @return
	 */
	private boolean validateLicence() throws CSValidationException {
		String driverNumber = getDriverNumberTextField();
		String licenceTypeId = ((DropdownCodeStringValue) comboSelectLicenceType.getSelectedItem()).getCode();
		String licenceIssueNumber = getLicenceIssueNumberTextField();
		
		if (licenceTypeId.equals("1") || licenceTypeId.equals("2")) {
			// Licence Issue Number must be present
			if (licenceIssueNumber == null || licenceIssueNumber.length() == 0) {
				throw new CSValidationException("gui.updateDefendantDialog.invalidDriverLicenceIssueNumber", "Invalid driver licence issue number for selected licence type");
			}
			if (driverNumber == null || driverNumber.length() == 0) {
				throw new CSValidationException("gui.updateDefendantDialog.invalidDriverNumber", "A valid driver number entered for selected licence type");
			}
			if (driverNumber != null && driverNumber.length()>0) {
				if (!UpdateDefendantPanel.validateDriverNumber(driverNumber)) {
					throw new CSValidationException("gui.updateDefendantDialog.invalidDriverNumberFormat", "The driver number entered is invalid");
				}
			}
		} else { // Licence Issue Number should not be present
			if (licenceIssueNumber != null && licenceIssueNumber.length() > 0) {
				throw new CSValidationException("gui.updateDefendantDialog.invalidDriverLicenceIssueNumber2", "Invalid driver licence issue number for selected licence type");
			}
		}
		
		/*if (driverNumber != null && driverNumber.length()>0) {
			if (!UpdateDefendantPanel.validateDriverNumber(driverNumber)) {
				throw new CSValidationException("gui.updateDefendantDialog.invalidDriverNumberFormat", "The driver number entered is invalid");
			}
			// Must also have a licence type selected
			if (licenceTypeId.equals("1") || licenceTypeId.equals("2")) {
				throw new CSValidationException("gui.updateDefendantDialog.licenceTypeMustBeSelected", "A valid licence type must be selected for this driver number");
			}
		}*/
		
		return true;
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
	}

	/**
	 * If save button clicked then check validation and then save the database
	 * changes
	 */
	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (btnSave.equals(getDeinitialiseSource())) {
			stepValidate();

			// --- Check DOB for <10 & >70 ---
			List<String> dobErrors = new ArrayList<String>();
			dobValidator.validate(dateOfBirth, dobErrors);
			if (dobErrors.isEmpty()) {
				moveScreenToModel();

				if (model.getDV().getDefendantID() == null || model.getDV().getDefendantID().equals(0)) {
					saveDefendantAppellant(model.getDA(), model.getDV(), model.getDf1(), model.getDf2(),
							model.getDefRefLicenceType(), model.getDefRefLicenceIssueNumber());
				} else {
					updateDefendantAppellant(model.getDA(), model.getDV(), model.getAddBV(), model.getDf1(),
							model.getDf2(), model.getDefRefLicenceType(), model.getDefRefLicenceIssueNumber());
				}
				changesMade = false;
				if (dialogParent != null) {
					dialogParent.dispose();
				}
				parentDialog.dispose();
			}
		} else { // Cancel button clicked or closing the window
			if (changesMade) {
				showCancelConfirmationMsg();
			} else {
				parentDialog.clearStatusBarScreenCode();
				parentDialog.dispose();
			}
		}
	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
	}

	// Method to add change listeners to each component within form
	private void addChangeListeners(Component[] components) {
		for (int i = 0; i < components.length; i++) {
			if (components[i].getClass() == XTextField.class) {
				((XTextField) components[i]).getDocument().addDocumentListener(new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						changesMade = true;
						enableSaveButton();
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						changesMade = true;
						enableSaveButton();
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						changesMade = true;
						enableSaveButton();
					}
				});
			} else if (components[i].getClass() == XDatePanel.class) {
				((XDatePanel) components[i]).getDateComponent().addMChangeListener(new MChangeListener() {
					@Override
					public void valueChanged(MChangeEvent event) {
						changesMade = true;
						enableSaveButton();
					}
				});

				((XDatePanel) components[i]).getEntryField().getDisplay().addKeyListener(new KeyListener() {
					@Override
					public void keyTyped(KeyEvent e) {
						changesMade = true;
						enableSaveButton();
					}

					@Override
					public void keyPressed(KeyEvent e) {
					}

					@Override
					public void keyReleased(KeyEvent e) {
					}
				});
			} else if (components[i].getClass() == XComboBox.class) {
				((XComboBox) components[i]).addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						changesMade = true;
						enableSaveButton();
					}
				});
			} else if (components[i].getClass() == JCheckBox.class) {
				((JCheckBox) components[i]).addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						changesMade = true;
						enableSaveButton();
					}
				});
			}
		}
	}

	// iterate through all validators and see if label is "Field is mandatory"
	// text
	public void enableSaveButton() {
		boolean isValid = true;
		for (int i = 0; i < mandatoryFields.size(); i++) {
			if (mandatoryFields.get(i).getClass() == XTextField.class) {
				if (((XTextField) mandatoryFields.get(i)).isEnabled()) {
					String s = ((XTextField) mandatoryFields.get(i)).getText();
					if (s.equals("") || s == null) {
						isValid = false;
						break;
					}
				}
			}
			if (mandatoryFields.get(i).getClass() == XDatePanel.class) {
				if (((XDatePanel) mandatoryFields.get(i)).isEnabled()) {
					String s = ((XDatePanel) mandatoryFields.get(i)).getText();
					if (s.equals("") || s == null) {
						isValid = false;
						break;
					}
				}
			}
			if (mandatoryFields.get(i).getClass() == XComboBox.class) {
				if (((XComboBox) mandatoryFields.get(i)).isEnabled()) {
					if (((XComboBox) mandatoryFields.get(i)).getSelectedIndex() == 0) {
						isValid = false;
						break;
					}
				}
			}
		}
		btnSave.setEnabled(isValid);
	}

	/*
	 * Used to dynamically add a asterisk to a label to indicate its mandatory -
	 * C.Kudzin CTX-1810
	 */
	public void addMandatoryLabel(JLabel label) {
		String text = label.getText();
		if (!(text.substring(text.length() - 1).equals("*"))) {
			text = text + "*";
			label.setText(text);
		}
	}

	/*
	 * Used to dynamically remove a asterisk to a label to indicate its no
	 * longer mandatory - C.Kudzin CTX-1810
	 */
	public void removeMandatoryLabel(JLabel label) {
		String text = label.getText();
		if (text.substring(text.length() - 1).equals("*")) {
			text = text.substring(0, text.length() - 1);
			label.setText(text);
		}
	}
	/**
	 * Add a leading 0 if only 1 digit
	 * 
	 * @return
	 */
	private String formatLicenceIssueNumber(String issueNumber) {
		if ((issueNumber != null) && (issueNumber.length() == 1)) {
			 return "0" + issueNumber;
		} else {
			return issueNumber;
		}
	}
	
	/**
	 * 
	 * @param licenceType
	 * @return
	 */
	private String formatLicenceType(String licenceType) {
		if (licenceType.equals("Select Licence Type")) {
			return "";
		}
		return licenceType;
	}
}
