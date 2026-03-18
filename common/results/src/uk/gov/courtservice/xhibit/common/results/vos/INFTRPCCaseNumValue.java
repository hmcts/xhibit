package uk.gov.courtservice.xhibit.common.results.vos;

import java.io.Serializable;

public class INFTRPCCaseNumValue implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String caseNumber;
	private String code;
	private String description;
	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}


}
