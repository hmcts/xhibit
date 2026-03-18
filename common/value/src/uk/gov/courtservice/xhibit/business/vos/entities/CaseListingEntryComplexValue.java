package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Collection;

import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

/**
 * <p>
 * Title: CaseListingEntryComplexValue
 * </p>
 * <p>
 * Description: CaseListingEntryComplexValue is intended to represent case entities as stored
 * in the CaseListingEntry table.
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */

public class CaseListingEntryComplexValue extends CaseListingEntryBasicValue {

    private static final long serialVersionUID = 1L;

    private CaseBasicValue caseBasicValue;
    private RefSystemCodeBasicValue refJudgeType;
    private RefJudgeBasicValue refJudge;
    private DirectionsForCaseBasicValue directionsForCase;
    private DiaryNoteEntryBasicValue highlightDiaryNoteEntry;
    private DiaryNoteEntryBasicValue preDefinedDiaryNoteEntry;
    private DiaryNoteEntryBasicValue freeTextDiaryNoteEntry;
    private DiaryNoteEntryBasicValue interpreterDiaryNoteEntry;
    private RefListingDataBasicValue highlightNoteType;
    private RefListingDataBasicValue defaultCaseNoteType;
    private RefListingDataBasicValue interpreterNoteType;
    private RefSystemCodeBasicValue ticketType;
    private Collection<CaseDiaryFixtureComplexValue> fixtures;
    private Collection<DefendantValue> defendants;
 
	public CaseListingEntryComplexValue() {
        super();
    }
	
    public CaseListingEntryComplexValue(Integer id, Integer version) {
        super(id, version);
    }

	public DiaryNoteEntryBasicValue getHighlightDiaryNoteEntry() {
		return highlightDiaryNoteEntry;
	}

	public void setHighlightDiaryNoteEntry(DiaryNoteEntryBasicValue highlightDiaryNoteEntry) {
		this.highlightDiaryNoteEntry = highlightDiaryNoteEntry;
	}

	public DiaryNoteEntryBasicValue getPreDefinedDiaryNoteEntry() {
		return preDefinedDiaryNoteEntry;
	}

	public void setPreDefinedDiaryNoteEntry(DiaryNoteEntryBasicValue preDefinedDiaryNoteEntry) {
		this.preDefinedDiaryNoteEntry = preDefinedDiaryNoteEntry;
	}

	public DiaryNoteEntryBasicValue getFreeTextDiaryNoteEntry() {
		return freeTextDiaryNoteEntry;
	}

	public void setFreeTextDiaryNoteEntry(DiaryNoteEntryBasicValue freeTextDiaryNoteEntry) {
		this.freeTextDiaryNoteEntry = freeTextDiaryNoteEntry;
	}

	public DiaryNoteEntryBasicValue getInterpreterDiaryNoteEntry() {
		return interpreterDiaryNoteEntry;
	}

	public void setInterpreterDiaryNoteEntry(DiaryNoteEntryBasicValue interpreterDiaryNoteEntry) {
		this.interpreterDiaryNoteEntry = interpreterDiaryNoteEntry;
	}

	public RefListingDataBasicValue getHighlightNoteType() {
		return highlightNoteType;
	}

	public void setHighlightNoteType(RefListingDataBasicValue highlightNoteType) {
		this.highlightNoteType = highlightNoteType;
	}

	public RefListingDataBasicValue getDefaultCaseNoteType() {
		return defaultCaseNoteType;
	}

	public void setDefaultCaseNoteType(RefListingDataBasicValue defaultCaseNoteType) {
		this.defaultCaseNoteType = defaultCaseNoteType;
	}
	
	public RefListingDataBasicValue getInterpreterNoteType() {
		return interpreterNoteType;
	}

	public void setInterpreterNoteType(RefListingDataBasicValue interpreterNoteType) {
		this.interpreterNoteType = interpreterNoteType;
	}

	public Collection<CaseDiaryFixtureComplexValue> getFixtures() {
		return fixtures;
	}

	public void setFixtures(Collection<CaseDiaryFixtureComplexValue> fixtures) {
		this.fixtures = fixtures;
	}

	public CaseBasicValue getCaseBasicValue() {
		return caseBasicValue;
	}

	public void setCaseBasicValue(CaseBasicValue caseBasicValue) {
		this.caseBasicValue = caseBasicValue;
	}

	public RefSystemCodeBasicValue getRefJudgeType() {
		return refJudgeType;
	}

	public void setRefJudgeType(RefSystemCodeBasicValue refJudgeType) {
		this.refJudgeType = refJudgeType;
	}

	public RefJudgeBasicValue getRefJudge() {
		return refJudge;
	}

	public void setRefJudge(RefJudgeBasicValue refJudge) {
		this.refJudge = refJudge;
	}

	public Collection<DefendantValue> getDefendants() {
		return defendants;
	}

	public void setDefendants(Collection<DefendantValue> defendants) {
		this.defendants = defendants;
	}

	public DirectionsForCaseBasicValue getDirectionsForCase() {
		return directionsForCase;
	}

	public void setDirectionsForCase(DirectionsForCaseBasicValue directionsForCase) {
		this.directionsForCase = directionsForCase;
	}

	public RefSystemCodeBasicValue getTicketType() {
		return ticketType;
	}

	public void setTicketType(RefSystemCodeBasicValue ticketType) {
		this.ticketType = ticketType;
	}
}
