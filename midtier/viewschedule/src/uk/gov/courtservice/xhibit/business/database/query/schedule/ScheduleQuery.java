package uk.gov.courtservice.xhibit.business.database.query.schedule;

import java.sql.Types;
import java.util.Date;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.TodaysScheduleValue;

/**
 * <p>
 * Title: ScheduleQuery
 * </p>
 * <p>
 * Description: Query object used for getting the schedule
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Meeraj Kunnumpurath
 * @version $Id: ScheduleQuery.java,v 1.10 2006/06/05 12:29:59 bzjrnl Exp $
 */
public class ScheduleQuery extends StoredProcedure {
    private static final Logger logger = CSServices.getLogger(ScheduleQuery.class);

    private static final String SQL_PROCEDURE = "{ call xhb_view_schedule_pkg.get_schedule(?,?,?,?) }";

    private static final int PARAMETER_TYPES[] = { Types.INTEGER, Types.DATE, Types.INTEGER };

    /**
     * Constructor, initializes the datasource and SQL
     */
    public ScheduleQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL_PROCEDURE);
        registerInTypes(PARAMETER_TYPES);
    }

    /**
     * Gets the schedule for the specified date and court
     * 
     * @param courtId
     *            Integer
     * @param startDate
     *            Date
     * @return Schedule for the specified court and date
     */
    public TodaysScheduleValue getSchedule(final Integer courtId, final Date startDate) {
        return this.getSchedule(courtId, startDate, null);
    }

    public TodaysScheduleValue getSchedule(final Integer courtId, final Date startDate, final Integer courtRoomId) {
        // Check the parameters & courtRoomId can be null
        if (courtId == null)
            throw new IllegalArgumentException("courtId");
        if (startDate == null)
            throw new IllegalArgumentException("startDate");

        // Strip the time
        final java.sql.Date strippedDate = DateTimeUtilities.stripTime(startDate);

        // Row processor
        final ScheduleRowProcessor rowProcessor = new ScheduleRowProcessor(courtId);
        setRowProcessor(rowProcessor);

        logger.debug("About to execute the schedule");

        execute(new Object[] { courtId, strippedDate, courtRoomId });

        // Return the schedule
        return rowProcessor.getSchedule();
    }
}