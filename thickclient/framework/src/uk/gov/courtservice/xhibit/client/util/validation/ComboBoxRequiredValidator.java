package uk.gov.courtservice.xhibit.client.util.validation;

import java.util.List;

import javax.swing.JComboBox;

/**
 * Validate that the combo box has a valid selection.
 * 
 * @author uphillj
 *
 */
public class ComboBoxRequiredValidator extends AbstractComboBoxValidator {

	@Override
	public void validate(JComboBox target, List<String> errors) {
		if (!hasSelection(target)) {
			errors.add("Field is mandatory");
		}
	}

}
