package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query;

import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.DefendantName;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.JudgeName;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.JuryStatusDailyListValue;

/**
 * Inner class for processing rows
 * 
 * @author pznwc5
 */
class JuryStatusDailyListProcessor extends CourtListProcessor {

    public JuryStatusDailyListProcessor() {
        super();
    }

    /**
     * Processes a record at a time
     * 
     * @param row
     *            to be processed
     */
    public void processRow(Row row) {
        // Get the scheduled hearing id
        Integer scheduledHearingId = row.getInteger(SCHEDULED_HEARING_ID);

        // Check the case is there in the cache
        JuryStatusDailyListValue value = (JuryStatusDailyListValue) values.get(scheduledHearingId);
        if (value == null) {
            // Create a new value object and add the object to the cache
            value = new JuryStatusDailyListValue();
            populateData(row, value);
            values.put(scheduledHearingId, value);
            arrayValues.add(value);
        } else {
            // Add the defendant
            value.addDefendantName(new DefendantName(row));
        }
    }

    /**
     * Populates a JuryStatusDailyListValue
     * 
     * @param row
     * @return
     */
    protected void populateData(Row row, JuryStatusDailyListValue value) {
        // Populate uk.gov.courtservice.xhibit.publicdisplay.publicdisplay data
        super.populateData(row, value);
        value.setFloating(row.getString(IS_FLOATING));
        value.setJudgeName(new JudgeName(row));
    }

}
