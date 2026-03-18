package uk.gov.courtservice.xhibit.common.results.vos;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class PRLISCaseValue extends CSAbstractValue {
	private static final long serialVersionUID = 1L;
	private String caseNumber;
	private String caseType;
	private String defandantSurname;
	private String defendantFirstName;
	private String defendantMiddleName;
	private String defendantGender;
	private String defendantDateOfBirth;
	private String dateOfHearing;
	private String solicitorName;
	private String solicitorPhoneNumber;
	private String bcStatus;
	private String prosecutorName;
	private String ptiurn;
	private String classCode;
	private String sentForTrialDate;
	private String committalDate;
	private String appealLodgedDate;
	private String charges;
	private Integer caseId;
	private String defendantNumberText;
	private Integer firstDefNo;
	private Integer currentDefNo;
	private String magistratesCourtName;
	private String magistratesTransferredCourtName;
	private String transferredDate;

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
	 * @return the caseType
	 */
	public String getCaseType() {
		return caseType;
	}
	/**
	 * @param caseType the caseType to set
	 */
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	/**
	 * @return the defandantSurname
	 */
	public String getDefandantSurname() {
		return defandantSurname;
	}
	/**
	 * @param defandantSurname the defandantSurname to set
	 */
	public void setDefandantSurname(String defandantSurname) {
		this.defandantSurname = defandantSurname;
	}
	/**
	 * @return the defendantFirstName
	 */
	public String getDefendantFirstName() {
		return defendantFirstName;
	}
	/**
	 * @param defendantFirstName the defendantFirstName to set
	 */
	public void setDefendantFirstName(String defendantFirstName) {
		this.defendantFirstName = defendantFirstName;
	}
	/**
	 * @return the defendantMiddleName
	 */
	public String getDefendantMiddleName() {
		return defendantMiddleName;
	}
	/**
	 * @param defendantMiddleName the defendantMiddleName to set
	 */
	public void setDefendantMiddleName(String defendantMiddleName) {
		this.defendantMiddleName = defendantMiddleName;
	}
	/**
	 * @return the defendantGender
	 */
	public String getDefendantGender() {
		return defendantGender;
	}
	/**
	 * @param defendantGender the defendantGender to set
	 */
	public void setDefendantGender(String defendantGender) {
		this.defendantGender = defendantGender;
	}
	/**
	 * @return the defendantDateOfBirth
	 */
	public String getDefendantDateOfBirth() {
		return defendantDateOfBirth;
	}
	/**
	 * @param defendantDateOfBirth the defendantDateOfBirth to set
	 */
	public void setDefendantDateOfBirth(String defendantDateOfBirth) {
		this.defendantDateOfBirth = defendantDateOfBirth;
	}
	/**
	 * @return the dateOfHearing
	 */
	public String getDateOfHearing() {
		return dateOfHearing;
	}
	/**
	 * @param dateOfHearing the dateOfHearing to set
	 */
	public void setDateOfHearing(String dateOfHearing) {
		this.dateOfHearing = dateOfHearing;
	}
	/**
	 * @return the solicitorName
	 */
	public String getSolicitorName() {
		return solicitorName;
	}
	/**
	 * @param solicitorName the solicitorName to set
	 */
	public void setSolicitorName(String solicitorName) {
		this.solicitorName = solicitorName;
	}
	/**
	 * @return the solicitorPhoneNumber
	 */
	public String getSolicitorPhoneNumber() {
		return solicitorPhoneNumber;
	}
	/**
	 * @param solicitorPhoneNumber the solicitorPhoneNumber to set
	 */
	public void setSolicitorPhoneNumber(String solicitorPhoneNumber) {
		this.solicitorPhoneNumber = solicitorPhoneNumber;
	}
	/**
	 * @return the bcStatus
	 */
	public String getBcStatus() {
		return bcStatus;
	}
	/**
	 * @param bcStatus the bcStatus to set
	 */
	public void setBcStatus(String bcStatus) {
		this.bcStatus = bcStatus;
	}
	/**
	 * @return the prosecutorName
	 */
	public String getProsecutorName() {
		return prosecutorName;
	}
	/**
	 * @param prosecutorName the prosecutorName to set
	 */
	public void setProsecutorName(String prosecutorName) {
		this.prosecutorName = prosecutorName;
	}
	/**
	 * @return the ptiurn
	 */
	public String getPtiurn() {
		return ptiurn;
	}
	
	/**
	 * @param ptiurn the ptiurn to set
	 */
	public void setPtiurn(String ptiurn) {
		this.ptiurn = ptiurn;
	}
	
	/**
	 * @return the classCode
	 */
	public String getClassCode() {
		return classCode;
	}
	/**
	 * @param classCode the classCode to set
	 */
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	/**
	 * @return the sentForTrialDate
	 */
	public String getSentForTrialDate() {
		return sentForTrialDate;
	}
	/**
	 * @param sentForTrialDate the sentForTrialDate to set
	 */
	public void setSentForTrialDate(String sentForTrialDate) {
		this.sentForTrialDate = sentForTrialDate;
	}
	/**
	 * @return the committalDate
	 */
	public String getCommittalDate() {
		return committalDate;
	}
	/**
	 * @param committalDate the committalDate to set
	 */
	public void setCommittalDate(String committalDate) {
		this.committalDate = committalDate;
	}
	/**
	 * @return the appealLodgedDate
	 */
	public String getAppealLodgedDate() {
		return appealLodgedDate;
	}
	/**
	 * @param appealLodgedDate the appealLodgedDate to set
	 */
	public void setAppealLodgedDate(String appealLodgedDate) {
		this.appealLodgedDate = appealLodgedDate;
	}
	/**
	 * @return the charges
	 */
	public String getCharges() {
		return charges;
	}
	/**
	 * @param charges the charges to set
	 */
	public void setCharges(String charges) {
		this.charges = charges;
	}
	public Integer getCaseId() {
		return caseId;
	}
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	/**
	 * @return the defendantNumberText
	 */
	public String getDefendantNumberText() {
		return defendantNumberText;
	}
	/**
	 * @param defendantNumberText the defendantNumberText to set
	 */
	public void setDefendantNumberText(String defendantNumberText) {
		this.defendantNumberText = defendantNumberText;
	}
	/**
	 * @return the firstDefNo
	 */
	public Integer getFirstDefNo() {
		return firstDefNo;
	}
	/**
	 * @param firstDefNo the firstDefNo to set
	 */
	public void setFirstDefNo(Integer firstDefNo) {
		this.firstDefNo = firstDefNo;
	}
	/**
	 * @return the currentDefNo
	 */
	public Integer getCurrentDefNo() {
		return currentDefNo;
	}
	/**
	 * @param currentDefNo the currentDefNo to set
	 */
	public void setCurrentDefNo(Integer currentDefNo) {
		this.currentDefNo = currentDefNo;
	}
	
	public String getMagistratesCourtName(){
		return magistratesCourtName;
	}
	
	public void setMagistratesCourtName(String magistratesCourtName) {
		this.magistratesCourtName = magistratesCourtName;	
	}
	public String getMagistratesTransferredCourtName() {
		return magistratesTransferredCourtName;
	}
	public void setMagistratesTransferredCourtName(String magistratesTransferredCourtName) {
		this.magistratesTransferredCourtName = magistratesTransferredCourtName;
	}
	public String getTransferredDate() {
		return transferredDate;
	}
	public void setTransferredDate(String transferredDate) {
		this.transferredDate = transferredDate;
	}
}
