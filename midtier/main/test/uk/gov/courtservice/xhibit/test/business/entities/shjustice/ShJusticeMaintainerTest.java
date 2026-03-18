//package uk.gov.courtservice.xhibit.test.business.entities.shjustice;
//
//
//// Jdk
//
//// J2ee
////import javax.naming.*;
////import javax.ejb.*;
//import javax.naming.InitialContext;
//import javax.transaction.UserTransaction;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendeeMaintainer;
//import uk.gov.courtservice.xhibit.business.entities.shjustice.ShJustice;
//import uk.gov.courtservice.xhibit.business.entities.shjustice.ShJusticeMaintainer;
//import uk.gov.courtservice.xhibit.business.vos.entities.SHJusticeBasicValue;
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
//public class ShJusticeMaintainerTest extends TestCase{
//
//    private static Logger log = CSServices.getLogger(ShJusticeMaintainerTest.class);
//
//    public  static String addrSQL= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1)";
//
//   public  static String courtSQL= "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID, CREST_IP_ADDRESS) VALUES ( \n"
//   +"1, 'court_type', 'circuit', 'name', 'cid', 'cpfix', 'short', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'fez', 'fez', 1, 1, 'addr')";
//
//   public static String sittingSQL = "INSERT INTO XHB_SITTING (SITTING_ID, SITTING_TIME, IS_FLOATING, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'y', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//
//
//   public static String refCourtSQL = "INSERT INTO XHB_REF_COURT (REF_COURT_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID, COURT_ID) VALUES ( 1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1, 1)";
//   public static String caseSQL = "INSERT INTO XHB_CASE (CASE_ID, REF_COURT_ID, COURT_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (1, 1, 1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//
//   public static String refHearingSQL = "INSERT INTO XHB_REF_HEARING_TYPE (REF_HEARING_TYPE_ID, HEARING_TYPE_CODE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, COURT_ID) VALUES (1, 'co', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1)";
//   public static String hearingSQL = "INSERT INTO XHB_HEARING (HEARING_ID, CASE_ID, REF_HEARING_TYPE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, COURT_ID, MP_HEARING_TYPE) VALUES (1, 1, 1,  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1, 1)";
//   public  static String linkSHSQL= "INSERT INTO XHB_LINKED_SH ( LINKED_SH_ID, VERSION, LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) VALUES ( 1, 1, 'pete', 'pete', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//
//   public static String schedHearingSQL = "INSERT INTO XHB_SCHEDULED_HEARING (SCHEDULED_HEARING_ID, SEQUENCE_NO, SITTING_ID, HEARING_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, LINKED_SH_ID) VALUES (1, 1, 1, 1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1)";
//
//   public static String schedAttendeeSQL = "INSERT INTO XHB_SCHED_HEARING_ATTENDEE (SH_ATTENDEE_ID, ATTENDEE_TYPE, SCHEDULED_HEARING_ID, VERSION, LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) VALUES (1, 'j', 1, 1, 'pete', 'pete', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//
//   ShJusticeMaintainer maintainer;
//   SchedHearingAttendeeMaintainer attendeeMaintain = new SchedHearingAttendeeMaintainer();
//   Integer version = new Integer(1);
//
//   private InitialContext initContext;
//   private UserTransaction ut = null;
//
//
//   public ShJusticeMaintainerTest(String s)
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
//	log.debug("setUp()");
//	log.debug("deleting data");
//	TestUtils.execSql("Delete from XHB_SH_JUSTICE");
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
//	log.debug("Insert records for constraints");
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
//
//
//
//
//    }
//
//    protected void tearDown()throws Exception
//    {
//	log.debug("tearDown()");
//	log.debug("deleting data");
//	TestUtils.execSql("Delete from XHB_SH_JUSTICE");
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
//    }
//
//    private SHJusticeBasicValue createBasicVO()
//    {
//	SHJusticeBasicValue justiceValue = new SHJusticeBasicValue(version);
//	justiceValue.setHearingID(version);
//	justiceValue.setJusticeName("Faisal");
//
//	return justiceValue;
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
//	 maintainer = new ShJusticeMaintainer();
//	 SHJusticeBasicValue value = createBasicVO();
//	 ut.begin();
//
//	  ShJustice shJusticeLocal = (ShJustice)maintainer.create( value );
//	  log.debug("version before commit = " + shJusticeLocal.getVersion());
//         ut.commit();
//	 log.debug("version = " + shJusticeLocal.getVersion());
//	  assertEquals( shJusticeLocal.getJusticeName(), value.getJusticeName());
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
//	 maintainer = new ShJusticeMaintainer();
//	 SHJusticeBasicValue value = createBasicVO();
//	 ut.begin();
//	 ShJustice shJusticeLocal = (ShJustice)maintainer.create( value );
//	 ut.commit();
//	 log.debug("created entity into db Version = "+shJusticeLocal.getVersion());
//	 Integer pKey = shJusticeLocal.getShJusticeId();
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
//	  maintainer = new ShJusticeMaintainer();
//	  SHJusticeBasicValue value = createBasicVO();
//	  ut.begin();
//	  ShJustice shJusticeLocal = (ShJustice)maintainer.create( value );
//	  ut.commit();
//	  Integer pKey = shJusticeLocal.getShJusticeId();
//	  log.debug("Pkey = " + pKey);
//
//	  ShJustice justice = maintainer.findByPrimaryKey(pKey);
//	  SHJusticeBasicValue justiceValue = maintainer.createBasicVOFromEntity(justice);
//	  log.debug("value object");
//	  log.debug(justiceValue.toString());
//	  justiceValue.setJusticeName("Faisal modified");
//	  log.debug("updating database");
//	  ut.begin();
//	  maintainer.update(justiceValue);
//	  ut.commit();
//	  ShJustice justice2 = (ShJustice)maintainer.findByPrimaryKey(pKey);
//	  log.debug("modified = " + justice2.getJusticeName());
//	  log.debug("value object = "+ justiceValue.getJusticeName());
//
//	  assertEquals(justice2.getJusticeName(), justiceValue.getJusticeName());
//      }
//      catch( Exception e )
//      {
//	 fail( "Caught exception :: in test update" + e );
//      }
//
//  }
//
//  public void testGetShJusticeBasicValue()
//  {
//      try
//      {
//	  log.debug("testGetShJusticeBasicValue");
//	  maintainer = new ShJusticeMaintainer();
//	  SHJusticeBasicValue value = createBasicVO();
//	  ut.begin();
//
//	  ShJustice shJusticeLocal = (ShJustice)maintainer.create( value );
//	  ut.commit();
//
//	  ShJustice shJusticeLocal2 = (ShJustice)maintainer.findByPrimaryKey(shJusticeLocal.getShJusticeId());
//
//	  SHJusticeBasicValue justiceValue = maintainer.getShJusticeBasicValue(shJusticeLocal2);
//	  log.debug("value object");
//	  log.debug(justiceValue.toString());
//
//	  assertEquals(justiceValue.getJusticeName(), shJusticeLocal2.getJusticeName());
//
//      }
//      catch( Exception e )
//      {
//	 fail( "Caught exception :: in vtestGetShJusticeBasicValuee" + e );
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