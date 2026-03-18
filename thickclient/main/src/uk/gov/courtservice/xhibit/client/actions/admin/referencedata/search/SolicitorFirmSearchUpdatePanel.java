package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.entities.xhb_wll_recipient.XhbWllRecipientBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailBasicValue;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchDetails;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.verifiers.EmailAddressVerifier;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * Ref SolicitorFirm - Update screen.
 * 
 * @author grewalg
 *
 */
public class SolicitorFirmSearchUpdatePanel extends RefSearchUpdatePanel {

	private static final long serialVersionUID = -5219710902763805593L;

	private static final Insets ERROR_INSETS = new Insets(0, 4, 0, 0);
	private static final double WEIGHT_LEFT = 0.32;
	private static final double WEIGHT_RIGHT = 0.32;

	private static BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();

	private JPanel mainPanel, buttonPanel;
	
	private JButton deleteButton;

	private JLabel solicitorFirmNameLbl, solicitorFirmNameErrLbl, address1Lbl, address1ErrLbl, address2ErrLbl,
			address3ErrLbl, address4ErrLbl, townLbl, townErrLbl, countyLbl, countyErrLbl, postcodeLbl, postcodeErrLbl,
			docExRefLbl, docExRefErrLbl, telephoneLbl, telephoneErrLbl, faxNoLbl, faxNoErrLbl, secureEmailLbl,
			secureEmailErrLbl, nonSecureEmailLbl, nonSecureEmailErrLbl, rOCodeLbl, rOCodeErrLbl, shortNameLocLbl,
			shortNameLocErrLbl;

	private XTextField solicitorFirmNameText, address1Text, address2Text, address3Text, address4Text, townText,
			countyText, postcodeText, docExRefText, telephoneText, faxNoText, secureEmailText, nonSecureEmailText,
			rOCodeText, shortNameLocText;

	private RefSolicitorFirmComplexValue refSolicitorFirm;
	
	private static final String userDisplayName = XhibitSingleton.getInstance().getUserSession()
	.getSessionProperty(UserTerminalProperties.DISPLAY_NAME);

	/*
	 * Getters and Setters
	 */

	/**
	 * @return the refSolicitorFirm
	 */
	public RefSolicitorFirmComplexValue getRefSolicitorFirm() {
		return refSolicitorFirm;
	}

	public CSValueObject getValueObject() {
		return refSolicitorFirm;
	}

	private String getSolicitorFirmName() {
		return solicitorFirmNameText.getText();
	}

	private String getAddress1() {
		return address1Text.getText();
	}

	private String getAddress2() {
		return address2Text.getText();
	}

	private String getAddress3() {
		return address3Text.getText();
	}

	private String getAddress4() {
		return address4Text.getText();
	}

	private String getTown() {
		return townText.getText();
	}

	private String getCounty() {
		return countyText.getText();
	}

	private String getPostcode() {
		return postcodeText.getText();
	}

	private String getDxRef() {
		return docExRefText.getText();
	}

	private String getTelephone() {
		return telephoneText.getText();
	}

	private String getFax() {
		return faxNoText.getText();
	}

	private String getSecureEmail() {
		return secureEmailText.getText();
	}

	private String getNonSecureEmail() {
		return nonSecureEmailText.getText();
	}

	private String getLaCode() {
		return rOCodeText.getText();
	}

	private String getShortNameLoc() {
		return shortNameLocText.getText();
	}

	/*
	 * Constructor/s
	 */
	public SolicitorFirmSearchUpdatePanel(XHIBITSearchDetails xsDetails, SolicitorFirmSearch xsSearch) {
		super(xsDetails, xsSearch);
		this.refSearchController = xsSearch;
	}

	/*
	 * Overridden methods.
	 */
	@Override
	public void jbInit(XHIBITSearchDetails ixsDetails) {
		this.xsDetails = ixsDetails;
	}

	@Override
	protected void createPanelControls(XDialog parent) {
		componentList = new ArrayList<Component>();
		textFields = new ArrayList<XTextField>();

		this.setLayout(new GridBagLayout());
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();

		gbc.gridwidth = 1;
		gbc.gridheight = 1;

		// Solicitor Firm Name
		addComponent(getPlaceholderPanel(), 0, 0, 0, ERROR_INSETS, GridBagConstraints.WEST);

		solicitorFirmNameLbl = new JLabel(XHIBITConstant.getResource(resources, "solicitorFirm.update.firmName"));
		addComponent(solicitorFirmNameLbl, 0, 1, 0, getNewInsets());

		solicitorFirmNameErrLbl = getErrorLbl();
		addComponent(solicitorFirmNameErrLbl, 1, 0, 0, ERROR_INSETS);

		solicitorFirmNameText = new XTextField(35, "^.{1,35}$", solicitorFirmNameErrLbl, true);
		solicitorFirmNameText.setMaxLength(35);
		addComponent(solicitorFirmNameText, 1, 1, WEIGHT_LEFT, getNewInsets());
		mandatoryFields.add(solicitorFirmNameText);

		// Address 1
		addComponent(getPlaceholderPanel(), 0, 2, 0, ERROR_INSETS, GridBagConstraints.WEST);

		address1Lbl = new JLabel(XHIBITConstant.getResource(resources, "solicitorFirm.update.address"));
		addComponent(address1Lbl, 0, 3, 0, getNewInsets());

		address1ErrLbl = getErrorLbl();
		addComponent(address1ErrLbl, 1, 2, 0, ERROR_INSETS);

		address1Text = new XTextField(30, "^.{1,30}$", address1ErrLbl, true);
		address1Text.setMaxLength(30);
		addComponent(address1Text, 1, 3, WEIGHT_LEFT, getNewInsets());
		mandatoryFields.add(address1Text);

		// Address 2
		addComponent(getPlaceholderPanel(), 0, 4, 0, ERROR_INSETS, GridBagConstraints.WEST);

		address2ErrLbl = getErrorLbl();
		addComponent(address2ErrLbl, 1, 4, 0, ERROR_INSETS);

		address2Text = new XTextField(30, "^.{1,30}$", address2ErrLbl, false);
		address2Text.setMaxLength(30);
		addComponent(address2Text, 1, 5, WEIGHT_LEFT, getNewInsets());

		// Address 3
		addComponent(getPlaceholderPanel(), 0, 6, 0, ERROR_INSETS, GridBagConstraints.WEST);

		address3ErrLbl = getErrorLbl();
		addComponent(address3ErrLbl, 1, 6, 0, ERROR_INSETS);

		address3Text = new XTextField(30, "^.{1,30}$", address3ErrLbl, false);
		address3Text.setMaxLength(30);

		addComponent(address3Text, 1, 7, WEIGHT_LEFT, getNewInsets());

		// Address 4
		addComponent(getPlaceholderPanel(), 0, 8, 0, ERROR_INSETS, GridBagConstraints.WEST);

		address4ErrLbl = getErrorLbl();
		addComponent(address4ErrLbl, 1, 8, 0, ERROR_INSETS);

		address4Text = new XTextField(30, "^.{1,30}$", address4ErrLbl, false);
		address4Text.setMaxLength(30);
		addComponent(address4Text, 1, 9, WEIGHT_LEFT, getNewInsets());

		// Town
		addComponent(getPlaceholderPanel(), 0, 10, 0, ERROR_INSETS, GridBagConstraints.WEST);

		townLbl = new JLabel(XHIBITConstant.getResource(resources, "solicitorFirm.update.town"));
		addComponent(townLbl, 0, 11, 0, getNewInsets());

		townErrLbl = getErrorLbl();
		addComponent(townErrLbl, 1, 10, 0, ERROR_INSETS);

		townText = new XTextField(30, "^.{1,30}$", townErrLbl, true);
		townText.setMaxLength(30);
		addComponent(townText, 1, 11, WEIGHT_LEFT, getNewInsets());
		mandatoryFields.add(townText);

		// County
		addComponent(getPlaceholderPanel(), 0, 12, 0, ERROR_INSETS, GridBagConstraints.WEST);

		countyLbl = new JLabel(XHIBITConstant.getResource(resources, "solicitorFirm.update.county"));
		addComponent(countyLbl, 0, 13, 0, getNewInsets());

		countyErrLbl = getErrorLbl();
		addComponent(countyErrLbl, 1, 12, 0, ERROR_INSETS);

		countyText = new XTextField(30, "^.{1,30}$", countyErrLbl, false);
		countyText.setMaxLength(30);
		addComponent(countyText, 1, 13, WEIGHT_LEFT, getNewInsets());

		// Postcode
		addComponent(getPlaceholderPanel(), 0, 14, 0, ERROR_INSETS, GridBagConstraints.WEST);

		postcodeLbl = new JLabel(XHIBITConstant.getResource(resources, "solicitorFirm.update.postCode"));
		addComponent(postcodeLbl, 0, 15, 0, getNewInsets());

		postcodeErrLbl = getErrorLbl();
		addComponent(postcodeErrLbl, 1, 14, 0, ERROR_INSETS);

		postcodeText = new XTextField(8, "", postcodeErrLbl, false, true);
		postcodeText.setMaxLength(8);
		addComponent(postcodeText, 1, 15, WEIGHT_LEFT, getNewInsets());

		// DocEx Ref
		addComponent(getPlaceholderPanel(), 0, 16, 0, ERROR_INSETS, GridBagConstraints.WEST);

		docExRefLbl = new JLabel(XHIBITConstant.getResource(resources, "solicitorFirm.update.docExRef"));
		addComponent(docExRefLbl, 0, 17, 0, getNewInsets());

		docExRefErrLbl = getErrorLbl();
		addComponent(docExRefErrLbl, 1, 16, 0, ERROR_INSETS);

		docExRefText = new XTextField(35, "^.{1,35}$", docExRefErrLbl, false);
		docExRefText.setMaxLength(35);
		addComponent(docExRefText, 1, 17, WEIGHT_LEFT, getNewInsets());

		// Telephone No.
		addComponent(getPlaceholderPanel(), 2, 0, 0, ERROR_INSETS, GridBagConstraints.WEST);

		telephoneLbl = new JLabel(XHIBITConstant.getResource(resources, "solicitorFirm.update.telephone"));
		addComponent(telephoneLbl, 2, 1, 0, getNewInsets());

		telephoneErrLbl = getErrorLbl();
		addComponent(telephoneErrLbl, 3, 0, 0, ERROR_INSETS);

		telephoneText = new XTextField(14, "^[0-9 ]{1,14}$", telephoneErrLbl, false);
		telephoneText.setMaxLength(14);
		addComponent(telephoneText, 3, 1, WEIGHT_RIGHT, getNewInsets());
		
		// Fax No.
		addComponent(getPlaceholderPanel(), 2, 2, 0, ERROR_INSETS, GridBagConstraints.WEST);

		faxNoLbl = new JLabel(XHIBITConstant.getResource(resources, "solicitorFirm.update.faxNo"));
		addComponent(faxNoLbl, 2, 3, 0, getNewInsets());

		faxNoErrLbl = getErrorLbl();
		addComponent(faxNoErrLbl, 3, 2, 0, ERROR_INSETS);

		faxNoText = new XTextField(14, "^[0-9 ]{1,14}$", faxNoErrLbl, false);
		faxNoText.setMaxLength(14);
		addComponent(faxNoText, 3, 3, WEIGHT_RIGHT, getNewInsets());

		// Secure Email
		addComponent(getPlaceholderPanel(), 2, 4, 0, ERROR_INSETS, GridBagConstraints.WEST);

		secureEmailLbl = new JLabel(XHIBITConstant.getResource(resources, "solicitorFirm.update.secureEmail"));
		addComponent(secureEmailLbl, 2, 5, 0, getNewInsets());

		secureEmailErrLbl = getErrorLbl();
		addComponent(secureEmailErrLbl, 3, 4, 0, ERROR_INSETS);

		secureEmailText = new XTextField(255, "^.{1,255}$", secureEmailErrLbl, false);
		addComponent(secureEmailText, 3, 5, WEIGHT_RIGHT, getNewInsets());
		secureEmailText.setMaxLength(255);

		// Non Secure Email
		addComponent(getPlaceholderPanel(), 2, 6, 0, ERROR_INSETS, GridBagConstraints.WEST);

		nonSecureEmailLbl = new JLabel(XHIBITConstant.getResource(resources, "solicitorFirm.update.nonSecureEmail"));
		addComponent(nonSecureEmailLbl, 2, 7, 0, getNewInsets());

		nonSecureEmailErrLbl = getErrorLbl();
		addComponent(nonSecureEmailErrLbl, 3, 6, 0, ERROR_INSETS);

		nonSecureEmailText = new XTextField(255, "^.{1,255}$", nonSecureEmailErrLbl, false);
		addComponent(nonSecureEmailText, 3, 7, WEIGHT_RIGHT, getNewInsets());
		nonSecureEmailText.setMaxLength(255);

		// RO Code
		addComponent(getPlaceholderPanel(), 2, 8, 0, ERROR_INSETS, GridBagConstraints.WEST);
		rOCodeLbl = new JLabel(XHIBITConstant.getResource(resources, "solicitorFirm.update.rOCode"));
		addComponent(rOCodeLbl, 2, 9, 0, getNewInsets());

		rOCodeErrLbl = getErrorLbl();
		addComponent(rOCodeErrLbl, 3, 8, 0, ERROR_INSETS);

		rOCodeText = new XTextField(6, "^[A-Za-z0-9]{1,6}$", rOCodeErrLbl, false);
		rOCodeText.setMaxLength(6);
		addComponent(rOCodeText, 3, 9, WEIGHT_RIGHT, getNewInsets());

		// ShortName Location
		addComponent(getPlaceholderPanel(), 2, 10, 0, ERROR_INSETS, GridBagConstraints.WEST);

		shortNameLocLbl = new JLabel(XHIBITConstant.getResource(resources, "solicitorFirm.update.shortNameLocation"));
		addComponent(shortNameLocLbl, 2, 11, 0, getNewInsets());

		shortNameLocErrLbl = getErrorLbl();
		addComponent(shortNameLocErrLbl, 3, 10, 0, ERROR_INSETS);

		shortNameLocText = new XTextField(28, "^.{1,28}$", shortNameLocErrLbl, true);
		shortNameLocText.setMaxLength(28);
		addComponent(shortNameLocText, 3, 11, WEIGHT_RIGHT, getNewInsets());
		mandatoryFields.add(shortNameLocText);

		// Add Main Panel
		gbc = new GridBagConstraints(0, 0, 4, 18, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(4, 4, 4, 4), 0, 0);
		doAdd(mainPanel, gbc);

		// Button Panel to contain the Back and Save buttons
		buttonPanel = new JPanel();
		this.backButton = new JButton();
		backButton.setAction(new BackAction(this));
		backButton.setMnemonic(((XAction) backButton.getAction()).getMnemonicKey().intValue());

		buttonPanel.add(backButton);
		componentList.add(backButton);
		
		// Delete button, separate from top level panel back and save buttons
		deleteButton = new JButton("Delete");
		deleteButton.setAction(new DeleteAction(this));
		buttonPanel.add(deleteButton);
		componentList.add(deleteButton);

		// Save button
		this.saveButton = new JButton();
		saveButton.setEnabled(false);
		saveButton.setAction(new SaveAction(this));
		buttonPanel.add(saveButton);
		componentList.add(saveButton);

		gbc = new GridBagConstraints(2, 18, 1, 1, 0.5, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(4, 0, 4, 8), 0, 0);
		doAdd(buttonPanel, gbc);

		// Spacer
		JSeparator separator = new JSeparator();
		gbc = new GridBagConstraints(0, 19, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				XHIBITConstant.nonContainerInsets, 0, 0);
		doAdd(separator, gbc);

		cancelButton = new JButton();
		cancelButton.setAction(new CancelAction(this));

		gbc = new GridBagConstraints(2, 20, 1, 1, 0.5, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(4, 0, 4, 12), 0, 0);
		doAdd(cancelButton, gbc);
		componentList.add(cancelButton);
	}

	@Override
	public void setValueObject(CSValueObject valueObject) {
		this.refSolicitorFirm = (RefSolicitorFirmComplexValue) valueObject;

		if (valueObject != null && isUpdate()) {
			solicitorFirmNameText.setText(this.refSolicitorFirm.getSolicitorFirmName());
			address1Text.setText(this.refSolicitorFirm.getAddress1());
			address2Text.setText(this.refSolicitorFirm.getAddress2());
			address3Text.setText(this.refSolicitorFirm.getAddress3());
			address4Text.setText(this.refSolicitorFirm.getAddress4());
			townText.setText(this.refSolicitorFirm.getTown());
			countyText.setText(this.refSolicitorFirm.getCounty());
			postcodeText.setText(this.refSolicitorFirm.getPostcode());
			docExRefText.setText(this.refSolicitorFirm.getDxRef());
			telephoneText.setText(this.refSolicitorFirm.getTelephoneNumber());
			faxNoText.setText(this.refSolicitorFirm.getFaxNumber());
			secureEmailText.setText(this.refSolicitorFirm.getSecureEmailAddress());
			nonSecureEmailText.setText(this.refSolicitorFirm.getNonsecureEmailAddress());
			rOCodeText.setText(this.refSolicitorFirm.getLaCode());
			shortNameLocText.setText(this.refSolicitorFirm.getShortName());
			
			if(refSolicitorFirm.getId() == null || refSolicitorFirm.getId() <= 0) {
				deleteButton.setEnabled(false);
			} else {
				deleteButton.setEnabled(true);
			}
		} else {
			solicitorFirmNameText.setText(null);
			address1Text.setText(null);
			address2Text.setText(null);
			address3Text.setText(null);
			address4Text.setText(null);
			townText.setText(null);
			countyText.setText(null);
			postcodeText.setText(null);
			docExRefText.setText(null);
			telephoneText.setText(null);
			faxNoText.setText(null);
			secureEmailText.setText(null);
			nonSecureEmailText.setText(null);
			rOCodeText.setText(null);
			shortNameLocText.setText(null);
			deleteButton.setEnabled(false);
		}
	}

	@Override
	protected void save() throws CSRecoverableException {
		// save to database only if all mandatory fields are entered and no
		// invalid
		// field entries exist.
		if (isUpdate()) {
			updateSolicitorFirm(refSolicitorFirm.getAddressId());
		} else {
			insertSolicitorFirm();
		}	
	}

	@Override
	protected void delete() throws CSRecoverableException {		
	}

	/**
	 * Get custom insets depending on controls position.
	 * 
	 * @return Insets
	 */
	private Insets getNewInsets() {
		int top = 2, left = 4, bottom = 4, right = 4;

		if (gbc.gridx == 0) {
			left = 10;
		}
		if (gbc.gridx == 2) {
			left = 20;
		}
		return new Insets(top, left, bottom, right);
	}

	/**
	 * Adds a component to the main panel.
	 * 
	 * @param comp
	 * @param gridx
	 * @param gridy
	 * @param weightx
	 * @param insets
	 */
	private void addComponent(Component comp, int gridx, int gridy, double weightx, Insets insets) {
		int defaultAnchor = GridBagConstraints.LINE_START;
		addComponent(comp, gridx, gridy, weightx, insets, defaultAnchor);
	}

	/**
	 * Adds a component to the main panel. Overloaded with custom anchor
	 * property specified.
	 * 
	 * @param comp
	 * @param gridx
	 * @param gridy
	 * @param weightx
	 * @param insets
	 * @param anchor
	 */
	private void addComponent(Component comp, int gridx, int gridy, double weightx, Insets insets, int anchor) {
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.weightx = weightx;
		gbc.insets = insets;
		gbc.anchor = anchor;

		if (comp instanceof XTextField) {
			((XTextField) comp).setColumns(12);
			((XTextField) comp).setPreferredSize(new Dimension(20, 20));
			((XTextField) comp).setUpperCase(true);
			comp.addFocusListener(new InvalidMandatoryDataFocusListener());
			comp.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					setModified(true);
				}
			});
			componentList.add(comp);
			textFields.add((XTextField) comp);
		}
		mainPanel.add(comp, gbc);
	}

	/**
	 * Only enable Save button if mandatory and invalid checks are met.
	 * 
	 * @return Boolean
	 */
	private Boolean validateSolicitorFirmFields() {
		// validate secure email address
		boolean isEmailValid = EmailAddressVerifier.isEmailAddressValid(secureEmailText.getText())
				&& validateSecureEmailAddress(secureEmailText.getText().toUpperCase(), "cjsm.net".toUpperCase());
		if ((getSecureEmail() != null) && !(getSecureEmail().equals(""))) {
			if (!isEmailValid) {
				secureEmailErrLbl.setVisible(true);
			} else {
				secureEmailErrLbl.setVisible(false);
			}
		} else {
			secureEmailErrLbl.setVisible(false);
		}

		// validate non-secure email address
		isEmailValid = EmailAddressVerifier.isEmailAddressValid(nonSecureEmailText.getText());
		if ((getNonSecureEmail() != null) && !(getNonSecureEmail().equals(""))) {
			if (!isEmailValid) {
				nonSecureEmailErrLbl.setVisible(true);
			} else {
				nonSecureEmailErrLbl.setVisible(false);
			}
		} else {
			nonSecureEmailErrLbl.setVisible(false);
		}

		// If mandatory fields are missing or there is invalid data then disable
		// the save button
		return validateFields();
	}
	
	/**
	 * Validation method for Secure Email Address.
	 * 
	 * @param text
	 * @param regex
	 * @return
	 */
	private boolean validateSecureEmailAddress(String text, String regex) {
		if (text.contains(regex)) {
			return true;
		}
		return false;
	}

	/**
	 * Update ValueObject from controls.
	 * 
	 */
	private void updateValueObject() {
		refSolicitorFirm.setSolicitorFirmName(getSolicitorFirmName());
		refSolicitorFirm.setAddress1(getAddress1());
		refSolicitorFirm.setAddress2(getAddress2());
		refSolicitorFirm.setAddress3(getAddress3());
		refSolicitorFirm.setAddress4(getAddress4());
		refSolicitorFirm.setTown(getTown());
		refSolicitorFirm.setCounty(getCounty());
		refSolicitorFirm.setPostcode(getPostcode());
		refSolicitorFirm.setDxRef(getDxRef());
		refSolicitorFirm.setTelephoneNumber(getTelephone());
		refSolicitorFirm.setFaxNumber(getFax());
		refSolicitorFirm.setSecureEmailAddress(getSecureEmail());
		refSolicitorFirm.setNonsecureEmailAddress(getNonSecureEmail());
		refSolicitorFirm.setLaCode(getLaCode());
		refSolicitorFirm.setShortName(getShortNameLoc());
	}

	@SuppressWarnings("unchecked")
	/**
	 * Save a RefSolicitorFirmComplex value to the database. If contact details
	 * don't exist, they will be inserted into the database otherwise just
	 * updated. Address details are updated in separate table.
	 * 
	 * @param addressId
	 */
	private void updateSolicitorFirm(Integer addressId) {
		updateValueObject();
		try {
			bizRefDelegate.updateRefSolicitorFirm(refSolicitorFirm.getId(), refSolicitorFirm, userDisplayName);
			
			setModified(false); 

			JOptionPane.showMessageDialog(null,
					XHIBITConstant.getResource(resources, "solicitorFirm.update.success.message"),
					XHIBITConstant.getResource(resources, "solicitorFirm.update.success.heading"),
					JOptionPane.INFORMATION_MESSAGE);
			
			getParentContainer().clearStatusBarScreenCode();
			getParentContainer().dispose();
		} catch (Exception er) {
			log.error("Error occurred while saving Solicitor Firm: " + er);
			JOptionPane.showMessageDialog((Component) null,
					"Unknown error occurred. Please contact system administrator", "Unknown Error occurred.",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Insert a new Solicitor Firm.
	 */
	private void insertSolicitorFirm() {
		updateValueObject();
		try {
			// Create Address
			AddressBasicValue address = new AddressBasicValue();
			address.setAddress1(refSolicitorFirm.getAddress1());
			address.setAddress2(refSolicitorFirm.getAddress2());
			address.setAddress3(refSolicitorFirm.getAddress3());
			address.setAddress4(refSolicitorFirm.getAddress4());
			address.setTown(refSolicitorFirm.getTown());
			address.setCounty(refSolicitorFirm.getCounty());
			address.setPostcode(refSolicitorFirm.getPostcode());

			// Create Contacts
			if (!(getTelephone().equals(""))) {
				refSolicitorFirm.setTelephoneNumber(getTelephone());
			}
			if (!(getFax().equals(""))) {
				refSolicitorFirm.setFaxNumber(getFax());
			}
			if (!(getSecureEmail().equals(""))) {
				refSolicitorFirm.setSecureEmailAddress(getSecureEmail());
			}
			if (!(getNonSecureEmail().equals(""))) {
				refSolicitorFirm.setNonsecureEmailAddress(getNonSecureEmail());
			}

			// Set CourtId
			refSolicitorFirm.setCourtId(XhibitSingleton.getInstance().getCourtId());

			// Create Solicitor Firm
			Integer refSolicitorFirmId = bizRefDelegate.createSolicitorFirm(refSolicitorFirm, address, 
					userDisplayName);

			refSolicitorFirm.setId(refSolicitorFirmId);
			setModified(false);
			JOptionPane.showMessageDialog(null,
					XHIBITConstant.getResource(resources, "solicitorFirm.update.success.message"),
					XHIBITConstant.getResource(resources, "solicitorFirm.update.success.heading"),
					JOptionPane.INFORMATION_MESSAGE);
			
			getParentContainer().clearStatusBarScreenCode();
			getParentContainer().dispose();
			
		} catch (Exception er) {
			log.error("Error occurred while saving Solicitor Firm: " + er);
			JOptionPane.showMessageDialog((Component) null,
					"Unknown error occurred. Please contact system administrator", "Unknown Error occurred.",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Event handler on tabbing out of a field to check validation.
	 * 
	 * @author grewalg
	 *
	 */
	class InvalidMandatoryDataFocusListener extends FocusAdapter {
		@Override
		public void focusLost(FocusEvent e) {
			saveButton
					.setEnabled(validateSolicitorFirmFields() && RefSearchUpdatePanelUtil.hasUnsavedData(componentList,
							((SolicitorFirmUpdateDialog) refSearchController.getUpdateDialog()).isUpdate(),
							getModified()));
		}
	}
	
	/* 
	 * DeleteAction called when delete button is pressed
	 * Created for CTX-3687
	 */
	class DeleteAction extends XAction {
		private static final long serialVersionUID = 1L;
		
		SolicitorFirmSearchUpdatePanel panel;
		
		public DeleteAction(SolicitorFirmSearchUpdatePanel panel) {
			this.panel = panel;
			populateFromBundle("btnDelete");
		}
		
		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			deleteSolicitorFirm();
		}
	}
	
	private void deleteSolicitorFirm() {
		int result = JOptionPane.showConfirmDialog(this, "Are you sure?", "Delete Solicitor Firm", JOptionPane.OK_CANCEL_OPTION);
		
		if(result == 0) {			
			try {
				// set obs ind for ref sol firm & update
				refSolicitorFirm.setObsInd("Y");
				bizRefDelegate.updateRefSolicitorFirm(refSolicitorFirm.getId(), refSolicitorFirm, userDisplayName);
				
				getParentContainer().clearStatusBarScreenCode();
				getParentContainer().dispose();
			} catch (SysRefControllerException e) {
				XHIBITConstant.handleError(e, this.getClass());
				log.debug("Failed deleting solicitor firm, id: " + refSolicitorFirm.getId());
			}
		}
	}
}