package uk.gov.courtservice.xhibit.client.util.validation;

import java.util.List;

import uk.gov.courtservice.xhibit.client.util.XDatePanel;

/**
 * Validate that if a date is entered in the XDatePanel control, it is equal to or
 * after today. If the control does not have a date then the validation will pass.
 * Use the DateRequiredValidator in addition to this one to ensure a date is entered.
 * 
 * @author uphillj
 *
 */
public class DateEqualOrAfterTodayValidator extends AbstractDateValidator {

	@Override
	public void validate(XDatePanel target, List<String> errors) {
		if (hasDate(target)) {
			if (getDate(target).before(getCalendarNoTime())) {
				errors.add("Date cannot be in past");
			}
		}
	}
}
