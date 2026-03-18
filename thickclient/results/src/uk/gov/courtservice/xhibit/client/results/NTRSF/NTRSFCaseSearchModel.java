package uk.gov.courtservice.xhibit.client.results.NTRSF;

import java.util.List;

import uk.gov.courtservice.xhibit.client.casemanagement.CaseType;

public class NTRSFCaseSearchModel {
	
	public static interface ValidValues {
        public static final List<String> CASE_TYPES = CaseType.CaseListingCaseTypes();       
    }
	private Integer caseId = 0;
	private String caseType = "";
	private Integer caseNumber = 0;
	private Integer courtId = 0;
	private String caseTitle = "";
	
	private boolean cancelled = false; // If true search was cancelled
	private boolean ok = false; // If true ok button was pressed
	
	private List<String> validCaseTypes;

	public NTRSFCaseSearchModel() {
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public Integer getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(Integer caseNumber) {
		this.caseNumber = caseNumber;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public List<String> getValidCaseTypes() {
		return validCaseTypes;
	}

	public void setValidCaseTypes(List<String> validCaseTypes) {
		this.validCaseTypes = validCaseTypes;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public boolean getCancelled() {
		return cancelled;
	}

	public boolean getOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String getCaseTitle() {
		return caseTitle;
	}

	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}

}
