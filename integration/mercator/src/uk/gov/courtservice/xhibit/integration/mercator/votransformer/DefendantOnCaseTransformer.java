package uk.gov.courtservice.xhibit.integration.mercator.votransformer;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.DefOnCaseMVO;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;

/**
 * <p>
 * Title: DefendantOnCaseTransformer
 * </p>
 * <p>
 * Description: This class is taking in a CSValueObject (XhbDefendantBasicValue) and
 * convert it to DefOnCaseMVO
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version 1.2
 * @history Kelvin Davies - 22/04/2009 - CCN400 - Updated to include new deportation reasons and nationality fields.
 * @history Kelvin Davies - 22/04/2009 - CCN1263 - Updated to include new Date_Exported field.
 */

public class DefendantOnCaseTransformer implements VOTransformer {
    
    private static Logger log = CSServices.getLogger(DefendantOnCaseTransformer.class);

    public DefendantOnCaseTransformer() {
    }

    public Object transformVO(Object valueObject) {
        if (valueObject == null) {
            return null;
        }
        // check the instance of the CSValueObject before trying to cast it.
        if (!(valueObject instanceof uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue)) {
            throw new CSConfigurationException(
                    "The CSValueObject is not of the expected type DefendantOnCaseBasicValue. Class is of type :"
                            + valueObject.getClass().toString() + " with vos :" + valueObject.toString());
        }

        DefOnCaseMVO defOnCaseMVO = new DefOnCaseMVO();
        
        DefendantOnCaseValue defendantOnCaseValue = (DefendantOnCaseValue)valueObject;
        
        if(defendantOnCaseValue.getCourtId()!=null)
        {
            log.debug("DefendantOnCase Transform: CourtId:" + defendantOnCaseValue.getCourtId());
            defOnCaseMVO.setCourtID(defendantOnCaseValue.getCourtId());
        }
        
        DefendantOnCaseBasicValue defendantOnCaseBasicValue = defendantOnCaseValue.getDefendantOnCaseBVO();

        String commitalDate = null;
        String exportedDate =null;
        String amendedExportedDate = null;
        

        
        if (defendantOnCaseBasicValue.getDateExported() != null){
            exportedDate =defendantOnCaseBasicValue.getDateExported().getTime().toString();
        }
        if(exportedDate !=null){
            defOnCaseMVO.setDateExported(exportedDate);
        }
        
        if (defendantOnCaseBasicValue.getCustodial() != null){
            defOnCaseMVO.setCustodial(defendantOnCaseBasicValue.getCustodial());
            }else{
                defOnCaseMVO.setCustodial("");
            }
        
        if(defendantOnCaseBasicValue.getSuspended() != null){
            defOnCaseMVO.setSuspended(defendantOnCaseBasicValue.getSuspended());
            }else{
                defOnCaseMVO.setSuspended("");
            }
        
        if(defendantOnCaseBasicValue.getSeriousDrugOffence() != null){
            defOnCaseMVO.setSeriousDrugOffence(defendantOnCaseBasicValue.getSeriousDrugOffence());
            }else{
                defOnCaseMVO.setSeriousDrugOffence("");
            }
        
        if(defendantOnCaseBasicValue.getRecommendedDeportation() != null){
            defOnCaseMVO.setRecommendedDeportation(defendantOnCaseBasicValue.getRecommendedDeportation());
            }else{
                defOnCaseMVO.setRecommendedDeportation("");
            }
        
        if(defendantOnCaseBasicValue.getNationality() != null){
            defOnCaseMVO.setNationality(defendantOnCaseBasicValue.getNationality());
            }else{
                defOnCaseMVO.setNationality("");
            }
                
        //RFC2878 -START
        if (defendantOnCaseBasicValue.getAmendedDateExported() != null){
            amendedExportedDate =defendantOnCaseBasicValue.getAmendedDateExported().getTime().toString();
        }
        if(amendedExportedDate !=null){
            defOnCaseMVO.setAmendedDateExported(amendedExportedDate);
        }
        
        if(defendantOnCaseBasicValue.getAmendedReason() != null){
            defOnCaseMVO.setAmendedReason(defendantOnCaseBasicValue.getAmendedReason());
            }else{
                defOnCaseMVO.setAmendedReason("");
            }
        //RFC2878 - END
        
        
        //RFS4224 -START
        if (defendantOnCaseBasicValue.getHateIndicator() != null){
            defOnCaseMVO.setHateIndicator(defendantOnCaseBasicValue.getHateIndicator());
        }else{
            defOnCaseMVO.setHateIndicator("");
        }
        
        if (defendantOnCaseBasicValue.getHateType() != null){
            defOnCaseMVO.setHateType(defendantOnCaseBasicValue.getHateType());
        }else{
            defOnCaseMVO.setHateType("");
        }
        
        boolean hateOptionsSelected = false;
        
        if ((defendantOnCaseBasicValue.getVictimDisability() != null) && 
                (defendantOnCaseBasicValue.getVictimDisability().booleanValue())) {
            defOnCaseMVO.setHateVictimDisability("Y");
            hateOptionsSelected = true;
        }else{
            defOnCaseMVO.setHateVictimDisability("");
        }
        
        if ((defendantOnCaseBasicValue.getRacialAggravated() != null) && 
                (defendantOnCaseBasicValue.getRacialAggravated().booleanValue())) {
            defOnCaseMVO.setHateRaceAggravated("Y");
            hateOptionsSelected = true;
        }else{
            defOnCaseMVO.setHateRaceAggravated("");
        }
        
        if ((defendantOnCaseBasicValue.getReligionAggravated() != null) && 
                (defendantOnCaseBasicValue.getReligionAggravated().booleanValue())) {
            defOnCaseMVO.setHateReligionAggravated("Y");
            hateOptionsSelected = true;
        }else{
            defOnCaseMVO.setHateReligionAggravated("");
        }
        
        if ((defendantOnCaseBasicValue.getGeneralSexual() != null) && 
                (defendantOnCaseBasicValue.getGeneralSexual().booleanValue())) {
            defOnCaseMVO.setHateGeneralSexual("Y");
            hateOptionsSelected = true;
        }else{
            defOnCaseMVO.setHateGeneralSexual("");
        }
        
        if ((defendantOnCaseBasicValue.getVictimSexual() != null) && 
                (defendantOnCaseBasicValue.getVictimSexual().booleanValue())) {
            defOnCaseMVO.setHateVictimSexual("Y");
            hateOptionsSelected = true;
        }else{
            defOnCaseMVO.setHateVictimSexual("");
        }
        
        if ((defendantOnCaseBasicValue.getGeneralTransgender() != null) && 
                (defendantOnCaseBasicValue.getGeneralTransgender().booleanValue())) {
            defOnCaseMVO.setHateGeneralTransgender("Y");
            hateOptionsSelected = true;
        }else{
            defOnCaseMVO.setHateGeneralTransgender("");
        }
        
        if ((defendantOnCaseBasicValue.getVictimTransgender() != null) && 
                (defendantOnCaseBasicValue.getVictimTransgender().booleanValue())) {
           defOnCaseMVO.setHateVictimTransgender("Y");
           hateOptionsSelected = true;
        }else{
           defOnCaseMVO.setHateVictimTransgender("");
        }
        
        if ((defendantOnCaseBasicValue.getGeneralDisability() != null) && 
                (defendantOnCaseBasicValue.getGeneralDisability().booleanValue())) {
            defOnCaseMVO.setHateGeneralDisability("Y");
            hateOptionsSelected = true;
        }else{
            defOnCaseMVO.setHateGeneralDisability("");
        }
        
        if ((defendantOnCaseBasicValue.getRaceAndReligionAggravated() != null) && 
                (defendantOnCaseBasicValue.getRaceAndReligionAggravated().booleanValue())) {
            defOnCaseMVO.setHateRaceAndReligionAggravated("Y");
            hateOptionsSelected = true;
        }else{
            defOnCaseMVO.setHateRaceAndReligionAggravated("");
        }
        
        if ((defendantOnCaseBasicValue.getHateSentencingUpdate() != null) && 
                (defendantOnCaseBasicValue.getHateSentencingUpdate().booleanValue())) {
            defOnCaseMVO.setHateSentencingUpdate(true);
        }else{
            defOnCaseMVO.setHateSentencingUpdate(false);
        }
        
        // if any options selected from hate crime tab then set to Y, otherwise N
        if (hateOptionsSelected){
            defOnCaseMVO.setHateSentIndicator("Y");
        }else{
            defOnCaseMVO.setHateSentIndicator("N");
        }

        //RFS4224 - END

        
        defOnCaseMVO.setAsn(defendantOnCaseBasicValue.getAsn());
        if(defendantOnCaseBasicValue.getCaseID()!=null)
        {
            defOnCaseMVO.setCaseId(defendantOnCaseBasicValue.getCaseID());
        }
        if(defendantOnCaseBasicValue.getCollectMagistrateCourtId()!=null)
        {
            defOnCaseMVO.setCollectionMagistrateCourtId(defendantOnCaseBasicValue.getCollectMagistrateCourtId());
        }
        if(defendantOnCaseBasicValue.getDefendantID()!=null)
        {
            defOnCaseMVO.setDefendantId(defendantOnCaseBasicValue.getDefendantID());
        }
        if(defendantOnCaseBasicValue.getDefendantNumber()!=null)
        {
            defOnCaseMVO.setDefendantNumber(defendantOnCaseBasicValue.getDefendantNumber());
        }
        if(defendantOnCaseBasicValue.getId()!=null)
        {
            defOnCaseMVO.setDefOnCaseId(defendantOnCaseBasicValue.getId());     
        }
        if(defendantOnCaseBasicValue.getFinalDrivingLicenceStatus()!=null)
        {
            defOnCaseMVO.setFinalDrivingLicenseStatus(defendantOnCaseBasicValue.getFinalDrivingLicenceStatus());
        }
        defOnCaseMVO.setIsJuvenile(defendantOnCaseBasicValue.getIsJuvenile());
        defOnCaseMVO.setIsMasked(defendantOnCaseBasicValue.getIsMasked());
        defOnCaseMVO.setMaskedName(defendantOnCaseBasicValue.getMaskedName());
        if(defendantOnCaseBasicValue.getNoOfTICs()!=null)
        {
            defOnCaseMVO.setNbrOfTics(defendantOnCaseBasicValue.getNoOfTICs());
        }
        defOnCaseMVO.setObsInd(defendantOnCaseBasicValue.getObsInd());
        defOnCaseMVO.setPtiurn(defendantOnCaseBasicValue.getPtiurn());
        defOnCaseMVO.setResultsVerified(defendantOnCaseBasicValue.getResultsVerified());
        
        // check if the dates are null, if not set the Strings
        if (defendantOnCaseBasicValue.getDateOfCommittal() != null) {
            commitalDate = defendantOnCaseBasicValue.getDateOfCommittal().getTime().toString();
        }
        
        if(commitalDate!=null)
        {
            defOnCaseMVO.setCommittalDate(commitalDate);
        }
 
        if (defendantOnCaseBasicValue.getVersion() != null) {
            defOnCaseMVO.setVersion(defendantOnCaseBasicValue.getVersion().intValue());
        }
        
        if (defendantOnCaseBasicValue.getId() != null) {
            //The DOCID is in the ID of defendantOnCaseBasicValue
            log.debug("DefendantOnCase Transform: DefOnCaseId:" + defendantOnCaseBasicValue.getId());
            defOnCaseMVO.setDefOnCaseId(defendantOnCaseBasicValue.getId().intValue());
            defOnCaseMVO.setId(defendantOnCaseBasicValue.getId().intValue());
        }
        
        return defOnCaseMVO;
    }

    public Object transformOutput(Object result) {
        /**
         * @todo Implement this
         *       uk.gov.courtservice.xhibit.integration.mercator.votransformer.VOTransformer
         *       method
         */
        return result;
    }
}