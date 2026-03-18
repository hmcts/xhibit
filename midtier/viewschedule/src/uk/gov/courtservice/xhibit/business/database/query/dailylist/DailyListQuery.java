package uk.gov.courtservice.xhibit.business.database.query.dailylist;

import java.sql.Types;
import java.util.Date;

import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.vos.services.dailylist.DailyListValue;

/**
 * <p>
 * Title: DailyListQuery
 * </p>
 * <p>
 * Description: Class for executing and processing the Daily list.
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version $Id: DailyListQuery.java,v 1.8 2006/06/05 12:29:59 bzjrnl Exp $
 */
public class DailyListQuery extends StoredProcedure {
    private static final String SQL_PROCEDURE = "{ call xhb_view_schedule_pkg.get_daily_list(?,?,?) }";

    private static final int PARAMETER_TYPES[] = { Types.INTEGER, Types.DATE };

    /**
     * Initialize the datasource and register the in parameter types
     */
    public DailyListQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL_PROCEDURE);
        registerInTypes(PARAMETER_TYPES);
    }

    /**
     * Gets the schedulle for the specified date and court
     * 
     * @param courtId
     *            Integer
     * @param date
     *            Date
     * @return DailyListValue - Daily list for the specified court and date
     */
    public DailyListValue getDailyList(final Integer courtId, final Date date) {
        // Check the parameters
        if (courtId == null)
            throw new IllegalArgumentException("courtId");
        if (date == null)
            throw new IllegalArgumentException("date");

        // Strip the time
        final java.sql.Date strippedDate = DateTimeUtilities.stripTime(date);

        // Row processor
        final DailyListRowProcessor rowProcessor = new DailyListRowProcessor(courtId);
        setRowProcessor(rowProcessor);

        // Execute the query
        execute(new Object[] { courtId, strippedDate });

        // Return the schedule
        return rowProcessor.getDailyList();
    }
}
