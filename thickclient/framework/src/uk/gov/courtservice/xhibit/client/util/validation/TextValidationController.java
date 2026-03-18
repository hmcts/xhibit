package uk.gov.courtservice.xhibit.client.util.validation;

import java.util.List;

import javax.swing.text.JTextComponent;

/**
 * Validation controller implementation for JTextComponent controls, e.g. JTextField.
 * 
 * @author uphillj
 *
 */
public class TextValidationController extends AbstractComponentValidationController<JTextComponent> {

	public TextValidationController(ValidationListener listener, JTextComponent target, ValidationView view, List<Validator<JTextComponent>> validators) {
		super(JTextComponent.class, ValidationStrategy.INPUT_VERIFIER, listener, target, view, validators);
	}

	@Override
	protected boolean isTargetModifiable() {
		return (getTarget().isEditable() && getTarget().isEnabled());
	}

}
