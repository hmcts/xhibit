package uk.gov.courtservice.xhibit.client.actions.listdistribution;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.listings.details.CaseListingDetailDialog;
import uk.gov.courtservice.xhibit.client.listings.details.CaseListingDetailModel;
import uk.gov.courtservice.xhibit.client.util.XAction;

public class CaseListingDetailAction extends XAction {

	private static final long serialVersionUID = 1L;

	public CaseListingDetailAction() {
        populateFromBundle("CaseListingDetail");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
    	Frame frame = (Frame) getController();
    	CaseListingDetailModel model = (CaseListingDetailModel) this.getModel();
    	// Call Case Listing Detail Dialog
    	CaseListingDetailDialog dialog = new CaseListingDetailDialog(frame, model);
    	dialog.setVisible(true);
    }

}