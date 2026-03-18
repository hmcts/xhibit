package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface CaseOnList extends CSEntityLocal {

	public Integer getCaseOnListId();
	public Integer getCaseId();
	public Integer getListId();
	public Integer getSittingOnListId();
	public Integer getCourtSiteId();
	public Integer getCourtRoomId();
	public String getReserved();
	public String getFloaterCase();
	public Integer getTimeMarkingId();
	public Date getTimeListed();
	public String getIsCourtRoomListEntry();
	public Integer getHearingTypeId();
	public String getReasonForRemoval();
	public Integer getCrackedIneffectiveId();
	public String getObsInd();
	public Integer getSeqNo();
	public String getCreatedBy();
	public String getLastUpdatedBy();
	public Date getCreationDate();
	public Date getLastUpdateDate();
	public Integer getCaseDiaryFixtureId();
	public Date getDateOfRemoval();
	public Integer getListNotePredefinedId();
	public String getListNoteText();
	public Integer getParentCaseOnListId();
	public String getNhaFirmList();
	public Integer getVacationPreDefinedRsonId();
	
	public void setCaseOnListId(Integer caseOnListId);
	public void setCaseId(Integer caseId);
	public void setListId(Integer listId);
	public void setSittingOnListId(Integer sittingOnListId);
	public void setCourtSiteId(Integer courtSiteId);
	public void setCourtRoomId(Integer courtRoomId);
	public void setReserved(String reserved);
	public void setFloaterCase(String floaterCase);
	public void setTimeMarkingId(Integer timeMarkingId);
	public void setTimeListed(Date timeListed);
	public void setIsCourtRoomListEntry(String isCourtRoomListEntry);
	public void setHearingTypeId(Integer hearingTypeId);
	public void setReasonForRemoval(String reasonForRemoval);
	public void setCrackedIneffectiveId(Integer crackedIneffectiveId);
	public void setObsInd(String obsInd);	
	public void setSeqNo(Integer seqNo);
	public void setCreatedBy(String createdBy);
	public void setLastUpdatedBy(String lastUpdatedBy);
	public void setCreationDate(Date creationDate);
	public void setLastUpdateDate(Date lastUpdateDate);	
	public void setCaseDiaryFixtureId(Integer caseDiaryFixtureId);
	public void setDateOfRemoval(Date dateOfRemoval);
	public void setListNotePredefinedId(Integer listNotePredefinedId);
	public void setListNoteText(String listNoteText);
	public void setParentCaseOnListId(Integer parentCaseOnListId);
	public void setNhaFirmList(String nhaFirmList);	
	public void setVacationPreDefinedRsonId(Integer vacationPreDefinedRsonId);
}