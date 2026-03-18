package uk.gov.courtservice.xhibit.client.util.validation;

import java.util.List;

/**
 * Interface for validators used to validate Swing controls.
 * 
 * @author uphillj
 *
 * @param <T>
 */
public interface Validator<T> {

	/**
	 * Class of the object that this validator can validate.
	 * 
	 * @return
	 */
	Class<T> getTargetType ();
	
	/**
	 * Method which validates the object and adds errors for failures.
	 * 
	 * @param target
	 * @param errors
	 */
	void validate(T target, List<String> errors);

}
