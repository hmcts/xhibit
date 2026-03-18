package uk.gov.courtservice.xhibit.client.updatecase;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;
import org.exolab.castor.util.JakartaOroEvaluator;

import mseries.ui.MChangeEvent;
import mseries.ui.MChangeListener;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.CaseStatusValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.updatecase.OpenAmendDefendantAction;
import uk.gov.courtservice.xhibit.client.actions.updatecase.UpdateDefendantModel;
import uk.gov.courtservice.xhibit.client.courtlog.CourtLogController;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.UnknownCaseTypeException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XNationalityPanel;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * UpdateDefendantXPanel
 * <p>
 * UpdateDefendantXPanel:
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
 * @author Frederik Vandendriessche
 * @version 1.0
 */
public class UpdateDefendantPanel extends XPanel {

	private static final long serialVersionUID = 1L;

	private static final Logger log = CSServices.getLogger(UpdateDefendantPanel.class);

	private static final int DRIVER_LIC_NO_OF_CHARS = 16;

	private static final String REG_EXP_POSTCODE = "(GIR 0AA)|^((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|"
			+ "(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z])))) [0-9][A-Z]{2})$";

	private static final String REG_EXP_NAME = "[A-Za-z0-9\\s~!\"@#$%&`'\\(\\)\\*\\+,\\-\\./:;<=>\\?\\[\\\\\\]_\\{\\}\\^£€]*";

	private static final String COMMA_SEPERATOR = ", ";

	private static final String EMPTY_SEPERATOR = "";

	private XhibitApplicationController xac;

	// this model represents all the info needed by XHIBIT to locate and
	// store a defendants' data
	// it does not include (all) the defendant's data itself (for that, see
	// the var 'model' below)
	private UpdateDefendantModel updateDefendantModel;

	// the value passed in during construction/initialisation and returned
	// to update the defendant
	private DefendantValue defendantValue;

	// Store the initial state so that we can determine
	// if the end state is different from the start state.
	private DefendantValue initialDefendantValue;

	// Name
	private JTextField firstNameTf = null;

	private JTextField middleTf = null;

	private JTextField initialTf = null;

	private JTextField lastNameTf = null;

	// Address
	private JTextField addressTf1 = null;

	private JTextField addressTf2 = null;

	private JTextField addressTf3 = null;

	private JTextField addressTf4 = null;

	private JTextField townTf = null;

	private JTextField countyTf = null;

	private JTextField postCodeTf = null;

	private XNationalityPanel nationalityCB = null;

	// DOB
	private XDatePanel dateOfBirth = null;

	// Sex
	private ButtonGroup sexRadioButtons = null;

	private JRadioButton maleRb;

	private JRadioButton femaleRB;

	private JRadioButton companyRB;

	// Juvenile
	private ButtonGroup juvenileRBGroup = null;

	private JRadioButton juvenileYes;

	private JRadioButton juvenileNo;

	// Masked name
	private ButtonGroup maskedRBGroup = null;

	private JRadioButton maskedYes;

	private JRadioButton maskedNo;

	private JTextField maskedNameJTextField;

	// Last conviction date
	private XDatePanel lastConvDate = null;

	// No of TICs
	private JTextField noOfTicsTF = null;
	private boolean noTicsChanged;
	
	// Driver license number
	private JTextField driverNoTF = null;
	private boolean driverNoChanged;
	
	//	Driver licence type and issue number
	private JComboBox licenceTypeCB = null;
	private boolean licenceTypeValid;
	private boolean licencetTypeChanged;
	
	private JTextField licenceIssueNumberTF = null;
	private boolean issueNumberValid;
	private boolean issueNumberChanged;
	private String hiddenIssueNumber;
	private boolean loading;

	private JTextField ptiurn = null;

	private JTextField asn = null;

	JLabel asnWarnTriangle;

	JLabel ptiurnWarnTriangle;

	/**
	 * Private variable used to store the magistrates court id, so that we can
	 * set the value object to the correct value once we are finished
	 */

	private OpenAmendDefendantAction openingAction;

	private OkCancelPanel buttonPanel;

	private ButtonGroup hideDefendantButtonGroup = new ButtonGroup();

	private JRadioButton hideDefendantInThisCase;

	private JRadioButton hideDefendantInAllCases;

	private JRadioButton hideDefendantNone;

	private GridBagConstraints gbc = XHIBITConstant.getDefaultGridBagConstraints();

	private UpdateDefendantDialog parent;

	private boolean asnModified = false;

	private boolean ptiurnModified = false;

	private final AsnHelper asnHelper = new AsnHelper();

	private final PtiurnHelper ptiurnHelper = new PtiurnHelper();

	// Hate Crime
	private JCheckBox hateCrime;

	private JComboBox hateCrimeComboBox;

	private Collection<RefSystemCodeBasicValue> colHateCodes;

	private DefaultComboBoxModel hateCodesModel;

	private RefSystemCodeBasicValue refSystemCodeValue;

	public UpdateDefendantPanel(UpdateDefendantDialog parent, UpdateDefendantModel model)
			throws CSRecoverableException {
		super();

		this.parent = parent;

		log.debug("Created Logger instance, continuing instantiation.");

		this.updateDefendantModel = model;

		this.buttonPanel = (OkCancelPanel) parent.getButtonPanel();
		this.openingAction = model.getAction();
		this.xac = (XhibitApplicationController) model.getAction().getController();

		if (XAction.internalDebug) {
			log.debug("the action's (" + openingAction.getName()
					+ ") internal debug is on, update defendant dialog component giving more debug info too:");
			log.debug("action.getModel() got Integer defendant id: " + this.updateDefendantModel.getDefendantID());
			log.debug("action.getModel() got Integer case id     : "
					+ this.updateDefendantModel.getCaseStatusValue().getCaseID());
			log.debug("action.getModel() got boolean in court    : "
					+ this.updateDefendantModel.getCaseStatusValue().isInCourt());
			log.debug("action.getModel() got CrestFormA Indicator: " + isCrestFormAFieldsOnly());
		}

		this.defendantValue = this.updateDefendantModel.getDefendantValue();
		this.initialDefendantValue = new DefendantValue(defendantValue);

		log.debug("action.getModel().getDefendantValue() : " + this.defendantValue);

		jbInit();
	}

	private boolean isCrestFormAFieldsOnly() {
		return this.updateDefendantModel.isCrestFormAFieldsOnly();
	}

	private void jbInit() throws CSRecoverableException {
		this.setLayout(new GridBagLayout());

		gbc.gridx = 0;
		gbc.gridy = 0;

		if (!isCrestFormAFieldsOnly()) {
			this.add(new JLabel(getResource("name")), gbc);

			// Standard defendant update screen
			gbc.gridx = 1;
			this.add(getFirstNameTf(), gbc);

			// mh - added the proper labels and names
			// middlename
			gbc.gridx = 0;
			gbc.gridy = gbc.gridy + 1;
			this.add(new JLabel(getResource("middlename")), gbc);
			gbc.gridx = 1;
			this.add(getMiddleTf(), gbc);

			// initials
			gbc.gridx = 0;
			gbc.gridy = gbc.gridy + 1;
			this.add(new JLabel(getResource("initials")), gbc);
			gbc.gridx = 1;
			this.add(getInitialTf(), gbc);

			// surname
			gbc.gridx = 0;
			gbc.gridy = gbc.gridy + 1;
			this.add(new JLabel(getResource("surname")), gbc);
			gbc.gridx = 1;
			this.add(getLastNameTf(), gbc);

			// DEFENDANT ADDRESS LINES
			gbc.fill = GridBagConstraints.NORTH;
			gbc.insets = XHIBITConstant.nonContainerInsets; // this is a non
			// container

			gbc.gridy = gbc.gridy + 1;
			gbc.gridx = 0;
			this.add(new JLabel(getResource("address")), gbc);
			gbc.gridx = 1;
			this.add(getAddressTf1(), gbc);
			gbc.gridy = gbc.gridy + 1;
			this.add(getAddressTf2(), gbc);
			gbc.gridy = gbc.gridy + 1;
			this.add(getAddressTf3(), gbc);
			gbc.gridy = gbc.gridy + 1;
			this.add(getAddressTf4(), gbc);

			// Town
			gbc.gridx = 0;
			gbc.gridy = gbc.gridy + 1;
			this.add(new JLabel(getResource("town")), gbc);
			gbc.gridx = 1;
			this.add(getTownTf(), gbc);

			// County
			gbc.gridx = 0;
			gbc.gridy = gbc.gridy + 1;
			this.add(new JLabel(getResource("county")), gbc);
			gbc.gridx = 1;
			this.add(getCountyTf(), gbc);

			// DEFENDANT POST CODE
			gbc.gridx = 0;
			gbc.gridy = gbc.gridy + 1;
			this.add(new JLabel(getResource("postcode")), gbc);
			gbc.gridx = 1;
			this.add(getPostCodeTf(), gbc);

			// CCN0400 - NATIONALITY - KD
			gbc.gridx = 0;
			gbc.gridy = gbc.gridy + 1;
			this.add(new JLabel(getResource("Nationality")), gbc);
			gbc.gridx = 1;
			this.add(getNationalityCB(), gbc);

			// DEFENDANT DATE OF BIRTH
			gbc.gridx = 0;
			gbc.gridy = gbc.gridy + 1;
			this.add(new JLabel(getResource("dateofbirth")), gbc);
			gbc.gridx = 1;
			this.add(getDateOfBirth(), gbc);

			// DEFENDANT SEX
			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.gridx = 0;
			gbc.gridy = gbc.gridy + 1;
			this.add(new JLabel(getResource("sex")), gbc);

			JPanel sexInputFields = new JPanel(new FlowLayout());
			sexInputFields.add(getMaleRb());
			sexInputFields.add(getFemaleRB());
			sexInputFields.add(getCompanyRb());

			sexRadioButtons = new ButtonGroup();
			sexRadioButtons.add(getMaleRb());
			sexRadioButtons.add(getFemaleRB());
			sexRadioButtons.add(getCompanyRb());

			gbc.insets = XHIBITConstant.containerInsets;
			gbc.gridx = 1;
			this.add(sexInputFields, gbc);

			// juvenile indicator
			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.gridy = gbc.gridy + 1;
			gbc.gridx = 0;
			this.add(new JLabel(getResource("juvenile")), gbc);

			JPanel juvenileRBPanel = new JPanel(new FlowLayout());
			juvenileRBPanel.add(getJuvenileYes());
			juvenileRBPanel.add(getJuvenileNo());

			juvenileRBGroup = new ButtonGroup();
			juvenileRBGroup.add(getJuvenileYes());
			juvenileRBGroup.add(getJuvenileNo());

			// X54142, ensure that the juvenile radio buttons are disabled
			getJuvenileYes().setEnabled(false);
			getJuvenileNo().setEnabled(false);

			gbc.insets = XHIBITConstant.containerInsets;
			gbc.gridx = 1;
			this.add(juvenileRBPanel, gbc);

			// masked name
			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.gridy = gbc.gridy + 1;
			gbc.gridx = 0;
			this.add(new JLabel(getResource("masked")), gbc);

			JPanel maskedRBPanel = new JPanel(new FlowLayout());
			maskedRBPanel.add(getMaskedYes());
			maskedRBPanel.add(getMaskedNo());
			maskedRBGroup = new ButtonGroup();
			maskedRBGroup.add(getMaskedYes());
			maskedRBGroup.add(getMaskedNo());

			gbc.insets = XHIBITConstant.containerInsets;
			gbc.gridx = 1;
			this.add(maskedRBPanel, gbc);

			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.gridy = gbc.gridy + 1;
			gbc.gridx = 0;
			this.add(new JLabel(getResource("maskedName")), gbc);

			gbc.gridx = 1;
			this.add(getMaskedNameJTextField(), gbc);

			// MH - changed from masked name to lastConvDate
			gbc.gridy = gbc.gridy + 1;
			gbc.gridx = 0;
			this.add(new JLabel(getResource("lastConvDate")), gbc);

			// MH - added to get the date as a XDatePanel
			gbc.gridx = 1;
			this.add(getLastConvDate(), gbc);

			// asn
			gbc.gridx = 0;
			gbc.gridy = gbc.gridy + 1;
			this.add(new JLabel(getResource("asn")), gbc);

			// add warningTriangle to east of the asn field
			this.add(getAsnWarnTriangle(), new GridBagConstraints(gbc.gridx, gbc.gridy, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.NONE, XHIBITConstant.containerInsets, 0, 0));

			gbc.gridx = 1;
			this.add(getAsn(), gbc);

			// ptiurn
			gbc.gridx = 0;
			gbc.gridy = gbc.gridy + 1;
			this.add(new JLabel(getResource("ptiurn")), gbc);
			// add warningTriangle to east of the asn field
			this.add(getPtiurnWarnTriangle(), new GridBagConstraints(gbc.gridx, gbc.gridy, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.NONE, XHIBITConstant.containerInsets, 0, 0));

			gbc.gridx = 1;
			this.add(getPtiurn(), gbc);

			// Hate crime
			gbc.gridx = 0;
			gbc.gridy = gbc.gridy + 1;
			this.add(new JLabel(getResource("hatecrime")), gbc);

			gbc.gridx = 1;
			this.add(getHateCrimeCheckBox(), gbc);

			// hateCrimeComboBox = new JComboBox();

			gbc.gridy = gbc.gridy + 1;
			// getHateCrimeComboBox();
			this.add(getHateCrimeComboBox(), gbc);

			if (updateDefendantModel.isShowPublicDisplayHidingControls()) {
				// Hide defendant in public display control
				gbc.gridx = 0;
				gbc.gridy = gbc.gridy + 1;
				this.add(new JLabel(getResource("hideInPublicDisplay")), gbc);

				gbc.gridx = 1;
				this.add(getHideInPublicDisplayPanel(), new GridBagConstraints(gbc.gridx, gbc.gridy, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE, XHIBITConstant.containerInsets, 0, 0));
			}

		} else {
			// CREST Form A defendant details screen
			// MH - updated section here to display the values
			// and also set the right labels.
			FlowLayout fl = new FlowLayout();
			fl.setAlignment(FlowLayout.LEFT);
			fl.setHgap(8);
			JPanel nameInputFields = new JPanel(fl);

			nameInputFields.add(new JLabel(getResource("name")));
			nameInputFields.add(getFirstNameTf());
			getFirstNameTf().setEditable(false);
			nameInputFields.add(getMiddleTf());
			getMiddleTf().setEditable(false);
			nameInputFields.add(getInitialTf());
			getInitialTf().setEditable(false);
			nameInputFields.add(getLastNameTf());
			getLastNameTf().setEditable(false);
			gbc.gridx = 0;

			// set left inset to handle horizontal gap of flow layout.
			Insets standardInsets = (Insets) gbc.insets.clone();
			gbc.insets = standardInsets;
			gbc.insets.left = -4;
			gbc.insets.bottom = -1;
			this.add(nameInputFields, gbc);

			// reset standard insets.
			gbc.insets = XHIBITConstant.nonContainerInsets;

			JPanel licenceDetails = new JPanel(fl);
			licenceDetails.add(new JLabel(getResource("numberOfTics")));
			licenceDetails.add(getNoOfTicsTF());
			licenceDetails.add(new JLabel(getResource("driverNumber")));
			licenceDetails.add(getDriverNoTF());
			
			gbc.gridy = gbc.gridy + 1;
			gbc.gridx = 0;
			this.add( licenceDetails, gbc);
			
			//	Licence type and issue number:
			JPanel licenceTypeDetails = new JPanel(fl);
			licenceTypeDetails.add(new JLabel(getResource("licenceType")));
			licenceTypeDetails.add(getLicenceTypeCB());
			licenceTypeDetails.add(new JLabel(getResource("licenceIssue")));
			licenceTypeDetails.add(getLicenceIssueNumberTF());
			
			gbc.gridy = gbc.gridy + 1;
			this.add( licenceTypeDetails, gbc);

			/** driving lic statuc */
			JLabel drLcStatLbl = new JLabel();
			gbc.gridy = gbc.gridy + 1;
			gbc.gridx = 0;
			this.add(drLcStatLbl, gbc);

		}
	}

	/**
	 * Returns a JTextField that automatically converts text to uppercase and
	 * only accepts a limited number of characters.
	 * 
	 * @return JTextField
	 */
	private JTextField getFirstNameTf() {
		if (firstNameTf == null) {
			Document doc = DocumentFactory
					.newDocument(new Capability[] { Capability.upperCase(), Capability.limitedText(35) });
			firstNameTf = JTextFieldFactory.getTextField(doc);
			firstNameTf.setText(tidyUp(defendantValue.getFirstName()));
			firstNameTf.setColumns(!isCrestFormAFieldsOnly() ? 18 : 8);
			firstNameTf.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					Name_keyPressed(e);
				}
			});
			log.debug("set first name to " + firstNameTf.getText());
		}
		return firstNameTf;
	}

	/**
	 * Returns a JTextField that automatically converts text to uppercase and
	 * only accepts a limited number of characters.
	 * 
	 * @return JTextField
	 */
	private JTextField getMiddleTf() {
		if (middleTf == null) {
			Document doc = DocumentFactory
					.newDocument(new Capability[] { Capability.upperCase(), Capability.limitedText(35) });
			middleTf = JTextFieldFactory.getTextField(doc);
			middleTf.setText(tidyUp(defendantValue.getMiddleName()));
			middleTf.setColumns(!isCrestFormAFieldsOnly() ? 18 : 8);
			middleTf.setEnabled(!isCrestFormAFieldsOnly());
			middleTf.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					Name_keyPressed(e);
				}
			});
			log.debug("set middle name to " + middleTf.getText());
		}
		return middleTf;
	}

	/**
	 * Returns a JTextField that store the defendants initials.
	 * 
	 * @return the initials JTextField
	 */
	private JTextField getInitialTf() {
		if (initialTf == null) {
			initialTf = createUpperCaseJTextField(4);
			initialTf.setText(tidyUp(defendantValue.getInitials()));
			initialTf.setColumns(!isCrestFormAFieldsOnly() ? 4 : 2);
			initialTf.setEnabled(!isCrestFormAFieldsOnly());
			initialTf.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					enableOkButton();
				}
			});
			log.debug("set initials name to " + initialTf.getText());
		}
		return initialTf;
	}

	/**
	 * Returns a JTextField that automatically converts text to uppercase and
	 * only accepts a limited number of characters.
	 * 
	 * @return JTextField
	 */
	private JTextField getLastNameTf() {
		if (lastNameTf == null) {
			Document doc = DocumentFactory
					.newDocument(new Capability[] { Capability.upperCase(), Capability.limitedText(35) });
			lastNameTf = JTextFieldFactory.getTextField(doc);
			lastNameTf.setText(tidyUp(defendantValue.getSurName()));
			lastNameTf.setColumns(!isCrestFormAFieldsOnly() ? 25 : 18);
			lastNameTf.setEnabled(!isCrestFormAFieldsOnly());
			lastNameTf.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					Name_keyPressed(e);
				}
			});
			log.debug("set last name to " + lastNameTf.getText());
		}
		return lastNameTf;
	}

	/**
	 * Returns a JTextField that automatically converts text to uppercase and
	 * only accepts a limited number of characters.
	 * 
	 * @return JTextField
	 */
	private JTextField getAddressTf1() {
		if (addressTf1 == null) {
			Document doc = DocumentFactory
					.newDocument(new Capability[] { Capability.upperCase(), Capability.limitedText(30) });
			addressTf1 = JTextFieldFactory.getTextField(doc);
			addressTf1.setText(defendantValue.getAddressValue() == null ? ""
					: tidyUp(defendantValue.getAddressValue().getAddress1()));
			addressTf1.setColumns(25);
			addressTf1.setEnabled(true);
			addressTf1.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					enableOkButton();
				}
			});
			log.debug("set address 1 to " + addressTf1.getText());
		}
		return addressTf1;
	}

	/**
	 * Returns a JTextField that automatically converts text to uppercase and
	 * only accepts a limited number of characters.
	 * 
	 * @return JTextField
	 */
	private JTextField getAddressTf2() {
		if (addressTf2 == null) {
			Document doc = DocumentFactory
					.newDocument(new Capability[] { Capability.upperCase(), Capability.limitedText(30) });
			addressTf2 = JTextFieldFactory.getTextField(doc);
			addressTf2.setText(defendantValue.getAddressValue() == null ? ""
					: tidyUp(defendantValue.getAddressValue().getAddress2()));
			addressTf2.setColumns(25);
			addressTf2.setEnabled(true);
			addressTf2.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					enableOkButton();
				}
			});
			log.debug("set address 2 to " + addressTf2.getText());
		}
		return addressTf2;
	}

	/**
	 * Returns a JTextField that automatically converts text to uppercase and
	 * only accepts a limited number of characters.
	 * 
	 * @return JTextField
	 */
	private JTextField getAddressTf3() {
		if (addressTf3 == null) {
			Document doc = DocumentFactory
					.newDocument(new Capability[] { Capability.upperCase(), Capability.limitedText(30) });
			addressTf3 = JTextFieldFactory.getTextField(doc);
			addressTf3.setText(defendantValue.getAddressValue() == null ? ""
					: tidyUp(defendantValue.getAddressValue().getAddress3()));
			addressTf3.setColumns(25);
			addressTf3.setEnabled(true);
			addressTf3.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					enableOkButton();
				}
			});
			log.debug("set address 3 to " + addressTf3.getText());
		}
		return addressTf3;
	}

	/**
	 * Returns a JTextField that automatically converts text to uppercase and
	 * only accepts a limited number of characters.
	 * 
	 * @return JTextField
	 */
	private JTextField getAddressTf4() {
		if (addressTf4 == null) {
			Document doc = DocumentFactory
					.newDocument(new Capability[] { Capability.upperCase(), Capability.limitedText(30) });
			addressTf4 = JTextFieldFactory.getTextField(doc);
			addressTf4.setText(defendantValue.getAddressValue() == null ? ""
					: tidyUp(defendantValue.getAddressValue().getAddress4()));
			addressTf4.setColumns(25);
			addressTf4.setEnabled(true);
			addressTf4.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					enableOkButton();
				}
			});
			log.debug("set address 4 to " + addressTf4.getText());
		}
		return addressTf4;
	}

	/**
	 * Returns a JTextField that automatically converts text to uppercase and
	 * only accepts a limited number of characters.
	 * 
	 * @return JTextField
	 */
	private JTextField getTownTf() {
		if (townTf == null) {
			Document doc = DocumentFactory
					.newDocument(new Capability[] { Capability.upperCase(), Capability.limitedText(30) });
			townTf = JTextFieldFactory.getTextField(doc);
			townTf.setText(
					defendantValue.getAddressValue() == null ? "" : tidyUp(defendantValue.getAddressValue().getTown()));
			townTf.setColumns(25);
			townTf.setEnabled(true);
			townTf.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					enableOkButton();
				}
			});
			log.debug("set town to " + townTf.getText());
		}
		return townTf;
	}

	private JTextField getPtiurn() {
		if (ptiurn == null) {
			Document doc = DocumentFactory
					.newDocument(new Capability[] { Capability.upperCase(), Capability.limitedText(11) });
			ptiurn = JTextFieldFactory.getTextField(doc);
			ptiurn.setText(tidyUp(defendantValue.getPtiurn()));
			ptiurn.setColumns(11);
			ptiurn.setToolTipText(getResource("PtiurnFormatToolTip"));
			ptiurn.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					enableOkButton();
					ptiurnModified = true;
					try {
						stepUpdateViewState();
					} catch (CSRecoverableException csre) {
						XHIBITErrorHandler.handleError(csre);
					}
				}
			});
			log.debug("set ptiurn to " + ptiurn.getText());
		}
		return ptiurn;
	}

	private JTextField getAsn() {
		if (asn == null) {
			Document doc = DocumentFactory
					.newDocument(new Capability[] { Capability.upperCase(), Capability.limitedText(20) });
			asn = JTextFieldFactory.getTextField(doc);
			asn.setText(tidyUp(defendantValue.getAsn()));
			asn.setColumns(20);
			asn.setToolTipText(getResource("AsnFormatToolTip"));
			asn.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					enableOkButton();
					asnModified = true;
					try {
						stepUpdateViewState();
					} catch (CSRecoverableException csre) {
						XHIBITErrorHandler.handleError(csre);
					}
				}
			});
			log.debug("set asn to " + asn.getText());
		}
		return asn;
	}

	protected JLabel getAsnWarnTriangle() {
		if (asnWarnTriangle == null) {
			log.debug("inside getAsnWarnTriangle");
			asnWarnTriangle = createWarningLabel(getResource("AsnWarningTriangleToolTip"));
		}
		return asnWarnTriangle;
	}

	protected JLabel getPtiurnWarnTriangle() {
		if (ptiurnWarnTriangle == null) {
			log.debug("inside getPtiurnWarningLabel");
			ptiurnWarnTriangle = createWarningLabel(getResource("PtiurnWarningTriangleToolTip"));
		}
		return ptiurnWarnTriangle;
	}

	public static JLabel createWarningLabel(String toolTipText) {
		JLabel warningLabel = new JLabel(getIcon("twarning.gif"));
		if (toolTipText != null)
			warningLabel.setToolTipText(toolTipText);
		warningLabel.setVisible(false);
		return warningLabel;
	}

	public static ImageIcon getIcon(String iconName) {
		URL u = UpdateDefendantPanel.class.getClassLoader().getResource(XHIBITConstant.imageRoot + iconName);
		ImageIcon icon = new ImageIcon(u);
		return icon;
	}

	/**
	 * Returns a JTextField that automatically converts text to uppercase and
	 * only accepts a limited number of characters.
	 * 
	 * @return JTextField
	 */
	private JTextField getCountyTf() {
		if (countyTf == null) {
			Document doc = DocumentFactory
					.newDocument(new Capability[] { Capability.upperCase(), Capability.limitedText(30) });
			countyTf = JTextFieldFactory.getTextField(doc);
			countyTf.setText(defendantValue.getAddressValue() == null ? ""
					: tidyUp(defendantValue.getAddressValue().getCounty()));
			countyTf.setColumns(25);
			countyTf.setEnabled(true);
			countyTf.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					enableOkButton();
				}
			});
			log.debug("set county to " + countyTf.getText());
		}
		return countyTf;
	}

	/**
	 * Returns a JTextField that automatically converts text to uppercase and
	 * only accepts a limited number of characters.
	 * 
	 * @return JTextField
	 */
	private JTextField getPostCodeTf() {
		if (postCodeTf == null) {
			postCodeTf = createUpperCaseJTextField(8);
			postCodeTf.setText(defendantValue.getAddressValue() == null ? ""
					: removeAllButLettersDigitsAndSpaces(tidyUp(defendantValue.getAddressValue().getPostcode())));
			postCodeTf.setColumns(8);
			postCodeTf.setEnabled(true);
			postCodeTf.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					enableOkButton();
				}
			});
			log.debug("set post code to " + postCodeTf.getText());
		}
		return postCodeTf;
	}

	private JCheckBox getHateCrimeCheckBox() {
		if (hateCrime == null) {
			hateCrime = new JCheckBox();
			boolean indicator = false;
			if (defendantValue.getHateIndicator().equals("Y")) {
				indicator = true;
				// hateCrimeComboBox.setEditable(true);
			} else {
				indicator = false;
				// hateCrimeComboBox.setEditable(false);
			}
			hateCrime.setSelected(indicator);
			hateCrime.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						stepUpdateViewState();
					} catch (CSRecoverableException csre) {
						XHIBITErrorHandler.handleError(csre);
					}
					// if(hateCrime.isSelected()){
					// getHateCrimeComboBox().setEnabled(true);
					// }else{
					// hateCrimeComboBox.setEnabled(false);
					// }
					enableOkButton();
				}
			});

		}
		return hateCrime;
	}

	/**
	 * CCN0400 - KD Returns a JComboBox that displays a list of Countries and
	 * their associated description.
	 * 
	 * @return JComboBox
	 */
	private XNationalityPanel getNationalityCB() {
		if (nationalityCB == null) {

			try {
				BisRefControllerBeanBusinessDelegate controller = XhibitDelegateHelper.getBizRefDelegate();
				Collection<String> nationalityCollection = controller.findAllNationalities();

				String curentNationality = defendantValue.getDefOnCaseBasicValue().getNationality();
				nationalityCB = new XNationalityPanel(null, curentNationality, nationalityCollection);
				nationalityCB.setEnabled(true);
				nationalityCB.setRequired(false);

				nationalityCB.getNationalityComponent().getDisplay().addKeyListener(new KeyAdapter() {
					public void keyReleased(KeyEvent e) {
						enableOkButton();
					}
				});

				nationalityCB.addMChangeListener(new MChangeListener() {
					public void valueChanged(MChangeEvent e) {
						if (e.getType() == MChangeEvent.PULLDOWN_OPENED
								|| e.getType() == MChangeEvent.PULLDOWN_CLOSED) {
							// If the chooser is being opened or closed the
							// nationality will not
							// have changed.
							return;
						}
						enableOkButton();
					}
				});

			} catch (SysRefControllerException e) {
				CSRecoverableException csre = new CSRecoverableException(
						"gui.updateDefendantActtion.defendantvalueupdate",
						"Exception whilst reading the xhb_ref_nationality table", e);
				XHIBITErrorHandler.handleError(csre);
			}
		}
		return nationalityCB;
	}

	private JRadioButton getHideDefendantInThisCaseRB() {
		if (hideDefendantInThisCase == null) {
			// Hide-in-all-cases takes priority over hide-in-this-case.
			boolean selected = (defendantValue.getDefOnCaseBasicValue().isHideDefendantInThisCase()
					&& !defendantValue.getHideDefendantInAllCases());
			hideDefendantInThisCase = new JRadioButton(getResource("hideInThisCase"), selected);
			hideDefendantInThisCase.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enableOkButton();
				}
			});
		}
		return hideDefendantInThisCase;
	}

	private JRadioButton getHideDefendantInAllCasesRB() {
		if (hideDefendantInAllCases == null) {
			boolean selected = defendantValue.getHideDefendantInAllCases();
			hideDefendantInAllCases = new JRadioButton(getResource("hideInAllCases"), selected);
			hideDefendantInAllCases.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enableOkButton();
				}
			});
		}
		return hideDefendantInAllCases;
	}

	private JRadioButton getHideDefendantNoneRB() {
		if (hideDefendantNone == null) {
			boolean selected = (!defendantValue.getHideDefendantInAllCases()
					&& !defendantValue.getDefOnCaseBasicValue().isHideDefendantInThisCase());
			hideDefendantNone = new JRadioButton(getResource("hideNone"), selected);
			hideDefendantNone.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enableOkButton();
				}
			});
		}
		return hideDefendantNone;
	}

	private JPanel getHideInPublicDisplayPanel() {
		JPanel panel = new JPanel();

		panel.add(getHideDefendantInThisCaseRB());
		panel.add(getHideDefendantInAllCasesRB());
		panel.add(getHideDefendantNoneRB());

		hideDefendantButtonGroup.add(getHideDefendantInThisCaseRB());
		hideDefendantButtonGroup.add(getHideDefendantInAllCasesRB());
		hideDefendantButtonGroup.add(getHideDefendantNoneRB());

		return panel;
	}

	/**
	 * Returns a JTextField that automatically converts text to uppercase and
	 * only accepts a limited number of characters.
	 * 
	 * @return JTextField
	 */
	private JTextField getDriverNoTF() {
		if (driverNoTF == null) {
			DefendantOnCaseValue dc = updateDefendantModel.getDefendantOnCaseValue();

			Document doc = DocumentFactory.newDocument(
					new Capability[] { Capability.upperCase(), Capability.limitedText(DRIVER_LIC_NO_OF_CHARS) });
			driverNoTF = JTextFieldFactory.getTextField(doc);
			driverNoTF.setText(dc == null ? "" : tidyUp(dc.getDriverNumber()));
			driverNoTF.setColumns(DRIVER_LIC_NO_OF_CHARS);
			driverNoTF.setEnabled(true);

			driverNoTF.getDocument().addDocumentListener( new DocumentListener() {

				@Override
				public void insertUpdate(DocumentEvent e) {
					check();					
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					check();					
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					check();					
				}
				
				private void check(){
					driverNoChanged = true;
					
					checkAllChanged();
				}
			});
			
			driverNoChanged = false;
			log.debug("set driver license number to " + driverNoTF.getText());
		}
		return driverNoTF;
	}

	/**
	 * Check all changes to enable OK button
	 */
	private void checkAllChanged(){
		LicenceType licenceType = (LicenceType)getLicenceTypeCB().getSelectedItem();
		
		String licenceTypeValue = licenceType.type;
		
		boolean driverNumberValid = true;
		
		if ( licenceTypeValue.equals(TYPE_PROV) || licenceTypeValue.equals(TYPE_FULL)){
			String driverNumber = getDriverNoTF().getText();
			
			driverNumberValid = driverNumber.length() == DRIVER_LIC_NO_OF_CHARS;
		}
		else if ( licenceTypeValue.equals(TYPE_UNDEFINED)){
			licenceTypeValid= false;
		}

		boolean outcome = ( driverNoChanged | noTicsChanged | issueNumberChanged |
						  licencetTypeChanged ) & ( issueNumberValid & driverNumberValid & licenceTypeValid );
		
		buttonPanel.okButton.setEnabled(outcome);
	}
	
	/**
	 * Manages changes to the licene type
	 * 
	 * @author rogersa
	 *
	 */
	private class LicenceTypeListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			licencetTypeChanged = true;
			licenceTypeValid = false;
			
			LicenceType licenceType = (LicenceType)getLicenceTypeCB().getSelectedItem();
			
			if ( licenceType != null ){
				updateIssueNumber( licenceType.type );
				licenceTypeValid = true;
			}
			
			checkAllChanged();
		}
		
	}

	/**
	 * Update the licence issue number depending upon the state of the licence type
	 * 
	 * @param 	licenceType				The licence type to check against
	 */
	private void updateIssueNumber( String licenceType ){
		loading = true;			//	Stops hiddenIssueNumber being cleared except when manually updated
		
		if ( licenceType.equals( TYPE_PROV) || licenceType.equals(TYPE_FULL)){
			getLicenceIssueNumberTF().setText(formatLicenceIssueNumber(hiddenIssueNumber));
			getLicenceIssueNumberTF().setEnabled( true );
			issueNumberValid = hiddenIssueNumber.length() > 0;
		}
		else{
			hiddenIssueNumber = getLicenceIssueNumberTF().getText();
			getLicenceIssueNumberTF().setText("");
			getLicenceIssueNumberTF().setEnabled(false);
			issueNumberValid = true;				//	If we can't change it, it's considered valid.
		}
		
		loading = false;
	}
	
	private class LicenceType
	{
		
		/**
		 * Constructor
		 * 
		 * @param 	description				The description
		 * @param 	type					The type
		 */
		public LicenceType( String description, String type )
		{
			this.description = description;
			this.type = type;
		}
		
		public String description;
		public String type;
		
		@Override public String toString(){
			return description;
		}
	}
	
	private static final String TYPE_UNDEFINED = "";
	private static final String TYPE_NONE = "0";
	private static final String TYPE_PROV = "1";
	private static final String TYPE_FULL = "2";
	private static final String TYPE_NONE_UK = "3";
	private static final String TYPE_DVLA = "5";
	
	/**
	 * Returns a JComboBox that contains the list of acceptable licence types.
	 * 
	 * @return	JComboBox
	 * 
	 * Note: This will need some updates.
	 */
	private JComboBox getLicenceTypeCB(){
		if ( licenceTypeCB == null){
			LicenceType[] types = {
				new LicenceType(getResource("typeUndefined"), TYPE_UNDEFINED),
				new LicenceType(getResource("typeNone"), TYPE_NONE),
				new LicenceType(getResource("typeProv"), TYPE_PROV ),
				new LicenceType(getResource("typeFull"), TYPE_FULL ),
				new LicenceType(getResource("typeNonUk"), TYPE_NONE_UK ),
				new LicenceType(getResource("typeDvla"), TYPE_DVLA )
			};
			
			DefendantOnCaseValue dc = updateDefendantModel.getDefendantOnCaseValue();
			String licenceType = dc == null ? "" : dc.getLicenceType();
			if ( licenceType == null ) {
				licenceType = "";
			}
			
			int index = -1;
			for ( int offset = 0; offset < types.length && index == -1; offset++){
				if ( types[ offset ].description.equals( licenceType) ){
					index = offset;
				}
			}
			
			licenceTypeCB = new JComboBox( types );
			licenceTypeCB.setEnabled(true);
			licenceTypeCB.setSelectedIndex( index );
			
			licenceTypeValid = !licenceType.equals(TYPE_UNDEFINED);
			
			if ((licenceType != null) && (licenceType.length() > 0)) {
				getLicenceIssueNumberTF().setEnabled( licenceType.substring(0,1).equals(TYPE_FULL) | licenceType.substring(0,1).equals(TYPE_PROV));
			}

			licenceTypeCB.addActionListener( new LicenceTypeListener() );
		}
		return licenceTypeCB;
	}
	
	/**
	 * Returns a JTextField, possibly populated with a licence issue number
	 * 
	 * @return	A JTextField
	 * 
	 * NOTE: need to get the issue number from the record. Need to know how to do that.
	 */
	private JTextField getLicenceIssueNumberTF(){
		if ( licenceIssueNumberTF == null){
			issueNumberChanged = false;
			
			Document doc = DocumentFactory
					.newDocument(new Capability[] { Capability.limitedText(2), Capability.numeric() });
			DefendantOnCaseValue dc = updateDefendantModel.getDefendantOnCaseValue();
			
			licenceIssueNumberTF = JTextFieldFactory.getTextField(doc);
			
			licenceIssueNumberTF.setColumns(4);
			licenceIssueNumberTF.setEnabled( true );
			String issueNumber = "";
			
			if ( dc != null){
				String licenceType = dc == null ? "" : dc.getLicenceType();
				hiddenIssueNumber = tidyUp(dc.getIssueNumber());
				
				if ( licenceType != null ) {
					if ( licenceType.substring(0,1).equals(TYPE_PROV) || licenceType.substring(0,1).equals(TYPE_FULL)){
						issueNumber = hiddenIssueNumber;
					}
				}
				
				issueNumberValid = ( issueNumber.length() > 0 );
			}
			
			licenceIssueNumberTF.setText(formatLicenceIssueNumber(issueNumber));
			
			loading = false;
			
			//	Add a listener here:
			licenceIssueNumberTF.getDocument().addDocumentListener( new DocumentListener() {
				
				@Override public void removeUpdate(DocumentEvent e) {
					check();
				}				
				
				@Override public void insertUpdate(DocumentEvent e) {
					check();
				}
				
				@Override public void changedUpdate(DocumentEvent e) {
					check();
				}
				
				/**
				 * Check the contents of the issue number text box to see if it is 'valid' 
				 * (exactly two characters)
				 */
				private void check(){
					if (!loading){
						String value = licenceIssueNumberTF.getText();
						
						//	This field is mandatory and must be 1 or more digits
						if ( value.length() > 0 ){
							issueNumberValid = true;
						}
						else{
							issueNumberValid = false;
						}
						
						hiddenIssueNumber = value;
						issueNumberChanged = true;
					}

					checkAllChanged();
				}
			});
		}
		
		return licenceIssueNumberTF;
	}
	
	/**
	 * Returns a JTextField that only accepts a limited number of numbers.
	 * 
	 * @return JTextField
	 */
	private JTextField getNoOfTicsTF() {
		if (noOfTicsTF == null) {
			DefendantOnCaseBasicValue dcbv = defendantValue.getDefOnCaseBasicValue();
			Integer tics = dcbv.getNoOfTICs();

			Document doc = DocumentFactory
					.newDocument(new Capability[] { Capability.limitedText(4), Capability.numeric() });
			noOfTicsTF = JTextFieldFactory.getTextField(doc);
			noOfTicsTF.setText(tics == null ? "" : tidyUp(tics.toString()));
			noOfTicsTF.setColumns(6);
			noOfTicsTF.setEnabled(true);

			noOfTicsTF.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					noTicsChanged = true;
					
					checkAllChanged();
				}
			});

			noTicsChanged = false;
			log.debug("set TICs number to " + noOfTicsTF.getText());
		}
		return noOfTicsTF;
	}

	private XDatePanel getDateOfBirth() {
		if (dateOfBirth == null) {
			Calendar dob = null;
			if (defendantValue.getDateOfBirth() != null) {
				dob = defendantValue.getDateOfBirth();
			}
			dateOfBirth = new XDatePanel(null, dob);
			dateOfBirth.setRequired(false);

			dateOfBirth.getDateComponent().getDisplay().addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					enableOkButton();
				}
			});

			dateOfBirth.getDateComponent().addMChangeListener(new MChangeListener() {
				public void valueChanged(MChangeEvent e) {

					if (e.getType() == MChangeEvent.PULLDOWN_OPENED || e.getType() == MChangeEvent.PULLDOWN_CLOSED) {
						return;
					}

					enableOkButton();
				}
			});
		}
		return dateOfBirth;
	}

	private JRadioButton getMaleRb() {
		if (maleRb == null) {
			maleRb = new JRadioButton(getResource("male"), defendantValue.getGender() == null ? false
					: defendantValue.getGender().equals(DefendantValue.GENDER_MALE));
			maleRb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enableOkButton();
				}
			});
		}
		return maleRb;
	}

	private JRadioButton getFemaleRB() {
		if (femaleRB == null) {
			femaleRB = new JRadioButton(getResource("female"), defendantValue.getGender() == null ? false
					: defendantValue.getGender().equals(DefendantValue.GENDER_FEMALE));
			femaleRB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enableOkButton();
				}
			});
		}
		return femaleRB;
	}

	private JRadioButton getCompanyRb() {
		if (companyRB == null) {
			companyRB = new JRadioButton(getResource("company"), defendantValue.getGender() == null ? false
					: defendantValue.getGender().equals(DefendantValue.GENDER_COMPANY));
			companyRB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enableOkButton();
				}
			});
		}
		return companyRB;
	}

	private JRadioButton getJuvenileYes() {
		if (juvenileYes == null) {
			juvenileYes = new JRadioButton(getResource("yes"), defendantValue.getIsJuvenile() == null ? false
					: defendantValue.getIsJuvenile().equalsIgnoreCase(DefendantValue.IS_JUVENILE_TRUE));
			juvenileYes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enableOkButton();
				}
			});
		}

		return juvenileYes;
	}

	private JRadioButton getJuvenileNo() {
		if (juvenileNo == null) {
			juvenileNo = new JRadioButton(getResource("no"), defendantValue.getIsJuvenile() == null ? false
					: defendantValue.getIsJuvenile().equalsIgnoreCase(DefendantValue.IS_JUVENILE_FALSE));
			juvenileNo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					enableOkButton();
				}
			});
		}
		return juvenileNo;
	}

	private JRadioButton getMaskedYes() {
		if (maskedYes == null) {
			maskedYes = new JRadioButton(getResource("yes"),
					defendantValue.getDefOnCaseBasicValue().getIsMasked() == null ? false
							: defendantValue.getDefOnCaseBasicValue().getIsMasked()
									.equalsIgnoreCase(DefendantValue.IS_MASKED_TRUE));
			maskedYes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					maskedNameJTextField.setEnabled(true);
					enableOkButton();
				}
			});
		}
		return maskedYes;
	}

	private JRadioButton getMaskedNo() {
		if (maskedNo == null) {
			maskedNo = new JRadioButton(getResource("no"),
					defendantValue.getDefOnCaseBasicValue().getIsMasked() == null ? false
							: defendantValue.getDefOnCaseBasicValue().getIsMasked()
									.equalsIgnoreCase(DefendantValue.IS_MASKED_FALSE));
			maskedNo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					maskedNameJTextField.setEnabled(false);
					enableOkButton();
				}
			});
		}
		return maskedNo;
	}

	private JTextField getMaskedNameJTextField() {
		if (maskedNameJTextField == null) {
			maskedNameJTextField = new JTextField();
			maskedNameJTextField.setText(defendantValue.getMaskedName() == null ? "" : defendantValue.getMaskedName());
			maskedNameJTextField.setColumns(25);
			maskedNameJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					enableOkButton();
				}
			});
		}
		return maskedNameJTextField;
	}

	private XDatePanel getLastConvDate() {
		if (lastConvDate == null) {
			Calendar lcd = null;
			if (defendantValue.getLastConvictionDate() != null) {
				lcd = defendantValue.getLastConvictionDate();
			}
			lastConvDate = new XDatePanel(null, lcd);
			lastConvDate.setRequired(false);

			lastConvDate.getDateComponent().getDisplay().addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					enableOkButton();
				}
			});

			lastConvDate.getDateComponent().addMChangeListener(new MChangeListener() {
				public void valueChanged(MChangeEvent e) {

					if (e.getType() == MChangeEvent.PULLDOWN_OPENED || e.getType() == MChangeEvent.PULLDOWN_CLOSED) {
						return;
					}

					enableOkButton();
				}
			});
		}
		return lastConvDate;
	}

	private JComboBox getHateCrimeComboBox() throws CSRecoverableException {

		if (colHateCodes == null) {
			colHateCodes = getHateCodes();
			hateCodesModel = new DefaultComboBoxModel(colHateCodes.toArray());

		}

		if (hateCrimeComboBox == null) {
			hateCrimeComboBox = new JComboBox(hateCodesModel);
			ComboBoxRenderer renderer = new ComboBoxRenderer();
			renderer.setPreferredSize(new Dimension(250, 20));
			hateCrimeComboBox.setRenderer(renderer);
			hateCrimeComboBox.setMinimumSize(new Dimension(200, 20));
			// Check if Hate Type already set.
			// Use to set start combo selection
			if (defendantValue.getHateType() != null) {
				Iterator it = colHateCodes.iterator();
				while (it.hasNext()) {
					refSystemCodeValue = (RefSystemCodeBasicValue) it.next();
					if (defendantValue.getHateType().equals(refSystemCodeValue.getCode())) {
						hateCrimeComboBox.setSelectedItem(refSystemCodeValue);
						break;
					}
				}
			}
			hateCrimeComboBox.addActionListener(new XAction() {
				private static final long serialVersionUID = 1L;

				public void xActionPerformed(ActionEvent ae) {
					try {
						stepUpdateViewState();
					} catch (CSRecoverableException csre) {
						XHIBITErrorHandler.handleError(csre);
					}
					enableOkButton();
				}
			});
		}

		if (getHateCrimeCheckBox().isSelected()) {
			hateCrimeComboBox.setEnabled(true);
		} else {
			hateCrimeComboBox.setEnabled(false);
		}
		return hateCrimeComboBox;
	}

	/**
	 * Gets the resource string from the resource bundle using the given key.
	 * 
	 * @param key
	 *            the key to lookup in the resource bundle.
	 * @return the resource string.
	 */
	private String getResource(final String key) {
		return ResourceBundleHelper.getResource(XhibitBundles.UpdateDefendant, key);
	}

	/**
	 * Gets the error resource string from the resource bundle using the given
	 * key.
	 * 
	 * @param key
	 *            the key to lookup in the error resource bundle.
	 * @return the error resource string.
	 */
	private String getErrorResource(final String key) {
		return ResourceBundleHelper.getResource(XhibitBundles.ErrorText, key);
	}

	public void stepInitialise() throws CSRecoverableException {
		log.debug("stepInitialize()");
	}

	public void stepActivate() {
		log.debug("stepActivate()");
		moveModelToScreen();

		try {
			stepUpdateViewState();
		} catch (CSRecoverableException csre) {
			XHIBITErrorHandler.handleError(csre);
		}
	}

	private void moveModelToScreen() {
		// Not strictly a screen widget, this is the ID that corresponds to
		// the collecting magistrate's court name
		if (updateDefendantModel.getDefendantOnCaseValue() != null
				&& updateDefendantModel.getDefendantOnCaseValue().getDefendantOnCaseBVO() != null) {
		}
	}

	/**
	 * Life-cycle method to enable/disable screen components
	 */
	public void stepUpdateViewState() throws CSRecoverableException {
		log.debug("stepUpdateViewState()");
		boolean isEditable = xac.getApplicationCaseModel() != null
				&& xac.getApplicationCaseModel().isInEditMode(FunctionList.EDefendant);

		getFirstNameTf().setEnabled(isEditable);
		getMiddleTf().setEnabled(isEditable);
		getInitialTf().setEnabled(isEditable);
		getLastNameTf().setEnabled(isEditable);
		getAddressTf1().setEnabled(isEditable);
		getAddressTf2().setEnabled(isEditable);
		getAddressTf3().setEnabled(isEditable);
		getAddressTf4().setEnabled(isEditable);
		getTownTf().setEnabled(isEditable);

		if (!isCrestFormAFieldsOnly() && updateDefendantModel.isShowPublicDisplayHidingControls()) {
			this.getHideDefendantInThisCaseRB().setEnabled(isEditable);
			this.getHideDefendantInAllCasesRB().setEnabled(isEditable);
			this.getHideDefendantNoneRB().setEnabled(isEditable);
		}

		if (isMiscellaneousAppealCase()) {
			// for misc appeal case fields are displayed but greyed out
			getAsn().setEnabled(false);
			getPtiurn().setEditable(false);
			// don't want to display the triangle if the field is disabled ie.
			// if can't ammend any errors
			getAsnWarnTriangle().setVisible(false);
			getPtiurnWarnTriangle().setVisible(false);
		} else {
			log.debug("is not a Miscallaneous Appeal Case");
			getHateCrimeCheckBox().setEnabled(true);
			// getHateCrimeComboBox().setEnabled(true);
			getPtiurn().setEnabled(isEditable);
			getAsn().setEnabled(isEditable);
			log.debug(" isEditable " + isEditable);
			if (isEditable)// only want warning triangles displayed if can edit
							// fields
			{
				if (updateDefendantModel.getDefendantValue() != null
						&& updateDefendantModel.getDefendantValue().getDefOnCaseBasicValue() != null) {
					if (!ptiurnModified) {
						try {
							DefendantOnCaseBasicValue defOnCaseBasicVal = updateDefendantModel.getDefendantValue()
									.getDefOnCaseBasicValue();
							ptiurnHelper.validatePtiurn(defOnCaseBasicVal.getPtiurn());
						} catch (CSValidationException e) {
							log.debug("stepUpdateViewState(): failed validation of ptiurn");
							getPtiurnWarnTriangle().setVisible(true);
						}
					} else {
						// remove warning triangle as soon as ptiurn field is
						// modified
						getPtiurnWarnTriangle().setVisible(false);
					}
					if (!asnModified) {
						try {
							DefendantOnCaseBasicValue defOnCaseBasicVal = updateDefendantModel.getDefendantValue()
									.getDefOnCaseBasicValue();
							asnHelper.validateAsn(defOnCaseBasicVal.getAsn());
						} catch (CSValidationException e) {
							getAsnWarnTriangle().setVisible(true);
						}
					} else {
						// remove warning triangle as soon as asn field is
						// modified
						getAsnWarnTriangle().setVisible(false);
					}
				}
			} else {
				// turn triangle off
				getPtiurnWarnTriangle().setVisible(false);
				getAsnWarnTriangle().setVisible(false);

			}
		}

		getCountyTf().setEnabled(isEditable);
		getPostCodeTf().setEnabled(isEditable);

		// PR6156: the new nationality selector requires setNationalityEnabled
		// to be
		// called instead of setEnabled.
		// getNationalityCB().setEnabled(isEditable);
		getNationalityCB().setNationalityEnabled(isEditable);

		getDateOfBirth().setDateEnabled(isEditable);
		getMaleRb().setEnabled(isEditable);
		getFemaleRB().setEnabled(isEditable);
		getCompanyRb().setEnabled(isEditable);
		getMaskedNameJTextField().setEnabled(isEditable);
 		getMaskedNo().setEnabled(isEditable);
 		if (getMaskedNo().isSelected()) {
 			maskedNameJTextField.setEnabled(false);
 		}
		getMaskedYes().setEnabled(isEditable);
		getNoOfTicsTF().setEnabled(isEditable);
		getDriverNoTF().setEnabled(isEditable);
		// X54142, ensure that the juvenile radio buttons are disabled
		// getJuvenileNo( ).setEnabled( isEditable );
		// getJuvenileYes( ).setEnabled( isEditable );
		getLastConvDate().setDateEnabled(isEditable);
		buttonPanel.okButton.setEnabled(asnModified || ptiurnModified);

		// hate crime fields not available for appeal cases
		if (parent.getShv().getCaseType().equals("A")) {
			getHateCrimeCheckBox().setEnabled(false);
			getHateCrimeComboBox().setEnabled(false);
		}

		getHateCrimeCheckBox().setEnabled(isEditable);
		// set state of hate crime combo box dependent on check box status
		if (getHateCrimeCheckBox().isSelected()) {
			getHateCrimeComboBox().setEnabled(isEditable);
		} else {
			getHateCrimeComboBox().setEnabled(false);
		}

	}

	/**
	 * Return null if the string is empty else return the string.
	 * 
	 * @param text
	 * @return The string passed in or null
	 */
	private String nullValue(String text) {
		if (text == null || text.equals("")) {
			return null;
		} else {
			return text;
		}
	}

	public void stepValidate() throws CSValidationException, CSRecoverableException {
		log.debug("stepValidate()");

		// ?put the values in the udm defendant value
		// and the defendant on case value
		DefendantValue dv = updateDefendantModel.getDefendantValue();

		// Trim all aspects of name as extra spaces at beginning prevent correct
		// ordering on public display.
		dv.setFirstName(nullValue(getFirstNameTf().getText().trim()));
		dv.setMiddleName(nullValue(getMiddleTf().getText().trim()));

		// Last name is mandatory
		if (getLastNameTf().getText().trim().length() == 0) {
			throw new CSValidationException("gui.updateDefendantDialog.surnameIsMandatory",
					"gui.updateDefendantDialog.surnameIsMandatory: surname is mandatory");
		}
		dv.setSurName(nullValue(getLastNameTf().getText().trim()));

		dv.setInitials(nullValue(getInitialTf().getText().trim()));

		// X54613 - Check if there's an AddressValue available in the
		// DefendantValue.
		// If not, create one first and then populate it.
		if (dv.getAddressValue() == null) {
			dv.setAddressValue(new AddressValue());
		}

		dv.getAddressValue().setAddress1(nullValue(getAddressTf1().getText()));
		dv.getAddressValue().setAddress2(nullValue(getAddressTf2().getText()));
		dv.getAddressValue().setAddress3(nullValue(getAddressTf3().getText()));
		dv.getAddressValue().setAddress4(nullValue(getAddressTf4().getText()));
		StringBuffer messageBuffer = new StringBuffer();
		StringBuffer titleBuffer = new StringBuffer();

		if (!isCrestFormAFieldsOnly()) {
			// Inform the user if the post code has an invalid format
			if (getPostCodeTf().getText().length() > 0) {
				if (!isValidPostCodeFormat(getPostCodeTf().getText())) {
					messageBuffer.append(getErrorResource("gui.updateDefendantDialog.postCode.format"));

					titleBuffer.append(getErrorResource("gui.updateDefendantDialog.postCode.title"));
				}
			}
			if (!isValidDCAStringFormat(getFirstNameTf().getText()) || !isValidDCAStringFormat(getInitialTf().getText())
					|| !isValidDCAStringFormat(getLastNameTf().getText())
					|| !isValidDCAStringFormat(getMiddleTf().getText())) {
				messageBuffer.append((messageBuffer.length() > 0) ? COMMA_SEPERATOR : EMPTY_SEPERATOR);
				titleBuffer.append((titleBuffer.length() > 0) ? COMMA_SEPERATOR : EMPTY_SEPERATOR);

				messageBuffer.append(getErrorResource("gui.updateDefendantDialog.name.format"));
				titleBuffer.append(getErrorResource("gui.updateDefendantDialog.name.title"));
			}

			if (asnModified) {
				try {
					asnHelper.validateAsn(getAsn().getText());// throws
																// CSValidationException
				} catch (CSValidationException e) {
					getAsn().requestFocus();
					throw e;
				}
			}

			if (ptiurnModified) {
				try {
					ptiurnHelper.validatePtiurn(getPtiurn().getText());// throws
																		// CSValidationException
				} catch (CSValidationException e) {
					getPtiurn().requestFocus();
					throw e;
				}
			}

			dv.setAsn(nullValue(getAsn().getText()));
			dv.setPtiurn(nullValue(getPtiurn().getText()));
			if (getHateCrimeCheckBox().isSelected()) {
				dv.setHateIndicator("Y");
				String hateCode = ((RefSystemCodeBasicValue) getHateCrimeComboBox().getSelectedItem()).getCode();
				dv.setHateType(hateCode);
			} else {
				dv.setHateIndicator("N");
				dv.setHateType("");
			}

		}

		if (messageBuffer.length() > 0) {
			JOptionPane.showMessageDialog(this,
					MessageFormat.format(getErrorResource("gui.updateDefendantDialog.warning.format"),
							new Object[] { messageBuffer.toString() }),
					MessageFormat.format(getErrorResource("gui.updateDefendantDialog.warning.title"),
							new Object[] { titleBuffer.toString() }),
					JOptionPane.WARNING_MESSAGE);
		}

		dv.getAddressValue().setPostcode(nullValue(getPostCodeTf().getText()));
		dv.getAddressValue().setTown(nullValue(getTownTf().getText()));
		dv.getAddressValue().setCounty(nullValue(getCountyTf().getText()));
		dv.getDefOnCaseBasicValue().setNationality(nullValue(getNationalityCB().getNationality()));

		if (!isCrestFormAFieldsOnly() && updateDefendantModel.isShowPublicDisplayHidingControls()) {
			dv.setHideDefendantInCallCases(
					hideDefendantButtonGroup.isSelected(getHideDefendantInAllCasesRB().getModel()));
			dv.getDefOnCaseBasicValue().setHideDefendantInThisCase(
					hideDefendantButtonGroup.isSelected(getHideDefendantInThisCaseRB().getModel()));
		}

		if (juvenileRBGroup != null) {
			if (juvenileRBGroup.isSelected(juvenileYes.getModel())) {
				dv.getDefOnCaseBasicValue().setIsJuvenile(DefendantValue.IS_JUVENILE_TRUE);
			} else if (juvenileRBGroup.isSelected(juvenileNo.getModel())) {
				dv.getDefOnCaseBasicValue().setIsJuvenile(DefendantValue.IS_JUVENILE_FALSE);
			}
		}
		if (maskedRBGroup != null) {
			if (maskedRBGroup.isSelected(maskedYes.getModel())) {
				dv.getDefOnCaseBasicValue().setIsMasked(DefendantValue.IS_MASKED_TRUE);
			} else if (maskedRBGroup.isSelected(maskedNo.getModel())) {
				dv.getDefOnCaseBasicValue().setIsMasked(DefendantValue.IS_MASKED_FALSE);
			}
		}
		if (sexRadioButtons != null) {
			if (sexRadioButtons.isSelected(maleRb.getModel())) {
				dv.setGender(DefendantValue.GENDER_MALE);
			} else if (sexRadioButtons.isSelected(femaleRB.getModel())) {
				dv.setGender(DefendantValue.GENDER_FEMALE);
			} else if (sexRadioButtons.isSelected(getCompanyRb().getModel())) {
				dv.setGender(DefendantValue.GENDER_COMPANY);
			}
		}

		if (this.maskedNameJTextField != null)
			dv.getDefOnCaseBasicValue().setMaskedName(nullValue(this.maskedNameJTextField.getText()));

		// DOB must be before the current date
		if (dateOfBirth != null) {
			if (dateOfBirth.getDate() != null && isDateInTheFuture(dateOfBirth.getDate())) {
				dateOfBirth.getDateComponent().requestFocus();

				throw new CSValidationException("gui.updateDefendantDialog.dobInTheFuture",
						"gui.updateDefendantDialog.dobInTheFuture: DOB in the future");
			}
			dv.setDateOfBirth(dateOfBirth.getDate());
		}

		// Last conviction date must be before the current date
		if (lastConvDate != null) {
			if (lastConvDate.getDate() != null && isDateInTheFuture(lastConvDate.getDate())) {
				lastConvDate.getDateComponent().requestFocus();
				throw new CSValidationException("gui.updateDefendantDialog.lastConvDateInTheFuture",
						"gui.updateDefendantDialog.lastConvDateInTheFuture: Last Conv date in the future");
			}
			
			dv.setLastConvictionDate(lastConvDate.getDate());
		}

		LicenceType licenceType = (LicenceType)getLicenceTypeCB().getSelectedItem();
		
		if ( licenceType != null && (licenceType.type.equals(TYPE_PROV) || licenceType.type.equals(TYPE_FULL)))
		{
			// Drivers license number must be 16 characters long
			// Note: Only validation of the drivers' license number is performed
			if (!validateDriverNumber(getDriverNoTF().getText().trim())) {
				getDriverNoTF().requestFocus();
				throw new CSValidationException("gui.updateDefendantDialog.invalidDriverLicence", "Invalid driver licence number entered");
			}
		}
	}

	/**
	 * Validate the driver number against the standard DVLA format
	 * 
	 * @param 		driverNumber				The driver number
	 * 
	 * @note		Driver number format:
	 * 					1 - 5:				The first five characters of the driver's name (Padded with 9's if less than 5 characters)
	 * 					6					The decade digit from drivers' DOB (1986 -> 8)
	 * 					7 - 8				The month of birth (1-12 for male, 51-62 for female)
	 * 					9 - 10				The date within the month
	 * 					11					The year digit from driver's DOB (1986 - 6)
	 * 					12 - 13				The first two initials of the driver's names, padded with '9' if no second name
	 * 					14					Arbitrary digit, usually 9, but decremented by 1 where other driver number match to this point.)
	 * 					15 - 16				Two computer check digits
	 * 
	 * @return		true if number is valid
	 */
	public static boolean validateDriverNumber( final String driverNumber ){
		boolean outcome = driverNumber != null && driverNumber.trim().length() == DRIVER_LIC_NO_OF_CHARS;
		
		if ( outcome ){
			//	Check the validity of the driver number.
			String pattern = "[A-Z,0-9]{5}[0-9][0,1,5,6][0-9]([0][1-9]|[1-2][0-9]|[3][0,1])[0-9][A-Z,0-9]{3}[A-Z]{2}";
			
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(driverNumber.trim().toUpperCase());

            outcome = m.matches();
		}
		
		return outcome;
	}
	/**
	 * Determines whether or not the data matches the expression
	 * 
	 * @param expression
	 *            - the regular expression to check the data against
	 * @param data
	 *            - the data to be checked
	 * @return true - if the data is valid for the expression
	 */
	private boolean isDataMatchesRegularExpression(String expression, String data) {
		boolean matched = false;

		try {
			RE regexp = new RE(expression);
			matched = regexp.match(data);
		} catch (RESyntaxException e) {
			matched = false;
		}

		return matched;
	}

	private boolean isDateInTheFuture(Calendar userEnteredDate) {
		return userEnteredDate.after(Calendar.getInstance());
	}

	private Collection<RefSystemCodeBasicValue> getHateCodes() throws CSRecoverableException {

		Integer courtId = XhibitSingleton.getInstance().getCourtId();

		RefSystemCodeCriteria rcc = new RefSystemCodeCriteria();
		rcc.setCourtId(courtId.toString());
		rcc.setCodeType("HATE_TYPE");
		Collection<RefSystemCodeBasicValue> colHCodes = getBizDelegate().findSystemCodes(rcc);
		if (colHCodes instanceof List) {
			List<RefSystemCodeBasicValue> list = (List<RefSystemCodeBasicValue>) colHCodes;
			Sorter.sort(list, new String[] { "code" });
		}

		return colHCodes;
	}

	public BisRefControllerBeanBusinessDelegate getBizDelegate() {
		return XhibitDelegateHelper.getBizRefDelegate();
	}

	public void stepDeactivate() {
		log.debug("stepDeactivate()");
	}

	/**
	 * Life-cycle method to manage the update of the defendant details.
	 * 
	 * @param update
	 * @throws CSRecoverableException
	 */
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		log.debug("stepDeinitialise() with update flag: " + update);

		updateDefendantModel.setUpdate(update);

		if (update) {
			updateDefendant();
			try {
				xac.reloadApplicationCaseModel();

				if (xac.getBodyPanel() instanceof CourtLogController) {
					log.debug("The CourtLog is showing and will now be updated to reflect any defendant changes");
					((CourtLogController) xac.getBodyPanel()).loadCourtLogHeaderTableModel();
					((CourtLogController) xac.getBodyPanel()).stepActivate();
					log.debug("The CourtLog has been refreshed");
				}
			} catch (Exception dce) {
				System.err.println("Trouble updating the currently open window(s) with the defendant changes");
			}
		}
	}

	private boolean isStateUnChangedExcludingHideInPublicDisplayValues(DefendantValue dv1, DefendantValue dv2) {
		DefendantValue copy1 = new DefendantValue(dv1);
		DefendantValue copy2 = new DefendantValue(dv2);
		copy1.setHideDefendantInCallCases(false);
		copy2.setHideDefendantInCallCases(false);
		if (copy1.getDefOnCaseBasicValue() != null) {
			copy1.getDefOnCaseBasicValue().setHideDefendantInThisCase(false);
		}
		if (copy2.getDefOnCaseBasicValue() != null) {
			copy2.getDefOnCaseBasicValue().setHideDefendantInThisCase(false);
		}
		return isStateUnChanged(copy1, copy2);
	}

	/**
	 * stateUnChanged method relies on the CSAbstractValue.toString method which
	 * uses reflection to print all the fields of the derived class, that have a
	 * getter method (i.e. methods that start get*). If new member variables are
	 * added to or old member variables are deleted from the value objects then
	 * this method should still work. This method is not called often and does
	 * not have to perform quickly, hence the toString calls should be ok.
	 * 
	 * @param dv1
	 * @param dv2
	 * @return true if dv1 and dv2 are value equivalent
	 */
	private boolean isStateUnChanged(DefendantValue dv1, DefendantValue dv2) {
		log.debug("the defendant was    : " + dv1.toString());
		log.debug("the defendant is now : " + dv2.toString());

		if (!dv1.toString().equals(dv2.toString())) {
			return false;
		}

		AddressValue av1 = dv1.getAddressValue();
		AddressValue av2 = dv2.getAddressValue();

		if ((av1 == null && av2 != null) || (av1 != null && av2 == null)) {
			return false;
		}

		if (av1 != null && av2 != null && !isStateUnChanged(av1, av2)) {
			return false;
		}

		DefendantOnCaseBasicValue doc1 = dv1.getDefOnCaseBasicValue();
		DefendantOnCaseBasicValue doc2 = dv2.getDefOnCaseBasicValue();

		if ((doc1 == null && doc2 != null) || (doc1 != null && doc2 == null)) {
			return false;
		}

		if (doc1 != null && doc2 != null && !isStateUnChanged(doc1, doc2)) {
			log.debug("Defendant on Case 1 does not match Defendant on Case 2");
			return false;
		}

		Calendar cal1 = dv1.getDateOfBirth();
		Calendar cal2 = dv2.getDateOfBirth();

		if (cal1 != null)
			log.debug("dv1 dob: " + cal1.toString());

		if (cal2 != null)
			log.debug("dv2 dob: " + cal2.toString());

		if ((cal1 == null && cal2 != null) || (cal1 != null && cal2 == null)) {
			log.debug("one dv dob is null, one is not");
			return false;
		}

		if (cal1 != null && cal2 != null && cal1.getTimeInMillis() != cal2.getTimeInMillis()) {
			log.debug("DateOfBirth1 != DateOfBirth2");
			return false;
		}

		Calendar cal3 = dv1.getLastConvictionDate();
		Calendar cal4 = dv2.getLastConvictionDate();

		if (cal3 != null)
			log.debug("dv1 lastonvdate: " + cal3.toString());

		if (cal4 != null)
			log.debug("dv2 lastconvdate: " + cal4.toString());

		if ((cal3 == null && cal4 != null) || (cal3 != null && cal4 == null)) {
			log.debug("one dv lastconvdate is null, one is not");
			return false;
		}

		if (cal3 != null && cal4 != null && cal3.getTimeInMillis() != cal4.getTimeInMillis()) {
			log.debug("LastConvDate1 != LastConvDate2");
			return false;
		}

		log.debug("returning true");
		return true;
	}

	private boolean isStateUnChanged(AddressValue av1, AddressValue av2) {
		log.debug("the address   was    : " + av1.toString());
		log.debug("the address is now   : " + av2.toString());

		return av1.toString().equals(av2.toString());
	}

	private boolean isStateUnChanged(DefendantOnCaseBasicValue doc1, DefendantOnCaseBasicValue doc2) {
		log.debug("the defoncase was    : " + doc1.toString());
		log.debug("the defoncase is now : " + doc2.toString());

		return doc1.toString().equals(doc2.toString());
	}

	/**
	 * Updates the defendant details. The update can take to forms, one for
	 * CREAT Form A updates and the other for standard defendant details.
	 * 
	 * @throws CSRecoverableException
	 */
	private void updateDefendant() throws CSRecoverableException {
		ChargeControllerBeanBusinessDelegate chargesControllerDelegate = XhibitDelegateHelper.getChargeDelegate();

		CaseStatusValue caseStatusValue = null;

		try {
			log.debug("UpdateDefendant(): id of the defendant to store is: " + updateDefendantModel.getDefendantID());
			log.debug("UpdateDefendant(): id of the related case is: "
					+ updateDefendantModel.getCaseStatusValue().getCaseID());
			log.debug("UpdateDefendant(): in court boolean of case is: "
					+ updateDefendantModel.getCaseStatusValue().isInCourt());
			log.debug("UpdateDefendant(): DefendantValue is: " + updateDefendantModel.getDefendantValue().toString());

			defendantValue = updateDefendantModel.getDefendantValue();
			if (defendantValue == null)
				log.error("UpdateDefendant(): DefendantValue is null for id " + updateDefendantModel.getDefendantID());
			else
				log.debug("UpdateDefendant(): DefendantValue firstName is  " + defendantValue.getFirstName());

			caseStatusValue = updateDefendantModel.getCaseStatusValue();
			if (caseStatusValue == null)
				log.error("UpdateDefendant(): caseStatusValue is null!");
			else
				log.debug("UpdateDefendant(): caseStatusValue is: "
						+ updateDefendantModel.getCaseStatusValue().toString());
		} catch (Exception dce) {
			CSRecoverableException csre = new CSRecoverableException(
					"gui.updateDefendantActtion.defendantvaluecreation",
					"Exception whilst creating the DefendantValue object", dce);
			XHIBITErrorHandler.handleError(csre);
		}

		if (!isCrestFormAFieldsOnly()) {
			// Standard defendant update screen, i.e. non-CREST Form A
			if (isStateUnChanged(initialDefendantValue, defendantValue)) {
				return;
			}

			try {
				// Set the client time to make sure that the court log event set
				// with the client time rather than the server time.
				Calendar cal = Calendar.getInstance();
				defendantValue.setUpdateTime(cal);				
				
				
				
				if (isStateUnChangedExcludingHideInPublicDisplayValues(initialDefendantValue, defendantValue)) {
					chargesControllerDelegate.updateDefendant(defendantValue, caseStatusValue,
							XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
					// getBusinessDelegate().updatePublicDisplayHideSettings(defendantValue,
					// caseStatusValue);
				} else {
					chargesControllerDelegate.updateDefendant(defendantValue, caseStatusValue,
							XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
					// getBusinessDelegate().updateDefendant(defendantValue,
					// caseStatusValue);
				}
				
				// update the model so can display on calling screen (form a uses  this version of screen!)
				this.updateDefendantModel.getDefendantValue().setLastConvictionDate(defendantValue.getLastConvictionDate());
				
			} catch (Exception dce) {
				CSRecoverableException csre = new CSRecoverableException(
						"gui.updateDefendantActtion.defendantvalueupdate",
						"Exception whilst saving the DefendantValue object to the mid tier", dce);
				XHIBITErrorHandler.handleError(csre);
			}
		} else {
			// the second scenario of update defendant, the crest form
			// related fields only.
			// DefendantOnCaseBasicValue basVal =
			// updateDefendantModel.getDefendantValue().getDefOnCaseBasicValue();
			DefendantOnCaseValue val = new DefendantOnCaseValue();
			val.setDefendantId(updateDefendantModel.getDefendantID());
			val.setDefendantOnCaseBVO(updateDefendantModel.getDefendantValue().getDefOnCaseBasicValue());
			val.setDriverNumber(getDriverNoTF().getText());			
			
			LicenceType licenceType = (LicenceType)getLicenceTypeCB().getSelectedItem();
			if ( licenceType != null ){
				if ( licenceType.type != TYPE_UNDEFINED ){
					val.setLicenceType(licenceType.description);
				}
			}
			
			String issueNumber = getLicenceIssueNumberTF().getText();
			if ( issueNumber != null ) {
				val.setIssueNumber(formatLicenceIssueNumber(issueNumber));
			}
			
			String nationality = getNationalityCB().getNationality();

			if (nationality != null && nationality.length() >= 3) {
				val.getDefendantOnCaseBVO().setNationality(nationality.substring(0, 3));
			} else {
				val.getDefendantOnCaseBVO().setNationality(null);
			}

			String noTicketsText = getNoOfTicsTF().getText();
			
			//	Handle blank "Number of Tickets" 
			if ( noTicketsText == null || noTicketsText.isEmpty()){
				noTicketsText = "0";
			}
			val.setNoOfTics(new Integer(noTicketsText));
			
			// update the model so can display on calling screen (form a)
			this.updateDefendantModel.getDefendantOnCaseValue().setDriverNumber(getDriverNoTF().getText());
			this.updateDefendantModel.getDefendantOnCaseValue().setNoOfTics(new Integer(noTicketsText));
			this.updateDefendantModel.getDefendantOnCaseValue().
				setLicenceType(((LicenceType)getLicenceTypeCB().getSelectedItem()).type);
			
			// Licence Issue Number will be 2 digits so add a leading 0 if needed
			this.updateDefendantModel.getDefendantOnCaseValue().setIssueNumber(formatLicenceIssueNumber(getLicenceIssueNumberTF().getText()));
			
			try {
				log.debug("the defendant on case to update is : " + val.toString());
				val.setCourtId(XhibitSingleton.getInstance().getCourtId());
				chargesControllerDelegate.updateDefendantOnCaseDetails(val, XhibitSingleton.getInstance()
						.getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
				// getBusinessDelegate()chargesController.updateDefendantOnCaseDetails(val,
				// XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
			} catch (Exception dce) {
				CSRecoverableException csre = new CSRecoverableException(
						"gui.updateDefendantActtion.defendantvalueupdate",
						"Exception whilst saving the DefendantValue object to the mid tier", dce);
				XHIBITErrorHandler.handleError(csre);
			}
		}
	}

	/**
	 * Obtains a reference to a defendant business delegate
	 * 
	 * @return DefendantControllerBusinessDelegate
	 */
	private DefendantControllerBeanBusinessDelegate getBusinessDelegate() {
		return XhibitDelegateHelper.getDefendantDelegate();
	}

	private String removeAllButLettersDigitsAndSpaces(String inString) {
		StringBuffer strbuf = null;
		int index = 0;
		boolean ready = false;

		if ((inString == null) || index == inString.length()) {
			strbuf = new StringBuffer(inString);
			ready = true;
		} else {
			strbuf = new StringBuffer();
			while (!ready) {
				char c = inString.charAt(index);

				if (Character.isLetterOrDigit(c) || Character.isSpaceChar(c)) {
					strbuf.append(c);
				}
				index++;
				if (index == inString.length())
					ready = true;
			}
		}
		return strbuf.toString();
	}

	/**
	 * Returns an empty string if the input value is null
	 * 
	 * @param value
	 * @return String
	 */
	private String tidyUp(String value) {
		return (value == null ? "" : value);
	}

	/**
	 * Validates that the post code conforms to one of the UK standard formats.
	 * Currently, these are:
	 * <ul>
	 * <li>AN NAA
	 * <li>ANN NAA
	 * <li>AAN NAA
	 * <li>AANN NAA
	 * <li>ANA NAA
	 * <li>AANA NAA
	 * </ul>
	 * Note too that the post code GIR 0AA is a special code that does not fit
	 * the standard.
	 * 
	 * @param param
	 *            - the post code to be validated
	 * @return - true if the post code format is valid otherwise false
	 */
	private boolean isValidPostCodeFormat(String param) {
		return isDataMatchesRegularExpression(REG_EXP_POSTCODE, param);
	}

	/**
	 * Validates that the (restricted) string conforms to that of the apd schema
	 * 
	 * @param param
	 *            - the string to be validated
	 * @return - true if the post code format is valid otherwise false
	 */
	private boolean isValidDCAStringFormat(String param) {
		log.debug("<<<>>> isValidDCAStringFormat - param " + param + "|");
		JakartaOroEvaluator jRE = new JakartaOroEvaluator();
		jRE.setExpression(REG_EXP_NAME);

		return (null != param) ? jRE.matches(param) : true;
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
	 * Creates a text field that only takes text which it will convert to
	 * uppercase
	 * 
	 * @param size
	 *            the number of characters that the text field will permit.
	 * @return the text field.
	 */
	private JTextField createUpperCaseJTextField(final int size) {
		// create the required capabilities (document decorators/validators)
		final Capability[] capabilities = new Capability[] { Capability.upperCase(), Capability.alphaNumeric(),
				Capability.limitedText(size) };

		// Create the text document with the required capabilities.
		final Document doc = DocumentFactory.newDocument(capabilities);

		// create the text field to return
		final JTextField textField = JTextFieldFactory.getTextField(doc);

		return textField;
	}

	private void Name_keyPressed(KeyEvent e) {
		enableOkButton();
		if (isValidChar(e.getKeyChar())) {
			e.consume();
		}
	}

	private void enableOkButton() {
		buttonPanel.okButton.setEnabled(true);
	}
	
	private void disableOkButton() {
		buttonPanel.okButton.setEnabled(false);
	}

	private boolean isValidChar(char text) {
		return (text == '<' || text == '>');
	}

	/**
	 * @return true if this is a miscallaneous appeal case
	 */
	boolean isMiscellaneousAppealCase() {
		boolean ret_val = false;
		try {
			if (parent.getShv() != null && XHIBITConstant.isMiscelleanousAppeal_CaseType(parent.getShv())) {
				ret_val = true;
			}
		} catch (UnknownCaseTypeException e) {
			ret_val = false;
		}
		return ret_val;
	}

	public boolean isStateChanged() {
		return !isStateUnChanged(initialDefendantValue, defendantValue);
	}

	class ComboBoxRenderer extends JLabel implements ListCellRenderer {

		private static final long serialVersionUID = 1L;

		public ComboBoxRenderer() {
			setOpaque(true);
			setHorizontalAlignment(CENTER);
			setVerticalAlignment(CENTER);
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			RefSystemCodeBasicValue refSystemCodeValue = (RefSystemCodeBasicValue) value;
			setHorizontalAlignment(LEFT);
			if (list.getSelectedValue() != null) {
				setText("HATE CRIME - " + refSystemCodeValue.getDecode());
				// setText(refSystemCodeValue.getCode() + " HATE CRIME - " +
				// refSystemCodeValue.getDecode());
			}
			return this;
		}
	}
}
