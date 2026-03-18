package uk.gov.courtservice.xhibit.client.actions.results.INFTRPC;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.Calendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayINFTRPCReportAction;
import uk.gov.courtservice.xhibit.client.actions.results.common.MonthYearDatePeriodReportPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;


public class INFTRPCReportPanel extends MonthYearDatePeriodReportPanel {
	
	private static final long serialVersionUID = 1L;
	private JLabel reportOptionLabel;
	private JPanel reportOptionPanel;
	private JPanel dateSelectionPanel;
	private JComboBox reportOptionCombo;
	
    public INFTRPCReportPanel(XDialog parent, INFTRPCModel model) throws CSRecoverableException{
    	super(parent, model);
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if(update){
			DisplayINFTRPCReportAction xaction = new DisplayINFTRPCReportAction(getModel(),parent.getParentFrame()); 
			xaction.actionPerformed(new ActionEvent(getModel().getXac(),0,"call DisplayINFTRPCReportAction "));
			// Halt the exit process
			throw new UserCancelException();
		}
	}

	@Override
	protected JPanel initDateSelectionPanel(){
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		this.setPreferredSize(new Dimension(400, 200));	
		dateSelectionPanel =  new JPanel();
		dateSelectionPanel.setLayout(new GridBagLayout());
		// Get the standard date objects
		dateSelectionPanel.add(super.initDateSelectionPanel(), gbc);
		
		gbc.weighty = 0.50;
		gbc.gridy++;
		reportOptionPanel = initReportOptionPanel();
		dateSelectionPanel.add(reportOptionPanel,gbc);
		
		return dateSelectionPanel;
	}
	
	private JPanel initReportOptionPanel(){
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		reportOptionPanel=  new JPanel();
		reportOptionPanel.setLayout(new GridBagLayout());
		
		// Add the panel elements
		gbc.weightx = 0.05;
		reportOptionLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"INFTRPCReport.panel_label_ReportOptionLabel"));
		reportOptionPanel.add(reportOptionLabel, gbc);
		
		gbc.gridx++;
		gbc.gridwidth = 2;
		gbc.weightx = 0.95;
		reportOptionCombo = new JComboBox();
		reportOptionCombo.setModel(new DefaultComboBoxModel(getReportOptions()));
		reportOptionPanel.add(reportOptionCombo,gbc);
		return reportOptionPanel;
		
	}
	@Override
	protected void moveScreenToModel() throws CSRecoverableException {
		// Update the model from the screen
		String countsPercentageReport = (String) reportOptionCombo.getSelectedItem();		
		getModel().setCountsPercentageReport(countsPercentageReport);
		String caseNumbersReport = (String) reportOptionCombo.getSelectedItem();
		getModel().setCaseNumbersReport(caseNumbersReport);
		super.moveScreenToModel();
	}
	
	private INFTRPCModel getModel(){
		return (INFTRPCModel)super.model;
		
	}
	@Override
	public void stepDeactivate() throws CSRecoverableException {
		 moveScreenToModel();

	}
	
	private String[]getReportOptions(){
		String[]options = new String[]{getModel().COUNTS_PERCENTAGE_REPORT,  getModel().CASENUMBER_DETAILS_REPORT};
		return options;
	}

	@Override
	protected String getSelectMonthLabel() {
		return XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"INFTRPCReport.panel_label_Select_Month_Label");
	}

	@Override
	protected String getPreviewLabel() {
		return XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "INFTRPCReport.panel_label_PreviewLabel");
	}
	
	@Override
	protected String[]getYears(){
		String[]years = new String[100];
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int thisMonth = cal.get(Calendar.MONTH);
		int thisYear = thisMonth==0?year-1:year;
		for (int i = 0;i < years.length;i++)
			years[i]=Integer.toString(thisYear -i);
		return years;
	}
}

