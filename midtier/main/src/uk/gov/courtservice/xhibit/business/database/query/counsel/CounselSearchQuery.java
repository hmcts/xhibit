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
 * Title: CounselSearchQuery
 * </p>
 * <p>
 * Description: Query object used for searching counsels that have signed in
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
public class CounselSearchQuery extends StoredProcedure {

    // The logger for this class.
    private static final Logger log = CSServices.getLogger(CounselSearchQuery.class);

    // The file that contains the SQL for this query
    private static final String SQL_FILE = "config/database/query/counsel/searchcounsel.sql";

    // SQL used by this query
    private static final String SQL = readSql(SQL_FILE);

    /**
     * Constructor initializes the datasource and SQL
     */
    public CounselSearchQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL);
        registerInTypes(new int[] { Types.INTEGER, Types.DATE, Types.VARCHAR, Types.VARCHAR });
    }

    /**
     * Gets the counsel data for the specified date and court and counsel name
     * 
     * @return Collection of partyOnCase values for the specified court and date
     *         and counsel name
     */
    public Collection searchForCounsel(Integer courtId, Date date, String counselFirstName, String counselSurname) {
        log.debug("CounselSearchQuery.searchForCounsel() called with courtId: " + courtId + ", Date : " + date
                + ", counselName: " + counselFirstName + ", counselSurname: " + counselSurname);

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
        execute(new Object[] { courtId, strippedDate, counselFirstName, counselSurname });

        // Return the counsel collection
        log.debug("CounselQuery.searchForCounsel() will try to get the " + "collection that will be returned");

        // pass in true as we want to remove party on case values
        // that do not have legal reps
        return rowProcessor.getAssignedRepresentatives(true);
    }
}
