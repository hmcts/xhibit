package uk.gov.courtservice.xhibit.client.util.validation;

import java.awt.event.FocusEvent;
import java.util.List;

import mseries.Calendar.MFieldListener;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;

/**
 * Validation controller implementation for XDatePanel controls.
 * 
 * @author uphillj
 *
 */
public class DateValidationController extends AbstractComponentValidationController<XDatePanel> implements MFieldListener {

	private XDatePanel target = null;
	
	public DateValidationController(ValidationListener listener, XDatePanel target, ValidationView view, List<Validator<XDatePanel>> validators) {
		super(XDatePanel.class, ValidationStrategy.ON_FOCUS_LOST, listener, target, view, validators);
		this.target = target;
	}

	@Override
	protected boolean isTargetModifiable() {
		return (getTarget().getDateComponent().isEditable() &&
				getTarget().getDateComponent().isEnabled());
	}

	/**
	 * Date specific focus listener is required to validate control.
	 */
	@Override
	protected void setFocusListener() {
		getTarget().getDateComponent().addMFieldListener(this);
	}

	/**
	 * No operation required on date specific focus gain.
	 */
	@Override
	public void fieldEntered(FocusEvent event) {
	}

	/**
	 * Date specific focus lost implementation to validate control.
	 */
	@Override
	public void fieldExited(FocusEvent event) {
		target.parseAndConvertDateString();
		performValidation(true);
	}
}
