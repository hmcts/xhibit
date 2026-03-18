package uk.gov.courtservice.xhibit.client.util.validation;

import java.util.List;

import uk.gov.courtservice.xhibit.client.util.XDatePanel;

/**
 * Validate that a valid date has been entered in the XDatePanel control.
 *  
 * @author uphillj
 *
 */
public class DateRequiredValidator extends DateValidValidator {

	@Override
	public void validate(XDatePanel target, List<String> errors) {
		if (!hasLength(target)) {
			errors.add("Field is mandatory");
		} else {
			super.validate(target, errors);
		}
	}
}
