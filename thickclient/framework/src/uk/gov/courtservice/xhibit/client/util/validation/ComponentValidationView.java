package uk.gov.courtservice.xhibit.client.util.validation;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * View implementation which supports simple JComponent implementations.
 * 
 * @author uphillj
 *
 */
public class ComponentValidationView extends AbstractComponentValidationView<JComponent> {

	public ComponentValidationView(JComponent component, JLabel label) {
		super(JComponent.class, component, label);
	}

	@Override
	protected JComponent getInputField() {
		return getComponent();
	}
}
