package uk.gov.courtservice.xhibit.business.entities.schedhearingattendee;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;

abstract public class SchedHearingAttendeeBean extends CSEntityBean {

    public Integer ejbCreate(String attendeeType, Integer refJudgeId, Integer refJusticeId, Integer refCourtRepId,
            ScheduledHearing scheduledHearing, String userDisplayName) throws CreateException {

        setAttendeeType(attendeeType);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        setRefJudgeId(refJudgeId);
        setRefCourtRepId(refCourtRepId);
        setRefJusticeId(refJusticeId);
        return null;
    }

    public void ejbPostCreate(String attendeeType, Integer refJudgeId, Integer refJusticeId, Integer refCourtRepId,
            ScheduledHearing scheduledHearing, String userDisplayName) throws CreateException {
        setScheduledHearing(scheduledHearing);
    }

    public abstract void setShAttendeeId(Integer shAttendeeId);

    public abstract void setAttendeeType(java.lang.String attendeeType);

    public abstract void setScheduledHearingId(Integer scheduledHearingId);

    public abstract void setLastUpdatedBy(java.lang.String lastUpdatedBy);

    public abstract void setCreatedBy(java.lang.String createdBy);

    public abstract void setShStaffId(Integer shStaffId);

    public abstract void setShJusticeId(Integer shJusticeId);

    public abstract void setRefJudgeId(Integer refJudgeId);

    public abstract void setRefCourtRepId(Integer refCourtRepId);

    public abstract void setRefJusticeId(Integer refJusticeId);

    // --------------------------------------------------------------------------------------------
    public abstract Integer getShAttendeeId();

    public abstract java.lang.String getAttendeeType();

    public abstract Integer getScheduledHearingId();

    public abstract java.lang.String getLastUpdatedBy();

    public abstract java.lang.String getCreatedBy();

    public abstract Integer getShStaffId();

    public abstract Integer getShJusticeId();

    public abstract Integer getRefJudgeId();

    public abstract Integer getRefCourtRepId();

    public abstract Integer getRefJusticeId();

    // -----------------------CMR---------------------------------------------------------------------

    public abstract void setScheduledHearing(
            uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing scheduledHearing);

    public abstract void setShStaff(uk.gov.courtservice.xhibit.business.entities.shstaff.ShStaff shStaff);

    public abstract void setShJustice(uk.gov.courtservice.xhibit.business.entities.shjustice.ShJustice shJustice);

    public abstract uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing getScheduledHearing();

    public abstract uk.gov.courtservice.xhibit.business.entities.shstaff.ShStaff getShStaff();

    public abstract uk.gov.courtservice.xhibit.business.entities.shjustice.ShJustice getShJustice();

}