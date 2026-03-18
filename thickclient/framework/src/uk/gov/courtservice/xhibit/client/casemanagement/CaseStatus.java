package uk.gov.courtservice.xhibit.client.casemanagement;

public class CaseStatus {
	private CaseProcess caseProcess = null;
	private CaseType caseType = null;
	private boolean caseCreateInProgressFlag = false;
	private Integer caseId = 0;
	private String caseTitle;
	
	public CaseStatus() {
	}
	
	public CaseStatus(CaseProcess caseProcess, CaseType caseType) {
		setCaseProcess(caseProcess);
		setCaseType(caseType);
	}
	
	public CaseStatus(CaseProcess caseProcess, CaseType caseType, Integer caseId) {
		setCaseProcess(caseProcess);
		setCaseType(caseType);
		setCaseId(caseId);
	}
	
	public CaseProcess getCaseProcess() {
		return caseProcess;
	}
	public void setCaseProcess(CaseProcess caseProcess) {
		this.caseProcess = caseProcess;
	}
	public boolean isCaseProcess(CaseProcess caseProcess) {
		return (this.caseProcess == caseProcess);
	}

	public CaseType getCaseType() {
		return caseType;
	}
	public void setCaseType(CaseType caseType) {
		this.caseType = caseType;
	}
	public boolean isCaseType(CaseType caseType) {
		return (this.caseType == caseType);
	}
	
	public void setCaseCreateInProgressFlag(boolean caseCreateFlag) {
		caseCreateInProgressFlag = caseCreateFlag;
	}
	public boolean getCaseCreateInProgressFlag() {
		return caseCreateInProgressFlag;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	public Integer getCaseId() {
		return caseId;
	}
	
	public void setCaseTitle(String caseTitle) {
		this.caseTitle=caseTitle;
	}
	
	public String getCaseTitle() {
		return caseTitle;
	}

}
