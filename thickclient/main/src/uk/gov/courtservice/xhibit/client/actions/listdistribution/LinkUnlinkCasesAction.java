package uk.gov.courtservice.xhibit.client.actions.listdistribution;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class LinkUnlinkCasesAction extends XAction {

	private static final long serialVersionUID = 1L;
	
    private XhibitApplicationController xac;
    
	public LinkUnlinkCasesAction() {
		populateFromBundle("LinkUnlinkCases");
	}

	public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
		xac = (XhibitApplicationController)getController();
	}

}



