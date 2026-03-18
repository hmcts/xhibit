package uk.gov.courtservice.xhibit.business.entities.listing;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class CaseOnListBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer caseOnListId, Integer caseId, Integer listId, Integer sittingOnListId, Integer courtSiteId,
			Integer courtRoomId, String reserved, String floaterCase, Integer timeMarkingId, Date timeListed,
			String isCourtRoomListEntry, Integer hearingTypeId, String reasonForRemoval, Integer crackedIneffectiveId,
			String obsInd, String userDisplayName, Integer seqNo, Integer caseDiaryFixtureId, Date dateOfRemoval,
			Integer listNotePredefinedId, String listNoteText, Integer parentCaseOnListId, String nhaFirmList,
			Integer vacationPreDefinedRsonId) throws CreateException {
		setCaseOnListId(caseOnListId);
		setCaseId(caseId);
		setListId(listId);
		setSittingOnListId(sittingOnListId);
		setCourtSiteId(courtSiteId);
		setCourtRoomId(courtRoomId);
		setReserved(reserved);
		setFloaterCase(floaterCase);
		setTimeMarkingId(timeMarkingId);
		setTimeListed(timeListed);
		setIsCourtRoomListEntry(isCourtRoomListEntry);
		setHearingTypeId(hearingTypeId);
		setReasonForRemoval(reasonForRemoval);
		setCrackedIneffectiveId(crackedIneffectiveId);
		setObsInd(obsInd);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        setSeqNo(seqNo);
		setCaseDiaryFixtureId(caseDiaryFixtureId);
		setDateOfRemoval(dateOfRemoval);
		setListNotePredefinedId(listNotePredefinedId);
		setListNoteText(listNoteText);
		setParentCaseOnListId(parentCaseOnListId);
		setNhaFirmList(nhaFirmList);
		setVacationPreDefinedRsonId(vacationPreDefinedRsonId);
        return null;
    }

    public void ejbPostCreate(Integer caseOnListId, Integer caseId, Integer listId, Integer sittingOnListId, Integer courtSiteId,
			Integer courtRoomId, String reserved, String floaterCase, Integer timeMarkingId, Date timeListed,
			String isCourtRoomListEntry, Integer hearingTypeId, String reasonForRemoval, Integer crackedIneffectiveId,
			String obsInd, String userDisplayName, Integer seqNo, Integer caseDiaryFixtureId, Date dateOfRemoval,
			Integer listNotePredefinedId, String listNoteText, Integer parentCaseOnListId, String nhaFirmList,
			Integer vacationPreDefinedRsonId) throws CreateException {
    }

    // ------------------------------CMP
    // Fields------------------------------------    
	public abstract Integer getCaseOnListId();
	public abstract Integer getCaseId();
	public abstract Integer getListId();
	public abstract Integer getSittingOnListId();
	public abstract Integer getCourtSiteId();
	public abstract Integer getCourtRoomId();
	public abstract String getReserved();
	public abstract String getFloaterCase();
	public abstract Integer getTimeMarkingId();
	public abstract Date getTimeListed();
	public abstract String getIsCourtRoomListEntry();
	public abstract Integer getHearingTypeId();
	public abstract String getReasonForRemoval();
	public abstract Integer getCrackedIneffectiveId();
	public abstract String getObsInd();
	public abstract Integer getSeqNo();
	public abstract String getCreatedBy();
	public abstract String getLastUpdatedBy();
	public abstract Date getCreationDate();
	public abstract Date getLastUpdateDate();
	public abstract Integer getCaseDiaryFixtureId();
	public abstract Date getDateOfRemoval();
	public abstract Integer getListNotePredefinedId();
	public abstract String getListNoteText();
	public abstract Integer getParentCaseOnListId();
	public abstract String getNhaFirmList();
	public abstract Integer getVacationPreDefinedRsonId();
	
	public abstract void setCaseOnListId(Integer caseOnListId);
	public abstract void setCaseId(Integer caseId);
	public abstract void setListId(Integer listId);
	public abstract void setSittingOnListId(Integer sittingOnListId);
	public abstract void setCourtSiteId(Integer courtSiteId);
	public abstract void setCourtRoomId(Integer courtRoomId);
	public abstract void setReserved(String reserved);
	public abstract void setFloaterCase(String floaterCase);
	public abstract void setTimeMarkingId(Integer timeMarkingId);
	public abstract void setTimeListed(Date timeListed);
	public abstract void setIsCourtRoomListEntry(String isCourtRoomListEntry);
	public abstract void setHearingTypeId(Integer hearingTypeId);
	public abstract void setReasonForRemoval(String reasonForRemoval);
	public abstract void setCrackedIneffectiveId(Integer crackedIneffectiveId);
	public abstract void setObsInd(String obsInd);  
	public abstract void setSeqNo(Integer seqNo);
	public abstract void setCreatedBy(String createdBy);
	public abstract void setLastUpdatedBy(String lastUpdatedBy);
	public abstract void setCreationDate(Date creationDate);
	public abstract void setLastUpdateDate(Date lastUpdateDate);
	public abstract void setCaseDiaryFixtureId(Integer caseDiaryFixtureId);
	public abstract void setDateOfRemoval(Date dateOfRemoval);
	public abstract void setListNotePredefinedId(Integer listNotePredefinedId);
	public abstract void setListNoteText(String listNoteText);
	public abstract void setParentCaseOnListId(Integer parentCaseOnListId);
	public abstract void setNhaFirmList(String nhaFirmList);
	public abstract void setVacationPreDefinedRsonId(Integer vacationPreDefinedRsonId);
}