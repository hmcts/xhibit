package uk.gov.courtservice.xhibit.business.vos.entities;

import java.io.Serializable;

public class ListingResultsInformation implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int caseId;
	private String caseNumber;
	private String caseTitle;
	private String courtSiteCode;
	private String courtRoomNo;
	private int sittingSequenceNo;
	private int isFloating;
	private String futureHearingsXcol;
	private String futureHearingsXcdf;
	private String caseStatus;
	private String hearingType;
	private String effectiveCrackedIneffective;
	private int defaultHearingType;
	private int trialTimeEstimate;
	private int trialTimeUnit;
	private int judgeId;
	private int caseListingEntryId;
	private int directionsForCaseId;
	
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
	 * @return the caseTitle
	 */
	public String getCaseTitle() {
		return caseTitle;
	}
	/**
	 * @param caseTitle the caseTitle to set
	 */
	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}
	/**
	 * @return the courtSiteCode
	 */
	public String getCourtSiteCode() {
		return courtSiteCode;
	}
	/**
	 * @param courtSiteCode the courtSiteCode to set
	 */
	public void setCourtSiteCode(String courtSiteCode) {
		this.courtSiteCode = courtSiteCode;
	}
	/**
	 * @return the courtRoomNo
	 */
	public String getCourtRoomNo() {
		return courtRoomNo;
	}
	/**
	 * @param courtRoomNo the courtRoomNo to set
	 */
	public void setCourtRoomNo(String courtRoomNo) {
		this.courtRoomNo = courtRoomNo;
	}
	/**
	 * @return the futureHearingsXcol
	 */
	public String getFutureHearingsXcol() {
		return futureHearingsXcol;
	}
	/**
	 * @param futureHearingsXcol the futureHearingsXcol to set
	 */
	public void setFutureHearingsXcol(String futureHearingsXcol) {
		this.futureHearingsXcol = futureHearingsXcol;
	}
	/**
	 * @return the caseStatus
	 */
	public String getCaseStatus() {
		return caseStatus;
	}
	/**
	 * @param caseStatus the caseStatus to set
	 */
	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}
	/**
	 * @return the hearingType
	 */
	public String getHearingType() {
		return hearingType;
	}
	/**
	 * @param hearingType the hearingType to set
	 */
	public void setHearingType(String hearingType) {
		this.hearingType = hearingType;
	}
	/**
	 * @return the effectiveCrackedIneffective
	 */
	public String getEffectiveCrackedIneffective() {
		return effectiveCrackedIneffective;
	}
	/**
	 * @param effectiveCrackedIneffective the effectiveCrackedIneffective to set
	 */
	public void setEffectiveCrackedIneffective(String effectiveCrackedIneffective) {
		this.effectiveCrackedIneffective = effectiveCrackedIneffective;
	}
	/**
	 * @return the caseId
	 */
	public int getCaseId() {
		return caseId;
	}
	/**
	 * @param caseId the caseId to set
	 */
	public void setCaseId(int caseId) {
		this.caseId = caseId;
	}
	/**
	 * @return the sittingSequenceNo
	 */
	public int getSittingSequenceNo() {
		return sittingSequenceNo;
	}
	/**
	 * @param sittingSequenceNo the sittingSequenceNo to set
	 */
	public void setSittingSequenceNo(int sittingSequenceNo) {
		this.sittingSequenceNo = sittingSequenceNo;
	}
	/**
	 * @return the isFloating
	 */
	public int getIsFloating() {
		return isFloating;
	}
	/**
	 * @param isFloating the isFloating to set
	 */
	public void setIsFloating(int isFloating) {
		this.isFloating = isFloating;
	}
	/**
	 * @return the futureHearingsXcdf
	 */
	public String getFutureHearingsXcdf() {
		return futureHearingsXcdf;
	}
	/**
	 * @param futureHearingsXcdf the futureHearingsXcdf to set
	 */
	public void setFutureHearingsXcdf(String futureHearingsXcdf) {
		this.futureHearingsXcdf = futureHearingsXcdf;
	}
	/**
	 * @return the defaultHearingType
	 */
	public int getDefaultHearingType() {
		return defaultHearingType;
	}
	/**
	 * @param defaultHearingType the defaultHearingType to set
	 */
	public void setDefaultHearingType(int defaultHearingType) {
		this.defaultHearingType = defaultHearingType;
	}
	/**
	 * @return the trialTimeEstimate
	 */
	public int getTrialTimeEstimate() {
		return trialTimeEstimate;
	}
	/**
	 * @param trialTimeEstimate the trialTimeEstimate to set
	 */
	public void setTrialTimeEstimate(int trialTimeEstimate) {
		this.trialTimeEstimate = trialTimeEstimate;
	}
	/**
	 * @return the trialTimeUnit
	 */
	public int getTrialTimeUnit() {
		return trialTimeUnit;
	}
	/**
	 * @param trialTimeUnit the trialTimeUnit to set
	 */
	public void setTrialTimeUnit(int trialTimeUnit) {
		this.trialTimeUnit = trialTimeUnit;
	}
	/**
	 * @return the judgeId
	 */
	public int getJudgeId() {
		return judgeId;
	}
	/**
	 * @param judgeId the judgeId to set
	 */
	public void setJudgeId(int judgeId) {
		this.judgeId = judgeId;
	}
	/**
	 * @return the caseListingEntryId
	 */
	public int getCaseListingEntryId() {
		return caseListingEntryId;
	}
	/**
	 * @param caseListingEntryId the caseListingEntryId to set
	 */
	public void setCaseListingEntryId(int caseListingEntryId) {
		this.caseListingEntryId = caseListingEntryId;
	}
	/**
	 * @return the directionsForCaseId
	 */
	public int getDirectionsForCaseId() {
		return directionsForCaseId;
	}
	/**
	 * @param directionsForCaseId the directionsForCaseId to set
	 */
	public void setDirectionsForCaseId(int directionsForCaseId) {
		this.directionsForCaseId = directionsForCaseId;
	}
	
}
