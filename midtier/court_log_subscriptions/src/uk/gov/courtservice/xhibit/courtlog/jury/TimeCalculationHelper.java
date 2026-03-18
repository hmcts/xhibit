package uk.gov.courtservice.xhibit.courtlog.jury;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;

/**
 * @author pznwc5
 */
public class TimeCalculationHelper {
    private static final Calendar gc = new GregorianCalendar(1970, Calendar.JANUARY, 1);

    private static final Logger log = CSServices.getLogger(TimeCalculationHelper.class);

    private static final String JURY_EVENT_CATEGORIES[] = { "Calc_Jury_Out_Time_Start", "Calc_Jury_Out_Time_Stop" };

    /**
     * Calculate the time that the Jury have been out on a Case.
     * 
     * @param caseId
     * @return The number of millisecs that the Jury have been out
     * @throws CourtLogException
     */
    public long calculateJuryOutTime(Integer caseId, Date lastLogEventTime) throws CourtLogBusinessException {
        String methodName = "CalculateJuryOutTime() - ";

        boolean gotStartDate = false;
        long runningTotal = 0;
        long startTime = 0;
        long endTime = 0;

        Collection startColl = new ArrayList();
        Collection endColl = new ArrayList();

        Iterator tmp = XhbCourtLogEntryBeanHelper2.findByCaseIdCategoryDesc(caseId, JURY_EVENT_CATEGORIES[0])
                .iterator();
        while (tmp.hasNext())
            startColl.add(((XhbCourtLogEntry) tmp.next()).getEventDescId());

        tmp = XhbCourtLogEntryBeanHelper2.findByCaseIdCategoryDesc(caseId, JURY_EVENT_CATEGORIES[1]).iterator();
        while (tmp.hasNext())
            endColl.add(((XhbCourtLogEntry) tmp.next()).getEventDescId());

        Collection entries = getSortedEventsByCategoryDesc(caseId, new Timestamp(gc.getTime().getTime()),
                new Timestamp(lastLogEventTime.getTime()));
        log.debug(methodName + "Number of Court Log Entries: " + entries.size());

        // Now iterate over the entries....
        // This collection is oreded by date-time (earliest first.)
        Iterator itt = entries.iterator();

        while (itt.hasNext()) {
            XhbCourtLogEntry courtLogEntry = (XhbCourtLogEntry) itt.next();
            log.debug(methodName + courtLogEntry.getEventDescId() + " " + courtLogEntry.getDateTime());

            if (startColl.contains(courtLogEntry.getEventDescId())) {
                if (gotStartDate == true) {
                    throw new CourtLogBusinessException("Court_Log.Error_Calculating_Jury_Out_Time",
                            "Start Time found without corresponding End Time");
                } else {
                    gotStartDate = true;
                    startTime = courtLogEntry.getDateTime().getTime();
                }
            } else if (endColl.contains(courtLogEntry.getEventDescId())) {
                if (gotStartDate) {
                    gotStartDate = false;
                    endTime = courtLogEntry.getDateTime().getTime();
                    runningTotal += (endTime - startTime);
                } else {
                    // throw new
                    // CourtLogException("Court_Log.Error_Calculating_Jury_Out_Time",
                    // "EndTime found without corresponding Start Time");
                    endTime = 0; // reset. End time without start time.
                    startTime = 0;
                    gotStartDate = false;
                    // look for next pair.
                }
            }
        }

        log.debug("return runningTotal: " + runningTotal);
        return runningTotal;
    }

    private Collection getSortedEventsByCategoryDesc(Integer caseId, Timestamp startDate, Timestamp endDate) {
        final String methodName = "getSortedEventsByCategoryDesc";
        log.debug(methodName + " called with caseId: " + caseId + " startDate: " + startDate + " endDate " + endDate);

        ArrayList<XhbCourtLogEntry> fullList = new ArrayList<XhbCourtLogEntry>();
        for (int i = 0; i < JURY_EVENT_CATEGORIES.length; i++) {
            Collection<XhbCourtLogEntry> entries = XhbCourtLogEntryBeanHelper2.findByCaseIdCategoryDescDate(caseId,
                    JURY_EVENT_CATEGORIES[i], startDate, endDate);
            if (entries != null && !entries.isEmpty()) {
                log.debug(methodName + ":: " + entries.size() + " found for category " + JURY_EVENT_CATEGORIES[i]);
                // Workaround as generated finder returns a Timestamp even
                // though entities-prefs.properties
                // specifies Date.
                for (XhbCourtLogEntry entry : entries) {
                    if (entry.getDateTime() instanceof Timestamp) {
                        entry.setDateTime(new Date(entry.getDateTime().getTime()));
                    }
                }
                fullList.addAll(entries);
            }
        }
        Sorter.sort(fullList, new String[] { "dateTime" });

        log.debug(methodName + " exitted with " + fullList.size() + " entries returned");
        return fullList;
    }

}