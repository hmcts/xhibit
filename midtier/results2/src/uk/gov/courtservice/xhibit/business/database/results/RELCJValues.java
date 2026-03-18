package uk.gov.courtservice.xhibit.business.database.results;

import java.io.Serializable;

public class RELCJValues implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String caseNumber;
	private String caseTitle;
	private String classCode;
	private String hearingType;
	private String est;
	private String requiredJudge;
	private String chargesInfo;
	
	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	public String getCaseTitle() {
		return caseTitle;
	}
	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
		public String getEst() {
		return est;
	}
	public void setEst(String est) {
		this.est = est;
	}
	public String getRequiredJudge() {
		return requiredJudge;
	}
	public void setRequiredJudge(String requiredJudge) {
		this.requiredJudge = requiredJudge;
	}
	
	public String getHearingType() {
		return hearingType;
	}
	public void setHearingType(String hearingType) {
		this.hearingType = hearingType;
	}

	public String getChargesInfo() {
		return chargesInfo;
	}

	public void setChargesInfo(String chargesInfo) {
		this.chargesInfo = chargesInfo;
	}



}
