package uk.gov.courtservice.xhibit.business.database.results;

import java.io.Serializable;

import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;

public class CaseNotes implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected String noteType;
	protected String notePrefix;
	protected String noteDate;
	protected String diaryDate;
	protected String diaryNoteText;

	public String getDiaryNoteText() {
		return diaryNoteText;
	}

	public void setDiaryNoteText(String diaryNoteText) {
		this.diaryNoteText = diaryNoteText;
	}

	public String getNoteType() {
		return noteType;
	}

	public void setNoteType(String noteType) {
		this.noteType = noteType;
	}

	public String getNoteDate() {
		return noteDate;
	}

	public void setNoteDate(String noteDate) {
		this.noteDate = noteDate;
	}

	public String getDiaryDate() {
		return diaryDate;
	}

	public void setDiaryDate(String diaryDate) {
		this.diaryDate = diaryDate;
	}

	public String getNotePrefix() {
		return notePrefix;
	}	
	
	public void setNotePrefix(String notePrefix) {
		if (RefListingDataBasicValue.DataValue.CASE_NOTE.equals(notePrefix)) {
			this.notePrefix = RefListingDataBasicValue.ShortName.CASE_NOTE;
		} else if (RefListingDataBasicValue.DataValue.DEFAULT_CASE_NOTE.equals(notePrefix)) {
			this.notePrefix = RefListingDataBasicValue.ShortName.DEFAULT_CASE_NOTE;
		} else if (RefListingDataBasicValue.DataValue.GENERAL_DIARY_NOTE.equals(notePrefix)) {
			this.notePrefix = RefListingDataBasicValue.ShortName.GENERAL_DIARY_NOTE;
		} else if (RefListingDataBasicValue.DataValue.HIGHLIGHT_NOTE.equals(notePrefix)) {
			this.notePrefix = RefListingDataBasicValue.ShortName.HIGHLIGHT_NOTE;
		}else if (RefListingDataBasicValue.DataValue.INTERPRETER_NOTE.equals(notePrefix)) {
			this.notePrefix = RefListingDataBasicValue.ShortName.INTERPRETER_NOTE;
		} else {
			this.notePrefix = notePrefix;
		}
	}
}
