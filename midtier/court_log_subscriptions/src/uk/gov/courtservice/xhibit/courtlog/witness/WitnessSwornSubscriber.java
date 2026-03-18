package uk.gov.courtservice.xhibit.courtlog.witness;

import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.exceptions.WitnessReleasedException;
import uk.gov.courtservice.xhibit.courtlog.exceptions.WitnessSwornException;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber;

/**
 * This is a category subscriber used for handling witness sworn events
 * 
 * @author tz0d5m
 * @version $Revision: 1.9 $
 */
public class WitnessSwornSubscriber extends Subscriber {
    /**
     * Pre-creation logic for witness sworn events, used to perform the required
     * business validation.
     * 
     * @param context
     *            The subscription context for shared state
     * @throws CourtLogBusinessException
     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber
     *      #preCreate(uk.gov.courtservice.xhibit.courtlog.OperationContext)
     */
    public void preCreate(OperationContext context) throws CourtLogBusinessException {
        log.debug("Start - preCreate for witness sworn subscriber");
        Integer eventId = context.getCrudValue().getEventType();

        if (WitnessHelper.isTrialWitnessEvent(eventId)) {
            log.debug("preCreate - trial witness event type");
            // X55121 - ensure that the witness id node is populated
            WitnessHelper.validateTrialWitnessId(context.getCrudValue());
            WitnessHelper.checkTrialWitnessEvents(context.getCrudValue());
        } else if (WitnessHelper.isAppealWitnessEvent(eventId)) {
            log.debug("preCreate - appeal witness event type");
            // X55121 - ensure that the witness id node is populated
            WitnessHelper.validateAppealWitnessId(context.getCrudValue());
            WitnessHelper.checkAppealWitnessEvents(context.getCrudValue());
        } else {
            log.debug("preCreate - neither appeal or trail event type");
        }

        log.debug("End - preCreate for witness sworn subscriber");
    }

    /**
     * Pre-update logic for witness sworn events, used to perform the required
     * business validation.
     * 
     * @param context
     *            The subscription context for shared state
     * @throws CourtLogBusinessException
     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber
     *      #preCreate(uk.gov.courtservice.xhibit.courtlog.OperationContext)
     */
    public void preUpdate(OperationContext context) throws CourtLogBusinessException {
        log.debug("Start - preUpdate for witness sworn subscriber");
        Integer eventId = context.getCrudValue().getEventType();

        if (WitnessHelper.isTrialWitnessEvent(eventId)) {
            log.debug("preUpdate - trial witness event type");
            WitnessHelper.validateTrialWitnessId(context.getCrudValue());
            WitnessHelper.checkTrialWitnessEventsForUpdate(context.getCrudValue());
        } else if (WitnessHelper.isAppealWitnessEvent(eventId)) {
            log.debug("preUpdate - appeal witness event type");
            WitnessHelper.validateAppealWitnessId(context.getCrudValue());
            WitnessHelper.checkAppealWitnessEventsForUpdate(context.getCrudValue());
        } else {
            log.debug("preUpdate - neither appeal or trail event type");
        }

        log.debug("End - preUpdate for witness sworn subscriber");
    }

    /**
     * Method to perform pre-delete validation logic.
     * 
     * @param context
     *            The subscription context for shared state
     * @throws WitnessReleasedException
     *             if there was a problem releasing a witness
     * @throws WitnessSwornException
     *             if there was a problem swearing in a witness
     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber
     *      #preDelete(uk.gov.courtservice.xhibit.courtlog.OperationContext)
     */
    public void preDelete(OperationContext context) throws WitnessReleasedException, WitnessSwornException {
        log.debug("Start - preDelete for witness sworn subscriber");

        Integer eventId = context.getCrudValue().getEventType();

        if (WitnessHelper.isTrialWitnessEvent(eventId)) {
            log.debug("preDelete - trial witness event type");
            WitnessHelper.checkTrialWitnessEventsForDeletion(context.getCrudValue());
        } else if (WitnessHelper.isAppealWitnessEvent(eventId)) {
            log.debug("preDelete - appeal witness event type");
            WitnessHelper.checkAppealWitnessEventsForDeletion(context.getCrudValue());
        } else {
            log.debug("preDelete - neither appeal or trail event type");
        }

        log.debug("End - preDelete for witness sworn subscriber");
    }
}
