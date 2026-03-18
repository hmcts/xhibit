package uk.gov.courtservice.xhibit.business.entities.caze;

import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class CaseBean extends CSEntityBean implements EntityBean {

	public Integer ejbCreate(Integer caseNumber, String caseType, String caseTitle, String caseSubType,
			Integer classCode, Integer courtID, Integer monitoringCategoryId, Timestamp appealLodgedDate,
			Timestamp receivedDate, Timestamp sentForTrialDate, String ticketRequired, Integer ticketTypeCode,
			Integer courtIdReceivingSite, Timestamp committalDate, String receiptType, String eitherWayType,
			Integer noDefendantsForCase, String transferredCase, String transferDeferredSentence,
			Timestamp dateTransFrom, Timestamp dateTransTo, String originalCaseNumber, Integer cccTransFromRefCourtId,
			String retrial, String secureCourt, Timestamp preliminaryDateOfHearing, Integer refCourtId,
			String magistratesCaseRef, Integer magcourtHearingtypeRefId, String originalJps1, String originalJps2,
			String originalJps3, String originalJps4, String caseDescription, String caseListed,
			Integer policeForceCode, Integer noPageProsEvidence, Timestamp origBodyDecisionDate,
			String chargeImportIndicator, Timestamp magConvictionDate, Timestamp lcSentDate, String videoLinkRequired,
			Integer defaultHearingType, Integer crackedIneffectiveId, String section28Name1, String section28Name2,
			String section28Phone1, String section28Phone2, String s28Eligible, String s28OrderMade, String userDisplayName, Integer caseGroupNumber,
			String civilUnrest, String televisedAppGranted, String televisedApplicationMade, Timestamp televisedAppMadeDate,
			String televisedAppRefusedFreetext, String televisedRemarksFilmed,
			Integer darRetentionPolicyId, Timestamp crpLastUpdateDate) throws CreateException {

		setCaseNumber(caseNumber);
		setCaseType(caseType);
		setCaseTitle(caseTitle);
		setCaseSubType(caseSubType);
		setClassCode(classCode);
		setCourtId(courtID);
		setMonitoringCategoryId(monitoringCategoryId);
		setAppealLodgedDate(appealLodgedDate);
		setReceivedDate(receivedDate);
		setSentForTrialDate(sentForTrialDate);
		setTicketTypeCode(ticketTypeCode);
		setTicketRequired(ticketRequired);
		setCourtIdReceivingSite(courtIdReceivingSite);
		setCommittalDate(committalDate);
		setReceiptType(receiptType);
		setEitherWayType(eitherWayType);
		setNoDefendantsForCase(noDefendantsForCase);
		setTransferredCase(transferredCase);
		setTransferDeferredSentence(transferDeferredSentence);
		setDateTransFrom(dateTransFrom);
		setDateTransTo(dateTransTo);
		setOriginalCaseNumber(originalCaseNumber);
		setCccTransFromRefCourtId(cccTransFromRefCourtId);
		setRetrial(retrial);
		setSecureCourt(secureCourt);
		setPreliminaryDateOfHearing(preliminaryDateOfHearing);
		setRefCourtId(refCourtId);
		setMagistratesCaseRef(magistratesCaseRef);
		setMagCourtHearingTypeRefId(magcourtHearingtypeRefId);
		setOriginalJps1(originalJps1);
		setOriginalJps2(originalJps2);
		setOriginalJps3(originalJps3);
		setOriginalJps4(originalJps4);
		setCaseDescription(caseDescription);
		setCaseListed(caseListed);
		setPoliceForceCode(policeForceCode);
		setNoPageProsEvidence(noPageProsEvidence);
		setOrigBodyDecisionDate(origBodyDecisionDate);
		setChargeImportIndicator(chargeImportIndicator);
		setMagConvictionDate(magConvictionDate);
		setLcSentDate(lcSentDate);
		setVideoLinkRequired(videoLinkRequired);
		setDefaultHearingType(defaultHearingType);
		setCrackedIneffectiveId(crackedIneffectiveId);
		setSection28Name1(section28Name1);
		setSection28Name2(section28Name2);
		setSection28Phone1(section28Phone1);
		setSection28Phone2(section28Phone2);
		setS28Eligible(s28Eligible);
		setS28OrderMade(s28OrderMade);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		setCaseGroupNumber(caseGroupNumber);
		setCivilUnrest(civilUnrest);
		setTelevisedAppGranted(televisedAppGranted);
		setTelevisedApplicationMade(televisedApplicationMade);
		setTelevisedAppMadeDate(televisedAppMadeDate);
		setTelevisedAppRefusedFreetext(televisedAppRefusedFreetext);
		setTelevisedRemarksFilmed(televisedRemarksFilmed);
		setDarRetentionPolicyId(darRetentionPolicyId);
		setCrpLastUpdateDate(crpLastUpdateDate);
		return null;
	}

	public Integer ejbCreate(Integer caseNumber, String caseType, String caseTitle, String caseSubType,
			Integer classCode, Integer courtID, Integer monitoringCategoryId, Timestamp appealLodgedDate,
			Timestamp receivedDate, Timestamp sentForTrialDate, String ticketRequired, Integer ticketTypeCode,
			Integer courtIdReceivingSite, Timestamp committalDate, String receiptType, String eitherWayType,
			Integer noDefendantsForCase, String transferredCase, String transferDeferredSentence,
			Timestamp dateTransFrom, Timestamp dateTransTo, String originalCaseNumber, Integer cccTransFromRefCourtId,
			String retrial, String secureCourt, Timestamp preliminaryDateOfHearing, Integer refCourtId,
			String magistratesCaseRef, Integer magcourtHearingtypeRefId, String originalJps1, String originalJps2,
			String originalJps3, String originalJps4, String caseDescription, Integer policeForceCode,
			Integer noPageProsEvidence, Timestamp origBodyDecisionDate, String chargeImportIndicator,
			Timestamp magConvictionDate, Timestamp lcSentDate, String videoLinkRequired, Integer defaultHearingType,
			Integer crackedIneffectiveId, String section28Name1, String section28Name2, String section28Phone1,
			String section28Phone2,  String s28Eligible, String s28OrderMade) throws CreateException {

		setCaseNumber(caseNumber);
		setCaseType(caseType);
		setCaseTitle(caseTitle);
		setCaseSubType(caseSubType);
		setClassCode(classCode);
		setCourtId(courtID);
		setMonitoringCategoryId(monitoringCategoryId);
		setAppealLodgedDate(appealLodgedDate);
		setReceivedDate(receivedDate);
		setSentForTrialDate(sentForTrialDate);
		setTicketTypeCode(ticketTypeCode);
		setTicketRequired(ticketRequired);
		setCourtIdReceivingSite(courtIdReceivingSite);
		setCommittalDate(committalDate);
		setReceiptType(receiptType);
		setEitherWayType(eitherWayType);
		setNoDefendantsForCase(noDefendantsForCase);
		setTransferredCase(transferredCase);
		setTransferDeferredSentence(transferDeferredSentence);
		setDateTransFrom(dateTransFrom);
		setDateTransTo(dateTransTo);
		setOriginalCaseNumber(originalCaseNumber);
		setCccTransFromRefCourtId(cccTransFromRefCourtId);
		setRetrial(retrial);
		setSecureCourt(secureCourt);
		setPreliminaryDateOfHearing(preliminaryDateOfHearing);
		setRefCourtId(refCourtId);
		setMagistratesCaseRef(magistratesCaseRef);
		setMagCourtHearingTypeRefId(magcourtHearingtypeRefId);
		setOriginalJps1(originalJps1);
		setOriginalJps2(originalJps2);
		setOriginalJps3(originalJps3);
		setOriginalJps4(originalJps4);
		setCaseDescription(caseDescription);
		setPoliceForceCode(policeForceCode);
		setNoPageProsEvidence(noPageProsEvidence);
		setOrigBodyDecisionDate(origBodyDecisionDate);
		setChargeImportIndicator(chargeImportIndicator);
		setMagConvictionDate(magConvictionDate);
		setLcSentDate(lcSentDate);
		setVideoLinkRequired(videoLinkRequired);
		setDefaultHearingType(defaultHearingType);
		setCrackedIneffectiveId(crackedIneffectiveId);
		setSection28Name1(section28Name1);
		setSection28Name2(section28Name2);
		setSection28Phone1(section28Phone1);
		setSection28Phone2(section28Phone2);
		setS28Eligible(s28Eligible);
		setS28OrderMade(s28OrderMade);
		
		return null;
	}

	public Integer ejbCreate(Integer caseNumber, String caseType, Timestamp magConvictionDate, String caseSubType,
			String caseTitle, String caseDescription, Integer linkedCaseId, String bailMagCode, Integer refCourtId,
			Integer courtId, String chargeImportIndicator, String crestSeveredInd, String crestIndictResp,
			Integer caseClass, String prosAgencyRef, Timestamp dateIndRec, Integer estPDHTrialLength,
			Integer noProsWitness, Integer noPageProsEvidence, Integer lengthTape, String judgeReasonForAppeal,
			String resultsVerified, String indictmentInfo1, String indictmentInfo2, String indictmentInfo3,
			String indictmentInfo4, String indictmentInfo5, String indictmentInfo6, String cpsCaseWorker,
			String exportCharges, String policeOfficerAttending, String indChangeStatus, Integer classCode,
			String offenceGroup, java.lang.Integer cccTransToRefCourtId, java.lang.String receiptType,
			String userDisplayName) throws CreateException {

		setCaseNumber(caseNumber);
		setCaseType(caseType);
		setMagConvictionDate(magConvictionDate);
		setCaseSubType(caseSubType);
		setCaseTitle(caseTitle);
		setCaseDescription(caseDescription);
		setLinkedCaseId(linkedCaseId);
		setBailMagCode(bailMagCode);
		setRefCourtId(refCourtId);
		setCourtId(courtId);
		setChargeImportIndicator(chargeImportIndicator);
		setCrestSeveredInd(crestSeveredInd);
		setCrestIndictResp(crestIndictResp);
		setCaseClass(caseClass);
		setProsAgencyRef(prosAgencyRef);
		setDateIndRec(dateIndRec);
		setEstPDHTrialLength(estPDHTrialLength);
		setNoProsWitness(noProsWitness);
		setNoPageProsEvidence(noPageProsEvidence);
		setLengthTape(lengthTape);
		setJudgeReasonForAppeal(judgeReasonForAppeal);
		setResultsVerified(resultsVerified);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		setIndictmentInfo1(indictmentInfo1);
		setIndictmentInfo2(indictmentInfo2);
		setIndictmentInfo3(indictmentInfo3);
		setIndictmentInfo4(indictmentInfo4);
		setIndictmentInfo5(indictmentInfo5);
		setIndictmentInfo6(indictmentInfo6);
		setPoliceOfficerAttending(policeOfficerAttending);
		setCpsCaseWorker(cpsCaseWorker);
		setExportCharges(exportCharges);
		setIndChangeStatus(indChangeStatus);
		setClassCode(classCode);
		setOffenceGroup(offenceGroup);
		setCccTransToRefCourtId(cccTransToRefCourtId);
		setReceiptType(receiptType);
		return null;
	}

	public Integer ejbCreate(Integer caseNumber, String caseType, Timestamp magConvictionDate, String caseSubType,
			String caseTitle, String caseDescription, Integer linkedCaseId, String bailMagCode, Integer refCourtId,
			Integer courtId, String chargeImportIndicator, String crestSeveredInd, String crestIndictResp,
			Integer caseClass, String prosAgencyRef, Timestamp dateIndRec, Integer estPDHTrialLength,
			Integer noProsWitness, Integer noPageProsEvidence, Integer lengthTape, String judgeReasonForAppeal,
			String resultsVerified, String indictmentInfo1, String indictmentInfo2, String indictmentInfo3,
			String indictmentInfo4, String indictmentInfo5, String indictmentInfo6, String cpsCaseWorker,
			String exportCharges, String policeOfficerAttending, String indChangeStatus, Integer classCode,
			String offenceGroup, java.lang.Integer cccTransToRefCourtId, java.lang.String receiptType,
			String vulnerableVictimIndicator, String userDisplayName) throws CreateException {

		ejbCreate(caseNumber, caseType, magConvictionDate, caseSubType, caseTitle, caseDescription, linkedCaseId,
				bailMagCode, refCourtId, courtId, chargeImportIndicator, crestSeveredInd, crestIndictResp, caseClass,
				prosAgencyRef, dateIndRec, estPDHTrialLength, noProsWitness, noPageProsEvidence, lengthTape,
				judgeReasonForAppeal, resultsVerified, indictmentInfo1, indictmentInfo2, indictmentInfo3,
				indictmentInfo4, indictmentInfo5, indictmentInfo6, cpsCaseWorker, exportCharges, policeOfficerAttending,
				indChangeStatus, classCode, offenceGroup, cccTransToRefCourtId, receiptType, userDisplayName);
		setVulnerableVictimIndicator(vulnerableVictimIndicator);
		return null;
	}

	public Integer ejbCreate(Integer caseNumber, String caseType, Timestamp magConvictionDate, String caseSubType,
			String caseTitle, String caseDescription, Integer linkedCaseId, String bailMagCode, Integer refCourtId,
			Integer courtId, String chargeImportIndicator, String crestSeveredInd, String crestIndictResp,
			Integer caseClass, String prosAgencyRef, Timestamp dateIndRec, Integer estPDHTrialLength,
			Integer noProsWitness, Integer noPageProsEvidence, Integer lengthTape, String judgeReasonForAppeal,
			String resultsVerified, String indictmentInfo1, String indictmentInfo2, String indictmentInfo3,
			String indictmentInfo4, String indictmentInfo5, String indictmentInfo6, String cpsCaseWorker,
			String exportCharges, String policeOfficerAttending, String indChangeStatus, Integer classCode,
			String offenceGroup, java.lang.Integer cccTransToRefCourtId, java.lang.String receiptType,
			String vulnerableVictimIndicator, String publicDisplayHide, String userDisplayName) throws CreateException {

		ejbCreate(caseNumber, caseType, magConvictionDate, caseSubType, caseTitle, caseDescription, linkedCaseId,
				bailMagCode, refCourtId, courtId, chargeImportIndicator, crestSeveredInd, crestIndictResp, caseClass,
				prosAgencyRef, dateIndRec, estPDHTrialLength, noProsWitness, noPageProsEvidence, lengthTape,
				judgeReasonForAppeal, resultsVerified, indictmentInfo1, indictmentInfo2, indictmentInfo3,
				indictmentInfo4, indictmentInfo5, indictmentInfo6, cpsCaseWorker, exportCharges, policeOfficerAttending,
				indChangeStatus, classCode, offenceGroup, cccTransToRefCourtId, receiptType, userDisplayName);
		setVulnerableVictimIndicator(vulnerableVictimIndicator);
		setPublicDisplayHide(publicDisplayHide);
		return null;
	}

	public Integer ejbCreate(Integer caseNumber, String caseType, String caseStatus, Timestamp magConvictionDate,
			String caseSubType, String caseTitle, String caseDescription, Integer linkedCaseId, String bailMagCode,
			Integer refCourtId, Integer courtId, String chargeImportIndicator, String crestSeveredInd,
			String crestIndictResp, Integer caseClass, String prosAgencyRef, Timestamp dateIndRec,
			Integer estPDHTrialLength, Integer noProsWitness, Integer noPageProsEvidence, Integer lengthTape,
			String judgeReasonForAppeal, String resultsVerified, String indictmentInfo1, String indictmentInfo2,
			String indictmentInfo3, String indictmentInfo4, String indictmentInfo5, String indictmentInfo6,
			String cpsCaseWorker, String exportCharges, String policeOfficerAttending, String indChangeStatus,
			Integer classCode, String offenceGroup, java.lang.Integer cccTransToRefCourtId,
			java.lang.String receiptType, String vulnerableVictimIndicator, String publicDisplayHide,
			String userDisplayName) throws CreateException {

		ejbCreate(caseNumber, caseType, magConvictionDate, caseSubType, caseTitle, caseDescription, linkedCaseId,
				bailMagCode, refCourtId, courtId, chargeImportIndicator, crestSeveredInd, crestIndictResp, caseClass,
				prosAgencyRef, dateIndRec, estPDHTrialLength, noProsWitness, noPageProsEvidence, lengthTape,
				judgeReasonForAppeal, resultsVerified, indictmentInfo1, indictmentInfo2, indictmentInfo3,
				indictmentInfo4, indictmentInfo5, indictmentInfo6, cpsCaseWorker, exportCharges, policeOfficerAttending,
				indChangeStatus, classCode, offenceGroup, cccTransToRefCourtId, receiptType, vulnerableVictimIndicator,
				publicDisplayHide, userDisplayName);
		setCaseStatus(caseStatus);
		return null;
	}

	@SuppressWarnings("unused")
	public void ejbPostCreate(Integer caseNumber, String caseType, String caseTitle, String caseSubType,
			Integer classCode, Integer courtID, Integer monitoringCategoryId, Timestamp appealLodgedDate,
			Timestamp receivedDate, Timestamp sentForTrialDate, String ticketRequired, Integer ticketTypeCode,
			Integer courtIdReceivingSite, Timestamp committalDate, String receiptType, String eitherWayType,
			Integer noDefendantsForCase, String transferredCase, String transferDeferredSentence,
			Timestamp dateTransFrom, Timestamp dateTransTo, String originalCaseNumber, Integer cccTransFromRefCourtId,
			String retrial, String secureCourt, Timestamp preliminaryDateOfHearing, Integer refCourtId,
			String magistratesCaseRef, Integer magcourtHearingtypeRefId, String originalJps1, String originalJps2,
			String originalJps3, String originalJps4, String caseDescription, String caseListed,
			Integer policeForceCode, Integer noPageProsEvidence, Timestamp origBodyDecisionDate,
			String chargeImportIndicator, Timestamp magConvictionDate, Timestamp lcSentDate, String videoLinkRequired,
			Integer defaultHearingType, Integer crackedIneffectiveId, String section28Name1, String section28Name2,
			String section28Phone1, String section28Phone2,  String s28Eligible, String s28OrderMade, String userDisplayName, Integer caseGroupNumber,
			String civilUnrest, String televisedAppGranted, String televisedApplicationMade, Timestamp televisedAppMadeDate,
			String televisedAppRefusedFreetext, String televisedRemarksFilmed,
			Integer darRetentionPolicyId, Timestamp crpLastUpdateDate) throws CreateException {
		// Empty
	}

	@SuppressWarnings("unused")
	public void ejbPostCreate(Integer caseNumber, String caseType, Timestamp magConvictionDate, String caseSubType,
			String caseTitle, String caseDescription, Integer linkedCaseId, String bailMagCode, Integer refCourtId,
			Integer courtId, String chargeImportIndicator, String crestSeveredInd, String crestIndictResp,
			Integer caseClass, String prosAgencyRef, Timestamp dateIndRec, Integer estPDHTrialLength,
			Integer noProsWitness, Integer noPageProsEvidence, Integer lengthTape, String judgeReasonForAppeal,
			String resultsVerified, String indictmentInfo1, String indictmentInfo2, String indictmentInfo3,
			String indictmentInfo4, String indictmentInfo5, String indictmentInfo6, String cpsCaseWorker,
			String exportCharges, String policeOfficerAttending, String indChangeStatus, Integer classCode,
			String offenceGroup, java.lang.Integer cccTransToRefCourtId, java.lang.String receiptType,
			String userDisplayName) throws CreateException {
		// Empty
	}

	@SuppressWarnings("unused")
	public void ejbPostCreate(Integer caseNumber, String caseType, Timestamp magConvictionDate, String caseSubType,
			String caseTitle, String caseDescription, Integer linkedCaseId, String bailMagCode, Integer refCourtId,
			Integer courtId, String chargeImportIndicator, String crestSeveredInd, String crestIndictResp,
			Integer caseClass, String prosAgencyRef, Timestamp dateIndRec, Integer estPDHTrialLength,
			Integer noProsWitness, Integer noPageProsEvidence, Integer lengthTape, String judgeReasonForAppeal,
			String resultsVerified, String indictmentInfo1, String indictmentInfo2, String indictmentInfo3,
			String indictmentInfo4, String indictmentInfo5, String indictmentInfo6, String cpsCaseWorker,
			String exportCharges, String policeOfficerAttending, String indChangeStatus, Integer classCode,
			String offenceGroup, java.lang.Integer cccTransToRefCourtId, java.lang.String receiptType,
			String vulnerableVictimIndicator, String userDisplayName) throws CreateException {
		// Empty
	}

	@SuppressWarnings("unused")
	public void ejbPostCreate(Integer caseNumber, String caseType, Timestamp magConvictionDate, String caseSubType,
			String caseTitle, String caseDescription, Integer linkedCaseId, String bailMagCode, Integer refCourtId,
			Integer courtId, String chargeImportIndicator, String crestSeveredInd, String crestIndictResp,
			Integer caseClass, String prosAgencyRef, Timestamp dateIndRec, Integer estPDHTrialLength,
			Integer noProsWitness, Integer noPageProsEvidence, Integer lengthTape, String judgeReasonForAppeal,
			String resultsVerified, String indictmentInfo1, String indictmentInfo2, String indictmentInfo3,
			String indictmentInfo4, String indictmentInfo5, String indictmentInfo6, String cpsCaseWorker,
			String exportCharges, String policeOfficerAttending, String indChangeStatus, Integer classCode,
			String offenceGroup, java.lang.Integer cccTransToRefCourtId, java.lang.String receiptType,
			String vulnerableVictimIndicator, String publicDisplayHide, String userDisplayName) throws CreateException {
		// Empty
	}

	public void ejbPostCreate(Integer caseNumber, String caseType, String caseTitle, String caseSubType,
			Integer classCode, Integer courtID, Integer monitoringCategoryId, Timestamp appealLodgedDate,
			Timestamp receivedDate, Timestamp sentForTrialDate, String ticketRequired, Integer ticketTypeCode,
			Integer courtIdReceivingSite, Timestamp committalDate, String receiptType, String eitherWayType,
			Integer noDefendantsForCase, String transferredCase, String transferDeferredSentence,
			Timestamp dateTransFrom, Timestamp dateTransTo, String originalCaseNumber, Integer cccTransFromRefCourtId,
			String retrial, String secureCourt, Timestamp preliminaryDateOfHearing, Integer refCourtId,
			String magistratesCaseRef, Integer magcourtHearingtypeRefId, String originalJps1, String originalJps2,
			String originalJps3, String originalJps4, String caseDescription, Integer policeForceCode,
			Integer noPageProsEvidence, Timestamp origBodyDecisionDate, String chargeImportIndicator,
			Timestamp magConvictionDate, Timestamp lcSentDate, String videoLinkRequired, Integer defaultHearingType,
			Integer crackedIneffectiveId, String section28Name1, String section28Name2, String section28Phone1,
			String section28Phone2, String s28Eligible, String s28OrderMade) throws CreateException {
		// --- Empty ---
	}

	public void ejbPostCreate(Integer caseNumber, String caseType, String caseStatus, Timestamp magConvictionDate,
			String caseSubType, String caseTitle, String caseDescription, Integer linkedCaseId, String bailMagCode,
			Integer refCourtId, Integer courtId, String chargeImportIndicator, String crestSeveredInd,
			String crestIndictResp, Integer caseClass, String prosAgencyRef, Timestamp dateIndRec,
			Integer estPDHTrialLength, Integer noProsWitness, Integer noPageProsEvidence, Integer lengthTape,
			String judgeReasonForAppeal, String resultsVerified, String indictmentInfo1, String indictmentInfo2,
			String indictmentInfo3, String indictmentInfo4, String indictmentInfo5, String indictmentInfo6,
			String cpsCaseWorker, String exportCharges, String policeOfficerAttending, String indChangeStatus,
			Integer classCode, String offenceGroup, java.lang.Integer cccTransToRefCourtId,
			java.lang.String receiptType, String vulnerableVictimIndicator, String publicDisplayHide,
			String userDisplayName) throws CreateException {
		// --- Empty ---
	}

	// ------------------------------CMP
	// Fields------------------------------------
	public abstract void setVulnerableVictimIndicator(String vulnerableVictimIndicator);

	public abstract String getVulnerableVictimIndicator();

	public abstract void setCaseId(Integer caseId);

	public abstract Integer getCaseId();

	public abstract void setCaseNumber(Integer caseNumber);

	public abstract Integer getCaseNumber();

	public abstract void setCaseType(String caseType);

	public abstract String getCaseType();

	public abstract void setCaseStatus(String caseStatus);

	public abstract String getCaseStatus();

	public abstract void setMagConvictionDate(Timestamp magConvictionDate);

	public abstract Timestamp getMagConvictionDate();

	public abstract void setCaseSubType(String caseSubType);

	public abstract String getCaseSubType();

	public abstract void setCaseTitle(String caseTitle);

	public abstract String getCaseTitle();

	public abstract void setCaseDescription(String caseDescription);

	public abstract String getCaseDescription();

	public abstract void setLinkedCaseId(Integer linkedCaseId);

	public abstract Integer getLinkedCaseId();

	public abstract void setBailMagCode(String bailMagCode);

	public abstract String getBailMagCode();

	public abstract void setRefCourtId(Integer refCourtId);

	public abstract Integer getRefCourtId();

	public abstract void setCourtId(Integer courtId);

	public abstract Integer getCourtId();

	public abstract void setChargeImportIndicator(String chargeImportIndicator);

	public abstract String getChargeImportIndicator();

	public abstract void setCrestSeveredInd(String crestSeveredInd);

	public abstract String getCrestSeveredInd();

	public abstract void setCrestIndictResp(String crestIndictResp);

	public abstract String getCrestIndictResp();

	public abstract void setCaseClass(Integer caseClass);

	public abstract Integer getCaseClass();

	public abstract void setProsAgencyRef(String prosAgencyRef);

	public abstract String getProsAgencyRef();

	public abstract void setDateIndRec(Timestamp dateIndRec);

	public abstract Timestamp getDateIndRec();

	public abstract void setEstPDHTrialLength(Integer estPDHTrialLength);

	public abstract Integer getEstPDHTrialLength();

	public abstract void setNoProsWitness(Integer noProsWitness);

	public abstract Integer getNoProsWitness();

	public abstract void setNoPageProsEvidence(Integer noPageProsEvidence);

	public abstract Integer getNoPageProsEvidence();

	public abstract void setLengthTape(Integer lengthTape);

	public abstract Integer getLengthTape();

	public abstract void setJudgeReasonForAppeal(String judgeReasonForAppeal);

	public abstract String getJudgeReasonForAppeal();

	public abstract void setResultsVerified(String resultsVerified);

	public abstract String getResultsVerified();

	public abstract void setIndictmentInfo1(String indictmentInfo1);

	public abstract String getIndictmentInfo1();

	public abstract void setIndictmentInfo2(String indictmentInfo2);

	public abstract String getIndictmentInfo2();

	public abstract void setIndictmentInfo3(String indictmentInfo3);

	public abstract String getIndictmentInfo3();

	public abstract void setIndictmentInfo4(String indictmentInfo4);

	public abstract String getIndictmentInfo4();

	public abstract void setIndictmentInfo5(String indictmentInfo5);

	public abstract String getIndictmentInfo5();

	public abstract void setIndictmentInfo6(String indictmentInfo6);

	public abstract String getIndictmentInfo6();

	public abstract void setCpsCaseWorker(String cpsCaseWorker);

	public abstract String getCpsCaseWorker();

	public abstract void setExportCharges(String exportCharges);

	public abstract String getExportCharges();

	public abstract void setPoliceOfficerAttending(String policeOfficerAttending);

	public abstract String getPoliceOfficerAttending();

	public abstract void setIndChangeStatus(String indChangeStatus);

	public abstract String getIndChangeStatus();

	public abstract void setClassCode(Integer classCode);

	public abstract Integer getClassCode();

	public abstract void setOffenceGroup(String offenceGroup);

	public abstract String getOffenceGroup();

	public abstract void setPublicDisplayHide(java.lang.String publicDisplayHide);

	public abstract String getPublicDisplayHide();

	// ------------------------------CMR
	// Fields------------------------------------
	public abstract void setLinkedCase(uk.gov.courtservice.xhibit.business.entities.linkedcase.LinkedCase linkedCase);

	public abstract uk.gov.courtservice.xhibit.business.entities.linkedcase.LinkedCase getLinkedCase();

	public abstract void setDefendantOnCases(java.util.Collection defendantOnCases);

	public abstract java.util.Collection getDefendantOnCases();

	public abstract void setCaseReference(
			uk.gov.courtservice.xhibit.business.entities.casereference.CaseReference caseReference);

	public abstract uk.gov.courtservice.xhibit.business.entities.casereference.CaseReference getCaseReference();

	public abstract void setCccTransToRefCourtId(java.lang.Integer cccTransToRefCourtId);

	public abstract java.lang.Integer getCccTransToRefCourtId();

	public abstract void setReceiptType(java.lang.String receiptType);

	public abstract java.lang.String getReceiptType();
	
	public abstract void setRefusedBroadcastCase(java.util.Collection refusedBroadcastCase);

	public abstract java.util.Collection getRefusedBroadcastCase();

	// ----------------new fields added============
	public abstract void setMonitoringCategoryId(Integer monitoringCategoryId);

	public abstract Integer getMonitoringCategoryId();

	public abstract void setAppealLodgedDate(Timestamp appealLodgedDate);

	public abstract Timestamp getAppealLodgedDate();

	public abstract void setReceivedDate(Timestamp receivedDate);

	public abstract Timestamp getReceivedDate();

	public abstract void setSentForTrialDate(Timestamp sentForTrialDate);

	public abstract Timestamp getSentForTrialDate();

	public abstract void setTicketRequired(String ticketRequired);

	public abstract String getTicketRequired();

	public abstract void setTicketTypeCode(Integer ticketTypeCode);

	public abstract Integer getTicketTypeCode();

	public abstract void setCourtIdReceivingSite(Integer courtIdReceivingSite);

	public abstract Integer getCourtIdReceivingSite();

	public abstract void setCommittalDate(Timestamp committalDate);

	public abstract Timestamp getCommittalDate();

	public abstract void setEitherWayType(String eitherWayType);

	public abstract String getEitherWayType();

	public abstract void setNoDefendantsForCase(Integer noDefendantsForCase);

	public abstract Integer getNoDefendantsForCase();

	public abstract void setTransferredCase(String transferredCase);

	public abstract String getTransferredCase();

	public abstract void setTransferDeferredSentence(String transferDeferredSentence);

	public abstract String getTransferDeferredSentence();

	public abstract void setDateTransFrom(Timestamp dateTransFrom);

	public abstract Timestamp getDateTransFrom();

	public abstract void setDateTransTo(Timestamp dateTransTo);

	public abstract Timestamp getDateTransTo();

	public abstract void setOriginalCaseNumber(String originalCaseNumber);

	public abstract String getOriginalCaseNumber();

	public abstract void setCccTransFromRefCourtId(Integer cccTransFromRefCourtId);

	public abstract Integer getCccTransFromRefCourtId();

	public abstract void setRetrial(String retrial);

	public abstract String getRetrial();

	public abstract void setSecureCourt(String secureCourt);

	public abstract String getSecureCourt();

	public abstract void setVideoLinkRequired(String videoLinkRequired);

	public abstract String getVideoLinkRequired();

	public abstract void setPreliminaryDateOfHearing(Timestamp preliminaryDateOfHearing);

	public abstract Timestamp getPreliminaryDateOfHearing();

	public abstract void setMagistratesCaseRef(String magistratesCaseRef);

	public abstract String getMagistratesCaseRef();

	public abstract Integer getMagCourtHearingTypeRefId();

	public abstract void setMagCourtHearingTypeRefId(Integer magCourtHearingTypeRefId);

	public abstract void setOriginalJps1(String originalJps1);

	public abstract String getOriginalJps1();

	public abstract void setOriginalJps2(String originalJps2);

	public abstract String getOriginalJps2();

	public abstract void setOriginalJps3(String originalJps3);

	public abstract String getOriginalJps3();

	public abstract void setOriginalJps4(String originalJps4);

	public abstract String getOriginalJps4();

	public abstract void setCaseListed(String caseListed);

	public abstract String getCaseListed();

	public abstract void setPoliceForceCode(Integer policeForceCode);

	public abstract Integer getPoliceForceCode();

	public abstract void setOrigBodyDecisionDate(Timestamp origBodyDecisionDate);

	public abstract Timestamp getOrigBodyDecisionDate();

	public abstract void setLcSentDate(Timestamp lcSentDate);

	public abstract Timestamp getLcSentDate();

	public abstract void setDefaultHearingType(Integer defaultHearingType);

	public abstract Integer getDefaultHearingType();

	public abstract void setCrackedIneffectiveId(Integer crackedIneffectiveId);

	public abstract Integer getCrackedIneffectiveId();

	public abstract void setSection28Name1(String section28Name1);

	public abstract String getSection28Name1();

	public abstract void setSection28Name2(String section28Name2);

	public abstract String getSection28Name2();

	public abstract void setSection28Phone1(String section28Phone1);

	public abstract String getSection28Phone1();

	public abstract void setSection28Phone2(String section28Phone2);

	public abstract String getSection28Phone2();
	
	public abstract void setS28Eligible(String s28Eligible);

	public abstract String getS28Eligible();
	
	public abstract void setS28OrderMade(String s28OrderMade);

	public abstract String getS28OrderMade();

	public abstract void setPubRunningListId(Integer pubRunningListId);

	public abstract Integer getPubRunningListId();
	
	public abstract void setCaseGroupNumber(Integer caseGroupNumber);
	
	public abstract Integer getCaseGroupNumber();
	
	
	
	public abstract void setMonetaryOrderTrackings(java.util.Collection monetaryOrderTrackings);

	public abstract java.util.Collection getMonetaryOrderTrackings();
	
	public abstract Timestamp getDateTransRecordedTo();

	public abstract void setDateTransRecordedTo(Timestamp dateTransRecordedTo);

	public abstract String getCivilUnrest();
	
	public abstract void setCivilUnrest(String civilUnrest);
	
	public abstract String getTelevisedApplicationMade();

	public abstract void setTelevisedApplicationMade(String televisedApplicationMade);

	public abstract Timestamp getTelevisedAppMadeDate();

	public abstract void setTelevisedAppMadeDate(Timestamp televisedAppMadeDate);

	public abstract String getTelevisedAppGranted();

	public abstract void setTelevisedAppGranted(String televisedAppGranted);

	public abstract String getTelevisedAppRefusedFreetext();

	public abstract void setTelevisedAppRefusedFreetext(String televisedAppRefusedFreetext);

	public abstract String getTelevisedRemarksFilmed();

	public abstract void setTelevisedRemarksFilmed(String televisedRemarksFilmed);
	
	public abstract Timestamp getLastUpdateDate();

	public abstract void setLastUpdateDate(Timestamp lastUpdateDate);
	
    public abstract Integer getDarRetentionPolicyId();
	
	public abstract void setDarRetentionPolicyId(Integer darRetentionPolicyId);
	
	public abstract Timestamp getCrpLastUpdateDate();

	public abstract void setCrpLastUpdateDate(Timestamp crpLastUpdateDate);
	
	public abstract Date getCreationDate();

	public abstract void setCreationDate(Date creationDate);
}
