package uk.gov.courtservice.xhibit.client.casemanagement.caselinking;

public class CaseLinkingModel {
	private Integer caseId = 0;
	
	private boolean caseSearched = false; 
	private boolean caseLinked = false;
	
	public CaseLinkingModel() {
	}
	public CaseLinkingModel(Integer caseId) {
		setCaseId(caseId);
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	public Integer getCaseId() {
		return caseId;
	}
	public boolean isCaseSearched() {
		return caseSearched;
	}
	public void setCaseSearched(boolean caseSearched) {
		this.caseSearched = caseSearched;
	}
	public boolean isCaseLinked() {
		return caseLinked;
	}
	public void setCaseLinked(boolean caseLinked) {
		this.caseLinked = caseLinked;
	}
	
	
}
