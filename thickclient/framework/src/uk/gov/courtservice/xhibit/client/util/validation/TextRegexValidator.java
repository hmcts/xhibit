package uk.gov.courtservice.xhibit.client.util.validation;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.JTextComponent;

/**
 * Validate that if text has been entered in the JTextComponent control it
 * matches the regular expression. Use the TextRequiredValidator instead
 * if the field is mandatory and must have non-whitespace text entered.
 * 
 * @author uphillj
 *
 */
public class TextRegexValidator extends AbstractTextValidator {

	private Pattern pattern;
	
	public TextRegexValidator(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}

	@Override
	public void validate(JTextComponent target, List<String> errors) {
		if (hasLength(target)) {
			Matcher matcher = pattern.matcher(target.getText());
			if (!matcher.matches()) {
				errors.add("Invalid entry");
			}
		}
	}
}
