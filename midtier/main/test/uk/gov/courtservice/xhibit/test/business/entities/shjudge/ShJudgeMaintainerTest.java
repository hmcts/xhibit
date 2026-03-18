//
//package uk.gov.courtservice.xhibit.test.business.entities.shjudge;
//
//// Jdk
//
//// J2ee
////import javax.naming.*;
////import javax.ejb.*;
//import javax.ejb.ObjectNotFoundException;
//import javax.naming.InitialContext;
//import javax.transaction.UserTransaction;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.shjudge.ShJudge;
//import uk.gov.courtservice.xhibit.business.entities.shjudge.ShJudgeMaintainer;
//import uk.gov.courtservice.xhibit.business.vos.entities.SHJudgeBasicValue;
//
//
///**
// * <p>Title: ShJudgeMaintainerTest</p>
// * <p>Description: ShJudgeMaintainerTest Test Class</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Paul Fitton
// * @version $Id: ShJudgeMaintainerTest.java,v 1.8 2006/07/11 14:16:57 xzfdtb Exp $
// *
// * <Change History/>
// *
// * <P>17/02/03 - PDF  - Created</P>
// */
//
//public class ShJudgeMaintainerTest extends TestCase {
//
//    private static Logger log = CSServices.getLogger(ShJudgeMaintainerTest.class);
//
//    private static String SQL_DELETE_ALL_ENTRIES = "delete from xhb_sh_judge";
//
//    private static int ACTION_CREATE_ID = 1;
//    private static int ACTION_UPDATE_ID = 2;
//    private static int ACTION_DELETE_ID = 3;
//    private static int ACTION_FIND_BY_PRI_KEY_ID = 4;
//    private static int ACTION_GET_BASIC_VO_ID = 5;
//    private static int ACTION_GET_COMPLEX_VO_ID = 6;
//
//    public  static String addrSQL= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1)";
//
//    public  static String courtSQL= "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID, CREST_IP_ADDRESS) VALUES ( \n"
//    +"1, 'court_type', 'circuit', 'name', 'cid', 'cpfix', 'short', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'fez', 'fez', 1, 1, 'addr')";
//
//    public static String sittingSQL = "INSERT INTO XHB_SITTING (SITTING_ID, SITTING_TIME, IS_FLOATING, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'y', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//
//
//    public static String refCourtSQL = "INSERT INTO XHB_REF_COURT (REF_COURT_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID, COURT_ID) VALUES ( 1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1, 1)";
//    public static String caseSQL = "INSERT INTO XHB_CASE (CASE_ID, REF_COURT_ID, COURT_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (1, 1, 1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//
//    public static String refHearingSQL = "INSERT INTO XHB_REF_HEARING_TYPE (REF_HEARING_TYPE_ID, HEARING_TYPE_CODE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, COURT_ID) VALUES (1, 'co', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1)";
//    public static String hearingSQL = "INSERT INTO XHB_HEARING (HEARING_ID, CASE_ID, REF_HEARING_TYPE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, COURT_ID, MP_HEARING_TYPE) VALUES (1, 1, 1,  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1, 1)";
//    public  static String linkSHSQL= "INSERT INTO XHB_LINKED_SH ( LINKED_SH_ID, VERSION, LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) VALUES ( 1, 1, 'pete', 'pete', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//
//    public static String schedHearingSQL = "INSERT INTO XHB_SCHEDULED_HEARING (SCHEDULED_HEARING_ID, SEQUENCE_NO, SITTING_ID, HEARING_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, LINKED_SH_ID) VALUES (1, 1, 1, 1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1)";
//
//    public static String schedAttendeeSQL = "INSERT INTO XHB_SCHED_HEARING_ATTENDEE (SH_ATTENDEE_ID, ATTENDEE_TYPE, SCHEDULED_HEARING_ID, VERSION, LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) VALUES (1, 'j', 1, 1, 'pete', 'pete', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//
//    public static String refjudgeSQL = "INSERT INTO XHB_REF_JUDGE (REF_JUDGE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//
//
//    private ShJudgeMaintainer shJudgeMaintainer = new ShJudgeMaintainer();
//    private InitialContext initContext;
//    private UserTransaction ut = null;
//
////    private Integer setUpPK;
//    private Integer setUpVersion;
//
//  public ShJudgeMaintainerTest(String s) {
//    super(s);
//  }
//
//  protected void setUp() {
//      try
//      {
//	  log.debug("setUp()");
//	log.debug("deleting data");
//	TestUtils.execSql("Delete from XHB_SH_JUDGE");
//	TestUtils.execSql("Delete from XHB_REF_JUDGE");
//	TestUtils.execSql("Delete from XHB_SCHED_HEARING_ATTENDEE");
//	TestUtils.execSql("Delete from XHB_SCHEDULED_HEARING");
//	TestUtils.execSql("Delete from XHB_LINKED_SH");
//	TestUtils.execSql("Delete from XHB_HEARING");
//	TestUtils.execSql("Delete from XHB_REF_HEARING_TYPE");
//	TestUtils.execSql("Delete from XHB_SITTING");
//	TestUtils.execSql("Delete from XHB_CASE");
//	TestUtils.execSql("Delete from XHB_REF_COURT");
//	TestUtils.execSql("Delete from XHB_COURT");
//	TestUtils.execSql("Delete from XHB_ADDRESS");
//
//	log.debug("Insert attendee record");
//	TestUtils.execSql(addrSQL);
//	TestUtils.execSql(courtSQL);
//	TestUtils.execSql(refCourtSQL);
//	TestUtils.execSql(caseSQL);
//	TestUtils.execSql(sittingSQL);
//	TestUtils.execSql(linkSHSQL);
//	TestUtils.execSql(refHearingSQL);
//	TestUtils.execSql(hearingSQL);
//	TestUtils.execSql(schedHearingSQL);
//	TestUtils.execSql(schedAttendeeSQL);
//	TestUtils.execSql(refjudgeSQL);
//
//
//          initContext = new InitialContext();
//          ut = (UserTransaction)initContext.lookup("java:comp/UserTransaction");
//      }
//      catch (Exception e)
//      {
//          log.debug ("setUp Error: " + e.toString( ));
//      }
//  }
//
//  protected void tearDown() {
//      try
//      {
//	  log.debug("deleting data");
//	TestUtils.execSql("Delete from XHB_SH_JUDGE");
//	TestUtils.execSql("Delete from XHB_REF_JUDGE");
//	TestUtils.execSql("Delete from XHB_SCHED_HEARING_ATTENDEE");
//	TestUtils.execSql("Delete from XHB_SCHEDULED_HEARING");
//	TestUtils.execSql("Delete from XHB_LINKED_SH");
//	TestUtils.execSql("Delete from XHB_HEARING");
//	TestUtils.execSql("Delete from XHB_REF_HEARING_TYPE");
//	TestUtils.execSql("Delete from XHB_SITTING");
//	TestUtils.execSql("Delete from XHB_CASE");
//	TestUtils.execSql("Delete from XHB_REF_COURT");
//	TestUtils.execSql("Delete from XHB_COURT");
//	TestUtils.execSql("Delete from XHB_ADDRESS");
//
//      }
//      catch (Exception e)
//      {
//          log.debug ("setUp Error: " + e.toString( ));
//      }
//  }
//
//  public void testCreate() {
//
//      try
//      {
//          log.debug("testCreate called");
//
//          SHJudgeBasicValue shJudgeVO =  createBasicVO( ACTION_CREATE_ID );
//
//          ut.begin();
//
//          ShJudge shJudgeLocal = (ShJudge)shJudgeMaintainer.create( shJudgeVO );
//
//          ut.commit();
//
//          assertEquals( shJudgeLocal.getRefJudgeId(), shJudgeVO.getRefJudgeID() );
//      }
//      catch( Exception e ) {
//
//          fail( "Caught exception :: " + e );
//      }
//
//  }
//
//  public void testUpdate() {
//
//      log.debug("testUpdate called");
//      try
//      {
//	  Integer setUpPK = createShJudgeEntry( ACTION_UPDATE_ID );
//
//	  ShJudgeMaintainer shJudgeMaintainer = new ShJudgeMaintainer();
//	  ShJudge shJudge=  (ShJudge)shJudgeMaintainer.findByPrimaryKey( setUpPK );
//
//	  SHJudgeBasicValue shJudgeVO = shJudgeMaintainer.getShJudgeBasicValue( shJudge );
//	  shJudgeVO.setDeputyHCJ("T");
//	  ut.begin();
//	  shJudgeMaintainer.update( shJudgeVO );
//	  ut.commit();
//	  ShJudge shJudge2Local=  (ShJudge)shJudgeMaintainer.findByPrimaryKey( setUpPK );
//
//	  assertEquals(shJudge2Local.getDeputyHcj(), "T" );
//      }
//      catch(Exception e)
//      {
//	  fail( "Caught exception :: " + e );
//      }
//
//  }
//
//  public void testDelete() {
//
//     log.debug("testDelete called");
//
//     Integer setUpPK = createShJudgeEntry( ACTION_DELETE_ID );
//     log.debug("Pkey = " + setUpPK);
//     try {
//
//          ShJudgeMaintainer ShJudgeMaintainer = new ShJudgeMaintainer();
//          Integer id1=  setUpPK;
//          Integer version2=  new Integer(1);
//	  ut.begin();
//          ShJudgeMaintainer.delete(id1, version2);
//	  ut.commit();
//          assertTrue(true);
//      }
//      catch (Exception e)
//      {
//          fail(e.toString());
//      }
//  }
//
//
//  public void testGetShJudgeBasicValue()
//  {
//    try
//    {
//      log.debug("testGetShJudgeBasicValue called");
//
//      Integer setUpPK = createShJudgeEntry( ACTION_GET_BASIC_VO_ID );
//
//      ShJudgeMaintainer shJudgeMaintainer = new ShJudgeMaintainer();
//      ShJudge value1=  (ShJudge)shJudgeMaintainer.findByPrimaryKey( setUpPK );
//      SHJudgeBasicValue shbv = shJudgeMaintainer.getShJudgeBasicValue(value1);
//
//      /** @todo:  Insert test code here.  Use assertEquals(), for example. */
////      assertEquals(shbv.getCurrentStatus(), value1.getCurrentStatus());
//      assertEquals(shbv.getDeputyHCJ(), value1.getDeputyHcj());
//    }
//    catch (ObjectNotFoundException e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//  }
//
////  public void testGetShJudgeComplexValue() {
////
////      log.debug("testGetShJudgeComplexValue called");
////
////      Integer setUpPK = createShJudgeEntry( ACTION_GET_COMPLEX_VO_ID );
////
////      ShJudgeMaintainer shJudgeMaintainer = new ShJudgeMaintainer();
////      ShJudge value1=  (ShJudge)shJudgeMaintainer.findByPrimaryKey( setUpPK );
////      SHJudgeComplexValue shbv = shJudgeMaintainer.getShJudgeComplexValue(value1);
////      /** @todo:  Insert test code here.  Use assertEquals(), for example. */
////      assertEquals(shbv.getCurrentStatus(), value1.getCurrentStatus());
////      assertEquals(shbv.getShJudgeID(), value1.getShJudgeId());
////  }
//
///*  public void testFindByPrimaryKey() {
//
//    log.debug("testFindByPrimaryKey called");
//
//    Integer setUpPK = createShJudgeEntry( ACTION_FIND_BY_PRI_KEY_ID );
//
//    ShJudgeMaintainer shjudgemaintainer = new ShJudgeMaintainer();
//    Integer id1=  null  /** @todo fill in non-null value */;
////    ShJudge shjudgeRet = shjudgemaintainer.findByPrimaryKey(id1);
//
//  /** @todo:  Insert test code here.  Use assertEquals(), for example. */
//
////  }
//
//
//  // -----------------
//  //  Private Methods
//  // -----------------
//
//
//  private SHJudgeBasicValue createBasicVO( int action )
//  {
//      SHJudgeBasicValue shJudgeBasicVO = new SHJudgeBasicValue();
//
//      String deputyHCJ = String.valueOf( action );
////      Integer hearingID = new Integer( "100" + action );
//      Integer refJudgeID = new Integer(1);
//      Integer shAttendeeID = new Integer(1);
////      Integer shJudgeID = new Integer( "400" + action );
//
//      shJudgeBasicVO.setDeputyHCJ( deputyHCJ );
////      shJudgeBasicVO.setHearingID( hearingID );
//      shJudgeBasicVO.setRefJudgeID( refJudgeID );
//      shJudgeBasicVO.setShAttendeeID( shAttendeeID );
////      shJudgeBasicVO.setShJudgeID( shJudgeID );
//
//      return shJudgeBasicVO;
//  }
//
//  private Integer createShJudgeEntry( int action )
//  {
//      log.debug("createShJudgeEntry called");
//
//      ShJudge shJudgeLocal = null;
//      try {
//          ut.begin();
//
//          shJudgeLocal = (ShJudge)shJudgeMaintainer.create( createBasicVO(action) );
//
//          ut.commit();
//      }
//      catch( Exception e ) {
//
//          fail( "Caught exception :: " + e );
//      }
//
//      Integer newPK = (Integer)shJudgeLocal.getPrimaryKey();
//      log.debug("createShJudgeEntry - created entry with Primary Key :: " + newPK );
//
//      return newPK;
//
//  }
//
//
//  public void testFindByShAttendeeID() {
//    log.debug(">>>>>>>>>>>> testFindByShAttendeeID called");
//    try
//    {
//      ShJudge shJudgeLocal = null;
//      try {
//        ut.begin();
//        log.debug("Will try to create a new shJudge");
//        shJudgeLocal = (ShJudge)shJudgeMaintainer.create( createBasicVO(ACTION_CREATE_ID) );
//
//        ut.commit();
//      }
//      catch( Exception e ) {
//
//        fail( "Caught exception :: " + e );
//      }
//      ShJudgeMaintainer shJudgeMaintainer = new ShJudgeMaintainer();
//      ShJudge shJudge =  (ShJudge)shJudgeMaintainer.findByShAttendeeId(
//          shJudgeLocal.getShAttendeeId());
//
//      SHJudgeBasicValue shJudgeVO = shJudgeMaintainer.getShJudgeBasicValue( shJudge );
//
//      log.debug("shJudgeVO.getRefJudgeID() : " + shJudgeVO.getRefJudgeID());
//      assertEquals(shJudgeVO.getRefJudgeID(), shJudgeLocal.getRefJudgeId());
//
//      log.debug("shJudgeVO.getShAttendeeID() : " + shJudgeVO.getShAttendeeID());
//      assertEquals(shJudgeVO.getShAttendeeID(), shJudgeLocal.getShAttendeeId());
//    }
//    catch(Exception e)
//    {
//      fail( "Caught exception :: " + e );
//    }
//  }
//
//
//}
//