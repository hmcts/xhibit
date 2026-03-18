package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

import java.util.Date;

/**
 * From Defendant, and DefendantOnCase in PreHearing CMR
 */
public class HRDefendantValue implements HRValueObject {

    private Integer defendantID;

    private String firstName;

    private String middleName;

    private String surname;

    private Date lastConvictionDate;

    private Integer noOfTICs;

    private String driverNumber; // this is acquired by use of

    // defendantID, and "driverNumber" on
    // DEFENDANT_REFERENCE table

    private Integer finalDrivingLicenseStatus;

    private String croNumber; // see note for driverNumber

    private String licenceType;
    private String issueNumber;
    
    private Integer defHearingRecordID;

    private Integer collectMagistrateCourtId;

    private String collectMagistrateCourtName;
    
    private boolean legallyAided = false;
    
    private String inCustody;
    
    private static final long serialVersionUID = 296359057756253654L;

    public HRDefendantValue(Integer defID) {
        this.defendantID = defID;
    }
    
    public HRDefendantValue() {
        //Empty
    }

    public String getCroNumber() {
        return croNumber;
    }

    public Integer getDefendantID() {
        return defendantID;
    }

    public String getDriverNumber() {
        return driverNumber;
    }

    public String getLicenceType(){
    	return licenceType;
    }
    
    public String getIssueNumber(){
    	return issueNumber;
    }
    
    public Integer getFinalDrivingLicenseStatus() {
        return finalDrivingLicenseStatus;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Integer getNoOfTICs() {
        return noOfTICs;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setNoOfTICs(Integer noOfTICs) {
        this.noOfTICs = noOfTICs;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setFinalDrivingLicenseStatus(Integer finalDrivingLicenseStatus) {
        this.finalDrivingLicenseStatus = finalDrivingLicenseStatus;
    }

    public void setDriverNumber(String driverNumber) {
        this.driverNumber = driverNumber;
    }

    public void setCroNumber(String croNumber) {
        this.croNumber = croNumber;
    }

    public void setLicenceType(String licenceType){
    	this.licenceType = licenceType;
    }
    
    public void setIssueNumber( String issueNumber ){
    	this.issueNumber = issueNumber;
    }
    
    public Date getLastConvictionDate() {
        return lastConvictionDate;
    }

    public void setLastConvictionDate(Date lastConvictionDate) {
        this.lastConvictionDate = lastConvictionDate;
    }

    public Integer getDefHearingRecordID() {
        return defHearingRecordID;
    }

    public void setDefHearingRecordID(Integer defHearingRecordID) {
        this.defHearingRecordID = defHearingRecordID;
    }

    public void setDefendantID(Integer id) {
        this.defendantID = id;
    }

    public Integer getCollectMagistrateCourtId() {
        return collectMagistrateCourtId;
    }

    public void setCollectMagistrateCourtId(Integer collectMagistrateCourtId) {
        this.collectMagistrateCourtId = collectMagistrateCourtId;
    }

    public String getCollectMagistrateCourtName() {
        return collectMagistrateCourtName;
    }

    public void setCollectMagistrateCourtName(String collectMagistrateCourtName) {
        this.collectMagistrateCourtName = collectMagistrateCourtName;
    }
    
    public boolean getLegallyAided() {
        return this.legallyAided;
    }
    
    public void setLegallyAided(boolean legallyAided) {
        this.legallyAided = legallyAided;
    }

	public String getInCustody() {
		return inCustody;
	}

	public void setInCustody(String inCustody) {
		this.inCustody = inCustody;
	}
}