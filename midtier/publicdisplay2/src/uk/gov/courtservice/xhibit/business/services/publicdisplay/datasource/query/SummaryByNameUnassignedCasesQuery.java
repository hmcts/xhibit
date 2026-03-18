package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query;

import java.util.Collection;
import java.util.Date;

/**
 * This class wraps the stored procedure that provides the data for the summary
 * by name document. Even though the instance is not thread safe, you can still
 * cache this in a session bean as the container serializes invocations on a
 * given session bean instance. In short, stateless session bean instances are
 * pooled and wouldn't allow concurrent invocations.
 * 
 * @author pznwc5
 */
public class SummaryByNameUnassignedCasesQuery extends PublicDisplayQuery {
    /** The stored procedure to execute */
    private static final String SQL_PROCEDURE = "{ call XHB_PUBLIC_DISPLAY_PKG.GET_SUMMARY_BY_NAME_U (?,?,?,?) }";

    /**
     * Constructor compiles the query
     */
    public SummaryByNameUnassignedCasesQuery() {
        super(SQL_PROCEDURE);
        log.debug("Query object created");
    }

    public Collection getData(Date date, int courtId, int[] courtRoomIds) {
        return execute(date, courtId, courtRoomIds, new SummaryByNameProcessor());
    }
}
