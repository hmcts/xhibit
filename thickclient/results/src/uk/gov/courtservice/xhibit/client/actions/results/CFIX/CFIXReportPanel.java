package uk.gov.courtservice.xhibit.client.actions.results.CFIX;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Calendar;

import javax.swing.JLabel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: CFIXReportPanel
 * </p>
 * <p>
 * Description: The panel which displays CFIX report
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Ervin P
 * @version 1.0
 */

public class CFIXReportPanel extends XPanel {
	private static final long serialVersionUID = 1L;
	private RunCFIXReportDialog parentDialog = null ;
	private JLabel endDateWarningLabel;
	private JLabel startDateWarningLabel;
    private XDatePanel startDate = null;
    private XDatePanel endDate =  null;
    
    public CFIXReportPanel(RunCFIXReportDialog runCFIXReportDialog) throws CSRecoverableException{
    	parentDialog = runCFIXReportDialog;
    	stepInitialise();	
	}

	private JLabel getEndDateWarningLabel() {
    	if (endDateWarningLabel == null) {
    		endDateWarningLabel = new JLabel(" ");
    		endDateWarningLabel.setForeground(Color.RED);
    	}
    	return endDateWarningLabel;
    }
	
	private JLabel getStartDateWarningLabel() {
    	if (startDateWarningLabel == null) {
    		startDateWarningLabel = new JLabel(" ");
    		startDateWarningLabel.setForeground(Color.RED);
    	}
    	return startDateWarningLabel;
    }
	
	/**
	 * @return the priorToDateField
	 */
	public XDatePanel getStartDateField() {
		if(startDate == null){
			startDate = new XDatePanel(this, Calendar.getInstance(), true, getStartDateWarningLabel(), "after");
		}
		return startDate;
	}
	
	/**
	 * @return the endDateField
	 */
	public XDatePanel getEndDateField() {
		if(endDate == null){
			endDate =  new XDatePanel(this, null, false, getEndDateWarningLabel(), "after");
		}
		return endDate;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(500,100);		
	}
	
	@Override
	public void stepInitialise() throws CSRecoverableException {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		this.add(new JLabel(ResourceBundleHelper.getResource(XhibitBundles.XhibitAdminResources,"CFIXReport.Hearing_start_label")), gbc);
 		
		gbc.gridx++;
		this.add( getStartDateField(), gbc); 	
			
		gbc.insets = XHIBITConstant.errorLabelInsets;		
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy++;
		this.add(getStartDateWarningLabel(), gbc);
		gbc.insets = XHIBITConstant.nonContainerInsets;
		
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 1;
		this.add(new JLabel(ResourceBundleHelper.getResource(XhibitBundles.XhibitAdminResources,"CFIXReport.Hearing_end_label")), gbc);                 
 		
		gbc.gridx++;
		this.add(getEndDateField(), gbc);	
		
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy++;
		this.add(getEndDateWarningLabel(), gbc);
		gbc.insets = XHIBITConstant.nonContainerInsets;	
	}
	
	@Override
	public void stepUpdateViewState() throws CSRecoverableException{
		if (parentDialog != null) {
			getStartDateField().validateDate();
            boolean enable = !getStartDateField().hasError();
           if(getStartDateField().getDate() != null && getEndDateField().getDate() != null 
        		  && getStartDateField().getDate().after(getEndDateField().getDate())) {
        	   getEndDateWarningLabel().setText(ResourceBundleHelper.getResource(XhibitBundles.XhibitAdminResources,"CFIXReport.start_date_after_end_date_message"));
        	   getEndDateWarningLabel().setVisible(true);
        	   enable = false;
           }else if(!getEndDateField().hasError()) {
        	   getEndDateWarningLabel().setVisible(false);
           }
           ((OkCancelPanel) parentDialog.getButtonPanel()).okButton.setEnabled(enable);
       }
    }

	@Override
	public void stepActivate() throws CSRecoverableException {
		// initialise display
		stepUpdateViewState();
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
}

