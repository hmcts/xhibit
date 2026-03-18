package uk.gov.courtservice.xhibit.courtlog.endhearing;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Iterator;
import uk.gov.courtservice.xhibit.business.entities.xhb_exporta.XhbExportaBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_exporta.XhbExporta;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_def_hearing_record.XhbDefHearingRecord;
import uk.gov.courtservice.xhibit.business.entities.xhb_def_hearing_record.XhbDefHearingRecordBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_def_hearing_record.XhbDefHearingRecordBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendant;
import uk.gov.courtservice.xhibit.business.entities.xhb_hearing.XhbHearing;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerLocal;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing.EndHearingConstants;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing.HearingEndedValidationException;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.exceptions.HearingAlreadyEndedException;
import uk.gov.courtservice.xhibit.courtlog.exceptions.FormAExportedException;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordConstants;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * Helper class for the end hearing subscriber.
 * 
 * @author tz0d5m
 * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.category
 *      .endhearing.EndHearingSubscriber
 */
public class EndHearingHelper {
    private static final Logger LOG = CSServices.getLogger(EndHearingHelper.class);

    private static final Integer END_HEARING_CASE_LEVEL = new Integer(30500);
    private static final Integer END_HEARING_DEFENDANT_LEVEL = new Integer(30600);
    private static final Integer DELETE_END_HEARING_CASE_LEVEL = new Integer(30501);
    private static final Integer DELETE_END_HEARING_DEFENDANT_LEVEL = new Integer(30601);
    
    /**
     * Property name for scheduled hearing id, used when reading from the crud
     * value
     */
    public static final String SCHEDULED_HEARING_ID_PROPERTY = "scheduled_hearing_id";

    /**
     * Single instance of the hearing schedule controller, MUST be accessed via
     * the getHearingScheduleController(), as lazily instantiated.
     * 
     * @see #getHearingScheduleController()
     */
    private static HearingScheduleControllerLocal hearingScheduledController = null;
    
    /**
     * Private constructor to prevent external instantiation
     */
    private EndHearingHelper() {
        // prevent external instantiation...
    }

    /**
     * Accessor for the single instance of the
     * <code>HearingScheduleController</code>, lazily instantiated, so must
     * be accessed via this method, and not directly.
     * 
     * @return The single, static instance of the
     *         <code>HearingScheduleController</code>
     */
    public static HearingScheduleControllerLocal getHearingScheduleController() {
        if (hearingScheduledController == null) {
            hearingScheduledController = (HearingScheduleControllerLocal) CSServices.getEJBServices()
                    .createLocalSession(HearingScheduleControllerLocalHome.class);
        }

        return hearingScheduledController;
    }

    /**
     * Return true if the status flag indicates the hearing has been exported.
     * This method is intended to be 'safe' in the respect that it will return
     * true even if the export proceses is currently 
     * running (HearingRecordConstants.IN_PROGRESS).
     * @param exportStatus
     * @return
     */
    private static boolean exported(String exportStatus) {
        return exportStatus != null 
        && !exportStatus.equals(HearingRecordConstants.EXPORT_FAILED);
    }
    
    /**
     * CCN1332 Deleting an end of hearing event should generate a 
     * new DeleteEndHearing event
     * @param context
     */
    public static void createDeleteEndHearingEvent(OperationContext context)
    throws CourtLogBusinessException {
        CourtLogCRUDValue logEntry = new CourtLogCRUDValue();

        if (context.getCrudValue().getEventType().equals(END_HEARING_CASE_LEVEL)) {
            logEntry.setEventType(DELETE_END_HEARING_CASE_LEVEL);
        } else if (context.getCrudValue().getEventType().equals(END_HEARING_DEFENDANT_LEVEL)) {
            logEntry.setEventType(DELETE_END_HEARING_DEFENDANT_LEVEL);
        } else {
            LOG.error("Invalid event type given to createDeleteEndHearingEvent");
            throw new CSUnrecoverableException("Invalid event type given to createDeleteEndHearingEvent");
        }
        
        logEntry.setEntryFreeText("");
        logEntry.setInCourt(context.getCrudValue().isInCourt());
        logEntry.setCaseId(context.getCrudValue().getCaseId());
        logEntry.setEntryDate(context.getCrudValue().getEntryDate());
        logEntry.setPropertyMap(context.getCrudValue().getPropertyMap());
        logEntry.setProcessLinkedCases(context.getCrudValue().processLinkedCases());
        logEntry.setDefendantOnCaseId(context.getCrudValue().getDefendantOnCaseId());
        logEntry.setScheduledHearingId(context.getCrudValue().getScheduledHearingId());
        
        CourtLogWorkFlow.newEntry(logEntry);
        
        LOG.debug("createDeleteEndHearingEvent() end, returning : " + logEntry);
    }
    
    /**
     * An end hearing event should not be deleted after the export form A
     * operation has been performed.
     * @param hearingId
     */
    public static void validateFormANotExported(Integer hearingId)
    throws FormAExportedException {
        Collection hearings = XhbExportaBeanHelper2.findByHearingId(hearingId);
        Iterator iterator = hearings.iterator();
        while (iterator.hasNext()) {
            XhbExporta exportA = (XhbExporta)iterator.next();
            String statusFlag = exportA.getStatusFlag();
            if (exported(statusFlag)) {
                throw new FormAExportedException(
                        EndHearingConstants.CANNOT_DELETE_END_HEARING_AFTER_FORM_A_EXPORT,
                        "Cannot delete an end of hearing event after form A export");
            }
        }
    }
    
    /**
     * Utility method used to determine if the hearing is valid for ending, this
     * will see if the hearing has already ended, and if it has, the exception
     * will be thrown.
     * 
     * @param hearing
     *            The hearing to check
     * @throws HearingAlreadyEndedException
     *             If the hearing has already ended
     */
    public static void validateHearingNotEnded(XhbHearing hearing) throws HearingAlreadyEndedException {
        if (hearing.isHearingEnded()) {
            throw new HearingAlreadyEndedException(EndHearingConstants.HEARING_ALREADY_ENDED,
                    "Cannot modify a hearing if it has already been ended");
        }
    }

    /**
     * Utility method used to determine if the def hearing is valid for ending,
     * this will see if the def hearing has already ended, and if it has, the
     * exception will be thrown.
     * 
     * @param defendantOnCaseId
     *            The primary key of the defendant on case we want to find the
     *            def hearing record for.
     * @param hearingId
     *            The primary key of the hearing.
     * @throws HearingAlreadyEndedException
     *             If the def hearing has already ended
     */
    public static void validateDefHearingNotEnded(Integer defendantOnCaseId, Integer hearingId)
            throws HearingAlreadyEndedException {
        if (defendantOnCaseId != null) {
            try {
                final XhbDefHearingRecord defHearingRecord = XhbDefHearingRecordBeanHelper2.findByDefOnCaseAndHearing(
                        defendantOnCaseId, hearingId);

                final boolean defHearingEnded = defHearingRecord.isDefHearingEnded();
                LOG.debug("defHearingRecord[" + defHearingRecord.getHearingRecordId() + "] ended = " + defHearingEnded);
                if (defHearingEnded) {
                    final Object[] parameters = new Object[] { getDefendantName(defHearingRecord
                            .getXhbDefendantOnCase().getXhbDefendant()) };

                    throw new HearingAlreadyEndedException(EndHearingConstants.DEFENDANT_HEARING_ALREADY_ENDED,
                            parameters, "Cannot modify a def hearing if it has already been ended");
                }
            } catch (XhbDefHearingRecordBeanNotFoundException e) {
                // this is possible, and the record will get created later,
                // so ignore...
            }
        }
    }

    /**
     * Helper method used to acquire the scheduled hearing id for the passed in
     * CRUD value. Originally this was passed via a property on the map, but it
     * has changed to be sent as a variable on the crud instead. Acquire the
     * scheduled hearing id from the variable if set, otherwise, use the old
     * method and read it from the property map.
     * 
     * @param clcv
     *            The <code>CourtLogCRUDValue</code> to get the scheduled
     *            hearing id from.
     * @return The acquired scheduled hearing id
     */
    public static Integer getScheduledHearingId(CourtLogCRUDValue clcv) {
        final Integer scheduledHearingId;

        if (clcv.getScheduledHearingId() != null) {
            scheduledHearingId = clcv.getScheduledHearingId();
        } else {
            scheduledHearingId = convertToInteger(clcv.getProperty(SCHEDULED_HEARING_ID_PROPERTY));
        }

        LOG.debug("getScheduledHearingId() - Returning " + scheduledHearingId);
        return scheduledHearingId;
    }

    /**
     * Utility method to acquire the defendants name using the passed in
     * defendat on case id
     * 
     * @param defendantOnCaseId
     * @return The formatted, defendants name
     * @throws HearingEndedValidationException
     */
    private static String getDefendantName(XhbDefendant defendant) {
        final StringBuffer name = new StringBuffer();

        // add firstname and any formatting
        if (defendant.getFirstName() != null) {
            name.append(defendant.getFirstName());
        }

        // add the middle name
        if (defendant.getMiddleName() != null) {
            if (name.length() > 0) {
                name.append(' ');
            }

            name.append(defendant.getMiddleName());
        }

        // add the surname
        if (defendant.getSurname() != null) {
            if (name.length() > 0) {
                name.append(' ');
            }

            name.append(defendant.getSurname());
        }

        return name.toString();
    }

    /**
     * Convert the passed in <code>Object</code> to an <code>Integer</code>.
     * 
     * @param input
     *            The <code>Object</code> to convert, this is done by passing
     *            the result of toString to the constructor for
     *            <code>Integer</code>.
     * @return The converted <code>Object</code> as a <code>Integer</code>
     */
    private static Integer convertToInteger(Object input) {
        LOG.debug("convertToInteger() - Trying to convert [" + input + "]");
        return ((input != null) ? new Integer(input.toString()) : null);
    }
}
