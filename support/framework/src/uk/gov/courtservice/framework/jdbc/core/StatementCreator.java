package uk.gov.courtservice.framework.jdbc.core;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <p>
 * Title: StatementCreator
 * </p>
 * <p>
 * Description: A factory for creating prepared and callable statements
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author XHIBIT User
 * @version 1.0
 */
public abstract class StatementCreator {

    // Singleton
    private static StatementCreator me = new DefaultStatementCreator();

    /**
     * Returns a statement creator instance
     * 
     * @return
     */
    public static StatementCreator getInstance() {
        return me;
    }

    /**
     * Creates a prepared statement
     * 
     * @connection con
     * @param sql
     * @param parameter
     *            types that are
     * @param input
     *            arguments
     * @return
     * @throws SQLException
     */
    public abstract PreparedStatement createPreparedStatement(Connection con, String sql, Parameter[] params)
            throws SQLException;

    /**
     * Creates a callable statement
     * 
     * @connection con
     * @param sql
     * @param input
     *            types
     * @param output
     *            types
     * @param output
     *            arguments
     * @return
     * @throws SQLException
     */
    public abstract CallableStatement createCallableStatement(Connection con, String call, Parameter[] params)
            throws SQLException;
}