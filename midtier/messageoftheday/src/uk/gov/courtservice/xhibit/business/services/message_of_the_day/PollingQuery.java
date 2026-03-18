package uk.gov.courtservice.xhibit.business.services.message_of_the_day;

import java.util.Collection;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.PollingValue;

/**
 * This class wraps the stored procedure that provides the data for the court
 * list document.
 * 
 * @author Patrick Dunne
 */
public class PollingQuery extends QueryOperation {
    private static final Logger log = CSServices.getLogger(PollingQuery.class);

    /** The stored procedure to execute */
    private static final String SQL = "SELECT * FROM XHB_CONFIG_PROP WHERE PROPERTY_NAME='POLL_INTERVAL' OR PROPERTY_NAME='CHECK_POLL_INTERVAL'";

    /**
     * Constructor compiles the query
     */
    public PollingQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL);
        log.debug("Query object created");
    }

    /**
     * Returns an array of CourtListValue.
     * 
     * @param date
     *            Id
     * @param courtId
     *            room ids for which the data is required
     * @param courtRoomIds
     *            Court room ids
     * 
     * @return Suumary by name data for the specified court rooms
     */
    public PollingValue getData() {
        PollingProcessor vp = new PollingProcessor();
        setRowProcessor(vp);
        execute(new Object[] {});
        Collection c = vp.getData();
        return (PollingValue) vp.arrayValues.get(0);
    }
}
