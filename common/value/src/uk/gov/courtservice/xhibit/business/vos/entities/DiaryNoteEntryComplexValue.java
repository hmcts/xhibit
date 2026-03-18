package uk.gov.courtservice.xhibit.business.vos.entities;

import java.sql.Timestamp;

import org.apache.axis.utils.StringUtils;

/**
 * <p>
 * Title: DiaryNoteEntryComplexValue
 * </p>
 * <p>
 * Description: DiaryNoteEntryComplexValue is intended to represent case entities as stored
 * in the DiaryNoteEntry table.
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author westalll
 * @version 1.0
 */

public class DiaryNoteEntryComplexValue extends DiaryNoteEntryBasicValue {

	private static final long serialVersionUID = 1L;

	private Integer caseNumber;
	private String caseType;
	private Timestamp dateTransTo;
	private RefListingDataBasicValue preDefinedNote;
	private RefListingDataBasicValue noteType;
	private RefListingDataBasicValue noteClassification;
 
	public DiaryNoteEntryComplexValue() {
        super();
    }
    
	public DiaryNoteEntryComplexValue(Integer id, Integer version) {
        super(id, version);
    }

	public Integer getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(Integer caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}	
	
	public String getDisplayCaseNumber() {
		return getCaseNumber() != null ? getCaseType() + getCaseNumber() : "";
	}
	
	public String getDisplayDiaryText() {
		String displayNumber = getDisplayCaseNumber();
		displayNumber = !StringUtils.isEmpty(displayNumber) ? displayNumber + " - " : displayNumber; 		
		return displayNumber + getDiaryNoteText();
	}
	
	@Override
	public String getDiaryNoteText() {
		if (getDiaryNotePreDefinedId() != null) {
			return getPreDefinedNote() != null ? getPreDefinedNote().getRefDataValue() : "";
		}
		return super.getDiaryNoteText();
	}
	
	public RefListingDataBasicValue getNoteClassification() {
		return noteClassification;
	}

	public void setNoteClassification(RefListingDataBasicValue noteClassification) {
		this.noteClassification = noteClassification;
	}

	public RefListingDataBasicValue getNoteType() {
		return noteType;
	}

	public void setNoteType(RefListingDataBasicValue noteType) {
		this.noteType = noteType;
	}
	
	public RefListingDataBasicValue getPreDefinedNote() {
		return preDefinedNote;
	}

	public void setPreDefinedNote(RefListingDataBasicValue preDefinedNote) {
		this.preDefinedNote = preDefinedNote;
	}
	
	public boolean isHighlight() {
		return RefListingDataBasicValue.DataValue.HIGHLIGHT_NOTE.equals(getNoteType().getRefDataValue());
	}
	
	public boolean isInterpreter() {
		return RefListingDataBasicValue.DataValue.INTERPRETER_NOTE.equals(getNoteType().getRefDataValue());
	}
	
	public boolean isDefaultCaseNote() {
		return RefListingDataBasicValue.DataValue.DEFAULT_CASE_NOTE.equals(getNoteType().getRefDataValue());
	}
	
	public boolean isGeneralDiaryNote() {
		return RefListingDataBasicValue.DataValue.GENERAL_DIARY_NOTE.equals(getNoteType().getRefDataValue());
	}
	
	public boolean isCaseNote() {
		return RefListingDataBasicValue.DataValue.CASE_NOTE.equals(getNoteType().getRefDataValue());
	}
		
	public String getNotesTypeShortName() {
		String shortName = null;
		if (getNoteType() != null && getNoteType().getRefDataValue() != null) {
			if (isHighlight()) {
				shortName = RefListingDataBasicValue.ShortName.HIGHLIGHT_NOTE;
			} else if (isInterpreter()) {
				shortName = RefListingDataBasicValue.ShortName.INTERPRETER_NOTE;
			} else if (isDefaultCaseNote()) {
				shortName = RefListingDataBasicValue.ShortName.DEFAULT_CASE_NOTE;
			} else if (isCaseNote()) {
				shortName = RefListingDataBasicValue.ShortName.CASE_NOTE;
			} else if (isGeneralDiaryNote()) {
				shortName = RefListingDataBasicValue.ShortName.GENERAL_DIARY_NOTE;
			}
		}
		return shortName;
	}
	
	public String getNoteClassificationShortName() {
		String shortName = null;
		if (getNoteClassification() != null && getNoteClassification().getRefDataValue() != null) {
			shortName = Character.toString(getNoteClassification().getRefDataValue().charAt(0));
		}
		return shortName;
	}

	public Timestamp getDateTransTo() {
		return dateTransTo;
	}

	public void setDateTransTo(Timestamp dateTransTo) {
		this.dateTransTo = dateTransTo;
	}
}
