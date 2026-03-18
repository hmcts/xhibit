package uk.gov.courtservice.xhibit.client.casemanagement.caselinking;

public class LinkedCaseDetailsModel {
	private Integer caseId = 0;
	private Integer caseGroupNumber = 0;
	
	public LinkedCaseDetailsModel() {
	}
	public LinkedCaseDetailsModel(Integer caseId) {
		setCaseId(caseId);
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	public Integer getCaseId() {
		return caseId;
	}
	
	public void setCaseGroupNumber(Integer caseGroupNumber) {
		this.caseGroupNumber = caseGroupNumber;
	}
	
	public Integer getCaseGroupNumber() {
		return caseGroupNumber;
	}
}
