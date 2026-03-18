package uk.gov.courtservice.xhibit.client.util.validation;

import javax.swing.text.JTextComponent;

/**
 * Base class for validators used to validate JTextComponent controls, e.g. JTextField.
 * 
 * @author uphillj
 *
 */
public abstract class AbstractTextValidator extends AbstractValidator<JTextComponent> {

	public AbstractTextValidator() {
		super(JTextComponent.class);
	}
	
	/**
	 * Return true if not null and contains any characters.
	 * 
	 * @param component
	 * @return
	 */
	protected boolean hasLength(JTextComponent component) {
		return ValidationUtils.hasLength(component);
	}

	/**
	 * Return true if text contains any non-whitespace characters.
	 * 
	 * @param component
	 * @return
	 */
	protected boolean hasText(JTextComponent component) {
		return ValidationUtils.hasText(component);
	}
}
