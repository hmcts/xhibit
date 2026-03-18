package uk.gov.courtservice.xhibit.client.casemanagement;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;

public class DeleteCaseModel {
	private CaseBasicValue caseBV = null;
	private String reasonForDeletion = null;

	public DeleteCaseModel() {
	}

	public CaseBasicValue getCaseBasicValue() {
		return caseBV;
	}

	public void setCaseBasicValue(CaseBasicValue caseBV) {
		this.caseBV = caseBV;
	}

	public void ClearModel() {
		caseBV = null;
	}

	public void setReasonForDeletion(String reasonForDeletion) {
		this.reasonForDeletion = reasonForDeletion;
	}

	public String getReasonForDeletion() {
		return reasonForDeletion;
	}
}
