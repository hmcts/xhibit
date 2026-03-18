package uk.gov.courtservice.xhibit.client.listings.details;

import java.util.ArrayList;
import java.util.Collection;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
public class CaseListingFixtureModel {

	private CaseDiaryFixtureComplexValue fixture;
	private String caseType;
	private Integer caseNumber;
	private ArrayList<RefHearingTypeBasicValue> hearingTypeArray;
	private ArrayList<CourtSiteBasicValue> courtSiteArray;
	private ArrayList<RefListingDataBasicValue> preDefinedNotesArray;
	private ArrayList<CaseDiaryFixtureComplexValue> fixturesOnCaseArray;
	private Collection<DefendantValue> defendants;
	private Integer courtId;
	private Integer caseId;
	
	public CaseListingFixtureModel(CaseDiaryFixtureComplexValue fixture,
			String caseType, 
			Integer caseNumber,
			Integer courtId, 
			Integer caseId) {

		setFixture(fixture);
		setCaseType(caseType);
		setCaseNumber(caseNumber);
		setCourtId(courtId);
		setCaseId(caseId);
		 
		clearmodel();
	}

	public void clearmodel() {
		//
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

	public CaseDiaryFixtureComplexValue getFixture() {
		return fixture;
	}

	public void setFixture(CaseDiaryFixtureComplexValue fixture) {
		this.fixture = fixture;
	}

	public boolean isEdit() {
		return (getFixture() != null && getFixture().getCaseDiaryFixtureId() != null);
	}

	public ArrayList<RefHearingTypeBasicValue> getHearingTypeArray() {
		return hearingTypeArray;
	}

	public void setHearingTypeArray(ArrayList<RefHearingTypeBasicValue> hearingTypeArray) {
		this.hearingTypeArray = hearingTypeArray;
	}
	
	public ArrayList<CourtSiteBasicValue> getCourtSiteArray() {
		return courtSiteArray;
	}

	public void setCourtSiteArray(ArrayList<CourtSiteBasicValue> courtSiteArray) {
		this.courtSiteArray = courtSiteArray;
	}

	public ArrayList<RefListingDataBasicValue> getPreDefinedNotesArray() {
		return preDefinedNotesArray;
	}

	public void setPreDefinedNotesArray(ArrayList<RefListingDataBasicValue> preDefinedNotesArray) {
		this.preDefinedNotesArray = preDefinedNotesArray;
	}
	
	public Collection<DefendantValue> getDefendants() {
		return defendants;
	}

	public void setDefendants(Collection<DefendantValue> defendants) {
		this.defendants = defendants;
	}
	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	
	public ArrayList<CaseDiaryFixtureComplexValue> getFixturesOnCase() {
		return fixturesOnCaseArray;
	}

	public void setFixturesOnCase(ArrayList<CaseDiaryFixtureComplexValue> fixturesOnCaseArray) {
		this.fixturesOnCaseArray = fixturesOnCaseArray;
	}
	
}

