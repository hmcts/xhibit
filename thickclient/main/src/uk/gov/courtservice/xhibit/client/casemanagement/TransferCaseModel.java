package uk.gov.courtservice.xhibit.client.casemanagement;

public class TransferCaseModel {
	private Integer caseId = 0;
	
	public TransferCaseModel() {
	}
	public TransferCaseModel(Integer caseId) {
		setCaseId(caseId);
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	public Integer getCaseId() {
		return caseId;
	}
	
}
