package uk.gov.courtservice.xhibit.business.services.charge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder.XhbJoinder;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder.XhbJoinderBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder_charge.XhbJoinderCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder_charge.XhbJoinderChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder_defendant_on_case.XhbJoinderDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder_defendant_on_case.XhbJoinderDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder_xml.XhbJoinderXml;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder_xml.XhbJoinderXmlBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;

/**
 * <p>
 * Title: DeleteJoinderIndictmentHelper
 * </p>
 * <p>
 * Description: This helper class is used to assist the deletion of
 * offences/charges on joinder indictments.
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
 * @version $Id: DeleteJoinderIndictmentHelper.java,v 1.9 2005/02/11 16:21:49
 *          sz0t7n Exp $
 * @history 2003-04-23: MH: BUG-FIX 347: Nullpointer exception when trying to
 *          find the joinder when there isn't any. Also the OffenceMaintainer
 *          was not initialised.
 */

public class DeleteJoinderIndictmentHelper {

    private JoinderIndictmentHelper jHelper = new JoinderIndictmentHelper();

    private static Logger log = CSServices.getLogger(DeleteJoinderIndictmentHelper.class);

    // error keys
    private static final String NULL_POINTER = "joinder.offenceIdJoinderIdIsNull ";

    public DeleteJoinderIndictmentHelper() {
    }

    /**
     * This method is used to delete offences on joinder indictments. It will
     * delete all offences accross joined charges that match the input offences
     * crestSequencenumber.
     *
     * @param offenceValue
     */
    public void deleteOffences(OffenceValue offenceValue) {

    }

    /**
     * Checks if the charge in the DelChargeValue is a joinder. If so creates a
     * DelChargeValue to delete for each charge in the joinder. If not just
     * returns the original DelChargeValue.
     *
     * @param delChargeVal
     *            The original value
     * @return An array of values including any for joinder charges
     */
    public DelChargeValue[] deleteCheckJoinderCharge(DelChargeValue delChargeVal) {
        DelChargeValue[] delChargeValues;

        // check if this is a joinder indictment
        HashMap joinderCharges = jHelper.getJoinderCharges(delChargeVal.getChargeID());
        if (joinderCharges != null) {
            // The HashMap contains one key, the joinder id, and therefore
            // one set
            // of chargeIds, these are the joinder chargeIds
            Integer[] chargeIds = (Integer[]) joinderCharges.values().iterator().next();
            int numChargeIds = chargeIds.length;

            delChargeValues = new DelChargeValue[numChargeIds];

            XhbCharge charge;

            for (int i = 0; i < numChargeIds; i++) {
                DelChargeValue newDelChargeVal = new DelChargeValue();
                charge = XhbChargeBeanHelper2.findByPrimaryKey(chargeIds[i]);

                newDelChargeVal.setCaseID(charge.getCaseId());
                newDelChargeVal.setChargeID(charge.getChargeId());
                newDelChargeVal.setCourtID(charge.getXhbCase().getCourtId());
                newDelChargeVal.setCrestChargeID(charge.getCrestChargeId());
                newDelChargeVal.setCrestChargeSeqNo(charge.getCrestChargeSeqNo());
                // set the last 3 attributes from the provided value as these
                // are
                // user input
                newDelChargeVal.setDeleteResults(delChargeVal.isDeleteResults());
                newDelChargeVal.setInCourt(delChargeVal.isInCourt());
                newDelChargeVal.setCourtLogDate(delChargeVal.getCourtLogDate());
                delChargeValues[i] = newDelChargeVal;
            }
        } else {
            delChargeValues = new DelChargeValue[] { delChargeVal };
        }

        return delChargeValues;
    }

    /**
     * This method returns a coolection of offences on joinder indictments. It
     * will delete all offences accross joined charges that match the input
     * offences crestSequencenumber.
     *
     * @param offenceValue
     */
    public Collection<Integer> getJoinedOffenceIds(DelOffenceValue delOffenceValue) throws ChargeControllerException {
        Collection<Integer> retValue = new ArrayList<Integer>();

        String methodName = "getJoinedOffenceIds(" + delOffenceValue + ") - ";
        log.debug("*** " + methodName + " entered ***");

        // get the offence Id out of the object
        Integer offenceId = delOffenceValue.getOffenceID();
        checkValueIsNull(offenceId, "offence Id returned was NULL");
        log.debug("*** Using Offence Id = " + offenceId + " ***");

        Integer joinderId = JoinderIndictmentHelper.getJoinderIdForOffence(offenceId);

        // This may nt be a joined offence so wil not have a corresponding
        // joinder offence id

        // locate all offences that are joined to this one
        // if joinder id is null, do not proceed, just return the original
        // offence id
        if (joinderId != null) {
            // get the offence value from the delOffenceValue as we need to 
            // know the crestOffenceSeqNo
            log.debug("Will try to find the offence with id : " + offenceId);
            XhbOffence tmpOffence = XhbOffenceBeanHelper2.findByPrimaryKey(offenceId);
            log.debug("Found the offence with id : " + tmpOffence.getOffenceId());

            Integer crestOffenceSeqNum = tmpOffence.getCrestOffenceSeqNo();
            if (crestOffenceSeqNum != null) {

	            Collection joinedOffences = XhbOffenceBeanHelper2.findByJoinderIdCrestSeqNo(joinderId, crestOffenceSeqNum);
	            log.debug("*** Returned from finding joined offences. Collection size returned is "
	                        + joinedOffences.size() + " ***");

	            Iterator jOIter = joinedOffences.iterator();
	            while (jOIter.hasNext()) {
	                XhbOffence offence = (XhbOffence) jOIter.next();
	                log.debug("*** Current offence value = " + offence + " ***");
	                // loop and add all joined offences in to the collection
	                retValue.add(offence.getOffenceId());
	            }
            } else {
            	retValue.add(offenceId);
            }
        } else {
            retValue.add(offenceId);
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
     * removes the joinder, joinderCharge and joinderDefOnCase records for this
     * joinder
     *
     * @param joinderId
     *            The joinderId
     */
    public void removeJoinderRecord(Integer joinderId) {
        try {
            // remove joinderDefOnCase(s)
            Collection joinderDocs = XhbJoinderDefendantOnCaseBeanHelper2.findByJoinderId(joinderId);
            if (joinderDocs.size() > 0) {
                Iterator it = joinderDocs.iterator();
                while (it.hasNext()) {
                    XhbJoinderDefendantOnCase joinderDoc = (XhbJoinderDefendantOnCase) it.next();
                    joinderDoc.remove();
                }
            }

            // remove joinderCharge(s)
            Collection joinderCharges = XhbJoinderChargeBeanHelper2.findByJoinderId(joinderId);
            if (joinderCharges.size() > 0) {
                Iterator it = joinderCharges.iterator();
                while (it.hasNext()) {
                    XhbJoinderCharge joinderCharge = (XhbJoinderCharge) it.next();
                    joinderCharge.remove();
                }
            }

            // remove joinderXml(s)
            Iterator iter = XhbJoinderXmlBeanHelper2.findByJoinderId(joinderId).iterator();
            while (iter.hasNext()) {
                ((XhbJoinderXml) iter.next()).remove();
            }

            // remove joinder
            XhbJoinder joinder = XhbJoinderBeanHelper2.findByPrimaryKey(joinderId);
            joinder.remove();
        } catch (RemoveException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new EJBException(e);
        }
    }
}