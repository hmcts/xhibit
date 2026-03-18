package uk.gov.courtservice.xhibit.courtlog.jury;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.helpers.CrudValueAssembler;
import uk.gov.courtservice.xhibit.courtlog.helpers.EntityHelper;
import uk.gov.courtservice.xhibit.courtlog.helpers.crud.CrudHelper;
import uk.gov.courtservice.xhibit.courtlog.helpers.crud.UpdateHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: JuryEventsHelper
 * </p>
 * <p>
 * Description: Helper class to carry out utility operations
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Stephen Tully
 * @version $Revision: 1.11 $
 */
public class JuryEventsHelper {
    protected static final Logger LOG = Logger.getLogger(JuryEventsHelper.class);

    private static final TimeCalculationHelper tcHelper = new TimeCalculationHelper();

    private static final Properties timeCalcProperties = CSServices.getConfigServices().getProperties("courtlog");

    /**
     * Generic post processing to get a collection of court log entries for the
     * criteria specified and re-calculate the jury time out for each one
     * 
     * @param crudValue
     * @param startDate
     * @param category
     */
    public static void genericPostProcessing(OperationContext context, Date startDate, String category)
            throws CourtLogBusinessException {
        LOG.debug("Start - genericPostProcessing(CourtLogCRUDValue, Date, String) for jury subscriber");
        String methodName = "genericPostProcessing::";

        Calendar lastLogEventTime = Calendar.getInstance();
        lastLogEventTime.set(Calendar.YEAR, 9999);

        // Obtain the court log entries for the case, supplied category and date
        // range using an end date sufficiently far in the future to guarantee
        // getting all court log events
        Collection entries = getJuryCourtLogEntries(context.getCrudValue().getCaseId(), startDate, lastLogEventTime
                .getTime(), timeCalcProperties.getProperty(category));

        LOG.debug(methodName + "Number of Court Log Entries: " + entries.size());

        // Iterate over the entries( ordered in ascending date-time sequence ).
        // For each entry...
        Iterator itt = entries.iterator();
        while (itt.hasNext()) {
            XhbCourtLogEntry courtLogEntry = (XhbCourtLogEntry) itt.next();
            LOG.debug(methodName + courtLogEntry.getEventDescId() + " " + courtLogEntry.getDateTime());

            // ...convert the entry from a XhbCourtLogEntryBasicValue to a
            // CourtLogCRUDValue, recalculate the jury time out and update
            // the record
            CourtLogCRUDValue thisCrud = CrudValueAssembler.createCourtLogCRUDValue(courtLogEntry.getData());
            recalculateJuryTimeOut(thisCrud);
        }

        LOG.debug("End - genericPostProcessing(CourtLogCRUDValue, Date, String) for jury subscriber");
    }

    /**
     * Post processing specific to delete transactions. Get a collection of
     * court log entries for the criteria specified but only re-calculate the
     * jury time out for the first one
     * 
     * @param crudValue
     * @param startDate
     * @param category
     */
    public static void deletePostProcessing(OperationContext context, Date startDate, String category) throws CourtLogBusinessException{
        LOG.debug("Start - deletePostProcessing(CourtLogCRUDValue, Date, String) for jury subscriber");
        String methodName = "deletePostProcessing::";

        // Return the court log entries for the supplied category and an end
        // date
        // sufficiently far in the future to guarantee getting all court log
        // events
        Calendar lastLogEventTime = Calendar.getInstance();
        lastLogEventTime.set(Calendar.YEAR, 9999);

        Collection entries = getJuryCourtLogEntries(context.getCrudValue().getCaseId(), startDate, lastLogEventTime
                .getTime(), timeCalcProperties.getProperty(category));

        LOG.debug(methodName + "Number of Court Log Entries: " + entries.size());

        if (!entries.isEmpty()) {
            XhbCourtLogEntry courtLogEntry = (XhbCourtLogEntry) (entries.toArray())[0];

            // Convert the XhbCourtLogEntryBasicValue to a CourtLogCRUDValue
            CourtLogCRUDValue thisCrud = CrudValueAssembler.createCourtLogCRUDValue(courtLogEntry.getData());

            // Perform an update on the event without changing anything so
            // that
            // the business logic is re-validated. No need to do more than
            // the
            // first as this is done implicitly by calling the business
            // logic.
            new UpdateHelper(context).updateEntry(thisCrud);
        }

        LOG.debug("End - deletePostProcessing(CourtLogCRUDValue, Date, String) for jury subscriber");
    }

    /**
     * Used from create \ update \ delete logic where later events exist for
     * which the time needs to be recalculated.
     * 
     * @param cruds
     * @throws CourtLogException
     */
    public static void recalculateJuryTimeOut(CourtLogCRUDValue crud) throws CourtLogBusinessException {
        String logEntryXml = null;

        // Recalculate the jury out time
        long timeInMilliSeconds = getTimeCalculationHelper().calculateJuryOutTime(
                new Integer(crud.getCaseId().intValue()), crud.getEntryDate());
        long long_minutes = (timeInMilliSeconds / 1000) / 60;
        long hours = long_minutes / 60;
        int minutes = (int) (long_minutes - (hours * 60));
        LOG.debug("Jury out time: " + hours + " hrs, " + minutes + " mins");
        crud.setProperty("E20913_Jury_Out_Time_Hours", new Long(hours));
        crud.setProperty("E20913_Jury_Out_Time_Minutes", new Integer(minutes));

        // Create the new XML
        logEntryXml = CrudHelper.validateEntry(crud);

        // Save the new XML.
        // Bypass the helper otherwise we get stuck in a recursive loop as
        // the update will re-trigger the check business logic which calls
        // this method!!!
        XhbCourtLogEntry cle = EntityHelper.getXhbCourtLogEntry(crud.getLogEntryId());
        cle.setLogEntryXml(logEntryXml);
    }

    /**
     * Call the find by caseID/date/categoryDescription finder and return the
     * results
     * 
     * @param caseId
     * @param startDate
     * @param endDate
     * @param category
     * @return Collection coresponding to the court log entries that match the
     *         criteria supplied
     */
    public static Collection getJuryCourtLogEntries(Integer caseId, Date startDate, Date endDate, String category) {
        return XhbCourtLogEntryBeanHelper2.findByCaseIdCategoryDescDate(caseId, category, new Timestamp(startDate
                .getTime()), new Timestamp(endDate.getTime()));
    }

    private static TimeCalculationHelper getTimeCalculationHelper() {
        return tcHelper;
    }
}
