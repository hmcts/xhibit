package uk.gov.courtservice.xhibit.business.services.results.saver;

import java.util.Date;

import uk.gov.courtservice.xhibit.business.database.results.ResultsDatabase;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictTypeEvent;

/**
 * <p>
 * Title: RecordSheetDisposalsPopulater
 * </p>
 * <p>
 * Description: Generate automatic disposals.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.10 $
 */
public class AutomaticDisposalHelper {
    /**
     * Stop unnecesary construction of this interface class
     */
    private AutomaticDisposalHelper() {
    }

    /**
     * Create a DISCH disposal for a NG and NGJA verdict on an offence
     */
    public static DisposalSaveValue createDischDisposal(Integer courtId, VerdictSaveValue verdictSaveValue)
            throws ResultsControllerException {
        if (courtId == null) {
            throw new IllegalArgumentException("courtId: null");
        }
        if (verdictSaveValue == null || !verdictSaveValue.isOnOffence()
                || (verdictSaveValue.getVerdictType() != VerdictTypeEvent.NOT_GUILTY_TYPE
                        && verdictSaveValue.getVerdictType() != VerdictTypeEvent.NOT_GUILTY_JUDGE_UNDER_DUC_VA2004_TYPE)) {
            throw new IllegalArgumentException("verdictSaveValue: " + verdictSaveValue);
        }

        return createDisposal(courtId, verdictSaveValue, "DISCH");
    }

    /**
     * Create a NOEV disposal for a NGJU verdict on an offence
     */
    public static DisposalSaveValue createNoevDisposal(Integer courtId, VerdictSaveValue verdictSaveValue)
            throws ResultsControllerException {
        if (courtId == null) {
            throw new IllegalArgumentException("courtId: null");
        }
        if (verdictSaveValue == null || !verdictSaveValue.isOnOffence()
                || verdictSaveValue.getVerdictType() != VerdictTypeEvent.NOT_GUILTY_JU_TYPE) {
            throw new IllegalArgumentException("verdictSaveValue: " + verdictSaveValue);
        }

        return createDisposal(courtId, verdictSaveValue, "NOEV");
    }

    // Disposal Code Constants
    // private static final String NGDIR_DISPOSAL_CODE = "NGDIR"; //
    // Verdict: NGJJ

    /**
     * Create a NGDIR disposal for a NGJJ verdict on an offence
     */
    public static DisposalSaveValue createNgdirDisposal(Integer courtId, VerdictSaveValue verdictSaveValue)
            throws ResultsControllerException {
        if (courtId == null) {
            throw new IllegalArgumentException("courtId: null");
        }
        if (verdictSaveValue == null || !verdictSaveValue.isOnOffence()
                || verdictSaveValue.getVerdictType() != VerdictTypeEvent.NOT_GUILTY_JUDGES_DIRECTION_TYPE) {
            throw new IllegalArgumentException("verdictSaveValue: " + verdictSaveValue);
        }

        return createDisposal(courtId, verdictSaveValue, "NGDIR");
    }

    /**
     * Create a NOEV disposal for a NGJU verdict on an offence
     */
    private static DisposalSaveValue createDisposal(Integer courtId, VerdictSaveValue verdictSaveValue,
            String disposalCode) throws ResultsControllerException {
        DisposalReferenceValue disposalReference = ResultsDatabase.getLatestReferenceDisposal(courtId, disposalCode);
        DisposalValue disposal = disposalReference.createDisposal(verdictSaveValue.getDefendantOnOffenceId());

        // The following flags are set to true when the mandatory data item is
        // populated
        boolean setDateOfResult = false;
        boolean setAdditionalText = false;
        boolean setReplaced = false;

        // Replaced?

        // Populate the disposal data, losely coupled to the template
        // implementation
        for (int i = 0, c = disposalReference.getLineCount(); i < c; i++) {
            DisposalLineReferenceValue lineReference = disposalReference.getLine(i);
            if (match(lineReference.getPrompt(), "date of result")) {
                Date dateOfResult = verdictSaveValue.getHearingDate();
                if (dateOfResult == null) {
                    dateOfResult = new Date();
                }
                disposal.addLine(lineReference.createValue(0, dateOfResult));
                setDateOfResult = true;
            }
            if (match(lineReference.getData(), "insert additional text")) {
                disposal.addLine(lineReference.createValue(1, "Automatically generated for verdict "
                        + verdictSaveValue.getRefVerdictCode() + "."));
                setAdditionalText = true;
            }
            if (match(lineReference.getPrompt(), "replaced?")) {
                disposal.addLine(lineReference.createValue(1, false));
                setReplaced = true;
            }
        }

        // Check we have set all data
        if (!setDateOfResult) {
            throw new ResultsControllerException("AutomaticDisposalHelper.SetDate", "Could not set date of result.");
        }
        if (!setAdditionalText) {
            throw new ResultsControllerException("AutomaticDisposalHelper.AddText", "Could not set additional text.");
        }
        if (!setReplaced) {
            throw new ResultsControllerException("AutomaticDisposalHelper.SetReplaced", "Could not set replaced.");
        }
        return new DisposalSaveValue(disposal, disposalReference, ResultSaveValue.ADD, verdictSaveValue
                .getCourtLogCaseId(), verdictSaveValue.getCourtLogCaseNumber(), verdictSaveValue.getCourtLogCaseType(),
                verdictSaveValue.getCaseSubType(), verdictSaveValue.getCrestOffenceId(), verdictSaveValue
                        .getCrestDefendantId(), verdictSaveValue.getCrestOffenceSeqNo(), verdictSaveValue
                        .getCrestChargeId(), verdictSaveValue.getCrestChargeSeqNo(), verdictSaveValue.getChargeType(),
                verdictSaveValue.getDefendantOnCaseId(), verdictSaveValue.getDefendantName(), verdictSaveValue
                        .isInCourt(), verdictSaveValue.getCourtLogDate(), verdictSaveValue.getScheduledHearingId(),
                null); // psdDisId is
        // derived from
        // psdDisposal2Id,
        // null since
        // psdDisposal2Id
        // is null
    }

    private static boolean match(String text, String criteria) {
        return text != null && text.toLowerCase().indexOf(criteria) != -1;
    }
}
