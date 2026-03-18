package uk.gov.courtservice.xhibit.courtlog.adjournment;

import java.util.Collection;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.exceptions.NoCaseCalledOnEventException;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * @author pznwc5
 * 
 * This is a category subscriber used for handling reporting restrictions
 */
public class AdjournmentSubscriber extends Subscriber {
    /** Case called on event */
    private static final String CASE_CALLED_ON = "Case_Called_On";

    /**
     * Send a message to CJSE after the event has been created
     * 
     * @param context
     *            Subscription context for shared state
     */
    public void preCreate(OperationContext context) throws NoCaseCalledOnEventException {
        log.debug("Start - pre create for adjourment subscriber");
        CourtLogCRUDValue logValue = context.getCrudValue();
        checkValidAdjournment(logValue.getCaseId());
        log.debug("End - pre create for adjourment subscriber");
    }

    /**
     * Checks whether the adjournment is valid
     * 
     * @param caseId
     *            Case Id
     * @throws NoCaseCalledOnEventException
     */
    private void checkValidAdjournment(Integer caseId) throws NoCaseCalledOnEventException {
        log.debug("Start - checkValidAdjournment() start caseId = " + caseId);

        Collection caseCalledOnEvents = XhbCourtLogEntryBeanHelper2.findByCaseIdEventDesc(caseId, CASE_CALLED_ON);

        if (caseCalledOnEvents.size() == 0) {
            throw new NoCaseCalledOnEventException(caseId);
        }

        log.debug("End - checkValidAdjournment() start caseId = " + caseId);
    }
}
