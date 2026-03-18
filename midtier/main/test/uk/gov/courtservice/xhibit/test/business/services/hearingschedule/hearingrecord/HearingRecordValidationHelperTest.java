//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
//import java.util.Date;
//
//import javax.ejb.ObjectNotFoundException;
//import javax.naming.NamingException;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
//import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordApplicationDateException;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordDateHelper;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordException;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordValidationHelper;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HrAdjournedDateException;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HrBailStatusException;
//
//
///**
// *
// * <p>Title:HearingRecordValidationHelperTest </p>
// * <p>Description: This class will test all the methods in the validation helper
// * class in the service\hearing record package.
// *
// * This will insert and delete cases with id 990-1000.
// *
// * </p>
// *
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//public class HearingRecordValidationHelperTest extends TransactionTestCase
//{
//
//
//  private static Logger log  = CSServices.getLogger(HearingRecordValidationHelperTest .class);
//  private HearingRecordValidationHelper helper = new HearingRecordValidationHelper();
//
//  private Integer case1_T = new Integer(-991);
//  private Integer case2_S = new Integer(-992);
//  private Integer case3_B = new Integer(-993);
//  private Integer case4_A = new Integer(-994);
//  private Integer case5_A_O = new Integer(-995);
//  private Integer case6_U = new Integer(-996);
//  private Integer case7_C = new Integer(-997);
//  private Integer case8_A_C = new Integer(-998);
//
//  private Date date1 = new Date(234567890993L);
//  private Date date2 = new Date(734567899893L);
//
//
//  private String deleteCases = "delete from xhb_case where case_id < -990";
//
//  private String insertCase1_T = "insert into xhb_case (case_id, case_number, case_type, " +
//     "case_sub_type, ref_court_id, court_id) values (" + case1_T + ", 20030001, 'T', null, "+
//                                 TestConstants.refCourtID.intValue()+", "+TestConstants.courtID.intValue()+")";
//
//  private String insertCase2_S = "insert into xhb_case (case_id, case_number, case_type, " +
//                                 "case_sub_type, ref_court_id, court_id) values (" + case2_S + ", 20030002, 'S', null, "+
//                                 TestConstants.refCourtID.intValue()+", "+TestConstants.courtID.intValue()+")";
//
//  private String insertCase3_B = "insert into xhb_case (case_id, case_number, case_type, " +
//                                 "case_sub_type, ref_court_id, court_id) values (" + case3_B + ", 20030003, 'B', null, "+
//                                 TestConstants.refCourtID.intValue()+", "+TestConstants.courtID.intValue()+")";
//
//  private String insertCase4_A = "insert into xhb_case (case_id, case_number, case_type, " +
//                                 "case_sub_type, ref_court_id, court_id) values (" + case4_A + ", 20030004, 'A', null, "+
//                                 TestConstants.refCourtID.intValue()+", "+TestConstants.courtID.intValue()+")";
//
//  private String insertCase5_A_O = "insert into xhb_case (case_id, case_number, case_type, " +
//                                   "case_sub_type, ref_court_id, court_id) values (" + case5_A_O + ", 20030005, 'A', 'O', "+
//                                 TestConstants.refCourtID.intValue()+", "+TestConstants.courtID.intValue()+")";
//
//  private String insertCase6_U = "insert into xhb_case (case_id, case_number, case_type, " +
//                                 "case_sub_type, ref_court_id, court_id) values (" + case6_U + ", 20030006, 'U', null, "+
//                                 TestConstants.refCourtID.intValue()+", "+TestConstants.courtID.intValue()+")";
//
//  private String insertCase7_C = "insert into xhb_case (case_id, case_number, case_type, " +
//                                 "case_sub_type, ref_court_id, court_id) values (" + case7_C + ", 20030007, 'C', null, "+
//                                 TestConstants.refCourtID.intValue()+", "+TestConstants.courtID.intValue()+")";
//
//  private String insertCase8_A_C = "insert into xhb_case (case_id, case_number, case_type, " +
//                                   "case_sub_type, ref_court_id, court_id) values (" + case8_A_C + ", 20030008, 'A', 'C', "+
//                                 TestConstants.refCourtID.intValue()+", "+TestConstants.courtID.intValue()+")";
//
//
//  private HearingRecordDateHelper dateHelper = new HearingRecordDateHelper();
//
//  /**
//   * HearingRecordValidationHelperTest
//   * @param s
//   */
//  public HearingRecordValidationHelperTest (String s) throws NamingException
//  {
//    super(s, true);
//  }
//
//  /**
//   * setUp
//   */
//  protected void setUp() throws Exception
//  {
//      super.setUp();
//    try
//    {
//      //remove the data
//      TestUtils.execSql(deleteCases);
//      TestUtils.execSql(TestConstants.delRefCourt);
//      TestUtils.execSql(TestConstants.delCourtRoom);
//      TestUtils.execSql(TestConstants.delCourtSite);
//      TestUtils.execSql(TestConstants.delAddr);
//      TestUtils.execSql(TestConstants.delCourt);
//
//      //insert the cases
//      TestUtils.execSql(TestConstants.insCourt);
//      TestUtils.execSql(TestConstants.insAddr);
//      TestUtils.execSql(TestConstants.insCourtSite);
//      TestUtils.execSql(TestConstants.insCourtRoom);
//      TestUtils.execSql(TestConstants.insRefCourt);
//      TestUtils.execSql(insertCase1_T);
//      TestUtils.execSql(insertCase2_S);
//      TestUtils.execSql(insertCase3_B);
//      TestUtils.execSql(insertCase4_A);
//      TestUtils.execSql(insertCase5_A_O);
//      TestUtils.execSql(insertCase6_U);
//      TestUtils.execSql(insertCase7_C);
//      TestUtils.execSql(insertCase8_A_C);
//
//      dateHelper.setFirstHearingDate(date1);
//      dateHelper.setLastHearingDate(date2);
//
//      // test data for testValidateHearingRepresentation()
//      TestUtils.execSql(TestConstants.insCase);
//      TestUtils.execSql(TestConstants.insCase2);
//      TestUtils.execSql(TestConstants.insHrgList);
//      TestUtils.execSql(TestConstants.insSitting);
//      TestUtils.execSql(TestConstants.insRefHrgType);
//      TestUtils.execSql(TestConstants.insHrg);
//      TestUtils.execSql(TestConstants.insHrg2);
//      TestUtils.execSql(TestConstants.insShHrg1);
//      TestUtils.execSql(TestConstants.insShHrg2);
//      TestUtils.execSql(TestConstants.insShHrg3);
//      TestUtils.execSql(TestConstants.insRefLegRep1);
//      TestUtils.execSql(TestConstants.insRefLegRep2);
//      TestUtils.execSql(TestConstants.insDef);
//      TestUtils.execSql(TestConstants.insDefOnCase);
//      TestUtils.execSql(TestConstants.insSchedHearDef);
//      TestUtils.execSql(TestConstants.insShLegRep3);
//      TestUtils.execSql(TestConstants.insShLegRep4);
//    }
//    catch (Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//
//  }
//
//  /**
//   * This will test all valid types + all known invalid types + some random ones
//   * Random ones are never valid.
//   * testValidateCaseTypes
//   */
//  public void testValidateCaseTypes()
//  {
//    try
//    {
//      log.debug(">>>>>>>>>>>> testValidateCaseTypes START <<<<<<<<<<<<<");
//
//      Boolean isCaseValid1 = helper.validateCaseTypes(case1_T);
//      log.debug("Test 1 : " + isCaseValid1.toString());
//      log.debug("Should be TRUE");
//
//      Boolean isCaseValid2 = helper.validateCaseTypes(case2_S);
//      log.debug("Test 2 : " + isCaseValid2.toString());
//      log.debug("Should be TRUE");
//
//      Boolean isCaseValid3 = helper.validateCaseTypes(case3_B);
//      log.debug("Test 3 : " + isCaseValid3.toString());
//      log.debug("Should be FALSE");
//
//      Boolean isCaseValid4 = helper.validateCaseTypes(case4_A);
//      log.debug("Test 4 : " + isCaseValid4.toString());
//      log.debug("Should be TRUE");
//
//      Boolean isCaseValid5 = helper.validateCaseTypes(case5_A_O);
//      log.debug("Test 5 : " + isCaseValid5.toString());
//      log.debug("Should be FALSE");
//
//      Boolean isCaseValid6 = helper.validateCaseTypes(case6_U);
//      log.debug("Test 6 : " + isCaseValid6.toString());
//      log.debug("Should be FALSE");
//
//      Boolean isCaseValid7 = helper.validateCaseTypes(case7_C);
//      log.debug("Test 7 : " + isCaseValid7.toString());
//      log.debug("Should be FALSE");
//
//      Boolean isCaseValid8 = helper.validateCaseTypes(case8_A_C);
//      log.debug("Test 8 : " + isCaseValid8.toString());
//      log.debug("Should be TRUE");
//
//
//      log.debug(">>>>>>>>>>>> testValidateCaseTypes FINISH <<<<<<<<<<<<<,");
//
//      if(isCaseValid1.booleanValue() && isCaseValid2.booleanValue() &&
//         !isCaseValid3.booleanValue() && isCaseValid4.booleanValue() &&
//         !isCaseValid5.booleanValue() && !isCaseValid6.booleanValue() &&
//         !isCaseValid7.booleanValue() && isCaseValid8.booleanValue())
//      {
//        this.assertTrue(true);
//      }
//      else
//      {
//        this.fail();
//      }
//
//    }
//    catch(Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      this.fail();
//    }
//  }
//
//
//  /**
//   * this will test a valid date and an invalid date
//   * testValidateAdjournedDate
//   */
//  public void testValidateAdjournedDate()
//  {
//    log.debug(">>>>>>>>>>>> testValidateAdjournedDate START <<<<<<<<<<<<<");
//
//    Date adjournedToDate = new Date();//todays date
//    Date veryearlyDate = new Date(34343);
//    log.debug(">>>>>>>>> First helper Date : " + dateHelper.getFirstHearingDate().toString());
//    log.debug(">>>>>>>>> Last helper Date : " + dateHelper.getLastHearingDate().toString());
//    log.debug(">>>>>>>>> Today's Date : " + adjournedToDate.toString());
//    log.debug(">>>>>>>>> Very early Date : " + veryearlyDate.toString());
//
//    //test with a valid date first - no exceptions expected.
//    try
//    {
//      helper.validateAdjournedDate(dateHelper, adjournedToDate);
//    }
//    catch(Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      this.fail();
//    }
//    //test with a date that is very much in the past - expect validation excption
//    try
//    {
//      helper.validateAdjournedDate(dateHelper, adjournedToDate);
//    }
//    catch(HrAdjournedDateException e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      log.debug(">>>>>>>>>> caught the expected exception HrAdjournedDateException - the test is a success");
//      this.assertTrue(true);
//
//    }
//    catch(Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      this.fail();
//    }
//    finally
//    {
//      log.debug(">>>>>>>>>>>> testValidateAdjournedDate FINISH <<<<<<<<<<<<<");
//    }
//  }
//
//  /**
// * this will test a valid date and an invalid date
// * date of application must be between start and end date.
// *
// * testValidateApplicationDate
// */
//public void testValidateApplicationDate()
//{
//  log.debug(">>>>>>>>>>>> testValidateApplicationDate START <<<<<<<<<<<<<");
//
//  Date dateOfApplication1 = new Date(534567899893L); //valid
//  Date dateOfApplication2 = new Date(934567899893L); //invalid
//  log.debug(">>>>>>>>> First helper Date : " + dateHelper.getFirstHearingDate().toString());
//  log.debug(">>>>>>>>> Last helper Date : " + dateHelper.getLastHearingDate().toString());
//  log.debug(">>>>>>>>> Today's Date : " + dateOfApplication1.toString());
//  log.debug(">>>>>>>>> Today's Date : " + dateOfApplication2.toString());
//
//  //test with a valid date first - no exceptions expected.
//  try
//  {
//    helper.validateApplicationDate(dateHelper, dateOfApplication1);
//  }
//  catch(Exception e)
//  {
//    log.debug(e.toString());
//    e.printStackTrace();
//    this.fail();
//  }
//  //test with a date that is very much in the past - expect validation excption
//  try
//  {
//    helper.validateApplicationDate(dateHelper, dateOfApplication2);
//  }
//  catch(HearingRecordApplicationDateException e)
//  {
//    log.debug(e.toString());
//    e.printStackTrace();
//    log.debug(">>>>>>>>>> caught the expected exception HearingRecordApplicationDateException - the test is a success");
//    this.assertTrue(true);
//  }
//  catch(Exception e)
//  {
//    log.debug(e.toString());
//    e.printStackTrace();
//    this.fail();
//  }
//  finally
//  {
//    log.debug(">>>>>>>>>>>> testValidateApplicationDate FINISH <<<<<<<<<<<<<");
//  }
//  }
//
//  /**
//   * Valid entries 'B'=bail, 'C'=custody, 'J'=in case  or 'N'=not applicable
//   * testValidateBailStatus
//   */
//  public void testValidateBailStatus()
//  {
//    log.debug(">>>>>>>>>>>> testValidateBailStatus START <<<<<<<<<<<<<");
//    String B = "B";
//    String C = "C";
//    String J = "J";
//    String N = "N";
//    String V = "V";
//
//    try
//    {
//      //This test should not throw any exceptions
//      helper.validateBailStatus(B);
//      helper.validateBailStatus(C);
//      helper.validateBailStatus(J);
//      helper.validateBailStatus(N);
//    }
//    catch(Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//
//    try
//    {
//      //This is not a valid test so a HrBailStatusException is expected.
//      helper.validateBailStatus(V);
//    }
//    catch(HrBailStatusException e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      log.debug(">>>>>>>>>> caught the expected exception HearingRecordApplicationDateException - the test is a success");
//      this.assertTrue(true);
//    }
//    catch(Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    finally
//    {
//      log.debug(">>>>>>>>>>>> testValidateBailStatus FINISH <<<<<<<<<<<<<");
//    }
//  }
//
//  /**
//   * Tests validating a hearing where defence representation is a barrister
//   * and does have the category set - should pass validation
//   */
//  public void testValidateHearingRepresentation()
//  {
//      HearingMaintainer maintainer = new HearingMaintainer();
//      try
//      {
//          Hearing hearing1 = maintainer.findByPK(TestConstants.hearingID);
//        try
//        {
//            // hearing1 should validate ok
//            helper.validateHearingRepresentation(hearing1);
//        }
//        catch (HearingRecordException ex)
//        {
//            fail("Hearing 1 was not validated");
//        }
//      }
//      catch (ObjectNotFoundException ex)
//      {
//          fail("Problem finding hearing with id " + TestConstants.hearingID +
//               " should have been created in setup");
//      }
//  }
//
//  /**
//   * Tests validating a hearing where defence representation is a barrister
//   * but does not have the category set - should fail validation
//   */
//  public void testValidateHearingRepresentationException()
//  {
//      HearingMaintainer maintainer = new HearingMaintainer();
//      try
//      {
//          Hearing hearing2 = maintainer.findByPK(TestConstants.hearingID2);
//        try
//        {
//            // hearing2 should fail validaion
//            helper.validateHearingRepresentation(hearing2);
//            fail("Hearing 2 was validated, this is incorrect");
//        }
//        catch (HearingRecordException ex)
//        {
//            // should catch exception here
//        }
//      }
//      catch (ObjectNotFoundException ex)
//      {
//          fail("Problem finding hearing with id " + TestConstants.hearingID2 +
//               " should have been created in setup");
//      }
//  }
//}