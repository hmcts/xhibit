package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import com.rsa.cryptoj.c.aJ.l;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.StringUtil;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefLegalRepresentativeBasicValue;
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

public class CounselDetailsPanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;
	private CounselDetailsModel model;
	private CounselDetailsDialog parentDialog;

	/**
	 * Fields on Counsel Details panel.
	 */
	private JLabel lblChamberRefNo = null;
	private JLabel lblSurname = null;
	private JLabel lblInitials = null;
	private JLabel lblFirstName = null;
	private JLabel lblOtherNames = null;
	private JLabel lblTitle = null;
	private JLabel lblQC = null;
	private JLabel lblHonours = null;
	private JLabel lblAdvocateType = null;
	private JLabel lblYearCalledToTheBar = null;
	private JLabel lblBarNumber = null;
	private JLabel lblDeleted = null;
	private XTextField txtChamberRefNo = null;
	private XTextField txtSurname = null;
	private XTextField txtInitials = null;
	private XTextField txtFirstName = null;
	private XTextField txtOtherNames = null;
	private XTextField txtTitle = null;
	private XTextField txtHonours = null;
	private XTextField txtYearCalledToTheBar = null;
	private XTextField txtBarNumber = null;
	private JCheckBox qcCheckbox = null;
	private JCheckBox deletedCheckbox = null;
	private JLabel lblManSurname = null;
	private JLabel lblManInitials = null;
	private JLabel lblFirstNameInvalidEntry = null;
	private JLabel lblOtherNamesInvalidEntry = null;
	private JLabel lblTitleInvalidEntry = null;
	private JLabel lblQCInvalidEntry = null;
	private JLabel lblHonoursInvalidEntry = null;
	private JLabel lblManAdvocateType = null;
	private JLabel lblYearCalledToTheBarInvalidEntry = null;
	private JLabel lblBarNumberInvalidEntry = null;
	private JLabel lblDeletedInvalidEntry = null;

	/**
	 * Fields on Advocate Type Radio Button panel.
	 */
	private JRadioButton advocateTypeSRbtn = null;
	private JRadioButton advocateTypeARbtn = null;

	/**
	 * Fields on Button panel.
	 */
	private JButton btnDelete = null;
	private JButton btnSave = null;
	private JButton btnCancel = null;

	/**
	 * JPanels.
	 */
	private JPanel mainPanel = null;
	private JPanel counselDetailsPanel = null;
	private JPanel advocateTypeRadioButtonPanel = null;

	// array of all the mandatory fields
	Vector<Object> mandatoryFields = new Vector<Object>();

	// Booleans
	private Boolean changesMade = false;

	/**
	 * Validators.
	 */
	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();

	public CounselDetailsPanel(CounselDetailsDialog parentDialog, CounselDetailsModel model)
			throws CSRecoverableException {
		this.model = model;
		this.parentDialog = parentDialog;
		stepInitialise();
		jbInit();
	}

	/**
	 * Initialises the look and feel of the panel.
	 */
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(700, 400));
		GridBagConstraints gbc = getGridBagLayout();

		mainPanel = getMainPanel();

		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weighty = 0.05;
		gbc.weightx = 0.95;
		gbc.fill = GridBagConstraints.BOTH;
		mainPanel.add(getCounselDetailsPanel(), gbc);

		CustomButtonPanel buttonPanel = (CustomButtonPanel) this.parentDialog.getButtonPanel();

		btnDelete = buttonPanel.addButton("CounselDetailsDelete", false, false);
		btnSave = buttonPanel.addButton("CounselDetailsSave", false, false);
		btnCancel = buttonPanel.addButton("CounselDetailsCancel", true, false);

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
			mainPanel.setPreferredSize(new Dimension(650, 350));
			this.add(scrollPane, gbc);
		}

		return mainPanel;
	}

	public JPanel getCounselDetailsPanel() {
		if (counselDetailsPanel == null) {
			counselDetailsPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			counselDetailsPanel.setLayout(new GridBagLayout());

			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			lblChamberRefNo = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"CounselDetails.chamberRefNoLabel"));
			counselDetailsPanel.add(lblChamberRefNo, gbc);
			gbc.gridy += 2;

			lblSurname = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "CounselDetails.surnameLabel"));
			counselDetailsPanel.add(lblSurname, gbc);
			gbc.gridy += 2;

			lblInitials = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"CounselDetails.initialsLabel"));
			counselDetailsPanel.add(lblInitials, gbc);
			gbc.gridy += 2;

			lblFirstName = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"CounselDetails.firstNameLabel"));
			counselDetailsPanel.add(lblFirstName, gbc);
			gbc.gridy += 2;

			lblOtherNames = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"CounselDetails.otherNamesLabel"));
			counselDetailsPanel.add(lblOtherNames, gbc);
			gbc.gridy += 2;

			lblTitle = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "CounselDetails.titleLabel"));
			counselDetailsPanel.add(lblTitle, gbc);
			gbc.gridy += 2;

			lblQC = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "CounselDetails.qcLabel"));
			counselDetailsPanel.add(lblQC, gbc);
			gbc.gridy += 2;

			lblHonours = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "CounselDetails.honoursLabel"));
			counselDetailsPanel.add(lblHonours, gbc);
			gbc.gridy += 2;

			lblAdvocateType = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"CounselDetails.advocateTypeLabel"));
			counselDetailsPanel.add(lblAdvocateType, gbc);

			/* Next Column */
			gbc.gridx = 1;
			gbc.gridy = 1;

			counselDetailsPanel.add(getChamberRefNo(), gbc);
			gbc.gridy += 2;

			counselDetailsPanel.add(getSurname(), gbc);
			gbc.gridy += 2;

			counselDetailsPanel.add(getInitials(), gbc);
			gbc.gridy += 2;

			counselDetailsPanel.add(getFirstName(), gbc);
			gbc.gridy += 2;

			counselDetailsPanel.add(getOtherNames(), gbc);
			gbc.gridy += 2;

			counselDetailsPanel.add(getTitle(), gbc);
			gbc.gridy += 2;

			counselDetailsPanel.add(getQCCheckBox(), gbc);
			gbc.gridy += 2;

			counselDetailsPanel.add(getHonours(), gbc);
			gbc.gridy += 2;

			counselDetailsPanel.add(getAdvocateTypeRadioButtonPanel(), gbc);

			/* Top Of Column */
			gbc.gridx = 1;
			gbc.gridy = 2;

			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;

			counselDetailsPanel.add(lblManSurname, gbc);
			gbc.gridy += 2;

			counselDetailsPanel.add(lblManInitials, gbc);
			gbc.gridy += 2;

			counselDetailsPanel.add(lblFirstNameInvalidEntry, gbc);
			gbc.gridy += 2;

			counselDetailsPanel.add(lblOtherNamesInvalidEntry, gbc);
			gbc.gridy += 2;

			counselDetailsPanel.add(lblTitleInvalidEntry, gbc);
			gbc.gridy += 2;

			// No label needed but used as a placeholder to keep layout
			// consistent
			counselDetailsPanel.add(lblQCInvalidEntry, gbc);
			gbc.gridy += 2;

			counselDetailsPanel.add(lblHonoursInvalidEntry, gbc);
			gbc.gridy += 2;

			counselDetailsPanel.add(lblManAdvocateType, gbc);

			/* Next Column */
			gbc.gridy = 1;
			gbc.gridx++;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			lblYearCalledToTheBar = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"CounselDetails.yearCalledToTheBarLabel"));
			counselDetailsPanel.add(lblYearCalledToTheBar, gbc);
			gbc.gridy += 2;

			lblBarNumber = new JLabel(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"CounselDetails.barNumberLabel"));
			counselDetailsPanel.add(lblBarNumber, gbc);
			gbc.gridy += 2;

			lblDeleted = new JLabel(
					XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "CounselDetails.deletedLabel"));
			counselDetailsPanel.add(lblDeleted, gbc);

			/* Top of Column */
			gbc.gridy = 1;
			gbc.gridx++;

			counselDetailsPanel.add(getYearCalledToTheBar(), gbc);
			gbc.gridy += 2;

			counselDetailsPanel.add(getBarNumber(), gbc);
			gbc.gridy += 2;

			counselDetailsPanel.add(getDeletedCheckBox(), gbc);

			/* Top Of Column */
			gbc.gridy = 0;

			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weightx = 0.5;

			counselDetailsPanel.add(lblYearCalledToTheBarInvalidEntry, gbc);
			gbc.gridy += 2;

			counselDetailsPanel.add(lblBarNumberInvalidEntry, gbc);
			gbc.gridy += 2;

			// No label needed but used as a placeholder to keep layout
			// consistent
			counselDetailsPanel.add(lblDeletedInvalidEntry, gbc);
		}
		return counselDetailsPanel;
	}

	/**
	 * Returns a panel which contains the Advocate Type radio buttons (S and A)
	 * 
	 * @return
	 */
	public JPanel getAdvocateTypeRadioButtonPanel() {
		if (lblManAdvocateType == null) {
			lblManAdvocateType = new JLabel(" ");
		}

		GridBagConstraints gbc = getGridBagLayout();
		if (advocateTypeRadioButtonPanel == null) {
			advocateTypeRadioButtonPanel = new JPanel(new GridBagLayout());

			advocateTypeSRbtn = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"CounselDetails.advocateTypeSLabel"));
			advocateTypeRadioButtonPanel.add(advocateTypeSRbtn, gbc);
			gbc.gridx++;

			advocateTypeARbtn = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
					"CounselDetails.advocateTypeALabel"));
			advocateTypeRadioButtonPanel.add(advocateTypeARbtn, gbc);

			advocateTypeARbtn.setSelected(true);

			ItemListener radioButtonListener = getRadioButtonListener();

			ButtonGroup advocateTypeGroup = new ButtonGroup();
			advocateTypeGroup.add(advocateTypeSRbtn);
			advocateTypeGroup.add(advocateTypeARbtn);

			advocateTypeSRbtn.addItemListener(radioButtonListener);
			advocateTypeARbtn.addItemListener(radioButtonListener);
		}

		return advocateTypeRadioButtonPanel;
	}

	public ItemListener getRadioButtonListener() {
		ItemListener radioButtonListener = new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				int state = itemEvent.getStateChange();
				if (state == ItemEvent.SELECTED) {
					enableSaveButton();
				}
			}
		};
		return radioButtonListener;
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

	public XTextField getSurname() {
		if (lblManSurname == null) {
			lblManSurname = new JLabel(" ");
		}
		if (txtSurname == null) {
			txtSurname = new XTextField();
			txtSurname.setMaxLength(35);
			txtSurname.setColumns(10);
			txtSurname.setUpperCase(true);
			txtSurname.setMinimumSize(txtSurname.getPreferredSize());
			TextValidationController surnameTxtValidation = ValidationControllerFactory.createTextRequired(this,
					txtSurname, lblManSurname, new AbstractTextValidator() {
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
			validationControllers.add(surnameTxtValidation);
			mandatoryFields.add(txtSurname);
		}
		return txtSurname;
	}

	public XTextField getInitials() {
		if (lblManInitials == null) {
			lblManInitials = new JLabel(" ");
		}
		if (txtInitials == null) {
			txtInitials = new XTextField();
			txtInitials.setMaxLength(4);
			txtInitials.setColumns(10);
			txtInitials.setUpperCase(true);
			txtInitials.setMinimumSize(txtInitials.getPreferredSize());
			TextValidationController initialsTxtValidation = ValidationControllerFactory.createTextRequired(this,
					txtInitials, lblManInitials, new TextRegexValidator("^.{1,4}$"));
			validationControllers.add(initialsTxtValidation);
			mandatoryFields.add(txtInitials);
		}
		return txtInitials;
	}

	public XTextField getFirstName() {
		if (lblFirstNameInvalidEntry == null) {
			lblFirstNameInvalidEntry = new JLabel(" ");
		}
		if (txtFirstName == null) {
			txtFirstName = new XTextField();
			txtFirstName.setMaxLength(35);
			txtFirstName.setColumns(10);
			txtFirstName.setUpperCase(true);
			txtFirstName.setMinimumSize(txtFirstName.getPreferredSize());
			TextValidationController firstNameTxtValidation = ValidationControllerFactory.createText(this, txtFirstName,
					lblFirstNameInvalidEntry, new TextRegexValidator("^.{1,35}$"));
			validationControllers.add(firstNameTxtValidation);
		}
		return txtFirstName;
	}

	public XTextField getOtherNames() {
		if (lblOtherNamesInvalidEntry == null) {
			lblOtherNamesInvalidEntry = new JLabel(" ");
		}
		if (txtOtherNames == null) {
			txtOtherNames = new XTextField();
			txtOtherNames.setMaxLength(35);
			txtOtherNames.setColumns(10);
			txtOtherNames.setUpperCase(true);
			txtOtherNames.setMinimumSize(txtOtherNames.getPreferredSize());
			TextValidationController otherNamesTxtValidation = ValidationControllerFactory.createText(this,
					txtOtherNames, lblOtherNamesInvalidEntry, new TextRegexValidator("^.{1,35}$"));
			validationControllers.add(otherNamesTxtValidation);
		}
		return txtOtherNames;
	}

	public XTextField getTitle() {
		if (lblTitleInvalidEntry == null) {
			lblTitleInvalidEntry = new JLabel(" ");
		}
		if (txtTitle == null) {
			txtTitle = new XTextField();
			txtTitle.setMaxLength(25);
			txtTitle.setColumns(10);
			txtTitle.setUpperCase(true);
			txtTitle.setMinimumSize(txtTitle.getPreferredSize());
			TextValidationController titleTxtValidation = ValidationControllerFactory.createText(this, txtTitle,
					lblTitleInvalidEntry, new TextRegexValidator("^.{1,25}$"));
			validationControllers.add(titleTxtValidation);
		}
		return txtTitle;
	}

	public JCheckBox getQCCheckBox() {
		if (lblQCInvalidEntry == null) {
			lblQCInvalidEntry = new JLabel(" ");
		}
		if (qcCheckbox == null) {
			qcCheckbox = new JCheckBox();
			qcCheckbox.addItemListener(getQCListener());
		}
		return qcCheckbox;
	}

	public ItemListener getQCListener() {
		ItemListener qcCheckboxListener = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				int state = e.getStateChange();
				if (state == ItemEvent.SELECTED) {
					addQC();
				} else {
					removeQC();
				}

			}

		};
		return qcCheckboxListener;
	}

	public XTextField getHonours() {
		if (lblHonoursInvalidEntry == null) {
			lblHonoursInvalidEntry = new JLabel(" ");
		}
		if (txtHonours == null) {
			txtHonours = new XTextField();
			txtHonours.setMaxLength(8);
			txtHonours.setColumns(10);
			txtHonours.setUpperCase(true);
			txtHonours.setMinimumSize(txtHonours.getPreferredSize());
			TextValidationController honoursTxtValidation = ValidationControllerFactory.createText(this, txtHonours,
					lblHonoursInvalidEntry, new TextRegexValidator("^.{1,8}$"));
			validationControllers.add(honoursTxtValidation);
		}
		return txtHonours;
	}

	public XTextField getYearCalledToTheBar() {
		if (lblYearCalledToTheBarInvalidEntry == null) {
			lblYearCalledToTheBarInvalidEntry = new JLabel(" ");
		}
		if (txtYearCalledToTheBar == null) {
			txtYearCalledToTheBar = new XTextField();
			txtYearCalledToTheBar.setMaxLength(4);
			txtYearCalledToTheBar.setColumns(10);
			txtYearCalledToTheBar.setUpperCase(true);
			txtYearCalledToTheBar.setNumeric(true);
			txtYearCalledToTheBar.setMinimumSize(txtYearCalledToTheBar.getPreferredSize());
			TextValidationController yearCalledToTheBarTxtValidation = ValidationControllerFactory.createText(this,
					txtYearCalledToTheBar, lblYearCalledToTheBarInvalidEntry, new TextRegexValidator("^[0-9]{1,4}$"));
			validationControllers.add(yearCalledToTheBarTxtValidation);
		}
		return txtYearCalledToTheBar;
	}

	public XTextField getBarNumber() {
		if (lblBarNumberInvalidEntry == null) {
			lblBarNumberInvalidEntry = new JLabel(" ");
		}
		if (txtBarNumber == null) {
			txtBarNumber = new XTextField();
			txtBarNumber.setMaxLength(5);
			txtBarNumber.setColumns(10);
			txtBarNumber.setUpperCase(true);
			txtBarNumber.setNumeric(true);
			txtBarNumber.setMinimumSize(txtBarNumber.getPreferredSize());
			TextValidationController barNumberTxtValidation = ValidationControllerFactory.createText(this, txtBarNumber,
					lblBarNumberInvalidEntry, new TextRegexValidator("^[0-9]{1,5}$"));
			validationControllers.add(barNumberTxtValidation);
		}
		return txtBarNumber;
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
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { txtSurname, txtInitials, txtFirstName,
				txtOtherNames, txtTitle, qcCheckbox, txtHonours, advocateTypeSRbtn, advocateTypeARbtn,
				txtYearCalledToTheBar, txtBarNumber, btnDelete, btnSave, btnCancel }));
	}

	public Boolean isQC(String advTypeInd) {
		String ind = "";

		if (advTypeInd != null) {
			ind = advTypeInd;

			if (ind.equals("Y")) {
				return true;
			} else if (ind.equals("N")) {
				return false;
			} else {
				return false;
			}
		}
		return false;
	}

	public void setAdvocateType(String legalRepType) {
		String repType = "";

		if (legalRepType != null) {
			repType = legalRepType;

			if (repType.equals("S")) {
				setRdbtnS(true);
			} else if (repType.equals("A")) {
				setRdbtnA(true);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Boolean barNumberAlreadyExists(RefAdvocateComplexValue counselComplexVal, Integer newBarNumber) {
		BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
		ArrayList<RefAdvocateBasicValue> counsels = new ArrayList<RefAdvocateBasicValue>();
		Boolean barNumberAlreadyExists = false;
		Integer counselId = 0;
		// If adding a new counsel this will be null
		if (counselComplexVal.getId() != null) {
			counselId = counselComplexVal.getId();
		}

		try {
			counsels = (ArrayList<RefAdvocateBasicValue>) bizRefDelegate.findCounselByBarNumberCourtId(newBarNumber,
					XhibitSingleton.getInstance().getCourtId());
			if (!counsels.isEmpty()) {
				for (RefAdvocateBasicValue counsel : counsels) {
					if ((newBarNumber.equals(counsel.getbarNo())) && (!(counselId.equals(counsel.getId())))) {
						barNumberAlreadyExists = true;
						break;
					}

					if ((newBarNumber.equals(counsel.getbarNo())) && (counselId.equals(counsel.getId()))) {
						// Only the bar number for the current record exists
						barNumberAlreadyExists = false;
					}
				}
			} else {
				barNumberAlreadyExists = false;
			}
		} catch (Exception er) {
			er.printStackTrace();
			XHIBITConstant.handleError(er);
		}

		return barNumberAlreadyExists;
	}

	public Boolean updateCounsel(RefAdvocateComplexValue refAdvocate, RefLegalRepresentativeBasicValue refLegalRep) {
		BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();

		try {
			bizRefDelegate.updateCounselDetails(refAdvocate, refLegalRep, XhibitSingleton.getInstance().getUserSession()
					.getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
			return true;
		} catch (Exception er) {
			XHIBITConstant.handleError(er);
		}
		return false;
	}

	public Boolean addCounsel(RefLegalRepresentativeBasicValue refLegalRep, RefAdvocateComplexValue refAdvocate) {
		BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
		String userName = XhibitSingleton.getInstance().getUserSession()
				.getSessionProperty(UserTerminalProperties.DISPLAY_NAME);
		try {
			bizRefDelegate.createNewCounsel(refAdvocate, refLegalRep, userName);
			return true;
		} catch (Exception er) {
			XHIBITConstant.handleError(er);
		}
		return false;
	}

	public String truncateHonours(String honours) {
		return honours.substring(0, Math.min(honours.length(), 8));
	}

	public void addQC() {
		if (!getTxtHonours().startsWith("KC")) {
			setTxtHonours(truncateHonours("KC " + getTxtHonours()));
		}
	}

	public void removeQC() {
		if (model.getRefAdvocateComplexValue() != null) {
			if (model.getRefAdvocateComplexValue().getHonours() != null) {
				if ((getTxtHonours().substring(0, 2).equals("QC")
						&& (!model.getRefAdvocateComplexValue().getHonours().substring(0, 2).equals("QC")))) {
					setTxtHonours(model.getRefAdvocateComplexValue().getHonours());
				} else if (model.getRefAdvocateComplexValue().getHonours().substring(0, 2).equals("QC")) {
					String removedQC = getTxtHonours().replace("QC ", "");
					setTxtHonours(removedQC);
					model.getRefAdvocateComplexValue().setHonours(removedQC);
				} else if ((getTxtHonours().substring(0, 2).equals("KC")
						&& (!model.getRefAdvocateComplexValue().getHonours().substring(0, 2).equals("KC")))) {
					setTxtHonours(model.getRefAdvocateComplexValue().getHonours());
				} else if (model.getRefAdvocateComplexValue().getHonours().substring(0, 2).equals("KC")) {
					String removedQC = getTxtHonours().replace("KC ", "");
					setTxtHonours(removedQC);
					model.getRefAdvocateComplexValue().setHonours(removedQC);
				} else {
					setTxtHonours(model.getRefAdvocateComplexValue().getHonours());
				}
			} else {
				String removedQC = "";
				if (getTxtHonours().contains("QC")) { 
					getTxtHonours().replace("QC ", "");
				} else if (getTxtHonours().contains("KC")) { 
					getTxtHonours().replace("KC ", "");
				}
				setTxtHonours(removedQC);
			}
		} else {
			String removedQC = "";
			if (getTxtHonours().contains("QC")) { 
				getTxtHonours().replace("QC ", "");
			} else if (getTxtHonours().contains("KC")) { 
				getTxtHonours().replace("KC ", "");
			}
			setTxtHonours(removedQC);
		}
	}

	public Boolean deleteCounsel(RefAdvocateComplexValue refAdvocate) {
		BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();

		try {
			bizRefDelegate.deleteCounselDetails(refAdvocate, XhibitSingleton.getInstance().getUserSession()
					.getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
			return true;
		} catch (Exception er) {
			XHIBITConstant.handleError(er);
		}
		return false;
	}

	public void showUpdateSuccessDialog() {
		XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
						"CounselDetails.updateSuccessTitle"),
				true, XMessageBox.ICONINFORMATION, XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
						"CounselDetails.updateSuccessMessage"),
				XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	}

	private void showUnsavedCancelConfirmationDialog() throws CSRecoverableException {
		boolean messageBoxReply = false;
		messageBoxReply = XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "CounselDetails.cancelTitle"), true,
				XMessageBox.ICONQUESTION,
				XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "CounselDetails.cancelMessage"),
				XMessageBox.YESNO, XMessageBox.DEFAULTCANCEL);
		if (!messageBoxReply) {
			throw new UserCancelException();
		}
		else {
			// Choosing to save without exiting so do not refresh the parent screen on return
			setParentRefreshSearchResults(false);
		}
	}

	private void showDeleteCounselConfirmationDialog() throws CSRecoverableException {
		boolean messageBoxReply = false;
		messageBoxReply = XMessageBox
				.alert(parentDialog,
						XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
								"CounselDetails.deleteConfirmationTitle"),
						true, XMessageBox.ICONQUESTION,
						XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
								"CounselDetails.deleteConfirmationMessage"),
						XMessageBox.YESNO, XMessageBox.DEFAULTCANCEL);
		if (!messageBoxReply) {
			throw new UserCancelException();
		}
	}

	public void showDeleteSuccessDialog() {
		XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
						"CounselDetails.deleteSuccessTitle"),
				true, XMessageBox.ICONINFORMATION, XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
						"CounselDetails.deleteSuccessMessage"),
				XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	}

	public void showBarNumberAlreadyExistsDialog() throws CSRecoverableException {
		XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
						"CounselDetails.barNumberExistsTitle"),
				true, XMessageBox.ICONERROR, XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails,
						"CounselDetails.barNumberExistsMessage"),
				XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
		throw new UserCancelException();
	}

	public void showAddSuccessDialog() {
		XMessageBox.alert(parentDialog,
				XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "CounselDetails.addSuccessTitle"),
				true, XMessageBox.ICONINFORMATION,
				XHIBITConstant.getResource(XhibitBundles.ChamberAndAdvocateDetails, "CounselDetails.addSuccessMessage"),
				XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
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
			} else if (components[i].getClass() == JCheckBox.class) {
				((JCheckBox) components[i]).addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						changesMade();
					}
				});
			}
		}
	}

	public void changesMade() {
		enableSaveButton();
		changesMade = true;
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
		qcCheckbox.setEnabled(false);
		advocateTypeSRbtn.setEnabled(false);
		advocateTypeARbtn.setEnabled(false);
	}

	private void makeReadOnly() {
		disableComponents(counselDetailsPanel.getComponents());
		btnDelete.setEnabled(false);
		btnSave.setEnabled(false);
	}

	private void setTxtChamberRefNo(String chamberRefNo) {
		this.txtChamberRefNo.setText(chamberRefNo);
	}

	private String getTxtSurname() {
		return txtSurname.getText();
	}

	private void setTxtSurname(String surname) {
		this.txtSurname.setText(surname);
	}

	private String getTxtInitials() {
		return txtInitials.getText();
	}

	private void setTxtInitials(String initials) {
		this.txtInitials.setText(initials);
	}

	private String getTxtFirstName() {
		return txtFirstName.getText();
	}

	private void setTxtFirstName(String firstName) {
		this.txtFirstName.setText(firstName);
	}

	private String getTxtOtherNames() {
		return txtOtherNames.getText();
	}

	private void setTxtOtherNames(String otherNames) {
		this.txtOtherNames.setText(otherNames);
	}

	private String getTxtTitle() {
		return txtTitle.getText();
	}

	private void setTxtTitle(String title) {
		this.txtTitle.setText(title);
	}

	private Boolean getChckbxQC() {
		return qcCheckbox.isSelected();
	}

	private void setChckbxQC(Boolean checked) {
		this.qcCheckbox.setSelected(checked);
	}

	private String getTxtHonours() {
		return txtHonours.getText();
	}

	private void setTxtHonours(String honours) {
		this.txtHonours.setText(honours);
	}

	private Boolean getRdbtnS() {
		return advocateTypeSRbtn.isSelected();
	}

	private void setRdbtnS(Boolean checked) {
		this.advocateTypeSRbtn.setSelected(checked);
	}

	private Boolean getRdbtnA() {
		return advocateTypeARbtn.isSelected();
	}

	private void setRdbtnA(Boolean checked) {
		this.advocateTypeARbtn.setSelected(checked);
	}

	private String getTxtYearCalledToTheBar() {
		return txtYearCalledToTheBar.getText();
	}

	private void setTxtYearCalledToTheBar(String yearCalledToTheBar) {
		this.txtYearCalledToTheBar.setText(yearCalledToTheBar);
	}

	private String getTxtBarNumber() {
		return txtBarNumber.getText();
	}

	private void setTxtBarNumber(String barNumber) {
		this.txtBarNumber.setText(barNumber);
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
					if (model.getRefAdvocateComplexValue() != null) {
						if (model.getRefAdvocateComplexValue().getId() != null) {
							if (model.getRefLegalRepresentativeBasicValue() != null) {
								if (model.getRefLegalRepresentativeBasicValue().getId() != null) {
									String yearCalledToTheBar = "";
									String barNumber = "";
									RefAdvocateComplexValue refAdvocateVal = new RefAdvocateComplexValue();
									refAdvocateVal = bizRefDelegate
											.findCounselByRefAdvocateId(model.getRefAdvocateComplexValue().getId());
									RefLegalRepresentativeBasicValue refLegalRepVal = new RefLegalRepresentativeBasicValue();
									refLegalRepVal = bizRefDelegate.findLegalRepresentativeFromAdvocateLegalRepId(
											model.getRefLegalRepresentativeBasicValue().getId());
									setTxtChamberRefNo(refAdvocateVal.getCrestChamberId().toString());
									setTxtSurname(refLegalRepVal.getSurname());
									setTxtInitials(refLegalRepVal.getInitials());
									setTxtFirstName(refLegalRepVal.getFirstName());
									setTxtOtherNames(refLegalRepVal.getMiddleName());
									setTxtTitle(refLegalRepVal.getTitle());
									setChckbxQC(isQC(refAdvocateVal.getAdvTypeInd()));
									setTxtHonours(refAdvocateVal.getHonours());
									if (getChckbxQC()) {
										addQC();
									}
									setAdvocateType(refLegalRepVal.getLegalRepType());
									if (refAdvocateVal.getyearOfCall() != null) {
										yearCalledToTheBar = refAdvocateVal.getyearOfCall().toString();
									}
									setTxtYearCalledToTheBar(yearCalledToTheBar);
									if (refAdvocateVal.getbarNo() != null) {
										barNumber = refAdvocateVal.getbarNo().toString();
									}
									setTxtBarNumber(barNumber);
									if (refAdvocateVal.getObsInd().equals("Y")) {
										setChckbxDeleted(true);
									}
									if (refAdvocateVal.getObsInd().equals("Y") || model.getReadOnly()) {
										makeReadOnly();
									}
								}
							}
						}
					}
				} catch (Exception er) {
					er.printStackTrace();
					XHIBITConstant.handleError(er);
				}
			} else if (model.getCallingClass() instanceof ChamberDetailsPanel) {
				setTxtChamberRefNo(model.getRefAdvocateComplexValue().getCrestChamberId().toString());
			}
		}
	}

	/**
	 * Set the data entered on screen into the model.
	 */
	private void moveScreenToModel() {
		if (model.getRefAdvocateComplexValue() == null) {
			model.setRefAdvocateComplexValue(new RefAdvocateComplexValue());
		}
		RefAdvocateComplexValue refAdvocate = model.getRefAdvocateComplexValue();
		if (model.getRefLegalRepresentativeBasicValue() == null) {
			model.setRefLegalRepresentativeBasicValue(new RefLegalRepresentativeBasicValue());
		}
		RefLegalRepresentativeBasicValue refLegalRep = model.getRefLegalRepresentativeBasicValue();
		Integer yearCalledToTheBar = null;
		Integer barNumber = null;
		refLegalRep.setSurname(getTxtSurname());
		refLegalRep.setInitials(getTxtInitials());
		refLegalRep.setFirstName(getTxtFirstName());
		refLegalRep.setMiddleName(getTxtOtherNames());
		refLegalRep.setTitle(getTxtTitle());
		// Check for QC ticked
		if (getChckbxQC()) {
			refAdvocate.setAdvTypeInd("Y");
		} else {
			refAdvocate.setAdvTypeInd("N");
		}
		refAdvocate.setHonours(getTxtHonours());
		// Advocate type radio buttons to legal rep type
		if (getRdbtnS()) {
			refLegalRep.setLegalRepType("S");
		} else if (getRdbtnA()) {
			refLegalRep.setLegalRepType("A");
		}
		if (!getTxtYearCalledToTheBar().equals("")) {
			yearCalledToTheBar = Integer.parseInt(getTxtYearCalledToTheBar());
		}
		refAdvocate.setyearOfCall(yearCalledToTheBar);
		if (!getTxtBarNumber().equals("")) {
			barNumber = Integer.parseInt(getTxtBarNumber());
		}
		refAdvocate.setbarNo(barNumber);
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
		addChangeListeners(counselDetailsPanel.getComponents());
		if (model.getCallingClass() instanceof ChamberDetailsPanel) {
			btnDelete.setEnabled(false);
		}
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

		if (btnDelete.equals(getDeinitialiseSource())) {
			showDeleteCounselConfirmationDialog();
			if (deleteCounsel(model.getRefAdvocateComplexValue())) {
				showDeleteSuccessDialog();
				// Deleting Counsel - refresh parent screen search results
				setParentRefreshSearchResults(true);
				parentDialog.clearStatusBarScreenCode();
				parentDialog.dispose();
			}
		} else if (btnSave.equals(getDeinitialiseSource())) {
			stepValidate();
			moveScreenToModel();

			if (barNumberAlreadyExists(model.getRefAdvocateComplexValue(),
					model.getRefAdvocateComplexValue().getbarNo())) {
				showBarNumberAlreadyExistsDialog();
			} else {
				// Adding/Amending Counsel - refresh parent screen search results
				setParentRefreshSearchResults(true);
				if (model.getRefAdvocateComplexValue().getId() == null
						|| model.getRefAdvocateComplexValue().getId().equals(0)) {
					// save Counsel
					if (addCounsel(model.getRefLegalRepresentativeBasicValue(), model.getRefAdvocateComplexValue())) {
						showAddSuccessDialog();
						setCounselAdded(true);
						parentDialog.clearStatusBarScreenCode();
						parentDialog.dispose();
					}
				} else {
					// amend Counsel
					if (updateCounsel(model.getRefAdvocateComplexValue(), model.getRefLegalRepresentativeBasicValue())) {
						showUpdateSuccessDialog();
						parentDialog.clearStatusBarScreenCode();
						parentDialog.dispose();
					}
				}
			}
		} else {
			if (changesMade) {
				showUnsavedCancelConfirmationDialog();
			}
			else {
				// Cancelling out - do not refresh parent screen search results
				setParentRefreshSearchResults(false);
			}
		}
		
	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {

	}
	
	/**
	 * The Counsel Details screen can be invoked from the Chamber Details screen, in which case we don't want
	 * to invoke setRefreshSearchResults on the parent class - only if the parent is ChamberAndAdvocateDetailsPanel
	 * @param refresh
	 */
	private void setParentRefreshSearchResults(boolean refresh) {
		if ( model.getCallingClass() instanceof ChamberAndAdvocateDetailsPanel ) {
			((ChamberAndAdvocateDetailsPanel) model.getCallingClass()).setRefreshSearchResults(refresh, false);
		}
	}
	
	/**
	 * Indicate on the parent class that a new counsel has been added - only when the parent class is 
	 * ChamberDetailsPanel
	 * @param added
	 */
	private void setCounselAdded(boolean added) {
		if ( model.getCallingClass() instanceof ChamberDetailsPanel ) {
			((ChamberDetailsPanel) model.getCallingClass()).setCounselAdded(added);
		}
	}

}
