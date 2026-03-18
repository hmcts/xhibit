//
//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.endhearing;
//
//import java.sql.PreparedStatement;
//import java.sql.Statement;
//import java.util.Calendar;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.services.conversion.XDateFormat;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing.TimeCalculationHelper;
//
//public class TestTimeCalculationHelper extends TransactionTestCase
//{
//    private static final Integer shId = new Integer(316);
//    private static final Integer docId1 = new Integer(404);
//    private static final Integer docId2 = new Integer(405);
//    private static final String xmlDocId1 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event>" +
//            "   <Listed_Def_On_Case_Ids>" +
//            "      <Def_On_Case_Id>" +
//            "            <doc_id>" + docId1 + "</doc_id>" +
//            "       </Def_On_Case_Id>" +
//            "   </Listed_Def_On_Case_Ids>" +
//            "</event>";
//    private static final String xmlDocId2 =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event>" +
//            "   <Listed_Def_On_Case_Ids>" +
//            "       <Def_On_Case_Id>" +
//            "            <doc_id>" + docId2 + "</doc_id>" +
//            "       </Def_On_Case_Id>" +
//            "   </Listed_Def_On_Case_Ids>" +
//            "</event>";
//    private static final String xmlBothDocIds =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//            "<event>" +
//            "   <Listed_Def_On_Case_Ids>" +
//            "      <Def_On_Case_Id>" +
//            "            <doc_id>" + docId1 + "</doc_id>" +
//            "       </Def_On_Case_Id>" +
//            "       <Def_On_Case_Id>" +
//            "            <doc_id>" + docId2 + "</doc_id>" +
//            "       </Def_On_Case_Id>" +
//            "   </Listed_Def_On_Case_Ids>" +
//            "</event>";
//    private static final String xmlNoDocId = "<xml/>";
//
//    private static final Integer testCaseId1 = new Integer (1451);
//    private static final Integer testCaseId2 = new Integer (1496);
//    private static final Integer testCaseId3 = new Integer (1293);
//
//    private static final Integer juryStartEvent     = new Integer (20914);
//    private static final Integer juryStopEvent      = new Integer (20913);
//    private static final Integer hearingStartEvent1 = new Integer (10100);
//    private static final Integer hearingStartEvent2 = new Integer (10500);
//    private static final Integer hearingStopEvent1  = new Integer (30100);
//    private static final Integer hearingStopEvent2  = new Integer (30600);
//
//    private static final Logger log =  CSServices.getLogger(TestTimeCalculationHelper.class);
//
//    private static Object[][] testData =
//    {
//        // the hearing time calculation need to be able to cope with:
//        //    valid events
//        {testCaseId1,"14-OCT-2003 09:28:31",hearingStartEvent1,xmlBothDocIds,null},
//        {testCaseId1,"14-OCT-2003 10:28:31",hearingStopEvent1,xmlNoDocId,docId1},
//        //    events for another defendant (ignore)
//        {testCaseId1,"14-OCT-2003 10:30:31",hearingStartEvent1,xmlDocId2,null},
//        {testCaseId1,"14-OCT-2003 10:35:31",hearingStopEvent1,xmlNoDocId,docId2},
//        //    consecutive start events (ignore second)
//        {testCaseId1,"14-OCT-2003 10:45:31",hearingStartEvent2,xmlDocId1,null},
//        {testCaseId1,"14-OCT-2003 10:50:31",hearingStartEvent2,xmlDocId1,null},
//        //    consecutive stop events (ignore second)
//        {testCaseId1,"14-OCT-2003 11:00:31",hearingStopEvent2,xmlBothDocIds,null},
//        {testCaseId1,"14-OCT-2003 11:05:31",hearingStopEvent2,xmlBothDocIds,null},
//        //    xml with no doc id (ignore)
//        {testCaseId1,"14-OCT-2003 11:15:31",hearingStartEvent1,xmlNoDocId,null},
//        {testCaseId1,"14-OCT-2003 11:25:31",hearingStopEvent2,xmlNoDocId,null},
//        //   events on another day (ignore)
//        {testCaseId1,"20-OCT-2003 10:45:31",hearingStartEvent1,xmlDocId1,null},
//        {testCaseId1,"20-OCT-2003 15:45:31",hearingStopEvent2,xmlBothDocIds,null},
//
//        {testCaseId2,"20-NOV-2002 07:00:05",juryStartEvent,"<xml/>",null},
//        {testCaseId2,"20-NOV-2002 08:00:05",juryStopEvent,"<xml/>",null},
//        {testCaseId3,"20-NOV-2002 09:00:05",juryStartEvent,"<xml/>",null}
//    };
//
//
//    public TestTimeCalculationHelper(String s)
//            throws Exception
//    {
//        super(s, true);
//    }
//
//    protected void setUp()
//            throws Exception
//    {
//        super.setUp();
//
//        Calendar c = Calendar.getInstance();
//        c.set(2002, 10, 20, 9, 0, 5);
//
//        Statement stmt = connection.createStatement();
//        int rs = stmt.executeUpdate("delete from XHB_COURT_LOG_ENTRY");
//        log.debug("Setup: Delete all rows: " + rs + " rows deleted");
//
//        // set SH date to 14/Oct/2003
//        rs = stmt.executeUpdate("Update xhb_scheduled_hearing set original_time = '14-OCT-2003' where scheduled_hearing_id = " + shId.toString());
//        log.debug("Setup: set original time: " + rs + " rows updated");
//
//        String insertSQL =
//                "insert into XHB_COURT_LOG_ENTRY(CASE_ID, DATE_TIME, EVENT_DESC_ID, " +
//                "LOG_ENTRY_XML, DEFENDANT_ON_CASE_ID) values (?, ?, " +
//                "(SELECT EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = ?), " +
//                "?,?)";
//
//        PreparedStatement ps = connection.prepareStatement(insertSQL);
//
//        for (int i = 0; i < testData.length; i++)
//        {
//            Object[] thisRow = testData[i];
//
//            ps.setInt(1, ((Integer)thisRow[0]).intValue());
//            Calendar parsedDate = XDateFormat.parse((String)thisRow[1]);
//
//            long l = parsedDate.getTime().getTime();
//            java.sql.Timestamp d = new java.sql.Timestamp(l);
//            ps.setTimestamp(2, d );
//
//            ps.setInt(3, ((Integer)thisRow[2]).intValue());
//
//            ps.setString(4, (String)thisRow[3]);
//
//            if (thisRow[4] == null)
//            {
//                ps.setNull(5,java.sql.Types.INTEGER);
//            }
//            else
//            {
//                ps.setInt(5, ((Integer)thisRow[4]).intValue());
//            }
//
//            ps.executeUpdate();
//        }
//
//    }
//
//    public void testCalculateScheduledHearingTime()
//            throws Exception
//    {
//        long l = TimeCalculationHelper.calculateScheduledHearingTime(shId, docId1);
//        // 1 hr 15 mins
//        assertEquals(75*60*1000, l);
//    }
//
//}