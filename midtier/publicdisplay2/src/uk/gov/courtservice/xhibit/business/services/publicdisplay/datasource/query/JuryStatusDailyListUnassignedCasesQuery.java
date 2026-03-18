package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query;

import java.util.Collection;
import java.util.Date;

/**
 * This class wraps the stored procedure that provides the data for the daily
 * list and jury status document and includes unassigned cases from all court
 * rooms (though currently they are only in court room 1).
 * 
 * @author pznwc5
 */
public class JuryStatusDailyListUnassignedCasesQuery extends PublicDisplayQuery {
    /** The stored procedure to execute */
    private static final String SQL_PROCEDURE = "{ call XHB_PUBLIC_DISPLAY_PKG.GET_JURY_STATUS_DAILY_LIST_U(?,?,?,?) }";

    /**
     * Constructor compiles the query
     */
    public JuryStatusDailyListUnassignedCasesQuery() {
        super(SQL_PROCEDURE);
        log.debug("Query object created");
    }

    /**
     * Returns an collection of JuryStatusDailyListValue.
     * 
     * @param date
     *            Id
     * @param courtId
     *            room ids for which the data is required
     * @param courtRoomIds
     *            Court room ids
     * 
     * @return Daily List data for the specified court rooms with unassigned
     *         cases
     */
    public Collection getData(Date date, int courtId, int[] courtRoomIds) {
        return execute(date, courtId, courtRoomIds, new JuryStatusDailyListProcessor());
    }
}
