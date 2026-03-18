package uk.gov.courtservice.xhibit.business.entities.defendantoncase;

import java.sql.Timestamp;
import java.util.Collection;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.defendant.Defendant;

public interface DefendantOnCase extends CSEntityLocal {
    //CCN0400 - KD
    public Timestamp getDateExported();
    public void setDateExported(Timestamp dateExported);
    
    public String getNationality();
    public void setNationality(String nationality);
    
    public String getRecommendedDeportation();
    public void setRecommendedDeportation(String value);
    
    public String getSeriousDrugOffence();
    public void setSeriousDrugOffence(String value);
    
    public String getCustodial();
    public void setCustodial(String value);
    
    public String getSuspended();
    public void setSuspended(String value);
    
    public Integer getDefendantOnCaseId();
    public void setNoOfTics(Integer noOfTics);

    public Integer getNoOfTics();

    public void setFinalDrivingLicenceStatus(Integer finalDrivingLicenceStatus);
    public Integer getFinalDrivingLicenceStatus();

    public void setPtiurn(String ptiurn);
    public String getPtiurn();

    public void setIsJuvenile(String isJuvenile);
    public String getIsJuvenile();

    public void setIsMasked(String isMasked);
    public String getIsMasked();

    public void setMaskedName(String maskedName);
    public String getMaskedName();

    public void setCaseId(Integer caseId);
    public Integer getCaseId();

    public void setObsInd(String obsInd);
    public String getObsInd();

    public void setDefendant(Defendant defendant);
    public Defendant getDefendant();

    public void setDefendantId(Integer defendantId);
    public Integer getDefendantId();

    public void setCaze(uk.gov.courtservice.xhibit.business.entities.caze.Case caze);
    public uk.gov.courtservice.xhibit.business.entities.caze.Case getCaze();

    public void setResultsVerified(String resultsVerified);
    public String getResultsVerified();

    public void setDefendantNumber(Integer defendantNumber);
    public void setDateOfCommittal(Timestamp dateOfCommittal);

    public Integer getDefendantNumber();

    public Timestamp getDateOfCommittal();

    public void setCollectMagistrateCourtId(Integer collectMagistrateCourtId);
    public Integer getCollectMagistrateCourtId();
    
    public void setAsn(String asn);
    public String getAsn();
    
    public void setPublicDisplayHide(String publicDisplayHide);
    public String getPublicDisplayHide();
    
    //RFC2871
    public Timestamp getAmendedDateExported();
    public void setAmendedDateExported(Timestamp amendedDateExported);
    
    public void setAmendedReason(String amendedReason);
    public String getAmendedReason();
    
    //RFS4224
    public void setHateIndicator(String hateIndicator);
    public String getHateIndicator();
    
    public void setHateType(String hateType);
    public String getHateType();
    
    public  void setHateSentIndicator(String hateSentIndicator);
    public String getHateSentIndicator();
    
    public void setHateSentencings(Collection hateSentencings);
    public Collection getHateSentencings();
    
    //Case creation
    public Timestamp getCustodyTimeLimit();
    public void setCustodyTimeLimit( Timestamp custodyTimeLimit );
    
    
    public String getCurrentBcStatus();
    public void setCurrentBcStatus(String currentBcStatus);
    
    public Timestamp getDrivingDisqSuspendedDate() ;
    public void setDrivingDisqSuspendedDate( Timestamp drivingDisqSuspendedDate ) ;
    
    public String getPncId() ;
    public void setPncId( String pncId ) ;
    
    public Timestamp getBenchWarrantExecDate() ;
    public void setBenchWarrantExecDate( Timestamp benchWarrantExecDate ) ;
    
    public String getBcStatusBwExecuted() ;
    public void setBcStatusBwExecuted( String bcStatusBwExecuted ) ;
    
    public Timestamp getMagCourtFirstHearingDate() ;
    public void setMagCourtFirstHearingDate( Timestamp magCourtFirstHearingDate ) ;

    public Timestamp getMagCourtFinalHearingDate() ;
    public void setMagCourtFinalHearingDate( Timestamp magCourtFinalHearingDate ) ;

    public String getSection28Name1( );
    public void setSection28Name1( String section28Name1 );
    
    public String getSection28Name2( );
    public void setSection28Name2( String section28Name2 );
    
    public String getSection28Phone1( );
    public void setSection28Phone1( String section28Phone1 );

    public String getSection28Phone2( );
    public void setSection28Phone2( String section28Phone2 );
    
    public String getCommBcStatus();
    public void setCommBcStatus(String commBcStatus);
    
    public String getDifferenceReport();
    public void setDifferenceReport(String differenceReport);
    
    public String getCacdAppealResult();
    public void setCacdAppealResult(String cacdAppealResult);
    
    public String getCoaStatus();
    public void setCoaStatus(String caoStatus);
    
    public Timestamp getFormNgSentDate();
    public void setFormNgSentDate(Timestamp formNgSentDate);
    
    public Timestamp getDateReceiptNoticeAppeal();
    public void setDateReceiptNoticeAppeal(Timestamp dateReceiptNoticeAppeal);
    
    public Timestamp getCacdAppealResultDate();
    public void setCacdAppealResultDate(Timestamp cacdAppealResultDate);
	
	public void setMonetaryOrderTrackings(java.util.Collection monetaryOrderTrackings);

	public java.util.Collection getMonetaryOrderTrackings();
	
	public void setCtlApplies(String ctlApplies);
	public String getCtlApplies();
	
	public Integer getDarRetentionPolicyId();
	public void setDarRetentionPolicyId(Integer darRetentionPolicyId);
}