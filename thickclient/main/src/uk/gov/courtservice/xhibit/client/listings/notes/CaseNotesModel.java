package uk.gov.courtservice.xhibit.client.listings.notes;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryComplexValue;

public class CaseNotesModel extends AbstractNotesModel {

	private Integer caseId;
	private String caseType;
	private Integer caseNumber;
	private Integer caseListingEntryId;
	private Timestamp dateTransTo;
	
	public CaseNotesModel(Integer caseId, String caseType, Integer caseNumber, Integer caseListingEntryId, Timestamp dateTransTo) {
		super();
		setCaseId(caseId);
		setCaseType(caseType);
        setCaseNumber(caseNumber);
        setCaseListingEntryId(caseListingEntryId);
        setDateTransTo(dateTransTo);
		clearmodel();
	}

	public void clearmodel() {
		super.clearmodel();
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

	public Integer getCaseNumber() {
        return caseNumber;
    }
    
    public void setCaseNumber(Integer caseNumber) {
    	this.caseNumber = caseNumber;
    }
    
	public Integer getCaseListingEntryId() {
		return caseListingEntryId;
	}

	public void setCaseListingEntryId(Integer caseListingEntryId) {
		this.caseListingEntryId = caseListingEntryId;
	}

	@Override
	public void setDiaryNotes(Collection<DiaryNoteEntryComplexValue> diaryNotes) {
		if (diaryNotes != null && !diaryNotes.isEmpty()) {
			Collections.sort((List<DiaryNoteEntryComplexValue>) diaryNotes, getSortByPriority());
		}
		super.setDiaryNotes(diaryNotes);
	}

	public Timestamp getDateTransTo() {
		return dateTransTo;
	}

	public void setDateTransTo(Timestamp dateTransTo) {
		this.dateTransTo = dateTransTo;
	}
}
