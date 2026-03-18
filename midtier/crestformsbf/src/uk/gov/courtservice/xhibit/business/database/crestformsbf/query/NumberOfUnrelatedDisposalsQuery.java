package uk.gov.courtservice.xhibit.business.database.crestformsbf.query;

import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.crestformsbf.processor.NumberProcessor;

/**
 * <p>
 * Title: NumberOfUnrelatedDisposalsQuery
 * </p>
 * <p>
 * Description: Check to see if any unrelated disposals for the defendant
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Surtar Bachra
 */

public class NumberOfUnrelatedDisposalsQuery extends QueryOperation {
    // The file that contains the SQL for this query, currently uses one
    // parameter.
    private static final String SQL_FILE = "config/database/query/crestformsbf/NumberOfUnrelatedDisposals.sql";

    // SQL used by this query
    private static final String SQL = readSql(SQL_FILE);

    /**
     * Constructor initializes the datasource and SQL
     */
    public NumberOfUnrelatedDisposalsQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL);
        registerInTypes(new int[] { Types.INTEGER });
    }

    /**
     * @return An array of case ids for representing the linked cases
     */
    public int getNumberOfUnrelatedDisposals(Integer docId) {

        // Check the parameters
        if (docId == null)
            throw new IllegalArgumentException("defendantOnCaseId");

        // Row processor
        NumberProcessor rowProcessor = new NumberProcessor();
        setRowProcessor(rowProcessor);

        // Execute the query, parameters(?) in the prepared sql use defendantId
        execute(new Object[] { docId });

        // Return the schedule
        return rowProcessor.getNumber();
    }
}
