//package uk.gov.courtservice.xhibit.courtlog.flr;
//
//import java.util.Date;
//
//import javax.sql.DataSource;
//
//import org.apache.log4j.Logger;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.framework.testutils.StandAloneDataSource;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogScheduledHearingValue;
//
///**
// * Test class for the <code>CourtLogEntryQueries</code> class.  If required to
// * run, the <code>StandAloneDataSource</code> can be configured via
// * command-line parameters, e.g.
// * "-Durl_key=jdbc:oracle:thin:@localhost:1521:ora9utf8" to specify which
// * database to point to.
// *
// * The tests performed by this class are dependent on hard-coded values.  These
// * could be acquired, but for simplicity will leave for now as hardcoded values.
// *
// * @author tz0d5m
// * @version $Id: TestCourtLogEntryQueries.java,v 1.3 2006/07/11 14:16:52 xzfdtb Exp $
// *
// * @see uk.gov.courtservice.xhibit.courtlog.flr.CourtLogEntryQueries
// */
//public class TestCourtLogEntryQueries extends TestCase
//{
//    private final Logger log = Logger.getLogger(this.getClass());
//
//    /** Instance of the class under test */
//    private CourtLogEntryQueries courtLogEntryQueries = null;
//
//    private Integer testCaseId = null;
//
//    /**
//     * Required constructor for JUnit framework to take the name of this test
//     * class as the parameter
//     *
//     * @param name The name of the test method
//     */
//    public TestCourtLogEntryQueries(String name)
//    {
//        super(name);
//    }
//
//    /**
//     * Overridden version of setUp() used to create the class under test.
//     *
//     * @throws Exception If an error occurs.
//     *
//     * @see junit.framework.TestCase#setUp()
//     */
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//
//        this.courtLogEntryQueries = new CourtLogEntryQueriesTestImplementation();
//        // @todo could do with acquiring this dynamically...
//        this.testCaseId = new Integer(29);
//    }
//
//    /**
//     * @see uk.gov.courtservice.xhibit.courtlog.flr.CourtLogEntryQueries
//     *      #getCourtLogEntries(java.lang.Integer)
//     */
//    public void testGetCourtLogEntriesIntegerValidInput()
//    {
//        // now test that an empty array is returned with an invalid id...
//        final XhbCourtLogEntryBasicValue[] values =
//                this.courtLogEntryQueries.getCourtLogEntries(this.testCaseId);
//        validateCourtLogEntry(values);
//
//        // could do with validating the exact number, but manually check for now...
//        log.debug("testGetCourtLogEntriesIntegerValidInput() - Found "
//                        + values.length + " entries");
//    }
//
//    /**
//     * @see uk.gov.courtservice.xhibit.courtlog.flr.CourtLogEntryQueries
//     *      #getCourtLogEntries(java.lang.Integer)
//     */
//    public void testGetCourtLogEntriesIntegerInvalidInput()
//    {
//        try
//        {
//            this.courtLogEntryQueries.getCourtLogEntries(null);
//            fail("Should have thrown an IllegalArgumentException");
//        }
//        catch (IllegalArgumentException e)
//        {
//            // ignore, this is the correct behaviour...
//        }
//
//        // now test that an empty array is returned with an invalid id...
//        final XhbCourtLogEntryBasicValue[] values =
//                this.courtLogEntryQueries.getCourtLogEntries(new Integer(-1));
//
//        assertNotNull("Returned values should not be null", values);
//        assertTrue("Array should be empty", (values.length == 0));
//    }
//
//    /**
//     * @see uk.gov.courtservice.xhibit.courtlog.flr.CourtLogEntryQueries
//     *      #getCourtLogEntries(java.lang.Integer, java.util.Date, java.util.Date)
//     */
//    public void testGetCourtLogEntriesIntegerDateDateValidInput()
//    {
//        // to test this we shall do all values for case for all time...
//        final XhbCourtLogEntryBasicValue[] values =
//                this.courtLogEntryQueries.getCourtLogEntries(this.testCaseId,
//                                new Date(0), new Date(Long.MAX_VALUE));
//        validateCourtLogEntry(values);
//
//        // could do with validating the exact number, but manually check for now...
//        log.debug("testGetCourtLogEntriesIntegerDateDateValidInput() - Found "
//                        + values.length + " entries");
//    }
//
//    /**
//     * @see uk.gov.courtservice.xhibit.courtlog.flr.CourtLogEntryQueries
//     *      #getCourtLogEntries(java.lang.Integer, java.util.Date, java.util.Date)
//     */
//    public void testGetCourtLogEntriesIntegerDateDateInvalidInput()
//    {
//        try
//        {
//            this.courtLogEntryQueries.getCourtLogEntries(null, new Date(), new Date());
//            fail("Should have thrown an IllegalArgumentException");
//        }
//        catch (IllegalArgumentException e)
//        {
//            // ignore, this is the correct behaviour...
//        }
//
//        try
//        {
//            this.courtLogEntryQueries.getCourtLogEntries(new Integer(-1), null, new Date());
//            fail("Should have thrown an IllegalArgumentException");
//        }
//        catch (IllegalArgumentException e)
//        {
//            // ignore, this is the correct behaviour...
//        }
//
//        try
//        {
//            this.courtLogEntryQueries.getCourtLogEntries(new Integer(-1), new Date(), null);
//            fail("Should have thrown an IllegalArgumentException");
//        }
//        catch (IllegalArgumentException e)
//        {
//            // ignore, this is the correct behaviour...
//        }
//
//        final XhbCourtLogEntryBasicValue[] values =
//                this.courtLogEntryQueries.getCourtLogEntries(new Integer(-1),
//                                                             new Date(),
//                                                             new Date());
//
//        assertNotNull("Returned values should not be null", values);
//        assertTrue("Array should be empty", (values.length == 0));
//    }
//
//    /**
//     * @see uk.gov.courtservice.xhibit.courtlog.flr.CourtLogEntryQueries
//     *      #getLastCourtLogEntry(java.lang.Integer, java.lang.Integer)
//     */
//    public void testGetLastCourtLogEntryValidInput()
//    {
//        final XhbCourtLogEntryBasicValue value =
//                this.courtLogEntryQueries.getLastCourtLogEntry(this.testCaseId,
//                                                               new Integer(60102));
//        validateCourtLogEntry(value);
//
//        // @todo need to test to ensure that this really is the last entry...
//        // manually testing/checking...
//        //System.err.println("!!!!value = " + value);
//    }
//
//    /**
//     * Test all invalid argument possibilities for the
//     * getLastCourtLogEntry() method.
//     *
//     * @see uk.gov.courtservice.xhibit.courtlog.flr.CourtLogEntryQueries
//     *      #getLastCourtLogEntry(java.lang.Integer, java.lang.Integer)
//     */
//    public void testGetLastCourtLogEntryInvalidInput()
//    {
//        try
//        {
//            this.courtLogEntryQueries.getLastCourtLogEntry(null, new Integer(-1));
//            fail("Should have thrown an IllegalArgumentException");
//        }
//        catch (IllegalArgumentException e)
//        {
//            // ignore, this is the correct behaviour...
//        }
//
//        try
//        {
//            this.courtLogEntryQueries.getLastCourtLogEntry(new Integer(-1), null);
//            fail("Should have thrown an IllegalArgumentException");
//        }
//        catch (IllegalArgumentException e)
//        {
//            // ignore, this is the correct behaviour...
//        }
//
//        final XhbCourtLogEntryBasicValue value =
//                this.courtLogEntryQueries.getLastCourtLogEntry(new Integer(-1),
//                                                               new Integer(-1));
//        assertNull("getLastCourtLogEntry() for illegal arguments must return "
//                        + " null", value);
//    }
//
//    /**
//     * @see uk.gov.courtservice.xhibit.courtlog.flr.CourtLogEntryQueries
//     *      #getLastCourtLogEntry(java.lang.Integer, java.lang.String,
//     *      java.util.Date, java.util.Date)
//     */
//    public void testGetCourtLogEntriesIntegerStringDateDateValidInput()
//    {
//        final XhbCourtLogEntryBasicValue[] values =
//            this.courtLogEntryQueries.getCourtLogEntries(this.testCaseId,
//                                                         "Charges",
//                                                         new Date(0),
//                                                         new Date(Long.MAX_VALUE));
//        validateCourtLogEntry(values);
//
//        // could do with validating the exact number, but manually check for now...
//        log.debug("testGetCourtLogEntriesIntegerStringDateDateValidInput() - Found "
//                        + values.length + " entries");
//    }
//
//    /**
//     * @see uk.gov.courtservice.xhibit.courtlog.flr.CourtLogEntryQueries
//     *      #getCourtLogEntries(java.lang.Integer, java.lang.String,
//     *      java.util.Date, java.util.Date)
//     */
//    public void testGetCourtLogEntriesIntegerStringDateDateInvalidInput()
//    {
//        try
//        {
//            this.courtLogEntryQueries.getCourtLogEntries(null, "", new Date(), new Date());
//            fail("Should have thrown an IllegalArgumentException");
//        }
//        catch (IllegalArgumentException e)
//        {
//            // ignore, this is the correct behaviour...
//        }
//
//        try
//        {
//            this.courtLogEntryQueries.getCourtLogEntries(new Integer(-1), null, new Date(), new Date());
//            fail("Should have thrown an IllegalArgumentException");
//        }
//        catch (IllegalArgumentException e)
//        {
//            // ignore, this is the correct behaviour...
//        }
//
//        try
//        {
//            this.courtLogEntryQueries.getCourtLogEntries(new Integer(-1), "", null, new Date());
//            fail("Should have thrown an IllegalArgumentException");
//        }
//        catch (IllegalArgumentException e)
//        {
//            // ignore, this is the correct behaviour...
//        }
//
//        try
//        {
//            this.courtLogEntryQueries.getCourtLogEntries(new Integer(-1), "", new Date(), null);
//            fail("Should have thrown an IllegalArgumentException");
//        }
//        catch (IllegalArgumentException e)
//        {
//            // ignore, this is the correct behaviour...
//        }
//
//        final XhbCourtLogEntryBasicValue[] values =
//                this.courtLogEntryQueries.getCourtLogEntries(new Integer(-1),
//                                                             "",
//                                                             new Date(),
//                                                             new Date());
//
//        assertNotNull("Returned values should not be null", values);
//        assertTrue("Array should be empty", (values.length == 0));
//    }
//
//
//    /**
//     * @see uk.gov.courtservice.xhibit.courtlog.flr.CourtLogEntryQueries
//     *      #getCourtLogEntries(java.lang.Integer, java.lang.String)
//     */
//    public void testGetCourtLogEntriesIntegerStringValidInput()
//    {
//        final XhbCourtLogEntryBasicValue[] values =
//            this.courtLogEntryQueries.getCourtLogEntries(this.testCaseId,
//                                                         "Charges");
//        validateCourtLogEntry(values);
//
//        // could do with validating the exact number, but manually check for now...
//        log.debug("testGetCourtLogEntriesIntegerStringValidInput() - Found "
//                        + values.length + " entries");
//    }
//
//    /**
//     * @see uk.gov.courtservice.xhibit.courtlog.flr.CourtLogEntryQueries
//     *      #getCourtLogEntries(java.lang.Integer, java.lang.String)
//     */
//    public void testGetCourtLogEntriesIntegerStringInvalidInput()
//    {
//        try
//        {
//            this.courtLogEntryQueries.getCourtLogEntries(null, "");
//            fail("Should have thrown an IllegalArgumentException");
//        }
//        catch (IllegalArgumentException e)
//        {
//            // ignore, this is the correct behaviour...
//        }
//
//        try
//        {
//            this.courtLogEntryQueries.getCourtLogEntries(new Integer(-1), null);
//            fail("Should have thrown an IllegalArgumentException");
//        }
//        catch (IllegalArgumentException e)
//        {
//            // ignore, this is the correct behaviour...
//        }
//
//        final XhbCourtLogEntryBasicValue[] values =
//                this.courtLogEntryQueries.getCourtLogEntries(new Integer(-1), "");
//
//        assertNotNull("Returned values should not be null", values);
//        assertTrue("Array should be empty", (values.length == 0));
//    }
//
//    /**
//     * @see uk.gov.courtservice.xhibit.courtlog.flr.CourtLogEntryQueries
//     *      #getCourtLogScheduledHearingValues(java.lang.Integer)
//     */
//    public void testGetCourtLogScheduledHearingValuesValidInput()
//    {
//        final CourtLogScheduledHearingValue[] values =
//            this.courtLogEntryQueries.getCourtLogScheduledHearingValues(this.testCaseId);
//
//        assertNotNull("Values should not be null", values);
//        assertTrue("Array should not be empty", (values.length > 0));
//
//        // could do with validating the exact number, but manually check for now...
//        log.debug("testGetCourtLogScheduledHearingValuesValidInput() - Found "
//                        + values.length + " entries");
//    }
//
//    /**
//     * @see uk.gov.courtservice.xhibit.courtlog.flr.CourtLogEntryQueries
//     *      #getCourtLogScheduledHearingValues(java.lang.Integer)
//     */
//    public void testGetCourtLogScheduledHearingValuesInvalidInput()
//    {
//        try
//        {
//            this.courtLogEntryQueries.getCourtLogScheduledHearingValues(null);
//            fail("Should have thrown an IllegalArgumentException");
//        }
//        catch (IllegalArgumentException e)
//        {
//            // ignore, this is the correct behaviour...
//        }
//
//        final CourtLogScheduledHearingValue[] values =
//            this.courtLogEntryQueries.getCourtLogScheduledHearingValues(new Integer(-1));
//
//        assertNotNull("Returned values should not be null", values);
//        assertTrue("Array should be empty", (values.length == 0));
//
//        for (int i = 0, n = values.length; i < n; i++)
//        {
//            assertNotNull("scheduledHearingId cannot be null",
//                            values[i].getScheduledHearingId());
//            assertNotNull("hearingType cannot be null", values[i].getHearingType());
//        }
//    }
//
//
//    //*************************************************************************
//    //*************************************************************************
//    //*************************************************************************
//
//
//    /**
//     * Private utility class to ensure that all of the fields that are required
//     * to be not <i>null</i> on the database have been set up correctly.
//     *
//     * @param value The <code>XhbCourtLogEntryBasicValue</code> to test.
//     */
//    private void validateCourtLogEntry(XhbCourtLogEntryBasicValue[] values)
//    {
//        assertNotNull("Values should not be null", values);
//        assertTrue("Array should not be empty", (values.length > 0));
//
//        for (int i = 0, n = values.length; i < n; i++)
//        {
//            validateCourtLogEntry(values[i]);
//        }
//    }
//
//    /**
//     * Private utility class to ensure that all of the fields that are required
//     * to be not <i>null</i> on the database have been set up correctly.
//     *
//     * @param value The <code>XhbCourtLogEntryBasicValue</code> to test.
//     */
//    private void validateCourtLogEntry(XhbCourtLogEntryBasicValue value)
//    {
//        assertNotNull("value cannot be null", value);
//
//        assertNotNull("entryId cannot be null", value.getEntryId());
//        assertNotNull("caseId cannot be null", value.getCaseId());
//        assertNotNull("version cannot be null", value.getVersion());
//        assertNotNull("lastUpdatedBy cannot be null", value.getLastUpdatedBy());
//        assertNotNull("createdBy cannot be null", value.getCreatedBy());
//        assertNotNull("creationDate cannot be null", value.getCreationDate());
//        assertNotNull("lastUpdateDate cannot be null", value.getLastUpdateDate());
//        assertNotNull("dateTime cannot be null", value.getDateTime());
//        assertNotNull("eventDescId cannot be null", value.getEventDescId());
//        assertNotNull("logEntryXml cannot be null", value.getLogEntryXml());
//    }
//
//    /**
//     * A custom implementation of the <code>CourtLogEntryQueries</code> class
//     * to allow testing to be performed outside of an application server.  This
//     * class overrides the getDataSource() method to return an implementation
//     * of a <code>StandAloneDataSource</code>.
//     *
//     * @author tz0d5m
//     */
//    private class CourtLogEntryQueriesTestImplementation extends CourtLogEntryQueries
//    {
//        /**
//         * Override the default implementation to return a
//         * <code>StandAloneDataSource</code>.
//         *
//         * @return A <code>StandAloneDataSource</code>.
//         *
//         * @see uk.gov.courtservice.xhibit.courtlog.flr.CourtLogEntryQueries
//         *      #getDataSource()
//         */
//        protected DataSource getDataSource()
//        {
//            // this is set up using command-line parameters...
//            // e.g. -Durl_key=jdbc:oracle:thin:@localhost:1521:ora9utf8
//            return new StandAloneDataSource();
//        }
//    }
//}
//