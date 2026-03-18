package uk.gov.courtservice.xhibit.client.util.validation;

import java.util.List;

import javax.swing.text.JTextComponent;

/**
 * Validate that at least one non-whitespace character has been
 * entered in the JTextComponent control.
 * 
 * @author uphillj
 *
 */
public class TextRequiredValidator extends AbstractTextValidator {

	@Override
	public void validate(JTextComponent target, List<String> errors) {
		if (!hasText(target)) {
			errors.add("Field is mandatory");
		}
	}
}
