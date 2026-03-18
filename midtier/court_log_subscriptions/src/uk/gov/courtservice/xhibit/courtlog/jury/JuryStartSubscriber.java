package uk.gov.courtservice.xhibit.courtlog.jury;

import java.util.Date;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: JuryStartSubscriber
 * </p>
 * <p>
 * Description: This is the subscriber for Jury_Start events
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
public class JuryStartSubscriber extends Subscriber {
    /**
     * Post create processing to re-calculate the jury time out
     * 
     * @param context
     * @throws CourtLogBusinessException
     */
    public void postCreate(OperationContext context) throws CourtLogBusinessException {
        log.debug("Start - postCreate(OperationContext) for jury start subscriber");

        JuryEventsHelper.genericPostProcessing(context, context.getCrudValue().getEntryDate(),
                "courtlog.category.stopJuryOutTime");

        log.debug("End - postCreate(OperationContext) for jury start subscriber");
    }

    /**
     * Post update logic to re-calculate the jury time out
     * 
     * @param context
     * @throws CourtLogBusinessException
     */
    public void postUpdate(OperationContext context) throws CourtLogBusinessException {
        log.debug("Start - postUpdate(OperationContext) for jury start subscriber");

        // Find the least value for entryDate for the crudValue and the
        // originalEntry
        CourtLogCRUDValue crudValue = context.getCrudValue();
        XhbCourtLogEntryBasicValue originalEntry = context.getOriginalBasicValue();
        long startDate = Math.min(crudValue.getEntryDate().getTime(), originalEntry.getDateTime().getTime());

        JuryEventsHelper.genericPostProcessing(context, new Date(startDate), "courtlog.category.stopJuryOutTime");

        log.debug("End - postUpdate(OperationContext) for jury start subscriber");
    }

    /**
     * Post delete logic to re-calculate the jury time out
     * 
     * @param context
     * @throws CaseNotFoundException
     * @throws CourtLogBusinessException
     */
    public void postDelete(OperationContext context) throws CourtLogBusinessException {
        log.debug("Start - postDelete(OperationContext) for jury start subscriber");

        JuryEventsHelper.genericPostProcessing(context, context.getCrudValue().getEntryDate(),
                "courtlog.category.stopJuryOutTime");

        log.debug("End - postDelete(OperationContext) for jury start subscriber");
    }
}
