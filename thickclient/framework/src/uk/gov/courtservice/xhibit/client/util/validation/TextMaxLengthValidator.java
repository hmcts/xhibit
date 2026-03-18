package uk.gov.courtservice.xhibit.client.util.validation;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.JTextComponent;

/**
 * Validate that if text has been entered in the JTextComponent control it
 * is not longer than the maximum length allowed.
 * 
 * @author uphillj
 *
 */
public class TextMaxLengthValidator extends AbstractTextValidator {

	private int maxLength;
	
	public TextMaxLengthValidator(int maxLength) {
		this.maxLength = maxLength;
	}

	@Override
	public void validate(JTextComponent target, List<String> errors) {
		if (hasLength(target)) {
			if (target.getText().length() > maxLength) {
				errors.add("Max length is " + maxLength);
			}
		}
	}
}
