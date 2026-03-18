package uk.gov.courtservice.xhibit.business.database.crestformsbf.query;

import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.crestformsbf.processor.CasesProcessor;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFCase;

/**
 * <p>
 * Title:
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
 */

public class CaseQuery extends QueryOperation {

    // The file that contains the SQL for this query, uses case id.
    private static final String SQL_FILE = "config/database/query/crestformsbf/Case.sql";

    // SQL used by this query
    private static final String SQL = readSql(SQL_FILE);

    /**
     * Constructor initializes the datasource and SQL
     */
    public CaseQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL);
        registerInTypes(new int[] { Types.INTEGER });
    }

    /**
     * @return An array of case ids for representing the linked cases
     */
    public CrestFormsBFCase getCase(Integer caseId) {

        // Check the parameter
        if (caseId == null)
            throw new IllegalArgumentException("caseid");

        // Row processor
        CasesProcessor rowProcessor = new CasesProcessor();
        // rowProcessor.setLinked(true); //??
        setRowProcessor(rowProcessor);

        // Execute the query, both parameters(?) in the prepared sql use case id
        execute(new Object[] { caseId });

        // Return the schedule
        CrestFormsBFCase[] cases = rowProcessor.getCases(); // should be
        // only one
        if (cases.length > 0) {
            return cases[0];
        } else {
            return null;
        }

    }

}