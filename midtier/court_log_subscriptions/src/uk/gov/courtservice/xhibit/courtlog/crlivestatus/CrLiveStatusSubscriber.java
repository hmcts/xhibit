package uk.gov.courtservice.xhibit.courtlog.crlivestatus;

import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber;
import uk.gov.courtservice.xhibit.crlivestatus.CrLiveStatusHelper;

/**
 * Subscriber to update CR live status for the internet status
 * 
 * @author pznwc5
 * @version $Revision: 1.10 $
 */
public class CrLiveStatusSubscriber extends Subscriber {
    /**
     * Court log entry is created, update cr live status
     */
    public void postCreate(OperationContext ctx) {
        CrLiveStatusHelper.updateInternetStatus(ctx.getNewViewValues()[0]);
    }

    /**
     * Court log entry is updated, update cr live status
     */
    public void postUpdate(OperationContext ctx) {
        log.debug("postUpdate(): start");
        CrLiveStatusHelper.updateInternetStatus(ctx.getOriginalViewValue());
        log.debug("postUpdate(): end");
    }
}
