package uk.gov.courtservice.xhibit.business.services.charge;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.refcourt.RefCourt;
import uk.gov.courtservice.xhibit.business.entities.refcourt.RefCourtHome;
import uk.gov.courtservice.xhibit.business.entities.refsystemcode.RefSystemCode;
import uk.gov.courtservice.xhibit.business.entities.refsystemcode.RefSystemCodeHome;
import uk.gov.courtservice.xhibit.business.entities.xhb_breach.XhbBreach;
import uk.gov.courtservice.xhibit.business.entities.xhb_breach.XhbBreachBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantCharge;
import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;

/**
 * <p>
 * Title: Helper class for Breaches
 * </p>
 * <p>
 * Description: Has methods to retrieve/populate a BreachValue.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */

public class BreachHelper {

    private static Logger log = CSServices.getLogger(BreachHelper.class);

    public BreachHelper() {
    }

    /**
     * Gets the BreachValue for the given breach id.
     * 
     * @param breachID
     *            the id of the breach
     * @return the BreachValue
     * @throws ChargeControllerException
     */
    public BreachValue getBreachValue(Integer breachID) throws ChargeControllerException {
        String methodName = "getBreachValue(breachID = " + breachID + ") - ";
        log.debug(methodName + " : entered");

        // Find the Breach Entity
        XhbBreach breach = XhbBreachBeanHelper2.findByPrimaryKey(breachID);

        XhbCharge charge = breach.getXhbCharge();

        // Find the caseID of this Breach
        Integer caseID = charge.getCaseId();

        // Find the RefSystemCode to retrieve the hoCode and hoDescription
        Integer refSystemCodeID = charge.getRefSystemCodeId();

        //Integer defendantOnChargeId = getDefOnChargeId(charge.getChargeId());
        Integer defendantOnChargeId = getDefOnChargeId(charge);

        BreachValue breachValue = getBreachValue(breach, caseID, refSystemCodeID, defendantOnChargeId);

        log.debug(methodName + " : exited");
        return breachValue;
    }

    /**
     * Gets the BreachValue for the given parameters.
     * 
     * @param breach
     * @param caseID
     *            the id of the case.
     * @param refSystemCodeID
     *            the id of the reference system code.
     * @param defendantOnChargeId
     *            the id of the defendant on charge.
     * @return the BreachValue
     * @throws ChargeControllerException
     */
    public BreachValue getBreachValue(XhbBreach breach, Integer caseID, Integer refSystemCodeID,
            Integer defendantOnChargeId) throws ChargeControllerException {
        String methodName = "getBreachValue(caseId = " + caseID + ") - ";
        log.debug(methodName + " : entered");

        try {
            // Convert Timestamps to Calendar
            Calendar origSentenceDate = null;
            Date origSentTS = breach.getOriginalSentenceDate();
            if (origSentTS != null) {
                origSentenceDate = Calendar.getInstance();
                origSentenceDate.setTime(origSentTS);
            }

            Calendar datePut = null;
            Date datePutTS = breach.getDatePut();
            if (datePutTS != null) {
                datePut = Calendar.getInstance();
                datePut.setTime(datePutTS);
            }

            RefSystemCode refSystemCode = (RefSystemCode) CSServices.getEJBServices().findLocalEntityByPrimaryKey(
                    RefSystemCodeHome.class, refSystemCodeID);

            RefCourt refCourt = null;
            if(breach.getRefCourtId() != null){
                // Find the original court to retrieve the original court name
               refCourt =  (RefCourt) CSServices.getEJBServices().findLocalEntityByPrimaryKey(RefCourtHome.class,
                       breach.getRefCourtId()); 
            }

            String refCourtFullName = refCourt == null? "" : refCourt.getCourtFullName();
            String refCourtShortName = refCourt == null? "" : refCourt.getCourtShortName();
            
            // String plea = getBreachPlea(defendantOnChargeId);
            String plea = ChargeHelper.getBreachPlea(defendantOnChargeId);

            BreachValue breachValue = new BreachValue(breach.getBreachId(), caseID, breach.getOriginalSentence(),
                    origSentenceDate, breach.getOriginalCourtType(), breach.getRefCourtId(), refCourtFullName
                    , datePut, breach.getBreachType(), breach.getBringBack(), refSystemCode.getCode(), 
                    refSystemCode.getDecode(), false, plea, breach.getChargeId(),
                    refSystemCodeID);

            breachValue.setOriginalCourtShortName(refCourtShortName);
            breachValue.setUpdateCount(breach.getVersion().intValue());

            log.debug(methodName + " : exited");
            return breachValue;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, GetChargesHelper.class);
            // this is an unexpected exception as both th e refSystemCodeID
            // and
            // refCourtID were retrieved from the database and the entities
            // should
            // exist.
            throw new CSUnrecoverableException("Exception finding an entity by a "
                    + "key which was retrieved from the db.", e);
        }
    }

    /**
     * Get the defendant on charge id for the given charge id.
     * 
     * @param charge
     *            the charge.
     * @return the defendant on charge id
     */
    private Integer getDefOnChargeId(XhbCharge charge) {
        String methodName = "getDefOnChargeId(" + charge.getChargeId() + ") - ";
        log.debug(methodName + " : entered");

        Integer defOnChargeId = null;

        // find the defendant on charge entity
        XhbDefendantCharge defOnCharge = null;
        //Collection defendantsOnCharge = XhbDefendantChargeBeanHelper2.findByChargeId(chargeId);
        Collection defendantsOnCharge = charge.getXhbDefendantCharges();

        Iterator i = defendantsOnCharge.iterator();
        if (i.hasNext()) {
            // Should only be one
            defOnCharge = (XhbDefendantCharge) i.next();
            defOnChargeId = defOnCharge.getDefendantChargeId();
        }

        log.debug(methodName + " : exited returning a defendant on charge id of: " + defOnChargeId);
        return defOnChargeId;
    }
}