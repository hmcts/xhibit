package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 * 
 * @history 25/05/2018 John Uphill Use DirectionsForCaseBasicValue and DefendantOnCaseBasicValue
 */
public class CaseComplexValue extends CaseBasicValue {

	private static final long serialVersionUID = 1L;

    // private LinkedCaseBasicValue linkedCase;
    private java.util.Collection caseProsecutorAgencies;

    private java.util.Collection charges;

    private java.util.Collection<DefendantOnCaseBasicValue> defendantOnCases;
    
    private java.util.Collection<IndictmentLogValue> indictmentLogs;
    
    private RefHearingTypeBasicValue defaultHearingTypeBasicValue;
    
    private DirectionsForCaseBasicValue directionsForCaseBasicValue;
	
	private CaseListingEntryBasicValue caseListingEntryBasicValue;

    public CaseComplexValue() {
    }

    public CaseComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    public void setCaseProsecutorAgencies(java.util.Collection caseProsecutorAgencies) {
        this.caseProsecutorAgencies = caseProsecutorAgencies;
    }

    public java.util.Collection getCaseProsecutorAgencies() {
        return caseProsecutorAgencies;
    }

    public void setCharges(java.util.Collection charges) {
        this.charges = charges;
    }

    public java.util.Collection getCharges() {
        return charges;
    }

    public void setDefendantOnCases(java.util.Collection<DefendantOnCaseBasicValue> defendantOnCases) {
        this.defendantOnCases = defendantOnCases;
    }

    public java.util.Collection<DefendantOnCaseBasicValue> getDefendantOnCases() {
        return defendantOnCases;
    }
    
    public java.util.Collection<IndictmentLogValue> getIndictmentLogs() {
        return indictmentLogs;
    }

	public RefHearingTypeBasicValue getDefaultHearingTypeBasicValue() {
		return defaultHearingTypeBasicValue;
	}

	public void setDefaultHearingTypeBasicValue(RefHearingTypeBasicValue defaultHearingTypeBasicValue) {
		this.defaultHearingTypeBasicValue = defaultHearingTypeBasicValue;
	}

	public DirectionsForCaseBasicValue getDirectionsForCase() {
		return directionsForCaseBasicValue;
	}

	public void setDirectionsForCase(DirectionsForCaseBasicValue directionsForCase) {
		this.directionsForCaseBasicValue = directionsForCase;
	}

	public CaseListingEntryBasicValue getCaseListingEntry() {
		return caseListingEntryBasicValue;
	}

	public void setCaseListingEntry(CaseListingEntryBasicValue caseListingEntryBasicValue) {
		this.caseListingEntryBasicValue = caseListingEntryBasicValue;
	}
}