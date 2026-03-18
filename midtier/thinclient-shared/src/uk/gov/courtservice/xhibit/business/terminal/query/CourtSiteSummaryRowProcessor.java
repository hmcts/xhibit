package uk.gov.courtservice.xhibit.business.terminal.query;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.terminal.CourtSiteSummaryImpl;
import uk.gov.courtservice.xhibit.business.terminal.interfaces.CourtSiteSummary;

/**
 * Class used to process each row generated from the query.
 * 
 * @author tz0d5m
 * @version $Revision: 1.3 $
 */
public class CourtSiteSummaryRowProcessor extends AbstractRowProcessor {
    // the constants used in the row processing to get the required
    // values...
    private static final String COURT_NAME = "COURT_NAME";

    private static final String COURT_ID = "COURT_ID";

    private static final String CREST_COURT_ID = "CREST_COURT_ID";

    private static final String COURT_SITE_NAME = "COURT_SITE_NAME";

    private static final String COURT_SITE_ID = "COURT_SITE_ID";

    // estimate as to an average size to reduce dynamic expansion...
    private final List results = new ArrayList(100);

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
        results.add(new CourtSiteSummaryImpl(row.getString(COURT_NAME), row.getInteger(COURT_ID), row
                .getString(CREST_COURT_ID), row.getString(COURT_SITE_NAME), row.getInteger(COURT_SITE_ID)));
    }

    /**
     * Acquire the results generated from the processing of the rows.
     * 
     * @return An array of <code>CourtSummary</code> value objects.
     */
    public CourtSiteSummary[] getResults() {
        return (CourtSiteSummary[]) results.toArray(new CourtSiteSummary[results.size()]);
    }
}
