package uk.gov.courtservice.xhibit.client.util.validation;

import javax.swing.JComponent;
import javax.swing.JLabel;

import uk.gov.courtservice.xhibit.client.util.XDatePanel;

/**
 * View implementation which supports the XDatePanel control.
 * 
 * @author uphillj
 *
 */
public class DateValidationView extends AbstractComponentValidationView<XDatePanel> {

	public DateValidationView(XDatePanel component, JLabel label) {
		super(XDatePanel.class, component, label);
	}

	@Override
	protected JComponent getInputField() {
		return getComponent().getDateComponent();
	}
}
