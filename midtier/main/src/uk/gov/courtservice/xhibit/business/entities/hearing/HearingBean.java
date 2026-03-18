package uk.gov.courtservice.xhibit.business.entities.hearing;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

public abstract class HearingBean extends CSEntityBean {

    public Integer ejbCreate(Integer caseId, Integer refHearingTypeId, Integer courtId, String mpHearingType,
            Timestamp hearingStartDate, Timestamp hearingEndDate, Integer linkedHearingId, String userDisplayName) throws CreateException {
        setCaseId(caseId);
        setRefHearingTypeId(refHearingTypeId);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        setCourtId(courtId);
        setMpHearingType(mpHearingType);
        setHearingStartDate(hearingStartDate);
        setHearingEndDate(hearingEndDate);
        setLinkedHearingId(linkedHearingId);
        return null;
    }

    public void ejbPostCreate(Integer caseId, Integer refHearingTypeId, Integer courtId, String mpHearingType,
            Timestamp hearingStartDate, Timestamp hearingEndDate, Integer linkedHearingId, String userDisplayName) throws CreateException {
    }

    // ------------------------------CMP
    // Fields---------------------------------
    public abstract void setHearingId(Integer hearingId);

    public abstract void setCaseId(Integer caseId);

    public abstract void setRefHearingTypeId(Integer refHearingTypeId);

    public abstract Integer getHearingId();

    public abstract Integer getCaseId();

    public abstract Integer getRefHearingTypeId();

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

    // ------------------------------CMR
    // Fields---------------------------------
    public abstract void setScheduledHearings(Collection scheduledHearings);

    public abstract void setDefHearingRecord(Collection defHearingRecord);

    public abstract Collection getScheduledHearings();

    public abstract Collection getDefHearingRecord();

}