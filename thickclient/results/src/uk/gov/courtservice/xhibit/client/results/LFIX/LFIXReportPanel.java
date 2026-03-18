package uk.gov.courtservice.xhibit.client.results.LFIX;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.LFIXRunDate;

/**
 * <p>
 * Title: LFIXReportPanel
 * </p>
 * <p>
 * Description: The panel which displays LFIX report
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
public class LFIXReportPanel extends XPanel  implements ValidationListener  {
    private static final long serialVersionUID = 1L;
    private RunLFIXReportDialog parentDialog = null;
    private XDatePanel runDate = null;
    private JCheckBox chkRunDate; 
    private DateValidationController runDateValidation;
    private String displayRunDate;
    private JLabel runDateWarningLabel;
    private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
	private static final String DATE_FORMAT = "dd-MMM-yyyy";
	public LFIXReportPanel(RunLFIXReportDialog runLFIXReportDialog) throws CSRecoverableException{
		parentDialog = runLFIXReportDialog;
		stepInitialise();
	}
	
	/**
	 * @return the runDate to send to the stored procedure
	 * @throws CSRecoverableException 
	 * @throws CSValidationException 
	 */
	public Date getRunDate() throws CSValidationException, CSRecoverableException {
		Date reportRunDate = null;
		if (isRunAgain() && null != runDate.getDate()) {
			reportRunDate = runDate.getDate().getTime();
		}
		return reportRunDate;
	}
	
	/**
	 * Indicates whether or not the run again checkbox is selected
	 * @return true if the checkbox is selected, else false
	 */
	public boolean isRunAgain() {
		return chkRunDate.isSelected();
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(550,100);
		
	}
	
	@Override
	public void stepInitialise() throws CSRecoverableException {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		this.setLayout(new GridBagLayout());
		
		// Determine the date that the report was last run
		LFIXRunDate lfixRunDate = 
	             XhibitDelegateHelper.getResults2Delegate().getReportRunDate(XhibitSingleton.getInstance().getCourtId(), XHIBITConstant.getResource(XhibitBundles.XhibitActionResources, "LFIXReportActionName"));
		displayRunDate = lfixRunDate.getLfixRunDateValues().get(0).getRundate();
		if (displayRunDate == "") {
			displayRunDate = new SimpleDateFormat(DATE_FORMAT).format(new Date());
		}
		
		gbc.gridwidth = 2;
		JLabel label1 = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "LFIXReport.dialog_label1"));
		this.add(label1,gbc);
		
		gbc.gridy++;
		JLabel label2 = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "LFIXReport.dialog_label2")+ " (" + displayRunDate + ").");
		this.add(label2,gbc);
		
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weightx = 0.7;
		gbc.gridx = 1;
		this.add(getRunDateWarningLabel(), gbc);
		gbc.insets = XHIBITConstant.nonContainerInsets;	
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0.3;
		chkRunDate = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "LFIXReport.dialog_text") ,false);
		this.add(chkRunDate,gbc);

		gbc.gridx++;
		gbc.weightx = 0.7;
        runDate = new XDatePanel(this, null, false);
		this.add(getRunDatePanel(), gbc);
		
		chkRunDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean status = chkRunDate.isSelected();
				runDate.setEnabled(status);
				if (!status)
				 runDate.clear();
			}
		});
		
		runDate.setEnabled(false);
	}
	
	private JLabel getRunDateWarningLabel() {
    	if (runDateWarningLabel == null) {
    		runDateWarningLabel = new JLabel(" ");
    	}
    	return runDateWarningLabel;
    }
	
	private XDatePanel getRunDatePanel(){
		 
		 runDateValidation = ValidationControllerFactory.createDateRequired(this,
								runDate, getRunDateWarningLabel(), new AbstractDateValidator() {
		 @Override
		 public void validate(XDatePanel target, List<String> errors) {
				if (chkRunDate.isSelected() &&  hasDate(target)) {
					 Calendar cal = Calendar.getInstance();
					 try {
						cal.setTime(new Date());
						cal.add(Calendar.DATE, -90);
						if (cal.getTime().after(runDate.getDate().getTime())) {
							 errors.add("Run Date must be at most 90 days before current date");
						}
					 } catch (CSValidationException e) {
						 errors.add("Error getting run date");
					 }
				} else {
					runDateValidation.clearErrors();
				}
		 }});
		 
		validationControllers.add(runDateValidation);
		
		return runDate;
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
		boolean valid = (!validationController.hasErrors()
				&& ValidationControllerFactory.validateComponents(validationControllers));
		parentDialog.getButtonPanel().okButton.setEnabled(valid);
		
	}
}
