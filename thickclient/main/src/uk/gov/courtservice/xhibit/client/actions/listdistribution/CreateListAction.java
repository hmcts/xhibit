package uk.gov.courtservice.xhibit.client.actions.listdistribution;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.listings.createlist.CreateListDialog;
import uk.gov.courtservice.xhibit.client.listings.createlist.CreateListModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class CreateListAction extends XAction {

	private static final long serialVersionUID = 1L;


	public CreateListAction() {
		populateFromBundle("CreateList");
	}

	public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
		XhibitApplicationController xac = (XhibitApplicationController)getController();
		ListMenuAccess listMenuAccess = new ListMenuAccess(xac);
		if (listMenuAccess.getCaseId() == 0){
			CreateListDialog createListDialog = new CreateListDialog((java.awt.Frame) getController(), new CreateListModel());
			createListDialog.setVisible(true);
		}

	}

}