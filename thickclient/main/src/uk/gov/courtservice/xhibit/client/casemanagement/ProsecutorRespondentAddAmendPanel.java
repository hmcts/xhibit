package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefProsecutorAgencyComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.TextRegexValidator;
import uk.gov.courtservice.xhibit.client.util.validation.TextValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class ProsecutorRespondentAddAmendPanel extends XPanel implements ValidationListener {
	private static final long serialVersionUID = 1L;
	private XTextField txtInitials;
	private XTextField txtTitle;
	private XTextField txtName;
	private XTextField txtFirstName;
	private XTextField txtOtherNames;
	private XTextField txtCpsCode;
	private XTextField txtAddress1;
	private XTextField txtAddress2;
	private XTextField txtAddress3;
	private XTextField txtAddress4;
	private XTextField txtTown;
	private XTextField txtCounty;
	private XTextField txtPostcode;
	private XTextField txtTelephone;
	private XTextField txtFax;
	private XTextField txtSecureEmail;
	private XTextField txtNonSecureEmail;
	private XTextField txtDocExReference;

	private JLabel lblInitials;
	private JLabel lblTitle;
	private JLabel lblName;
	private JLabel lblFirstName;
	private JLabel lblOtherNames;
	private JLabel lblCPSCode;
	private JLabel lblAddress;
	private JLabel lblTown;
	private JLabel lblCounty;
	private JLabel lblPostcode;
	private JLabel lblTelephone;
	private JLabel lblFax;
	private JLabel lblSecureEmail;
	private JLabel lblNonSecure;
	private JLabel lblDocRef;

	private JLabel lblIInitials;
	private JLabel lblITitle;
	private JLabel lblIName;
	private JLabel lblIFirstName;
	private JLabel lblIOtherNames;
	private JLabel lblICPSCode;
	private JLabel lblIAddress1;
	private JLabel lblIAddress2;
	private JLabel lblIAddress3;
	private JLabel lblIAddress4;
	private JLabel lblITown;
	private JLabel lblICounty;
	private JLabel lblIPostcode;
	private JLabel lblITelephone;
	private JLabel lblIFax;
	private JLabel lblISecureEmail;
	private JLabel lblINonSecure;
	private JLabel lblIDocRef;

	private JButton btnSave;
	private JButton btnBack;
	private JButton btnCancel;

	private String fullName;
	private RefProsecutorAgencyComplexValue refProsecutor;
	private boolean changesMade = false;

	private ProsecutorRespondentAddAmendModel model;
	private ProsecutorRespondentAddAmendDialog parentDialog;
	private ProsecutorRespondentAddAmendPanel thisClass;

	// Validators
	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();

	// Panels
	private JPanel respondentDetailsPanel = null;
	private CustomButtonPanel buttonPanel = null;
	private JPanel mainPanel = null;
	
	private BisRefControllerBeanBusinessDelegate bizRefDelegate;			
	private static final String userDisplayName = XhibitSingleton.getInstance().getUserSession()
			.getSessionProperty(UserTerminalProperties.DISPLAY_NAME);


	/* @author - kudzinc - CTX-1870, 1871, 1827 04/05/2018 */

	public ProsecutorRespondentAddAmendPanel(ProsecutorRespondentAddAmendDialog parent,
			ProsecutorRespondentAddAmendModel model) throws CSRecoverableException {
		
		bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
		this.model = model;
		this.parentDialog = parent;
		jbInit();
		moveModelToScreen();
	}

	private void jbInit() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(900, 600));
		GridBagConstraints gbc = getGridBagLayout();
		thisClass = this;
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		parentDialog.setCancelVerifyInputWhenFocusTarget(true);
		parentDialog.setTitle("Add New Prosecutor/Respondent");

		JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainPanel.setPreferredSize(new Dimension(750, 500));
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 0, 0, 0);
		this.add(scrollPane, gbc);

		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weighty = 0.5;
		gbc.gridwidth = 4;
		mainPanel.add(getRespondentDetailsPanel(), gbc);

		buttonPanel = (CustomButtonPanel) parentDialog.getButtonPanel();

		btnBack = buttonPanel.addButton("ProsResBack", false, false);
		btnBack.setEnabled(true);
		btnSave = buttonPanel.addButton("ProsResSave", false, false);
		btnSave.setEnabled(false);
		btnCancel = buttonPanel.addButton("btnCancel", false, false);
		btnCancel.setEnabled(true);

	}

	public JPanel getRespondentDetailsPanel() {
		if (respondentDetailsPanel == null) {
			respondentDetailsPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			respondentDetailsPanel.setLayout(new GridBagLayout());

			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			lblInitials = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRes.Initials"));
			respondentDetailsPanel.add(lblInitials, gbc);
			gbc.gridy += 2;

			lblTitle = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRes.Title"));
			respondentDetailsPanel.add(lblTitle, gbc);
			gbc.gridy += 2;

			lblName = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRes.Name"));
			respondentDetailsPanel.add(lblName, gbc);
			gbc.gridy += 2;

			lblFirstName = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRes.FirstName"));
			respondentDetailsPanel.add(lblFirstName, gbc);
			gbc.gridy += 2;

			lblOtherNames = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRes.OtherNames"));
			respondentDetailsPanel.add(lblOtherNames, gbc);
			gbc.gridy += 2;

			lblCPSCode = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRes.CPSCode"));
			respondentDetailsPanel.add(lblCPSCode, gbc);
			gbc.gridy += 2;

			lblAddress = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRes.Address"));
			respondentDetailsPanel.add(lblAddress, gbc);
			gbc.gridy += 8;

			lblTown = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRes.Town"));
			respondentDetailsPanel.add(lblTown, gbc);
			gbc.gridy += 2;

			lblCounty = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRes.County"));
			respondentDetailsPanel.add(lblCounty, gbc);
			gbc.gridy += 2;

			lblPostcode = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRes.Postcode"));
			respondentDetailsPanel.add(lblPostcode, gbc);

			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 1;

			respondentDetailsPanel.add(getTxtInitials(), gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(getTxtTitle(), gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(getTxtName(), gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(getTxtFirstName(), gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(getTxtOtherNames(), gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(getTxtCpsCode(), gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(getTxtAddress1(), gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(getTxtAddress2(), gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(getTxtAddress3(), gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(getTxtAddress4(), gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(getTxtTown(), gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(getTxtCounty(), gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(getTxtPostcode(), gbc);
			gbc.gridy++;

			/* Top Of Column */
			gbc.gridx = 1;
			gbc.gridy = 0;

			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;

			respondentDetailsPanel.add(lblIInitials, gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(lblITitle, gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(lblIName, gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(lblIFirstName, gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(lblIOtherNames, gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(lblICPSCode, gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(lblIAddress1, gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(lblIAddress2, gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(lblIAddress3, gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(lblIAddress4, gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(lblITown, gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(lblICounty, gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(lblIPostcode, gbc);

			/* Next Column */
			gbc.gridy = 1;
			gbc.gridx++;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			lblTelephone = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRes.Telephone"));
			respondentDetailsPanel.add(lblTelephone, gbc);
			gbc.gridy += 2;

			lblFax = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRes.Fax"));
			respondentDetailsPanel.add(lblFax, gbc);
			gbc.gridy += 2;

			lblSecureEmail = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRes.SecureEmail"));
			respondentDetailsPanel.add(lblSecureEmail, gbc);
			gbc.gridy += 2;

			lblNonSecure = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRes.NonSecureEmail"));
			respondentDetailsPanel.add(lblNonSecure, gbc);
			gbc.gridy += 2;

			lblDocRef = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.CaseMaintenanceResources, "prosRes.DocExRef"));
			respondentDetailsPanel.add(lblDocRef, gbc);

			/* Top of Column */
			gbc.gridy = 1;
			gbc.gridx++;

			respondentDetailsPanel.add(getTxtTelephone(), gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(getTxtFax(), gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(getTxtSecureEmail(), gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(getTxtNonSecureEmail(), gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(getTxtDocExReference(), gbc);

			/* Top Of Column */
			gbc.gridy = 0;

			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;

			respondentDetailsPanel.add(lblITelephone, gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(lblIFax, gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(lblISecureEmail, gbc);
			gbc.gridy += 2;

			respondentDetailsPanel.add(lblINonSecure, gbc);
			gbc.gridy += 2;

			lblIDocRef = new JLabel(" ");
			respondentDetailsPanel.add(lblIDocRef, gbc);
			gbc.gridy += 2;

		}
		return respondentDetailsPanel;
	}

	public XTextField getTxtInitials() {
		if (lblIInitials == null) {
			lblIInitials = new JLabel(" ");
		}
		if (txtInitials == null) {
			txtInitials = new XTextField(4, "^.{1,4}$", lblIInitials, false);
			txtInitials.setMaxLength(4);
			txtInitials.setGridBagLayout(true);
			txtInitials.setColumns(10);
			txtInitials.setUpperCase(true);
		}

		return txtInitials;
	}

	public XTextField getTxtTitle() {
		if (lblITitle == null) {
			lblITitle = new JLabel(" ");
		}
		if (txtTitle == null) {
			txtTitle = new XTextField(25, "^.{1,25}$", lblITitle, false);
			txtTitle.setGridBagLayout(true);
			txtTitle.setMaxLength(25);
			txtTitle.setColumns(10);
			txtTitle.setUpperCase(true);

		}
		return txtTitle;
	}

	public XTextField getTxtName() {
		if (lblIName == null) {
			lblIName = new JLabel(" ");
		}
		if (txtName == null) {
			txtName = new XTextField(35, "^.{1,35}$", lblIName, true);
			txtName.setGridBagLayout(true);
			txtName.setMaxLength(35);
			txtName.setColumns(10);
			txtName.setUpperCase(true);
			TextValidationController nameTxtValidation = ValidationControllerFactory.createTextRequired(this, txtName,
					lblIName, new TextRegexValidator("^.{1,35}$"));
			validationControllers.add(nameTxtValidation);
		}
		return txtName;
	}

	public XTextField getTxtFirstName() {
		if (lblIFirstName == null) {
			lblIFirstName = new JLabel(" ");
		}
		if (txtFirstName == null) {
			txtFirstName = new XTextField(35, "^.{1,35}$", lblIFirstName, false);
			txtFirstName.setMaxLength(35);
			txtFirstName.setGridBagLayout(true);
			txtFirstName.setColumns(10);
			txtFirstName.setUpperCase(true);
		}
		return txtFirstName;
	}

	public XTextField getTxtOtherNames() {
		if (lblIOtherNames == null) {
			lblIOtherNames = new JLabel(" ");
		}
		if (txtOtherNames == null) {
			txtOtherNames = new XTextField(35, "^.{1,35}$", lblIOtherNames, false);
			txtOtherNames.setMaxLength(35);
			txtOtherNames.setGridBagLayout(true);
			txtOtherNames.setColumns(10);
			txtOtherNames.setUpperCase(true);
		}
		return txtOtherNames;
	}

	public XTextField getTxtCpsCode() {
		if (lblICPSCode == null) {
			lblICPSCode = new JLabel(" ");
		}
		if (txtCpsCode == null) {
			txtCpsCode = new XTextField(4, "^.{1,4}$", lblICPSCode, false);
			txtCpsCode.setMaxLength(4);
			txtCpsCode.setGridBagLayout(true);
			txtCpsCode.setColumns(10);
			txtCpsCode.setUpperCase(true);
		}
		return txtCpsCode;
	}

	public XTextField getTxtAddress1() {
		if (lblIAddress1 == null) {
			lblIAddress1 = new JLabel(" ");
		}
		if (txtAddress1 == null) {
			txtAddress1 = new XTextField(30, "^.{1,30}$", lblIAddress1, true);
			txtAddress1.setGridBagLayout(true);
			txtAddress1.setMaxLength(30);
			txtAddress1.setColumns(10);
			txtAddress1.setUpperCase(true);

			TextValidationController Address1TxtValidation = ValidationControllerFactory.createTextRequired(this,
					txtAddress1, lblIAddress1, new TextRegexValidator("^.{1,35}$"));
			validationControllers.add(Address1TxtValidation);
		}
		return txtAddress1;
	}

	public XTextField getTxtAddress2() {
		if (lblIAddress2 == null) {
			lblIAddress2 = new JLabel(" ");
		}
		if (txtAddress2 == null) {
			txtAddress2 = new XTextField(30, "^.{1,30}$", lblIAddress2, false);
			txtAddress2.setMaxLength(30);
			txtAddress2.setGridBagLayout(true);
			txtAddress2.setColumns(10);
			txtAddress2.setUpperCase(true);
		}
		return txtAddress2;
	}

	public XTextField getTxtAddress3() {
		if (lblIAddress3 == null) {
			lblIAddress3 = new JLabel(" ");
		}
		if (txtAddress3 == null) {
			txtAddress3 = new XTextField(30, "^.{1,30}$", lblIAddress3, false);
			txtAddress3.setMaxLength(30);
			txtAddress3.setGridBagLayout(true);
			txtAddress3.setColumns(10);
			txtAddress3.setUpperCase(true);
		}
		return txtAddress3;
	}

	public XTextField getTxtAddress4() {
		if (lblIAddress4 == null) {
			lblIAddress4 = new JLabel(" ");
		}
		if (txtAddress4 == null) {
			txtAddress4 = new XTextField(30, "^.{1,30}$", lblIAddress4, false);
			txtAddress4.setMaxLength(30);
			txtAddress4.setGridBagLayout(true);
			txtAddress4.setColumns(10);
			txtAddress4.setUpperCase(true);
		}
		return txtAddress4;
	}

	public XTextField getTxtTown() {
		if (lblITown == null) {
			lblITown = new JLabel(" ");
		}
		if (txtTown == null) {
			txtTown = new XTextField(30, "^.{1,30}$", lblITown, true);
			txtTown.setMaxLength(30);
			txtTown.setGridBagLayout(true);
			txtTown.setColumns(10);
			txtTown.setUpperCase(true);

			TextValidationController townTxtValidation = ValidationControllerFactory.createTextRequired(this, txtTown,
					lblITown, new TextRegexValidator("^.{1,35}$"));
			validationControllers.add(townTxtValidation);
		}
		return txtTown;
	}

	public XTextField getTxtCounty() {
		if (lblICounty == null) {
			lblICounty = new JLabel(" ");
		}
		if (txtCounty == null) {
			txtCounty = new XTextField(30, "^.{1,30}$", lblICounty, false);
			txtCounty.setMaxLength(30);
			txtCounty.setGridBagLayout(true);
			txtCounty.setColumns(10);
			txtCounty.setUpperCase(true);
		}
		return txtCounty;
	}

	public XTextField getTxtPostcode() {
		if (lblIPostcode == null) {
			lblIPostcode = new JLabel(" ");
			lblIPostcode.setForeground(Color.RED);
		}

		if (txtPostcode == null) {
			txtPostcode = new XTextField(8, "", lblIPostcode, false, true);
			txtPostcode.setMaxLength(8);
			txtPostcode.setGridBagLayout(true);
			txtPostcode.setColumns(8);
			txtPostcode.setUpperCase(true);
		}
		return txtPostcode;
	}

	public XTextField getTxtTelephone() {
		if (lblITelephone == null) {
			lblITelephone = new JLabel(" ");
		}
		if (txtTelephone == null) {
			txtTelephone = new XTextField(14, "^[0-9 ]{1,14}$", lblITelephone, false);
			txtTelephone.setMaxLength(14);
			txtTelephone.setGridBagLayout(true);
			txtTelephone.setColumns(10);
			txtTelephone.setUpperCase(true);
			TextValidationController telNumberTxtValidation = ValidationControllerFactory.createText(this,
					txtTelephone, lblITelephone, new TextRegexValidator("^[0-9 ]{1,14}$"));
			validationControllers.add(telNumberTxtValidation);
		}
		return txtTelephone;
	}

	public XTextField getTxtFax() {
		if (lblIFax == null) {
			lblIFax = new JLabel(" ");
		}
		if (txtFax == null) {
			txtFax = new XTextField(14, "^[0-9 ]{1,14}$", lblIFax, false);
			txtFax.setMaxLength(14);
			txtFax.setGridBagLayout(true);
			txtFax.setColumns(10);
			txtFax.setUpperCase(true);
			TextValidationController faxNumberTxtValidation = ValidationControllerFactory.createText(this,
					txtFax, lblIFax, new TextRegexValidator("^[0-9 ]{1,14}$"));
			validationControllers.add(faxNumberTxtValidation);
		}
		return txtFax;
	}

	public XTextField getTxtSecureEmail() {
		if (lblISecureEmail == null) {
			lblISecureEmail = new JLabel(" ");
		}
		if (txtSecureEmail == null) {
			txtSecureEmail = new XTextField(255, "([A-Za-z0-9._%+-]){1,}@{1}([A-Za-z0-9._%+-]){1,}[.](CJSM)[.]NET{1,}",
					lblISecureEmail, false);
			txtSecureEmail.setMaxLength(255);
			txtSecureEmail.setGridBagLayout(true);
			txtSecureEmail.setColumns(10);
			txtSecureEmail.setUpperCase(true);
			TextValidationController nameSecureEmail = ValidationControllerFactory.createTextRegex(this, txtSecureEmail,
					lblISecureEmail, "([A-Za-z0-9._%+-]){1,}@{1}([A-Za-z0-9._%+-]){1,}[.](CJSM)[.]NET{1,}");
			validationControllers.add(nameSecureEmail);

		}
		return txtSecureEmail;
	}

	public XTextField getTxtNonSecureEmail() {
		if (lblINonSecure == null) {
			lblINonSecure = new JLabel(" ");
		}
		if (txtNonSecureEmail == null) {
			txtNonSecureEmail = new XTextField(255, "^.{1,255}@.*$", lblINonSecure, false);
			txtNonSecureEmail.setMaxLength(255);
			txtNonSecureEmail.setGridBagLayout(true);
			txtNonSecureEmail.setColumns(10);
			txtNonSecureEmail.setUpperCase(true);
			TextValidationController nameNonSecureEmail = ValidationControllerFactory.createTextRegex(this,
					txtNonSecureEmail, lblINonSecure, "^.{1,255}@.*$");
			validationControllers.add(nameNonSecureEmail);
		}
		return txtNonSecureEmail;
	}

	public XTextField getTxtDocExReference() {
		if (lblIDocRef == null) {
			lblIDocRef = new JLabel(" ");
		}
		if (txtDocExReference == null) {
			txtDocExReference = new XTextField(35, "^.{1,35}$", lblIDocRef, false);
			txtDocExReference.setMaxLength(35);
			txtDocExReference.setGridBagLayout(true);
			txtDocExReference.setColumns(10);
			txtDocExReference.setUpperCase(true);
		}
		return txtDocExReference;
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

	public void configureTabOrderForProsecutorRespondentDetails() {
		// Set up tab order including the cancel button from Prosecutor
		// Respondent Search
		thisClass.setFocusTraversalPolicyProvider(true);
		thisClass.setFocusTraversalPolicy(new FocusTraversalOnArray(
				new Component[] { txtInitials, txtTitle, txtName, txtFirstName, txtOtherNames, txtCpsCode, txtAddress1,
						txtAddress2, txtAddress3, txtAddress4, txtTown, txtCounty, txtPostcode, txtTelephone, txtFax,
						txtSecureEmail, txtNonSecureEmail, txtDocExReference, btnSave, btnBack, btnCancel }));
	}

	// Prosecutor/Respondent
	public void configureTabOrderForCaseMaintenance() {
		// Set up tab order
		thisClass.setFocusTraversalPolicyProvider(true);
		thisClass.setFocusTraversalPolicy(new FocusTraversalOnArray(
				new Component[] { txtInitials, txtTitle, txtName, txtFirstName, txtOtherNames, txtCpsCode, txtAddress1,
						txtAddress2, txtAddress3, txtAddress4, txtTown, txtCounty, txtPostcode, txtTelephone, txtFax,
						txtSecureEmail, txtNonSecureEmail, txtDocExReference, btnSave, btnBack }));
	}

	public void saveProsecutor(RefProsecutorAgencyComplexValue refProsecutor) {
		try {
			AddressBasicValue address = populateAddressBasicValue();
			refProsecutor.setCourtId(XhibitSingleton.getInstance().getCourtId());

			if (!(getTelephone().equals(""))) {
				refProsecutor.setTelephoneNumber(getTelephone());
			}
			if(!(getFax().equals(""))) {
				refProsecutor.setFaxNumber(getFax());
			}
			if (!(getSecureEmail().equals(""))) {
				refProsecutor.setSecureEmailAddress(getSecureEmail());
			}
			if (!(getNonSecureEmail().equals(""))) {
				refProsecutor.setNonsecureEmailAddress(getNonSecureEmail());
			}
			
			refProsecutor = bizRefDelegate.createProsecutorAgency(refProsecutor, address, userDisplayName);
			refProsecutor.setRefProsecutorAgencyId(refProsecutor.getId());

			JOptionPane.showMessageDialog(this, "Prosecutor/Respondent successfully saved", "Prosecutor/Respondent",
					JOptionPane.INFORMATION_MESSAGE);
			ProsecutorRespondent newProsecutorRespondent = new ProsecutorRespondent(refProsecutor, true);
			((ProsecutorRespondentSearchPanel) model.getCallingClass()).addProsecutorCallSave(newProsecutorRespondent);
			
			parentDialog.clearStatusBarScreenCode();
			parentDialog.dispose();
		} catch (Exception er) {
			XHIBITConstant.handleError(er);
		}
	}

	public void updateProsecutor(RefProsecutorAgencyComplexValue refProsecutor) {

		try {

			bizRefDelegate.updateRefProsecutorAgency(refProsecutor,userDisplayName);

			JOptionPane.showMessageDialog(this, "Prosecutor/Respondent successfully updated", "Prosecutor/Respondent",
						JOptionPane.INFORMATION_MESSAGE);
			/* ctx-2302 */
			if (model.getCallingClass() instanceof ProsecutorRespondentSearchPanel) {
				((ProsecutorRespondentSearchPanel) model.getCallingClass())
						.updateProsecutorRespondentCallback(model.getRefProsecutorAgencyComplexValue());
			}
			
			parentDialog.clearStatusBarScreenCode();
			parentDialog.dispose();

		} catch (Exception e) {
			XHIBITConstant.handleError(e);
		}

	}


	public AddressBasicValue populateAddressBasicValue() {
		AddressBasicValue adVal = new AddressBasicValue();
		adVal.setAddress1(getAddress1());
		adVal.setAddress2(getAddress2());
		adVal.setAddress3(getAddress3());
		adVal.setAddress4(getAddress4());
		adVal.setCounty(getCounty());
		adVal.setTown(getTown());
		adVal.setPostcode(getPostcode());
		return adVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepInitialise()
	 */
	@Override
	public void stepInitialise() throws CSRecoverableException {
		moveModelToScreen();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepActivate()
	 */
	@Override
	public void stepActivate() throws CSRecoverableException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepUpdateViewState()
	 */
	@Override
	public void stepUpdateViewState() throws CSRecoverableException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepValidate()
	 */
	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		// Throw exception if validation failures to prevent saving
		if (!ValidationControllerFactory.validateComponents(validationControllers)) {
			throw new CSValidationException("validation.general", "Field validation Failed");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepDeactivate()
	 */
	@Override
	public void stepDeactivate() throws CSRecoverableException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.gov.courtservice.xhibit.client.util.XPanel#stepDeinitialise(boolean)
	 */
	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (btnSave.equals(getDeinitialiseSource())) {
			if (!ValidationControllerFactory.validateComponents(validationControllers)) {
				throw new CSValidationException("validation.general", "Field validation failed");
			}
			if (!(lblIPostcode.getText().equals(" "))) {
				throw new CSValidationException("validation.general", "Field validation failed");
			}
			moveScreenToModel();
			if (model.getRefProsecutorAgencyComplexValue().getRefProsecutorAgencyId() == null
					|| model.getRefProsecutorAgencyComplexValue().getRefProsecutorAgencyId().equals(0)) {

				saveProsecutor(model.getRefProsecutorAgencyComplexValue());
			} else {
				updateProsecutor(model.getRefProsecutorAgencyComplexValue());
				
			}
		}
		if (btnBack.equals(getDeinitialiseSource())) {
			if (changesMade) {
				int option = JOptionPane.showConfirmDialog(this,
						"There are un-saved changes on the screen. All the changes will be lost.  Are you sure?",
						"Unsaved changes", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					parentDialog.clearStatusBarScreenCode();
					parentDialog.dispose();
				}
			} else {
				parentDialog.clearStatusBarScreenCode();
				parentDialog.dispose();
			}

		}
		if (btnCancel.equals(getDeinitialiseSource())) {
			if (changesMade) {
				int option = JOptionPane.showConfirmDialog(this,
						"There are un-saved changes on the screen. All the changes will be lost.  Are you sure?",
						"Unsaved changes", JOptionPane.YES_NO_OPTION);
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

	/*
	 * Enable or disable the OK/Save?update buttons based on whether all the
	 * field validations have passed or failed
	 */
	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
	}

	public String getInitials() {
		return txtInitials.getText();
	}

	public void setInitials(String initials) {
		txtInitials.setText(initials);
	}

	public void setTitle(String title) {
		txtTitle.setText(title);
	}

	public String getTitle() {
		return txtTitle.getText();
	}

	public void setName(String name) {
		txtName.setText(name);
	}

	public String getName() {
		return txtName.getText();
	}

	public void setFirstName(String firstName) {
		txtFirstName.setText(firstName);
	}

	public String getFirstName() {
		return txtFirstName.getText();
	}

	public void setOtherNames(String otherNames) {
		if (otherNames != null) {
			txtOtherNames.setText(otherNames);
		}
	}

	public String getOtherName() {
		return txtOtherNames.getText();
	}

	public void setCpsCode(String cpsCode) {
		if (cpsCode != null) {
			txtCpsCode.setText(cpsCode);
		}
	}

	public String getCpsCode() {
		return txtCpsCode.getText();
	}

	public void setAddress1(String address1) {
		if (txtAddress1 != null) {
			txtAddress1.setText(address1);
		}
	}

	public String getAddress1() {
		return txtAddress1.getText();
	}

	public void setAddress2(String address2) {
		if (txtAddress2 != null) {
			txtAddress2.setText(address2);
		}
	}

	public String getAddress2() {
		return txtAddress2.getText();
	}

	public void setAddress3(String address3) {
		if (txtAddress3 != null) {
			txtAddress3.setText(address3);
		}
	}

	public String getAddress3() {
		return txtAddress3.getText();
	}

	public void setAddress4(String address4) {
		if (txtAddress4 != null) {
			txtAddress4.setText(address4);
		}
	}

	public String getAddress4() {
		return txtAddress4.getText();
	}

	public void setTown(String town) {
		if (txtTown != null) {
			txtTown.setText(town);
		}
	}

	public String getTown() {
		return txtTown.getText();
	}

	public void setCounty(String county) {
		if (txtCounty != null) {
			txtCounty.setText(county);
		}
	}

	public String getCounty() {
		return txtCounty.getText();
	}

	public void setPostcode(String postcode) {
		if (txtPostcode != null) {
			txtPostcode.setText(postcode);
		}
	}

	public String getPostcode() {
		return txtPostcode.getText();

	}

	public void setTelephone(String telephone) {
		if (txtTelephone != null) {
			txtTelephone.setText(telephone);
		}
	}

	public String getTelephone() {
		return txtTelephone.getText();
	}

	public void setFax(String fax) {
		if (txtFax != null) {
			txtFax.setText(fax);
		}
	}

	public String getFax() {
		return txtFax.getText();
	}

	public void setSecureEmail(String secureEmail) {
		if (txtSecureEmail != null) {
			txtSecureEmail.setText(secureEmail);
		}
	}

	public String getSecureEmail() {
		return txtSecureEmail.getText();
	}

	public void setNonSecureEmail(String nonSecureEmail) {
		if (txtNonSecureEmail != null) {
			txtNonSecureEmail.setText(nonSecureEmail);
		}
	}

	public String getNonSecureEmail() {
		return txtNonSecureEmail.getText();
	}

	public void setDocExReference(String docExReference) {
		if (txtDocExReference != null) {
			txtDocExReference.setText(docExReference);
		}
	}

	public String getDocExReference() {
		return txtDocExReference.getText();
	}

	public void setRefProsecutorAgencyComplexValue(RefProsecutorAgencyComplexValue refProsecutor) {
		this.refProsecutor = refProsecutor;
	}

	public RefProsecutorAgencyComplexValue getRefProsecutorAgencyComplexValue() {
		return refProsecutor;
	}

	/**
	 * Set the data entered on screen into the model.
	 */
	private void moveScreenToModel() {
		if (model.getRefProsecutorAgencyComplexValue() == null) {
			model.setRefProsecutorAgencyComplexValue(new RefProsecutorAgencyComplexValue());
		}
		RefProsecutorAgencyComplexValue ref = model.getRefProsecutorAgencyComplexValue();
		AddressBasicValue abv = new AddressBasicValue();
		if(ref!=null&&ref.getAddress()!=null) {
			abv=ref.getAddress();
		}
		abv.setAddress1(getAddress1());
		abv.setAddress2(getAddress2());
		abv.setAddress3(getAddress3());
		abv.setAddress4(getAddress4());
		abv.setTown(getTown());
		abv.setCounty(getCounty());
		abv.setPostcode(getPostcode());
		
		ref.setAddress(abv);
		ref.setCourtId(XhibitSingleton.getInstance().getCourtId());
		ref.setCpsCode(getCpsCode());
		ref.setDxRef(getDocExReference());
		ref.setFaxNumber(getFax());
		ref.setInitials(getInitials());
		ref.setFullName(getFullName());
		ref.setNonsecureEmailAddress(getNonSecureEmail());
		ref.setProsecutorName1(getFirstName());
		ref.setProsecutorName2(getOtherName());
		ref.setProsecutorName3(getName());
		ref.setSecureEmailAddress(getSecureEmail());
		ref.setTelephoneNumber(getTelephone());
		ref.setTitle(getTitle());
		model.setRefProsecutorAgencyComplexValue(ref);
	}

	/**
	 * If the model is not null then populate the fields with the values.
	 * Setting the caret to 0 as well because if it's over 20columns it'll show
	 * the first portion of the field not the end.
	 */
	private void moveModelToScreen() {
		if (model != null) {
			if (model.getCallingClass() != null) {

				if (model.getRefProsecutorAgencyComplexValue() != null) {
					if (model.getRefProsecutorAgencyComplexValue().getRefProsecutorAgencyId() != null) {
						RefProsecutorAgencyComplexValue refProsecutor = bizRefDelegate.findByRefProsecutorAgencyId(
								model.getRefProsecutorAgencyComplexValue().getRefProsecutorAgencyId());
						setInitials(refProsecutor.getInitials());
						setTitle(refProsecutor.getTitle());
						setName(refProsecutor.getProsecutorName3());
						setFirstName(refProsecutor.getProsecutorName1());
						setOtherNames(refProsecutor.getProsecutorName2());
						setCpsCode(refProsecutor.getCpsCode());

						setAddress1(refProsecutor.getAddress().getAddress1());
						if (refProsecutor.getAddress().getAddress1() != null) {
							parentDialog.setTitle("Amend Prosecutor/Respondent");
						}
						setAddress2(refProsecutor.getAddress().getAddress2());
						setAddress3(refProsecutor.getAddress().getAddress3());
						setAddress4(refProsecutor.getAddress().getAddress4());
						setTown(refProsecutor.getAddress().getTown());
						setCounty(refProsecutor.getAddress().getCounty());
						setPostcode(refProsecutor.getAddress().getPostcode());

						setTelephone(refProsecutor.getTelephoneNumber());
						setFax(refProsecutor.getFaxNumber());
						setSecureEmail(refProsecutor.getSecureEmailAddress());
						setNonSecureEmail(refProsecutor.getNonsecureEmailAddress());
						setDocExReference(refProsecutor.getDxRef());
						model.setRefProsecutorAgencyComplexValue(refProsecutor);
					}
				}
				if (model.getCallingClass() instanceof ProsecutorRespondentSearchPanel) {
					configureTabOrderForProsecutorRespondentDetails();
					updateProseutorListener();
				} else {
					configureTabOrderForCaseMaintenance();
					btnCancel.setVisible(false);
				}
			}
			changesMade = false;
		}

	}

	public void setFullName() {
		// --- Make full name string ---
		fullName = "";
		if (getTitle() != null) {
			fullName = getTitle();
		}
		if (getInitials() != null) {
			if (fullName.length() > 0) {
				fullName = fullName + " ";
			}
			fullName += getInitials();
		}
		if (getName() != null) {
			if (fullName.length() > 0) {
				fullName = fullName + " ";
			}
			fullName += getName();
		}
		if (getFirstName() != null) {
			if (fullName.length() > 0) {
				fullName = fullName + " ";
			}
			fullName += getFirstName();
		}
		if (getOtherName() != null) {
			if (fullName.length() > 0) {
				fullName = fullName + " ";
			}
			fullName += getOtherName();
		}
	}

	public String getFullName() {
		return fullName;
	}

	public void updateProseutorListener() {
		for (Component c : respondentDetailsPanel.getComponents()) {
			if (c instanceof XTextField) {
				((XTextField) c).getDocument().addDocumentListener(new docListener());
			}
		}
	}

	// Used to detect whether text fields have been entered/changed -
	// C.Kudzin
	public class docListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			changesMade = true;
			btnSave.setEnabled(true);
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			changesMade = true;
			btnSave.setEnabled(true);
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			changesMade = true;
			btnSave.setEnabled(true);
		}
	}

}
