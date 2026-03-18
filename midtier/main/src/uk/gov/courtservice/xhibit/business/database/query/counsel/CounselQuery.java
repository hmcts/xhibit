package uk.gov.courtservice.xhibit.business.database.query.counsel;

import java.sql.Types;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.DateTimeUtilities;

/**
 * <p>
 * Title: CounselQuery
 * </p>
 * <p>
 * Description: Query object used for getting the counsel sign in information
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford / Marie Holmberg
 * @version 1.0
 */

public class CounselQuery extends StoredProcedure {
    // The logger for this class.
    private static Logger log = CSServices.getLogger(CounselQuery.class);

    // The file that contains the SQL for this query
    private static final String SQL_FILE = "config/database/query/counsel/counselfacility.sql";

    // SQL used by this query
    private static final String SQL = readSql(SQL_FILE);

    /**
     * Constructor initializes the datasource and SQL
     */
    public CounselQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL);
        registerInTypes(new int[] { Types.INTEGER, Types.DATE, Types.INTEGER });
    }

    /**
     * Gets the counsel data for the specified date and court
     * 
     * @return Collection of partyOnCase values for the specified court and date
     */
    public Collection getAssignedRepresentatives(Integer courtId, Date date, Integer courtRoomId) {
        log.debug("CounselQuery.getAssignedRepresentatives() called with courtId: " + courtId + ", Date : " + date
                + ", courtRoomId: " + courtRoomId);

        // Check the parameters
        if (courtId == null)
            throw new IllegalArgumentException("courtId");
        if (date == null)
            throw new IllegalArgumentException("date");

        // Strip the time
        java.sql.Date strippedDate = DateTimeUtilities.stripTime(date);

        // Row processor
        CounselRowProcessor rowProcessor = new CounselRowProcessor();
        setRowProcessor(rowProcessor);

        // Execute the query
        log.debug("Will call the execute method");
        execute(new Object[] { courtId, strippedDate, courtRoomId });

        // Return the counsel collection
        log
                .debug("CounselQuery.getAssignedRepresentatives() will try to get the "
                        + "collection that will be returned");
        return rowProcessor.getAssignedRepresentatives(false);

    }
}