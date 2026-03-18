package uk.gov.courtservice.xhibit.client.listings.notes;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryComplexValue;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseType;

public class ListOfficersDiaryModel extends AbstractNotesModel {

    public static interface ValidValues {
        public static final List<String> CASE_TYPES = CaseType.CaseListingCaseTypes();       
    }
	private Date selectedDate;
	
	public ListOfficersDiaryModel(Date selectedDate) {
		super();
		setSelectedDate(selectedDate);
	}

	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = getDateWithoutTime(selectedDate);
	}
	
	@Override
	public void setDiaryNotes(Collection<DiaryNoteEntryComplexValue> diaryNotes) {
		if (diaryNotes != null && !diaryNotes.isEmpty()) {
			Collections.sort((List<DiaryNoteEntryComplexValue>) diaryNotes, getSortByListOfficersDiary());
		}
		super.setDiaryNotes(diaryNotes);
	}
}
