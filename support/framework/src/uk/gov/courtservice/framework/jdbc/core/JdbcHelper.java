package uk.gov.courtservice.framework.jdbc.core;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;
import uk.gov.courtservice.framework.jdbc.exception.ExceptionTranslator;

/**
 * <p>
 * Title: JdbcHelper
 * </p>
 * <p>
 * Description: This class uses inversion of control to hide JDBC API. This
 * class has some Oracle specific features in the way callable statements are
 * executed
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Meeraj Kunnumpurath
 * @version $Id: JdbcHelper.java,v 1.13 2013/11/15 16:19:53 hingstb Exp $
 */
public class JdbcHelper {
    // Logger
    private static final Logger log = Logger.getLogger(JdbcHelper.class);

    // Rowcount
    private int maxRowCount = -1;

    // Row processor for SQL queries
    private RowProcessor rowProcessor;

    // Datasource used for getting the connection
    private final DataSource ds;

    // SQL that is executed
    private final String sql;

    /**
     * Initializes the datasource and SQL
     * 
     * @param newDs
     * @param newSql
     */
    public JdbcHelper(DataSource newDs, String newSql) {
        if (newDs == null) {
            throw new IllegalArgumentException("constructor::newDs cannot be null");
        }

        if (newSql == null) {
            throw new IllegalArgumentException("constructor::sql cannot be null");
        }

        ds = newDs;
        sql = newSql;
    }

    /**
     * Set the maximum number of rows that should be processed
     * 
     * @param maxRowCount
     */
    public void setMaxRowCount(final int maxRowCount) {
        this.maxRowCount = maxRowCount;
    }

    /**
     * Sets the row processor for SQL queries
     * 
     * @param Row
     *            processor
     */
    public void setRowProcessor(RowProcessor newRowProcessor) {
        rowProcessor = newRowProcessor;
    }

    /**
     * Executes a SQL using prepared statement
     * 
     * @param params
     * @return Number of records returned
     * @throws DataAccessException
     */
    public int execute(Parameter[] params) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Get the connection
            con = ds.getConnection();
            log.debug("Connection created");

            // Prepare the statement
            ps = StatementCreator.getInstance().createPreparedStatement(con, sql, params);
            log.debug("Statement created");

            // Execute the query
            boolean hasResults = ps.execute();

            // Is this a query
            if (hasResults) {
                rs = ps.getResultSet();
                // altered to call the common procedure
                return processResultSet(rs);
            }

            return ps.getUpdateCount();
        } catch (SQLException ex) {
            throw ExceptionTranslator.getInstance().translate(ex);
        } finally {
            closeResources(rs, ps, con);
        }
    }

    /**
     * Executes a SQL update using prepared statement
     * 
     * @param params
     * @throws DataAccessException
     */
    public void executeUpdate(Parameter[] params) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Get the connection
            con = ds.getConnection();
            log.debug("Connection created");

            // Prepare the statement
            ps = StatementCreator.getInstance().createPreparedStatement(con, sql, params);
            log.debug("Statement created");

            // Execute the query and commit
            ps.execute();
            
            // Removed the commit as this is done elsewhere and invoking commit here results
            // in an 'Cannot call commit when using distributed transactions' error
            //con.commit();

            
        } catch (SQLException ex) {
            throw ExceptionTranslator.getInstance().translate(ex);
        } finally {
            closeResources(rs, ps, con);
        }
    }
    
    /**
     * Executes a stored procedure that return resultsets
     * 
     * @param params
     * @return int The number of rows retrieved.
     * @throws DataAccessException
     */
    public int executeStoredProcedure(Parameter[] params) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            // Get the connection
            con = ds.getConnection();
            log.debug("Connection created");

            // Prepare the statement
            cs = StatementCreator.getInstance().createCallableStatement(con, sql, params);
            log.debug("Statement created");

            // This is Oracle specific
            cs.execute();
            rs = (ResultSet) cs.getObject(1);

            // altered to call the common procedure
            return processResultSet(rs);
        } catch (SQLException ex) {
            throw ExceptionTranslator.getInstance().translate(ex);
        } finally {
            // Free the resources.
            closeResources(rs, cs, con);
        }
    }

    /**
     * Executes a stored function that return any non-database related object.
     * 
     * @param params
     * @return Object That must be cast externally to the required type.
     * @throws DataAccessException
     */
    public Object executeStoredFunction(Parameter[] params) {
        Connection con = null;
        CallableStatement cs = null;

        try {
            // Get the connection
            con = ds.getConnection();
            log.debug("Connection created");

            // Prepare the statement
            cs = StatementCreator.getInstance().createCallableStatement(con, sql, params);
            log.debug("Statement created");

            cs.execute();

            // only return the first parameter if it is an out parameter...
            if ((params.length > 0) && params[0].isOut()) {
                return cs.getObject(1);
            }

            return null;
        } catch (SQLException ex) {
            throw ExceptionTranslator.getInstance().translate(ex);
        } finally {
            // Free the resources.
            closeResources(null, cs, con);
        }
    }

    /**
     * A private common target for processing a <code>ResultSet</code>
     * 
     * @param rset
     *            A <code>ResultSet</code> that processing is to be done
     *            against
     * @return An <code>int</code> value representing the number of rows
     *         processed
     * @throws A
     *             <code>SQLException</code> if any database related exception
     *             occurs
     */
    private int processResultSet(final ResultSet rset) throws SQLException {
        final Row row = new Row(rset);
        // store the limit here as boolean to prevent unnecessary int checks
        final boolean limitCount = (this.maxRowCount != -1);

        log.debug("Query executed");

        int processedRows = 0;
        for (; rset.next(); processedRows++) {
            if (limitCount && (processedRows >= this.maxRowCount)) {
                break;
            }

            processRow(rowProcessor, row);
        }

        return processedRows;
    }

    /**
     * Processes a row
     * 
     * @param processor
     * @param row
     */
    private void processRow(RowProcessor processor, Row row) {
        if (processor.getChildProcessors().size() > 0) {
            // Ask all the children to process
            Iterator it = processor.getChildProcessors().iterator();
            while (it.hasNext()) {
                processRow((RowProcessor) it.next(), row);
            }
        }

        // Have processed all descendants
        processor.processRow(row);
    }

    protected void closeResources(ResultSet rset, Statement stmt, Connection con) {
        closeResultSet(rset);
        closeStatement(stmt);
        closeConnection(con);

        if (log.isDebugEnabled()) {
            log.debug("Resources released");
        }
    }

    /**
     * A true helper method used to close the passed in <code>ResultSet</code>
     * if it is not <i>null</i>. If any errors occur whilst closing they will
     * simply be logged and processing allowed to continue.
     * 
     * @param rset
     *            The <code>ResultSet</code> to close.
     */
    public static final void closeResultSet(final ResultSet rset) {
        if (rset != null) {
            try {
                rset.close();
            } catch (final Throwable t) {
                log.error("Error closing ResultSet", t);
            }
        }
    }

    /**
     * A true helper method used to close the passed in <code>Statement</code>
     * if it is not <i>null</i>. If any errors occur whilst closing they will
     * simply be logged and processing allowed to continue.
     * 
     * @param stmt
     *            The <code>Statement</code> to close.
     */
    public static final void closeStatement(final Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (final Throwable t) {
                log.error("Error closing Statement", t);
            }
        }
    }

    /**
     * A true helper method used to close the passed in <code>Connection</code>
     * if it is not <i>null</i>. If any errors occur whilst closing they will
     * simply be logged and processing allowed to continue.
     * 
     * @param con
     *            The <code>Connection</code> to close.
     */
    public static final void closeConnection(final Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (final Throwable t) {
                log.error("Error closing Connection", t);
            }
        }
    }
    
    /**
     * New code below to handle cursors returned as a result
     */
    public interface RowMapper<T> {
	  T mapRow(ResultSet rs) throws SQLException;
	}
    
    public <T> T executeStoredFunctionCursorSingle(Parameter[] params, RowMapper<T> mapper) {
    	  Connection con = null;
    	  CallableStatement cs = null;
    	  ResultSet rs = null;

    	  try {
    	    con = ds.getConnection();
    	    cs = StatementCreator.getInstance().createCallableStatement(con, sql, params);
    	    cs.execute();

    	    // Return value (pos 1) should be a REF CURSOR -> ResultSet
    	    Object ret = cs.getObject(1);
    	    if (!(ret instanceof ResultSet)) {
    	      return null; // or throw if you prefer
    	    }

    	    rs = (ResultSet) ret;
    	    if (rs.next()) {
    	      return mapper.mapRow(rs);
    	    }
    	    return null;
    	  } catch (SQLException ex) {
    	    throw ExceptionTranslator.getInstance().translate(ex);
    	  } finally {
    	    // Close cursor before statement/connection
    	    if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
    	    closeResources(null, cs, con);
    	  }
    	}
}
