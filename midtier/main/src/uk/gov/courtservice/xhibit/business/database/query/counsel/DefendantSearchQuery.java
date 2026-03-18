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
 * Title: DefendantSearchQuery
 * </p>
 * <p>
 * Description: Query object used for searhcing defendants on todays schedule
 * for use with counsel sign in
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
public class DefendantSearchQuery extends StoredProcedure {

    // The logger for this class.
    private static final Logger log = CSServices.getLogger(CounselSearchQuery.class);

    // The file that contains the SQL for this query
    private static final String SQL_FILE = "config/database/query/counsel/searchdefendants.sql";

    // SQL used by this query
    private static final String SQL = readSql(SQL_FILE);

    /**
     * Constructor initializes the datasource and SQL
     */
    public DefendantSearchQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL);
        registerInTypes(new int[] { Types.INTEGER, Types.DATE, Types.VARCHAR, Types.VARCHAR });
    }

    /**
     * Gets the counsel data for the specified date and court and counsel name
     * 
     * @return Collection of partyOnCase values for the specified court and date
     *         and counsel name
     */
    public Collection searchForDefendants(Integer courtId, Date date, String defFirstName, String defSurname) {
        log.debug("DefendantSearchQuery.searchForDefendants() called with courtId: " + courtId + ", Date : " + date
                + ", defFirstName: " + defFirstName + ", defSurname: " + defSurname);

        // Check the parameters
        if (courtId == null)
            throw new IllegalArgumentException("courtId");
        if (date == null)
            throw new IllegalArgumentException("date");

        // Strip the time
        Date strippedDate = DateTimeUtilities.stripTime(date);

        // Row processor
        CounselRowProcessor rowProcessor = new CounselRowProcessor();
        setRowProcessor(rowProcessor);

        // Execute the query
        log.debug("Will call the execute method");
        execute(new Object[] { courtId, strippedDate, defFirstName, defSurname });

        // Return the counsel collection
        log.debug("DefendantSearchQuery.searchForDefendants() will try to get the "
                + "collection that will be returned");

        return rowProcessor.getAssignedRepresentatives(false);
    }
}
