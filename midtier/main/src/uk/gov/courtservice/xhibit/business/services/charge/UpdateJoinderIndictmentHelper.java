package uk.gov.courtservice.xhibit.business.services.charge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.services.charge.JoinderOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;

/**
 * <p>
 * Title: UpdateJoinderIndictmentHelper
 * </p>
 * <p>
 * Description: This helper class is used to update offences for joinder
 * indictments
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author Ian Hannaford
 * @author Sarah Tong
 * @version 1.0
 */
public class UpdateJoinderIndictmentHelper {

    private static Logger log = CSServices.getLogger(UpdateJoinderIndictmentHelper.class);

    // error keys
    private static final String DEFENDANT_NOT_ON_CASE = "charge.defendantnotoncase";

    private static final String NULL_POINTER = "joinder.offenceIdJoinderIdIsNull ";

    public UpdateJoinderIndictmentHelper() {
        // default constructor.
    }

    /**
     * This method is used to updates offences on joinder indictments. It will
     * update all offences accross joined charges that match the input offences
     * crestSequencenumber.
     *
     * @param offenceValue
     */
    public Collection<Integer> getJoinedOffenceIds(OffenceValue offenceValue) throws ChargeControllerException {
        Collection<Integer> retValue = new ArrayList<Integer>();

        String methodName = "getJoinedOffenceIds(" + offenceValue + ") - ";
        log.debug("*** " + methodName + " entered ***");

        // Check whether the value past in is an instance of JoinderOffenceValue
        if ((offenceValue instanceof JoinderOffenceValue)) {
            // get the offence Id out of the object
            Integer offenceId = offenceValue.getOffenceID();
            checkValueIsNull(offenceId, "offence Id returned was NULL");
            log.debug("*** Using Offence Id = " + offenceId + " ***");

            // find the joinderId for this offence
            //Integer joinderId = joinderChargeMaintainer.getJoinderIdForOffence(offenceId);
            Integer joinderId = JoinderIndictmentHelper.getJoinderIdForOffence(offenceId);
            checkValueIsNull(joinderId, "Joinder Id returned was NULL");
            log.debug("*** Joinder Id for this offence is = " + joinderId);

            // locate all offences that are joined to this one
            Integer crestOffenceSeqNum = offenceValue.getCrestOffenceSeqNo();
            checkValueIsNull(crestOffenceSeqNum, "crest offence sequence number is NULL");

            //Collection joinedOffences = offenceMaintainer.findByJoinderIdAndCrestOffenceSequenceNumber(joinderId,
            //        crestOffenceSeqNum);
            Collection joinedOffences = XhbOffenceBeanHelper2.findByJoinderIdCrestSeqNo(joinderId, crestOffenceSeqNum);
            log.debug("*** Returned from finding joined offences. Collection size returned is " + joinedOffences.size()
                    + " ***");

            Iterator jOIter = joinedOffences.iterator();
            while (jOIter.hasNext()) {
                XhbOffence offence = (XhbOffence) jOIter.next();
                log.debug("*** Current offence value = " + offence + " ***");
                // loop and add all joined offences in to the collection
                retValue.add(offence.getOffenceId());
            }
        } else {
            // just return the original input
            retValue.add(offenceValue.getOffenceID());
        }

        return retValue;
    }

    private void checkValueIsNull(Object value, String errorMessage) throws ChargeControllerException {
        log.debug("*** Checking for null value ***");
        if (value == null) {
            throw new ChargeControllerException(NULL_POINTER, errorMessage);
        }
    }

    /**
     * Offence ids in a JoinderCharge come from the first charge found in the
     * joinder group. The defendant we want to add to the offence will not
     * necessarily be on the case which holds this charge. If the defendant is
     * not on the case we need to find the charge in the joinder group which is
     * on a case which this defendant is on. The defendant can then be added to
     * the offence on this charge.
     *
     * This method first checks if the offence is part of a joinder and if so
     * checks the above rule and replaces the offence id with a valid offence id
     * where necessary.
     *
     * @param countDef
     *            An Integer[] with 2 elements, offenceId and defendantId
     * @return
     */
    public Integer[] checkJoinderOffence(Integer[] countDef) throws ChargeControllerException {
        log.debug("checkJoinderOffence(Integer[] countDef) start with countDef = " + countDef);

        JoinderIndictmentHelper jHelper = new JoinderIndictmentHelper();
        HashMap joinderCharges;
        XhbOffence offence;

        // find the joinder charges for this offence
        offence = XhbOffenceBeanHelper2.findByPrimaryKey(countDef[0]);
        joinderCharges = jHelper.getJoinderCharges(offence.getChargeId());

        if (joinderCharges != null) {
            // we do have a Joinder Offence, need to check we're adding the
            // defendant to the offence on the correct case and charge
            Integer caseId = offence.getXhbCharge().getCaseId();

            if (!checkDefendantOnCase(countDef[1], caseId)) {
                // the defendant is not on the case with the specified offence,
                // need
                // to find another offence in joinderCharge group where the
                // defendant
                // is on the case

                // The joinderCharges HashMap contains one key, the joinder id,
                // and
                // therefore one set of chargeIds, these are the joinder
                // chargeIds
                Integer[] chargeIds = (Integer[]) (joinderCharges.values().iterator().next());
                int numChargeIds = chargeIds.length;
                boolean foundOffence = false;

                // for each charge id check if the defendant is on this case
                for (int i = 0; i < numChargeIds; i++) {
                    Integer chargeId = chargeIds[i];
                    XhbCharge charge = XhbChargeBeanHelper2.findByPrimaryKey(chargeId);
                    if (checkDefendantOnCase(countDef[1], charge.getCaseId())) {
                        // we've found a case with this defendant on so
                        // set the offence
                        // id from the charge on this case
                        Iterator it = charge.getXhbOffences().iterator();

                        // find the offence on this charge which matched
                        // the original
                        // offence, i.e. the one with the same sequence
                        // number
                        while (it.hasNext()) {
                            XhbOffence newOffence = (XhbOffence) it.next();
                            if (newOffence.getCrestOffenceSeqNo().intValue() == offence.getCrestOffenceSeqNo().intValue()) {
                                // this is the offence we want
                                countDef[0] = newOffence.getOffenceId();
                                // stop looking
                                foundOffence = true;
                                break;
                            }
                        } // end offence loop
                    } // end if defendant on case

                    if (foundOffence) {
                        // stop looking
                        break;
                    }

                } // end charge loop

                if (!foundOffence) {
                    // the defendant is not on the case of original offence
                    // OR any of the joinder cases
                    throw new ChargeControllerException(DEFENDANT_NOT_ON_CASE, "Defendant Id " + countDef[1]
                            + " is not on case Id " + caseId + " cannot add.");
                }
            }

        }

        log.debug("checkJoinderOffence(Integer[] countDef) returning countDef = " + countDef);
        return countDef;
    }

    // ----------------------- Private Methods
    // --------------------------------- //

    /**
     * Checks if the defendant exists on the case specified.
     *
     * @param defendantId
     *            The defendant id
     * @param caseId
     *            The case Id
     * @return true if the defendant exists on the case, false otherwise
     */
    private boolean checkDefendantOnCase(Integer defendantId, Integer caseId) {
        log.debug("checkDefendantOnCase(Integer defendantId, Integer caseId) - start");
        try {
            XhbDefendantOnCase doc = XhbDefendantOnCaseBeanHelper2.findByDefendantAndCase(defendantId, caseId);
        } catch (XhbDefendantOnCaseBeanNotFoundException e) {
            // the defendant is not on the case
            log.debug("checkDefendantOnCase(Integer defendantId, Integer caseId) - returning false");
            return false;
        }
        log.debug("checkDefendantOnCase(Integer defendantId, Integer caseId) - returning true");
        return true;
    }
}