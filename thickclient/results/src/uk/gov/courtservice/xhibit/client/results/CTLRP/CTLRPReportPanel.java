package uk.gov.courtservice.xhibit.client.results.CTLRP;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JCheckBox;
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
 * Title: CTLRPReportPanel
 * </p>
 * <p>
 * Description: The panel which displays CTLRP report
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
public class CTLRPReportPanel extends XPanel  implements ValidationListener  {
    private static final long serialVersionUID = 1L;
    
    private XDatePanel limitDate = null;
    private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
	private DateValidationController limitDateValidation;
	private JLabel limitDateWarningLabel;
	private JCheckBox chkExceptionsReport;
	private JCheckBox chkPrintReminder; 
	private boolean invalidEntry;
    
    public CTLRPReportPanel(RunCTLRPReportDialog runCTRLPReportDialog) throws CSRecoverableException{
    	stepInitialise();
        jbInit();
	}
	
	@Override
	public Dimension getPreferredSize() {
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
		this.add(getLimitDateWarningLabel(), gbc);
		gbc.insets = XHIBITConstant.nonContainerInsets;
			
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0.1;
		JLabel startDateLabel = new JLabel(
		XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "CTLRPReport.dialog_custody_time_limit"));
		this.add(startDateLabel, gbc);
			
		gbc.gridx++;
		gbc.weightx = 0.9;
		limitDate = new XDatePanel(this, null, false);
		this.add(getLimitDatePanel(), gbc);
			
		gbc.gridx = 0;
		gbc.gridy++;
		chkExceptionsReport = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "CTLRPReport.dialog_print_exceptions_report") ,false);
		this.add(chkExceptionsReport, gbc);
		
		gbc.gridx = 0;
		gbc.gridy++;
		chkPrintReminder = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "CTLRPReport.dialog_print_reminder_letters") ,false);
		this.add(chkPrintReminder, gbc);
	}
	
	private JLabel getLimitDateWarningLabel() {
    	if (limitDateWarningLabel == null) {
    		limitDateWarningLabel = new JLabel(" ");
    	}
    	return limitDateWarningLabel;
    }
	
	private XDatePanel getLimitDatePanel() {
		 limitDateValidation = ValidationControllerFactory.createDateRequired(this,
								limitDate, getLimitDateWarningLabel(), new AbstractDateValidator() {
		
		 SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		 	 
		 @Override
		 public void validate(XDatePanel target, List<String> errors) {
 				try {
					if (hasDate(target) && getDate(target).getTime().compareTo(sdf.parse(sdf.format(new Date()))) == -1) {
						 errors.add("Custody Time Limit cannot be in the past");
					}	else if (hasDate(limitDate)) {
							limitDateValidation.clearErrors();
					}
 				} catch (ParseException e) {
 					errors.add("Error parsing Customer Time Limit");
				}
		 }});
		 
		 validationControllers.add(limitDateValidation);
		
		 return limitDate;
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

	public JCheckBox getChkExceptionsReport() {
		return chkExceptionsReport;
	}
	
	public JCheckBox getChkPrintReminder() {
		return chkPrintReminder;
	}
}
