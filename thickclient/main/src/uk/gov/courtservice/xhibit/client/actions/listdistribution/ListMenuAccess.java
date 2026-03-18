package uk.gov.courtservice.xhibit.client.actions.listdistribution;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationControllerImpl;

public class ListMenuAccess {
	private final Logger log = CSServices.getLogger(getClass());
	private Integer caseId;	
	public ListMenuAccess(XhibitApplicationController xac ) throws CSRecoverableException {
		log.debug("ListMenuAccess:");
		setCaseId(0);
		if (xac.getCaseStatus().getCaseCreateInProgressFlag() == true || 
				((XhibitApplicationControllerImpl) xac).isCaseOpened()) {
			log.debug("Already a case creation in progress");

			if(!xac.getCaseStatus().getCaseCreateInProgressFlag()) 	// Accessed from outside of Case Create/Amend
				xac.close();
			else {
				// only create the custom pop-up if accessed inside of CC/CA 
				int result = JOptionPane.showConfirmDialog((Component) null, "Do you want to exit the case?", "Case Management",
						JOptionPane.YES_NO_OPTION);
				if ((result == JOptionPane.NO_OPTION) || (result == JOptionPane.CLOSED_OPTION)) {
					log.debug("Dialog No option clicked, will not create new update case");
					setCaseId(1);
					return;
				} 
			}

		}
		try {
			xac.getCaseStatus().setCaseCreateInProgressFlag(false);
			xac.getCaseStatus().setCaseId(0);			
			xac.close();
		} catch (CSRecoverableException e) {
			e.printStackTrace();
		}	
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Integer getCaseId() {
		return caseId;
	}

}	


