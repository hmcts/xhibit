package uk.gov.courtservice.xhibit.business.database.results;

import java.io.Serializable;
import java.util.List;

public class OutstandingCasesValues implements Serializable {


	private static final long serialVersionUID = 1L;

	private String diaryNoteEntryId;
	private String caseNumber;
	private String caseTitle;
	private String juvenile;
	private String commitedSent;
	private String classCode;
	private String hearingType;
	private String monitoringCategoryCode;
	private String loest;
	private String caseGroupNumber;
	private String firstNad;
	private String listed;
	private String noteClassification;
	private String custodyCase;
	private List<OutstandingCaseNotes> outstandingcasenotes;
	public String getDiaryNoteEntryId() {
		return diaryNoteEntryId;
	}
	public void setDiaryNoteEntryId(String diaryNoteEntryId) {
		this.diaryNoteEntryId = diaryNoteEntryId;
	}
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
	public String getLoest() {
		return loest;
	}
	public void setLoest(String loest) {
		this.loest = loest;
	}
	public String getCaseGroupNumber() {
		return caseGroupNumber;
	}
	public void setCaseGroupNumber(String caseGroupNumber) {
		this.caseGroupNumber = caseGroupNumber;
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
	public String getNoteClassification() {
		return noteClassification;
	}
	public void setNoteClassification(String noteClassification) {
		this.noteClassification = noteClassification;
	}
	public String getCustodyCase() {
		return custodyCase;
	}
	public void setCustodyCase(String custodyCase) {
		this.custodyCase = custodyCase;
	}
	public List<OutstandingCaseNotes> getOutstandingcasenotes() {
		return outstandingcasenotes;
	}
	public void setOutstandingcasenotes(List<OutstandingCaseNotes> outstandingcasenotes) {
		this.outstandingcasenotes = outstandingcasenotes;
	}
}