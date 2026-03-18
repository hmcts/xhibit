package uk.gov.courtservice.xhibit.client.util.validation;

import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;

import uk.gov.courtservice.xhibit.client.util.XDatePanel;

/**
 * Utilities class with methods to validate Swing components.
 * 
 * @author uphillj
 *
 */
public class ValidationUtils {

	private ValidationUtils() {}

	/**
	 * Returns true if the user has made a valid selection
	 * in the combo box.
	 * 
	 * @param component
	 * @return
	 */
	public static boolean hasSelection(JComboBox component) {
		return (component.getSelectedIndex() > 0);
	}

	/**
	 * Return true if not null and contains any characters.
	 * 
	 * @param component
	 * @return
	 */
	public static boolean hasLength(XDatePanel component) {
		String str = component.getText();
		return (str != null && str.length() > 0);
	}
	
	/**
	 * Return true if contains valid date.
	 * 
	 * @param component
	 * @return
	 */
	public static boolean hasDate(XDatePanel component) {
		return hasLength(component) && component.isDateValidate();
	}
	
	/**
	 * Return true if not null and contains any characters.
	 * 
	 * @param component
	 * @return
	 */
	public static boolean hasLength(JTextComponent component) {
		String str = component.getText();
		return (str != null && str.length() > 0);
	}

	/**
	 * Return true if text contains any non-whitespace characters.
	 * 
	 * @param component
	 * @return
	 */
	public static boolean hasText(JTextComponent component) {
		// Default is no text in value
		boolean text = false;
		
		// Text in value if at least one non-whitespace character
		if (hasLength(component)) {
			String str = component.getText();
			int length = str.length();
			for (int i = 0; i < length; i++) {
				if (!Character.isWhitespace(str.charAt(i))) {
					text = true;
					break;
				}
			}
		}
		
		return text;
	}

}
