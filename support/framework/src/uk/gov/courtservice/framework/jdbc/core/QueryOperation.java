package uk.gov.courtservice.framework.jdbc.core;

import javax.sql.DataSource;

/**
 * <p>
 * Title: QueryOperation
 * </p>
 * <p>
 * Description: Select operation
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Meeraj Kunnumpurath
 * @version 1.0
 */
public class QueryOperation extends SqlOperation {

    /**
     * Initializes the object
     * 
     * @param Datasource
     *            to use
     * @param SQL
     *            to use
     */
    public QueryOperation(DataSource ds, String sql) {
        super(ds, sql);
    }

    /**
     * Sets the row processor for the query
     * 
     * @param rowProcessor
     */
    public void setRowProcessor(RowProcessor rowProcessor) {
        helper.setRowProcessor(rowProcessor);
    }

    public void setMaxRowCount(int rowCount) {
        helper.setMaxRowCount(rowCount);
    }
}