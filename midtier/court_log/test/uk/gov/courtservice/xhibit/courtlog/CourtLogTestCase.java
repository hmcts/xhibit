//package uk.gov.courtservice.xhibit.courtlog;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//import javax.naming.NamingException;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
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
//    private static final Long    INVALID_ENTITY_ID_LONG    = new Long(-1);
//    private static final Integer INVALID_ENTITY_ID_INTEGER = new Integer(-1);
//
//    protected final Logger log = CSServices.getLogger(getClass());
//
//    /* Constant used to represent one hour in milliseconds
//    private static final int ONE_HOUR = 1000 * 60 * 60; */
//    /* Constant used to represent one day in milliseconds
//    private static final int ONE_DAY = ONE_HOUR * 24; */
//
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
//     * Aquire a primary key for a court log event desc found in the database.
//     * If one cannot be found, then this will force the test  to fail.
//     *
//     * @return A <code>Long</code> representing a primary key found
//     */
//    public Integer getCourtLogEventDescId()
//    {
//        return getColumnIntegerValues("xhb_court_log_event_desc", "event_desc_id")[0];
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
//     * Aquire a primary key for a defendant on case found in the database.
//     * If one cannot be found, then this will force the test  to fail.
//     *
//     * @return A <code>Long</code> representing a primary key found
//     */
//    public Integer getDefendantOnCaseId()
//    {
//        return getColumnIntegerValues("xhb_defendant_on_case", "defendant_on_case_id")[0];
//    }
//
//    /**
//     * Aquire a primary key for a defendant on offence found in the database.
//     * If one cannot be found, then this will force the test  to fail.
//     *
//     * @return A <code>Long</code> representing a primary key found
//     */
//    public Integer getDefendantOnOffenceId()
//    {
//        return getColumnIntegerValues("xhb_defendant_on_offence", "defendant_on_offence_id")[0];
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
//     * Aquire the category description for a court log category desc
//     * found in the database.  If one cannot be found, then this will force the
//     * test to fail.
//     *
//     * @return A <code>String</code> containing the category description.
//     */
//    public String getCourtLogCategoryDescDescription()
//    {
//        return getColumnStringValues("xhb_court_log_category_desc",
//                "category_description")[0];
//    }
//
//    /**
//     * Acquire a primary key that should not be found in the database - a
//     * negative number.
//     *
//     * @return A <code>Long</code> representing a primary key that should not
//     *         be found in the database
//     */
//    public Long getInvalidEntityIdLong()
//    {
//        return INVALID_ENTITY_ID_LONG;
//    }
//
//    /**
//     * Acquire a primary key that should not be found in the database - a
//     * negative number.
//     *
//     * @return An <code>Integer</code> representing a primary key that should
//     *         not be found in the database
//     */
//    public Integer getInvalidEntityIdInteger()
//    {
//        return INVALID_ENTITY_ID_INTEGER;
//    }
//
//    /*
//     * Utility method to construct a number of court log entries of the passed
//     * in types for the specified case.  Following this method call all
//     * previous court log entries for the case will be removed, and replaced
//     * with the new ones.
//     *
//     * @param caseId The case the log entries are to be associated with
//     * @param eventTypeIds an <code>Integer[]</code> of all event types to
//     *        create.
//     * @param caseType The case type to set the case to
//     * @param numDaysEvents The multiple of the event types to create, by day.
//     * @return The number of newly created court log entries.
//     *
//    protected static int constructCourtLog(Integer caseId,
//                                           Integer scheduledHearingId,
//                                           Integer[] eventTypeIds,
//                                           String caseType,
//                                           int numDaysEvents)
//    {
//        final XhbCourtLogEventDesc[] eventTypes = new XhbCourtLogEventDesc[eventTypeIds.length];
//
//        for (int i = 0; i < eventTypeIds.length; i++)
//        {
//            eventTypes[i] = EventHelper.getXhbCourtLogEventDescByEventType(eventTypeIds[i]);
//        }
//
//        XhbScheduledHearing xhbScheduledHearing = null;
//        XhbCase xhbCase = null;
//
//        if (scheduledHearingId != null)
//        {
//            xhbScheduledHearing = EntityHelper.getXhbScheduledHearing(scheduledHearingId);
//            xhbCase = xhbScheduledHearing.getXhbHearing().getXhbCase();
//        }
//        else
//        {
//            xhbCase = EntityHelper.getXhbCase(caseId);
//        }
//
//        xhbCase.setCaseType(caseType);
//
//        // ensure that there are no previous court log entries...
//        xhbCase.getXhbCourtLogEntries().clear();
//
//        int courtLogEventsCreated = 0;
//
//        // now construct the court log entries that we need for the tests...
//        for (int i = 0; i < numDaysEvents; i++)
//        {
//            for (int j = 0; j < eventTypes.length; j++)
//            {
//                final long tmp = System.currentTimeMillis() - (numDaysEvents * ONE_DAY)
//                        + (i * ONE_DAY) + (j * ONE_HOUR);
//                getNewCourtLogEntryId(xhbCase, xhbScheduledHearing, eventTypes[j], new Date(tmp));
//                courtLogEventsCreated++;
//            }
//        }
//
//        assertEquals("Incorrect number of events created",
//                    courtLogEventsCreated, (numDaysEvents * eventTypes.length));
//        return courtLogEventsCreated;
//    }
//*/
//
//    /*
//     * Utility method that constructs a court log entry without any attached
//     * defendant on case or offence, and also has a cleared out logEntryXml.
//     *
//     * @param xhbCase The case the log event is associated with
//     * @param xhbCourtLogEventDesc The event description for the new
//     *        court log event
//     * @param dateTime The date (and time) that the court log entry takes place
//     * @return The primary key of the newly created court log entry
//     *
//    private static Long getNewCourtLogEntryId(XhbCase xhbCase, XhbScheduledHearing xhbScheduledHearing,
//                                              XhbCourtLogEventDesc xhbCourtLogEventDesc,
//                                              Date dateTime)
//    {
//        // validation not needed as private method, we can guarantee what
//        // is passed to this method.
//
//        final XhbCourtLogEntryBasicValue value = new XhbCourtLogEntryBasicValue();
//        // not-null constraints on database...
//        value.setDateTime(dateTime);
//        value.setLogEntryXml("<test />");
//
//        final XhbCourtLogEntry courtLogEntry =
//                XhbCourtLogEntryBeanHelper2.createLocal(value,
//                        xhbCase, xhbScheduledHearing,
//                        xhbCourtLogEventDesc,  null, null);
//
//        // ensure that a court log entry was created...
//        assertNotNull("Error creating the value", courtLogEntry);
//        assertNotNull("Entry id returned was null", courtLogEntry.getEntryId());
//
//        return courtLogEntry.getEntryId();
//    }
//*/
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
//    private Integer[] getColumnIntegerValues(String tableName, String columns)
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
//    private Long[] getColumnLongValues(String tableName, String columns)
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
//     * Utility method to acquire the <code>String</code> values of a rows (which
//     * one is not guaranteed) columns which are named by the input parameter.
//     * This method is guaranteed to not return <i>null</i>.  The test case will
//     * fail if there are any problems, or if there are no results in the table
//     * to query.
//     *
//     * @param tableName Name of the table to which we want to do the lookup on
//     * @param columns Name of the column/s required to be returned
//     * @return A <code>String[]</code> containing the Strings returned from the
//     *         selected query columns.
//     */
//    private String[] getColumnStringValues(String tableName, String columns)
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
//                final String[] returnValues =
//                        new String[rset.getMetaData().getColumnCount()];
//
//                for (int i = 0; i < returnValues.length; i++)
//                {
//                    returnValues[i] = rset.getString(i + 1);
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