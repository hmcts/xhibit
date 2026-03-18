package uk.gov.courtservice.xhibit.client.util.validation;

import java.util.List;

/**
 * Interface for classes used to display validation errors.
 * 
 * @author uphillj
 *
 */
public interface ValidationView {

	/**
	 * Show the errors in the UI.
	 * 
	 * @param errors
	 */
	void showErrors(List<String> errors);
	
	/**
	 * Clear the errors shown in the UI.
	 */
	void clearErrors();
}
