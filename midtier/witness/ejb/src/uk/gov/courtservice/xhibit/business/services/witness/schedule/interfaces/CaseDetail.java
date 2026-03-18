package uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces;

import uk.gov.courtservice.xhibit.business.services.witness.exceptions.DurationLessThanMinimumException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.NoDirectionsForCaseException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.NoJudgeForCaseException;

/**
 * <p>
 * Title: Case Details.
 * </p>
 * <p>
 * Description: An interface which provides access to the parts of a Case that
 * are relevant to Witnes Facilities.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * 
 * @version $Revision: 1.10 $
 * 
 * @invariant getMetaState() != REMOVED
 */
public interface CaseDetail extends java.io.Serializable,
        uk.gov.courtservice.xhibit.business.services.witness.interfaces.StateManaged {
    /**
     * Obtain the primary key (that is the caseId)
     * 
     * @pre getMetaState() != REMOVED
     * @post (getMetaState() == UPDATED || getMetaState() == MODIFIED) implies
     *       return != null
     * @return the primary key
     */
    public Integer getId();

    /**
     * @return A string array of defendant names.
     * 
     * @post return != null
     */
    public String[] getDefendantNames();

    /**
     * 
     * @return the name of the court the case will be heard in.
     */
    public String getCourtName();

    /**
     * 
     * @return the court code for the court.
     */
    public String getCourtCode();

    /**
     * 
     * @return
     */
    public String getBailMagCode();

    /**
     * 
     * @return
     */
    public String getPoliceOfficerAttending();

    /**
     * 
     * @return
     */
    public String getCpsCaseWorker();

    /**
     * 
     * @return
     */
    public String getCaseTitle();

    /**
     * 
     * @return
     */
    public String getCaseNumber();

    /**
     * 
     * @return the case type (eg. "T" or "S")
     */
    public String getCaseType();

    /**
     * 
     * @return court prefix eg: 'Crown Court'
     */
    public String getCourtPrefix();

    /**
     * @pre cpsCaseWorker != null
     * 
     * @param cpsCaseWorker
     */
    public void setCpsCaseWorker(String cpsCaseWorker);

    /**
     * 
     * @param policeOfficerAttending
     */
    public void setPoliceOfficerAttending(String policeOfficerAttending);

    /**
     * 
     * @return the full name of the judge.
     */
    public String getJudgeName(String userDisplayName) throws NoJudgeForCaseException;

    /**
     * 
     * @return the estimated case duration in days.
     */
    public int getEstimatedCaseDuration() throws NoDirectionsForCaseException;

    /**
     * Set the estimated case duration.
     * 
     * @param durationInDays
     * @param force
     *            do not throw an exception if constraint is broken
     * @throws DurationLessThanMinimumException
     *             if duration rounded up to the nearest half day is less than
     *             the last witness day number + session.
     */
    public void setEstimatedCaseDuration(float durationInDays, boolean force) throws DurationLessThanMinimumException,
            NoDirectionsForCaseException;

}
