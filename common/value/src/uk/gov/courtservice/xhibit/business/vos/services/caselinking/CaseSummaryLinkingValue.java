package uk.gov.courtservice.xhibit.business.vos.services.caselinking;

public class CaseSummaryLinkingValue extends CaseLinkingValue {	
	
	private static final long serialVersionUID = 1L;

	private String liveStatus; 
	private String courtFullName;
	private Integer caseListingEntryId;

	public CaseSummaryLinkingValue() {
		super();
	}

	public String getLiveStatus() {
		return liveStatus;
	}

	public void setLiveStatus(String liveStatus) {
		this.liveStatus = liveStatus;
	}

	public String getCourtFullName() {
		return courtFullName;
	}

	public void setCourtFullName(String courtFullName) {
		this.courtFullName = courtFullName;
	}
	
	public Integer getCaseListingEntryId() {
		return caseListingEntryId;
	}

	public void setCaseListingEntryId(Integer caseListingEntryId) {
		this.caseListingEntryId = caseListingEntryId;
	}
}
