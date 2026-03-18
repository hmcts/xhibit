package uk.gov.courtservice.xhibit.business.vos.services.listing;

import java.util.Collection;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;


/**
 * <p>
 * Title: CaseListingEntryValue
 * </p>
 * <p>
 * Description: 
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */

public class CaseListingEntryValue extends CSAbstractValue {
    
    private static final long serialVersionUID = 1L;
	
	private CaseListingEntryBasicValue caseListingEntryBasicValue;
	private CaseBasicValue caseBasicValue;
	private DiaryNoteEntryBasicValue highlightDiaryNoteEntry;
	private DiaryNoteEntryBasicValue interpreterDiaryNoteEntry;
	private DiaryNoteEntryBasicValue preDefinedDiaryNoteEntry;
	private DiaryNoteEntryBasicValue freeTextDiaryNoteEntry;
	private DirectionsForCaseBasicValue directionsForCase;
	private Collection<DefendantValue> defendants;
    
    public CaseListingEntryValue(CaseListingEntryBasicValue caseListingEntryBasicValue,
    		CaseBasicValue caseBasicValue,
    		DiaryNoteEntryBasicValue highlightDiaryNoteEntry,
    		DiaryNoteEntryBasicValue interpreterDiaryNoteEntry,
    		DiaryNoteEntryBasicValue preDefinedDiaryNoteEntry,
    		DiaryNoteEntryBasicValue freeTextDiaryNoteEntry,
    		DirectionsForCaseBasicValue directionsForCase,
    		Collection<DefendantValue> defendants) {
    	setCaseListingEntryBasicValue(caseListingEntryBasicValue);
    	setCaseBasicValue(caseBasicValue);
    	setHighlightDiaryNoteEntry(highlightDiaryNoteEntry);
    	setInterpreterDiaryNoteEntry(interpreterDiaryNoteEntry);
    	setPreDefinedDiaryNoteEntry(preDefinedDiaryNoteEntry);
    	setFreeTextDiaryNoteEntry(freeTextDiaryNoteEntry);
    	setDirectionsForCase(directionsForCase);
    	setDefendants(defendants);
    }
    
	public Collection<DefendantValue> getDefendants() {
		return defendants;
	}

	public void setDefendants(Collection<DefendantValue> defendants) {
		this.defendants = defendants;
	}

	public CaseListingEntryBasicValue getCaseListingEntryBasicValue() {
		return caseListingEntryBasicValue;
	}
	
	public void setCaseListingEntryBasicValue(CaseListingEntryBasicValue caseListingEntryBasicValue) {
		this.caseListingEntryBasicValue = caseListingEntryBasicValue;
	}

	public CaseBasicValue getCaseBasicValue() {
		return caseBasicValue;
	}
	
	public void setCaseBasicValue(CaseBasicValue caseBasicValue) {
		this.caseBasicValue = caseBasicValue;
	}
	
	public DiaryNoteEntryBasicValue getHighlightDiaryNoteEntry() {
		return highlightDiaryNoteEntry;
	}

	public void setHighlightDiaryNoteEntry(DiaryNoteEntryBasicValue highlightDiaryNoteEntry) {
		this.highlightDiaryNoteEntry = highlightDiaryNoteEntry;
	}

	public DiaryNoteEntryBasicValue getInterpreterDiaryNoteEntry() {
		return interpreterDiaryNoteEntry;
	}

	public void setInterpreterDiaryNoteEntry(DiaryNoteEntryBasicValue interpreterDiaryNoteEntry) {
		this.interpreterDiaryNoteEntry = interpreterDiaryNoteEntry;
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

	public DirectionsForCaseBasicValue getDirectionsForCase() {
		return directionsForCase;
	}

	public void setDirectionsForCase(DirectionsForCaseBasicValue directionsForCase) {
		this.directionsForCase = directionsForCase;
	}

}