//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
//import uk.gov.courtservice.framework.business.vos.*;
//import uk.gov.courtservice.framework.business.entities.*;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//
//
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.*;
//
//import org.apache.log4j.Logger;
//
//import java.util.ArrayList;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import javax.naming.NamingException;
//
///**
// * <p>Title: HearingRecordExportWorkflowTest</p>
// * <p>Description: Test class to export a single hearing or linked heaarings.
// * This test will remove and re-create hearings with id 900-907. It will also
// * remove all ExportAs that have been created for these hearings.
// * </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//public class HearingRecordExportWorkflowTest extends TransactionTestCase {
//
//  private static Logger log  = CSServices.getLogger(HearingRecordExportWorkflowTest.class);
//
//  private HearingRecordWorkflow hearingRecordWorkflow = new HearingRecordWorkflow();
//
//  private Integer insertSingleHearingInt = new Integer(-900);
//  private Integer insertLinkedHearing1Int = new Integer(-901);
//  private Integer insertLinkedHearing2Int = new Integer(-902);
//  private Integer singleHearingNotEndedInt = new Integer(-903);
//  private Integer insertHearingAlreadyExportedInt = new Integer(-904);
//  private Integer insertHearingExportFailedInt = new Integer(-905);
//  private Integer insertHearingExportInProgressInt = new Integer(-906);
//  private Integer insertHearingReadyforExportInt = new Integer(-907);
//  private Integer removeHrgs = new Integer(-910);
//
////court
// private String delCourt = "delete from xhb_court where court_id = "+TestConstants.courtID.intValue();
// private String insCourt = "insert into xhb_court (COURT_ID, COURT_TYPE, COURT_NAME, CREST_COURT_ID, COURT_PREFIX,"+
//                           "CREST_IP_ADDRESS, IN_SERVICE_FLAG, PROBATION_OFFICE_NAME, INTERNET_COURT_NAME) values ("+
//                           TestConstants.courtID.intValue() +", 'CR', 'Chelmsford', '123', 'CH', '123.456.789.1', 'Y', 'Probation off.', 'CH-internet')";
// //address
// private String delAddr = "delete from xhb_address where address_id = "+TestConstants.addressID.intValue();
// private String insAddr = "insert into xhb_address (address_id, town) values ("+TestConstants.addressID.intValue()+", 'London')";
//
// //courtSite
// private String insCourtSite = "insert into xhb_court_site (court_site_id, court_id, address_id) values ("+
//                               TestConstants.courtSiteID.intValue() +", "+TestConstants.courtID.intValue()+", "+TestConstants.addressID.intValue()+")";
// private String delCourtSite = "delete from xhb_court_site where court_site_id = " + TestConstants.courtSiteID.intValue() ;
//
// //courtroom
// private String insCourtRoom = "insert into xhb_court_room (court_room_id, crest_court_room_no, court_site_id) values ("+
//                               TestConstants.courtRoomID.intValue()+ ", 7, " + TestConstants.courtSiteID.intValue() +")";
// private String delCourtRoom = "delete from xhb_court_room where court_room_id = "+TestConstants.courtRoomID.intValue();
//
////refCourt
// private String insRefCourt = "insert into xhb_ref_court (ref_court_id, court_full_name, court_id) values ("+
//                              TestConstants.refCourtID.intValue() +", 'BAS magistrates court', "+TestConstants.courtID.intValue()+")";
// private String delRefCourt = "delete from xhb_ref_court where ref_court_id = "+TestConstants.refCourtID.intValue();
//
////Case
// private String delCase = "delete from xhb_case where case_id = "+TestConstants.caseID.intValue();
// private String insCase = "insert into xhb_case (case_id, case_number, case_type, ref_court_id, court_id) values ("+TestConstants.caseID.intValue()+", 20030310, 'T', "+TestConstants.refCourtID.intValue()+", "+TestConstants.courtID.intValue()+")";
//
////refhearing type
// private String delRefHrgType = "delete from xhb_ref_hearing_type where ref_hearing_type_id = "+TestConstants.refHrgTypeID.intValue();
// private String insRefHrgType = "insert into xhb_ref_hearing_type (ref_hearing_type_id, hearing_type_code, hearing_type_desc, court_id) values ("+TestConstants.refHrgTypeID.intValue()+", 'TRI', 'TRIAL', "+TestConstants.courtID.intValue()+")";
//
//  //Create new hearing and exportA entries.
// private String removeAllExportAs = "delete from xhb_exporta where hearing_id > "+removeHrgs.intValue()+" and hearing_id <= "+insertSingleHearingInt.intValue()+" or " +
//                             "linked_hearing_id = 999";
//
// private String removeHearings = "delete from xhb_hearing where hearing_id > "+removeHrgs.intValue()+" and hearing_id <= "+insertSingleHearingInt.intValue();
//
// private String insertSingleHearing = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                        " court_id, hearing_end_date, linked_hearing_id) values ("+insertSingleHearingInt.intValue()+", "+TestConstants.caseID.intValue()+", "+TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//                         "TO_Date( '02/21/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , null)";
//
// private String insertLinkedHearing1 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                        " court_id, hearing_end_date, linked_hearing_id) values ("+insertLinkedHearing1Int.intValue()+", "+TestConstants.caseID.intValue()+", "+TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//                         "TO_Date( '02/22/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , 999)";
//
// private String insertLinkedHearing2 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                        " court_id, hearing_end_date, linked_hearing_id) values ("+insertLinkedHearing2Int.intValue()+", "+TestConstants.caseID.intValue()+", "+TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//                         "TO_Date( '02/23/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , 999)";
//
// private String singleHearingNotEnded = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                        " court_id, linked_hearing_id) values ("+singleHearingNotEndedInt.intValue()+", "+TestConstants.caseID.intValue()+", "+TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", null)";
//
// private String insertHearingAlreadyExported = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                        " court_id, hearing_end_date, linked_hearing_id) values ("+insertHearingAlreadyExportedInt.intValue()+", "+TestConstants.caseID.intValue()+", "+TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//                         "TO_Date( '02/24/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , null)";
//
// private String insertExportAAlreadyExported = "insert into xhb_exporta (court_clerk_export, status_flag, " +
//                                        "hearing_id) values ('Marie', 'S', "+insertHearingAlreadyExportedInt.intValue()+")";
//
// private String insertHearingExportFailed = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                        " court_id, hearing_end_date, linked_hearing_id) values ("+insertHearingExportFailedInt.intValue()+", "+TestConstants.caseID.intValue()+", "+TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//                         "TO_Date( '02/25/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , null)";
//
// private String insertExportAExportFailed = "insert into xhb_exporta (court_clerk_export, status_flag, " +
//                                        "hearing_id) values ('Marie', 'F', "+insertHearingExportFailedInt.intValue()+")";
//
// private String insertHearingExportInProgress = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                        " court_id, hearing_end_date, linked_hearing_id) values ("+insertHearingExportInProgressInt.intValue()+", "+TestConstants.caseID.intValue()+", "+TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//                         "TO_Date( '02/26/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , null)";
//
// private String insertExportAExportInProgress = "insert into xhb_exporta (court_clerk_export, status_flag, " +
//                                        "hearing_id) values ('Marie', 'P', "+insertHearingExportInProgressInt.intValue()+")";
//
// private String insertHearingReadyforExport = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                      " court_id, hearing_end_date, linked_hearing_id) values ("+insertHearingReadyforExportInt.intValue()+", "+TestConstants.caseID.intValue()+", "+TestConstants.refHrgTypeID.intValue()+", "+TestConstants.courtID.intValue()+", " +
//                       "TO_Date( '02/27/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , null)";
//
// private String insertExportAReadyforExport = "insert into xhb_exporta (court_clerk_export, status_flag, " +
//                                       "hearing_id) values ('Marie', 'R', "+insertHearingReadyforExportInt.intValue()+")";
//
// private String delSchedHearings = "delete from xhb_scheduled_hearing where hearing_id > 900 and hearing_id < 960";
//
// String courtClerk = "Marie";
//
//  /**
//   * HearingRecordExportWorkflowTest
//   * @param s
//   */
//  public HearingRecordExportWorkflowTest(String s) throws NamingException
//  {
//    super(s, true);
//  }
//
//  /**
//   * setUp()
//   */
//  protected void setUp() throws Exception
//  {
//      super.setUp();
//
//    try
//    {
//      //remove data
//       TestUtils.execSql(removeAllExportAs);
//       TestUtils.execSql(delSchedHearings);
//       TestUtils.execSql(removeHearings);
//       TestUtils.execSql(delRefHrgType);
//       TestUtils.execSql(delCase);
//       TestUtils.execSql(delRefCourt);
//       TestUtils.execSql(delCourtRoom);
//       TestUtils.execSql(delCourtSite);
//       TestUtils.execSql(delAddr);
//       TestUtils.execSql(delCourt);
//
//       //insert data
//       TestUtils.execSql(insCourt);
//       TestUtils.execSql(insAddr);
//       TestUtils.execSql(insCourtSite);
//       TestUtils.execSql(insCourtRoom);
//       TestUtils.execSql(insRefCourt);
//       TestUtils.execSql(insCase);
//       TestUtils.execSql(insRefHrgType);
//       TestUtils.execSql(insertSingleHearing);
//       TestUtils.execSql(insertLinkedHearing1);
//       TestUtils.execSql(insertLinkedHearing2);
//       TestUtils.execSql(singleHearingNotEnded);
//       TestUtils.execSql(insertHearingAlreadyExported);
//       TestUtils.execSql(insertExportAAlreadyExported);
//       TestUtils.execSql(insertHearingExportFailed);
//       TestUtils.execSql(insertExportAExportFailed);
//       TestUtils.execSql(insertHearingExportInProgress);
//       TestUtils.execSql(insertExportAExportInProgress);
//       TestUtils.execSql(insertHearingReadyforExport);
//       TestUtils.execSql(insertExportAReadyforExport);
//
//    }
//    catch (Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  /**
//   * Test to export single hearing
//   */
//  public void testEpxortHearingRecord1() {
//
//   try
//    {
//      log.debug("####################### TEST 1 start ########################");
//      //test to export single hearing
//      ArrayList test1 = new ArrayList();
//      test1.add(insertSingleHearingInt);
//      hearingRecordWorkflow.epxortHearingRecord(test1, courtClerk);
//      this.assertTrue(true);
//      log.debug("####################### TEST 1 Finish ########################");
//    }
//    catch (Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  /**
//   * Test to export linked hearings
//   */
//  public void testEpxortHearingRecord2() {
//
//
//    try
//    {
//      log.debug("####################### TEST 2 start ########################");
//      ArrayList test2 = new ArrayList();
//      test2.add(insertLinkedHearing1Int);
//      test2.add(insertLinkedHearing2Int);
//      hearingRecordWorkflow.epxortHearingRecord(test2, courtClerk);
//      this.assertTrue(true);
//      log.debug("####################### TEST 2 Finish ########################");
//    }
//    catch (Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  /**
//   * Test to export a hearing that has not yet been ended
//   */
//  public void testEpxortHearingRecord3() {
//
//    try
//    {
//      log.debug("####################### TEST 3 start ########################");
//      ArrayList test3 = new ArrayList();
//      test3.add(singleHearingNotEndedInt);
//      hearingRecordWorkflow.epxortHearingRecord(test3, courtClerk);
//      this.assertTrue(false);
//      log.debug("####################### TEST 3 Finish ########################");
//    }
//    catch (HearingRecordException ex)
//    {
//      //log.debug(ex.toString());
//      //ex.printStackTrace();
//      log.debug("Caught a HearingRecordHearingNotEndedException as expected");
//      this.assertTrue(true);
//    }
//    catch (Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//
//  /**
//   * Test to export hearing that has already been exported
//   */
//  public void testEpxortHearingRecord4() {
//    try
//    {
//      log.debug("####################### TEST 4 start ########################");
//      ArrayList test4 = new ArrayList();
//      test4.add(insertHearingAlreadyExportedInt);
//      hearingRecordWorkflow.epxortHearingRecord(test4, courtClerk);
//      this.assertTrue(false);
//      log.debug("####################### TEST 4 Finish ########################");
//    }
//    catch (HearingRecordException ex)
//    {
//      //log.debug(ex.toString());
//      //ex.printStackTrace();
//      log.debug("Caught a HearingRecordHearingAlreadyExportedException as expected");
//      this.assertTrue(true);
//    }
//    catch (Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  /**
//   * Test to export hearing that has previously failed. The export should be
//   * ok and be set to ready for export again.
//   */
//  public void testEpxortHearingRecord5() {
//    try
//    {
//      log.debug("####################### TEST 5 start ########################");
//      ArrayList test5 = new ArrayList();
//      test5.add(insertHearingExportFailedInt);
//      hearingRecordWorkflow.epxortHearingRecord(test5, courtClerk);
//      this.assertTrue(true);
//      log.debug("####################### TEST 5 Finish ########################");
//    }
//    catch (Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  /**
//   * Test to export something that is already in progress
//   */
//  public void testEpxortHearingRecord6() {
//    try
//    {
//      log.debug("####################### TEST 6 start ########################");
//      ArrayList test6 = new ArrayList();
//      test6.add(insertHearingExportInProgressInt);
//      hearingRecordWorkflow.epxortHearingRecord(test6, courtClerk);
//      this.assertTrue(false);
//      log.debug("####################### TEST 6 Finish ########################");
//    }
//    catch (HearingRecordException ex)
//    {
//      //log.debug(ex.toString());
//      //ex.printStackTrace();
//      log.debug("Caught a HearingRecordExportInProgressException as expected");
//      this.assertTrue(true);
//    }
//    catch (Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//
//}
//