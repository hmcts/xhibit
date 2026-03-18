package uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant;

import java.util.Collection;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;

abstract public class SchedHearingDefendantBean extends CSEntityBean {

    public Integer ejbCreate(Integer scheduledHearingId, Integer defOnCaseID, CSEntityLocal scheduledHearing, String userDisplayName)
            throws CreateException {
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        setDefOnCaseID(defOnCaseID);
        // Note don't set scheduledHearing or scheduledHearingId as they are
        // CMRs
        return null;
    }

    public void ejbPostCreate(Integer scheduledHearingId, Integer defOnCaseID, CSEntityLocal scheduledHearing, String userDisplayName)
            throws CreateException {
        // Note: this is a CMR setting and will automatically set
        // scheduledHearingId (the foreign key) as well
        setScheduledHearing((ScheduledHearing) scheduledHearing);
    }

    // ------------------------------CMP
    // Fields----------------------------------
    public abstract void setSchedHearDefId(Integer schedHearDefId);

    public abstract void setScheduledHearingId(Integer scheduledHearingId);

    public abstract void setDefOnCaseID(Integer defOnCaseID);

    public abstract Integer getSchedHearDefId();

    public abstract Integer getScheduledHearingId();

    public abstract Integer getDefOnCaseID();

    // ------------------------------CMR
    // Fields----------------------------------
    public abstract void setScheduledHearing(ScheduledHearing scheduledHearing);

    public abstract void setShLegRep(Collection shLegRep);

    public abstract ScheduledHearing getScheduledHearing();

    public abstract Collection getShLegRep();
}