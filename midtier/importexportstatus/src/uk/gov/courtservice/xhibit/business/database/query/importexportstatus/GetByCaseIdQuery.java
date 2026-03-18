package uk.gov.courtservice.xhibit.business.database.query.importexportstatus;

import java.sql.Types;
import java.util.Collection;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: GetByCaseIdQuery
 * </p>
 * <p>
 * Description: Query object used for getting the xhb_import_export_status rows
 * by case ID
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Steve Tully
 * @version 1.0
 */
public class GetByCaseIdQuery extends StoredProcedure {
    // The logger for this class.
    private static Logger log = CSServices.getLogger(GetByCaseIdQuery.class);

    // The file that contains the SQL for this query
    private static final String SQL_FILE = "config/database/query/importexportstatus/getByCaseId.sql";

    // SQL used by this query
    private static final String SQL = readSql(SQL_FILE);

    /**
     * Constructor initializes the datasource and SQL
     */
    public GetByCaseIdQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL);
        registerInTypes(new int[] { Types.INTEGER });
    }

    /**
     * Gets the xhb_import_export_status rows for the specified case
     * 
     * @return Collection of records for the specified case
     */
    public Collection getResult(Integer caseId) {
        log.debug("GetByCaseIdQuery.getResults() called with caseId: " + caseId);

        // Check the parameters
        if (caseId == null)
            throw new IllegalArgumentException("caseId");

        // Row processor
        ImportExportStatusRowProcessor rowProcessor = new ImportExportStatusRowProcessor();
        setRowProcessor(rowProcessor);

        // Execute the query
        log.debug("Will call the execute method");
        execute(new Object[] { caseId });

        // Return the import/export status collection
        log.debug("GetByCaseIdQuery.getResults() will try to get the collection that will be returned");
        return rowProcessor.getData();
    }
}
