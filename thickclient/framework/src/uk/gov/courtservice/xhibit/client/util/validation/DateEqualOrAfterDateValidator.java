package uk.gov.courtservice.xhibit.client.util.validation;

import java.util.List;

import uk.gov.courtservice.xhibit.client.util.XDatePanel;

/**
 * Validate that if a date is entered in the XDatePanel control, it is equal to or
 * after the date in the comparison XDatePanel control if one is entered there too.
 * If either control does not have a date then the validation will pass. Use the
 * DateRequiredValidator in addition to this one to ensure a date is entered.
 * 
 * @author uphillj
 *
 */
public class DateEqualOrAfterDateValidator extends AbstractDateCompareValidator {

	public DateEqualOrAfterDateValidator(String fieldName, String compareName, XDatePanel compareDate) {
		super(fieldName, compareName, compareDate);
	}

	@Override
	public void validate(XDatePanel target, List<String> errors) {
		if (hasDate(target) && hasCompareDate()) {
			if (getDate(target).before(getCompareDate())) {
				errors.add(getFieldName() + " must be on the same day or after " + getCompareName());
			}
		}
	}
}
