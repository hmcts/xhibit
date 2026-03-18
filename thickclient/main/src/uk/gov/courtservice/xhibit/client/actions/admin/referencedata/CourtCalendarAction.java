package uk.gov.courtservice.xhibit.client.actions.admin.referencedata;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.admin.referencedata.CourtCalendarDialog;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class CourtCalendarAction extends XAction {

	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());

	public CourtCalendarAction() {
		populateFromBundle("CourtCalendar");
	}

	public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
		log.debug("Court Calendar menu item clicked");

		XhibitApplicationController xac = (XhibitApplicationController) getController();
		xac.close();

		CourtCalendarDialog myDialog = new CourtCalendarDialog(xac);

		myDialog.setVisible(true);
	}
}