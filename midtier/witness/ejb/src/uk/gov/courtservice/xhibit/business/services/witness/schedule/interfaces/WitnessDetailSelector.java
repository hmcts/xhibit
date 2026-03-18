package uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces;

import uk.gov.courtservice.xhibit.business.services.witness.exceptions.NoScheduleForCaseException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: .
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.6 $
 * 
 */
public interface WitnessDetailSelector {

    /**
     * Returns an array containing all the witnesses for the case specified.
     * 
     * @param caseId
     * @post forall WitnessDetail wd in return | wd != null
     * @return
     * @throws
     *       uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException
     * @throws NoScheduleForCaseException
     */
    WitnessDetail[] getAllWitnessDetails(Integer caseId)
            throws uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException,
            NoScheduleForCaseException;

}
