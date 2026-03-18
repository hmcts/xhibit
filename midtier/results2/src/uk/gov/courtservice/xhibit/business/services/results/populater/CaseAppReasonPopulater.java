package uk.gov.courtservice.xhibit.business.services.results.populater;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case_app_reason.XhbCaseAppReasonBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_case_app_reason.XhbCaseAppReasonBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.common.results.vos.CaseAppReasonValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;

/**
 * <p>
 * Title: CaseAppReasonPopulater
 * </p>
 * <p>
 * Description: Populates ResultsCompositeValue with CaseAppReasons Ensures
 * related(per offence) and unrelated Disposals are set
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.5 $
 */
public class CaseAppReasonPopulater extends AbstractResultsPopulater {
    private static final Logger log = CSServices.getLogger(CaseAppReasonPopulater.class);

    public void populate(ResultsCompositeValue rcv) throws ResultsControllerException {
        log.debug("populate() - START");
        populateCaseAppReasons(rcv);
        log.debug("populate() - END");
    }

    // Get the CaseAppReason for the case
    private void populateCaseAppReasons(ResultsCompositeValue rcv) {
        XhbCaseAppReasonBasicValue[] caseAppReasonBasicValues = XhbCaseAppReasonBeanHelper2
                .findCurrentByCaseIdValue(rcv.getCaseId());

        for (int i = 0; i < caseAppReasonBasicValues.length; i++) {
            rcv.addCaseAppReasonValue(new CaseAppReasonValue(caseAppReasonBasicValues[i]));
        }
    }
}
