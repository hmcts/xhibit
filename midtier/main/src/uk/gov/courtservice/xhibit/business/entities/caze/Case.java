package uk.gov.courtservice.xhibit.business.entities.caze;

import java.sql.Timestamp;
import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.casereference.CaseReference;

public interface Case extends CSEntityLocal {

	public void setVulnerableVictimIndicator(String vulnerableVictimIndicator);

	public String getVulnerableVictimIndicator();

	public void setCaseId(Integer caseId);

	public Integer getCaseId();

	public void setCaseNumber(Integer caseNumber);

	public Integer getCaseNumber();

	public void setCaseType(String caseType);

	public String getCaseType();

	public void setCaseStatus(String caseType);

	public String getCaseStatus();

	public void setMagConvictionDate(Timestamp magConvictionDate);

	public Timestamp getMagConvictionDate();

	public void setCaseSubType(String caseSubType);

	public String getCaseSubType();

	public void setCaseTitle(String caseTitle);

	public String getCaseTitle();

	public void setCaseDescription(String caseDescription);

	public String getCaseDescription();

	public void setLinkedCaseId(Integer linkedCaseId);

	public Integer getLinkedCaseId();

	public void setBailMagCode(String bailMagCode);

	public String getBailMagCode();

	public void setRefCourtId(Integer refCourtId);

	public Integer getRefCourtId();

	public void setCourtId(Integer courtId);

	public Integer getCourtId();

	public String getChargeImportIndicator();

	public void setChargeImportIndicator(String chargeImportIndicator);

	public java.lang.String getCrestSeveredInd();

	public void setCrestSeveredInd(String crestSeveredInd);

	public java.lang.String getCrestIndictResp();

	public void setCrestIndictResp(String crestIndictResp);

	public Integer getCaseClass();

	public void setCaseClass(Integer caseClass);

	public void setProsAgencyRef(java.lang.String prosAgencyRef);

	public java.lang.String getProsAgencyRef();

	public void setDateIndRec(java.sql.Timestamp dateIndRec);

	public java.sql.Timestamp getDateIndRec();

	public void setEstPDHTrialLength(Integer estPDHTrialLength);

	public Integer getEstPDHTrialLength();

	public void setNoProsWitness(Integer noProsWitness);

	public Integer getNoProsWitness();

	public void setNoPageProsEvidence(Integer noPageProsEvidence);

	public Integer getNoPageProsEvidence();

	public void setLengthTape(Integer lengthTape);

	public Integer getLengthTape();

	public void setJudgeReasonForAppeal(java.lang.String judgeReasonForAppeal);

	public java.lang.String getJudgeReasonForAppeal();

	public void setResultsVerified(java.lang.String resultsVerified);

	public java.lang.String getResultsVerified();

	public void setIndictmentInfo1(String indictmentInfo1);

	public String getIndictmentInfo1();

	public void setIndictmentInfo2(String indictmentInfo2);

	public String getIndictmentInfo2();

	public void setIndictmentInfo3(String indictmentInfo3);

	public String getIndictmentInfo3();

	public void setIndictmentInfo4(String indictmentInfo4);

	public String getIndictmentInfo4();

	public void setIndictmentInfo5(String indictmentInfo5);

	public String getIndictmentInfo5();

	public void setIndictmentInfo6(String indictmentInfo6);

	public String getIndictmentInfo6();

	public void setCpsCaseWorker(String cpsCaseWorker);

	public String getCpsCaseWorker();

	public void setExportCharges(String exportCharges);

	public String getExportCharges();

	public void setPoliceOfficerAttending(String policeOfficerAttending);

	public String getPoliceOfficerAttending();

	public void setIndChangeStatus(String indChangeStatus);

	public String getIndChangeStatus();

	public void setClassCode(Integer classCode);

	public Integer getClassCode();

	public void setOffenceGroup(String offenceGroup);

	public String getOffenceGroup();

	public void setReceiptType(String receiptType);

	public String getReceiptType();

	public void setLinkedCase(uk.gov.courtservice.xhibit.business.entities.linkedcase.LinkedCase linkedCase);

	public uk.gov.courtservice.xhibit.business.entities.linkedcase.LinkedCase getLinkedCase();

	public void setDefendantOnCases(java.util.Collection defendantOnCases);

	public java.util.Collection getDefendantOnCases();

	public abstract CaseReference getCaseReference();

	public abstract void setCaseReference(CaseReference caseReference);

	public void setCccTransToRefCourtId(Integer cccTransToRefCourtId);

	public Integer getCccTransToRefCourtId();

	public void setPublicDisplayHide(String publicDisplayHide);

	public String getPublicDisplayHide();

	// adding new fields
	public void setMonitoringCategoryId(Integer monitoringCategoryId);

	public Integer getMonitoringCategoryId();

	public void setAppealLodgedDate(Timestamp appealLodgedDate);

	public Timestamp getAppealLodgedDate();

	public void setReceivedDate(Timestamp receivedDate);

	public Timestamp getReceivedDate();

	public void setSentForTrialDate(Timestamp sentForTrialDate);

	public Timestamp getSentForTrialDate();

	public void setTicketRequired(String ticketRequired);

	public String getTicketRequired();

	public void setTicketTypeCode(Integer ticketTypeCode);

	public Integer getTicketTypeCode();

	public Integer getCourtIdReceivingSite();

	public void setCommittalDate(Timestamp committalDate);

	public Timestamp getCommittalDate();

	public void setEitherWayType(String eitherWayType);

	public String getEitherWayType();

	public void setNoDefendantsForCase(Integer noDefendantsForCase);

	public Integer getNoDefendantsForCase();

	public void setTransferredCase(String transferredCase);

	public String getTransferredCase();

	public void setTransferDeferredSentence(String transferDeferredSentence);

	public String getTransferDeferredSentence();

	public void setDateTransFrom(Timestamp dateTransFrom);

	public Timestamp getDateTransFrom();

	public void setDateTransTo(Timestamp dateTransTo);

	public Timestamp getDateTransTo();

	public void setOriginalCaseNumber(String originalCaseNumber);

	public String getOriginalCaseNumber();

	public Integer getCccTransFromRefCourtId();

	public void setRetrial(String retrial);

	public String getRetrial();

	public void setSecureCourt(String secureCourt);

	public String getSecureCourt();

	public void setVideoLinkRequired(String videoLinkRequired);

	public String getVideoLinkRequired();

	public Timestamp getPreliminaryDateOfHearing();

	public void setPreliminaryDateOfHearing(Timestamp preliminaryDateOfHearing);

	public String getMagistratesCaseRef();

	public void setMagistratesCaseRef(String magistratesCaseRef);

	public void setMagCourtHearingTypeRefId(Integer magCourtHearingTypeRefId);

	public Integer getMagCourtHearingTypeRefId();

	public String getOriginalJps1();

	public void setOriginalJps1(String originalJps1);

	public String getOriginalJps2();

	public void setOriginalJps2(String originalJps2);

	public String getOriginalJps3();

	public void setOriginalJps3(String originalJps3);

	public String getOriginalJps4();

	public void setOriginalJps4(String originalJps4);

	public String getCaseListed();

	public void setCaseListed(String caseListed);

	public Integer getPoliceForceCode();

	public void setPoliceForceCode(Integer policeForceCode);

	public void setCccTransFromRefCourtId(Integer cccTransFromRefCourtId);

	public void setCourtIdReceivingSite(Integer courtIdReceivingSite);

	public Timestamp getOrigBodyDecisionDate();

	public void setOrigBodyDecisionDate(Timestamp origBodyDecisionDate);

	public Timestamp getLcSentDate();

	public void setLcSentDate(Timestamp lcSentDate);

	public Integer getCrackedIneffectiveId();

	public void setCrackedIneffectiveId(Integer crackedIneffectiveId);

	public Integer getDefaultHearingType();

	public void setDefaultHearingType(Integer defaultHearingType);

	public String getSection28Name1();

	public void setSection28Name1(String section28Name1);

	public String getSection28Name2();

	public void setSection28Name2(String section28Name2);

	public String getSection28Phone1();

	public void setSection28Phone1(String section28Phone1);

	public String getSection28Phone2();

	public void setSection28Phone2(String section28Phone2);
	
	public void setS28Eligible(String s28Eligible);

	public String getS28Eligible();
	
	public void setS28OrderMade(String s28OrderMade);

	public String getS28OrderMade();


	public Integer getPubRunningListId();

	public void setPubRunningListId(Integer pubRunningListId);
	
	public Integer getCaseGroupNumber();
	
	public void setCaseGroupNumber(Integer caseGroupNumber);
	
	public void setMonetaryOrderTrackings(java.util.Collection monetaryOrderTrackings);

	public java.util.Collection getMonetaryOrderTrackings();
	
	public Timestamp getDateTransRecordedTo();

	public void setDateTransRecordedTo(Timestamp dateTransRecordedTo);
	
	public String getCivilUnrest();
	
	public void setCivilUnrest(String civilUnrest);
	
	public String getTelevisedApplicationMade();

	public void setTelevisedApplicationMade(String televisedApplicationMade);

	public Timestamp getTelevisedAppMadeDate();

	public void setTelevisedAppMadeDate(Timestamp televisedAppMadeDate);

	public String getTelevisedAppGranted();

	public void setTelevisedAppGranted(String televisedAppGranted);

	public String getTelevisedAppRefusedFreetext();

	public void setTelevisedAppRefusedFreetext(String televisedAppRefusedFreetext);

	public String getTelevisedRemarksFilmed();

	public void setTelevisedRemarksFilmed(String televisedRemarksFilmed);
	
	public void setRefusedBroadcastCase(java.util.Collection refusedBroadcastCase);

	public java.util.Collection getRefusedBroadcastCase();
	
	public Timestamp getLastUpdateDate();

	public void setLastUpdateDate(Timestamp lastUpdateDate);
	
	public Integer getDarRetentionPolicyId();
	
	public void setDarRetentionPolicyId(Integer darRetentionPolicyId);
	
	public Timestamp getCrpLastUpdateDate();

	public void setCrpLastUpdateDate(Timestamp crpLastUpdateDate);
	
	public Date getCreationDate();

	public void setCreationDate(Date creationDate);

}