package uk.gov.courtservice.xhibit.business.services.version;

import java.sql.Types;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.AbstractDatabaseCall;
import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_terminal_default.XhbTerminalDefaultBasicValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Update 1 row in the table XHB_TERMINAL based on terminal id
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version $Id: UpdateTerminal.java,v 1.4 2013/11/21 15:12:13 atwells Exp $
 */

public class UpdateTerminal extends AbstractDatabaseCall {

    private static final Logger log = CSServices.getLogger(UpdateTerminal.class);

    /**
     * The query: UPDATE XHB_TERMINAL SET LOCATION = XX, COURT_ROOM_ID = XX,
     * COURT_SITE_ID = XX, COURTROOM_OR_SITE = XX, COURT_ID = XX WHERE TERMINAL_NAME=
     */
    private static final String UPDATETERMINALSSQL1 = "UPDATE XHB_TERMINAL SET LOCATION ='";
    private static final String UPDATETERMINALSSQL2 = "', COURT_ROOM_ID = ";   
    private static final String UPDATETERMINALSSQL3 = ", COURT_SITE_ID = '";
    private static final String UPDATETERMINALSSQL4 = "', COURTROOM_OR_SITE = '";
    private static final String UPDATETERMINALSSQL5 = "', COURT_ID = '";
    private static final String UPDATETERMINALSSQL6 = "' WHERE TERMINAL_NAME = '";
    
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
     * @return An array of <code>XhbTerminalBasicValue</code>'s.
     */
    public XhbTerminalBasicValue[] updateTerminal(String terminalName, String location, Integer courtId, Integer courtSiteId, Integer courtRoomId, String roomOrSite) {
        String roaming = "N";
        if (log.isDebugEnabled()) {
            log.debug("updateTerminal(" + terminalName + ", \"" + location + "\", " + courtId + ", " + courtSiteId + ", " + courtRoomId + ", " + roomOrSite + ", " + roaming + ")");
        }

        final String procedureName = "{ call xhb_terminal_pkg.update_terminal(?,?,?,?,?,?,?,?) }";
        final int[] parameterTypes = { Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR };
        final Object[] parameterValues = { terminalName, location, courtId, courtRoomId, courtSiteId, roomOrSite, roaming };

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
     * @return An array of <code>XhbTerminalBasicValue</code> objects.
     */
    private XhbTerminalBasicValue[] executeStoredProcedure(final String procedureName, final int[] inTypes, final Object[] values) {
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
        final ReflectionRowProcessor rowProcessor = new ReflectionRowProcessor(XhbTerminalBasicValue.class);
        rowProcessor.registerDefaultBindings();
        sp.setRowProcessor(rowProcessor);

        sp.execute(values);

        // finally convert the Collection into a typed-array...
        return convertCollection(rowProcessor.getResults());
    }
    
    /**
     * Utility method to convert the passed in <code>Collection</code> to an
     * array of <code>XhbTerminalBasicValue</code>s. If the passed in
     * parameter is <i>null</i> an empty array will be returned (although
     * should never get <i>null</i> passed to this method).
     * 
     * @param results
     *            The <code>Collection</code> to return.
     * @return The converted array.
     */
    private XhbTerminalBasicValue[] convertCollection(final Collection results) {
        if (results == null) {
            log.debug("convertResults() - input is null");
            return new XhbTerminalBasicValue[0];
        }

        log.debug("convertResults() - Converting " + results.size() + " entries");
        return (XhbTerminalBasicValue[]) results.toArray(new XhbTerminalBasicValue[results.size()]);
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
}
