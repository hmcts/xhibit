package uk.gov.courtservice.xhibit.client.util.validation;

import java.util.Calendar;

import uk.gov.courtservice.xhibit.client.util.XDatePanel;

/**
 * Base class for validators used to validate XDatePanel controls by comparing
 * its value to another XDatePanel control.
 * 
 * @author uphillj
 *
 */
public abstract class AbstractDateCompareValidator extends AbstractDateValidator {

	private String fieldName;
	private String compareName;
	private XDatePanel compareDate;

	public AbstractDateCompareValidator(String fieldName, String compareName, XDatePanel compareDate) {
		this.fieldName = fieldName;
		this.compareName = compareName;
		this.compareDate = compareDate;
	}

	/**
	 * Get field name for control being validated.
	 * 
	 * @return
	 */
	protected String getFieldName() {
		return fieldName;
	}

	/**
	 * Get field name for control being compared against.
	 * 
	 * @return
	 */
	protected String getCompareName() {
		return compareName;
	}
	
	/**
	 * Return true if compare date contains valid date.
	 * 
	 * @return
	 */
	protected boolean hasCompareDate() {
		return hasDate(compareDate);
	}

	/**
	 * Return calendar if date valid, or null if empty or invalid.
	 * Use with hasCompareDate to know if safe to call to get date.
	 * 
	 * @return
	 */
	protected Calendar getCompareDate() {
		return getDate(compareDate);
	}
}
