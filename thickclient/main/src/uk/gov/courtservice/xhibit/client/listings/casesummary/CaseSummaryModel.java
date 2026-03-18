package uk.gov.courtservice.xhibit.client.listings.casesummary;

import java.util.Date;
import java.util.List;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DarRetentionPolicyComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.caseprosecutoragency.CaseProsecutorAgencyValue;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseStatus;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseType;

public class CaseSummaryModel {

    public static interface ValidValues {
        public static final List<String> CASE_TYPES = CaseType.CaseListingCaseTypes();       
    }
	private Integer caseId = 0;
	private CaseStatus caseStatus;
	private String liveStatus;
	private CaseListingEntryComplexValue caseListingEntry;
	private List<CaseProsecutorAgencyValue> caseProsecutors;
	private CourtSiteBasicValue courtSite;
	private DarRetentionPolicyComplexValue dartsRetentionPolicy;
	private boolean fromListScreen;
	private boolean dataChanged = false;
	private Date dvrReleaseDate;

	public CaseSummaryModel(Integer caseId) {
		this.caseId = caseId;
		setFromListScreen(false);
	}

	/**
	 * @return the caseId
	 */
	public Integer getCaseId() {
		return caseId;
	}

	/**
	 * @param caseId the caseId to set
	 */
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	
	public CaseBasicValue getCase()
	{
		return this.caseListingEntry.getCaseBasicValue();
	}

	public void setCaseStatus(CaseStatus caseStatus) {
		this.caseStatus = caseStatus;
	}
	
	public CaseStatus getCaseStatus()
	{
		return this.caseStatus;
	}

	public String getLiveStatus() {
		return liveStatus;
	}

	public void setLiveStatus(String liveStatus) {
		this.liveStatus = liveStatus;
	}

	public void setCaseListingEntry(CaseListingEntryComplexValue caseListingEntry) {
		this.caseListingEntry = caseListingEntry;
	}
	
	public CaseListingEntryComplexValue getCaseListingEntry()
	{
		return this.caseListingEntry;
	}

	public void setCaseProsecutors(List<CaseProsecutorAgencyValue> caseProsecutors) {
		this.caseProsecutors = caseProsecutors;
	}
	
	public List<CaseProsecutorAgencyValue> getCaseProsecutors(){
		return this.caseProsecutors;
	}

	public boolean isDefendantTabRequired() {
		return getCaseStatus().isCaseType(CaseType.SENTENCE) || getCaseStatus().isCaseType(CaseType.TRIAL);
	}
	
	public boolean isAppellantTabRequired() {
		return getCaseStatus().isCaseType(CaseType.APPEAL) || getCaseStatus().isCaseType(CaseType.MISC);
	}

	public CourtSiteBasicValue getCourtSite() {
		return courtSite;
	}

	public void setCourtSite(CourtSiteBasicValue courtSite) {
		this.courtSite = courtSite;
	}

	public boolean isFromListScreen() {
		return fromListScreen;
	}

	public void setFromListScreen(boolean fromListScreen) {
		this.fromListScreen = fromListScreen;
	}
	
	public boolean isDataChanged() {
		return dataChanged;
	}

	public void setDataChanged(boolean dataChanged) {
		this.dataChanged = dataChanged;
	}

	public DarRetentionPolicyComplexValue getDartsRetentionPolicy() {
		return dartsRetentionPolicy;
	}

	public void setDartsRetentionPolicy(DarRetentionPolicyComplexValue dartsRetentionPolicy) {
		this.dartsRetentionPolicy = dartsRetentionPolicy;
	}
	
	public Date getDvrReleaseDate() {
		return dvrReleaseDate;
	}

	public void setDvrReleaseDate(Date dvrReleaseDate) {
		this.dvrReleaseDate = dvrReleaseDate;
	}

	public boolean getDartsRetentionPolicyhasLife() {
		return "Y".equals(getDartsRetentionPolicy().getHasLife());
	}
	
	public Integer getDartsRetentionPolicyDurationYears() {
		return getDartsRetentionPolicyhasLife() ? 0 : getDartsRetentionPolicy().getDurationYears();
	}

	public Integer getDartsRetentionPolicyDurationMonths() {
		return getDartsRetentionPolicyhasLife() ? 0 : getDartsRetentionPolicy().getDurationMonths();
	}

	public Integer getDartsRetentionPolicyDurationDays() {
		return getDartsRetentionPolicyhasLife() ? 0 : getDartsRetentionPolicy().getDurationDays();
	}
}
