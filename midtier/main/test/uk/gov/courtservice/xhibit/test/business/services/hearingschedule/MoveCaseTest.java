//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule;
//
//import java.sql.Timestamp;
//import java.util.Calendar;
//import java.util.Collection;
//import java.util.GregorianCalendar;
//import java.util.Iterator;
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
//import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
//import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
//import uk.gov.courtservice.xhibit.business.entities.sitting.Sitting;
//import uk.gov.courtservice.xhibit.business.entities.sitting.SittingMaintainer;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleController;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerHome;
//import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.schedule.MoveCaseValue;
//
//public class MoveCaseTest extends TestCase
//{
//
//    private static Logger log = CSServices.getLogger(MoveCaseTest.class);
//    private HearingScheduleController hearing;
//    GregorianCalendar gc = new GregorianCalendar(2002, Calendar.FEBRUARY, 8);
//    java.util.Date date = gc.getTime();
//    private int whichTest;
//
//
//    private ScheduledHearingMaintainer schedMaintainer = new ScheduledHearingMaintainer();
//    private SchedHearingAttendeeMaintainer attendeeMaintainer = new SchedHearingAttendeeMaintainer();
//    private SittingMaintainer sittingMaintainer = new SittingMaintainer();
//
//    public  static String addrSQL= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1)";
//
//   public  static String courtSQL= "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID, CREST_IP_ADDRESS) VALUES ( \n"
//   +"1, 'court_type', 'circuit', 'name', 'cid', 'cpfix', 'short', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'fez', 'fez', 1, 1, 'addr')";
//
//    public String sittingSQL = "INSERT INTO XHB_SITTING(SITTING_ID, SITTING_SEQUENCE_NO, IS_FLOATING, REF_JUDGE_ID, COURT_ROOM_ID, COURT_SITE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (1000, 1, 'N', 1, 1, 1, TO_Date( '11/15/2002 03:38:02', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//    public String siiting2SQL = "INSERT INTO XHB_SITTING(SITTING_ID, SITTING_SEQUENCE_NO, SITTING_TIME,  IS_FLOATING, REF_JUDGE_ID, COURT_ROOM_ID, COURT_SITE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (2000, 2, TO_Date( '02/02/2003 10:00:00', 'MM/DD/YYYY HH:MI:SS AM'), 'N', 2, 2, 1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//    public String siting3SQL = "INSERT INTO XHB_SITTING(SITTING_ID, SITTING_SEQUENCE_NO, IS_FLOATING, REF_JUDGE_ID, COURT_ROOM_ID, COURT_SITE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (3000, 3, 'N', 2, 2, 1, TO_Date( '11/15/2002 03:38:02', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//
//    public String courtSiteSQL = "INSERT INTO XHB_COURT_SITE(COURT_SITE_ID) VALUES (1)";
//    public String courtRoomSQL = "INSERT INTO XHB_COURT_ROOM(COURT_ROOM_ID, COURT_ROOM_NAME, COURT_SITE_ID) VALUES (1, 'courtRoom 1', 1)";
//    public String courtRoom2SQL = "INSERT INTO XHB_COURT_ROOM(COURT_ROOM_ID, COURT_ROOM_NAME, COURT_SITE_ID) VALUES (2, 'courtRoom 2', 1)";
//
//    public static String schedHearingSQL = "INSERT INTO XHB_SCHEDULED_HEARING (SCHEDULED_HEARING_ID, SEQUENCE_NO, SITTING_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, LINKED_SH_ID) VALUES (1, 1, 1000,  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1)";
//    public static String schedHearing2SQL = "INSERT INTO XHB_SCHEDULED_HEARING (SCHEDULED_HEARING_ID, SEQUENCE_NO, SITTING_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, LINKED_SH_ID) VALUES (2, 1, 3000,  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1)";
//    public static String schedHearing3SQL = "INSERT INTO XHB_SCHEDULED_HEARING (SCHEDULED_HEARING_ID, SEQUENCE_NO, SITTING_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (3, 1, 3000,  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//    public static String schedHearing4SQL = "INSERT INTO XHB_SCHEDULED_HEARING (SCHEDULED_HEARING_ID, SEQUENCE_NO, SITTING_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (4, 1, 3000,  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//    public static String schedHearing5SQL = "INSERT INTO XHB_SCHEDULED_HEARING (SCHEDULED_HEARING_ID, SEQUENCE_NO, SITTING_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (5, 3, 3000,  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//    public static String schedHearing6SQL = "INSERT INTO XHB_SCHEDULED_HEARING (SCHEDULED_HEARING_ID, SEQUENCE_NO, SITTING_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (6, 7, 3000,  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//
//    public String refJudgeSQL = "INSERT INTO XHB_REF_JUDGE (REF_JUDGE_ID) VALUES (1)";
//    public String refJudge2SQL = "INSERT INTO XHB_REF_JUDGE (REF_JUDGE_ID) VALUES (2)";
//
//    public String refCourtRepSQL = "INSERT INTO XHB_REF_COURT_REPORTER(REF_COURT_REPORTER_ID) Values (1)";
//    public String staffSQL = "INSERT INTO XHB_SH_STAFF(SH_STAFF_ID) Values (1)";
//
//    public String hearingListSQL = "INSERT INTO XHB_HEARING_LIST(LIST_ID, START_DATE, COURT_ID) VALUES (1, TO_Date( '02/02/2003', 'MM/DD/YYYY'), 1)";
//
//    public static String linkedSQL = "INSERT INTO XHB_LINKED_SH(LINKED_SH_ID) Values(1)";
//
//    InitialContext initContext = null;
//    UserTransaction ut = null;
//
//  public MoveCaseTest(String s)
//  {
//    super(s);
//  }
//
//  protected void setUp()
//  {
//
//      try
//      {
//	  initContext = new InitialContext();
//	  ut = (UserTransaction)initContext.lookup("java:comp/UserTransaction");
//
//	  log.debug("Sitting date = " + date);
//	  log.debug("getting the remote");
//	  hearing = (HearingScheduleController)CSServices.getEJBServices().
//	     createRemoteSession(HearingScheduleControllerHome.class);
//	  log.debug("got handle to remote");
//
//	  date = Timestamp.valueOf("2003-02-02 11:00:00");
//
//
//      }
//      catch(Exception e)
//      {
//	  log.debug("caught Exception in startup " + e);
//      }
//
//
//  }
//
//  protected void tearDown()
//  {
//  }
//
//  public void testMoveCase()
//  {
//      whichTest = 1;
//     MoveCaseValue caseValue = populateMoveCaseValue();
//
//
//    try
//    {
//	log.debug("deleting data");
//	TestUtils.execSql("Delete from XHB_REF_COURT_REPORTER");
//	TestUtils.execSql("Delete from XHB_SH_STAFF");
//	TestUtils.execSql("Delete from XHB_LINKED_SH");
//	TestUtils.execSql("Delete from XHB_HEARING_LIST");
//	TestUtils.execSql("Delete from XHB_SCHED_HEARING_ATTENDEE");
//	TestUtils.execSql("Delete from XHB_SCHEDULED_HEARING");
//	TestUtils.execSql("Delete from XHB_LINKED_SH");
//	TestUtils.execSql("Delete from XHB_HEARING");
//	TestUtils.execSql("Delete from XHB_REF_JUDGE");
//	TestUtils.execSql("Delete from XHB_SITTING");
//	TestUtils.execSql("Delete from XHB_COURT_ROOM");
//	TestUtils.execSql("Delete from XHB_COURT_SITE");
//	TestUtils.execSql("Delete from XHB_COURT");
//	TestUtils.execSql("Delete from XHB_ADDRESS");
//
//	TestUtils.execSql(addrSQL);
//	TestUtils.execSql(courtSQL);
//	TestUtils.execSql(courtSiteSQL);
//	TestUtils.execSql(courtRoomSQL);
//	TestUtils.execSql(courtRoom2SQL);
//	TestUtils.execSql(refJudgeSQL);
//	TestUtils.execSql(refJudge2SQL);
//	TestUtils.execSql(sittingSQL);
//	TestUtils.execSql(siiting2SQL);
//	TestUtils.execSql(siting3SQL);
//	TestUtils.execSql(linkedSQL);
//	TestUtils.execSql(schedHearingSQL);
//	TestUtils.execSql(schedHearing2SQL);
//	TestUtils.execSql(schedHearing3SQL);
//	TestUtils.execSql(schedHearing4SQL);
//	TestUtils.execSql(schedHearing5SQL);
//	TestUtils.execSql(staffSQL);
//	TestUtils.execSql(refCourtRepSQL);
//	TestUtils.execSql(hearingListSQL);
//
////	ut.begin();
//	log.debug("calling move case");
//	hearing.moveCase(caseValue);
//	log.debug("method called checking updates");
//
//	ScheduledHearing schedLocal = schedMaintainer.findByPK(new Integer(1));
//	String movedFrom = schedLocal.getMovedFrom();
//	log.debug("moved from = " + movedFrom);
//	Sitting sitting = schedLocal.getSitting();
//	Integer sittingID = sitting.getSittingId();
//	log.debug("SittingId = "+ sittingID);
//
//	log.debug("Not before Time = " + schedLocal.getNotBeforeTime().toString());
//	log.debug("Not before time in value object = "+ caseValue.getNewHearingTime().toString());
//
////if you are creating siiting comment this out
//	assertEquals(sittingID.intValue(), 2000);
//	assertEquals(movedFrom, "courtRoom 1");
//
//	for(int i = 1; i<4; i++)
//	{
//	    Collection coll = attendeeMaintainer.findByScheduledHearingId(new Integer(i));
//	    Iterator itr = coll.iterator();
//
//	    while(itr.hasNext())
//	    {
//		SchedHearingAttendee attendee = (SchedHearingAttendee)itr.next();
//		ScheduledHearing sched = attendee.getScheduledHearing();
//		Integer schId = sched.getScheduledHearingId();
//		String type = attendee.getAttendeeType();
//		Integer judId = attendee.getRefJudgeId();
//
//		log.debug("Values in attendee = schId = " + schId + "type = " + type+ " jud ID = " + judId);
//
//		assertEquals(schId.intValue(), caseValue.getScheduledHearingId().intValue());
//		if(attendee.getAttendeeType().equals("J"))
//                assertEquals(judId.intValue(), caseValue.getExistingJudgeId().intValue());
//
//	    }
//	}
//
//	//check on linked cases
//	Collection schedColl = schedMaintainer.findByLinkedSchedHearingId(caseValue.getLinkedSHId());
//	Iterator itr1 = schedColl.iterator();
//
//	while(itr1.hasNext())
//	{
//	    ScheduledHearing shear = (ScheduledHearing)itr1.next();
//	    assertEquals(shear.getMovedFrom(), "courtRoom 1");
//	}
//
//
//
//
////
////	ut.commit();
//
////	log.debug("got attendee reusltd");
////	ResultSet results = TestUtils.execSql("Select * from XHB_SCHED_HEARING_ATTENDEE");
////	if(results.next())
////	{
////	    System.out.print("Results = " + results.getInt(1) + ", " + results.getString(2)
////			     + ", " + results.getInt(3)+ ", " + results.getInt(8));
////	}
////
////	log.debug("got scheduled hearing results");
////	ResultSet results2 = TestUtils.execSql("Select * from XHB_SCHEDULED_HEARING where SCHEDULED_HEARING_ID = 1");
////	if(results.next())
////	{
////	    System.out.println("Results of SchedHearing = " + results.getInt(1) + ", " + results.getInt(2)
////			       +", " + results.getInt(7) + ", "+ results.getString(9));
////	}
//    }
//    catch(Exception e)
//    {
//	log.debug("caught exception when move case is called" + e);
//    }
//    finally
//    {
//	try
//	{
//	    log.debug("deleting data for a second ime");
////	    TestUtils.execSql("Delete from XHB_REF_COURT_REPORTER");
////	    TestUtils.execSql("Delete from XHB_SH_STAFF");
////	    TestUtils.execSql("Delete from XHB_SCHED_HEARING_ATTENDEE");
////	    TestUtils.execSql("Delete from XHB_SCHEDULED_HEARING");
////	    TestUtils.execSql("Delete from XHB_HEARING_LIST");
////	    TestUtils.execSql("Delete from XHB_LINKED_SH");
////	    TestUtils.execSql("Delete from XHB_HEARING");
////	    TestUtils.execSql("Delete from XHB_REF_JUDGE");
////	    TestUtils.execSql("Delete from XHB_SITTING");
////	    TestUtils.execSql("Delete from XHB_COURT_ROOM");
////	    TestUtils.execSql("Delete from XHB_COURT_SITE");
////	    TestUtils.execSql("Delete from XHB_COURT");
////	    TestUtils.execSql("Delete from XHB_ADDRESS");
//
//	}
//	catch(Exception e)
//	{
//	    log.debug("caught exception when move case is called in finally block" + e);
//	}
//
//    }
//
//  }
//
////  public void testMoveCaseJudgeNew()
////  {
////      whichTest = 2;
////     MoveCaseValue caseValue = populateMoveCaseValue();
////
////
////    try
////    {
////	log.debug("deleting data");
////	TestUtils.execSql("Delete from XHB_SCHED_HEARING_ATTENDEE");
////	TestUtils.execSql("Delete from XHB_SCHEDULED_HEARING");
////	TestUtils.execSql("Delete from XHB_LINKED_SH");
////	TestUtils.execSql("Delete from XHB_HEARING");
////	TestUtils.execSql("Delete from XHB_REF_JUDGE");
////	TestUtils.execSql("Delete from XHB_SITTING");
////	TestUtils.execSql("Delete from XHB_COURT_ROOM");
////	TestUtils.execSql("Delete from XHB_COURT_SITE");
////	TestUtils.execSql("Delete from XHB_COURT");
////	TestUtils.execSql("Delete from XHB_ADDRESS");
////
////	TestUtils.execSql(addrSQL);
////	TestUtils.execSql(courtSQL);
////	TestUtils.execSql(courtSiteSQL);
////	TestUtils.execSql(courtRoomSQL);
////	TestUtils.execSql(courtRoom2SQL);
////	TestUtils.execSql(sittingSQL);
////	TestUtils.execSql(siiting2SQL);
////	TestUtils.execSql(siting3SQL);
////	TestUtils.execSql(schedHearingSQL);
////	TestUtils.execSql(schedHearing2SQL);
////	TestUtils.execSql(schedHearing3SQL);
////	TestUtils.execSql(schedHearing4SQL);
////	TestUtils.execSql(schedHearing5SQL);
////
////	log.debug("calling move case");
////	hearing.moveCase(caseValue);
////	log.debug("method called checking updates");
////
////	log.debug("got attendee reusltd");
////	ResultSet results = TestUtils.execSql("Select * from XHB_SCHED_HEARING_ATTENDEE");
////	if(results.next())
////	{
////	    System.out.print("Results = " + results.getInt(1) + ", " + results.getString(2)
////			     + ", " + results.getInt(3)+ ", " + results.getInt(8));
////	}
////
////	log.debug("got scheduled hearing results");
////	ResultSet results2 = TestUtils.execSql("Select * from XHB_SCHEDULED_HEARING where SCHEDULED_HEARING_ID = 1");
////	if(results.next())
////	{
////	    System.out.println("Results of SchedHearing = " + results.getInt(1) + ", " + results.getInt(2)
////			       +", " + results.getInt(7) + ", "+ results.getString(9));
////	}
////    }
////    catch(Exception e)
////    {
////	log.debug("caught exception when move case is called" + e);
////    }
////    finally
////    {
////	try
////	{
////	    log.debug("deleting data for a second ime");
////	    TestUtils.execSql("Delete from XHB_SCHED_HEARING_ATTENDEE");
////	    TestUtils.execSql("Delete from XHB_SCHEDULED_HEARING");
////	    TestUtils.execSql("Delete from XHB_LINKED_SH");
////	    TestUtils.execSql("Delete from XHB_HEARING");
////	    TestUtils.execSql("Delete from XHB_REF_JUDGE");
////	    TestUtils.execSql("Delete from XHB_SITTING");
////	    TestUtils.execSql("Delete from XHB_COURT_ROOM");
////	    TestUtils.execSql("Delete from XHB_COURT_SITE");
////	    TestUtils.execSql("Delete from XHB_COURT");
////	    TestUtils.execSql("Delete from XHB_ADDRESS");
////
////	}
////	catch(Exception e)
////	{
////	    log.debug("caught exception when move case is called in finally block" + e);
////	}
////
////    }
////
////  }
//
//  private MoveCaseValue populateMoveCaseValue()
//  {
//      MoveCaseValue value = new MoveCaseValue();
//      value.setAdjourned(false);
//      value.setCourtId(new Integer(1));
//      value.setExistingJudgeId(new Integer(1));
//      value.setNewCourtRoomId(new Integer(2));
//      value.setNewHearingTime(date);
//      value.setOldCourtRoomId(new Integer(1));
//      value.setScheduledHearingId(new Integer(1));
//      value.setExisitingSHWriterId(new Integer(1));
//      value.setExistingCourtClerkId(new Integer(1));
//      value.setUseExistingCourtClerk(true);
//      value.setUseExistingSHWriter(true);
//      value.setLinkedSHId(new Integer(1));
//
//      if(whichTest == 1)
//      {
//	  value.setUseExistingJudge(true);
//      }
//      if(whichTest == 2)
//      {
//	  value.setUseExistingJudge(false);
//      }
//
//
//      return value;
//
//  }
//
//}