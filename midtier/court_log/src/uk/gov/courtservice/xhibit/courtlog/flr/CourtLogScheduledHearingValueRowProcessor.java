package uk.gov.courtservice.xhibit.courtlog.flr;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogScheduledHearingValue;

/**
 * Class used to process each row generated from the query.
 * 
 * @author tz0d5m
 * @version $Id: CourtLogScheduledHearingValueRowProcessor.java,v 1.1 2004/11/01
 *          15:19:31 tz0d5m Exp $
 */
public class CourtLogScheduledHearingValueRowProcessor extends AbstractRowProcessor {
    private static final String SCHEDULED_HEARING_ID = "SCHEDULED_HEARING_ID";

    private static final String HEARING_TYPE_CODE = "HEARING_TYPE_CODE";

    private static final String START_DATE = "START_DATE";

    private final List results = new ArrayList();

    /**
     * Create an instance of <code>CourtLogScheduledHearingValue</code> using
     * the values acquired from the <code>Row</code> passed in.
     * 
     * @param row
     *            The row to process.
     * 
     * @see uk.gov.courtservice.framework.jdbc.core.RowProcessor
     *      #processRow(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public void processRow(Row row) {
        results.add(new CourtLogScheduledHearingValue(row.getInteger(SCHEDULED_HEARING_ID), row
                .getString(HEARING_TYPE_CODE), row.getTimestamp(START_DATE)));
    }

    /**
     * Acquire the results generated from the processing of the rows.
     * 
     * @return An array of <code>CourtLogScheduledHearingValue</code> objects.
     */
    public CourtLogScheduledHearingValue[] getResults() {
        return (CourtLogScheduledHearingValue[]) results.toArray(new CourtLogScheduledHearingValue[results.size()]);
    }
}
