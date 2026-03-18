package uk.gov.courtservice.xhibit.courtlog.endhearing;

import uk.gov.courtservice.xhibit.business.entities.xhb_hearing.XhbHearing;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearing;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing.HearingEndedAlreadyException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing.HearingEndedValidationException;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.exceptions.HearingAlreadyEndedException;
import uk.gov.courtservice.xhibit.courtlog.exceptions.FormAExportedException;
import uk.gov.courtservice.xhibit.courtlog.helpers.EntityHelper;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber;

/**
 * This is a category subscriber used for handling end hearing events
 * 
 * @author tz0d5m
 * @version $Revision: 1.13 $
 */
public class EndHearingSubscriber extends Subscriber {
    private static final String HEARING_ID_ATTRIBUTE = "EHS_HEARING_ID";

    private static final String SCHEDULED_HEARING_ID_ATTRIBUTE = "EHS_SCHEDULED_HEARING_ID";

    /**
     * Pre-creation logic for end hearing events, used to perform the required
     * business validation.
     * 
     * @param context
     *            The subscription context for shared state
     * @throws HearingAlreadyEndedException
     *             if the hearing for the event we are trying to create has
     *             already ended
     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber
     *      #preCreate(uk.gov.courtservice.xhibit.courtlog.OperationContext)
     */
    public void preCreate(OperationContext context) throws HearingAlreadyEndedException {
        log.debug("Start - preCreate for end hearing subscriber");

        final Integer hearingId = this.init(context);

        // now that the standard hearing details have been checked, ensure
        // that the defendants hearing has not already ended (if set).
        final Integer defendantOnCaseId = context.getCrudValue().getDefendantOnCaseId();
        EndHearingHelper.validateDefHearingNotEnded(defendantOnCaseId, hearingId);

        log.debug("End - preCreate for end hearing subscriber");
    }

    /**
     * Pre-deletion logic for end hearing events, used to perform the required
     * business validation.
     * 
     * @param context
     *            The subscription context for shared state
     * @throws HearingAlreadyEndedException
     *             if the hearing for the event we are trying to delete has
     *             already ended
     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber
     *      #preDelete(uk.gov.courtservice.xhibit.courtlog.OperationContext)
     */
    public void preDelete(OperationContext context) 
    throws FormAExportedException, HearingAlreadyEndedException {
        log.debug("Start - preDelete for end hearing subscriber");

        this.init(context, true);
        final Integer hearingId = (Integer) context.getAttribute(HEARING_ID_ATTRIBUTE);
        
        EndHearingHelper.validateFormANotExported(hearingId);

        log.debug("End - preDelete for end hearing subscriber");
    }

    /**
     * Pre-update logic for end hearing events, used to perform the required
     * business validation.
     * 
     * @param context
     *            The subscription context for shared state
     * @throws HearingAlreadyEndedException
     *             if the hearing for the event we are trying to update has
     *             already ended
     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber
     *      #preUpdate(uk.gov.courtservice.xhibit.courtlog.OperationContext)
     */
    public void preUpdate(OperationContext context) throws HearingAlreadyEndedException {
        log.debug("Start - preUpdate for end hearing subscriber");

        this.init(context);

        log.debug("End - preUpdate for end hearing subscriber");
    }

    /**
     * Post-creation logic for end hearing events
     * 
     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber
     *      #postCreate(uk.gov.courtservice.xhibit.courtlog.OperationContext)
     */
    public void postCreate(OperationContext context) throws CourtLogBusinessException {
        log.debug("Start - postCreate for end hearing subscriber");

        final Integer hearingId = (Integer) context.getAttribute(HEARING_ID_ATTRIBUTE);
        final Integer defendantOnCaseId = context.getCrudValue().getDefendantOnCaseId();
        final Integer scheduledHearingId = (Integer) context.getAttribute(SCHEDULED_HEARING_ID_ATTRIBUTE);
        final String userDisplayName = "XHIBIT";

        try {
            EndHearingHelper.getHearingScheduleController()
                    .endHearing(hearingId, scheduledHearingId, defendantOnCaseId, userDisplayName);
        } catch (HearingEndedAlreadyException e) {
            throw new HearingAlreadyEndedException(e);
        } catch (HearingEndedValidationException e) {
            throw new CourtLogBusinessException(e);
        }

        log.debug("End - postCreate for end hearing subscriber");
    }

    /**
     * Post-deletion logic for end hearing events, used to clear the start and
     * end hearing dates on the defHearingRecord for the defendant linked on the
     * passed in context (if there is one).
     * 
     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber
     *      #postDelete(uk.gov.courtservice.xhibit.courtlog.OperationContext)
     */
    public void postDelete(OperationContext context) throws CourtLogBusinessException {
        log.debug("Start - postDelete for end hearing subscriber");
        
        // sGet the user display name
        String userDisplayName = "XHIBIT";
        
        final Integer hearingId = (Integer) context.getAttribute(HEARING_ID_ATTRIBUTE);
        final Integer defendantOnCaseId = context.getCrudValue().getDefendantOnCaseId();
        final Integer scheduledHearingId = (Integer) context.getAttribute(SCHEDULED_HEARING_ID_ATTRIBUTE);

        try {
            EndHearingHelper.getHearingScheduleController()
                    .deleteEndHearing(hearingId, scheduledHearingId, defendantOnCaseId, userDisplayName);
        } catch (HearingEndedAlreadyException e) {
            throw new HearingAlreadyEndedException(e);
        } catch (HearingEndedValidationException e) {
            throw new CourtLogBusinessException(e);
        }

        EndHearingHelper.createDeleteEndHearingEvent(context);
        
        log.debug("End - postDelete for end hearing subscriber");
    }

    /**
     * Pre-creation logic for end hearing events of linked cases, performs the
     * same validation as the preCreate method
     * 
     * @param context
     *            The subscription context for shared state
     * @throws HearingAlreadyEndedException
     *             if the hearing for the event we are trying to create has
     *             already ended
     * @see #preCreate(uk.gov.courtservice.xhibit.courtlog.OperationContext)
     */
    public void preCreateLinked(OperationContext context) throws HearingAlreadyEndedException {
        log.debug("Start - preCreateLinked for end hearing subscriber");

        // perform the same logic as if this was a straight create...
        preCreate(context);

        log.debug("End - preCreateLinked for end hearing subscriber");
    }

    /**
     * Post-creation logic for end hearing events of linked cases, performs the
     * same logic as the postCreate method
     * 
     * @param context
     *            The subscription context for shared state
     * @see #postCreate(uk.gov.courtservice.xhibit.courtlog.OperationContext)
     */
    public void postCreateLinked(OperationContext context) throws CourtLogBusinessException {
        log.debug("Start - postCreateLinked for end hearing subscriber");

        // perform the same logic as if this was a straight create...
        postCreate(context);

        log.debug("End - postCreateLinked for end hearing subscriber");
    }

    /**
     * Initialisation method for the EndHearingSubscriber. This will perform the
     * lookups required for the pre/post methods and store the values (for
     * example the hearing) to the context. General validation (whether the
     * hearing has already ended) is also performed here.
     * 
     * @param context
     *            The context values can be acquired from, and saved to
     * @return The <code>Integer</code> primary key of the hearing used to set
     *         the properties to on the context.
     * @throws HearingAlreadyEndedException
     *             if the hearing has already ended, as no processing is allowed
     *             to an end hearing event if the hearing has already ended.
     */
    private Integer init(OperationContext context) 
    throws HearingAlreadyEndedException {
        return init(context, false);
    }
    
    private Integer init(OperationContext context, boolean deleteEndHearing)
    throws HearingAlreadyEndedException {
        final Integer scheduledHearingId = EndHearingHelper.getScheduledHearingId(context.getCrudValue());

        // attempt to lookup the hearing, and add it to the context
        final XhbScheduledHearing xhbScheduledHearing = EntityHelper.getXhbScheduledHearing(scheduledHearingId);
        final XhbHearing xhbHearing = xhbScheduledHearing.getXhbHearing();

        if (!deleteEndHearing) {
            EndHearingHelper.validateHearingNotEnded(xhbHearing);
        }

        // now add all of our looked up values to the context...
        context.putAttribute(HEARING_ID_ATTRIBUTE, xhbHearing.getHearingId());
        context.putAttribute(SCHEDULED_HEARING_ID_ATTRIBUTE, scheduledHearingId);

        return xhbHearing.getHearingId();
    }
}
