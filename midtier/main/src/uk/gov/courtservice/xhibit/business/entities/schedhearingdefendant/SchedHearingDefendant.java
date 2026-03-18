package uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant;

import java.util.Collection;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;

public interface SchedHearingDefendant extends CSEntityLocal {
    public Integer getSchedHearDefId();

    public Integer getScheduledHearingId();

    public abstract Integer getDefOnCaseID();

    public abstract void setDefOnCaseID(Integer defOnCaseID);

    public void setScheduledHearingId(Integer scheduledHearingId);

    public abstract void setScheduledHearing(ScheduledHearing schedHearing);

    public abstract void setShLegRep(Collection shLegRep);

    public abstract ScheduledHearing getScheduledHearing();

    public abstract Collection getShLegRep();
}