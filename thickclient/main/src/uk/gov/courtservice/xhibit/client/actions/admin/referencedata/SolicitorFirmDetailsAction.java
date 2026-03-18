package uk.gov.courtservice.xhibit.client.actions.admin.referencedata;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.AbstractSearchAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class SolicitorFirmDetailsAction extends XAction {

	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());

	public SolicitorFirmDetailsAction() {
		populateFromBundle("SolicitorFirmDetails");
	}

	public void xActionPerformed(ActionEvent e) throws CSRecoverableException, Exception {
		log.debug("Solicitor Firm Details menu item clicked");

		XhibitApplicationController xac = (XhibitApplicationController) getController();
		xac.close();

		AbstractSearchAction sa = (AbstractSearchAction) XhibitActions.getAction(xac,
				XhibitActions.OpenSearchSolicitorFirm);
		sa.setCaller(this);
		sa.xActionPerformed(e);
	}
}