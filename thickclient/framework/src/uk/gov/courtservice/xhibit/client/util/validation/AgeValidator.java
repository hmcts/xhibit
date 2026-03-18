package uk.gov.courtservice.xhibit.client.util.validation;

import java.awt.Component;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;

/**
 * Validate that if a date is entered in the XDatePanel control as a date of birth,
 * the age of the person in question is over minAge and under maxAge.
 * If the control does not have a date then the validation will pass.
 * If either minAge or maxAge is negative then the validation it will pass for that comparison.
 * If warningIsError is true then failure validation will fail
 * Use the DateRequiredValidator in addition to this one to ensure a date is entered.
 * 
 * @author ruddocka
 *
 */

public class AgeValidator extends AbstractDateValidator {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private Integer minAge = -1;
	private Integer maxAge = -1;
	private boolean warningIsError = true;
	private boolean onClose = false;
	private boolean active = false;		//True when dialog being displayed
	private Calendar orgDate = getCalendarNoTime();
	private int option;

	public void setMinAge(Integer minAge) { this.minAge = minAge; };
	public Integer getMinAge() { return minAge; };
	
	public void setMaxAge(Integer maxAge) { this.maxAge = maxAge; };
	public Integer getMaxAge() { return maxAge; };
	
	public void setWarningIsError(boolean warningIsError) { this.warningIsError = warningIsError; };
	public boolean getWarningIsError() { return warningIsError; };
	
	public void setOnClose(boolean onClose) { this.onClose = onClose; };
	public boolean getOnClose() { return onClose; };
	
	public void setOrgDate(Calendar orgDate) { this.orgDate = orgDate; };
	public Calendar getOrgDate() { return orgDate; };
	
	public boolean isActive() { return active; };

	public int getOption() { return option; };


	public AgeValidator(String fieldName, String compareName, XDatePanel compareDate) {
		super();
		try {
			orgDate = compareDate.getDate();
		} catch (CSValidationException e) {
			e.printStackTrace();
		}
	}


	public AgeValidator(String fieldName, String compareName, Integer minAge, Integer maxAge, XDatePanel compareDate) {
		super();
		this.minAge = minAge;
		this.maxAge = maxAge;
		try {
			orgDate = compareDate.getDate();
		} catch (CSValidationException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public void validate(XDatePanel target, List<String> errors) {
		if (hasDate(target)) {
			Calendar minDate = getCalendarNoTime();
			Calendar maxDate = getCalendarNoTime();
			minDate.add(Calendar.YEAR, -(minAge));
			maxDate.add(Calendar.YEAR, -(maxAge));
			if (getDate(target).after(getCalendarNoTime())) {
				errors.add("Date cannot be in future");
			} else {
				if (onClose)
					return;
				if ((minAge >= 0) && (getDate(target).after(minDate))) {
					if (warningIsError) {
						errors.add("Age must be older than " + minAge.toString());
					} else {
						String msg = "<html><center><strong>Are you sure?</strong><br><br>";
						msg += "This date makes the person under " + minAge.toString() + " years of age.<br><br>";
						msg += "Do you wish to continue?<br><br></html>";
						active = true;
						option = JOptionPane.showConfirmDialog(null, msg, "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if (JOptionPane.YES_OPTION != option) {
							target.setDate(orgDate);
							errors.add("age error occurred");
						}
						active = false;
					}
				} else {	//Only want to display one dialog in case of overlap
					if ((maxAge >= 0) && (getDate(target).before(maxDate))) {
						if (warningIsError) {
							errors.add("Age must be younger than " + maxAge.toString());
						} else {
							String msg = "<html><center>The date you have entered makes this person " + maxAge.toString() + " or over.<br><br>";
							msg += "Do you wish to continue?<br><br></html>";
							active = true;
							option = JOptionPane.showConfirmDialog(null, msg, "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
							if (JOptionPane.YES_OPTION != option) {
								target.setDate(orgDate);
							}
							active = false;
						}
					} 
				}
			}
		}
	}
	
}
