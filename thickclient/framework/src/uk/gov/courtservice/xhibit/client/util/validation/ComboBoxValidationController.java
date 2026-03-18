package uk.gov.courtservice.xhibit.client.util.validation;

import java.util.List;

import javax.swing.JComboBox;

/**
 * Validation controller implementation for JComboBox controls.
 * 
 * @author uphillj
 *
 */
public class ComboBoxValidationController extends AbstractComponentValidationController<JComboBox> {

	public ComboBoxValidationController(ValidationListener listener, JComboBox target, ValidationView view, List<Validator<JComboBox>> validators) {
		super(JComboBox.class, ValidationStrategy.INPUT_VERIFIER, listener, target, view, validators);
	}

	@Override
	protected boolean isTargetModifiable() {
		return getTarget().isEnabled();
	}

}
