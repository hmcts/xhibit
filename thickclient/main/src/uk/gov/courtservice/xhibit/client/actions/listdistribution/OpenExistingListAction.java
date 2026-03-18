package uk.gov.courtservice.xhibit.client.actions.listdistribution;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.listings.createlist.CreateListDialog;
import uk.gov.courtservice.xhibit.client.listings.createlist.CreateListModel;
import uk.gov.courtservice.xhibit.client.openexistinglist.OpenExistingListDialog;
import uk.gov.courtservice.xhibit.client.openexistinglist.OpenExistingListModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class OpenExistingListAction extends XAction {

	private static final long serialVersionUID = 1L;

	public OpenExistingListAction() {
		populateFromBundle("OpenExistingList");
	}

	public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
		XhibitApplicationController xac = (XhibitApplicationController)getController();
		//method to check if case already open before opening dialog 	
		ListMenuAccess listMenuAccess = new ListMenuAccess(xac);
		if (listMenuAccess.getCaseId() == 0){
			OpenExistingListDialog openExistingListDialog = new OpenExistingListDialog((java.awt.Frame) getController(), new OpenExistingListModel());
			openExistingListDialog.setVisible(true);
		}

	}

}


