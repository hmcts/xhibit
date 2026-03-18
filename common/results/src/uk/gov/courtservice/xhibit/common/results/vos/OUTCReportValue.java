package uk.gov.courtservice.xhibit.common.results.vos;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class OUTCReportValue extends CSAbstractValue {
	
	private static final long serialVersionUID = 1L;
	
	private String caseNumber;
	private String caseTitle;
	private String juvenile;
	private String commitedSent;
	private String classCode;
	private String hearingType;
	private String monitoringCategoryCode;
	private String loEst;
	private String caseGroupeNumber;
	private String firstNad;
	private String listed;
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
	public String getJuvenile() {
		return juvenile;
	}
	public void setJuvenile(String juvenile) {
		this.juvenile = juvenile;
	}
	public String getCommitedSent() {
		return commitedSent;
	}
	public void setCommitedSent(String commitedSent) {
		this.commitedSent = commitedSent;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getHearingType() {
		return hearingType;
	}
	public void setHearingType(String hearingType) {
		this.hearingType = hearingType;
	}
	public String getMonitoringCategoryCode() {
		return monitoringCategoryCode;
	}
	public void setMonitoringCategoryCode(String monitoringCategoryCode) {
		this.monitoringCategoryCode = monitoringCategoryCode;
	}
	public String getLoEst() {
		return loEst;
	}
	public void setLoEst(String loEst) {
		this.loEst = loEst;
	}
	public String getCaseGroupeNumber() {
		return caseGroupeNumber;
	}
	public void setCaseGroupeNumber(String caseGroupeNumber) {
		this.caseGroupeNumber = caseGroupeNumber;
	}
	public String getFirstNad() {
		return firstNad;
	}
	public void setFirstNad(String firstNad) {
		this.firstNad = firstNad;
	}
	public String getListed() {
		return listed;
	}
	public void setListed(String listed) {
		this.listed = listed;
	}
	
	

}
