package uk.gov.courtservice.xhibit.business.vos.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class CaseListingHistoryInformation implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String SUCCESS = "SUCCESS";
	private static final String FIXTURE = "Fixture";
	private static final String WARNED = "Warned";
	private static final String FIRM = "Firm";
	private Timestamp listStartDate;
	private Timestamp listEndDate;
	private Timestamp timeListed;
	private String listType;
	private String listNumber;
	private String draftOrFinal;
	private String reasonForRemoval;
	private Integer caseDiaryFixtureId;
	private Integer caseOnListId;
	private Timestamp dateOfRemoval;
	private String publishStatus;
	private Timestamp publishDate;
	private Timestamp lastUpdateDate;
	private String hearingTypeCode;
	private String courtSiteCode;
	private Integer courtRoomNo;
	private String sittingNumber;
	private String floaterCase;
	private String reserved;
	private String obsInd;

	/**
	 * @return the listStartDate
	 */
	public Timestamp getListStartDate() {
		return listStartDate;
	}
	
	/**
	 * @param listStartDate the listStartDate to set
	 */
	public void setListStartDate(Timestamp listStartDate) {
		this.listStartDate = listStartDate;
	}
	
	/**
	 * @return the listEndDate
	 */
	public Timestamp getListEndDate() {
		return listEndDate;
	}
	
	/**
	 * @param listEndDate the listEndDate to set
	 */
	public void setListEndDate(Timestamp listEndDate) {
		this.listEndDate = listEndDate;
	}

	/**
	 * @return the timeListed
	 */
	public Timestamp getTimeListed() {
		return timeListed;
	}
	
	/**
	 * @param set the timeListed
	 */
	public void setTimeListed(Timestamp timeListed) {
		this.timeListed = timeListed;
	}

	/**
	 * @return the listType
	 */
	public String getListType() {
		return listType;
	}
	
	/**
	 * @param listType the listType to set
	 */
	public void setListType(String listType) {
		this.listType = listType;
	}
	
	/**
	 * @return the reasonForRemoval
	 */
	public String getReasonForRemoval() {
		return reasonForRemoval;
	}
	
	/**
	 * @param reasonForRemoval the reasonForRemoval to set
	 */
	public void setReasonForRemoval(String reasonForRemoval) {
		this.reasonForRemoval = reasonForRemoval;
	}

	public Integer getCaseOnListId() {
		return caseOnListId;
	}

	public void setCaseOnListId(Integer caseOnListId) {
		this.caseOnListId = caseOnListId;
	}

	public Integer getCaseDiaryFixtureId() {
		return caseDiaryFixtureId;
	}

	public void setCaseDiaryFixtureId(Integer caseDiaryFixtureId) {
		this.caseDiaryFixtureId = caseDiaryFixtureId;
	}

	public Timestamp getDateOfRemoval() {
		return dateOfRemoval;
	}

	public void setDateOfRemoval(Timestamp dateOfRemoval) {
		this.dateOfRemoval = dateOfRemoval;
	}

	public String getHearingTypeCode() {
		return hearingTypeCode;
	}

	public void setHearingTypeCode(String hearingTypeCode) {
		this.hearingTypeCode = hearingTypeCode;
	}

	public String getCourtSiteCode() {
		return courtSiteCode;
	}

	public void setCourtSiteCode(String courtSiteCode) {
		this.courtSiteCode = courtSiteCode;
	}

	public Integer getCourtRoomNo() {
		return courtRoomNo;
	}

	public void setCourtRoomNo(Integer courtRoomNo) {
		this.courtRoomNo = courtRoomNo;
	}

	public String getSittingNumber() {
		return sittingNumber;
	}

	public void setSittingNumber(String sittingNumber) {
		this.sittingNumber = sittingNumber;
	}

	public String getFloaterCase() {
		return floaterCase;
	}

	public void setFloaterCase(String floaterCase) {
		this.floaterCase = floaterCase;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public String getListNumber() {
		return listNumber;
	}

	public void setListNumber(String listNumber) {
		this.listNumber = listNumber;
	}
	
	public String getDraftOrFinal() {
		return draftOrFinal;
	}

	public void setDraftOrFinal(String draftOrFinal) {
		this.draftOrFinal = draftOrFinal;
	}

	public Timestamp getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Timestamp publishDate) {
		this.publishDate = publishDate;
	}

	public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public Timestamp getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Timestamp lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}
	
	public boolean isPublished() {
		return SUCCESS.equals(getPublishStatus());
	}
	
	public boolean isFixture() {
		return FIXTURE.equalsIgnoreCase(getListType());
	}
	
	public boolean isWarned() {
		return WARNED.equalsIgnoreCase(getListType());
	}
	
	public boolean isChangeRemovalReasonMenuValid() {
		return getReasonForRemoval() != null;
	}

	public boolean isFutureDated() {
		return getListEndDate().after(new Date()); 
	}
	
	public boolean isReinstateMenuValid() {
		return getReasonForRemoval() != null && 
				isFutureDated() && isFixture();
	}
	
	public boolean isFirm() {
		return FIRM.equalsIgnoreCase(getListType());
	}

	public boolean isGoToListMenuValid() {
		return getCaseOnListId() != null;
	}
	
	public boolean isRemoveFromListMenuValid() {
		return (!isFixture() && 
				"N".equals(getObsInd()) && 
				getReasonForRemoval() == null && 
				getDateOfRemoval() == null);
	}
}
