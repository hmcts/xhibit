package uk.gov.courtservice.xhibit.client.results.RSIT;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractDateValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationUtils;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: RAGEReportPanel
 * </p>
 * <p>
 * Description: The panel which displays RSIT report
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
public class RSITReportPanel extends XPanel  implements ValidationListener  {
    private static final long serialVersionUID = 1L;
     
	private JLabel lblSite;
	private JLabel lblDate;
	private JLabel lblPreview;
	
	private XComboBox siteCombo;
	
	private XDatePanel dteWeekEnding;
	
	private Date dateToBeUsed;
	
	private String reportWeekMonthEnding;
	
	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
	private DateValidationController weeklyDateValidation;
	private JLabel weeklyDateWarningLabel;
	
	private Boolean invalidEntry = true;
	
	private SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy",Locale.ENGLISH);
	
    public RSITReportPanel(RunRSITReportDialog runRSITReportDialog) throws CSRecoverableException{
    	stepInitialise();
        jbInit();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(400,125);
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
		 	 
		 @Override
		 public void validate(XDatePanel target, List<String> errors) {
				try {
					if ( !handleWeekEndingDate() ) {
						 errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText, "RSIT.weekendingdate.dateinfuture"));
					} 
					else { 
							weeklyDateValidation.clearErrors();
					}
				} catch (Exception e) {
					errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText, "RSIT.weekendingdate.parsingError"));
				}
		 }});
		 
		 validationControllers.add(weeklyDateValidation);
	
		 return dteWeekEnding;
	}
	
	private JPanel initPanel2() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		gbc.insets = XHIBITConstant.errorLabelInsets;		
		gbc.weightx = 1.0;
		gbc.gridx++;
		gbc.gridwidth = 2;
		panel.add(getWeeklyDateWarningLabel(), gbc);
		gbc.insets = XHIBITConstant.nonContainerInsets;
		
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0.10;
		lblDate = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RSITReport.panel_label_Enter_Date_Label"));
		panel.add(lblDate, gbc);
									
		gbc.gridx++;
		gbc.weightx = 0.90;
		dteWeekEnding = new XDatePanel(this, null, false);
		panel.add(getWeeklyDatePanel(), gbc);
		
		
		return panel;
	}
	
	
	/**
	 * Set the array and if the array is empty disable the dropdown box
	 */
	private void setDropdownBoxArray(XComboBox comboBox, final Object[] arrayItems) {
		comboBox.setModel(new DefaultComboBoxModel(arrayItems));
		if (comboBox.getSelectedItem() != null) {
			comboBox.setRenderer(new DefaultListCellRenderer()); 
			comboBox.enableAutoSelect();
			comboBox.setSelectedIndex(0);
		} else {
			comboBox.setEnabled(false);
		}
	}
	
	@SuppressWarnings("unchecked")
	private JPanel initPanel1() throws CSRecoverableException {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		// Add the panel elements
		gbc.weightx = 0.10;
		lblSite = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RSITReport.panel_label_Enter_Site_Label"));
		
		panel.add(lblSite, gbc);
		
		Collection courtSites;
		
		courtSites = XhibitDelegateHelper.getResults2Delegate().getCourtSites(XhibitSingleton.getInstance().getCourtId());
		
		ArrayList<DropdownCodeStringValue> sites = buildDropDrownList(new ArrayList(courtSites));
			
		if (sites != null) {
			gbc.gridx++;
			gbc.weightx = 0.90;
			siteCombo = new XComboBox();
			setDropdownBoxArray(siteCombo, sites.toArray());
			panel.add(siteCombo, gbc);
		}		
		
		return panel;
	}
	
	public String getCourtSiteSelection() {
		return ((DropdownCodeStringValue) siteCombo.getSelectedItem()).getPrintString();
	}
	
	private ArrayList<DropdownCodeStringValue> buildDropDrownList(List courtSites) {
		ArrayList<DropdownCodeStringValue> results = new ArrayList<DropdownCodeStringValue>();
		if (courtSites.size() > 1)
			results.add(new DropdownCodeStringValue("All Sites","0","0"));
		String siteInfo[];
		for (Iterator it = courtSites.iterator(); it.hasNext();) {
			siteInfo = ((StringBuffer) it.next()).toString().split("\\*{2}");
			results.add(new DropdownCodeStringValue(siteInfo[1], siteInfo[0], siteInfo[0]));
		}
		
		return results;
	}
	
	/**
	 * Checks validation that the date entered on screen is valid and is no further in the future than the Saturday
	 * of the current week.  If it is ok, then the date is updated to be the Saturday following the date entered
	 * (unless it is already a Saturday)
	 * @return true if the date entered is valid and has been updated to be a Saturday, else false
	 * @throws CSValidationException
	 */
	private Boolean handleWeekEndingDate() throws CSValidationException {
		boolean handleWeekEndingDate = false;
		if ( ValidationUtils.hasDate(dteWeekEnding) ) {
			// Valid date entered, check if the date entered on screen is after the next Saturday in the future
			Calendar nextSaturday = getNextSaturdayCalendarObject(Calendar.getInstance());
			Calendar dateEntered = dteWeekEnding.getDate();
			if ( !dateEntered.after(nextSaturday) ) {
				// Date entered is not set after the Saturday of the current week.  
				// Update the screen to use the Saturday of the date entered.
				Calendar saturdayOfDayEntered = getNextSaturdayCalendarObject(dateEntered);
				dteWeekEnding.setDate(saturdayOfDayEntered);
				dateToBeUsed = saturdayOfDayEntered.getTime();
				reportWeekMonthEnding = "week ending " + format.format(dateToBeUsed);
				handleWeekEndingDate = true;
			}
		}
		return handleWeekEndingDate;
	}
	
	/**
	 * For a given date, a Calendar object is returned representing the Saturday
	 * following the date supplied (unless it is already a Saturday)
	 * @param date	Calendar object for the date to be processed
	 * @return The Saturday after the date object passed in
	 */
	private Calendar getNextSaturdayCalendarObject(final Calendar date) {
		Calendar nextSaturday = date;
		int dayOfWeek = nextSaturday.get(Calendar.DAY_OF_WEEK);
		nextSaturday.add(Calendar.DATE, (14 - dayOfWeek) % 7);
		return nextSaturday;
	}
	
	public void jbInit() throws CSRecoverableException {
	
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		
		this.setLayout(new GridBagLayout());
		
	    gbc.weighty = 0.48;
		JPanel panel1 = initPanel1();
		this.add(panel1, gbc);
	    
	    gbc.weighty = 0.52;
	    gbc.gridy++;
	    JPanel panel2 = initPanel2();
	    this.add(panel2, gbc);
	    
		gbc.gridy++;
		
		lblPreview = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RSITReport.panel_label_Preview_Label"));
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