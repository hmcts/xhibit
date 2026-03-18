package uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearingBeanHelper2;
import uk.gov.courtservice.xhibit.courtlog.CourtLogCategoryDescription;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * <p>
 * Description: Calculate times for Jury out and Hearings/cases.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Joseph Babad
 * @version $Revision: 1.5 $
 */
public class TimeCalculationHelper {
    private static final Logger LOG = CSServices.getLogger(TimeCalculationHelper.class);

    private static final String DOC_ID_XPATH = "event/Listed_Def_On_Case_Ids/Def_On_Case_Id/doc_id/text()";

    private TimeCalculationHelper() {
    }

    /**
     * Calculate the total time that a scheduled hearing has taken.
     * 
     * @param shId
     *            scheduled hearing Id
     * @param docId
     *            the defendant on case id for the defendant we are calculating
     *            the time for
     * @return totalTime The total time that a scheduled hearing has taken.
     * @throws HearingEndedDurationCalculationException
     */
    public static long calculateScheduledHearingTime(final Integer shId, final Integer docId)
            throws HearingEndedDurationCalculationException {
        String methodName = "calculateScheduledHearingTime() - ";
        long totalTime = 0;

        LOG.debug(methodName + "scheduledHearingId: shId " + shId + " docId " + docId);

        XhbScheduledHearing schedHearing = XhbScheduledHearingBeanHelper2.findByPrimaryKey(shId);

        if (schedHearing.getOriginalTime() == null) {
            // @todo - this really shouldn't be this exception...
            HearingEndedDurationCalculationException cle = new HearingEndedDurationCalculationException(
                    "Court_Log.Error_Calculating_Scheduled_Hearing_Time", "No originalTime for Scheduled Hearing");
            CSServices.getDefaultErrorHandler().handleError(cle, TimeCalculationHelper.class, cle.toString());
            throw cle;
        }

        // Define date of sched hearing...
        Date date = DateTimeUtilities.stripTimeToUtilDate(schedHearing.getOriginalTime());

        Calendar endTime = Calendar.getInstance();
        endTime.setTime(date);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59);

        final String start = CourtLogCategoryDescription.SCHEDULED_HEARING_TIME_START_CATEGORY_DESC;
        final String stop = CourtLogCategoryDescription.SCHEDULED_HEARING_TIME_STOP_CATEGORY_DESC;
        final String endHearing = CourtLogCategoryDescription.END_HEARING_CATEGORY_DESC;

        final Integer[] startEventsTypes = CourtLogWorkFlow.getEventTypesByCategoryDesc(new String[] { start });
        final Integer[] stopEventsTypes = CourtLogWorkFlow.getEventTypesByCategoryDesc(new String[] { stop });
        final Integer[] endHearingEventsTypes = CourtLogWorkFlow
                .getEventTypesByCategoryDesc(new String[] { endHearing });

        final Integer caseId = schedHearing.getXhbHearing().getCaseId();
        LOG.debug(methodName + "Case Id: " + caseId);

        Collection courtLogEntries = getSortedEventsByCategoryDesc(caseId, date, endTime.getTime(), new String[] {
                start, stop });

        filterEntriesByDocId(courtLogEntries, docId);

        // Debug...
        if (LOG.isDebugEnabled()) {
            Iterator it = courtLogEntries.iterator();

            while (it.hasNext()) {
                CourtLogViewValue courtLogEntry = (CourtLogViewValue) it.next();
                LOG.debug(methodName + "EntryId: " + courtLogEntry.getId() + " DateTime: "
                        + courtLogEntry.getEntryDate());
            }
        }

        // Time calculation...
        final Iterator cleValues = courtLogEntries.iterator();
        boolean isTicking = false;
        long begin = 0;
        long end = 0;

        while (cleValues.hasNext()) {
            CourtLogViewValue cleValue = (CourtLogViewValue) cleValues.next();
            LOG.debug("checking eventDescId=" + cleValue.getEventType());
            if (arrayContains(startEventsTypes, cleValue.getEventType())) {
                LOG.debug("eventDescId found in startIds isTicking = " + isTicking);
                if (isTicking == false) {
                    begin = cleValue.getEntryDate().getTime();
                    isTicking = true;
                }
            } else if (arrayContains(stopEventsTypes, cleValue.getEventType())) {
                LOG.debug("eventDescId found in stopIds isTicking = " + isTicking);
                if (isTicking == true) {
                    end = cleValue.getEntryDate().getTime();
                    LOG.debug("end time=" + end);
                    totalTime += (end - begin);
                    LOG.debug("totalTime time=" + totalTime);
                    isTicking = false;
                }

                if (arrayContains(endHearingEventsTypes, cleValue.getEventType())) {
                    // process no more if hearing ended for the defendant...
                    LOG.debug("eventDescId found in endHearingIds");
                    break;
                }
            }
        }

        LOG.debug(methodName + "returning totalTime: " + totalTime);
        return totalTime;
    }

    private static Collection getSortedEventsByCategoryDesc(Integer caseId, Date startDate, Date endDate,
            String[] categories) {
        final String methodName = "getSortedEventsByCategoryDesc";
        LOG.debug(methodName + " called with caseId: " + caseId + " startDate: " + startDate + " endDate " + endDate);

        ArrayList fullList = new ArrayList();
        for (int i = 0; i < categories.length; i++) {
            CourtLogViewValue[] entries = CourtLogWorkFlow.getCourtLog(caseId, categories[i], startDate, endDate);

            if (entries.length > 0) {
                LOG.debug(methodName + ":: " + entries.length + " found for category " + categories[i]);
                fullList.addAll(Arrays.asList(entries));
            }
        }

        Sorter.sort(fullList, new String[] { "entryDate" });

        LOG.debug(methodName + " exitted with " + fullList.size() + " entries returned");
        return fullList;
    }

    private static void filterEntriesByDocId(Collection entries, Integer docId) {
        Iterator it = entries.iterator();
        while (it.hasNext()) {
            CourtLogViewValue courtLogEntry = (CourtLogViewValue) it.next();
            if (courtLogEntry.getDefendantOnCaseId() != null) {
                if (!courtLogEntry.getDefendantOnCaseId().equals(docId)) {
                    LOG.debug("Removing entry as docId = " + courtLogEntry.getDefendantOnCaseId());
                    // remove from the collection, this event does not
                    // relate
                    // to the defendant we are interested in
                    it.remove();
                }
            } else {
                // this is a case level event and the list of doc ids must be
                // checked
                String[] docIds = CSServices.getXMLServices().getXpathValuesFromXmlString(courtLogEntry.getLogEntry(),
                        DOC_ID_XPATH);
                if ((docIds.length == 0) || !arrayContains(docIds, docId.toString())) {
                    LOG
                            .debug("Removing entry as docIds.length = " + docIds.length
                                    + " and array may not contain docId");
                    it.remove();
                }
            }
        }
    }

    private static boolean arrayContains(Object[] array, Object itemInArray) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(itemInArray)) {
                return true;
            }
        }

        return false;
    }
}