package uk.gov.courtservice.xhibit.business.entities.indictmentlog;

import java.util.Collection;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.vos.entities.IndictmentLogValue;

public interface IndictmentLog extends CSEntityLocal {

    // -------------------------- getter methods ------------------
    public Integer getIndictmentLogId();
    
    public Integer getCaseId();
    
    public Integer getSequenceNo();

    public String getIndictmentInfo();
    
    public String getObsInd();

    public Collection<IndictmentLogValue> getIndictmentLogs();
    // public Integer getVersion();
    // public String getLastUpdatedBy();
    // public String getCreatedBy();
    // public Date getCreationDate();
    // public Date getLastUpdateDate();

    // ------------------------- setter methods -----------------
    public void setIndictmentInfo(String indictmentInfo);

    public void setObsInd(String obsInd);

    public void setSequenceNo(Integer sequenceNo);

    public void setCaseId(Integer caseId);
    
    public void setIndictmentLogId(Integer indictmentLogId);
    
    
    // public void setVersion(Integer version);
    // public void setLastUpdatedBy(String lastUpdatedBy);
    // public void setCreatedBy(String createdBy);
    // public void setCreationDate(Date creationDate);
    // public void setLastUpdateDate(Date lastUpdateDate);

}