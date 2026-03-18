package uk.gov.courtservice.xhibit.client.actions.casemanagement;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseProcess;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseStatus;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseType;
import uk.gov.courtservice.xhibit.client.casemanagement.ManageCase;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationControllerImpl;

public class CreateAppealCaseAction extends XAction {

	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private ManageCase appealCase;

	public CreateAppealCaseAction() {
		populateFromBundle("CreateAppealCase");
	}

	public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
		String exitString = "Do you want to exit the case?"; // default
		log.debug("Create Criminal Appeal Case menu item clicked");
		XhibitApplicationController xac = (XhibitApplicationController) getController();
		// check if there is already a case creation open
		if ((xac.getCaseStatus().getCaseCreateInProgressFlag() || 
				((XhibitApplicationControllerImpl) xac).isCaseOpened()) && !xac.isCaseChargesDisposalsOpened()) {
			log.debug("Already a case creation in progress");

			if(!xac.getCaseStatus().getCaseCreateInProgressFlag())	// Accessed from outside of Case Create/Amend
				xac.close();			
			else if(xac.getCaseStatus().getCaseProcess() == CaseProcess.NEW) 		// New create
				exitString = "The case creation process is incomplete. All changes will be lost, do you want to exit?";
			else if(xac.getCaseStatus().getCaseProcess() == CaseProcess.AMEND || 
					xac.getCaseStatus().getCaseProcess() == CaseProcess.SAYG_AMEND)	// Amend/Update
				exitString = "Do you want to exit the case?";			
			
			if(xac.getCaseStatus().getCaseCreateInProgressFlag()) {	// only create the custom pop-up if accessed inside of CC/CA 
				int result = JOptionPane.showConfirmDialog((Component) null, exitString, "Create Management",
						JOptionPane.YES_NO_OPTION);
				if ((result == JOptionPane.NO_OPTION) || (result == JOptionPane.CLOSED_OPTION)) {
					log.debug("Dialog No option clicked, will not create new case");
					return;
				} else {
					log.debug("New case being created overwriting old case");
				}
			}
		}

		((XhibitApplicationControllerImpl) xac).setNewTitle("Create Criminal Appeal Case");
		appealCase = new ManageCase(xac, CaseType.APPEAL);
	}

}
