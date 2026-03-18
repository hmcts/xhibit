//package uk.gov.courtservice.xhibit.test.business.entities.schedhearingattendee;
//
//import javax.naming.InitialContext;
//import javax.transaction.UserTransaction;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendee;
//import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendeeMaintainer;
//import uk.gov.courtservice.xhibit.business.vos.entities.SchedHearingAttendeeBasicValue;
//
//
///**
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Faisal Shoukat
// * @version 1.0
// */
//
//public class SchedHearingAttendeeMaintainerTest extends TestCase{
//
//    private static Logger log = CSServices.getLogger(SchedHearingAttendeeMaintainerTest.class);
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
//    public static String crtRepSQL = "INSERT INTO XHB_REF_COURT_REPORTER (REF_COURT_REPORTER_ID) VALUES (1)";
//    public static String judgeSQL = "INSERT INTO XHB_REF_JUDGE (REF_JUDGE_ID) VALUES (1)";
////    public static String schedAttendeeSQL = "INSERT INTO XHB_SCHED_HEARING_ATTENDEE (SH_ATTENDEE_ID, ATTENDEE_TYPE, SCHEDULED_HEARING_ID, VERSION, LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) VALUES (1, 'j', 1, 1, 'pete', 'pete', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//
//   SchedHearingAttendeeMaintainer maintainer;
//   //SchedHearingAttendeeMaintainer attendeeMaintain = new SchedHearingAttendeeMaintainer();
//   Integer version = new Integer(1);
//
//   private InitialContext initContext;
//   private UserTransaction ut = null;
//
//
//   public SchedHearingAttendeeMaintainerTest(String s)
//   {
//       super(s);
//   }
//
//   protected void setUp() throws Exception
//    {
//
//       initContext = new InitialContext();
//       ut = (UserTransaction)initContext.lookup("java:comp/UserTransaction");
//
//
//       log.debug("setUp()");
//       log.debug("deleting data");
//       TestUtils.execSql("Delete from XHB_SCHED_HEARING_ATTENDEE");
//        TestUtils.execSql("Delete from XHB_REF_COURT_REPORTER");
//        TestUtils.execSql("Delete from XHB_REF_JUDGE");
//       TestUtils.execSql("Delete from XHB_SCHEDULED_HEARING");
//       TestUtils.execSql("Delete from XHB_LINKED_SH");
//       TestUtils.execSql("Delete from XHB_HEARING");
//       TestUtils.execSql("Delete from XHB_REF_HEARING_TYPE");
//       TestUtils.execSql("Delete from XHB_SITTING");
//       TestUtils.execSql("Delete from XHB_CASE");
//       TestUtils.execSql("Delete from XHB_REF_COURT");
//       TestUtils.execSql("Delete from XHB_COURT");
//       TestUtils.execSql("Delete from XHB_ADDRESS");
//
//       log.debug("Insert attendee record");
//       TestUtils.execSql(addrSQL);
//       TestUtils.execSql(courtSQL);
//       TestUtils.execSql(refCourtSQL);
//       TestUtils.execSql(caseSQL);
//       TestUtils.execSql(sittingSQL);
//       TestUtils.execSql(linkSHSQL);
//       TestUtils.execSql(refHearingSQL);
//       TestUtils.execSql(hearingSQL);
//       TestUtils.execSql(crtRepSQL);
//       TestUtils.execSql(judgeSQL);
//       TestUtils.execSql(schedHearingSQL);
// //      TestUtils.execSql(schedAttendeeSQL);
//
//
//
//    }
//
//    protected void tearDown()throws Exception
//    {
//	log.debug("tearDown()");
//	log.debug("deleting data");
//	TestUtils.execSql("Delete from XHB_SCHED_HEARING_ATTENDEE");
//	TestUtils.execSql("Delete from XHB_REF_COURT_REPORTER");
//	TestUtils.execSql("Delete from XHB_REF_JUDGE");
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
//  }
//
//    private SchedHearingAttendeeBasicValue createBasicVO()
//    {
//	SchedHearingAttendeeBasicValue shdValue = new SchedHearingAttendeeBasicValue(version);
//	shdValue.setAttendeeType("S");
//	shdValue.setRefCourtReporterID(version);
//	shdValue.setRefJudgeID(version);
//
//	return shdValue;
//
//    }
//
//    public void testCreate()
//    {
//
//      try
//      {
//	  log.debug("testCreate called");
//
//	 maintainer = new SchedHearingAttendeeMaintainer();
//	 SchedHearingAttendeeBasicValue value = createBasicVO();
//	 ut.begin();
//
//	  SchedHearingAttendee shdLocal = (SchedHearingAttendee)maintainer.create( value );
//	  log.debug("version before commit = " + shdLocal.getVersion());
//	 ut.commit();
//	 log.debug("version = " + shdLocal.getVersion());
//	  assertEquals( shdLocal.getAttendeeType(), value.getAttendeeType());
//      }
//      catch( Exception e ) {
//
//	  fail( "Caught exception :: in test create" + e );
//      }
//
//  }
//
//  public void testDelete()
//  {
//      log.debug("testDelete called");
//
//     try
//     {
//	 maintainer = new SchedHearingAttendeeMaintainer();
//	 SchedHearingAttendeeBasicValue value = createBasicVO();
//	 ut.begin();
//	 SchedHearingAttendee shdLocal = (SchedHearingAttendee)maintainer.create( value );
//	 ut.commit();
//	 log.debug("created entity into db Version = "+shdLocal.getVersion());
//	 Integer pKey = shdLocal.getShAttendeeId();
//	 log.debug("pkey = " + pKey);
//	 ut.begin();
//	 maintainer.delete(pKey, version);
//	// maintainer.delete(pKey, new Integer(4));
//
//	 log.debug("deleted entity");
//
//
//	 ut.commit();
//	 assertTrue(true);
//      }
//      catch (Exception e)
//      {
//	  fail(e.toString());
//      }
//  }
//
//  public void testUpdate()
//  {
//      try
//      {
//	  log.debug("testUpdate called");
//	  maintainer = new SchedHearingAttendeeMaintainer();
//	  SchedHearingAttendeeBasicValue value = createBasicVO();
//	  ut.begin();
//	  SchedHearingAttendee shdLocal = (SchedHearingAttendee)maintainer.create( value );
//	  ut.commit();
//	  Integer pKey = shdLocal.getShAttendeeId();
//	  log.debug("Pkey = " + pKey);
//
//	  SchedHearingAttendee shd = maintainer.findByPrimaryKey(pKey);
//	  SchedHearingAttendeeBasicValue shdValue = maintainer.getSchedHearingAttendeeBasicValue(shd);
//	  log.debug("value object");
//	  log.debug(shdValue.toString());
//	  shdValue.setAttendeeType("M");
//	  log.debug("updating database");
//	  ut.begin();
//	  maintainer.update(shdValue);
//	  ut.commit();
//	  SchedHearingAttendee shd2 = (SchedHearingAttendee)maintainer.findByPrimaryKey(pKey);
//	  log.debug("modified = " + shd2.getAttendeeType());
//	  log.debug("value object = "+ shdValue.getAttendeeType());
//
//	  assertEquals(shd2.getAttendeeType(), shdValue.getAttendeeType());
//      }
//      catch( Exception e )
//      {
//	 fail( "Caught exception :: in test update" + e );
//      }
//
//  }
//
//  public void testGetSchedHearingAttendeeBasicValue()
//  {
//      try
//      {
//	  log.debug("testGetShJusticeBasicValue");
//	  maintainer = new SchedHearingAttendeeMaintainer();
//	  SchedHearingAttendeeBasicValue value = createBasicVO();
//	  ut.begin();
//
//	  SchedHearingAttendee shdLocal = (SchedHearingAttendee)maintainer.create( value );
//	  ut.commit();
//
//	  SchedHearingAttendee shdLocal2 = (SchedHearingAttendee)maintainer.findByPrimaryKey(shdLocal.getShAttendeeId());
//
//	  SchedHearingAttendeeBasicValue shdValue = maintainer.getSchedHearingAttendeeBasicValue(shdLocal2);
//	  log.debug("value object");
//	  log.debug(shdValue.toString());
//
//	  assertEquals(shdValue.getAttendeeType(), shdLocal2.getAttendeeType());
//
//      }
//      catch( Exception e )
//      {
//	 fail( "Caught exception :: in vtestGetSchedHearingAttendeeBasicValuee" + e );
//      }
//
//
//  }
//
///*  public void testGetShJusticeComplexValue()
//  {
//      log.debug("testGetShJusticeComplexValue");
//      maintainer = new ShJusticeMaintainer();
//      SHJusticeBasicValue value = createBasicVO();
////          ut.begin();
//
//      ShJustice shJusticeLocal = (ShJustice)maintainer.create( value );
//      ShJustice shJusticeLocal2 = (ShJustice)maintainer.findByPrimaryKey(shJusticeLocal.getShJusticeId());
//
//      SHJusticeComplexValue justiceComplex = maintainer.getShJusticeComplexValue(shJusticeLocal2);
//      log.debug("value object");
//      log.debug(justiceComplex.toString());
//
//      SchedHearingAttendee attendee =(SchedHearingAttendee)justiceComplex.getSchedHearingAttendee();
//
//      //SchedHearingAttendee attendee2 = attendeeMaintain.findByPrimaryKey(attendee.getShAttendeeId());
//
//      //Attendee type in attendee table = j
//
//      assertEquals(attendee.getAttendeeType(), "j");
//
//
//  }*/
//
//
//
//}