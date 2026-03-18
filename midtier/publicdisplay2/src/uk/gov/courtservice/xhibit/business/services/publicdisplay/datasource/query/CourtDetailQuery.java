package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.CourtDetailValue;

/**
 * This class wraps the stored procedure that provides the data for the court
 * detail document.
 * 
 * @author
 */
public class CourtDetailQuery extends PublicDisplayQuery {

    /** Parameters expected by the stored procedure */
    private static final int[] PARAMETER_TYPES = { Types.NUMERIC, Types.DATE, Types.NUMERIC };

    /** The stored procedure to execute */
    private static final String SQL_PROCEDURE = "{ call XHB_PUBLIC_DISPLAY_PKG.GET_COURT_DETAIL(?,?,?,?) }";

    /**
     * Registers the parameter types and SQL
     * 
     */
    public CourtDetailQuery() {
        super(SQL_PROCEDURE);
        registerInTypes(PARAMETER_TYPES);
    }

    /**
     * Common interface
     */
    public Collection<CourtDetailValue> getData(Date date, int courtId, int[] courtRoomIds) {
        checkParams(date, courtId, courtRoomIds);
        List<CourtDetailValue> data = new ArrayList<CourtDetailValue>();
        CourtDetailValue value = getData(date, courtId, courtRoomIds[0]);
        if (value != null)
            data.add(value);
        return data;
    }

    /**
     * Creates the court detail object
     */
    public CourtDetailValue getData(Date date, int courtId, int courtRoomId) {
        // Create the processor
        CourtDetailProcessor processor = new CourtDetailProcessor(courtRoomId);

        // Check the parameters
        checkParams(date, courtId, new int[] { courtRoomId });

        // Strip the time
        final java.sql.Date strippedDate = DateTimeUtilities.stripTimeToSQLDate(date);

        setRowProcessor(processor);

        // Log the parameters
        logParams(courtId, strippedDate, String.valueOf(courtRoomId));

        // Execute the stored procedure
        execute(new Object[] { new Integer(courtId), strippedDate, new Integer(courtRoomId) });
        log.debug("Stored procedure executed");

        // Return the data
        return processor.getCourtDetailValue();
    }
}
