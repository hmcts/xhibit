package uk.gov.courtservice.xhibit.business.database.query.importexportstatus;

import java.sql.Types;
import java.util.Collection;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: GetByCourtIdQuery
 * </p>
 * <p>
 * Description: Query object used for getting the xhb_import_export_status rows
 * by court ID
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
public class GetByCourtIdQuery extends StoredProcedure {
    // The logger for this class.
    private static Logger log = CSServices.getLogger(GetByCourtIdQuery.class);

    // The file that contains the SQL for this query
    private static final String SQL_FILE = "config/database/query/importexportstatus/getByCourtId.sql";

    // SQL used by this query
    private static final String SQL = readSql(SQL_FILE);

    /**
     * Constructor initializes the datasource and SQL
     */
    public GetByCourtIdQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL);
        registerInTypes(new int[] { Types.INTEGER });
    }

    /**
     * Gets the xhb_import_export_status rows for the specified court
     * 
     * @return Collection of records for the specified court
     */
    public Collection getResult(Integer courtId) {
        log.debug("GetByCourtIdQuery.getResults() called with courtId: " + courtId);

        // Check the parameters
        if (courtId == null)
            throw new IllegalArgumentException("courtId");

        // Row processor
        ImportExportStatusRowProcessor rowProcessor = new ImportExportStatusRowProcessor();
        setRowProcessor(rowProcessor);

        // Execute the query
        log.debug("Will call the execute method");
        execute(new Object[] { courtId });

        // Return the import/export status collection
        log.debug("GetByCourtIdQuery.getResults() will try to get the collection that will be returned");
        return rowProcessor.getData();
    }
}
