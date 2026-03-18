//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
//import java.util.ArrayList;
//
//import javax.naming.NamingException;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleController;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerHome;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordException;
//
///**
// * <p>Title: HearingScheduleControllerRemoteHRExportTest </p>
// * <p>Description: Test class to export a single hearing or linked heaarings.
// * This test will remove and re-create hearings with id -900 to -907. It will also
// * remove all ExportAs that have been created for these hearings.
// * </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//public class HearingScheduleControllerRemoteHRExportTest extends TransactionTestCase
//{
//
//    private static Logger log  = CSServices.getLogger(HearingScheduleControllerRemoteHRExportTest.class);
//
//    private HearingScheduleController controller =
//            (HearingScheduleController)CSServices.getEJBServices().
//            createRemoteSession(HearingScheduleControllerHome.class);
//
//
//    private Integer insertSingleHearingInt = new Integer(-900);
//    private Integer insertLinkedHearing1Int = new Integer(-901);
//    private Integer insertLinkedHearing2Int = new Integer(-902);
//    private Integer singleHearingNotEndedInt = new Integer(-903);
//    private Integer insertHearingAlreadyExportedInt = new Integer(-904);
//    private Integer insertHearingExportFailedInt = new Integer(-905);
//    private Integer insertHearingExportInProgressInt = new Integer(-906);
//    private Integer insertHearingReadyforExportInt = new Integer(-907);
//    private Integer removeHrgs = new Integer(-910);
//
//    //Create new hearing and exportA entries.
//    private String removeAllExportAs = "delete from xhb_exporta where hearing_id > "+removeHrgs.intValue()+" and hearing_id <= "+insertSingleHearingInt.intValue()+" or " +
//                                       "linked_hearing_id = 999";
//
//    private String removeHearings = "delete from xhb_hearing where hearing_id > "+removeHrgs.intValue()+" and hearing_id <= "+insertSingleHearingInt.intValue();
//
//    private String insertSingleHearing = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//            " court_id, hearing_end_date, linked_hearing_id) values ("+insertSingleHearingInt.intValue()+", "+TestConstants.caseID.intValue()+", "+TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//            "TO_Date( '02/21/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , null)";
//
//    private String insertLinkedHearing1 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//            " court_id, hearing_end_date, linked_hearing_id) values ("+insertLinkedHearing1Int.intValue()+", "+TestConstants.caseID.intValue()+", "+TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//            "TO_Date( '02/22/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , 999)";
//
//    private String insertLinkedHearing2 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//            " court_id, hearing_end_date, linked_hearing_id) values ("+insertLinkedHearing2Int.intValue()+", "+TestConstants.caseID.intValue()+", "+TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//            "TO_Date( '02/23/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , 999)";
//
//    private String singleHearingNotEnded = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//            " court_id, linked_hearing_id) values ("+singleHearingNotEndedInt.intValue()+", "+TestConstants.caseID.intValue()+", "+TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", null)";
//
//    private String insertHearingAlreadyExported = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//            " court_id, hearing_end_date, linked_hearing_id) values ("+insertHearingAlreadyExportedInt.intValue()+", "+TestConstants.caseID.intValue()+", "+TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//            "TO_Date( '02/24/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , null)";
//
//    private String insertExportAAlreadyExported = "insert into xhb_exporta (court_clerk_export, status_flag, " +
//            "hearing_id) values ('Marie', 'S', "+insertHearingAlreadyExportedInt.intValue()+")";
//
//    private String insertHearingExportFailed = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//            " court_id, hearing_end_date, linked_hearing_id) values ("+insertHearingExportFailedInt.intValue()+", "+TestConstants.caseID.intValue()+", "+TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//            "TO_Date( '02/25/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , null)";
//
//    private String insertExportAExportFailed = "insert into xhb_exporta (court_clerk_export, status_flag, " +
//            "hearing_id) values ('Marie', 'F', "+insertHearingExportFailedInt.intValue()+")";
//
//    private String insertHearingExportInProgress = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//            " court_id, hearing_end_date, linked_hearing_id) values ("+insertHearingExportInProgressInt.intValue()+", "+TestConstants.caseID.intValue()+", "+TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//            "TO_Date( '02/26/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , null)";
//
//    private String insertExportAExportInProgress = "insert into xhb_exporta (court_clerk_export, status_flag, " +
//            "hearing_id) values ('Marie', 'P', "+insertHearingExportInProgressInt.intValue()+")";
//
//    private String insertHearingReadyforExport = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//            " court_id, hearing_end_date, linked_hearing_id) values ("+insertHearingReadyforExportInt.intValue()+", "+TestConstants.caseID.intValue()+", "+TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//            "TO_Date( '02/27/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , null)";
//
//    private String insertExportAReadyforExport = "insert into xhb_exporta (court_clerk_export, status_flag, " +
//            "hearing_id) values ('Marie', 'R', "+insertHearingReadyforExportInt.intValue()+")";
//
//    private String delSchedHearings = "delete from xhb_scheduled_hearing where hearing_id > 900 and hearing_id < 960";
//
//    String courtClerk = "Marie";
//
//    /**
//     * HearingScheduleControllerRemoteHRExportTest
//     * @param s
//     */
//    public HearingScheduleControllerRemoteHRExportTest(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//    /**
//     * setUp()
//     */
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//        try
//        {
//            //remove data
//            TestUtils.execSql(removeAllExportAs);
//            TestUtils.execSql(delSchedHearings);
//            TestUtils.execSql(removeHearings);
//            TestUtils.execSql(TestConstants.delRefHrgType);
//            TestUtils.execSql(TestConstants.delCase);
//            TestUtils.execSql(TestConstants.delRefCourt);
//            TestUtils.execSql(TestConstants.delCourtRoom);
//            TestUtils.execSql(TestConstants.delCourtSite);
//            TestUtils.execSql(TestConstants.delAddr);
//            TestUtils.execSql(TestConstants.delCourt);
//
//            //insert data
//            TestUtils.execSql(TestConstants.insCourt);
//            TestUtils.execSql(TestConstants.insAddr);
//            TestUtils.execSql(TestConstants.insCourtSite);
//            TestUtils.execSql(TestConstants.insCourtRoom);
//            TestUtils.execSql(TestConstants.insRefCourt);
//            TestUtils.execSql(TestConstants.insCase);
//            TestUtils.execSql(TestConstants.insRefHrgType);
//            TestUtils.execSql(insertSingleHearing);
//            TestUtils.execSql(insertLinkedHearing1);
//            TestUtils.execSql(insertLinkedHearing2);
//            TestUtils.execSql(singleHearingNotEnded);
//            TestUtils.execSql(insertHearingAlreadyExported);
//            TestUtils.execSql(insertExportAAlreadyExported);
//            TestUtils.execSql(insertHearingExportFailed);
//            TestUtils.execSql(insertExportAExportFailed);
//            TestUtils.execSql(insertHearingExportInProgress);
//            TestUtils.execSql(insertExportAExportInProgress);
//            TestUtils.execSql(insertHearingReadyforExport);
//            TestUtils.execSql(insertExportAReadyforExport);
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    /**
//     * Test to export single hearing
//     */
//    public void testEpxortHearingRecord1() {
//
//        try
//        {
//            log.debug("####################### TEST 1 start ########################");
//            //test to export single hearing
//            ArrayList test1 = new ArrayList();
//            test1.add(insertSingleHearingInt);
//            controller.exportHearingRecord(test1, courtClerk);
//            assertTrue(true);
//            log.debug("####################### TEST 1 Finish ########################");
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    /**
//     * Test to export linked hearings
//     */
//    public void testEpxortHearingRecord2() {
//
//
//        try
//        {
//            log.debug("####################### TEST 2 start ########################");
//            ArrayList test2 = new ArrayList();
//            test2.add(insertLinkedHearing1Int);
//            test2.add(insertLinkedHearing2Int);
//            controller.exportHearingRecord(test2, courtClerk);
//            assertTrue(true);
//            log.debug("####################### TEST 2 Finish ########################");
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    /**
//     * Test to export a hearing that has not yet been ended
//     */
//    public void testEpxortHearingRecord3() {
//
//        try
//        {
//            log.debug("####################### TEST 3 start ########################");
//            ArrayList test3 = new ArrayList();
//            test3.add(singleHearingNotEndedInt);
//            controller.exportHearingRecord(test3, courtClerk);
//            assertTrue(false);
//            log.debug("####################### TEST 3 Finish ########################");
//        }
//        catch (HearingRecordException ex)
//        {
//            log.debug("Caught a HearingRecordHearingNotEndedException as expected");
//            assertTrue(true);
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//    /**
//     * Test to export hearing that has already been exported
//     */
//    public void testEpxortHearingRecord4() {
//        try
//        {
//            log.debug("####################### TEST 4 start ########################");
//            ArrayList test4 = new ArrayList();
//            test4.add(insertHearingAlreadyExportedInt);
//            controller.exportHearingRecord(test4, courtClerk);
//            assertTrue(false);
//            log.debug("####################### TEST 4 Finish ########################");
//        }
//
//        catch (HearingRecordException ex)
//        {
//            log.debug("Caught a HearingRecordHearingAlreadyExportedException as expected");
//            assertTrue(true);
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    /**
//     * Test to export hearing that has previously failed. The export should be
//     * ok and be set to ready for export again.
//     */
//    public void testEpxortHearingRecord5() {
//        try
//        {
//            log.debug("####################### TEST 5 start ########################");
//            ArrayList test5 = new ArrayList();
//            test5.add(insertHearingExportFailedInt);
//            controller.exportHearingRecord(test5, courtClerk);
//            assertTrue(true);
//            log.debug("####################### TEST 5 Finish ########################");
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//    /**
//     * Test to export something that is already in progress
//     */
//    public void testEpxortHearingRecord6() {
//        try
//        {
//            log.debug("####################### TEST 6 start ########################");
//            ArrayList test6 = new ArrayList();
//            test6.add(insertHearingExportInProgressInt);
//            controller.exportHearingRecord(test6, courtClerk);
//            assertTrue(false);
//            log.debug("####################### TEST 6 Finish ########################");
//        }
//        catch (HearingRecordException ex)
//        {
//
//            log.debug("Caught a HearingRecordExportInProgressException as expected");
//            assertTrue(true);
//        }
//        catch (Exception e)
//        {
//            log.debug(e.toString());
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//}