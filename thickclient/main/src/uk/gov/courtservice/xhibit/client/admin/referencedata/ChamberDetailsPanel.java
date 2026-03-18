package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
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

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.StringUtil;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefChamberComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractTextValidator;
import uk.gov.courtservice.xhibit.client.util.validation.TextRegexValidator;
import uk.gov.courtservice.xhibit.client.util.validation.TextValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class ChamberDetailsPanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;
	private ChamberDetailsModel model;
	private ChamberDetailsDialog parentDialog;

	/**
	 * Fields on Chamber Details panel.
	 */
	private JLabel lblChamberRefNo = null;
	private JLabel lblFirmName = null;
	private JLabel lblAddress = null;
	private JLabel lblTown = null;
	private JLabel lblCounty = null;
	private JLabel lblPostcode = null;
	private JLabel lblClerkName = null;
	private JLabel lblTelephoneNumber = null;
	private JLabel lblFaxNumber = null;
	private JLabel lblDocExReference = null;
	private JLabel lblEmail = null;
	private JLabel lblSecureEmail = null;
	private JLabel lblDeleted = null;
	private XTextField txtChamberRefNo = null;
	private XTextField txtFirmName = null;
	private XTextField txtAddress1 = null;
	private XTextField txtAddress2 = null;
	private XTextField txtAddress3 = null;
	private XTextField txtAddress4 = null;
	private XTextField txtTown = null;
	private XTextField txtCounty = null;
	private XTextField txtPostcode = null;
	private XTextField txtClerkName = null;
	private XTextField txtTelephoneNumber = null;
	private XTextField txtFaxNumber = null;
	private XTextField txtDocExReference = null;
	private XTextField txtEmail = null;
	private XTextField txtSecureEmail = null;
	private JCheckBox deletedCheckbox = null;
	private JLabel lblManFirmName = null;
	private JLabel lblManAddress1 = null;
	private JLabel lblAddress2InvalidEntry = null;
	private JLabel lblAddress3InvalidEntry = null;
	private JLabel lblAddress4InvalidEntry = null;
	private JLabel lblManTown = null;
	private JLabel lblCountyInvalidEntry = null;
	private JLabel lblPostcodeInvalidEntry = null;
	private JLabel lblClerkNameInvalidEntry = null;
	private JLabel lblTelephoneNumberInvalidEntry = null;
	private JLabel lblFaxNumberInvalidEntry = null;
	private JLabel lblDocExReferenceInvalidEntry = null;
	private JLabel lblEmailInvalidEntry = null;
	private JLabel lblSecureEmailInvalidEntry = null;
	private JLabel lblDeletedInvalidEntry = null;

	/**
	 * Fields on Button panel.
	 */
	private JButton btnAddCounsel = null;
	private JButton btnSave = null;
	private JButton btnCancel = null;
	private JButton btnDelete = null;

	/**
	 * JPanels.
	 */
	private JPanel mainPanel = null;
	private JPanel chamberDetailsPanel = null;

	// array of all the mandatory fields
	Vector<Object> mandatoryFields = new Vector<Object>();

	private Boolean changesMade = false;
	private Boolean isAmend = false;
	private Boolean isAddNew = false;
	private Boolean counselAdded = false;

	/**
	 * Validators.
	 */
	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();

	private static final Logger log = CSServices.getLogger(ChamberDetailsPanel.class);

	public ChamberDetailsPanel(ChamberDetailsDialog parentDialog, ChamberDetailsModel model)
			throws CSRecoverableException {
		this.model = model;
		this.parentDialog = parentDialog;
		stepInitialise();
		jbInit();
	}
	
	public void setCounselAdded(boolean counselAdded) {
		this.counselAdded = counselAdded;
	}
	
	private Boolean getCounselAdded() {
		return counselAdded;
	}

	/**
	 * Initialises the look and feel of the panel.
	 */
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(750, 400));
		GridBagConstraints gbc = getGridBagLayout();

		mainPanel = getMainPanel();

		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weighty = 0.05;
		gbc.weightx = 0.95;
		gbc.fill = GridBagConstraints.BOTH;
		mainPanel.add(getChamberDetailsPanel(), gbc);

		CustomButtonPanel buttonPanel = (CustomButtonPanel) this.parentDialog.getButtonPanel();

		btnAddCounsel = buttonPanel.addButton("ChamberDetailsAddCounsel", false, false);
		btnDelete = buttonPanel.addButton("ChamberDetailsDelete", false, false);
		btnSave = buttonPanel.addButton("ChamberDetailsSave", false, false);
		btnCancel = buttonPanel.addButton("ChamberDetailsCancel", true, false);

		configureTabOrder();
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
			mainPanel.setPreferredSize(new Dimension(700, 375));
			this.add(scrollPane, gbc);
		}

		return mainPanel;
	}

	public JPanel getChamberDetailsPanel() {
		if (chamberDetailsPanel == null) {
			chamberDetailsPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			chamberDetailsPanel.setLayout(new GridBagLayout());

			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			lblChamberRefNo = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"ChamberDetails.chamberRefNoLabel"));
			chamberDetailsPanel.add(lblChamberRefNo, gbc);
			gbc.gridy += 2;

			lblFirmName = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"ChamberDetails.firmNameLabel"));
			chamberDetailsPanel.add(lblFirmName, gbc);
			gbc.gridy += 2;

			lblAddress = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "ChamberDetails.addressLabel"));
			chamberDetailsPanel.add(lblAddress, gbc);
			// No need for address 2, 3 and 4 label
			gbc.gridy += 8;

			lblTown = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "ChamberDetails.townLabel"));
			chamberDetailsPanel.add(lblTown, gbc);
			gbc.gridy += 2;

			lblCounty = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "ChamberDetails.countyLabel"));
			chamberDetailsPanel.add(lblCounty, gbc);
			gbc.gridy += 2;

			lblPostcode = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"ChamberDetails.postcodeLabel"));
			chamberDetailsPanel.add(lblPostcode, gbc);

			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 1;

			chamberDetailsPanel.add(getChamberRefNo(), gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(getFirmName(), gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(getAddress1(), gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(getAddress2(), gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(getAddress3(), gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(getAddress4(), gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(getTown(), gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(getCounty(), gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(getPostcode(), gbc);

			/* Top Of Column */
			gbc.gridx = 1;
			gbc.gridy = 2;

			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;

			chamberDetailsPanel.add(lblManFirmName, gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(lblManAddress1, gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(lblAddress2InvalidEntry, gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(lblAddress3InvalidEntry, gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(lblAddress4InvalidEntry, gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(lblManTown, gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(lblCountyInvalidEntry, gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(lblPostcodeInvalidEntry, gbc);

			/* Next Column */
			gbc.gridy = 1;
			gbc.gridx++;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			lblClerkName = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"ChamberDetails.clerkNameLabel"));
			chamberDetailsPanel.add(lblClerkName, gbc);
			gbc.gridy += 2;

			lblTelephoneNumber = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"ChamberDetails.telephoneNumberLabel"));
			chamberDetailsPanel.add(lblTelephoneNumber, gbc);
			gbc.gridy += 2;

			lblFaxNumber = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"ChamberDetails.faxNumberLabel"));
			chamberDetailsPanel.add(lblFaxNumber, gbc);
			gbc.gridy += 2;

			lblDocExReference = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"ChamberDetails.docExReferenceLabel"));
			chamberDetailsPanel.add(lblDocExReference, gbc);
			gbc.gridy += 2;

			lblEmail = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "ChamberDetails.emailLabel"));
			chamberDetailsPanel.add(lblEmail, gbc);
			gbc.gridy += 2;

			lblSecureEmail = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"ChamberDetails.secureEmailLabel"));
			chamberDetailsPanel.add(lblSecureEmail, gbc);
			gbc.gridy += 2;

			lblDeleted = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"ChamberDetails.deletedChamberLabel"));
			chamberDetailsPanel.add(lblDeleted, gbc);

			/* Top of Column */
			gbc.gridy = 1;
			gbc.gridx++;

			chamberDetailsPanel.add(getClerkName(), gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(getTelephoneNumber(), gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(getFaxNumber(), gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(getDocExReference(), gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(getEmail(), gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(getSecureEmail(), gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(getDeletedCheckBox(), gbc);

			/* Top Of Column */
			gbc.gridy = 0;

			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;

			chamberDetailsPanel.add(lblClerkNameInvalidEntry, gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(lblTelephoneNumberInvalidEntry, gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(lblFaxNumberInvalidEntry, gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(lblDocExReferenceInvalidEntry, gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(lblEmailInvalidEntry, gbc);
			gbc.gridy += 2;

			chamberDetailsPanel.add(lblSecureEmailInvalidEntry, gbc);
			gbc.gridy += 2;

			// No label needed but used as a placeholder to keep layout
			// consistent
			chamberDetailsPanel.add(lblDeletedInvalidEntry, gbc);
			gbc.gridy += 2;
		}
		return chamberDetailsPanel;
	}

	public XTextField getChamberRefNo() {
		if (txtChamberRefNo == null) {
			txtChamberRefNo = new XTextField();
			txtChamberRefNo.setColumns(10);
			txtChamberRefNo.setUpperCase(true);
			txtChamberRefNo.setMinimumSize(txtChamberRefNo.getPreferredSize());
			txtChamberRefNo.setEnabled(false);
		}
		return txtChamberRefNo;
	}

	public XTextField getFirmName() {
		if (lblManFirmName == null) {
			lblManFirmName = new JLabel(" ");
		}
		if (txtFirmName == null) {
			txtFirmName = new XTextField();
			txtFirmName.setMaxLength(35);
			txtFirmName.setColumns(10);
			txtFirmName.setUpperCase(true);
			txtFirmName.setMinimumSize(txtFirmName.getPreferredSize());
			TextValidationController firmNameTxtValidation = ValidationControllerFactory.createTextRequired(this,
					txtFirmName, lblManFirmName, new AbstractTextValidator() {
						@Override
						public void validate(JTextComponent target, List<String> errors) {
							char[] charArr =target.getText().toCharArray();
							int len=0;
							for(int i=0;i<charArr.length;i++){
								len=len+StringUtil.getLengthOfChar(charArr[i]);
								if(len>35) {
									errors.add("Invalid entry");
									break;
								}
							}
						}
					});
			validationControllers.add(firmNameTxtValidation);
			mandatoryFields.add(txtFirmName);
		}
		return txtFirmName;
	}

	public XTextField getAddress1() {
		if (lblManAddress1 == null) {
			lblManAddress1 = new JLabel(" ");
		}
		if (txtAddress1 == null) {
			txtAddress1 = new XTextField();
			txtAddress1.setMaxLength(30);
			txtAddress1.setColumns(10);
			txtAddress1.setUpperCase(true);
			txtAddress1.setMinimumSize(txtAddress1.getPreferredSize());
			TextValidationController address1TxtValidation = ValidationControllerFactory.createTextRequired(this,
					txtAddress1, lblManAddress1, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(address1TxtValidation);
			mandatoryFields.add(txtAddress1);
		}
		return txtAddress1;
	}

	public XTextField getAddress2() {
		if (lblAddress2InvalidEntry == null) {
			lblAddress2InvalidEntry = new JLabel(" ");
		}
		if (txtAddress2 == null) {
			txtAddress2 = new XTextField();
			txtAddress2.setMaxLength(30);
			txtAddress2.setColumns(10);
			txtAddress2.setUpperCase(true);
			txtAddress2.setMinimumSize(txtAddress2.getPreferredSize());
			TextValidationController address2TxtValidation = ValidationControllerFactory.createText(this, txtAddress2,
					lblAddress2InvalidEntry, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(address2TxtValidation);
		}
		return txtAddress2;
	}

	public XTextField getAddress3() {
		if (lblAddress3InvalidEntry == null) {
			lblAddress3InvalidEntry = new JLabel(" ");
		}
		if (txtAddress3 == null) {
			txtAddress3 = new XTextField();
			txtAddress3.setMaxLength(30);
			txtAddress3.setColumns(10);
			txtAddress3.setUpperCase(true);
			txtAddress3.setMinimumSize(txtAddress3.getPreferredSize());
			TextValidationController address3TxtValidation = ValidationControllerFactory.createText(this, txtAddress3,
					lblAddress3InvalidEntry, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(address3TxtValidation);
		}
		return txtAddress3;
	}

	public XTextField getAddress4() {
		if (lblAddress4InvalidEntry == null) {
			lblAddress4InvalidEntry = new JLabel(" ");
		}
		if (txtAddress4 == null) {
			txtAddress4 = new XTextField();
			txtAddress4.setMaxLength(30);
			txtAddress4.setColumns(10);
			txtAddress4.setUpperCase(true);
			txtAddress4.setMinimumSize(txtAddress4.getPreferredSize());
			TextValidationController address4TxtValidation = ValidationControllerFactory.createText(this, txtAddress4,
					lblAddress4InvalidEntry, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(address4TxtValidation);
		}
		return txtAddress4;
	}

	public XTextField getTown() {
		if (lblManTown == null) {
			lblManTown = new JLabel(" ");
		}
		if (txtTown == null) {
			txtTown = new XTextField();
			txtTown.setMaxLength(30);
			txtTown.setColumns(10);
			txtTown.setUpperCase(true);
			txtTown.setMinimumSize(txtTown.getPreferredSize());
			TextValidationController townTxtValidation = ValidationControllerFactory.createTextRequired(this, txtTown,
					lblManTown, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(townTxtValidation);
			mandatoryFields.add(txtTown);
		}
		return txtTown;
	}

	public XTextField getCounty() {
		if (lblCountyInvalidEntry == null) {
			lblCountyInvalidEntry = new JLabel(" ");
		}
		if (txtCounty == null) {
			txtCounty = new XTextField();
			txtCounty.setMaxLength(30);
			txtCounty.setColumns(10);
			txtCounty.setUpperCase(true);
			txtCounty.setMinimumSize(txtCounty.getPreferredSize());
			TextValidationController countyTxtValidation = ValidationControllerFactory.createText(this, txtCounty,
					lblCountyInvalidEntry, new TextRegexValidator("^.{1,30}$"));
			validationControllers.add(countyTxtValidation);
		}
		return txtCounty;
	}

	public XTextField getPostcode() {
		if (lblPostcodeInvalidEntry == null) {
			lblPostcodeInvalidEntry = new JLabel(" ");
			lblPostcodeInvalidEntry.setForeground(Color.RED);
		}
		if (txtPostcode == null) {
			txtPostcode = new XTextField(8, "", lblPostcodeInvalidEntry, false, true);
			txtPostcode.setGridBagLayout(true);
			txtPostcode.setMaxLength(8);
			txtPostcode.setColumns(10);
			txtPostcode.setUpperCase(true);
			txtPostcode.setMinimumSize(txtPostcode.getPreferredSize());
			txtPostcode.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void focusLost(FocusEvent e) {
					String text = txtPostcode.getText();
					txtPostcode.setText("");
					txtPostcode.setForeground(Color.RED);
					if (lblPostcodeInvalidEntry.getText().trim().length() == 0) {
						txtPostcode.setForeground(Color.BLACK);
					} 
					txtPostcode.setText(text);
				}
			});
		}
		return txtPostcode;
	}

	public XTextField getClerkName() {
		if (lblClerkNameInvalidEntry == null) {
			lblClerkNameInvalidEntry = new JLabel(" ");
		}
		if (txtClerkName == null) {
			txtClerkName = new XTextField();
			txtClerkName.setMaxLength(35);
			txtClerkName.setColumns(10);
			txtClerkName.setUpperCase(true);
			txtClerkName.setMinimumSize(txtClerkName.getPreferredSize());
			TextValidationController clerkNameTxtValidation = ValidationControllerFactory.createText(this, txtClerkName,
					lblClerkNameInvalidEntry, new TextRegexValidator("^.{1,35}$"));
			validationControllers.add(clerkNameTxtValidation);
		}
		return txtClerkName;
	}

	public XTextField getTelephoneNumber() {
		if (lblTelephoneNumberInvalidEntry == null) {
			lblTelephoneNumberInvalidEntry = new JLabel(" ");
		}
		if (txtTelephoneNumber == null) {
			txtTelephoneNumber = new XTextField();
			txtTelephoneNumber.setMaxLength(14);
			txtTelephoneNumber.setColumns(10);
			txtTelephoneNumber.setUpperCase(true);
			txtTelephoneNumber.setMinimumSize(txtTelephoneNumber.getPreferredSize());
			TextValidationController telephoneNumberTxtValidation = ValidationControllerFactory.createText(this,
					txtTelephoneNumber, lblTelephoneNumberInvalidEntry, new TextRegexValidator("^[0-9 ]{1,14}$"));
			validationControllers.add(telephoneNumberTxtValidation);
		}
		return txtTelephoneNumber;
	}

	public XTextField getFaxNumber() {
		if (lblFaxNumberInvalidEntry == null) {
			lblFaxNumberInvalidEntry = new JLabel(" ");
		}
		if (txtFaxNumber == null) {
			txtFaxNumber = new XTextField();
			txtFaxNumber.setMaxLength(14);
			txtFaxNumber.setColumns(10);
			txtFaxNumber.setUpperCase(true);
			txtFaxNumber.setMinimumSize(txtFaxNumber.getPreferredSize());
			TextValidationController faxNumberTxtValidation = ValidationControllerFactory.createText(this, txtFaxNumber,
					lblFaxNumberInvalidEntry, new TextRegexValidator("^[0-9 ]{1,14}$"));
			validationControllers.add(faxNumberTxtValidation);
		}
		return txtFaxNumber;
	}

	public XTextField getDocExReference() {
		if (lblDocExReferenceInvalidEntry == null) {
			lblDocExReferenceInvalidEntry = new JLabel(" ");
		}
		if (txtDocExReference == null) {
			txtDocExReference = new XTextField();
			txtDocExReference.setMaxLength(35);
			txtDocExReference.setColumns(10);
			txtDocExReference.setUpperCase(true);
			txtDocExReference.setMinimumSize(txtDocExReference.getPreferredSize());
			TextValidationController docExReferenceTxtValidation = ValidationControllerFactory.createText(this,
					txtDocExReference, lblDocExReferenceInvalidEntry, new TextRegexValidator("^.{1,35}$"));
			validationControllers.add(docExReferenceTxtValidation);
		}
		return txtDocExReference;
	}

	public XTextField getEmail() {
		if (lblEmailInvalidEntry == null) {
			lblEmailInvalidEntry = new JLabel(" ");
		}
		if (txtEmail == null) {
			txtEmail = new XTextField();
			txtEmail.setMaxLength(255);
			txtEmail.setColumns(10);
			txtEmail.setUpperCase(true);
			txtEmail.setMinimumSize(txtEmail.getPreferredSize());
			TextValidationController emailTxtValidation = ValidationControllerFactory.createText(this, txtEmail,
					lblEmailInvalidEntry, new TextRegexValidator("^.{1,255}@.*$"));
			validationControllers.add(emailTxtValidation);
		}
		return txtEmail;
	}

	public XTextField getSecureEmail() {
		if (lblSecureEmailInvalidEntry == null) {
			lblSecureEmailInvalidEntry = new JLabel(" ");
		}
		if (txtSecureEmail == null) {
			txtSecureEmail = new XTextField();
			txtSecureEmail.setMaxLength(255);
			txtSecureEmail.setColumns(10);
			txtSecureEmail.setUpperCase(true);
			txtSecureEmail.setMinimumSize(txtSecureEmail.getPreferredSize());
			TextValidationController secureEmailTxtValidation = ValidationControllerFactory.createText(this,
					txtSecureEmail, lblSecureEmailInvalidEntry,
					new TextRegexValidator("([A-Za-z0-9._%+-]){1,}@{1}([A-Za-z0-9._%+-]){1,}[.](CJSM)[.]NET{1,}"));
			validationControllers.add(secureEmailTxtValidation);
		}
		return txtSecureEmail;
	}

	public JCheckBox getDeletedCheckBox() {
		if (lblDeletedInvalidEntry == null) {
			lblDeletedInvalidEntry = new JLabel(" ");
		}
		if (deletedCheckbox == null) {
			deletedCheckbox = new JCheckBox();
			deletedCheckbox.setEnabled(false);
		}
		return deletedCheckbox;
	}

	public void configureTabOrder() {
		setFocusTraversalPolicyProvider(true);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { txtFirmName, txtAddress1, txtAddress2,
				txtAddress3, txtAddress4, txtTown, txtCounty, txtPostcode, txtClerkName, txtTelephoneNumber,
				txtFaxNumber, txtDocExReference, txtEmail, txtSecureEmail, btnAddCounsel, btnDelete, btnSave, btnCancel }));
	}

	public void updateChamber(RefChamberComplexValue refChamber) {
		BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
		String userName = XhibitSingleton.getInstance().getUserSession()
				.getSessionProperty(UserTerminalProperties.DISPLAY_NAME);

		try {
			bizRefDelegate.updateChamberDetails(refChamber, userName);
			showUpdateSuccessDialog();
		} catch (Exception e) {
			XHIBITConstant.handleError(e);
		}
	}

	public void showUpdateSuccessDialog() {
		XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
						"ChamberDetails.updateSuccessTitle"),
				true, XMessageBox.ICONINFORMATION, XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
						"ChamberDetails.updateSuccessMessage"),
				XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	}

	public void addChamber(RefChamberComplexValue refChamber) throws Exception {
		Integer newCrestChamberId = null;
		BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
		String userName = XhibitSingleton.getInstance().getUserSession()
				.getSessionProperty(UserTerminalProperties.DISPLAY_NAME);
			newCrestChamberId = bizRefDelegate.createNewChamber(refChamber, userName);
			showAddSuccessDialog(newCrestChamberId);
		
	}

	public void showAddSuccessDialog(Integer crestChamberId) {
		String message1 = XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
				"ChamberDetails.addSuccess.Chamber");
		String message2 = XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
				"ChamberDetails.addSuccess.Message");

		XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "ChamberDetails.addSuccessTitle"),
				true, XMessageBox.ICONINFORMATION, message1 + " " + crestChamberId + " " + message2,
				XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	}

	private void showUnsavedCancelConfirmationDialog() throws CSRecoverableException {
		boolean messageBoxReply = false;
		messageBoxReply = XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "ChamberDetails.cancelTitle"), true,
				XMessageBox.ICONQUESTION,
				XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "ChamberDetails.cancelMessage"),
				XMessageBox.YESNO, XMessageBox.DEFAULTCANCEL);
		if (!messageBoxReply) {
			throw new UserCancelException();
		}
		else {
			setParentRefreshSearchResults(false);
		}
	}

	// Method to add change listeners to each component within form
	private void addChangeListeners(Component[] components) {
		for (int i = 0; i < components.length; i++) {
			if (components[i].getClass() == XTextField.class) {
				((XTextField) components[i]).getDocument().addDocumentListener(new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						changesMade();
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						changesMade();
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						changesMade();
					}
				});
			}
		}
	}

	public void changesMade() {
		enableSaveButton();
		changesMade = true;
		btnAddCounsel.setEnabled(!isAmend & !isAddNew);
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
		}

		btnSave.setEnabled(isValid);
	}

	// Method to disable each component within form
	private void disableComponents(Component[] components) {
		for (int i = 0; i < components.length; i++) {
			if (components[i].getClass() == XTextField.class) {
				((XTextField) components[i]).setEnabled(false);
				((XTextField) components[i]).setDisabledTextColor(Color.BLACK);
			}
		}
	}

	private void makeReadOnly() {
		disableComponents(chamberDetailsPanel.getComponents());
		btnSave.setEnabled(false);
		btnAddCounsel.setEnabled(false);
		btnDelete.setEnabled(false);
	}

	private void setTxtChamberRefNo(String chamberRefNo) {
		this.txtChamberRefNo.setText(chamberRefNo);
	}

	private String getTxtFirmName() {
		return txtFirmName.getText();
	}

	private void setTxtFirmName(String firmName) {
		this.txtFirmName.setText(firmName);
	}

	private String getTxtAddress1() {
		return txtAddress1.getText();
	}

	private void setTxtAddress1(String address1) {
		this.txtAddress1.setText(address1);
	}

	private String getTxtAddress2() {
		return txtAddress2.getText();
	}

	private void setTxtAddress2(String address2) {
		this.txtAddress2.setText(address2);
	}

	private String getTxtAddress3() {
		return txtAddress3.getText();
	}

	private void setTxtAddress3(String address3) {
		this.txtAddress3.setText(address3);
	}

	private String getTxtAddress4() {
		return txtAddress4.getText();
	}

	private void setTxtAddress4(String address4) {
		this.txtAddress4.setText(address4);
	}

	private String getTxtTown() {
		return txtTown.getText();
	}

	private void setTxtTown(String town) {
		this.txtTown.setText(town);
	}

	private String getTxtCounty() {
		return txtCounty.getText();
	}

	private void setTxtCounty(String county) {
		this.txtCounty.setText(county);
	}

	private String getTxtPostcode() {
		return txtPostcode.getText();
	}

	private void setTxtPostcode(String postcode) {
		this.txtPostcode.setText(postcode);
	}

	private String getTxtClerkName() {
		return txtClerkName.getText();
	}

	private void setTxtClerkName(String clerkName) {
		this.txtClerkName.setText(clerkName);
	}

	private String getTxtTelephoneNumber() {
		return txtTelephoneNumber.getText();
	}

	private void setTxtTelephoneNumber(String telephone) {
		this.txtTelephoneNumber.setText(telephone);
	}

	private String getTxtFaxNumber() {
		return txtFaxNumber.getText();
	}

	private void setTxtFaxNumber(String fax) {
		this.txtFaxNumber.setText(fax);
	}

	private String getTxtDocExReference() {
		return txtDocExReference.getText();
	}

	private void setTxtDocExReference(String docEx) {
		this.txtDocExReference.setText(docEx);
	}

	private String getTxtEmail() {
		return txtEmail.getText();
	}

	private void setTxtEmail(String email) {
		this.txtEmail.setText(email);
	}

	private String getTxtSecureEmail() {
		return txtSecureEmail.getText();
	}

	private void setTxtSecureEmail(String secureEmail) {
		this.txtSecureEmail.setText(secureEmail);
	}

	private void setChckbxDeleted(Boolean checked) {
		this.deletedCheckbox.setSelected(checked);
	}

	/**
	 * If the model is not null then populate the fields with the values. Also
	 * sets the caret to 0 so that if the field is too long then it'll show the
	 * first half of the string instead of the end of the string.
	 */
	private void moveModelToScreen() {
		if (model != null) {
			if (model.getCallingClass() instanceof ChamberAndAdvocateDetailsPanel) {
				BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
				try {
					if (model.getRefChamberComplexValue() != null) {
						if (model.getRefChamberComplexValue().getId() != null) {
							isAmend = true;
							String address1 = "";
							String address2 = "";
							String address3 = "";
							String address4 = "";
							String town = "";
							String county = "";
							String postcode = "";
							RefChamberComplexValue refChamberVal = new RefChamberComplexValue();

							refChamberVal = bizRefDelegate
									.findChamberByRefChamberId(model.getRefChamberComplexValue().getId());
							setTxtChamberRefNo(refChamberVal.getCrestChamberId().toString());
							setTxtFirmName(refChamberVal.getFirmName());
							if (refChamberVal.getAddress() != null) {
								address1 = refChamberVal.getAddress().getAddress1();
								address2 = refChamberVal.getAddress().getAddress2();
								address3 = refChamberVal.getAddress().getAddress3();
								address4 = refChamberVal.getAddress().getAddress4();
								town = refChamberVal.getAddress().getTown();
								county = refChamberVal.getAddress().getCounty();
								postcode = refChamberVal.getAddress().getPostcode();
							}
							setTxtAddress1(address1);
							setTxtAddress2(address2);
							setTxtAddress3(address3);
							setTxtAddress4(address4);
							setTxtTown(town);
							setTxtCounty(county);
							setTxtPostcode(postcode);
							setTxtClerkName(refChamberVal.getClerkName());
							setTxtTelephoneNumber(refChamberVal.getTelephoneNumber());
							setTxtFaxNumber(refChamberVal.getFaxNumber());
							setTxtDocExReference(refChamberVal.getDxRef());
							setTxtEmail(refChamberVal.getEmailAddress());
							setTxtSecureEmail(refChamberVal.getSecureEmailAddress());
							if (refChamberVal.getObsInd().equals("Y")) {
								setChckbxDeleted(true);
							}
							if (refChamberVal.getObsInd().equals("Y") || (model.getReadOnly())) {
								makeReadOnly();
							}
						}
					} else {
						// Complex value is empty so Add New
						btnAddCounsel.setEnabled(false);
						isAddNew = true;
					}
				} catch (Exception er) {
					XHIBITConstant.handleError(er);
				}
			}
		}
	}

	/**
	 * Set the data entered on screen into the model.
	 */
	private void moveScreenToModel() {
		if (model.getRefChamberComplexValue() == null) {
			model.setRefChamberComplexValue(new RefChamberComplexValue());
		}
		RefChamberComplexValue ref = model.getRefChamberComplexValue();
		ref.setFirmName(getTxtFirmName());
		AddressBasicValue abv = new AddressBasicValue();
		abv.setAddress1(getTxtAddress1());
		abv.setAddress2(getTxtAddress2());
		abv.setAddress3(getTxtAddress3());
		abv.setAddress4(getTxtAddress4());
		abv.setTown(getTxtTown());
		abv.setCounty(getTxtCounty());
		abv.setPostcode(getTxtPostcode());
		if (ref.getAddress() != null && ref.getAddressId() != null) {
			if (!(ref.getAddressId().equals(0))) {
				abv.setAddressId(ref.getAddressId());
			}
		}
		ref.setAddress(abv);
		ref.setCourtId(XhibitSingleton.getInstance().getCourtId());
		ref.setClerkName(getTxtClerkName());
		ref.setTelephoneNumber(getTxtTelephoneNumber());
		ref.setFaxNumber(getTxtFaxNumber());
		ref.setDxRef(getTxtDocExReference());
		ref.setEmailAddress(getTxtEmail());
		ref.setSecureEmailAddress(getTxtSecureEmail());
		model.setRefChamberComplexValue(ref);
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
		addChangeListeners(chamberDetailsPanel.getComponents());
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
			throw new CSValidationException("validation.general", "Field validation Failed");
		}
		if (!(lblPostcodeInvalidEntry.getText().equals(" "))) {
			throw new CSValidationException("validation.general", "Field validation failed");
		}
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
		if (btnAddCounsel.equals(getDeinitialiseSource())) {
			RefAdvocateComplexValue refAdvocate = new RefAdvocateComplexValue();
			refAdvocate.setRefChamberId(model.getRefChamberComplexValue().getId());
			refAdvocate.setCrestChamberId(model.getRefChamberComplexValue().getCrestChamberId());
			CounselDetailsDialog counselDetailsDialog;
			counselDetailsDialog = new CounselDetailsDialog(parentDialog.getParentFrame(),
					new CounselDetailsModel(this, refAdvocate, false));
			counselDetailsDialog.setLocationRelativeTo(parentDialog.getParentFrame());
			counselDetailsDialog.setVisible(true);
		}
		else if (btnDelete.equals(getDeinitialiseSource())) {
			// check for any non-obsolete counsel records attached to this chamber
			BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
			Integer countLiveCounsel = bizRefDelegate.countRefAdvocateWithCrestChamberId(model.getRefChamberComplexValue().getCrestChamberId()); 
	
			if (countLiveCounsel > 0) {
				// Live counsel identified on the chamber - confirm want to delete chamber and associated counsel
				int result = JOptionPane.showConfirmDialog(
						this, 
						XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "ChamberDetails.delete.liveCounselMessage"),
						XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "ChamberDetails.delete.liveCounselTitle"), 
						JOptionPane.YES_NO_OPTION);
				if ((result == JOptionPane.NO_OPTION) || (result == JOptionPane.CLOSED_OPTION)) {
					log.debug("Dialog No option clicked, will not delete chamber and associated counsel");
					return;
				}
			} 
			else {
				// No associated counsel - ask user to confirm delete
				int result = JOptionPane.showConfirmDialog(
						this, 
						XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "ChamberDetails.delete.confirmMessage"), 
						XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "ChamberDetails.delete.confirmTitle"), 
						JOptionPane.OK_CANCEL_OPTION);
				if ((result == JOptionPane.CANCEL_OPTION) || (result == JOptionPane.CLOSED_OPTION)) {
					log.debug("Dialog No option clicked, will not delete chamber");
					return;
				}
			}
			
			// Delete all XHB_REF_CHAMBER for the given crest_chamber_id and any associated counsel
			bizRefDelegate.deleteRefChamber(model.getRefChamberComplexValue().getCrestChamberId(), 
					XhibitSingleton.getInstance().getUserSession().getUserName());
			// Success!
			JOptionPane.showMessageDialog(
					this, 
					XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "ChamberDetails.delete.successMessage"),
					XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "ChamberDetails.delete.successTitle"), 
					JOptionPane.INFORMATION_MESSAGE);
			
			setParentRefreshSearchResults(true);
			parentDialog.clearStatusBarScreenCode();
			parentDialog.dispose();
		}
		else if (btnSave.equals(getDeinitialiseSource())) {
			stepValidate();
			moveScreenToModel();
			setParentRefreshSearchResults(true);
			if (model.getRefChamberComplexValue().getId() == null
					|| model.getRefChamberComplexValue().getId().equals(0)) {
				// save Chamber
				try {
					addChamber(model.getRefChamberComplexValue());
					parentDialog.clearStatusBarScreenCode();
					parentDialog.dispose();
				} catch (Exception e) {
					XHIBITConstant.handleError(e);
				}
			} else {
				// amend Chamber
				updateChamber(model.getRefChamberComplexValue());
				parentDialog.clearStatusBarScreenCode();
				parentDialog.dispose();
			}
		} 
		else {
			if (changesMade) {
				log.debug("changes made");
				showUnsavedCancelConfirmationDialog();
			} else {
				setParentRefreshSearchResults(false);
				log.debug("No changes made");
			}
		}
	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {

	}
	
	/**
	 * Indicate on the parent class that a screen refresh is required - only if the parent is ChamberAndAdvocateDetailsPanel
	 * @param refresh
	 */
	private void setParentRefreshSearchResults(boolean refresh) {
		if ( model.getCallingClass() instanceof ChamberAndAdvocateDetailsPanel ) {
			((ChamberAndAdvocateDetailsPanel) model.getCallingClass()).setRefreshSearchResults(refresh, getCounselAdded());
		}
	}

}
