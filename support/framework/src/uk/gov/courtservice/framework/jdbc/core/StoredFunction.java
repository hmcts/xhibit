package uk.gov.courtservice.framework.jdbc.core;

import javax.sql.DataSource;

import uk.gov.courtservice.framework.jdbc.core.JdbcHelper.RowMapper;

/**
 * Although this class is named <code>StoredFunction</code> it would be more
 * descriptive to name in StoredProcedure, however, that name is used to
 * represent a stored procedure/function that only returns a
 * <code>ResultSet</code> as its first parameter.
 * 
 * This class allows the return of any single non-database specific class, or no
 * return value, from a stored procedure/function or anonymous block (if the
 * database supports them). Permitted return values include <code>String</code>s
 * (VARCHAR's) and <code>int</code>s (INTEGER's), but DO NOT include
 * <code>ResultSet</code>'s, or custom database types.
 * 
 * @author tz0d5m
 * @version $Id: StoredFunction.java,v 1.3 2006/06/05 12:30:16 bzjrnl Exp $
 */
public class StoredFunction extends SqlOperation {
    public StoredFunction(DataSource ds, String sql) {
        super(ds, sql);
    }

    public int execute(Object[] args) {
        throw new UnsupportedOperationException("execute is not supported, "
                + "please use one of the executeFunction methods");
    }

    public Object executeFunction(Object[] args) {
        Parameter[] params = getParameters(args);
        return helper.executeStoredFunction(params);
    }

    public Object executeFunction(Object[] args, int outputType) {
        Parameter[] params = getParameters(args, outputType);
        return helper.executeStoredFunction(params);
    }
    
    @SuppressWarnings("unchecked")
	public Object executeStoredFunctionCursorSingle(Parameter[] params, RowMapper rm) {
        return helper.executeStoredFunctionCursorSingle(params, rm);
    }
}
