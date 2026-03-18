//package uk.gov.courtservice.xhibit.test.business.entities.schedhearingdefendant;
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
//import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendeeMaintainer;
//import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendant;
//import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendantMaintainer;
//import uk.gov.courtservice.xhibit.business.vos.entities.SchedHearingDefendantBasicValue;
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
//public class SchedHearingDefendantMaintainerTest extends TestCase{
//
//    private static Logger log = CSServices.getLogger(SchedHearingDefendantMaintainerTest.class);
//
//    public  static String addrSQL= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1)";
//
//    public  static String courtSQL= "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID, CREST_IP_ADDRESS) VALUES ( \n"
//				  +"1, 'court_type', 'circuit', 'name', 'cid', 'cpfix', 'short', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'fez', 'fez', 1, 1, 'addr')";
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
//    public static String defSQL = "INSERT INTO XHB_DEFENDANT (DEFENDANT_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, COURT_ID) VALUES (1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1)";
//    public static String defOnCaseSQL = "INSERT INTO XHB_DEFENDANT_ON_CASE (DEFENDANT_ON_CASE_ID, CASE_ID, DEFENDANT_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (1, 1, 1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
////    public static String schedDefSQL = "INSERT INTO XHB_SCHED_HEARING_DEFENDANT (SCHED_HEAR_DEF_ID, SCHEDULED_HEARING_ID, DEFENDANT_ON_CASE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (1, 1, 1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//    public static String defOnCase2SQL = "INSERT INTO XHB_DEFENDANT_ON_CASE (DEFENDANT_ON_CASE_ID, CASE_ID, DEFENDANT_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (2, 1, 1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//
//   SchedHearingDefendantMaintainer maintainer;
//   SchedHearingAttendeeMaintainer attendeeMaintain = new SchedHearingAttendeeMaintainer();
//   Integer version = new Integer(1);
//
//   private InitialContext initContext;
//   private UserTransaction ut = null;
//
//
//   public SchedHearingDefendantMaintainerTest(String s)
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
//       TestUtils.execSql("Delete from XHB_SCHED_HEARING_DEFENDANT");
//       TestUtils.execSql("Delete from XHB_DEFENDANT_ON_CASE");
//       TestUtils.execSql("Delete from XHB_DEFENDANT");
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
//       TestUtils.execSql(defSQL);
//       TestUtils.execSql(defOnCaseSQL);
//       TestUtils.execSql(defOnCase2SQL);
////       TestUtils.execSql(schedDefSQL);
//
//
//
//    }
//
//    protected void tearDown()throws Exception
//    {
//	log.debug("tearDown()");
//	log.debug("deleting data");
//	log.debug("setUp()");
//	log.debug("deleting data");
//	TestUtils.execSql("Delete from XHB_SCHED_HEARING_DEFENDANT");
//	TestUtils.execSql("Delete from XHB_DEFENDANT_ON_CASE");
//	TestUtils.execSql("Delete from XHB_DEFENDANT");
//	TestUtils.execSql("Delete from XHB_SCHEDULED_HEARING");
//	TestUtils.execSql("Delete from XHB_LINKED_SH");
//	TestUtils.execSql("Delete from XHB_HEARING");
//	TestUtils.execSql("Delete from XHB_REF_HEARING_TYPE");
//	TestUtils.execSql("Delete from XHB_SITTING");
//	TestUtils.execSql("Delete from XHB_CASE");
//	TestUtils.execSql("Delete from XHB_REF_COURT");
//	TestUtils.execSql("Delete from XHB_COURT");
//	TestUtils.execSql("Delete from XHB_ADDRESS");
//   }
//
//    private SchedHearingDefendantBasicValue createBasicVO()
//    {
//	SchedHearingDefendantBasicValue shdValue = new SchedHearingDefendantBasicValue(version);
//	shdValue.setDefendantOnCaseID(version);
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
//	 maintainer = new SchedHearingDefendantMaintainer();
//	 SchedHearingDefendantBasicValue value = createBasicVO();
//	 ut.begin();
//
//	  SchedHearingDefendant shdLocal = (SchedHearingDefendant)maintainer.create( value );
//	  log.debug("version before commit = " + shdLocal.getVersion());
//	 ut.commit();
//	 log.debug("version = " + shdLocal.getVersion());
//	  assertEquals( shdLocal.getDefOnCaseID().intValue(), value.getDefendantOnCaseID().intValue());
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
//	 maintainer = new SchedHearingDefendantMaintainer();
//	 SchedHearingDefendantBasicValue value = createBasicVO();
//	 ut.begin();
//	 SchedHearingDefendant shdLocal = (SchedHearingDefendant)maintainer.create( value );
//	 ut.commit();
//	 log.debug("created entity into db Version = "+shdLocal.getVersion());
//	 Integer pKey = shdLocal.getSchedHearDefId();
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
//	  maintainer = new SchedHearingDefendantMaintainer();
//	  SchedHearingDefendantBasicValue value = createBasicVO();
//	  ut.begin();
//	  SchedHearingDefendant shdLocal = (SchedHearingDefendant)maintainer.create( value );
//	  ut.commit();
//	  Integer pKey = shdLocal.getSchedHearDefId();
//	  log.debug("Pkey = " + pKey);
//
//	  SchedHearingDefendant shd = maintainer.findByPrimaryKey(pKey);
//	  SchedHearingDefendantBasicValue shdValue = maintainer.getSchedHearingDefendantBasicValue(shd);
//	  log.debug("value object");
//	  log.debug(shdValue.toString());
//	  shdValue.setDefendantOnCaseID(new Integer(2));
//	  log.debug("updating database");
//	  ut.begin();
//	  maintainer.update(shdValue);
//	  ut.commit();
//	  SchedHearingDefendant shd2 = (SchedHearingDefendant)maintainer.findByPrimaryKey(pKey);
//	  log.debug("modified = " + shd2.getDefOnCaseID());
//	  log.debug("value object = "+ shdValue.getDefendantOnCaseID());
//
//	  assertEquals(shd2.getDefOnCaseID(), shdValue.getDefendantOnCaseID());
//      }
//      catch( Exception e )
//      {
//	 fail( "Caught exception :: in test update" + e );
//      }
//
//  }
//
//  public void testGetSchedHearingDefendantBasicValue()
//  {
//      try
//      {
//	  log.debug("testGetShJusticeBasicValue");
//	  maintainer = new SchedHearingDefendantMaintainer();
//	  SchedHearingDefendantBasicValue value = createBasicVO();
//	  ut.begin();
//
//	  SchedHearingDefendant shdLocal = (SchedHearingDefendant)maintainer.create( value );
//	  ut.commit();
//
//	  SchedHearingDefendant shdLocal2 = (SchedHearingDefendant)maintainer.findByPrimaryKey(shdLocal.getSchedHearDefId());
//
//	  SchedHearingDefendantBasicValue shdValue = maintainer.getSchedHearingDefendantBasicValue(shdLocal2);
//	  log.debug("value object");
//	  log.debug(shdValue.toString());
//
//	  assertEquals(shdValue.getDefendantOnCaseID().intValue(), shdLocal2.getDefOnCaseID().intValue());
//
//      }
//      catch( Exception e )
//      {
//	 fail( "Caught exception :: in vtestGetSchedHearingDefendantBasicValuee" + e );
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