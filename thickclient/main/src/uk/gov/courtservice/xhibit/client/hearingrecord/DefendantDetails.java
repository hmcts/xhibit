package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import mseries.Calendar.MFieldListener;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordConstants;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.DefHearingRecordValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRDefendantValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.hearingrecord.EditDefendantSpecialAction;
import uk.gov.courtservice.xhibit.client.casemanagement.DropdownBoxCellRender;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;

/**
 * <p>
 * Title: DefendantDetails
 * </p>
 * <p>
 * Description: The tab that contains the defendant details on the hearing
 * record screen.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sherie De Silva
 * @version 1.0
 */
public class DefendantDetails extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final int CUSTODY_TIME_LIMIT = 181;


	// only to be set in the consructor
	private final HearingRecordModel model;

	private JTextField firstNameText;
	
	private JTextField middleInitialText;

	private JTextField surnameText;

	private JTextField lastConvDate;

	private JTextField ticsText;

	private JTextField driverNumberText;

	private JTextField licenceTypeText;
	private JTextField licenceIssueText;
	
	private JButton defEditBtn;

	private JButton editBtn;
	
	//section 41
	private JRadioButton appMadeYesRb;

	private JRadioButton appMadeNoRb;

	private JRadioButton appGrantedRb;

	private JRadioButton appRefusedRb;
	
	private JRadioButton appMadeAtTrialRb;

	private JRadioButton appMadeAdvanceOfTrialRb;
	
	//ctl
	private XDatePanel ctlDate;
	private JTextField ctlTf;
	private String currDate ;


	private XComboBox ctlRequired;
	private JCheckBox ctlCheck;

	//bail status
	private XComboBox bCStatusDropDown;
	private ArrayList<DropdownCodeStringValue> bcStatus;
	public DefendantDetails(HearingRecordModel model) {
		this.model = model;
		jbInit();
	}

	public void jbInit() {
		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new GridBagLayout());

		final JPanel defDetPanel = new JPanel();
		defDetPanel.setLayout(new GridBagLayout());
		if (model.isAppealType()) {
			defDetPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
					XHIBITConstant.getResource(XhibitBundles.HearingRecord, "appellant")));
		} else {
			defDetPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
					XHIBITConstant.getResource(XhibitBundles.HearingRecord, "defendant")));
		}
		bcStatus = new ArrayList<DropdownCodeStringValue>();
		DropdownCodeStringValue def= new DropdownCodeStringValue(" ", " ");
		DropdownCodeStringValue bail= new DropdownCodeStringValue(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "bail"), HearingRecordConstants.BAIL_STATUS);
		DropdownCodeStringValue custody= new DropdownCodeStringValue(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "custody"), HearingRecordConstants.CUSTODY_STATUS);
		DropdownCodeStringValue inCare= new DropdownCodeStringValue(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "inCare"), HearingRecordConstants.IN_CASE_STATUS);
		DropdownCodeStringValue notApplicable= new DropdownCodeStringValue(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "notApplicable"), HearingRecordConstants.NOT_APPLICABLE_STATUS);

		bcStatus.add(def);
		bcStatus.add(bail);
		bcStatus.add(custody);
		bcStatus.add(inCare);
		bcStatus.add(notApplicable);

		
		final int nameFieldWidth = 40;
		firstNameText = new JTextField(nameFieldWidth);
		firstNameText.setEditable(false);
		middleInitialText = new JTextField(nameFieldWidth);
		middleInitialText.setEditable(false);
		surnameText = new JTextField(nameFieldWidth);
		surnameText.setEditable(false);
		lastConvDate = new JTextField(12);
		lastConvDate.setEditable(false);
		ticsText = new JTextField(5);
		ticsText.setEditable(false);
		driverNumberText = new JTextField(12);
		driverNumberText.setEditable(false);
		
		licenceTypeText = new JTextField( 16 );
		licenceTypeText.setEditable( false );
		
		licenceIssueText = new JTextField( 4 );
		licenceIssueText.setEditable( false );
		
		defEditBtn = new JButton();
		XhibitActions.getAction(model.getXac(), XhibitActions.EditDefendant).setCaller(this);
		defEditBtn.setAction(XhibitActions.getAction(model.getXac(), XhibitActions.EditDefendant));

		editBtn = new JButton();
		XhibitActions.getAction(model.getXac(), XhibitActions.EditDefendantSpecial).setCaller(this);
		XhibitActions.getAction(model.getXac(), XhibitActions.EditDefendantSpecial).setModel(this.model);
		((EditDefendantSpecialAction) XhibitActions.getAction(model.getXac(), XhibitActions.EditDefendantSpecial))
				.setCrestFormAFieldsOnly(true);
		editBtn.setAction(XhibitActions.getAction(model.getXac(), XhibitActions.EditDefendantSpecial));
		editBtn.setText(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "edit"));

		int row = 0;
		/* First Name */
		defDetPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "firstName")),
				new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(4, 4, 4, 4), 0, 0));
		defDetPanel.add(firstNameText, new GridBagConstraints(1, row++, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		/* Middle Name */
		defDetPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "middleName")),
				new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(4, 4, 4, 4), 0, 0));
		defDetPanel.add(middleInitialText, new GridBagConstraints(1, row++, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		/* Surname */
		defDetPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "lastName")),
				new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(4, 4, 4, 4), 0, 0));
		defDetPanel.add(surnameText, new GridBagConstraints(1, row++, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		/* Date of last conviction */
		defDetPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "dateOfLastConviction")),
				new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(4, 4, 4, 4), 0, 0));
		defDetPanel.add(lastConvDate, new GridBagConstraints(1, row, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		/* Defendant Edit button */
		defDetPanel.add(defEditBtn, new GridBagConstraints(3, row++, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		/* Number of TICs & Driver Number*/
		defDetPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "numberOfTics")),
				new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(4, 4, 4, 4), 0, 0));
		
		JPanel licenceDetails = new JPanel();
		licenceDetails.add( ticsText );
		licenceDetails.add( new JLabel( "      " ));
		licenceDetails.add( new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "driverNumber")) );
		licenceDetails.add( new JLabel( "  " ));
		licenceDetails.add( driverNumberText );
		
		defDetPanel.add( licenceDetails, new GridBagConstraints( 1, row++, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		defDetPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "licenceType")),
				new GridBagConstraints( 0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(4, 4, 4, 4), 0, 0 ));
		
		//	Collect the Licence type elements together:
		JPanel typeDetails = new JPanel();
		typeDetails.add( licenceTypeText );
		typeDetails.add( new JLabel( "    " ) );
		typeDetails.add( new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "issueNumber" )));
		typeDetails.add( new JLabel ( " " ) );
		typeDetails.add( licenceIssueText );
		
		defDetPanel.add( typeDetails, new GridBagConstraints( 1, row, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		
		/* Edit button */
		defDetPanel.add(editBtn, new GridBagConstraints(1, row++, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		containerPanel.add(defDetPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));

		final JPanel casePanel = new JPanel();
		casePanel.setLayout(new GridBagLayout());
		casePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
				XHIBITConstant.getResource(XhibitBundles.HearingRecord, "case")));

		XhibitActions.getAction(getModel().getXac(), XhibitActions.OpenSearchColMagCourts).setCaller(this);

		// End Bail status
		bCStatusDropDown = new XComboBox();
		bCStatusDropDown.setModel(new DefaultComboBoxModel(bcStatus.toArray()));
		bCStatusDropDown.setSelectedIndex(-1);
		bCStatusDropDown.setRenderer(new DropdownBoxCellRender());
		bCStatusDropDown.setSelectedIndex(0);

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel pan = new JPanel();

		pan.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "bailStatusAtEnd")),gbc);

		gbc.gridx++;
		pan.add(bCStatusDropDown, gbc);
		
		gbc.gridx++;
		pan.add(new JLabel("CTL?"), gbc);
		
		ctlRequired = new XComboBox();
		String[]vals = {"","Y","N"};
		ctlRequired.setModel(new DefaultComboBoxModel(vals));
		ctlRequired.setEnabled(false);
		ctlRequired.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if(ctlRequired.getSelectedIndex()==0 || ctlRequired.getSelectedItem().equals("N")) {
					enableTf();
				} else if(ctlRequired.getSelectedItem().equals("Y")) {
					enableDateField();
				}
			}
		});
		gbc.gridx++;
		pan.add(ctlRequired, gbc);
		
		if (ctlTf == null) {
			ctlTf = new JTextField("");
			ctlTf.setEditable(false);
			ctlTf.setVisible(false);
		}
		gbc.gridx++;
		pan.add(ctlTf, gbc);
		
		if (ctlDate == null) { 
			ctlDate = new XDatePanel(this, null, false);
			ctlTf.setPreferredSize(ctlDate.getMinimumSize());
			ctlDate.setVisible(false);
		}
		gbc.gridx++;
		pan.add(ctlDate, gbc);
		
		gbc.gridx++;
		ctlCheck = new JCheckBox("In Custody?");
		ctlCheck.setEnabled(false);
		pan.add(ctlCheck, gbc);
		

		
		casePanel.add(pan, new GridBagConstraints(1, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		containerPanel.add(casePanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));
		
		//---------------------section 41--------------------------//
		// create section 41 panel
		final JPanel section41Panel = new JPanel();
		section41Panel.setLayout(new GridBagLayout());
		section41Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
				XHIBITConstant.getResource(XhibitBundles.HearingRecord, "section41")));
		
		//set up buttons and button groups
		appMadeYesRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "appMadeYes"));
		appMadeNoRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "appMadeNo"));

		appGrantedRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "appGranted"));
		appRefusedRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "appRefused"));
		
		appMadeAtTrialRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "appAtTrial"));
		appMadeAdvanceOfTrialRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "appAdvanceTrial"));	

		ButtonGroup appMadeBG = new ButtonGroup();
		appMadeBG.add(appMadeYesRb);
		appMadeBG.add(appMadeNoRb);
		
		ButtonGroup appGrantRefusedBG = new ButtonGroup();
		appGrantRefusedBG.add(appGrantedRb);
		appGrantRefusedBG.add(appRefusedRb);
		
		ButtonGroup appTrialBG = new ButtonGroup();
		appTrialBG.add(appMadeAtTrialRb);
		appTrialBG.add(appMadeAdvanceOfTrialRb);

		section41Panel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "section41Made")),
				new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(4, 4, 4, 4), 0, 0));

		JPanel made = new JPanel();
		made.add(appMadeYesRb);
		made.add(appMadeNoRb);
		section41Panel.add(made, new GridBagConstraints(1, 0, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		
		//application panel
		JPanel application = new JPanel();
		application.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
				XHIBITConstant.getResource(XhibitBundles.HearingRecord, "application")));
		
		JPanel granted = new JPanel();
		granted.setBorder(BorderFactory.createEtchedBorder());
		granted.add(appGrantedRb, new GridBagConstraints(0, 0, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		granted.add(appRefusedRb, new GridBagConstraints(1, 0, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		JPanel app = new JPanel();
		app.setBorder(BorderFactory.createEtchedBorder());
		app.add(appMadeAtTrialRb, new GridBagConstraints(0, 0, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		app.add(appMadeAdvanceOfTrialRb, new GridBagConstraints(1, 0, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		
		application.add(granted, new GridBagConstraints(0, 0, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		application.add(app, new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		
		section41Panel.add(application, new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		containerPanel.add(section41Panel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));

		this.add(containerPanel);
	}

	/**
	 * Setting the data to be updated.
	 */
	public void setUpdateData() {
		if (model.getHearingRecordUpdateVal() != null) {
			DefHearingRecordValue recordValue = model.getHearingRecordUpdateVal().getDefHearingRecordValue();

			if (recordValue != null) {
				//get the value from the dropdown 
				// setting the end bail status.
				DropdownCodeStringValue selected = (DropdownCodeStringValue)bCStatusDropDown.getSelectedItem();
				recordValue.setEndBailStatus(selected.getCode());
				getModel().getDefOnCaseBasicValue().setCurrentBcStatus(selected.getCode());
				
				model.getHearingRecordVal().getHearingRecordDisplayValue()
					.getHrDefendantValue().setInCustody(ctlCheck.isSelected()?"Y":"N");
						
						 
				
				if(ctlRequired.isEnabled() && ctlRequired.getSelectedItem().equals("Y")) {
					getModel().getDefOnCaseBasicValue().setCtlApplies("Y");
					try {
						getModel().getDefOnCaseBasicValue().setCustodyTimeLimit(ctlDate.getTimestamp());
					} catch (CSRecoverableException e) {
						getModel().getDefOnCaseBasicValue().setCustodyTimeLimit(null);
					}
				} else if(ctlRequired.isEnabled() && ctlRequired.getSelectedItem().equals("N")) {
					getModel().getDefOnCaseBasicValue().setCtlApplies("N");
					getModel().getDefOnCaseBasicValue().setCustodyTimeLimit(null);
				} else {
					//check if it's incorrect data we want to flag an update so it gets saved over with nulls
					if(getModel().getDefOnCaseBasicValue().getCtlApplies()!=null || getModel().getDefOnCaseBasicValue().getCustodyTimeLimit()!=null) {
						getModel().setUpdated(true);
					}
					getModel().getDefOnCaseBasicValue().setCtlApplies(null);
					getModel().getDefOnCaseBasicValue().setCustodyTimeLimit(null);
				}
				// setting section 41 details
				if (this.appMadeYesRb.isSelected()) {
					recordValue.setS41Application(HearingRecordConstants.S41_APPLICATION_YES);	
					
					if (this.appGrantedRb.isSelected()) {
						recordValue.setS41Granted(HearingRecordConstants.S41_APPLICATION_GRANTED);					
					} else if (this.appRefusedRb.isSelected()) {
						recordValue.setS41Granted(HearingRecordConstants.S41_APPLICATION_REFUSED);
					}					
					
					if (this.appMadeAtTrialRb.isSelected()) {
						recordValue.setS41ApplicationMade(HearingRecordConstants.S41_APPLICATION_MADE_AT_TRIAL);					
					} else if (this.appMadeAdvanceOfTrialRb.isSelected()) {
						recordValue.setS41ApplicationMade(HearingRecordConstants.S41_APPLICATION_MADE_ADVANCE_0F_TRIAL);
					}
					
				} else if (this.appMadeNoRb.isSelected()) {
					recordValue.setS41Application(HearingRecordConstants.S41_APPLICATION_NO);
					recordValue.setS41Granted(null);
					recordValue.setS41ApplicationMade(null);
				}								
			}
		}
	}

	private static final String TYPE_NONE = "0";
	private static final String TYPE_PROV = "1";
	private static final String TYPE_FULL = "2";
	private static final String TYPE_NONE_UK = "3";
	private static final String TYPE_DVLA = "5";

	/**
	 * Public method used to update only the read-only components on the screen
	 * from the latest value-objects
	 */
	public void populateReadOnlyComponents() {
		if ((model.getHearingRecordVal() != null)
				&& (model.getHearingRecordVal().getHearingRecordDisplayValue() != null)
				&& (model.getHearingRecordVal().getHearingRecordDisplayValue().getHrDefendantValue() != null)) {
			HRDefendantValue defValue = model.getHearingRecordVal().getHearingRecordDisplayValue()
					.getHrDefendantValue();

			if ( defValue != null )
			{
				this.firstNameText.setText(defValue.getFirstName());
				this.middleInitialText.setText(defValue.getMiddleName());
				this.surnameText.setText(defValue.getSurname());
	
				this.lastConvDate.setText(XDateFormat.format(defValue.getLastConvictionDate(), XDateFormat.DATEFORMAT));
	
				if (defValue.getNoOfTICs() != null) {
					this.ticsText.setText(defValue.getNoOfTICs().toString());
	
				}
	
				this.driverNumberText.setText(defValue.getDriverNumber());
				
				//	Licence type:
				String licenceType = defValue.getLicenceType();
				String licenceTypeKey = "0";
				
				try {
					if ( licenceType != null && !licenceType.isEmpty() ) {
						licenceTypeKey = licenceType.substring(0,1); 
					
						Map<String, String> licenceTypes = new HashMap<String, String>();
						licenceTypes.put(TYPE_NONE, XHIBITConstant.getResource(XhibitBundles.HearingRecord, "typeNone"));
						licenceTypes.put(TYPE_PROV, XHIBITConstant.getResource(XhibitBundles.HearingRecord, "typeProv" ));
						licenceTypes.put(TYPE_FULL, XHIBITConstant.getResource(XhibitBundles.HearingRecord, "typeFull" ));
						licenceTypes.put(TYPE_NONE_UK, XHIBITConstant.getResource(XhibitBundles.HearingRecord, "typeNonUk" ));
						licenceTypes.put(TYPE_DVLA, XHIBITConstant.getResource(XhibitBundles.HearingRecord, "typeDvla" ));
						
						if (licenceTypes.containsKey(licenceTypeKey) ) {
							licenceTypeText.setText( licenceTypes.get( licenceTypeKey ) );
						}
					}
				} catch (NumberFormatException nfe) {
					// Do nothing here
				}
				
				String issueNumber = "";
				
				if ( licenceTypeKey != null ) {
					if (licenceTypeKey.equals( TYPE_PROV ) || licenceTypeKey.equals( TYPE_FULL ) ) { 
						issueNumber = defValue.getIssueNumber(); 
					}
				}
				
				if ( issueNumber != null && !issueNumber.isEmpty() ) {
					licenceIssueText.setText( formatLicenceIssueNumber(issueNumber) );
				}
				else{
					licenceIssueText.setText("");
				}
				
				if(defValue.getInCustody()!=null && defValue.getInCustody().equals("Y")) {
					this.ctlCheck.setSelected(true);
				} else {
					this.ctlCheck.setSelected(false);
				}
			}
		}
	}

	/**
	 * Set data to populate the screen with
	 */
	public void populateScreen() {
		// first, populate all of the read-only components...
		populateReadOnlyComponents();

		// now, populate all of the updateable components...
		if ((model.getHearingRecordUpdateVal() != null)
				&& (model.getHearingRecordUpdateVal().getDefHearingRecordValue() != null)) {
			// Display end hearing details.
			final String endBailStatus = model.getHearingRecordUpdateVal().getDefHearingRecordValue()
					.getEndBailStatus();
			
			final String s41Application = model.getHearingRecordUpdateVal().getDefHearingRecordValue().getS41Application();
			final String s41Granted = model.getHearingRecordUpdateVal().getDefHearingRecordValue().getS41Granted();
			final String s41ApplicationMade = model.getHearingRecordUpdateVal().getDefHearingRecordValue().getS41ApplicationMade();

			//bail status 
			if (endBailStatus != null) {
				for (int j = 1; j < bCStatusDropDown.getItemCount(); j++) {
					if (((DropdownCodeStringValue) bCStatusDropDown.getItemAt(j)).getCode().equals(endBailStatus)) {
						bCStatusDropDown.setSelectedItem(bCStatusDropDown.getItemAt(j));
						break;						
					}
				}
			}
			
			//ctl applies/date
			if(model.getHearingRecordVal().getHearingRecordDisplayValue().getHrCaseValue().getCaseType().equals("T")) {
				if(model.getDefOnCaseBasicValue()!=null) {
					final String ctlAppl = model.getDefOnCaseBasicValue().getCtlApplies();
					if (model.getXac().getApplicationCaseModel().isInEditMode(FunctionList.EExportHearingRecord) 
							&& endBailStatus != null && (endBailStatus.equals("J") || endBailStatus.equals("C"))) {
						ctlRequired.setEnabled(true);
					}
					if(ctlAppl!=null){
						
						ctlRequired.setSelectedItem(ctlAppl);
						if(ctlAppl.equals("Y")) {
							if(model.getDefOnCaseBasicValue().getCustodyTimeLimit()!=null) {
								ctlDate.setDate(model.getDefOnCaseBasicValue().getCustodyTimeLimit());
							}
							enableDateField();
						} else {
							enableTf();
						}
					} else {
						ctlRequired.setSelectedIndex(0);
						enableTf();
						}
					
				}
			} else {
				ctlRequired.setSelectedIndex(0);
				enableTf();
			}
			//SECTION 41

			//s41Application
			if (HearingRecordConstants.S41_APPLICATION_YES.equals(s41Application)) {
				this.appMadeYesRb.setSelected(true);
				this.appGrantedRb.setEnabled(true);
				this.appRefusedRb.setEnabled(true);
				this.appMadeAtTrialRb.setEnabled(true);
				this.appMadeAdvanceOfTrialRb.setEnabled(true);
			} else if (HearingRecordConstants.S41_APPLICATION_NO.equals(s41Application)) {
				this.appMadeNoRb.setSelected(true);
				this.appGrantedRb.setEnabled(false);
				this.appRefusedRb.setEnabled(false);
				this.appMadeAtTrialRb.setEnabled(false);
				this.appMadeAdvanceOfTrialRb.setEnabled(false);
			}
			if(!HearingRecordConstants.S41_APPLICATION_YES.equals(s41Application) &&
					(!HearingRecordConstants.S41_APPLICATION_NO.equals(s41Application))) {
				this.appMadeYesRb.setEnabled(true);
				this.appMadeNoRb.setEnabled(true);
				this.appGrantedRb.setEnabled(false);
				this.appRefusedRb.setEnabled(false);
				this.appMadeAtTrialRb.setEnabled(false);
				this.appMadeAdvanceOfTrialRb.setEnabled(false);
			}
			//s41Granted
			if (HearingRecordConstants.S41_APPLICATION_GRANTED.equals(s41Granted)) {
				this.appGrantedRb.setSelected(true);
			} else if (HearingRecordConstants.S41_APPLICATION_REFUSED.equals(s41Granted)) {
				this.appRefusedRb.setSelected(true);
			}
			//s41ApplicationMade
			if (HearingRecordConstants.S41_APPLICATION_MADE_AT_TRIAL.equals(s41ApplicationMade)) {
				this.appMadeAtTrialRb.setSelected(true);
			} else if (HearingRecordConstants.S41_APPLICATION_MADE_ADVANCE_0F_TRIAL.equals(s41ApplicationMade)) {
				this.appMadeAdvanceOfTrialRb.setSelected(true);
			}
		}
	}

	/**
	 * method used to set all screen components to read only if cfa already
	 * exported.
	 */
	public void setScreenReadOnly() {
		this.defEditBtn.setEnabled(false);
		this.editBtn.setEnabled(false);
		this.bCStatusDropDown.setEnabled(false);
		this.ctlCheck.setEnabled(false);
		this.ctlDate.setEnabled(false);
		this.ctlRequired.setEnabled(false);
		//section 41
		this.appMadeYesRb.setEnabled(false);
		this.appMadeNoRb.setEnabled(false);
		this.appGrantedRb.setEnabled(false);
		this.appRefusedRb.setEnabled(false);
		this.appMadeAtTrialRb.setEnabled(false);
		this.appMadeAdvanceOfTrialRb.setEnabled(false);
	}

	/**
	 * Method that adds listeners to endbail status and collecting magistrates
	 * court
	 */
	public void addListeners(final HearingRecordPanel.UpdateListener updateListener) {
		
		if (model.getXac().getApplicationCaseModel().isInEditMode(FunctionList.EExportHearingRecord)) {
			// we only require one to be created
			final Section41ActionListener actionListener = new Section41ActionListener();
	
			// changed to use the single listener from the model
			this.bCStatusDropDown.addActionListener(new CTLActionListener());		
			this.ctlRequired.addActionListener(updateListener);
			this.ctlDate.getDateComponent().addMFieldListener(new DateListener());
			
			//section 41
			//use actionListener to ensure other radio buttons set correctly.
			this.appMadeYesRb.addActionListener(actionListener);
			this.appMadeNoRb.addActionListener(actionListener);
			
			// use the update listener from the model as no custom functionality for the below
			this.appGrantedRb.addActionListener(updateListener);
			this.appRefusedRb.addActionListener(updateListener);
			this.appMadeAtTrialRb.addActionListener(updateListener);
			this.appMadeAdvanceOfTrialRb.addActionListener(updateListener);
		}
	}
	
	private class DateListener implements MFieldListener {

		@Override
		public void fieldEntered(FocusEvent event) {
			currDate=ctlDate.getEntryField().getText();
			
		}

		@Override
		public void fieldExited(FocusEvent event) {
			if(!currDate.equals(ctlDate.getEntryField().getText())) {
				getModel().setUpdated(true);
			}
			
		}
		
	}
	
	private class Section41ActionListener implements ActionListener {
		public void actionPerformed(final ActionEvent e) {
			getModel().setUpdated(true);
			stepUpdateViewState();
		}
	}
	
	
	public void stepUpdateViewState() {
		if(this.appMadeYesRb.isSelected()){	
			this.appGrantedRb.setEnabled(true);
			this.appRefusedRb.setEnabled(true);
			this.appMadeAtTrialRb.setEnabled(true);
			this.appMadeAdvanceOfTrialRb.setEnabled(true);
		}
		else if(this.appMadeNoRb.isSelected()){	
			this.appGrantedRb.setEnabled(false);		
			this.appRefusedRb.setEnabled(false);				
			this.appMadeAtTrialRb.setEnabled(false);			
			this.appMadeAdvanceOfTrialRb.setEnabled(false);		
		}
	}
	
	/**
	 * Action listener triggered when you change the bc status dropdown
	 *
	 */
	private class CTLActionListener implements ActionListener {
		public void actionPerformed(final ActionEvent e) {
			//flag that we have updated
			getModel().setUpdated(true);
			
			DropdownCodeStringValue selected = (DropdownCodeStringValue)bCStatusDropDown.getSelectedItem();
			//sorting out the ticking/unticking of incustody 
			
			String inCustody = model.getHearingRecordUpdateVal().getDefHearingRecordValue()
					.getEndBailStatus();
			//if its the first time we've come into forma
			if(inCustody ==null) {
				boolean currentFlagVal = ctlCheck.isSelected() ;
				boolean currentlyInCustody = isInCustody(selected.getCode());
				ctlCheck.setSelected(currentFlagVal || currentlyInCustody);

			}
			else if (!inCustody.equals(selected.getCode())) {
				boolean previouslyInCustodyNowOnBail = inCustody != null && isInCustody(inCustody) && isOnBail(selected.getCode());
				boolean currentlyInCustody = isInCustody(selected.getCode());
				ctlCheck.setSelected(previouslyInCustodyNowOnBail || currentlyInCustody);
			}
		
			
			//if its bail or not applicable or it's not a T case then we need to set ctlRequired to be not enabled, hide the date and show the text field
			if( (selected.getCode().equals(HearingRecordConstants.BAIL_STATUS)||
					selected.getCode().equals(HearingRecordConstants.NOT_APPLICABLE_STATUS)||
					selected.getCode().equals(" ")) 
				|| !model.getHearingRecordVal().getHearingRecordDisplayValue().getHrCaseValue().getCaseType().equals("T")) {
				ctlRequired.setSelectedIndex(0);
				ctlRequired.setEnabled(false);
				enableTf();
				//else its a T case with in custody/care
			} else {
				//set ctl applies to null and read only
				ctlRequired.setSelectedIndex(0);
				ctlRequired.setEnabled(true);
				//set ctl date to null and display not applicable
				enableTf();//we display Tf until they have selected 'Y' in the drop down
			}
		}
		private boolean isInCustody(String bcStatus) {
			return HearingRecordConstants.IN_CASE_STATUS.equals(bcStatus) || HearingRecordConstants.CUSTODY_STATUS.equals(bcStatus);
		}
		
		private boolean isOnBail(String bcStatus) {
			return HearingRecordConstants.BAIL_STATUS.equals(bcStatus);
		}
	}
	
	public HearingRecordModel getModel() {
		return model;
	}

	public void stepValidate() throws CSValidationException {
		// ctx-2706 - end bail status must be populated
		if (bCStatusDropDown.getSelectedIndex()==0) {
			throw new CSValidationException("validation.mustselect",
					new String[] { XHIBITConstant.getResource(XhibitBundles.HearingRecord, "bailStatusAtEnd") },
					"In Crest Form A.DefendantDetails.A Bail status at end must be selected");
		}
		
		if(ctlRequired.isEnabled() && ctlRequired.getSelectedIndex()==0) {
			throw new CSValidationException("validation.mustselect",
					new String[] {"CTL Applies" },
					"In Crest Form A.DefendantDetails.A CTL Applies must be selected for In Custody/In Care");
		}
		
		if(ctlDate.isVisible()) {
			if(ctlDate.getDate()!=null 	
					&& model.getDefOnCaseBasicValue().getMagCourtFirstHearingDate()!=null){
				Calendar cal = Calendar.getInstance();
				cal.setTime( model.getDefOnCaseBasicValue().getMagCourtFirstHearingDate() );
				cal.add(Calendar.DAY_OF_YEAR, CUSTODY_TIME_LIMIT);
				if(ctlDate.getDate().before(cal)) {
					throw new CSValidationException("validation.ctldate",
							new String[] {XDateFormat.format(cal.getTime(), XDateFormat.DATEFORMAT) },
							"In Crest Form A.DefendantDetails.CTL date is too early for defendant");
				}
			}
		}
		
		
		//section 41
		if (this.appMadeYesRb.isSelected() == false && this.appMadeNoRb.isSelected() == false) {
			throw new CSValidationException("validation.s41.mustselect",
					new String[] { XHIBITConstant.getResource(XhibitBundles.HearingRecord, "section41AppMade") },
					"In Crest Form A.DefendantDetails. Section 41 Application status radio button must be selected");
		}
		
		if (this.appMadeYesRb.isSelected() == true && this.appGrantedRb.isSelected() == false && this.appRefusedRb.isSelected() == false) {
			throw new CSValidationException("validation.s41.applicationmustselect",
					new String[] { XHIBITConstant.getResource(XhibitBundles.HearingRecord, "section41GrantedRefused") },
					"In Crest Form A.DefendantDetails. Section 41 Application granted or refused and made at trial or advance of trial radio button must be selected");
		}
		
		if (this.appMadeYesRb.isSelected() == true && this.appMadeAtTrialRb.isSelected() == false && this.appMadeAdvanceOfTrialRb.isSelected() == false) {
			throw new CSValidationException("validation.s41.applicationmustselect",
					new String[] { XHIBITConstant.getResource(XhibitBundles.HearingRecord, "section41AtTrialAdvance") },
					"In Crest Form A.DefendantDetails. Section 41 Application made at trial or advance of trial radio button must be selected");
		}
		
	}
	
	
	/**
	 * Hides the text field and shows the date field.
	 */
	private void enableDateField() {
		ctlDate.setVisible(true);
		ctlTf.setVisible(false);
	}
	/**
	 * Hides the date field and shows the text field.
	 */
	private void enableTf() {
		ctlDate.setVisible(false);
		ctlTf.setVisible(true);
		if(bCStatusDropDown.getSelectedIndex()!=0 && ctlRequired.getSelectedIndex()!=0){
			ctlTf.setText("Not Applicable");
		} else {
			ctlTf.setText("");

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

}