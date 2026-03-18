package uk.gov.courtservice.xhibit.client.util.validation;

import java.awt.Color;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Base class for all validation view implementations for JComponent controls.
 * 
 * @author uphillj
 *
 */
public abstract class AbstractComponentValidationView<T extends JComponent> implements ValidationView {

	private Class<T> componentType;
	private T component;
	private JLabel label;
	private Border errorsBorder;
	private Border originalBorder;
	private PropertyChangeListener disabledListener;
	
	public AbstractComponentValidationView(Class<T> componentType, T component, JLabel label) {
		// Local copies of the supplied parameters
		this.componentType = componentType;
		this.component = component;
		this.label = label;
		
		// Initialise border for errors, setting line border overwrites the insets attached to the already existing border
		// on the component, therefore it's necessary to add an empty border between the line border and the component 
		// of the correct inset values, else the component will resize when an error is flagged.
		if(this.component.getBorder() != null) {	
			Insets copyInsets = this.component.getBorder().getBorderInsets(this.component);
			Insets newInsets = new Insets(copyInsets.top - 1, copyInsets.left - 1, copyInsets.bottom - 1, copyInsets.right - 1);
			this.errorsBorder = BorderFactory.createCompoundBorder(new LineBorder(java.awt.Color.RED, 1), new EmptyBorder(newInsets));
		} else {
			this.errorsBorder = BorderFactory.createLineBorder(java.awt.Color.RED);	
		}
		// Initialise listener for when the control is disabled
		this.disabledListener = new ComponentDisabledListener();
		getInputField().addPropertyChangeListener("enabled", disabledListener);
		getInputField().addPropertyChangeListener("editable", disabledListener);
	}

	/**
	 * Class of the object that this view handles.
	 * 
	 * @return
	 */
	public Class<T> getComponentType() {
		return componentType;
	}
	
	/**
	 * Get the component managed by this class.
	 * 
	 * @return
	 */
	protected T getComponent() {
		return component;
	}

	/**
	 * Sub-classes override to supply the component which accepts
	 * the user input which in most cases is just the component
	 * but could be an inner component, e.g. XDatePanel.
	 * 
	 * @return
	 */
	protected abstract JComponent getInputField();

	/**
	 * Show the first error in the label and change border.
	 * 
	 * @param errors
	 */
	@Override
	public void showErrors(List<String> errors) {
		// Change to error border if not already shown
		if (!errorsBorder.equals(component.getBorder())) {
			originalBorder = component.getBorder();
			component.setBorder(errorsBorder);
		}
		
		// Show first error
		label.setForeground(Color.RED);
		label.setText(errors.get(0));
		label.setVisible(true);
	}

	/**
	 * Hide the label and restore border.
	 */
	@Override
	public void clearErrors() {
		// Change to original border if showing error
		if (errorsBorder.equals(component.getBorder())) {
			component.setBorder(originalBorder);
		}
		
		// Prevent resize by setting to empty string rather than hiding
		label.setText(" ");
	}
	
	/**
	 * Property change listener used to clear errors when control disabled
	 */
	protected class ComponentDisabledListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (Boolean.FALSE.equals(evt.getNewValue())) {
				AbstractComponentValidationView.this.clearErrors();
			}
		}
	}
}
