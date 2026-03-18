package uk.gov.courtservice.xhibit.business.database.results.query;

// JDK
import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.results.processor.RefDisposalTypeProcessor;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;

/**
 * <p>
 * Title: RefDisposalTypeQuery
 * </p>
 * <p>
 * Description: Query the database for disposal type reference data
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version 1.0
 */
public class RefDisposalTypeQuery extends QueryOperation {

    // The file that contains the SQL for this query, uses case id.
    private static final String SQL_FILE = "config/database/query/results/RefDisposalType.sql";

    // SQL used by this query
    private static final String SQL = readSql(SQL_FILE);

    /**
     * Constructor initializes the datasource and SQL
     */
    public RefDisposalTypeQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL);
        registerInTypes(new int[] { Types.NUMERIC });
    }

    /**
     * Get the latest disposal (DisposalReferenceValue) with the specified
     * disposal code
     * 
     * @return the disposal
     */
    public DisposalReferenceValue getType(Integer refDisposalTypeId) {
        if (refDisposalTypeId == null) {
            throw new IllegalArgumentException("refDisposalTypeId: null");
        }
        RefDisposalTypeProcessor processor = new RefDisposalTypeProcessor();
        setRowProcessor(processor);
        execute(new Object[] { refDisposalTypeId });
        return processor.getType();
    }

}