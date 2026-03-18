package uk.gov.courtservice.xhibit.business.entities.scheduledhearing;

import java.sql.Timestamp;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.sitting.Sitting;

public interface ScheduledHearing extends CSEntityLocal {
    public Integer getScheduledHearingId();

    public void setSequenceNo(Integer sequenceNo);

    public Integer getSequenceNo();

    public void setNotBeforeTime(Timestamp notBeforeTime);

    public Timestamp getNotBeforeTime();

    public void setOriginalTime(Timestamp originalTime);

    public Timestamp getOriginalTime();

    public void setListingNote(String listingNote);

    public String getListingNote();

    public void setHearingProgress(Integer hearingProgress);

    public Integer getHearingProgress();

    public void setMovedFrom(String movedFrom);

    public String getMovedFrom();

    public void setHearingId(Integer hearingId);

    public Integer getHearingId();

    public void setSittingId(Integer sittingId);

    public void setLinkedSHId(Integer linkedSHId);

    public void setEndTime(java.sql.Timestamp endTime);

    public void setStartTime(java.sql.Timestamp startTime);

    public void setDateOfHearing(java.sql.Timestamp dateOfHearing);
    
    public void setAddHearingUsed(String addHearingUsed);

    public Integer getSittingId();

    public Integer getLinkedSHId();

    public java.sql.Timestamp getEndTime();

    public java.sql.Timestamp getStartTime();

    public java.sql.Timestamp getDateOfHearing();
    
    public String getAddHearingUsed();

    public void setIsCaseActive(java.lang.String isCaseActive);

    public java.lang.String getIsCaseActive();

    public void setMovedFromCourtRoomId(Integer movedFromHearingId);

    public Integer getMovedFromCourtRoomId();

    public void setHearing(Hearing hearing);

    public Hearing getHearing();

    public void setSitting(Sitting sitting);

    public Sitting getSitting();
    
    public void setRefCrackedEffectiveId(Integer refCrackedEffectiveId);

    public Integer getRefCrackedEffectiveId();

    public abstract void setScheduledHearingAttendee(java.util.Collection scheduledHearingAttendee);

    public abstract java.util.Collection getScheduledHearingAttendee();

    public abstract void setSchedHearingDefendant(java.util.Collection schedHearingDefendant);

    public abstract java.util.Collection getSchedHearingDefendant();

    public abstract void setShLegRep(java.util.Collection shLegRep);

    public abstract java.util.Collection getShLegRep();

}