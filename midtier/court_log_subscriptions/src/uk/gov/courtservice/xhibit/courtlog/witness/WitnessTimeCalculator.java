package uk.gov.courtservice.xhibit.courtlog.witness;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.courtlog.CourtLogCategoryDescription;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: The WitnessTimeCalculator is used to calculate the amount of
 * time a witness has spent in court for the calculation of expenses. The main
 * use of this class will be by the WitnessHelper.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.7 $
 */
public class WitnessTimeCalculator {
    private static final Logger LOG = CSServices.getLogger(WitnessTimeCalculator.class);

    /**
     * @pre releaseDateTime != null
     * @pre witnessId != null
     * @param releaseDateTime
     * @param witness
     */
    private WitnessTimeCalculator() {
        // private constructor to prevent external instantiation...
    }

    /**
     * Have any events called
     * 
     * @return
     */
    public static Date getWitnessTime(Integer caseId, Date arrivalDate, Date releaseDate) {
        CourtLogViewValue[] startEvents = CourtLogWorkFlow.getCourtLog(caseId,
                CourtLogCategoryDescription.SCHEDULED_HEARING_TIME_START_CATEGORY_DESC, arrivalDate, releaseDate);

        CourtLogViewValue[] stopEvents = CourtLogWorkFlow.getCourtLog(caseId,
                CourtLogCategoryDescription.SCHEDULED_HEARING_TIME_STOP_CATEGORY_DESC, arrivalDate, releaseDate);

        LOG.debug("Sorted start events:" + startEvents.length);
        LOG.debug("Sorted stop events:" + stopEvents.length);

        return getDurationFromStartAndStopEvents(stopEvents, startEvents, arrivalDate, releaseDate);
    }

    /**
     * This method follows the following process:
     * <p>
     * For a given set of stop and start events occurring between the arrival
     * and release times.
     * <p>
     * 1/ calculate difference between release and arrival times.
     * <p>
     * 2/ find paired stops and starts and subtract differences from result of
     * 1/
     * <p>
     * 3/ finally if stop last event before release, subtract time between stop
     * and release from result of 2/ 4/ return result of 3/
     * 
     * @param stopEvents
     * @param startEvents
     * @param arrivalDate
     * @param releaseDate
     * @return
     */
    private static Date getDurationFromStartAndStopEvents(CourtLogViewValue[] stopEvents,
            CourtLogViewValue[] startEvents, Date arrivalDate, Date releaseDate) {
        // Start by getting the total time between arrival and release.
        // This is our first guess at witness time.
        long guessTime = releaseDate.getTime() - arrivalDate.getTime();
        Iterator stopIterator = Arrays.asList(stopEvents).iterator();
        Iterator startIterator = Arrays.asList(startEvents).iterator();
        // If present set start/stop events else set to null
        CourtLogViewValue startEvent = startIterator.hasNext() ? ((CourtLogViewValue) startIterator.next()) : null;
        CourtLogViewValue stopEvent = stopIterator.hasNext() ? ((CourtLogViewValue) stopIterator.next()) : null;

        // We go to the first stop event, then look for the first subsequent
        // start event
        // the difference between them is subtracted from the guess time.
        while (stopEvent != null && startEvent != null) {
            // Go to the first start event after the current stop event.
            // Note use of ! and .after() this is to handle the rare
            // eventuality
            // when
            // the dates are identical.
            while (startEvent != null // We haven't reached the end of the
                    // start
                    // event iterator.
                    && !startEvent.getEntryDate().after(stopEvent.getEntryDate())) // The
                                                                                    // start
                                                                                    // event
                                                                                    // is
            // before
            // the current stop event
            {
                // If there is a start event get it if not set start event to
                // null.
                startEvent = startIterator.hasNext() ? ((CourtLogViewValue) startIterator.next()) : null;
            }
            // If we have a start event after the stop event.
            if (startEvent != null) {
                // subtract difference beween start and stop from guessTime.
                guessTime -= (startEvent.getEntryDate().getTime() - stopEvent.getEntryDate().getTime());
                // go to stop event immediately after current start event.
                // Note use of ! and .after() this is to handle the rare
                // eventuality when
                // the dates are identical.
                while (stopEvent != null // We haven't reached the end of
                        // the
                        // stop event iterator.
                        && !stopEvent.getEntryDate().after(startEvent.getEntryDate()))// The
                                                                                        // stop
                // event is
                // before
                // the current stop event
                {
                    stopEvent = stopIterator.hasNext() ? ((CourtLogViewValue) stopIterator.next()) : null;
                }
            }
        }// If last event a start event, stopEvent will be null.
        if (stopEvent != null) // assume witness not in court after session
        // stopped
        {
            // calculate difference between stopEvent and releaseDate for
            // subtraction from guessTime.
            guessTime -= (releaseDate.getTime() - stopEvent.getEntryDate().getTime());
        }
        
        // return the guessed time.
        Calendar calculatedTime = Calendar.getInstance();
        calculatedTime.set(Calendar.HOUR_OF_DAY, (int)guessTime/3600000);
        calculatedTime.set(Calendar.MINUTE,      (int)guessTime%3600000/60000);
        calculatedTime.set(Calendar.SECOND,      (int)guessTime%60000/1000);
        return calculatedTime.getTime();
    }
}
