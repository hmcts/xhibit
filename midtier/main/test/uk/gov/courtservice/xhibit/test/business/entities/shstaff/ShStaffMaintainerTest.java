//package uk.gov.courtservice.xhibit.test.business.entities.shstaff;
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
//import uk.gov.courtservice.xhibit.business.entities.shstaff.ShStaff;
//import uk.gov.courtservice.xhibit.business.entities.shstaff.ShStaffMaintainer;
//import uk.gov.courtservice.xhibit.business.vos.entities.SHStaffBasicValue;
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
//public class ShStaffMaintainerTest extends TestCase{
//
//    private static Logger log = CSServices.getLogger(ShStaffMaintainerTest.class);
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
//  //  public static String shStaffSQl = "INSERT INTO XHB_SH_STAFF (SH_STAFF_ID, STAFF_ROLE, STAFF_NAME, VERSION, LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) VALUES (1, 'u', 'fez', 1, 'pete', 'pete', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//
//   ShStaffMaintainer maintainer;
//   SchedHearingAttendeeMaintainer attendeeMaintain = new SchedHearingAttendeeMaintainer();
//   Integer version = new Integer(1);
//
//   private InitialContext initContext;
//   private UserTransaction ut = null;
//
//
//   public ShStaffMaintainerTest(String s)
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
//
//       TestUtils.execSql("Delete from XHB_SH_STAFF");
//       TestUtils.execSql("Delete from XHB_SCHED_HEARING_ATTENDEE");
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
//       TestUtils.execSql(schedHearingSQL);
//       TestUtils.execSql(schedAttendeeSQL);
//
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
//	TestUtils.execSql("Delete from XHB_SH_STAFF");
//       TestUtils.execSql("Delete from XHB_SCHED_HEARING_ATTENDEE");
//       TestUtils.execSql("Delete from XHB_SCHEDULED_HEARING");
//       TestUtils.execSql("Delete from XHB_LINKED_SH");
//       TestUtils.execSql("Delete from XHB_HEARING");
//       TestUtils.execSql("Delete from XHB_REF_HEARING_TYPE");
//       TestUtils.execSql("Delete from XHB_SITTING");
//       TestUtils.execSql("Delete from XHB_CASE");
//       TestUtils.execSql("Delete from XHB_REF_COURT");
//       TestUtils.execSql("Delete from XHB_COURT");
//       TestUtils.execSql("Delete from XHB_ADDRESS");
//    }
//
//    private SHStaffBasicValue createBasicVO()
//    {
//	SHStaffBasicValue staffValue = new SHStaffBasicValue(version);
//	staffValue.setStaffName("Faisal");
//	staffValue.setStaffRole("XP");
//
//	return staffValue;
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
//	 maintainer = new ShStaffMaintainer();
//	 SHStaffBasicValue value = createBasicVO();
//	 ut.begin();
//
//	  ShStaff shStaffLocal = (ShStaff)maintainer.create( value );
//	  log.debug("version before commit = " + shStaffLocal.getVersion());
//	 ut.commit();
//	 log.debug("version = " + shStaffLocal.getVersion());
//	  assertEquals( shStaffLocal.getStaffName(), value.getStaffName());
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
//	 maintainer = new ShStaffMaintainer();
//	 SHStaffBasicValue value = createBasicVO();
//	 ut.begin();
//	 ShStaff shStaffLocal = (ShStaff)maintainer.create( value );
//	 ut.commit();
//	 log.debug("created entity into db Version = "+shStaffLocal.getVersion());
//	 Integer pKey = shStaffLocal.getShStaffId();
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
//	  maintainer = new ShStaffMaintainer();
//	  SHStaffBasicValue value = createBasicVO();
//	  ut.begin();
//	  ShStaff shStaffLocal = (ShStaff)maintainer.create( value );
//	  ut.commit();
//	  Integer pKey = shStaffLocal.getShStaffId();
//	  log.debug("Pkey = " + pKey);
//
//	  ShStaff staff = maintainer.findByPrimaryKey(pKey);
//	  SHStaffBasicValue staffValue = maintainer.createBasicVOFromEntity(staff);
//	  log.debug("value object");
//	  log.debug(staffValue.toString());
//	  staffValue.setStaffName("SD");
//	  log.debug("updating database");
//	  ut.begin();
//	  maintainer.update(staffValue);
//	  ut.commit();
//	  ShStaff staff2 = (ShStaff)maintainer.findByPrimaryKey(pKey);
//	  log.debug("modified = " + staff2.getStaffName());
//	  log.debug("value object = "+ staffValue.getStaffName());
//
//	  assertEquals(staff2.getStaffName(), staffValue.getStaffName());
//      }
//      catch( Exception e )
//      {
//	 fail( "Caught exception :: in test update" + e );
//      }
//
//  }
//
//  public void testGetShStaffBasicValue()
//  {
//      try
//      {
//	  log.debug("testGetShStaffBasicValue");
//	  maintainer = new ShStaffMaintainer();
//	  SHStaffBasicValue value = createBasicVO();
//	  ut.begin();
//
//	  ShStaff shStaffLocal = (ShStaff)maintainer.create( value );
//	  ut.commit();
//
//	  ShStaff shStaffLocal2 = (ShStaff)maintainer.findByPrimaryKey(shStaffLocal.getShStaffId());
//
//	  SHStaffBasicValue staffValue = maintainer.getShStaffBasicValue(shStaffLocal2);
//	  log.debug("value object");
//	  log.debug(staffValue.toString());
//
//	  assertEquals(staffValue.getStaffName(), shStaffLocal2.getStaffName());
//
//      }
//      catch( Exception e )
//      {
//	 fail( "Caught exception :: in vtestGetShStaffBasicValuee" + e );
//      }
//
//
//  }
//
///*  public void testGetShStaffComplexValue()
//  {
//      log.debug("testGetShStaffComplexValue");
//      maintainer = new ShStaffMaintainer();
//      SHStaffBasicValue value = createBasicVO();
////          ut.begin();
//
//      ShStaff shStaffLocal = (ShStaff)maintainer.create( value );
//      ShStaff shStaffLocal2 = (ShStaff)maintainer.findByPrimaryKey(shStaffLocal.getShStaffId());
//
//      SHStaffComplexValue staffComplex = maintainer.getShStaffComplexValue(shStaffLocal2);
//      log.debug("value object");
//      log.debug(staffComplex.toString());
//
//      SchedHearingAttendee attendee =(SchedHearingAttendee)staffComplex.getSchedHearingAttendee();
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