package uk.gov.courtservice.xhibit.courtlog.flr;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import uk.gov.courtservice.framework.jdbc.core.AbstractDatabaseCall;
import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogScheduledHearingValue;

/**
 * A utility class used to extract all of the database reads performed by the
 * court log.
 * 
 * @author tz0d5m
 * @version $Id: CourtLogEntryQueries.java,v 1.6 2006/06/05 12:28:53 bzjrnl Exp $
 */
public class CourtLogEntryQueries extends AbstractDatabaseCall {
    /**
     * Query to acquire all of the court log scheduled hearing values for a
     * particular case.
     * 
     * @param caseId
     *            The id of the case we want the court log scheduled hearing
     *            values for.
     * @return An array of <code>CourtLogScheduledHearingValue</code> for the
     *         case passed in.
     */
    public CourtLogScheduledHearingValue[] getCourtLogScheduledHearingValues(final Integer caseId) {
        if (log.isDebugEnabled()) {
            log.debug("getCourtLogScheduledHearingValues() - caseId = " + caseId);
        }

        //
        validateParameterNotNull("caseId", caseId);

        final StoredProcedure sp = createStoredProcedure("{ call xhb_court_log_pkg.get_court_sched_hearing_value(?,?) }");
        sp.registerInTypes(new int[] { Types.INTEGER });

        final CourtLogScheduledHearingValueRowProcessor rowProcessor = new CourtLogScheduledHearingValueRowProcessor();
        sp.setRowProcessor(rowProcessor);

        sp.execute(new Object[] { caseId });

        return rowProcessor.getResults();
    }

    /**
     * Query to acquire all of the court log entries for a particular case.
     * 
     * @param caseId
     *            The id of the case we want the court log entries for.
     * @return An array of <code>XhbCourtLogEntryBasicValue</code>'s.
     */
    public XhbCourtLogEntryBasicValue[] getCourtLogEntries(final Integer caseId) {
        if (log.isDebugEnabled()) {
            log.debug("getCourtLogEntries(" + caseId + ")");
        }

        final String procedureName = "{ call xhb_court_log_pkg.get_by_case_id(?,?) }";
        final int[] parameterTypes = { Types.INTEGER };
        final Object[] parameterValues = { caseId };

        return executeStoredProcedure(procedureName, parameterTypes, parameterValues);
    }

    /**
     * Query to acquire all of the court log entries for a particular case
     * between the specified date range.
     * 
     * @param caseId
     *            The id of the case we want the court log entries for.
     * @param fromDate
     *            The earliest date permitted for the returned results.
     * @param toDate
     *            The latest date permitted for the returned results.
     * @return An array of <code>XhbCourtLogEntryBasicValue</code>'s.
     */
    public XhbCourtLogEntryBasicValue[] getCourtLogEntries(final Integer caseId, final Date fromDate, final Date toDate) {
        if (log.isDebugEnabled()) {
            log.debug("getCourtLogEntries(" + caseId + ", " + fromDate + ", " + toDate + ")");
        }

        final String procedureName = "{ call xhb_court_log_pkg.get_by_case_id_date(?,?,?,?) }";
        final int[] parameterTypes = { Types.INTEGER, Types.DATE, Types.DATE };
        final Object[] parameterValues = { caseId, convertDateToTimestamp(fromDate), convertDateToTimestamp(toDate) };

        return executeStoredProcedure(procedureName, parameterTypes, parameterValues);
    }

    /**
     * Query to acquire the last court log entry for a particular case of the
     * passed in event type.
     * 
     * @param caseId
     *            The id of the case we want the court log entries for.
     * @param eventType
     *            The only event type permitted for the returned results.
     * @return A <code>XhbCourtLogEntryBasicValue</code>, or <i>null</i> if
     *         there is no entry of the specified event type.
     */
    // @todo - the stored procedure associated with this method can be
    // improved, so as to only return a single value...
    public XhbCourtLogEntryBasicValue getLastCourtLogEntry(final Integer caseId, final Integer eventType) {
        if (log.isDebugEnabled()) {
            log.debug("getLastCourtLogEntry(" + caseId + ", " + eventType + ")");
        }

        final String procedureName = "{ call xhb_court_log_pkg.get_by_case_id_eventtype(?,?,?) }";
        final int[] parameterTypes = { Types.INTEGER, Types.INTEGER };
        final Object[] parameterValues = { caseId, eventType };

        final XhbCourtLogEntryBasicValue[] results = executeStoredProcedure(procedureName, parameterTypes,
                parameterValues);

        // return the last entry in the list...
        return ((results.length > 0) ? results[results.length - 1] : null);
    }

    /**
     * Query to acquire all of the court log entries for a particular case
     * between the specified date range, of the specified category
     * 
     * @param caseId
     *            The id of the case we want the court log entries for.
     * @param categoryDesc
     *            The description of the category we want entries for.
     * @param fromDate
     *            The earliest date permitted for the returned results.
     * @param toDate
     *            The latest date permitted for the returned results.
     * @return An array of <code>XhbCourtLogEntryBasicValue</code>'s.
     */
    public XhbCourtLogEntryBasicValue[] getCourtLogEntries(final Integer caseId, final String categoryDesc) {
        if (log.isDebugEnabled()) {
            log.debug("getCourtLogEntries(" + caseId + ", \"" + categoryDesc + "\")");
        }

        final String procedureName = "{ call xhb_court_log_pkg.get_by_case_id_catdesc(?,?,?) }";
        final int[] parameterTypes = { Types.INTEGER, Types.VARCHAR };
        final Object[] parameterValues = { caseId, categoryDesc };

        return executeStoredProcedure(procedureName, parameterTypes, parameterValues);
    }

    /**
     * Query to acquire all of the court log entries for a particular case
     * between the specified date range, of the specified category
     * 
     * @param caseId
     *            The id of the case we want the court log entries for.
     * @param categoryDesc
     *            The description of the category we want entries for.
     * @param fromDate
     *            The earliest date permitted for the returned results.
     * @param toDate
     *            The latest date permitted for the returned results.
     * @return An array of <code>XhbCourtLogEntryBasicValue</code>'s.
     */
    public XhbCourtLogEntryBasicValue[] getCourtLogEntries(final Integer caseId, final String categoryDesc,
            final Date fromDate, final Date toDate) {
        if (log.isDebugEnabled()) {
            log.debug("getCourtLogEntries(" + caseId + ", \"" + categoryDesc + "\", " + fromDate + ", " + toDate + ")");
        }

        final String procedureName = "{ call xhb_court_log_pkg.get_by_case_id_date_catdesc(?,?,?,?,?) }";
        final int[] parameterTypes = { Types.INTEGER, Types.DATE, Types.DATE, Types.VARCHAR };
        final Object[] parameterValues = { caseId, convertDateToTimestamp(fromDate), convertDateToTimestamp(toDate),
                categoryDesc };

        return executeStoredProcedure(procedureName, parameterTypes, parameterValues);
    }

    // *************************************************************************
    // *************************************************************************
    // *************************************************************************

    /**
     * Method used to extract the common processing required for the extraction
     * of court log entries from the query to execute.
     * 
     * @param procedureName
     *            The full JDBC connection String required to call the stored
     *            procedure/function.
     * @param inTypes
     *            An array of the input parameter database types.
     * @param values
     *            An <code>Object</code> array containing the values to set
     *            for the array of input database types.
     * @return An array of <code>XhbCourtLogEntryBasicValue</code> objects.
     */
    private XhbCourtLogEntryBasicValue[] executeStoredProcedure(final String procedureName, final int[] inTypes,
            final Object[] values) {
        if (log.isDebugEnabled()) {
            final StringBuffer debug = new StringBuffer(100);
            debug.append("executeStoredProcedure(): ").append(procedureName);
            debug.append(";inTypes.length = ").append(inTypes.length);
            debug.append(";values = ").append(Arrays.asList(values));

            log.debug(debug.toString());
        }

        // ensure the passed in array of values contains no null entries...
        validateParametersNotNull(procedureName, values);

        final StoredProcedure sp = createStoredProcedure(procedureName);
        sp.registerInTypes(inTypes);

        // if the reflection performance is shown to be poor, then this can be
        // changed to use a specific RowProcessor...
        final ReflectionRowProcessor rowProcessor = new ReflectionRowProcessor(XhbCourtLogEntryBasicValue.class);
        rowProcessor.registerDefaultBindings();
        sp.setRowProcessor(rowProcessor);

        sp.execute(values);

        // finally convert the Collection into a typed-array...
        return convertCollection(rowProcessor.getResults());
    }

    /**
     * Utility method to convert the passed in <code>Collection</code> to an
     * array of <code>XhbCourtLogEntryBasicValue</code>s. If the passed in
     * parameter is <i>null</i> an empty array will be returned (although
     * should never get <i>null</i> passed to this method).
     * 
     * @param results
     *            The <code>Collection</code> to return.
     * @return The converted array.
     */
    private XhbCourtLogEntryBasicValue[] convertCollection(final Collection results) {
        if (results == null) {
            log.debug("convertResults() - input is null");
            return new XhbCourtLogEntryBasicValue[0];
        }

        log.debug("convertResults() - Converting " + results.size() + " entries");
        return (XhbCourtLogEntryBasicValue[]) results.toArray(new XhbCourtLogEntryBasicValue[results.size()]);
    }

    /**
     * Utility method to validate that the passed in array of values does not
     * contain any <i>null</i> values.
     * 
     * @param procedureName
     *            The name of the procedure we should be executing, used only if
     *            an error occurs to construct the message.
     * @param values
     *            The array of values to check for <i>null</i>'s.
     * @throws IllegalArgumentException
     *             if the passed in values array contains any <i>null</i>
     *             values.
     */
    private void validateParametersNotNull(final String procedureName, final Object[] values) {
        // ensure that all of the passed parameters are not null...
        for (int i = 0, n = values.length; i < n; i++) {
            if (values[i] == null) {
                final String error = procedureName + ": null parameter supplied: " + Arrays.asList(values);
                log.error(error);
                throw new IllegalArgumentException(error);
            }
        }
    }

    /**
     * Extraction of common code to convert the passed in <code>Date</code>
     * object to a <code>Timestamp</code> object to be passed to the stored
     * procedures/functions.
     * 
     * @param input
     *            The <code>Date</code> to convert.
     * @return The converted <code>Timestamp</code>.
     */
    private Timestamp convertDateToTimestamp(final Date input) {
        return ((input != null) ? new Timestamp(input.getTime()) : null);
    }
}
