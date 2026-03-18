package uk.gov.courtservice.xhibit.business.entities.refhearingtype;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface RefHearingType extends CSEntityLocal {

    public Integer getRefHearingTypeId(); // Primary Key field (immutable)

    // public void setRefHearingTypeId(Integer newValue); // re-introduced
    // temporarily - removing it yesterday caused errors.

    public Integer getCourtId();

    public String getCategory();

    public String getHearingTypeCode();

    public String getHearingTypeDesc();

    public Integer getListSequence();

    public String getObsInd();

    public Integer getSeqNo();

    public void setCategory(String newValue);

    public void setCourtId(Integer newValue);

    public void setHearingTypeCode(String newValue);

    public void setHearingTypeDesc(String newValue);

    public void setListSequence(Integer newValue);

    public void setObsInd(String newValue);

    public void setSeqNo(Integer newValue);
}