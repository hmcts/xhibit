package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.util.Date;

public class DiaryTableRowModel {

	private Integer diaryNoteEntryId;
	private String caseNote;
	private String userId;
	private Date diaryDate;
	
	public DiaryTableRowModel(Integer diaryNoteEntryId, String caseNote, String userId, Date diaryDate) {
		this.diaryNoteEntryId = diaryNoteEntryId;
		this.caseNote = caseNote;
		this.userId = userId;
		this.setDiaryDate(diaryDate);
	}

	public void setDiaryNoteEntryId(Integer diaryNoteEntryId) {
		this.diaryNoteEntryId = diaryNoteEntryId;
	}

	public String getCaseNote() {
		return caseNote;
	}

	public void setCaseNote(String caseNote) {
		this.caseNote = caseNote;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public Date getDiaryDate() {
		return diaryDate;
	}

	public void setDiaryDate(Date diaryDate) {
		this.diaryDate = diaryDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((diaryNoteEntryId == null) ? 0 : diaryNoteEntryId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DiaryTableRowModel other = (DiaryTableRowModel) obj;
		if (diaryNoteEntryId == null) {
			if (other.diaryNoteEntryId != null)
				return false;
		} else if (!diaryNoteEntryId.equals(other.diaryNoteEntryId))
			return false;
		return true;
	}
}
