package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: DefHearingRecordBasicValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the
 * DefHearingRecord enitity CMP fields.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Khanh Tran
 * @version 1.0
 */
public class DefHearingRecordBasicValue extends CSAbstractValue {
	private Integer refAdjournmentID;

	private Date adjournedDate;

	private Date startDateNewBailStatus;

	private String newBailStatus;

	private Date dateBailApplication;

	private String substBailApplication;

	private Integer refDefHearingTypeID;

	private String endBailStatus;

	private String startBailStatus;

	private Integer defendantOnCaseID;

	private Integer hearingID;

	private String resultBailApplication;

	private String hearingDateFreetext1;

	private String hearingDateFreetext2;

	private String hearingDateFreetext3;

	private String isAdjourned;

	private String isHraApplication;

	private String oralEvidence;

	private Date hearingStartDate;

	private Date hearingEndDate;

	private Long lastCalculatedDuration;

	private String mpHearingType;

	private String trialInDefAbsence;

	private String sentenceInDefAbsence;

	private String formAStatus;

	private String formACourtClerk;
	
	private String s41Application;
	
	private String s41Granted;	
	
	private String s41ApplicationMade;
	
	private static final long serialVersionUID = 3730792904018250874L;

	public String getMpHearingType() {
		return mpHearingType;
	}

	public void setMpHearingType(String mpHearingType) {
		this.mpHearingType = mpHearingType;
	}

	public DefHearingRecordBasicValue() {
		super();
	}

	public DefHearingRecordBasicValue(Integer version) {
		super(version);
	}

	public DefHearingRecordBasicValue(Integer hearingRecordID, Integer version) {
		super(hearingRecordID, version);
	}

	public void setRefAdjournmentID(Integer refAdjournmentID) {
		this.refAdjournmentID = refAdjournmentID;
	}

	public Integer getRefAdjournmentID() {
		return refAdjournmentID;
	}

	public void setAdjournedDate(Date adjournedDate) {
		this.adjournedDate = adjournedDate;
	}

	public Date getAdjournedDate() {
		return adjournedDate;
	}

	public void setStartDateNewBailStatus(Date startDateNewBailStatus) {
		this.startDateNewBailStatus = startDateNewBailStatus;
	}

	public Date getStartDateNewBailStatus() {
		return startDateNewBailStatus;
	}

	public void setNewBailStatus(String newBailStatus) {
		this.newBailStatus = newBailStatus;
	}

	public String getNewBailStatus() {
		return newBailStatus;
	}

	public void setDateBailApplication(Date dateBailApplication) {
		this.dateBailApplication = dateBailApplication;
	}

	public Date getDateBailApplication() {
		return dateBailApplication;
	}

	public void setSubstBailApplication(String substBailApplication) {
		this.substBailApplication = substBailApplication;
	}

	public String getSubstBailApplication() {
		return substBailApplication;
	}

	public void setRefDefHearingTypeID(Integer refDefHearingTypeID) {
		this.refDefHearingTypeID = refDefHearingTypeID;
	}

	public Integer getRefDefHearingTypeID() {
		return refDefHearingTypeID;
	}

	public void setEndBailStatus(String endBailStatus) {
		this.endBailStatus = endBailStatus;
	}

	public void setStartBailStatus(String startBailStatus) {
		this.startBailStatus = startBailStatus;
	}

	public void setDefendantOnCaseID(Integer defendantOnCaseID) {
		this.defendantOnCaseID = defendantOnCaseID;
	}

	public void setHearingID(Integer hearingID) {
		this.hearingID = hearingID;
	}

	public String getEndBailStatus() {
		return endBailStatus;
	}

	public String getStartBailStatus() {
		return startBailStatus;
	}

	public Integer getDefendantOnCaseID() {
		return defendantOnCaseID;
	}

	public Integer getHearingID() {
		return hearingID;
	}

	public void setResultBailApplication(String resultBailApplication) {
		this.resultBailApplication = resultBailApplication;
	}

	public String getResultBailApplication() {
		return resultBailApplication;
	}

	public void setHearingDateFreetext1(String hearingDateFreetext1) {
		this.hearingDateFreetext1 = hearingDateFreetext1;
	}

	public String getHearingDateFreetext1() {
		return hearingDateFreetext1;
	}

	public void setHearingDateFreetext2(String hearingDateFreetext2) {
		this.hearingDateFreetext2 = hearingDateFreetext2;
	}

	public String getHearingDateFreetext2() {
		return hearingDateFreetext2;
	}

	public void setHearingDateFreetext3(String hearingDateFreetext3) {
		this.hearingDateFreetext3 = hearingDateFreetext3;
	}

	public String getHearingDateFreetext3() {
		return hearingDateFreetext3;
	}

	public void setIsAdjourned(String isAdjourned) {
		this.isAdjourned = isAdjourned;
	}

	public String getIsAdjourned() {
		return isAdjourned;
	}

	public void setIsHraApplication(String isHraApplication) {
		this.isHraApplication = isHraApplication;
	}

	public String getIsHraApplication() {
		return isHraApplication;
	}

	public void setOralEvidence(String oralEvidence) {
		this.oralEvidence = oralEvidence;
	}

	public String getOralEvidence() {
		return oralEvidence;
	}

	public void setHearingStartDate(Date hearingStartDate) {
		this.hearingStartDate = hearingStartDate;
	}

	public Date getHearingStartDate() {
		return this.hearingStartDate;
	}

	public void setHearingEndDate(Date hearingEndDate) {
		this.hearingEndDate = hearingEndDate;
	}

	public Date getHearingEndDate() {
		return this.hearingEndDate;
	}

	public void setLastCalculatedDuration(Long lastCalculatedDuration) {
		this.lastCalculatedDuration = lastCalculatedDuration;
	}

	public Long getLastCalculatedDuration() {
		return this.lastCalculatedDuration;
	}

	public void setTrialInDefAbsence(String trialInDefAbsence) {
		this.trialInDefAbsence = trialInDefAbsence;
	}

	public String getTrialInDefAbsence() {
		return trialInDefAbsence;
	}

	public void setSentenceInDefAbsence(String sentenceInDefAbsence) {
		this.sentenceInDefAbsence = sentenceInDefAbsence;
	}

	public String getSentenceInDefAbsence() {
		return sentenceInDefAbsence;
	}

	public void setFormAStatus(String formAStatus) {
		this.formAStatus = formAStatus;
	}

	public String getFormAStatus() {
		return formAStatus;
	}

	public void setFormACourtClerk(String formACourtClerk) {
		this.formACourtClerk = formACourtClerk;
	}

	public String getFormACourtClerk() {
		return formACourtClerk;
	}
	
	
	/**
	 * @return the s41Application
	 */
	public String getS41Application() {
		return s41Application;
	}

	/**
	 * @param s41Application the s41Application to set
	 */
	public void setS41Application(String s41Application) {
		this.s41Application = s41Application;
	}

	/**
	 * @return the s41Granted
	 */
	public String getS41Granted() {
		return s41Granted;
	}

	/**
	 * @param s41Granted the s41Granted to set
	 */
	public void setS41Granted(String s41Granted) {
		this.s41Granted = s41Granted;
	}

	/**
	 * @return the s41ApplicationMade
	 */
	public String getS41ApplicationMade() {
		return s41ApplicationMade;
	}

	/**
	 * @param s41ApplicationMade the s41ApplicationMade to set
	 */
	public void setS41ApplicationMade(String s41ApplicationMade) {
		this.s41ApplicationMade = s41ApplicationMade;
	}

}
