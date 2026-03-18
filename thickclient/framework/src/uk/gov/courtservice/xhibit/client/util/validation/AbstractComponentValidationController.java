package uk.gov.courtservice.xhibit.client.util.validation;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.InputVerifier;
import javax.swing.JComponent;

/**
 * Base class for validator controllers used to validate Swing controls.
 * 
 * @author uphillj
 *
 * @param <T>
 */
public abstract class AbstractComponentValidationController<T extends JComponent> implements ValidationController<T>, FocusListener {

	protected enum ValidationStrategy { INPUT_VERIFIER, ON_FOCUS_LOST };
	
	private boolean runningValidations;
	private List<String> errors;
	private Class<T> targetType;
	private ValidationListener listener;
	private T target;
	private ValidationView view;
	private List<Validator<T>> validators;

	public AbstractComponentValidationController(Class<T> targetType, ValidationStrategy strategy, ValidationListener listener,
													T target, ValidationView view, List<Validator<T>> validators) {
		// Local copies of the supplied parameters
		this.targetType = targetType;
		this.listener = listener;
		this.target = target;
		this.view = view;
		this.validators = validators;

		// Initialise errors list and input verifier on control
		this.errors = new ArrayList<String>();
		
		// Trigger validation either by input verifier or focus being lost
		if (ValidationStrategy.INPUT_VERIFIER.equals(strategy)) {
			setInputVerifier(new InputValidation());
		} else if (ValidationStrategy.ON_FOCUS_LOST.equals(strategy)) {
			setFocusListener();
		}
	}

	@Override
	public Class<T> getTargetType() {
		return targetType;
	}

	@Override
	public T getTarget() {
		return target;
	}

	@Override
	public boolean validate() {
		return validate(true);
	}

	@Override
	public boolean validate(boolean updateView) {
		performValidation(updateView);
		return !hasErrors();
	}

	@Override
	public boolean hasErrors() {
		return (errors.size() > 0);
	}

	@Override
	public void showErrors() {
		if (hasErrors()) {
			view.showErrors(errors);
		}
	}

	@Override
	public void clearErrors() {
		view.clearErrors();
	}

	/**
	 * Entry point for validating control.
	 */
	protected void performValidation(boolean updateView) {
		// Prevent any recursive calls to run validations from the
		// listener updated view event or cross-field validation
		if (!runningValidations) {
			try {
				// Update flag as validations are now running
				runningValidations = true;
				
				// Reset errors list
				errors = new ArrayList<String>();
				
				// Validate control if it is accepting user input
				if (isTargetModifiable()) {
					// Run all the validators
					for (Validator<T> validator : validators) {
						validator.validate(target, errors);
					}
					
					// Update the view and notify listener
					if (updateView) {
						updateView();
					}
				}
			}
			finally {
				// Reset flag as validations are now finished
				runningValidations = false;
			}
		}
	}
	
	/**
	 * Update the view with the validation errors
	 * or ensure previous ones are cleared.
	 */
	protected void updateView() {
		if (hasErrors()) {
			view.showErrors(errors);
		} else {
			view.clearErrors();
		}
		listener.validationUpdatedView(this);
	}
	
	/**
	 * Sub-classes override this and return true if the control
	 * is enabled or editable and so its value can be changed.
	 * 
	 * @return
	 */
	protected abstract boolean isTargetModifiable();

	/**
	 * Install input verifier implementation to validate control.
	 * 
	 * @param inputVerifier
	 */
	protected void setInputVerifier(InputVerifier inputVerifier) {
		target.setInputVerifier(inputVerifier);
	}

	/**
	 * Install focus listener implementation to validate control.
	 */
	protected void setFocusListener() {
		target.addFocusListener(this);
	}
	
	/**
	 * No operation required on focus gain.
	 */
	@Override
	public void focusGained(FocusEvent e) {
	}

	/**
	 * Focus lost implementation to validate control.
	 */
	@Override
	public void focusLost(FocusEvent e) {
		performValidation(true);
	}
	
	/**
	 * Input verifier implementation used to validate a control.
	 */
	protected class InputValidation extends InputVerifier {

		/**
		 * User can always tab out of control even if invalid.
		 */
		@Override
		public boolean shouldYieldFocus(JComponent input) {
			super.shouldYieldFocus(input);
			return true;
		}

		/**
		 * Trigger validation of the control.
		 */
		@Override
		public boolean verify(JComponent input) {
			performValidation(true);
			return AbstractComponentValidationController.this.hasErrors();
		}
	}
}
