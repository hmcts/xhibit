package uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces;

import java.util.Date;

import uk.gov.courtservice.xhibit.business.services.witness.exceptions.CouldNotCreateSessionException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleAlreadyIssued;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.SkeletonDayHasDateException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.TrialSessionNotFoundException;

/**
 * <p>
 * Title: Skeleton Schedule for Witness Facilities.
 * </p>
 * <p>
 * Description: Provides methods to work with a skeleton schedule. A skeleton
 * schedule is an initial and not neccesarily complete schedule for a court
 * case. It contains days which themselves contain sessions, a session for
 * example represents a morning or an afternoon.
 * 
 * The TrialSession object encapsulates both the day and session and is provided
 * for convenience to the front end.
 * 
 * </p>
 * <H1>This is not thread safe, under the covers it uses unsynchronized lazy
 * instantiation. You have been warned :-) </H1>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * 
 * @version $Revision: 1.7 $
 * 
 */
public interface SkeletonSchedule extends java.lang.Comparable, java.io.Serializable,
        uk.gov.courtservice.xhibit.business.services.witness.interfaces.StateManaged {
    public static final String NOTREADY = "NOTREADY";

    public static final String READY = "READY";

    public static final String DELIVERED = "DELIVERED";

    public static final String FAILED = "FAILED";

    /**
     * 
     * The primary key of this SkeletonSchedule instance.
     * 
     * @post return != null
     * @return
     */
    public Integer getId();

    /**
     * Returns the primary key of the related case. This should never be null.
     * 
     * @post return != null
     * @return the key.
     */
    public Integer getCaseId();

    public boolean isDeliverable();

    public void setDeliverable(boolean deliverable);

    public String getDeliveryStatus();

    /**
     * 
     * @param status
     */
    public void setDeliveryStatus(String status);

    public void issue() throws ScheduleAlreadyIssued, ModificationException;

    /**
     * Returns true if Skeleton Schedule not marked for export.
     * 
     * @post return == ( isDeliverable() && ( getDeliveryStatus() == null ||
     *       getDeliveryStatus().equals(READY) ||
     *       getDeliveryStatus().equals(DELIVERED) ||
     *       getDeliveryStatus().equals(FAILED) ) )
     * 
     * @return
     */
    public boolean isIssued();

    /**
     * Returns a case associated with this skeleton schedule there should always
     * be a case associated with a schedule.
     * 
     * @pre getCaseId() != null
     * @post return != null && return.getId() == this.getCaseId()
     * 
     * @return a CaseDetail
     */
    public CaseDetail getCaseDetail()
            throws uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException;

    /**
     * Returns an array of Trial Sessions.
     * 
     * @post return != null && return.length > 0 implies forall TrialSession ts
     *       in return | ts != null
     * @post return != null
     * 
     * @return an array of TrialSessions (presorted).
     */
    public TrialSession[] getTrialSessions();

    /**
     * This returns all the sessions that could exist between the beginning of
     * the case and the duration specified. The dummy values it includes are not
     * for general usage as they do not have primary keys etc. Just use the get
     * and set methods to get the data you need from them. Genuine values have
     * primary keys (ie getId() != null).
     * 
     * @param estimatedDuration
     * @return all the sessions that are less than or equal to estimatedDuration
     *         rounded up to the nearest half day.
     */
    public TrialSession[] getTrialSessionsIncludingDummyValuesForDuration(float estimatedDuration);

    /**
     * @pre day != null && day.intValue() >= 0
     * @pre session != null
     * @post return != null && return.getId() != null
     * @post return.getMetaState() == return.UPDATED
     * 
     * @param day
     *            the trial day for which to find the session
     * @param session
     *            the session (that is morning or afternoon) we are interested
     *            in.
     * @return a specific TrialSession
     * @throws TrialSessionNotFoundException
     *             if the session cannot be found.
     */
    public TrialSession getTrialSession(Integer dayNumber, String session) throws TrialSessionNotFoundException;

    /**
     * 
     * @param sessionId
     * @return
     * @throws TrialSessionNotFoundException
     */
    public TrialSession getTrialSession(Integer sessionId) throws TrialSessionNotFoundException;

    /**
     * 
     * @pre day.intValue() >= 0 //@post
     *      (java.util.Arrays.binarySearch(return.getSessionTypes(), session) >=
     *      0)
     * @post return != null && return.getId() != null
     * @post return.getMetaState() == return.UPDATED
     * @param day
     * @param session
     * @return a newly created but also persisted TrialSession object.
     * @throws
     *         uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException
     *         if changes to the underlying data store cannot be made.
     */
    public TrialSession createTrialSession(Integer dayNumber, String session)
            throws uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException,
            SkeletonDayHasDateException, CouldNotCreateSessionException;

    /**
     * 
     * @pre day != null && day.intValue() >= 0
     * @pre session != null
     * @pre trialDate != null //@post
     *      (java.util.Arrays.binarySearch(return.getSessionTypes(), session) >=
     *      0)
     * @post (return != null) && (return.getId() != null) &&
     *       (return.getMetaState() == return.UPDATED)
     * 
     * @param day
     * @param trialDate
     * @param session
     * @return a newly created but also persisted TrialSession object.
     * @throws
     *         uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException
     *         if changes to the underlying data store cannot be made.
     */
    public TrialSession createTrialSession(Integer dayNumber, String session, Date trialDate)
            throws uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException,
            SkeletonDayHasDateException, CouldNotCreateSessionException;

}
