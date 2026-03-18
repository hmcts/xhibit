package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.CourtDetailValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.DefendantName;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.JudgeName;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCourtStatusValue;

/**
 * Inner class for processing rows
 * 
 * @author pznwc5
 */
class CourtDetailProcessor extends AllCourtStatusProcessor {

    private static final Logger log = CSServices.getLogger(CourtDetailProcessor.class);

    /** Cache of value objects */
    protected CourtDetailValue value;

    /** Court room id */
    private int courtRoomId;

    /** Public notice query */
    private PublicNoticeQuery publicNoticeQuery = new PublicNoticeQuery();

    public CourtDetailProcessor(int courtRoomId) {
        super();
        this.courtRoomId = courtRoomId;
    }

    /**
     * Processes a record at a time
     * 
     * @param row
     *            to be processed
     */
    public void processRow(Row row) {
        // Get the case number

        if (value == null) {
            // Create a new value object add to the cache
            value = new CourtDetailValue();
            populateData(row, value);
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
        log.warn("getData() unsuppoprted, dummy list returned.");
        return new ArrayList<AllCourtStatusValue>();
    }

    /**
     * Creates a AllCourtStatusValue
     * 
     * @param row
     * @return
     */
    protected void populateData(Row row, CourtDetailValue value) {
        super.populateData(row, value);
        value.setJudgeName(new JudgeName(row));
        value.setHearingDescription(row.getString(HEARING_DESCRIPTION));
    }

    /**
     * @return
     */
    public CourtDetailValue getCourtDetailValue() {
        if (value != null) {
            value.setPublicNotices(publicNoticeQuery.execute(courtRoomId));
        }
        return value;
    }

}
