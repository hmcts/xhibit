//
//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.endhearing;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleController;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerHome;
//import uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord.TestConstants;
//
///**
// *
// * <p>Title: EndHearingHearingScheduleControllerBeanTest</p>
// * <p>Description: Test the local interface for end hearing.
// *
// * This test will remove and re-create hearings with id 900-903. It will also
// * remove and re-create scheduled hearings with id 900-906.
// *
// * This test requires the HearingScheduleController, HearingScheduleEntities,
// * SysAdminController, SysAdminEntities and PrehearingEngities to be deployed.
// *
// * </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//public class EndHearingHearingScheduleControllerBeanTest extends TestCase {
//
//
//  private HearingScheduleController hearingScheduleController =
//      (HearingScheduleController)CSServices.getEJBServices().createLocalSession(
//      HearingScheduleControllerHome.class);
//
//  private static Logger log  = CSServices.getLogger(EndHearingHearingScheduleControllerBeanTest.class);
//
//
//  private Integer minHearing = new Integer(900);
//  private Integer maxHearing = new Integer(906);
//  private Integer schedHearingIDEndSingleHearing = new Integer(900);
//  private Integer iInsertLinkedHearing1 = new Integer(901);
//  private Integer schedHearingIDEndLinkedHearings = new Integer(902);
//  private Integer iInsertSchedHearing1 = new Integer(900);
//  private Integer iInsertSchedHearing2 = new Integer(901);
//  private Integer iInsertSchedHearing3 = new Integer(902);
//  private Integer iInsertSchedHearing4 = new Integer(903);
//  private Integer iInsertSchedHearing5 = new Integer(904);
//  private Integer iInsertSchedHearing6 = new Integer(905);
//  private Integer courtID = new Integer(9990);
//  private Integer courtSiteID = new Integer(9991);
//  private Integer courtRoomID = new Integer(9992);
//  private Integer refCourtID = new Integer(99993);
//  private Integer addressID = new Integer(9994);
//  private Integer hrgListID = new Integer(906);
//  private Integer sittingID = new Integer(907);
//  private Integer caseID = new Integer(901);
//  private Integer refHrgTypeID = new Integer(903);
//
//  private Integer dummyDefOnCaseID = new Integer(1);
//
//  //private Integer schedHearingIDEndSingleHearing = new Integer(900);
//  //private Integer schedHearingIDEndLinkedHearings = new Integer(902);
//  private Long newDuration = new Long(123);
//
//  //court
//  private String delCourt = "delete from xhb_court where court_id = "+courtID.intValue();
//  private String insCourt = "insert into xhb_court (COURT_ID, COURT_TYPE, COURT_NAME, CREST_COURT_ID, COURT_PREFIX,"+
//                            "CREST_IP_ADDRESS, IN_SERVICE_FLAG, PROBATION_OFFICE_NAME, INTERNET_COURT_NAME) values ("+
//                            courtID.intValue() +", 'CR', 'Chelmsford', '123', 'CH', '123.456.789.1', 'Y', 'Probation off.', 'CH-internet')";
//  //address
//  private String delAddr = "delete from xhb_address where address_id = "+addressID.intValue();
//  private String insAddr = "insert into xhb_address (address_id, town) values ("+addressID.intValue()+", 'London')";
//
//  //courtSite
//  private String insCourtSite = "insert into xhb_court_site (court_site_id, court_id, address_id) values ("+
//                                courtSiteID.intValue() +", "+courtID.intValue()+", "+addressID.intValue()+")";
//  private String delCourtSite = "delete from xhb_court_site where court_site_id = " + courtSiteID.intValue() ;
//
//  //courtroom
//  private String insCourtRoom = "insert into xhb_court_room (court_room_id, crest_court_room_no, court_site_id) values ("+
//                                courtRoomID.intValue()+ ", 7, " + courtSiteID.intValue() +")";
//  private String delCourtRoom = "delete from xhb_court_room where court_room_id = "+courtRoomID.intValue();
//
//  //refCourt
//  private String insRefCourt = "insert into xhb_ref_court (ref_court_id, court_full_name, court_id) values ("+
//                               refCourtID.intValue() +", 'BAS magistrates court', "+courtID.intValue()+")";
//  private String delRefCourt = "delete from xhb_ref_court where ref_court_id = "+refCourtID.intValue();
//
//  //hearing_list
//  private String delHrgList = "delete from xhb_hearing_list where list_id = "+hrgListID.intValue();
//  private String insHrgList = "insert into xhb_hearing_list (list_id, crest_list_id, court_id) values ("+hrgListID.intValue()+", 123, "+courtID.intValue()+")";
//
//  //sitting
//  private String delSitting = "delete from xhb_sitting where sitting_id = "+sittingID.intValue();
//  private String insSitting = "insert into xhb_sitting (sitting_id, is_floating, list_id, court_room_id, court_site_id) values ("+sittingID.intValue()+", 'T', "+hrgListID.intValue()+", "+courtRoomID.intValue()+", "+courtSiteID.intValue()+")";
//
//  //Case
//  private String delCase = "delete from xhb_case where case_id = " + caseID.intValue();
//  private String insCase = "insert into xhb_case (case_id, case_number, case_type, ref_court_id, court_id) values ("+caseID.intValue()+", 20030310, 'T', "+refCourtID.intValue()+", "+courtID.intValue()+")";
//
//  //refhearing type
//  private String delRefHrgType = "delete from xhb_ref_hearing_type where ref_hearing_type_id = "+refHrgTypeID.intValue();
//  private String insRefHrgType = "insert into xhb_ref_hearing_type (ref_hearing_type_id, hearing_type_code, hearing_type_desc, court_id) values ("+refHrgTypeID.intValue()+", 'TRI', 'TRIAL', "+courtID.intValue()+")";
//
//
//  private String removeHearing = "delete from xhb_hearing where hearing_id >= "+
//                         minHearing.intValue()+" and hearing_id < "+maxHearing.intValue();
//
//  private String removeScheduledHearing = "delete from xhb_scheduled_hearing where " +
//                                  "scheduled_hearing_id >= "+minHearing.intValue()+
//                                  " and scheduled_hearing_id < "+maxHearing.intValue();
//
//  private String insertSingleHearing = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                               " court_id, hearing_end_date, linked_hearing_id) values ("+
//                               schedHearingIDEndSingleHearing.toString()+", "+caseID.intValue()+", "+refHrgTypeID.intValue()+", "+courtID.intValue()+", " +
//                               "null, null)";
//
//  private String insertLinkedHearing1 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                                " court_id, hearing_end_date, linked_hearing_id) values ("+
//                                iInsertLinkedHearing1.intValue()+", "+caseID.intValue()+", "+refHrgTypeID.intValue()+", "+courtID.intValue()+", " +
//                                "null , 999)";
//
//  private String insertLinkedHearing2 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                                " court_id, hearing_end_date, linked_hearing_id) values ("+
//                                schedHearingIDEndLinkedHearings.intValue()+", "+caseID.intValue()+", "+refHrgTypeID.intValue()+", "+courtID.intValue()+", " +
//                                "null , 999)";
//
//  private String insertSchedHearing1 = "insert into xhb_scheduled_hearing " +
//                               "(IS_CASE_ACTIVE, SCHEDULED_HEARING_ID, SEQUENCE_NO, ORIGINAL_TIME, SITTING_ID, HEARING_ID) " +
//                               "values ('Y', "+iInsertSchedHearing1.intValue()+", 1, TO_Date( '02/28/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'), "+sittingID.intValue()+", "+schedHearingIDEndSingleHearing.toString()+")";
//
//  private String insertSchedHearing2 = "insert into xhb_scheduled_hearing " +
//                               "(IS_CASE_ACTIVE, SCHEDULED_HEARING_ID, SEQUENCE_NO, ORIGINAL_TIME, SITTING_ID, HEARING_ID) " +
//                               "values ('N', "+iInsertSchedHearing2.intValue()+", 1, TO_Date( '02/28/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'), "+sittingID.intValue()+", "+schedHearingIDEndSingleHearing.toString()+")";
//
//  private String insertSchedHearing3 = "insert into xhb_scheduled_hearing " +
//                               "(IS_CASE_ACTIVE, SCHEDULED_HEARING_ID, SEQUENCE_NO, ORIGINAL_TIME, SITTING_ID, HEARING_ID) " +
//                               "values ('Y', "+iInsertSchedHearing3.intValue()+", 1, TO_Date( '02/28/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'), "+sittingID.intValue()+", "+iInsertLinkedHearing1.intValue()+")";
//
//  private String insertSchedHearing4 = "insert into xhb_scheduled_hearing " +
//                               "(IS_CASE_ACTIVE, SCHEDULED_HEARING_ID, SEQUENCE_NO, ORIGINAL_TIME, SITTING_ID, HEARING_ID) " +
//                               "values ('N', "+iInsertSchedHearing4.intValue()+", 1, TO_Date( '02/28/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'), "+sittingID.intValue()+", "+iInsertLinkedHearing1.intValue()+")";
//
//  private String insertSchedHearing5 = "insert into xhb_scheduled_hearing " +
//                               "(IS_CASE_ACTIVE, SCHEDULED_HEARING_ID, SEQUENCE_NO, ORIGINAL_TIME, SITTING_ID, HEARING_ID) " +
//                               "values ('Y', "+iInsertSchedHearing5.intValue()+", 1, TO_Date( '02/28/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'), "+sittingID.intValue()+", "+schedHearingIDEndLinkedHearings.intValue()+")";
//
//  private String insertSchedHearing6 = "insert into xhb_scheduled_hearing " +
//                               "(IS_CASE_ACTIVE, SCHEDULED_HEARING_ID, SEQUENCE_NO, ORIGINAL_TIME, SITTING_ID, HEARING_ID) " +
//                               "values ('Y', "+iInsertSchedHearing6.intValue()+", 1, TO_Date( '02/28/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'), "+sittingID.intValue()+", "+schedHearingIDEndLinkedHearings.intValue()+")";
//
//  private String delExportA = "delete from xhb_exporta";
//
// /**
//  * EndHearingHearingScheduleControllerBeanTest
//  * @param s
//  */
//  public EndHearingHearingScheduleControllerBeanTest(String s) {
//    super(s);
//  }
//
//  /**
//   * setUp
//   */
//  protected void setUp() {
//    log.debug("############## setUp starts ################");
//    try
//    {
//      //delete the data
//      TestUtils.execSql(TestConstants.delExportA);
//      TestUtils.execSql(TestConstants.DEL_SOLICITOR);
//      TestUtils.execSql(TestConstants.DEL_SOL_FIRM);
//      TestUtils.execSql(TestConstants.DEL_ADV);
//      TestUtils.execSql(TestConstants.DEL_CHAMBER);
//      TestUtils.execSql(TestConstants.delShLegRep);
//      TestUtils.execSql(TestConstants.delSchedHearDef);
//      TestUtils.execSql(TestConstants.delRefLegRep);
//      TestUtils.execSql(TestConstants.delShJudge);
//      TestUtils.execSql(TestConstants.delShAtt);
//      TestUtils.execSql(TestConstants.delJudge);
//      TestUtils.execSql(TestConstants.DEL_SH_ATT_CR);
//      TestUtils.execSql(TestConstants.DEL_SH_JUSTICE);
//      TestUtils.execSql(TestConstants.delShHrg1);
//      TestUtils.execSql(TestConstants.delSitting);
//      TestUtils.execSql(TestConstants.delHrgList);
//      TestUtils.execSql(TestConstants.delDefHrgRec);
//      TestUtils.execSql(TestConstants.delHrg);
//      TestUtils.execSql(TestConstants.delRefHrgType);
//      TestUtils.execSql(TestConstants.delDefOnCase);
//      TestUtils.execSql(TestConstants.DEL_CR);
//      TestUtils.execSql(TestConstants.DEL_CR_FIRM);
//      TestUtils.execSql(TestConstants.delCase);
//      TestUtils.execSql(TestConstants.DEL_CASES);
//      TestUtils.execSql(TestConstants.DEL_DEF_REF);//
//      TestUtils.execSql(TestConstants.delDef);
//      TestUtils.execSql(TestConstants.delSolFirm);
//      TestUtils.execSql(TestConstants.delCCInfo);
//      TestUtils.execSql(TestConstants.delRefCourt);
//      TestUtils.execSql(TestConstants.delCourtRoom);
//      TestUtils.execSql(TestConstants.delCourtSite);
//      TestUtils.execSql(TestConstants.delAddr);
//      TestUtils.execSql(TestConstants.delCourt);
//
//      //TestUtils.execSql("Remove hearing time values");
//      TestUtils.execSql(delExportA);
//      TestUtils.execSql(removeScheduledHearing);
//      TestUtils.execSql(removeHearing);
//      TestUtils.execSql(delSitting);
//      TestUtils.execSql(delHrgList);
//      TestUtils.execSql(delRefHrgType);
//      TestUtils.execSql(delSitting);
//      TestUtils.execSql(delCase);
//      TestUtils.execSql(delRefCourt);
//      TestUtils.execSql(delCourtRoom);
//      TestUtils.execSql(delCourtSite);
//      TestUtils.execSql(delAddr);
//      TestUtils.execSql(delCourt);
//
//      //insert
//      TestUtils.execSql(insCourt);
//      TestUtils.execSql(insAddr);
//      TestUtils.execSql(insCourtSite);
//      TestUtils.execSql(insCourtRoom);
//      TestUtils.execSql(insRefCourt);
//      TestUtils.execSql(insCase);
//      TestUtils.execSql(insRefHrgType);
//      TestUtils.execSql(insHrgList);
//      TestUtils.execSql(insSitting);
//      TestUtils.execSql(insertSingleHearing);
//      TestUtils.execSql(insertLinkedHearing1);
//      TestUtils.execSql(insertLinkedHearing2);
//      TestUtils.execSql(insertSchedHearing1);
//      TestUtils.execSql(insertSchedHearing2);
//      TestUtils.execSql(insertSchedHearing3);
//      TestUtils.execSql(insertSchedHearing4);
//      TestUtils.execSql(insertSchedHearing5);
//      TestUtils.execSql(insertSchedHearing6);
//
//    }
//    catch(Exception e)
//    {
//      System.err.println(e.getMessage());
//      e.printStackTrace();
//    }
//    log.debug("############## setUp ends ################");
//  }
//
//  /**
//   * tearDown
//   */
//  protected void tearDown() {
//    try
//    {
//      //TestUtils.execSql("Remove hearing time values");
//      TestUtils.execSql(delExportA);
//      TestUtils.execSql(removeScheduledHearing);
//      TestUtils.execSql(removeHearing);
//      TestUtils.execSql(removeScheduledHearing);
//      TestUtils.execSql(removeHearing);
//      TestUtils.execSql(delSitting);
//      TestUtils.execSql(delHrgList);
//      TestUtils.execSql(delRefHrgType);
//      TestUtils.execSql(delCase);
//      TestUtils.execSql(delRefCourt);
//      TestUtils.execSql(delCourtRoom);
//      TestUtils.execSql(delCourtSite);
//      TestUtils.execSql(delAddr);
//      TestUtils.execSql(delCourt);
//    }
//    catch(Exception e)
//    {
//      System.err.println(e.getMessage());
//      e.printStackTrace();
//    }
//  }
//
//
//  /**
//   * testAmendHearingDuration()
//   */
//  public void testAmendHearingDuration() {
//    log.debug("############## testAmendHearingDuration start ################");
//    Boolean testAssert = new Boolean(false);
//
//    try {
//      log.debug("hearingScheduleController.toString() : " + hearingScheduleController.toString());
//      log.debug("schedHearingIDEndSingleHearing : " + schedHearingIDEndSingleHearing.toString());
//      log.debug("newDuration : " + newDuration.toString());
//
//      hearingScheduleController.amendHearingDuration(schedHearingIDEndSingleHearing, newDuration);
//      testAssert = new Boolean(true);
//    }
//    catch(Exception e) {
//      System.err.println("Exception thrown:  "+e);
//      e.printStackTrace();
//      testAssert = new Boolean(false);
//    }
//    finally
//    {
//      assertEquals(testAssert.toString(), "true");
//    }
//    log.debug("############## testAmendHearingDuration finsish ################");
//  }
//
//  /**
//   * testIsHearingEnded
//   */
//  public void testIsHearingEnded() {
//    log.debug("############## testIsHearingEnded start ################");
//    Boolean testAssert = new Boolean(false);
//    Boolean booleanRet1 = new Boolean(false);
//    Boolean booleanRet2 = new Boolean(false);
//
//    try {
//      //after resetting the database - it should return false since the hearing is not ended.
//      booleanRet2 = hearingScheduleController.isHearingEnded(schedHearingIDEndSingleHearing);
//      log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//      log.debug("hearing should not have been ended flag is : " + booleanRet2.toString());
//
//      //test where the hearing is ended - should return true.
//      //hearingScheduleController.endSingleHearing(schedHearingIDEndSingleHearing);
//      booleanRet1 = hearingScheduleController.isHearingEnded(schedHearingIDEndSingleHearing);
//      log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//      log.debug("hearing should have been ended flag is : " + booleanRet1.toString());
//    }
//    catch(Exception e) {
//      System.err.println("Exception thrown:  "+e);
//      e.printStackTrace();
//      testAssert = new Boolean(false);
//    }
//    finally
//    {
//      if(booleanRet1.booleanValue()  && !booleanRet2.booleanValue())
//      {
//        testAssert = new Boolean(true);
//        log.debug("~~~~~~~~~~~~~~~~~~~~SUCCESS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//        log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//        log.debug("booleanRet1 : " + booleanRet1.toString());
//        log.debug("booleanRet2 : " + booleanRet2.toString());
//        assertEquals(testAssert.toString(), "true");
//      }
//      else
//      { log.debug("~~~~~~~~~~~~~~~~~~~~FAILURE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//        log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//        log.debug("booleanRet1 : " + booleanRet1.toString());
//        log.debug("booleanRet2 : " + booleanRet2.toString());
//        assertEquals(testAssert.toString(), "false");
//      }
//    }
//    log.debug("############## testIsHearingEnded finsish ################");
//  }
//
//
//  /**
//   * testReCalculateHearingDuration
//   */
//  /*
//  public void testReCalculateHearingDuration() {
//    log.debug("############## testReCalculateHearingDuration start ################");
//    Boolean testAssert = new Boolean(false);
//
//    try {
//      hearingScheduleController.reCalculateHearingDuration(
//              schedHearingIDEndSingleHearing, dummyDefOnCaseID);
//      testAssert = new Boolean(true);
//    }
//    catch(Exception e) {
//      System.err.println("Exception thrown:  "+e);
//      e.printStackTrace();
//      testAssert = new Boolean(false);
//    }
//    finally
//    {
//      assertEquals(testAssert.toString(), "true");
//    }
//    log.debug("############## testReCalculateHearingDuration finsish ################");
//  }
//*/
//}
//