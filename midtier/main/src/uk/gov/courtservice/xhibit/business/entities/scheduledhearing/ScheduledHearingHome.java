package uk.gov.courtservice.xhibit.business.entities.scheduledhearing;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface ScheduledHearingHome extends javax.ejb.EJBLocalHome {
    public ScheduledHearing create(Integer sequenceNo, Timestamp notBeforeTime, Timestamp originalTime,
            String listingNote, Integer hearingProgress, String movedFrom, CSEntityLocal hearing,
            CSEntityLocal sitting, Integer linkedSHId, Timestamp endTime, Timestamp startTime, Timestamp dateOfHearing,
            String isCaseActive, java.lang.Integer movedFromCourtRoomId,
            String addHearingUsed, java.lang.Integer refCrackedEffectiveId, String userDisplayName) throws CreateException;

    public ScheduledHearing findByPrimaryKey(Integer scheduledHearingId) throws FinderException;

    /**
     * Find all scheduled hearings for the specified sitting id.
     * 
     * @param sittingId
     * @return Collection of scheduled hearings for the specified sitting id.
     * @throws FinderException
     */
    public Collection findBySittingId(Integer sittingId) throws FinderException;

    /**
     * Find all scheduled hearings for a sitting id, which started before or at
     * the notBeforeTime specified.
     * 
     * @param notBeforeTime
     * @param sittingId
     * @return Collection of scheduled hearings for a sitting id, which started
     *         before or at the notBeforeTime specified.
     * @throws FinderException
     */
    public Collection findByTimeSittingId(Timestamp notBeforeTime, Integer sittingId) throws FinderException;

    /**
     * Find all scheduled hearings for the specified hearing id.
     * 
     * @param hearingId
     * @return Collection of scheduled hearings for the specified hearing id.
     * @throws FinderException
     */
    public Collection findByHearingId(Integer hearingId) throws FinderException;

    /**
     * Find all scheduled hearings for the specified linked scheduled hearing
     * id.
     * 
     * @param linkedSchedHearingId
     * @return Collection of scheduled hearings for the specified linked
     *         scheduled hearing id.
     * @throws FinderException
     */
    public Collection<ScheduledHearing> findByLinkedSchedHearingId(Integer hearingId) throws FinderException;

    /**
     * Find all the scheduled hearings created with 'Add Hearing'
     * @param fromDate
     * @param toDate
     * @param courtId
     * @return Collection of scheduled hearings where 'Add Hearing' was used to create them
     */
    public Collection<ScheduledHearing> findWhereAddHearingUsed(Date fromDate, Date toDate, Integer courtId) throws FinderException;
    
    /**
     * Find all the scheduled hearings created without 'Add Hearing'
     * @param fromDate
     * @param toDate
     * @param courtId
     * @return Collection of scheduled hearings where 'Add Hearing' was used to create them
     */
    public Collection<ScheduledHearing> findWhereAddHearingNotUsed(Date fromDate, Date toDate, Integer courtId) throws FinderException;
}