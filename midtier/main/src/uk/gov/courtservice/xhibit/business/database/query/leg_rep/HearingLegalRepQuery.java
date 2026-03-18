package uk.gov.courtservice.xhibit.business.database.query.leg_rep;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_hearing_leg_rep.XhbHearingLegRepBasicValue;

/**
 * A utility class used to extract all of the database reads performed by the
 * court log.
 * 
 * @author tz0d5m
 * @version $Id: HearingLegalRepQuery.java,v 1.5 2006/06/05 12:29:15 bzjrnl Exp $
 */
public class HearingLegalRepQuery {
    /** The log4j logger instance */
    protected static final Logger log = CSServices.getLogger(HearingLegalRepQuery.class);

    /**
     * Query to acquire all of the court log entries for a particular case.
     * 
     * @param caseId
     *            The id of the case we want the court log entries for.
     * @return An array of <code>XhbCourtLogEntryBasicValue</code>'s.
     */
    public XhbHearingLegRepBasicValue[] getHearingLegRepEntries(Integer hearingId, Integer refLegRepId, Date shDate) {
        if (log.isDebugEnabled()) {
            log.debug("getHearingLegRepEntries(" + hearingId + ", " + refLegRepId + ", " + shDate.toString() + ")");
        }

        final String procedureName = "{ call xhb_hearing_pkg.get_hearing_leg_reps(?,?,?,?) }";
        final int[] parameterTypes = { Types.INTEGER, Types.INTEGER, Types.TIMESTAMP };
        final Object[] parameterValues = { hearingId, refLegRepId, convertDateToTimestamp(shDate) };

        return executeStoredProcedure(procedureName, parameterTypes, parameterValues);
    }

    public XhbHearingLegRepBasicValue[] getHearingLegRepEntriesForRemoval(Integer hearingId, Integer refLegRepId,
            Date shDate) {
        if (log.isDebugEnabled()) {
            log.debug("getHearingLegRepEntriesForRemoval(" + hearingId + ", " + refLegRepId + ", " + shDate.toString()
                    + ")");
        }

        final String procedureName = "{ call xhb_hearing_pkg.get_hearing_leg_reps_to_remove(?,?,?,?) }";
        final int[] parameterTypes = { Types.INTEGER, Types.INTEGER, Types.TIMESTAMP };
        final Object[] parameterValues = { hearingId, refLegRepId, convertDateToTimestamp(shDate) };

        return executeStoredProcedure(procedureName, parameterTypes, parameterValues);
    }

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
    private XhbHearingLegRepBasicValue[] executeStoredProcedure(String procedureName, int[] inTypes, Object[] values) {
        if (log.isDebugEnabled()) {
            final StringBuffer debug = new StringBuffer(100);
            debug.append("executeStoredProcedure(): ").append(procedureName);
            debug.append(";inTypes.length = ").append(inTypes.length);
            debug.append(";values = ").append(Arrays.asList(values));

            log.debug(debug.toString());
        }

        // ensure the passed in array of values contains no null entries...
        validateParametersNotNull(procedureName, values);

        final StoredProcedure sp = new StoredProcedure(getDataSource(), procedureName);
        sp.registerInTypes(inTypes);

        // if the reflection performance is shown to be poor, then this can be
        // changed to use a specific RowProcessor...
        final ReflectionRowProcessor rowProcessor = new ReflectionRowProcessor(XhbHearingLegRepBasicValue.class);
        rowProcessor.registerDefaultBindings();
        sp.setRowProcessor(rowProcessor);

        sp.execute(values);

        // finally convert the Collection into a typed-array...
        return convertCollection(rowProcessor.getResults());
    }

    /**
     * Method to acquire the <code>DataSource</code> to use to execute the flr
     * searches. Extracted and made protected to allow sub-classes to specify
     * custom <code>DataSource</code>s (i.e. one for testing).
     * 
     * @return The <code>DataSource</code> to use.
     */
    protected DataSource getDataSource() {
        log.debug("Returning the default DataSource");
        return CSServices.getServiceLocator().getDataSource();
    }

    /**
     * Utility method to convert the passed in <code>Collection</code> to an
     * array of <code>XhbHearingLegRepBasicValue</code>s. If the passed in
     * parameter is <i>null</i> an empty array will be returned (although
     * should never get <i>null</i> passed to this method).
     * 
     * @param results
     *            The <code>Collection</code> to return.
     * @return The converted array.
     */
    private XhbHearingLegRepBasicValue[] convertCollection(Collection results) {
        if (results == null) {
            log.debug("convertResults() - input is null");
            return new XhbHearingLegRepBasicValue[0];
        }

        log.debug("convertResults() - Converting " + results.size() + " entries");
        return (XhbHearingLegRepBasicValue[]) results.toArray(new XhbHearingLegRepBasicValue[results.size()]);
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
    private void validateParametersNotNull(String procedureName, Object[] values) {
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
    private Timestamp convertDateToTimestamp(Date input) {
        return ((input != null) ? new Timestamp(input.getTime()) : null);
    }

    /*
     * // a simple test main method, implemented to quickly the functionality...
     * public static void main(String[] args) { HearingLegalRepQuery cleq = new
     * HearingLegalRepQuery() { protected DataSource getDataSource() { return
     * new uk.gov.courtservice.xhibit.business.database.StandAloneDataSource(); } }; //
     * Object[] obj = cleq.getHearingLegRepEntries(new Integer(2), new
     * Integer(11103), java.util.Calendar.getInstance().getTime()); Object[] obj =
     * cleq.getHearingLegRepEntriesForRemoval(new Integer(2), new
     * Integer(11103), java.util.Calendar.getInstance().getTime());
     * System.err.println("obj = " + obj.length + "; " + Arrays.asList(obj));
     * 
     * java.util.Calendar c = java.util.Calendar.getInstance();
     * c.add(java.util.Calendar.HOUR,-24); Object[] obj2 =
     * cleq.getHearingLegRepEntriesForRemoval(new Integer(3), new Integer(3830),
     * c.getTime()); System.err.println("obj2 = " + obj2.length + "; " +
     * Arrays.asList(obj2)); }
     */
}
