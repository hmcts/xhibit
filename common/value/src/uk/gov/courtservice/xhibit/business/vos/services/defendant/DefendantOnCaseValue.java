package uk.gov.courtservice.xhibit.business.vos.services.defendant;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantReferenceBasicValue;

/**
 * <p>
 * Title: DefendantOnCaseValue
 * </p>
 * <p>
 * Description: This value object composes 3 value objects that contain
 * updatable data from XHIBIT. The internal value obejcts are
 * DefendantOnCaseBasicValue which holds finalDrivingLicienceStatus and noOfTics
 * and two DefendantReferenceBasicValues which hold driverNumber and croNumber.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford
 * @version 1.0
 */

public class DefendantOnCaseValue extends CSAbstractValue {
    
    private static final long serialVersionUID = 1L;

    private DefendantOnCaseBasicValue defOnCaseBVO;

    private DefendantReferenceBasicValue driverNumberDefRefBVO;

    private DefendantReferenceBasicValue croNumberDefRefBVO;

    private DefendantReferenceBasicValue licenceTypeRefBVO;
    
    private DefendantReferenceBasicValue issueNumberRefBVO;
    
    private String colMagCourtName;
    
    private Integer courtId = null;
    
    private Boolean hateCrimeFlag;
    private Boolean generalDisability;
    private Boolean victimDisability;
    private Boolean racialAggravated;
    private Boolean raceAndReligionAggravated;
    private Boolean religionAggravated;
    private Boolean generalSexual;
    private Boolean victimSexual;
    private Boolean generalTransgender;
    private Boolean victimTransgender;

    // Aggravating Reasons
    private Boolean aggravatingAssaultOnWorkers;
    private Boolean aggravatingTerroristConnection;
    private Boolean aggravatingEmergencyWorkers;
    private Boolean aggravatingHostility;
    private Boolean aggravatingSexualOrientation;
    private Boolean aggravatingSexualOrientationOfVictim;
    private Boolean aggravatingTransgender;
    private Boolean aggravatingTransgenderOfVictim;
    
    public DefendantOnCaseValue() {
        driverNumberDefRefBVO = new DefendantReferenceBasicValue();
        driverNumberDefRefBVO.setReferenceName(DefendantReferenceProperties.DRIVER_NUMBER);
        
        croNumberDefRefBVO = new DefendantReferenceBasicValue();
        croNumberDefRefBVO.setReferenceName(DefendantReferenceProperties.CRO_NUMBER);
        
        licenceTypeRefBVO = new DefendantReferenceBasicValue();
        licenceTypeRefBVO.setReferenceName(DefendantReferenceProperties.LICENCE_TYPE);
        
        issueNumberRefBVO = new DefendantReferenceBasicValue();
        issueNumberRefBVO.setReferenceName(DefendantReferenceProperties.LICENCE_ISSUE_NUMBER);
    }

    // getters
    public DefendantReferenceBasicValue getDriverNumberBVO() {
        return this.driverNumberDefRefBVO;
    }

    public DefendantReferenceBasicValue getCRONumberBVO() {
        return this.croNumberDefRefBVO;
    }
    
    public DefendantReferenceBasicValue getLicenceTypeBVO(){
    	return this.licenceTypeRefBVO;
    }
    
    public DefendantReferenceBasicValue getIssueNumberBVO(){
    	return this.issueNumberRefBVO;
    }

    public DefendantOnCaseBasicValue getDefendantOnCaseBVO() {
        return this.defOnCaseBVO;
    }

    public String getDriverNumber() {
        return this.driverNumberDefRefBVO.getReferenceValue();
    }

    public String getCRONumber() {
        return this.croNumberDefRefBVO.getReferenceValue();
    }

    public String getLicenceType(){
    	String licenceType = "";
    	
    	if ( this.licenceTypeRefBVO != null ){
    		licenceType = this.licenceTypeRefBVO.getReferenceValue();
    	}
    	
    	return licenceType;
    }
    
    public String getIssueNumber() {
    	String issueNumber = "";
    	
    	if ( this.issueNumberRefBVO != null ){
    		issueNumber = this.issueNumberRefBVO.getReferenceValue();
    	}
    	
    	return issueNumber;
    }
    
    public Integer getNoOfTics() {
        return this.defOnCaseBVO == null ? null : this.defOnCaseBVO.getNoOfTICs();
    }

    public Integer getFinalDrivingLicienceStatus() {
        return this.defOnCaseBVO == null ? null : this.defOnCaseBVO.getFinalDrivingLicenceStatus();
    }

    public String getColMagCourtName() {
        return this.colMagCourtName;
    }
    
    public Integer getCourtId() {
        return this.courtId;
    }
    
    public Boolean getHideDefendantInThisCase() {
        return this.defOnCaseBVO == null ? null 
                : new Boolean(this.defOnCaseBVO.isHideDefendantInThisCase());
    }

	public Integer getCaseId() {
		return this.getDefendantOnCaseBVO().getCaseID();
	}

	// setters
    public void setDriverNumberBVO(DefendantReferenceBasicValue defRefBVO) {
        this.driverNumberDefRefBVO = defRefBVO;
    }

    public void setCRONumberBVO(DefendantReferenceBasicValue defRefBVO) {
        this.croNumberDefRefBVO = defRefBVO;
    }

    public void setLicenceTypeBVO(DefendantReferenceBasicValue defRefBVO) {
    	this.licenceTypeRefBVO = defRefBVO;
    }
    
    public void setIssueNumberBVO(DefendantReferenceBasicValue defRefBVO) {
    	this.issueNumberRefBVO = defRefBVO;
    }
    
    public void setDefendantOnCaseBVO(DefendantOnCaseBasicValue defOnCaseBVO) {
        this.defOnCaseBVO = defOnCaseBVO;
    }

    public void setDefendantId(Integer defId) {
        driverNumberDefRefBVO.setDefendantID(defId);
        croNumberDefRefBVO.setDefendantID(defId);
        licenceTypeRefBVO.setDefendantID(defId);
        issueNumberRefBVO.setDefendantID(defId);
    }

    public void setDriverNumber(String driverNumber) {
        if (this.driverNumberDefRefBVO != null) {
            this.driverNumberDefRefBVO.setReferenceValue(driverNumber);
        }
    }

    public void setCRONumber(String croNumber) {
        if (this.croNumberDefRefBVO != null) {
            this.croNumberDefRefBVO.setReferenceValue(croNumber);
        }
    }

    public void setNoOfTics(Integer noOfTics) {
        if (this.defOnCaseBVO != null) {
            this.defOnCaseBVO.setNoOfTICs(noOfTics);
        }
    }

    public void setLicenceType(String licenceType){
    	if ( this.licenceTypeRefBVO != null ){
    		this.licenceTypeRefBVO.setReferenceValue(licenceType);
    	}
    }
    
    public void setIssueNumber(String issueNumber) {
    	if ( this.issueNumberRefBVO != null) {
    		this.issueNumberRefBVO.setReferenceValue(issueNumber);
    	}
    }
    
    public void setFinalDrivingLicienceStatus(Integer finalDrivingLicienceStat) {
        if (this.defOnCaseBVO != null) {
            this.defOnCaseBVO.setFinalDrivingLicenceStatus(finalDrivingLicienceStat);
        }
    }

    public void setColMagCourtName(String colMagCourtName) {
        this.colMagCourtName = colMagCourtName;
    }
    
    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public void setHideDefendantInThisCase(boolean hideDefendantInThisCase) {
        if (this.defOnCaseBVO != null) {
            this.defOnCaseBVO.setHideDefendantInThisCase(hideDefendantInThisCase);
        }
    }

    public Boolean getGeneralDisability() {
        return generalDisability;
    }

    public void setGeneralDisability(Boolean generalDisability) {
        this.generalDisability = generalDisability;
    }

    public Boolean getGeneralSexual() {
        return generalSexual;
    }

    public void setGeneralSexual(Boolean generalSexual) {
        this.generalSexual = generalSexual;
    }

    public Boolean getGeneralTransgender() {
        return generalTransgender;
    }

    public void setGeneralTransgender(Boolean generalTransgender) {
        this.generalTransgender = generalTransgender;
    }

    public Boolean getHateCrimeFlag() {
        return hateCrimeFlag;
    }

    public void setHateCrimeFlag(Boolean hateCrimeFlag) {
        this.hateCrimeFlag = hateCrimeFlag;
    }

    public Boolean getRaceAndReligionAggravated() {
        return raceAndReligionAggravated;
    }

    public void setRaceAndReligionAggravated(Boolean raceAndReligionAggravated) {
        this.raceAndReligionAggravated = raceAndReligionAggravated;
    }

    public Boolean getRacialAggravated() {
        return racialAggravated;
    }

    public void setRacialAggravated(Boolean racialAggravated) {
        this.racialAggravated = racialAggravated;
    }

    public Boolean getReligionAggravated() {
        return religionAggravated;
    }

    public void setReligionAggravated(Boolean religionAggravated) {
        this.religionAggravated = religionAggravated;
    }

    public Boolean getVictimDisability() {
        return victimDisability;
    }

    public void setVictimDisability(Boolean victimDisability) {
        this.victimDisability = victimDisability;
    }

    public Boolean getVictimSexual() {
        return victimSexual;
    }

    public void setVictimSexual(Boolean victimSexual) {
        this.victimSexual = victimSexual;
    }

    public Boolean getVictimTransgender() {
        return victimTransgender;
    }

    public void setVictimTransgender(Boolean victimTransgender) {
        this.victimTransgender = victimTransgender;
    }

	public Boolean getAggravatingAssaultOnWorkers() {
		return aggravatingAssaultOnWorkers;
	}

	public Boolean getAggravatingTerroristConnection() {
		return aggravatingTerroristConnection;
	}

	public Boolean getAggravatingEmergencyWorkers() {
		return aggravatingEmergencyWorkers;
	}

	public Boolean getAggravatingHostility() {
		return aggravatingHostility;
	}

	public Boolean getAggravatingSexualOrientation() {
		return aggravatingSexualOrientation;
	}

	public Boolean getAggravatingSexualOrientationOfVictim() {
		return aggravatingSexualOrientationOfVictim;
	}

	public Boolean getAggravatingTransgender() {
		return aggravatingTransgender;
	}

	public Boolean getAggravatingTransgenderOfVictim() {
		return aggravatingTransgenderOfVictim;
	}

	public void setAggravatingAssaultOnWorkers(Boolean aggravatingAssaultOnWorkers) {
		this.aggravatingAssaultOnWorkers = aggravatingAssaultOnWorkers;
	}

	public void setAggravatingTerroristConnection(Boolean aggravatingTerroristConnection) {
		this.aggravatingTerroristConnection = aggravatingTerroristConnection;
	}

	public void setAggravatingEmergencyWorkers(Boolean aggravatingEmergencyWorkers) {
		this.aggravatingEmergencyWorkers = aggravatingEmergencyWorkers;
	}

	public void setAggravatingHostility(Boolean aggravatingHostility) {
		this.aggravatingHostility = aggravatingHostility;
	}

	public void setAggravatingSexualOrientation(Boolean aggravatingSexualOrientation) {
		this.aggravatingSexualOrientation = aggravatingSexualOrientation;
	}

	public void setAggravatingSexualOrientationOfVictim(Boolean aggravatingSexualOrientationOfVictim) {
		this.aggravatingSexualOrientationOfVictim = aggravatingSexualOrientationOfVictim;
	}

	public void setAggravatingTransgender(Boolean aggravatingTransgender) {
		this.aggravatingTransgender = aggravatingTransgender;
	}

	public void setAggravatingTransgenderOfVictim(Boolean aggravatingTransgenderOfVictim) {
		this.aggravatingTransgenderOfVictim = aggravatingTransgenderOfVictim;
	}
}