package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

import java.util.Collection;

/**
 * This is a value object to store the summary information. It will list all
 * linked hearings and their related defendants. It will also store the
 * appropriate case information.
 * 
 * Values from Defendant, Hearing, Case, refHearingType
 * 
 * @author Anthony Martin
 * @version 1.1
 */
public class HearingListSummaryValue implements HRValueObject {

	private Integer caseID;

	private String caseType;

	private String caseSubType;

	private Integer caseNumber;

	private Integer hearingID;

	private Integer refHearingTypeID;

	private Integer linkedHearingID;

	private String refHearingTypeCode;

	private String refHearingTypeDesc;

	private Collection hrDefendantValues;

	private static final long serialVersionUID = -942883437997995599L;

	/*
	 * private Integer defendantID; private String firstName; private String
	 * middleName; private String surname;
	 */

	public Integer getCaseID() {
		return caseID;
	}

	public Integer getCaseNumber() {
		return caseNumber;
	}

	public String getCaseType() {
		return caseType;
	}

	public String getCaseSubType() {
		return caseSubType;
	}

	public Integer getHearingID() {
		return hearingID;
	}

	public Integer getLinkedHearingID() {
		return linkedHearingID;
	}

	public String getRefHearingTypeCode() {
		return refHearingTypeCode;
	}

	public String getRefHearingTypeDesc() {
		return refHearingTypeDesc;
	}

	public Integer getRefHearingTypeID() {
		return refHearingTypeID;
	}

	public void setRefHearingTypeID(Integer refHearingTypeID) {
		this.refHearingTypeID = refHearingTypeID;
	}

	public void setRefHearingTypeDesc(String refHearingTypeDesc) {
		this.refHearingTypeDesc = refHearingTypeDesc;
	}

	public void setRefHearingTypeCode(String refHearingTypeCode) {
		this.refHearingTypeCode = refHearingTypeCode;
	}

	public void setLinkedHearingID(Integer linkedHearingID) {
		this.linkedHearingID = linkedHearingID;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public void setCaseSubType(String caseSubType) {
		this.caseSubType = caseSubType;
	}

	public void setCaseNumber(Integer caseNumber) {
		this.caseNumber = caseNumber;
	}

	public void setCaseID(Integer caseID) {
		this.caseID = caseID;
	}

	public void setHearingID(Integer hearingID) {
		this.hearingID = hearingID;
	}

	public Collection getHrDefendantValues() {
		return hrDefendantValues;
	}

	public void setHrDefendantValues(Collection hrDefendantValues) {
		this.hrDefendantValues = hrDefendantValues;
	}
}
