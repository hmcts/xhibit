package uk.gov.courtservice.xhibit.business.services.results.populater;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_breach.XhbBreach;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPlea;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_system_code.XhbRefSystemCode;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.services.charge.UncodedOffenceInterface;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.JoinderChargeInfoValue;
import uk.gov.courtservice.xhibit.common.results.vos.MoveResultsValue;
import uk.gov.courtservice.xhibit.common.results.vos.PleaSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.PleaValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;

/**
 * <p>
 * Title: Populates ResultsCompositeValue with Pleas
 * </p>
 * <p>
 * Description: Ensures Pleas for Defendant on Charge and Defendant on Offence
 * are set
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author Bal Bhamra
 * @version $Revision: 1.27 $
 */
public class PleasPopulater extends AbstractResultsPopulater implements UncodedOffenceInterface {
    private static final Logger log = CSServices.getLogger(PleasPopulater.class);

    public void populate(ResultsCompositeValue rcv) throws ResultsControllerException {
        log.debug("populate() - START");
        populatePleas(rcv);
        log.debug("populate() - END");
    }

    public void populateSaveValues(ResultsSaveValue results, MoveResultsValue oldMoveValue,
            MoveResultsValue newMoveValue) throws ResultsControllerException {
        log.debug("populateSaveValues -  START");
        final Collection pleas = XhbPleaBeanHelper2.findByDefendantOnOffenceId(oldMoveValue.getDefendantOnOffenceId());

        // if empty do nothing
        if (pleas.size() == 0) {
            return;
        }

        // shouldn't be more than 1 active plea
        if (pleas.size() > 1) {
            throw new ResultsControllerException(null, "Invalid number of Pleas:" + pleas.size());
        }

        PleaValue oldPleaValue = createPlea((XhbPlea) pleas.iterator().next());

        PleaSaveValue oldPlea = new PleaSaveValue(oldPleaValue, ResultSaveValue.DELETE, oldMoveValue.getCaseId(),
                oldMoveValue.getCaseNumber(), oldMoveValue.getCaseType(), oldMoveValue.getCrestOffenceId(),
                oldMoveValue.getCrestDefendantId(), oldMoveValue.getChargeType());

        oldPlea.setCourtLogged(false);
        oldPlea.setVcoDate(oldMoveValue.getVcoDate());
        oldPlea.setVcoFlag(oldMoveValue.getVcoFlag());

        results.addResultSaveValue(oldPlea);
        results.addResultSaveValue(getNewPlea(oldPlea, newMoveValue));
        log.debug("populateSaveValues -  END");
    }

    private void populatePleas(ResultsCompositeValue rcv) {
        log.debug("getPleas() - START");

        Collection charges = rcv.getChargeCompositeValue().getCharges();
        Iterator chargesIterator = charges.iterator();

        while (chargesIterator.hasNext()) {
            ChargeValue chargeValue = (ChargeValue) chargesIterator.next();
            // Uncomment for verbose debug of ChargeValue
            // processCharges(chargeValue);
            JoinderChargeInfoValue[] joinderChargeInfoValue = chargeValue.getJoinderChargeInfoValues();
            if (joinderChargeInfoValue != null)// is this a joinder indictment
            {
                // if so get results for all charges in joinder
                for (int i = 0, len = joinderChargeInfoValue.length; i < len; i++) {
                    populatePleasForOffence(rcv, joinderChargeInfoValue[i].getChargeId());
                }
            } else {
                Integer chargeId = chargeValue.getChargeID();
                populatePleasForOffence(rcv, chargeId);
                populatePleasForCharge(rcv, chargeId);
            }
        }

        log.debug("getPleas() - END");
    }

    private void populatePleasForOffence(ResultsCompositeValue rcv, Integer chargeId) {
        log.debug("populatePleasForOffence() - START");

        // find Pleas and set to ResultsCompositeValue
        final Collection pleas = XhbPleaBeanHelper2.findByChargeIdForOffence(chargeId);
        final Iterator pleasIterator = pleas.iterator();
        log.debug("populatePleasForOffence() - pleas.size(): " + pleas.size());

        while (pleasIterator.hasNext()) {
            PleaValue pleaValue = createPlea((XhbPlea) pleasIterator.next());
            rcv.addPlea(pleaValue.getDefendantOnOffenceId(), pleaValue);
        }

        log.debug("populatePleasForOffence() - END");
    }

    private void populatePleasForCharge(ResultsCompositeValue rcv, Integer chargeId) {
        log.debug("populatePleasForCharge() - START");

        // find Pleas and set to ResultsCompositeValue
        final Collection pleas = XhbPleaBeanHelper2.findByChargeIdForCharge(chargeId);
        final Iterator pleasIterator = pleas.iterator();
        log.debug("populatePleasForCharge() - pleas.size(): " + pleas.size());

        Date datePut = getDatePut(chargeId);
        log.debug("populatePleasForCharge() - Date Put: " + datePut);

        while (pleasIterator.hasNext()) {
            PleaValue pleaValue = createPlea((XhbPlea) pleasIterator.next());
            pleaValue.setDatePut(datePut);
            rcv.addChargePlea(pleaValue.getDefendantChargeId(), pleaValue);
        }

        log.debug("populatePleasForCharge() - END");
    }

    /**
     * Public method to determine/call whether sys or ref code needs to be
     * looked up for for pleas.
     *
     * @param PleaValue
     * @return PleaValue
     */
    private static PleaValue createPlea(XhbPlea plea) {
        final PleaValue pleaValue = new PleaValue(plea.getData());
        pleaValue.setOriginalPleaDate(pleaValue.getPleaDate());

        final XhbRefOffence refOffence = plea.getXhbRefOffence();
        final XhbRefSystemCode refSystemCode = plea.getXhbRefSystemCode();

        if (refOffence != null) {
            pleaValue.setViewAltRefOffenceCode(refOffence.getOffenceCode());
            if (!refOffence.getOffenceCode().equalsIgnoreCase(UNCODED_OFFENCE_REFERENCE_CODE)) {
                pleaValue.setViewAltRefOffenceDesc(refOffence.getOffenceDesc());
            } else {
                pleaValue.setViewAltRefOffenceDesc(plea.getData().getAltUncodedOffenceDesc());
            }
        }

        if (refSystemCode != null) {
            pleaValue.setOriginalRefPleaId(refSystemCode.getRefSystemCodeId());
            pleaValue.setRefPleaDesc(refSystemCode.getDeCode());
            pleaValue.setRefPleaCode(refSystemCode.getCode());
        }

        XhbVerdictBasicValue verdictBasicValue = getVerdictBasicValue(pleaValue);
        if (verdictBasicValue != null) {
            Integer refVerdictId = verdictBasicValue.getRefVerdictId();
            if (refVerdictId != null) {
                pleaValue.setOriginalRefVerdictId(refVerdictId);
            }

            Date originalVerdictDate = verdictBasicValue.getVerdictDate();
            if (originalVerdictDate != null) {
                pleaValue.setOriginalVerdictDate(originalVerdictDate);
            }
        }

        return pleaValue;
    }

    /**
     * Get the refVerdictId for the plea (if it exists)
     */
    private static XhbVerdictBasicValue getVerdictBasicValue(PleaValue pleaValue) {
        XhbVerdictBasicValue[] verdictValues = null;

        if (pleaValue.isOnCharge()) {
            verdictValues = XhbVerdictBeanHelper2.findByDefendantChargeIdValue(pleaValue.getDefendantChargeId());
        } else if (pleaValue.isOnOffence()) {
            verdictValues = XhbVerdictBeanHelper2.findByDefendantOnOffenceIdValue(pleaValue.getDefendantOnOffenceId());
        }

        return ((verdictValues != null) && (verdictValues.length > 0)) ? verdictValues[0] : null;
    }

    private static Date getDatePut(Integer chargeId) {
        log.debug("getDatePut() - chargeId: " + chargeId);

        XhbCharge charge = XhbChargeBeanHelper2.findByPrimaryKey(chargeId);
        log.debug("getDatePut() - charge.getChargeType(): " + charge.getChargeType());
        if (ChargeTypes.isBreachChargeType(charge.getChargeType())) {
            // log.debug("getDatePut() - charge.getXhbBreaches().size():" +
            // charge.getXhbBreaches().size());
            XhbBreach breach = charge.getXhbBreach();
            // log.debug("getDatePut() - breach.getDatePut():" +
            // breach.getDatePut());
            return breach.getDatePut();
        }
        return null;
    }

    /**
     * @description creates a new PleaSaveValue based on the old Plea and new
     *              move Value
     * @param oldPlea
     * @param newMoveValue
     * @return new PleaSaveValue
     */
    private static PleaSaveValue getNewPlea(PleaSaveValue oldPlea, MoveResultsValue newMoveValue) {
        XhbPleaBasicValue oldPleaBV = oldPlea.getPleaBasicValue();
        XhbPleaBasicValue newPleaBV = new XhbPleaBasicValue();

        newPleaBV.setAltRefOffenceId(oldPleaBV.getAltRefOffenceId());
        newPleaBV.setArraignmentDate(oldPleaBV.getArraignmentDate());
        newPleaBV.setDefendantOnOffenceId(newMoveValue.getDefendantOnOffenceId());
        newPleaBV.setDefOnChargeOrOffence(oldPleaBV.getDefOnChargeOrOffence());
        newPleaBV.setOtherPleaText(oldPleaBV.getOtherPleaText());
        newPleaBV.setAltUncodedOffenceDesc(oldPleaBV.getAltUncodedOffenceDesc());
        newPleaBV.setPleaId(null);
        newPleaBV.setRefPleaId(oldPleaBV.getRefPleaId());

        PleaValue oldPleaValue = oldPlea.getPleaValue();
        PleaValue newPleaValue = new PleaValue(newPleaBV);

        // These values should not be set as these are the values currently
        // stored in the DB for a given Defendant On Offence
        // newPleaValue.setOriginalPleaDate(oldPleaValue.getOriginalPleaDate());
        // newPleaValue.setOriginalRefPleaId(oldPleaValue.getOriginalRefPleaId());
        // newPleaValue.setOriginalRefVerdictId(oldPleaValue.getOriginalRefVerdictId());
        // newPleaValue.setOriginalVerdictDate(oldPleaValue.getOriginalVerdictDate());
        newPleaValue.setRefPleaCode(oldPleaValue.getRefPleaCode());

        PleaSaveValue newPlea = new PleaSaveValue(newPleaValue, ResultSaveValue.ADD, newMoveValue.getCaseId(),
                newMoveValue.getCaseNumber(), newMoveValue.getCaseType(), newMoveValue.getCrestOffenceId(),
                newMoveValue.getCrestDefendantId(), oldPlea.getChargeType());
        newPlea.setCourtLogged(false);
        newPlea.setVcoDate(oldPlea.getVcoDate());
        newPlea.setVcoFlag(oldPlea.getVcoFlag());

        return newPlea;
    }
}