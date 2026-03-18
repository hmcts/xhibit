package uk.gov.courtservice.xhibit.client.actions.listdistribution;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.listings.listresults.ListResultsDialog;
import uk.gov.courtservice.xhibit.client.listings.listresults.ListResultsModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class ListResultsAction extends XAction {

	private static final long serialVersionUID = 1L;

	public ListResultsAction() {
		populateFromBundle("ListResults");
	}

	public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
		XhibitApplicationController xac = (XhibitApplicationController)getController();
		//method to check if case already open before opening dialog 	
		ListMenuAccess listMenuAccess = new ListMenuAccess(xac);
		if (listMenuAccess.getCaseId() == 0){
			ListResultsDialog listResultsDialog = new ListResultsDialog((java.awt.Frame) getController(), new ListResultsModel(xac));
			listResultsDialog.setVisible(true);
		}

	}

}