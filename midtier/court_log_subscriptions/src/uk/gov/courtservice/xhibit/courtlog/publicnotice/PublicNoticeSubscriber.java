package uk.gov.courtservice.xhibit.courtlog.publicnotice;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeWorkFlow;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * @author pznwc5
 * 
 * Subscriber used for sending JMS messages to public display
 */
public class PublicNoticeSubscriber extends Subscriber {

    /** Logger */
    private static final Logger LOG = Logger.getLogger(PublicNoticeSubscriber.class);

    /**
     * Logic after a public display event is created
     */
    public void postCreate(OperationContext ctx) {
        try {
            // Valid only for in court
            if (!ctx.getCrudValue().isInCourt())
                return;
            CourtLogSubscriptionValue subVal = ctx.getNewSubscriptionValues()[0];
            PublicNoticeWorkFlow.setPublicNoticeforCourtRoom(subVal, null);
            LOG.debug("Message sent in post create");
        } catch (Throwable th) {
            LOG.fatal(th.getMessage(), th);
        }
    }

    /**
     * Logic after a public display event is created
     */
    public void postUpdate(OperationContext ctx) {
        try {
            // Valid only for in court
            if (!ctx.getCrudValue().isInCourt())
                return;
            CourtLogSubscriptionValue subVal = ctx.getNewSubscriptionValues()[0];
            PublicNoticeWorkFlow.setPublicNoticeforCourtRoom(subVal, null);
            LOG.debug("Message sent in post update");
        } catch (Throwable th) {
            LOG.fatal(th.getMessage(), th);
        }
    }
}