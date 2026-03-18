package uk.gov.courtservice.xhibit.business.entities.hearing;

import java.sql.Timestamp;
import java.util.Collection;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface Hearing extends CSEntityLocal {
    public Integer getHearingId();

    public void setCaseId(Integer caseId);

    public Integer getCaseId();

    public void setRefHearingTypeId(Integer refHearingTypeId);

    public Integer getRefHearingTypeId();

    public abstract void setCourtId(Integer courtId);

    public abstract Integer getCourtId();

    public abstract void setMpHearingType(String mpHearingType);

    public abstract String getMpHearingType();

    public abstract void setHearingStartDate(Timestamp hearingStartDate);

    public abstract void setHearingEndDate(Timestamp hearingEndDate);

    public abstract void setLinkedHearingId(Integer linkedHearingId);

    public abstract Timestamp getHearingStartDate();

    public abstract Timestamp getHearingEndDate();

    public abstract Integer getLinkedHearingId();

    public void setScheduledHearings(Collection scheduledHearings);

    public Collection getScheduledHearings();

    public abstract void setDefHearingRecord(Collection defHearingRecord);

    public abstract Collection getDefHearingRecord();
}