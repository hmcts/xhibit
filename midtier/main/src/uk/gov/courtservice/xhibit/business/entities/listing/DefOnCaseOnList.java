package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface DefOnCaseOnList extends CSEntityLocal {

	public Integer getDefOnCaseOnListId();
	public Integer getDefendantOnCaseId();
	public Integer getCaseOnListId();
	public Integer getCaseId();
	public String getObsInd();
	public String getCreatedBy();
	public String getLastUpdatedBy();
	public Date getCreationDate();
	public Date getLastUpdateDate();
	public String getIsCourtRoomListEntry();
	
	public void setDefOnCaseOnListId(Integer defOnCaseOnListId);
	public void setDefendantOnCaseId(Integer defendantOnCaseId);
	public void setCaseOnListId(Integer caseOnListId);
	public void setCaseId(Integer caseId);
	public void setObsInd(String obsInd);	
	public void setCreatedBy(String createdBy);
	public void setLastUpdatedBy(String lastUpdatedBy);
	public void setCreationDate(Date creationDate);
	public void setLastUpdateDate(Date lastUpdateDate);	
	public void setIsCourtRoomListEntry(String isCourtRoomListEntry);
}