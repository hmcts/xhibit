package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Collection;

/**
 * <p>
 * Title: CaseDiaryFixtureComplexValue
 * </p>
 * <p>
 * Description: CaseDiaryFixtureComplexValue is intended to represent case entities as stored
 * in the CaseDiaryFixture table.
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */

public class CaseDiaryFixtureComplexValue extends CaseDiaryFixtureBasicValue {

    private static final long serialVersionUID = 1L;
    
    private static final String NOTE_TEXT_DELIMITER = ";";
    
    private RefHearingTypeBasicValue refHearingType;
    private RefListingDataBasicValue preDefinedlistNote;
    private Collection<FixtureDeftAttendingBasicValue> fixtureDeftAttending;
    private CaseBasicValue caseBasicValue;
    private DirectionsForCaseBasicValue directionsForCaseBasicValue;

	public CaseDiaryFixtureComplexValue() {
        super();
    }
    
	public CaseDiaryFixtureComplexValue(Integer id, Integer version) {
        super(id, version);
    }

	public RefHearingTypeBasicValue getRefHearingType() {
		return refHearingType;
	}

	public void setRefHearingType(RefHearingTypeBasicValue refHearingType) {
		this.refHearingType = refHearingType;
	}
	
	public RefListingDataBasicValue getPreDefinedlistNote() {
		return preDefinedlistNote;
	}

	public void setPreDefinedlistNote(RefListingDataBasicValue preDefinedlistNote) {
		this.preDefinedlistNote = preDefinedlistNote;
	}

	public String getHearingTypeDesc() {
		return getRefHearingType() != null ? getRefHearingType().getHearingTypeDesc() : null;
	}
	
	public String getDisplayListNoteText() {
		String result = getListNoteText();
		if (getPreDefinedlistNote() != null) {
			if ( result == null ) {
				result = getPreDefinedlistNote().getRefDataValue();
			}
			else {
				result = getPreDefinedlistNote().getRefDataValue().concat(NOTE_TEXT_DELIMITER).concat(result);
			}
		}
		return result;
	}

	public Collection<FixtureDeftAttendingBasicValue> getFixtureDeftAttending() {
		return fixtureDeftAttending;
	}

	public void setFixtureDeftAttending(Collection<FixtureDeftAttendingBasicValue> fixtureDeftAttending) {
		this.fixtureDeftAttending = fixtureDeftAttending;
	}

	public CaseBasicValue getCase() {
		return caseBasicValue;
	}

	public void setCase(CaseBasicValue caseBasicValue) {
		this.caseBasicValue = caseBasicValue;
	}

	public DirectionsForCaseBasicValue getDirectionsForCase() {
		return directionsForCaseBasicValue;
	}

	public void setDirectionsForCase(DirectionsForCaseBasicValue directionsForCase) {
		this.directionsForCaseBasicValue = directionsForCase;
	}
}
