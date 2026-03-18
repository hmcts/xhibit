package uk.gov.courtservice.xhibit.business.terminal.query;

import java.sql.Types;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.terminal.interfaces.CourtSiteSummary;
import uk.gov.courtservice.xhibit.business.terminal.interfaces.CourtSummary;
import uk.gov.courtservice.xhibit.business.terminal.interfaces.TerminalSummary;

/**
 * Fast lane reader class for use in the terminal sub-project. Contains calls to
 * all of the stored procedures/functions from the xhb_terminal_pkg package.
 * 
 * @author tz0d5m
 * @version $Revision: 1.5 $
 */
public class TerminalQueries extends StoredProcedure {
    /** The log4j logger instance */
    private static final Logger log = CSServices.getLogger(TerminalQueries.class);

    /** The name of the database package that the functions belongto */
    private static final String PACKAGE = "xhb_terminal_pkg";

    // Constants representing the database function calls...
    private static final String GET_TERMINALS = "{ ? = call " + PACKAGE + ".get_terminals(?) }";

    private static final String GET_COURTS = "{ ? = call " + PACKAGE + ".get_courts() }";

    private static final String GET_COURT_SITES = "{ ? = call " + PACKAGE + ".get_court_sites() }";

    private static final String GET_TERMINAL_BY_PRIMARY_KEY = "{ ? = call " + PACKAGE
            + ".get_terminal_by_primary_key(?) }";

    /**
     * Private constructor to prevent external instantiation, as all access to
     * the queries is handled by this class via static methods.
     * 
     * @param procedureName
     *            The name of the stored procedure on the database to call.
     */
    private TerminalQueries(String procedureName) {
        super(CSServices.getServiceLocator().getDataSource(), procedureName);
    }

    /**
     * Static method used as the access point for running the getTerminals
     * query.
     * 
     * @param courtId
     *            The court id to get all of the terminals for.
     * @return Implementations of the <code>TerminalSummary</code> interface.
     */
    public static TerminalSummary[] getTerminalsForCourt(Integer courtId) {
        log.debug("getTerminalsForCourt() - courtId = " + courtId);

        final TerminalQueries tq = new TerminalQueries(GET_TERMINALS);
        final TerminalRowProcessor rowProcessor = new TerminalRowProcessor();

        tq.registerInTypes(new int[] { Types.INTEGER });
        tq.setRowProcessor(rowProcessor);
        tq.execute(new Object[] { courtId });

        return rowProcessor.getResults();
    }

    /**
     * Method to acquire the terminal details given the primary key.
     * 
     * @param terminalId
     *            The id of the terminal we want to retrieve.
     * @return An implementation of the <code>TerminalSummary</code>
     *         interface.
     */
    public static TerminalSummary getTerminalByPrimaryKey(Integer terminalId) {
        log.debug("getTerminalByPrimaryKey() - terminalId = " + terminalId);

        final TerminalQueries tq = new TerminalQueries(GET_TERMINAL_BY_PRIMARY_KEY);
        final TerminalRowProcessor rowProcessor = new TerminalRowProcessor();

        tq.registerInTypes(new int[] { Types.INTEGER });
        tq.setRowProcessor(rowProcessor);
        tq.execute(new Object[] { terminalId });

        final TerminalSummary[] results = rowProcessor.getResults();

        if (results.length != 1) {
            // find by primary must return 1 row, else it is an error...
            final String message = "For terminal " + terminalId + ", " + results.length + " results returned";

            log.error(message);
            throw new CSUnrecoverableException(message);
        }

        return results[0];
    }

    /**
     * Method to retrieve details for all of the courts.
     * 
     * @return An array of <code>CourtSummary</code> implementations.
     */
    public static CourtSummary[] getCourts() {
        log.debug("getCourts()");

        final TerminalQueries tq = new TerminalQueries(GET_COURTS);
        final CourtSummaryRowProcessor rowProcessor = new CourtSummaryRowProcessor();

        tq.setRowProcessor(rowProcessor);
        tq.execute(new Object[0]);

        return rowProcessor.getResults();
    }

    /**
     * Method to retrieve details for all of the court site.
     * 
     * @return An array of <code>CourtSiteSummary</code> implementations.
     */
    public static CourtSiteSummary[] getCourtSites() {
        log.debug("getCourtSites()");

        final TerminalQueries tq = new TerminalQueries(GET_COURT_SITES);
        final CourtSiteSummaryRowProcessor rowProcessor = new CourtSiteSummaryRowProcessor();

        tq.setRowProcessor(rowProcessor);
        tq.execute(new Object[0]);

        return rowProcessor.getResults();
    }
}
