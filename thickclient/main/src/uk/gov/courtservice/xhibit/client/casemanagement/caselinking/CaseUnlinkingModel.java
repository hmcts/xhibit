package uk.gov.courtservice.xhibit.client.casemanagement.caselinking;

public class CaseUnlinkingModel {
	private Integer caseId = 0;
	
	public CaseUnlinkingModel() {
	}
	public CaseUnlinkingModel(Integer caseId) {
		setCaseId(caseId);
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	public Integer getCaseId() {
		return caseId;
	}
	
}
