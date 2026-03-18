package uk.gov.courtservice.xhibit.business.services.results.populater;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_hearing.XhbHearingBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_hearing.XhbHearingBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_app_result.XhbRefAppResult;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_court.XhbRefCourt;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_system_code.XhbRefSystemCode;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdict;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.services.charge.UncodedOffenceInterface;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.JoinderChargeInfoValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.common.results.vos.MoveResultsValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;

/**
 * <p>
 * Title: Populates ResultsCompositeValue with Verdicts
 * </p>
 * <p>
 * Description: Ensures Verdicts for Defendant on Charge and Defendant on
 * Offence are set
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Bal Bhamra
 * @version $Revision: 1.29 $
 */
public class VerdictsPopulater extends AbstractResultsPopulater implements UncodedOffenceInterface {
    private static final Logger log = CSServices.getLogger(VerdictsPopulater.class);

    public void populate(ResultsCompositeValue rcv) throws ResultsControllerException {
        log.debug("populate() - START");
        populateVerdicts(rcv);
        log.debug("populate() - END");
    }

    public void populateSaveValues(ResultsSaveValue results, MoveResultsValue oldMoveValue,
            MoveResultsValue newMoveValue) throws ResultsControllerException {
        final Collection verdicts = XhbVerdictBeanHelper2.findByDefendantOnOffenceId(oldMoveValue
                .getDefendantOnOffenceId());

        // if empty do nothing
        if (verdicts.size() == 0) {
            return;
        }

        // shouldn't be more than 1 active verdict
        if (verdicts.size() > 1) {
            throw new ResultsControllerException(null, "Invalid number of Verdicts:" + verdicts.size());
        }

        VerdictValue oldVerdictValue = createVerdictValue((XhbVerdict) verdicts.iterator().next(), null);

        VerdictSaveValue oldVerdict = new VerdictSaveValue(oldVerdictValue, ResultSaveValue.DELETE, oldMoveValue
                .getCaseId(), oldMoveValue.getCaseNumber(), oldMoveValue.getCaseType(), oldMoveValue.getCaseSubType(),
                oldMoveValue.getCrestOffenceId(), oldMoveValue.getCrestDefendantId(), oldMoveValue.getChargeType());

        oldVerdict.setCourtLogged(false);
        oldVerdict.setVcoDate(oldMoveValue.getVcoDate());
        oldVerdict.setVcoFlag(oldMoveValue.getVcoFlag());

        results.addResultSaveValue(oldVerdict);
        results.addResultSaveValue(getNewVerdict(oldVerdict, newMoveValue));
    }

    private void populateVerdicts(ResultsCompositeValue rcv) {
        log.debug("populateVerdicts() - START");

        if (rcv.getChargeCompositeValue().getCaseBasicValue().getCaseType().equals("A")) {
            // Only Appeal cases have verdicts (Appeal Results) at a case
            // level.
            populateVerdictsForCase(rcv);
        }

        Iterator chargesIterator = rcv.getChargeCompositeValue().getCharges().iterator();
        while (chargesIterator.hasNext()) {
            ChargeValue chargeValue = (ChargeValue) chargesIterator.next();

            if (chargeValue.getChargeType().equals(ChargeTypes.INDICTMENT.getChargeType())) {
                JoinderChargeInfoValue[] joinderChargeInfoValue = chargeValue.getJoinderChargeInfoValues();
                if (joinderChargeInfoValue != null) {
                    // This is a joinder indictment so get results for all
                    // charges in joinder
                    for (int i = 0, len = joinderChargeInfoValue.length; i < len; i++) {
                        populateVerdictsForOffence(rcv, joinderChargeInfoValue[i].getChargeId());
                    }
                } else {
                    populateVerdicts(rcv, chargeValue);
                }
            } else {
                populateVerdicts(rcv, chargeValue);
            }
        }
        log.debug("populateVerdicts() - END");
    }

    private void populateVerdicts(final ResultsCompositeValue rcv, final ChargeValue chargeValue) {
        // SG - Don't think Verdicts For Charge exist?
        // populateVerdictsForCharge(rcv, chargeId);

        populateVerdictsForOffence(rcv, chargeValue.getChargeID());

        // Criminal Appeals can have Appeal Results against Magistrate Court
        // General Disposals.
        if (chargeValue.getChargeType().equals(ChargeTypes.CRIMINAL_APPEAL.getChargeType())) {
            populateVerdictsForDisposals(rcv);
        }
    }

    private void populateVerdictsForCase(ResultsCompositeValue rcv) {
        log.debug("populateVerdictsForCase() - START");

        // find Verdicts and set to ResultsCompositeValue
        final XhbCase caseValue = XhbCaseBeanHelper2.findByPrimaryKey(rcv.getCaseId());
        final XhbRefCourt refCourt = caseValue.getXhbRefCourtByCccTransToRefCourtId();
        final XhbHearingBasicValue hearingValue = getHearing(rcv.getScheduledHearingId());
        
        log.debug("rcv.getScheduledHearingId(): " + rcv.getScheduledHearingId());
        // Changed to use finder instead of CMR because CMR does not use
        // obsolete indicator.
        final Iterator verdictsIterator = XhbVerdictBeanHelper2.findByCaseIdForCase(caseValue.getCaseId()).iterator();
        while (verdictsIterator.hasNext()) {
            final XhbVerdict verdict = (XhbVerdict) verdictsIterator.next();
            final VerdictValue verdictValue = createVerdictValue(verdict, refCourt);
            if (hearingValue != null) {
                verdictValue.setHearingDate(hearingValue.getHearingStartDate());
                verdictValue.setLastCalculatedDuration(hearingValue.getLastCalculatedDuration());
            }
            rcv.addCaseVerdict(verdictValue.getCaseId(), verdictValue);
        }
        log.debug("populateVerdictsForCase() - END");
    }

    /**
     * Populate Verdicts (Appeal Results) for (Magistrate Court General)
     * Disposals.
     * 
     * @param rcv
     *            ResultsCompositeValue
     */
    private void populateVerdictsForDisposals(final ResultsCompositeValue rcv) {
        log.debug("populateVerdictsForDisposals() - START");

        final ChargeCompositeValue ccv = rcv.getChargeCompositeValue();
        if (ccv.getAllDefendants() != null && ccv.getAllDefendants().size() > 0) {
            log.debug("Number of defendants = " + ccv.getAllDefendants().size());

            final Iterator i = ccv.getAllDefendants().iterator();
            // There should be one and only one defendant on a Criminal
            // Appeal Case.
            if (i.hasNext()) {
                final DefendantValue defendantValue = (DefendantValue) i.next();

                log.debug("DefOnCaseBasicValue = " + defendantValue.getDefOnCaseBasicValue());

                final Iterator verdictsIterator = XhbVerdictBeanHelper2.findByDefendantOnCaseId(
                        defendantValue.getDefOnCaseBasicValue().getId()).iterator();
                while (verdictsIterator.hasNext()) {
                    final XhbVerdict verdict = (XhbVerdict) verdictsIterator.next();
                    final VerdictValue verdictValue = createVerdictValue(verdict, null);
                    rcv.addVerdictForDisposal(verdictValue.getDisposal2Id(), verdictValue);
                }
            }
        }

        log.debug("populateVerdictsForDisposals() - END");
    }

    /**
     * XhbHearingBasicValue information returned.
     * 
     * @param scheduledHearingId
     * @return
     */
    private static XhbHearingBasicValue getHearing(Integer scheduledHearingId) {
        if (scheduledHearingId == null || scheduledHearingId <= 0)
            return null;
        return XhbHearingBeanHelper2.findByScheduledHearingIdValue(scheduledHearingId);
    }

    private void populateVerdictsForCharge(ResultsCompositeValue rcv, Integer chargeId) {
        log.debug("populateVerdictsForCharge() - START");

        // find Verdicts and set to ResultsCompositeValue
        final Iterator verdictsIterator = XhbVerdictBeanHelper2.findByChargeIdForCharge(chargeId).iterator();

        while (verdictsIterator.hasNext()) {
            final XhbVerdict verdict = (XhbVerdict) verdictsIterator.next();
            final VerdictValue verdictValue = createVerdictValue(verdict, null);
            rcv.addChargeVerdict(verdictValue.getDefendantChargeId(), verdictValue);
        }
        log.debug("populateVerdictsForCharge() - END");
    }

    private void populateVerdictsForOffence(ResultsCompositeValue rcv, Integer chargeId) {
        log.debug("populateVerdictsForOffence() - START");

        // find Verdicts and set to ResultsCompositeValue
        final Iterator verdictsIterator = XhbVerdictBeanHelper2.findByChargeIdForOffence(chargeId).iterator();

        while (verdictsIterator.hasNext()) {
            final XhbVerdict verdict = (XhbVerdict) verdictsIterator.next();
            final VerdictValue verdictValue = createVerdictValue(verdict, null);
            rcv.addVerdict(verdictValue.getDefendantOnOffenceId(), verdictValue);
        }
        log.debug("populateVerdictsForOffence() - END");
    }

    /**
     * Public method to determine/call whether sys or ref code needs to be
     * looked up for for verdicts.
     * 
     * @param VerdictValue
     */
    private VerdictValue createVerdictValue(XhbVerdict verdict, XhbRefCourt refCourt) {
        log.debug("populateVerdict() - verdict = " + verdict.getVerdictId());
        final VerdictValue verdictValue = new VerdictValue(verdict.getData());
        final XhbRefOffence refOffence = verdict.getXhbRefOffence();
        final XhbRefSystemCode refSystemCode = verdict.getXhbRefSystemCode();
        final XhbRefAppResult refAppealResult = verdict.getXhbRefAppResult();

        if (refCourt != null) {
            verdictValue.setCccTransToRefCourtId(refCourt.getRefCourtId());
            verdictValue.setCccTransToRefCourtCode(refCourt.getCrestCode());
            verdictValue.setCccTransToRefCourtDesc(refCourt.getCourtFullName());
        }

        if (refOffence != null) {
            verdictValue.setViewAltRefOffenceCode(refOffence.getOffenceCode());
            if (!refOffence.getOffenceCode().equalsIgnoreCase(UNCODED_OFFENCE_REFERENCE_CODE)) {
                verdictValue.setViewAltRefOffenceDesc(refOffence.getOffenceDesc());
            } else {
                verdictValue.setViewAltRefOffenceDesc(verdict.getData().getAltUncodedOffenceDesc());
            }
        }

        if (refSystemCode != null) {
            verdictValue.setOriginalRefVerdictId(refSystemCode.getRefSystemCodeId());
            verdictValue.setRefVerdictDesc(refSystemCode.getDeCode());
            verdictValue.setRefVerdictCode(refSystemCode.getCode());
            verdictValue.setRefVerdictType(refSystemCode.getCodeType());
            /** @todo Is Type set to CodeType?? */
        }

        if (refAppealResult != null) {
        	verdictValue.setRefAppResultId(refAppealResult.getRefAppResultId());
            verdictValue.setRefAppealOffenceCode(refAppealResult.getAppResultCode());
            verdictValue.setOriginalRefVerdictId(refAppealResult.getRefAppResultId());
            verdictValue.setRefAppealOffenceDesc(refAppealResult.getAppResultDescr1() + " "
                    + refAppealResult.getAppResultDescr2() == null ? "" : refAppealResult.getAppResultDescr2());
        }

        verdictValue.setOriginalVerdictDate(verdictValue.getVerdictDate());

        XhbPleaBasicValue pleaBasicValue = getPleaBasicValue(verdictValue);
        if (pleaBasicValue != null) {
            Integer refPleaId = pleaBasicValue.getRefPleaId();
            if (refPleaId != null) {
                verdictValue.setOriginalRefPleaId(refPleaId);
            }

            Date pleaDate = pleaBasicValue.getArraignmentDate();
            if (pleaDate == null) {
                verdictValue.setOriginalPleaDate(pleaDate);
            }
        }
        return verdictValue;
    }

    /**
     * Get the refPleaId for the Verdict (if it exists)
     */
    private XhbPleaBasicValue getPleaBasicValue(VerdictValue verdictValue) {
        XhbPleaBasicValue[] pleaValues = null;

        if (verdictValue.isOnCharge()) {
            pleaValues = XhbPleaBeanHelper2.findByDefendantChargeIdValue(verdictValue.getDefendantChargeId());
        } else if (verdictValue.isOnOffence()) {
            pleaValues = XhbPleaBeanHelper2.findByDefendantOnOffenceIdValue(verdictValue.getDefendantOnOffenceId());
        }
        return (((pleaValues != null) && (pleaValues.length > 0)) ? pleaValues[0] : null);
    }

    /**
     * @description creates a new VerdictSaveValue based on the old Verdict and
     *              new move Value
     * @param oldVerdict
     * @param newMoveValue
     * @return new VerdictSaveValue
     */
    private static VerdictSaveValue getNewVerdict(VerdictSaveValue oldVerdict, MoveResultsValue newMoveValue) {
        XhbVerdictBasicValue oldVerdictBV = oldVerdict.getVerdictBasicValue();
        XhbVerdictBasicValue newVerdictBV = new XhbVerdictBasicValue();

        newVerdictBV.setAltRefOffenceId(oldVerdictBV.getAltRefOffenceId());
        newVerdictBV.setAppLesserOff(oldVerdictBV.getAppLesserOff());
        newVerdictBV.setDefendantOnOffenceId(newMoveValue.getDefendantOnOffenceId());
        newVerdictBV.setDefOnChargeOrOffence(oldVerdictBV.getDefOnChargeOrOffence());
        newVerdictBV.setJurorsAssenting(oldVerdictBV.getJurorsAssenting());
        newVerdictBV.setJurorsDissenting(oldVerdictBV.getJurorsDissenting());
        newVerdictBV.setOtherVerdictText(oldVerdictBV.getOtherVerdictText());
        newVerdictBV.setAltUncodedOffenceDesc(oldVerdictBV.getAltUncodedOffenceDesc());
        newVerdictBV.setVerdictId(null);
        newVerdictBV.setRefVerdictId(oldVerdictBV.getRefVerdictId());
        newVerdictBV.setVerdictDate(oldVerdictBV.getVerdictDate());

        VerdictValue oldVerdictValue = oldVerdict.getVerdictValue();
        VerdictValue newVerdictValue = new VerdictValue(newVerdictBV);

        // These values should not be set as these are the values currently
        // stored in the DB
        // for a given Defendant On Offence
        // newVerdictValue.setOriginalPleaDate(oldVerdictValue.getOriginalPleaDate());
        // newVerdictValue.setOriginalRefPleaId(oldVerdictValue.getOriginalRefPleaId());
        // newVerdictValue.setOriginalRefVerdictId(oldVerdictValue.getOriginalRefVerdictId());
        // newVerdictValue.setOriginalVerdictDate(oldVerdictValue.getOriginalVerdictDate());
        newVerdictValue.setRefVerdictCode(oldVerdictValue.getRefVerdictCode());
        newVerdictValue.setRefVerdictType(oldVerdictValue.getRefVerdictType());
        newVerdictValue.setRefVerdictDesc(oldVerdictValue.getRefVerdictDesc());

        VerdictSaveValue newVerdict = new VerdictSaveValue(newVerdictValue, ResultSaveValue.ADD, newMoveValue
                .getCaseId(), newMoveValue.getCaseNumber(), newMoveValue.getCaseType(), newMoveValue.getCaseSubType(),
                newMoveValue.getCrestOffenceId(), newMoveValue.getCrestDefendantId(), oldVerdict.getChargeType());

        newVerdict.setCourtLogged(false);
        newVerdict.setVcoDate(oldVerdict.getVcoDate());
        newVerdict.setVcoFlag(oldVerdict.getVcoFlag());

        return newVerdict;
    }
}
