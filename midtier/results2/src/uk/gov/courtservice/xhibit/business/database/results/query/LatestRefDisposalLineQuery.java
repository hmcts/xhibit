package uk.gov.courtservice.xhibit.business.database.results.query;

// JDK
import java.sql.Types;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.results.processor.RefDisposalLinesProcessor;

/**
 * <p>
 * Title: RefDisposalLineQuery
 * </p>
 * <p>
 * Description: Query the database for disposal line reference data
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
public class LatestRefDisposalLineQuery extends QueryOperation {

    // The file that contains the SQL for this query, uses case id.
    private static final String SQL_FILE = "config/database/query/results/LatestRefDisposalLine.sql";

    // SQL used by this query
    private static final String SQL = readSql(SQL_FILE);

    /**
     * Constructor initializes the datasource and SQL
     */
    public LatestRefDisposalLineQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL);
        registerInTypes(new int[] { Types.NUMERIC, Types.VARCHAR });
    }

    /**
     * Get the disposal (DisposalReferenceLineValue) list for the latest
     * disposal with the given code
     * 
     * @return the disposal lines for the given code
     */
    public List getLineList(Integer courtId, String disposalCode) {
        if (courtId == null) {
            throw new IllegalArgumentException("courtId: null");
        }
        if (disposalCode == null) {
            throw new IllegalArgumentException("disposalCode: null");
        }
        RefDisposalLinesProcessor processor = new RefDisposalLinesProcessor();
        setRowProcessor(processor);
        execute(new Object[] { courtId, disposalCode });
        return processor.getLineList();
    }

}