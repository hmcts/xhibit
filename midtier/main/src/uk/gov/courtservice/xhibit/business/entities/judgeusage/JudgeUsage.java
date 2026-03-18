package uk.gov.courtservice.xhibit.business.entities.judgeusage;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface JudgeUsage extends CSEntityLocal {
	
    public java.lang.Integer getJudgeUsageId();
    public void setJudgeUsageId(java.lang.Integer Id);
    
    public java.lang.Integer getCourtRoomId();
    public void setCourtRoomId(java.lang.Integer Id);
    
    public java.lang.String getCourtChambersInd();
    public void setCourtChambersInd(java.lang.String ind);
    
    public java.lang.Integer getRefJudgeId();
    public void setRefJudgeId(java.lang.Integer Id);
    
    public java.util.Date getSittingDate();
    public void setSittingDate(java.util.Date sittingDate);
    
    public java.lang.String getCreatedBy();
    public void setCreatedBy(java.lang.String user);
    
    public java.util.Date getCreationDate();
    public void setCreationDate(java.util.Date creationDate);
    
    public java.lang.String getLastUpdatedBy();
    public void setLastUpdatedBy(java.lang.String user);
    
    public java.util.Date getLastUpdateDate();
    public void setLastUpdateDate(java.util.Date updatedDate);
    
    
    public java.lang.Integer getVersion();
    public void setVersion(java.lang.Integer version);
    
    public java.lang.String getObsInd(  ) ;
	public void setObsInd(java.lang.String obsInd);
	
}