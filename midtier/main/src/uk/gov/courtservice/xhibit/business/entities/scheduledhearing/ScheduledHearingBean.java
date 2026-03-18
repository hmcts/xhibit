package uk.gov.courtservice.xhibit.business.entities.scheduledhearing;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.sitting.Sitting;

abstract public class ScheduledHearingBean extends CSEntityBean {
    public Integer ejbCreate(Integer sequenceNo, java.sql.Timestamp notBeforeTime, java.sql.Timestamp originalTime,
            String listingNote, Integer hearingProgress, java.lang.String movedFrom, CSEntityLocal hearing,
            CSEntityLocal sitting, Integer linkedSHId, java.sql.Timestamp endTime, java.sql.Timestamp startTime,
            java.sql.Timestamp dateOfHearing, java.lang.String isCaseActive, java.lang.Integer movedFromCourtRoomId,
            java.lang.String addHearingUsed, java.lang.Integer refCrackedEffectiveId, String userDisplayName)
            throws CreateException {
        setSequenceNo(sequenceNo);
        setNotBeforeTime(notBeforeTime);
        setOriginalTime(originalTime);
        setListingNote(listingNote);
        setHearingProgress(hearingProgress);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        setMovedFrom(movedFrom);
        setLinkedSHId(linkedSHId);
        setEndTime(endTime);
        setStartTime(startTime);
        setDateOfHearing(dateOfHearing);
        setIsCaseActive(isCaseActive);
        setMovedFromCourtRoomId(movedFromCourtRoomId);
        setAddHearingUsed(addHearingUsed);
        setRefCrackedEffectiveId(refCrackedEffectiveId);
        // NOTE: must set the mandatory CMR fields in ejbPostCreate (database
        // insert is delayed until then)
        return null;
    }

    public void ejbPostCreate(Integer sequenceNo, java.sql.Timestamp notBeforeTime, java.sql.Timestamp originalTime,
            String listingNote, Integer hearingProgress, java.lang.String movedFrom, CSEntityLocal hearing,
            CSEntityLocal sitting, Integer linkedSHId, java.sql.Timestamp endTime, java.sql.Timestamp startTime,
            java.sql.Timestamp dateOfHearing, java.lang.String isCaseActive, java.lang.Integer movedFromCourtRoomId,
            java.lang.String addHearingUsed, java.lang.Integer refCrackedEffectiveId, String userDisplayName)
            throws CreateException {
        setHearing((Hearing) hearing);
        setSitting((Sitting) sitting);
    }

    // ------------------------------CMP
    // Fields------------------------------------
    public abstract void setScheduledHearingId(Integer scheduledHearingId);

    public abstract void setSequenceNo(Integer sequenceNo);

    public abstract void setNotBeforeTime(java.sql.Timestamp notBeforeTime);

    public abstract void setOriginalTime(java.sql.Timestamp originalTime);

    public abstract void setListingNote(String listingNote);

    public abstract void setHearingProgress(Integer hearingProgress);

    public abstract void setMovedFrom(java.lang.String movedFrom);

    public abstract void setHearingId(Integer hearingId);

    public abstract void setSittingId(Integer sittingId);

    public abstract void setLinkedSHId(Integer linkedSHId);

    public abstract void setEndTime(java.sql.Timestamp endTime);

    public abstract void setStartTime(java.sql.Timestamp startTime);

    public abstract void setDateOfHearing(java.sql.Timestamp dateOfHearing);
    
    public abstract void setAddHearingUsed(String addHearingUsed);
    
    public abstract void setRefCrackedEffectiveId(Integer refCrackedEffectiveId);

    public abstract Integer getScheduledHearingId();

    public abstract Integer getSequenceNo();

    public abstract java.sql.Timestamp getNotBeforeTime();

    public abstract java.sql.Timestamp getOriginalTime();

    public abstract String getListingNote();

    public abstract Integer getHearingProgress();

    public abstract java.lang.String getMovedFrom();

    public abstract Integer getHearingId();

    public abstract Integer getSittingId();

    public abstract Integer getLinkedSHId();

    public abstract java.sql.Timestamp getEndTime();

    public abstract java.sql.Timestamp getStartTime();

    public abstract java.sql.Timestamp getDateOfHearing();
    
    public abstract String getAddHearingUsed();
    
    public abstract Integer getRefCrackedEffectiveId();

    public abstract void setIsCaseActive(java.lang.String isCaseActive);

    public abstract java.lang.String getIsCaseActive();

    // ------------------------------CMR
    // Fields------------------------------------
    public abstract void setHearing(uk.gov.courtservice.xhibit.business.entities.hearing.Hearing hearing);

    public abstract void setSitting(uk.gov.courtservice.xhibit.business.entities.sitting.Sitting sitting);

    public abstract void setScheduledHearingAttendee(java.util.Collection scheduledHearingAttendee);

    public abstract void setSchedHearingDefendant(java.util.Collection schedHearingDefendant);

    public abstract void setShLegRep(java.util.Collection shLegRep);

    public abstract void setMovedFromCourtRoomId(java.lang.Integer movedFromCourtRoomId);

    public abstract uk.gov.courtservice.xhibit.business.entities.hearing.Hearing getHearing();

    public abstract uk.gov.courtservice.xhibit.business.entities.sitting.Sitting getSitting();

    public abstract java.util.Collection getScheduledHearingAttendee();

    public abstract java.util.Collection getSchedHearingDefendant();

    public abstract java.util.Collection getShLegRep();

    public abstract java.lang.Integer getMovedFromCourtRoomId();
}