
package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.ComboHelperVO;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class OtherCasesPanelFilterSelectionPanel extends XPanel{

	private static final long serialVersionUID = 1L;
	private JPanel caseTypePanel;
	private JPanel caseClassPanel;
	private JPanel requiredJudgeTypePanel;
	private JPanel secureCourtroomPanel;
	private JPanel defaultHearingPanel;
	private JPanel bcStatusPanel;
	private JPanel timeUnitsPanel;
	private JPanel juvenileOnlyPanel;
	private JPanel timeEstimatePanel;
	private JPanel casesOlderThanPanel;
	private JPanel caseTypeClassStatusPanel;
	private JPanel defaultTypeTimeDaysPanel;
	private JPanel requiredCasesWeeksPanel;
	private JPanel secureJuvenilePanel;
	private JPanel defaultTypeTimeDays;
	private JLabel casesOlderThanLabel;
	private JLabel weeksOldLabel;
	private JLabel caseTypeLabel;
	private JLabel defaultHearingTypeLabel;
	private JLabel requiredJudgeTypeLabel;
	private JLabel bcStatusLabel;
	private JLabel timeEstimateLabel;
	private JLabel timeEstimateAndLabel;
	private JLabel caseClassLabel;
	private JComboBox caseTypeCombo;
	private XComboBox bcStatusCombo;
	private XComboBox defaultHearingTypeCombo;
	private XComboBox requiredJudgeTypeCombo;
	private JComboBox timeUnitsCombo;
	private JRadioButton caseClassSelectAllRadioButton;
	private JRadioButton caseClassSelectOneRadioButton;
	private JRadioButton caseClassSelectTwoRadioButton;
	private JRadioButton caseClassSelectThreeRadioButton;
	private JCheckBox secureCourtroomCheckBox;
	private JCheckBox juvenileOnlyCheckBox;
	private XTextField timeEstFrom;
	private XTextField timeEstTo;
	private XTextField timeEstWeeks;
	private ArrayList<RefHearingTypeBasicValue> hearingTypeArray;
	private ArrayList<DropdownCodeStringValue> bcStatusArray;
	private ArrayList<RefSystemCodeBasicValue> requiredJudgeTypesArray;
	private ArrayList<DropdownCodeStringValue> caseTypesArray;
	private Vector timeEstUnitArray;
	protected XDialog parent;

	private OtherCasesPanelFilterSelectionModel model;

	public OtherCasesPanelFilterSelectionPanel(OtherCasesPanelFilterSelectionDialog otherCasesPanelFilterSelectionDialog, 
			OtherCasesPanelFilterSelectionModel model) throws CSRecoverableException {
		this.model = model;
		this.parent = otherCasesPanelFilterSelectionDialog;
		stepInitialise();
		jbInit();
	}


	private void jbInit() {

		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		this.setPreferredSize(new Dimension(600, 350));

		// Content Panel Element

		// Panel - Case types ComboBox , Case Class, B/C Status
		gbc.weighty = 0.01;

		caseTypeClassStatusPanel = initCaseTypeClassStatusPanel();
		this.add(caseTypeClassStatusPanel, gbc);

		// Panel Default Time Estimate Days
		gbc.weighty = 0.01;
		gbc.gridy++;

		defaultTypeTimeDays = initDefaultTypeTimeDaysPanel();
		this.add(defaultTypeTimeDays, gbc);

		// Time range
		gbc.weighty = 0.02;
		gbc.gridy++;
		timeEstimatePanel = initTimeEstimatePanel();
		this.add(timeEstimatePanel, gbc);

		// Required weeks panel

		gbc.weighty = 0.02;
		gbc.gridy++;
		requiredCasesWeeksPanel = initRequiredCasesWeeksPanel();
		this.add(requiredCasesWeeksPanel, gbc);

		// secure and juvenile panel

		gbc.weighty = 0.02;
		gbc.gridy++;
		secureJuvenilePanel = initSecureJuvenilePanel();
		this.add(secureJuvenilePanel, gbc);
	}

	@Override
	public void stepInitialise() throws CSRecoverableException {
	}

	private JPanel initCaseTypePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		caseTypePanel = new JPanel();
		caseTypePanel.setLayout(new GridBagLayout());

		// Add the panel elements
		// Panel - Case Type

		caseTypeLabel = new JLabel(getResource("CaseTypeLabel"));
		caseTypePanel.add(caseTypeLabel, gbc);
		gbc.gridx++;
		if (caseTypesArray == null) {
			caseTypesArray = ListingDropdownPopulation.getCaseTypeCodes();
		}
		caseTypeCombo = new XComboBox();
		caseTypeCombo.setModel(new DefaultComboBoxModel(caseTypesArray.toArray()));
		caseTypeCombo.setRenderer(new OtherCasesPanelFilterSelectionDropDownBoxCellRenderer());
		caseTypePanel.add(caseTypeCombo, gbc);

		return caseTypePanel;

	}

	private JPanel initDefaultHearingTypePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		defaultHearingPanel = new JPanel();
		defaultHearingPanel.setLayout(new GridBagLayout());

		// Add the panel elements
		gbc.weightx = 0.05;
		defaultHearingTypeLabel = new JLabel(getResource("DefaultHearingTypeLabel"));
		defaultHearingPanel.add(defaultHearingTypeLabel, gbc);
		hearingTypeArray = ListingDropdownPopulation.getHearingTypes();

		gbc.gridx++;
		gbc.weightx = 0.95;
		defaultHearingTypeCombo = new XComboBox();
		defaultHearingTypeCombo.setModel(new DefaultComboBoxModel(hearingTypeArray.toArray()));
		defaultHearingTypeCombo.setRenderer(new OtherCasesPanelFilterSelectionDropDownBoxCellRenderer());
		defaultHearingPanel.add(defaultHearingTypeCombo, gbc);

		return defaultHearingPanel;

	}

	private JPanel initRequiredJudgeTypePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		requiredJudgeTypePanel = new JPanel();
		requiredJudgeTypePanel.setLayout(new GridBagLayout());

		// Add the panel elements
		gbc.weightx = 0.05;
		requiredJudgeTypeLabel = new JLabel(getResource("RequiredJudgeTypeLabel"));
		requiredJudgeTypePanel.add(requiredJudgeTypeLabel, gbc);
		gbc.gridx++;
		gbc.weightx = 0.95;
		if (requiredJudgeTypesArray == null) {
			requiredJudgeTypesArray = ListingDropdownPopulation.getRequiredJudgeTypes();
		}
		requiredJudgeTypeCombo = new XComboBox();
		requiredJudgeTypeCombo.setModel(new DefaultComboBoxModel(requiredJudgeTypesArray.toArray()));
		requiredJudgeTypeCombo.setRenderer(new OtherCasesPanelFilterSelectionDropDownBoxCellRenderer());
		requiredJudgeTypePanel.add(requiredJudgeTypeCombo, gbc);

		return requiredJudgeTypePanel;
	}

	private JPanel initsecureCourtroomPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		secureCourtroomPanel = new JPanel();
		secureCourtroomPanel.setLayout(new GridBagLayout());
		// Add the panel elements
		secureCourtroomCheckBox = new JCheckBox(getResource("SecureCourtroomCheckBoxLabel"));
		secureCourtroomPanel.add(secureCourtroomCheckBox, gbc);
		return secureCourtroomPanel;
	}

	private JPanel initJuvenileOnlyPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		juvenileOnlyPanel = new JPanel();
		juvenileOnlyPanel.setLayout(new GridBagLayout());
		// Add the panel elements
		juvenileOnlyCheckBox = new JCheckBox(getResource("JuvenileOnlyCheckBoxLabel"));
		juvenileOnlyPanel.add(juvenileOnlyCheckBox, gbc);
		return juvenileOnlyPanel;
	}

	private JPanel initCaseClassPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		caseClassPanel = new JPanel();
		caseClassPanel.setLayout(new GridBagLayout());

		// add Case Class Label panel

		caseClassLabel = new JLabel(getResource("CaseClassLabel"));
		caseClassPanel.add(caseClassLabel, gbc);

		// Add the panel elements
		gbc.gridx++;
		caseClassSelectAllRadioButton = new JRadioButton(getResource("caseClassSelectAllRadioButtonLabel"));
		caseClassPanel.add(caseClassSelectAllRadioButton, gbc);

		gbc.gridy++;
		caseClassSelectOneRadioButton = new JRadioButton(getResource("caseClassSelectOneRadioButtonLabel"));
		caseClassSelectOneRadioButton.setActionCommand("1");
		caseClassPanel.add(caseClassSelectOneRadioButton, gbc);

		gbc.gridy++;
		caseClassSelectTwoRadioButton = new JRadioButton(getResource("caseClassSelectTwoRadioButtonLabel"));
		caseClassSelectTwoRadioButton.setActionCommand("2");
		caseClassPanel.add(caseClassSelectTwoRadioButton, gbc);

		gbc.gridy++;
		caseClassSelectThreeRadioButton = new JRadioButton(getResource("caseClassSelectThreeRadioButtonLabel"));
		caseClassSelectThreeRadioButton.setActionCommand("3");
		caseClassPanel.add(caseClassSelectThreeRadioButton, gbc);

		caseClassSelectAllRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (caseClassSelectAllRadioButton.isSelected()) {
					caseClassSelectOneRadioButton.setSelected(true);
					caseClassSelectTwoRadioButton.setSelected(true);
					caseClassSelectThreeRadioButton.setSelected(true);
				} else {
					caseClassSelectOneRadioButton.setSelected(false);
					caseClassSelectTwoRadioButton.setSelected(false);
					caseClassSelectThreeRadioButton.setSelected(false);
				}
			}
		});

		return caseClassPanel;
	}
	
	private String getCaseClassSelected() {
		StringBuilder selected = new StringBuilder(""); 
	
		if (caseClassSelectOneRadioButton.isSelected()) {
			selected.append(caseClassSelectOneRadioButton.getActionCommand());
		};
		if (caseClassSelectTwoRadioButton.isSelected()) {
			if (!"".equals(selected.toString())) {
				selected.append(",");	
			}
			selected.append(caseClassSelectTwoRadioButton.getActionCommand());
		};
		if (caseClassSelectThreeRadioButton.isSelected()) {
			if (!"".equals(selected.toString())) {
				selected.append(",");	
			}
			selected.append(caseClassSelectThreeRadioButton.getActionCommand());
		};
		return selected.toString();
	}

	private JPanel initTimeEstimatePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		timeEstimatePanel = new JPanel();
		timeEstimatePanel.setLayout(new GridBagLayout());

		// Add the panel elements
		gbc.weightx = 0.25;
		timeEstimateLabel = new JLabel(getResource("TimeEstimateLabel"));
		timeEstimatePanel.add(timeEstimateLabel, gbc);

		gbc.weightx = 0.25;
		gbc.gridx++;
		timeEstFrom = new XTextField(2);
		timeEstFrom.setMaxLength(2);
		timeEstFrom.setNumeric(true);
		timeEstFrom.getDocument().addDocumentListener(new TimeEstimateRangeListener());
		timeEstimatePanel.add(timeEstFrom, gbc);

		gbc.weightx = 0.25;
		gbc.gridx++;
		timeEstimateAndLabel = new JLabel(getResource("TimeEstimateAndLabel"));
		timeEstimatePanel.add(timeEstimateAndLabel, gbc);

		gbc.weightx = 0.25;
		gbc.gridx++;
		timeEstTo = new XTextField(2);
		timeEstTo.setMaxLength(2);
		timeEstTo.setNumeric(true);
		timeEstTo.getDocument().addDocumentListener(new TimeEstimateRangeListener());
		timeEstimatePanel.add(timeEstTo, gbc);

		// Time Units
		gbc.weightx = 0.20;
		gbc.gridx++;
		timeUnitsPanel = initTimeUnitsPanel();
		timeEstimatePanel.add(timeUnitsPanel);

		return timeEstimatePanel;
	}

	private JPanel initCasesOlderThanPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		casesOlderThanPanel = new JPanel();
		casesOlderThanPanel.setLayout(new GridBagLayout());

		// Add the panel elements
		gbc.weightx = 0.15;
		casesOlderThanLabel = new JLabel(getResource("CasesOlderThanLabel"));
		casesOlderThanPanel.add(casesOlderThanLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 0.15;
		timeEstWeeks = new XTextField(2);
		timeEstWeeks.setNumeric(true);
		timeEstWeeks.setMaxLength(2);
		casesOlderThanPanel.add(timeEstWeeks, gbc);

		gbc.gridx++;
		gbc.weightx = 0.10;

		weeksOldLabel = new JLabel(getResource("WeeksOldLabel"));
		casesOlderThanPanel.add(weeksOldLabel, gbc);

		return casesOlderThanPanel;
	}

	private JPanel initBCStatusPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.CENTER, XHIBITConstant.nonContainerInsets, 0, 0);
		bcStatusPanel = new JPanel();
		bcStatusPanel.setLayout(new GridBagLayout());

		// Add the panel elements
		gbc.weightx = 0.05;
		bcStatusLabel = new JLabel(getResource("BCStatusLabel"));
		bcStatusPanel.add(bcStatusLabel, gbc);
		gbc.gridx++;
		gbc.weightx = 0.95;
		if (bcStatusArray == null) {
			bcStatusArray = ListingDropdownPopulation.getBcStatus(true);
		}
		bcStatusCombo = new XComboBox();
		bcStatusCombo.setModel(new DefaultComboBoxModel(bcStatusArray.toArray()));
		bcStatusCombo.setRenderer(new OtherCasesPanelFilterSelectionDropDownBoxCellRenderer());
		bcStatusPanel.add(bcStatusCombo, gbc);
		return bcStatusPanel;
	}

	private JPanel initTimeUnitsPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		timeUnitsPanel = new JPanel();
		timeUnitsPanel.setLayout(new GridBagLayout());

		// Add the panel elements
		gbc.weightx = 0.05;
		gbc.gridx++;
		gbc.weightx = 0.95;
		if (timeEstUnitArray == null) {
			timeEstUnitArray = ListingDropdownPopulation.getTimeEstimateUnits();
		}
		timeUnitsCombo = new XComboBox(new DefaultComboBoxModel(timeEstUnitArray));
		timeUnitsCombo.setEnabled(false);
		timeUnitsPanel.add(timeUnitsCombo, gbc);

		return timeUnitsPanel;
	}

	private JPanel initCaseTypeClassStatusPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		caseTypeClassStatusPanel = new JPanel();
		caseTypeClassStatusPanel.setLayout(new GridBagLayout());

		// Panel Case Type
		gbc.weightx = 0.75;
		gbc.gridheight = 2;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		caseTypePanel = initCaseTypePanel();
		caseTypeClassStatusPanel.add(caseTypePanel);

		// Panel Case Class
		gbc.weightx = 0.20;

		gbc.gridwidth = 2;
		gbc.gridx++;
		caseClassPanel = initCaseClassPanel();
		caseTypeClassStatusPanel.add(caseClassPanel, gbc);

		gbc.weightx = 0.05;
		gbc.gridheight = 2;
		gbc.gridwidth = 2;
		gbc.gridx++;
		bcStatusPanel = initBCStatusPanel();
		caseTypeClassStatusPanel.add(bcStatusPanel);

		return caseTypeClassStatusPanel;
	}

	private JPanel initDefaultTypeTimeDaysPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		defaultTypeTimeDaysPanel = new JPanel();
		defaultTypeTimeDaysPanel.setLayout(new GridBagLayout());

		// Default Hearing Panel
		gbc.weightx = 0.25;
		defaultHearingPanel = initDefaultHearingTypePanel();
		defaultTypeTimeDaysPanel.add(defaultHearingPanel);

		return defaultTypeTimeDaysPanel;
	}

	private JPanel initRequiredCasesWeeksPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		requiredCasesWeeksPanel = new JPanel();
		requiredCasesWeeksPanel.setLayout(new GridBagLayout());

		// Required Judge Panel
		gbc.weightx = 0.50;
		requiredJudgeTypePanel = initRequiredJudgeTypePanel();
		requiredCasesWeeksPanel.add(requiredJudgeTypePanel);

		// Cases Older Than Panel
		gbc.weightx = 0.50;
		gbc.gridx++;
		casesOlderThanPanel = initCasesOlderThanPanel();
		requiredCasesWeeksPanel.add(casesOlderThanPanel);

		return requiredCasesWeeksPanel;
	}

	private JPanel initSecureJuvenilePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		secureJuvenilePanel = new JPanel();
		secureJuvenilePanel.setLayout(new GridBagLayout());

		// Secure courtroom Panel
		gbc.weightx = 0.50;
		secureCourtroomPanel = initsecureCourtroomPanel();
		secureJuvenilePanel.add(secureCourtroomPanel);

		// secure juvenile panel
		gbc.weightx = 0.50;
		gbc.gridx++;
		juvenileOnlyPanel = initJuvenileOnlyPanel();
		secureJuvenilePanel.add(juvenileOnlyPanel);

		return secureJuvenilePanel;
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {	
	}

	@Override
	public void stepActivate() throws CSRecoverableException {
		stepUpdateViewState();
	}

	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		moveScreenToModel();
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {	
		if(update){
			if (!validateCriteria()) {
				throw new UserCancelException();
			}
			OtherCasesPanelFilterGetResultsAction xaction = new OtherCasesPanelFilterGetResultsAction(model); 
			xaction.actionPerformed(new ActionEvent(model.getXac(),0,"call OtherCasesPanelFilterGetResultsAction "));
		}
	}

	private void moveScreenToModel() throws CSRecoverableException {
		// Update the model from the screen
		String caseType = ((DropdownCodeStringValue) caseTypeCombo.getSelectedItem()).getPrintString();
		model.setCaseType(caseType != null ? caseType: null);
		model.setCaseClass(getCaseClassSelected());	
		String bcStatus = ((DropdownCodeStringValue) bcStatusCombo.getSelectedItem()).getCode();
		model.setBcStatus(bcStatus != null ? bcStatus: null );		
		RefHearingTypeBasicValue defaultHearingType = (RefHearingTypeBasicValue) defaultHearingTypeCombo.getModel()
				.getSelectedItem();
		model.setDefaultHearingType(defaultHearingType);		
		model.setTimeFrom(timeEstFrom.getText());		
		model.setTimeTo(timeEstTo.getText());		
		model.setTimeUnits (Integer.valueOf(((ComboHelperVO) timeUnitsCombo.getSelectedItem()).getDbValue()));	
		model.setTimeEstWeeks(timeEstWeeks.getText());		
		RefSystemCodeBasicValue requiredJudgeType = (RefSystemCodeBasicValue) requiredJudgeTypeCombo.getModel()
				.getSelectedItem();
		model.setRequiredJudgeType(requiredJudgeType);		 
		model.setSecureCourtroomAsBoolean(secureCourtroomCheckBox.isSelected());
		model.setJuvenileOnlyAsBoolean(juvenileOnlyCheckBox.isSelected());		
	}

	public static String getResource(String key) {
		return XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingsOtherCasesPanelFilter." + key);
	}

	private boolean validateCriteria() {
		return validateTimeEstimate();	
	}

	private boolean validateTimeEstimate() {
		String errorMessage = null;
		
		if ( (timeEstFrom.getText().isEmpty() && !timeEstTo.getText().isEmpty())
                || (!timeEstFrom.getText().isEmpty() && timeEstTo.getText().isEmpty())) {			
        	errorMessage = getResource("TimeEstimateIncompleteMessage");			
		}
		else if (!timeEstFrom.getText().isEmpty() && !timeEstTo.getText().isEmpty() 
				&& Integer.parseInt(timeEstFrom.getText()) > Integer.parseInt(timeEstTo.getText()) ) {
			errorMessage = getResource("TimeEstimateInvalidRangeMessage");
		}
		if (errorMessage != null) {
			XMessageBox.alert(parent, getResource("Error"), true, XMessageBox.ICONERROR, 
					errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
		}
		return errorMessage == null;
	}
	
	private class TimeEstimateRangeListener extends TextFieldListener {
		@Override
		public void setFieldChanged(DocumentEvent e) { 
			timeUnitsCombo.setEnabled(!timeEstFrom.getText().isEmpty() || !timeEstTo.getText().isEmpty());
		}
	}
	
	private class TextFieldListener implements DocumentListener {
		@Override
		public void insertUpdate(DocumentEvent e) {
			setFieldChanged(e);
		}
		@Override
		public void removeUpdate(DocumentEvent e) {
			setFieldChanged(e);
		}
		@Override
		public void changedUpdate(DocumentEvent e) {
			setFieldChanged(e);
		}
		public void setFieldChanged(DocumentEvent e) {
			//Overridden at field level
		}
	}
	
}
