package uk.gov.courtservice.xhibit.client.casemanagement;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class ManageCase {
	private CaseXPanel manageCasePanel;
	private final Logger log = CSServices.getLogger(getClass());

	public ManageCase(XhibitApplicationController xac, CaseType c) throws CSRecoverableException {
		log.debug("CreateCase: " + c.toString());
		xac.getCaseStatus().setCaseProcess(CaseProcess.NEW);
		xac.getCaseStatus().setCaseType(c);		
		xac.getCaseStatus().setCaseCreateInProgressFlag(true);	
		xac.getCaseStatus().setCaseId(-1);
		//need to surround with try catch as if an EJBException has been thrown during setup
		//eg. pros populate then we need to set all the above back to null
		try {
			manageCasePanel = new CaseXPanel(xac);
			xac.open(manageCasePanel);
		} catch(CSUnrecoverableException e) {
			xac.getCaseStatus().setCaseCreateInProgressFlag(false);
			xac.getCaseStatus().setCaseId(0);
			
			log.error("An error occurred : during managing the case " + e);			
			xac.close();
			throw e;

		}
	}
	
	public ManageCase(XhibitApplicationController xac, Integer caseId) throws CSRecoverableException {
		log.debug("ManageCase: " + caseId);
		if (caseId > 0) {
			xac.getCaseStatus().setCaseProcess(CaseProcess.AMEND);
			xac.getCaseStatus().setCaseId(caseId);
 			xac.getCaseStatus().setCaseCreateInProgressFlag(true);	
 			try {
 				manageCasePanel = new CaseXPanel(xac);
 				xac.open(manageCasePanel);
 			} catch(CSUnrecoverableException e) {
 				xac.getCaseStatus().setCaseCreateInProgressFlag(false);
 				xac.getCaseStatus().setCaseId(0);
 				log.error("An error occurred : during managing the case " + e);
 				xac.close();
 				throw e;

 			}
		}
	}

}
