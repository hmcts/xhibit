package uk.gov.courtservice.xhibit.business.database.query.counsel;

import java.sql.Types;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.query.CourtList;

/**
 * Helper class used to execute the stored procedures/functions required for the
 * counsel facilities queries.
 * 
 * @author tz0d5m
 * @version $Revision: 1.4 $
 */
public class CounselFacilitiesQueries {
    private static final Logger log = CSServices.getLogger(CounselFacilitiesQueries.class);

    // The queries/stored procedures that this query object represents...
    private static final String ASSIGN_REPS_QUERY = "{ ? = call counselfacilities.get_court_room_list(?,?,?) }";

    /**
     * Method to handle the acquisition of the court list from the database.
     * 
     * @param courtId
     *            The id of the court to get the list for. Cannot be <i>null</i>.
     * @param startDate
     *            The date of the list, if contains time data, this will be
     *            removed first. Cannot be <i>null</i>.
     * @param courtRoomId
     *            The id of the court room the list is for (optional).
     * @return A <code>CourtList</code> value object containing the details
     *         for the list requested.
     * 
     * @throws IllegalArgumentException
     *             if either the courtId or startDate is <i>null</i>.
     */
    public CourtList getCourtRoomList(final Integer courtId, final Date startDate, final Integer courtRoomId) {
        if (log.isDebugEnabled()) {
            log.debug("getCourtRoomList(..)  courtId = " + courtId + "; startDate = " + startDate + "; courtRoomId = "
                    + courtRoomId);
        }

        if (courtId == null) {
            throw new IllegalArgumentException("courtId cannot be null");
        }

        if (startDate == null) {
            throw new IllegalArgumentException("startDate cannot be null");
        }

        final StoredProcedure cfq = new StoredProcedure(getDataSource(), ASSIGN_REPS_QUERY);
        final CounselFaciltiesRowProcessor rowProcessor = new CounselFaciltiesRowProcessor();

        cfq.registerInTypes(new int[] { Types.INTEGER, Types.DATE, Types.INTEGER });
        cfq.setRowProcessor(rowProcessor);
        cfq.execute(new Object[] { courtId, new java.sql.Date(startDate.getTime()), courtRoomId });

        // get the results from the row processor...
        return rowProcessor.getCourtList();
    }

    /**
     * Method to acquire the <code>DataSource</code> to use to execute the flr
     * searches. Extracted and made protected to allow sub-classes to specify
     * custom <code>DataSource</code>s (i.e. one for testing).
     * 
     * @return The <code>DataSource</code> to use.
     */
    protected DataSource getDataSource() {
        log.debug("Returning the default DataSource");
        return CSServices.getServiceLocator().getDataSource();
    }
}
