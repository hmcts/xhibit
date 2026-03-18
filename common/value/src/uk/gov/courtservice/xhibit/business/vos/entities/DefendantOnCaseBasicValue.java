package uk.gov.courtservice.xhibit.business.vos.entities;

import java.sql.Timestamp;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Abdul Rahim Hussain
 * @version 1.0
 */
import java.util.Calendar;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;


public class DefendantOnCaseBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = 2L;
	private Integer defendantID;
	private Integer defendantOnCaseId;
	private Integer caseID;
	private Integer noOfTICs;
	private Integer finalDrivingLicenceStatus;
	private String ptiurn;
	private String isJuvenile;
	private String isMasked;
	private String maskedName;
	private Integer defendantNumber;
	private java.util.Calendar dateOfCommittal;
	private String obsInd;
	private String resultsVerified;
	private Integer collectMagistrateCourtId;
	private String asn;
	private String custodial;
	private String suspended;
	private String seriousDrugOffence;
	private String recommendedDeportation;
	private String nationality;
	private java.util.Calendar dateExported; 
	private String publicDisplayHide;
	private String differenceReport;
	//2878 new fields
	private java.util.Calendar amendedDateExported;
	private String amendedReason;
	private String hateIndicator;
	private String hateType;
	private String hateSentIndicator;
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
	private Boolean hateSentencingUpdate;
	private String section28Name1;
	private String section28Name2;
	private String section28Phone1;
	private String section28Phone2;
	private Timestamp custodyTimeLimit;

	//Aggravating Reasons
	private boolean aggravatingAssaultOnWorkers;
	private boolean aggravatingTerroristConnection;
    private boolean aggravatingEmergencyWorkers;
    private boolean aggravatingHostility;
    private boolean aggravatingSexualOrientation;
    private boolean aggravatingSexualOrientationOfVictim;
    private boolean aggravatingTransgender;
    private boolean aggravatingTransgenderOfVictim;
    
	//create case
	private String currentBcStatus;
	private Timestamp drivingDisqSuspendedDate;
	private String pncId;
	private Timestamp benchWarrantExecDate;
	private String bcStatusBwExecuted;
	private Timestamp magCourtFinalHearingDate;
	private Timestamp magCourtFirstHearingDate;
	private String commBcStatus;
	private Timestamp dateReceiptNoticeAppeal;
	private Timestamp formNgSentDate;
 	private String cacdAppealResult;
 	private Timestamp cacdAppealResultDate;
 	private String coaStatus;
 	private Integer version;
 	private String ctlApplies;
	private Integer darRetentionPolicyId;
 	
 	
 	public Timestamp getCacdAppealResultDate() {
		return cacdAppealResultDate;
	}

	public void setCacdAppealResultDate(Timestamp cacdAppealResultDate) {
		this.cacdAppealResultDate = cacdAppealResultDate;
	}
	
	public Timestamp getFormNgSentDate() {
		return formNgSentDate;
	}

	public void setFormNgSentDate(Timestamp formNgSentDate) {
		this.formNgSentDate = formNgSentDate;
	} 
 	
	public void setDatePapersSent(Timestamp formNgSentDate) {
		this.formNgSentDate = formNgSentDate;
	}
	
	public Integer getDefendantOnCaseId(){
		return defendantOnCaseId;
	}
	public void setDefendantOnCaseId(Integer defOnCaseId){
		defendantOnCaseId = defOnCaseId;
	}

	public Boolean getHateSentencingUpdate() {
		return hateSentencingUpdate;
	}
	public void setHateSentencingUpdate(Boolean hateSentencingUpdate) {
		this.hateSentencingUpdate = hateSentencingUpdate;
	}

	public DefendantOnCaseBasicValue() {
		// empty
	}

	public DefendantOnCaseBasicValue(Integer id, Integer version) {
		super(id, version);
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
	
	 
	public Timestamp getDateReceiptNoticeAppeal() {
		return dateReceiptNoticeAppeal;
	}
	public void setDateReceiptNoticeAppeal(Timestamp dateReceiptNoticeAppeal) {
		this.dateReceiptNoticeAppeal = dateReceiptNoticeAppeal;
	}

	public DefendantOnCaseBasicValue(Integer defendantOnCaseID, Integer version, Integer defendantID, Integer caseID,
			Integer noOfTICs, Integer finalDrivingLicenceStatus, String ptiurn, String isJuvenile, String isMasked,
			String maskedName, Integer defendantNumber, java.util.Calendar dateOfCommittal, String resultsVerified,
			Integer collectMagistrateCourtID, String asn) {

		this(defendantOnCaseID, version);
		this.defendantID = defendantID;
		this.caseID = caseID;
		this.noOfTICs = noOfTICs;
		this.finalDrivingLicenceStatus = finalDrivingLicenceStatus;
		this.ptiurn = ptiurn;
		this.isJuvenile = isJuvenile;
		this.isMasked = isMasked;
		this.maskedName = maskedName;
		this.defendantNumber = defendantNumber;
		this.dateOfCommittal = dateOfCommittal;
		this.resultsVerified = resultsVerified;
		this.collectMagistrateCourtId = collectMagistrateCourtID;
		this.asn = asn;
	}

	/**
	 * Description: 
	 *  This constructor was created by NW for case create
	 */
	public DefendantOnCaseBasicValue(Integer caseID, Integer defendantID, String asn, String commBcStatus, 
			String isJuvenile, Timestamp drivingDisqSuspendedDate, String pncId, String ptiurn, String isMasked, String maskedName,
			String hateInd, String hateType, Timestamp magCourtFirstHearingDate, 
			Timestamp magCourtFinalHearingDate, String nationality) {

		this.defendantID = defendantID;
		this.caseID = caseID;
		this.asn = asn;
		this.commBcStatus = commBcStatus;
		this.isJuvenile = isJuvenile;
		this.drivingDisqSuspendedDate = drivingDisqSuspendedDate;
		this.pncId = pncId;
		this.ptiurn = ptiurn;
		this.isMasked = isMasked;
		this.maskedName = maskedName;
		this.hateIndicator = hateInd;
		this.hateType = hateType;
		this.magCourtFirstHearingDate = magCourtFirstHearingDate;
		this.magCourtFinalHearingDate = magCourtFinalHearingDate;
		this.nationality = nationality;
	}

	/**
	 * Description: 
	 *  This constructor was created by KD for CCN0400
	 *  This constructor includes the custodial and suspended field     * 
	 *@author davieskl 
	 *@version 1.0
	 */
	public DefendantOnCaseBasicValue(Integer defendantOnCaseID, Integer version, Integer defendantID, Integer caseID,
			Integer noOfTICs, Integer finalDrivingLicenceStatus, String ptiurn, String isJuvenile, String isMasked,
			String maskedName, Integer defendantNumber, java.util.Calendar dateOfCommittal, String resultsVerified,
			Integer collectMagistrateCourtID, String asn, String custodial, String suspended,
			String seriousDrugOffence, String recommendedDeportation, String nationality, java.util.Calendar dateExported,
			String publicDisplayHide) {

		this(defendantOnCaseID, version);
		this.defendantID = defendantID;
		this.caseID = caseID;
		this.noOfTICs = noOfTICs;
		this.finalDrivingLicenceStatus = finalDrivingLicenceStatus;
		this.ptiurn = ptiurn;
		this.isJuvenile = isJuvenile;
		this.isMasked = isMasked;
		this.maskedName = maskedName;
		this.defendantNumber = defendantNumber;
		this.dateOfCommittal = dateOfCommittal;
		this.resultsVerified = resultsVerified;
		this.collectMagistrateCourtId = collectMagistrateCourtID;
		this.asn = asn;
		this.custodial = custodial;
		this.suspended = suspended;
		this.seriousDrugOffence = seriousDrugOffence;
		this.recommendedDeportation = recommendedDeportation;
		this.nationality = nationality;
		this.dateExported = dateExported;
		this.publicDisplayHide = publicDisplayHide;
	}


	/**
	 * Description: 
	 *  This constructor was created by BH for L-R-4224-01
	 *  This constructor includes the hate crime fields     * 
	 *@author hingstonb 
	 *@version 1.0
	 */
	public DefendantOnCaseBasicValue(Integer defendantOnCaseID, Integer version, Integer defendantID, Integer caseID,
			Integer noOfTICs, Integer finalDrivingLicenceStatus, String ptiurn, String isJuvenile, String isMasked,
			String maskedName, Integer defendantNumber, java.util.Calendar dateOfCommittal, String resultsVerified,
			Integer collectMagistrateCourtID, String asn, String custodial, String suspended,
			String seriousDrugOffence, String recommendedDeportation, String nationality, java.util.Calendar dateExported,
			String publicDisplayHide, String hateIndicator, String hateType, String hateSentIndicator) {

		this(defendantOnCaseID, version);
		this.defendantID = defendantID;
		this.caseID = caseID;
		this.noOfTICs = noOfTICs;
		this.finalDrivingLicenceStatus = finalDrivingLicenceStatus;
		this.ptiurn = ptiurn;
		this.isJuvenile = isJuvenile;
		this.isMasked = isMasked;
		this.maskedName = maskedName;
		this.defendantNumber = defendantNumber;
		this.dateOfCommittal = dateOfCommittal;
		this.resultsVerified = resultsVerified;
		this.collectMagistrateCourtId = collectMagistrateCourtID;
		this.asn = asn;
		this.custodial = custodial;
		this.suspended = suspended;
		this.seriousDrugOffence = seriousDrugOffence;
		this.recommendedDeportation = recommendedDeportation;
		this.nationality = nationality;
		this.dateExported = dateExported;
		this.publicDisplayHide = publicDisplayHide;
		this.hateIndicator = hateIndicator;
		this.hateType = hateType;
		this.hateSentIndicator = hateSentIndicator;
	}
 
	
	private Calendar defensiveCopy(Calendar calendar) {
		return (calendar == null ? null : (Calendar)calendar.clone());
	}

	public DefendantOnCaseBasicValue(DefendantOnCaseBasicValue defendantOnCaseBasicValue) {
		if (defendantOnCaseBasicValue == null) {
			return;
		}

		this.setId(defendantOnCaseBasicValue.getId());
		this.setVersion(defendantOnCaseBasicValue.getVersion());

		this.defendantID = defendantOnCaseBasicValue.defendantID;
		this.caseID = defendantOnCaseBasicValue.caseID;
		this.noOfTICs = defendantOnCaseBasicValue.noOfTICs;
		this.finalDrivingLicenceStatus = defendantOnCaseBasicValue.finalDrivingLicenceStatus;
		this.ptiurn = defendantOnCaseBasicValue.ptiurn;
		this.isJuvenile = defendantOnCaseBasicValue.isJuvenile;
		this.isMasked = defendantOnCaseBasicValue.isMasked;
		this.maskedName = defendantOnCaseBasicValue.maskedName;
		this.defendantNumber = defendantOnCaseBasicValue.defendantNumber;
		this.dateOfCommittal = defensiveCopy(defendantOnCaseBasicValue.dateOfCommittal);
		this.obsInd = defendantOnCaseBasicValue.obsInd;
		this.resultsVerified = defendantOnCaseBasicValue.resultsVerified;
		this.collectMagistrateCourtId = defendantOnCaseBasicValue.collectMagistrateCourtId;
		this.asn = defendantOnCaseBasicValue.asn;
		this.custodial = defendantOnCaseBasicValue.custodial;
		this.suspended = defendantOnCaseBasicValue.suspended;
		this.seriousDrugOffence = defendantOnCaseBasicValue.seriousDrugOffence;
		this.recommendedDeportation = defendantOnCaseBasicValue.recommendedDeportation;
		this.nationality = defendantOnCaseBasicValue.nationality;
		this.dateExported = defensiveCopy(defendantOnCaseBasicValue.dateExported); 
		this.publicDisplayHide = defendantOnCaseBasicValue.publicDisplayHide;
		this.amendedDateExported = defendantOnCaseBasicValue.amendedDateExported;
		this.amendedReason = defendantOnCaseBasicValue.amendedReason;
		this.hateIndicator = defendantOnCaseBasicValue.hateIndicator;
		this.hateType = defendantOnCaseBasicValue.hateType;
		this.hateSentIndicator = defendantOnCaseBasicValue.hateSentIndicator;
		this.differenceReport = defendantOnCaseBasicValue.differenceReport;
 	}

	public void setDefendantID(Integer defendantID) {
		this.defendantID = defendantID;
	}
	public Integer getDefendantID() {
		return defendantID;
	}

	public void setCaseID(Integer caseID) {
		this.caseID = caseID;
	}
	public Integer getCaseID() {
		return caseID;
	}

	public void setNoOfTICs(Integer noOfTICs) {
		this.noOfTICs = noOfTICs;
	}
	public Integer getNoOfTICs() {
		return noOfTICs;
	}

	public void setFinalDrivingLicenceStatus(Integer finalDrivingLicenceStatus) {
		this.finalDrivingLicenceStatus = finalDrivingLicenceStatus;
	}
	public Integer getFinalDrivingLicenceStatus() {
		return finalDrivingLicenceStatus;
	}

	public void setPtiurn(String ptiurn) {
		this.ptiurn = ptiurn;
	}
	public String getPtiurn() {
		return ptiurn;
	}

	public void setIsJuvenile(String isJuvenile) {
		this.isJuvenile = isJuvenile;
	}
	public String getIsJuvenile() {
		return isJuvenile;
	}

	public void setIsMasked(String isMasked) {
		this.isMasked = isMasked;
	}
	public String getIsMasked() {
		return isMasked;
	}

	public void setMaskedName(String maskedName) {
		this.maskedName = maskedName;
	}
	public String getMaskedName() {
		return maskedName;
	}

	public void setDefendantNumber(Integer defendantNumber) {
		this.defendantNumber = defendantNumber;
	}
	public Integer getDefendantNumber() {
		return defendantNumber;
	}

	public void setDateOfCommittal(java.util.Calendar dateOfCommittal) {
		this.dateOfCommittal = dateOfCommittal;
	}
	public java.util.Calendar getDateOfCommittal() {
		return dateOfCommittal;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}
	public String getObsInd() {
		return obsInd;
	}

	public void setResultsVerified(String resultsVerified) {
		this.resultsVerified = resultsVerified;
	}
	public String getResultsVerified() {
		return resultsVerified;
	}

	public void setCollectMagistrateCourtId(Integer collectMagistrateCourtId) {
		this.collectMagistrateCourtId = collectMagistrateCourtId;
	}
	public Integer getCollectMagistrateCourtId() {
		return collectMagistrateCourtId;
	}

	public void setAsn(String asn) {
		this.asn = asn;
	}
	public String getAsn() {
		return asn;
	}

	public String getCustodial() {
		return custodial;
	}
	public void setCustodial(String custodial) {
		this.custodial = custodial;
	}

	public String getSuspended() {
		return suspended;
	}
	public void setSuspended(String suspended) {
		this.suspended = suspended;
	}

	public String getRecommendedDeportation() {
		return recommendedDeportation;
	}
	public void setRecommendedDeportation(String recommendedDeportation) {
		this.recommendedDeportation = recommendedDeportation;
	}

	public String getSeriousDrugOffence() {
		return seriousDrugOffence;
	}
	public void setSeriousDrugOffence(String seriousDrugOffence) {
		this.seriousDrugOffence = seriousDrugOffence;
	}

	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public java.util.Calendar getDateExported() {
		return dateExported;
	}
	public void setDateExported(java.util.Calendar dateExported) {
		this.dateExported = dateExported;
	}

	public String getPublicDisplayHide() {
		return publicDisplayHide;
	}
	
	public String getDifferenceReport() {
		return differenceReport;
	}
	public void setDifferenceReport(String differenceReport) {
		this.differenceReport = differenceReport;
	}

	public boolean isHideDefendantInThisCase() {
		if (getPublicDisplayHide() != null) {
			return getPublicDisplayHide().equals(CaseBasicValue.HIDE_IN_PUBLIC_DISPLAY_FLAG);
		} else {
			return false;
		}
	}

	public void setPublicDisplayHide(String publicDisplayHide) {
		this.publicDisplayHide = publicDisplayHide;
	}
	public void setHideDefendantInThisCase(boolean setHideDefendantInThisCase) {
		if (setHideDefendantInThisCase) {
			setPublicDisplayHide(CaseBasicValue.HIDE_IN_PUBLIC_DISPLAY_FLAG);
		} else {
			setPublicDisplayHide(null);
		}
	}

	public void setAmendedDateExported(java.util.Calendar amendedDateExported) {
		this.amendedDateExported = amendedDateExported;
	}
	public java.util.Calendar getAmendedDateExported() {
		return amendedDateExported;
	}

	public String getAmendedReason() {
		return amendedReason;
	}
	public void setAmendedReason(String amendedReason) {
		this.amendedReason = amendedReason;
	}

	public String getHateIndicator () {
		return hateIndicator;
	}
	public void setHateIndicator(String hateIndicator){
		this.hateIndicator = hateIndicator;
	}

	public String getHateType () {
		return hateType;
	}
	public void setHateType(String hateType){
		this.hateType = hateType;
	}

	public String getHateSentIndicator () {
		return hateSentIndicator;
	}
	public void setHateSentIndicator(String hateSentIndicator){
		this.hateSentIndicator = hateSentIndicator;
	}

	public String getCurrentBcStatus() {
		return currentBcStatus;
	}
	public void setCurrentBcStatus(String currentBcStatus) {
		this.currentBcStatus = currentBcStatus;
	}

	public Timestamp getDrivingDisqSuspendedDate(  ) {
		return drivingDisqSuspendedDate;
	}
	public void setDrivingDisqSuspendedDate( Timestamp drivingDisqSuspendedDate ) {
		this.drivingDisqSuspendedDate = drivingDisqSuspendedDate;
	}

	public String getPncId(  ) {
		return pncId;
	}
	public void setPncId( String pncId ) {
		this.pncId = pncId;
	}

	public Timestamp getBenchWarrantExecDate(  ) {
		return benchWarrantExecDate;
	}
	public void setBenchWarrantExecDate( Timestamp benchWarrantExecDate ) {
		this.benchWarrantExecDate=benchWarrantExecDate;
	}

	public String getBcStatusBwExecuted(  ) {
		return bcStatusBwExecuted;
	}
	public void setBcStatusBwExecuted( String bcStatusBwExecuted ) {
		this.bcStatusBwExecuted=bcStatusBwExecuted;
	}

	public Timestamp getMagCourtFirstHearingDate(  ) {
		return magCourtFirstHearingDate;
	}
	public void setMagCourtFirstHearingDate( Timestamp magCourtFirstHearingDate ) {
		this.magCourtFirstHearingDate=magCourtFirstHearingDate;
	}

	public Timestamp getMagCourtFinalHearingDate(  ) {
		return magCourtFinalHearingDate;
	}
	public void setMagCourtFinalHearingDate( Timestamp magCourtFinalHearingDate ) {
		this.magCourtFinalHearingDate=magCourtFinalHearingDate;
	}
	public String getSection28Name1() {
		return section28Name1;
	}
	public void setSection28Name1(String section28Name1) {
		this.section28Name1 = section28Name1;
	}
	public String getSection28Name2() {
		return section28Name2;
	}
	public void setSection28Name2(String section28Name2) {
		this.section28Name2 = section28Name2;
	}
	public String getSection28Phone1() {
		return section28Phone1;
	}
	public void setSection28Phone1(String section28Phone1) {
		this.section28Phone1 = section28Phone1;
	}
	public String getSection28Phone2() {
		return section28Phone2;
	}
	public void setSection28Phone2(String section28Phone2) {
		this.section28Phone2 = section28Phone2;
	}

	public Timestamp getCustodyTimeLimit() {
		return custodyTimeLimit;
	}
	public void setCustodyTimeLimit(Timestamp custodyTimeLimit) {
		this.custodyTimeLimit = custodyTimeLimit;
	}

	public String getCommBcStatus() {
		return commBcStatus;
	}
	
	public void setCommBcStatus(String commBcStatus) {
		this.commBcStatus = commBcStatus;
	}
	public String getCacdAppealResult() {
		return cacdAppealResult;
	}
	public void setCacdAppealResult(String cacdAppealResult) {
		this.cacdAppealResult = cacdAppealResult;
	}
	public String getCoaStatus() {
		return coaStatus;
	}
	public void setCoaStatus(String coaStatus) {
		this.coaStatus = coaStatus;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getCtlApplies() {
		return ctlApplies;
	}

	public void setCtlApplies(String ctlApplies) {
		this.ctlApplies = ctlApplies;
	}

	public boolean isAggravatingAssaultOnWorkers() {
		return aggravatingAssaultOnWorkers;
	}

	public boolean isAggravatingTerroristConnection() {
		return aggravatingTerroristConnection;
	}

	public boolean isAggravatingEmergencyWorkers() {
		return aggravatingEmergencyWorkers;
	}

	public boolean isAggravatingHostility() {
		return aggravatingHostility;
	}

	public boolean isAggravatingSexualOrientation() {
		return aggravatingSexualOrientation;
	}

	public boolean isAggravatingSexualOrientationOfVictim() {
		return aggravatingSexualOrientationOfVictim;
	}

	public boolean isAggravatingTransgender() {
		return aggravatingTransgender;
	}

	public boolean isAggravatingTransgenderOfVictim() {
		return aggravatingTransgenderOfVictim;
	}

	public void setAggravatingAssaultOnWorkers(boolean aggravatingAssaultOnWorkers) {
		this.aggravatingAssaultOnWorkers = aggravatingAssaultOnWorkers;
	}

	public void setAggravatingTerroristConnection(boolean aggravatingTerroristConnection) {
		this.aggravatingTerroristConnection = aggravatingTerroristConnection;
	}

	public void setAggravatingEmergencyWorkers(boolean aggravatingEmergencyWorkers) {
		this.aggravatingEmergencyWorkers = aggravatingEmergencyWorkers;
	}

	public void setAggravatingHostility(boolean aggravatingHostility) {
		this.aggravatingHostility = aggravatingHostility;
	}

	public void setAggravatingSexualOrientation(boolean aggravatingSexualOrientation) {
		this.aggravatingSexualOrientation = aggravatingSexualOrientation;
	}

	public void setAggravatingSexualOrientationOfVictim(boolean aggravatingSexualOrientationOfVictim) {
		this.aggravatingSexualOrientationOfVictim = aggravatingSexualOrientationOfVictim;
	}

	public void setAggravatingTransgender(boolean aggravatingTransgender) {
		this.aggravatingTransgender = aggravatingTransgender;
	}

	public void setAggravatingTransgenderOfVictim(boolean aggravatingTransgenderOfVictim) {
		this.aggravatingTransgenderOfVictim = aggravatingTransgenderOfVictim;
	}

	public Integer getDarRetentionPolicyId() {
		return darRetentionPolicyId;
	}

	public void setDarRetentionPolicyId(Integer darRetentionPolicyId) {
		this.darRetentionPolicyId = darRetentionPolicyId;
	}

}
