package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.DefendantName;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.SummaryByNameValue;

/**
 * Inner class for processing rows
 * 
 * @author pznwc5
 */
class SummaryByNameProcessor extends PublicDisplayRowProcessor {
    /** Cache for summary by name values */
    private final List<SummaryByNameValue> summaryByNameValues = new ArrayList<SummaryByNameValue>();

    /**
     * Processes a record at a time
     * 
     * @param row
     *            to be processed
     */
    public void processRow(Row row) {
        SummaryByNameValue value = new SummaryByNameValue();
        populateData(row, value);
        summaryByNameValues.add(value);
    }

    /**
     * Returns the final data
     * 
     * @return An array of summary by name value objects
     */
    Collection<SummaryByNameValue> getData() {
        return summaryByNameValues;
    }

    /**
     * Populates a SummaryByNameValue
     * 
     * @param row
     */
    protected void populateData(Row row, SummaryByNameValue value) {
        // Populate public display
        // uk.gov.courtservice.xhibit.publicdisplay.publicdisplay data
        super.populateData(row, value);
        value.setReportingRestricted(row.getInt(REPORTING_RESTRICTIONS) == 1);
        value.setFloating(row.getString(IS_FLOATING));
        value.setDefendantName(new DefendantName(row));
    }

}
