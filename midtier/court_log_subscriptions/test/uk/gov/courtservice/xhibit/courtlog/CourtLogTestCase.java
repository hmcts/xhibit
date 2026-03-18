//package uk.gov.courtservice.xhibit.courtlog;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//
///**
// * Parent class to be used by test classes that require any currently existing
// * entity in the database for modifications and testing of.  The transaction
// * will always be rolled back in the tearDown method.
// *
// * @author tz0d5m
// */
//public class CourtLogTestCase extends TransactionTestCase
//{
//    /**
//     * Required constructor for JUnit framework to take the name of this test
//     * class as the parameter.  The transaction will always be rolled back.
//     *
//     * @param name The name of this test class
//     * @throws NamingException if the super class (<code>TransactionTestCase</code>)
//     *         fails in the lookup of the <code>DataSource</code>
//     */
//    public CourtLogTestCase(String name) throws NamingException
//    {
//        super(name, true);
//    }
//
//    /*
//     * Required constructor for JUnit framework to take the name of this test
//     * class as the parameter.  The transaction will always be rolled back.
//     *
//     * @param name The name of this test class
//     * @param commit Whether to commit the transaction
//     * @throws NamingException if the super class (<code>TransactionTestCase</code>)
//     *         fails in the lookup of the <code>DataSource</code>
//     *
//
//
//    // ANDY:: I've removed this constructor as the boolean is used to represent
//    // rollback, not commit.  And it is not being used anywhere currently.
//
//    public CourtLogTestCase(String name, boolean commit) throws NamingException
//    {
//        super(name, commit);
//    }
//*/
//
//    /**
//     * Aquire a primary key for a witness found in the database.
//     * If one cannot be found, then this will force the test to fail.
//     *
//     * @return A <code>Long</code> representing a primary key found
//     */
//    public Integer getWitnessId()
//    {
//        return getColumnIntegerValues("xhb_witness", "witness_id")[0];
//    }
//
//    /**
//     * Aquire a primary key for a court log entry found in the database.
//     * If one cannot be found, then this will force the test  to fail.
//     *
//     * @return A <code>Long</code> representing a primary key found
//     */
//    public Long getCourtLogEntryId()
//    {
//        return getColumnLongValues("xhb_court_log_entry", "entry_id")[0];
//    }
//
//    /**
//     * Aquire a primary key for a case found in the database.
//     * If one cannot be found, then this will force the test  to fail.
//     *
//     * @return A <code>Long</code> representing a primary key found
//     */
//    public Integer getCaseId()
//    {
//        return getColumnIntegerValues("xhb_case", "case_id")[0];
//    }
//
//    /**
//     * Aquire a primary key for a hearing found in the database.
//     * If one cannot be found, then this will force the test  to fail.
//     *
//     * @return A <code>Long</code> representing a primary key found
//     */
//    public Integer getHearingId()
//    {
//        return getColumnIntegerValues("xhb_hearing", "hearing_id")[0];
//    }
//
//    /**
//     * Aquire a primary key for a scheduled hearing found in the database.
//     * If one cannot be found, then this will force the test  to fail.
//     *
//     * @return A <code>Long</code> representing a primary key found
//     */
//    public Integer getScheduledHearingId()
//    {
//        return getColumnIntegerValues("xhb_scheduled_hearing", "scheduled_hearing_id")[0];
//    }
//
//    /**
//     * Aquire the hearing id and defendant on case id for a def hearing record
//     * found in the database.  If one cannot be found, then this will force the
//     * test to fail.
//     *
//     * @return A <code>Long[]</code> containing the hearing id at Long[0], and
//     *         the defendant on case id at Long[1].
//     */
//    public Integer[] getDefHearingRecordHearingIdDefendantOnCaseId()
//    {
//        return getColumnIntegerValues("xhb_def_hearing_record",
//                "hearing_id, defendant_on_case_id");
//    }
//
//    /**
//     * Utility method to acquire the <code>Integer[]</code> values of a rows
//     * (which one is not guaranteed) columns which are named by the input
//     * parameter. Generally used to acquire any primary key for a table, which
//     * can be used to look up the required entity and modify to start off the
//     * test case (without having to hard-code any primary keys).  This method
//     * is guaranteed to not return <i>null</i>.  The test case will fail if
//     * there are any problems, or if there are no results in the table to query.
//     *
//     * @param tableName Name of the table to which we want to do the lookup on
//     * @param columns Name of the column/s required to be returned
//     * @return A <code>Integer[]</code> containing the Integerss returned from
//     *         the selected query columns.
//     */
//    protected Integer[] getColumnIntegerValues(String tableName, String columns)
//    {
//        Long[] longValues = getColumnLongValues(tableName, columns);
//        Integer[] values = new Integer[longValues.length];
//
//        for (int i = 0; i < longValues.length; i++)
//        {
//            values[i] = new Integer(longValues[i].intValue());
//        }
//
//        return values;
//    }
//
//    /**
//     * Utility method to acquire the <code>Long[]</code> values of a rows (which
//     * one is not guaranteed) columns which are named by the input parameter.
//     * Generally used to acquire any primary key for a table, which can be
//     * used to look up the required entity and modify to start off the test
//     * case (without having to hard-code any primary keys).  This method is
//     * guaranteed to not return <i>null</i>.  The test case will fail if there
//     * are any problems, or if there are no results in the table to query.
//     *
//     * @param tableName Name of the table to which we want to do the lookup on
//     * @param columns Name of the column/s required to be returned
//     * @return A <code>Long[]</code> containing the Longs returned from the
//     *         selected query columns.
//     */
//    protected Long[] getColumnLongValues(String tableName, String columns)
//    {
//        // no need to validate parameters, as private we know that the
//        // values passed will not be null...
//
//        Statement stmt = null;
//        ResultSet rset = null;
//
//        try
//        {
//            stmt = super.connection.createStatement();
//            rset = stmt.executeQuery("SELECT " + columns + " FROM " + tableName);
//
//            if (rset.next())
//            {
//                final Long[] returnValues =
//                        new Long[rset.getMetaData().getColumnCount()];
//
//                for (int i = 0; i < returnValues.length; i++)
//                {
//                    returnValues[i] = new Long(rset.getLong(i + 1));
//                }
//
//                return returnValues;
//            }
//        }
//        catch (SQLException e)
//        {
//            fail("SQLException::" + e.getMessage());
//        }
//        finally
//        {
//            // ensure that we always close the resources used...
//            closeResources(stmt, rset);
//        }
//
//        // always fail as the table is empty...
//        fail("The table " + tableName + " is empty.");
//        return null;
//    }
//
//    /**
//     * Utility method to close the passed in resources.
//     *
//     * @param stmt The <code>Statement</code> to close.
//     * @param rset The <code>ResultSet</code> to close.
//     * @see #closeResultSet(java.sql.ResultSet)
//     * @see #closeStatement(java.sql.Statement)
//     */
//    private void closeResources(Statement stmt, ResultSet rset)
//    {
//        closeResultSet(rset);
//        closeStatement(stmt);
//    }
//
//    /**
//     * Utility method to close the passed in <code>ResultSet</code> if it
//     * is not <i>null</i>.  Any problems when closing will be printed to the
//     * standard error output stream, and the exception then swallowed.
//     *
//     * @param rset The <code>ResultSet</code> to close.
//     */
//    private void closeResultSet(ResultSet rset)
//    {
//        if (rset != null)
//        {
//            try
//            {
//                rset.close();
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * Utility method to close the passed in <code>Statement</code> if it
//     * is not <i>null</i>.  Any problems when closing will be printed to the
//     * standard error output stream, and the exception then swallowed.
//     *
//     * @param stmt The <code>Statement</code> to close.
//     */
//    private void closeStatement(Statement stmt)
//    {
//        if (stmt != null)
//        {
//            try
//            {
//                stmt.close();
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }
//}
//