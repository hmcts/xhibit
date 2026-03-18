package uk.gov.courtservice.xhibit.integration.services.stub;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.integration.services.MercatorException;

/**
 * <p>
 * Title: DefendantUpdateStub
 * </p>
 * <p>
 * Description: Mimics mercator methods which fall into the defendant area
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: DefendantUpdateStub.java,v 1.13 2009/06/02 15:01:52 davieskl Exp $
 */
public class DefendantUpdateStub {
    private static final Logger log = CSServices.getLogger(DefendantUpdateStub.class);

    public DefendantUpdateStub() {
    }

    /**
     * Mimics the same method on the <code>IntegrationFacadeImple</code>. The
     * defendant is not updated in CREST, however the xhb_defendant, xhb_address
     * and xhb_defendant_on_case tables are updated in Xhibit
     * 
     * @param defendantValue
     *            Contains the values with which to update Xhibit
     * @throws MercatorException
     *             never
     */
    public void updateDefendant(DefendantValue defendantValue) throws MercatorException {
        if (log.isDebugEnabled()) {
            log.debug("updateDefendant(DefendantValue defendantValue) started with " + defendantValue);
        }

        // update the defendant
        XhbDefendantBasicValue defendantBasicValue = XhbDefendantBeanHelper2.findByPrimaryKeyValue(defendantValue
                .getDefendantID());
        defendantBasicValue.setFirstName(defendantValue.getFirstName());
        defendantBasicValue.setMiddleName(defendantValue.getMiddleName());
        defendantBasicValue.setInitials(defendantValue.getInitials());
        defendantBasicValue.setSurname(defendantValue.getSurName());
        if (defendantValue.getDateOfBirth()!= null){
            defendantBasicValue.setDateOfBirth(defendantValue.getDateOfBirth().getTime());
        }
        defendantBasicValue.setGender(new Byte(defendantValue.getGender().byteValue()));
        if (defendantValue.getLastConvictionDate() != null) {
            defendantBasicValue.setLastConvictionDate(defendantValue.getLastConvictionDate().getTime());
        }
        XhbDefendantBeanHelper2.update(defendantBasicValue);

        // update the address
        AddressValue addressValue = defendantValue.getAddressValue();
        if (log.isDebugEnabled()) {
            log.debug("Updating with AddressValue " + addressValue);
        }

        XhbAddressBasicValue addrBasicValue = XhbAddressBeanHelper2.findByPrimaryKeyValue(addressValue.getAddressID());
        if(addrBasicValue != null){
            addrBasicValue.setAddress1(addressValue.getAddress1());
            addrBasicValue.setAddress2(addressValue.getAddress2());
            addrBasicValue.setAddress3(addressValue.getAddress3());
            addrBasicValue.setAddress4(addressValue.getAddress4());
            addrBasicValue.setCountry(addressValue.getCountry());
            addrBasicValue.setCounty(addressValue.getCounty());
            addrBasicValue.setPostcode(addressValue.getPostcode());
            addrBasicValue.setTown(addressValue.getTown());
        }
        
        XhbAddressBeanHelper2.update(addrBasicValue);

        // update the defendant on case
        DefendantOnCaseBasicValue docValue = defendantValue.getDefOnCaseBasicValue();
        if (log.isDebugEnabled()) {
            log.debug("Updating with DefendantOnCaseBasicValue " + docValue);
        }

        log.debug("updateDefendant: updateDefendantOnCase start with Case ID: " + docValue.getCaseID() + " and Defendant ID: " + docValue.getDefendantID() + " and DOC ID: " + docValue.getId());
        
        DefendantOnCaseValue defendantOnCaseValue = new DefendantOnCaseValue();
        defendantOnCaseValue.setDefendantOnCaseBVO(docValue);
        
        updateDefendantOnCase(defendantOnCaseValue);
    }
    
    /**
     * This method is used to update DefendantOnCase. 
     * 
     * @param DefendantOnCaseBasicValue  
     * 
     */
    public void updateDefendantOnCase(DefendantOnCaseValue defendantOnCaseValue) 
              throws MercatorException
    {
        DefendantOnCaseBasicValue defendantOnCaseBasicValue = defendantOnCaseValue.getDefendantOnCaseBVO();
        
        log.debug("updateDefendantOnCase start with Court ID: " + defendantOnCaseValue.getCourtId() + ", Case ID: " + defendantOnCaseBasicValue.getCaseID() + " and Defendant ID: " + defendantOnCaseBasicValue.getDefendantID() + " and DOC ID: " + defendantOnCaseBasicValue.getId());
        
        XhbDefendantOnCaseBasicValue docBasicValue = null;
        
        if(defendantOnCaseBasicValue.getId()!=null && defendantOnCaseBasicValue.getId()!=0)
        {
            log.debug("updateDefendantOnCase Use findByPrimaryKeyValue with the Id for lookup of DOC: " + defendantOnCaseBasicValue.getId());
            docBasicValue = XhbDefendantOnCaseBeanHelper2.findByPrimaryKeyValue(defendantOnCaseBasicValue.getId());
        }
        else
        {
            //This should work just as well providing we have both keys, however better to have the DOC ID
            log.debug("updateDefendantOnCase Use findByDefendantAndCaseValue for lookup of DOC Case ID: " + defendantOnCaseBasicValue.getCaseID() + " and Defendant ID: " + defendantOnCaseBasicValue.getDefendantID());
            docBasicValue = XhbDefendantOnCaseBeanHelper2.findByDefendantAndCaseValue(defendantOnCaseBasicValue.getDefendantID(), defendantOnCaseBasicValue.getCaseID());
        }
        
        log.debug("updateDefendantOnCase docBasicValue Case ID: " + docBasicValue.getCaseId() + " and Defendant ID: " + docBasicValue.getDefendantId() + " and DOC ID: " + docBasicValue.getDefendantOnCaseId());
        
        docBasicValue.setAsn(defendantOnCaseBasicValue.getAsn());
        docBasicValue.setCaseId(defendantOnCaseBasicValue.getCaseID());
        docBasicValue.setCollectMagistrateCourtId(defendantOnCaseBasicValue.getCollectMagistrateCourtId());        
        if(defendantOnCaseBasicValue.getDateOfCommittal()!=null)
        {
            docBasicValue.setDateOfCommittal(defendantOnCaseBasicValue.getDateOfCommittal().getTime());
        }
        docBasicValue.setDefendantId(defendantOnCaseBasicValue.getDefendantID());
        docBasicValue.setDefendantNumber(defendantOnCaseBasicValue.getDefendantNumber());
        docBasicValue.setDefendantOnCaseId(defendantOnCaseBasicValue.getId());
        if(defendantOnCaseBasicValue.getFinalDrivingLicenceStatus()!=null)
        {
            docBasicValue.setFinalDrivingLicenceStatus(defendantOnCaseBasicValue.getFinalDrivingLicenceStatus().byteValue());
        }
        docBasicValue.setIsJuvenile(defendantOnCaseBasicValue.getIsJuvenile());
        docBasicValue.setIsMasked(defendantOnCaseBasicValue.getIsMasked());
        docBasicValue.setMaskedName(defendantOnCaseBasicValue.getMaskedName());
        if(defendantOnCaseBasicValue.getNoOfTICs()!=null)
        {
            docBasicValue.setNoOfTics(defendantOnCaseBasicValue.getNoOfTICs().shortValue());
        }
        docBasicValue.setObsInd(defendantOnCaseBasicValue.getObsInd());
        docBasicValue.setPtiurn(defendantOnCaseBasicValue.getPtiurn());
        docBasicValue.setResultsVerified(defendantOnCaseBasicValue.getResultsVerified());
        
        //CCN400 KD START
        docBasicValue.setNationality(defendantOnCaseBasicValue.getNationality());
        docBasicValue.setCustodial(defendantOnCaseBasicValue.getCustodial());
        docBasicValue.setSeriousDrugOffence(defendantOnCaseBasicValue.getSeriousDrugOffence());
        docBasicValue.setRecommendedDeportation(defendantOnCaseBasicValue.getRecommendedDeportation());
        docBasicValue.setSuspended(defendantOnCaseBasicValue.getSuspended());
        
        //CCN400 KD END
        
        XhbDefendantOnCaseBeanHelper2.update(docBasicValue);
        
        log.debug("updateDefendantOnCase end with Case ID: " + defendantOnCaseBasicValue.getCaseID() + " and Defendant ID: " + defendantOnCaseBasicValue.getDefendantID());
    }       
}
