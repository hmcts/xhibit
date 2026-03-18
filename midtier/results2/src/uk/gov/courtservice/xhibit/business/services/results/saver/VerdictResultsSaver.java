package uk.gov.courtservice.xhibit.business.services.results.saver;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_hearing.XhbHearing;
import uk.gov.courtservice.xhibit.business.entities.xhb_hearing.XhbHearingBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_court.XhbRefCourtBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearingBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictTypeEvent;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: This interface defines what the ResultsSaver can do.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version $Revision: 1.32 $
 */
public class VerdictResultsSaver extends AbstractResultsSaver {
    private static final Logger log = CSServices.getLogger(VerdictResultsSaver.class);

    public void preprocess(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException {
        if (resultsSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not preprocess null results.");
        }

        ResultSaveValue resultSaveValue = resultsSaveValue.getResultSaveValue(index);

        if (resultSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not preprocess null results.");
        }

        if (!(resultSaveValue instanceof VerdictSaveValue)) {
            throw new ResultsControllerException("ResultsSaver.InvalidResult", new Object[] { resultSaveValue
                    .getClass().getName() }, "Can not preprocess result of type "
                    + resultSaveValue.getClass().getName() + ".");
        }

        preprocess(resultsSaveValue, index, (VerdictSaveValue) resultSaveValue);
    }

    public void save(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException {
        if (resultsSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not save null results.");
        }

        ResultSaveValue resultSaveValue = resultsSaveValue.getResultSaveValue(index);

        if (resultSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not save null results.");
        }

        if (!(resultSaveValue instanceof VerdictSaveValue)) {
            throw new ResultsControllerException("ResultsSaver.InvalidResult", new Object[] { resultSaveValue
                    .getClass().getName() }, "Can not save result of type " + resultSaveValue.getClass().getName()
                    + ".");
        }

        save((VerdictSaveValue) resultSaveValue);
    }

    public void log(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException {
        if (resultsSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not log null results.");
        }

        ResultSaveValue resultSaveValue = resultsSaveValue.getResultSaveValue(index);

        if (resultSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not log null results.");
        }

        if (!(resultSaveValue instanceof VerdictSaveValue)) {
            throw new ResultsControllerException("ResultsSaver.InvalidResult", new Object[] { resultSaveValue
                    .getClass().getName() }, "Can not log result of type " + resultSaveValue.getClass().getName() + ".");
        }

        VerdictCourtLogHelper.getInstance().log((VerdictSaveValue) resultSaveValue);
        Collection cases = XhbCaseBeanHelper2.findByJoinderDefendantOnOffenceId(((VerdictSaveValue) resultSaveValue).getDefendantOnOffenceId());
        if(cases!=null && cases.size()>0) {
	        // get hold of all available linked cases to this case
	        Collection<XhbHearing> linkedScheduledHearings =
	        		XhbHearingBeanHelper2.findLinkedHearingsFromSchedId(((VerdictSaveValue)resultSaveValue).getScheduledHearingId());
	
	        if (linkedScheduledHearings != null) {
	        	  final Iterator<XhbHearing> it = linkedScheduledHearings.iterator();
	              while (it.hasNext()) {
	                resultSaveValue.setCourtLogCaseId(it.next().getCaseId());
	                VerdictCourtLogHelper.getInstance().log((VerdictSaveValue) resultSaveValue);
	            }
	        }
        }
    }

    private void preprocess(ResultsSaveValue resultsSaveValue, int index, VerdictSaveValue verdictSaveValue)
            throws ResultsControllerException {

        // validate
        validate(verdictSaveValue);

        if (verdictSaveValue.isOnOffence()) {
            // Calculate VCO
            VCOCalculator.calculate(resultsSaveValue, index, verdictSaveValue);

            // Automatic Disposals
            DisposalSaveValue disposalSaveValue = createNotGuiltyDisposal(resultsSaveValue.getCourtId(),
                    verdictSaveValue);
            if (disposalSaveValue != null && !exists(resultsSaveValue, disposalSaveValue)) {
                setJoinderAttributes(disposalSaveValue, disposalSaveValue.getDefendantOnOffenceId());
                resultsSaveValue.addResultSaveValue(disposalSaveValue);
            }
            setJoinderAttributes(verdictSaveValue, verdictSaveValue.getDefendantOnOffenceId());
        }
    }

    private void save(VerdictSaveValue result) throws ResultsControllerException {
        String operation = result.getOperation();
        if (operation.equals(ResultSaveValue.ADD)) {
            createVerdict(result);
        } else if (operation.equals(ResultSaveValue.UPDATE)) {
            updateVerdict(result);
        } else if (operation.equals(ResultSaveValue.DELETE)) {
            deleteVerdict(result);
        } else {
            throw new ResultsControllerException("AbstractResultsSaver.UnrecognisedOperation", new Object[] { result
                    .getOperation() }, "Unrecognised operation " + result.getOperation() + ".");
        }
    }

    private void createVerdict(VerdictSaveValue result) {
        log.debug("createVerdict(VerdictSaveValue result) : START");

        result.setVerdictBasicValue(XhbVerdictBeanHelper2.create(result.getVerdictBasicValue()));
        // update CccTransToRefCourtId
        updateCase(result);
        updateHearing(result);
        updateVco(result);

        log.debug("createVerdict(VerdictSaveValue result) : END");
    }

    private void updateVerdict(VerdictSaveValue result) {
        log.debug("updateVerdict(VerdictSaveValue result)- START");
        log.debug("updateVerdict(VerdictSaveValue result)- VERSION BEFORE update: "
                + result.getVerdictBasicValue().getVersion());
        log.debug("Input VBV BEFORE UPDATE: " + result.getVerdictBasicValue());

        XhbVerdictBeanHelper2.update(result.getVerdictBasicValue());
        // update CccTransToRefCourtId
        updateCase(result);
        updateHearing(result);
        updateVco(result);

        log.debug("updateVerdict(VerdictSaveValue result)- VERSION AFTER update: "
                + result.getVerdictBasicValue().getVersion());
        log.debug("updateVerdict(VerdictSaveValue result)- END");
    }

    private void deleteVerdict(VerdictSaveValue result) {
        log.debug("deleteVerdict - START");
        if (!result.isObsolete()) {
            log.debug("deleteVerdict - set Obsolete flag");
            result.setObsInd(true);
            XhbVerdictBeanHelper2.update(result.getVerdictBasicValue());

            // remove CccTransToRefCourtId from xhbCase
            result.setCccTransToRefCourtId(null);
            updateCase(result);
            updateVco(result);

            // don't need to remove CaseAppReason values implicitly since
            // they are removed explicity by client.
        }
        log.debug("deleteVerdict - END");
    }

    private void updateCase(VerdictSaveValue result) {
        log.debug("updateCase - START");

        // update case value in all instances since new verdict may require
        // cccTransToRefCourtId to be null
        Integer cccTransToRefCourtId = result.getCccTransToRefCourtId();
        log.debug("CccTransToRefCourtId= " + cccTransToRefCourtId);
        log.debug("CourtLogCaseId= " + result.getCourtLogCaseId());
        XhbCase entityBean = XhbCaseBeanHelper2.findByPrimaryKey(result.getCourtLogCaseId());
        log.debug("XhbCase.cccTransToRefCourtId= " + entityBean.getCccTransToRefCourtId());

        if (!equals(entityBean.getCccTransToRefCourtId(), cccTransToRefCourtId)) {
            entityBean.setXhbRefCourtByCccTransToRefCourtId(cccTransToRefCourtId == null ? null
                    : XhbRefCourtBeanHelper2.findByPrimaryKey(cccTransToRefCourtId));
        }
    }

    /**
     * @todo remove no longer called
     */
    private void populateHearingData(VerdictSaveValue result) {
        Integer scheduledHearingId = result.getScheduledHearingId();
        if (scheduledHearingId == null) {
            throw new IllegalArgumentException("scheduledHearingId is null");
        }
        XhbScheduledHearing scheduledHearing = XhbScheduledHearingBeanHelper2.findByPrimaryKey(scheduledHearingId);
        XhbHearing hearing = scheduledHearing.getXhbHearing();
        result.setHearingDate(hearing.getHearingStartDate());
        if (hearing.getLastCalculatedDuration() != null) {
            result.setLastCalculatedDuration(new Long(hearing.getLastCalculatedDuration().longValue()));
        } else {
            result.setLastCalculatedDuration(new Long(0));
        }
    }

    /**
     * Private method used to add the hearing start date to the hearing, and to
     * calculate the hearing duration. This will only do any processing for
     * miscellaneous appeal cases.
     * 
     * @param result
     */
    private void updateHearing(VerdictSaveValue result) {
        if (result.isMiscAppeal()) {
            final Integer scheduledHearingId = result.getScheduledHearingId();
            final XhbScheduledHearing scheduledHearing = XhbScheduledHearingBeanHelper2
                    .findByPrimaryKey(scheduledHearingId);
            final XhbHearing hearing = scheduledHearing.getXhbHearing();

            hearing.setHearingStartDate(result.getHearingDate());
            hearing.setLastCalculatedDuration(result.getLastCalculatedDuration());
        }
    }

    /**
     * Update the vco date on the defendant on offence object
     */
    private void updateVco(VerdictSaveValue result) {
        if (result.isOnOffence()) {
            updateVco(result.getDefendantOnOffenceId(), result.getVcoDate(), result.getVcoFlag());
        }
    }

    private DisposalSaveValue createNotGuiltyDisposal(Integer courtId, VerdictSaveValue result)
            throws ResultsControllerException {

        switch (result.getVerdictType()) {
        case VerdictTypeEvent.NOT_GUILTY_JUDGES_DIRECTION_TYPE: // NGJJ
            return AutomaticDisposalHelper.createNgdirDisposal(courtId, result);
        case VerdictTypeEvent.NOT_GUILTY_JU_TYPE: // NGJU
            return AutomaticDisposalHelper.createNoevDisposal(courtId, result);
        case VerdictTypeEvent.NOT_GUILTY_TYPE: // NG
            return AutomaticDisposalHelper.createDischDisposal(courtId, result);
        case VerdictTypeEvent.NOT_GUILTY_JUDGE_UNDER_DUC_VA2004_TYPE: // NGJA
            // Added for PR6176
            return AutomaticDisposalHelper.createDischDisposal(courtId, result);
        default:
            return null;
        }
    }

    private void validate(VerdictSaveValue result) throws ResultsControllerException {
        if (!hasvalidAssentingDissenting(result)) {
            throw new ResultsControllerException("VerdictResulsSaver.AscDesc", new Object[] {
                    result.getRefVerdictCode(), result.getJurorsAssenting(), result.getJurorsDissenting() },
                    "Jurors Assenting " + result.getJurorsAssenting() + " Dissenting " + result.getJurorsDissenting()
                            + " is invalid for verdict " + result.getRefVerdictCode() + ".");
        }
    }

    private boolean hasvalidAssentingDissenting(VerdictSaveValue result) {
        Integer assenting = result.getJurorsAssenting();
        Integer dissenting = result.getJurorsDissenting();

        return result.getJurorsAssentingOption()
                || (result.hasAssentingDissenting() && assenting != null && dissenting != null)
                || (!result.hasAssentingDissenting() && assenting == null && dissenting == null);
    }

    private boolean exists(ResultsSaveValue resultsSaveValue, DisposalSaveValue disposalSaveValue) {
        if (resultsSaveValue == null) {
            throw new IllegalArgumentException("disposalSaveValue: null");
        }

        if (disposalSaveValue == null) {
            throw new IllegalArgumentException("disposalSaveValue: " + disposalSaveValue);
        }

        if (disposalSaveValue.isRelatedDisposal()) {
            existsRelated(resultsSaveValue, disposalSaveValue);
        }

        if (disposalSaveValue.isUnrelatedDisposal()) {
            existsUnrelated(resultsSaveValue, disposalSaveValue);
        }

        return false;
    }

    private boolean existsRelated(ResultsSaveValue resultsSaveValue, DisposalSaveValue disposalSaveValue) {
        if (resultsSaveValue == null) {
            throw new IllegalArgumentException("disposalSaveValue: null");
        }

        if (disposalSaveValue == null || !disposalSaveValue.isRelatedDisposal()) {
            throw new IllegalArgumentException("disposalSaveValue: " + disposalSaveValue);
        }

        String disposalCode = disposalSaveValue.getDisposalCode();
        Integer defendantOnOffenceId = disposalSaveValue.getDefendantOnOffenceId();
        for (int i = 0, c = resultsSaveValue.getResultSaveValueCount(); i < c; i++) {
            ResultSaveValue value = resultsSaveValue.getResultSaveValue(i);
            if (value instanceof DisposalSaveValue) {
                DisposalSaveValue disposal = (DisposalSaveValue) value;
                if (disposal.isRelatedDisposal() && defendantOnOffenceId.equals(disposal.getDefendantOnOffenceId())
                        && disposalCode.equals(disposal.getDisposalCode())) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean existsUnrelated(ResultsSaveValue resultsSaveValue, DisposalSaveValue disposalSaveValue) {
        if (resultsSaveValue == null) {
            throw new IllegalArgumentException("disposalSaveValue: null");
        }

        if (disposalSaveValue == null || !disposalSaveValue.isUnrelatedDisposal()) {
            throw new IllegalArgumentException("disposalSaveValue: " + disposalSaveValue);
        }

        String disposalCode = disposalSaveValue.getDisposalCode();
        Integer defendantOnCaseId = disposalSaveValue.getDefendantOnCaseId();
        for (int i = 0, c = resultsSaveValue.getResultSaveValueCount(); i < c; i++) {
            ResultSaveValue value = resultsSaveValue.getResultSaveValue(i);
            if (value instanceof DisposalSaveValue) {
                DisposalSaveValue disposal = (DisposalSaveValue) value;
                if (!disposal.isUnrelatedDisposal() && defendantOnCaseId.equals(disposal.getDefendantOnCaseId())
                        && disposalCode.equals(disposal.getDisposalCode())) {
                    return true;
                }
            }
        }

        return false;
    }

}
