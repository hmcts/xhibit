//package uk.gov.courtservice.xhibit.test.business.entities.scheduledhearing;
//
//import java.sql.Timestamp;
//import java.util.Collection;
//
//import javax.naming.Context;
//import javax.rmi.PortableRemoteObject;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
//import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingHome;
//
///**
// *
// * <p>Note: This class is not runnable without removing all the contraints,
// * apart from primary key, of the SCHEDULED_HEARING table </p>
// * <p>Title: ScheduledHearingTest </p>
// * <p>Description: To test finder methhods of Scheduled Hearing entity</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Khanh Tran, Faisal Shoukat
// * @version 1.0
// */
//
//
//public class ScheduledHearingTest extends TestCase
//{
//    private Logger log =  CSServices.getLogger(ScheduledHearingTest.class);
//
//    public ScheduledHearingTest(String s)
//    {
//        super(s);
//    }
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
//    public static String schedHearingSQL = "INSERT INTO XHB_SCHEDULED_HEARING (SCHEDULED_HEARING_ID, SEQUENCE_NO, SITTING_ID, HEARING_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, LINKED_SH_ID) VALUES (17, 1, 3, 3, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1)";
//
//
//    protected void setUp() throws Exception
//    {
//        log.debug("setUp()");
//        log.debug("deleting data");
//        //TestUtils.execSql("Delete from XHB_SCHEDULED_HEARING");
//
//	log.debug("setUp()");
//	log.debug("deleting data");
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
//
//    }
//
//    protected void tearDown()throws Exception
//    {
//        log.debug("tearDown()");
//        log.debug("deleting data");
//	log.debug("tearDown()");
//	log.debug("deleting data");
//	TestUtils.execSql("Delete from XHB_SCHEDULED_HEARING");
//	TestUtils.execSql("Delete from XHB_LINKED_SH");
//	TestUtils.execSql("Delete from XHB_HEARING");
//	TestUtils.execSql("Delete from XHB_REF_HEARING_TYPE");
//	TestUtils.execSql("Delete from XHB_SITTING");
//	TestUtils.execSql("Delete from XHB_CASE");
//	TestUtils.execSql("Delete from XHB_REF_COURT");
//	TestUtils.execSql("Delete from XHB_COURT");
//        TestUtils.execSql("Delete from XHB_ADDRESS");
//    }
//
//    public void testFindByHearingId()
//   {
//       try
//       {
//	   log.debug("testFindByHearingId() - Insert ScheduledHearing");
//	   String s = "INSERT INTO XHB_SCHEDULED_HEARING (HEARING_ID, SCHEDULED_HEARING_ID) VALUES (1, ";
//	   int noOfHearings = 10;
//	   for(int i = 0; i < noOfHearings; i++)
//	   {
//	       s.concat((i+1) + ")");
//	       TestUtils.execSql(s.concat((i+1) + ")"));
//	   }
//
//	   ScheduledHearingHome home = lookupHome();
//	   log.debug("findByHearingId() - Got ScheduledHearingHome");
//
//	   log.debug("Hearing ID = 1");
//	   Collection locals = home.findByHearingId(new Integer(1));
//	   assertEquals(10, locals.size());
//
//	   log.debug("Sitting ID = 2");
//	   locals = home.findByHearingId(new Integer(2));
//	   assertEquals(0, locals.size());
//       }
//       catch(Exception e)
//       {
//	   log.debug("testFindByHearingId() is failed");
//	   e.printStackTrace();
//	   fail();
//       }
//   }
//
//
//
//    public void testFindBySittingId()
//    {
//        try
//        {
//            log.debug("testFindBySittingId() - Insert ScheduledHearing");
//            String s = "INSERT INTO XHB_SCHEDULED_HEARING (SITTING_ID, SCHEDULED_HEARING_ID) VALUES (1, ";
//            int noOfSittings = 10;
//            for(int i = 0; i < noOfSittings; i++)
//            {
//                s.concat((i+1) + ")");
//                TestUtils.execSql(s.concat((i+1) + ")"));
//            }
//
//            ScheduledHearingHome home = lookupHome();
//            log.debug("findBySittingId() - Got ScheduledHearingHome");
//
//            log.debug("Sitting ID = 1");
//            Collection locals = home.findBySittingId(new Integer(1));
//            assertEquals(10, locals.size());
//
//            log.debug("Sitting ID = 2");
//            locals = home.findBySittingId(new Integer(2));
//            assertEquals(0, locals.size());
//        }
//        catch(Exception e)
//        {
//            log.debug("testFindBySittingId() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//    public void testFindByTimeSittingId()
//    {
//        try
//        {
//            log.debug("testFindByTimeSittingId() - Insert ScheduledHearing");
//            String s = "INSERT INTO XHB_SCHEDULED_HEARING (SITTING_ID, NOT_BEFORE_TIME, SCHEDULED_HEARING_ID) VALUES (1, TO_Date( '11/11/2002 12:00:00', 'MM/DD/YYYY HH24:MI:SS'), ";
//            int noOfSittings = 10;
//            for(int i = 0; i < noOfSittings; i++)
//            {
//                s.concat((i+1) + ")");
//                TestUtils.execSql(s.concat((i+1) + ")"));
//            }
//
//            Timestamp time;
//            ScheduledHearingHome home = lookupHome();
//            log.debug("testFindByTimeSittingId() - Got ScheduledHearingHome");
//
//            log.debug("Time = 2002-11-11 12:00:00; Sitting ID = 1");
//            time = Timestamp.valueOf("2002-11-11 12:00:00");
//            Collection locals = home.findByTimeSittingId(time, new Integer(1));
//            assertEquals(10, locals.size());
//
//            log.debug("Time = 2002-11-11 13:00:00; Sitting ID = 1");
//            time = Timestamp.valueOf("2002-11-11 13:00:00");
//            locals = home.findByTimeSittingId(time, new Integer(1));
//            assertEquals(10, locals.size());
//
//            log.debug("Time = 2002-11-11 11:00:00; Sitting ID = 1");
//            time = Timestamp.valueOf("2002-11-11 11:00:00");
//            locals = home.findByTimeSittingId(time, new Integer(1));
//            assertEquals(0, locals.size());
//
//            log.debug("Time = 2002-11-11 12:00:00; Sitting ID = 2");
//            time = Timestamp.valueOf("2002-11-11 12:00:00");
//            locals = home.findByTimeSittingId(time, new Integer(2));
//            assertEquals(0, locals.size());
//        }
//        catch(Exception e)
//        {
//            log.debug("testFindByTimeSittingId() is failed");
//            e.printStackTrace();
//            fail();
//        }
//
//    }
//
//    public void testFindByPrimaryKey()
//    {
//	try
//	{
//	    ScheduledHearingHome home = lookupHome();
//	    log.debug("testFindByPrimaryKey() - Got ScheduledHEaringHomeHome");
//	    ScheduledHearing local = home.findByPrimaryKey(new Integer(1));
//		//log.debug("repID : " + local.getDefendantOnCase());
//	    assertEquals(1, local.getScheduledHearingId().intValue());
//	}
//	catch(Exception e)
//	{
//	    log.debug("findByPrimaryKey() is failed");
//	    e.printStackTrace();
//	    fail();
//	}
//    }
//
//
//    private ScheduledHearingHome lookupHome() throws Exception
//    {
//        Context ctx = CSServices.getServiceLocator().getInitialContext();
//        Object home = (ScheduledHearingHome) ctx.lookup("ScheduledHearingHome");
//        return (ScheduledHearingHome) PortableRemoteObject.narrow(home, ScheduledHearingHome.class);
//    }
//}