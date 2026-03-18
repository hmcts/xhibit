package uk.gov.courtservice.xhibit.business.terminal.query;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.terminal.TerminalSummaryImpl;
import uk.gov.courtservice.xhibit.business.terminal.interfaces.TerminalSummary;

/**
 * Class used to process each row generated from the query.
 * 
 * @author tz0d5m
 * @version $Revision: 1.3 $
 */
public class TerminalRowProcessor extends AbstractRowProcessor {
    // the constants used in the row processing to get the required
    // values...
    private static final String TERMINAL_NAME = "TERMINAL_NAME";

    private static final String TERMINAL_ID = "TERMINAL_ID";

    private static final String COURT_ROOM_NAME = "COURT_ROOM_NAME";

    private static final String COURT_SITE_NAME = "COURT_SITE_NAME";

    // estimate as to an average size to prevent dynamic expansion...
    private final List results = new ArrayList(60);

    /**
     * Create an implementation of the <code>TerminalSummary</code> interface
     * based upon the value for the <code>Row</code> passed in.
     * 
     * @param row
     *            The row to process.
     * @see uk.gov.courtservice.framework.jdbc.core.RowProcessor
     *      #processRow(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public void processRow(Row row) {
        results.add(new TerminalSummaryImpl(row.getString(TERMINAL_NAME), row.getInteger(TERMINAL_ID), row
                .getString(COURT_ROOM_NAME), row.getString(COURT_SITE_NAME)));
    }

    /**
     * Acquire the results generated from the processing of the rows.
     * 
     * @return An array of <code>TerminalSummary</code> value objects.
     */
    public TerminalSummary[] getResults() {
        return (TerminalSummary[]) results.toArray(new TerminalSummary[results.size()]);
    }
}
