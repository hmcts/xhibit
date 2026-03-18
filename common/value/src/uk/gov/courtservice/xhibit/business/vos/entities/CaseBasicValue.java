package uk.gov.courtservice.xhibit.business.vos.entities;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: CaseBasicValue
 * </p>
 * <p>
 * Description: CaseBasicValue is intended to represent case entities as stored
 * in the case table.
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 */

public class CaseBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = 1L;
	public static final String READY = "R";
	public static final String EXPORTING = "E";
	public static final String COMPLETE = "C";
	public static final String FAILED = "F";
	public static final String HIDE_IN_PUBLIC_DISPLAY_FLAG = "Y";
	private Integer caseId;
	private Integer caseNumber;
	private String caseType;
	private Calendar magConvictionDate;
	private String caseSubType;
	private String caseTitle;
	private String caseDescription;
	private Integer linkedCaseID;
	private Integer courtID;
	private Integer refCourtID;
	private String bailMagCode;
	private String chargeImportIndicator;
	private java.util.Calendar dateIndRec;
	private Integer estPDHTrialLength;
	private String crestIndictResp;
	private String judgeReasonForAppeal;
	private java.util.Calendar leaseTime;
	private Integer lengthTape;
	private Integer noPageProsEvidence;
	private Integer noProsWitness;
	private String prosAgencyRef;
	private String crestSeveredInd;
	private Integer caseClass;
	private String resultsVerified;
	private String indictmentInfo1;
	private String indictmentInfo2;
	private String indictmentInfo3;
	private String indictmentInfo4;
	private String indictmentInfo5;
	private String indictmentInfo6;
	private String policeOfficerAttending;
	private String cpsCaseWorker;
	private String exportCharges;
	private String indChangeStatus;
	private Integer classCode;
	private String offenceGroupCode;
	private Integer cccTransToRefCourtId;
	private String receiptType;
	private String vulnerableVictimIndicator;
	private String publicDisplayHide;

	private Timestamp origBodyDecisionDate;
	private Collection<IndictmentLogValue> indictmentLog;

	// new fields
	private Integer monitoringCategoryId;
	private Timestamp appealLodgedDate;
	private Timestamp receivedDate;
	private Timestamp sentForTrialDate;
	private String ticketRequired;
	private Integer ticketTypeCode;
	private Timestamp committalDate;
	private String eitherWayType;
	private Integer noDefendantsForCase;
	private String transferredCase;
	private String transferDeferredSentence;
	private Timestamp dateTransFrom;
	private Timestamp dateTransTo;
	private String originalCaseNumber;
	private String retrial;
	private String secureCourt;
	private Timestamp preliminaryDateOfHearing;
	private String magistratesCaseRef;
	private Integer magCourtHearingTypeRefId;
	private String originalJps1;
	private String originalJps2;
	private String originalJps3;
	private String originalJps4;
	private String caseListed;
	private Integer policeForceCode;
	private Integer cccTransFromRefCourtId;
	private Integer courtIdReceivingSite;
	private String videoLinkRequired = "N"; // DB default won't work so specify
											// here.
	private Integer crackedIneffectiveId;
	private Integer defaultHearingType;
	private String section28Name1;
	private String section28Name2;
	private String section28Phone1;
	private String section28Phone2;
	private String s28Eligible;
	private String s28OrderMade;
	
	private Integer caseGroupNumber;
	private Integer pubRunningListId;
	// adding for defendant case create
	private Timestamp lcSentDate;
	//adding for transfer case
	private Timestamp dateTransRecordedTo;

	// adding for View Case Details
	private String caseStatus;
	
	private String civilUnrest;
	
	//new fields after broadcasting changes
	private String televisedApplicationMade;
	private Timestamp televisedAppMadeDate;
	private String televisedAppGranted;
	private String televisedAppRefusedFreetext;
	private String televisedRemarksFilmed;
	
	//DARTS
	private Integer darRetentionPolicyId;
	private Timestamp crpLastUpdateDate;
	private Date creationDate;

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public Timestamp getCrpLastUpdateDate() {
		return crpLastUpdateDate;
	}

	public void setCrpLastUpdateDate(Timestamp crpLastUpdateDate) {
		this.crpLastUpdateDate = crpLastUpdateDate;
	}

	public CaseBasicValue() {
		super();
	}

	public CaseBasicValue(Integer id, Integer version) {
		super(id, version);
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	/**
	 * @return java.lang.Integer
	 */
	public Integer getCaseNumber() {
		return caseNumber;
	}

	/**
	 * @return java.lang.String
	 */
	public String getCaseType() {
		return caseType;
	}

	/**
	 * @return java.lang.Date
	 */
	public Calendar getMagConvictionDate() {
		return magConvictionDate;
	}

	/**
	 * @return java.lang.String
	 */
	public String getCaseSubType() {
		return caseSubType;
	}

	/**
	 * @return java.lang.String
	 */
	public String getCaseTitle() {
		return caseTitle;
	}

	/**
	 * @return java.lang.String
	 */
	public String getCaseDescription() {
		return caseDescription;
	}

	/**
	 * @return java.lang.Integer
	 */
	public Integer getLinkedCaseID() {
		return linkedCaseID;
	}

	/**
	 * @param java.lang.Date
	 */
	public void setMagConvictionDate(Calendar magConvictionDate) {
		this.magConvictionDate = magConvictionDate;
	}

	/**
	 * @param java.lang.String
	 */
	public void setCaseSubType(String caseSubType) {
		this.caseSubType = caseSubType;
	}

	/**
	 * @param java.lang.String
	 */
	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}

	/**
	 * @param java.lang.String
	 */
	public void setCaseDescription(String caseDescription) {
		this.caseDescription = caseDescription;
	}

	/**
	 * @param java.lang.Integer
	 */
	public void setLinkedCaseID(Integer linkedCaseID) {
		this.linkedCaseID = linkedCaseID;
	}

	public void setRefCourtID(Integer refCourtID) {
		this.refCourtID = refCourtID;
	}

	public Integer getRefCourtID() {
		return refCourtID;
	}

	public void setBailMagCode(String bailMagCode) {
		this.bailMagCode = bailMagCode;
	}

	public String getBailMagCode() {
		return bailMagCode;
	}

	public void setChargeImportIndicator(String chargeImportIndicator) {
		this.chargeImportIndicator = chargeImportIndicator;
	}

	public String getChargeImportIndicator() {
		return chargeImportIndicator;
	}

	public void setDateIndRec(java.util.Calendar dateIndRec) {
		this.dateIndRec = dateIndRec;
	}

	public java.util.Calendar getDateIndRec() {
		return dateIndRec;
	}

	public void setEstPDHTrialLength(Integer estPDHTrialLength) {
		this.estPDHTrialLength = estPDHTrialLength;
	}

	public Integer getEstPDHTrialLength() {
		return estPDHTrialLength;
	}

	public void setCrestIndictResp(String crestIndictResp) {
		this.crestIndictResp = crestIndictResp;
	}

	public String getCrestIndictResp() {
		return crestIndictResp;
	}

	public void setJudgeReasonForAppeal(String judgeReasonForAppeal) {
		this.judgeReasonForAppeal = judgeReasonForAppeal;
	}

	public String getJudgeReasonForAppeal() {
		return judgeReasonForAppeal;
	}

	public Integer getCourtID() {
		return courtID;
	}

	public void setCourtID(Integer courtID) {
		this.courtID = courtID;
	}

	public void setLeaseTime(java.util.Calendar leaseTime) {
		this.leaseTime = leaseTime;
	}

	public java.util.Calendar getLeaseTime() {
		return leaseTime;
	}

	public void setLengthTape(Integer lengthTape) {
		this.lengthTape = lengthTape;
	}

	public Integer getLengthTape() {
		return lengthTape;
	}

	public void setNoPageProsEvidence(Integer noPageProsEvidence) {
		this.noPageProsEvidence = noPageProsEvidence;
	}

	public Integer getNoPageProsEvidence() {
		return noPageProsEvidence;
	}

	public void setNoProsWitness(Integer noProsWitness) {
		this.noProsWitness = noProsWitness;
	}

	public Integer getNoProsWitness() {
		return noProsWitness;
	}

	public void setProsAgencyRef(String prosAgencyRef) {
		this.prosAgencyRef = prosAgencyRef;
	}

	public String getProsAgencyRef() {
		return prosAgencyRef;
	}

	public void setResultsVerified(String resultsVerified) {
		this.resultsVerified = resultsVerified;
	}

	public String getResultsVerified() {
		return resultsVerified;
	}

	public void setCrestSeveredInd(String crestSeveredInd) {
		this.crestSeveredInd = crestSeveredInd;
	}

	public String getCrestSeveredInd() {
		return crestSeveredInd;
	}

	public void setCaseNumber(Integer caseNumber) {
		this.caseNumber = caseNumber;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public void setCaseClass(Integer caseClass) {
		this.caseClass = caseClass;
	}

	public Integer getCaseClass() {
		return caseClass;
	}

	public String getIndictmentInfo1() {
		return indictmentInfo1;
	}

	public String getIndictmentInfo2() {
		return indictmentInfo2;
	}

	public String getIndictmentInfo3() {
		return indictmentInfo3;
	}

	public String getIndictmentInfo4() {
		return indictmentInfo4;
	}

	public String getIndictmentInfo5() {
		return indictmentInfo5;
	}

	public String getIndictmentInfo6() {
		return indictmentInfo6;
	}

	public void setIndictmentInfo6(String indictmentInfo6) {
		this.indictmentInfo6 = indictmentInfo6;
	}

	public void setIndictmentInfo5(String indictmentInfo5) {
		this.indictmentInfo5 = indictmentInfo5;
	}

	public void setIndictmentInfo4(String indictmentInfo4) {
		this.indictmentInfo4 = indictmentInfo4;
	}

	public void setIndictmentInfo3(String indictmentInfo3) {
		this.indictmentInfo3 = indictmentInfo3;
	}

	public void setIndictmentInfo2(String indictmentInfo2) {
		this.indictmentInfo2 = indictmentInfo2;
	}

	public void setIndictmentInfo1(String indictmentInfo1) {
		this.indictmentInfo1 = indictmentInfo1;
	}

	public String getCpsCaseWorker() {
		return cpsCaseWorker;
	}

	public void setCpsCaseWorker(String cpsCaseWorker) {
		this.cpsCaseWorker = cpsCaseWorker;
	}

	public String getExportCharges() {
		return exportCharges;
	}

	public void setExportCharges(String exportCharges) {
		this.exportCharges = exportCharges;
	}

	public String getPoliceOfficerAttending() {
		return policeOfficerAttending;
	}

	public void setPoliceOfficerAttending(String policeOfficerAttending) {
		this.policeOfficerAttending = policeOfficerAttending;
	}

	public String getIndChangeStatus() {
		return indChangeStatus;
	}

	public void setIndChangeStatus(String indChangeStatus) {
		this.indChangeStatus = indChangeStatus;
	}

	/**
	 * @param classCode
	 */
	public void setClassCode(Integer classCode) {
		this.classCode = classCode;
	}

	/**
	 * @param offenceGroupCode
	 */
	public void setOffenceGroupCode(String offenceGroupCode) {
		this.offenceGroupCode = offenceGroupCode;
	}

	/**
	 * @return Integer the classCode
	 */
	public Integer getClassCode() {
		return this.classCode;
	}

	/**
	 * @return String the offenceGroupCode
	 */
	public String getOffenceGroupCode() {
		return this.offenceGroupCode;
	}

	public void setCccTransToRefCourtId(Integer cccTransToRefCourtId) {
		this.cccTransToRefCourtId = cccTransToRefCourtId;
	}

	public Integer getCccTransToRefCourtId() {
		return cccTransToRefCourtId;
	}

	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}

	public String getReceiptType() {
		return receiptType;
	}

	public String getVulnerableVictimIndicator() {
		return vulnerableVictimIndicator;
	}

	public void setVulnerableVictimIndicator(String vulnerableVictimIndicator) {
		this.vulnerableVictimIndicator = vulnerableVictimIndicator;
	}

	public String getPublicDisplayHide() {
		return publicDisplayHide;
	}

	public void setPublicDisplayHide(String publicDisplayHide) {
		this.publicDisplayHide = publicDisplayHide;
	}

	public boolean isHideCaseInPublicDisplay() {
		if (getPublicDisplayHide() != null) {
			return getPublicDisplayHide().equals(HIDE_IN_PUBLIC_DISPLAY_FLAG);
		} else {
			return false;
		}
	}

	public void setHideCaseInPublicDisplay(boolean hideCaseInPublicDisplay) {
		if (hideCaseInPublicDisplay) {
			setPublicDisplayHide(HIDE_IN_PUBLIC_DISPLAY_FLAG);
		} else {
			setPublicDisplayHide(null);
		}
	}

	public Collection<IndictmentLogValue> getIndictmentLog() {
		return indictmentLog;
	}

	public void setIndictmentLog(Collection<IndictmentLogValue> indictmentLog) {
		this.indictmentLog = indictmentLog;
	}

	// adding new fields for case create.
	public Integer getMonitoringCategoryId() {
		return monitoringCategoryId;
	}

	public void setMonitoringCategoryId(Integer monitoringCategoryId) {
		this.monitoringCategoryId = monitoringCategoryId;
	}

	public Timestamp getAppealLodgedDate() {
		return appealLodgedDate;
	}

	public void setAppealLodgedDate(Timestamp appealLodgedDate) {
		this.appealLodgedDate = appealLodgedDate;
	}

	public Timestamp getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Timestamp receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Timestamp getSentForTrialDate() {
		return sentForTrialDate;
	}

	public void setSentForTrialDate(Timestamp sentForTrialDate) {
		this.sentForTrialDate = sentForTrialDate;
	}

	public String getTicketRequired() {
		return ticketRequired;
	}

	public void setTicketRequired(String ticketRequired) {
		this.ticketRequired = ticketRequired;
	}

	public Integer getTicketTypeCode() {
		return ticketTypeCode;
	}

	public void setTicketTypeCode(Integer ticketTypeCode) {
		this.ticketTypeCode = ticketTypeCode;
	}

	public Integer getCourtIdReceivingSite() {
		return courtIdReceivingSite;
	}

	public Timestamp getCommittalDate() {
		return committalDate;
	}

	public void setCommittalDate(Timestamp committalDate) {
		this.committalDate = committalDate;
	}

	public String getEitherWayType() {
		return eitherWayType;
	}

	public void setEitherWayType(String eitherWayType) {
		this.eitherWayType = eitherWayType;
	}

	public Integer getNoDefendantsForCase() {
		return noDefendantsForCase;
	}

	public void setNoDefendantsForCase(Integer noDefendantsForCase) {
		this.noDefendantsForCase = noDefendantsForCase;
	}

	public String getTransferredCase() {
		return transferredCase;
	}

	public void setTransferredCase(String transferredCase) {
		this.transferredCase = transferredCase;
	}

	public String getTransferDeferredSentence() {
		return transferDeferredSentence;
	}

	public void setTransferDeferredSentence(String transferDeferredSentence) {
		this.transferDeferredSentence = transferDeferredSentence;
	}

	public Timestamp getDateTransFrom() {
		return dateTransFrom;
	}

	public Timestamp getDateTransTo() {
		return dateTransTo;
	}

	public void setDateTransFrom(Timestamp dateTransFrom) {
		this.dateTransFrom = dateTransFrom;
	}

	public void setDateTransTo(Timestamp dateTransTo) {
		this.dateTransTo = dateTransTo;
	}

	public String getOriginalCaseNumber() {
		return originalCaseNumber;
	}

	public void setOriginalCaseNumber(String originalCaseNumber) {
		this.originalCaseNumber = originalCaseNumber;
	}

	public Integer getCccTransFromRefCourtId() {
		return cccTransFromRefCourtId;
	}

	public String getRetrial() {
		return retrial;
	}

	public void setRetrial(String retrial) {
		this.retrial = retrial;
	}

	public String getSecureCourt() {
		return secureCourt;
	}

	public void setSecureCourt(String secureCourt) {
		this.secureCourt = secureCourt;
	}

	public Timestamp getPreliminaryDateOfHearing() {
		return preliminaryDateOfHearing;
	}

	public void setPreliminaryDateOfHearing(Timestamp preliminaryDateOfHearing) {
		this.preliminaryDateOfHearing = preliminaryDateOfHearing;
	}

	public String getMagistratesCaseRef() {
		return magistratesCaseRef;
	}

	public void setMagistratesCaseRef(String magistratesCaseRef) {
		this.magistratesCaseRef = magistratesCaseRef;
	}

	public void setMagCourtHearingTypeRefId(Integer magCourtHearingTypeRefId) {
		this.magCourtHearingTypeRefId = magCourtHearingTypeRefId;
	}

	public Integer getMagCourtHearingTypeRefId() {
		return magCourtHearingTypeRefId;
	}

	public String getOriginalJps1() {
		return originalJps1;
	}

	public void setOriginalJps1(String originalJps1) {
		this.originalJps1 = originalJps1;
	}

	public String getOriginalJps2() {
		return originalJps2;
	}

	public void setOriginalJps2(String originalJps2) {
		this.originalJps2 = originalJps2;
	}

	public String getOriginalJps3() {
		return originalJps3;
	}

	public void setOriginalJps3(String originalJps3) {
		this.originalJps3 = originalJps3;
	}

	public String getOriginalJps4() {
		return originalJps4;
	}

	public void setOriginalJps4(String originalJps4) {
		this.originalJps4 = originalJps4;
	}

	public String getCaseListed() {
		return caseListed;
	}

	public void setCaseListed(String caseListed) {
		this.caseListed = caseListed;
	}

	public Integer getPoliceForceCode() {
		return policeForceCode;
	}

	public void setPoliceForceCode(Integer policeForceCode) {
		this.policeForceCode = policeForceCode;
	}

	public void setCccTransFromRefCourtId(Integer cccTransFromRefCourtId) {
		this.cccTransFromRefCourtId = cccTransFromRefCourtId;
	}

	public void setCourtIdReceivingSite(Integer courtIdReceivingSite) {
		this.courtIdReceivingSite = courtIdReceivingSite;
	}

	public Timestamp getOrigBodyDecisionDate() {
		return origBodyDecisionDate;
	}

	public void setOrigBodyDecisionDate(Timestamp origBodyDecisionDate) {
		this.origBodyDecisionDate = origBodyDecisionDate;
	}

	public Timestamp getLcSentDate() {
		return lcSentDate;
	}

	public void setLcSentDate(Timestamp lcSentDate) {
		this.lcSentDate = lcSentDate;
	}

	public String getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getVideoLinkRequired() {
		return videoLinkRequired;
	}

	public void setVideoLinkRequired(String videoLinkRequired) {
		this.videoLinkRequired = videoLinkRequired;
	}

	public Integer getCrackedIneffectiveId() {
		return crackedIneffectiveId;
	}

	public void setCrackedIneffectiveId(Integer crackedIneffectiveId) {
		this.crackedIneffectiveId = crackedIneffectiveId;
	}

	public Integer getDefaultHearingType() {
		return defaultHearingType;
	}

	public void setDefaultHearingType(Integer defaultHearingType) {
		this.defaultHearingType = defaultHearingType;
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

	public Integer getCaseGroupNumber() {
		return caseGroupNumber;
	}

	public void setCaseGroupNumber(Integer caseGroupNumber) {
		this.caseGroupNumber = caseGroupNumber;
	}

	/**
	 * @return the pubRunningListId
	 */
	public Integer getPubRunningListId() {
		return pubRunningListId;
	}

	/**
	 * @param pubRunningListId
	 *            the pubRunningListId to set
	 */
	public void setPubRunningListId(Integer pubRunningListId) {
		this.pubRunningListId = pubRunningListId;
	}
	
	/**
	 * @return the S28Eligible
	 */
	public String getS28Eligible() {
		return s28Eligible;
	}

	/**
	 * @param S28Eligible the S28Eligible to set
	 */
	public void setS28Eligible(String s28Eligible) {
		this.s28Eligible = s28Eligible;
	}

	/**
	 * @return the s28OrderMade
	 */
	public String getS28OrderMade() {
		return s28OrderMade;
	}

	/**
	 * @param s28OrderMade the s28OrderMade to set
	 */
	public void setS28OrderMade(String s28OrderMade) {
		this.s28OrderMade = s28OrderMade;
	}

	/**
	 * Based upon case type and class code, this method determines what the
	 * default judge type code should be for the case which is stored in the 
	 * case listing entry record.
	 * @return Default Judge Type
	 */
	public String getDefaultJudgeTypeCode() {
		String defaultJudgeTypeCode = null;
		if ( "T".equals(getCaseType()) ) {
			// Trial Cases with a class of 1 or 2 should have a judge type of HJ, but class 3 and NULL should have a 
			// judge type of CJ
			if ( null == getClassCode() || Integer.valueOf(3).equals(getClassCode()) ) {
				defaultJudgeTypeCode = "CJ";
			}
			else {
				defaultJudgeTypeCode = "HJ";
			}
		}
		else if ( "S".equals(getCaseType()) || "A".equals(getCaseType()) ) {
			// Sentance and Appeal Cases should have a judge type of CJ
			defaultJudgeTypeCode = "CJ";
		}
		
		return defaultJudgeTypeCode;
	}

	public Timestamp getDateTransRecordedTo() {
		return dateTransRecordedTo;
	}

	public void setDateTransRecordedTo(Timestamp dateTransRecordedTo) {
		this.dateTransRecordedTo = dateTransRecordedTo;
	}

	public String getCivilUnrest() {
		return civilUnrest;
	}
	
	public void setCivilUnrest(String civilUnrest) {
		this.civilUnrest = civilUnrest;
	}
	
	public String getTelevisedApplicationMade() {
		return televisedApplicationMade;
	}

	public void setTelevisedApplicationMade(String televisedApplicationMade) {
		this.televisedApplicationMade = televisedApplicationMade;
	}

	public Timestamp getTelevisedAppMadeDate() {
		return televisedAppMadeDate;
	}

	public void setTelevisedAppMadeDate(Timestamp televisedAppMadeDate) {
		this.televisedAppMadeDate = televisedAppMadeDate;
	}

	public String getTelevisedAppGranted() {
		return televisedAppGranted;
	}

	public void setTelevisedAppGranted(String televisedAppGranted) {
		this.televisedAppGranted = televisedAppGranted;
	}

	public String getTelevisedAppRefusedFreetext() {
		return televisedAppRefusedFreetext;
	}

	public void setTelevisedAppRefusedFreetext(String televisedAppRefusedFreetext) {
		this.televisedAppRefusedFreetext = televisedAppRefusedFreetext;
	}

	public String getTelevisedRemarksFilmed() {
		return televisedRemarksFilmed;
	}

	public void setTelevisedRemarksFilmed(String televisedRemarksFilmed) {
		this.televisedRemarksFilmed = televisedRemarksFilmed;
	}

	public Integer getDarRetentionPolicyId() {
		return darRetentionPolicyId;
	}

	public void setDarRetentionPolicyId(Integer darRetentionPolicyId) {
		this.darRetentionPolicyId = darRetentionPolicyId;
	}
}
