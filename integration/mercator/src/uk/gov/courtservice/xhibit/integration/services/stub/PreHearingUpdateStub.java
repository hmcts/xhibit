package uk.gov.courtservice.xhibit.integration.services.stub;

// jdk \ j2ee
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Calendar;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_breach.XhbBreach;
import uk.gov.courtservice.xhibit.business.entities.xhb_breach.XhbBreachBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_breach.XhbBreachBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbChargeBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPlea;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_court.XhbRefCourt;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_court.XhbRefCourtBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.CaseUpdateValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DefendantOnOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.SignIndValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.OriginalChargeVO;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.OutputTransformationException;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.TransformationException;
import uk.gov.courtservice.xhibit.integration.services.MercatorException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.LinkCountDefValue;

/**
 * <p>
 * Title: PreHearingUpdateStub
 * </p>
 * <p>
 * Description: Mimics mercator methods which fall into the update pre hearing
 * area
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author Sarah Tong
 * @version $Id: PreHearingUpdateStub.java,v 1.33 2010/04/22 15:58:57 warsoph Exp $
 */
public class PreHearingUpdateStub {
    private static final Logger log = CSServices.getLogger(PreHearingUpdateStub.class);

    private static final String COMPLETE = "C";

    private static final String OBSOLETE = "Y";

    public PreHearingUpdateStub() {
        // empty
    }

    /**
     * Mimics the same method on the <code>IntegrationFacadeImple</code>. The
     * Charges are not sent to CREST, however the status flag is set to
     * successful as it would be if this had occurred without error.
     *
     * @param caseId
     *            The id of the case for which charges are exported.
     * @throws MercatorException
     */
    public void exportCharges(Integer caseId) throws MercatorException {
        log.debug("exportCharges(Integer caseId) start with : " + caseId);
        // XHB_CASE.exportCharges - set this to C for complete

        // Do not fully understand this method, as the entity will not get
        // updated... However, this is only used in development and no-one
        // has raised as an issue, so leaving as previously implemented...
        XhbCaseBasicValue caseValue = XhbCaseBeanHelper2.findByPrimaryKeyValue(caseId);
        caseValue.setExportCharges(COMPLETE);

        log.debug("exportCharges(Integer caseId) finished");
    }

    /**
     * Mimics the same method on the <code>IntegrationFacadeImple</code>.
     * Updates are not made to CREST, the xhb_offence table is updated with
     * vaules from the supplied VO
     *
     * @param offenceValue
     *            Contains the values with which to update the offence
     * @throws MercatorException
     *             never
     */
    public void updateOffence(OffenceValue offenceValue) throws MercatorException {
        log.debug("updateOffence(OffenceValue offenceValue) start with : " + offenceValue);

        XhbOffenceBasicValue offenceBasicValue = XhbOffenceBeanHelper2.findByPrimaryKeyValue(offenceValue
                .getOffenceID());
        // probably only RefOfenceId and RefSystemCode need updating, but
        // copying the rest for safety
        offenceBasicValue.setCrestOffenceFreetext(offenceValue.getCrestOffenceFreeText());
        offenceBasicValue.setCrestHooClassFreetext(offenceValue.getCrestHOClass());
        offenceBasicValue.setCrestHooSubclassFreetext(offenceValue.getCrestHOSubclass());
        if (offenceValue.getMultiple() != null) {
            offenceBasicValue.setMultiple(new Byte(offenceValue.getMultiple().byteValue()));
        }
        offenceBasicValue.setOffenceId(new Integer(offenceValue.getOffenceID().intValue()));
        offenceBasicValue.setRefOffenceId(new Integer(offenceValue.getRefOffenceID().intValue()));
        if (offenceValue.getRefSystemCodeID() != null) {
            offenceBasicValue.setRefSystemCodeId(new Integer(offenceValue.getRefSystemCodeID().intValue()));
        }
         
        AddressValue addressValue = offenceValue.getAddressValue();
        
        XhbAddressBasicValue addrBasicValue = null;
        
        if(addressValue != null){   
            if(addressValue.getAddressID()!=null && addressValue.getAddressID()!=0)
            {
                log.debug("Update a new Address with ID: " + addressValue.getAddressID() + " and AddressValue " + addressValue);
                addrBasicValue = XhbAddressBeanHelper2.findByPrimaryKeyValue(addressValue.getAddressID());
            }
            else
            {
                log.debug("Create a new Address with AddressValue " + addressValue);
                addrBasicValue = new XhbAddressBasicValue();
            }
            
            addrBasicValue.setAddress1(addressValue.getAddress1());
            addrBasicValue.setAddress2(addressValue.getAddress2());
            addrBasicValue.setAddress3(addressValue.getAddress3());
            addrBasicValue.setAddress4(addressValue.getAddress4());
            addrBasicValue.setCountry(addressValue.getCountry());
            addrBasicValue.setCounty(addressValue.getCounty());
            addrBasicValue.setPostcode(addressValue.getPostcode());
            addrBasicValue.setTown(addressValue.getTown());
            
            if(addressValue.getAddressID()!=null && addressValue.getAddressID()!=0)
            {
                addrBasicValue = XhbAddressBeanHelper2.update(addrBasicValue);
            }
            else
            {
                addrBasicValue = XhbAddressBeanHelper2.create(addrBasicValue);
            }
    
            offenceBasicValue.setLocationAddressId(addrBasicValue.getAddressId());
        }
         
        if(offenceValue.getForceLocationCode()!=null)
        {
            offenceBasicValue.setForceLocationCode(offenceValue.getForceLocationCode());
        }
        if(offenceValue.getOffenceStartDateTime()!=null)
        {
            offenceBasicValue.setStartDate(offenceValue.getOffenceStartDateTime().getTime());  
        }
        if(offenceValue.getOffenceEndDateTime()!=null)
        {
            offenceBasicValue.setEndDate(offenceValue.getOffenceEndDateTime().getTime());
        }
        
        XhbOffenceBeanHelper2.update(offenceBasicValue);
        
        log.debug("updateOffence(OffenceValue offenceValue) finished");
    }

    /**
     * Mimics the same method on the <code>IntegrationFacadeImple</code>.
     * Updates are not made to CREST, the xhb_breach table, xhb_charge and in
     * some cases xhb_plea tables are updated with vaules from the supplied VO
     *
     * @param breachValue
     *            Contains the values with which to update the breach
     * @throws MercatorException
     *             never
     */
    public void updateBreach(BreachValue breachValue) throws MercatorException {
        log.debug("updateBreach() start with breachValue : " + breachValue);

        XhbBreach breachBean = XhbBreachBeanHelper2.findByPrimaryKey(breachValue.getBreachID());
        XhbBreachBasicValue breachBasicValue = breachBean.getData();

        if (breachValue.getDatePut() != null) {
            breachBasicValue.setDatePut(breachValue.getDatePut().getTime());
        }
        breachBasicValue.setOriginalSentence(breachValue.getOriginalSentence());
        breachBasicValue.setOriginalSentenceDate(breachValue.getOriginalSentenceDate().getTime());
        breachBasicValue.setVersion(breachValue.getVersion());
        XhbBreachBeanHelper2.update(breachBasicValue);

        updateBreachCourt(breachValue, breachBean);

        // find the charge to update the ho proc code
        XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(breachValue.getChargeID());
        XhbChargeBasicValue chargeBasicValue = chargeBean.getData();

        chargeBasicValue.setRefSystemCodeId(breachValue.getRefSystemCodeID());
        XhbChargeBeanHelper2.update(chargeBasicValue);

        // find the plea to update the breach admitted, or create a plea
        if (breachValue.getPlea() != null) {
            updateBreachPlea(breachValue, chargeBean);
        }

        log.debug("updateBreach() end");
    }
    
    
    /**
     * Description: The aim of thsi method is to ensure the XHB_Case tablehas been updated, when a call is made
     *              to the mercator map updateCase
     * @param caseValue
     * @throws MercatorException
     */
    public void updateCase(CaseUpdateValue caseValue) throws MercatorException {
        log.debug("updateCAse() start with caseValue :" + caseValue);
        
        XhbCaseBasicValue cazeBV = XhbCaseBeanHelper2.findByNumberTypeAndCourtValue(caseValue.getCaseNumber(), caseValue.getCaseType(),caseValue.getCourtID());
        cazeBV.setVulnerableVictimIndicator(caseValue.getVulnerableVictimIndicator());
        XhbCaseBeanHelper2.update(cazeBV);
        
        log.debug("updateCase() end");

    }

    /**
     * Mimics the same method on the <code>IntegrationFacadeImple</code>.
     * Updates are not made to CREST, an update will be made to the xhb_charge
     * table with values from the supplied VO.
     *
     * @param signIndVal
     *            Contains the details to be added to xhibit
     * @throws MercatorException
     *             never
     */
    public void signIndictment(SignIndValue signIndVal) throws MercatorException {
        log.debug("signIndictment() start with signIndVal :" + signIndVal);

        // find the charge to update
        XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(signIndVal.getChargeID());
        chargeBean.setIndSignedDate(signIndVal.getIndSignedDate().getTime());

        log.debug("signIndictment() end");
    }

    /**
     * Mimics the same method on the <code>IntegrationFacadeImple</code>.
     * Updates are no made to CREST, updates will be made to some or all of
     * xhb_offence, xhb_defendant_on_offence, xhb_charge, xhb_defendant_charge,
     * xhb_breach
     *
     * @param delOffenceVal
     *            Identifies which offence to delete
     * @throws MercatorException
     *             never
     */
    public void deleteOffence(DelOffenceValue delOffenceVal) throws MercatorException {
        log.debug("deleteOffence() start with delOffenceVal : " + delOffenceVal);
        // results should also be deleted, however looking at the application
        // code it does not appear that this actually happens, the offence is
        // simply marked as obsolete and the results are then unreachable - this
        // leaves dead data in the database, however as this stub is simply a
        // development aid and not intended for use in production I am not going
        // to the trouble to implement that behaviour here

        // logically delete offence, logically delete def on offence
        XhbOffence offenceBean = XhbOffenceBeanHelper2.findByPrimaryKey(delOffenceVal.getOffenceID());
        logicalDeleteOffenceAndDef(offenceBean);

        // check if there are other (non-deleted) offences on this charge
        Collection offenceBeans = offenceBean.getXhbCharge().getXhbOffences();
        Iterator offenceBeansIt = offenceBeans.iterator();

        ArrayList<XhbOffence> activeOffences = new ArrayList<XhbOffence>();

        while (offenceBeansIt.hasNext()) {
            XhbOffence checkOffenceBean = (XhbOffence) offenceBeansIt.next();
            if (checkOffenceBean.getObsInd()==null 
                || !checkOffenceBean.getObsInd().equalsIgnoreCase(OBSOLETE)) 
            {
                activeOffences.add(checkOffenceBean);
            }
        }

        // if so, renumber (in case we have deleted other than the last offence)
        if (activeOffences.size() > 0) {
            Collections.sort(activeOffences, OFFENCE_SEQ_ORDER);
            Iterator activeOffenceIt = activeOffences.iterator();
            int i = 0;
            while (activeOffenceIt.hasNext()) {
                XhbOffence orderOffenceBean = (XhbOffence) activeOffenceIt.next();
                orderOffenceBean.setCrestOffenceSeqNo(new Integer(++i));
            }
        } else {
            // if not logically delete charge and any defendant charge records
            logicalDeleteChargeAndDef(offenceBean.getXhbCharge());
        }

        log.debug("deleteOffence() end");
    }

    /**
     * Mimics the same method on the <code>IntegrationFacadeImple</code>.
     * Updates are no made to CREST, updates will be made to some or all of
     * xhb_offence, xhb_defendant_on_offence, xhb_charge, xhb_defendant_charge,
     * xhb_breach
     *
     * @param delChargeVal
     *            Identifies which charge to delete
     * @throws MercatorException
     *             never
     */
    public void deleteCharge(DelChargeValue delChargeVal) throws MercatorException {
        log.debug("deleteCharge() start with : " + delChargeVal);

        // logically delete the charge
        XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(delChargeVal.getChargeID());
        logicalDeleteChargeAndDef(chargeBean);

        // logically delete all offences
        Collection offenceBeans = chargeBean.getXhbOffences();
        Iterator offenceBeansIt = offenceBeans.iterator();
        while (offenceBeansIt.hasNext()) {
            XhbOffence offenceBean = (XhbOffence) offenceBeansIt.next();
            logicalDeleteOffenceAndDef(offenceBean);
        }

        log.debug("deleteCharge() end");
    }

    
    /**
     * This method is used to update an Original Charge. 
     * DefendantId, SeqNo,  OriginalChargeFreeText, DefOnOffenceId and OffenceID are supplied
     * SeqNo and OriginalChargeFreeText can be updated
     * Note: The OriginalChargeFreeText is updated in CrestOffenceFreeText of Offence
     * 
     * @param OriginalChargeValue 
     * 
     */
    public void updateOriginalCharge(OriginalChargeVO originalChargeValue) 
             throws MercatorException, TransformationException, OutputTransformationException
    {
        log.debug("updateOriginalCharge start with Offence Id : " + originalChargeValue.getOffenceId() + ", DOO Id: " + originalChargeValue.getDefendantOnOffenceId());
        
        XhbDefendantOnOffenceBasicValue defendantOnOffenceValue = null;
        XhbOffenceBasicValue offenceBasicValue = null;
        
        defendantOnOffenceValue = XhbDefendantOnOffenceBeanHelper2.findByPrimaryKeyValue(originalChargeValue.getDefendantOnOffenceId());    
        defendantOnOffenceValue.setDefendantOnOffenceId(originalChargeValue.getDefendantOnOffenceId());
        defendantOnOffenceValue.setSeqNo(originalChargeValue.getSeqNo());

        XhbDefendantOnOffenceBeanHelper2.update(defendantOnOffenceValue);

        offenceBasicValue = XhbOffenceBeanHelper2.findByPrimaryKeyValue(originalChargeValue.getOffenceId());        
        offenceBasicValue.setOffenceId(originalChargeValue.getOffenceId());
        offenceBasicValue.setCrestOffenceFreetext(originalChargeValue.getOriginalCharge());
          
        XhbOffenceBeanHelper2.update(offenceBasicValue);
        
        log.debug("updateOriginalCharge start with Offence Id : " + originalChargeValue.getOffenceId() + ", DOO Id: " + originalChargeValue.getDefendantOnOffenceId());
    }
    
    
    /**
     * This method is used to update an Original Charge. 
     * DefendantId, DefOnOffenceId,  OffenceID and ChargeID are supplied
     * 
     * @param OriginalChargeValue 
     * 
     */
    public void deleteOriginalCharge(OriginalChargeVO originalChargeValue) 
             throws MercatorException, TransformationException, OutputTransformationException
    {
        log.debug("deleteOriginalCharge start with charge Id : " + originalChargeValue.getChargeId());

        // Find the charge
        XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(originalChargeValue.getChargeId());
        
        // logically delete all offences (in theory only one)
        Collection offenceBeans = chargeBean.getXhbOffences();
        Iterator offenceBeansIt = offenceBeans.iterator();
        while (offenceBeansIt.hasNext()) {
            XhbOffence offenceBean = (XhbOffence) offenceBeansIt.next();
            log.debug("deleteOriginalCharge: " + originalChargeValue.getChargeId() + ", delete Offence with offence Id : " + offenceBean.getOffenceId());            
            logicalDeleteOffenceAndDef(offenceBean);
        }

        //logically delete the charge
        logicalDeleteChargeAndDef(chargeBean);

        log.debug("deleteOriginalCharge end with charge Id : " + originalChargeValue.getChargeId());
    }
    
    /**
     * This method is used to update DefendantOnOffence. 
     * 
     * @param DefendantOnOffenceValue 
     * 
     */
    public void updateDefendantOnOffence(LinkCountDefValue linkDefendantOnOffenceValue) 
              throws MercatorException, TransformationException, OutputTransformationException
    {
        log.debug("updateDefendantOnOffence start with case Id : " + linkDefendantOnOffenceValue.getCaseID());

        XhbDefendantOnCaseBasicValue xhbDefendantOnCaseBasicValue = null;
        XhbDefendantOnOffence xhbDefendantOnOffence = null;
        Collection<DefendantOnOffenceValue> defendantOnOffenceValues = new ArrayList<DefendantOnOffenceValue>();
        DefendantOnOffenceValue  defendantOnOffenceValue = null;
        XhbDefendantOnOffenceBasicValue xhbDefendantOnOffenceBasicValue = null;
        
        defendantOnOffenceValues = linkDefendantOnOffenceValue.getDefendantOnOffenceValues();
        
        if(!defendantOnOffenceValues.isEmpty())
        {
            Iterator it = defendantOnOffenceValues.iterator();
            while(it.hasNext())
            {
                //In theory there should only be one DOO to be updated
                defendantOnOffenceValue = (DefendantOnOffenceValue)it.next();
                
                log.debug("updateDefendantOnOffence defendantOnOffenceValue.getDefendantId() : " + defendantOnOffenceValue.getDefendantId());
                log.debug("updateDefendantOnOffence defendantOnOffenceValue.getOffenceId() : " + defendantOnOffenceValue.getOffenceId());
                
                xhbDefendantOnCaseBasicValue = XhbDefendantOnCaseBeanHelper2.findByDefendantAndCaseValue(defendantOnOffenceValue.getDefendantId(), linkDefendantOnOffenceValue.getCaseID());
                       
                log.debug("updateDefendantOnOffence xhbDefendantOnCaseBasicValue.getDefendantOnCaseId() : " + xhbDefendantOnCaseBasicValue.getDefendantOnCaseId());
                
                xhbDefendantOnOffence = XhbDefendantOnOffenceBeanHelper2.findByDefOnCaseAndOffence(xhbDefendantOnCaseBasicValue.getDefendantOnCaseId(), defendantOnOffenceValue.getOffenceId());    
                                
                xhbDefendantOnOffenceBasicValue = xhbDefendantOnOffence.getData();
                
                log.debug("xhbDefendantOnOffenceBasicValue xhbDefendantOnCaseBasicValue.getDefendantOnCaseId() : " + xhbDefendantOnOffenceBasicValue.getDefendantOnCaseId());
                log.debug("xhbDefendantOnOffenceBasicValue xhbDefendantOnCaseBasicValue.getDefendantOnOffenceId() : " + xhbDefendantOnOffenceBasicValue.getDefendantOnOffenceId());

                xhbDefendantOnOffenceBasicValue.setIsCommittedOnBail(defendantOnOffenceValue.getIsCommittedOnBail());
                
                if(defendantOnOffenceValue.getDateOfCharge()!=null)
                {
                    xhbDefendantOnOffenceBasicValue.setChargeDate(defendantOnOffenceValue.getDateOfCharge().getTime());
                }
                else
                {
                    xhbDefendantOnOffenceBasicValue.setChargeDate(null);
                }
                if(defendantOnOffenceValue.getDateOfArrest()!=null)
                {
                    xhbDefendantOnOffenceBasicValue.setArrestDate(defendantOnOffenceValue.getDateOfArrest().getTime());
                }
                else
                {
                    xhbDefendantOnOffenceBasicValue.setArrestDate(null);
                }
                xhbDefendantOnOffenceBasicValue.setSeqNo(defendantOnOffenceValue.getSequenceNo());
                
                xhbDefendantOnOffenceBasicValue.setCrnId(defendantOnOffenceValue.getCrn());
                
                xhbDefendantOnOffenceBasicValue.setObsInd(defendantOnOffenceValue.getObsInd());
                
                XhbDefendantOnOffenceBeanHelper2.update(xhbDefendantOnOffenceBasicValue);  
            }            
        }
        
        log.debug("updateDefendantOnOffence end with case Id : " + linkDefendantOnOffenceValue.getCaseID());
    }
    
    /**
     * This method is used to update Offence and DefendantOnOffence. 
     * 
     * @param OffenceValue 
     * 
     */
    public void updateOffenceAndDefOnOffence(OffenceValue offenceValue) 
              throws MercatorException, TransformationException, OutputTransformationException
    {
        log.debug("updateOffenceAndDefOnOffence start with case Id : " + offenceValue.getCaseID() + " and Offence ID: " + offenceValue.getOffenceID());
        
        LinkCountDefValue linkDefendantOnOffenceValue = new LinkCountDefValue();        
        linkDefendantOnOffenceValue.setCaseID(offenceValue.getCaseID());
        
        DefendantOnOffenceComplexValue defendantOnOffenceComplexValue = null;
        DefendantOnOffenceValue defendantOnOffenceValue = null;
        Integer defendantId = null;
        
        Map <Integer,DefendantOnOffenceComplexValue>defendantOnOffenceComplexValues = new HashMap<Integer,DefendantOnOffenceComplexValue>();
        //offenceValue.getDefOnOffenceBasicValues is actually a hashmap of DefendantOnOffenceComplexValue keyed on Defendant ID
        defendantOnOffenceComplexValues = offenceValue.getDefOnOffenceBasicValues();
        Set<Integer> defendantOnOffenceComplexValuesKeySet = defendantOnOffenceComplexValues.keySet();
        Iterator it = defendantOnOffenceComplexValuesKeySet.iterator();    
        Collection <DefendantOnOffenceValue>defendantOnOffenceValues = new ArrayList<DefendantOnOffenceValue>();
        
        while(it.hasNext())
        {
            defendantId = (Integer)it.next();
            log.debug("defendantId: " + defendantId);
            
            defendantOnOffenceComplexValue = defendantOnOffenceComplexValues.get(defendantId);            
            log.debug("defendantOnOffenceComplexValue.getOffenceId(): " + defendantOnOffenceComplexValue.getOffenceId());
             
            defendantOnOffenceValue = new DefendantOnOffenceValue(defendantOnOffenceComplexValue.getOffenceId(), 
                                                                  defendantId, 
                                                                  defendantOnOffenceComplexValue.getCrnId());

            defendantOnOffenceValue.setIsCommittedOnBail(defendantOnOffenceComplexValue.getIsCommittedOnBail());
            
            if(defendantOnOffenceComplexValue.getChargeDate()!=null)
            {
                Calendar chargeDate = Calendar.getInstance();
                chargeDate.setTime(defendantOnOffenceComplexValue.getChargeDate());
                defendantOnOffenceValue.setDateOfCharge(chargeDate);
            }
            
            if(defendantOnOffenceComplexValue.getArrestDate()!=null)
            {
                Calendar arrestDate = Calendar.getInstance();
                arrestDate.setTime(defendantOnOffenceComplexValue.getArrestDate());
                defendantOnOffenceValue.setDateOfArrest(arrestDate);
            }
            
            defendantOnOffenceValue.setSequenceNo(defendantOnOffenceComplexValue.getSeqNo());
            
            defendantOnOffenceValues.add(defendantOnOffenceValue);
        }
        
        linkDefendantOnOffenceValue.setDefendantOnOffenceValues(defendantOnOffenceValues);
        
        linkDefendantOnOffenceValue.setCourtID(offenceValue.getCourtID());
        linkDefendantOnOffenceValue.setIsInCourt(offenceValue.isInCourt());
        
        XhbCharge chargeBean = XhbChargeBeanHelper2.findByPrimaryKey(offenceValue.getChargeID());
        linkDefendantOnOffenceValue.setChargeType(chargeBean.getChargeType());
        
        updateDefendantOnOffence(linkDefendantOnOffenceValue);
        
        updateOffence(offenceValue);
        
        log.debug("updateOffenceAndDefOnOffence end with case Id : " + offenceValue.getCaseID() + " and Offence ID: " + offenceValue.getOffenceID());
    }  
  
    
    
    // -------------------------- Private Methods
    // --------------------------- //
    private void updateBreachPlea(BreachValue breachValue, XhbCharge chargeBean) {
        Collection defCharges = chargeBean.getXhbDefendantCharges();
        // there should be one and only on defCharge record for a Breach charge
        if (defCharges.size() != 1) {
            throw new EJBException("There should be one and only on defCharge record for a Breach charge "
                    + defCharges.size() + " found");
        }

        XhbDefendantCharge defChargeBean = (XhbDefendantCharge) defCharges.iterator().next();
        Collection pleasIncObsolete = defChargeBean.getXhbPleas();
        Collection<XhbPlea> nonObsoletePleas = new ArrayList<XhbPlea>();
        Iterator iter = pleasIncObsolete.iterator();
        while (iter.hasNext()) {
            XhbPlea item = (XhbPlea) iter.next();
            if (item.getObsInd() == null || !item.getObsInd().equals("Y")) {
                nonObsoletePleas.add(item);
            }
        }

        if (nonObsoletePleas.size() == 0) {
            PreHearingAddStub.addBreachPlea(breachValue.getPlea(), defChargeBean.getDefendantChargeId());
        } else if (nonObsoletePleas.size() > 1) {
            throw new EJBException("There should be zero or one pleas recorded for a Breach charge "
                    + nonObsoletePleas.size() + " found");
        } else {
            XhbPlea pleaBean = nonObsoletePleas.iterator().next();
            pleaBean.setBreachAdmitted(breachValue.getPlea());
        }
    }

    private void updateBreachCourt(BreachValue breachValue, XhbBreach breachBean) {
        // find the court to update
        // find ref court
        XhbRefCourt refCourt = XhbRefCourtBeanHelper2.findByPrimaryKey(breachValue.getOriginalCourtID());
        breachBean.setXhbRefCourt(refCourt);
    }

    private void logicalDeleteOffenceAndDef(XhbOffence offenceBean) {
        offenceBean.setObsInd(OBSOLETE);
        Collection defOnOffenceBeans = offenceBean.getXhbDefendantOnOffences();
        Iterator defOnOffenceBeansIt = defOnOffenceBeans.iterator();
        while (defOnOffenceBeansIt.hasNext()) {
            XhbDefendantOnOffence defOnOffenceBean = (XhbDefendantOnOffence) defOnOffenceBeansIt.next();
            defOnOffenceBean.setObsInd(OBSOLETE);
        }
    }

    private void logicalDeleteChargeAndDef(XhbCharge chargeBean) {
        chargeBean.setObsInd(OBSOLETE);

        if (chargeBean.getChargeType().equals("B")) {
        	XhbBreach breachBean = chargeBean.getXhbBreach();
        	breachBean.setObsInd(OBSOLETE);
		}

        Collection defChargeBeans = chargeBean.getXhbDefendantCharges();
        Iterator defChargeBeansIt = defChargeBeans.iterator();
        while (defChargeBeansIt.hasNext()) {
            XhbDefendantCharge defChargeBean = (XhbDefendantCharge) defChargeBeansIt.next();
            defChargeBean.setObsInd(OBSOLETE);
        }

        // check if there are other (non-deleted) charges of the same type
        Collection chargeBeans = chargeBean.getXhbCase().getXhbCharges();
        Iterator chargeBeansIt = chargeBeans.iterator();

        ArrayList<XhbCharge> activeCharges = new ArrayList<XhbCharge>();

        while (chargeBeansIt.hasNext()) {
            XhbCharge checkChargeBean = (XhbCharge) chargeBeansIt.next();
            if (checkChargeBean.getChargeType().equals(chargeBean.getChargeType())
                    && (checkChargeBean.getObsInd()==null 
                        || !checkChargeBean.getObsInd().equalsIgnoreCase(OBSOLETE))) {
                activeCharges.add(checkChargeBean);
            }
        }

        // if so, renumber (in case we have deleted other than the last charge)
        if (activeCharges.size() > 0) {
            Collections.sort(activeCharges, CHARGE_SEQ_ORDER);
            Iterator activeChargesIt = activeCharges.iterator();
            int i = 0;
            while (activeChargesIt.hasNext()) {
                XhbCharge orderChargeBean = (XhbCharge) activeChargesIt.next();
                orderChargeBean.setCrestChargeSeqNo(new Integer(++i));
            }
        }
    }

    static final Comparator<XhbOffence> OFFENCE_SEQ_ORDER = new Comparator<XhbOffence>() {
        public int compare(XhbOffence o1, XhbOffence o2) {
            XhbOffence offence1 = o1;
            XhbOffence offence2 = o2;
            
            if(offence1==null || 
               offence2==null || 
               offence1.getCrestOffenceSeqNo()==null ||
               offence2.getCrestOffenceSeqNo()==null)
            {
                return 0;
            }            
            
            // a negative integer, zero, or a positive integer as the first
            // argument is less than, equal to, or greater than the second
            if (offence1.getCrestOffenceSeqNo().intValue() < offence2.getCrestOffenceSeqNo().intValue()) {
                return -1;
            } else if (offence1.getCrestOffenceSeqNo().intValue() > offence2.getCrestOffenceSeqNo().intValue()) {
                return 1;
            } else {
                return 0;
            }
        }
    };

    static final Comparator<XhbCharge> CHARGE_SEQ_ORDER = new Comparator<XhbCharge>() {
        public int compare(XhbCharge o1, XhbCharge o2) {
            XhbCharge charge1 = o1;
            XhbCharge charge2 = o2;
            
            if(charge1==null || 
               charge2==null || 
               charge1.getCrestChargeSeqNo()==null ||
               charge2.getCrestChargeSeqNo()==null)
            {
                return 0;
            }
            
            // a negative integer, zero, or a positive integer as the first
            // argument is less than, equal to, or greater than the second
            if (charge1.getCrestChargeSeqNo().intValue() < charge2.getCrestChargeSeqNo().intValue()) {
                return -1;
            } else if (charge1.getCrestChargeSeqNo().intValue() > charge2.getCrestChargeSeqNo().intValue()) {
                return 1;
            } else {
                return 0;
            }
        }
    };

}
