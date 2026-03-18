package uk.gov.courtservice.xhibit.client.listings.details;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureComplexValue;

public class CaseListingDeleteFixtureModel {

	private CaseDiaryFixtureComplexValue fixture;

	/**
	 * Constructor
	 * @param fixture CaseDiaryFixtureComplexValue
	 */
	public CaseListingDeleteFixtureModel(CaseDiaryFixtureComplexValue fixture) {
		setFixture(fixture);
	}	

	/**
	 * Retrieves the CaseDiaryFixtureComplexValue object
	 * @return	CaseDiaryFixtureComplexValue
	 */
	public CaseDiaryFixtureComplexValue getFixture() {
		return fixture;
	}

	/**
	 * Sets the CaseDiaryFixtureComplexValue object
	 * @param fixture CaseDiaryFixtureComplexValue
	 */
	public void setFixture(CaseDiaryFixtureComplexValue fixture) {
		this.fixture = fixture;
	}
}