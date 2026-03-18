package uk.gov.courtservice.xhibit.business.entities.hatesentencing;

import java.util.Collection;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.vos.entities.IndictmentLogValue;

public interface HateSentencing extends CSEntityLocal {

    // -------------------------- getter methods ------------------
    public Integer getHateSentencingId();
    
    public Integer getDefendantOnCaseId();
    
    public Integer getRefHateSentTypeId();
    
    public String getObsInd();
    
    public DefendantOnCase getDefendantOnCase();

    // public Integer getVersion();
    // public String getLastUpdatedBy();
    // public String getCreatedBy();
    // public Date getCreationDate();
    // public Date getLastUpdateDate();

    // ------------------------- setter methods -----------------
    public void setRefHateSentTypeId(Integer refHateSentTypeId);

    public void setObsInd(String obsInd);

    public void setDefendantOnCaseId(Integer defendantOnCaseId);
    
    public void setHateSentencingId(Integer hateSentencingId);
    
    public void setDefendantOnCase(DefendantOnCase defendantOnCase);
    
    
    // public void setVersion(Integer version);
    // public void setLastUpdatedBy(String lastUpdatedBy);
    // public void setCreatedBy(String createdBy);
    // public void setCreationDate(Date creationDate);
    // public void setLastUpdateDate(Date lastUpdateDate);

}