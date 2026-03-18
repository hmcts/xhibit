package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: DiaryNoteEntryBasicValue
 * </p>
 * <p>
 * Description: DiaryNoteEntryBasicValue is intended to represent case entities as stored
 * in the DiaryNoteEntry table.
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Jasvir Boparai
 * @version 1.0
 */

public class DiaryNoteEntryBasicValue extends CSAbstractValue {

    private static final long serialVersionUID = 1L;
    
    private Integer diaryNoteEntryId;
	private Integer caseListingEntryId;
	private Integer noteTypeId;
	private Integer noteClassificationId;
	private String diaryNoteText;
	private Integer diaryNotePreDefinedId;
    private Date diaryDate;
	private Integer courtId;
	private String lastUpdatedBy;
	private Integer caseId;
	private Date lastUpdateDate;
	private Date creationDate;
	
	public DiaryNoteEntryBasicValue() {
        super();
    }
    
	public DiaryNoteEntryBasicValue(Integer noteTypeId) {
        super();
        setNoteTypeId(noteTypeId);
    }
	
    public DiaryNoteEntryBasicValue(Integer id, Integer version) {
        super(id, version);
    }
    
    /**
     * @return java.lang.Integer
     */
	public Integer getDiaryNoteEntryId() {
		return diaryNoteEntryId;
	}

	public void setDiaryNoteEntryId(Integer diaryNoteEntryId) {
		this.diaryNoteEntryId = diaryNoteEntryId;
	}

	public Integer getCaseListingEntryId() {
		return caseListingEntryId;
	}

	public void setCaseListingEntryId(Integer caseListingEntryId) {
		this.caseListingEntryId = caseListingEntryId;
	}

	public Integer getNoteTypeId() {
		return noteTypeId;
	}

	public void setNoteTypeId(Integer noteTypeId) {
		this.noteTypeId = noteTypeId;
	}

	public Integer getNoteClassificationId() {
		return noteClassificationId;
	}

	public void setNoteClassificationId(Integer noteClassificationId) {
		this.noteClassificationId = noteClassificationId;
	}

	public String getDiaryNoteText() {
		return diaryNoteText;
	}

	public void setDiaryNoteText(String diaryNoteText) {
		this.diaryNoteText = diaryNoteText;
	}

	public Integer getDiaryNotePreDefinedId() {
		return diaryNotePreDefinedId;
	}

	public void setDiaryNotePreDefinedId(Integer diaryNotePreDefinedId) {
		this.diaryNotePreDefinedId = diaryNotePreDefinedId;
	}

	public Date getDiaryDate() {
		return diaryDate;
	}

	public void setDiaryDate(Date diaryDate) {
		this.diaryDate = diaryDate;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	
	public boolean hasNote() {
		return (getDiaryNoteText() != null && !getDiaryNoteText().trim().isEmpty()) || getDiaryNotePreDefinedId() != null;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		return "DiaryNoteEntryBasicValue [diaryNoteEntryId=" + diaryNoteEntryId + ", caseListingEntryId="
				+ caseListingEntryId + ", noteTypeId=" + noteTypeId + ", noteClassificationId=" + noteClassificationId
				+ ", diaryNoteText=" + diaryNoteText + ", diaryNotePreDefinedId=" + diaryNotePreDefinedId
				+ ", diaryDate=" + diaryDate + ", courtId=" + courtId + ", caseId=" + caseId + "]";
	}
	
	
}
