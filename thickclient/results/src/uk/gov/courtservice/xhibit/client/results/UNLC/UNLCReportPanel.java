package uk.gov.courtservice.xhibit.client.results.UNLC;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
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
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayUNLCReportAction;
import uk.gov.courtservice.xhibit.client.results.OUTC.OUTCReportDropDownBoxCellRenderer;
import uk.gov.courtservice.xhibit.client.results.OUTC.OUTCReportDropDownPopulation;
import uk.gov.courtservice.xhibit.client.results.OUTC.OUTCReportModel;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: UNLCReportPanel
 * </p>
 * <p>
 * Description: The panel which displays OUTC report
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Ervin P
 * @version 1.0
 */
public class UNLCReportPanel extends XPanel {

	private static final long serialVersionUID = 1L;
	private JPanel caseTypePanel;
	private JPanel caseClassPanel;
	private JPanel showNotesPanel;
	private JPanel sortByPanel;
	private JPanel requiredJudgeTypePanel;
	private JPanel secureCourtroomPanel;
	private JPanel defaultHearingPanel;
	private JPanel bcStatusPanel;
	private JPanel previewPanel;
	private JPanel timeUnitsPanel;
	private JPanel juvenileOnlyPanel;
	private JPanel timeEstimatePanel;
	private JPanel casesOlderThanPanel;
	private JPanel caseTypeClassStatusPanel;
	private JPanel defaultTypeTimeDaysPanel;
	private JPanel requiredCasesWeeksPanel;
	private JPanel secureJuvenilePanel;
	private JPanel showNotesSortByPanel;
	private JPanel defaultTypeTimeDays;
	private JLabel casesOlderThanLabel;
	private JLabel weeksOldLabel;
	private JLabel caseTypeLabel;
	private JLabel defaultHearingTypeLabel;
	private JLabel requiredJudgeTypeLabel;
	private JLabel previewLabel;
	private JLabel bcStatusLabel;
	private JLabel timeEstimateLabel;
	private JLabel timeEstimateAndLabel;
	private JLabel caseClassLabel;
	private JComboBox caseTypeCombo;
	private XComboBox bcStatusCombo;
	private XComboBox defaultHearingTypeCombo;
	private XComboBox requiredJudgeTypeCombo;
	private JComboBox timeUnitsCombo;
	private ButtonGroup sortByRadioButtonGroup;
	private JCheckBox caseClassSelectAnyCheckBox;
	private JCheckBox caseClassSelectOneCheckBox;
	private JCheckBox caseClassSelectTwoCheckBox;
	private JCheckBox caseClassSelectThreeCheckBox;
	private JCheckBox secureCourtroomCheckBox;
	private JCheckBox juvenileOnlyCheckBox;
	private JCheckBox priorityCheckBox;
	private JCheckBox restrictedCheckBox;
	private JCheckBox standardCheckBox;
	private JRadioButton ageRadioButton;
	private JRadioButton caseNumberRadioButton;
	private XTextField timeEstFrom;
	private XTextField timeEstTo;
	private XTextField timeEstWeeks;
	private ArrayList<RefHearingTypeBasicValue> hearingTypeArray;
	private ArrayList<DropdownCodeStringValue> bcStatusArray;
	private ArrayList<RefSystemCodeBasicValue> requiredJudgeTypesArray;
	private ArrayList<DropdownCodeStringValue> caseTypesArray;
	private Vector timeEstUnitArray;
	protected XDialog parent;

	private OUTCReportModel model;

	public UNLCReportPanel(RunUNLCReportDialog runUNLCReportDialog, OUTCReportModel model) throws CSRecoverableException {
		this.model = model;
		this.parent = runUNLCReportDialog;
		stepInitialise();
		jbInit();
	}


	private void jbInit() {

		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		this.setPreferredSize(new Dimension(600, 450));

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

		gbc.weighty = 0.02;
		gbc.gridy++;
		JPanel showNotesPanel = initShowNotesSortByPanel();
		this.add(showNotesPanel, gbc);
		
		
		gbc.weighty = 0.02;
		gbc.gridy++;
		previewPanel = initPreviewPanel();
		this.add(previewPanel, gbc);

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

		caseTypeLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "OUTCReport.panel_label_CaseTypeLabel"));
		caseTypePanel.add(caseTypeLabel, gbc);
		gbc.gridx++;
		if (caseTypesArray == null) {
			caseTypesArray = OUTCReportDropDownPopulation.getCaseTypeCodes();
		}
		caseTypeCombo = new XComboBox();
		caseTypeCombo.setModel(new DefaultComboBoxModel(caseTypesArray.toArray()));
		caseTypeCombo.setRenderer(new OUTCReportDropDownBoxCellRenderer());
		
		caseTypeCombo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String caseType = ((DropdownCodeStringValue) caseTypeCombo.getSelectedItem()).getPrintString();
				if ( caseType != null && "T".equals(caseType) ) {
					// Enable the case class options for Trial cases
					resetCaseClassOptions(true);
				}
				else {
					// Disable the case class options as non-Trial cases could be selected
					resetCaseClassOptions(false);
				}
			}
		});
		
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
		defaultHearingTypeLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"OUTCReport.panel_label_DefaultHearingTypeLabel"));
		defaultHearingPanel.add(defaultHearingTypeLabel, gbc);
		hearingTypeArray = OUTCReportDropDownPopulation.getHearingTypes();

		gbc.gridx++;
		gbc.weightx = 0.95;
		defaultHearingTypeCombo = new XComboBox();
		defaultHearingTypeCombo.setModel(new DefaultComboBoxModel(hearingTypeArray.toArray()));
		defaultHearingTypeCombo.setRenderer(new OUTCReportDropDownBoxCellRenderer());
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
		requiredJudgeTypeLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"OUTCReport.panel_label_RequiredJudgeTypeLabel"));
		requiredJudgeTypePanel.add(requiredJudgeTypeLabel, gbc);
		gbc.gridx++;
		gbc.weightx = 0.95;
		if (requiredJudgeTypesArray == null) {
			requiredJudgeTypesArray = OUTCReportDropDownPopulation.getRequiredJudgeTypes();
		}
		requiredJudgeTypeCombo = new XComboBox();
		requiredJudgeTypeCombo.setModel(new DefaultComboBoxModel(requiredJudgeTypesArray.toArray()));
		requiredJudgeTypeCombo.setRenderer(new OUTCReportDropDownBoxCellRenderer());
		requiredJudgeTypePanel.add(requiredJudgeTypeCombo, gbc);

		return requiredJudgeTypePanel;
	}

	private JPanel initsecureCourtroomPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		secureCourtroomPanel = new JPanel();
		secureCourtroomPanel.setLayout(new GridBagLayout());
		// Add the panel elements
		secureCourtroomCheckBox = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"OUTCReport.panel_label_SecureCourtroomCheckBoxLabel"));
		secureCourtroomPanel.add(secureCourtroomCheckBox, gbc);
		return secureCourtroomPanel;
	}

	private JPanel initJuvenileOnlyPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		juvenileOnlyPanel = new JPanel();
		juvenileOnlyPanel.setLayout(new GridBagLayout());
		// Add the panel elements
		juvenileOnlyCheckBox = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"OUTCReport.panel_label_JuvenileOnlyCheckBoxLabel"));
		juvenileOnlyPanel.add(juvenileOnlyCheckBox, gbc);
		return juvenileOnlyPanel;
	}

	private JPanel initCaseClassPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		caseClassPanel = new JPanel();
		caseClassPanel.setLayout(new GridBagLayout());

		// add Case Class Label panel
		caseClassLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"OUTCReport.panel_label_CaseClassLabel"));
		caseClassPanel.add(caseClassLabel, gbc);

		// Add the panel elements
		gbc.gridx++;
		// Option of any case class - by default is selected and disabled
		caseClassSelectAnyCheckBox = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"OUTCReport.panel_label_caseClassSelectAnyCheckBoxLabel"));
		caseClassSelectAnyCheckBox.setSelected(true);
		caseClassSelectAnyCheckBox.setEnabled(false);
		caseClassPanel.add(caseClassSelectAnyCheckBox, gbc);

		gbc.gridy++;
		// Option of case class 1 - by default is disabled
		caseClassSelectOneCheckBox = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"OUTCReport.panel_label_caseClassSelectOneCheckBoxLabel"));
		caseClassSelectOneCheckBox.setActionCommand("1");
		caseClassSelectOneCheckBox.setEnabled(false);
		caseClassPanel.add(caseClassSelectOneCheckBox, gbc);

		gbc.gridy++;
		// Option of case class 2 - by default is disabled
		caseClassSelectTwoCheckBox = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"OUTCReport.panel_label_caseClassSelectTwoCheckBoxLabel"));
		caseClassSelectTwoCheckBox.setActionCommand("2");
		caseClassSelectTwoCheckBox.setEnabled(false);
		caseClassPanel.add(caseClassSelectTwoCheckBox, gbc);

		gbc.gridy++;
		// Option of case class 3 - by default is disabled
		caseClassSelectThreeCheckBox = new JCheckBox(XHIBITConstant.getResource(
				XhibitBundles.XhibitAdminResources, "OUTCReport.panel_label_caseClassSelectThreeCheckBoxLabel"));
		caseClassSelectThreeCheckBox.setActionCommand("3");
		caseClassSelectThreeCheckBox.setEnabled(false);
		caseClassPanel.add(caseClassSelectThreeCheckBox, gbc);

		caseClassSelectAnyCheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (caseClassSelectAnyCheckBox.isSelected()) {
					// If Any checkbox is ticked then deselect options 1-3 and disable them
					caseClassSelectOneCheckBox.setSelected(false);
					caseClassSelectOneCheckBox.setEnabled(false);
					caseClassSelectTwoCheckBox.setSelected(false);
					caseClassSelectTwoCheckBox.setEnabled(false);
					caseClassSelectThreeCheckBox.setSelected(false);
					caseClassSelectThreeCheckBox.setEnabled(false);
				} else {
					// Any checkbox is not ticked so enable options 1-3
					caseClassSelectOneCheckBox.setEnabled(true);
					caseClassSelectTwoCheckBox.setEnabled(true);
					caseClassSelectThreeCheckBox.setEnabled(true);
				}
			}
		});

		return caseClassPanel;
	}
	
	private String getCaseClassSelected() {
		StringBuilder selected = new StringBuilder(""); 
	
		if (caseClassSelectOneCheckBox.isSelected()) {
			selected.append(caseClassSelectOneCheckBox.getActionCommand());
		};
		if (caseClassSelectTwoCheckBox.isSelected()) {
			if (!"".equals(selected.toString())) {
				selected.append(",");	
			}
			selected.append(caseClassSelectTwoCheckBox.getActionCommand());
		};
		if (caseClassSelectThreeCheckBox.isSelected()) {
			if (!"".equals(selected.toString())) {
				selected.append(",");	
			}
			selected.append(caseClassSelectThreeCheckBox.getActionCommand());
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
		timeEstimateLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"OUTCReport.panel_label_TimeEstimateLabel"));
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
		timeEstimateAndLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"OUTCReport.panel_label_TimeEstimateAndLabel"));
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
		casesOlderThanLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "OUTCReport.panel_casesOlderThanLabel"));
		casesOlderThanPanel.add(casesOlderThanLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 0.15;
		timeEstWeeks = new XTextField(2);
		timeEstWeeks.setMaxLength(2);
		timeEstWeeks.setNumeric(true);
		casesOlderThanPanel.add(timeEstWeeks, gbc);

		gbc.gridx++;
		gbc.weightx = 0.10;

		weeksOldLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "OUTCReport.panel_label_weeksOldLabel"));
		casesOlderThanPanel.add(weeksOldLabel, gbc);

		return casesOlderThanPanel;
	}
	
	private JPanel initshowNotesPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		showNotesPanel = new JPanel();
		showNotesPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant
				.getResource(XhibitBundles.XhibitAdminResources, "OUTCReport.panel_label_ShowNotesBorderLabel")));
		showNotesPanel.setLayout(new GridBagLayout());
		// Add the panel elements

		priorityCheckBox = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"OUTCReport.panel_label_PriorityCheckBoxLabel"));
		showNotesPanel.add(priorityCheckBox, gbc);
		priorityCheckBox.setSelected(true);

		gbc.gridx++;
		restrictedCheckBox = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"OUTCReport.panel_label_RestrictedCheckBoxLabel"));
		showNotesPanel.add(restrictedCheckBox, gbc);
		restrictedCheckBox.setSelected(true);
		gbc.gridx++;
		standardCheckBox = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"OUTCReport.panel_label_StandardCheckBoxLabel"));
		showNotesPanel.add(standardCheckBox, gbc);

		return showNotesPanel;
	}



	private JPanel initsortByPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		sortByPanel = new JPanel();
		sortByPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant
				.getResource(XhibitBundles.XhibitAdminResources, "OUTCReport.panel_label_SortByBorderLabel")));

		sortByPanel.setLayout(new GridBagLayout());

		sortByRadioButtonGroup = new ButtonGroup();
		// Add the panel elements
		
		

		ageRadioButton = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"OUTCReport.panel_label_AgeRadioButtonLabel"));
		sortByRadioButtonGroup.add(ageRadioButton);
		sortByPanel.add(ageRadioButton, gbc);

		gbc.gridx++;
		caseNumberRadioButton = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"OUTCReport.panel_label_CaseNumberRadionButtonLabel"));
		sortByRadioButtonGroup.add(caseNumberRadioButton);
		sortByPanel.add(caseNumberRadioButton, gbc);
		ageRadioButton.setSelected(true);
		this.ageRadioButton.setActionCommand("AGE");
		this.caseNumberRadioButton.setActionCommand("CASENUMBER");

		return sortByPanel;
	}

	private JPanel initBCStatusPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.CENTER, XHIBITConstant.nonContainerInsets, 0, 0);
		bcStatusPanel = new JPanel();
		bcStatusPanel.setLayout(new GridBagLayout());

		// Add the panel elements
		gbc.weightx = 0.05;
		bcStatusLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "OUTCReport.panel_label_BCStatusLabel"));
		bcStatusPanel.add(bcStatusLabel, gbc);
		gbc.gridx++;
		gbc.weightx = 0.95;
		if (bcStatusArray == null) {
			bcStatusArray = OUTCReportDropDownPopulation.getBcStatus();
		}
		bcStatusCombo = new XComboBox();
		bcStatusCombo.setModel(new DefaultComboBoxModel(bcStatusArray.toArray()));
		bcStatusCombo.setRenderer(new OUTCReportDropDownBoxCellRenderer());
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
			timeEstUnitArray = OUTCReportDropDownPopulation.getTimeEstimateUnits();
		}
		timeUnitsCombo = new XComboBox(new DefaultComboBoxModel(timeEstUnitArray));
		timeUnitsCombo.setEnabled(false);
		timeUnitsPanel.add(timeUnitsCombo, gbc);

		return timeUnitsPanel;
	}

	private JPanel initPreviewPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		previewPanel = new JPanel();
		previewPanel.setLayout(new GridBagLayout());
		previewLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "OUTCReport.panel_label_PreviewLabel"));
		previewPanel.add(previewLabel, gbc);

		return previewPanel;
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


	
	private JPanel initShowNotesSortByPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		showNotesSortByPanel = new JPanel();
		showNotesSortByPanel.setLayout(new GridBagLayout());
		
		// show notes panel
		gbc.weightx = 0.60;
		showNotesPanel = initshowNotesPanel();
		showNotesSortByPanel.add(showNotesPanel);

		// sortbyPanel
		gbc.weightx = 0.40;
		gbc.gridx++;
		sortByPanel = initsortByPanel();
		showNotesSortByPanel.add(sortByPanel);

		return showNotesSortByPanel;
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
		if(update ){
			if (validateTimeEstimate()) {
				DisplayUNLCReportAction xaction = new DisplayUNLCReportAction(model,parent.getParentFrame()); 
				xaction.actionPerformed(new ActionEvent(model.getXac(),0,"call DisplayUNLCReportAction "));
			}
			// Halt the exit process
			throw new UserCancelException();
		}
	}
	
	/**
	 * Validate that both time estimate fields have been entered and the range is valid when click Ok
	 * @return true if ok to run report, else false
	 */
	private boolean validateTimeEstimate() {
		boolean valid = true;
		String from = timeEstFrom.getText();
		String to = timeEstTo.getText();
		String messageBoxTitle;
		String errorMessage;
		
		if ( (from == null || from.equals("")) && (to != null && !to.equals(""))
                || (from != null && !from.equals("")) && (to == null || to.equals(""))) {
			messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.ErrorText, "Reports.UNLC.alert.errorTitle");
        	errorMessage = XHIBITConstant.getResource(XhibitBundles.ErrorText, "Reports.UNLC.alert.timeEstimateIncompleteMessage");
			XMessageBox.alert(parent, messageBoxTitle, true, XMessageBox.ICONERROR, 
					errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			valid = false;
		}
		else if ( from != null && !from.equals("") && to != null && !to.equals("") 
				&& Integer.parseInt(from) > Integer.parseInt(to) ) {
			messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.ErrorText, "Reports.UNLC.alert.errorTitle");
        	errorMessage = XHIBITConstant.getResource(XhibitBundles.ErrorText, "Reports.UNLC.alert.timeEstimateInvalidRangeMessage");
			XMessageBox.alert(parent, messageBoxTitle, true, XMessageBox.ICONERROR, 
					errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			valid = false;
		}
		return valid;
	}

	private void moveScreenToModel() throws CSRecoverableException {
		// Update the model from the screen
		String caseType = ((DropdownCodeStringValue) caseTypeCombo.getSelectedItem()).getPrintString();
		model.setCaseType(caseType != null ? caseType: null);
		String caseClass = getCaseClassSelected();
		model.setCaseClass("".equals(caseClass) ? null : caseClass );	
		String bcStatus = ((DropdownCodeStringValue) bcStatusCombo.getSelectedItem()).getPrintString();
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
		model.setPriorityAsBooean(priorityCheckBox.isSelected());
		model.setRestrictedAsBoolean(restrictedCheckBox.isSelected());
		model.setStandardAsBoolean(standardCheckBox.isSelected());
	    model.setSortBy(sortByRadioButtonGroup.getSelection().getActionCommand());		
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
	
	/**
	 * Sets up the Case Class option enablement and selected rules
	 * @param enableAny True if the Any checkbox needs to enabled, else false
	 */
	private void resetCaseClassOptions(boolean enableAny) {
		// Default state of Any option is selected, options 1-3 is unselected and disabled
		caseClassSelectAnyCheckBox.setEnabled(enableAny);
		caseClassSelectAnyCheckBox.setSelected(true);
		caseClassSelectOneCheckBox.setSelected(false);
		caseClassSelectOneCheckBox.setEnabled(false);
		caseClassSelectTwoCheckBox.setSelected(false);
		caseClassSelectTwoCheckBox.setEnabled(false);
		caseClassSelectThreeCheckBox.setSelected(false);
		caseClassSelectThreeCheckBox.setEnabled(false);
	}
}