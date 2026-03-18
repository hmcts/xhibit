package uk.gov.courtservice.xhibit.business.services.schedhearingdefendant;

import java.util.Collection;

import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendantMaintainer;

/**
 * <p>
 * Title: SchedHearingDefendantControllerBean
 * </p>
 * <p>
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * @version 1.0
 * 
 * @ejb.bean name="SchedHearingDefendantController" 
 * 			 description="Scheduled hearing defendant Session Bean" 
 * 			 type="Stateless" 
 * 			 view-type="both"
 *           jndi-name="SchedHearingDefendantControllerHome"
 *           local-jndi-name="SchedHearingDefendantControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * 
 */
public class SchedHearingDefendantControllerBean extends CSSessionBean implements SessionBean {

	/**
	 * @param defOnCaseId
	 * @return Collection
	 * @throws ObjectNotFoundException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection findByDefendantOnCaseId(Integer defOnCaseId)
			throws SchedHearingDefendantException, ObjectNotFoundException {
		SchedHearingDefendantMaintainer shdMaintainer = new SchedHearingDefendantMaintainer();
		try {
			debug("HearingScheduleControllerBean.getScheduledHearings(): exec.");
			return shdMaintainer.findByDefOnCaseId(defOnCaseId);
		} finally {
			debug("finishing SchedHearingDefendantControllerBean.findByDefendantOnCaseId()");
		}
	}

	/**
	 * Convenience method for the log4j debugger tool.
	 * 
	 * @param message
	 */
	private void debug(String message) {
		log.debug(message);
	}
}