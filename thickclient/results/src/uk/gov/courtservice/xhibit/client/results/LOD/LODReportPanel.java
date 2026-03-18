package uk.gov.courtservice.xhibit.client.results.LOD;

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
 * Title: LODReportPanel
 * </p>
 * <p>
 * Description: The panel which displays LOD report
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
public class LODReportPanel extends XPanel implements ValidationListener  {
    private static final long serialVersionUID = 1L;
    private XDatePanel startDate = null;
    private XDatePanel endDate = null;
    private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
	private DateValidationController startDateValidation;
	private DateValidationController endDateValidation;
	private JLabel endDateWarningLabel;
	private JLabel startDateWarningLabel;
	private boolean invalidEntry;
	
	public LODReportPanel(RunLODReportDialog runLODReportDialog) throws CSRecoverableException{
		stepInitialise();
        jbInit();
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(750,150);
		
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
		this.add(getStartDateWarningLabel(), gbc);
		gbc.insets = XHIBITConstant.nonContainerInsets;
		
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0.5;
		JLabel startDateLabel = new JLabel(
		XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "LODReport.dialog_from_date"));
		this.add(startDateLabel, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.5;
		startDate = new XDatePanel(this, null, false);
		this.add(getStartDatePanel(), gbc);
		
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.weightx = 0.1;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy++;
		this.add(getEndDateWarningLabel(), gbc);
		gbc.insets = XHIBITConstant.nonContainerInsets;		

		gbc.gridy++;
		gbc.weightx = 0.5;
		JLabel endDateLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "LODReport.dialog_to_date"));
		this.add(endDateLabel, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.4;
		endDate = new XDatePanel(this, null, false);
		this.add(getEndDatePanel(), gbc);
		
		gbc.gridx = 0;
		gbc.gridy++;
		JLabel previewLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "LODReport.dialog_preview"));
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
					 errors.add("Start Date must be on or before End Date");
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
					 errors.add("End Date must be on or after Start Date");
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
