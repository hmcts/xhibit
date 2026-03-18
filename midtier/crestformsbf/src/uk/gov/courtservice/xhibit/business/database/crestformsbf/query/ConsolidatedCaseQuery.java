package uk.gov.courtservice.xhibit.business.database.crestformsbf.query;

import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.crestformsbf.processor.CasesProcessor;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFCase;

public class ConsolidatedCaseQuery extends QueryOperation {

    // The file that contains the SQL for this query, currently uses two
    // parameters, both case id.
    private static final String SQL_FILE = "config/database/query/crestformsbf/ConsolidatedCase.sql";

    // SQL used by this query
    private static final String SQL = readSql(SQL_FILE);

    /**
     * Constructor initializes the datasource and SQL
     */
    public ConsolidatedCaseQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL);
        registerInTypes(new int[] { Types.INTEGER });
    }

    /**
     * @return An array of case ids for representing the linked cases
     */
    public CrestFormsBFCase[] getConsolidatedQuery(Integer caseId) {

        // Check the parameter
        if (caseId == null)
            throw new IllegalArgumentException("caseId");

        // Row processor
        CasesProcessor rowProcessor = new CasesProcessor();
        rowProcessor.setLinked(true);
        setRowProcessor(rowProcessor);

        // Execute the query passing in the case id
        execute(new Object[] { caseId });

        // Return the schedule
        return rowProcessor.getCases();
    }
}
