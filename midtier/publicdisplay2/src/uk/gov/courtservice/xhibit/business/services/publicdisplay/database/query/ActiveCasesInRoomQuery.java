package uk.gov.courtservice.xhibit.business.services.publicdisplay.database.query;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Active Cases in Court Room query
 * </p>
 * <p>
 * Description: This runs the stored procedure that for a given list, it finds
 * all the active cases in the court room except for the case supplied
 * (identified by its scheduled hearing id). I would expect that in most cases
 * this will only be zero or one record.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: ActiveCasesInRoomQuery.java,v 1.3 2005/11/17 10:55:48 bzjrnl
 *          Exp $
 */

public class ActiveCasesInRoomQuery extends StoredProcedure {
    /** Logger object */
    private static final Logger log = Logger.getLogger(ActiveCasesInRoomQuery.class);

    /** Parameters expected by the stored procedure */
    private static final int[] PARAMETER_TYPES = { Types.NUMERIC, Types.NUMERIC, Types.NUMERIC };

    private static final String SQL_PROCEDURE = "{ call XHB_PUBLIC_DISPLAY_PKG.GET_ACTIVE_CASES_IN_ROOM(?,?,?,?) }";

    public ActiveCasesInRoomQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL_PROCEDURE);
        this.registerInTypes(PARAMETER_TYPES);
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
    public Collection getData(Integer listId, Integer courtRoomId, Integer scheduledHearingId) {
        ActiveCasesInRoomProcessor processor = new ActiveCasesInRoomProcessor();
        setRowProcessor(processor);

        // Log the parameters
        logParams(listId, courtRoomId, scheduledHearingId);

        // Execute the stored procedure
        execute(new Object[] { listId, courtRoomId, scheduledHearingId });
        log.debug("Stored procedure executed");

        // Return the data (never a null!)
        if (processor.getData() == null) {
            return new ArrayList();
        } else {
            return processor.getData();
        }
    }

    /**
     * Logs the parameters
     * 
     * @param courtId
     * @param strippedDate
     * @param courtRoomIdsAsString
     */
    protected void logParams(Integer courtRoomId, Integer listId, Integer scheduledHearingId) {
        log.debug("court Room Id: " + courtRoomId);
        log.debug("List Id: " + listId);
        log.debug("scheduled Hearing Id: " + scheduledHearingId);
    }
}
