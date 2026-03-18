package uk.gov.courtservice.framework.jdbc.core;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;
import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;

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
 * @version $Id: StoredProcedure.java,v 1.6 2006/06/05 12:30:16 bzjrnl Exp $
 */
public class StoredProcedure extends QueryOperation {
    /**
     * Initializes the object
     * 
     * @param Datasource
     *            to use
     * @param SQL
     *            to use
     */
    public StoredProcedure(DataSource ds, String sql) {
        super(ds, sql);
    }

    /**
     * Executes the SQL operation
     * 
     * @param Arguments
     *            to the operation
     * @throws DataAccessException
     */
    public int execute(Object args[]) throws DataAccessException {
        Parameter[] params = getParameters(args, OracleTypes.CURSOR);
        return helper.executeStoredProcedure(params);
    }
}
