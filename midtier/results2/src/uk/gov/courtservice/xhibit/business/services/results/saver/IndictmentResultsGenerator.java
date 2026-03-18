package uk.gov.courtservice.xhibit.business.services.results.saver;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendant;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffence;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.services.results.ResultsPopulater;
import uk.gov.courtservice.xhibit.business.services.results.ResultsPopulaterFactory;
import uk.gov.courtservice.xhibit.common.results.vos.MoveResultsValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;

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
 * @author Abdul Rahim Hussain
 * @version $Revision: 1.6 $
 */
public class IndictmentResultsGenerator {

    private static final Logger log = CSServices.getLogger(IndictmentResultsGenerator.class);

    /**
     * Stop unnecesary construction of this class
     */
    private IndictmentResultsGenerator() {
    }

    public static void setDefendantOnOffenceResults(ResultsSaveValue results, Integer defendantOnOffenceId,
            Integer newDefendantOnOffenceId) throws ResultsControllerException {
        // Loop through and populate all resultSaveValues
        log.debug("setDefendantOnOffenceResults - START");
        ResultsPopulater[] resultsPopulators = ResultsPopulaterFactory.getInstance().getResultsPopulaters();
        for (int i = 0; i < resultsPopulators.length; i++) {
            resultsPopulators[i].populateSaveValues(results, createMoveValue(defendantOnOffenceId),
                    createMoveValue(newDefendantOnOffenceId));
        }
        log.debug("setDefendantOnOffenceResults - END");
    }

    private static MoveResultsValue createMoveValue(Integer defendantOnOffenceId) {
        XhbDefendantOnOffence defendantOnOffence = XhbDefendantOnOffenceBeanHelper2
                .findByPrimaryKey(defendantOnOffenceId);
        XhbDefendant defendant = defendantOnOffence.getXhbDefendantOnCase().getXhbDefendant();
        XhbOffence offence = defendantOnOffence.getXhbOffence();
        XhbCharge charge = offence.getXhbCharge();
        XhbCase caze = charge.getXhbCase();

        return new MoveResultsValue(defendantOnOffenceId, caze.getCaseId(), caze.getCaseNumber(), caze.getCaseType(),
                caze.getCaseSubType(), offence.getCrestOffenceId(), defendant.getCrestDefendantId(), charge
                        .getChargeType(), charge.getCrestChargeId(), defendantOnOffence.getVcoDate(),
                defendantOnOffence.getVcoFlag());
    }
}
