package uk.gov.courtservice.xhibit.client.util.validation;

import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import uk.gov.courtservice.xhibit.client.util.XDatePanel;

/**
 * Factory class for creating validation controllers for handling
 * the validation of Swing components.
 * 
 * @author uphillj
 *
 */
public class ValidationControllerFactory {

	/**
	 * Private constructor to prevent creation.
	 */
	private ValidationControllerFactory() {
	}

	/**
	 * Main method used to create component validation view.
	 * 
	 * @param target
	 * @param label
	 * @return
	 */
	private static ValidationView createComponentValidationView(JComponent target, JLabel label) {
		return new ComponentValidationView(target, label);
	}

	/**
	 * Main method used to create date validation view.
	 * 
	 * @param target
	 * @param label
	 * @return
	 */
	private static ValidationView createDateValidationView(XDatePanel target, JLabel label) {
		return new DateValidationView(target, label);
	}
	
	/**
	 * Main method used to create text validation controller.
	 * 
	 * @param listener
	 * @param target
	 * @param view
	 * @param validators
	 * @return
	 */
	private static TextValidationController createTextValidationController(ValidationListener listener, JTextComponent target, ValidationView view, Validator<JTextComponent>... validators) {
		return new TextValidationController(listener, target, view, Arrays.asList(validators));
	}

	/**
	 * Main method used to create date validation controller.
	 * 
	 * @param listener
	 * @param target
	 * @param view
	 * @param validators
	 * @return
	 */
	private static DateValidationController createDateValidationController(ValidationListener listener, XDatePanel target, ValidationView view, Validator<XDatePanel>... validators) {
		return new DateValidationController(listener, target, view, Arrays.asList(validators));
	}

	/**
	 * Main method used to create combo box validation controller.
	 * 
	 * @param listener
	 * @param target
	 * @param view
	 * @param validators
	 * @return
	 */
	private static ComboBoxValidationController createComboBoxValidationController(ValidationListener listener, JComboBox target, ValidationView view, Validator<JComboBox>... validators) {
		return new ComboBoxValidationController(listener, target, view, Arrays.asList(validators));
	}

	/**
	 * Create text validation controller with label for displaying errors.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param validators
	 * @return
	 */
	private static TextValidationController createTextValidationController(ValidationListener listener, JTextComponent target, JLabel label, Validator<JTextComponent>... validators) {
		return createTextValidationController(listener, target, createComponentValidationView(target, label), validators);
	}

	/**
	 * Create date validation controller with label for displaying errors.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param validators
	 * @return
	 */
	private static DateValidationController createDateValidationController(ValidationListener listener, XDatePanel target, JLabel label, Validator<XDatePanel>... validators) {
		return createDateValidationController(listener, target, createDateValidationView(target, label), validators);
	}

	/**
	 * Create combo box validation controller with label for displaying errors.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param validators
	 * @return
	 */
	private static ComboBoxValidationController createComboBoxValidationController(ValidationListener listener, JComboBox target, JLabel label, Validator<JComboBox>... validators) {
		return createComboBoxValidationController(listener, target, createComponentValidationView(target, label), validators);
	}

	/**
	 * Helper method for validating components managed by a list of validation
	 * controllers and returning true if all pass their validation or false as
	 * soon as the first component fails validation. This method does not update
	 * the UI as it only validates the controls until the first one fails. This
	 * method might validate controls that the user has yet to visit and so they
	 * would not expect a validation error to suddenly appear on those controls.
	 * 
	 * @param validationControllers
	 * @return
	 */
	public static boolean validateComponents(List<ValidationController<?>> validationControllers) {
		boolean passed = true;
		
		// Cycle through all the supplied controllers and if any have errors,
		// then the overall validation has failed and so return false
		for (ValidationController<?> validationController : validationControllers) {
			if (!validationController.validate(false)) {
				passed = false;
				break;
			}
		}
		
		return passed;
	}

	/**
	 * Create text validation controller with supplied validator.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param validator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TextValidationController createText(ValidationListener listener, JTextComponent target, JLabel label, Validator<JTextComponent> validator) {
		return createTextValidationController(listener, target, label, validator);
	}

	/**
	 * Create text validation controller with list of supplied validators.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param validators
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TextValidationController createText(ValidationListener listener, JTextComponent target, JLabel label, List<Validator<JTextComponent>> validators) {
		return createTextValidationController(listener, target, label, (Validator<JTextComponent>[])Arrays.copyOf(validators.toArray(), validators.toArray().length, Validator[].class));
	}
	
	/**
	 * Create text validation controller where non-whitespace text is mandatory.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TextValidationController createTextRequired(ValidationListener listener, JTextComponent target, JLabel label) {
		return createTextValidationController(listener, target, label, new TextRequiredValidator());
	}

	/**
	 * Create text validation controller where non-whitespace text is mandatory
	 * and it also passes the additional supplied validator.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param validator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TextValidationController createTextRequired(ValidationListener listener, JTextComponent target, JLabel label, Validator<JTextComponent> validator) {
		return createTextValidationController(listener, target, label, new TextRequiredValidator(), validator);
	}

	/**
	 * Create text validation controller that ensures if text is entered it
	 * is not longer than the maximum length allowed.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param maxLength
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TextValidationController createTextMaxLength(ValidationListener listener, JTextComponent target, JLabel label, int maxLength) {
		return createTextValidationController(listener, target, label, new TextMaxLengthValidator(maxLength));
	}

	/**
	 * Create text validation controller that ensures if text is entered it
	 * is not longer than the maximum length allowed and it also passes the
	 * additional supplied validator.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param maxLength
	 * @param validator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TextValidationController createTextMaxLength(ValidationListener listener, JTextComponent target, JLabel label, int maxLength, Validator<JTextComponent> validator) {
		return createTextValidationController(listener, target, label, new TextMaxLengthValidator(maxLength), validator);
	}

	/**
	 * Create text validation controller that ensures if text is entered it
	 * matches the supplied regex pattern.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param pattern
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TextValidationController createTextRegex(ValidationListener listener, JTextComponent target, JLabel label, String pattern) {
		return createTextValidationController(listener, target, label, new TextRegexValidator(pattern));
	}

	/**
	 * Create text validation controller that ensures if text is entered it
	 * matches the supplied regex pattern and it also passes the additional
	 * supplied validator.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param pattern
	 * @param validator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TextValidationController createTextRegex(ValidationListener listener, JTextComponent target, JLabel label, String pattern, Validator<JTextComponent> validator) {
		return createTextValidationController(listener, target, label, new TextRegexValidator(pattern), validator);
	}

	/**
	 * Create text validation controller where non-whitespace text is mandatory
	 * and the text is not longer than the maximum length allowed.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param maxLength
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TextValidationController createTextRequiredAndMaxLength(ValidationListener listener, JTextComponent target, JLabel label, int maxLength) {
		return createTextValidationController(listener, target, label, new TextRequiredValidator(), new TextMaxLengthValidator(maxLength));
	}

	/**
	 * Create text validation controller where non-whitespace text is mandatory
	 * and the text is not longer than the maximum length allowed and it also
	 * passes the additional supplied validator.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param maxLength
	 * @param validator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TextValidationController createTextRequiredAndMaxLength(ValidationListener listener, JTextComponent target, JLabel label, int maxLength, Validator<JTextComponent> validator) {
		return createTextValidationController(listener, target, label, new TextRequiredValidator(), new TextMaxLengthValidator(maxLength), validator);
	}

	/**
	 * Create text validation controller where non-whitespace text is mandatory
	 * and the text must match the supplied regex pattern.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param pattern
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TextValidationController createTextRequiredAndRegex(ValidationListener listener, JTextComponent target, JLabel label, String pattern) {
		return createTextValidationController(listener, target, label, new TextRequiredValidator(), new TextRegexValidator(pattern));
	}

	/**
	 * Create text validation controller where non-whitespace text is mandatory
	 * and the text must match the supplied regex pattern and it also passes the
	 * additional supplied validator.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param pattern
	 * @param validator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TextValidationController createTextRequiredAndRegex(ValidationListener listener, JTextComponent target, JLabel label, String pattern, Validator<JTextComponent> validator) {
		return createTextValidationController(listener, target, label, new TextRequiredValidator(), new TextRegexValidator(pattern), validator);
	}

	/**
	 * Create date validation controller with list of supplied validators.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param validators
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static DateValidationController createDate(ValidationListener listener, XDatePanel target, JLabel label, List<Validator<XDatePanel>> validators) {
		return createDateValidationController(listener, target, label, (Validator<XDatePanel>[])Arrays.copyOf(validators.toArray(), validators.toArray().length, Validator[].class));
	}

	/**
	 * Create date validation controller that ensures if a date is entered it is valid.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static DateValidationController createDateValid(ValidationListener listener, XDatePanel target, JLabel label) {
		return createDateValidationController(listener, target, label, new DateValidValidator());
	}

	/**
	 * Create date validation controller that ensures if a date is entered it is valid
	 * and it also passes the additional supplied validator.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param validator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static DateValidationController createDateValid(ValidationListener listener, XDatePanel target, JLabel label, Validator<XDatePanel> validator) {
		return createDateValidationController(listener, target, label, new DateValidValidator(), validator);
	}

	/**
	 * Create date validation controller that ensures if a date is entered it is valid
	 * and it also passes the additional two supplied validators.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param validator1
	 * @param validator2
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static DateValidationController createDateValid(ValidationListener listener, XDatePanel target, JLabel label, Validator<XDatePanel> validator1, Validator<XDatePanel> validator2) {
		return createDateValidationController(listener, target, label, new DateValidValidator(), validator1, validator2);
	}

	/**
	 * Create date validation controller where a date is mandatory.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static DateValidationController createDateRequired(ValidationListener listener, XDatePanel target, JLabel label) {
		return createDateValidationController(listener, target, label, new DateRequiredValidator());
	}

	/**
	 * Create date validation controller where a date is mandatory
	 * and it also passes the additional supplied validator.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param validator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static DateValidationController createDateRequired(ValidationListener listener, XDatePanel target, JLabel label, Validator<XDatePanel> validator) {
		return createDateValidationController(listener, target, label, new DateRequiredValidator(), validator);
	}

	/**
	 * Create date validation controller where a date is mandatory
	 * and it also passes the additional two supplied validators.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param validator1
	 * @param validator2
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static DateValidationController createDateRequired(ValidationListener listener, XDatePanel target, JLabel label, Validator<XDatePanel> validator1, Validator<XDatePanel> validator2) {
		return createDateValidationController(listener, target, label, new DateRequiredValidator(), validator1, validator2);
	}

	/**
	 * Create combo box validation controller with supplied validator.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param validator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ComboBoxValidationController createComboBox(ValidationListener listener, JComboBox target, JLabel label, Validator<JComboBox> validator) {
		return createComboBoxValidationController(listener, target, label, validator);
	}

	/**
	 * Create combo box validation controller with list of supplied validators.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param validators
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ComboBoxValidationController createComboBox(ValidationListener listener, JComboBox target, JLabel label, List<Validator<JComboBox>> validators) {
		return createComboBoxValidationController(listener, target, label, (Validator<JComboBox>[])Arrays.copyOf(validators.toArray(), validators.toArray().length, Validator[].class));
	}

	/**
	 * Create combo box validation controller where selection is mandatory.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ComboBoxValidationController createComboBoxRequired(ValidationListener listener, JComboBox target, JLabel label) {
		return createComboBoxValidationController(listener, target, label, new ComboBoxRequiredValidator());
	}

	/**
	 * Create combo box validation controller where selection is mandatory
	 * and it also passes the additional supplied validator.
	 * 
	 * @param listener
	 * @param target
	 * @param label
	 * @param validator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ComboBoxValidationController createComboBoxRequired(ValidationListener listener, JComboBox target, JLabel label, Validator<JComboBox> validator) {
		return createComboBoxValidationController(listener, target, label, new ComboBoxRequiredValidator(), validator);
	}
}
