package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

import java.awt.Component;
import java.util.List;

import javax.swing.JOptionPane;

import uk.gov.courtservice.xhibit.client.util.XTextField;

public class RefSearchUpdatePanelUtil {
	/**
	 * Returns if there's unsaved data and whether to display a pop-up on
	 * pressing the Back or Cancel buttons. For insert scenario check if any
	 * fields contain data. For update scenario, as soon as any field is edited
	 * (i.e. keyReleased event).
	 * 
	 * @return Boolean
	 */
	public static Boolean hasUnsavedData(List<Component> componentList, boolean isUpdate, boolean isModified) {
		Boolean unsavedData = false;
		// For new record
		if (!isUpdate) {
			for (Component comp : componentList) {
				if (comp instanceof XTextField) {
					XTextField currentTextField = (XTextField) comp;
					if (!(currentTextField.getText().isEmpty())) {
						unsavedData = true;
						break;
					}
				}
			}
			// For existing record
		} else {
			unsavedData = isModified;
		}
		return unsavedData;
	}
	
	/**
	 * Pop up dialog to allow user to choose whether to discard changes or not.
	 * 
	 * @return
	 */
	public static int unsavedChangesDialog() {
		return JOptionPane.showConfirmDialog(null,
				"There are un-saved changes on the screen. All the changes will be lost.  Are you sure?",
				"Unsaved changes", JOptionPane.YES_NO_OPTION);
	}
}
