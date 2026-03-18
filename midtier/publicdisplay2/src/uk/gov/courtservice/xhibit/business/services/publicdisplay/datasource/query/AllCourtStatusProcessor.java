package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCourtStatusValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.DefendantName;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.nodes.BranchEventXMLNode;

/**
 * Inner class for processing rows
 * 
 * @author pznwc5
 */
class AllCourtStatusProcessor extends PublicDisplayRowProcessor {
    /** Cache of value objects */
    protected Map<String, AllCourtStatusValue> values = new HashMap<String, AllCourtStatusValue>();

    /** Need to store in the array list so that sort order is maintained */
    protected ArrayList<AllCourtStatusValue> arrayValues = new ArrayList<AllCourtStatusValue>();

    public AllCourtStatusProcessor() {
        super();
    }

    /**
     * Processes a record at a time
     * 
     * @param row
     *            to be processed
     */
    public void processRow(Row row) {
        // Get the court Site code and court room name
        String courtSiteCode = row.getString(COURT_SITE_CODE);
        String courtRoomName = row.getString(COURT_ROOM_NAME);

        // key for hashmap
        String siteRoomName = courtSiteCode + courtRoomName;

        // Check the case is there in the cache
        AllCourtStatusValue value = values.get(siteRoomName);
        if (value == null) {
            // Create a new value object add to the cache
            value = new AllCourtStatusValue();
            populateData(row, value);
            values.put(siteRoomName, value);
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
    Collection<AllCourtStatusValue> getData() {
        return arrayValues;
    }

    /**
     * Creates a AllCourtStatusValue
     * 
     * @param row
     *            The row to parse
     * @param value
     *            the value to populate.
     */
    public void populateData(Row row, AllCourtStatusValue value) {
        super.populateCourtSiteRoomData(row, value);
        value.setCaseNumber(row.getString(CASE_NUMBER));
        value.setReportingRestricted(row.getInt(REPORTING_RESTRICTIONS) == 1);
        value.setEvent((BranchEventXMLNode) EventXMLNodeHelper.buildEventNode(row.getString(PUBLIC_DISPLAY_STATUS)));
        value.setEventTime(row.getTimestamp(TIME_STATUS_SET));
        value.setCaseTitle(row.getString(CASE_TITLE));
        value.addDefendantName(new DefendantName(row));
    }
}
