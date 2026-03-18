package uk.gov.courtservice.xhibit.business.entities.defendantoncase;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.defendant.Defendant;

public interface DefendantOnCaseHome extends javax.ejb.EJBLocalHome {

    public DefendantOnCase create(Integer noOfTics, Integer finalDrivingLicenceStatus, String ptiurn,
            String isJuvenile, String isMasked, String maskedName, Integer caseId, String obsInd, Integer defendantId,
            String resultsVerified, Integer defendantNumber, Timestamp dateOfCommittal, 
            Integer collectMagistrateCourtId, String asn, String userDisplayName)
            throws CreateException;
    
    // Orders Controller Bean
    public DefendantOnCase create(Case thisCase, Defendant aDefendant, Integer noOfTics, Integer finalDrivingLicenceStatus, String ptiurn,
            String isJuvenile, String isMasked, String maskedName, String obsInd, Integer defendantId,
            String resultsVerified, Integer defendantNumber, Timestamp dateOfCommittal, 
            Integer collectMagistrateCourtId, String asn, String userDisplayName)
            throws CreateException;
    
    //CCN0400 - KD - START
    public DefendantOnCase create(Integer noOfTics, Integer finalDrivingLicenceStatus, String ptiurn,
            String isJuvenile, String isMasked, String maskedName, Integer caseId, String obsInd, Integer defendantId,
            String resultsVerified, Integer defendantNumber, Timestamp dateOfCommittal, 
            Integer collectMagistrateCourtId, String asn, String custodial, String suspended, String seriousDrugOffence,
            String recommendedDeportation, String nationality,Timestamp dateExported, String userDisplayName)
            throws CreateException;
    //CCN0400 - KD - END

    public DefendantOnCase create(Integer noOfTics, Integer finalDrivingLicenceStatus, String ptiurn,
            String isJuvenile, String isMasked, String maskedName, Integer caseId, String obsInd, Integer defendantId,
            String resultsVerified, Integer defendantNumber, Timestamp dateOfCommittal, 
            Integer collectMagistrateCourtId, String asn, String custodial, String suspended, String seriousDrugOffence,
            String recommendedDeportation, String nationality, Timestamp dateExported, String publicDisplayHide,
            Timestamp amendedDateExported, String amendedReason, String userDisplayName)
            throws CreateException;
    
    public DefendantOnCase create(Integer noOfTics, Integer finalDrivingLicenceStatus, String ptiurn,
            String isJuvenile, String isMasked, String maskedName, Integer caseId, String obsInd, Integer defendantId,
            String resultsVerified, Integer defendantNumber, Timestamp dateOfCommittal, 
            Integer collectMagistrateCourtId, String asn, String custodial, String suspended, String seriousDrugOffence,
            String recommendedDeportation, String nationality, Timestamp dateExported, String publicDisplayHide,
            Timestamp amendedDateExported, String amendedReason, String hateInd,
            String hateType, String hateSentInd, String userDisplayName)
            throws CreateException;
    
    public DefendantOnCase create(Integer noOfTics, Integer finalDrivingLicenceStatus, String ptiurn,
            String isJuvenile, String isMasked, String maskedName, Integer caseId, String obsInd, Integer defendantId,
            String resultsVerified, Integer defendantNumber, Timestamp dateOfCommittal, 
            Integer collectMagistrateCourtId, String asn, String custodial, String suspended, String seriousDrugOffence,
            String recommendedDeportation, String nationality, Timestamp dateExported, String publicDisplayHide,
            Timestamp amendedDateExported, String amendedReason, String hateInd,
            String hateType, String hateSentInd, String userDisplayName, String differenceReport)
            throws CreateException;
    
    //case create
    public DefendantOnCase create(Case thisCase, Defendant defendant, String asn, String currentBcStatus, 
    		String isJuvenile, Timestamp drivingDisqSuspendedDate, String pncId, String ptiurn, String isMasked, String maskedName,
    		String hateInd, String hateType, Timestamp benchWarrantExecDate, String bcStatusBwExecuted, Timestamp magCourtFirstHearingDate, 
    		Timestamp magCourtFinalHearingDate, String nationality, String section28Name1, String section28Name2, String section28Phone1,
    		String section28Phone2,Timestamp custodyTimeLimit, String commBcStatus, String userDisplayName)
            throws CreateException;
 
    
    // ctx-1922 adding defendant number
    public DefendantOnCase create(Case thisCase, Defendant defendant, String asn, String currentBcStatus, 
    		String isJuvenile, Timestamp drivingDisqSuspendedDate, String pncId, String ptiurn, String isMasked, String maskedName,
    		String hateInd, String hateType, Timestamp benchWarrantExecDate, String bcStatusBwExecuted, Timestamp magCourtFirstHearingDate, 
    		Timestamp magCourtFinalHearingDate, String nationality, String section28Name1, String section28Name2, String section28Phone1,
    		String section28Phone2,Timestamp custodyTimeLimit, String commBcStatus, String userDisplayName, int defendantNumber, Timestamp dateOfCommittal, String ctlApplies,
    		Integer darRetentionPolicyId)
            throws CreateException;
 
    public DefendantOnCase findByPrimaryKey(Integer defendantOnCaseId) throws FinderException;

    public Collection findByCaseId(Integer caseId) throws FinderException;

    public DefendantOnCase findByDefendantAndCase(Integer defendantId, Integer caseId) throws FinderException;

    public Collection findByDefendantAndCaseIncludeObsolete(Integer defendantId, Integer caseId) throws FinderException;

    public DefendantOnCase findByKeyAndVersion(Integer defendantOnCaseId, Integer version) throws FinderException;
    
    public Collection findByDefendantIdInCustody(Integer defendantId) throws FinderException;

    //view case details
    public Collection findByDefendantId(Integer defendantId) throws FinderException;
    
    // automatic case linking query 
    public Collection findDefendantOnActiveCases(Integer defendantId) throws FinderException;
}
