package uk.gov.courtservice.xhibit.courtlog.witness;

import java.util.Date;
import java.util.Map;

import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.exceptions.WitnessReleasedException;
import uk.gov.courtservice.xhibit.courtlog.exceptions.WitnessSwornException;
import uk.gov.courtservice.xhibit.courtlog.helpers.xml.CourtLogXmlHelper;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * This is a category subscriber used for handling witness released events
 * 
 * @author tz0d5m
 * @version $Revision: 1.14 $
 */
public class WitnessReleasedSubscriber extends Subscriber {
    /**
     * Pre-update logic for witness released events, ensure that the new value
     * contains all of the old properties if not overwritten by the new value
     * 
     * @param context
     *            The subscription context for shared state
     * @throws CourtLogBusinessException
     *             if there is a problem acquiring the original court log entry
     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber
     *      #preUpdate(uk.gov.courtservice.xhibit.courtlog.OperationContext)
     */
    public void preUpdate(OperationContext context) throws CourtLogBusinessException {
        log.debug("Start - preUpdate for witness released subscriber");

        Integer eventId = context.getCrudValue().getEventType();

        if (WitnessHelper.isTrialWitnessEvent(eventId)) {
            log.debug("preUpdate - trial witness event type");
            WitnessHelper.checkTrialWitnessEventsForUpdate(context.getCrudValue());
        } else if (WitnessHelper.isAppealWitnessEvent(eventId)) {
            log.debug("preUpdate - appeal witness event type");
            WitnessHelper.checkAppealWitnessEventsForUpdate(context.getCrudValue());
        } else {
            log.debug("preUpdate - neither appeal or trail event type");
        }

        // X55241, if it is a witness released event, then we will need to
        // ensure that the properties are still populated
        final CourtLogViewValue original = context.getOriginalViewValue();

        // ensure we copy the new properties (if set) over the old ones
        final Map originalProperties = CourtLogXmlHelper.getPropertySet(original.getLogEntry());
        originalProperties.putAll(context.getCrudValue().getPropertyMap());
        context.getCrudValue().setPropertyMap(originalProperties);

        log.debug("End - preUpdate for witness released subscriber");
    }

    /**
     * Method to perform pre-creation validation logic.
     * 
     * @param context
     *            The subscription context for shared state
     * @throws WitnessReleasedException
     *             if there was a problem releasing a witness
     * @throws WitnessSwornException
     *             if there was a problem swearing in a witness
     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber
     *      #preCreate(uk.gov.courtservice.xhibit.courtlog.OperationContext)
     */
    public void preCreate(OperationContext context) throws WitnessReleasedException, WitnessSwornException {
        log.debug("Start - preCreate for witness released subscriber");

        Integer eventId = context.getCrudValue().getEventType();

        if (WitnessHelper.isTrialWitnessEvent(eventId)) {
            log.debug("preCreate - trial witness event type");
            WitnessHelper.checkTrialWitnessEvents(context.getCrudValue());
        } else if (WitnessHelper.isAppealWitnessEvent(eventId)) {
            log.debug("preCreate - appeal witness event type");
            WitnessHelper.checkAppealWitnessEvents(context.getCrudValue());
        } else {
            log.debug("preCreate - neither appeal or trail event type");
        }

        log.debug("End - preCreate for witness released subscriber");
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
        log.debug("Start - preDelete for witness released subscriber");

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

        log.debug("End - preDelete for witness released subscriber");
    }

    /**
     * Post deletion logic for when a witness has been released. This is
     * required to update all necessary released information for a witness if
     * one has been created.
     * 
     * @throws CourtLogBusinessException
     *             if there is a problem acquiring the original court log entry
     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber
     *      #postDelete(uk.gov.courtservice.xhibit.courtlog.OperationContext)
     */
    public void postDelete(OperationContext context) throws CourtLogBusinessException {
        log.debug("Start - postDelete for witness released subscriber");

        final Integer caseId = context.getOriginalViewValue().getCaseId();
        final Integer eventType = context.getOriginalViewValue().getEventType();
        final Date entryDate = context.getOriginalViewValue().getEntryDate();

        Integer witnessId = WitnessHelper.getLastWitnessId(eventType, caseId, entryDate);

        if (witnessId != null) {
            // if the witness id is not -1 find and update the witness
            // entity
            WitnessHelper.removeReleaseInfo(witnessId);
        }

        log.debug("End - postDelete for witness released subscriber");
    }

    /**
     * Calculates witness time.
     * 
     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber
     *      #postCreate(uk.gov.courtservice.xhibit.courtlog.OperationContext)
     */
    public void postCreate(OperationContext context) {
        log.debug("Start - postCreate for witness released subscriber");

        final CourtLogViewValue[] newViewValues = context.getNewViewValues();

        for (int i = 0; i < newViewValues.length; i++) {
            // first get the last witness sworn event...
            Integer witnessId = WitnessHelper.getLastWitnessId(newViewValues[i].getEventType(), newViewValues[i]
                    .getCaseId(), newViewValues[i].getEntryDate());

            if (witnessId != null) {
                WitnessHelper.releaseWitness(newViewValues[i].getEntryDate(), newViewValues[i].getCaseId(), witnessId);
            }
        }

        log.debug("End - postCreate for witness released subscriber");
    }
}
