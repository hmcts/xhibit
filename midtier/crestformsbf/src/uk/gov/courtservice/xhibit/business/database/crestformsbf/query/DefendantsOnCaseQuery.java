package uk.gov.courtservice.xhibit.business.database.crestformsbf.query;

import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.crestformsbf.processor.DefendantsProcessor;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFDefendant;

/**
 * <p>
 * Title: ScheduleQuery
 * </p>
 * <p>
 * Description: Query object used for getting linked id's for linked cases
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

public class DefendantsOnCaseQuery extends QueryOperation {

    // The file that contains the SQL for this query, uses case id.
    private static final String SQL_FILE = "config/database/query/crestformsbf/DefendantsOnCase.sql";

    // SQL used by this query
    private static final String SQL = readSql(SQL_FILE);

    /**
     * Constructor initializes the datasource and SQL
     */
    public DefendantsOnCaseQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL);
        registerInTypes(new int[] { Types.INTEGER });
    }

    /**
     * @return An array of case ids for representing the linked cases
     */
    public CrestFormsBFDefendant[] getDefendantsOnCase(Integer caseId) {

        // Check the parameter
        if (caseId == null)
            throw new IllegalArgumentException("caseId");

        // Row processor
        DefendantsProcessor rowProcessor = new DefendantsProcessor();
        setRowProcessor(rowProcessor);

        // Execute the query, both parameters(?) in the prepared sql use case id
        execute(new Object[] { caseId });

        // Return the schedule
        return rowProcessor.getDefendants();

    }

}