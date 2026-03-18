package uk.gov.courtservice.xhibit.business.entities.shstaff;

import java.util.Collection;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface ShStaff extends CSEntityLocal {
    public Integer getShStaffId();

    public void setStaffRole(String staffRole);

    public String getStaffRole();

    public void setStaffName(String staffName);

    public String getStaffName();

    public void setScheduledHearingAttendee(Collection scheduledHearingAttendee);

    public Collection getScheduledHearingAttendee();
}