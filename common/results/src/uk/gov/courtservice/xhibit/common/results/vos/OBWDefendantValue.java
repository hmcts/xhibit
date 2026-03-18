package uk.gov.courtservice.xhibit.common.results.vos;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class OBWDefendantValue extends CSAbstractValue{
    private static final long serialVersionUID = 1L;

    private Integer caseId;
    private String caseNumber;
    private Integer defendantNumber;
    private String defendantName;
    private Integer classCode;
    private String bwIssueDate;
    private String solicitorFirmName;
    private String ptiurn;
	private String todayDate;
	
	public Integer getCaseId() {
		return caseId;
	}
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	
	public Integer getDefendantNumber() {
		return defendantNumber;
	}
	public void setDefendantNumber(Integer defendantNumber) {
		this.defendantNumber = defendantNumber;
	}
	
	public String getDefendantName() {
		return defendantName;
	}
	public void setDefendantName(String defendantName) {
		this.defendantName = defendantName;
	}
	public Integer getClassCode() {
		return classCode;
	}
	public void setClassCode(Integer classCode) {
		this.classCode = classCode;
	}
	public String getBwIssueDate() {
		return bwIssueDate;
	}
	public void setBwIssueDate(String bwIssueDate) {
		this.bwIssueDate = bwIssueDate;
	}
	public String getSolicitorFirmName() {
		return solicitorFirmName;
	}
	public void setSolicitorFirmName(String solicitorFirmName) {
		this.solicitorFirmName = solicitorFirmName;
	}
	public String getPtiurn() {
		return ptiurn;
	}
	public void setPtiurn(String ptiurn) {
		this.ptiurn = ptiurn;
	}
	public String getTodayDate() {
		return todayDate;
	}
	public void setTodayDate(String todayDate) {
		this.todayDate = todayDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	


}
