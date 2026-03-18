package uk.gov.courtservice.xhibit.client.actions.admin.referencedata;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.admin.referencedata.ChamberAndAdvocateDetailsDialog;
import uk.gov.courtservice.xhibit.client.admin.referencedata.ChamberAndAdvocateDetailsModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationControllerImpl;

public class ChamberAndAdvocateDetailsAction extends XAction {

	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	ChamberAndAdvocateDetailsDialog chamberAndAdvocateDetailsDialog;

	public ChamberAndAdvocateDetailsAction() {
		populateFromBundle("ChamberAndAdvocateDetails");
	}

	public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
		XhibitApplicationController xac = (XhibitApplicationController) getController();
		xac.close();
		chamberAndAdvocateDetailsDialog = new ChamberAndAdvocateDetailsDialog(xac, new ChamberAndAdvocateDetailsModel(false));
		chamberAndAdvocateDetailsDialog.setLocationRelativeTo(xac);
		chamberAndAdvocateDetailsDialog.setVisible(true);
	}

}