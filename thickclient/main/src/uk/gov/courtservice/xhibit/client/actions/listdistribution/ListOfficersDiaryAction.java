package uk.gov.courtservice.xhibit.client.actions.listdistribution;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Date;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.listings.notes.ListOfficersDiaryDialog;
import uk.gov.courtservice.xhibit.client.listings.notes.ListOfficersDiaryModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class ListOfficersDiaryAction extends XAction {

	private static final long serialVersionUID = 1L;

	public ListOfficersDiaryAction() {
        populateFromBundle("ListOfficersDiary");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
		XhibitApplicationController xac = (XhibitApplicationController) getController(); 
		ListOfficersDiaryModel model = new ListOfficersDiaryModel(new Date());		
		ListOfficersDiaryDialog dialog = new ListOfficersDiaryDialog((Frame) xac, model);
		if (dialog.isDialogValid()) {
			dialog.setVisible(true);
		} else {
			dialog.dispose();
		}
    }

}