package uk.gov.courtservice.xhibit.business.services.charge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPlea;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.services.results.verifyresults.DefendantOnOffenceVerifyResultsValue;

/**
 * Helper class containing methods used for validating results for charge and
 * offences.
 *
 * @author tz0d5m
 */
public class ChargeHelper {
    private static final Logger log = CSServices.getLogger(ChargeHelper.class);

    /**
     * Gets the breach plea (breach admitted) for the given defendant on charge
     * id.
     *
     * @param defendantChargeId
     *            the id of the defendant on charge.
     * @return the breach plea of Y, N or null.
     */
    public static String getBreachPlea(Integer defendantChargeId) {
        Collection c = XhbPleaBeanHelper2.findMandatoryByDefendantChargeId(defendantChargeId);
        if (c.isEmpty())
            return null;

        final XhbPlea plea = (XhbPlea) c.iterator().next();

        if ((plea != null) && (plea.getDefendantChargeId() != null) && (plea.getBreachAdmitted() != null)) {
            return plea.getBreachAdmitted();
        }

        return null;
    }

    /**
     * This method will check if results exist for this charge.
     *
     * @param chargeId
     *            Integer
     * @throws A
     *             <code>ResultsFoundException</code> if any results were
     *             found.
     */
    public static void checkResultsForCharge(Integer chargeId) throws ResultsFoundException {
        log.debug("checkResultsForCharge() - called :: chargeId: " + chargeId);
        Collection col = getVerifyResultsByChargeId(chargeId);
        checkResultsForDefOnOffence(col);
    }

    /**
     * This method will check if results exist for this offence.
     *
     * @param offenceId
     *            Integer
     * @throws A
     *             <code>ResultsFoundException</code> if any results were
     *             found.
     */
    public static void checkResultsForOffence(Integer offenceId) throws ResultsFoundException {
        log.debug("checkResultsForOffence() - called :: offenceId: " + offenceId);
        Collection col = getVerifyResultsByOffenceId(offenceId);
        checkResultsForDefOnOffence(col);
    }

    /**
     * Method to determine if there are any pleas for the defendant on offence
     * whos id is passed in.
     *
     * @param defendantOnOffenceId
     * @return <i>true</i> if ther are pleas, <i>false</i> otherwise.
     */
    private static boolean hasPleas(Integer defendantOnOffenceId) {
        return !(XhbPleaBeanHelper2.findByDefendantOnOffenceId(defendantOnOffenceId).isEmpty());
    }

    /**
     * Method to determine if there are any breach pleas for the defendant on
     * charge whos id is passed in.
     *
     * @param defendantOnChargeId
     * @return <i>true</i> if ther are breach pleas, <i>false</i> otherwise.
     */
    private static boolean hasBreachPleas(Integer defendantOnChargeId) {
        return !(XhbPleaBeanHelper2.findMandatoryByDefendantChargeId(defendantOnChargeId).isEmpty());
        // return (getPleaByDefendantChargeId(defendantOnChargeId) != null);
    }

    /**
     * Method to determine if there are any verdicts for the defendant on
     * offence whos id is passed in.
     *
     * @param defendantOnOffenceId
     * @return <i>true</i> if ther are verdicts, <i>false</i> otherwise.
     */
    private static boolean hasVerdicts(Integer defendantOnOffenceId) {
        return !(XhbVerdictBeanHelper2.findByDefendantOnOffenceId(defendantOnOffenceId).isEmpty());
    }

    /**
     * Method to determine if there are any disposals for the defendant on
     * offence whos id is passed in.
     *
     * @param defendantOnOffenceId
     * @return <i>true</i> if ther are disposals, <i>false</i> otherwise.
     */
    private static boolean hasDisposals(Integer defendantOnOffenceId) {
        return !(XhbDisposal2BeanHelper2.findRSByDefendantOnOffenceId(defendantOnOffenceId).isEmpty());
    }

    /**
     * Get Results for a charge
     *
     * @param chargeId
     *            Charge Id
     * @return A <code>Collection</code> of
     *         <code>DefendantOnOffenceVerifyResultsValue</code> value
     *         objects.
     */
    private static Collection getVerifyResultsByChargeId(Integer chargeId) {
        log.debug("getResultsByChargeId() - called :: chargeId: " + chargeId);
        final ArrayList values = new ArrayList();

        final XhbCharge charge = XhbChargeBeanHelper2.findByPrimaryKey(chargeId);

        // Get all offences and iterate over...
        final Iterator it = charge.getXhbOffences().iterator();

        while (it.hasNext()) {
            XhbOffence offence = (XhbOffence) it.next();
            values.addAll(getVerifyResultsByOffenceId(offence.getOffenceId()));
        }

        return values;
    }

    /**
     * Get Results for an offence
     *
     * @param offenceId
     *            Offence Id
     * @return A <code>Collection</code> of
     *         <code>DefendantOnOffenceVerifyResultsValue</code> value
     *         objects.
     */
    private static Collection getVerifyResultsByOffenceId(Integer offenceId) {
        log.debug("getVerifyResultsByOffenceId() called :: offenceId: " + offenceId);

        try {
            // Get all the DefOnOffence for this offence Id
            Collection coll = XhbDefendantOnOffenceBeanHelper2.findByOffenceId(offenceId);
            return createResultsByDefOnOffenceCollection(coll);
        } catch (XhbDefendantOnOffenceBeanNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, ChargeHelper.class, e.toString());
        }

        return new ArrayList();
    }

    /**
     * Searches through <code>Collection</code> to see if there are any
     * results.
     *
     * @param doovrvs
     *            A <code>Collection</code> of
     *            <code>DefendantOnOffenceVerifyResultsValue</code> value
     *            objects.
     * @throws A
     *             <code>ResultsFoundException</code> if any results were
     *             found.
     */
    private static void checkResultsForDefOnOffence(Collection doovrvs) throws ResultsFoundException {
        final Iterator it = doovrvs.iterator();

        while (it.hasNext()) {
            DefendantOnOffenceVerifyResultsValue doovrv = (DefendantOnOffenceVerifyResultsValue) it.next();

            if (doovrv.hasResults()) {
                throw new ResultsFoundException();
            }
        }
    }

    /**
     * Create a <code>Collection</code> containing all of the
     * <code>DefendantOnOffenceVerifyResultsValue</code> for all of the
     * defendants on offence contained in the passed in <code>Collection</code>.
     *
     * @param defendantsOnOffence
     *            A <code>Collection</code> of the defendants on offence that
     *            we need to acquire the
     *            <code>DefendantOnOffenceVerifyResultsValue</code>s for.
     * @return A <code>Collection</code> of
     *         <code>DefendantOnOffenceVerifyResultsValue</code> value
     *         objects.
     */
    private static Collection createResultsByDefOnOffenceCollection(Collection defendantsOnOffence) {
        ArrayList arrayList = new ArrayList();
        Iterator it = defendantsOnOffence.iterator();

        while (it.hasNext()) {
            XhbDefendantOnOffence doo = (XhbDefendantOnOffence) it.next();
            arrayList.add(createResultsByDefOnOffence(doo));
        }

        return arrayList;
    }

    /**
     * Acquire the <code>DefendantOnOffenceVerifyResultsValue</code> value
     * object for the defendant on offence whose primary key is passed in.
     *
     * @param id
     *            The primary key for the defendant on offence.
     * @return A <code>DefendantOnOffenceVerifyResultsValue</code> value
     *         object
     */
    private static DefendantOnOffenceVerifyResultsValue createResultsByDefOnOffence(XhbDefendantOnOffence doo) {
        // Get defOnCase, offence and charge
        XhbDefendantOnCase doc = doo.getXhbDefendantOnCase();
        XhbCharge charge = doo.getXhbOffence().getXhbCharge();

        // If the charge is a Breach the Plea will be recorded against the
        // defendantOnCharge instead of the defendantOnOffence
        boolean verdictsExist = hasVerdicts(doo.getDefendantOnOffenceId());
        boolean disposalsExist = hasDisposals(doo.getDefendantOnOffenceId());
        boolean pleasExist = false;

        if (ChargeTypes.isBreachChargeType(charge.getChargeType())) {
            Iterator it = charge.getXhbDefendantCharges().iterator();
            while (it.hasNext()) {
                XhbDefendantCharge defOnCharge = (XhbDefendantCharge) it.next();
                XhbDefendantOnCase doc1 = defOnCharge.getXhbDefendantOnCase();

                if (doc1.getDefendantOnCaseId().equals(doc.getDefendantOnCaseId())) {
                    pleasExist = hasBreachPleas(defOnCharge.getDefendantChargeId());
                    break;
                }
            }
        } else {
            pleasExist = hasPleas(doo.getDefendantOnOffenceId());
        }

        final DefendantOnOffenceVerifyResultsValue doovrv = new DefendantOnOffenceVerifyResultsValue();

        // Populate the VO...
        doovrv.setPleas(pleasExist);
        doovrv.setVerdicts(verdictsExist);
        doovrv.setDisposals(disposalsExist);

        return doovrv;
    }
}
