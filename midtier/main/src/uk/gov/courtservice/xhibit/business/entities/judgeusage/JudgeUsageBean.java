package uk.gov.courtservice.xhibit.business.entities.judgeusage;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

public abstract class JudgeUsageBean extends uk.gov.courtservice.framework.business.entities.CSEntityBean implements
        EntityBean {

	
	public Integer ejbCreate(Integer courtRoomId, String courtChambersInd,
			Integer judgeId, Date sittingDate, String userDisplayName) throws CreateException {
		setCourtRoomId(courtRoomId);
		setCourtChambersInd(courtChambersInd);
		setRefJudgeId(judgeId);
		setSittingDate(sittingDate);
		setLastUpdatedBy(userDisplayName);
		setCreatedBy(userDisplayName);
		return null;
	}

	public void ejbPostCreate(Integer courtRoomId, String courtChambersInd, Integer judgeId,
			Date sittingDate, String userDisplayName) throws CreateException {
		/** @todo Complete this method */
	}

  
    public abstract java.lang.Integer getJudgeUsageId();
    public abstract void setJudgeUsageId(java.lang.Integer Id);
    
    public abstract java.lang.Integer getCourtRoomId();
    public abstract void setCourtRoomId(java.lang.Integer Id);
    
    public abstract java.lang.String getCourtChambersInd();
    public abstract void setCourtChambersInd(java.lang.String ind);
    
    public abstract java.lang.Integer getRefJudgeId();
    public abstract void setRefJudgeId(java.lang.Integer Id);
    
    public abstract java.util.Date getSittingDate();
    public abstract void setSittingDate(java.util.Date sittingDate);
    
    public abstract java.lang.String getCreatedBy();
    public abstract void setCreatedBy(java.lang.String user);
    
    public abstract java.util.Date getCreationDate();
    public abstract void setCreationDate(java.util.Date creationDate);
    
    public abstract java.lang.String getLastUpdatedBy();
    public abstract void setLastUpdatedBy(java.lang.String user);
    
    public abstract java.util.Date getLastUpdateDate();
    public abstract void setLastUpdateDate(java.util.Date updatedDate);
    
    public abstract java.lang.Integer getVersion();
    public abstract void setVersion(java.lang.Integer version);
    
    public abstract java.lang.String getObsInd(  ) ;
	public abstract void setObsInd(java.lang.String obsInd);
}