package uk.gov.courtservice.xhibit.common.results.vos;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class LODReportValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;
	
	private String caseNumber; 
	private String caseTitle;
	private String diaryNoteText;
	private String creationDate;
	
	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	
	public String getDiaryNoteText() {
		return diaryNoteText;
	}
	public void setDiaryNoteText(String diaryNoteText) {
		this.diaryNoteText = diaryNoteText;
	}
	/**
	 * @return the surname
	 */
	
	/**
	 * @return the creationDate
	 */
	public String getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return the caseTitle
	 */
	public String getCaseTitle() {
		return caseTitle;
	}
	/**
	 * @param caseTitle the caseTitle to set
	 */
	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}
}
