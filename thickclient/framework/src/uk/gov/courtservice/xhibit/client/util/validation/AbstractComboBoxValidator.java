package uk.gov.courtservice.xhibit.client.util.validation;

import javax.swing.JComboBox;

public abstract class AbstractComboBoxValidator extends AbstractValidator<JComboBox> {

	public AbstractComboBoxValidator() {
		super(JComboBox.class);
	}

	/**
	 * Returns true if the user has made a valid selection
	 * in the combo box.
	 * 
	 * @param component
	 * @return
	 */
	protected boolean hasSelection(JComboBox component) {
		return ValidationUtils.hasSelection(component);
	}
}
