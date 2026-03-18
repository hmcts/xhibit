package uk.gov.courtservice.xhibit.business.database.results;

import java.io.Serializable;
import java.util.List;

public class Hearings implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String caseDiaryFixtureId;
	private String hearingDate;
	private String caseNumber;
	private String caseTitle;
	private String classCode;
	private String bcStatus;
	private String hearingType;
	private String est;
	private String site;
	private String highlightNote;
	private String interpreterNote;
	private String requiredJudge;
	private String videoLinkRequired;
	private List<Notes> notes;
	public String getHearingDate() {
		return hearingDate;
	}
	public void setHearingDate(String hearingDate) {
		this.hearingDate = hearingDate;
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
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getBcStatus() {
		return bcStatus;
	}
	public void setBcStatus(String bcStatus) {
		this.bcStatus = bcStatus;
	}
	public String getHearingType() {
		return hearingType;
	}
	public void setHearingType(String hearingType) {
		this.hearingType = hearingType;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}

	public List<Notes> getNotes() {
		return notes;
	}
	public void setNotes(List<Notes> notes) {
		this.notes = notes;
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
	public String getVideoLinkRequired() {
		return videoLinkRequired;
	}
	public void setVideoLinkRequired(String videoLinkRequired) {
		this.videoLinkRequired = videoLinkRequired;
	}
	public String getHighlightNote() {
		return highlightNote;
	}
	public void setHighlightNote(String highlightNote) {
		this.highlightNote = highlightNote;
	}
	public String getInterpreterNote() {
		return interpreterNote;
	}
	public void setInterpreterNote(String interpreterNote) {
		this.interpreterNote = interpreterNote;
	}
	public String getCaseDiaryFixtureId() {
		return caseDiaryFixtureId;
	}
	public void setCaseDiaryFixtureId(String caseDiaryFixtureId) {
		this.caseDiaryFixtureId = caseDiaryFixtureId;
	}
	

}
