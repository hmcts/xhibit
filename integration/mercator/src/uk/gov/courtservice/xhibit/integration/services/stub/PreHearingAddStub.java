package uk.gov.courtservice.xhibit.integration.services.stub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_breach.XhbBreachBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_breach.XhbBreachBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbChargeBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourt;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantChargeBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_court.XhbRefCourt;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_court.XhbRefCourtBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DefendantOnOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.LinkCountDefValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.OriginalChargeVO;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.OutputTransformationException;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.TransformationException;
import uk.gov.courtservice.xhibit.integration.services.MercatorException;

/**
 * <p>
 * Title: PreHearingUpdateStub
 * </p>
 * <p>
 * Description: Mimics mercator methods which fall into the add pre hearing area
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author Sarah Tong
 * @version $Id: PreHearingAddStub.java,v 1.32 2009/06/22 11:15:29 powellja Exp $
 */
public class PreHearingAddStub {
    private static final Logger log = CSServices.getLogger(PreHearingAddStub.class);

    private static final String DEF_ON_CHARGE = "C";

    public PreHearingAddStub() {
        // Empty
    }

    public Integer[] addJoinderChargeToCase(ChargeValue[] chargeValue) throws MercatorException {
        log.debug("addJoinderChargeToCase(ChargeValue[] chargeValues start with : " + chargeValue);
        Integer[] chargeIds = new Integer[chargeValue.length];
        for (int i = 0; i < chargeValue.length; i++) {
            chargeIds[i] = addChargeToCase(chargeValue[i]);
        }
        return chargeIds;
    }

    /**
     * Mimics the same method on the <code>IntegrationFacadeImple</code>.
     * Updates are not made to CREST, inserts will be made to the xhb_charge,
     * xhb_offence and xhb_defendant_on_offence tables with values from the
     * supplied VO. An insert will be made to xhb_breach and
     * xhb_defendant_on_charge tables if the charge is a breach. The xhb_case
     * table may be updated with dateIndRec and indResp.
     *
     * @param chargeValue
     *            Contains the details to be added to xhibit
     * @return The chargeId of the new charge
     * @throws MercatorException
     *             never
     */
    public Integer addChargeToCase(ChargeValue chargeValue) throws MercatorException {
        log.debug("addChargeToCase(ChargeValue chargeValue) start with : " + chargeValue);

        XhbCase theCase = XhbCaseBeanHelper2.findByPrimaryKey(chargeValue.getCaseID());

        XhbChargeBasicValue chargeBasicValue = new XhbChargeBasicValue();

        chargeBasicValue.setChargeType(chargeValue.getChargeType());

        // not going to CREST, so make these values up
        chargeBasicValue.setCrestChargeId(new Integer(1));
        chargeBasicValue.setCrestChargeSeqNo(getNextCrestChargeSeqNum(theCase, chargeValue.getChargeType()));

        if (chargeValue.getIndSignedDate() != null) {
            chargeBasicValue.setIndSignedDate(chargeValue.getIndSignedDate().getTime());
        }
        chargeBasicValue.setObsInd("N");

        if (chargeValue.getProsPaperServedDate() != null) {
            chargeBasicValue.setProsPaperServedDate(chargeValue.getProsPaperServedDate().getTime());
        }

        if (chargeValue.getBreachValue() != null) {
            chargeBasicValue.setRefSystemCodeId(chargeValue.getBreachValue().getRefSystemCodeID());
        }

        log.debug("About to create new charge...");
       
        chargeBasicValue.setCaseId(theCase.getCaseId());
        XhbCharge newCharge = XhbChargeBeanHelper2.createLocal(chargeBasicValue);
        log.debug("New chargeID = " + newCharge.getChargeId());

        if (chargeValue.getChargeType().equals(ChargeTypes.INDICTMENT.getChargeType())) {
            log.debug("Got indictment");
            // update the dateIndRec and indictResp
            addChargeCaseUpdates(chargeValue);
        }

        // add offences
        if (chargeValue.getOffenceValues() != null) {
            addOffences(chargeValue.getOffenceValues(), newCharge);
        }

        if (ChargeTypes.isBreachChargeType(chargeValue.getChargeType())) {
            // add the breach value and defendantOnCharge record
            Integer defChargeId = createDefCharge(chargeValue.getDefendantID(), newCharge);
            
            XhbCourt court = XhbCourtBeanHelper2.findByPrimaryKey(theCase.getCourtId());
            Collection courts = XhbRefCourtBeanHelper2.findByCrestCode(court.getCrestCourtId());
            Iterator it = courts.iterator();
            XhbRefCourt refCourt = (XhbRefCourt) it.next();
            addBreach(chargeValue.getBreachValue(), newCharge, defChargeId,refCourt.getRefCourtId());

        }
        log.debug("addChargeToCase(ChargeValue chargeValue) end, returning id " + newCharge.getChargeId());
        return newCharge.getChargeId();
    }

    /**
     * Mimics the same method on the <code>IntegrationFacadeImple</code>.
     * Updates are not made to CREST, inserts will be made to the xhb_offence,
     * and xhb_defendant_on_offence tables with values from the supplied VO.
     *
     * @param offenceValue
     *            Contains the details to be added to xhibit
     * @return The offenceId of the new offence
     * @throws MercatorException
     *             never
     */
    public Integer addOffence(OffenceValue offenceValue) throws MercatorException {
        log.debug("addOffence() start with offenceValue : " + offenceValue);
        XhbCharge theCharge = XhbChargeBeanHelper2.findByPrimaryKey(offenceValue.getChargeID());
        ArrayList<OffenceValue> offenceValues = new ArrayList<OffenceValue>();
        offenceValues.add(offenceValue);
        Integer[] newOffenceIds = addOffences(offenceValues, theCharge);
        // only one id in the array as we've only added one offence
        log.debug("addOffence() end returning : " + newOffenceIds[0]);
        return newOffenceIds[0];
    }

    /**
     * Mimics the same method on the <code>IntegrationFacadeImple</code>.
     * Updates are not made to CREST, inserts will be made to the
     * xhb_defendant_on_offence table with values from the supplied VO.
     *
     * @param linkVal
     *            Contains the details to be added to xhibit
     * @throws MercatorException
     *             never
     */
    public void linkCountsAndDefendants(LinkCountDefValue linkVal) throws MercatorException {
        log.debug("start linkCountsAndDefendants() with linkVal : " + linkVal);
        Iterator dooValuesIt = linkVal.getDefendantOnOffenceValues().iterator();

        // create def on offence records for each pair
        while (dooValuesIt.hasNext()) {
            DefendantOnOffenceValue dooValue = (DefendantOnOffenceValue) dooValuesIt.next();
            log.debug("Creating def on offence from : " + dooValue);
            XhbDefendantOnOffenceBasicValue dooBasicValue = new XhbDefendantOnOffenceBasicValue();
            
            dooBasicValue.setCrnId(dooValue.getCrn());
            dooBasicValue.setArrestDate(dooValue.getDateOfArrest().getTime());
            dooBasicValue.setChargeDate(dooValue.getDateOfCharge().getTime());
            dooBasicValue.setIsCommittedOnBail(dooValue.getIsCommittedOnBail());
            dooBasicValue.setSeqNo(dooValue.getSequenceNo());
            dooBasicValue.setDefendantOnCaseId(findDefOnCase(dooValue.getDefendantId(), linkVal.getCaseID()));
            dooBasicValue.setOffenceId(dooValue.getOffenceId());
            XhbDefendantOnOffenceBeanHelper2.create(dooBasicValue);
        }

        log.debug("end linkCountsAndDefendants()");
    }

    

    /**
     * This method is used to add an Original Charge. 
     * CaseID, DefendantId, DefendantOnCaseId, SeqNo and OriginalChargeFreeText are supplied
     * Note: The OriginalChargeFreeText is set in CrestOffenceFreeText of Offence
     * 
     * @param OriginalChargeValue 
     * 
     */
    public Integer addOriginalCharge(OriginalChargeVO originalChargeValue) 
             throws MercatorException, TransformationException, OutputTransformationException
    {
        log.debug("addOriginalCharge start with case Id " + originalChargeValue.getCaseId());
        
        Collection <OffenceValue>offenceValues = new ArrayList<OffenceValue>();
        Integer chargeId = null;
        Integer defOnOffenceId = null;
        ChargeValue charge = null;
        OffenceValue offence = null;
        DefendantOnOffenceComplexValue defendantOnOffenceValue = null;
        DefendantOnCaseBasicValue defendantOnCaseValue = null;
        
        charge = new ChargeValue(originalChargeValue.getCaseId(),ChargeTypes.getChargeType(originalChargeValue.getChargeType()));
        charge.setDefendantID(originalChargeValue.getDefendantId());        
        log.debug("addOriginalCharge Court ID:" + originalChargeValue.getCourtId());
        charge.setCourtID(originalChargeValue.getCourtId());
        offence = new OffenceValue();
           
        offence.setCrestOffenceFreeText(originalChargeValue.getOriginalCharge());
        offence.setRefOffenceID(originalChargeValue.getRefOffenceId());
        defendantOnOffenceValue = new DefendantOnOffenceComplexValue();
        log.debug("addOriginalCharge seq no:" + originalChargeValue.getSeqNo());
        
        defendantOnOffenceValue.setSeqNo(originalChargeValue.getSeqNo());
        defendantOnOffenceValue.setDefendantOnCaseId(originalChargeValue.getDefendantOnCaseId());
        
        defendantOnCaseValue = new DefendantOnCaseBasicValue();
        defendantOnCaseValue.setCaseID(originalChargeValue.getCaseId());
        defendantOnCaseValue.setDefendantID(originalChargeValue.getDefendantId());
                
        defendantOnOffenceValue.setDefendantOnCase(defendantOnCaseValue);
                
        offence.addDefOnOffenceComplexValue(originalChargeValue.getDefendantId(), defendantOnOffenceValue);
        
        offenceValues.add(offence);
        
        charge.setOffenceValues(offenceValues);
        
        //This will create all the PKs and set all the FK references        
        chargeId = addChargeToCase(charge);
        log.debug("chargeId " + chargeId);
        
        Collection defOnOffences =  XhbDefendantOnOffenceBeanHelper2.findByChargeId(chargeId);
        
        if(!defOnOffences.isEmpty())
        {
            for(Object defOnOffence:defOnOffences)
            {
                //should only be one for the original charge Charge ID
                defOnOffenceId=((XhbDefendantOnOffence)defOnOffence).getDefendantOnOffenceId();
                log.debug("defOnOffenceId " + defOnOffenceId);
            }
        }
        
        log.debug("End addOriginalCharge with Case Id: " + originalChargeValue.getCaseId() + ", charge Id: " + chargeId + ", DefOnOffenceId: " + defOnOffenceId);
        
        return defOnOffenceId;
    }
    
    // package\default level access as this method is used by the
    // PreHearingUpdateStub
    static void addBreachPlea(String plea, Integer defChargeId) {
        XhbPleaBasicValue pleaBasicValue = new XhbPleaBasicValue();
        pleaBasicValue.setDefendantChargeId(defChargeId);
        pleaBasicValue.setBreachAdmitted(plea);
        pleaBasicValue.setDefOnChargeOrOffence(DEF_ON_CHARGE);
        XhbPleaBeanHelper2.create(pleaBasicValue);
    }

    // -------------------------- Private Methods
    // --------------------------- //
    private Integer getNextCrestChargeSeqNum(XhbCase caze, String chargeType) {
        int seqNum = 0;
        Collection charges = caze.getXhbCharges();
        Iterator chargesIt = charges.iterator();
        while (chargesIt.hasNext()) {
            XhbCharge charge = (XhbCharge) chargesIt.next();
            
            if (charge.getChargeType().equals(chargeType) && 
                charge.getCrestChargeSeqNo() != null &&
                charge.getCrestChargeSeqNo().intValue() > seqNum) {
                
                seqNum = charge.getCrestChargeSeqNo().intValue();
            }
        }

        return new Integer(seqNum + 1);
    }

    private void addChargeCaseUpdates(ChargeValue chargeValue) {
        XhbCaseBasicValue caseBasicValue = XhbCaseBeanHelper2.findByPrimaryKeyValue(chargeValue.getCaseID());
        // only set these for the first indictment on the case, if they are
        // already set do not update
        if (caseBasicValue.getDateIndRec() == null) {
            if (chargeValue.getDateIndRec() != null)
                caseBasicValue.setDateIndRec(chargeValue.getDateIndRec().getTime());
            caseBasicValue.setIndictResp(chargeValue.getIndResp());
        }
    }

    private Integer[] addOffences(Collection offenceValues, XhbCharge newCharge) {
        log.debug("addOffences() start");
        Iterator itOffences = offenceValues.iterator();

        ArrayList<Integer> newOffenceIds = new ArrayList<Integer>();

        while (itOffences.hasNext()) {
            Integer nextSeqNum = null;

            OffenceValue offenceValue = (OffenceValue) itOffences.next();
            log.debug("adding offence " + offenceValue);
            XhbOffenceBasicValue offenceBasicValue = new XhbOffenceBasicValue();
            offenceBasicValue.setChargeId(newCharge.getChargeId());
            offenceBasicValue.setCrestOffenceFreetext(offenceValue.getCrestOffenceFreeText());
            offenceBasicValue.setCrestHooClassFreetext(offenceValue.getCrestHOClass());
            offenceBasicValue.setCrestHooSubclassFreetext(offenceValue.getCrestHOSubclass());

            log.debug("crestOffenceId = " + offenceValue.getCrestOffenceID());
            offenceBasicValue.setCrestOffenceId(new Integer(1));

            if (offenceValue.getCrestOffenceSeqNo() != null) {
                // this is set for indictment offences
                offenceBasicValue.setCrestOffenceSeqNo(offenceValue.getCrestOffenceSeqNo());
            } else {
                if (nextSeqNum == null) {
                    nextSeqNum = getNextCrestOffenceSeqNum(newCharge);
                } else {
                    nextSeqNum = new Integer(nextSeqNum.intValue() + 1);
                }
                offenceBasicValue.setCrestOffenceSeqNo(nextSeqNum);
            }
            if (offenceValue.getMultiple() != null) {
                offenceBasicValue.setMultiple(new Byte(offenceValue.getMultiple().byteValue()));
            }

            offenceBasicValue.setObsInd("N");
            offenceBasicValue.setOffenceId(offenceValue.getOffenceID());
            offenceBasicValue.setRefOffenceId(offenceValue.getRefOffenceID());
            offenceBasicValue.setRefSystemCodeId(offenceValue.getRefSystemCodeID());

            if(offenceValue.getAddressValue()!=null)
            {
                AddressValue addressValue = offenceValue.getAddressValue();
                log.debug("Create a new Address with AddressValue " + addressValue);
            
                XhbAddressBasicValue addrBasicValue = new XhbAddressBasicValue();
            
                addrBasicValue.setAddress1(addressValue.getAddress1());
                addrBasicValue.setAddress2(addressValue.getAddress2());
                addrBasicValue.setAddress3(addressValue.getAddress3());
                addrBasicValue.setAddress4(addressValue.getAddress4());
                addrBasicValue.setCountry(addressValue.getCountry());
                addrBasicValue.setCounty(addressValue.getCounty());
                addrBasicValue.setPostcode(addressValue.getPostcode());
                addrBasicValue.setTown(addressValue.getTown());
            
                addrBasicValue = XhbAddressBeanHelper2.create(addrBasicValue);            

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
            
            log.debug("About to create new offence...");
            offenceBasicValue = XhbOffenceBeanHelper2.create(offenceBasicValue);
            log.debug("Created new offence with id " + offenceBasicValue.getOffenceId());
            // casting the Integer down to Integer - tempory measure due to
            // the majority
            // of the app staying with int pk's when the full app is
            // converted this
            // method will return a Integer. Assumes pk's will not require >
            // int vaule
            // in development
            newOffenceIds.add(offenceBasicValue.getOffenceId());
            XhbOffence newOffence = XhbOffenceBeanHelper2.findByPrimaryKey(offenceBasicValue.getOffenceId());

            createDefOnOffences(offenceValue.getDefOnOffenceBasicValues(), newOffence);
        } // end while offences has next

        Integer[] returnVal = newOffenceIds.toArray(new Integer[newOffenceIds.size()]);
        log.debug("addOffences() end, returning : " + returnVal);
        return returnVal;
    }

    private void createDefOnOffences(HashMap doos, XhbOffence newOffence) {
        log.debug("createDefOnOffences() start");
        Collection defendantIds = doos.keySet();
        Iterator itDefendantIds = defendantIds.iterator();
        while (itDefendantIds.hasNext()) {
            Integer defendantId = (Integer) itDefendantIds.next();
            XhbDefendantOnOffenceBasicValue dooBV = (XhbDefendantOnOffenceBasicValue) doos.get(defendantId);
            log.debug("Got def on offence " + dooBV);
            // find the defendant on case
            Integer defendantOnCaseId = findDefOnCase(defendantId, newOffence.getXhbCharge().getCaseId());

            XhbDefendantOnOffenceBasicValue xhbDooBV = new XhbDefendantOnOffenceBasicValue();

            xhbDooBV.setAppealAgainstType(dooBV.getAppealAgainstType());
            xhbDooBV.setCrnId(dooBV.getCrnId());
            xhbDooBV.setVcoFlag(dooBV.getVcoFlag());
            if (dooBV.getVcoDate() != null) {
                xhbDooBV.setVcoDate(dooBV.getVcoDate());
            }
            xhbDooBV.setIsStayed(dooBV.getIsStayed());
            xhbDooBV.setObsInd(dooBV.getObsInd());
            log.debug("about to create def on offence with getSeqNo:" + dooBV.getSeqNo());
            xhbDooBV.setDefendantOnCaseId(defendantOnCaseId);
            xhbDooBV.setOffenceId(newOffence.getOffenceId());
            xhbDooBV.setSeqNo(dooBV.getSeqNo());
            xhbDooBV.setArrestDate(dooBV.getArrestDate());
            xhbDooBV.setChargeDate(dooBV.getChargeDate());
            xhbDooBV.setIsCommittedOnBail(dooBV.getIsCommittedOnBail());
            xhbDooBV = XhbDefendantOnOffenceBeanHelper2.create(xhbDooBV);
            log.debug("new doo id = " + xhbDooBV.getDefendantOnOffenceId());
        } // end while more dooBVs
    }

    private Integer createDefCharge(Integer defendantId, XhbCharge charge) {
        Integer defOnCaseId = findDefOnCase(defendantId, charge.getCaseId());
        XhbDefendantChargeBasicValue dcBasicValue = new XhbDefendantChargeBasicValue();
        log.debug("About to create new defendantCharge...");
        dcBasicValue.setDefendantOnCaseId(defOnCaseId);
        dcBasicValue.setChargeId(charge.getChargeId());
        dcBasicValue = XhbDefendantChargeBeanHelper2.create(dcBasicValue);
        log.debug("Created new defendantCharge returning id : " + dcBasicValue.getDefendantChargeId());
        // casting the Integer down to Integer - tempory measure due to the
        // majority
        // of the app staying with int pk's when the full app is converted this
        // method will return a Integer. Assumes pk's will not require > int
        // vaule
        // in development
        return dcBasicValue.getDefendantChargeId();
    }

    private void addBreach(BreachValue breachValue, XhbCharge charge, Integer defChargeId, Integer refCourtId) {
        log.debug("addBreach() start with breachValue " + breachValue);
        XhbBreachBasicValue breachBasicValue = new XhbBreachBasicValue();

        breachBasicValue.setBreachType(breachValue.getBreachType());
        breachBasicValue.setBringBack(breachValue.getBringBack());
        if(breachValue.getDatePut() != null){
            breachBasicValue.setDatePut(breachValue.getDatePut().getTime());
        }
        breachBasicValue.setObsInd("N");
        breachBasicValue.setOriginalCourtType(breachValue.getOriginalCourtType());
        breachBasicValue.setOriginalSentence(breachValue.getOriginalSentence());
        if(breachValue.getOriginalSentenceDate() != null){
            breachBasicValue.setOriginalSentenceDate(breachValue.getOriginalSentenceDate().getTime());
        }

        // XhbRefCourt refCourt = XhbRefCourtBeanHelper2.findByPrimaryKey(
        // breachValue.getOriginalCourtID());

        log.debug("About to create breach...");
        if(breachValue.getOriginalCourtID() != null){
            breachBasicValue.setRefCourtId(breachValue.getOriginalCourtID());
        }else{
            breachBasicValue.setRefCourtId(refCourtId);
        }
        breachBasicValue.setChargeId(charge.getChargeId());
        XhbBreachBeanHelper2.create(breachBasicValue);
        log.debug("New breach created.");

        // create plea if breach admitted is set
        if (breachValue.getPlea() != null) {
            addBreachPlea(breachValue.getPlea(), defChargeId);
        }
    }

    private Integer getNextCrestOffenceSeqNum(XhbCharge charge) {
        int seqNum = 0;
        Collection offences = charge.getXhbOffences();
        Iterator offencesIt = offences.iterator();
        while (offencesIt.hasNext()) {
            XhbOffence offence = (XhbOffence) offencesIt.next();
            if (offence.getCrestOffenceSeqNo().intValue() > seqNum)
                seqNum = offence.getCrestOffenceSeqNo().intValue();
        }

        return new Integer(seqNum + 1);
    }

    private Integer findDefOnCase(Integer defendantId, Integer caseId) {
        return XhbDefendantOnCaseBeanHelper2.findByDefendantAndCase(defendantId, caseId).getDefendantOnCaseId();
    }
}
