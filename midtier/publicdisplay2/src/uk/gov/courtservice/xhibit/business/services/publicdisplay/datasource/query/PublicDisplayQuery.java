package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.DateTimeUtilities;

/**
 * Abstract query class used by public display
 * 
 * @author pznwc5
 */
public abstract class PublicDisplayQuery extends StoredProcedure {

    /** Parameters expected by the stored procedure */
    private static final int[] PARAMETER_TYPES = { Types.NUMERIC, Types.DATE, Types.VARCHAR };

    /** Logger object */
    protected final Logger log = Logger.getLogger(getClass());

    /**
     * Creates a new PublicDisplayQuery object.
     * 
     * @param sql
     *            Stored procedure call
     */
    protected PublicDisplayQuery(String sql) {
        super(CSServices.getServiceLocator().getDataSource(), sql);
        this.registerInTypes(PARAMETER_TYPES);
        log.debug("Row processor set");
    }

    /**
     * Checks the parameters
     * 
     * @param date
     * @param courtId
     * @param courtRoomIds
     * 
     * @throws IllegalArgumentException
     */
    protected void checkParams(Date date, int courtId, int[] courtRoomIds) {
        // Check the parameters
        if (date == null) {
            throw new IllegalArgumentException("The parameter date is null.");
        }
        log.debug("date: " + date);

        if (courtId < 0) {
            throw new IllegalArgumentException("The parameter courtId is negative.");
        }
        log.debug("courtId: " + courtId);

        if (courtRoomIds == null) {
            throw new IllegalArgumentException("The parameter courtRoomIds is null.");
        }

        if (courtRoomIds.length == 0) {
            throw new IllegalArgumentException("The parameter courtRoomIds is a zero sized array.");
        }
        log.debug("courtRoomIds: " + courtRoomIds);

    }

    /**
     * 
     * @param courtRoomIds
     * @return
     * @pre courtRoomIds != null
     */
    protected String getCourtRoomIdsString(int courtRoomIds[]) {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < courtRoomIds.length; i++) {
            buff.append(courtRoomIds[i]);
            if (i != courtRoomIds.length - 1)
                buff.append(',');
        }
        return buff.toString();
    }

    /**
     * Logs the parameters
     * 
     * @param courtId
     * @param strippedDate
     * @param courtRoomIdsAsString
     */
    protected void logParams(int courtId, final java.sql.Date strippedDate, String courtRoomIdsAsString) {
        log.debug("Court Id: " + courtId);
        log.debug("Stripped date: " + strippedDate);
        log.debug("Court room ids: " + courtRoomIdsAsString);
    }

    /**
     * Executes the stored procedure and returns the data.
     * 
     * @param date
     *            Id
     * @param courtId
     *            room ids for which the data is required
     * @param courtRoomIds
     *            Court room ids
     * @param processor
     *            to use
     * 
     * @return Public display data
     * @post return != null
     */
    protected Collection execute(Date date, int courtId, int[] courtRoomIds, PublicDisplayRowProcessor processor) {
        // Check the parameters
        checkParams(date, courtId, courtRoomIds);

        // Strip the time
        final java.sql.Date strippedDate = DateTimeUtilities.stripTimeToSQLDate(date);
        String courtRoomIdsAsString = getCourtRoomIdsString(courtRoomIds);

        setRowProcessor(processor);

        // Log the parameters
        logParams(courtId, strippedDate, courtRoomIdsAsString);

        // Execute the stored procedure
        try {
        	execute(new Object[] { new Integer(courtId), strippedDate, courtRoomIdsAsString });
        	log.debug("Stored procedure executed");
        } catch (Exception e) {
        	log.error("execute() - Exception retrieving data - " + e.getMessage());
        }

        // Return the data (never a null!)
        if (processor.getData() == null) {
            return new ArrayList();
        } else {
            return processor.getData();
        }

    }

    /**
     * Returns an array of SummaryByNameValue.
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
    public abstract Collection getData(Date date, int courtId, int[] courtRoomIds);
}
