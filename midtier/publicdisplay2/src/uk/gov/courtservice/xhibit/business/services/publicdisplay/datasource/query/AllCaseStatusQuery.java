package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query;

import java.util.Collection;
import java.util.Date;

/**
 * This class wraps the stored procedure that provides the data for the all case
 * status document.
 * 
 * @author Rakesh Lakhani
 */
public class AllCaseStatusQuery extends PublicDisplayQuery {
    /** The stored procedure to execute */
    private static final String SQL_PROCEDURE = "{ call XHB_PUBLIC_DISPLAY_PKG.GET_ALL_CASE_STATUS(?,?,?,?) }";

    /**
     * Constructor compiles the query
     */
    public AllCaseStatusQuery() {
        super(SQL_PROCEDURE);
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
    public Collection getData(Date date, int courtId, int[] courtRoomIds) {
        return execute(date, courtId, courtRoomIds, new AllCaseStatusProcessor());
    }
}
