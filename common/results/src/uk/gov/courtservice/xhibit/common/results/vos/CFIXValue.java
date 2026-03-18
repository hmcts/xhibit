package uk.gov.courtservice.xhibit.common.results.vos;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class CFIXValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private String hearingDate;
	private String caseNumber;
	private String caseTitle;
	private String classCode;
	private String bcStatus;
	private String hearingType;
	private String est;
	private String site;
	private String noteType;
	private String diaryNoteText;
	private String judgeRequired;
	private String videoLinkRequired;
	private String listNote;

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

	public String getNoteType() {
		return noteType;
	}

	public void setNoteType(String NoteType) {
		this.noteType = NoteType;
	}

	public String getDiaryNoteText() {
		return diaryNoteText;
	}

	public void setDiaryNoteText(String diaryNoteText) {
		this.diaryNoteText = diaryNoteText;
	}

	public String getJudgeRequired() {
		return judgeRequired;
	}

	public void setJudgeRequired(String judgeRequired) {
		this.judgeRequired = judgeRequired;
	}

	public String getVideoLinkRequired() {
		return videoLinkRequired;
	}

	public void setVideoLinkRequired(String videoLinkRequired) {
		this.videoLinkRequired = videoLinkRequired;
	}

	public String getListNote() {
		return listNote;
	}

	public void setListNote(String listNote) {
		this.listNote = listNote;
	}

	public String getEst() {
		return est;
	}

	public void setEst(String est) {
		this.est = est;
	}

}
