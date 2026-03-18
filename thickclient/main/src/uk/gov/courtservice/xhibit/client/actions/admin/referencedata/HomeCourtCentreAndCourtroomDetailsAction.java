package uk.gov.courtservice.xhibit.client.actions.admin.referencedata;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.admin.referencedata.HomeCourtDialog;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class HomeCourtCentreAndCourtroomDetailsAction extends XAction {

	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass()); 
	
	HomeCourtDialog homeCourtDialog ;

	public HomeCourtCentreAndCourtroomDetailsAction() {
		populateFromBundle("HomeCourtCentreAndCourtroomDetails");
	}

	public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
		log.debug("Home Court Centre And Courtroom Details menu item clicked");

		XhibitApplicationController xac = (XhibitApplicationController) getController();
		xac.close();
		
		homeCourtDialog = new HomeCourtDialog(xac);
		homeCourtDialog.setLocationRelativeTo(xac);
		homeCourtDialog.setVisible(true);
	}
}