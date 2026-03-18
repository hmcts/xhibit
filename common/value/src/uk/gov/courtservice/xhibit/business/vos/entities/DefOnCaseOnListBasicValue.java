package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: DefOnCaseOnListBasicValue
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
public class DefOnCaseOnListBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private Integer defOnCaseOnListId;
	private Integer defendantOnCaseId;
	private Integer caseOnListId;
	private Integer caseId;
	private String obsInd;
	private String createdBy;
	private String lastUpdatedBy; 
	private Date creationDate;
	private Date lastUpdateDate;
	private String isCourtRoomListEntry;

	public DefOnCaseOnListBasicValue() {
		super();
	}

	public DefOnCaseOnListBasicValue(Integer id, Integer version) {
		super(id, version);
	}

	public Integer getDefOnCaseOnListId() {
		return defOnCaseOnListId;
	}

	public void setDefOnCaseOnListId(Integer defOnCaseOnListId) {
		this.defOnCaseOnListId = defOnCaseOnListId;
	}

	public Integer getDefendantOnCaseId() {
		return defendantOnCaseId;
	}

	public void setDefendantOnCaseId(Integer defendantOnCaseId) {
		this.defendantOnCaseId = defendantOnCaseId;
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

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
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
	
	public String getIsCourtRoomListEntry() {
		return isCourtRoomListEntry;
	}

	public void setIsCourtRoomListEntry(String isCourtRoomListEntry) {
		this.isCourtRoomListEntry = isCourtRoomListEntry;
	}

	@Override
	public String toString() {
		return "DefOnCaseOnListBasicValue [defOnCaseOnListId=" + defOnCaseOnListId
				+ ", defendantOnCaseId=" + defendantOnCaseId
				+ ", caseOnListId=" + caseOnListId
				+ ", caseId=" + caseId 
				+ ", obsInd=" + obsInd 
				+ ", isCourtRoomListEntry=" + isCourtRoomListEntry
				+ "]";
	}
}