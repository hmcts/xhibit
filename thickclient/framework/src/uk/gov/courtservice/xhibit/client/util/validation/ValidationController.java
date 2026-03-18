package uk.gov.courtservice.xhibit.client.util.validation;

/**
 * Interface for controller used to validate Swing controls.
 * 
 * @author uphillj
 *
 * @param <T>
 */
public interface ValidationController<T> {

	/**
	 * Class of the object that this controller can validate.
	 * 
	 * @return
	 */
	Class<T> getTargetType ();

	/**
	 * Instance of the class that this controller will validate.
	 * 
	 * @return
	 */
	T getTarget();
	
	/**
	 * Method to request revalidation of the component managed by
	 * this component and update the UI with the result.
	 * 
	 * @return true if control valid, false if errors
	 */
	boolean validate();
	
	/**
	 * Method to request revalidation of the component managed by
	 * this component and optionally update the UI with the result.
	 *
	 * @param updateView
	 * @return true if control valid, false if errors
	 */
	boolean validate(boolean updateView);
	
	/**
	 * Return true if there are validation errors on the control
	 * from the last time the control was validated.
	 * 
	 * @return
	 */
	boolean hasErrors();
	
	/**
	 * Method to display any errors for the component, which can
	 * be useful if errors have been cleared and just need to be
	 * shown again without re-running the validation.
	 */
	void showErrors();	
	
	/**
	 * Method to clear any errors that are displayed for the component,
	 * which can be useful in cross field validation scenarios.
	 */
	void clearErrors();	
}
