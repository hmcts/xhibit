package uk.gov.courtservice.xhibit.client.results.RAGE;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractTextValidator;
import uk.gov.courtservice.xhibit.client.util.validation.TextValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;

/**
 * <p>
 * Title: RAGEReportPanel
 * </p>
 * <p>
 * Description: The panel which displays RAGE report
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Gurinder Brar
 * @version 1.0
 */
public class RAGEReportPanel extends XPanel  implements ValidationListener  {
    private static final long serialVersionUID = 1L;
    
    private XDatePanel limitDate = null;
    private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
	private TextValidationController limitWeeksValidation;
	private TextValidationController fromWeeksValidation;
	private JLabel limitWeeksWarningLabel;
	private JLabel fromWeeksWarningLabel;

	private boolean invalidEntry;
	
	private JLabel includeCases1;
	private JLabel includeCases2;
	private JLabel includeCases3;
	private JLabel previewLabel;
	private JLabel bailStatus;
	private JLabel caseClass;
	
	private XTextField weekFrom;
	private XTextField weekTo;
	
	private JCheckBox chkCaseClass1;
	private JCheckBox chkCaseClass2;
	private JCheckBox chkCaseClass3;
	
	private XComboBox bcStatusCombo;
	
	private ArrayList<DropdownCodeStringValue> bcStatusArray;
	
    public RAGEReportPanel(RunRAGEReportDialog runCTRLPReportDialog) throws CSRecoverableException{
    	stepInitialise();
        jbInit();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(500,250);
	}
	
	@Override
	public void stepInitialise() throws CSRecoverableException {
		
	}
	
	private JPanel initCaseClassPanel1() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		gbc.weightx = 0.17;
		caseClass = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RAGEReport.dialog_include_case"));
				panel.add(caseClass, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.83;
		chkCaseClass1 = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RAGEReport.dialog_include_case_class1") ,false);
		panel.add(chkCaseClass1, gbc);
		
		return panel;
	}
	
	private JPanel initCaseClassPanel2() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		gbc.weightx = 0.38;
		caseClass = new JLabel(
				"");
				panel.add(caseClass, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.62;
		chkCaseClass2 = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RAGEReport.dialog_include_case_class2") ,false);
		panel.add(chkCaseClass2, gbc);
		
		return panel;
	}
	
	private JPanel initCaseClassPanel3() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		gbc.weightx = 0.38;
		caseClass = new JLabel(
				"");
				panel.add(caseClass, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.62;
		chkCaseClass3 = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RAGEReport.dialog_include_case_class3") ,false);
		panel.add(chkCaseClass3, gbc);
		
		return panel;
	}
	
	private JPanel initBailCustodyPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		gbc.weightx = 0.25;
		JLabel bailStatus = new JLabel(
		XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RAGEReport.bailStatus"));
		panel.add(bailStatus, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.75;
		
		bcStatusArray = getBcStatus();
		
		bcStatusCombo = new XComboBox();
		bcStatusCombo.setModel(new DefaultComboBoxModel(bcStatusArray.toArray()));
		bcStatusCombo.setSelectedIndex(0);
		bcStatusCombo.setRenderer(new DefaultListCellRenderer());
		panel.add(bcStatusCombo, gbc);
		
		return panel;
	}
	
	private JPanel initIncludeCasesPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		gbc.insets = XHIBITConstant.errorLabelInsets;		
		gbc.weightx = 1.0;
		gbc.gridwidth = 5;
		panel.add(getLimitWeeksWarningLabel(), gbc);
		gbc.insets = XHIBITConstant.nonContainerInsets;
			
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy++;
		//gbc.weightx = 0.4;
		JLabel includeCases1 = new JLabel(
		XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RAGEReport.dialog_cases1"));
		panel.add(includeCases1, gbc);
		
		gbc.gridx++;
		//gbc.weightx = 0.1;
		panel.add(getFromWeeksPanel(),gbc);
		
		gbc.gridx++;
		//gbc.weightx = 0.4;
		JLabel includeCases2 = new JLabel(
		XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RAGEReport.dialog_cases2"));
		panel.add(includeCases2, gbc);
		
		gbc.gridx++;
		//gbc.weightx = 0.05;
		panel.add(getLimitWeeksPanel(),gbc);
		
		gbc.gridx++;
		gbc.weightx = 1.0;
		JLabel includeCases3 = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RAGEReport.dialog_cases3"));
				this.add(includeCases3, gbc);
		panel.add(includeCases3,gbc);
		
		return panel;
	}
	
	public void jbInit() {
	
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		
		this.setLayout(new GridBagLayout());
	 
	    gbc.weighty = 0.20;
		JPanel panel1 = initIncludeCasesPanel();
		this.add(panel1, gbc);
		
	    gbc.weighty = 0.20;
	    gbc.gridy++;
	    JPanel panel2 = initBailCustodyPanel();
	    this.add(panel2, gbc);
	    
	    gbc.weighty = 0.20;
	    gbc.gridy++;
	    JPanel panel3 = initCaseClassPanel1();
	    this.add(panel3, gbc);
	    
	    gbc.weighty = 0.20;
	    gbc.gridy++;
	    JPanel panel4 = initCaseClassPanel2();
	    this.add(panel4, gbc);
	    
	    gbc.weighty = 0.20;
	    gbc.gridy++;
	    JPanel panel5 = initCaseClassPanel3();
	    this.add(panel5, gbc);
		
		gbc.gridy++;
		
		previewLabel= new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RAGEReport.dialog_text"));
		this.add(previewLabel, gbc);
		
	}
	
	private ArrayList<DropdownCodeStringValue> getBcStatus() {
		ArrayList<DropdownCodeStringValue> results = new ArrayList<DropdownCodeStringValue>();
		results.add(new DropdownCodeStringValue("All Cases","", ""));
		results.add(new DropdownCodeStringValue("Bail Cases Only", "B","B"));
		results.add(new DropdownCodeStringValue("Custody Cases Only", "C","C"));
		return results;
	}
	
	
	private JLabel getLimitWeeksWarningLabel() {
    	if (limitWeeksWarningLabel == null) {
    		limitWeeksWarningLabel = new JLabel(" ");
    	}
    	return limitWeeksWarningLabel;
    }
	
	private JLabel getFromWeeksWarningLabel() {
    	if (fromWeeksWarningLabel == null) {
    		fromWeeksWarningLabel = new JLabel(" ");
    	}
    	return limitWeeksWarningLabel;
    }
	
	private XTextField getLimitWeeksPanel() {
		
		weekTo = new XTextField(3);
		weekTo.setMaxLength(3);
		weekTo.setNumeric(true);

		limitWeeksValidation = ValidationControllerFactory.createText(this, weekTo,
				 getLimitWeeksWarningLabel(), new AbstractTextValidator() {
				@Override
				public void validate(JTextComponent target, List<String> errors) {
					// Variables to determine if weeks to and weeks from are populated
					boolean weeksToEmpty = (weekTo.getText() == null || weekTo.getText().equals(""));
					boolean weeksFromEmpty = (weekFrom.getText() == null || weekFrom.getText().equals(""));
					
					if (weeksToEmpty) {
						// No value in weeks to specified
						errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText, "Reports.RAGE.error.noWeeksTo"));
					} 
					else if (!weeksFromEmpty && !weeksToEmpty) {
						// Both weeks from and weeks to are set, validate the difference between them
						Integer diff = Integer.parseInt(weekTo.getText()) -  Integer.parseInt(weekFrom.getText());
						if ( diff < 0 ) {
							// Weeks from is greater than weeks to
							errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText, "Reports.RAGE.error.negativeWeeksDifference"));
						}
						else if ( diff > 14 ) {
							// The maximum difference is 14 weeks
							errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText, "Reports.RAGE.error.maximumWeeksDifference"));
						}
						else {
							// Both weeks from and weeks to are populated and the difference is valid
							fromWeeksValidation.clearErrors();
							limitWeeksValidation.clearErrors();
						}
					} else {
						// Weeks to is populated but weeks from is not so clear all errors
						limitWeeksValidation.clearErrors();
					}
				}
			});
	
		 validationControllers.add(limitWeeksValidation);
		 
		 return weekTo;
	}
	
	private XTextField getFromWeeksPanel() {
		
		weekFrom = new XTextField(3);
		weekFrom.setMaxLength(3);
		weekFrom.setNumeric(true);

		fromWeeksValidation = ValidationControllerFactory.createText(this, weekFrom,
				 getFromWeeksWarningLabel(), new AbstractTextValidator() {
				@Override
				public void validate(JTextComponent target, List<String> errors) {
					// Variables to determine if weeks to and weeks from are populated
					boolean weeksToEmpty = (weekTo.getText() == null || weekTo.getText().equals(""));
					boolean weeksFromEmpty = (weekFrom.getText() == null || weekFrom.getText().equals(""));
					
					if (weeksFromEmpty) {
						errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText, "Reports.RAGE.error.noWeeksFrom"));
					}
					else if (!weeksFromEmpty && !weeksToEmpty) {
						// Both weeks from and weeks to are set, validate the difference between them
						Integer diff = Integer.parseInt(weekTo.getText()) -  Integer.parseInt(weekFrom.getText());
						if ( diff < 0 ) {
							// Weeks from is greater than weeks to
							errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText, "Reports.RAGE.error.negativeWeeksDifference"));
						}
						else if ( diff > 14 ) {
							// The maximum difference is 14 weeks
							errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText, "Reports.RAGE.error.maximumWeeksDifference"));
						}
						else {
							// Both weeks from and weeks to are populated and the difference is valid
							fromWeeksValidation.clearErrors();
							limitWeeksValidation.clearErrors();
						}
					}
					else {
						// Weeks from is populated but weeks to is not so clear all errors
						fromWeeksValidation.clearErrors();
					} 
				}
			});
	
		 validationControllers.add(fromWeeksValidation);
		 
		 return weekFrom;
	}
	
	public XDatePanel getLimitDate() {
		return limitDate;
	}

	public boolean isInvalidEntry() {
		return invalidEntry;
	}

	public void setInvalidEntry(boolean invalidEntry) {
		this.invalidEntry = invalidEntry;
	}
	
	@Override
	public void stepUpdateViewState() throws CSRecoverableException{
		
    }

	@Override
	public void stepActivate() throws CSRecoverableException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		// TODO Auto-generated method stub

	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		 setInvalidEntry((validationController.hasErrors()
					|| !ValidationControllerFactory.validateComponents(validationControllers)));
		
	}
	
	private String getBCStatus() {
		return ((DropdownCodeStringValue) bcStatusCombo.getSelectedItem()).getPrintString();
	}
	
	private String getCaseClassList() {
		String list="";
		if (chkCaseClass1.isSelected()) {
			list+=chkCaseClass1.getText()+",";
		}
		if (chkCaseClass2.isSelected()) {
			list+=chkCaseClass2.getText()+",";
		}
		if (chkCaseClass3.isSelected()) {
			list+=chkCaseClass3.getText()+",";
		}
		
		if (list!="")
			list = list.substring(0, list.length()-1);
		
		return list;
	}
	
	private String getWeeksFrom() {
		return weekFrom.getText();
	}
	
	private String getWeeksTo() {
		return weekTo.getText();
	}
	
	public ArrayList<String> getScreenSelections() {
		ArrayList<String> selections = new ArrayList<String>();
		selections.add(getWeeksFrom());
		selections.add(getWeeksTo());
		selections.add(getBCStatus());
		selections.add(getCaseClassList());
		
		return selections;
	}

	public JLabel getIncludeCases1() {
		return includeCases1;
	}


	public JLabel getIncludeCases2() {
		return includeCases2;
	}

	public JLabel getIncludeCases3() {
		return includeCases3;
	}


	public JLabel getBailStatus() {
		return bailStatus;
	}
	
}
