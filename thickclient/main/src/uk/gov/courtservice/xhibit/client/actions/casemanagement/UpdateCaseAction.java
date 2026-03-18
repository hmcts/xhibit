package uk.gov.courtservice.xhibit.client.actions.casemanagement;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseProcess;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseStatus;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseType;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseMaintain;
import uk.gov.courtservice.xhibit.client.casemanagement.ManageCase;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationControllerImpl;

public class UpdateCaseAction extends XAction {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());

	public UpdateCaseAction() {
        populateFromBundle("UpdateCase");
	}
	
	public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
		log.debug("CaseUpdate");
        XhibitApplicationController xac = (XhibitApplicationController)getController();
        ((XhibitApplicationControllerImpl) xac).setNewTitle("Amend Case");
        CaseMaintain updateCase = new CaseMaintain(xac);
        if (updateCase.getCaseId() !=  null) {
        	ManageCase manageCase = new ManageCase(xac, updateCase.getCaseId());
        }
	}

}
