package uk.gov.courtservice.xhibit.business.services.version;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.version.VersionValue;

/**
 * Inner class for processing rows
 * 
 * @author Rakesh Lakhani
 */
class VersionProcessor extends AbstractRowProcessor {

    /** Need to store in the array list so that sort order is maintained */
    protected ArrayList arrayValues = new ArrayList();

    private static final Logger LOG = CSServices.getLogger(VersionProcessor.class);

    /**
     * Processes a record at a time
     * 
     * @param row
     *            to be processed
     */
    public void processRow(Row row) {
        VersionValue value = new VersionValue();
        populateData(row, value);
        // Store in the array list to keep order
        arrayValues.add(value);
    }

    /**
     * Returns the final data
     * 
     * @return An array of summary by name value objects
     */
    Collection getData() {
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
    protected void populateData(Row row, VersionValue value) {
        value.setSchemaName(row.getString("SCHEMA_NAME"));
        value.setSchemaVersion(row.getString("SCHEMA_VERSION"));
        value.setLastUpdateDate(row.getTimestamp("LAST_UPDATE_DATE"));
        value.setUpdatedBy(row.getString("UPDATED_BY"));
        value.setDisplayName(row.getString("DISPLAY_NAME"));
        value.setDisplaySeq(row.getInt("DISPLAY_SEQ"));
    }
}
