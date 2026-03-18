package uk.gov.courtservice.xhibit.client.util.validation;

import java.util.Calendar;
import java.util.Date;

import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;

/**
 * Base class for validators used to validate XDatePanel controls.
 * 
 * @author uphillj
 *
 */
public abstract class AbstractDateValidator extends AbstractValidator<XDatePanel> {

	public AbstractDateValidator() {
		super(XDatePanel.class);
	}

	/**
	 * Return true if not null and contains any characters.
	 * 
	 * @param component
	 * @return
	 */
	protected boolean hasLength(XDatePanel component) {
		return ValidationUtils.hasLength(component);
	}
	
	/**
	 * Return true if contains valid date.
	 * 
	 * @param component
	 * @return
	 */
	protected boolean hasDate(XDatePanel component) {
		return ValidationUtils.hasDate(component);
	}

	/**
	 * Return calendar if date valid, or null if empty or invalid.
	 * Use with hasDate to know if safe to call to get date.
	 * 
	 * @param component
	 * @return
	 */
	protected Calendar getDate(XDatePanel component) {
		Calendar calendar;
		try {
			calendar = component.getDate();
		} catch (CSValidationException e) {
			calendar = null;
		}
		return calendar;
	}
	
	/**
	 * Create calendar with today's date and no time.
	 * 
	 * @return calendar
	 */
	protected Calendar getCalendarNoTime() {
		final Calendar calendar = Calendar.getInstance();
		clearTimeComponents(calendar);
		return calendar;
	}

	/**
	 * Create calendar from supplied date with no time.
	 * 
	 * @param date date
	 * @return calendar
	 */
	protected Calendar getCalendarNoTime(Date date) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		clearTimeComponents(calendar);
		return calendar;
	}

	/**
	 * Clear time from supplied calendar.
	 * 
	 * @param calendar calendar
	 */
	protected void clearTimeComponents(Calendar calendar) {
		calendar.set(Calendar.AM_PM, Calendar.AM);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}
}
