package uk.gov.courtservice.xhibit.business.entities.shjustice;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class ShJusticeBean extends CSEntityBean implements EntityBean {

    public Integer ejbCreate(java.lang.String justiceName, Integer hearingId, String userDisplayName) throws CreateException {
        setJusticeName(justiceName);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        setHearingId(hearingId);
        return null;
    }

    public void ejbPostCreate(java.lang.String justiceName, Integer hearingId, String userDisplayName) throws CreateException {
    }

    public abstract void setShJusticeId(Integer shJusticeId);

    public abstract void setJusticeName(java.lang.String justiceName);

    public abstract void setHearingId(Integer hearingId);

    public abstract void setLastUpdatedBy(java.lang.String lastUpdatedBy);

    public abstract void setCreatedBy(java.lang.String createdBy);

    // -------------------------------------------------------------------------------------------

    public abstract Integer getShJusticeId();

    public abstract java.lang.String getJusticeName();

    public abstract Integer getHearingId();

    public abstract java.lang.String getLastUpdatedBy();

    public abstract java.lang.String getCreatedBy();

    // --------------------CMR--------------------------------------------------------------------

    public abstract void setScheduledHearingAttendee(
            uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendee scheduledHearingAttendee);

    public abstract uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendee getScheduledHearingAttendee();

}