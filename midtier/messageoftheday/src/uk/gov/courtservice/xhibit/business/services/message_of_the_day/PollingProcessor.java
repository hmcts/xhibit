package uk.gov.courtservice.xhibit.business.services.message_of_the_day;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.PollingValue;
import uk.gov.courtservice.xhibit.business.vos.services.version.VersionValue;

/**
 * Inner class for processing rows
 * 
 * @author Patrick Dunne
 */
class PollingProcessor extends AbstractRowProcessor {

	private static String POLLING_INTERVAL="POLL_INTERVAL";
	private static String CHECK_POLLING_INTERVAL="CHECK_POLL_INTERVAL";
	private static String VALUE_COLUMN_NAME="PROPERTY_VALUE";
	private static String PROPERTY_COLUMN_NAME="PROPERTY_NAME";
    /** Create the value Object */
	 PollingValue value;
	 ArrayList arrayValues;

	public PollingProcessor() {
		value = new PollingValue();
		arrayValues= new ArrayList();
		arrayValues.add(value);
	}
	 
    private static final Logger LOG = CSServices.getLogger(PollingProcessor.class);

    /**
     * Processes a record at a time
     * 
     * @param row
     *            to be processed
     */
    public void processRow(Row row) {
        populateData(row, value);
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
    protected void populateData(Row row, PollingValue value) {
    	String columnName = row.getString(PROPERTY_COLUMN_NAME);
        String columnValue="";
    	if (columnName.equalsIgnoreCase(POLLING_INTERVAL)) {
    		columnValue = row.getString(VALUE_COLUMN_NAME);
    		value.setPollingInterval(columnValue);
    	}
    	else
    		if (columnName.equalsIgnoreCase(CHECK_POLLING_INTERVAL)) {
        		columnValue = row.getString(VALUE_COLUMN_NAME);
        		value.setCheckPollingInterval(columnValue);
        	}
    }
}
