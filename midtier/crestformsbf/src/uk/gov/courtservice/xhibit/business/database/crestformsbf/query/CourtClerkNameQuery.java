package uk.gov.courtservice.xhibit.business.database.crestformsbf.query;

import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.crestformsbf.processor.CourtClerkNameProcessor;

/**
 * <p>
 * Title: NumberOfMiscAppealChargesQuery
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Edward Cawley
 * 
 */

public class CourtClerkNameQuery extends QueryOperation {

    // The file that contains the SQL for this query, currently uses one
    // parameter.
    private static final String SQL_FILE = "config/database/query/crestformsbf/CourtClerkName.sql";

    // SQL used by this query
    private static final String SQL = readSql(SQL_FILE);

    /**
     * Constructor initializes the datasource and SQL
     */
    public CourtClerkNameQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL);
        registerInTypes(new int[] { Types.INTEGER });
    }

    /**
     * @return An array of case ids for representing the linked cases
     */
    public String getCourtClerkName(Integer scheduledHearingId) {

        // Check the parameter
        if (scheduledHearingId == null)
            throw new IllegalArgumentException("scheduledHearingId");

        // Row processor
        CourtClerkNameProcessor rowProcessor = new CourtClerkNameProcessor();
        setRowProcessor(rowProcessor);

        // Execute the query, both parameters(?) in the prepared sql use case id
        execute(new Object[] { scheduledHearingId });

        // Return the schedule
        return rowProcessor.getCourtClerk();

    }

}