package uk.gov.courtservice.xhibit.client.util.validation;

import java.util.List;

import uk.gov.courtservice.xhibit.client.util.XDatePanel;

/**
 * Validate that if text has been entered in the XDatePanel control it is a valid date.
 * Use the DateRequiredValidator instead if a date is mandatory and must be entered.
 * 
 * @author uphillj
 *
 */
public class DateValidValidator extends AbstractDateValidator {

	@Override
	public void validate(XDatePanel target, List<String> errors) {
		if (hasLength(target) && !hasDate(target)) {
			errors.add("Invalid date");
		}
	}
}
