package uk.gov.courtservice.xhibit.courtlog.probation;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * @author pznwc5
 * 
 * Court log logic for probation service
 */
public class ProbationServiceSubscriber extends Subscriber {

    /** Logger */
    private static final Logger LOG = Logger.getLogger(ProbationServiceSubscriber.class);

    /**
     * Do post create processing for CJSE
     */
    public void postCreate(OperationContext ctx) throws CourtLogBusinessException {

        CourtLogSubscriptionValue subVals[] = ctx.getNewSubscriptionValues();
        ProbationCourtLogHelper helper = new ProbationCourtLogHelper();

        for (int i = 0; i < subVals.length; i++) {
            try {
                helper.processRequest(subVals[i]);
            } catch (Throwable e) {
                // We don't want this to abort court log
                LOG.fatal(e.getMessage(), e);
            }
        }
    }

}
