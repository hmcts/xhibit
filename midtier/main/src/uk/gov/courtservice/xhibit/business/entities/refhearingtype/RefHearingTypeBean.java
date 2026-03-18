package uk.gov.courtservice.xhibit.business.entities.refhearingtype;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class RefHearingTypeBean extends CSEntityBean {

    public Integer ejbCreate(Integer courtId, String category, String hearingTypeCode, String hearingTypeDesc,
            Integer listSequence, String obsInd, Integer seqNo, String userDisplayName) throws CreateException {

        setCategory(category);
        setCourtId(courtId);
        setCreatedBy(userDisplayName);
        setHearingTypeCode(hearingTypeCode);
        setHearingTypeDesc(hearingTypeDesc);
        setLastUpdatedBy(userDisplayName);
        setListSequence(listSequence);
        setObsInd(obsInd);
        setSeqNo(seqNo);
        return null;
    }

    public void ejbPostCreate(Integer courtId, String category, String hearingTypeCode, String hearingTypeDesc,
            Integer listSequence, String obsInd, Integer seqNo, String userDisplayName) throws CreateException {
    }

    // ------------------------------CMP
    // Fields------------------------------------
    public abstract Integer getRefHearingTypeId(); // Primary Key field

    // (immutable)
    // public abstract void setRefHearingTypeId(Integer refHearingTypeId);
    // // re-introduced temporarily - removing it yesterday caused errors.

    public abstract Integer getCourtId();

    public abstract String getCategory();

    public abstract String getHearingTypeCode();

    public abstract String getHearingTypeDesc();

    public abstract Integer getListSequence();

    public abstract String getObsInd();

    public abstract Integer getSeqNo();

    public abstract void setCategory(String category);

    public abstract void setCourtId(Integer courtId);

    public abstract void setHearingTypeCode(String hearingTypeCode);

    public abstract void setHearingTypeDesc(String hearingTypeDesc);

    public abstract void setListSequence(Integer listSequence);

    public abstract void setObsInd(String obsInd);

    public abstract void setSeqNo(Integer seqNo);

    public abstract void setRefHearingTypeId(Integer refHearingTypeId);
}