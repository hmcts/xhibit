//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingheader;
//
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.GregorianCalendar;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Vector;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.caze.Case;
//import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
//import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
//import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
//import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
//import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
//import uk.gov.courtservice.xhibit.business.entities.shlegrep.ShLegRep;
//import uk.gov.courtservice.xhibit.business.entities.shlegrep.ShLegRepMaintainer;
//import uk.gov.courtservice.xhibit.business.entities.xhb_hearing_leg_rep.XhbHearingLegRepBasicValue;
//import uk.gov.courtservice.xhibit.business.entities.xhb_hearing_leg_rep.XhbHearingLegRepBeanHelper;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingheader.HearingHeaderWorkFlow;
//import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepBasicValue;
//
///**
// * <p>Title: Test HearingHeaderWorkFlow</p>
// * <p>Description: Test the update of the case from the hearing header</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// *
// */
//public class TestHearingHeaderWorkFlowTest extends TransactionTestCase
//{
//
//  private static Logger log  = CSServices.getLogger(TestHearingHeaderWorkFlowTest.class);
//
//  //static / final values
//
//  //Case ids to be used!
//  private static Integer CASE_ID_T;
//  private static Integer CASE_ID_S;
//  private static Integer CASE_ID_T_IN_PROGRESS;
//
//  private final static String SENTENCE = "S";
//  private final static String TRIAL = "T";
//  private static final String EXPORT_IN_PROGRESS = "U";
//  /** The hearing id for the test case */
//  private static final Integer HEARING_ID = new Integer(1);
//  /** The defendant on case id for the test case */
//  private static final Integer DEFENDANT_ON_CASE_ID = new Integer(1);
//  /** The scheduledhearing id for the test case */
//  private static final Integer SCHEDULED_HEARING_ID = new Integer(1);
//  /** The Legal Rep Type id for the test case */
//  private static final Integer REF_LEG_TYPE_ID = new Integer(1);
//  /** The Hearing Legal Rep id for the test case */
//  private static final Integer REF_LEG_REP_ID = new Integer(1);
//  /** The Scheduled Hearing Rep Type id for the test case */
//  private static Integer SH_LEG_REP_ID1;
//  /** The Scheduled Hearing Rep Type id for the test case */
//  private static Integer SH_LEG_REP_ID2;
//
//
//  //SQL queries
//  private final static String FIND_CASE = "SELECT * FROM XHB_CASE WHERE CASE_TYPE = 'T' OR CASE_TYPE = 'S'";
//  private final static String INSERT_HEARING_LEG_REP_ADD1 = "INSERT INTO xhb_hearing_leg_rep (HEARING_ID,REF_LEGAL_REP_ID, START_DATE, END_DATE) VALUES ( 1, 1, TO_Date( '05/01/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '05/02/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//  private final static String INSERT_HEARING_LEG_REP_ADD2 = "INSERT INTO xhb_hearing_leg_rep (HEARING_ID,REF_LEGAL_REP_ID, START_DATE, END_DATE) VALUES ( 1, 1, TO_Date( '04/28/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '04/29/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//  private final static String INSERT_HEARING_LEG_REP_ADD3 = "INSERT INTO xhb_hearing_leg_rep (HEARING_ID,REF_LEGAL_REP_ID, START_DATE, END_DATE) VALUES ( 1, 1, TO_Date( '05/04/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '05/05/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//  private final static String INSERT_HEARING_LEG_REP_ADD4 = "INSERT INTO xhb_hearing_leg_rep (HEARING_ID,REF_LEGAL_REP_ID, START_DATE, END_DATE) VALUES ( 1, 1, TO_Date( '05/02/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '05/05/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//  private final static String INSERT_HEARING_LEG_REP_REMOVE1a = "INSERT INTO xhb_hearing_leg_rep (HEARING_ID,REF_LEGAL_REP_ID, START_DATE, END_DATE) VALUES ( 1, 1, TO_Date( '05/03/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '05/03/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//  private final static String INSERT_HEARING_LEG_REP_REMOVE1b = "INSERT INTO xhb_hearing_leg_rep (HEARING_ID,REF_LEGAL_REP_ID, START_DATE, END_DATE) VALUES ( 1, 1, TO_Date( '05/05/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '05/07/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//  private final static String INSERT_HEARING_LEG_REP_REMOVE1c = "INSERT INTO xhb_hearing_leg_rep (HEARING_ID,REF_LEGAL_REP_ID, START_DATE, END_DATE) VALUES ( 1, 1, TO_Date( '05/09/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '05/11/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//  private final static String INSERT_HEARING_LEG_REP_REMOVE2a = "INSERT INTO xhb_hearing_leg_rep (HEARING_ID,REF_LEGAL_REP_ID, START_DATE, END_DATE) VALUES ( 1, 1, TO_Date( '05/03/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '05/04/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//  private final static String INSERT_HEARING_LEG_REP_REMOVE2b = "INSERT INTO xhb_hearing_leg_rep (HEARING_ID,REF_LEGAL_REP_ID, START_DATE, END_DATE) VALUES ( 1, 1, TO_Date( '05/01/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '05/02/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//  private final static String INSERT_HEARING_LEG_REP_REMOVE2c = "INSERT INTO xhb_hearing_leg_rep (HEARING_ID,REF_LEGAL_REP_ID, START_DATE, END_DATE) VALUES ( 1, 1, TO_Date( '05/05/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '05/06/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//  private final static String INSERT_HEARING_LEG_REP_REMOVE3a = "INSERT INTO xhb_hearing_leg_rep (HEARING_ID,REF_LEGAL_REP_ID, START_DATE, END_DATE) VALUES ( 1, 1, TO_Date( '05/01/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '05/03/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//  private final static String INSERT_HEARING_LEG_REP_REMOVE3b = "INSERT INTO xhb_hearing_leg_rep (HEARING_ID,REF_LEGAL_REP_ID, START_DATE, END_DATE) VALUES ( 1, 1, TO_Date( '05/04/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '05/06/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//  private final static String INSERT_HEARING_LEG_REP_REMOVE3c = "INSERT INTO xhb_hearing_leg_rep (HEARING_ID,REF_LEGAL_REP_ID, START_DATE, END_DATE) VALUES ( 1, 1, TO_Date( '04/29/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '04/30/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//  private final static String INSERT_HEARING_LEG_REP_REMOVE4a = "INSERT INTO xhb_hearing_leg_rep (HEARING_ID,REF_LEGAL_REP_ID, START_DATE, END_DATE) VALUES ( 1, 1, TO_Date( '04/29/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '05/05/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//  private final static String INSERT_HEARING_LEG_REP_REMOVE4b = "INSERT INTO xhb_hearing_leg_rep (HEARING_ID,REF_LEGAL_REP_ID, START_DATE, END_DATE) VALUES ( 1, 1, TO_Date( '04/27/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '04/28/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//  private final static String INSERT_HEARING_LEG_REP_REMOVE4c = "INSERT INTO xhb_hearing_leg_rep (HEARING_ID,REF_LEGAL_REP_ID, START_DATE, END_DATE) VALUES ( 1, 1, TO_Date( '05/06/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '05/07/2003 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//
//  private final static String INSERT_SH_LEG_REP_REMOVE1 = "INSERT INTO XHB_SH_LEG_REP (is_signed_in, sol_firm_or_ref_legal_rep, ref_legal_rep_id, scheduled_hearing_id) VALUES ('Y','L',1,1)";
//  private final static String INSERT_SH_LEG_REP_REMOVE2 = "INSERT INTO XHB_SH_LEG_REP (is_signed_in, sol_firm_or_ref_legal_rep, ref_legal_rep_id, scheduled_hearing_id) VALUES ('Y','L',1,2)";
//
//  private final static String SELECT_SH_LEG_REP_REMOVE = "SELECT MAX(sh_leg_rep_id) FROM XHB_SH_LEG_REP";
//
//  public TestHearingHeaderWorkFlowTest(String name) throws Exception
//  {
//    super(name, true);/** @todo change to false for data to remain in DB */
//  }
//
//  /**
//   * SetUp method to find the first available T and S case. This so that no
//   * primary keys need to be hard coded in this class.
//   *
//   * It is assumed that these case exists and have
//   * - a valid hearing
//   * - a valid scheduled hearing
//   *
//   * @throws Exception
//   */
//  protected void setUp()throws Exception
//  {
//    super.setUp();
//
//    if(CASE_ID_T == null || CASE_ID_S == null)
//    {
//      //Find all cases
//      Statement stmt = connection.createStatement();
//      ResultSet rs = stmt.executeQuery(FIND_CASE);
//
//      while(rs.next())
//      {
//        log.debug("Case type: " + rs.getString("CASE_TYPE"));
//        System.out.println("Case type :" + rs.getString("CASE_TYPE"));
//
//        if(CASE_ID_T == null && rs.getString("CASE_TYPE").equalsIgnoreCase(TRIAL))
//        {
//          CASE_ID_T = new Integer(rs.getInt("CASE_ID"));
//          System.out.println("Found a Trial case with id : " + CASE_ID_T);
//          log.debug("Found a Trial case with id : " + CASE_ID_T);
//          continue;
//        }
//
//        if(CASE_ID_S == null && rs.getString("CASE_TYPE").equalsIgnoreCase(SENTENCE))
//        {
//          CASE_ID_S = new Integer(rs.getInt("CASE_ID"));
//          System.out.println("Found a sentence case with id : " + CASE_ID_S);
//          log.debug("Found a sentence case with id : " + CASE_ID_S);
//          continue;
//        }
//
//        //we need a seperate case for the test where the export is in progress
//        //the case must be of type trial
//        if(CASE_ID_T_IN_PROGRESS == null &&
//           rs.getString("CASE_TYPE").equalsIgnoreCase(TRIAL) &&
//           (CASE_ID_T != null && CASE_ID_T.intValue() != rs.getInt("CASE_ID")))
//        {
//          CASE_ID_T_IN_PROGRESS = new Integer(rs.getInt("CASE_ID"));
//          System.out.println("Found a Trial case with id : " + CASE_ID_T_IN_PROGRESS);
//          log.debug("Found a Trial case with id : " + CASE_ID_T_IN_PROGRESS);
//        }
//
//        //when we have all cases then break the loop
//        if(CASE_ID_S != null && CASE_ID_T != null && CASE_ID_T_IN_PROGRESS != null)
//        {
//          break;
//        }
//      }
//    }
//  }
//
//  /**
//   * Run the test with all values passed in as null.
//   * CaseBasicValue = null
//   * scheduledHearingId = null
//   * hearingProgress = null
//   */
//
//  public void testUpdateHHMain1()
//  {
//    log.debug("Run testUpdateHHMain1");
//    System.out.println("Run testUpdateHHMain1");
//    HearingHeaderWorkFlow hearingheaderworkflow = new HearingHeaderWorkFlow();
//    CaseBasicValue caseBasicValue1=  null;
//    Integer scheduledHearingId2=  null;
//    Integer hearingProgress3=  null;
//    try
//    {
//      //Call the update method
//      hearingheaderworkflow.updateHHMain(caseBasicValue1, scheduledHearingId2, hearingProgress3);
//
//      //set to true if app managed null-values
//      assertTrue(true);
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      System.err.println("Exception thrown:  "+e);
//
//      //fail the test if error thrown
//      assertTrue(false);
//    }
//    log.debug("testUpdateHHMain1 finished");
//  }
//
//  /**
//   * Test where the caseBasicValue is populated but not the rest of the argument
//   * they are still null. Try for a
//   * CaseBasicValue populated from static value CASE_ID
//   * scheduledHearingId = null
//   * hearingProgress = null
//   */
//
//  public void testUpdateHHMain2()
//  {
//    log.debug("Run testUpdateHHMain2");
//    System.out.println("Run testUpdateHHMain2");
//    HearingHeaderWorkFlow hearingheaderworkflow = new HearingHeaderWorkFlow();
//
//    Integer scheduledHearingId2=  null;
//    Integer hearingProgress3=  null;
//    try
//    {
//      CaseBasicValue caseBasicValue = setCaseBasicValue(CASE_ID_T);
//      caseBasicValue.setIndChangeStatus(null);
//
//      //run the real test.
//      hearingheaderworkflow.updateHHMain(caseBasicValue, scheduledHearingId2, hearingProgress3);
//
//      //set to true if app managed null-values
//      assertTrue(true);
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      System.err.println("Exception thrown:  "+e);
//
//      //fail the test if error thrown
//      assertTrue(false);
//    }
//    log.debug("testUpdateHHMain2 finished");
//  }
//
//  /**
//   * Test where the caseBasicValue and scheduledHearingID are populated but
//   * not the hearingProgress.
//   * CaseBasicValue populated from static value CASE_ID
//   * scheduledHearingId populated from derivied values from static value CASE_ID
//   * hearingProgress = null
//   */
//
//  public void testUpdateHHMain3()
//  {
//    log.debug("Run testUpdateHHMain3");
//    System.out.println("Run testUpdateHHMain3");
//    HearingHeaderWorkFlow hearingheaderworkflow = new HearingHeaderWorkFlow();
//
//    Integer hearingProgress3=  null;
//    try
//    {
//      CaseBasicValue caseBasicValue = setCaseBasicValue(CASE_ID_T);
//      caseBasicValue.setIndChangeStatus(null);
//
//      //Set the scheduled hearing for the passed in case
//      Integer scheduledHearingId2=  setScheduledHearingId(CASE_ID_T);
//      //run the real test.
//      hearingheaderworkflow.updateHHMain(caseBasicValue, scheduledHearingId2, hearingProgress3);
//
//      //set to true if app managed null-values
//      assertTrue(true);
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      System.err.println("Exception thrown:  "+e);
//
//      //fail the test if error thrown
//      assertTrue(false);
//    }
//    log.debug("testUpdateHHMain3 finished");
//  }
//
//  /**
//   * Test where the caseBasicValue and scheduledHearingID are populated but
//   * not the hearingProgress.
//   * CaseBasicValue populated from static value CASE_ID
//   * scheduledHearingId populated from derivied values from static value CASE_ID
//   * hearingProgress populated with 1
//   */
//
//  public void testUpdateHHMain4()
//  {
//    log.debug("Run testUpdateHHMain4");
//    System.out.println("Run testUpdateHHMain4");
//    HearingHeaderWorkFlow hearingheaderworkflow = new HearingHeaderWorkFlow();
//
//    Integer hearingProgress3= new Integer(1);
//    try
//    {
//      CaseBasicValue caseBasicValue = setCaseBasicValue(CASE_ID_T);
//      caseBasicValue.setIndChangeStatus(null);
//
//      //Set the scheduled hearing for the passed in case
//      Integer scheduledHearingId2=  setScheduledHearingId(CASE_ID_T);
//
//      //run the real test.
//      hearingheaderworkflow.updateHHMain(caseBasicValue, scheduledHearingId2, hearingProgress3);
//
//      //set to true if all went through
//      assertTrue(true);
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      System.err.println("Exception thrown:  "+e);
//
//      //fail the test if error thrown
//      assertTrue(false);
//    }
//    log.debug("testUpdateHHMain4 finished");
//  }
//
//  /**
//   * Test where the caseBasicValue and scheduledHearingID are populated but
//   * not the hearingProgress.
//   * CaseBasicValue populated from static value CASE_ID
//   * scheduledHearingId = null
//   * hearingProgress populated with 1
//   */
//
//  public void testUpdateHHMain5()
//  {
//    log.debug("Run testUpdateHHMain5");
//    System.out.println("Run testUpdateHHMain5");
//    HearingHeaderWorkFlow hearingheaderworkflow = new HearingHeaderWorkFlow();
//
//    Integer scheduledHearingId2=  null;
//    Integer hearingProgress3= new Integer(1);
//
//    try
//    {
//      CaseBasicValue caseBasicValue = setCaseBasicValue(CASE_ID_T);
//      caseBasicValue.setIndChangeStatus(null);
//
//      //run the real test.
//      hearingheaderworkflow.updateHHMain(caseBasicValue, scheduledHearingId2, hearingProgress3);
//
//      //set to true if all went through
//      assertTrue(true);
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      System.err.println("Exception thrown:  "+e);
//
//      //fail the test if error thrown
//      assertTrue(false);
//    }
//    log.debug("testUpdateHHMain5 finished");
//  }
//
//  /**
//   * Test where the caseBasicValue and scheduledHearingID are populated but
//   * not the hearingProgress.
//   *
//   * NOTE: THIS TEST IS ONLY SUCCESSFUL IF IT FAILS BECAUSE OF VALIDATION RULES.
//   * Try to break the validation rules - by using a S case!
//   * CaseBasicValue populated from static value CASE_ID
//   *
//   * scheduledHearingId = null
//   * hearingProgress populated with 1
//   */
//
//  public void testUpdateHHMain6()
//  {
//    log.debug("Run testUpdateHHMain6");
//    System.out.println("Run testUpdateHHMain6");
//    HearingHeaderWorkFlow hearingheaderworkflow = new HearingHeaderWorkFlow();
//    Integer hearingProgress3= new Integer(1);
//    try
//    {
//      CaseBasicValue caseBasicValue = setCaseBasicValue(CASE_ID_S);
//      caseBasicValue.setIndChangeStatus(null);
//
//      //get the derived scheduled hearing from the case
//      Integer scheduledHearingId2=  setScheduledHearingId(CASE_ID_S);
//
//      //run the real test.
//      hearingheaderworkflow.updateHHMain(caseBasicValue, scheduledHearingId2, hearingProgress3);
//
//      //set to false if all went through since this should fail for an S case.
//      assertTrue(false);
//    }
//    catch(HearingScheduleException hse)
//    {
//      hse.printStackTrace();
//      System.err.println("Expected exception thrown:  "+hse);
//      System.err.println("getUserMessage"+hse.getUserMessage());
//      System.err.println("getUserMessage.getKey"+hse.getUserMessageAsMessage().getKey());
//
//      //if valid expected exception thrown the test was successful
//      assertTrue(true);
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      System.err.println("Exception thrown:  "+e);
//
//      //fail the test if error thrown
//      assertTrue(false);
//    }
//    log.debug("testUpdateHHMain6 finished");
//  }
//
//  /**
//   * Test to save a case when the export of it is in progress.
//   *
//   * NOTE: THIS TEST IS ONLY SUCCESSFUL IF IT FAILS BECAUSE OF VALIDATION RULES.
//   */
//
// public void testUpdateHHMain7()
// {
//   log.debug("Run testUpdateHHMain7");
//   System.out.println("Run testUpdateHHMain7");
//
//   try
//   {
//     CaseBasicValue caseBasicValue = setCaseBasicValue(CASE_ID_T_IN_PROGRESS);
//     caseBasicValue.setIndChangeStatus(EXPORT_IN_PROGRESS);
//     log.debug("Case basic value after set export in progress : " + caseBasicValue);
//
//     HearingHeaderWorkFlow hearingheaderworkflow = new HearingHeaderWorkFlow();
//     Integer hearingProgress3 = new Integer(1);
//
//     //get the derived scheduled hearing from the case
//     Integer scheduledHearingId2 =  setScheduledHearingId(CASE_ID_T_IN_PROGRESS);
//
//     //run the real test.
//     log.debug("Case basic value before update : " + caseBasicValue);
//     hearingheaderworkflow.updateHHMain(caseBasicValue, scheduledHearingId2, hearingProgress3);
//     log.debug("After update : " + caseBasicValue);
//
//     //set to false if all went through since this should fail for an S case.
//     assertTrue(false);
//   }
//   catch(HearingScheduleException hse)
//   {
//     hse.printStackTrace();
//     System.err.println("Expected exception thrown:  "+hse);
//     System.err.println("getUserMessage"+hse.getUserMessage());
//     System.err.println("getUserMessage.getKey"+hse.getUserMessageAsMessage().getKey());
//
//     //if valid expected exception thrown the test was successful
//     assertTrue(true);
//   }
//   catch(Exception e)
//   {
//     e.printStackTrace();
//     System.err.println("Exception thrown:  "+e);
//
//     //fail the test if error thrown
//     assertTrue(false);
//   }
//   log.debug("testUpdateHHMain7 finished");
//  }
//
//
//  /**
//   * Method to find an existing case from the database. Just change the
//   * primary key for the case if testing with another case.
//   *
//   * @return CaseBasicValue
//   */
//
//  private CaseBasicValue setCaseBasicValue(Integer caseId) throws Exception
//  {
//    log.debug("setCaseBasicValue called - try to find case with id: "+caseId);
//    if(caseId == null)
//    {
//      throw new Exception("COULD NOT FIND ANY CASES IN THE DATABASE "+
//                          "- PLEASE LOAD THE DATABASE AND RETRY!");
//    }
//    CaseMaintainer maintainer = new CaseMaintainer();
//    Case caze = maintainer.findByPrimaryKey(caseId);
//    log.debug("Case found");
//    CaseBasicValue caseBasicValue = maintainer.getCaseBasicValue(caze);
//
//    caseBasicValue.setClassCode(new Integer(1));
//    caseBasicValue.setOffenceGroupCode("B");
//
//    caseBasicValue.setCrestSeveredInd(null);
//    return caseBasicValue;
//  }
//
//
//  /**
//   * Find a hearing and a scheduled hearing for CASE_ID.
//   * @return Integer - primary key for scheudled hearing.
//   * @throws Exception
//   */
//
//  private Integer setScheduledHearingId(Integer caseId) throws Exception
//  {
//    log.debug("setScheduledHearingId called by case id : "+caseId);
//    //get the hearing maintainer.
//    HearingMaintainer maintainer = new HearingMaintainer();
//    Integer shId = null;
//
//    //Get the collection of hearings for the case
//    Collection hearings = maintainer.findByCaseId(caseId);
//
//    if(hearings != null)
//    {
//      log.debug("Hearings found : " + hearings.size());
//    }
//    else
//    {
//      log.debug("No hearings found!");
//    }
//
//    if(hearings != null && hearings.size() > 0)
//    {
//      //just get the first hearing
//      Hearing hearing = (Hearing)hearings.iterator().next();
//
//      //find the scheduled hearings for the hearing.
//      ScheduledHearingMaintainer shMaintainer = new ScheduledHearingMaintainer();
//      Collection scheduledHearings = shMaintainer.findByHearingId(hearing.getHearingId());
//
//      //just get the first scheduled hearing
//      if(scheduledHearings != null && scheduledHearings.size() > 0)
//      {
//        ScheduledHearing scheduledHearing = (ScheduledHearing)scheduledHearings.iterator().next();
//        shId = scheduledHearing.getScheduledHearingId();
//      }
//    }
//
//    log.debug("Scheduled Hearing id to be returned : " + shId);
//
//    //return the primary key
//    return shId;
//  }
//
//  /**
//   * Run the test on inserted record with following dates:
//   * StartDate - 01/05/2003
//   * EndDate -  02/05/2003
//   * Legal Rep added on : 03/05/2003
//   *
//   * Expected Result: The existing record's EndDate is extended to 03/05/2003
//   */
//  public void testAddLegalRep1() throws Exception
//  {
//      log.debug("*** Run testAddLegalRep1");
//      System.out.println("*** Run testAddLegalRep1");
//
//      //Initialise data before this test
//      Statement stmt = connection.createStatement();
//      stmt.executeUpdate(INSERT_HEARING_LEG_REP_ADD1);
//
//      // Setting month May
//      Calendar startCal = new GregorianCalendar(2003,04,01);
//      java.util.Date startDate = startCal.getTime();
//
//      // Setting month May
//      Calendar endCal = new GregorianCalendar(2003,04,03);
//      java.util.Date endDate = endCal.getTime();
//
//      try
//      {
//          //create SHLegRepBasicValue to pass to HearingHeaderWorkFlow.addLegalRep
//          SHLegRepBasicValue shLegRepBasicValue = new SHLegRepBasicValue();
//          shLegRepBasicValue.setScheduledHearingID(SCHEDULED_HEARING_ID);
//          shLegRepBasicValue.setCcInfoID(null);
//          shLegRepBasicValue.setCrestSequenceNo(null);
//          shLegRepBasicValue.setIsSignIn( "Y" );
//          shLegRepBasicValue.setRefDefenceCategoryID(null);
//          shLegRepBasicValue.setRefLegalRepID(REF_LEG_TYPE_ID);
//          shLegRepBasicValue.setSolFirmOrRefLegalRep( "L" );
//
//          //call addLegalRep method
//          HearingHeaderWorkFlow hearingheaderworkflow = new HearingHeaderWorkFlow();
//          hearingheaderworkflow.addLegalRep(shLegRepBasicValue);
//
//          //search for BVs for Hearing Id and Legal rep Id
//          XhbHearingLegRepBasicValue[] legRepsBVs =  XhbHearingLegRepBeanHelper.findByHearingAndRefLegRepValue(HEARING_ID, REF_LEG_REP_ID);
//
//          assertNotNull("XhbHearingLegRepBeanHelper.findByHearingAndRefLegRepValue"
//                            + " should return at least one value", legRepsBVs);
//
//          //sort values return in descending startDate order
//          List legRepList = Arrays.asList(legRepsBVs);
//          Collections.sort(legRepList, EVENT_DATE_ORDER);
//
//          Iterator legRepsIter = legRepList.iterator();
//          XhbHearingLegRepBasicValue legRepBV;
//
//          // There will only be one for this test
//          if (legRepsIter.hasNext())
//          {
//              legRepBV = (XhbHearingLegRepBasicValue)legRepsIter.next();
//              System.out.println("HearingLegRepId: " + legRepBV.getHearingLegRepId());
//              System.out.println("Start Date: " + legRepBV.getStartDate());
//              System.out.println("End Date: " + legRepBV.getEndDate());
//              assertEquals("Start Date must be equal to that set",startDate,resetTime(legRepBV.getStartDate()));
//              assertEquals("End Date must be equal to that set",endDate,resetTime(legRepBV.getEndDate()));
//          }
//      }
//      catch(HearingScheduleException hse)
//      {
//          hse.printStackTrace();
//          System.err.println("Expected exception thrown:  "+hse);
//          System.err.println("getUserMessage"+hse.getUserMessage());
//          System.err.println("getUserMessage.getKey"+hse.getUserMessageAsMessage().getKey());
//
//          //if valid expected exception thrown the test was successful
//          assertTrue(true);
//      }
//      catch(Exception e)
//      {
//          e.printStackTrace();
//          System.err.println("Exception thrown:  "+e);
//
//          //fail the test if error thrown
//          assertTrue(false);
//      }
//      log.debug("testAddLegalRep1 finished");
//  }
//
//  /**
//   * Run the test on inserted record with following dates:
//   * StartDate - 28/04/2003
//   * EndDate -  29/04/2003
//   * Legal Rep added on : 03/05/2003
//   *
//   * Expected Result: A new record is created with both StartDate and EndDate set to 03/05/2003
//   */
//  public void testAddLegalRep2() throws Exception
//  {
//      log.debug("*** Run testAddLegalRep1");
//      System.out.println("*** Run testAddLegalRep1");
//
//      //Initialise data before this test
//      Statement stmt = connection.createStatement();
//      stmt.executeUpdate(INSERT_HEARING_LEG_REP_ADD2);
//
//      // Setting month May
//      Calendar startCal = new GregorianCalendar(2003,04,03);
//      java.util.Date startDate = startCal.getTime();
//
//      // Setting month May
//      Calendar endCal = new GregorianCalendar(2003,04,03);
//      java.util.Date endDate = endCal.getTime();
//
//      try
//      {
//          //create SHLegRepBasicValue to pass to HearingHeaderWorkFlow.addLegalRep
//          SHLegRepBasicValue shLegRepBasicValue = new SHLegRepBasicValue();
//          shLegRepBasicValue.setScheduledHearingID(SCHEDULED_HEARING_ID);
//          shLegRepBasicValue.setCcInfoID(null);
//          shLegRepBasicValue.setCrestSequenceNo(null);
//          shLegRepBasicValue.setIsSignIn( "Y" );
//          shLegRepBasicValue.setRefDefenceCategoryID(null);
//          shLegRepBasicValue.setRefLegalRepID(REF_LEG_TYPE_ID);
//          shLegRepBasicValue.setSolFirmOrRefLegalRep( "L" );
//
//          //call addLegalRep method
//          HearingHeaderWorkFlow hearingheaderworkflow = new HearingHeaderWorkFlow();
//          hearingheaderworkflow.addLegalRep(shLegRepBasicValue);
//
//          //search for BVs for Hearing Id and Legal rep Id
//          XhbHearingLegRepBasicValue[] legRepsBVs =  XhbHearingLegRepBeanHelper.findByHearingAndRefLegRepValue(HEARING_ID, REF_LEG_REP_ID);
//
//          assertNotNull("XhbHearingLegRepBeanHelper.findByHearingAndRefLegRepValue"
//                            + " should return at least one value", legRepsBVs);
//
//          //sort values return in descending startDate order
//          List legRepList = Arrays.asList(legRepsBVs);
//          Collections.sort(legRepList, EVENT_DATE_ORDER);
//
//          Iterator legRepsIter = legRepList.iterator();
//          XhbHearingLegRepBasicValue legRepBV;
//
//          // There will be more than one record for this hearing/legrep pair
//          boolean found = false;
//          while (legRepsIter.hasNext())
//          {
//              legRepBV = (XhbHearingLegRepBasicValue)legRepsIter.next();
//              System.out.println("HearingLegRepId: " + legRepBV.getHearingLegRepId());
//              System.out.println("Start Date: " + legRepBV.getStartDate());
//              System.out.println("End Date: " + legRepBV.getEndDate());
//              if (resetTime(legRepBV.getStartDate()).compareTo(startDate) == 0
//                  && resetTime(legRepBV.getEndDate()).compareTo(endDate) == 0)
//              {
//                  found = true;
//                  break;
//              }
//          }
//          assertTrue("A record must exists with start and end date equal to notBefore Date",found);
//      }
//      catch(HearingScheduleException hse)
//      {
//          hse.printStackTrace();
//          System.err.println("Expected exception thrown:  "+hse);
//          System.err.println("getUserMessage"+hse.getUserMessage());
//          System.err.println("getUserMessage.getKey"+hse.getUserMessageAsMessage().getKey());
//
//          //if valid expected exception thrown the test was successful
//          assertTrue(true);
//      }
//      catch(Exception e)
//      {
//          e.printStackTrace();
//          System.err.println("Exception thrown:  "+e);
//
//          //fail the test if error thrown
//          assertTrue(false);
//      }
//      log.debug("testAddLegalRep1 finished");
//  }
//
//  /**
//   * Run the test on inserted record with following dates:
//   * StartDate - 04/05/2003
//   * EndDate -  05/05/2003
//   * Legal Rep added on : 03/05/2003
//   *
//   * Expected Result: The existing record's StartDate is set to 03/05/2003
//   */
//  public void testAddLegalRep3() throws Exception
//  {
//      log.debug("*** Run testAddLegalRep1");
//      System.out.println("*** Run testAddLegalRep1");
//
//      //Initialise data before this test
//      Statement stmt = connection.createStatement();
//      stmt.executeUpdate(INSERT_HEARING_LEG_REP_ADD3);
//
//      // Setting month May
//      Calendar startCal = new GregorianCalendar(2003,04,03);
//      java.util.Date startDate = startCal.getTime();
//
//      // Setting month May
//      Calendar endCal = new GregorianCalendar(2003,04,05);
//      java.util.Date endDate = endCal.getTime();
//
//      try
//      {
//          //create SHLegRepBasicValue to pass to HearingHeaderWorkFlow.addLegalRep
//          SHLegRepBasicValue shLegRepBasicValue = new SHLegRepBasicValue();
//          shLegRepBasicValue.setScheduledHearingID(SCHEDULED_HEARING_ID);
//          shLegRepBasicValue.setCcInfoID(null);
//          shLegRepBasicValue.setCrestSequenceNo(null);
//          shLegRepBasicValue.setIsSignIn( "Y" );
//          shLegRepBasicValue.setRefDefenceCategoryID(null);
//          shLegRepBasicValue.setRefLegalRepID(REF_LEG_TYPE_ID);
//          shLegRepBasicValue.setSolFirmOrRefLegalRep( "L" );
//
//          //call addLegalRep method
//          HearingHeaderWorkFlow hearingheaderworkflow = new HearingHeaderWorkFlow();
//          hearingheaderworkflow.addLegalRep(shLegRepBasicValue);
//
//          //search for BVs for Hearing Id and Legal rep Id
//          XhbHearingLegRepBasicValue[] legRepsBVs =  XhbHearingLegRepBeanHelper.findByHearingAndRefLegRepValue(HEARING_ID, REF_LEG_REP_ID);
//
//          assertNotNull("XhbHearingLegRepBeanHelper.findByHearingAndRefLegRepValue"
//                            + " should return at least one value", legRepsBVs);
//
//          //sort values return in descending startDate order
//          List legRepList = Arrays.asList(legRepsBVs);
//          Collections.sort(legRepList, EVENT_DATE_ORDER);
//
//          Iterator legRepsIter = legRepList.iterator();
//          XhbHearingLegRepBasicValue legRepBV;
//
//          // There will be more than one record for this hearing/legrep pair
//          boolean found = false;
//          while (legRepsIter.hasNext())
//          {
//              legRepBV = (XhbHearingLegRepBasicValue)legRepsIter.next();
//              System.out.println("HearingLegRepId: " + legRepBV.getHearingLegRepId());
//              System.out.println("Start Date: " + legRepBV.getStartDate());
//              System.out.println("End Date: " + legRepBV.getEndDate());
//              if (resetTime(legRepBV.getStartDate()).compareTo(startDate) == 0
//                  && resetTime(legRepBV.getEndDate()).compareTo(endDate) == 0)
//              {
//                  found = true;
//                  break;
//              }
//          }
//          assertTrue("A record must exists with start equal to " + startDate +
//                 " and end date equal to " + endDate,found);
//      }
//      catch(HearingScheduleException hse)
//      {
//          hse.printStackTrace();
//          System.err.println("Expected exception thrown:  "+hse);
//          System.err.println("getUserMessage"+hse.getUserMessage());
//          System.err.println("getUserMessage.getKey"+hse.getUserMessageAsMessage().getKey());
//
//          //if valid expected exception thrown the test was successful
//          assertTrue(true);
//      }
//      catch(Exception e)
//      {
//          e.printStackTrace();
//          System.err.println("Exception thrown:  "+e);
//
//          //fail the test if error thrown
//          assertTrue(false);
//      }
//      log.debug("testAddLegalRep1 finished");
//  }
//
//  /**
//   * Run the test on inserted record with following dates:
//   * StartDate - 02/05/2003
//   * EndDate -  05/05/2003
//   * Legal Rep added on : 03/05/2003
//   *
//   * Expected Result: The existing record is left unchanged as the legal rep added date
//   * is between the Start and End dates
//   */
//  public void testAddLegalRep4() throws Exception
//  {
//      log.debug("*** Run testAddLegalRep1");
//      System.out.println("*** Run testAddLegalRep1");
//
//      //Initialise data before this test
//      Statement stmt = connection.createStatement();
//      stmt.executeUpdate(INSERT_HEARING_LEG_REP_ADD4);
//
//      // Setting month May
//      Calendar startCal = new GregorianCalendar(2003,04,02);
//      java.util.Date startDate = startCal.getTime();
//
//      // Setting month May
//      Calendar endCal = new GregorianCalendar(2003,04,05);
//      java.util.Date endDate = endCal.getTime();
//
//      try
//      {
//          //create SHLegRepBasicValue to pass to HearingHeaderWorkFlow.addLegalRep
//          SHLegRepBasicValue shLegRepBasicValue = new SHLegRepBasicValue();
//          shLegRepBasicValue.setScheduledHearingID(SCHEDULED_HEARING_ID);
//          shLegRepBasicValue.setCcInfoID(null);
//          shLegRepBasicValue.setCrestSequenceNo(null);
//          shLegRepBasicValue.setIsSignIn( "Y" );
//          shLegRepBasicValue.setRefDefenceCategoryID(null);
//          shLegRepBasicValue.setRefLegalRepID(REF_LEG_TYPE_ID);
//          shLegRepBasicValue.setSolFirmOrRefLegalRep( "L" );
//
//          //call addLegalRep method
//          HearingHeaderWorkFlow hearingheaderworkflow = new HearingHeaderWorkFlow();
//          hearingheaderworkflow.addLegalRep(shLegRepBasicValue);
//
//          //search for BVs for Hearing Id and Legal rep Id
//          XhbHearingLegRepBasicValue[] legRepsBVs =  XhbHearingLegRepBeanHelper.findByHearingAndRefLegRepValue(HEARING_ID, REF_LEG_REP_ID);
//
//          assertNotNull("XhbHearingLegRepBeanHelper.findByHearingAndRefLegRepValue"
//                            + " should return at least one value", legRepsBVs);
//
//          //sort values return in descending startDate order
//          List legRepList = Arrays.asList(legRepsBVs);
//          Collections.sort(legRepList, EVENT_DATE_ORDER);
//
//          Iterator legRepsIter = legRepList.iterator();
//          XhbHearingLegRepBasicValue legRepBV;
//
//          // There will be more than one record for this hearing/legrep pair
//          boolean found = false;
//          while (legRepsIter.hasNext())
//          {
//              legRepBV = (XhbHearingLegRepBasicValue)legRepsIter.next();
//              System.out.println("HearingLegRepId: " + legRepBV.getHearingLegRepId());
//              System.out.println("Start Date: " + legRepBV.getStartDate());
//              System.out.println("End Date: " + legRepBV.getEndDate());
//              if (resetTime(legRepBV.getStartDate()).compareTo(startDate) == 0
//                  && resetTime(legRepBV.getEndDate()).compareTo(endDate) == 0)
//              {
//                  found = true;
//                  break;
//              }
//          }
//          assertTrue("A record must exists with start equal to " + startDate +
//                 " and end date equal to " + endDate,found);
//      }
//      catch(HearingScheduleException hse)
//      {
//          hse.printStackTrace();
//          System.err.println("Expected exception thrown:  "+hse);
//          System.err.println("getUserMessage"+hse.getUserMessage());
//          System.err.println("getUserMessage.getKey"+hse.getUserMessageAsMessage().getKey());
//
//          //if valid expected exception thrown the test was successful
//          assertTrue(true);
//      }
//      catch(Exception e)
//      {
//          e.printStackTrace();
//          System.err.println("Exception thrown:  "+e);
//
//          //fail the test if error thrown
//          assertTrue(false);
//      }
//      log.debug("testAddLegalRep1 finished");
//  }
//
//
//  /**
//   * Run the test on inserted record with following dates:
//   * StartDate - 03/05/2003
//   * EndDate -  03/05/2003
//   * Legal Rep added on : 03/05/2003
//   *
//   * Expected Result: The existing record is left unchanged as the legal rep
//   * stilll represents a defendant on a scheduled hearing
//   */
//  public void testRemoveLegalRepNotRemoved() throws Exception
//  {
//      log.debug("*** Run testRemoveLegalRepNotRemoved");
//      System.out.println("*** Run testRemoveLegalRepNotRemoved");
//
//      // Insert and find 1st SH_LEG_REP record
//      Statement stmt = connection.createStatement();
//      stmt.executeUpdate(INSERT_SH_LEG_REP_REMOVE1);
//
//      ResultSet rs = stmt.executeQuery(SELECT_SH_LEG_REP_REMOVE);
//      if(rs.next())
//      {
//          // set variable with primary key so can be found in tests
//          SH_LEG_REP_ID1 = new Integer(rs.getInt(1));
//      }
//
//      // Insert and find 2nd SH_LEG_REP record
//      stmt = connection.createStatement();
//      stmt.executeUpdate(INSERT_SH_LEG_REP_REMOVE1);
//
//      rs = stmt.executeQuery(SELECT_SH_LEG_REP_REMOVE);
//      if(rs.next())
//      {
//          // set variable with primary key so can be found in tests
//          SH_LEG_REP_ID2 = new Integer(rs.getInt(1));
//      }
//
//      //Initialise data before this test
//      stmt = connection.createStatement();
//      stmt.executeUpdate(INSERT_HEARING_LEG_REP_REMOVE1a);
//
//      //Scheduled Hearing Not Before Date
//      // Setting month May
//      Calendar notBeforeCal = new GregorianCalendar(2003,04,03);
//      java.util.Date notBeforeDate = notBeforeCal.getTime();
//
//      try
//      {
//          //Find existing ShLegRep
//          ShLegRepMaintainer  shLegRepMaintainer = new ShLegRepMaintainer();;
//          ShLegRep shLegRep = shLegRepMaintainer.findByPrimaryKey(SH_LEG_REP_ID1);
//          SHLegRepBasicValue shLegRepBasicValue = shLegRepMaintainer.getShLegRepBasicValue(shLegRep);
//
//          Vector shLegRepVec = new Vector();
//          shLegRepVec.add(shLegRepBasicValue);
//
//          //call removeLegalRep method
//          HearingHeaderWorkFlow hearingheaderworkflow = new HearingHeaderWorkFlow();
//          hearingheaderworkflow.removeLegalReps(shLegRepVec);
//
//          //search for BVs for Hearing Id and Legal rep Id
//          XhbHearingLegRepBasicValue[] legRepsBVs =  XhbHearingLegRepBeanHelper.findByHearingAndRefLegRepValue(HEARING_ID, REF_LEG_REP_ID);
//
//          System.out.println("legRepsBVs size: "+ legRepsBVs.length);
//
//          if(legRepsBVs.length == 0)
//          {
//              System.out.println("No entries after delete");
//              assertTrue("Record must exists with start and end date equal to notBefore "
//                     + "Date as another schdedule hearing exists with the same Legal Rep.",false);
//          }
//          else
//          {
//              System.out.println("Entries for Hearing_Id and Ref_Leg_Rep_Id exist");
//              //sort values return in descending startDate order
//              List legRepList = Arrays.asList(legRepsBVs);
//              Collections.sort(legRepList, EVENT_DATE_ORDER);
//
//              Iterator legRepsIter = legRepList.iterator();
//              XhbHearingLegRepBasicValue legRepBV;
//
//              boolean foundEntry = false;
//              // There will only be one for this test
//              while (legRepsIter.hasNext())
//              {
//                  legRepBV = (XhbHearingLegRepBasicValue)legRepsIter.next();
//                  if (resetTime(legRepBV.getStartDate()).compareTo(notBeforeDate) == 0
//                      && resetTime(legRepBV.getEndDate()).compareTo(notBeforeDate) == 0)
//                  {
//                      foundEntry = true;
//                      break;
//                  }
//              }
//              assertTrue("Record must exists with start and end date equal to notBefore "
//                     + "Date as another schdedule hearing exists with the same Legal Rep.",foundEntry);
//          }
//      }
//      catch(HearingScheduleException hse)
//      {
//          hse.printStackTrace();
//          System.err.println("Expected exception thrown:  "+hse);
//          System.err.println("getUserMessage"+hse.getUserMessage());
//          System.err.println("getUserMessage.getKey"+hse.getUserMessageAsMessage().getKey());
//
//          //if valid expected exception thrown the test was successful
//          assertTrue(true);
//      }
//      catch(Exception e)
//      {
//          e.printStackTrace();
//          System.err.println("Exception thrown:  "+e);
//
//          //fail the test if error thrown
//          assertTrue(false);
//      }
//      log.debug("testAddLegalRep1 finished");
//  }
//
//  /**
//  * Run the test on inserted record with following dates:
//  * StartDate - 03/05/2003
//  * EndDate -  03/05/2003
//  * Legal Rep to be removed on : 03/05/2003
//  *
//  * Expected Result: The existing record is removed.
//   */
//  public void testRemoveLegalRep1() throws Exception
//  {
//      log.debug("*** Run testRemoveLegalRep1");
//      System.out.println("*** Run testRemoveLegalRep1");
//
//      // Insert and find SH_LEG_REP record
//      Statement stmt = connection.createStatement();
//      stmt.executeUpdate(INSERT_SH_LEG_REP_REMOVE1);
//      ResultSet rs = stmt.executeQuery(SELECT_SH_LEG_REP_REMOVE);
//      if(rs.next())
//      {
//          // set variable with primary key so can be found in tests
//          SH_LEG_REP_ID1 = new Integer(rs.getInt(1));
//      }
//
//      //Initialise data before this test
//      stmt = connection.createStatement();
//      stmt.executeUpdate(INSERT_HEARING_LEG_REP_REMOVE1a);
//      stmt = connection.createStatement();
//      stmt.executeUpdate(INSERT_HEARING_LEG_REP_REMOVE1b);
//      stmt = connection.createStatement();
//      stmt.executeUpdate(INSERT_HEARING_LEG_REP_REMOVE1c);
//
//      //Scheduled Hearing Not Before Date
//      // Setting month May
//      Calendar notBeforeCal = new GregorianCalendar(2003,04,03);
//      java.util.Date notBeforeDate = notBeforeCal.getTime();
//
//      try
//      {
//          //Find existing ShLegRep
//          ShLegRepMaintainer  shLegRepMaintainer = new ShLegRepMaintainer();;
//          ShLegRep shLegRep = shLegRepMaintainer.findByPrimaryKey(SH_LEG_REP_ID1);
//          SHLegRepBasicValue shLegRepBasicValue = shLegRepMaintainer.getShLegRepBasicValue(shLegRep);
//
//          Vector shLegRepVec = new Vector();
//          shLegRepVec.add(shLegRepBasicValue);
//
//          //call removeLegalRep method
//          HearingHeaderWorkFlow hearingheaderworkflow = new HearingHeaderWorkFlow();
//          hearingheaderworkflow.removeLegalReps(shLegRepVec);
//
//          //search for BVs for Hearing Id and Legal rep Id
//          XhbHearingLegRepBasicValue[] legRepsBVs =  XhbHearingLegRepBeanHelper.findByHearingAndRefLegRepValue(HEARING_ID, REF_LEG_REP_ID);
//
//          System.out.println("legRepsBVs size: "+ legRepsBVs.length);
//
//          if(legRepsBVs.length == 0)
//          {
//              System.out.println("No entries after delete");
//              assertTrue(true);
//          }
//          else
//          {
//              System.out.println("Entries for Hearing_Id and Ref_Leg_Rep_Id exist");
//              //sort values return in descending startDate order
//              List legRepList = Arrays.asList(legRepsBVs);
//              Collections.sort(legRepList, EVENT_DATE_ORDER);
//
//              Iterator legRepsIter = legRepList.iterator();
//              XhbHearingLegRepBasicValue legRepBV;
//
//              boolean notFound = true;
//              // There will only be one for this test
//              while (legRepsIter.hasNext())
//              {
//                  legRepBV = (XhbHearingLegRepBasicValue)legRepsIter.next();
//                  if (resetTime(legRepBV.getStartDate()).compareTo(notBeforeDate) == 0
//                      && resetTime(legRepBV.getEndDate()).compareTo(notBeforeDate) == 0)
//                  {
//                      notFound = false;
//                      break;
//                  }
//              }
//              assertTrue("Record exists with start and end date equal to notBefore Date",notFound);
//          }
//      }
//      catch(HearingScheduleException hse)
//      {
//          hse.printStackTrace();
//          System.err.println("Expected exception thrown:  "+hse);
//          System.err.println("getUserMessage"+hse.getUserMessage());
//          System.err.println("getUserMessage.getKey"+hse.getUserMessageAsMessage().getKey());
//
//          //if valid expected exception thrown the test was successful
//          assertTrue(true);
//      }
//      catch(Exception e)
//      {
//          e.printStackTrace();
//          System.err.println("Exception thrown:  "+e);
//
//          //fail the test if error thrown
//          assertTrue(false);
//      }
//      log.debug("testAddLegalRep1 finished");
//  }
//
//  /**
//  * Run the test on inserted record with following dates:
//  * StartDate - 03/05/2003
//  * EndDate -  04/05/2003
//  * Legal Rep to be removed on : 03/05/2003
//  *
//  * Expected Result: The existing record StartDate is changed to 04/05/2003.
//   */
//  public void testRemoveLegalRep2() throws Exception
//    {
//        log.debug("*** Run testRemoveLegalRep2");
//        System.out.println("*** Run testRemoveLegalRep2");
//
//        //Initialise data before this test
//        // Insert and find SH_LEG_REP record
//        Statement stmt = connection.createStatement();
//        stmt.executeUpdate(INSERT_SH_LEG_REP_REMOVE1);
//        ResultSet rs = stmt.executeQuery(SELECT_SH_LEG_REP_REMOVE);
//        if(rs.next())
//        {
//            // set variable with primary key so can be found in tests
//            SH_LEG_REP_ID1 = new Integer(rs.getInt(1));
//        }
//
//        stmt = connection.createStatement();
//        stmt.executeUpdate(INSERT_HEARING_LEG_REP_REMOVE2a);
//        stmt = connection.createStatement();
//        stmt.executeUpdate(INSERT_HEARING_LEG_REP_REMOVE2b);
//        stmt = connection.createStatement();
//        stmt.executeUpdate(INSERT_HEARING_LEG_REP_REMOVE2c);
//
//        // Setting month May
//        Calendar startCal = new GregorianCalendar(2003,04,04);
//        java.util.Date startDate = startCal.getTime();
//
//        // Setting month May
//        Calendar endCal = new GregorianCalendar(2003,04,04);
//        java.util.Date endDate = endCal.getTime();
//
//        try
//        {
//            //Find existing ShLegRep
//            ShLegRepMaintainer  shLegRepMaintainer = new ShLegRepMaintainer();;
//            ShLegRep shLegRep = shLegRepMaintainer.findByPrimaryKey(SH_LEG_REP_ID1);
//            SHLegRepBasicValue shLegRepBasicValue = shLegRepMaintainer.getShLegRepBasicValue(shLegRep);
//
//            Vector shLegRepVec = new Vector();
//            shLegRepVec.add(shLegRepBasicValue);
//
//            //call removeLegalRep method
//            HearingHeaderWorkFlow hearingheaderworkflow = new HearingHeaderWorkFlow();
//            hearingheaderworkflow.removeLegalReps(shLegRepVec);
//
//            //search for BVs for Hearing Id and Legal rep Id
//            XhbHearingLegRepBasicValue[] legRepsBVs =  XhbHearingLegRepBeanHelper.findByHearingAndRefLegRepValue(HEARING_ID, REF_LEG_REP_ID);
//
//            System.out.println("legRepsBVs size: "+ legRepsBVs.length);
//
//
//            if (legRepsBVs.length == 0)
//            {
//                assertTrue("The hearing leg rep record must not be removed but the start date needs to change to a day after the ScheduledHaering.notBeforeTime Date", false);
//            }
//            else
//            {
//                //sort values return in descending startDate order
//                List legRepList = Arrays.asList(legRepsBVs);
//                Collections.sort(legRepList, EVENT_DATE_ORDER);
//
//                Iterator legRepsIter = legRepList.iterator();
//                XhbHearingLegRepBasicValue legRepBV;
//
//                boolean recordFound = false;
//
//                // There will only be one for this test
//                while (legRepsIter.hasNext())
//                {
//                    legRepBV = (XhbHearingLegRepBasicValue)legRepsIter.next();
//
//                    if (resetTime(legRepBV.getStartDate()).compareTo(startDate) == 0 &&
//                        resetTime(legRepBV.getEndDate()).compareTo(endDate) == 0)
//                    {
//                        recordFound = true;
//                    }
//                }
//
//                assertTrue("Record not found: Record must exist with a Start Date " +
//                       "equal to a day after the notBefore day and End Date unchanged", recordFound);
//            }
//        }
//        catch(HearingScheduleException hse)
//        {
//            hse.printStackTrace();
//            System.err.println("Expected exception thrown:  "+hse);
//            System.err.println("getUserMessage"+hse.getUserMessage());
//            System.err.println("getUserMessage.getKey"+hse.getUserMessageAsMessage().getKey());
//
//            //if valid expected exception thrown the test was successful
//            assertTrue(true);
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//            System.err.println("Exception thrown:  "+e);
//
//            //fail the test if error thrown
//            assertTrue(false);
//        }
//        log.debug("testRemoveLegalRep2 finished");
//    }
//
//    /**
//     * Run the test on inserted record with following dates:
//     * StartDate - 01/05/2003
//     * EndDate -  03/05/2003
//     * Legal Rep to be removed on : 03/05/2003
//     *
//     * Result: The existing record EndDate is changed to 02/05/2003.
//     */
//    public void testRemoveLegalRep3() throws Exception
//      {
//          log.debug("*** Run testRemoveLegalRep3");
//          System.out.println("*** Run testRemoveLegalRep3");
//
//          //Initialise data before this test
//
//          Statement stmt = connection.createStatement();
//          stmt.executeUpdate(INSERT_SH_LEG_REP_REMOVE1);
//          ResultSet rs = stmt.executeQuery(SELECT_SH_LEG_REP_REMOVE);
//          if(rs.next())
//          {
//              // set variable with primary key so can be found in tests
//              SH_LEG_REP_ID1 = new Integer(rs.getInt(1));
//          }
//
//          stmt = connection.createStatement();
//          stmt.executeUpdate(INSERT_HEARING_LEG_REP_REMOVE3a);
//          stmt = connection.createStatement();
//          stmt.executeUpdate(INSERT_HEARING_LEG_REP_REMOVE3b);
//          stmt = connection.createStatement();
//          stmt.executeUpdate(INSERT_HEARING_LEG_REP_REMOVE3c);
//
//
//          // Setting month May
//          Calendar startCal = new GregorianCalendar(2003,04,01);
//          java.util.Date startDate = startCal.getTime();
//
//          // Setting month May
//          Calendar endCal = new GregorianCalendar(2003,04,02);
//          java.util.Date endDate = endCal.getTime();
//
//          try
//          {
//              //Find existing ShLegRep
//              ShLegRepMaintainer  shLegRepMaintainer = new ShLegRepMaintainer();
//              ShLegRep shLegRep = shLegRepMaintainer.findByPrimaryKey(SH_LEG_REP_ID1);
//              SHLegRepBasicValue shLegRepBasicValue = shLegRepMaintainer.getShLegRepBasicValue(shLegRep);
//
//              Vector shLegRepVec = new Vector();
//              shLegRepVec.add(shLegRepBasicValue);
//
//              //call removeLegalRep method
//              HearingHeaderWorkFlow hearingheaderworkflow = new HearingHeaderWorkFlow();
//              hearingheaderworkflow.removeLegalReps(shLegRepVec);
//
//              //search for BVs for Hearing Id and Legal rep Id
//              XhbHearingLegRepBasicValue[] legRepsBVs = XhbHearingLegRepBeanHelper.findByHearingAndRefLegRepValue(HEARING_ID, REF_LEG_REP_ID);
//
//              System.out.println("legRepsBVs size: "+ legRepsBVs.length);
//
//              if (legRepsBVs.length == 0)
//              {
//                  assertTrue("The hearing leg rep record must not be removed but the start date changed to a day after the ScheduledHaering.notBeforeTime Date", false);
//              }
//              else
//              {
//                  //sort values return in descending startDate order
//                  List legRepList = Arrays.asList(legRepsBVs);
//                  Collections.sort(legRepList, EVENT_DATE_ORDER);
//
//                  Iterator legRepsIter = legRepList.iterator();
//                  XhbHearingLegRepBasicValue legRepBV;
//
//                  java.util.Date loopStartDate;
//                  java.util.Date loopEndDate;
//                  boolean recordFound = false;
//                  while (legRepsIter.hasNext())
//                  {
//                      legRepBV = (XhbHearingLegRepBasicValue)legRepsIter.next();
//                      loopStartDate = resetTime(legRepBV.getStartDate());
//                      loopEndDate = resetTime(legRepBV.getEndDate());
//                      System.out.println("HearingLegRepId: " + legRepBV.getHearingLegRepId());
//                      System.out.println("Start Date: " + loopStartDate);
//                      System.out.println("End Date: " + loopEndDate);
//                      if (loopStartDate.compareTo(startDate) == 0 && loopEndDate.compareTo(endDate) == 0)
//                      {
//                          recordFound = true;
//                      }
//                  }
//                  assertTrue("Record not found: Record must exist with an End Date " +
//                       "equal to a day before the notBefore day and Start Date unchanged", recordFound);
//              }
//          }
//          catch(HearingScheduleException hse)
//          {
//              hse.printStackTrace();
//              System.err.println("Expected exception thrown:  "+hse);
//              System.err.println("getUserMessage"+hse.getUserMessage());
//              System.err.println("getUserMessage.getKey"+hse.getUserMessageAsMessage().getKey());
//
//              //if valid expected exception thrown the test was successful
//              assertTrue(true);
//          }
//          catch(Exception e)
//          {
//              e.printStackTrace();
//              System.err.println("Exception thrown:  "+e);
//
//              //fail the test if error thrown
//              assertTrue(false);
//          }
//          log.debug("testRemoveLegalRep3 finished");
//      }
//
//
//      /**
//       * Run the test on inserted record with following dates:
//       * StartDate - 29/04/2003
//       * EndDate -  05/05/2003
//       * Legal Rep to be removed on : 03/05/2003
//       *
//       * Result: The existing record EndDate is changed to 02/05/2003.
//       * A New record is created with StartDate- 04/05/2003 and EndDate- 05/05/2003
//     */
//      public void testRemoveLegalRep4() throws Exception
//      {
//          log.debug("*** Run testRemoveLegalRep4");
//          System.out.println("*** Run testRemoveLegalRep4");
//
//          //Initialise data before this test
//          // Insert and find SH_LEG_REP record
//          Statement stmt = connection.createStatement();
//          stmt.executeUpdate(INSERT_SH_LEG_REP_REMOVE1);
//          ResultSet rs = stmt.executeQuery(SELECT_SH_LEG_REP_REMOVE);
//          if(rs.next())
//          {
//              // set variable with primary key so can be found in tests
//              SH_LEG_REP_ID1 = new Integer(rs.getInt(1));
//          }
//
//          stmt = connection.createStatement();
//          stmt.executeUpdate(INSERT_HEARING_LEG_REP_REMOVE4a);
//          stmt = connection.createStatement();
//          stmt.executeUpdate(INSERT_HEARING_LEG_REP_REMOVE4b);
//          stmt = connection.createStatement();
//          stmt.executeUpdate(INSERT_HEARING_LEG_REP_REMOVE4c);
//
//          // Setting month April
//          Calendar startCal1 = new GregorianCalendar(2003,03,29);
//          java.util.Date startDate1 = startCal1.getTime();
//
//          // Setting month May
//          Calendar endCal1 = new GregorianCalendar(2003,04,02);
//          java.util.Date endDate1 = endCal1.getTime();
//
//
//          // Setting month April
//          Calendar startCal2 = new GregorianCalendar(2003,04,04);
//          java.util.Date startDate2 = startCal2.getTime();
//
//          // Setting month May
//          Calendar endCal2 = new GregorianCalendar(2003,04,05);
//          java.util.Date endDate2 = endCal2.getTime();
//
//          try
//          {
//              //Find existing ShLegRep
//              ShLegRepMaintainer  shLegRepMaintainer = new ShLegRepMaintainer();;
//              ShLegRep shLegRep = shLegRepMaintainer.findByPrimaryKey(SH_LEG_REP_ID1);
//              SHLegRepBasicValue shLegRepBasicValue = shLegRepMaintainer.getShLegRepBasicValue(shLegRep);
//
//              System.out.println("shLegRepBasicValue ID: "+ shLegRepBasicValue.getId());
//              System.out.println("shLegRepBasicValue version: "+ shLegRepBasicValue.getVersion());
//              System.out.println("shLegRepBasicValue SH ID: "+ shLegRepBasicValue.getScheduledHearingID());
//
//              Vector shLegRepVec = new Vector();
//              shLegRepVec.add(shLegRepBasicValue);
//
//              //call removeLegalRep method
//              HearingHeaderWorkFlow hearingheaderworkflow = new HearingHeaderWorkFlow();
//              hearingheaderworkflow.removeLegalReps(shLegRepVec);
//
//              //search for BVs for Hearing Id and Legal rep Id
//              XhbHearingLegRepBasicValue[] legRepsBVs = XhbHearingLegRepBeanHelper.findByHearingAndRefLegRepValue(HEARING_ID, REF_LEG_REP_ID);
//
//              System.out.println("legRepsBVs size: "+ legRepsBVs.length);
//
//              if (legRepsBVs.length == 0)
//              {
//                  assertTrue("The hearing leg rep record must not be removed but the " +
//                              "start date changed to a day after the ScheduledHearing.notBeforeTime Date", false);
//              }
//              else
//              {
//                  //sort values return in descending startDate order
//                  List legRepList = Arrays.asList(legRepsBVs);
//                  Collections.sort(legRepList, EVENT_DATE_ORDER);
//
//                  Iterator legRepsIter = legRepList.iterator();
//                  XhbHearingLegRepBasicValue legRepBV;
//
//                  java.util.Date loopStartDate;
//                  java.util.Date loopEndDate;
//
//                  boolean updatedFound = false;
//                  boolean createdFound = false;
//
//                  while (legRepsIter.hasNext())
//                  {
//                      legRepBV = (XhbHearingLegRepBasicValue)legRepsIter.next();
//
//                      loopStartDate = resetTime(legRepBV.getStartDate());
//                      loopEndDate = resetTime(legRepBV.getEndDate());
//
//                      System.out.println("HearingLegRepId: " + legRepBV.getHearingLegRepId());
//                      System.out.println("Start Date: " + loopStartDate);
//                      System.out.println("End Date: " + loopEndDate);
//
//                      if(loopStartDate.compareTo(startDate1) == 0 && loopEndDate.compareTo(endDate1) == 0)
//                      {
//                          updatedFound = true;
//                      }
//                      else if(loopStartDate.compareTo(startDate2) == 0 && loopEndDate.compareTo(endDate2) == 0)
//                      {
//                          createdFound = true;
//                      }
//                  }
//                  assertTrue("Missing/Incorrect updated record: An updated record with the original Start Date and an End Date before the notBefore Date must exist",updatedFound);
//                  assertTrue("Missing/Incorrect created record: A newly created record with the Start Date set to a day after the notBefore Date and the original End Date of the previously updated record must exist",createdFound);
//              }
//          }
//          catch(HearingScheduleException hse)
//          {
//              hse.printStackTrace();
//              System.err.println("Expected exception thrown:  "+hse);
//              System.err.println("getUserMessage"+hse.getUserMessage());
//              System.err.println("getUserMessage.getKey"+hse.getUserMessageAsMessage().getKey());
//
//              //if valid expected exception thrown the test was successful
//              assertTrue(true);
//          }
//          catch(Exception e)
//          {
//              e.printStackTrace();
//              System.err.println("Exception thrown:  "+e);
//
//              //fail the test if error thrown
//              assertTrue(false);
//          }
//          log.debug("testRemoveLegalRep4 finished");
//      }
//
//
//      private java.util.Date resetTime(java.util.Date inDate)
//      {
//          Calendar cal = Calendar.getInstance();
//          cal.setTime(inDate);
//          return resetTime(cal);
//      }
//
//      private java.util.Date resetTime(Calendar inCal)
//      {
//          inCal.set(Calendar.HOUR_OF_DAY, 0);
//          inCal.set(Calendar.MINUTE, 0);
//          inCal.set(Calendar.SECOND, 0);
//          inCal.set(Calendar.MILLISECOND, 0);
//          return inCal.getTime();
//      }
//
//      static final Comparator EVENT_DATE_ORDER = new Comparator()
//      {
//          public int compare(Object o1, Object o2)
//          {
//              XhbHearingLegRepBasicValue entry1 = (XhbHearingLegRepBasicValue) o1;
//              XhbHearingLegRepBasicValue entry2 = (XhbHearingLegRepBasicValue) o2;
//
//              // Timestamp implements Comparable so fine to do this, we want
//              // the results in reverse chronological order so the latest event
//              // is first
//              return entry2.getStartDate().compareTo(entry1.getStartDate());
//          }
//      };
//  }