package uk.gov.courtservice.xhibit.business.entities.caze;

// jdk
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface CaseHome extends javax.ejb.EJBLocalHome {
	
	public Case create(Integer caseNumber, String caseType, String caseTitle, String caseSubType, Integer classCode, Integer courtID,
			  Integer monitoringCategoryId, Timestamp appealLodgedDate, Timestamp receivedDate, Timestamp sentForTrialDate, 
			  String ticketRequired, Integer ticketTypeCode, Integer courtIdReceivingSite, Timestamp committalDate, String receiptType, 
			  String eitherWayType, Integer noDefendantsForCase, String transferredCase, String transferDeferredSentence, 
			  Timestamp dateTransFrom, Timestamp dateTransTo, String originalCaseNumber, Integer cccTransFromRefCourtId,
			  String retrial, String secureCourt, Timestamp preliminaryDateOfHearing, 
			  Integer refCourtId, String magistratesCaseRef, Integer magcourtHearingtypeRefId, String originalJps1, 
			  String originalJps2, String originalJps3, String originalJps4, String caseDescription, String caseListed, 
			  Integer policeForceCode, Integer noPageProsEvidence, Timestamp origBodyDecisionDate, String chargeImportIndicator,
			  Timestamp magConvictionDate, Timestamp lcSentDate, String videoLinkRequired, Integer defaultHearingType,
			  Integer crackedIneffectiveId, String section28Name1, String section28Name2, String section28Phone1, String section28Phone2, 
			  String s28Eligible, String s28OrderMade, String userDisplayName, Integer caseGroupNumber,String civilUnrest,
			  String televisedAppGranted, String televisedApplicationMade, Timestamp televisedAppMadeDate, 
			  String televisedAppRefusedFreetext, String televisedRemarksFilmed,
			  Integer darRetentionPolicyId, Timestamp crpLastUpdateDate) throws CreateException ;
	
	public Case create(Integer caseNumber, String caseType, String caseTitle, String caseSubType, Integer classCode, Integer courtID,
			  Integer monitoringCategoryId, Timestamp appealLodgedDate, Timestamp receivedDate, Timestamp sentForTrialDate, 
			  String ticketRequired, Integer ticketTypeCode, Integer courtIdReceivingSite, Timestamp committalDate, String receiptType, 
			  String eitherWayType, Integer noDefendantsForCase, String transferredCase, String transferDeferredSentence, 
			  Timestamp dateTransFrom, Timestamp dateTransTo, String originalCaseNumber, Integer cccTransFromRefCourtId,
			  String retrial, String secureCourt, Timestamp preliminaryDateOfHearing, 
			  Integer refCourtId, String magistratesCaseRef, Integer magcourtHearingtypeRefId, String originalJps1, 
			  String originalJps2, String originalJps3, String originalJps4, String caseDescription, 
			  Integer policeForceCode, Integer noPageProsEvidence, Timestamp origBodyDecisionDate, String chargeImportIndicator,
			  Timestamp magConvictionDate, Timestamp lcSentDate, String videoLinkRequired, Integer defaultHearingType,
			  Integer crackedIneffectiveId, String section28Name1, String section28Name2, String section28Phone1, String section28Phone2,
			  String s28Eligible, String s28OrderMade) throws CreateException ;
	
    public Case create(Integer caseNumber, String caseType, Timestamp magConvictionDate,
            String caseSubType, String caseTitle, String caseDescription, Integer linkedCaseId, String bailMagCode,
            Integer refCourtId, Integer courtId, String chargeImportIndicator, String crestServedInd,
            String crestIndictResp, Integer caseClass, String prosAgencyRef,
            Timestamp dateIndRec, Integer estPDHTrailLength, Integer noProsWitness, Integer noPageProsEvidence,
            Integer lengthTape, String judgeReasonForAppeal, String resultsVerified, String indictmentInfo1,
            String indictmentInfo2, String indictmentInfo3, String indictmentInfo4, String indictmentInfo5,
            String indictmentInfo6, String policeOfficerAttending, String cpsCaseWorker, String exportCharges,
            String indChangeStatus, Integer classCode, String offenceGroup, Integer cccTransToRefCourtId,
            String receiptType, String userDisplayName) throws CreateException;
    
     //created by KD for CCN1236 - to include the new VulnerablVictimIndicator field/column
    public Case create(Integer caseNumber, String caseType, Timestamp magConvictionDate,
            String caseSubType, String caseTitle, String caseDescription, Integer linkedCaseId, String bailMagCode,
            Integer refCourtId, Integer courtId, String chargeImportIndicator, String crestServedInd,
            String crestIndictResp, Integer caseClass, String prosAgencyRef,
            Timestamp dateIndRec, Integer estPDHTrailLength, Integer noProsWitness, Integer noPageProsEvidence,
            Integer lengthTape, String judgeReasonForAppeal, String resultsVerified, String indictmentInfo1,
            String indictmentInfo2, String indictmentInfo3, String indictmentInfo4, String indictmentInfo5,
            String indictmentInfo6, String policeOfficerAttending, String cpsCaseWorker, String exportCharges,
            String indChangeStatus, Integer classCode, String offenceGroup, Integer cccTransToRefCourtId,
            String receiptType, String vulnerableVictimIndicator, String userDisplayName) throws CreateException;

    public Case create(Integer caseNumber, String caseType, Timestamp magConvictionDate,
            String caseSubType, String caseTitle, String caseDescription, Integer linkedCaseId, String bailMagCode,
            Integer refCourtId, Integer courtId, String chargeImportIndicator, String crestServedInd,
            String crestIndictResp, Integer caseClass, String prosAgencyRef,
            Timestamp dateIndRec, Integer estPDHTrailLength, Integer noProsWitness, Integer noPageProsEvidence,
            Integer lengthTape, String judgeReasonForAppeal, String resultsVerified, String indictmentInfo1,
            String indictmentInfo2, String indictmentInfo3, String indictmentInfo4, String indictmentInfo5,
            String indictmentInfo6, String policeOfficerAttending, String cpsCaseWorker, String exportCharges,
            String indChangeStatus, Integer classCode, String offenceGroup, Integer cccTransToRefCourtId,
            String receiptType, String vulnerableVictimIndicator, String publicDisplayHide, String userDisplayName) throws CreateException;
    
    public Case findByPrimaryKey(Integer caseId) throws FinderException;

    public Case findByNumberTypeAndCourt(Integer caseId, String caseType, Integer courtId) throws FinderException;

    public Case findByKeyAndVersion(Integer caseId, Integer version) throws FinderException;

    public Collection findByLinkedCaseId(Integer linkedCaseId) throws FinderException;
    
    public Collection findAllCasesByDefendantId(Integer defendantId, Integer courtId) throws FinderException;

    public Collection findByDefendantNamesAndCaseNumber( String defendantFirstName, String defendantSurname, String caseType, String caseNumber, Integer courtId) throws FinderException;

	public Collection findByDefendantSurnameAndCaseNumber(String defendantName, String caseType, String caseNumber, Integer courtId) throws FinderException;
}