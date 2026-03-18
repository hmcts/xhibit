package uk.gov.courtservice.xhibit.business.database.results;

import java.io.Serializable;

public class CaseNumber implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String caseNumber;

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	

}
