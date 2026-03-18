package uk.gov.courtservice.framework.testutils.junit;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import junit.framework.TestCase;
import uk.gov.courtservice.framework.testutils.StandAloneDataSource;

/**
 * <p>
 * Title: Test Case class that uses setUp() and tearDown to start and
 * rollback/commit an user transaction and provides access to a DB connection
 * through a DataSource.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Using this class as a super class for JUnitEE tests, we can sucessfully test
 * complex CMR requiring transactions. As a bonus, this class can rollback the
 * transaction reducing the complexity of any subsequent tearDown().
 * </p>
 * <p>
 * Includes some utility methods for extarcting values from a ResultSet
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */

public abstract class TransactionTestCase extends TestCase {
    private UserTransaction transaction;

    protected DataSource dataSource;

    private boolean rollback;

    protected Connection connection;

    public static final String DEFAULT_DATA_SOURCE = "XhibitOracleTxDataSource";

    public static final String DEFAULT_TRANSACTION = "javax/transaction/UserTransaction";

    /**
     * Constructor retrieving the DEFAULT_TRANSACTION user transaction from the
     * server in which the unit test is running and initialising a connection
     * from the DEFAULT_DATA_SOURCE.
     * 
     * @param s
     *            The name of the unit test.
     * @param doRollback
     *            Whether to always rollback at the end of the transaction or to
     *            commit.
     * @throws javax.naming.NamingException
     *             When there is as issue in retrieving the transaction or data
     *             source.
     */
    protected TransactionTestCase(String s, boolean rollback) throws javax.naming.NamingException {
        this(s, rollback, DEFAULT_DATA_SOURCE, DEFAULT_TRANSACTION);
    }

    /**
     * Constructor retrieving a named user transaction from the server in which
     * the unit test is running and initialising a connection from the named
     * DataSource.
     * 
     * @param s
     *            The name of the unit test.
     * @param doRollback
     *            Whether to always rollback at the end of the transaction or to
     *            commit.
     * @param dataSourceName
     *            The JNDI name of the DataSource to retrieve a connection from.
     * @param transactionName
     *            The JNDI name of the transaction to use.
     * @throws javax.naming.NamingException
     *             When there is as issue in retrieving the transaction or data
     *             source.
     */
    protected TransactionTestCase(String s, boolean rollback, String dataSourceName, String transactionName)
            throws javax.naming.NamingException {
        super(s);
        this.rollback = rollback;
        InitialContext ic = null;

        try {
            dataSource = new StandAloneDataSource();
        } finally {
            if (ic != null)
                ic.close();
        }
    }

    /**
     * Starts the transaction.
     * 
     * @throws Exception
     *             When there is a problem in the transaction.
     */
    protected void setUp() throws Exception {
        super.setUp();
        transaction.begin();
        connection = dataSource.getConnection();
    }

    /**
     * Rolls back the transaction.
     * 
     * @throws Exception
     *             When there is a problem in rolling back the transaction.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        if (rollback)
            transaction.rollback();
        else
            transaction.commit();
        connection.close();
        connection = null;
    }

    /**
     * Down and dirty way of getting a string from a Clob. Will break in the
     * case of an exceptionally large Clob.
     * 
     * @param clob
     *            the Clob to convert to a String.
     * @return The String representation of the Clob.
     * @throws SQLException
     *             When there is a problem communicating with the database.
     */
    public static String getStringFromClob(Clob clob) throws SQLException {
        if (clob == null)
            return null;
        else {
            return clob.getSubString(1l, (int) clob.length());
        }
    }

    /**
     * Down and dirty way of getting an Integer out of a database, in particular
     * for Oracle databases which have a habit of returning BigDecimal.
     * 
     * @param rs
     *            The result set from which to retrieve the column.
     * @param column
     *            The name of the column.
     * @return An Integer representation of the column which will be null if the
     *         entry is null
     * @throws SQLException
     *             When there is a problem connecting to the database.
     */
    public static Integer getIntegerFromResultSetColumn(ResultSet rs, String column) throws SQLException {
        int value = rs.getInt(column);
        if (rs.wasNull())
            return null;
        return new Integer(value);
    }

    /**
     * Down and dirty way of getting a Long out of a database, in particular for
     * Oracle databases which have a habit of returning BigDecimal.
     * 
     * @param rs
     *            The result set from which to retrieve the column.
     * @param column
     *            The name of the column.
     * @return A Long representation of the column which will be null if the
     *         entry is null
     * @throws SQLException
     *             When there is a problem connecting to the database.
     */
    public static Long getLongFromResultSetColumn(ResultSet rs, String column) throws SQLException {
        long value = rs.getLong(column);
        if (rs.wasNull())
            return null;
        return new Long(value);
    }
}