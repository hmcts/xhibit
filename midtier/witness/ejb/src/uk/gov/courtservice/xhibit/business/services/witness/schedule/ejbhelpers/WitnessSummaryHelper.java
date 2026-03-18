package uk.gov.courtservice.xhibit.business.services.witness.schedule.ejbhelpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonDay;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonSchedule;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonScheduleHelper;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbWitness;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbWitnessHelper;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.InvalidSkeletonScheduleException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.NoScheduleForCaseException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessSessionImpl;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: .
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 */
public class WitnessSummaryHelper {
    private static final Logger log = CSServices.getLogger(WitnessSummaryHelper.class);

    public static Calendar getBeginningOfToday() {
        final Calendar firstThing = new GregorianCalendar();
        firstThing.set(Calendar.HOUR_OF_DAY, 0);
        firstThing.set(Calendar.MINUTE, 0);
        firstThing.set(Calendar.SECOND, 0);
        return firstThing;
    }

    public static Calendar getEndOfToday() {
        final Calendar lastThing = new GregorianCalendar();
        lastThing.set(Calendar.HOUR_OF_DAY, 23);
        lastThing.set(Calendar.MINUTE, 59);
        lastThing.set(Calendar.SECOND, 59);
        return lastThing;
    }

    public WitnessSession[] getWitnessesForTimeRange(final Integer caseId, final Calendar start, final Calendar end)
            throws NoScheduleForCaseException {
        final long startTimeInSecs;
        final long endTimeInSecs;
        if (start != null) {
            startTimeInSecs = start.getTime().getTime() / 1000L;
        } else {
            startTimeInSecs = 0;
        }
        if (end != null) {
            endTimeInSecs = end.getTime().getTime() / 1000L;
        } else {
            endTimeInSecs = Long.MAX_VALUE;
        }
        Collection skeletons = null;
        try {
            skeletons = XhbSkeletonScheduleHelper.getLocalHome().findByCase(caseId);
        } catch (javax.ejb.FinderException e) {
            log.error(e);
            throw new CSUnrecoverableException(e);
        }
        if (skeletons.size() == 0) {
            throw new NoScheduleForCaseException();
        }
        if (skeletons.size() > 1) {
            throw new InvalidSkeletonScheduleException("There were multiple skeleton schedules for this case.");
        }
        final XhbSkeletonSchedule schedule = (XhbSkeletonSchedule) skeletons.toArray()[0];
        final Collection xhbSkeletonDays = schedule.getXhbSkeletonDays();
        if (log.isDebugEnabled()) {
            log.debug("Looking for witnesses in range: " + start + " - " + end);
        }

        ArrayList result = null;
        result = getWitnessesForDaysInRange(xhbSkeletonDays, startTimeInSecs, endTimeInSecs, schedule);
        WitnessSession[] sessions = (WitnessSession[]) result.toArray(new WitnessSession[result.size()]);
        Arrays.sort(sessions, WitnessSessionComparator.getInstance());
        return sessions;
    }

    public static ArrayList getWitnessesForDaysInRange(final Collection xhbSkeletonDays, final long startTimeInSecs,
            final long endTimeInSecs, final XhbSkeletonSchedule schedule) {
        final ArrayList result = new ArrayList();
        for (Iterator iterator3 = xhbSkeletonDays.iterator(); iterator3.hasNext();) {
            final XhbSkeletonDay xhbSkeletonDay = (XhbSkeletonDay) iterator3.next();
            final java.sql.Timestamp skeletonDate = xhbSkeletonDay.getSkeletonDate();
            if (skeletonDate == null) {
                continue;
            }
            final long timeInSecs = skeletonDate.getTime() / 1000;
            log.debug("Checking to see if timeInSecs: " + timeInSecs + " is between " + startTimeInSecs + " and "
                    + endTimeInSecs);
            if (timeInSecs >= startTimeInSecs && timeInSecs <= endTimeInSecs) {
                log.debug("Date in range");
                // at this point the code used to get all sessions for the
                // scedule, resulting in a complete set of witnesses for each
                // day, instead we need to get witnesses by the day . . .
                /*
                 * final Collection xhbSkeletonSessions =
                 * schedule.getXhbSkeletonSessions(); for (Iterator iterator =
                 * xhbSkeletonSessions.iterator(); iterator.hasNext();) { final
                 * XhbSkeletonSession xhbSkeletonSession = (XhbSkeletonSession)
                 * iterator.next(); final Collection xhbWitnesses =
                 * xhbSkeletonSession.getXhbWitnesss(); for (Iterator iterator2 =
                 * xhbWitnesses.iterator(); iterator2.hasNext();) { final
                 * XhbWitness xhbWitness = (XhbWitness) iterator2.next(); if
                 * (log.isDebugEnabled()) { log.debug("Adding: " +
                 * xhbWitness.getData(false)); } result.add(new
                 * WitnessSessionImpl(xhbWitness.getData(false))); } }
                 */
                // new code here simply gets the witnesses for this allowed day
                // . . .
                try {
                    Collection xhbWitness = XhbWitnessHelper.getLocalHome().findByCaseAndDay(
                            schedule.getXhbCase().getCaseId(), xhbSkeletonDay.getDayNumber());
                    Iterator i = xhbWitness.iterator();
                    while (i.hasNext()) {
                        XhbWitness wv = (XhbWitness) i.next();
                        result.add(new WitnessSessionImpl(wv.getData(false)));
                    }
                } catch (FinderException fe) {
                    log.debug("No witnesses for caseId:"+schedule.getXhbCase().getCaseId()+", day: "+xhbSkeletonDay.getDayNumber());
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Date not in range: " + xhbSkeletonDay.getData());
                }
            }
        }
        return result;
    }
}
