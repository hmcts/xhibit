package uk.gov.courtservice.xhibit.common.results.vos;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class ADJSSValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;
	private String caseNumber;
	private String defendantName;
	private String judgeName;
	private String hearingDate;
	private String hearingAdjournedDate;
	private String ptiUrn;
	private String reason;
	private String listDate;
	/**
	 * @return the caseNumber
	 */
	public String getCaseNumber() {
		return caseNumber;
	}
	/**
	 * @param caseNumber the caseNumber to set
	 */
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	/**
	 * @return the defendantName
	 */
	public String getDefendantName() {
		return defendantName;
	}
	/**
	 * @param defendantName the defendantName to set
	 */
	public void setDefendantName(String defendantName) {
		this.defendantName = defendantName;
	}
	/**
	 * @return the judgeName
	 */
	public String getJudgeName() {
		return judgeName;
	}
	/**
	 * @param judgeName the judgeName to set
	 */
	public void setJudgeName(String judgeName) {
		this.judgeName = judgeName;
	}
	/**
	 * @return the hearingDate
	 */
	public String getHearingDate() {
		return hearingDate;
	}
	/**
	 * @param hearingStartDate the hearingStartDate to set
	 */
	public void setHearingDate(String hearingDate) {
		this.hearingDate = hearingDate;
	}
	/**
	 * @return the hearingEndDate
	 */
	public String getHearingAdjournedDate() {
		return hearingAdjournedDate;
	}
	/**
	 * @param hearingEndDate the hearingEndDate to set
	 */
	public void setHearingAdjournedDate(String hearingAdjournedDate) {
		this.hearingAdjournedDate = hearingAdjournedDate;
	}
	
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getListDate() {
		return listDate;
	}
	public void setListDate(String listDate) {
		this.listDate = listDate;
	}
	public String getPtiUrn() {
		return ptiUrn;
	}
	public void setPtiUrn(String ptiUrn) {
		this.ptiUrn = ptiUrn;
	}

}
