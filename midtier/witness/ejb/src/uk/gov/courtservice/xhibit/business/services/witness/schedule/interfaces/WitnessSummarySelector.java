package uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces;

import uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException;
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
public interface WitnessSummarySelector {
    /**
     * Get all the witnesses for a case as summaries.
     * 
     * @param caseId
     * @return
     * @throws CaseNotFoundException
     * @throws NoScheduleForCaseException
     */
    WitnessSummary[] getAllWitnesses(Integer caseId) throws CaseNotFoundException, NoScheduleForCaseException;

    /**
     * Get all witnesses who will appear in the future. That is those sessions
     * that are after today.
     * 
     * @param caseId
     * @return
     * @throws
     *            uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException
     * @throws NoScheduleForCaseException
     */
    WitnessSummary[] getFutureWitnesses(Integer caseId) throws CaseNotFoundException, NoScheduleForCaseException;

    /**
     * Get all witnesses who have appeared in the past. That is those sessions
     * that are before today.
     * 
     * @param caseId
     * @return
     * @throws CaseNotFoundException
     * @throws NoScheduleForCaseException
     */
    WitnessSummary[] getPastWitnesses(Integer caseId) throws CaseNotFoundException, NoScheduleForCaseException;

    WitnessSummary[] getSignedInWitnesses(Integer caseId, Integer courtId);

    /**
     * Get all witnesses appearing today or in the future.
     * 
     * @param caseId
     * @return
     * @throws CaseNotFoundException
     * @throws NoScheduleForCaseException
     */
    WitnessSummary[] getTodayAndFutureWitnesses(Integer caseId) throws CaseNotFoundException,
            NoScheduleForCaseException;

    /**
     * Get all witnesses that are appearing today.
     * 
     * @param caseId
     * @return
     * @throws CaseNotFoundException
     * @throws NoScheduleForCaseException
     */
    WitnessSummary[] getTodaysWitnesses(Integer caseId) throws CaseNotFoundException, NoScheduleForCaseException;

}
