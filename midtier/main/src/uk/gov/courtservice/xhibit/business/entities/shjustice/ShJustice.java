package uk.gov.courtservice.xhibit.business.entities.shjustice;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendee;

public interface ShJustice extends CSEntityLocal {
    public Integer getShJusticeId();

    public void setJusticeName(String justiceName);

    public String getJusticeName();

    public void setScheduledHearingAttendee(SchedHearingAttendee scheduledHearingAttendee);

    public SchedHearingAttendee getScheduledHearingAttendee();

    public void setHearingId(Integer hearingId);

    public Integer getHearingId();

}