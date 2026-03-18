package uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces;

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
 * @author qzd3k3
 * 
 * @version $Revision: 1.10 $
 */
public interface WitnessSessionSelector {
    /**
     * Returns true if there are witnesses in the specified week of the case.
     * The weekNumber starts at 1.
     * 
     * @pre weekNumber > 0
     * @pre caseId != null
     * @post return == ( getWitnessesForWeek(caseId, weekNumber).length > 0)
     * @param caseId
     *            caseId the case we are interested in.
     * @param weekNumber
     *            the week number to use as a criteria (starts at 1).
     * @return true if there are witnesses for the case and week.
     */
    public boolean areWitnessesInWeek(Integer caseId, int weekNumber);

    /**
     * Returns true if there are witnesses on the specified case.
     * 
     * @pre caseId != null
     * @post return == ( getWitnessesOnCaes(caseId).length > 0)
     * @param caseId
     *            caseId the case we are interested in.
     * @return true if there are witnesses for the case.
     */
    public boolean areWitnessesOnCase(Integer caseId);

    /**
     * Returns all the witnesses that will be appearing in the week specified
     * for the case specified.
     * 
     * @pre weekNumber > 0
     * @pre caseId != null
     * @post forall WitnessDetail wd in return | (wd.getMetaState() ==
     *       wd.UPDATED)
     * @post forall WitnessDetail wd2 in return |
     *       (wd2.getTrialSession().getDayNumber() <= weekNumber*5 &&
     *       wd2.getTrialSession().getDayNumber() > (weekNumber-1)*5)
     * 
     * @param caseId
     *            the case we are interested in.
     * @param weekNumber
     *            the week number to use as a criteria (starts at 1).
     * @return an array of matching WitnessSession's..
     * 
     * 
     */
    WitnessSession[] getWitnessesForWeek(Integer caseId, int weekNumber);

    /**
     * Returns all the witnesses that will be appearing on the day for the case
     * specified.
     * 
     * @pre dayNumber > 0
     * @pre caseId != null
     * @post forall WitnessDetail wd in return | (wd.getMetaState() ==
     *       wd.UPDATED)
     * @post forall WitnessDetail wd2 in return |
     *       (wd2.getTrialSession().getDayNumber() <= weekNumber*5 &&
     *       wd2.getTrialSession().getDayNumber() > (weekNumber-1)*5)
     * 
     * @param caseId
     *            the case we are interested in.
     * @param dayNumber
     *            the day number to use as a criteria (starts at 1).
     * @return an array of matching WitnessSession's..
     * 
     * 
     */
    WitnessSession[] getWitnessesForDay(Integer caseId, int dayNumber);

    /**
     * Return all witnesses for today
     * 
     * @param caseId
     *            the case interested in
     * 
     */
    WitnessSession[] getTodayWitnesses(Integer caseId);

    /**
     * Return all witnesses
     * 
     * @param caseId
     *            the case interested in
     * 
     */
    WitnessSession[] getAllWitnessesForSession(Integer caseId);

    /**
     * Return all future witnesses
     * 
     * @param caseId
     *            the case interested in
     * 
     */
    WitnessSession[] getAllFutureWitnesses(Integer caseId);

    /**
     * Return all today and future witnesses
     * 
     * @param caseId
     *            the case interested in
     * 
     */
    WitnessSession[] getAllTodayAndFutureWitnesses(Integer caseId);
}
