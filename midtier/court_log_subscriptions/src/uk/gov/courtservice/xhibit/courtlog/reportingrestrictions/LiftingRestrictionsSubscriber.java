package uk.gov.courtservice.xhibit.courtlog.reportingrestrictions;

import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * @author pznwc5
 * 
 * This is a category subscriber used for handling reporting restrictions
 */
public class LiftingRestrictionsSubscriber extends Subscriber {
    /**
     * Send a message to CJSE after the event has been created
     * 
     * @param context
     *            Subscription context for shared state
     */
    public void preCreate(OperationContext context) {
        log.debug("Start - pre create for reporting restrictions subscriber");
        CourtLogCRUDValue crudValue = context.getCrudValue();
        ReportingRestrictionsHelper.liftRestrictions(crudValue.getCaseId());
        log.debug("End - pre create for reporting restrictions subscriber");
    }
}
