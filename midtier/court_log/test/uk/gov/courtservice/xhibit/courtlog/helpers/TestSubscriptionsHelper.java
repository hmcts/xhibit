//package uk.gov.courtservice.xhibit.courtlog.helpers;
//
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.text.SimpleDateFormat;
//
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//
///**
// * <p>Title: TestSubscriptionsHelper</p>
// * <p>Description: Tests the methods in SubscriptionsHelper</p>
// * <p>Copyright: Copyright (c) 2004</p>
// * <p>Company: </p>
// * @author Sarah Tong
// * @version $Id: TestSubscriptionsHelper.java,v 1.8 2006/07/11 14:16:52 xzfdtb Exp $
// */
//public class TestSubscriptionsHelper extends TransactionTestCase
//{
//    private static final Integer CASE_ID = new Integer(1);
//    private static final String DATE_TIME = "27/02/2004";
//    private static final Integer EVENT_DESC_ID = new Integer(2);
//    private static final String LOG_ENTRY_XML =
//            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"+
//            "<event>Some xml</event>";
//    private static final Integer DEF_ON_CASE_ID = new Integer(3);
//    private static final Integer DEF_ON_OFFENCE_ID = new Integer(4);
//
//    private static final String insertEntry =
//            "INSERT INTO XHB_COURT_LOG_ENTRY " +
//            "(CASE_ID, DATE_TIME, EVENT_DESC_ID, LOG_ENTRY_XML, " +
//            " DEFENDANT_ON_CASE_ID, DEFENDANT_ON_OFFENCE_ID) VALUES (" +
//            CASE_ID + ",to_date('" + DATE_TIME + "', 'DD/MM/YYYY')," + EVENT_DESC_ID + ",'" +
//            LOG_ENTRY_XML + "'," + DEF_ON_CASE_ID + "," + DEF_ON_OFFENCE_ID + ")";
//    // in reality zero or one of DEF_ON_CASE_ID, DEF_ON_OFFENCE_ID would be set
//    // however setting both here allows us to ensure all values are retrieved correctly
//
//    private static final String findEntry =
//            "SELECT MAX (ENTRY_ID) FROM XHB_COURT_LOG_ENTRY";
//
//    private static final String findEventyType =
//            "SELECT EVENT_TYPE FROM XHB_COURT_LOG_EVENT_DESC WHERE " +
//            "EVENT_DESC_ID=" + EVENT_DESC_ID;
//
//    private Long _entryId;
//    private Integer _eventType;
//
//    public TestSubscriptionsHelper(String name) throws NamingException
//    {
//        super(name, true);
//    }
//
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//        connection = dataSource.getConnection();
//        Statement stmt = connection.createStatement();
//        stmt.executeUpdate(insertEntry);
//
//        ResultSet rs1 = stmt.executeQuery(findEntry);
//        while (rs1.next())
//        {
//            _entryId = new Long(rs1.getLong(1));
//        }
//
//        ResultSet rs2 = stmt.executeQuery(findEventyType);
//        while (rs2.next())
//        {
//            _eventType = new Integer(rs2.getInt(1));
//        }
//    }
//
//    /**
//     * Test that the createCourtLogViewValue method correctly builds a
//     * CourtLogViewValue from a entryId
//     */
//    public void testCreateCourtLogViewValue()
//    {
//        CourtLogViewValue clvv =
//                SubscriptionsHelper.createCourtLogViewValue(_entryId);
//
//        assertEquals("Incorrect case id retrieved.",
//                     CASE_ID, clvv.getCaseId());
//
//        SimpleDateFormat formatter = new SimpleDateFormat ("dd/MM/yyyy");
//        String dateString = formatter.format(clvv.getEntryDate());
//        assertEquals("Incorrect date time retrieved.",
//                     DATE_TIME, dateString);
//        assertEquals("Incorrect event type retrieved.",
//                     _eventType, clvv.getEventType());
//        assertEquals("Incorrect log entry retrieved.",
//                     LOG_ENTRY_XML, clvv.getLogEntry());
//        assertEquals("Incorrect defendant on case id retrieved.",
//                     DEF_ON_CASE_ID, clvv.getDefendantOnCaseId());
//        assertEquals("Incorrect defendant on offence id retrieved.",
//                     DEF_ON_OFFENCE_ID, clvv.getDefendantOnOffenceId());
//    }
//
//    /**
//     * Test the lookUpEventType method correctly retrieves the eventType from
//     * the eventDescId
//     */
//    public void testLookupEventType()
//    {
//        Integer eventType = SubscriptionsHelper.lookupEventType(EVENT_DESC_ID);
//        assertEquals("Incorrect event type retrieved.", _eventType, eventType);
//    }
//}
//