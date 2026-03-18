package uk.gov.courtservice.xhibit.business.entities.schedhearingattendee;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.shjustice.ShJustice;
import uk.gov.courtservice.xhibit.business.entities.shstaff.ShStaff;

public interface SchedHearingAttendee extends CSEntityLocal {
    public Integer getShAttendeeId();

    public void setAttendeeType(String attendeeType);

    public String getAttendeeType();

    public void setScheduledHearingId(Integer scheduledHearingId);

    public Integer getScheduledHearingId();

    public void setShStaffId(Integer shStaffId);

    public Integer getShStaffId();

    public void setShJusticeId(Integer shJusticeId);

    public Integer getShJusticeId();

    public void setRefJudgeId(Integer refJudgeId);

    public Integer getRefJudgeId();

    public void setRefCourtRepId(Integer refCourtRepId);

    public Integer getRefCourtRepId();

    public void setRefJusticeId(Integer refJusticeId);

    public Integer getRefJusticeId();

    public void setScheduledHearing(ScheduledHearing scheduledHearing);

    public ScheduledHearing getScheduledHearing();

    public void setShStaff(ShStaff shStaff);

    public ShStaff getShStaff();

    public void setShJustice(ShJustice shJustice);

    public ShJustice getShJustice();

}