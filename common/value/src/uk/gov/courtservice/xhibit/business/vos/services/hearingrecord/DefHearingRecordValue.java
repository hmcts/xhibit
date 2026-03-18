package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: This is the value object that represents the DefHearingRecord
 * entity. The values come from those on the DefHearingRecord in Hearing CMR,
 * this is the Hearing Record version of that VO; this enables the data
 * manipulation to remain unchanged when the data schemas are evolved.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Anthony Martin / Marie Holmberg
 * @version 1.0
 */
public class DefHearingRecordValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	// private Integer defHearingRecordID;
	private Integer hearingID;

	private String startBailStatus;

	private String endBailStatus;

	private String hearingDateFreetext1;

	private String hearingDateFreetext2;

	private String hearingDateFreetext3;

	private Integer defendantOnCaseID;

	private Integer refDefHearingTypeID;

	private String refDefHearingTypeDesc;

	private String isHraApplication;

	private String resultBailApplication;

	private String oralEvidence;

	private String substBailApplication;

	private Date dateBailApplication;

	private String newBailStatus;

	private Date startDateNewBailStatus;

	private String isAdjourned;

	private Date adjournedDate;

	private Integer refAdjournmentID;

	private String refAdjournmentDesc;

	private String mpHearingType;

	private String trialInDefAbsence;
	private String sentenceInDefAbsence;

	private String formAStatus;
	private String formACourtClerk;
	
	private String s41Application;
	private String s41Granted;
	private String s41ApplicationMade;

	// removed from no-arguments constructor (which appears wrong anyway)
	// and
	// put here. This automatically creates the empty property map, so it is
	// not required
	private final CourtLogCRUDValue courtLogCRUDValue = new CourtLogCRUDValue();

	// end hearing changes, require end hearing to be defendant level...
	private Date hearingStartDate;

	private Date hearingEndDate;

	private Long lastCalculatedDuration;

	private boolean defaultHearingType = false;

	/**
	 * Constructor that takes the defHearingId and the version as arguments.
	 * This to ensure the Optimistic locking.
	 * 
	 * @param defHearingRecordID
	 * @param version
	 */
	public DefHearingRecordValue(Integer defHearingRecordID, Integer version) {
		super(defHearingRecordID, version);
	}

	public DefHearingRecordValue() {
		/*
		 * //construct the CourtLogCRUDVAlue CourtLogCRUDValue courtLogCRUDValue
		 * = new CourtLogCRUDValue(); //give it an empty map to start with
		 * HashMap hashMap = new HashMap();
		 * courtLogCRUDValue.setPropertyMap(hashMap);
		 */
	}

	// getters
	public Integer getHearingID() {
		return hearingID;
	}

	public String getStartBailStatus() {
		return startBailStatus;
	}

	public Date getAdjournedDate() {
		return adjournedDate;
	}

	public Date getDateBailApplication() {
		return dateBailApplication;
	}

	public Integer getDefendantOnCaseID() {
		return defendantOnCaseID;
	}

	public String getIsAdjourned() {
		return isAdjourned;
	}

	public String getOralEvidence() {
		return oralEvidence;
	}

	public String getNewBailStatus() {
		return newBailStatus;
	}

	public String getResultBailApplication() {
		return resultBailApplication;
	}

	public Date getStartDateNewBailStatus() {
		return startDateNewBailStatus;
	}

	public String getSubstBailApplication() {
		return substBailApplication;
	}

	public String getIsHraApplication() {
		return isHraApplication;
	}

	public String getRefDefHearingTypeDesc() {
		return refDefHearingTypeDesc;
	}

	public Integer getRefDefHearingTypeID() {
		return refDefHearingTypeID;
	}

	public String getRefAdjournmentDesc() {
		return refAdjournmentDesc;
	}

	public Integer getRefAdjournmentID() {
		return refAdjournmentID;
	}

	public String getHearingDateFreetext1() {
		return hearingDateFreetext1;
	}

	public String getHearingDateFreetext2() {
		return hearingDateFreetext2;
	}

	public String getHearingDateFreetext3() {
		return hearingDateFreetext3;
	}

	public String getEndBailStatus() {
		return endBailStatus;
	}

	public Date getHearingStartDate() {
		return this.hearingStartDate;
	}

	public Date getHearingEndDate() {
		return this.hearingEndDate;
	}

	public Long getLastCalculatedDuration() {
		return this.lastCalculatedDuration;
	}

	public String getMpHearingType() {
		return this.mpHearingType;
	}
	
	public String getS41Application() {
		return this.s41Application;
	}
	public String getS41Granted() {
		return this.s41Granted;
	}
	public String getS41ApplicationMade() {
		return this.s41ApplicationMade;
	}

	// setters
	public void setHearingID(Integer id) {
		this.hearingID = id;
	}

	public void setEndBailStatus(String status) {
		this.endBailStatus = status;
	}

	public void setStartBailStatus(String status) {
		this.startBailStatus = status;
	}

	public void setAdjournedDate(Date adjournedDate) {
		this.adjournedDate = adjournedDate;
	}

	public void setDateBailApplication(Date dateBailApplication) {
		this.dateBailApplication = dateBailApplication;
	}

	public void setDefendantOnCaseID(Integer defendantOnCaseID) {
		this.defendantOnCaseID = defendantOnCaseID;
	}

	public void setIsAdjourned(String isAdjourned) {
		this.isAdjourned = isAdjourned;
	}

	public void setNewBailStatus(String newBailStatus) {
		this.newBailStatus = newBailStatus;
	}

	public void setOralEvidence(String oralEvidence) {
		this.oralEvidence = oralEvidence;
	}

	public void setResultBailApplication(String resultBailApplication) {
		this.resultBailApplication = resultBailApplication;
	}

	public void setStartDateNewBailStatus(Date startDateNewBailStatus) {
		this.startDateNewBailStatus = startDateNewBailStatus;
	}

	public void setSubstBailApplication(String substBailApplication) {
		this.substBailApplication = substBailApplication;
	}

	public void setIsHraApplication(String isHraApplication) {
		this.isHraApplication = isHraApplication;
	}

	public void setRefDefHearingTypeDesc(String refDefHearingTypeDesc) {
		this.refDefHearingTypeDesc = refDefHearingTypeDesc;
	}

	public void setRefDefHearingTypeID(Integer refDefHearingTypeID) {
		this.refDefHearingTypeID = refDefHearingTypeID;
	}

	public void setRefAdjournmentDesc(String refAdjournmentDesc) {
		this.refAdjournmentDesc = refAdjournmentDesc;
	}

	public void setRefAdjournmentID(Integer refAdjournmentID) {
		this.refAdjournmentID = refAdjournmentID;
	}

	public void setHearingDateFreetext1(String hearingDateFreetext1) {
		this.hearingDateFreetext1 = hearingDateFreetext1;
	}

	public void setHearingDateFreetext2(String hearingDateFreetext2) {
		this.hearingDateFreetext2 = hearingDateFreetext2;
	}

	public void setHearingDateFreetext3(String hearingDateFreetext3) {
		this.hearingDateFreetext3 = hearingDateFreetext3;
	}

	public void setHearingStartDate(Date hearingStartDate) {
		this.hearingStartDate = hearingStartDate;
	}

	public void setHearingEndDate(Date hearingEndDate) {
		this.hearingEndDate = hearingEndDate;
	}

	public void setLastCalculatedDuration(Long lastCalculatedDuration) {
		this.lastCalculatedDuration = lastCalculatedDuration;
	}

	public void setMpHearingType(String mpHearingType) {
		this.mpHearingType = mpHearingType;
	}

	public void setS41Application(String s41Application) {
		this.s41Application = s41Application;
	}
	public void setS41Granted(String s41Granted) {
		this.s41Granted = s41Granted;
	}
	public void setS41ApplicationMade(String s41ApplicationMade) {
		this.s41ApplicationMade = s41ApplicationMade;
	}
	
	public void setTrialInDefAbsence(String trialInDefAbsence) {
		this.trialInDefAbsence = trialInDefAbsence;
	}

	public void setSentenceInDefAbsence(String sentenceInDefAbsence) {
		this.sentenceInDefAbsence = sentenceInDefAbsence;
	}

	public String getTrialInDefAbsence() {
		return trialInDefAbsence;
	}

	public String getSentenceInDefAbsence() {
		return sentenceInDefAbsence;
	}

	public String getFormAStatus() {
		return formAStatus;
	}

	public void setFormAStatus(String formAStatus) {
		this.formAStatus = formAStatus;
	}

	public void setFormACourtClerk(String formACourtClerk) {
		this.formACourtClerk = formACourtClerk;
	}

	public String getFormACourtClerk() {
		return formACourtClerk;
	}

	/**
	 * Used to check if the UI has modified the EstimateTrialTime length
	 * 
	 * @return null if the UI has not modified the time. Otherwise returns the
	 *         newly modified time
	 */
	public Float getEstimatedTrialTime() {
		Map map = courtLogCRUDValue.getPropertyMap();
		Float estimatedTrialTime = (Float) map.get("E20901_TEO_time");
		return estimatedTrialTime;
	}

	public void setEstimatedTrialTime(Float trialTime, String units) {
		// create the map for the time and units
		HashMap<String, Object> map = new HashMap<String, Object>();
		// add the time
		map.put("E20901_TEO_time", trialTime);
		// create an enumeration for the units
		Vector<String> v = new Vector<String>();
		v.addElement(units);
		Enumeration enumeration = v.elements();
		map.put("E20901_TEO_units", enumeration);

		// create the container map
		HashMap<String, HashMap<String, Object>> containerMap = new HashMap<String, HashMap<String, Object>>();
		containerMap.put("E20901_Time_Estimate_Options", map);

		// populate the courtLogCRUDValue with the map
		courtLogCRUDValue.setPropertyMap(containerMap);
	}

	public CourtLogCRUDValue getCourtLogCRUDValue() {
		return this.courtLogCRUDValue;
	}

	public boolean isDefaultHearingType() {
		return defaultHearingType;
	}

	public void setDefaultHearingType(boolean defaultHearingType) {
		this.defaultHearingType = defaultHearingType;
	}
}
