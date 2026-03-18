package uk.gov.courtservice.xhibit.business.entities.courtroomusage;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface CourtRoomUsage extends CSEntityLocal {
	
    public java.lang.Integer getCourtRoomUsageId();
    public void setCourtRoomUsageId(java.lang.Integer Id);
    
    public java.lang.Integer getCourtRoomId();
    public void setCourtRoomId(java.lang.Integer Id);
    
    public java.lang.Integer getAmTimeHours();
    public void setAmTimeHours(java.lang.Integer hours);

    public java.lang.Integer getAmTimeMins();
    public void setAmTimeMins(java.lang.Integer mins);

    public java.lang.Integer getPmTimeHours();
    public void setPmTimeHours(java.lang.Integer hours);

    public java.lang.Integer getPmTimeMins();
    public void setPmTimeMins(java.lang.Integer mins);
    
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