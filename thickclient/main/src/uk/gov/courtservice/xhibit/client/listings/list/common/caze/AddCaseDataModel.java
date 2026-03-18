package uk.gov.courtservice.xhibit.client.listings.list.common.caze;

import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * Data model used to add a new U or B case.
 * @author westalll
 *
 */

public class AddCaseDataModel {
	private Integer caseId;
	private Integer caseNumber;
	private String caseType;
	private Integer courtId;
	private Integer courtSiteId;
	private String caseTitle;
	private String caseTypeAndNumber;
	private String hearingTypeCode;
	private Integer hearingTypeId;
	private String hearingTypeDescription;
	private XhibitApplicationController xac;
	
	public final Integer getCaseId() {
		return caseId;
	}
	public final void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	
	public final Integer getCaseNumber() {
		return caseNumber;
	}
	public final void setCaseNumber(Integer caseNumber) {
		this.caseNumber = caseNumber;
	}
	public final String getCaseType() {
		return caseType;
	}
	public final void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public final Integer getCourtId() {
		return courtId;
	}
	public final void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}
	public final Integer getCourtSiteId() {
		return courtSiteId;
	}
	public final void setCourtSiteId(Integer courtSiteId) {
		this.courtSiteId = courtSiteId;
	}
	public final String getCaseTitle() {
		return caseTitle;
	}
	public final void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}
	public final String getCaseTypeAndNumber() {
		return caseTypeAndNumber;
	}
	public final void setCaseTypeAndNumber(String caseTypeAndNumber) {
		this.caseTypeAndNumber = caseTypeAndNumber;
	}
	public final String getHearingTypeCode() {
		return hearingTypeCode;
	}
	public final void setHearingTypeCode(String hearingTypeCode) {
		this.hearingTypeCode = hearingTypeCode;
	}
	public final Integer getHearingTypeId() {
		return hearingTypeId;
	}
	public final void setHearingTypeId(Integer hearingTypeId) {
		this.hearingTypeId = hearingTypeId;
	}
	public final String getHearingTypeDescription() {
		return hearingTypeDescription;
	}
	public final void setHearingTypeDescription(String hearingTypeDescription) {
		this.hearingTypeDescription = hearingTypeDescription;
	}
	public final XhibitApplicationController getXac() {
		return xac;
	}
	public final void setXac(XhibitApplicationController xac) {
		this.xac = xac;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AddCaseDataModel [caseId=");
		builder.append(caseId);
		builder.append(", caseNumber=");
		builder.append(caseNumber);
		builder.append(", caseType=");
		builder.append(caseType);
		builder.append(", courtId=");
		builder.append(courtId);
		builder.append(", courtSiteId=");
		builder.append(courtSiteId);
		builder.append(", caseTitle=");
		builder.append(caseTitle);
		builder.append(", caseTypeAndNumber=");
		builder.append(caseTypeAndNumber);
		builder.append(", hearingTypeCode=");
		builder.append(hearingTypeCode);
		builder.append(", hearingTypeId=");
		builder.append(hearingTypeId);
		builder.append(", hearingTypeDescription=");
		builder.append(hearingTypeDescription);
		builder.append("]");
		return builder.toString();
	}

	
}
