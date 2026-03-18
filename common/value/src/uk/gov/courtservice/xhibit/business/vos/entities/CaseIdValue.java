package uk.gov.courtservice.xhibit.business.vos.entities;

import java.io.Serializable;

public class CaseIdValue implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer caseId;

	public CaseIdValue() {
		this(null);
	}

	public CaseIdValue(Integer caseId) {
		setCaseId(caseId);
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
}
