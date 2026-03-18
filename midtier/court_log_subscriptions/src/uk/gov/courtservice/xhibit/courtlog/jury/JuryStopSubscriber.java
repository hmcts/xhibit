package uk.gov.courtservice.xhibit.courtlog.jury;

import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber;

/**
 * <p>
 * Title: JuryStopSubscriber
 * </p>
 * <p>
 * Description: This is the subscriber for Jury_Stop events
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Stephen Tully
 * @version $Revision: 1.4 $
 */
public class JuryStopSubscriber extends Subscriber {
    /**
     * Post create processing to re-calculate the jury time out
     * 
     * @param context
     * @throws CourtLogBusinessException
     */
    public void postCreate(OperationContext context) throws CourtLogBusinessException {
        log.debug("Start - postCreate(OperationContext) for jury stop subscriber");

        JuryEventsHelper.genericPostProcessing(context, context.getCrudValue().getEntryDate(),
                "courtlog.category.stopJuryOutTime");

        log.debug("End - postCreate(OperationContext) for jury stop subscriber");
    }

    /**
     * Post update logic to re-calculate the jury time out
     * 
     * @param context
     * @throws CourtLogBusinessException
     */
    public void postUpdate(OperationContext context) throws CourtLogBusinessException {
        log.debug("Start - postUpdate(OperationContext) for jury stop subscriber");

        JuryEventsHelper.genericPostProcessing(context, context.getCrudValue().getEntryDate(),
                "courtlog.category.stopJuryOutTime");

        log.debug("End - postUpdate(OperationContext) for jury stop subscriber");
    }

    /**
     * Post delete logic to re-calculate the jury time out
     * 
     * @param context
     * @throws CourtLogBusinessException
     */
    public void postDelete(OperationContext context) throws CourtLogBusinessException {
        log.debug("Start - postDelete(OperationContext) for jury stop subscriber");

        JuryEventsHelper.deletePostProcessing(context, context.getCrudValue().getEntryDate(),
                "courtlog.category.startJuryOutTime");

        log.debug("End - postDelete(OperationContext) for jury stop subscriber");
    }
}
