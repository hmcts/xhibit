package uk.gov.courtservice.xhibit.client.results.RREC;

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
import java.util.Locale;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractDateValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;

/**
 * <p>
 * Title: RAGEReportPanel
 * </p>
 * <p>
 * Description: The panel which displays RREC report
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
public class RRECReportPanel extends XPanel  implements ValidationListener  {
    private static final long serialVersionUID = 1L;
     
	private JLabel lblRunType;
	private JLabel lblWeekEnding;
	private JLabel lblMonthEnding;
	private JLabel lblPreview;
	
	private XTextField txtMonthEnding;
	private XDatePanel dteWeekEnding;
	
	private JCheckBox chkWeekEnding;
	private JCheckBox chkMonthEnding;
	
	private Calendar date;
	private Calendar emptyDate = null;
	private Date dateToBeUsed;
	
	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
	private DateValidationController weeklyDateValidation;
	private JLabel weeklyDateWarningLabel;
	
	private String reportWeekMonthEnding;
	
	private Boolean invalidEntry = true;
	
	private SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy",Locale.ENGLISH);
	
    public RRECReportPanel(RunRRECReportDialog runCTRLPReportDialog) throws CSRecoverableException{
    	stepInitialise();
        jbInit();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(450,150);
	}
	
	@Override
	public void stepInitialise() throws CSRecoverableException {
		
	}
	
	private JLabel getWeeklyDateWarningLabel() {
    	if (weeklyDateWarningLabel == null) {
    		weeklyDateWarningLabel = new JLabel("                                   ");
    	}                                        
    	return weeklyDateWarningLabel;
    }
	
	private XDatePanel getWeeklyDatePanel() {
		 weeklyDateValidation = ValidationControllerFactory.createDateRequired(this,
				 dteWeekEnding, getWeeklyDateWarningLabel(), new AbstractDateValidator() {
		
		 SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		 	 
		 @Override
		 public void validate(XDatePanel target, List<String> errors) {
				try {
					if (handleWeekEndingDate() && getDate(target).getTime().compareTo(sdf.parse(sdf.format(new Date()))) == 1) {
						 errors.add("Weekly Date cannot be in the future");
					}	else if (hasDate(dteWeekEnding)) {
							weeklyDateValidation.clearErrors();
					}
				} catch (Exception e) {
					errors.add("Error parsing Weekly Date");
				}
		 }});
		 
		 validationControllers.add(weeklyDateValidation);
	
		 return dteWeekEnding;
	}
	
	private JPanel initPanel1() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		gbc.insets = XHIBITConstant.errorLabelInsets;		
		gbc.weightx = 1.0;
		gbc.gridwidth = 2;
		panel.add(getWeeklyDateWarningLabel(), gbc);
		gbc.insets = XHIBITConstant.nonContainerInsets;
		
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0.6;
		lblRunType = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RRECReport.dialog_runType"));
		panel.add(lblRunType, gbc);
				
		gbc.gridx++;
		gbc.weightx = 0.4;
		chkWeekEnding = new JCheckBox();
		panel.add(chkWeekEnding,gbc);
					
		gbc.gridx++;
		gbc.weightx = 0.0;
		lblWeekEnding = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RRECReport.dialog_weekEnding"));
		panel.add(lblWeekEnding, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.0;
		dteWeekEnding = new XDatePanel(this, null, false);
		dteWeekEnding.setEnabled(false);
		panel.add(getWeeklyDatePanel(), gbc);
		
		chkWeekEnding.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean status = chkWeekEnding.isSelected();
				if (status) {
					chkMonthEnding.setSelected(false);
					txtMonthEnding.setText("");
					dteWeekEnding.setEnabled(true);
					invalidEntry = true;
					
				} else{
					dteWeekEnding.setDate(emptyDate);
					dteWeekEnding.setEnabled(false);
					invalidEntry = true;
				}
			}
		});
		
		return panel;
	}
	
	private JPanel initPanel2() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		gbc.weightx = 0.73;
		JLabel lblDummy = new JLabel(" ");
		panel.add(lblDummy, gbc);
				
		gbc.gridx++;
		gbc.weightx = 0.27;
		chkMonthEnding = new JCheckBox();
		panel.add(chkMonthEnding,gbc);
					
		gbc.gridx++;
		gbc.weightx = 0.00;
		lblMonthEnding = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RRECReport.dialog_monthEnding"));
		panel.add(lblMonthEnding, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.00;
		txtMonthEnding = new XTextField(13);
		txtMonthEnding.setEnabled(false);
		panel.add(txtMonthEnding,gbc);
		
		chkMonthEnding.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean status = chkMonthEnding.isSelected();
				if (status) {
				 chkWeekEnding.setSelected(false);
				 dteWeekEnding.setDate(emptyDate);
				 dteWeekEnding.setEnabled(false);
				 date = Calendar.getInstance();
				 date.setTime(new Date());
				 date.add(Calendar.MONTH, -1);
				 date.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DATE));
				 Date dte = date.getTime();
				 SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy",Locale.ENGLISH);
				 txtMonthEnding.setText(formatter.format(dte));
				 dateToBeUsed = null;
				 reportWeekMonthEnding = "month ending " + format.format(dte);
				 invalidEntry = false;
				} else {
					txtMonthEnding.setText("");
					invalidEntry = true;
				}
			}
		});
		
		return panel;
	}
	
	private Boolean handleWeekEndingDate() throws CSValidationException {
		 if (dteWeekEnding.getDate() != null && dteWeekEnding.isDateValidate()
				&& !dteWeekEnding.getDate().after(Calendar.getInstance())) {
			date = dteWeekEnding.getDate();
			
			int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
			date.add(Calendar.DATE, (14 - dayOfWeek) % 7);
			dteWeekEnding.setDate(date);
			dateToBeUsed = dteWeekEnding.getDate().getTime();
			if (!dateToBeUsed.after(Calendar.getInstance().getTime())) {
				reportWeekMonthEnding = "week ending " + format.format(dateToBeUsed);
				return false;
			}
		 }
		
		return true;
	}
	
	public void jbInit() {
	
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		
		this.setLayout(new GridBagLayout());
	 
	    gbc.weighty = 0.50;
		JPanel panel1 = initPanel1();
		this.add(panel1, gbc);
	    
	    gbc.weighty = 0.50;
	    gbc.gridy++;
	    JPanel panel2 = initPanel2();
	    this.add(panel2, gbc);
	    
		gbc.gridy++;
		
		lblPreview = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RRECReport.dialog_text"));
		this.add(lblPreview, gbc);
	}
	
	public Boolean getInvalidEntry() {
		return invalidEntry;
	}

	public void setInvalidEntry(Boolean invalidEntry) {
		this.invalidEntry = invalidEntry;
	}
	
	public Date getDateToBeUsed() {
		return dateToBeUsed;
	}

	public void setDateToBeUsed(Date dateToBeUsed) {
		this.dateToBeUsed = dateToBeUsed;
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

	public String getReportWeekMonthEnding() {
		return reportWeekMonthEnding;
	}

	public void setReportWeekMonthEnding(String reportWeekMonthEnding) {
		this.reportWeekMonthEnding = reportWeekMonthEnding;
	}
}