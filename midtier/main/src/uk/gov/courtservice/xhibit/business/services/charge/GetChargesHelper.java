package uk.gov.courtservice.xhibit.business.services.charge;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.refoffence.RefOffence;
import uk.gov.courtservice.xhibit.business.entities.refoffence.RefOffenceHome;
import uk.gov.courtservice.xhibit.business.entities.xhb_breach.XhbBreach;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendant;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffence;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocal;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantHelper;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantOnOffenceHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.JoinderChargeInfoValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.JoinderOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author Sarah Tong
 * @version 1.0
 *
 * <Change History/>
 * <P>
 * 01/05/03 - MH - Updated to include the defOnCaseID since this is required to
 * get Verdicts.
 *
 * <P>
 * 18/09/03 - SG - BUG : 54102, Changed to use the new BreachHelper class due to
 * extra processing required to retrieve the breach plea.
 * 
 * <P>
 * 17/11/08 - JP - PR5598. Added a new method to return a list of all charges (including those which are 
 * obsolete) for a particular case number. This is needed by the thick client as deleted sequence numbers cannot be re-used.
 */
public class GetChargesHelper implements UncodedOffenceInterface {
    private static final Logger LOG = CSServices.getLogger(GetChargesHelper.class);

    // error keys
    private static final String BREACH_DELETED = "getcharges.breachdeleted";

    // private static final String NO_DEFENDANTS_FOR_OFFENCE =
    // "getcharges.nodefendantsofence";
    private static final String NO_DEFENDANTS_FOR_CHARGE = "getcharges.nodefendantscharge";

    private static final String CASE_NOT_FOUND = "getcharges.casenotfound";

    private static final String ObsInd = "Y";

    private final JoinderIndictmentHelper jIHelper = new JoinderIndictmentHelper();

    private final BreachHelper breachHelper = new BreachHelper();

    private final DefendantOnOffenceHelper dofHelper = new DefendantOnOffenceHelper();

    private final Vector<ChargeValue> chargeValues = new Vector<ChargeValue>(); // The charge vos

    private XhbCaseBasicValue caseBasicValue;

    private Integer defOnCaseID;
    
    
    /**
     * This method returns a List of all charges, including those which have been logically deleted (marked
     * as obsolete)
     * @param caseID
     * @return
     * @throws ChargeControllerException
     */
    public Collection getChargesList(Integer caseID) throws ChargeControllerException {
        LOG.debug("getChargesList START");
        try {
            // Get the case value object for this case
            XhbCase caseBean = XhbCaseBeanHelper2.findByPrimaryKey(caseID);

            caseBasicValue = caseBean.getData();

            // Get all the charges objects for this case
            Collection chargeBeans = caseBean.getXhbCharges();
            Iterator chargesIterator = chargeBeans.iterator();

            // instantiate joinderCharges HashMap keyed on joinderId,
            // contains an array of chargeIds
            HashMap<Integer, Integer[]> joinderCharges = new HashMap<Integer, Integer[]>();

            while (chargesIterator.hasNext()) {
                Vector<OffenceValue> offenceValues = new Vector<OffenceValue>(); // The offence vos for
                // this charge
                XhbCharge chargeBean = (XhbCharge) chargesIterator.next();

                // check if joinder indictment if it is treat it separately
                HashMap<Integer, Integer[]> jcs = jIHelper.getJoinderCharges(chargeBean.getChargeId());
                if (jcs != null) {
                    joinderCharges.putAll(jcs);
                    continue;
                }

                // populate a ChargeValue object for this Charge, includes a
                // BreachValue
                ChargeValue chargeValue = populateChargeValueForList(chargeBean);

                // Get the collection of offence value object for this charge ID
                Collection offenceBeans = chargeBean.getXhbOffences();
                Iterator offencesIterator = offenceBeans.iterator();

                // create a collection of OffenceValue objects
                while (offencesIterator.hasNext()) {
                    XhbOffence offenceBean = (XhbOffence) offencesIterator.next();                    
                    // populate an OffenceValue object for this offence,
                    // includes DefendantValue(s)
                    // and DefendantOnOffenceBasicValue(s)
                    OffenceValue offenceValue = populateOffenceValue(offenceBean, caseBean, chargeValue.getChargeID());
                    offenceValues.add(offenceValue);
                }

                // set the charge value object with this collection of offence
                // value objects
                chargeValue.setOffenceValues(offenceValues);
                // add this charge value object to the Collection of charge
                // value object
                chargeValues.add(chargeValue);
            }

            // now set joinder charge if any
            if (!joinderCharges.isEmpty()) {
                chargeValues.addAll(getJoinderIndictments(joinderCharges, caseBean,true));
            }                        
                
            LOG.debug("getChargesList END");
            
            return chargeValues;
        } catch (XhbCaseBeanNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new ChargeControllerException(CASE_NOT_FOUND, new Object[] { caseID },
                    "Error getting charges, case not found. CaseID = " + caseID, e);
        }
    }
    
    /**
     * Returns all the Charge, Breach, Offence and Defendant information for a
     * given case.
     *
     * @param caseID
     *            Unique identifer for case
     * @param chargeLogRequired
     *            true if you require the charge log events. Should only be for
     *            the charges screen.
     * @return ChargeCompositeValue
     * @throws ChargeControllerException
     */
    public ChargeCompositeValue getCharges(Integer caseID, boolean chargeLogRequired) throws ChargeControllerException {
        LOG.debug("getCharges START");
        try {
            // Get the case value object for this case
            XhbCase caseBean = XhbCaseBeanHelper2.findByPrimaryKey(caseID);

            caseBasicValue = caseBean.getData();

            // Get all the charges objects for this case
            Collection chargeBeans = caseBean.getXhbCharges();
            Iterator chargesIterator = chargeBeans.iterator();

            // instantiate joinderCharges HashMap keyed on joinderId,
            // contains an array of chargeIds
            HashMap<Integer, Integer[]> joinderCharges = new HashMap<Integer, Integer[]>();

            while (chargesIterator.hasNext()) {
                Vector<OffenceValue> offenceValues = new Vector<OffenceValue>(); // The offence vos for
                // this charge
                XhbCharge chargeBean = (XhbCharge) chargesIterator.next();

                // if this charge has been logically deleted ignore and continue
                // to the next charge
                if (chargeBean.getObsInd() != null && chargeBean.getObsInd().equalsIgnoreCase(ObsInd)) {
                    continue;
                }

                // check if joinder indictment if it is treat it separately
                HashMap<Integer, Integer[]> jcs = jIHelper.getJoinderCharges(chargeBean.getChargeId());
                if (jcs != null) {
                    joinderCharges.putAll(jcs);
                    continue;
                }

                // populate a ChargeValue object for this Charge, includes a
                // BreachValue
                ChargeValue chargeValue = populateChargeValue(chargeBean);

                // Get the collection of offence value object for this charge ID
                Collection offenceBeans = chargeBean.getXhbOffences();
                Iterator offencesIterator = offenceBeans.iterator();

                // create a collection of OffenceValue objects
                while (offencesIterator.hasNext()) {
                    XhbOffence offenceBean = (XhbOffence) offencesIterator.next();
                    // if this offence has been logically deleted ignore and
                    // continue to the next charge
                    if (offenceBean.getObsInd() != null && offenceBean.getObsInd().equalsIgnoreCase(ObsInd)) {
                        continue;
                    }
                    // populate an OffenceValue object for this offence,
                    // includes DefendantValue(s)
                    // and DefendantOnOffenceBasicValue(s)
                    OffenceValue offenceValue = populateOffenceValue(offenceBean, caseBean, chargeValue.getChargeID());
                    offenceValues.add(offenceValue);
                }

                // set the charge value object with this collection of offence
                // value objects
                chargeValue.setOffenceValues(offenceValues);
                // add this charge value object to the Collection of charge
                // value object
                chargeValues.add(chargeValue);
            }

            // now set joinder charge if any
            if (!joinderCharges.isEmpty()) {
                chargeValues.addAll(getJoinderIndictments(joinderCharges, caseBean,false));
            }            
            
            ChargeCompositeValue returnValue = new ChargeCompositeValue(caseBasicValue, chargeValues);

            if (chargeLogRequired) {
                returnValue.setChargeLogItems(getChargesCourtLog(caseID));
            }

            // get all the defendants for the case
            CaseControllerLocal caseController = (CaseControllerLocal) CSServices.getEJBServices().createLocalSession(
                    CaseControllerLocalHome.class);
            Collection allDefendants = caseController.getDefendants(caseID);

            returnValue.setAllDefendants(allDefendants);
            LOG.debug("getCharges END");
            
            return returnValue;
        } catch (XhbCaseBeanNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new ChargeControllerException(CASE_NOT_FOUND, new Object[] { caseID },
                    "Error getting charges, case not found. CaseID = " + caseID, e);
        } catch (DefendantControllerException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new ChargeControllerException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);
        } catch (CaseControllerException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new ChargeControllerException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);
        }
    }

    /**
     * Extracted method to acquire all of the court log events for a particular
     * case that is a charge event.
     *
     * @param caseId
     * @return
     * @throws CSUnrecoverableException
     */
    private CourtLogViewValue[] getChargesCourtLog(Integer caseId) {
        LOG.debug("Will search for court log events for caseid : " + caseId);

        return CourtLogWorkFlow.getCourtLog(caseId, "Charges");
    }

    /**
     * Gets joinder charges for a specific joinderId (used for joinderXML table)
     *
     * @param caseID
     *            the id of the case
     * @param joinderId
     *            the id of the joinder
     * @return ChargeCompositeValue all the charge information.
     * @throws ChargeControllerException
     */
    public ChargeCompositeValue getJoinderCharges(Integer caseID, Integer joinderId) throws ChargeControllerException {
        try {
            // Get the case value object for this case
            XhbCase caseBean = XhbCaseBeanHelper2.findByPrimaryKey(caseID);

            caseBasicValue = caseBean.getData();

            // Get all the charges objects for this case
            Collection chargeBeans = caseBean.getXhbCharges();
            Iterator chargesIterator = chargeBeans.iterator();

            // instantiate joinderCharges HashMap keyed on joinderId,
            // contains an array of chargeIds
            HashMap<Integer, Integer[]> joinderCharges = new HashMap<Integer, Integer[]>();

            while (chargesIterator.hasNext()) {
                XhbCharge chargeBean = (XhbCharge) chargesIterator.next();

                // if this charge has been logically deleted ignore and continue
                // to the next charge
                if (chargeBean.getObsInd() != null && chargeBean.getObsInd().equalsIgnoreCase(ObsInd)) {
                    continue;
                }

                // check if joinder indictment for this joinderId if it is add
                // it
                HashMap<Integer, Integer[]> jcs = jIHelper.getJoinderCharges(chargeBean.getChargeId());
                if (jcs != null && jcs.containsKey(joinderId)) {
                    joinderCharges.putAll(jcs);
                }
            }

            // now set joinder charge if any
            if (!joinderCharges.isEmpty()) {
                chargeValues.addAll(getJoinderIndictments(joinderCharges, caseBean,false));
            }

            ChargeCompositeValue returnValue = new ChargeCompositeValue(caseBasicValue, chargeValues);

            // Charge log events are not required when building the charges
            // for
            // export.
            // returnValue.setChargeLogItems(getChargesCourtLog(caseID));

            // get all the defendants for the case
            CaseControllerLocal caseController = (CaseControllerLocal) CSServices.getEJBServices().createLocalSession(
                    CaseControllerLocalHome.class);
            Collection allDefendants = caseController.getDefendants(caseID);

            returnValue.setAllDefendants(allDefendants);

            return returnValue;
        } catch (XhbCaseBeanNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new ChargeControllerException(CASE_NOT_FOUND, new Object[] { caseID },
                    "Error getting charges, case not found. CaseID = " + caseID, e);
        } catch (DefendantControllerException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new ChargeControllerException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);
        } catch (CaseControllerException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new ChargeControllerException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);
        }
    }

    // -------------------------Private methods
    // -----------------------------------    
    /**
     * This method constructs a ChargeValue based on an XHBCharge bean. This method differs to populateChargeValue 
     * because it doesn't ignore obsolete charges/offences
     */
    private ChargeValue populateChargeValueForList(XhbCharge chargeBean) throws ChargeControllerException{
        ChargeValue chargeValue = new ChargeValue(chargeBean.getChargeId(), caseBasicValue.getCaseId(), ChargeTypes
                .getChargeType(chargeBean.getChargeType()), chargeBean.getCrestChargeId(), chargeBean
                .getCrestChargeSeqNo());
        
        // if this charge is a breach get the breach value
        if (chargeValue.getChargeTypeDescription().equals(ChargeTypes.BREACH.getTypeDescription())) {

            XhbBreach breach = chargeBean.getXhbBreach();

            // for breaches the defendant is stored at the charge level so
            // set the defendant id
            chargeValue.setDefendantID(getChargeDefendant(chargeValue.getChargeID()));
            chargeValue.setDefendantOnChargeID(defOnCaseID);

            BreachValue breachValue;
            if (breach != null) {
                breachValue = 
                    breachHelper.getBreachValue(breach, caseBasicValue.getCaseId(), chargeBean
                            .getRefSystemCodeId(), defOnCaseID);
            } else if (chargeBean.getObsInd() != null && chargeBean.getObsInd().equals("Y")) {
                // Usually the breach will have obs_ind set to Y if the charge has.
                // Cope with the case where the breach has been physically deleted.
                breachValue = new BreachValue();
            } else {
                throw new ChargeControllerException(
                        BREACH_DELETED, "Breach details for charge have been deleted.");
            }

            chargeValue.setBreachValue(breachValue);

            LOG.debug("Defendant ID : " + chargeValue.getDefendantID());
            LOG.debug("Defendant On Charge ID : " + chargeValue.getDefendantOnChargeID());
        } else if (chargeValue.getChargeTypeDescription().equals(ChargeTypes.MISC_APPEAL.getTypeDescription())) {
            // for miscellaneous appeals the defendant is stored at the
            // charge level so set the defendant id
            chargeValue.setDefendantID(getChargeDefendant(chargeValue.getChargeID()));
            chargeValue.setDefendantOnChargeID(defOnCaseID);

            LOG.debug("Defendant ID : " + chargeValue.getDefendantID());
            LOG.debug("Defendant On Charge ID : " + chargeValue.getDefendantOnChargeID());
        }
        
        return chargeValue;
    }
    
    
    private ChargeValue populateChargeValue(XhbCharge chargeBean) throws ChargeControllerException {
        // convert Timestamps to Calendar
        Calendar prosPaperServedDate = Calendar.getInstance();
        if (chargeBean.getProsPaperServedDate() != null)
            prosPaperServedDate.setTime(chargeBean.getProsPaperServedDate());
        else
            prosPaperServedDate = null;
        Calendar dateIndRec = Calendar.getInstance();
        if (chargeBean.getXhbCase().getDateIndRec() != null)
            dateIndRec.setTime(chargeBean.getXhbCase().getDateIndRec());
        else
            dateIndRec = null;
        Calendar indSignedDate = Calendar.getInstance();
        if (chargeBean.getIndSignedDate() != null)
            indSignedDate.setTime(chargeBean.getIndSignedDate());
        else
            indSignedDate = null;

        ChargeValue chargeValue = new ChargeValue(chargeBean.getChargeId(), caseBasicValue.getCaseId(), ChargeTypes
                .getChargeType(chargeBean.getChargeType()), chargeBean.getCrestChargeId(), chargeBean
                .getCrestChargeSeqNo(), prosPaperServedDate, null,
                // breachValue will be set later if exists
                null, // offence vos are set later
                null, // defendant id will be set later for breach or Misc Appeal
                null, // court id not used here
                dateIndRec, indSignedDate, chargeBean.getXhbCase().getIndictResp());

        chargeValue.setUpdateCount(chargeBean.getVersion().intValue());
        
        // if this charge is a breach get the breach value
        if (ChargeTypes.isBreachChargeType(chargeValue.getChargeType())) {

            XhbBreach breach = chargeBean.getXhbBreach();

            // if this breach has been logically deleted throw a business
            // exception
            if (breach.getObsInd() != null && breach.getObsInd().equalsIgnoreCase(ObsInd)) {
                throw new ChargeControllerException(BREACH_DELETED, "Breach details for charge have been deleted.");
            }

            // set the breach value
            // chargeValue.setBreachValue(getBreachValue(breach,
            // caseBasicValue.getId(),
            // chargeBean.getRefSystemCodeId(),
            // defOnCaseID));

            // for breaches the defendant is stored at the charge level so
            // set the defendant id
            chargeValue.setDefendantID(getChargeDefendant(chargeValue.getChargeID()));
            chargeValue.setDefendantOnChargeID(defOnCaseID);

            BreachValue breachValue = breachHelper.getBreachValue(breach, caseBasicValue.getCaseId(), chargeBean
                    .getRefSystemCodeId(), defOnCaseID);

            chargeValue.setBreachValue(breachValue);

            LOG.debug("Defendant ID : " + chargeValue.getDefendantID());
            LOG.debug("Defendant On Charge ID : " + chargeValue.getDefendantOnChargeID());
        } else if (chargeValue.getChargeTypeDescription().equals(ChargeTypes.MISC_APPEAL.getTypeDescription())) {
            // for miscellaneous appeals the defendant is stored at the
            // charge level so set the defendant id
            chargeValue.setDefendantID(getChargeDefendant(chargeValue.getChargeID()));
            chargeValue.setDefendantOnChargeID(defOnCaseID);

            LOG.debug("Defendant ID : " + chargeValue.getDefendantID());
            LOG.debug("Defendant On Charge ID : " + chargeValue.getDefendantOnChargeID());
        }

        return chargeValue;
    }
    
    private OffenceValue populateOffenceValue(XhbOffence offenceBean, XhbCase caseBean, Integer chargeID)
            throws ChargeControllerException {
        XhbDefendantOnOffence defendantOnOffenceBean = null;
        Collection defendantsOnOffence = null; // The defendantOnOffence beans
        Iterator defendantsIterator = null; // The defendantOnOffence beans

        XhbDefendant defendant = null;
        Vector<DefendantValue> defendantValues = new Vector<DefendantValue>(); // The defendant vos for this
        // offence
        Vector<Integer> defendantIDs = new Vector<Integer>(); // The defendant IDs for this
        // offence
        // The DefendantOnOffenceBasicValues for this offence keyed on
        // defendantId
        HashMap<Integer, DefendantOnOffenceComplexValue> defendantOnOffences = 
            new HashMap<Integer, DefendantOnOffenceComplexValue>();

        // Get the collection of defendants for this offence
        defendantsOnOffence = offenceBean.getXhbDefendantOnOffences();
        defendantsIterator = defendantsOnOffence.iterator();

        // create the defendant and defendant on offence value objects
        while (defendantsIterator.hasNext()) {
            defendantOnOffenceBean = (XhbDefendantOnOffence) defendantsIterator.next();
            
            // ignore defendant on offence if OBS_IND is set
            if ( (defendantOnOffenceBean.getObsInd() == null 
                    || !defendantOnOffenceBean.getObsInd().equalsIgnoreCase("Y")))
            {
                defendant = defendantOnOffenceBean.getXhbDefendantOnCase().getXhbDefendant();

                // create the DefendantValue via the DefendantController
                DefendantValue defendantValue = populateDefendantValue(defendant.getDefendantId(), caseBean.getCaseId());
            
                if (defendantValue.getDefOnCaseBasicValue() != null
                        && (defendantValue.getDefOnCaseBasicValue().getObsInd() == null
                                || !defendantValue.getDefOnCaseBasicValue().getObsInd().equals("Y"))) {
                    // The defendant has not been deleted.
                    // create the DefendantOnOffenceComplexValue using the
                    // maintainer
                    DefendantOnOffenceComplexValue defOnOffenceComplexValue = 
                        getDefendantOnOffenceComplexValue(defendantOnOffenceBean);

                     populateOriginalResultAndDisposalDetail(defendantOnOffenceBean, defOnOffenceComplexValue);

                    defendantValues.add(defendantValue);
                    defendantOnOffences.put(defendant.getDefendantId(), defOnOffenceComplexValue);
                    defendantIDs.add(defendant.getDefendantId());
                } else {
                    LOG.debug("Defendant id " + defendantValue.getDefendantID() + " is obsolete");
                }
            }
        }

        OffenceValue offenceValue = getOffenceValue(offenceBean, caseBean.getCourtId(), chargeID);
        offenceValue.setDefendantIDs(defendantIDs);

        // Set the offence value object with the collection of defendant value objects
        offenceValue.setDefendantValues(defendantValues);
        
        // Set the offence value object with the DefendantOnOffenceComplexValues
        /* slightly misleading as should really be complex values now */
        offenceValue.setDefOnOffenceBasicValues(defendantOnOffences);

        return offenceValue;
    }

    
    /**
     * 
     * @param offenceBean
     * @param courtId
     * @param chargeID
     * @return
     */
    private OffenceValue getOffenceValue(XhbOffence offenceBean, Integer courtId, Integer chargeID) {
        try {
            RefOffence refOffence = (RefOffence) CSServices.getEJBServices().findLocalEntityByPrimaryKey(
                    RefOffenceHome.class, offenceBean.getRefOffenceId());

            String offenceDescription = new String();

            // Determine appropriate description for uncoded offence
            if (refOffence.getOffenceCode().trim().compareToIgnoreCase(UNCODED_OFFENCE_REFERENCE_CODE) != 0) {
                offenceDescription = refOffence.getOffenceDesc();
            } else {
                if (offenceBean.getCrestOffenceFreetext() != null) {
                    offenceDescription = offenceBean.getCrestOffenceFreetext();
                }
            }

            Byte multipleByte = offenceBean.getMultiple();
            Integer multiple = multipleByte == null ? null : new Integer(multipleByte.intValue());

            // Create an offence value object - // defendantIds not populated in this method
            OffenceValue offenceValue = new OffenceValue(offenceBean.getOffenceId(), chargeID,
                    offenceBean.getRefOffenceId(), null, offenceBean.getCrestOffenceFreetext(),
                    offenceBean.getCrestOffenceId(), offenceBean.getCrestOffenceSeqNo(), multiple, offenceDescription);
            offenceValue.setCaseID(caseBasicValue.getCaseId());
            offenceValue.setUpdateCount(offenceBean.getVersion().intValue());
            offenceValue.setRefSystemCodeID(offenceBean.getRefSystemCodeId());
            offenceValue.setPlea("");
            /** @todo Populate with Plea when we know where it comes from */
            offenceValue.setCourtID(courtId);
            offenceValue.setOffenceCode(refOffence.getOffenceCode());
            offenceValue.setStatute(refOffence.getStatute());
            offenceValue.setActSection(refOffence.getActSection());

            
            // CR58
            offenceValue.setCrestHOClass(offenceBean.getCrestHooClassFreetext());
            offenceValue.setCrestHOSubclass(offenceBean.getCrestHooSubclassFreetext());
            //RFC 1745
            LOG.debug("POPULATING ADDITIONAL OFFENCE DETAILS NO DATES!!!");
            LOG.debug("getStartDate: "+ offenceBean.getStartDate());
            LOG.debug("getEndDate: "+ offenceBean.getEndDate());
            LOG.debug("getForceLocationCode: "+ offenceBean.getForceLocationCode());
            
            if (offenceBean.getStartDate()!= null){
                Calendar offenceStartDate = Calendar.getInstance();
                offenceStartDate.setTime(offenceBean.getStartDate());
                offenceValue.setOffenceStartDateTime(offenceStartDate);
            }
            if (offenceBean.getEndDate()!= null){
                Calendar offenceEndDate = Calendar.getInstance();
                offenceEndDate.setTime(offenceBean.getEndDate());
                offenceValue.setOffenceEndDateTime(offenceEndDate);
            }
            if (offenceBean.getForceLocationCode() != null){
                LOG.debug("FORCE LOCATION CODE NOT NULL: "+ offenceBean.getForceLocationCode());
                offenceValue.setForceLocationCode(offenceBean.getForceLocationCode());
            }
            offenceValue.setAddressId(offenceBean.getLocationAddressId());
            
            if (offenceBean.getAppealType() != null) {
            	offenceValue.setAppealType(offenceBean.getAppealType());
            }
            
            return offenceValue;
        } catch (FinderException e) {
            // unexpected, the refOffenceID has come from the db so the
            // entity should
            // exist
            CSServices.getDefaultErrorHandler().handleError(e, GetChargesHelper.class);
            throw new CSUnrecoverableException(e);
        }
    }

    /**
     * Connects the original results and then the disposal details objects to
     * the defendant on offence object.
     *
     * @param doob
     * @param doocv
     */
    public void populateOriginalResultAndDisposalDetail(XhbDefendantOnOffence doob, DefendantOnOffenceComplexValue doocv) {
        LOG
                .debug("start method: populateOriginalResultAndDisposalDetail(DefendantOnOffence doob, DefendantOnOffenceComplexValue doocv) with doob = "
                        + doob + "\nand with doocv = " + doocv);

        /*
         * // WDF: original Results Now Stored in Disposal Table
         *
         * OriginalResultMaintainer originalResultMaintainer = new
         * OriginalResultMaintainer(); OriginalResult originalResult; Integer
         * originalResultPrimaryKey = doocv.getOriginalResultID(); // 0..1
         * relationship if(originalResultPrimaryKey != null) { try { // get the
         * original result if there is one originalResult =
         * originalResultMaintainer.findByPrimaryKey(originalResultPrimaryKey);
         *
         * DisposalDetailMaintainer disposalDetailMaintainer = new
         * DisposalDetailMaintainer(); // get the disposal details for the
         * original result Collection disposalDetails =
         * disposalDetailMaintainer.findByOriginalResultId(originalResult.getOriginalResultId());
         * OriginalResultComplexValue orcv =
         * originalResultMaintainer.getOriginalResultComplexValue(originalResult);
         *
         * doocv.setOriginalResultComplexValue(orcv);
         * orcv.setDisposalDetails(disposalDetailMaintainer.getDisposalDetailComplexValues(disposalDetails)); }
         * catch (ObjectNotFoundException onfe) {
         * CSServices.getDefaultErrorHandler().handleError(onfe,
         * GetChargesHelper.class); throw new EJBException(onfe); } }
         */

        LOG
                .debug("end method: populateOriginalResultAndDisposalDetail(DefendantOnOffence doob, DefendantOnOffenceComplexValue doocv) with doob = "
                        + doob + "\nand with doocv = " + doocv);
    }

    // Don't use this anymore, should use the DefendantController method to
    // populate DefendantValue objects so this is only coded and maintained
    // in
    // one place
    private DefendantValue populateDefendantValue(Integer defendantId, Integer caseId) throws ChargeControllerException {
        try {
            DefendantHelper defHelper = new DefendantHelper();
            return defHelper.getDefendantDetails(defendantId, caseId, true);
        } catch (DefendantControllerException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            Object[] params = e.getUserMessageAsMessage().getParameters();
            if (params.length > 0)
                throw new ChargeControllerException(e.getUserMessageAsMessage().getKey(), params, e.getMessage(), e);
            else
                throw new ChargeControllerException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);
        }

        /*
         * // convert Timestamps to Calendars Calendar dob =
         * Calendar.getInstance(); if (defendantBean.getDateOfBirth() != null)
         * dob.setTime(defendantBean.getDateOfBirth()); else dob = null;
         * Calendar lastConvDate = Calendar.getInstance(); if
         * (defendantBean.getLastConvictionDate() != null)
         * lastConvDate.setTime(defendantBean.getLastConvictionDate()); else
         * lastConvDate = null;
         *
         * DefendantValue defendantValue = new
         * DefendantValue(defendantBean.getDefendantId(),
         * defendantBean.getCrestDefendantId(), defendantBean.getFirstName(),
         * defendantBean.getMiddleName(), defendantBean.getSurname(),
         * defendantBean.getInitials(), dob, defendantBean.getGender(),
         * lastConvDate, defendantBean.getCourtId());
         * defendantValue.setUpdateCount(defendantBean.getVersion().intValue());
         *
         * return defendantValue;
         */
    }

    private Integer getChargeDefendant(Integer chargeID) throws ChargeControllerException {
        Collection defendantsOnCharge = XhbDefendantChargeBeanHelper2.findByChargeId(chargeID);
        Iterator defendantsIt = defendantsOnCharge.iterator();
        while (defendantsIt.hasNext()) {
            XhbDefendantCharge defOnCharge = (XhbDefendantCharge) defendantsIt.next();

            // MH - added defOnCaseID
            defOnCaseID = defOnCharge.getDefendantChargeId();

            // there should only be one defendant where the defendant is set
            // at the charge level
            return defOnCharge.getXhbDefendantOnCase().getDefendantId();
        }
        // if there was no defendant for this charge throw a business
        // exception
        throw new ChargeControllerException(NO_DEFENDANTS_FOR_CHARGE, new Object[] { chargeID },
                "GetChargesHelper:getChargeDefendant(): No defendant found for charge ID " + chargeID);
    }

    private Vector<ChargeValue> getJoinderIndictments(HashMap joinderCharges, XhbCase caseBean,boolean returnObsCharges) throws ChargeControllerException {
        String methodName = "getJoinderIndictments - ";
        if (LOG.isDebugEnabled())
            LOG.debug(methodName + "called with : case Id =  " + caseBean.getCaseId());

        Vector<ChargeValue> joinderIndictments = new Vector<ChargeValue>();

        // loop chargeId sets by joinder Id
        Iterator jIdIt = joinderCharges.keySet().iterator();

        while (jIdIt.hasNext()) {
            Integer joinderId = (Integer) jIdIt.next();
            Integer[] chargeIds = (Integer[]) joinderCharges.get(joinderId);

            // safety check, shouldn't happen
            if (chargeIds.length == 0) {
                LOG.debug("joinderId is " + joinderId + " but no chargeIds.");
                continue;
            }

            // set chargevalues based on the charge for this case so that we
            // get the correct charge sequence number

            XhbCharge ch = null;
            XhbCharge chargeBean = null;
            boolean chargeSet = false;
            JoinderChargeInfoValue[] joinderChargeInfoValues = new JoinderChargeInfoValue[chargeIds.length];

            for (int i = 0; i < chargeIds.length; i++) {
                ch = XhbChargeBeanHelper2.findByPrimaryKey(chargeIds[i]);
                // get case number case type and charge no for each joinder charge
                joinderChargeInfoValues[i] = getJoinderChargeInfo(ch);

                if (!chargeSet && ch.getCaseId().equals(caseBean.getCaseId())) {
                    // only ever need one charge within a joinder
                    // indictment, so just pick the first one
                    chargeBean = ch;
                    LOG.debug("Found charge with matching case id. " + "Charge id is " + chargeBean.getChargeId());
                    chargeSet = true;
                }
            }

            // populate a ChargeValue object for this Charge
            ChargeValue chargeValue = populateChargeValue(chargeBean);

            chargeValue.setJoinderChargeInfoValues(joinderChargeInfoValues);

            // Get the collection of offence value object for this charge ID
            Iterator offences = chargeBean.getXhbOffences().iterator();
            Vector<OffenceValue> offenceValues = new Vector<OffenceValue>();
            // create a collection of OffenceValue objects
            while (offences.hasNext()) {
                XhbOffence offenceBean = (XhbOffence) offences.next();
                // if this offence has been logically deleted ignore and
                // continue to the next charge
                if ((!returnObsCharges) && offenceBean.getObsInd() != null && offenceBean.getObsInd().equalsIgnoreCase(ObsInd)) {
                    continue;
                }

                // populate an OffenceValue object for this offence, includes
                // DefendantValue(s) and DefendantOnOffenceBasicValue(s)
                JoinderOffenceValue jOffenceValue = populateJoinderOffenceValue(offenceBean, chargeValue
                            .getChargeID(), chargeIds, joinderId);
                offenceValues.add(jOffenceValue);
            }

            // set the charge value object with this collection of offence
            // value objects
            chargeValue.setOffenceValues(offenceValues);
            // add this charge value object to the Collection of charge
            // value object
            joinderIndictments.add(chargeValue);
        }

        LOG.debug(methodName + "Exited OK");
        return joinderIndictments;
    }

    private JoinderOffenceValue populateJoinderOffenceValue(XhbOffence offenceBean, Integer chargeID, Integer[] chargeIds,
            Integer joinderId) throws ChargeControllerException {
        String methodName = "populateJoinderOffenceValue - ";
        if (LOG.isDebugEnabled())
            LOG.debug(methodName + "called with : offence Id =  " + offenceBean.getOffenceId());

        // populate OffenceValue
        OffenceValue offenceValue = getOffenceValue(offenceBean, caseBasicValue.getCourtId(), chargeID);

        // create joinder offence value based on offenceValue
        JoinderOffenceValue joValue = new JoinderOffenceValue(offenceValue);

        // initialise orginalCaseOffences
        Vector<Integer[]> originalCaseOffences = new Vector<Integer[]>();

        Vector<DefendantValue> defendantValues = new Vector<DefendantValue>(); // The defendant vos for this
        // offence
        Vector<Integer> defendantIDs = new Vector<Integer>(); // The defendant IDs for this
        // offence
        // The DefendantOnOffenceBasicValues for this offence keyed on
        // defendantId
        HashMap<Integer, DefendantOnOffenceComplexValue> defendantOnOffences = 
            new HashMap<Integer, DefendantOnOffenceComplexValue>();

        for (int i = 0; i < chargeIds.length; i++) {
            // get charge entity
            XhbCharge charge = XhbChargeBeanHelper2.findByPrimaryKey(chargeIds[i]);

            // get caseId for charge
            Integer caseId = charge.getCaseId();

            // get offences
            Iterator offences = charge.getXhbOffences().iterator();
            LOG.debug("looping charges, iteration " + i + " chargeId = " + chargeIds[i]);
            while (offences.hasNext()) {
                XhbOffence offence = (XhbOffence) offences.next();
                // find matching offence
                LOG.debug("looping offences, offenceId = " + offence.getOffenceId()
                            + " offence.getCrestOffenceSeqNo = " + offence.getCrestOffenceSeqNo());
                if (offenceValue.getCrestOffenceSeqNo() != null
                        && offenceValue.getCrestOffenceSeqNo().equals(offence.getCrestOffenceSeqNo())) {
                    // set orginalCaseOffences
                    originalCaseOffences.add(new Integer[] { caseId, offence.getOffenceId() });

                    // get DefendantOnOffences - for a joinder, offences
                    // will not always
                    // have defendants on them
                    Collection dofCol = offence.getXhbDefendantOnOffences();
                    if (dofCol.size() > 0) {
                        Iterator dofs = dofCol.iterator();

                        while (dofs.hasNext()) {

                            XhbDefendantOnOffence dof = (XhbDefendantOnOffence) dofs.next();

                            LOG.debug("looping defendantOnOffences , DefendantOnOffenceId = "
                                        + dof.getDefendantOnOffenceId() + "crn = " + dof.getCrnId());
                            XhbDefendantOnCase doc = dof.getXhbDefendantOnCase();
                            LOG.debug("doc.defendantId = " + doc.getDefendantId());

                            // does this defendant have an alias
                            DefendantValue defendantValue = jIHelper.getDefendantAlias(doc, joinderId);

                            LOG.debug("defendantValue.defendantId = " + defendantValue.getDefendantID());
                            // this will never be the case, returns the doc
                            // def if no
                            // alias found
                            // if (defendant==null)
                            // {
                            // if no alias then use normal defendant
                            // defendant = doc.getDefendant();
                            // }

                            // create the DefendantValue via the
                            // DefendantController
                            // DefendantValue defendantValue =
                            // populateDefendantValue(defendant.getDefendantId(),
                            // caseId);
                            // create the DefendantOnOffenceComplexValue
                            // using the maintainer
                            DefendantOnOffenceComplexValue defOnOffenceComplexValue = getDefendantOnOffenceComplexValue(dof);

                            populateOriginalResultAndDisposalDetail(dof, defOnOffenceComplexValue);
                                defendantValues.add(defendantValue);
                                defendantOnOffences.put(defendantValue.getDefendantID(), defOnOffenceComplexValue);
                                defendantIDs.add(defendantValue.getDefendantID());
                        }
                    }
                }
            }

            // set remaining joinderOffence attributes

            joValue.setDefendantIDs(defendantIDs);
            joValue.setDefendantValues(defendantValues);
            joValue.setDefOnOffenceBasicValues(defendantOnOffences);
            joValue.setOriginalCaseOffences(originalCaseOffences);
        }

        LOG.debug(methodName + "Exited OK");
        return joValue;
    }

    private JoinderChargeInfoValue getJoinderChargeInfo(XhbCharge charge) {
        String methodName = "JoinderChargeInfoValue - ";
        LOG.debug(methodName + "called ");

        XhbCase caze = charge.getXhbCase();
        LOG.debug(methodName + "Exited OK");

        return new JoinderChargeInfoValue(caze.getCaseType(), caze.getCaseNumber(), charge.getCrestChargeSeqNo(),
                charge.getChargeId());
    }

    public DefendantOnOffenceComplexValue getDefendantOnOffenceComplexValue(XhbDefendantOnOffence dof) {
        DefendantOnOffenceComplexValue value = new DefendantOnOffenceComplexValue(dof.getDefendantOnOffenceId(),
                dof.getVersion());
        dofHelper.setDefendantOnOffenceBasicValue(value, dof);
        return value;
    }
}
