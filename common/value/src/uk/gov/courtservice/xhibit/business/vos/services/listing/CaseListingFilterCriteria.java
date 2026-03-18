package uk.gov.courtservice.xhibit.business.vos.services.listing;

import java.io.Serializable;

/**
 * Search criteria for the List Cases
 * 
 * <p>
 * Copyright: Copyright (c) 2019
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class CaseListingFilterCriteria implements Serializable {
		
	private static final long serialVersionUID = 1L;

	private Integer caseId;
	private Integer courtId;
	private String caseType;
	private String caseClass;
	private String bcStatus;
	private String hearingTypeCode;
	private Integer timeEstFrom;
	private Integer timeEstTo;
	private Integer units;
	private Integer refJudgeTypeId;
	private Integer unitsWeeks;
	private String SecureCourtRoom;
	private String juvenileOnly;

	public CaseListingFilterCriteria() {		
		this(null);
	}

	public CaseListingFilterCriteria(Integer caseId) {
		setCaseId(caseId);
	}
	
	public Integer getCourtId() {
		return courtId;
	}
	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}
	public Integer getCaseId() {
		return caseId;
	}
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public String getCaseClass() {
		return caseClass;
	}
	public void setCaseClass(String caseClass) {
		this.caseClass = caseClass;
	}
	public String getBcStatus() {
		return bcStatus;
	}
	public void setBcStatus(String bcStatus) {
		this.bcStatus = bcStatus;
	}
	public String getHearingTypeCode() {
		return hearingTypeCode;
	}
	public void setHearingTypeCode(String hearingTypeCode) {
		this.hearingTypeCode = hearingTypeCode;
	}
	public Integer getTimeEstFrom() {
		return timeEstFrom;
	}
	public void setTimeEstFrom(Integer timeEstFrom) {
		this.timeEstFrom = timeEstFrom;
	}
	public Integer getTimeEstTo() {
		return timeEstTo;
	}
	public void setTimeEstTo(Integer timeEstTo) {
		this.timeEstTo = timeEstTo;
	}
	public Integer getUnits() {
		return units;
	}
	public void setUnits(Integer units) {
		this.units = units;
	}
	public Integer getRefJudgeTypeId() {
		return refJudgeTypeId;
	}
	public void setRefJudgeTypeId(Integer refJudgeTypeId) {
		this.refJudgeTypeId = refJudgeTypeId;
	}
	public Integer getUnitsWeeks() {
		return unitsWeeks;
	}
	public void setUnitsWeeks(Integer unitsWeeks) {
		this.unitsWeeks = unitsWeeks;
	}
	public String getSecureCourtRoom() {
		return SecureCourtRoom;
	}
	public void setSecureCourtRoom(String secureCourtRoom) {
		SecureCourtRoom = secureCourtRoom;
	}
	public String getJuvenileOnly() {
		return juvenileOnly;
	}
	public void setJuvenileOnly(String juvenileOnly) {
		this.juvenileOnly = juvenileOnly;
	}

}