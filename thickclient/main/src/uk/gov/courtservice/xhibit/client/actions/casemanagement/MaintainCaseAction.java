package uk.gov.courtservice.xhibit.client.actions.casemanagement;

import java.awt.event.ActionEvent;
import java.util.List;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseMaintain;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseType;
import uk.gov.courtservice.xhibit.client.casemanagement.ManageCase;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationControllerImpl;

public class MaintainCaseAction extends XAction {
    
	/**
	 * serialversionuid
	 */
	private static final long serialVersionUID = 1L;
	public static final List<String> CASE_TYPES = CaseType.CaseListingCaseTypes();   
	public MaintainCaseAction() {
        populateFromBundle("MaintainCase");
	}
	
	public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController)getController();
        CaseMaintain maintainCase = new CaseMaintain(xac, CASE_TYPES);
        if (maintainCase.getCaseId() !=  null) {
        	if(maintainCase.getCaseId() > 0) {	// returns 0 by default so just null check wasn't enough
	        	ManageCase manageCase = new ManageCase(xac, maintainCase.getCaseId());
	        	((XhibitApplicationControllerImpl) xac).setNewTitle("Case "+xac.getCaseStatus().getCaseTitle()+" - Amend Case");
        	}
        }
	}
}
