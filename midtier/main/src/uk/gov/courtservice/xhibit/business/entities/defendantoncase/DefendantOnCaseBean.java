package uk.gov.courtservice.xhibit.business.entities.defendantoncase;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.defendant.Defendant;

abstract public class DefendantOnCaseBean extends CSEntityBean {

	//case create
    public Integer ejbCreate(Case thisCase, Defendant defendant, String asn, String currentBcStatus, 
    		String isJuvenile, Timestamp drivingDisqSuspendedDate, String pncId, String ptiurn, String isMasked, String maskedName,
    		String hateInd, String hateType, Timestamp benchWarrantExecDate, String bcStatusBwExecuted, Timestamp magCourtFirstHearingDate, 
    		Timestamp magCourtFinalHearingDate, String nationality, String section28Name1, String section28Name2, String section28Phone1,
    		String section28Phone2,Timestamp custodyTimeLimit, String commBcStatus, String userDisplayName)
            throws CreateException {
    	
        setAsn(asn);
        setCurrentBcStatus(currentBcStatus);
        setCommBcStatus(commBcStatus);
        setIsJuvenile(isJuvenile);
        setDrivingDisqSuspendedDate(drivingDisqSuspendedDate);
        setPncId(pncId);
        setPtiurn(ptiurn);
        setIsMasked(isMasked);
        setMaskedName(maskedName);
        setHateIndicator(hateInd);
        setHateType(hateType);
        setBenchWarrantExecDate(benchWarrantExecDate);
        setBcStatusBwExecuted(bcStatusBwExecuted);
        setMagCourtFirstHearingDate(magCourtFirstHearingDate);
        setMagCourtFinalHearingDate(magCourtFinalHearingDate);
        setNationality(nationality);
        setSection28Name1(section28Name1);
        setSection28Name2(section28Name2); 
        setSection28Phone1(section28Phone1);
		setSection28Phone2(section28Phone2);
		setCustodyTimeLimit(custodyTimeLimit);
        setCreatedBy(userDisplayName);
    	return null;
    }

    public Integer ejbCreate(Case thisCase, Defendant defendant, String asn, String currentBcStatus, 
    		String isJuvenile, Timestamp drivingDisqSuspendedDate, String pncId, String ptiurn, String isMasked, String maskedName,
    		String hateInd, String hateType, Timestamp benchWarrantExecDate, String bcStatusBwExecuted, Timestamp magCourtFirstHearingDate, 
    		Timestamp magCourtFinalHearingDate, String nationality, String section28Name1, String section28Name2, String section28Phone1,
    		String section28Phone2,Timestamp custodyTimeLimit, String commBcStatus, String userDisplayName, int defendantNumber, Timestamp dateOfCommittal, String ctlApplies,
    		Integer darRetentionPolicyId)
            throws CreateException {
    	
        setAsn(asn);
        setCurrentBcStatus(currentBcStatus);
        setCommBcStatus(commBcStatus);
        setIsJuvenile(isJuvenile);
        setDrivingDisqSuspendedDate(drivingDisqSuspendedDate);
        setPncId(pncId);
        setPtiurn(ptiurn);
        setIsMasked(isMasked);
        setMaskedName(maskedName);
        setHateIndicator(hateInd);
        setHateType(hateType);
        setBenchWarrantExecDate(benchWarrantExecDate);
        setBcStatusBwExecuted(bcStatusBwExecuted);
        setMagCourtFirstHearingDate(magCourtFirstHearingDate);
        setMagCourtFinalHearingDate(magCourtFinalHearingDate);
        setNationality(nationality);
        setSection28Name1(section28Name1);
        setSection28Name2(section28Name2); 
        setSection28Phone1(section28Phone1);
		setSection28Phone2(section28Phone2);
		setCustodyTimeLimit(custodyTimeLimit);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        setDefendantNumber(defendantNumber);
        setDateOfCommittal(dateOfCommittal);
        setCtlApplies(ctlApplies);
        setDarRetentionPolicyId(darRetentionPolicyId);
    	return null;
    }
    
    public void ejbPostCreate(Case thisCase, Defendant defendant, String asn, String currentBcStatus, 
    		String isJuvenile, Timestamp drivingDisqSuspendedDate, String pncId, String ptiurn, String isMasked, String maskedName,
    		String hateInd, String hateType, Timestamp benchWarrantExecDate, String bcStatusBwExecuted, Timestamp magCourtFirstHearingDate, 
    		Timestamp magCourtFinalHearingDate, String nationality,  String section28Name1, String section28Name2, String section28Phone1,
    		String section28Phone2, Timestamp custodyTimeLimit, String commBcStatus, String userDisplayName)
            throws CreateException {
        setCaze(thisCase);
        setDefendant(defendant);
    }

    public void ejbPostCreate(Case thisCase, Defendant defendant, String asn, String currentBcStatus, 
    		String isJuvenile, Timestamp drivingDisqSuspendedDate, String pncId, String ptiurn, String isMasked, String maskedName,
    		String hateInd, String hateType, Timestamp benchWarrantExecDate, String bcStatusBwExecuted, Timestamp magCourtFirstHearingDate, 
    		Timestamp magCourtFinalHearingDate, String nationality,  String section28Name1, String section28Name2, String section28Phone1,
    		String section28Phone2, Timestamp custodyTimeLimit, String commBcStatus, String userDisplayName, int defendantNumber, Timestamp dateOfCommittal, String ctlApplies,
    		Integer darRetentionPolicyId) {
    	
        setCaze(thisCase);
        setDefendant(defendant);
    }
    
    public Integer ejbCreate(Integer noOfTics, Integer finalDrivingLicenceStatus, String ptiurn, String isJuvenile,
            String isMasked, String maskedName, Integer caseId, String obsInd, Integer defendantId,
            String resultsVerified, Integer defendantNumber, Timestamp dateOfCommittal, 
            Integer collectMagistrateCourtId, String asn, String userDisplayName)
            throws CreateException {
        setNoOfTics(noOfTics);
        setFinalDrivingLicenceStatus(finalDrivingLicenceStatus);
        setPtiurn(ptiurn);
        setIsJuvenile(isJuvenile);
        setIsMasked(isMasked);
        setMaskedName(maskedName);
        setCaseId(caseId);
        setCreatedBy(userDisplayName);
        setObsInd(obsInd);
        setDefendantId(defendantId);
        setResultsVerified(resultsVerified);
        setDefendantNumber(defendantNumber);
        setDateOfCommittal(dateOfCommittal);
        setCollectMagistrateCourtId(collectMagistrateCourtId);
        setAsn(asn);
        return null;
    }


    public Integer ejbCreate(Case thisCase, Defendant aDefendant, Integer noOfTics, Integer finalDrivingLicenceStatus,
            String ptiurn, String isJuvenile,
            String isMasked, String maskedName, String obsInd, Integer defendantId,
            String resultsVerified, Integer defendantNumber, Timestamp dateOfCommittal, 
            Integer collectMagistrateCourtId, String asn, String userDisplayName)
            throws CreateException {
        setNoOfTics(noOfTics);
        setFinalDrivingLicenceStatus(finalDrivingLicenceStatus);
        setPtiurn(ptiurn);
        setIsJuvenile(isJuvenile);
        setIsMasked(isMasked);
        setMaskedName(maskedName);
        setCreatedBy(userDisplayName);
        setObsInd(obsInd);
        setResultsVerified(resultsVerified);
        setDefendantNumber(defendantNumber);
        setDateOfCommittal(dateOfCommittal);
        setCollectMagistrateCourtId(collectMagistrateCourtId);
        setAsn(asn);

        return null;
    }

    @SuppressWarnings("unused")
    public void ejbPostCreate(
    Integer noOfTics, Integer finalDrivingLicenceStatus, String ptiurn, String isJuvenile,
            String isMasked, String maskedName, Integer caseId, String obsInd, Integer defendantId,
            String resultsVerified, Integer defendantNumber, Timestamp dateOfCommittal, 
            Integer collectMagistrateCourtId, String asn, String userDisplayName)
            throws CreateException {
        // Empty
    }
    
    @SuppressWarnings("unused")
    public void ejbPostCreate(
            Case thisCase, Defendant aDefendant, Integer noOfTics, Integer finalDrivingLicenceStatus,
            String ptiurn, String isJuvenile,
            String isMasked, String maskedName, String obsInd, Integer defendantId,
            String resultsVerified, Integer defendantNumber, Timestamp dateOfCommittal, 
            Integer collectMagistrateCourtId, String asn, String userDisplayName)
            throws CreateException {
        // Empty
        setCaze(thisCase);
        setDefendant(aDefendant);
    }
    
    public Integer ejbCreate(Integer noOfTics, Integer finalDrivingLicenceStatus, String ptiurn, String isJuvenile,
            String isMasked, String maskedName, Integer caseId, String obsInd, Integer defendantId,
            String resultsVerified, Integer defendantNumber, Timestamp dateOfCommittal, 
            Integer collectMagistrateCourtId, String asn, String custodial, String suspended, String seriousDrugOffence,
            String recommendedDeportation, String nationality, Timestamp dateExported, String userDisplayName)
            throws CreateException {
        
        ejbCreate(noOfTics, finalDrivingLicenceStatus, ptiurn, isJuvenile,
                isMasked, maskedName, caseId, obsInd, defendantId,
                resultsVerified, defendantNumber, dateOfCommittal, 
                collectMagistrateCourtId, asn, userDisplayName);
        
        setCustodial(custodial);
        setSuspended(suspended);
        setSeriousDrugOffence(seriousDrugOffence);
        setRecommendedDeportation(recommendedDeportation);
        setNationality(nationality);
        setDateExported(dateExported);
        return null;
    }

    @SuppressWarnings("unused")
    public void ejbPostCreate(Integer noOfTics, Integer finalDrivingLicenceStatus, String ptiurn, String isJuvenile,
            String isMasked, String maskedName, Integer caseId, String obsInd, Integer defendantId,
            String resultsVerified, Integer defendantNumber, Timestamp dateOfCommittal, 
            Integer collectMagistrateCourtId, String asn, String custodial, String suspended,
            String seriousDrugOffence, String recommendedDeportation, String nationality, Timestamp dateExported, String userDisplayName)
            throws CreateException {
        // Empty
    }


    public Integer ejbCreate(Integer noOfTics, Integer finalDrivingLicenceStatus, String ptiurn, String isJuvenile,
            String isMasked, String maskedName, Integer caseId, String obsInd, Integer defendantId,
            String resultsVerified, Integer defendantNumber, Timestamp dateOfCommittal, 
            Integer collectMagistrateCourtId, String asn, String custodial, String suspended, String seriousDrugOffence,
            String recommendedDeportation, String nationality, Timestamp dateExported, String publicDisplayHide,
            Timestamp amendedDateExported, String amendedReason, String userDisplayName)
            throws CreateException {
        
        ejbCreate(noOfTics, finalDrivingLicenceStatus, ptiurn, isJuvenile,
                isMasked, maskedName, caseId, obsInd, defendantId,
                resultsVerified, defendantNumber, dateOfCommittal, 
                collectMagistrateCourtId, asn, custodial, suspended,
                seriousDrugOffence, recommendedDeportation, nationality,
                dateExported, userDisplayName);
        
        setPublicDisplayHide(publicDisplayHide);
        setAmendedDateExported(amendedDateExported);
        setAmendedReason(amendedReason);
        
        return null;
    }
    

    public Integer ejbCreate(Integer noOfTics, Integer finalDrivingLicenceStatus, String ptiurn, String isJuvenile,
            String isMasked, String maskedName, Integer caseId, String obsInd, Integer defendantId,
            String resultsVerified, Integer defendantNumber, Timestamp dateOfCommittal, 
            Integer collectMagistrateCourtId, String asn, String custodial, String suspended, String seriousDrugOffence,
            String recommendedDeportation, String nationality, Timestamp dateExported, String publicDisplayHide,
            Timestamp amendedDateExported, String amendedReason, String hateInd, String hateType, String hateSentInd, 
            String userDisplayName)
            throws CreateException {
        
        ejbCreate(noOfTics, finalDrivingLicenceStatus, ptiurn, isJuvenile,
                isMasked, maskedName, caseId, obsInd, defendantId,
                resultsVerified, defendantNumber, dateOfCommittal, 
                collectMagistrateCourtId, asn, custodial, suspended, seriousDrugOffence,
                recommendedDeportation, nationality, dateExported, publicDisplayHide,
                amendedDateExported, amendedReason, userDisplayName);
        
        setHateIndicator(hateInd);
        setHateType(hateType);
        setHateSentIndicator(hateSentInd);
        
        return null;
    }
  
    public Integer ejbCreate(Integer noOfTics, Integer finalDrivingLicenceStatus, String ptiurn, String isJuvenile,
            String isMasked, String maskedName, Integer caseId, String obsInd, Integer defendantId,
            String resultsVerified, Integer defendantNumber, Timestamp dateOfCommittal, 
            Integer collectMagistrateCourtId, String asn, String custodial, String suspended, String seriousDrugOffence,
            String recommendedDeportation, String nationality, Timestamp dateExported, String publicDisplayHide,
            Timestamp amendedDateExported, String amendedReason, String hateInd, String hateType, String hateSentInd, 
            String userDisplayName, String differenceReport)
            throws CreateException {
        
        ejbCreate(noOfTics, finalDrivingLicenceStatus, ptiurn, isJuvenile,
                isMasked, maskedName, caseId, obsInd, defendantId,
                resultsVerified, defendantNumber, dateOfCommittal, 
                collectMagistrateCourtId, asn, custodial, suspended, seriousDrugOffence,
                recommendedDeportation, nationality, dateExported, publicDisplayHide,
                amendedDateExported, amendedReason, hateInd, hateType, hateSentInd, 
                userDisplayName);
        
        setDifferenceReport(differenceReport);
        
        return null;
    }
    
    @SuppressWarnings("unused")
    public void ejbPostCreate(Integer noOfTics, Integer finalDrivingLicenceStatus, String ptiurn, String isJuvenile,
            String isMasked, String maskedName, Integer caseId, String obsInd, Integer defendantId,
            String resultsVerified, Integer defendantNumber, Timestamp dateOfCommittal, 
            Integer collectMagistrateCourtId, String asn, String custodial, String suspended,
            String seriousDrugOffence, String recommendedDeportation, String nationality, Timestamp dateExported,
            String publicDisplayHide, Timestamp amendedDateExported, String amendedReason, String userDisplayName)
            throws CreateException {
        // Empty
    }
    
    @SuppressWarnings("unused")
    public void ejbPostCreate(Integer noOfTics, Integer finalDrivingLicenceStatus, String ptiurn, String isJuvenile,
            String isMasked, String maskedName, Integer caseId, String obsInd, Integer defendantId,
            String resultsVerified, Integer defendantNumber, Timestamp dateOfCommittal, 
            Integer collectMagistrateCourtId, String asn, String custodial, String suspended,
            String seriousDrugOffence, String recommendedDeportation, String nationality, Timestamp dateExported,
            String publicDisplayHide, Timestamp amendedDateExported, String amendedReason, String hateInd,
            String hateType, String hateSentInd, String userDisplayName)
            throws CreateException {
        // Empty
    }
    
    @SuppressWarnings("unused")
    public void ejbPostCreate(Integer noOfTics, Integer finalDrivingLicenceStatus, String ptiurn, String isJuvenile,
            String isMasked, String maskedName, Integer caseId, String obsInd, Integer defendantId,
            String resultsVerified, Integer defendantNumber, Timestamp dateOfCommittal, 
            Integer collectMagistrateCourtId, String asn, String custodial, String suspended,
            String seriousDrugOffence, String recommendedDeportation, String nationality, Timestamp dateExported,
            String publicDisplayHide, Timestamp amendedDateExported, String amendedReason, String hateInd,
            String hateType, String hateSentInd, String userDisplayName, String differenceReport)
            throws CreateException {
        // Empty
    }
    
    // ------------------------------CMP
    // Fields------------------------------------
    
    public abstract void setDateExported(Timestamp dateExported);
    
    public abstract void setNationality(String nationality);
    
    public abstract void setRecommendedDeportation(String value);
    
    public abstract void setSeriousDrugOffence(String value);  
    
    public abstract void setCustodial(String custodial);
    
    public abstract void setSuspended(String suspended);
    
    public abstract void setDefendantOnCaseId(Integer defendantOnCaseId);

    public abstract void setNoOfTics(Integer noOfTics);

    public abstract void setFinalDrivingLicenceStatus(Integer finalDrivingLicenceStatus);

    public abstract void setPtiurn(String ptiurn);

    public abstract void setIsJuvenile(String isJuvenile);

    public abstract void setIsMasked(String isMasked);

    public abstract void setMaskedName(String maskedName);

    public abstract void setCaseId(Integer caseId);

    public abstract void setObsInd(java.lang.String obsInd);

    public abstract void setDefendantId(Integer defendantId);

    public abstract void setResultsVerified(String resultsVerified);

    public abstract void setDefendantNumber(Integer defendantNumber);

    public abstract void setDateOfCommittal(Timestamp dateOfCommittal);

    public abstract void setCollectMagistrateCourtId(Integer collectMagistrateCourtId);

    public abstract void setAsn(String asn);
    
    public abstract void setPublicDisplayHide(String publicDisplayHide);
    
    public abstract void setDifferenceReport(String differenceReport);
    
    public abstract Timestamp getDateExported();
    
    public abstract String getNationality();
    
    public abstract String getRecommendedDeportation();
    
    public abstract String getSeriousDrugOffence();    
    
    public abstract String getCustodial();
    
    public abstract String getSuspended();

    public abstract Integer getDefendantOnCaseId();

    public abstract Integer getNoOfTics();

    public abstract Integer getFinalDrivingLicenceStatus();

    public abstract String getPtiurn();

    public abstract String getIsJuvenile();

    public abstract String getIsMasked();

    public abstract String getMaskedName();

    public abstract Integer getCaseId();

    public abstract java.lang.String getObsInd();

    public abstract Integer getDefendantId();

    public abstract String getResultsVerified();

    public abstract Integer getDefendantNumber();

    public abstract Timestamp getDateOfCommittal();

    public abstract Integer getCollectMagistrateCourtId();

    public abstract String getAsn();
    
    public abstract String getPublicDisplayHide();
    
    public abstract String getDifferenceReport();

    //RFC2871
    public abstract Timestamp getAmendedDateExported();
    
    public abstract String getAmendedReason();
    
    public abstract void setAmendedDateExported(Timestamp amendedDateExported);
    
    public abstract void setAmendedReason(String amendedReason);
    
    //RFS4224
    public abstract String getHateIndicator();
    
    public abstract void setHateIndicator(String hateIndicator);
    
    public abstract String getHateType();
    
    public abstract void setHateType(String hateType);
    
    public abstract String getHateSentIndicator();
    
    public abstract void setHateSentIndicator(String hateSentIndicator);
    
    // ------------------------------CMR
    // Fields------------------------------------
    public abstract void setDefendant(uk.gov.courtservice.xhibit.business.entities.defendant.Defendant defendant);

    public abstract void setCaze(uk.gov.courtservice.xhibit.business.entities.caze.Case caze);

    public abstract uk.gov.courtservice.xhibit.business.entities.defendant.Defendant getDefendant();

    public abstract uk.gov.courtservice.xhibit.business.entities.caze.Case getCaze();
    
    public abstract Collection getHateSentencings();
    
    public abstract void setHateSentencings(Collection hateSentencings);
    
    public abstract Collection getBwHistory();
    
    public abstract void setBwHistory(Collection bwHistory);

    //Case creation
    public abstract String getCurrentBcStatus();
    public abstract void setCurrentBcStatus(String currentBcStatus);
    
    public abstract Timestamp getDrivingDisqSuspendedDate(  ) ;
    public abstract void setDrivingDisqSuspendedDate( Timestamp drivingDisqSuspendedDate ) ;
    
    public abstract java.lang.String getPncId(  );
    public abstract void setPncId( String pncId ) ;
    
    public abstract Timestamp getBenchWarrantExecDate(  ) ;
    public abstract void setBenchWarrantExecDate( Timestamp benchWarrantExecDate );
    
    public abstract String getBcStatusBwExecuted(  ) ;
    public abstract void setBcStatusBwExecuted( String bcStatusBwExecuted ) ;
    
    public abstract Timestamp getMagCourtFirstHearingDate(  ) ;
    public abstract void setMagCourtFirstHearingDate( Timestamp magCourtFirstHearingDate ) ;

    public abstract Timestamp getMagCourtFinalHearingDate(  ) ;
    public abstract void setMagCourtFinalHearingDate( Timestamp magCourtFinalHearingDate ) ;
    
    public abstract String getSection28Name1();
    public abstract void setSection28Name1(String section28Name1);
    
    public abstract String getSection28Name2();
    public abstract void setSection28Name2(String section28Name2);
    
    public abstract String getSection28Phone1();
    public abstract void setSection28Phone1(String section28Phone1);
    
    public abstract String getSection28Phone2();
    public abstract void setSection28Phone2(String section28Phone2);
    
    public abstract Timestamp getCustodyTimeLimit(  ) ;
    public abstract void setCustodyTimeLimit( Timestamp custodyTimeLimit ) ;
    
    public abstract String getCommBcStatus();
    public abstract void setCommBcStatus(String commBcStatus);
    
    public abstract String getCacdAppealResult();
    public abstract void setCacdAppealResult(String cacdAppealResult);
    
    public abstract Timestamp getCacdAppealResultDate();
    public abstract void setCacdAppealResultDate(Timestamp cacdAppealResultDate);
    
    public abstract String getCoaStatus();
    public abstract void setCoaStatus(String caoStatus);
    
    public abstract Timestamp getFormNgSentDate();
    public abstract void setFormNgSentDate(Timestamp formNgSentDate);
    
    public abstract Timestamp getDateReceiptNoticeAppeal();
    public abstract void setDateReceiptNoticeAppeal(Timestamp dateReceiptNoticeAppeal);
	
	public abstract void setMonetaryOrderTrackings(java.util.Collection monetaryOrderTrackings);
	public abstract java.util.Collection getMonetaryOrderTrackings();
	
	public abstract void setCtlApplies(String ctlApplies);
	public abstract String getCtlApplies();
    
	public abstract Integer getDarRetentionPolicyId();
	public abstract void setDarRetentionPolicyId(Integer darRetentionPolicyId);
}