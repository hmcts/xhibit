//package uk.gov.courtservice.xhibit.courtlog.cjse;
//
//import java.io.StringReader;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_subscr_event_control.XhbSubscrEventControlBasicValue;
//import uk.gov.courtservice.xhibit.courtlog.cjse.eventmapper.TestCjseEventMapperHandlers;
//import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;
//
///**
// * <p>Title: Test Case for CjseCLHelper</p>
// * <p>Description: </p>
// * <p>
// * Test the full flow of converting an XHIBIT court log event into a CJSE event
// * stored in the database.
// * </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Eds</p>
// * @author Bob Boothby
// * @version 1.0
// */
//public class TestCjseClHelper extends TransactionTestCase
//{
//    private CjseClHelper cjseClHelper;
//
//    private static String query =
//            "SELECT *\n" +
//            "FROM   XHB_SUBSCR_EVENT_CONTROL\n" +
//            "WHERE  EVENT_CONTROL_ID = (\n" +
//            "       SELECT MAX(EVENT_CONTROL_ID)\n" +
//            "       FROM   XHB_SUBSCR_EVENT_CONTROL)";
//
//    private static String EVENT_CONTROL_ID = "EVENT_CONTROL_ID";
//    private static String EVENT_TYPE = "EVENT_TYPE";
//    private static String EVENT_DATA = "EVENT_DATA";
//    private static String EVENT_IDENTIFIER = "EVENT_IDENTIFIER";
//    private static String EVENT_LEVEL = "EVENT_LEVEL";
//    private static String EVENT_TIME = "EVENT_TIME";
//    private static String CREST_COURT_ID = "CREST_COURT_ID";
//    private static String LAST_UPDATE_DATE = "LAST_UPDATE_DATE";
//    private static String CREATION_DATE = "CREATION_DATE";
//    private static String CREATED_BY = "CREATED_BY";
//    private static String LAST_UPDATED_BY = "LAST_UPDATED_BY";
//    private static String VERSION = "VERSION";
//
//    /**
//     * This constructor sets up a transaction that will be rolled back
//     * with each teardown.
//     * @param s The name of the test.
//     * @throws Exception When there is a problem.
//     */
//    public TestCjseClHelper(String s) throws Exception
//    {
//        //Set up to be rolled back, using default datasources.
//        super(s, true);
//    }
//
//    /**
//     * Initialise a new instance of CjseClHelper for testing and calls the
//     * constructor on the parent.
//     * @throws Exception when there os a problem in setting up the transaction,
//     * connection or instance of CjseClHelper.
//     */
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//        cjseClHelper = new CjseClHelper();
//    }
//
//    /**
//     * Test the transformation of a case level event with a simple one to one mapping.
//     * @throws Exception When there is a problem in the transformation.
//     */
//    public void testTransformClEventCaseOneToOne() throws Exception
//    {
//        cjseClHelper.transformClEvent(CjseEventFixture.getCaseCLSubsValue1(new Integer(11124), ""));
//
//        Statement stmt = connection.createStatement();
//        ResultSet rs = stmt.executeQuery(query);
//        while(rs.next())
//        {
//            XhbSubscrEventControlBasicValue value =
//                    getSubscrEventValueFromResultSet(rs);
//            assertEquals("Event type: ", new Integer(20400), value.getEventType());
//            assertEquals("Event Identifier: ", "T20028779", value.getEventIdentifier());
//            assertEquals("Event level: ", new Integer(0), value.getEventLevel());
//            assertEquals("Crest court id: ","475",value.getCrestCourtId());
//            EventParameters eventData = EventParameters.unmarshal(
//                    new StringReader(value.getEventData()));
//            assertEquals("XML: event type: ", "20400", eventData.getEventTypeID());
//            assertEquals("XML: event location: ", "475", eventData.getEventLocation());
//            assertEquals("XML: messageText: ",
//                         "T20028779 TEST T DEFENDANT T20028779-1MNOPQR",
//                         eventData.getMessageText());
//            assertEquals("XML: number of casefFileIds: ",
//                         1,
//                         eventData.getCaseFileIDs().getCaseFileIDCount());
//            assertEquals("XML: CaseFileID: ",
//                         "T20028779",
//                         eventData.getCaseFileIDs().getCaseFileID(0));
//            assertEquals("XML: number of genders: ",
//                         1,
//                         eventData.getGenders().getGenderCount());
//            assertEquals("XML: Gender: ",
//                         "Male",
//                         eventData.getGenders().getGender(0).toString());
//        }
//    }
//
//    /**
//     * Test the transformation of a case level event with a one to many mapping.
//     * @throws Exception When there is a problem in the transformation.
//     */
//    public void testTransformClEventCaseOneToMany() throws Exception
//    {
//        cjseClHelper.transformClEvent(CjseEventFixture.getCaseCLSubsValue1(new Integer(11111),
//                TestCjseEventMapperHandlers.ONETOMANY_EVENT_XML_1));
//
//        Statement stmt = connection.createStatement();
//        ResultSet rs = stmt.executeQuery(query);
//        while(rs.next())
//        {
//            XhbSubscrEventControlBasicValue value =
//                    getSubscrEventValueFromResultSet(rs);
//            assertEquals("Event type: ",
//                         TestCjseEventMapperHandlers.CJSE_ID_1,
//                         value.getEventType());
//            assertEquals("Event Identifier: ", "T20028779", value.getEventIdentifier());
//            assertEquals("Event level: ", new Integer(0), value.getEventLevel());
//            assertEquals("Crest court id: ","475",value.getCrestCourtId());
//            EventParameters eventData = EventParameters.unmarshal(
//                    new StringReader(value.getEventData()));
//            assertEquals("XML: event type: ",
//                         TestCjseEventMapperHandlers.CJSE_ID_1.toString(),
//                         eventData.getEventTypeID());
//            assertEquals("XML: event location: ", "475", eventData.getEventLocation());
//            assertEquals("XML: messageText: ",
//                         "T20028779 TEST T DEFENDANT T20028779-1MNOPQR",
//                         eventData.getMessageText());
//            assertEquals("XML: number of casefFileIds: ",
//                         1,
//                         eventData.getCaseFileIDs().getCaseFileIDCount());
//            assertEquals("XML: CaseFileID: ",
//                         "T20028779",
//                         eventData.getCaseFileIDs().getCaseFileID(0));
//            assertEquals("XML: number of genders: ",
//                         1,
//                         eventData.getGenders().getGenderCount());
//            assertEquals("XML: Gender: ",
//                         "Male",
//                         eventData.getGenders().getGender(0).toString());
//        }
//    }
//
//    /**
//     * Utility method to get an instance of XhbSubscrEventControlBasicValue from a result set
//     * created against the XHB_SUBSCR_EVENT_CONTROL table.
//     * @param rs The result set (should be at row to be instantiated).
//     * @return The value object created.
//     * @throws SQLException when there is a problem in the database.
//     */
//    public static XhbSubscrEventControlBasicValue getSubscrEventValueFromResultSet(
//            ResultSet rs) throws SQLException
//    {
//        XhbSubscrEventControlBasicValue value = new XhbSubscrEventControlBasicValue();
//        value.setCreatedBy(rs.getString(CREATED_BY));
//        value.setCreationDate(rs.getTimestamp(CREATION_DATE));
//        value.setCrestCourtId(rs.getString(CREST_COURT_ID));
//        value.setEventControlId(
//                getIntegerFromResultSetColumn(rs, EVENT_CONTROL_ID));
//        value.setEventData(getStringFromClob(rs.getClob(EVENT_DATA)));
//        value.setEventIdentifier(rs.getString(EVENT_IDENTIFIER));
//        value.setEventLevel(
//                getIntegerFromResultSetColumn(rs, EVENT_LEVEL));
//        value.setEventTime(rs.getTimestamp(EVENT_TIME));
//        value.setEventType(
//                getIntegerFromResultSetColumn(rs, EVENT_TYPE));
//        value.setLastUpdateDate(rs.getTimestamp(LAST_UPDATE_DATE));
//        value.setLastUpdatedBy(rs.getString(LAST_UPDATED_BY));
//        value.setVersion(
//                getIntegerFromResultSetColumn(rs, VERSION));
//        return value;
//    }
//}