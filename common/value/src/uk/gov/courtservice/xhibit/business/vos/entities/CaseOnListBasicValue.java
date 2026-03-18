package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.util.DateTimeUtilities;

/**
 * <p>
 * Title: CaseOnListBasicValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class CaseOnListBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	// 1st January 1970 is used for date of time listed when only time is required
	private static final Calendar ZERO_DATE = new GregorianCalendar(1970, 0, 1);

	private Integer caseOnListId;
	private Integer caseId;
	private Integer listId;
	private Integer sittingOnListId;
	private Integer courtSiteId;
	private Integer courtRoomId;	
	private String reserved;
	private String floaterCase;
	private Integer timeMarkingId;
	private Calendar timeListed;
	private String isCourtRoomListEntry;
	private Integer hearingTypeId;
	private String reasonForRemoval;
	private Integer crackedIneffectiveId;	
	private String obsInd;
	private Integer seqNo;
	private String createdBy;
	private String lastUpdatedBy; 
	private Date creationDate;
	private Date lastUpdateDate;
	private Integer caseDiaryFixtureId;
	private Date dateOfRemoval;
	private Integer listNotePredefinedId;
	private String listNoteText;
	private Integer parentCaseOnListId;
	private String nhaFirmList;
	private Integer vacationPreDefinedRsonId;

	public CaseOnListBasicValue() {
		super();
	}

	public CaseOnListBasicValue(Integer id, Integer version) {
		super(id, version);
	}

	public Integer getCaseOnListId() {
		return caseOnListId;
	}

	public void setCaseOnListId(Integer caseOnListId) {
		this.caseOnListId = caseOnListId;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Integer getListId() {
		return listId;
	}

	public void setListId(Integer listId) {
		this.listId = listId;
	}

	public Integer getSittingOnListId() {
		return sittingOnListId;
	}

	public void setSittingOnListId(Integer sittingOnListId) {
		this.sittingOnListId = sittingOnListId;
	}

	public Integer getCourtSiteId() {
		return courtSiteId;
	}

	public void setCourtSiteId(Integer courtSiteId) {
		this.courtSiteId = courtSiteId;
	}

	public Integer getCourtRoomId() {
		return courtRoomId;
	}

	public void setCourtRoomId(Integer courtRoomId) {
		this.courtRoomId = courtRoomId;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public Integer getTimeMarkingId() {
		return timeMarkingId;
	}

	public void setTimeMarkingId(Integer timeMarkingId) {
		this.timeMarkingId = timeMarkingId;
	}

	public Calendar getTimeListed() {
		return timeListed;
	}	

	public void setTimeListed(Calendar timeListed) {
		this.timeListed = timeListed;
	}

	public Date getTimeListedDate() {
		Date dateListed = null;
		if (timeListed != null) {
			dateListed = DateTimeUtilities.stripTimeToUtilDate(timeListed.getTime());
		}
		return dateListed;
	}	

	public void setTimeListedDate(Date dateListed) {
		if (timeListed == null) {
			timeListed = DateTimeUtilities.stripTimeToCalendar(dateListed);			
		} else {
			Calendar cal = DateTimeUtilities.convertToCalendar(dateListed);
			timeListed.set(Calendar.YEAR, cal.get(Calendar.YEAR));
			timeListed.set(Calendar.MONTH, cal.get(Calendar.MONTH));
			timeListed.set(Calendar.DATE, cal.get(Calendar.DATE));
		}
	}

	public boolean hasTimeListedDate() {
		boolean hasDate = false;
		if (timeListed != null &&
			!(timeListed.get(Calendar.YEAR) == ZERO_DATE.get(Calendar.YEAR) &&
			  timeListed.get(Calendar.MONTH) == ZERO_DATE.get(Calendar.MONTH) &&
			  timeListed.get(Calendar.DATE) == ZERO_DATE.get(Calendar.DATE))) {
			hasDate = true;
		}
		return hasDate;
	}
	
	public int getTimeListedHour() {
		int hourListed = 0;
		if (timeListed != null) {
			hourListed = timeListed.get(Calendar.HOUR_OF_DAY);
		}
		return hourListed;
	}	
	
	public void setTimeListedHour(int hourListed) {
		if (timeListed == null) {
			timeListed = DateTimeUtilities.stripTimeToCalendar(new Date());
			clearTimeListedDate();
		}
		timeListed.set(Calendar.HOUR_OF_DAY, hourListed);
	}
	
	public int getTimeListedMinute() {
		int minuteListed = 0;
		if (timeListed != null) {
			minuteListed = timeListed.get(Calendar.MINUTE);
		}
		return minuteListed;
	}	
	
	public void setTimeListedMinute(int minuteListed) {
		if (timeListed == null) {
			timeListed = DateTimeUtilities.stripTimeToCalendar(new Date());
			clearTimeListedDate();
		}
		timeListed.set(Calendar.MINUTE, minuteListed);
	}

	public void clearTimeListedDate() {
		if (timeListed != null) {
			timeListed.set(Calendar.YEAR, ZERO_DATE.get(Calendar.YEAR));
			timeListed.set(Calendar.MONTH, ZERO_DATE.get(Calendar.MONTH));
			timeListed.set(Calendar.DATE, ZERO_DATE.get(Calendar.DATE));
		}
	}

	public void clearTimeListedTime() {
		if (timeListed != null) {
			timeListed = DateTimeUtilities.stripTimeToCalendar(timeListed.getTime());
		}
	}
	
	public String getIsCourtRoomListEntry() {
		return isCourtRoomListEntry;
	}

	public void setIsCourtRoomListEntry(String isCourtRoomListEntry) {
		this.isCourtRoomListEntry = isCourtRoomListEntry;
	}

	public Integer getHearingTypeId() {
		return hearingTypeId;
	}

	public void setHearingTypeId(Integer hearingTypeId) {
		this.hearingTypeId = hearingTypeId;
	}

	public String getReasonForRemoval() {
		return reasonForRemoval;
	}

	public void setReasonForRemoval(String reasonForRemoval) {
		this.reasonForRemoval = reasonForRemoval;
	}

	public Integer getCrackedIneffectiveId() {
		return crackedIneffectiveId;
	}

	public void setCrackedIneffectiveId(Integer crackedIneffectiveId) {
		this.crackedIneffectiveId = crackedIneffectiveId;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}


	public String getFloaterCase() {
		return floaterCase;
	}

	public void setFloaterCase(String floaterCase) {
		this.floaterCase = floaterCase;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	public Integer getCaseDiaryFixtureId() {
		return caseDiaryFixtureId;
	}

	public void setCaseDiaryFixtureId(Integer caseDiaryFixtureId) {
		this.caseDiaryFixtureId = caseDiaryFixtureId;
	}
	
	public Date getDateOfRemoval() {
		return dateOfRemoval;
	}

	public void setDateOfRemoval(Date dateOfRemoval) {
		this.dateOfRemoval = dateOfRemoval;
	}
	
	public Integer getListNotePredefinedId() {
		return listNotePredefinedId;
	}

	public void setListNotePredefinedId(Integer listNotePredefinedId) {
		this.listNotePredefinedId = listNotePredefinedId;
	}
	
	public String getListNoteText() {
		return listNoteText;
	}

	public void setListNoteText(String listNoteText) {
		this.listNoteText = listNoteText;
	}

	public Integer getParentCaseOnListId() {
		return parentCaseOnListId;
	}

	public void setParentCaseOnListId(Integer parentCaseOnListId) {
		this.parentCaseOnListId = parentCaseOnListId;
	}

	public String getNhaFirmList() {
		return nhaFirmList;
	}

	public void setNhaFirmList(String nhaFirmList) {
		this.nhaFirmList = nhaFirmList;
	}
	
	public Integer getVacationPreDefinedRsonId() {
		return vacationPreDefinedRsonId;
	}

	public void setVacationPreDefinedRsonId(Integer vacationPreDefinedRsonId) {
		this.vacationPreDefinedRsonId = vacationPreDefinedRsonId;
	}

	@Override
	public String toString() {
		return "CaseOnListBasicValue [caseOnListId=" + caseOnListId + ", caseId=" + caseId + ", listId=" + listId
				+ ", sittingOnListId=" + sittingOnListId + ", courtSiteId=" + courtSiteId + ", courtRoomId=" + courtRoomId
				+ ", reserved=" + reserved + ", floaterCase=" + floaterCase + ", timeMarkingId=" + timeMarkingId
				+ ", timeListed=" + timeListed + ", isCourtRoomListEntry=" + isCourtRoomListEntry + ", hearingTypeId="
				+ hearingTypeId + ", reasonForRemoval=" + reasonForRemoval + ", crackedIneffectiveId="
				+ crackedIneffectiveId + ", obsInd=" + obsInd + ", seqNo=" + seqNo + ", caseDiaryFixtureId=" + caseDiaryFixtureId
				+ ", dateOfRemoval=" + dateOfRemoval + ", listNotePredefinedId=" + listNotePredefinedId  
				+ ", parentCaseOnListId =" + parentCaseOnListId  + ", nhaFirmList=" + nhaFirmList 
				+ ", vacationPreDefinedRsonId=" + vacationPreDefinedRsonId + ", listNoteText=" + listNoteText + "]";
	}

}