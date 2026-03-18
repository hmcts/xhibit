package uk.gov.courtservice.xhibit.client.listings.notes;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;

import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryComplexValue;

public class AbstractNotesModel {
	
	private Collection<DiaryNoteEntryComplexValue> diaryNotes;
	private DiaryNoteEntryComplexValue selectedDiaryNote;
	private Date diaryDate;
	private boolean dataChanged = false;
	
	public AbstractNotesModel() {
	}
	
    public void clearmodel() {
    	setDiaryNotes(null);
    	setSelectedDiaryNote(null);
    }

	public boolean isDataChanged() {
		return dataChanged;
	}

	public void setDataChanged(boolean dataChanged) {
		this.dataChanged = dataChanged;
	}

	public Collection<DiaryNoteEntryComplexValue> getDiaryNotes() {
		return diaryNotes;
	}

	public void setDiaryNotes(Collection<DiaryNoteEntryComplexValue> diaryNotes) {
		this.diaryNotes = diaryNotes;
	}

	public DiaryNoteEntryComplexValue getSelectedDiaryNote() {
		return selectedDiaryNote;
	}

	public void setSelectedDiaryNote(DiaryNoteEntryComplexValue selectedDiaryNote) {
		this.selectedDiaryNote = selectedDiaryNote;
	}
	
	public Date getDiaryDate() {
		return diaryDate;
	}

	public void setDiaryDate(Date diaryDate) {
		this.diaryDate = getDateWithoutTime(diaryDate);
	}
	
	public Date getDateWithoutTime(Date date) {
		return date != null ? DateTimeUtilities.stripTimeToUtilDate(date) : null;
	}

	public static Comparator<DiaryNoteEntryComplexValue> getSortByListOfficersDiary() {
		return sortByListOfficersDiary;
	}
	
	public static Comparator<DiaryNoteEntryComplexValue> getSortByPriority() {
		return sortByPriority;
	}
	
	private static Comparator<DiaryNoteEntryComplexValue> sortByListOfficersDiary = new Comparator<DiaryNoteEntryComplexValue>() {
		
		private static final String LAST_CHARACTER = "Z";
		
		public int compare(DiaryNoteEntryComplexValue o1, DiaryNoteEntryComplexValue o2) {
			// Sort by non-case first, then case type (descending)...
			String o1CaseType = o1.getCaseType() != null ? o1.getCaseType() : LAST_CHARACTER;
			String o2CaseType = o2.getCaseType() != null ? o2.getCaseType() : LAST_CHARACTER;
			Integer diff = o2CaseType.compareTo(o1CaseType);
			
			// ...then sort by case number (descending)...
			if (Integer.valueOf(0).equals(diff)) {
				Integer o1CaseNumber = o1.getCaseNumber() != null ? o1.getCaseNumber() : Integer.valueOf(0);
				Integer o2CaseNumber = o2.getCaseNumber() != null ? o2.getCaseNumber() : Integer.valueOf(0);
				diff = o2CaseNumber.compareTo(o1CaseNumber);
			}
			
			// ...then Sort by creation date
			if (Integer.valueOf(0).equals(diff)) {
				diff = o1.getCreationDate().compareTo(o2.getCreationDate());
			}
			return diff; 
		}
	};
	
	private static Comparator<DiaryNoteEntryComplexValue> sortByPriority = new Comparator<DiaryNoteEntryComplexValue>() {
		
		private Integer getPriority(DiaryNoteEntryComplexValue diaryNote) {
			Integer result = Integer.valueOf(99);
			if (diaryNote.isHighlight()) {
				// Highlight				
				result = Integer.valueOf(0);				
			} else if (diaryNote.isInterpreter()) {
				// Interpreter				
				result = Integer.valueOf(1);
			} else if (diaryNote.isDefaultCaseNote()) {
				// Case Note (Default)				
				result = Integer.valueOf(2);
			} else if (diaryNote.isCaseNote()) {
				// Case Note 				
				result = Integer.valueOf(3);
			} else if (diaryNote.isGeneralDiaryNote()) {
				// Diary Note
				result = Integer.valueOf(3);
			}  
			return result;
		}
		
		public int compare(DiaryNoteEntryComplexValue o1, DiaryNoteEntryComplexValue o2) {
			// Sort by priority...
			Integer o1Priority = getPriority(o1);
			Integer o2Priority = getPriority(o2);
			Integer diff = o1Priority.compareTo(o2Priority);		
			// ...then Sort by chronological order
			if (Integer.valueOf(0).equals(diff)) {
				diff = o1.getCreationDate().compareTo(o2.getCreationDate());
			}
			return diff; 
		}
	};
}
