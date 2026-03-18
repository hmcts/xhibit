package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.CourtListValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.DefendantName;

/**
 * Inner class for processing rows
 * 
 * @author pznwc5
 */
class CourtListProcessor extends PublicDisplayRowProcessor {
    /**
     * Cache of value objects
     */
    protected Map<Integer, CourtListValue> values = new HashMap<Integer, CourtListValue>();

    /** Need to store in the array list so that sort order is maintained */
    protected ArrayList<CourtListValue> arrayValues = new ArrayList<CourtListValue>();

    public CourtListProcessor() {
        super();
    }

    /**
     * Processes a record at a time
     * 
     * @param row
     *            to be processed
     */
    public void processRow(Row row) {
        // Get the scheduled hearing Id
        Integer scheduledHearingId = row.getInteger(SCHEDULED_HEARING_ID);

        // Check the case is there in the cache
        CourtListValue value = values.get(scheduledHearingId);
        if (value == null) {
            // Create a new value object add to the cache
            value = new CourtListValue();
            populateData(row, value);
            values.put(scheduledHearingId, value);
            // Store in the array list to keep order
            arrayValues.add(value);
        } else {
            // Add the defendant
            value.addDefendantName(new DefendantName(row));
        }
    }

    /**
     * Returns the final data
     * 
     * @return An array of summary by name value objects
     */
    Collection<CourtListValue> getData() {
        return arrayValues;
    }

    /**
     * Creates a CourtListValue
     * 
     * @param row
     */
    protected void populateData(Row row, CourtListValue value) {
        super.populateData(row, value);

        value.setCaseNumber(row.getString(CASE_NUMBER));
        value.setHearingDescription(row.getString(HEARING_DESCRIPTION));
        value.setHearingProgress(row.getInt(HEARING_PROGRESS));
        value.setReportingRestricted(row.getInt(REPORTING_RESTRICTIONS) == 1);
        value.setCaseTitle(row.getString(CASE_TITLE));
        value.setListCourtRoomId(row.getInt(LIST_COURT_ROOM_ID));
        DefendantName defendantName = new DefendantName(row);
        if (defendantName.hasValue()) {
            value.addDefendantName(defendantName);
        }
    }

}
