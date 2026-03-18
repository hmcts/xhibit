package uk.gov.courtservice.xhibit.business.database.crestformsbf.query;

import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.crestformsbf.processor.NumberProcessor;

/**
 * <p>
 * Title: NumberOfSection41OffencesQuery
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

public class NumberOfSection41OffencesQuery extends QueryOperation {

    // The file that contains the SQL for this query, currently uses two
    // parameters.
    private static final String SQL_FILE = "config/database/query/crestformsbf/NumberOfSection41Offences.sql";

    // SQL used by this query
    private static final String SQL = readSql(SQL_FILE);

    /**
     * Constructor initializes the datasource and SQL
     */
    public NumberOfSection41OffencesQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL);
        registerInTypes(new int[] { Types.INTEGER, Types.INTEGER });
    }

    /**
     * @return An array of case ids for representing the linked cases
     */
    public int getNumberOfSection41Offences(Integer caseId, Integer defendantId) {

        // Check the parameters
        if (caseId == null)
            throw new IllegalArgumentException("caseId");

        if (defendantId == null)
            throw new IllegalArgumentException("defendantId");

        // Row processor
        NumberProcessor rowProcessor = new NumberProcessor();
        setRowProcessor(rowProcessor);

        // Execute the query, both parameters(?) in the prepared sql use case id
        execute(new Object[] { caseId, defendantId });

        // Return the schedule
        return rowProcessor.getNumber();

    }

}