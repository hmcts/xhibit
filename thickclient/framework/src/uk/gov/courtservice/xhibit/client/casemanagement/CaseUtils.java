package uk.gov.courtservice.xhibit.client.casemanagement;

import javax.swing.JLabel;

/**
 * A class used to keep cross class functionality 
 * @author waltersn
 *
 */
public class CaseUtils {
	
	/*
	 * Used to dynamically add a asterisk to a label to indicate its mandatory -
	 * C.Kudzin CTX-1810
	 */
	public static void addMandatoryLabel(JLabel label) {
		String text = label.getText();
		if (!(text.substring(text.length() - 1).equals("*"))) {
			text = text + "*";
			label.setText(text);
		}
	}

	/*
	 * Used to dynamically remove a asterisk to a label to indicate its no
	 * longer mandatory - C.Kudzin CTX-1810
	 */
	public static void removeMandatoryLabel(JLabel label) {
		String text = label.getText();
		if (text.substring(text.length() - 1).equals("*")) {
			text = text.substring(0, text.length() - 1);
			label.setText(text);
		}
	}
	
	public static boolean compareObjectsWithNullCheck(Object obj1, Object obj2) {
			if (obj1 == null && obj2 == null) {
				return true;
			} else if (obj1 == null && obj2 != null) {
				if (obj2.getClass().equals(String.class)) {
					if (((String) obj2).isEmpty()) {
						return true;
					} else { 
						return false;
					}
				}
				return false;
			} else if (obj1 != null && obj2 == null) {
				if (obj1.getClass().equals(String.class)) {
					if (((String) obj1).isEmpty()) {
						return true;
					} else { 
						return false;
					}
				}			
				return false;
			} else {	// both are not null
				if (obj1.getClass().equals(obj2.getClass())) {
					if (obj1.equals(obj2)) {
						return true;
					}
				}
			}
			return false;
		}
}
