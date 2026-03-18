package uk.gov.courtservice.xhibit.client.util.validation;

/**
 * Interface for client classes to implement to get notified when
 * a ValidationController performs validation.
 * 
 * @author uphillj
 *
 */
public interface ValidationListener {

	/**
	 * Method called after validation has been run and the UI has been
	 * updated to show the errors or clear any previous errors.
	 * 
	 * @param validationController validation controller for component
	 */
	void validationUpdatedView(ValidationController<?> validationController);
}
