package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class DefOnCaseOnListBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer defendantOnCaseId, Integer caseOnListId, Integer caseId, 
			String obsInd, String userDisplayName, String isCourtRoomListEntry) throws CreateException {
		setDefendantOnCaseId(defendantOnCaseId);
		setCaseOnListId(caseOnListId);
		setCaseId(caseId);
		setObsInd(obsInd);  
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		setIsCourtRoomListEntry(isCourtRoomListEntry);
		return null;
	}

	public void ejbPostCreate(Integer defendantOnCaseId, Integer caseOnListId, Integer caseId, 
			String obsInd, String userDisplayName, String isCourtRoomListEntry) throws CreateException {
	}

	// ------------------------------CMP
	// Fields------------------------------------    
	public abstract Integer getDefOnCaseOnListId();
	public abstract Integer getDefendantOnCaseId();
	public abstract Integer getCaseOnListId();
	public abstract Integer getCaseId();
	public abstract String getObsInd();
	public abstract String getCreatedBy();
	public abstract String getLastUpdatedBy();
	public abstract Date getCreationDate();
	public abstract Date getLastUpdateDate();
	public abstract String getIsCourtRoomListEntry();

	public abstract void setDefOnCaseOnListId(Integer defOnCaseOnListId);
	public abstract void setDefendantOnCaseId(Integer defendantOnCaseId);	
	public abstract void setCaseOnListId(Integer caseOnListId);
	public abstract void setCaseId(Integer caseId);
	public abstract void setObsInd(String obsInd);
	public abstract void setCreatedBy(String createdBy);
	public abstract void setLastUpdatedBy(String lastUpdatedBy);
	public abstract void setCreationDate(Date creationDate);
	public abstract void setLastUpdateDate(Date lastUpdateDate);	
	public abstract void setIsCourtRoomListEntry(String isCourtRoomListEntry);
}