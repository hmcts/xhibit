package uk.gov.courtservice.xhibit.client.results.DARTS;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractDateValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;

/**
 * <p>
 * Title: DARTSReportPanel
 * </p>
 * <p>
 * Description: The panel which displays DARTS report
 * </p>
 * <p>
 * Copyright: Copyright (c) 2021
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Luke Gittins
 * @version 1.0
 */
public class DARTSReportPanel extends XPanel implements ValidationListener  {
    private static final long serialVersionUID = 1L;
    private XDatePanel startDate = null;
    private XDatePanel endDate = null;
    private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
	private DateValidationController startDateValidation;
	private DateValidationController endDateValidation;
	private JLabel endDateWarningLabel;
	private JLabel startDateWarningLabel;
	private boolean invalidEntry;
	
	public DARTSReportPanel(RunDARTSReportDialog runLODReportDialog) throws CSRecoverableException{
		stepInitialise();
        jbInit();
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(500,150);
		
	}
	
	@Override
	public void stepInitialise() throws CSRecoverableException {
		
	}
	
	public void jbInit() {
    GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		
		this.setLayout(new GridBagLayout());
		
		gbc.insets = XHIBITConstant.errorLabelInsets;		
		gbc.weightx = 0.1;
		gbc.gridwidth = 2;
		gbc.gridx++;
		this.add(getStartDateWarningLabel(), gbc);
		gbc.insets = XHIBITConstant.nonContainerInsets;
		
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0.5;
		JLabel startDateLabel = new JLabel(
		XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "DARTSReport.dialog_start_date"));
		this.add(startDateLabel, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.5;
		startDate = new XDatePanel(this, null, false);
		this.add(getStartDatePanel(), gbc);
		
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.weightx = 0.1;
		gbc.gridwidth = 2;
		gbc.gridx = 1;
		gbc.gridy++;
		this.add(getEndDateWarningLabel(), gbc);
		gbc.insets = XHIBITConstant.nonContainerInsets;		

		gbc.gridy++;
		gbc.weightx = 0.5;
		gbc.gridx = 0;
		JLabel endDateLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "DARTSReport.dialog_end_date"));
		this.add(endDateLabel, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.4;
		endDate = new XDatePanel(this, null, false);
		this.add(getEndDatePanel(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy++;
		JLabel previewLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "DARTSReport.dialog_preview"));
		this.add(previewLabel, gbc);
	}
	
	private JLabel getEndDateWarningLabel() {
    	if (endDateWarningLabel == null) {
    		endDateWarningLabel = new JLabel(" ");
    	}
    	return endDateWarningLabel;
    }
	
	private JLabel getStartDateWarningLabel() {
    	if (startDateWarningLabel == null) {
    		startDateWarningLabel = new JLabel(" ");
    	}
    	return startDateWarningLabel;
    }
	
	private XDatePanel getStartDatePanel(){
		 startDateValidation = ValidationControllerFactory.createDateRequired(this,
								startDate, getStartDateWarningLabel(), new AbstractDateValidator() {
		 @Override
		 public void validate(XDatePanel target, List<String> errors) {
 				if (hasDate(target) && hasDate(endDate) && getDate(target).after(getDate(endDate))) {
					 errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText,"Reports.DARTS.error.startDateAfterEndDate"));
					 return;
				} 
				
				if (hasDate(startDate)) {
					startDateValidation.clearErrors();
				}
				
				if (hasDate(endDate)) {
					endDateValidation.clearErrors();
				}
		 }});
		 
		validationControllers.add(startDateValidation);
		
		return startDate;
	 }
	
	 private XDatePanel getEndDatePanel() {
		 endDateValidation = ValidationControllerFactory.createDateValid(this,
								endDate, getEndDateWarningLabel(), new AbstractDateValidator() {
		 @Override
		 public void validate(XDatePanel target, List<String> errors) {
				if (hasDate(target) && hasDate(startDate) && getDate(target).before(getDate(startDate))) {
					 errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText,"Reports.DARTS.error.endDateBeforeStartDate"));
					 return;
				} 
				
				if (hasDate(startDate)) {
					startDateValidation.clearErrors();
				}
				
				if (hasDate(endDate)) {
					endDateValidation.clearErrors();
				}
		}});
		 
		validationControllers.add(endDateValidation);
		
		return endDate;
	 }
	 
	 @Override
	 public void validationUpdatedView(ValidationController<?> validationController) {
		 setInvalidEntry((validationController.hasErrors()
					|| !ValidationControllerFactory.validateComponents(validationControllers)));
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

	public XDatePanel getStartDate() {
		return startDate;
	}

	public XDatePanel getEndDate() {
		return endDate;
	}

	public boolean isInvalidEntry() {
		return invalidEntry;
	}

	public void setInvalidEntry(boolean invalidEntry) {
		this.invalidEntry = invalidEntry;
	}
}
