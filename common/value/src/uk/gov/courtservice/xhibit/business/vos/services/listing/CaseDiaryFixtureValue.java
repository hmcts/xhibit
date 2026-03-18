package uk.gov.courtservice.xhibit.business.vos.services.listing;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureComplexValue;

/**
 * <p>
 * Title: CaseDiaryFixtureValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Ervin
 * @version 1.0
 */

public class CaseDiaryFixtureValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private CaseDiaryFixtureComplexValue fixture;
	private Integer caseId;
	private Integer courtId;

	/* ctx-2625 - kudzinc used for REDEL */
	private Integer caseDiaryFixtureId;
	private Integer caseListingEntryId;
	private Integer hearingTypeId;
	private String obsInd;

	public CaseDiaryFixtureValue(CaseDiaryFixtureComplexValue fixture, Integer courtId, Integer caseId) {
		setFixture(fixture);
		setCaseId(caseId);
		setCourtId(courtId);
	}

	public CaseDiaryFixtureComplexValue getFixture() {
		return fixture;
	}

	public void setFixture(CaseDiaryFixtureComplexValue fixture) {
		this.fixture = fixture;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	/* ctx-2625 - kudzinc used for REDEL */
	public Integer getCaseDiaryFixtureId() {
		return caseDiaryFixtureId;
	}

	public void setCaseDiaryFixtureId(Integer caseDiaryFixtureId) {
		this.caseDiaryFixtureId = caseDiaryFixtureId;
	}

	public Integer getCaseListingEntryId() {
		return caseListingEntryId;
	}

	public void setCaseListingEntryId(Integer caseListingEntryId) {
		this.caseListingEntryId = caseListingEntryId;
	}

	public Integer getHearingTypeId() {
		return hearingTypeId;
	}

	public void setHearingTypeId(Integer hearingTypeId) {
		this.hearingTypeId = hearingTypeId;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}
}
