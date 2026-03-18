package uk.gov.courtservice.xhibit.business.entities.schedhearingattendee;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;

public interface SchedHearingAttendeeHome extends javax.ejb.EJBLocalHome {
    public SchedHearingAttendee create(String attendeeType, Integer refJudgeId, Integer refJusticeId,
            Integer refCourtRepId, ScheduledHearing scheduledHearing, String userDisplayName) throws CreateException;

    public SchedHearingAttendee findByPrimaryKey(Integer shAttendeeId) throws FinderException;

    public Collection findByScheduledHearingId(Integer scheduledHearingId) throws FinderException;

    public Collection findByRefJudgeIdAndSHAttendeeId(Integer refJudgeId, Integer shAttendeeId) throws FinderException;

    public Collection findByRefCourtReporterId(Integer refCourtRepId) throws FinderException;

    public Collection findBySHStaffId(Integer shStaffId) throws FinderException;
}