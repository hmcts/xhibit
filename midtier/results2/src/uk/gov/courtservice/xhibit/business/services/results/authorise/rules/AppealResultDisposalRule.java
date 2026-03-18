package uk.gov.courtservice.xhibit.business.services.results.authorise.rules;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.results.Results2WorkFlow;
import uk.gov.courtservice.xhibit.business.services.results.authorise.DefendantRule;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.DisposalAppealResultFailureMessage;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.FailureMessage;

/**
 * <p>
 * Title: Verifies that an appeal result is recorded for each Magistrate Court
 * General Disposal.
 * </p>
 * <p>
 * Description: Validates if an appeal result is corrently recorded for the
 * Magistrates Court General Disposal. Business Rule RESULT61
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author Simon Gilmore
 * @version $Id: AppealResultDisposalRule.java,v 1.1 2005/01/28 08:44:58 rzvddy
 *          Exp $
 */

public class AppealResultDisposalRule implements DefendantRule {
    private static final Logger log = CSServices.getLogger(AppealResultDisposalRule.class);

    private static AppealResultDisposalRule instance = null;

    private AppealResultDisposalRule() {
    }

    public static AppealResultDisposalRule getInstance() {
        if (instance == null) {
            instance = new AppealResultDisposalRule();
        }
        return instance;
    }

    public FailureMessage[] process(ResultsCompositeValue rcv, Integer defendantOnCaseId) {
        log.debug("process - BEGIN");

        final List failures = new ArrayList();

        for (int i = 0; i < rcv.getUnrelatedDisposalCount(defendantOnCaseId); i++) {
            DisposalValue disposalValue = rcv.getUnrelatedDisposal(defendantOnCaseId, i);
            if (disposalValue.isMagistrateGeneralDisposal()) {
                VerdictValue verdictValue = rcv.getVerdictForDisposal(disposalValue.getDisposal2Id());
                if (verdictValue == null) {
                    log.debug("process - appeal result not found for disposal.");

                    String disposalDetail = getDisposalDetail(rcv.getChargeCompositeValue().getCaseBasicValue()
                            .getCourtId(), disposalValue);
                    DisposalAppealResultFailureMessage message = new DisposalAppealResultFailureMessage(
                            "authorise.appealresultdisposal.notrecorded", new Object[] { disposalDetail });

                    // return new String[] {
                    // "authorise.appealresultdisposal.notrecorded" };
                    failures.add(message);
                }
            }
        }
        return (FailureMessage[]) failures.toArray(new FailureMessage[failures.size()]);
    }

    private String getDisposalDetail(Integer courtId, DisposalValue disposalValue) {
        log.debug("getDisposalDetail - Begin: courtId = " + courtId);

        DisposalReferenceValue drv = Results2WorkFlow.getReferenceDisposal(new Integer(disposalValue
                .getRefDisposalTypeId()));

        final String disposalDetail = drv.getResultSheetText(disposalValue);
        log.debug("getDisposalDetail - End: disposalDetail = " + disposalDetail);
        return disposalDetail;
    }
}